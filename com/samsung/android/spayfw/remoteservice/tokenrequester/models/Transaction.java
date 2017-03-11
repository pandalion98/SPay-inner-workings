package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Transaction {
    private Code authorization;
    private Url service;

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

    public String getTransactionUrl() {
        if (this.service != null) {
            return this.service.url;
        }
        return null;
    }

    public String getTransactionCode() {
        if (this.authorization != null) {
            return this.authorization.code;
        }
        return null;
    }
}
