/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.PrivateKey
 *  java.util.Arrays
 */
package org.bouncycastle.pqc.jcajce.provider.rainbow;

import java.io.IOException;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
import org.bouncycastle.pqc.asn1.RainbowPrivateKey;
import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;
import org.bouncycastle.pqc.jcajce.spec.RainbowPrivateKeySpec;
import org.bouncycastle.util.Arrays;

public class BCRainbowPrivateKey
implements PrivateKey {
    private static final long serialVersionUID = 1L;
    private short[][] A1inv;
    private short[][] A2inv;
    private short[] b1;
    private short[] b2;
    private Layer[] layers;
    private int[] vi;

    public BCRainbowPrivateKey(RainbowPrivateKeyParameters rainbowPrivateKeyParameters) {
        this(rainbowPrivateKeyParameters.getInvA1(), rainbowPrivateKeyParameters.getB1(), rainbowPrivateKeyParameters.getInvA2(), rainbowPrivateKeyParameters.getB2(), rainbowPrivateKeyParameters.getVi(), rainbowPrivateKeyParameters.getLayers());
    }

    public BCRainbowPrivateKey(RainbowPrivateKeySpec rainbowPrivateKeySpec) {
        this(rainbowPrivateKeySpec.getInvA1(), rainbowPrivateKeySpec.getB1(), rainbowPrivateKeySpec.getInvA2(), rainbowPrivateKeySpec.getB2(), rainbowPrivateKeySpec.getVi(), rainbowPrivateKeySpec.getLayers());
    }

    public BCRainbowPrivateKey(short[][] arrs, short[] arrs2, short[][] arrs3, short[] arrs4, int[] arrn, Layer[] arrlayer) {
        this.A1inv = arrs;
        this.b1 = arrs2;
        this.A2inv = arrs3;
        this.b2 = arrs4;
        this.vi = arrn;
        this.layers = arrlayer;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        boolean bl = true;
        boolean bl2 = false;
        if (object != null) {
            boolean bl3 = object instanceof BCRainbowPrivateKey;
            bl2 = false;
            if (bl3) {
                BCRainbowPrivateKey bCRainbowPrivateKey = (BCRainbowPrivateKey)object;
                boolean bl4 = RainbowUtil.equals(this.A1inv, bCRainbowPrivateKey.getInvA1()) ? bl : false;
                boolean bl5 = bl4 && RainbowUtil.equals(this.A2inv, bCRainbowPrivateKey.getInvA2()) ? bl : false;
                boolean bl6 = bl5 && RainbowUtil.equals(this.b1, bCRainbowPrivateKey.getB1()) ? bl : false;
                boolean bl7 = bl6 && RainbowUtil.equals(this.b2, bCRainbowPrivateKey.getB2()) ? bl : false;
                if (!bl7 || !java.util.Arrays.equals((int[])this.vi, (int[])bCRainbowPrivateKey.getVi())) {
                    bl = false;
                }
                int n = this.layers.length;
                int n2 = bCRainbowPrivateKey.getLayers().length;
                bl2 = false;
                if (n == n2) {
                    int n3 = -1 + this.layers.length;
                    bl2 = bl;
                    for (int i = n3; i >= 0; bl2 &= this.layers[i].equals((Object)bCRainbowPrivateKey.getLayers()[i]), --i) {
                    }
                }
            }
        }
        return bl2;
    }

    public final String getAlgorithm() {
        return "Rainbow";
    }

    public short[] getB1() {
        return this.b1;
    }

    public short[] getB2() {
        return this.b2;
    }

    public byte[] getEncoded() {
        PrivateKeyInfo privateKeyInfo;
        RainbowPrivateKey rainbowPrivateKey = new RainbowPrivateKey(this.A1inv, this.b1, this.A2inv, this.b2, this.vi, this.layers);
        try {
            privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(PQCObjectIdentifiers.rainbow, DERNull.INSTANCE), rainbowPrivateKey);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
        try {
            byte[] arrby = privateKeyInfo.getEncoded();
            return arrby;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    public String getFormat() {
        return "PKCS#8";
    }

    public short[][] getInvA1() {
        return this.A1inv;
    }

    public short[][] getInvA2() {
        return this.A2inv;
    }

    public Layer[] getLayers() {
        return this.layers;
    }

    public int[] getVi() {
        return this.vi;
    }

    public int hashCode() {
        int n = 37 * (37 * (37 * (37 * (37 * this.layers.length + Arrays.hashCode(this.A1inv)) + Arrays.hashCode(this.b1)) + Arrays.hashCode(this.A2inv)) + Arrays.hashCode(this.b2)) + Arrays.hashCode(this.vi);
        for (int i = -1 + this.layers.length; i >= 0; --i) {
            n = n * 37 + this.layers[i].hashCode();
        }
        return n;
    }
}

