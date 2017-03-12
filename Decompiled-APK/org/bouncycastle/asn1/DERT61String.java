package org.bouncycastle.asn1;

import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Strings;

public class DERT61String extends ASN1Primitive implements ASN1String {
    private byte[] string;

    public DERT61String(String str) {
        this(Strings.toByteArray(str));
    }

    public DERT61String(byte[] bArr) {
        this.string = bArr;
    }

    public static DERT61String getInstance(Object obj) {
        if (obj == null || (obj instanceof DERT61String)) {
            return (DERT61String) obj;
        }
        if (obj instanceof byte[]) {
            try {
                return (DERT61String) ASN1Primitive.fromByteArray((byte[]) obj);
            } catch (Exception e) {
                throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
            }
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static DERT61String getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        ASN1Primitive object = aSN1TaggedObject.getObject();
        return (z || (object instanceof DERT61String)) ? getInstance(object) : new DERT61String(ASN1OctetString.getInstance(object).getOctets());
    }

    boolean asn1Equals(ASN1Primitive aSN1Primitive) {
        return !(aSN1Primitive instanceof DERT61String) ? false : Arrays.areEqual(this.string, ((DERT61String) aSN1Primitive).string);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(20, this.string);
    }

    int encodedLength() {
        return (StreamUtil.calculateBodyLength(this.string.length) + 1) + this.string.length;
    }

    public byte[] getOctets() {
        return Arrays.clone(this.string);
    }

    public String getString() {
        return Strings.fromByteArray(this.string);
    }

    public int hashCode() {
        return Arrays.hashCode(this.string);
    }

    boolean isConstructed() {
        return false;
    }

    public String toString() {
        return getString();
    }
}
