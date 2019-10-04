/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.tls.TlsCredentials;

public interface TlsEncryptionCredentials
extends TlsCredentials {
    public byte[] decryptPreMasterSecret(byte[] var1);
}

