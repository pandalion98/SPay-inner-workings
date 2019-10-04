/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.util.List
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ActivationParameters;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TimeStamp;
import java.util.List;

public class Activation
extends TimeStamp {
    private List<ActivationParameters> parameters;

    public Activation(long l2) {
        super(l2);
    }

    public List<ActivationParameters> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<ActivationParameters> list) {
        this.parameters = list;
    }
}

