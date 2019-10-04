/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.microsoft;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface MicrosoftObjectIdentifiers {
    public static final ASN1ObjectIdentifier microsoft = new ASN1ObjectIdentifier("1.3.6.1.4.1.311");
    public static final ASN1ObjectIdentifier microsoftAppPolicies;
    public static final ASN1ObjectIdentifier microsoftCaVersion;
    public static final ASN1ObjectIdentifier microsoftCertTemplateV1;
    public static final ASN1ObjectIdentifier microsoftCertTemplateV2;
    public static final ASN1ObjectIdentifier microsoftCrlNextPublish;
    public static final ASN1ObjectIdentifier microsoftPrevCaCertHash;

    static {
        microsoftCertTemplateV1 = microsoft.branch("20.2");
        microsoftCaVersion = microsoft.branch("21.1");
        microsoftPrevCaCertHash = microsoft.branch("21.2");
        microsoftCrlNextPublish = microsoft.branch("21.4");
        microsoftCertTemplateV2 = microsoft.branch("21.7");
        microsoftAppPolicies = microsoft.branch("21.10");
    }
}

