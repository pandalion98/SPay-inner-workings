/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Deprecated
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class SecureContext {
    private DevCryptoContext deviceCryptoContext = new DevCryptoContext();
    private String encryptedContent;
    private String usesServerLevelEncryption = "false";

    public SecureContext() {
    }

    public SecureContext(String string) {
        this.encryptedContent = string;
    }

    public String getEncryptedPayload() {
        return this.encryptedContent;
    }

    @Deprecated
    public String getKeyId() {
        return null;
    }

    public void setEncryptedPayload(String string) {
        this.encryptedContent = string;
    }

    @Deprecated
    public void setKeyId(String string) {
    }

    public static class DevCryptoContext {
        private static final String ENCRYPTION_TYPE_DEFAULT = "JWE";
        public String cekAlgo = null;
        public String encryptedCEK = null;
        public String encryptionType = "JWE";
        public String initVector = null;
        public String kekAlgo = null;

        DevCryptoContext() {
        }
    }

}

