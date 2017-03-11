package org.bouncycastle.asn1;

public interface ASN1SetParser extends ASN1Encodable, InMemoryRepresentable {
    ASN1Encodable readObject();
}
