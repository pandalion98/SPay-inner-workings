package org.bouncycastle.asn1;

import java.math.BigInteger;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class ASN1Enumerated extends ASN1Primitive {
    private static ASN1Enumerated[] cache;
    byte[] bytes;

    static {
        cache = new ASN1Enumerated[12];
    }

    public ASN1Enumerated(int i) {
        this.bytes = BigInteger.valueOf((long) i).toByteArray();
    }

    public ASN1Enumerated(BigInteger bigInteger) {
        this.bytes = bigInteger.toByteArray();
    }

    public ASN1Enumerated(byte[] bArr) {
        this.bytes = bArr;
    }

    static ASN1Enumerated fromOctetString(byte[] bArr) {
        if (bArr.length > 1) {
            return new ASN1Enumerated(Arrays.clone(bArr));
        }
        if (bArr.length == 0) {
            throw new IllegalArgumentException("ENUMERATED has zero length");
        }
        int i = bArr[0] & GF2Field.MASK;
        if (i >= cache.length) {
            return new ASN1Enumerated(Arrays.clone(bArr));
        }
        ASN1Enumerated aSN1Enumerated = cache[i];
        if (aSN1Enumerated != null) {
            return aSN1Enumerated;
        }
        ASN1Enumerated[] aSN1EnumeratedArr = cache;
        aSN1Enumerated = new ASN1Enumerated(Arrays.clone(bArr));
        aSN1EnumeratedArr[i] = aSN1Enumerated;
        return aSN1Enumerated;
    }

    public static ASN1Enumerated getInstance(Object obj) {
        if (obj == null || (obj instanceof ASN1Enumerated)) {
            return (ASN1Enumerated) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return (ASN1Enumerated) ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (Exception e) {
                throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static ASN1Enumerated getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        ASN1Primitive object = aSN1TaggedObject.getObject();
        return (z || (object instanceof ASN1Enumerated)) ? getInstance(object) : fromOctetString(((ASN1OctetString) object).getOctets());
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        if (!(aSN1Primitive instanceof ASN1Enumerated)) {
            return false;
        }
        return Arrays.areEqual(this.bytes, ((ASN1Enumerated) aSN1Primitive).bytes);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(10, this.bytes);
    }

    int encodedLength() {
        return (StreamUtil.calculateBodyLength(this.bytes.length) + 1) + this.bytes.length;
    }

    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }

    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }

    boolean isConstructed() {
        return false;
    }
}
