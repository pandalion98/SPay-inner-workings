package org.bouncycastle.asn1.eac;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface EACObjectIdentifiers {
    public static final ASN1ObjectIdentifier bsi_de;
    public static final ASN1ObjectIdentifier id_CA;
    public static final ASN1ObjectIdentifier id_CA_DH;
    public static final ASN1ObjectIdentifier id_CA_DH_3DES_CBC_CBC;
    public static final ASN1ObjectIdentifier id_CA_ECDH;
    public static final ASN1ObjectIdentifier id_CA_ECDH_3DES_CBC_CBC;
    public static final ASN1ObjectIdentifier id_EAC_ePassport;
    public static final ASN1ObjectIdentifier id_PK;
    public static final ASN1ObjectIdentifier id_PK_DH;
    public static final ASN1ObjectIdentifier id_PK_ECDH;
    public static final ASN1ObjectIdentifier id_TA;
    public static final ASN1ObjectIdentifier id_TA_ECDSA;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_1;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_224;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_256;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_384;
    public static final ASN1ObjectIdentifier id_TA_ECDSA_SHA_512;
    public static final ASN1ObjectIdentifier id_TA_RSA;
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_1;
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_256;
    public static final ASN1ObjectIdentifier id_TA_RSA_PSS_SHA_512;
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_1;
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_256;
    public static final ASN1ObjectIdentifier id_TA_RSA_v1_5_SHA_512;

    static {
        bsi_de = new ASN1ObjectIdentifier("0.4.0.127.0.7");
        id_PK = bsi_de.branch("2.2.1");
        id_PK_DH = id_PK.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_PK_ECDH = id_PK.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_CA = bsi_de.branch("2.2.3");
        id_CA_DH = id_CA.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_CA_DH_3DES_CBC_CBC = id_CA_DH.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_CA_ECDH = id_CA.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_CA_ECDH_3DES_CBC_CBC = id_CA_ECDH.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_TA = bsi_de.branch("2.2.2");
        id_TA_RSA = id_TA.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_TA_RSA_v1_5_SHA_1 = id_TA_RSA.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_TA_RSA_v1_5_SHA_256 = id_TA_RSA.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_TA_RSA_PSS_SHA_1 = id_TA_RSA.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        id_TA_RSA_PSS_SHA_256 = id_TA_RSA.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        id_TA_RSA_v1_5_SHA_512 = id_TA_RSA.branch("5");
        id_TA_RSA_PSS_SHA_512 = id_TA_RSA.branch("6");
        id_TA_ECDSA = id_TA.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_TA_ECDSA_SHA_1 = id_TA_ECDSA.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_TA_ECDSA_SHA_224 = id_TA_ECDSA.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_TA_ECDSA_SHA_256 = id_TA_ECDSA.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        id_TA_ECDSA_SHA_384 = id_TA_ECDSA.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        id_TA_ECDSA_SHA_512 = id_TA_ECDSA.branch("5");
        id_EAC_ePassport = bsi_de.branch("3.1.2.1");
    }
}
