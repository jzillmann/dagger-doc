package io.morethan.daggerdoc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;

import org.junit.jupiter.api.Test;

import com.google.common.base.Joiner;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.morethan.daggerdoc.model.DependencyGraph;
import io.morethan.daggerdoc.model.Link;
import io.morethan.daggerdoc.model.LinkType;
import io.morethan.daggerdoc.model.Node;
import io.morethan.daggerdoc.model.NodeType;

public class ContributesToTest {

    @Test
    void test() throws Exception {
        DependencyGraph dependencyGraph = new DaggerDocCompiler().compileClass(getClass());

        Node component = new Node("TestComponent", NodeType.COMPONENT, Optional.empty());
        Node sink1Module = new Node("ContributionSink1Module", NodeType.MODULE, Optional.empty());
        Node sink2Module = new Node("ContributionSink2Module", NodeType.MODULE, Optional.empty());
        Node contributingModule = new Node("ContributingToModule", NodeType.MODULE, Optional.empty());

        assertThat(dependencyGraph.nodes()).containsOnly(
                component,
                sink1Module,
                sink2Module,
                contributingModule

        );

        assertThat(dependencyGraph.links()).containsOnly(
                new Link(component, sink1Module, LinkType.DEPENDS_ON),
                new Link(component, sink2Module, LinkType.DEPENDS_ON),
                new Link(component, contributingModule, LinkType.DEPENDS_ON),
                new Link(contributingModule, sink1Module, LinkType.CONTRIBUTES_TO),
                new Link(contributingModule, sink2Module, LinkType.CONTRIBUTES_TO)

        );
    }

    @Component(modules = { ContributionSink1Module.class, ContributionSink2Module.class, ContributingToModule.class })
    @Singleton
    static interface TestComponent {

        @Named("comma")
        String commaJoined();

        @Named("dash")
        String dashJoined();
    }

    @Module
    static class ContributionSink1Module {
        @Provides
        @Singleton
        @Named("comma")
        public static String joined(Set<String> parts) {
            return Joiner.on(',').join(parts);
        }
    }

    @Module
    static class ContributionSink2Module {
        @Provides
        @Singleton
        @Named("dash")
        public static String joined(Set<String> parts) {
            return Joiner.on('-').join(parts);
        }
    }

    @Module
    static class ContributingToModule {
        @Provides
        @Singleton
        @IntoSet
        public static String part1() {
            return "part1";
        }

        @Provides
        @Singleton
        @IntoSet
        public static String part2() {
            return "part2";
        }
    }

}
