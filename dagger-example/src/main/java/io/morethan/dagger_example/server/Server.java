package io.morethan.dagger_example.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Verify;
import com.google.common.util.concurrent.AbstractIdleService;

public class Server extends AbstractIdleService {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);
    private final int _port;
    private final String _address;
    private final Map<String, ServerEndpoint> _endpoints;

    public Server(int port, Map<String, ServerEndpoint> serverEndpoints) {
        this("0.0.0.0", port, serverEndpoints);
    }

    public Server(String address, int port, Map<String, ServerEndpoint> serverEndpoints) {
        _address = address;
        _port = port;
        _endpoints = serverEndpoints;
    }

    public String getAddress() {
        return _address;
    }

    public int getPort() {
        return _port;
    }

    public void call(String endpointName, Map<String, String> parameters) {
        ServerEndpoint endpoint = Verify.verifyNotNull(_endpoints.get(endpointName), "No endpoint '%s' found!", endpointName);
        endpoint.call(parameters);
    }

    @Override
    protected void startUp() throws Exception {
        LOG.info("Starting up server on port {}", _port);
    }

    @Override
    protected void shutDown() throws Exception {
        LOG.info("Shutdown server");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(_address).addValue(_port).addValue(_endpoints).toString();
    }

}
