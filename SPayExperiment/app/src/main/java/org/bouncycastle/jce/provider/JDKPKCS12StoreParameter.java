/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.security.KeyStore
 *  java.security.KeyStore$LoadStoreParameter
 *  java.security.KeyStore$PasswordProtection
 *  java.security.KeyStore$ProtectionParameter
 */
package org.bouncycastle.jce.provider;

import java.io.OutputStream;
import java.security.KeyStore;

public class JDKPKCS12StoreParameter
implements KeyStore.LoadStoreParameter {
    private OutputStream outputStream;
    private KeyStore.ProtectionParameter protectionParameter;
    private boolean useDEREncoding;

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public KeyStore.ProtectionParameter getProtectionParameter() {
        return this.protectionParameter;
    }

    public boolean isUseDEREncoding() {
        return this.useDEREncoding;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setPassword(char[] arrc) {
        this.protectionParameter = new KeyStore.PasswordProtection(arrc);
    }

    public void setProtectionParameter(KeyStore.ProtectionParameter protectionParameter) {
        this.protectionParameter = protectionParameter;
    }

    public void setUseDEREncoding(boolean bl) {
        this.useDEREncoding = bl;
    }
}

