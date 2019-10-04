/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface ETSIQCObjectIdentifiers {
    public static final ASN1ObjectIdentifier id_etsi_qcs_LimiteValue;
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcCompliance;
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcSSCD;
    public static final ASN1ObjectIdentifier id_etsi_qcs_RetentionPeriod;

    static {
        id_etsi_qcs_QcCompliance = new ASN1ObjectIdentifier("0.4.0.1862.1.1");
        id_etsi_qcs_LimiteValue = new ASN1ObjectIdentifier("0.4.0.1862.1.2");
        id_etsi_qcs_RetentionPeriod = new ASN1ObjectIdentifier("0.4.0.1862.1.3");
        id_etsi_qcs_QcSSCD = new ASN1ObjectIdentifier("0.4.0.1862.1.4");
    }
}

