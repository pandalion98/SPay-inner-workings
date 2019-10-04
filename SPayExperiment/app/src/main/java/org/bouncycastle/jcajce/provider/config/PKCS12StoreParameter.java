/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.security.KeyStore
 *  java.security.KeyStore$PasswordProtection
 *  java.security.KeyStore$ProtectionParameter
 */
package org.bouncycastle.jcajce.provider.config;

import java.io.OutputStream;
import java.security.KeyStore;

public class PKCS12StoreParameter
extends org.bouncycastle.jcajce.PKCS12StoreParameter {
    public PKCS12StoreParameter(OutputStream outputStream, KeyStore.ProtectionParameter protectionParameter) {
        super(outputStream, protectionParameter, false);
    }

    public PKCS12StoreParameter(OutputStream outputStream, KeyStore.ProtectionParameter protectionParameter, boolean bl) {
        super(outputStream, protectionParameter, bl);
    }

    public PKCS12StoreParameter(OutputStream outputStream, char[] arrc) {
        super(outputStream, arrc, false);
    }

    public PKCS12StoreParameter(OutputStream outputStream, char[] arrc, boolean bl) {
        super(outputStream, (KeyStore.ProtectionParameter)new KeyStore.PasswordProtection(arrc), bl);
    }
}

