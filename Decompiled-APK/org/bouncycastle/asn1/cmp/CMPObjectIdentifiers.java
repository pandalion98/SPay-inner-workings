package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CMPObjectIdentifiers {
    public static final ASN1ObjectIdentifier ct_encKeyWithID;
    public static final ASN1ObjectIdentifier dhBasedMac;
    public static final ASN1ObjectIdentifier id_pkip;
    public static final ASN1ObjectIdentifier id_regCtrl;
    public static final ASN1ObjectIdentifier id_regInfo;
    public static final ASN1ObjectIdentifier it_caKeyUpdateInfo;
    public static final ASN1ObjectIdentifier it_caProtEncCert;
    public static final ASN1ObjectIdentifier it_confirmWaitTime;
    public static final ASN1ObjectIdentifier it_currentCRL;
    public static final ASN1ObjectIdentifier it_encKeyPairTypes;
    public static final ASN1ObjectIdentifier it_implicitConfirm;
    public static final ASN1ObjectIdentifier it_keyPairParamRep;
    public static final ASN1ObjectIdentifier it_keyPairParamReq;
    public static final ASN1ObjectIdentifier it_origPKIMessage;
    public static final ASN1ObjectIdentifier it_preferredSymAlg;
    public static final ASN1ObjectIdentifier it_revPassphrase;
    public static final ASN1ObjectIdentifier it_signKeyPairTypes;
    public static final ASN1ObjectIdentifier it_suppLangTags;
    public static final ASN1ObjectIdentifier it_unsupportedOIDs;
    public static final ASN1ObjectIdentifier passwordBasedMac;
    public static final ASN1ObjectIdentifier regCtrl_altCertTemplate;
    public static final ASN1ObjectIdentifier regCtrl_authenticator;
    public static final ASN1ObjectIdentifier regCtrl_oldCertID;
    public static final ASN1ObjectIdentifier regCtrl_pkiArchiveOptions;
    public static final ASN1ObjectIdentifier regCtrl_pkiPublicationInfo;
    public static final ASN1ObjectIdentifier regCtrl_protocolEncrKey;
    public static final ASN1ObjectIdentifier regCtrl_regToken;
    public static final ASN1ObjectIdentifier regInfo_certReq;
    public static final ASN1ObjectIdentifier regInfo_utf8Pairs;

    static {
        passwordBasedMac = new ASN1ObjectIdentifier("1.2.840.113533.7.66.13");
        dhBasedMac = new ASN1ObjectIdentifier("1.2.840.113533.7.66.30");
        it_caProtEncCert = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.1");
        it_signKeyPairTypes = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.2");
        it_encKeyPairTypes = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.3");
        it_preferredSymAlg = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.4");
        it_caKeyUpdateInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.5");
        it_currentCRL = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.6");
        it_unsupportedOIDs = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.7");
        it_keyPairParamReq = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.10");
        it_keyPairParamRep = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.11");
        it_revPassphrase = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.12");
        it_implicitConfirm = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.13");
        it_confirmWaitTime = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.14");
        it_origPKIMessage = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.15");
        it_suppLangTags = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.4.16");
        id_pkip = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5");
        id_regCtrl = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1");
        id_regInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.2");
        regCtrl_regToken = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1.1");
        regCtrl_authenticator = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1.2");
        regCtrl_pkiPublicationInfo = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1.3");
        regCtrl_pkiArchiveOptions = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1.4");
        regCtrl_oldCertID = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1.5");
        regCtrl_protocolEncrKey = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1.6");
        regCtrl_altCertTemplate = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.1.7");
        regInfo_utf8Pairs = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.2.1");
        regInfo_certReq = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.5.2.2");
        ct_encKeyWithID = new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.1.21");
    }
}
