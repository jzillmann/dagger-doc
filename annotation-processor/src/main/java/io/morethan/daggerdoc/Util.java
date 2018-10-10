package io.morethan.daggerdoc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.AbstractTypeVisitor8;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;

import dagger.Provides;
import io.morethan.daggerdoc.model.DependencyGraph;

/**
 * Helper for navigating the {@link Element} tree with its annotations and annotation values.
 */
class Util {

    public static void consumeAnnotationValues(
            Element element,
            Class<?> annotationClass,
            String annotationFieldName,
            Consumer<Entry<? extends ExecutableElement, ? extends AnnotationValue>> consumer) {

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotationClass.getName())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
                    if (entry.getKey().getSimpleName().contentEquals(annotationFieldName)) {
                        consumer.accept(entry);
                    }
                }
            }
        }
    }

    public static void consumeClassValues(AnnotationValue annotationValue, Consumer<TypeMirror> typeConsumer) {
        new ClassValueAnnotationVisitor(typeConsumer).visit(annotationValue);
    }

    /**
     * Getting values of type {@link Class} from an annotation is not straightforward. See
     * http://hauchee.blogspot.com/2015/12/compile-time-annotation-processing-getting-class-value.html.
     * 
     * This {@link AnnotationValueVisitor} helps to simplify the retrieval.
     */
    private static class ClassValueAnnotationVisitor extends SimpleAnnotationValueVisitor8<Void, Void> {

        private final Consumer<TypeMirror> _typeConsumer;

        private ClassValueAnnotationVisitor(Consumer<TypeMirror> typeConsumer) {
            _typeConsumer = typeConsumer;
        }

        @Override
        public Void visitArray(List<? extends AnnotationValue> vals, Void p) {
            for (AnnotationValue annotationValue : vals) {
                visit(annotationValue);
            }
            return super.visitArray(vals, p);
        }

        @Override
        public Void visitType(TypeMirror t, Void p) {
            _typeConsumer.accept(t);
            return super.visitType(t, p);
        }

    }

    /**
     * Is applied to the parameters of a {@link Provides} method for information extraction.
     */
    static class ProvidesParamEvaluator extends AbstractTypeVisitor8<Void, Void> {

        private final ProcessingEnvironment _processingEnv;
        private final DependencyGraph.Builder _graphBuilder;
        private final Element _containerElement;
        private final TypeMirror _typeOfSet;

        public ProvidesParamEvaluator(ProcessingEnvironment processingEnv, DependencyGraph.Builder graphBuilder, Element containerElement) {
            _processingEnv = processingEnv;
            _graphBuilder = graphBuilder;
            _containerElement = containerElement;
            _typeOfSet = _processingEnv.getElementUtils().getTypeElement("java.util.Set").asType();
        }

        @Override
        public Void visitPrimitive(PrimitiveType t, Void p) {
            return null;
        }

        @Override
        public Void visitNull(NullType t, Void p) {
            return null;
        }

        @Override
        public Void visitArray(ArrayType t, Void p) {
            return null;
        }

        @Override
        public Void visitDeclared(DeclaredType t, Void p) {

            // Detect multi-binding consumers
            if (_processingEnv.getTypeUtils().isSubtype(t.asElement().asType(), _typeOfSet)) {
                TypeMirror typeMirrorOfSet = t.getTypeArguments().get(0);
                _graphBuilder.addMultibindingConsumer(_containerElement, typeMirrorOfSet);
            }
            return null;
        }

        @Override
        public Void visitError(ErrorType t, Void p) {
            return null;
        }

        @Override
        public Void visitTypeVariable(TypeVariable t, Void p) {
            return null;
        }

        @Override
        public Void visitWildcard(WildcardType t, Void p) {
            return null;
        }

        @Override
        public Void visitExecutable(ExecutableType t, Void p) {
            return null;
        }

        @Override
        public Void visitNoType(NoType t, Void p) {
            return null;
        }

        @Override
        public Void visitIntersection(IntersectionType t, Void p) {
            return null;
        }

        @Override
        public Void visitUnion(UnionType t, Void p) {
            return null;
        }

    }

}
