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

public class GMSSPrivateKey extends ASN1Object {
    private ASN1Primitive primitive;

    private GMSSPrivateKey(ASN1Sequence aSN1Sequence) {
        int i;
        int i2;
        ASN1Sequence aSN1Sequence2 = (ASN1Sequence) aSN1Sequence.getObjectAt(0);
        int[] iArr = new int[aSN1Sequence2.size()];
        for (int i3 = 0; i3 < aSN1Sequence2.size(); i3++) {
            iArr[i3] = checkBigIntegerInIntRange(aSN1Sequence2.getObjectAt(i3));
        }
        aSN1Sequence2 = (ASN1Sequence) aSN1Sequence.getObjectAt(1);
        byte[][] bArr = new byte[aSN1Sequence2.size()][];
        for (i = 0; i < bArr.length; i++) {
            bArr[i] = ((DEROctetString) aSN1Sequence2.getObjectAt(i)).getOctets();
        }
        aSN1Sequence2 = (ASN1Sequence) aSN1Sequence.getObjectAt(2);
        bArr = new byte[aSN1Sequence2.size()][];
        for (i = 0; i < bArr.length; i++) {
            bArr[i] = ((DEROctetString) aSN1Sequence2.getObjectAt(i)).getOctets();
        }
        aSN1Sequence2 = (ASN1Sequence) aSN1Sequence.getObjectAt(3);
        byte[][][] bArr2 = new byte[aSN1Sequence2.size()][][];
        for (i2 = 0; i2 < bArr2.length; i2++) {
            int i4;
            ASN1Sequence aSN1Sequence3 = (ASN1Sequence) aSN1Sequence2.getObjectAt(i2);
            bArr2[i2] = new byte[aSN1Sequence3.size()][];
            for (i4 = 0; i4 < bArr2[i2].length; i4++) {
                bArr2[i2][i4] = ((DEROctetString) aSN1Sequence3.getObjectAt(i4)).getOctets();
            }
        }
        aSN1Sequence2 = (ASN1Sequence) aSN1Sequence.getObjectAt(4);
        bArr2 = new byte[aSN1Sequence2.size()][][];
        for (i2 = 0; i2 < bArr2.length; i2++) {
            aSN1Sequence3 = (ASN1Sequence) aSN1Sequence2.getObjectAt(i2);
            bArr2[i2] = new byte[aSN1Sequence3.size()][];
            for (i4 = 0; i4 < bArr2[i2].length; i4++) {
                bArr2[i2][i4] = ((DEROctetString) aSN1Sequence3.getObjectAt(i4)).getOctets();
            }
        }
        Treehash[][] treehashArr = new Treehash[((ASN1Sequence) aSN1Sequence.getObjectAt(5)).size()][];
    }

    public GMSSPrivateKey(int[] iArr, byte[][] bArr, byte[][] bArr2, byte[][][] bArr3, byte[][][] bArr4, Treehash[][] treehashArr, Treehash[][] treehashArr2, Vector[] vectorArr, Vector[] vectorArr2, Vector[][] vectorArr3, Vector[][] vectorArr4, byte[][][] bArr5, GMSSLeaf[] gMSSLeafArr, GMSSLeaf[] gMSSLeafArr2, GMSSLeaf[] gMSSLeafArr3, int[] iArr2, byte[][] bArr6, GMSSRootCalc[] gMSSRootCalcArr, byte[][] bArr7, GMSSRootSig[] gMSSRootSigArr, GMSSParameters gMSSParameters, AlgorithmIdentifier algorithmIdentifier) {
        this.primitive = encode(iArr, bArr, bArr2, bArr3, bArr4, bArr5, treehashArr, treehashArr2, vectorArr, vectorArr2, vectorArr3, vectorArr4, gMSSLeafArr, gMSSLeafArr2, gMSSLeafArr3, iArr2, bArr6, gMSSRootCalcArr, bArr7, gMSSRootSigArr, gMSSParameters, new AlgorithmIdentifier[]{algorithmIdentifier});
    }

