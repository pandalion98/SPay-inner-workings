/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.PublicKey
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.security.PublicKey;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
import org.bouncycastle.pqc.asn1.RainbowPublicKey;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPublicKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;
import org.bouncycastle.pqc.jcajce.provider.util.KeyUtil;
import org.bouncycastle.pqc.jcajce.spec.RainbowPublicKeySpec;
import org.bouncycastle.util.Arrays;

public class BCRainbowPublicKey
implements PublicKey {
    private static final long serialVersionUID = 1L;
    private short[][] coeffquadratic;
    private short[] coeffscalar;
    private short[][] coeffsingular;
    private int docLength;
    private RainbowParameters rainbowParams;

    public BCRainbowPublicKey(int n, short[][] arrs, short[][] arrs2, short[] arrs3) {
        this.docLength = n;
        this.coeffquadratic = arrs;
        this.coeffsingular = arrs2;
        this.coeffscalar = arrs3;
    }

    public BCRainbowPublicKey(RainbowPublicKeyParameters rainbowPublicKeyParameters) {
        this(rainbowPublicKeyParameters.getDocLength(), rainbowPublicKeyParameters.getCoeffQuadratic(), rainbowPublicKeyParameters.getCoeffSingular(), rainbowPublicKeyParameters.getCoeffScalar());
    }

    public BCRainbowPublicKey(RainbowPublicKeySpec rainbowPublicKeySpec) {
        this(rainbowPublicKeySpec.getDocLength(), rainbowPublicKeySpec.getCoeffQuadratic(), rainbowPublicKeySpec.getCoeffSingular(), rainbowPublicKeySpec.getCoeffScalar());
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        BCRainbowPublicKey bCRainbowPublicKey;
        return object != null && object instanceof BCRainbowPublicKey && this.docLength == (bCRainbowPublicKey = (BCRainbowPublicKey)object).getDocLength() && RainbowUtil.equals(this.coeffquadratic, bCRainbowPublicKey.getCoeffQuadratic()) && RainbowUtil.equals(this.coeffsingular, bCRainbowPublicKey.getCoeffSingular()) && RainbowUtil.equals(this.coeffscalar, bCRainbowPublicKey.getCoeffScalar());
    }

    public final String getAlgorithm() {
        return "Rainbow";
    }

    public short[][] getCoeffQuadratic() {
        return this.coeffquadratic;
    }

    public short[] getCoeffScalar() {
        return Arrays.clone(this.coeffscalar);
    }

    public short[][] getCoeffSingular() {
        short[][] arrarrs = new short[this.coeffsingular.length][];
        for (int i = 0; i != this.coeffsingular.length; ++i) {
            arrarrs[i] = Arrays.clone(this.coeffsingular[i]);
        }
        return arrarrs;
    }

    public int getDocLength() {
        return this.docLength;
    }

    public byte[] getEncoded() {
        RainbowPublicKey rainbowPublicKey = new RainbowPublicKey(this.docLength, this.coeffquadratic, this.coeffsingular, this.coeffscalar);
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.rainbow, DERNull.INSTANCE), rainbowPublicKey);
    }

    public String getFormat() {
        return "X.509";
    }

    public int hashCode() {
        return 37 * (37 * (37 * this.docLength + Arrays.hashCode(this.coeffquadratic)) + Arrays.hashCode(this.coeffsingular)) + Arrays.hashCode(this.coeffscalar);
    }
}

