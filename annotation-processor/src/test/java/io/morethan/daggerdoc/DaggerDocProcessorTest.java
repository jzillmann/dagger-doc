package io.morethan.daggerdoc;

import static com.google.testing.compile.Compiler.javac;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        Node appComponent = new Node("AppComponent", NodeType.COMPONENT, Optional.empty());
        Node appModule = new Node("AppModule", NodeType.MODULE, Optional.of("App Layer"));
        Node serverModule = new Node("ServerModule", NodeType.MODULE, Optional.of("UI Access Layer"));
        Node executionModule = new Node("ExecutionModule", NodeType.MODULE, Optional.of("Infrastructure Layer"));
        Node persistenceModule = new Node("PersistenceModule", NodeType.MODULE, Optional.of("Infrastructure Layer"));
        Node mailModule = new Node("MailModule", NodeType.MODULE, Optional.of("Infrastructure Layer"));
        Node userModule = new Node("UserModule", NodeType.MODULE, Optional.of("Domain Layer"));
        Node chatModule = new Node("ChatModule", NodeType.MODULE, Optional.of("Domain Layer"));

        assertThat(dependencyGraph.nodes()).containsOnly(
                appComponent,
                appModule,
                serverModule,
                executionModule,
                persistenceModule,
                mailModule,
                userModule,
                chatModule

        );

        assertThat(dependencyGraph.links()).containsOnly(
                new Link(appComponent, appModule, LinkType.DEPENDS_ON),
                new Link(appComponent, userModule, LinkType.DEPENDS_ON),
                new Link(appComponent, chatModule, LinkType.DEPENDS_ON),
                new Link(appModule, serverModule, LinkType.DEPENDS_ON),
                new Link(appModule, persistenceModule, LinkType.DEPENDS_ON),
                new Link(appModule, executionModule, LinkType.DEPENDS_ON),
                new Link(appModule, mailModule, LinkType.DEPENDS_ON),
                new Link(executionModule, mailModule, LinkType.DEPENDS_ON),
                new Link(userModule, persistenceModule, LinkType.DEPENDS_ON),
                new Link(userModule, mailModule, LinkType.DEPENDS_ON),
                new Link(chatModule, persistenceModule, LinkType.DEPENDS_ON),

                new Link(userModule, serverModule, LinkType.CONTRIBUTES_TO),
                new Link(chatModule, serverModule, LinkType.CONTRIBUTES_TO)

        );

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
