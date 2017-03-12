package org.bouncycastle.asn1.icao;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface ICAOObjectIdentifiers {
    public static final ASN1ObjectIdentifier id_icao;
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
        id_icao = new ASN1ObjectIdentifier("2.23.136");
        id_icao_mrtd = id_icao.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_icao_mrtd_security = id_icao_mrtd.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_icao_ldsSecurityObject = id_icao_mrtd_security.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        id_icao_cscaMasterList = id_icao_mrtd_security.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_icao_cscaMasterListSigningKey = id_icao_mrtd_security.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        id_icao_documentTypeList = id_icao_mrtd_security.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        id_icao_aaProtocolObject = id_icao_mrtd_security.branch("5");
        id_icao_extensions = id_icao_mrtd_security.branch("6");
        id_icao_extensions_namechangekeyrollover = id_icao_extensions.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
    }
}
