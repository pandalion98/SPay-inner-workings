package org.bouncycastle.asn1.x509;

import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface X509AttributeIdentifiers {
    public static final ASN1ObjectIdentifier RoleSyntax;
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
        RoleSyntax = new ASN1ObjectIdentifier("2.5.4.72");
        id_pe_ac_auditIdentity = X509ObjectIdentifiers.id_pe.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        id_pe_aaControls = X509ObjectIdentifiers.id_pe.branch("6");
        id_pe_ac_proxying = X509ObjectIdentifiers.id_pe.branch(Constants.CLIENT_MINOR_VERSION);
        id_ce_targetInformation = X509ObjectIdentifiers.id_ce.branch("55");
        id_aca = X509ObjectIdentifiers.id_pkix.branch(Constants.CLIENT_MINOR_VERSION);
        id_aca_authenticationInfo = id_aca.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_aca_accessIdentity = id_aca.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_aca_chargingIdentity = id_aca.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        id_aca_group = id_aca.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        id_aca_encAttrs = id_aca.branch("6");
        id_at_role = new ASN1ObjectIdentifier("2.5.4.72");
        id_at_clearance = new ASN1ObjectIdentifier("2.5.1.5.55");
    }
}
