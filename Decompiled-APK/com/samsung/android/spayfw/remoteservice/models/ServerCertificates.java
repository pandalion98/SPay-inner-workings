package com.samsung.android.spayfw.remoteservice.models;

public class ServerCertificates {
    private Certificates server;

    private static class Certificates {
        private CertificateInfo[] certificates;

        private Certificates() {
        }
    }

    public CertificateInfo[] getCertificates() {
        if (this.server != null) {
            return this.server.certificates;
        }
        return null;
    }
}
