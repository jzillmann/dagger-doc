package io.morethan.daggerdoc.model;

import com.google.common.base.MoreObjects;

/**
 * A link between 2 {@link Node}s.
 */
public class Link {

    private final Node _node1;
    private final Node _node2;
    private final LinkType _type;

    public Link(Node node1, Node node2, LinkType type) {
        _node1 = node1;
        _node2 = node2;
        _type = type;
    }

    public Node node1() {
        return _node1;
    }

    public Node node2() {
        return _node2;
    }

    public LinkType type() {
        return _type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_node1 == null) ? 0 : _node1.hashCode());
        result = prime * result + ((_node2 == null) ? 0 : _node2.hashCode());
        result = prime * result + ((_type == null) ? 0 : _type.hashCode());
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
        Link other = (Link) obj;
        if (_node1 == null) {
            if (other._node1 != null) {
                return false;
            }
        } else if (!_node1.equals(other._node1)) {
            return false;
        }
        if (_node2 == null) {
            if (other._node2 != null) {
                return false;
            }
        } else if (!_node2.equals(other._node2)) {
            return false;
        }
        if (_type != other._type) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(_node1.name())
                .addValue(_node2.name())
                .addValue(_type)
                .toString();
    }

}
