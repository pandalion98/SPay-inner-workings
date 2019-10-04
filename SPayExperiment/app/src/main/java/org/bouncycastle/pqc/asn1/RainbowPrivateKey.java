/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.pqc.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;

public class RainbowPrivateKey
extends ASN1Object {
    private byte[] b1;
    private byte[] b2;
    private byte[][] invA1;
    private byte[][] invA2;
    private Layer[] layers;
    private ASN1ObjectIdentifier oid;
    private ASN1Integer version;
    private byte[] vi;

    /*
     * Enabled aggressive block sorting
     */
    private RainbowPrivateKey(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0));
        } else {
            this.oid = ASN1ObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        }
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(1);
        this.invA1 = new byte[aSN1Sequence2.size()][];
        for (int i = 0; i < aSN1Sequence2.size(); ++i) {
            this.invA1[i] = ((ASN1OctetString)aSN1Sequence2.getObjectAt(i)).getOctets();
        }
        this.b1 = ((ASN1OctetString)((ASN1Sequence)aSN1Sequence.getObjectAt(2)).getObjectAt(0)).getOctets();
        ASN1Sequence aSN1Sequence3 = (ASN1Sequence)aSN1Sequence.getObjectAt(3);
        this.invA2 = new byte[aSN1Sequence3.size()][];
        for (int i = 0; i < aSN1Sequence3.size(); ++i) {
            this.invA2[i] = ((ASN1OctetString)aSN1Sequence3.getObjectAt(i)).getOctets();
        }
        this.b2 = ((ASN1OctetString)((ASN1Sequence)aSN1Sequence.getObjectAt(4)).getObjectAt(0)).getOctets();
        this.vi = ((ASN1OctetString)((ASN1Sequence)aSN1Sequence.getObjectAt(5)).getObjectAt(0)).getOctets();
        ASN1Sequence aSN1Sequence4 = (ASN1Sequence)aSN1Sequence.getObjectAt(6);
        byte[][][][] arrarrby = new byte[aSN1Sequence4.size()][][][];
        byte[][][][] arrarrby2 = new byte[aSN1Sequence4.size()][][][];
        byte[][][] arrarrby3 = new byte[aSN1Sequence4.size()][][];
        byte[][] arrarrby4 = new byte[aSN1Sequence4.size()][];
        int n = 0;
        do {
            ASN1Sequence aSN1Sequence5;
            ASN1Sequence aSN1Sequence6;
            if (n < aSN1Sequence4.size()) {
                aSN1Sequence6 = (ASN1Sequence)aSN1Sequence4.getObjectAt(n);
                aSN1Sequence5 = (ASN1Sequence)aSN1Sequence6.getObjectAt(0);
                arrarrby[n] = new byte[aSN1Sequence5.size()][][];
            } else {
                int n2 = -1 + this.vi.length;
                this.layers = new Layer[n2];
                int n3 = 0;
                do {
                    Layer layer;
                    if (n3 >= n2) {
                        return;
                    }
                    this.layers[n3] = layer = new Layer(this.vi[n3], this.vi[n3 + 1], RainbowUtil.convertArray(arrarrby[n3]), RainbowUtil.convertArray(arrarrby2[n3]), RainbowUtil.convertArray(arrarrby3[n3]), RainbowUtil.convertArray(arrarrby4[n3]));
                    ++n3;
                } while (true);
            }
            for (int i = 0; i < aSN1Sequence5.size(); ++i) {
                ASN1Sequence aSN1Sequence7 = (ASN1Sequence)aSN1Sequence5.getObjectAt(i);
                arrarrby[n][i] = new byte[aSN1Sequence7.size()][];
                for (int j = 0; j < aSN1Sequence7.size(); ++j) {
                    arrarrby[n][i][j] = ((ASN1OctetString)aSN1Sequence7.getObjectAt(j)).getOctets();
                }
            }
            ASN1Sequence aSN1Sequence8 = (ASN1Sequence)aSN1Sequence6.getObjectAt(1);
            arrarrby2[n] = new byte[aSN1Sequence8.size()][][];
            for (int i = 0; i < aSN1Sequence8.size(); ++i) {
                ASN1Sequence aSN1Sequence9 = (ASN1Sequence)aSN1Sequence8.getObjectAt(i);
                arrarrby2[n][i] = new byte[aSN1Sequence9.size()][];
                for (int j = 0; j < aSN1Sequence9.size(); ++j) {
                    arrarrby2[n][i][j] = ((ASN1OctetString)aSN1Sequence9.getObjectAt(j)).getOctets();
                }
            }
            ASN1Sequence aSN1Sequence10 = (ASN1Sequence)aSN1Sequence6.getObjectAt(2);
            arrarrby3[n] = new byte[aSN1Sequence10.size()][];
            for (int i = 0; i < aSN1Sequence10.size(); ++i) {
                arrarrby3[n][i] = ((ASN1OctetString)aSN1Sequence10.getObjectAt(i)).getOctets();
            }
            arrarrby4[n] = ((ASN1OctetString)aSN1Sequence6.getObjectAt(3)).getOctets();
            ++n;
        } while (true);
    }

    public RainbowPrivateKey(short[][] arrs, short[] arrs2, short[][] arrs3, short[] arrs4, int[] arrn, Layer[] arrlayer) {
        this.version = new ASN1Integer(1L);
        this.invA1 = RainbowUtil.convertArray(arrs);
        this.b1 = RainbowUtil.convertArray(arrs2);
        this.invA2 = RainbowUtil.convertArray(arrs3);
        this.b2 = RainbowUtil.convertArray(arrs4);
        this.vi = RainbowUtil.convertIntArray(arrn);
        this.layers = arrlayer;
    }

    public static RainbowPrivateKey getInstance(Object object) {
        if (object instanceof RainbowPrivateKey) {
            return (RainbowPrivateKey)object;
        }
        if (object != null) {
            return new RainbowPrivateKey(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    public short[] getB1() {
        return RainbowUtil.convertArray(this.b1);
    }

    public short[] getB2() {
        return RainbowUtil.convertArray(this.b2);
    }

    public short[][] getInvA1() {
        return RainbowUtil.convertArray(this.invA1);
    }

    public short[][] getInvA2() {
        return RainbowUtil.convertArray(this.invA2);
    }

    public Layer[] getLayers() {
        return this.layers;
    }

    public ASN1Integer getVersion() {
        return this.version;
    }

    public int[] getVi() {
        return RainbowUtil.convertArraytoInt(this.vi);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.version != null) {
            aSN1EncodableVector.add(this.version);
        } else {
            aSN1EncodableVector.add(this.oid);
        }
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i = 0; i < this.invA1.length; ++i) {
            aSN1EncodableVector2.add(new DEROctetString(this.invA1[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector3.add(new DEROctetString(this.b1));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector3));
        ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
        for (int i = 0; i < this.invA2.length; ++i) {
            aSN1EncodableVector4.add(new DEROctetString(this.invA2[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector4));
        ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
        aSN1EncodableVector5.add(new DEROctetString(this.b2));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector5));
        ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector6.add(new DEROctetString(this.vi));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector6));
        ASN1EncodableVector aSN1EncodableVector7 = new ASN1EncodableVector();
        int n = 0;
        do {
            if (n >= this.layers.length) {
                aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector7));
                return new DERSequence(aSN1EncodableVector);
            }
            ASN1EncodableVector aSN1EncodableVector8 = new ASN1EncodableVector();
            byte[][][] arrby = RainbowUtil.convertArray(this.layers[n].getCoeffAlpha());
            ASN1EncodableVector aSN1EncodableVector9 = new ASN1EncodableVector();
            for (int i = 0; i < arrby.length; ++i) {
                ASN1EncodableVector aSN1EncodableVector10 = new ASN1EncodableVector();
                for (int j = 0; j < arrby[i].length; ++j) {
                    aSN1EncodableVector10.add(new DEROctetString(arrby[i][j]));
                }
                aSN1EncodableVector9.add(new DERSequence(aSN1EncodableVector10));
            }
            aSN1EncodableVector8.add(new DERSequence(aSN1EncodableVector9));
            byte[][][] arrby2 = RainbowUtil.convertArray(this.layers[n].getCoeffBeta());
            ASN1EncodableVector aSN1EncodableVector11 = new ASN1EncodableVector();
            for (int i = 0; i < arrby2.length; ++i) {
                ASN1EncodableVector aSN1EncodableVector12 = new ASN1EncodableVector();
                for (int j = 0; j < arrby2[i].length; ++j) {
                    aSN1EncodableVector12.add(new DEROctetString(arrby2[i][j]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
            }
            aSN1EncodableVector8.add(new DERSequence(aSN1EncodableVector11));
            byte[][] arrby3 = RainbowUtil.convertArray(this.layers[n].getCoeffGamma());
            ASN1EncodableVector aSN1EncodableVector13 = new ASN1EncodableVector();
            for (int i = 0; i < arrby3.length; ++i) {
                aSN1EncodableVector13.add(new DEROctetString(arrby3[i]));
            }
            aSN1EncodableVector8.add(new DERSequence(aSN1EncodableVector13));
            aSN1EncodableVector8.add(new DEROctetString(RainbowUtil.convertArray(this.layers[n].getCoeffEta())));
            aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector8));
            ++n;
        } while (true);
    }
}

