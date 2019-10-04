/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.signers;

import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class DSADigestSigner
implements Signer {
    private final Digest digest;
    private final DSA dsaSigner;
    private boolean forSigning;

    public DSADigestSigner(DSA dSA, Digest digest) {
        this.digest = digest;
        this.dsaSigner = dSA;
    }

    private BigInteger[] derDecode(byte[] arrby) {
        ASN1Sequence aSN1Sequence = (ASN1Sequence)ASN1Primitive.fromByteArray(arrby);
        BigInteger[] arrbigInteger = new BigInteger[]{((ASN1Integer)aSN1Sequence.getObjectAt(0)).getValue(), ((ASN1Integer)aSN1Sequence.getObjectAt(1)).getValue()};
        return arrbigInteger;
    }

    private byte[] derEncode(BigInteger bigInteger, BigInteger bigInteger2) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new ASN1Integer(bigInteger));
        aSN1EncodableVector.add(new ASN1Integer(bigInteger2));
        return new DERSequence(aSN1EncodableVector).getEncoded("DER");
    }

    @Override
    public byte[] generateSignature() {
        if (!this.forSigning) {
            throw new IllegalStateException("DSADigestSigner not initialised for signature generation.");
        }
        byte[] arrby = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby, 0);
        BigInteger[] arrbigInteger = this.dsaSigner.generateSignature(arrby);
        try {
            byte[] arrby2 = this.derEncode(arrbigInteger[0], arrbigInteger[1]);
            return arrby2;
        }
        catch (IOException iOException) {
            throw new IllegalStateException("unable to encode signature");
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.forSigning = bl;
        AsymmetricKeyParameter asymmetricKeyParameter = cipherParameters instanceof ParametersWithRandom ? (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters() : (AsymmetricKeyParameter)cipherParameters;
        if (bl && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Signing Requires Private Key.");
        }
        if (!bl && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Verification Requires Public Key.");
        }
        this.reset();
        this.dsaSigner.init(bl, cipherParameters);
    }

    @Override
    public void reset() {
        this.digest.reset();
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }

    @Override
    public boolean verifySignature(byte[] arrby) {
        if (this.forSigning) {
            throw new IllegalStateException("DSADigestSigner not initialised for verification");
        }
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(arrby2, 0);
        try {
            BigInteger[] arrbigInteger = this.derDecode(arrby);
            boolean bl = this.dsaSigner.verifySignature(arrby2, arrbigInteger[0], arrbigInteger[1]);
            return bl;
        }
        catch (IOException iOException) {
            return false;
        }
    }
}

