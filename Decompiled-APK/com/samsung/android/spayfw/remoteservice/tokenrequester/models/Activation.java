package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import java.util.List;

public class Activation extends TimeStamp {
    private List<ActivationParameters> parameters;

    public Activation(long j) {
        super(j);
    }

    public List<ActivationParameters> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<ActivationParameters> list) {
        this.parameters = list;
    }
}
