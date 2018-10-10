package io.morethan.daggerdoc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import com.google.common.base.Verify;

/**
 * Assembly of {@link Node}s and {@link Link}s.
 */
public class DependencyGraph {

    private final Set<Node> _nodes;
    private final Set<Link> _links;

    private DependencyGraph(Set<Node> nodes, Set<Link> links) {
        _nodes = nodes;
        _links = links;
    }

    public Set<Node> nodes() {
        return _nodes;
    }

    public Set<Link> links() {
        return _links;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Map<String, Node> _nodeById = new HashMap<>();
        private final List<Linker> _linkers = new ArrayList<>();
        private final Set<MultibindingLinker> _multibindingLinkers = new HashSet<>();
        private final Map<TypeString, String> _multibindingConsumers = new HashMap<>();

        public Node addNode(Element element, NodeType type) {
            String id = nodeId(element);
            Node node = _nodeById.get(id);
            if (node != null) {
                Verify.verify(node.type() == type, "Can't register node for %s of type %s since it is already registered with type %s", id, type, node.type());
            } else {
                node = new Node(element.getSimpleName().toString(), type);
                _nodeById.put(id, node);
            }
            return node;
        }

        public void addDependency(Element element, TypeMirror typeMirror) {
            _linkers.add(new Linker(nodeId(element), nodeId(typeMirror)));
        }

        public void addMultibindingLink(Element element, TypeMirror multibindinType) {
            _multibindingLinkers.add(new MultibindingLinker(nodeId(element), multibindinType));
        }

        public void addMultibindingConsumer(Element containerElement, TypeMirror multiBindingType) {
            _multibindingConsumers.put(new TypeString(multiBindingType), nodeId(containerElement));
        }

        private String nodeId(Element element) {
            return element.toString();
        }

        private static String nodeId(TypeMirror typeMirror) {
            return typeMirror.toString();
        }

        public DependencyGraph build() {
            Set<Link> links = resolveLinks();
            return new DependencyGraph(new HashSet<>(_nodeById.values()), links);
        }

        private Set<Link> resolveLinks() {
            Set<Link> links = new HashSet<>();
            for (Linker linker : _linkers) {
                Node node1 = Verify.verifyNotNull(_nodeById.get(linker.nodeId1()), "Could not resolve %s", linker.nodeId1());
                Node node2 = Verify.verifyNotNull(_nodeById.get(linker.nodeId2()), "Could not resolve %s", linker.nodeId2());
                links.add(new Link(node1, node2, LinkType.DEPENDS_ON));
            }
            for (MultibindingLinker linker : _multibindingLinkers) {
                Node node1 = Verify.verifyNotNull(_nodeById.get(linker.nodeId()), "Could not resolve %s", linker.nodeId());
                String nodeId2 = Verify.verifyNotNull(_multibindingConsumers.get(linker.multibindingType()), "Did not find multibinding sink for type %s", linker.multibindingType());
                Node node2 = Verify.verifyNotNull(_nodeById.get(nodeId2), "Could not resolve %s", nodeId2);
                if (!node1.equals(node2)) {
                    links.add(new Link(node1, node2, LinkType.CONTRIBUTES_TO));
                }
            }
            return links;
        }

    }

    /**
     * Typed wrapper around {@link TypeMirror} which does {@link #equals(Object)} comparison just based of the type string.
     */
    static class TypeString {

        private final String _typeString;

        public TypeString(TypeMirror typeMirror) {
            _typeString = typeMirror.toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((_typeString == null) ? 0 : _typeString.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TypeString other = (TypeString) obj;
            if (_typeString == null) {
                if (other._typeString != null) {
                    return false;
                }
            } else if (!_typeString.equals(other._typeString)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return _typeString;
        }

    }
}
