package org.bouncycastle.asn1.crmf;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public interface CRMFObjectIdentifiers {
    public static final ASN1ObjectIdentifier id_ct_encKeyWithID;
    public static final ASN1ObjectIdentifier id_pkip;
    public static final ASN1ObjectIdentifier id_pkix;
    public static final ASN1ObjectIdentifier id_regCtrl;
    public static final ASN1ObjectIdentifier id_regCtrl_authenticator;
    public static final ASN1ObjectIdentifier id_regCtrl_pkiArchiveOptions;
    public static final ASN1ObjectIdentifier id_regCtrl_pkiPublicationInfo;
    public static final ASN1ObjectIdentifier id_regCtrl_regToken;

    static {
        id_pkix = new ASN1ObjectIdentifier("1.3.6.1.5.5.7");
        id_pkip = id_pkix.branch("5");
        id_regCtrl = id_pkip.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_regCtrl_regToken = id_regCtrl.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_regCtrl_authenticator = id_regCtrl.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_regCtrl_pkiPublicationInfo = id_regCtrl.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        id_regCtrl_pkiArchiveOptions = id_regCtrl.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        id_ct_encKeyWithID = PKCSObjectIdentifiers.id_ct.branch("21");
    }
}
