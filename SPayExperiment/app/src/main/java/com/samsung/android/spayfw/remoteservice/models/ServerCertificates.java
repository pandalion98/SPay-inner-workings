/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.remoteservice.models;

import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

public class ServerCertificates {
    private Certificates server;

    public CertificateInfo[] getCertificates() {
        if (this.server != null) {
            return this.server.certificates;
        }
        return null;
    }

    private static class Certificates {
        private CertificateInfo[] certificates;

        private Certificates() {
        }
    }

}

