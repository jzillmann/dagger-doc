package io.morethan.daggerdoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.morethan.daggerdoc.Util.ProvidesParamEvaluator;
import io.morethan.daggerdoc.model.DependencyGraph;
import io.morethan.daggerdoc.model.NodeType;

/**
 * Main annotation processor that generates a {@link DependencyGraph} out of the processed sources. Implementations of
 * {@link ResultWriter} are then responsible for converting the {@link DependencyGraph} to anything meaningful!
 */
@AutoService(Processor.class)
public class DaggerDocProcessor extends AbstractProcessor {

    private static final String WRITERS_OPTION = "writers";

    private final DependencyGraph.Builder _graphBuilder = DependencyGraph.builder();
    private List<ResultWriter> _resultWriters;

    public DaggerDocProcessor() {
        // default constructor called by regular compilation
    }

    @VisibleForTesting
    public DaggerDocProcessor(ResultWriter resultWriter) {
        _resultWriters = new ArrayList<>();
        _resultWriters.add(resultWriter);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add("dagger.Component");
        annotations.add("dagger.Module");
        return annotations;
    }

    @Override
    public Set<String> getSupportedOptions() {
        Verify.verifyNotNull(_resultWriters);
        ImmutableSet.Builder<String> supportedOptions = ImmutableSet.builder();
        supportedOptions.add(WRITERS_OPTION);
        for (ResultWriter resultWriter : _resultWriters) {
            supportedOptions.addAll(resultWriter.supportedOptions());
        }
        return supportedOptions.build();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        if (_resultWriters == null) {
            _resultWriters = loadResultWriters(processingEnv.getOptions(), processingEnv.getMessager());
        }
    }

    private static List<ResultWriter> loadResultWriters(Map<String, String> options, Messager messager) {
        List<ResultWriter> resultWriters = ImmutableList.copyOf(ServiceLoader.load(ResultWriter.class, DaggerDocProcessor.class.getClassLoader()));
        Preconditions.checkState(resultWriters.size() > 0, "No instances of '%s' found!", ResultWriter.class.getName());

        String writersOption = options.get(WRITERS_OPTION);
        if (writersOption != null) {
            Set<String> pickedWriters = ImmutableSet.copyOf(Splitter.on(',').omitEmptyStrings().split(writersOption));
            if (pickedWriters.size() > 0) {
                resultWriters = resultWriters.stream().filter(writer -> pickedWriters.contains(writer.id())).collect(Collectors.toList());
            }
        }
        messager.printMessage(Kind.NOTE, "Activated result writers (writers=" + writersOption + "):", null);
        for (ResultWriter resultWriter : resultWriters) {
            messager.printMessage(Kind.NOTE, "\t" + resultWriter.id() + "\t- " + resultWriter.getClass().getName(), null);
        }
        return resultWriters;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // Inspect components
        for (Element componentClass : roundEnv.getElementsAnnotatedWith(Component.class)) {
            _graphBuilder.addNode(componentClass, NodeType.COMPONENT, Optional.empty());
            Util.consumeAnnotationValues(componentClass, Component.class, "modules", entry -> {
                Util.consumeClassValues(entry.getValue(), typeMirror -> {
                    _graphBuilder.addDependency(componentClass, typeMirror);
                });
            });
        }

        // Inspect modules
        for (Element moduleClass : roundEnv.getElementsAnnotatedWith(Module.class)) {
            Optional<String> moduleCategory = Optional.empty();
            ModuleDoc annotation = moduleClass.getAnnotation(ModuleDoc.class);
            if (annotation != null && !Strings.isNullOrEmpty(annotation.category())) {
                moduleCategory = Optional.of(annotation.category());
            }
            _graphBuilder.addNode(moduleClass, NodeType.MODULE, moduleCategory);
            Util.consumeAnnotationValues(moduleClass, Module.class, "includes", entry -> {
                Util.consumeClassValues(entry.getValue(), typeMirror -> {
                    _graphBuilder.addDependency(moduleClass, typeMirror);
                });
            });

            // Inspect the @Provides methods of the module
            moduleClass.getEnclosedElements().stream()
                    .filter(elem -> elem.getKind() == ElementKind.METHOD && elem.getAnnotation(Provides.class) != null)
                    .map(method -> (ExecutableElement) method)
                    .forEach(providesMethod -> {
                        // Multi-binding contributors
                        if (providesMethod.getAnnotation(IntoSet.class) != null) {
                            TypeMirror returnType = providesMethod.getReturnType();
                            _graphBuilder.addMultibindingLink(moduleClass, returnType);
                        }

                        // Multi-binding consumers
                        ProvidesParamEvaluator typeVisitor = new ProvidesParamEvaluator(processingEnv, _graphBuilder, moduleClass);
                        for (Element methodParam : providesMethod.getParameters()) {
                            methodParam.asType().accept(typeVisitor, null);
                        }
                    });
        }

        if (roundEnv.processingOver()) {
            for (ResultWriter resultWriter : _resultWriters) {
                resultWriter.write(processingEnv, _graphBuilder.build());
            }
        }
        return false;
    }

}
