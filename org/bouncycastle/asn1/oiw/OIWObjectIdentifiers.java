package org.bouncycastle.asn1.oiw;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface OIWObjectIdentifiers {
    public static final ASN1ObjectIdentifier desCBC;
    public static final ASN1ObjectIdentifier desCFB;
    public static final ASN1ObjectIdentifier desECB;
    public static final ASN1ObjectIdentifier desEDE;
    public static final ASN1ObjectIdentifier desOFB;
    public static final ASN1ObjectIdentifier dsaWithSHA1;
    public static final ASN1ObjectIdentifier elGamalAlgorithm;
    public static final ASN1ObjectIdentifier idSHA1;
    public static final ASN1ObjectIdentifier md4WithRSA;
    public static final ASN1ObjectIdentifier md4WithRSAEncryption;
    public static final ASN1ObjectIdentifier md5WithRSA;
    public static final ASN1ObjectIdentifier sha1WithRSA;

    static {
        md4WithRSA = new ASN1ObjectIdentifier("1.3.14.3.2.2");
        md5WithRSA = new ASN1ObjectIdentifier("1.3.14.3.2.3");
        md4WithRSAEncryption = new ASN1ObjectIdentifier("1.3.14.3.2.4");
        desECB = new ASN1ObjectIdentifier("1.3.14.3.2.6");
        desCBC = new ASN1ObjectIdentifier("1.3.14.3.2.7");
        desOFB = new ASN1ObjectIdentifier("1.3.14.3.2.8");
        desCFB = new ASN1ObjectIdentifier("1.3.14.3.2.9");
        desEDE = new ASN1ObjectIdentifier("1.3.14.3.2.17");
        idSHA1 = new ASN1ObjectIdentifier("1.3.14.3.2.26");
        dsaWithSHA1 = new ASN1ObjectIdentifier("1.3.14.3.2.27");
        sha1WithRSA = new ASN1ObjectIdentifier("1.3.14.3.2.29");
        elGamalAlgorithm = new ASN1ObjectIdentifier("1.3.14.7.2.1.1");
    }
}
