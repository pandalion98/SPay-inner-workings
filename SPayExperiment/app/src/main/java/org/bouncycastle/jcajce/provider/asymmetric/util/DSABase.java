/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.math.BigInteger
 *  java.security.SignatureException
 *  java.security.SignatureSpi
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.math.BigInteger;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.jcajce.provider.asymmetric.util.DSAEncoder;

public abstract class DSABase
extends SignatureSpi
implements PKCSObjectIdentifiers,
X509ObjectIdentifiers {
    protected Digest digest;
    protected DSAEncoder encoder;
    protected DSA signer;

    protected DSABase(Digest digest, DSA dSA, DSAEncoder dSAEncoder) {
        this.digest = digest;
        this.signer = dSA;
        this.encoder = dSAEncoder;
    }

    protected Object engineGetParameter(String string) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineSetParameter(String string, Object object) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected byte[] engineSign() {
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        try {
            BigInteger[] arrbigInteger = this.signer.generateSignature(arrby);
            byte[] arrby2 = this.encoder.encode(arrbigInteger[0], arrbigInteger[1]);
            return arrby2;
        }
        catch (Exception exception) {
            throw new SignatureException(exception.toString());
        }
    }

    protected void engineUpdate(byte by) {
        this.digest.update(by);
    }

    protected void engineUpdate(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }

    protected boolean engineVerify(byte[] arrby) {
        BigInteger[] arrbigInteger;
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby2, 0);
        try {
            arrbigInteger = this.encoder.decode(arrby);
        }
        catch (Exception exception) {
            throw new SignatureException("error decoding signature bytes.");
        }
        return this.signer.verifySignature(arrby2, arrbigInteger[0], arrbigInteger[1]);
    }
}

