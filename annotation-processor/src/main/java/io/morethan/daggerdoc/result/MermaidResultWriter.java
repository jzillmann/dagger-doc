package io.morethan.daggerdoc.result;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.google.auto.service.AutoService;

import io.morethan.daggerdoc.ResultWriter;
import io.morethan.daggerdoc.model.DependencyGraph;
import io.morethan.daggerdoc.model.LinkType;
import io.morethan.daggerdoc.model.Node;
import io.morethan.daggerdoc.model.NodeType;

/**
 * Writes a graph visualization in mermaid markdown syntax (https://mermaidjs.github.io/).
 */
@AutoService(ResultWriter.class)
public class MermaidResultWriter implements ResultWriter {

    @Override
    public String id() {
        return "mermaid";
    }

    @Override
    public void write(ProcessingEnvironment processingEnv, DependencyGraph dependencyGraph) {
        try {
            FileObject outputFile = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "dagger-dependencies.md");
            try (FilePrinter ouput = new FilePrinter(outputFile)) {

                ouput.println("graph BT");
                ouput.println();

                // Print components
                dependencyGraph.nodes().stream().filter(node -> node.type() == NodeType.COMPONENT).forEach(node -> {
                    ouput.printf("%s[%<s]", node.name());
                });
                ouput.println();

                // Print modules
                Map<String, List<Node>> nodesByCategory = dependencyGraph.nodes().stream()
                        .filter(node -> node.type() == NodeType.MODULE)
                        .collect(Collectors.groupingBy(node -> node.category().orElse("")));
                if (nodesByCategory.containsKey("")) {
                    nodesByCategory.remove("").forEach(node -> ouput.printf("%s(%<s)", node.name()));
                }

                nodesByCategory.entrySet().stream().forEach(entry -> {
                    ouput.printf("subgraph %s", entry.getKey());
                    entry.getValue().stream().forEach(node -> ouput.printf("  %s(%<s)", node.name()));
                    ouput.println("end");
                    ouput.println();
                });

                // Print depends-on links for components
                dependencyGraph.links().stream().filter(link -> link.type() == LinkType.DEPENDS_ON && link.node1().type() == NodeType.COMPONENT).forEach(link -> {
                    ouput.printf("%s ==> %s", link.node2().name(), link.node1().name());
                });

                // Print depends-on links for modules
                dependencyGraph.links().stream().filter(link -> link.type() == LinkType.DEPENDS_ON && link.node1().type() == NodeType.MODULE).forEach(link -> {
                    ouput.printf("%s --> %s", link.node2().name(), link.node1().name());
                });

                // Print contribution links
                dependencyGraph.links().stream().filter(link -> link.type() == LinkType.CONTRIBUTES_TO).forEach(link -> {
                    ouput.printf("%s -.-> %s", link.node1().name(), link.node2().name());
                });
            }

            processingEnv.getMessager().printMessage(Kind.NOTE, "Written Mermaid code to file://" + outputFile.getName(), null);
            processingEnv.getMessager().printMessage(Kind.NOTE, "Use https://mermaidjs.github.io/mermaid-live-editor to convert the code into an SVG", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
