package io.morethan.daggerdoc;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;

import io.morethan.daggerdoc.model.DependencyGraph;

/**
 * Called at the end of the annotation processing with the parsed result in form of an {@link DependencyGraph}.
 */
public interface ResultWriter {

    String id();

    /**
     * @return options that can be specified as compile options (-Ax=y) and retrieved via
     *         {@link ProcessingEnvironment#getOptions()}
     */
    default Set<String> supportedOptions() {
        return Collections.emptySet();
    }

    void write(ProcessingEnvironment processingEnv, DependencyGraph dependencyGraph);

}
