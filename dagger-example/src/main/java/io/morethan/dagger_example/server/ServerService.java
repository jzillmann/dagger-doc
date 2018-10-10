package io.morethan.dagger_example.server;

import com.google.common.base.MoreObjects;

public class ServerService {

    private final String _name;

    public ServerService(String name) {
        _name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
        ServerService other = (ServerService) obj;
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(_name).toString();
    }

}
