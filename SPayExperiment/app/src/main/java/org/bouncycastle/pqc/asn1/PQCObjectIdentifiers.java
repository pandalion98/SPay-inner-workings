/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.asn1;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface PQCObjectIdentifiers {
    public static final ASN1ObjectIdentifier gmss;
    public static final ASN1ObjectIdentifier gmssWithSha1;
    public static final ASN1ObjectIdentifier gmssWithSha224;
    public static final ASN1ObjectIdentifier gmssWithSha256;
    public static final ASN1ObjectIdentifier gmssWithSha384;
    public static final ASN1ObjectIdentifier gmssWithSha512;
    public static final ASN1ObjectIdentifier mcEliece;
    public static final ASN1ObjectIdentifier mcElieceCca2;
    public static final ASN1ObjectIdentifier rainbow;
    public static final ASN1ObjectIdentifier rainbowWithSha1;
    public static final ASN1ObjectIdentifier rainbowWithSha224;
    public static final ASN1ObjectIdentifier rainbowWithSha256;
    public static final ASN1ObjectIdentifier rainbowWithSha384;
    public static final ASN1ObjectIdentifier rainbowWithSha512;

    static {
        rainbow = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.5.3.2");
        rainbowWithSha1 = rainbow.branch("1");
        rainbowWithSha224 = rainbow.branch("2");
        rainbowWithSha256 = rainbow.branch("3");
        rainbowWithSha384 = rainbow.branch("4");
        rainbowWithSha512 = rainbow.branch("5");
        gmss = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.3");
        gmssWithSha1 = gmss.branch("1");
        gmssWithSha224 = gmss.branch("2");
        gmssWithSha256 = gmss.branch("3");
        gmssWithSha384 = gmss.branch("4");
        gmssWithSha512 = gmss.branch("5");
        mcEliece = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.1");
        mcElieceCca2 = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.4.2");
    }
}

