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
import org.bouncycastle.crypto.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsUtils;

public class DigitallySigned {
    protected SignatureAndHashAlgorithm algorithm;
    protected byte[] signature;

    public DigitallySigned(SignatureAndHashAlgorithm signatureAndHashAlgorithm, byte[] arrby) {
        if (arrby == null) {
            throw new IllegalArgumentException("'signature' cannot be null");
        }
        this.algorithm = signatureAndHashAlgorithm;
        this.signature = arrby;
    }

    public static DigitallySigned parse(TlsContext tlsContext, InputStream inputStream) {
        boolean bl = TlsUtils.isTLSv12(tlsContext);
        SignatureAndHashAlgorithm signatureAndHashAlgorithm = null;
        if (bl) {
            signatureAndHashAlgorithm = SignatureAndHashAlgorithm.parse(inputStream);
        }
        return new DigitallySigned(signatureAndHashAlgorithm, TlsUtils.readOpaque16(inputStream));
    }

    public void encode(OutputStream outputStream) {
        if (this.algorithm != null) {
            this.algorithm.encode(outputStream);
        }
        TlsUtils.writeOpaque16(this.signature, outputStream);
    }

    public SignatureAndHashAlgorithm getAlgorithm() {
        return this.algorithm;
    }

    public byte[] getSignature() {
        return this.signature;
    }
}

