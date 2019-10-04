/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;

public interface X509AttributeIdentifiers {
    public static final ASN1ObjectIdentifier RoleSyntax = new ASN1ObjectIdentifier("2.5.4.72");
    public static final ASN1ObjectIdentifier id_aca;
    public static final ASN1ObjectIdentifier id_aca_accessIdentity;
    public static final ASN1ObjectIdentifier id_aca_authenticationInfo;
    public static final ASN1ObjectIdentifier id_aca_chargingIdentity;
    public static final ASN1ObjectIdentifier id_aca_encAttrs;
    public static final ASN1ObjectIdentifier id_aca_group;
    public static final ASN1ObjectIdentifier id_at_clearance;
    public static final ASN1ObjectIdentifier id_at_role;
    public static final ASN1ObjectIdentifier id_ce_targetInformation;
    public static final ASN1ObjectIdentifier id_pe_aaControls;
    public static final ASN1ObjectIdentifier id_pe_ac_auditIdentity;
    public static final ASN1ObjectIdentifier id_pe_ac_proxying;

    static {
        id_pe_ac_auditIdentity = X509ObjectIdentifiers.id_pe.branch("4");
        id_pe_aaControls = X509ObjectIdentifiers.id_pe.branch("6");
        id_pe_ac_proxying = X509ObjectIdentifiers.id_pe.branch("10");
        id_ce_targetInformation = X509ObjectIdentifiers.id_ce.branch("55");
        id_aca = X509ObjectIdentifiers.id_pkix.branch("10");
        id_aca_authenticationInfo = id_aca.branch("1");
        id_aca_accessIdentity = id_aca.branch("2");
        id_aca_chargingIdentity = id_aca.branch("3");
        id_aca_group = id_aca.branch("4");
        id_aca_encAttrs = id_aca.branch("6");
        id_at_role = new ASN1ObjectIdentifier("2.5.4.72");
        id_at_clearance = new ASN1ObjectIdentifier("2.5.1.5.55");
    }
}

