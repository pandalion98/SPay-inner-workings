package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import java.util.List;

public class IdvSelectionResponseData {
    private IdvSelectionParameters activation;

    private static class Expiry {
        private int max;
        private long on;

        private Expiry() {
        }
    }

    private static class IdvSelectionParameters extends Activation {
        private Max attempts;
        private Length code;
        private JsonObject data;
        private Expiry expiry;
        private Id method;

        public IdvSelectionParameters(long j) {
            super(j);
        }
    }

    private static class Length {
        private int length;

        private Length() {
        }
    }

    private static class Max {
        private int max;

        private Max() {
        }
    }

    public int getCodeLength() {
        if (this.activation == null || this.activation.code == null) {
            return -1;
        }
        return this.activation.code.length;
    }

    public long getExpiryMax() {
        if (this.activation == null || this.activation.expiry == null) {
            return -1;
        }
        return (long) this.activation.expiry.max;
    }

    public long getExpiryOn() {
        if (this.activation == null || this.activation.expiry == null) {
            return -1;
        }
        return this.activation.expiry.on;
    }

    public int getMaxAttempts() {
        if (this.activation == null || this.activation.attempts == null) {
            return -1;
        }
        return this.activation.attempts.max;
    }

    public String getMethodId() {
        if (this.activation == null || this.activation.method == null) {
            return null;
        }
        return this.activation.method.getId();
    }

    public List<ActivationParameters> getActivationParameters() {
        if (this.activation != null) {
            return this.activation.getParameters();
        }
        return null;
    }

    public long getActivationTimestamp() {
        if (this.activation != null) {
            return this.activation.getTimestamp();
        }
        return -1;
    }

    public JsonObject getData() {
        if (this.activation != null) {
            return this.activation.data;
        }
        return null;
    }
}
