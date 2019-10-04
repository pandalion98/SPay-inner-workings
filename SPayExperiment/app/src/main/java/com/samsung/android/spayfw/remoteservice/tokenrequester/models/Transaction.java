/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Transaction {
    private Code authorization;
    private Url service;

    public String getTransactionCode() {
        if (this.authorization != null) {
            return this.authorization.code;
        }
        return null;
    }

    public String getTransactionUrl() {
        if (this.service != null) {
            return this.service.url;
        }
        return null;
    }

    private static class Code {
        private String code;

        private Code() {
        }
    }

    private static class Url {
        private String url;

        private Url() {
        }
    }

}

