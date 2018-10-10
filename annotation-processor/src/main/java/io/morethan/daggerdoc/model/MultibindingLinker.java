package io.morethan.daggerdoc.model;

import javax.lang.model.type.TypeMirror;

import com.google.common.base.MoreObjects;

import io.morethan.daggerdoc.model.DependencyGraph.TypeString;

/**
 * A {@link MultibindingLinker} will be resolved into a {@link LinkType#CONTRIBUTES_TO} {@link Link} once all
 * {@link Node}s has been added to the {@link DependencyGraph}.
 */
class MultibindingLinker {

    private final String _nodeId;
    private final TypeString _multibindingType;

    public MultibindingLinker(String nodeId, TypeMirror multibindingType) {
        _nodeId = nodeId;
        _multibindingType = new TypeString(multibindingType);
    }

    public String nodeId() {
        return _nodeId;
    }

    public TypeString multibindingType() {
        return _multibindingType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_multibindingType == null) ? 0 : _multibindingType.hashCode());
        result = prime * result + ((_nodeId == null) ? 0 : _nodeId.hashCode());
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
        MultibindingLinker other = (MultibindingLinker) obj;
        if (_multibindingType == null) {
            if (other._multibindingType != null) {
                return false;
            }
        } else if (!_multibindingType.equals(other._multibindingType)) {
            return false;
        }
        if (_nodeId == null) {
            if (other._nodeId != null) {
                return false;
            }
        } else if (!_nodeId.equals(other._nodeId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(_nodeId)
                .addValue(_multibindingType)
                .toString();
    }
}
