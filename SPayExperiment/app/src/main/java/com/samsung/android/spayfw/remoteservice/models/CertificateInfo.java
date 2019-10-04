/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.models;

public class CertificateInfo {
    public static final String CERT_USAGE_CA = "CA";
    public static final String CERT_USAGE_DEC = "DEC";
    public static final String CERT_USAGE_ENC = "ENC";
    public static final String CERT_USAGE_SIG = "SIG";
    public static final String CERT_USAGE_SSL = "SSL";
    public static final String CERT_USAGE_VER = "VER";
    private String alias;
    private String content;
    private String usage;

    public String getAlias() {
        return this.alias;
    }

    public String getContent() {
        return this.content;
    }

    public String getUsage() {
        return this.usage;
    }

    public void setAlias(String string) {
        this.alias = string;
    }

    public void setContent(String string) {
        this.content = string;
    }

    public void setUsage(String string) {
        this.usage = string;
    }

    public String toString() {
        return "CertificateInfo [alias=" + this.alias + ", content=" + this.content + ", usage=" + this.usage + "]";
    }
}

