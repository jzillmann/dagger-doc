package io.morethan.daggerdoc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;

import io.morethan.daggerdoc.model.DependencyGraph;
import io.morethan.daggerdoc.model.Link;
import io.morethan.daggerdoc.model.LinkType;
import io.morethan.daggerdoc.model.Node;
import io.morethan.daggerdoc.model.NodeType;

/**
 * Tests the outcome of the example project setup.
 */
class ExampleProjectTest {

    @Test
    void test() throws Exception {
        DependencyGraph dependencyGraph = new DaggerDocCompiler().compileFolder("../dagger-example/src/main/java/io/morethan/dagger_example/", ImmutableSet.of("AppMain.java"));

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

}
