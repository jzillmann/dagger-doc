package io.morethan.daggerdoc;

import static com.google.testing.compile.Compiler.javac;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.tools.JavaFileObject;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

import io.morethan.daggerdoc.model.DependencyGraph;
import io.morethan.daggerdoc.model.Link;
import io.morethan.daggerdoc.model.LinkType;
import io.morethan.daggerdoc.model.Node;
import io.morethan.daggerdoc.model.NodeType;

class DaggerDocProcessorTest {

    @Test
    void test() throws Exception {
        TestResultWriter resultWriter = new TestResultWriter();
        Compiler compiler = javac().withProcessors(new DaggerDocProcessor(resultWriter));

        List<File> files = new ArrayList<>();
        gatherSourceFiles(new File("../dagger-example/src/main/java/io/morethan/dagger_example/"), ImmutableSet.of("AppMain.java"), files);
        JavaFileObject[] sourceFiles = files.stream().map(file -> {
            try {
                return JavaFileObjects.forResource(file.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }).toArray(JavaFileObject[]::new);

        Compilation compilation = compiler.compile(sourceFiles);
        assertThat(compilation.errors()).isEmpty();
        DependencyGraph dependencyGraph = resultWriter.dependencyGraph();
        assertThat(dependencyGraph).isNotNull();

        Node appComponent = new Node("AppComponent", NodeType.COMPONENT);
        Node appModule = new Node("AppModule", NodeType.MODULE);
        Node serverModule = new Node("ServerModule", NodeType.MODULE);
        Node serviceModule = new Node("ServiceModule", NodeType.MODULE);

        assertThat(dependencyGraph.nodes()).containsOnly(
                appComponent,
                appModule,
                serverModule,
                serviceModule);

        assertThat(dependencyGraph.links()).containsOnly(
                new Link(appComponent, appModule, LinkType.DEPENDS_ON),
                new Link(appModule, serverModule, LinkType.DEPENDS_ON),
                new Link(appModule, serviceModule, LinkType.DEPENDS_ON),
                new Link(serviceModule, serverModule, LinkType.CONTRIBUTES_TO),
                new Link(appModule, serverModule, LinkType.CONTRIBUTES_TO));

    }

    private void gatherSourceFiles(File rootFolder, Set<String> excludes, List<File> javaFiles) throws IOException {
        assertThat(rootFolder).exists().isDirectory();
        for (File child : rootFolder.listFiles()) {
            if (child.isDirectory()) {
                gatherSourceFiles(child, excludes, javaFiles);
            } else if (child.getName().endsWith(".java") && !excludes.contains(child.getName())) {
                javaFiles.add(child.getCanonicalFile());
            }
        }
    }

}
