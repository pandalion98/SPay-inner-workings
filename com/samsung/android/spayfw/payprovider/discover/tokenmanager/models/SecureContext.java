package com.samsung.android.spayfw.payprovider.discover.tokenmanager.models;

public class SecureContext {
    private DevCryptoContext deviceCryptoContext;
    private String encryptedContent;
    private String usesServerLevelEncryption;

    public static class DevCryptoContext {
        private static final String ENCRYPTION_TYPE_DEFAULT = "JWE";
        public String cekAlgo;
        public String encryptedCEK;
        public String encryptionType;
        public String initVector;
        public String kekAlgo;

        DevCryptoContext() {
            this.encryptionType = ENCRYPTION_TYPE_DEFAULT;
            this.cekAlgo = null;
            this.initVector = null;
            this.kekAlgo = null;
            this.encryptedCEK = null;
        }
    }

    public SecureContext(String str) {
        this.deviceCryptoContext = new DevCryptoContext();
        this.usesServerLevelEncryption = "false";
        this.encryptedContent = str;
    }

    public SecureContext() {
        this.deviceCryptoContext = new DevCryptoContext();
        this.usesServerLevelEncryption = "false";
    }

    public String getEncryptedPayload() {
        return this.encryptedContent;
    }

    public void setEncryptedPayload(String str) {
        this.encryptedContent = str;
    }

    @Deprecated
    public void setKeyId(String str) {
    }

    @Deprecated
    public String getKeyId() {
        return null;
    }
}