    private static int checkBigIntegerInIntRange(ASN1Encodable aSN1Encodable) {
        BigInteger value = ((ASN1Integer) aSN1Encodable).getValue();
        if (value.compareTo(BigInteger.valueOf(2147483647L)) <= 0 && value.compareTo(BigInteger.valueOf(-2147483648L)) >= 0) {
            return value.intValue();
        }
        throw new IllegalArgumentException("BigInteger not in Range: " + value.toString());
    }

    private ASN1Primitive encode(int[] iArr, byte[][] bArr, byte[][] bArr2, byte[][][] bArr3, byte[][][] bArr4, byte[][][] bArr5, Treehash[][] treehashArr, Treehash[][] treehashArr2, Vector[] vectorArr, Vector[] vectorArr2, Vector[][] vectorArr3, Vector[][] vectorArr4, GMSSLeaf[] gMSSLeafArr, GMSSLeaf[] gMSSLeafArr2, GMSSLeaf[] gMSSLeafArr3, int[] iArr2, byte[][] bArr6, GMSSRootCalc[] gMSSRootCalcArr, byte[][] bArr7, GMSSRootSig[] gMSSRootSigArr, GMSSParameters gMSSParameters, AlgorithmIdentifier[] algorithmIdentifierArr) {
        int i;
        int i2;
        int i3;
        ASN1EncodableVector aSN1EncodableVector;
        int i4;
        int i5;
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
        for (int i22 : iArr) {
            aSN1EncodableVector3.add(new ASN1Integer((long) i22));
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (byte[] dEROctetString : bArr) {
            aSN1EncodableVector3.add(new DEROctetString(dEROctetString));
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (byte[] dEROctetString2 : bArr2) {
            aSN1EncodableVector3.add(new DEROctetString(dEROctetString2));
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
        for (i = 0; i < bArr3.length; i++) {
            for (byte[] dEROctetString3 : bArr3[i]) {
                aSN1EncodableVector3.add(new DEROctetString(dEROctetString3));
            }
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
            aSN1EncodableVector3 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector4));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        for (i = 0; i < bArr4.length; i++) {
            for (byte[] dEROctetString32 : bArr4[i]) {
                aSN1EncodableVector3.add(new DEROctetString(dEROctetString32));
            }
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
            aSN1EncodableVector3 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector4));
        ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (i = 0; i < treehashArr.length; i++) {
            int i6 = 0;
            while (i6 < treehashArr[i].length) {
                int i7;
                aSN1EncodableVector4.add(new DERSequence(algorithmIdentifierArr[0]));
                int i8 = treehashArr[i][i6].getStatInt()[1];
                aSN1EncodableVector7.add(new DEROctetString(treehashArr[i][i6].getStatByte()[0]));
                aSN1EncodableVector7.add(new DEROctetString(treehashArr[i][i6].getStatByte()[1]));
                aSN1EncodableVector7.add(new DEROctetString(treehashArr[i][i6].getStatByte()[2]));
                for (i7 = 0; i7 < i8; i7++) {
                    aSN1EncodableVector7.add(new DEROctetString(treehashArr[i][i6].getStatByte()[i7 + 3]));
                }
                aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector7));
                aSN1EncodableVector = new ASN1EncodableVector();
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr[i][i6].getStatInt()[0]));
                aSN1EncodableVector3.add(new ASN1Integer((long) i8));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr[i][i6].getStatInt()[2]));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr[i][i6].getStatInt()[3]));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr[i][i6].getStatInt()[4]));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr[i][i6].getStatInt()[5]));
                for (i3 = 0; i3 < i8; i3++) {
                    aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr[i][i6].getStatInt()[i3 + 6]));
                }
                aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
                aSN1EncodableVector7 = new ASN1EncodableVector();
                aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
                aSN1EncodableVector4 = new ASN1EncodableVector();
                i6++;
                aSN1EncodableVector3 = aSN1EncodableVector7;
                aSN1EncodableVector7 = aSN1EncodableVector;
            }
            aSN1EncodableVector5.add(new DERSequence(aSN1EncodableVector6));
            aSN1EncodableVector6 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector5));
        aSN1EncodableVector5 = new ASN1EncodableVector();
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (i = 0; i < treehashArr2.length; i++) {
            i6 = 0;
            while (i6 < treehashArr2[i].length) {
                aSN1EncodableVector4.add(new DERSequence(algorithmIdentifierArr[0]));
                i8 = treehashArr2[i][i6].getStatInt()[1];
                aSN1EncodableVector7.add(new DEROctetString(treehashArr2[i][i6].getStatByte()[0]));
                aSN1EncodableVector7.add(new DEROctetString(treehashArr2[i][i6].getStatByte()[1]));
                aSN1EncodableVector7.add(new DEROctetString(treehashArr2[i][i6].getStatByte()[2]));
                for (i7 = 0; i7 < i8; i7++) {
                    aSN1EncodableVector7.add(new DEROctetString(treehashArr2[i][i6].getStatByte()[i7 + 3]));
                }
                aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector7));
                aSN1EncodableVector = new ASN1EncodableVector();
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr2[i][i6].getStatInt()[0]));
                aSN1EncodableVector3.add(new ASN1Integer((long) i8));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr2[i][i6].getStatInt()[2]));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr2[i][i6].getStatInt()[3]));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr2[i][i6].getStatInt()[4]));
                aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr2[i][i6].getStatInt()[5]));
                for (i3 = 0; i3 < i8; i3++) {
                    aSN1EncodableVector3.add(new ASN1Integer((long) treehashArr2[i][i6].getStatInt()[i3 + 6]));
                }
                aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
                aSN1EncodableVector7 = new ASN1EncodableVector();
                aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
                aSN1EncodableVector4 = new ASN1EncodableVector();
                i6++;
                aSN1EncodableVector3 = aSN1EncodableVector7;
                aSN1EncodableVector7 = aSN1EncodableVector;
            }
            aSN1EncodableVector5.add(new DERSequence(new DERSequence(aSN1EncodableVector6)));
            aSN1EncodableVector6 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector5));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        for (i = 0; i < bArr5.length; i++) {
            for (byte[] dEROctetString322 : bArr5[i]) {
                aSN1EncodableVector3.add(new DEROctetString(dEROctetString322));
            }
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
            aSN1EncodableVector3 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector4));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector7 = aSN1EncodableVector3;
        for (i4 = 0; i4 < vectorArr.length; i4++) {
            for (i22 = 0; i22 < vectorArr[i4].size(); i22++) {
                aSN1EncodableVector7.add(new DEROctetString((byte[]) vectorArr[i4].elementAt(i22)));
            }
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector6));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector7 = aSN1EncodableVector3;
        for (i4 = 0; i4 < vectorArr2.length; i4++) {
            for (i22 = 0; i22 < vectorArr2[i4].size(); i22++) {
                aSN1EncodableVector7.add(new DEROctetString((byte[]) vectorArr2[i4].elementAt(i22)));
            }
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector6));
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector8 = aSN1EncodableVector7;
        aSN1EncodableVector7 = aSN1EncodableVector3;
        i4 = 0;
        while (i4 < vectorArr3.length) {
            aSN1EncodableVector4 = aSN1EncodableVector8;
            for (i5 = 0; i5 < vectorArr3[i4].length; i5++) {
                for (i6 = 0; i6 < vectorArr3[i4][i5].size(); i6++) {
                    aSN1EncodableVector4.add(new DEROctetString((byte[]) vectorArr3[i4][i5].elementAt(i6)));
                }
                aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector4));
                aSN1EncodableVector4 = new ASN1EncodableVector();
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
            i4++;
            aSN1EncodableVector8 = aSN1EncodableVector4;
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector));
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector8 = aSN1EncodableVector7;
        aSN1EncodableVector7 = aSN1EncodableVector3;
        i4 = 0;
        while (i4 < vectorArr4.length) {
            aSN1EncodableVector4 = aSN1EncodableVector8;
            for (i5 = 0; i5 < vectorArr4[i4].length; i5++) {
                for (i6 = 0; i6 < vectorArr4[i4][i5].size(); i6++) {
                    aSN1EncodableVector4.add(new DEROctetString((byte[]) vectorArr4[i4][i5].elementAt(i6)));
                }
                aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector4));
                aSN1EncodableVector4 = new ASN1EncodableVector();
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
            i4++;
            aSN1EncodableVector8 = aSN1EncodableVector4;
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector));
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (i = 0; i < gMSSLeafArr.length; i++) {
            aSN1EncodableVector4.add(new DERSequence(algorithmIdentifierArr[0]));
            byte[][] statByte = gMSSLeafArr[i].getStatByte();
            aSN1EncodableVector7.add(new DEROctetString(statByte[0]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[1]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[2]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[3]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
            int[] statInt = gMSSLeafArr[i].getStatInt();
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[0]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[1]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[2]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[3]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
            aSN1EncodableVector3 = new ASN1EncodableVector();
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
            aSN1EncodableVector4 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector6));
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (i = 0; i < gMSSLeafArr2.length; i++) {
            aSN1EncodableVector4.add(new DERSequence(algorithmIdentifierArr[0]));
            statByte = gMSSLeafArr2[i].getStatByte();
            aSN1EncodableVector7.add(new DEROctetString(statByte[0]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[1]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[2]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[3]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
            statInt = gMSSLeafArr2[i].getStatInt();
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[0]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[1]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[2]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[3]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
            aSN1EncodableVector3 = new ASN1EncodableVector();
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
            aSN1EncodableVector4 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector6));
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (i = 0; i < gMSSLeafArr3.length; i++) {
            aSN1EncodableVector4.add(new DERSequence(algorithmIdentifierArr[0]));
            statByte = gMSSLeafArr3[i].getStatByte();
            aSN1EncodableVector7.add(new DEROctetString(statByte[0]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[1]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[2]));
            aSN1EncodableVector7.add(new DEROctetString(statByte[3]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
            statInt = gMSSLeafArr3[i].getStatInt();
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[0]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[1]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[2]));
            aSN1EncodableVector3.add(new ASN1Integer((long) statInt[3]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
            aSN1EncodableVector3 = new ASN1EncodableVector();
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
            aSN1EncodableVector4 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector6));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (int i222 : iArr2) {
            aSN1EncodableVector3.add(new ASN1Integer((long) i222));
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (byte[] dEROctetString22 : bArr6) {
            aSN1EncodableVector3.add(new DEROctetString(dEROctetString22));
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        ASN1EncodableVector aSN1EncodableVector9 = new ASN1EncodableVector();
        ASN1EncodableVector aSN1EncodableVector10 = new ASN1EncodableVector();
        aSN1EncodableVector8 = new ASN1EncodableVector();
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector = aSN1EncodableVector10;
        ASN1EncodableVector aSN1EncodableVector11 = new ASN1EncodableVector();
        i4 = 0;
        aSN1EncodableVector8 = aSN1EncodableVector7;
        aSN1EncodableVector7 = aSN1EncodableVector11;
        while (i4 < gMSSRootCalcArr.length) {
            aSN1EncodableVector.add(new DERSequence(algorithmIdentifierArr[0]));
            aSN1EncodableVector10 = new ASN1EncodableVector();
            int i9 = gMSSRootCalcArr[i4].getStatInt()[0];
            i8 = gMSSRootCalcArr[i4].getStatInt()[7];
            aSN1EncodableVector6.add(new DEROctetString(gMSSRootCalcArr[i4].getStatByte()[0]));
            for (i6 = 0; i6 < i9; i6++) {
                aSN1EncodableVector6.add(new DEROctetString(gMSSRootCalcArr[i4].getStatByte()[i6 + 1]));
            }
            for (i6 = 0; i6 < i8; i6++) {
                aSN1EncodableVector6.add(new DEROctetString(gMSSRootCalcArr[i4].getStatByte()[(i9 + 1) + i6]));
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector6));
            aSN1EncodableVector10 = new ASN1EncodableVector();
            aSN1EncodableVector4.add(new ASN1Integer((long) i9));
            aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[1]));
            aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[2]));
            aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[3]));
            aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[4]));
            aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[5]));
            aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[6]));
            aSN1EncodableVector4.add(new ASN1Integer((long) i8));
            for (i5 = 0; i5 < i9; i5++) {
                aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[i5 + 8]));
            }
            for (i5 = 0; i5 < i8; i5++) {
                aSN1EncodableVector4.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getStatInt()[(i9 + 8) + i5]));
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector4));
            aSN1EncodableVector6 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector12 = new ASN1EncodableVector();
            ASN1EncodableVector aSN1EncodableVector13 = new ASN1EncodableVector();
            aSN1EncodableVector5 = new ASN1EncodableVector();
            if (gMSSRootCalcArr[i4].getTreehash() != null) {
                for (i222 = 0; i222 < gMSSRootCalcArr[i4].getTreehash().length; i222++) {
                    int i10;
                    aSN1EncodableVector12.add(new DERSequence(algorithmIdentifierArr[0]));
                    int i11 = gMSSRootCalcArr[i4].getTreehash()[i222].getStatInt()[1];
                    aSN1EncodableVector13.add(new DEROctetString(gMSSRootCalcArr[i4].getTreehash()[i222].getStatByte()[0]));
                    aSN1EncodableVector13.add(new DEROctetString(gMSSRootCalcArr[i4].getTreehash()[i222].getStatByte()[1]));
                    aSN1EncodableVector13.add(new DEROctetString(gMSSRootCalcArr[i4].getTreehash()[i222].getStatByte()[2]));
                    for (i10 = 0; i10 < i11; i10++) {
                        aSN1EncodableVector13.add(new DEROctetString(gMSSRootCalcArr[i4].getTreehash()[i222].getStatByte()[i10 + 3]));
                    }
                    aSN1EncodableVector12.add(new DERSequence(aSN1EncodableVector13));
                    aSN1EncodableVector13 = new ASN1EncodableVector();
                    aSN1EncodableVector5.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getTreehash()[i222].getStatInt()[0]));
                    aSN1EncodableVector5.add(new ASN1Integer((long) i11));
                    aSN1EncodableVector5.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getTreehash()[i222].getStatInt()[2]));
                    aSN1EncodableVector5.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getTreehash()[i222].getStatInt()[3]));
                    aSN1EncodableVector5.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getTreehash()[i222].getStatInt()[4]));
                    aSN1EncodableVector5.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getTreehash()[i222].getStatInt()[5]));
                    for (i10 = 0; i10 < i11; i10++) {
                        aSN1EncodableVector5.add(new ASN1Integer((long) gMSSRootCalcArr[i4].getTreehash()[i222].getStatInt()[i10 + 6]));
                    }
                    aSN1EncodableVector12.add(new DERSequence(aSN1EncodableVector5));
                    aSN1EncodableVector5 = new ASN1EncodableVector();
                    aSN1EncodableVector8.add(new DERSequence(aSN1EncodableVector12));
                    aSN1EncodableVector12 = new ASN1EncodableVector();
                }
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector8));
            aSN1EncodableVector4 = new ASN1EncodableVector();
            aSN1EncodableVector5 = new ASN1EncodableVector();
            if (gMSSRootCalcArr[i4].getRetain() != null) {
                aSN1EncodableVector13 = aSN1EncodableVector5;
                for (i9 = 0; i9 < gMSSRootCalcArr[i4].getRetain().length; i9++) {
                    for (int i12 = 0; i12 < gMSSRootCalcArr[i4].getRetain()[i9].size(); i12++) {
                        aSN1EncodableVector13.add(new DEROctetString((byte[]) gMSSRootCalcArr[i4].getRetain()[i9].elementAt(i12)));
                    }
                    aSN1EncodableVector7.add(new DERSequence(aSN1EncodableVector13));
                    aSN1EncodableVector13 = new ASN1EncodableVector();
                }
            }
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
            aSN1EncodableVector9.add(new DERSequence(aSN1EncodableVector));
            aSN1EncodableVector = new ASN1EncodableVector();
            i4++;
            aSN1EncodableVector8 = aSN1EncodableVector4;
            aSN1EncodableVector4 = aSN1EncodableVector6;
            aSN1EncodableVector6 = aSN1EncodableVector10;
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector9));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (byte[] dEROctetString222 : bArr7) {
            aSN1EncodableVector3.add(new DEROctetString(dEROctetString222));
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        aSN1EncodableVector6 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector8 = new ASN1EncodableVector();
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (i = 0; i < gMSSRootSigArr.length; i++) {
            aSN1EncodableVector4.add(new DERSequence(algorithmIdentifierArr[0]));
            aSN1EncodableVector10 = new ASN1EncodableVector();
            aSN1EncodableVector7.add(new DEROctetString(gMSSRootSigArr[i].getStatByte()[0]));
            aSN1EncodableVector7.add(new DEROctetString(gMSSRootSigArr[i].getStatByte()[1]));
            aSN1EncodableVector7.add(new DEROctetString(gMSSRootSigArr[i].getStatByte()[2]));
            aSN1EncodableVector7.add(new DEROctetString(gMSSRootSigArr[i].getStatByte()[3]));
            aSN1EncodableVector7.add(new DEROctetString(gMSSRootSigArr[i].getStatByte()[4]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector7));
            aSN1EncodableVector7 = new ASN1EncodableVector();
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[0]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[1]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[2]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[3]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[4]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[5]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[6]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[7]));
            aSN1EncodableVector3.add(new ASN1Integer((long) gMSSRootSigArr[i].getStatInt()[8]));
            aSN1EncodableVector4.add(new DERSequence(aSN1EncodableVector3));
            aSN1EncodableVector3 = new ASN1EncodableVector();
            aSN1EncodableVector6.add(new DERSequence(aSN1EncodableVector4));
            aSN1EncodableVector4 = new ASN1EncodableVector();
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector6));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        aSN1EncodableVector7 = new ASN1EncodableVector();
        aSN1EncodableVector4 = new ASN1EncodableVector();
        aSN1EncodableVector6 = new ASN1EncodableVector();
        for (i = 0; i < gMSSParameters.getHeightOfTrees().length; i++) {
            aSN1EncodableVector7.add(new ASN1Integer((long) gMSSParameters.getHeightOfTrees()[i]));
            aSN1EncodableVector4.add(new ASN1Integer((long) gMSSParameters.getWinternitzParameter()[i]));
            aSN1EncodableVector6.add(new ASN1Integer((long) gMSSParameters.getK()[i]));
        }
        aSN1EncodableVector3.add(new ASN1Integer((long) gMSSParameters.getNumOfLayers()));
        aSN1EncodableVector3.add(new DERSequence(aSN1EncodableVector7));
        aSN1EncodableVector3.add(new DERSequence(aSN1EncodableVector4));
        aSN1EncodableVector3.add(new DERSequence(aSN1EncodableVector6));
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        aSN1EncodableVector3 = new ASN1EncodableVector();
        for (ASN1Encodable add : algorithmIdentifierArr) {
            aSN1EncodableVector3.add(add);
        }
        aSN1EncodableVector2.add(new DERSequence(aSN1EncodableVector3));
        return new DERSequence(aSN1EncodableVector2);
    }

    public ASN1Primitive toASN1Primitive() {
        return this.primitive;
    }
}
