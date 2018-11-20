package io.morethan.daggerdoc.result;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.google.auto.service.AutoService;

import io.morethan.daggerdoc.ResultWriter;
import io.morethan.daggerdoc.model.DependencyGraph;

/**
 * Generates a html report with help of the html-report module.
 */
@AutoService(ResultWriter.class)
public class HtmlReportGenerator implements ResultWriter {

    private static final String UI_RESOURCE_FOLDER = "/ui";

    @Override
    public String id() {
        return "html";
    }

    @Override
    public void write(ProcessingEnvironment processingEnv, DependencyGraph dependencyGraph) {
        writeUiResources(path -> {
            try {
                FileObject outputFile = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", path.getFileName().toString());
                try (OutputStream outputStream = outputFile.openOutputStream();) {
                    Files.copy(path, outputStream);
                }
                processingEnv.getMessager().printMessage(Kind.NOTE, "Wrote file " + outputFile.getName(), null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            FileObject providedJsFile = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "provided.js");
            try (FilePrinter writer = new FilePrinter(providedJsFile)) {
                writer.println("var graphPayload = { ");

                // Print Nodes
                writer.println("\t\"nodes\": [");
                dependencyGraph.nodes().stream().sorted((node1, node2) -> node1.type().compareTo(node2.type())).forEach(node -> {
                    writer.printf("\t\t{");
                    writer.printf("\t\t\"name\":\"%s\",", node.name());
                    writer.printf("\t\t\"type\":\"%s\",", node.type());
                    node.category().ifPresent(category -> {
                        writer.printf("\t\t\"category\":\"%s\"", category);
                    });
                    writer.printf("\t\t},");
                });
                writer.println("],");

                // Print Links
                writer.println("\t\"links\": [");
                dependencyGraph.links().stream().forEach(link -> {
                    writer.printf("\t\t{");
                    writer.printf("\t\t\"from\":\"%s\",", link.node1().name());
                    writer.printf("\t\t\"to\":\"%s\",", link.node2().name());
                    writer.printf("\t\t\"type\":\"%s\"", link.type());
                    writer.printf("\t\t},");
                });
                writer.println("]");

                writer.println("}");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeUiResources(Consumer<Path> pathConsumer) {
        URI resourceFolder = getResourceFolder();
        try {
            Path myPath;
            if (resourceFolder.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(resourceFolder, Collections.<String, Object>emptyMap());
                myPath = fileSystem.getPath(UI_RESOURCE_FOLDER);
            } else {
                myPath = Paths.get(resourceFolder);
            }
            try (Stream<Path> walk = Files.walk(myPath, 1);) {
                for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
                    Path path = it.next();
                    if (!Files.isDirectory(path)) {
                        pathConsumer.accept(path);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to writer ui resources", e);
        }
    }

    private static URI getResourceFolder() {
        URL resource = HtmlReportGenerator.class.getResource(UI_RESOURCE_FOLDER);
        if (resource == null) {
            throw new IllegalStateException("Failed to load ui resources.");
        }
        try {
            return resource.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
