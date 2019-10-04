/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Activation;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ActivationParameters;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Id;
import java.util.List;

public class IdvSelectionResponseData {
    private IdvSelectionParameters activation;

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
        return -1L;
    }

    public int getCodeLength() {
        if (this.activation != null && this.activation.code != null) {
            return this.activation.code.length;
        }
        return -1;
    }

    public JsonObject getData() {
        if (this.activation != null) {
            return this.activation.data;
        }
        return null;
    }

    public long getExpiryMax() {
        if (this.activation != null && this.activation.expiry != null) {
            return this.activation.expiry.max;
        }
        return -1L;
    }

    public long getExpiryOn() {
        if (this.activation != null && this.activation.expiry != null) {
            return this.activation.expiry.on;
        }
        return -1L;
    }

    public int getMaxAttempts() {
        if (this.activation != null && this.activation.attempts != null) {
            return this.activation.attempts.max;
        }
        return -1;
    }

    public String getMethodId() {
        if (this.activation != null && this.activation.method != null) {
            return this.activation.method.getId();
        }
        return null;
    }

    private static class Expiry {
        private int max;
        private long on;

        private Expiry() {
        }
    }

    private static class IdvSelectionParameters
    extends Activation {
        private Max attempts;
        private Length code;
        private JsonObject data;
        private Expiry expiry;
        private Id method;

        public IdvSelectionParameters(long l2) {
            super(l2);
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

}

