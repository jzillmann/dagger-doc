package io.morethan.dagger_example.server;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.MoreObjects;

public class Server {

    private final List<ServerService> _services = new ArrayList<>();
    private final int _port;
    private final String _address;

    public Server(int port) {
        this("0.0.0.0", port);
    }

    public Server(String address, int port) {
        _address = address;
        _port = port;
    }

    public void add(ServerService service) {
        _services.add(service);
    }

    public String getAddress() {
        return _address;
    }

    public int getPort() {
        return _port;
    }

    public List<ServerService> getServices() {
        return _services;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(_address).addValue(_port).addValue(_services).toString();
    }

}
