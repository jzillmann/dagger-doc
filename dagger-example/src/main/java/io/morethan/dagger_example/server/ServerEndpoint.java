package io.morethan.dagger_example.server;

import java.util.Map;

import com.google.common.base.MoreObjects;

public abstract class ServerEndpoint {

    public String type() {
        return getClass().getSimpleName();
    }

    public abstract void call(Map<String, String> parameters);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type() == null) ? 0 : type().hashCode());
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
        ServerEndpoint other = (ServerEndpoint) obj;
        if (type() == null) {
            if (other.type() != null) {
                return false;
            }
        } else if (!type().equals(other.type())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(type()).toString();
    }

}
