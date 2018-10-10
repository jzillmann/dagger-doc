package io.morethan.daggerdoc.model;

import com.google.common.base.MoreObjects;

/**
 * A node in a dependency graph.
 */
public class Node {

    private final String _name;
    private final NodeType _type;

    public Node(String name, NodeType type) {
        _name = name;
        _type = type;
    }

    public String name() {
        return _name;
    }

    public NodeType type() {
        return _type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
        Node other = (Node) obj;
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
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
                .addValue(_name)
                .addValue(_type)
                .toString();
    }

}
