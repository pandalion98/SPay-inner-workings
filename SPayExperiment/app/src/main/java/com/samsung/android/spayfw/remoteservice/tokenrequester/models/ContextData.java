/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Environment;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Network;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Token;

public class ContextData {
    private Environment environment;
    private Network network;
    private Token token;

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}

