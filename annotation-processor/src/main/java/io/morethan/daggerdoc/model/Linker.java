package io.morethan.daggerdoc.model;

import com.google.common.base.MoreObjects;

/**
 * A {@link Linker} will be resolved into a {@link LinkType#DEPENDS_ON} {@link Link} once all {@link Node}s has been
 * added to the {@link DependencyGraph}.
 */
class Linker {

    private final String _nodeId1;
    private final String _nodeId2;

    public Linker(String nodeId1, String nodeId2) {
        _nodeId1 = nodeId1;
        _nodeId2 = nodeId2;
    }

    public String nodeId1() {
        return _nodeId1;
    }

    public String nodeId2() {
        return _nodeId2;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_nodeId1 == null) ? 0 : _nodeId1.hashCode());
        result = prime * result + ((_nodeId2 == null) ? 0 : _nodeId2.hashCode());
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
        Linker other = (Linker) obj;
        if (_nodeId1 == null) {
            if (other._nodeId1 != null) {
                return false;
            }
        } else if (!_nodeId1.equals(other._nodeId1)) {
            return false;
        }
        if (_nodeId2 == null) {
            if (other._nodeId2 != null) {
                return false;
            }
        } else if (!_nodeId2.equals(other._nodeId2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(_nodeId1)
                .addValue(_nodeId2)
                .toString();
    }
}
