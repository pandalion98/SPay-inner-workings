package org.bouncycastle.asn1.ua;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface UAObjectIdentifiers {
    public static final ASN1ObjectIdentifier UaOid;
    public static final ASN1ObjectIdentifier dstu4145be;
    public static final ASN1ObjectIdentifier dstu4145le;

    static {
        UaOid = new ASN1ObjectIdentifier("1.2.804.2.1.1.1");
        dstu4145le = UaOid.branch("1.3.1.1");
        dstu4145be = UaOid.branch("1.3.1.1.1.1");
    }
}
