package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class IdvOptionsData {
    private IdvOptions activation;

    private static class IdvOptions {
        private IdvOptionData[] methods;

        private IdvOptions() {
        }
    }

    public IdvOptionData[] getMethods() {
        if (this.activation != null) {
            return this.activation.methods;
        }
        return null;
    }
}
