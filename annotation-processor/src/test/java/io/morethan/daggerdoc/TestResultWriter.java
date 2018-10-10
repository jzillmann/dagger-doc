package io.morethan.daggerdoc;

import javax.annotation.processing.ProcessingEnvironment;

import io.morethan.daggerdoc.model.DependencyGraph;

public class TestResultWriter implements ResultWriter {

    private DependencyGraph _dependencyGraph;

    @Override
    public String id() {
        return "test";
    }

    public DependencyGraph dependencyGraph() {
        return _dependencyGraph;
    }

    @Override
    public void write(ProcessingEnvironment processingEnv, DependencyGraph dependencyGraph) {
        _dependencyGraph = dependencyGraph;
    }

}
