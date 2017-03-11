package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class ContextData {
    private Environment environment;
    private Network network;
    private Token token;

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
