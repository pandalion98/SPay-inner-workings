/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.icao;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface ICAOObjectIdentifiers {
    public static final ASN1ObjectIdentifier id_icao = new ASN1ObjectIdentifier("2.23.136");
    public static final ASN1ObjectIdentifier id_icao_aaProtocolObject;
    public static final ASN1ObjectIdentifier id_icao_cscaMasterList;
    public static final ASN1ObjectIdentifier id_icao_cscaMasterListSigningKey;
    public static final ASN1ObjectIdentifier id_icao_documentTypeList;
    public static final ASN1ObjectIdentifier id_icao_extensions;
    public static final ASN1ObjectIdentifier id_icao_extensions_namechangekeyrollover;
    public static final ASN1ObjectIdentifier id_icao_ldsSecurityObject;
    public static final ASN1ObjectIdentifier id_icao_mrtd;
    public static final ASN1ObjectIdentifier id_icao_mrtd_security;

    static {
        id_icao_mrtd = id_icao.branch("1");
        id_icao_mrtd_security = id_icao_mrtd.branch("1");
        id_icao_ldsSecurityObject = id_icao_mrtd_security.branch("1");
        id_icao_cscaMasterList = id_icao_mrtd_security.branch("2");
        id_icao_cscaMasterListSigningKey = id_icao_mrtd_security.branch("3");
        id_icao_documentTypeList = id_icao_mrtd_security.branch("4");
        id_icao_aaProtocolObject = id_icao_mrtd_security.branch("5");
        id_icao_extensions = id_icao_mrtd_security.branch("6");
        id_icao_extensions_namechangekeyrollover = id_icao_extensions.branch("1");
    }
}

