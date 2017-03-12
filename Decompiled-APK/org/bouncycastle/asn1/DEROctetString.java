package org.bouncycastle.asn1;

public class DEROctetString extends ASN1OctetString {
    public DEROctetString(ASN1Encodable aSN1Encodable) {
        super(aSN1Encodable.toASN1Primitive().getEncoded(ASN1Encoding.DER));
    }

    public DEROctetString(byte[] bArr) {
        super(bArr);
    }

    static void encode(DEROutputStream dEROutputStream, byte[] bArr) {
        dEROutputStream.writeEncoded(4, bArr);
    }

    void encode(ASN1OutputStream aSN1OutputStream) {
        aSN1OutputStream.writeEncoded(4, this.string);
    }

    int encodedLength() {
        return (StreamUtil.calculateBodyLength(this.string.length) + 1) + this.string.length;
    }

    boolean isConstructed() {
        return false;
    }
}
