package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class DERApplicationSpecific extends ASN1Primitive {
    private final boolean isConstructed;
    private final byte[] octets;
    private final int tag;

    public DERApplicationSpecific(int i, ASN1Encodable aSN1Encodable) {
        this(true, i, aSN1Encodable);
    }

    public DERApplicationSpecific(int i, ASN1EncodableVector aSN1EncodableVector) {
        this.tag = i;
        this.isConstructed = true;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i2 = 0;
        while (i2 != aSN1EncodableVector.size()) {
            try {
                byteArrayOutputStream.write(((ASN1Object) aSN1EncodableVector.get(i2)).getEncoded(ASN1Encoding.DER));
                i2++;
            } catch (Throwable e) {
                throw new ASN1ParsingException("malformed object: " + e, e);
            }
        }
        this.octets = byteArrayOutputStream.toByteArray();
    }

    public DERApplicationSpecific(int i, byte[] bArr) {
        this(false, i, bArr);
    }

    public DERApplicationSpecific(boolean z, int i, ASN1Encodable aSN1Encodable) {
        ASN1Primitive toASN1Primitive = aSN1Encodable.toASN1Primitive();
        Object encoded = toASN1Primitive.getEncoded(ASN1Encoding.DER);
        boolean z2 = z || (toASN1Primitive instanceof ASN1Set) || (toASN1Primitive instanceof ASN1Sequence);
        this.isConstructed = z2;
        this.tag = i;
        if (z) {
            this.octets = encoded;
            return;
        }
        int lengthOfHeader = getLengthOfHeader(encoded);
        Object obj = new byte[(encoded.length - lengthOfHeader)];
        System.arraycopy(encoded, lengthOfHeader, obj, 0, obj.length);
        this.octets = obj;
    }

    DERApplicationSpecific(boolean z, int i, byte[] bArr) {
        this.isConstructed = z;
        this.tag = i;
        this.octets = bArr;
    }

    public static DERApplicationSpecific getInstance(Object obj) {
        if (obj == null || (obj instanceof DERApplicationSpecific)) {
            return (DERApplicationSpecific) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[]) obj));
            } catch (IOException e) {
                throw new IllegalArgumentException("failed to construct object from byte[]: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + obj.getClass().getName());
    }

    private int getLengthOfHeader(byte[] bArr) {
        int i = bArr[1] & GF2Field.MASK;
        if (i == X509KeyUsage.digitalSignature || i <= CertificateBody.profileType) {
            return 2;
        }
        int i2 = i & CertificateBody.profileType;
        if (i2 <= 4) {
            return i2 + 2;
        }
        throw new IllegalStateException("DER length more than 4 bytes: " + i2);
    }

    private byte[] replaceTagNumber(int i, byte[] bArr) {
        int i2;
        if ((bArr[0] & 31) == 31) {
            i2 = bArr[1] & GF2Field.MASK;
            if ((i2 & CertificateBody.profileType) == 0) {
                throw new ASN1ParsingException("corrupted stream - invalid high tag number found");
            }
            int i3 = 0;
            int i4 = i2;
            i2 = 2;
            while (i4 >= 0 && (i4 & X509KeyUsage.digitalSignature) != 0) {
                i3 = ((i4 & CertificateBody.profileType) | i3) << 7;
                int i5 = bArr[i2] & GF2Field.MASK;
                i2++;
                i4 = i5;
            }
            i4 = (i4 & CertificateBody.profileType) | i3;
        } else {
            i2 = 1;
        }
        Object obj = new byte[((bArr.length - i2) + 1)];
        System.arraycopy(bArr, i2, obj, 1, obj.length - 1);
        obj[0] = (byte) i;
        return obj;
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof DERApplicationSpecific)) {
            return false;
        }
        DERApplicationSpecific dERApplicationSpecific = (DERApplicationSpecific) aSN1Primitive;
        return this.isConstructed == dERApplicationSpecific.isConstructed && this.tag == dERApplicationSpecific.tag && Arrays.areEqual(this.octets, dERApplicationSpecific.octets);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        int i = 64;
        if (this.isConstructed) {
            i = 96;
        }
        aSN1OutputStream.writeEncoded(i, this.tag, this.octets);
    }

    int encodedLength() {
        return (StreamUtil.calculateTagLength(this.tag) + StreamUtil.calculateBodyLength(this.octets.length)) + this.octets.length;
    }

    public int getApplicationTag() {
        return this.tag;
    }

    public byte[] getContents() {
        return this.octets;
    }

    public ASN1Primitive getObject() {
        return new ASN1InputStream(getContents()).readObject();
    }

    public ASN1Primitive getObject(int i) {
        if (i >= 31) {
            throw new IOException("unsupported tag number");
        }
        byte[] encoded = getEncoded();
        byte[] replaceTagNumber = replaceTagNumber(i, encoded);
        if ((encoded[0] & 32) != 0) {
            replaceTagNumber[0] = (byte) (replaceTagNumber[0] | 32);
        }
        return new ASN1InputStream(replaceTagNumber).readObject();
    }

    public int hashCode() {
        return ((this.isConstructed ? 1 : 0) ^ this.tag) ^ Arrays.hashCode(this.octets);
    }

    public boolean isConstructed() {
        return this.isConstructed;
    }
}
