/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.isismtt;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface ISISMTTObjectIdentifiers {
    public static final ASN1ObjectIdentifier id_isismtt = new ASN1ObjectIdentifier("1.3.36.8");
    public static final ASN1ObjectIdentifier id_isismtt_at;
    public static final ASN1ObjectIdentifier id_isismtt_at_PKReference;
    public static final ASN1ObjectIdentifier id_isismtt_at_additionalInformation;
    public static final ASN1ObjectIdentifier id_isismtt_at_admission;
    public static final ASN1ObjectIdentifier id_isismtt_at_certHash;
    public static final ASN1ObjectIdentifier id_isismtt_at_certInDirSince;
    public static final ASN1ObjectIdentifier id_isismtt_at_dateOfCertGen;
    public static final ASN1ObjectIdentifier id_isismtt_at_declarationOfMajority;
    public static final ASN1ObjectIdentifier id_isismtt_at_iCCSN;
    public static final ASN1ObjectIdentifier id_isismtt_at_liabilityLimitationFlag;
    public static final ASN1ObjectIdentifier id_isismtt_at_monetaryLimit;
    public static final ASN1ObjectIdentifier id_isismtt_at_nameAtBirth;
    public static final ASN1ObjectIdentifier id_isismtt_at_namingAuthorities;
    public static final ASN1ObjectIdentifier id_isismtt_at_procuration;
    public static final ASN1ObjectIdentifier id_isismtt_at_requestedCertificate;
    public static final ASN1ObjectIdentifier id_isismtt_at_restriction;
    public static final ASN1ObjectIdentifier id_isismtt_at_retrieveIfAllowed;
    public static final ASN1ObjectIdentifier id_isismtt_cp;
    public static final ASN1ObjectIdentifier id_isismtt_cp_accredited;

    static {
        id_isismtt_cp = id_isismtt.branch("1");
        id_isismtt_cp_accredited = id_isismtt_cp.branch("1");
        id_isismtt_at = id_isismtt.branch("3");
        id_isismtt_at_dateOfCertGen = id_isismtt_at.branch("1");
        id_isismtt_at_procuration = id_isismtt_at.branch("2");
        id_isismtt_at_admission = id_isismtt_at.branch("3");
        id_isismtt_at_monetaryLimit = id_isismtt_at.branch("4");
        id_isismtt_at_declarationOfMajority = id_isismtt_at.branch("5");
        id_isismtt_at_iCCSN = id_isismtt_at.branch("6");
        id_isismtt_at_PKReference = id_isismtt_at.branch("7");
        id_isismtt_at_restriction = id_isismtt_at.branch("8");
        id_isismtt_at_retrieveIfAllowed = id_isismtt_at.branch("9");
        id_isismtt_at_requestedCertificate = id_isismtt_at.branch("10");
        id_isismtt_at_namingAuthorities = id_isismtt_at.branch("11");
        id_isismtt_at_certInDirSince = id_isismtt_at.branch("12");
        id_isismtt_at_certHash = id_isismtt_at.branch("13");
        id_isismtt_at_nameAtBirth = id_isismtt_at.branch("14");
        id_isismtt_at_additionalInformation = id_isismtt_at.branch("15");
        id_isismtt_at_liabilityLimitationFlag = new ASN1ObjectIdentifier("0.2.262.1.10.12.0");
    }
}

