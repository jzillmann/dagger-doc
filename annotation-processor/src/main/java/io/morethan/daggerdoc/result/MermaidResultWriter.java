package io.morethan.daggerdoc.result;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.google.auto.service.AutoService;
import com.google.common.io.BaseEncoding;

import io.morethan.daggerdoc.ResultWriter;
import io.morethan.daggerdoc.model.DependencyGraph;
import io.morethan.daggerdoc.model.LinkType;
import io.morethan.daggerdoc.model.Node;
import io.morethan.daggerdoc.model.NodeType;

/**
 * Writes a graph visualization in mermaid markdown syntax (https://mermaidjs.github.io/).
 * 
 * 
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
            Writer base64Writer = new StringWriter();
            try (FilePrinter ouput = new FilePrinter(outputFile, base64Writer)) {

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

            processingEnv.getMessager().printMessage(Kind.NOTE, "Written result file to file://" + outputFile.getName(), null);
            processingEnv.getMessager().printMessage(Kind.NOTE, "Open at https://mermaidjs.github.io/mermaid-live-editor/#/edit/" + base64Writer.toString(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static class FilePrinter implements AutoCloseable {

        private final PrintWriter _fileWriter;
        private final PrintWriter _base64Writer;

        public FilePrinter(FileObject outputFile, Writer base64Writer) {
            try {
                _fileWriter = new PrintWriter(new OutputStreamWriter(outputFile.openOutputStream(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            _base64Writer = new PrintWriter(BaseEncoding.base64().encodingStream(base64Writer));
        }

        public void println() {
            _fileWriter.println();
            _base64Writer.println();
        }

        public void println(String string) {
            _fileWriter.println(string);
            _base64Writer.println(string);
        }

        public void printf(String template, Object... values) {
            println(String.format(template, values));
        }

        @Override
        public void close() {
            _fileWriter.close();
            _base64Writer.close();
        }

    }
}
