/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.tls;

import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.tls.TlsUtils;

public class SignatureAndHashAlgorithm {
    protected short hash;
    protected short signature;

    public SignatureAndHashAlgorithm(short s2, short s3) {
        if (!TlsUtils.isValidUint8(s2)) {
            throw new IllegalArgumentException("'hash' should be a uint8");
        }
        if (!TlsUtils.isValidUint8(s3)) {
            throw new IllegalArgumentException("'signature' should be a uint8");
        }
        if (s3 == 0) {
            throw new IllegalArgumentException("'signature' MUST NOT be \"anonymous\"");
        }
        this.hash = s2;
        this.signature = s3;
    }

    public static SignatureAndHashAlgorithm parse(InputStream inputStream) {
        return new SignatureAndHashAlgorithm(TlsUtils.readUint8(inputStream), TlsUtils.readUint8(inputStream));
    }

    public void encode(OutputStream outputStream) {
        TlsUtils.writeUint8(this.getHash(), outputStream);
        TlsUtils.writeUint8(this.getSignature(), outputStream);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        SignatureAndHashAlgorithm signatureAndHashAlgorithm;
        return object instanceof SignatureAndHashAlgorithm && (signatureAndHashAlgorithm = (SignatureAndHashAlgorithm)object).getHash() == this.getHash() && signatureAndHashAlgorithm.getSignature() == this.getSignature();
    }

    public short getHash() {
        return this.hash;
    }

    public short getSignature() {
        return this.signature;
    }

    public int hashCode() {
        return this.getHash() << 16 | this.getSignature();
    }
}

