package io.morethan.daggerdoc.result;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

import com.google.auto.service.AutoService;

import io.morethan.daggerdoc.ResultWriter;
import io.morethan.daggerdoc.model.DependencyGraph;
import io.morethan.daggerdoc.model.Link;
import io.morethan.daggerdoc.model.Node;

@AutoService(ResultWriter.class)
public class LoggingResultWriter implements ResultWriter {

    @Override
    public String id() {
        return "log";
    }

    @Override
    public void write(ProcessingEnvironment processingEnv, DependencyGraph dependencyGraph) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Kind.NOTE, "Nodes:", null);
        for (Node node : dependencyGraph.nodes()) {
            messager.printMessage(Kind.NOTE, "\t" + node.name() + " - " + node.type(), null);
        }
        messager.printMessage(Kind.NOTE, "Links:", null);
        for (Link link : dependencyGraph.links()) {
            messager.printMessage(Kind.NOTE, "\t" + link.node1().name() + " -- " + link.type() + " --> " + link.node2().name(), null);
        }
    }

}
