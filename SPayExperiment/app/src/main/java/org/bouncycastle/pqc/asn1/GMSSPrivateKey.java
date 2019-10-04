/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Vector
 */
package org.bouncycastle.pqc.asn1;

import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.pqc.crypto.gmss.GMSSLeaf;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootCalc;
import org.bouncycastle.pqc.crypto.gmss.GMSSRootSig;
import org.bouncycastle.pqc.crypto.gmss.Treehash;

public class GMSSPrivateKey
extends ASN1Object {
    private ASN1Primitive primitive;

    private GMSSPrivateKey(ASN1Sequence aSN1Sequence) {
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence)aSN1Sequence.getObjectAt(0);
        int[] arrn = new int[aSN1Sequence2.size()];
        for (int i = 0; i < aSN1Sequence2.size(); ++i) {
            arrn[i] = GMSSPrivateKey.checkBigIntegerInIntRange(aSN1Sequence2.getObjectAt(i));
        }
        ASN1Sequence aSN1Sequence3 = (ASN1Sequence)aSN1Sequence.getObjectAt(1);
        byte[][] arrarrby = new byte[aSN1Sequence3.size()][];
        for (int i = 0; i < arrarrby.length; ++i) {
            arrarrby[i] = ((DEROctetString)aSN1Sequence3.getObjectAt(i)).getOctets();
        }
        ASN1Sequence aSN1Sequence4 = (ASN1Sequence)aSN1Sequence.getObjectAt(2);
        byte[][] arrarrby2 = new byte[aSN1Sequence4.size()][];
        for (int i = 0; i < arrarrby2.length; ++i) {
            arrarrby2[i] = ((DEROctetString)aSN1Sequence4.getObjectAt(i)).getOctets();
        }
        ASN1Sequence aSN1Sequence5 = (ASN1Sequence)aSN1Sequence.getObjectAt(3);
        byte[][][] arrarrby3 = new byte[aSN1Sequence5.size()][][];
        for (int i = 0; i < arrarrby3.length; ++i) {
            ASN1Sequence aSN1Sequence6 = (ASN1Sequence)aSN1Sequence5.getObjectAt(i);
            arrarrby3[i] = new byte[aSN1Sequence6.size()][];
            for (int j = 0; j < arrarrby3[i].length; ++j) {
                arrarrby3[i][j] = ((DEROctetString)aSN1Sequence6.getObjectAt(j)).getOctets();
            }
        }
        ASN1Sequence aSN1Sequence7 = (ASN1Sequence)aSN1Sequence.getObjectAt(4);
        byte[][][] arrarrby4 = new byte[aSN1Sequence7.size()][][];
        for (int i = 0; i < arrarrby4.length; ++i) {
            ASN1Sequence aSN1Sequence8 = (ASN1Sequence)aSN1Sequence7.getObjectAt(i);
            arrarrby4[i] = new byte[aSN1Sequence8.size()][];
            for (int j = 0; j < arrarrby4[i].length; ++j) {
                arrarrby4[i][j] = ((DEROctetString)aSN1Sequence8.getObjectAt(j)).getOctets();
            }
        }
        new Treehash[((ASN1Sequence)aSN1Sequence.getObjectAt(5)).size()][];
    }

    public GMSSPrivateKey(int[] arrn, byte[][] arrby, byte[][] arrby2, byte[][][] arrby3, byte[][][] arrby4, Treehash[][] arrtreehash, Treehash[][] arrtreehash2, Vector[] arrvector, Vector[] arrvector2, Vector[][] arrvector3, Vector[][] arrvector4, byte[][][] arrby5, GMSSLeaf[] arrgMSSLeaf, GMSSLeaf[] arrgMSSLeaf2, GMSSLeaf[] arrgMSSLeaf3, int[] arrn2, byte[][] arrby6, GMSSRootCalc[] arrgMSSRootCalc, byte[][] arrby7, GMSSRootSig[] arrgMSSRootSig, GMSSParameters gMSSParameters, AlgorithmIdentifier algorithmIdentifier) {
        this.primitive = this.encode(arrn, arrby, arrby2, arrby3, arrby4, arrby5, arrtreehash, arrtreehash2, arrvector, arrvector2, arrvector3, arrvector4, arrgMSSLeaf, arrgMSSLeaf2, arrgMSSLeaf3, arrn2, arrby6, arrgMSSRootCalc, arrby7, arrgMSSRootSig, gMSSParameters, new AlgorithmIdentifier[]{algorithmIdentifier});
    }

    private static int checkBigIntegerInIntRange(ASN1Encodable aSN1Encodable) {
        BigInteger bigInteger = ((ASN1Integer)aSN1Encodable).getValue();
        if (bigInteger.compareTo(BigInteger.valueOf((long)Integer.MAX_VALUE)) > 0 || bigInteger.compareTo(BigInteger.valueOf((long)Integer.MIN_VALUE)) < 0) {
            throw new IllegalArgumentException("BigInteger not in Range: " + bigInteger.toString());
        }
        return bigInteger.intValue();
    }

    private ASN1Primitive encode(int[] arrn, byte[][] arrby, byte[][] arrby2, byte[][][] arrby3, byte[][][] arrby4, byte[][][] arrby5, Treehash[][] arrtreehash, Treehash[][] arrtreehash2, Vector[] arrvector, Vector[] arrvector2, Vector[][] arrvector3, Vector[][] arrvector4, GMSSLeaf[] arrgMSSLeaf, GMSSLeaf[] arrgMSSLeaf2, GMSSLeaf[] arrgMSSLeaf3, int[] arrn2, byte[][] arrby6, GMSSRootCalc[] arrgMSSRootCalc, byte[][] arrby7, GMSSRootSig[] arrgMSSRootSig, GMSSParameters gMSSParameters, AlgorithmIdentifier[] arralgorithmIdentifier) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        for (int i = 0; i < arrn.length; ++i) {
            aSN1EncodableVector2.add(new ASN1Integer(arrn[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
        for (int i = 0; i < arrby.length; ++i) {
            aSN1EncodableVector3.add(new DEROctetString(arrby[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector3));
        ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
        for (int i = 0; i < arrby2.length; ++i) {
            aSN1EncodableVector4.add(new DEROctetString(arrby2[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector4));
        ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
        for (int i = 0; i < arrby3.length; ++i) {
            for (int j = 0; j < arrby3[i].length; ++j) {
                aSN1EncodableVector5.add(new DEROctetString(arrby3[i][j]));
            }
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector5));
            aSN1EncodableVector5 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector6));
        ASN1EncodableVector aSN1EncodableVector7 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector8 = new ASN1EncodableVector();
        for (int i = 0; i < arrby4.length; ++i) {
            for (int j = 0; j < arrby4[i].length; ++j) {
                aSN1EncodableVector7.add(new DEROctetString(arrby4[i][j]));
            }
            aSN1EncodableVector8.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector8));
        ASN1EncodableVector aSN1EncodableVector9 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector10 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector11 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector12 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector13 = new ASN1EncodableVector();
        for (int i = 0; i < arrtreehash.length; ++i) {
            for (int j = 0; j < arrtreehash[i].length; ++j) {
                aSN1EncodableVector11.add(new DERSequence(arralgorithmIdentifier[0]));
                int n = arrtreehash[i][j].getStatInt()[1];
                aSN1EncodableVector12.add(new DEROctetString(arrtreehash[i][j].getStatByte()[0]));
                aSN1EncodableVector12.add(new DEROctetString(arrtreehash[i][j].getStatByte()[1]));
                aSN1EncodableVector12.add(new DEROctetString(arrtreehash[i][j].getStatByte()[2]));
                for (int k = 0; k < n; ++k) {
                    aSN1EncodableVector12.add(new DEROctetString(arrtreehash[i][j].getStatByte()[k + 3]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector12));
                ASN1EncodableVector aSN1EncodableVector14 = new ASN1EncodableVector();
                aSN1EncodableVector13.add(new ASN1Integer(arrtreehash[i][j].getStatInt()[0]));
                aSN1EncodableVector13.add(new ASN1Integer(n));
                aSN1EncodableVector13.add(new ASN1Integer(arrtreehash[i][j].getStatInt()[2]));
                aSN1EncodableVector13.add(new ASN1Integer(arrtreehash[i][j].getStatInt()[3]));
                aSN1EncodableVector13.add(new ASN1Integer(arrtreehash[i][j].getStatInt()[4]));
                aSN1EncodableVector13.add(new ASN1Integer(arrtreehash[i][j].getStatInt()[5]));
                for (int k = 0; k < n; ++k) {
                    aSN1EncodableVector13.add(new ASN1Integer(arrtreehash[i][j].getStatInt()[k + 6]));
                }
                aSN1EncodableVector11.add(new DERSequence(aSN1EncodableVector13));
                ASN1EncodableVector aSN1EncodableVector15 = new ASN1EncodableVector();
                aSN1EncodableVector10.add(new DERSequence(aSN1EncodableVector11));
                aSN1EncodableVector11 = new ASN1EncodableVector();
                aSN1EncodableVector13 = aSN1EncodableVector15;
                aSN1EncodableVector12 = aSN1EncodableVector14;
            }
            aSN1EncodableVector9.add(new DERSequence(aSN1EncodableVector10));
            aSN1EncodableVector10 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector9));
        ASN1EncodableVector aSN1EncodableVector16 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector17 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector18 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector19 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector20 = new ASN1EncodableVector();
        for (int i = 0; i < arrtreehash2.length; ++i) {
            for (int j = 0; j < arrtreehash2[i].length; ++j) {
                aSN1EncodableVector18.add(new DERSequence(arralgorithmIdentifier[0]));
                int n = arrtreehash2[i][j].getStatInt()[1];
                aSN1EncodableVector19.add(new DEROctetString(arrtreehash2[i][j].getStatByte()[0]));
                aSN1EncodableVector19.add(new DEROctetString(arrtreehash2[i][j].getStatByte()[1]));
                aSN1EncodableVector19.add(new DEROctetString(arrtreehash2[i][j].getStatByte()[2]));
                for (int k = 0; k < n; ++k) {
                    aSN1EncodableVector19.add(new DEROctetString(arrtreehash2[i][j].getStatByte()[k + 3]));
                }
                aSN1EncodableVector18.add(new DERSequence(aSN1EncodableVector19));
                ASN1EncodableVector aSN1EncodableVector21 = new ASN1EncodableVector();
                aSN1EncodableVector20.add(new ASN1Integer(arrtreehash2[i][j].getStatInt()[0]));
                aSN1EncodableVector20.add(new ASN1Integer(n));
                aSN1EncodableVector20.add(new ASN1Integer(arrtreehash2[i][j].getStatInt()[2]));
                aSN1EncodableVector20.add(new ASN1Integer(arrtreehash2[i][j].getStatInt()[3]));
                aSN1EncodableVector20.add(new ASN1Integer(arrtreehash2[i][j].getStatInt()[4]));
                aSN1EncodableVector20.add(new ASN1Integer(arrtreehash2[i][j].getStatInt()[5]));
                for (int k = 0; k < n; ++k) {
                    aSN1EncodableVector20.add(new ASN1Integer(arrtreehash2[i][j].getStatInt()[k + 6]));
                }
                aSN1EncodableVector18.add(new DERSequence(aSN1EncodableVector20));
                ASN1EncodableVector aSN1EncodableVector22 = new ASN1EncodableVector();
                aSN1EncodableVector17.add(new DERSequence(aSN1EncodableVector18));
                aSN1EncodableVector18 = new ASN1EncodableVector();
                aSN1EncodableVector20 = aSN1EncodableVector22;
                aSN1EncodableVector19 = aSN1EncodableVector21;
            }
            aSN1EncodableVector16.add(new DERSequence(new DERSequence(aSN1EncodableVector17)));
            aSN1EncodableVector17 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector16));
        ASN1EncodableVector aSN1EncodableVector23 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector24 = new ASN1EncodableVector();
        for (int i = 0; i < arrby5.length; ++i) {
            for (int j = 0; j < arrby5[i].length; ++j) {
                aSN1EncodableVector23.add(new DEROctetString(arrby5[i][j]));
            }
            aSN1EncodableVector24.add(new DERSequence(aSN1EncodableVector23));
            aSN1EncodableVector23 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector24));
        ASN1EncodableVector aSN1EncodableVector25 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector26 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector27 = aSN1EncodableVector25;
        for (int i = 0; i < arrvector.length; ++i) {
            for (int j = 0; j < arrvector[i].size(); ++j) {
                aSN1EncodableVector27.add(new DEROctetString((byte[])arrvector[i].elementAt(j)));
            }
            aSN1EncodableVector26.add(new DERSequence(aSN1EncodableVector27));
            aSN1EncodableVector27 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector26));
        ASN1EncodableVector aSN1EncodableVector28 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector29 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector30 = aSN1EncodableVector28;
        for (int i = 0; i < arrvector2.length; ++i) {
            for (int j = 0; j < arrvector2[i].size(); ++j) {
                aSN1EncodableVector30.add(new DEROctetString((byte[])arrvector2[i].elementAt(j)));
            }
            aSN1EncodableVector29.add(new DERSequence(aSN1EncodableVector30));
            aSN1EncodableVector30 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector29));
        ASN1EncodableVector aSN1EncodableVector31 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector32 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector33 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector34 = aSN1EncodableVector31;
        ASN1EncodableVector aSN1EncodableVector35 = aSN1EncodableVector32;
        for (int i = 0; i < arrvector3.length; ++i) {
            ASN1EncodableVector aSN1EncodableVector36 = aSN1EncodableVector34;
            for (int j = 0; j < arrvector3[i].length; ++j) {
                for (int k = 0; k < arrvector3[i][j].size(); ++k) {
                    aSN1EncodableVector36.add(new DEROctetString((byte[])arrvector3[i][j].elementAt(k)));
                }
                aSN1EncodableVector35.add(new DERSequence(aSN1EncodableVector36));
                aSN1EncodableVector36 = new ASN1EncodableVector();
            }
            aSN1EncodableVector33.add(new DERSequence(aSN1EncodableVector35));
            aSN1EncodableVector35 = new ASN1EncodableVector();
            aSN1EncodableVector34 = aSN1EncodableVector36;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector33));
        ASN1EncodableVector aSN1EncodableVector37 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector38 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector39 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector40 = aSN1EncodableVector37;
        ASN1EncodableVector aSN1EncodableVector41 = aSN1EncodableVector38;
        for (int i = 0; i < arrvector4.length; ++i) {
            ASN1EncodableVector aSN1EncodableVector42 = aSN1EncodableVector40;
            for (int j = 0; j < arrvector4[i].length; ++j) {
                for (int k = 0; k < arrvector4[i][j].size(); ++k) {
                    aSN1EncodableVector42.add(new DEROctetString((byte[])arrvector4[i][j].elementAt(k)));
                }
                aSN1EncodableVector41.add(new DERSequence(aSN1EncodableVector42));
                aSN1EncodableVector42 = new ASN1EncodableVector();
            }
            aSN1EncodableVector39.add(new DERSequence(aSN1EncodableVector41));
            aSN1EncodableVector41 = new ASN1EncodableVector();
            aSN1EncodableVector40 = aSN1EncodableVector42;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector39));
        ASN1EncodableVector aSN1EncodableVector43 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector44 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector45 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector46 = new ASN1EncodableVector();
        for (int i = 0; i < arrgMSSLeaf.length; ++i) {
            aSN1EncodableVector44.add(new DERSequence(arralgorithmIdentifier[0]));
            byte[][] arrby8 = arrgMSSLeaf[i].getStatByte();
            aSN1EncodableVector45.add(new DEROctetString(arrby8[0]));
            aSN1EncodableVector45.add(new DEROctetString(arrby8[1]));
            aSN1EncodableVector45.add(new DEROctetString(arrby8[2]));
            aSN1EncodableVector45.add(new DEROctetString(arrby8[3]));
            aSN1EncodableVector44.add(new DERSequence(aSN1EncodableVector45));
            aSN1EncodableVector45 = new ASN1EncodableVector();
            int[] arrn3 = arrgMSSLeaf[i].getStatInt();
            aSN1EncodableVector46.add(new ASN1Integer(arrn3[0]));
            aSN1EncodableVector46.add(new ASN1Integer(arrn3[1]));
            aSN1EncodableVector46.add(new ASN1Integer(arrn3[2]));
            aSN1EncodableVector46.add(new ASN1Integer(arrn3[3]));
            aSN1EncodableVector44.add(new DERSequence(aSN1EncodableVector46));
            aSN1EncodableVector46 = new ASN1EncodableVector();
            aSN1EncodableVector43.add(new DERSequence(aSN1EncodableVector44));
            aSN1EncodableVector44 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector43));
        ASN1EncodableVector aSN1EncodableVector47 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector48 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector49 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector50 = new ASN1EncodableVector();
        for (int i = 0; i < arrgMSSLeaf2.length; ++i) {
            aSN1EncodableVector48.add(new DERSequence(arralgorithmIdentifier[0]));
            byte[][] arrby9 = arrgMSSLeaf2[i].getStatByte();
            aSN1EncodableVector49.add(new DEROctetString(arrby9[0]));
            aSN1EncodableVector49.add(new DEROctetString(arrby9[1]));
            aSN1EncodableVector49.add(new DEROctetString(arrby9[2]));
            aSN1EncodableVector49.add(new DEROctetString(arrby9[3]));
            aSN1EncodableVector48.add(new DERSequence(aSN1EncodableVector49));
            aSN1EncodableVector49 = new ASN1EncodableVector();
            int[] arrn4 = arrgMSSLeaf2[i].getStatInt();
            aSN1EncodableVector50.add(new ASN1Integer(arrn4[0]));
            aSN1EncodableVector50.add(new ASN1Integer(arrn4[1]));
            aSN1EncodableVector50.add(new ASN1Integer(arrn4[2]));
            aSN1EncodableVector50.add(new ASN1Integer(arrn4[3]));
            aSN1EncodableVector48.add(new DERSequence(aSN1EncodableVector50));
            aSN1EncodableVector50 = new ASN1EncodableVector();
            aSN1EncodableVector47.add(new DERSequence(aSN1EncodableVector48));
            aSN1EncodableVector48 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector47));
        ASN1EncodableVector aSN1EncodableVector51 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector52 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector53 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector54 = new ASN1EncodableVector();
        for (int i = 0; i < arrgMSSLeaf3.length; ++i) {
            aSN1EncodableVector52.add(new DERSequence(arralgorithmIdentifier[0]));
            byte[][] arrby10 = arrgMSSLeaf3[i].getStatByte();
            aSN1EncodableVector53.add(new DEROctetString(arrby10[0]));
            aSN1EncodableVector53.add(new DEROctetString(arrby10[1]));
            aSN1EncodableVector53.add(new DEROctetString(arrby10[2]));
            aSN1EncodableVector53.add(new DEROctetString(arrby10[3]));
            aSN1EncodableVector52.add(new DERSequence(aSN1EncodableVector53));
            aSN1EncodableVector53 = new ASN1EncodableVector();
            int[] arrn5 = arrgMSSLeaf3[i].getStatInt();
            aSN1EncodableVector54.add(new ASN1Integer(arrn5[0]));
            aSN1EncodableVector54.add(new ASN1Integer(arrn5[1]));
            aSN1EncodableVector54.add(new ASN1Integer(arrn5[2]));
            aSN1EncodableVector54.add(new ASN1Integer(arrn5[3]));
            aSN1EncodableVector52.add(new DERSequence(aSN1EncodableVector54));
            aSN1EncodableVector54 = new ASN1EncodableVector();
            aSN1EncodableVector51.add(new DERSequence(aSN1EncodableVector52));
            aSN1EncodableVector52 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector51));
        ASN1EncodableVector aSN1EncodableVector55 = new ASN1EncodableVector();
        for (int i = 0; i < arrn2.length; ++i) {
            aSN1EncodableVector55.add(new ASN1Integer(arrn2[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector55));
        ASN1EncodableVector aSN1EncodableVector56 = new ASN1EncodableVector();
        for (int i = 0; i < arrby6.length; ++i) {
            aSN1EncodableVector56.add(new DEROctetString(arrby6[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector56));
        ASN1EncodableVector aSN1EncodableVector57 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector58 = new ASN1EncodableVector();
        new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector59 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector60 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector61 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector62 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector63 = aSN1EncodableVector58;
        ASN1EncodableVector aSN1EncodableVector64 = aSN1EncodableVector61;
        ASN1EncodableVector aSN1EncodableVector65 = aSN1EncodableVector62;
        for (int i = 0; i < arrgMSSRootCalc.length; ++i) {
            aSN1EncodableVector63.add(new DERSequence(arralgorithmIdentifier[0]));
            new ASN1EncodableVector();
            int n = arrgMSSRootCalc[i].getStatInt()[0];
            int n2 = arrgMSSRootCalc[i].getStatInt()[7];
            aSN1EncodableVector59.add(new DEROctetString(arrgMSSRootCalc[i].getStatByte()[0]));
            for (int j = 0; j < n; ++j) {
                aSN1EncodableVector59.add(new DEROctetString(arrgMSSRootCalc[i].getStatByte()[j + 1]));
            }
            for (int j = 0; j < n2; ++j) {
                aSN1EncodableVector59.add(new DEROctetString(arrgMSSRootCalc[i].getStatByte()[j + (n + 1)]));
            }
            aSN1EncodableVector63.add(new DERSequence(aSN1EncodableVector59));
            ASN1EncodableVector aSN1EncodableVector66 = new ASN1EncodableVector();
            aSN1EncodableVector60.add(new ASN1Integer(n));
            aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[1]));
            aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[2]));
            aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[3]));
            aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[4]));
            aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[5]));
            aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[6]));
            aSN1EncodableVector60.add(new ASN1Integer(n2));
            for (int j = 0; j < n; ++j) {
                aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[j + 8]));
            }
            for (int j = 0; j < n2; ++j) {
                aSN1EncodableVector60.add(new ASN1Integer(arrgMSSRootCalc[i].getStatInt()[j + (n + 8)]));
            }
            aSN1EncodableVector63.add(new DERSequence(aSN1EncodableVector60));
            ASN1EncodableVector aSN1EncodableVector67 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector68 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector69 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector70 = new ASN1EncodableVector();
            if (arrgMSSRootCalc[i].getTreehash() != null) {
                for (int j = 0; j < arrgMSSRootCalc[i].getTreehash().length; ++j) {
                    aSN1EncodableVector68.add(new DERSequence(arralgorithmIdentifier[0]));
                    int n3 = arrgMSSRootCalc[i].getTreehash()[j].getStatInt()[1];
                    aSN1EncodableVector69.add(new DEROctetString(arrgMSSRootCalc[i].getTreehash()[j].getStatByte()[0]));
                    aSN1EncodableVector69.add(new DEROctetString(arrgMSSRootCalc[i].getTreehash()[j].getStatByte()[1]));
                    aSN1EncodableVector69.add(new DEROctetString(arrgMSSRootCalc[i].getTreehash()[j].getStatByte()[2]));
                    for (int k = 0; k < n3; ++k) {
                        aSN1EncodableVector69.add(new DEROctetString(arrgMSSRootCalc[i].getTreehash()[j].getStatByte()[k + 3]));
                    }
                    aSN1EncodableVector68.add(new DERSequence(aSN1EncodableVector69));
                    aSN1EncodableVector69 = new ASN1EncodableVector();
                    aSN1EncodableVector70.add(new ASN1Integer(arrgMSSRootCalc[i].getTreehash()[j].getStatInt()[0]));
                    aSN1EncodableVector70.add(new ASN1Integer(n3));
                    aSN1EncodableVector70.add(new ASN1Integer(arrgMSSRootCalc[i].getTreehash()[j].getStatInt()[2]));
                    aSN1EncodableVector70.add(new ASN1Integer(arrgMSSRootCalc[i].getTreehash()[j].getStatInt()[3]));
                    aSN1EncodableVector70.add(new ASN1Integer(arrgMSSRootCalc[i].getTreehash()[j].getStatInt()[4]));
                    aSN1EncodableVector70.add(new ASN1Integer(arrgMSSRootCalc[i].getTreehash()[j].getStatInt()[5]));
                    for (int k = 0; k < n3; ++k) {
                        ASN1Integer aSN1Integer = new ASN1Integer(arrgMSSRootCalc[i].getTreehash()[j].getStatInt()[k + 6]);
                        aSN1EncodableVector70.add(aSN1Integer);
                    }
                    aSN1EncodableVector68.add(new DERSequence(aSN1EncodableVector70));
                    aSN1EncodableVector70 = new ASN1EncodableVector();
                    aSN1EncodableVector64.add(new DERSequence(aSN1EncodableVector68));
                    aSN1EncodableVector68 = new ASN1EncodableVector();
                }
            }
            aSN1EncodableVector63.add(new DERSequence(aSN1EncodableVector64));
            ASN1EncodableVector aSN1EncodableVector71 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector72 = new ASN1EncodableVector();
            if (arrgMSSRootCalc[i].getRetain() != null) {
                ASN1EncodableVector aSN1EncodableVector73 = aSN1EncodableVector72;
                for (int j = 0; j < arrgMSSRootCalc[i].getRetain().length; ++j) {
                    for (int k = 0; k < arrgMSSRootCalc[i].getRetain()[j].size(); ++k) {
                        aSN1EncodableVector73.add(new DEROctetString((byte[])arrgMSSRootCalc[i].getRetain()[j].elementAt(k)));
                    }
                    aSN1EncodableVector65.add(new DERSequence(aSN1EncodableVector73));
                    aSN1EncodableVector73 = new ASN1EncodableVector();
                }
            }
            aSN1EncodableVector63.add(new DERSequence(aSN1EncodableVector65));
            aSN1EncodableVector65 = new ASN1EncodableVector();
            aSN1EncodableVector57.add(new DERSequence(aSN1EncodableVector63));
            aSN1EncodableVector63 = new ASN1EncodableVector();
            aSN1EncodableVector64 = aSN1EncodableVector71;
            aSN1EncodableVector60 = aSN1EncodableVector67;
            aSN1EncodableVector59 = aSN1EncodableVector66;
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector57));
        ASN1EncodableVector aSN1EncodableVector74 = new ASN1EncodableVector();
        for (int i = 0; i < arrby7.length; ++i) {
            aSN1EncodableVector74.add(new DEROctetString(arrby7[i]));
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector74));
        ASN1EncodableVector aSN1EncodableVector75 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector76 = new ASN1EncodableVector();
        new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector77 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector78 = new ASN1EncodableVector();
        for (int i = 0; i < arrgMSSRootSig.length; ++i) {
            aSN1EncodableVector76.add(new DERSequence(arralgorithmIdentifier[0]));
            new ASN1EncodableVector();
            aSN1EncodableVector77.add(new DEROctetString(arrgMSSRootSig[i].getStatByte()[0]));
            aSN1EncodableVector77.add(new DEROctetString(arrgMSSRootSig[i].getStatByte()[1]));
            aSN1EncodableVector77.add(new DEROctetString(arrgMSSRootSig[i].getStatByte()[2]));
            aSN1EncodableVector77.add(new DEROctetString(arrgMSSRootSig[i].getStatByte()[3]));
            aSN1EncodableVector77.add(new DEROctetString(arrgMSSRootSig[i].getStatByte()[4]));
            aSN1EncodableVector76.add(new DERSequence(aSN1EncodableVector77));
            aSN1EncodableVector77 = new ASN1EncodableVector();
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[0]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[1]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[2]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[3]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[4]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[5]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[6]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[7]));
            aSN1EncodableVector78.add(new ASN1Integer(arrgMSSRootSig[i].getStatInt()[8]));
            aSN1EncodableVector76.add(new DERSequence(aSN1EncodableVector78));
            aSN1EncodableVector78 = new ASN1EncodableVector();
            aSN1EncodableVector75.add(new DERSequence(aSN1EncodableVector76));
            aSN1EncodableVector76 = new ASN1EncodableVector();
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector75));
        ASN1EncodableVector aSN1EncodableVector79 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector80 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector81 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector82 = new ASN1EncodableVector();
        for (int i = 0; i < gMSSParameters.getHeightOfTrees().length; ++i) {
            aSN1EncodableVector80.add(new ASN1Integer(gMSSParameters.getHeightOfTrees()[i]));
            aSN1EncodableVector81.add(new ASN1Integer(gMSSParameters.getWinternitzParameter()[i]));
            aSN1EncodableVector82.add(new ASN1Integer(gMSSParameters.getK()[i]));
        }
        aSN1EncodableVector79.add(new ASN1Integer(gMSSParameters.getNumOfLayers()));
        aSN1EncodableVector79.add(new DERSequence(aSN1EncodableVector80));
        aSN1EncodableVector79.add(new DERSequence(aSN1EncodableVector81));
        aSN1EncodableVector79.add(new DERSequence(aSN1EncodableVector82));
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector79));
        ASN1EncodableVector aSN1EncodableVector83 = new ASN1EncodableVector();
        for (int i = 0; i < arralgorithmIdentifier.length; ++i) {
            aSN1EncodableVector83.add(arralgorithmIdentifier[i]);
        }
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector83));
        return new DERSequence(aSN1EncodableVector);
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.primitive;
    }
}

