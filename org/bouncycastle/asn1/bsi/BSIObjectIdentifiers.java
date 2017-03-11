package org.bouncycastle.asn1.bsi;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface BSIObjectIdentifiers {
    public static final ASN1ObjectIdentifier bsi_de;
    public static final ASN1ObjectIdentifier ecdsa_plain_RIPEMD160;
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA1;
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA224;
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA256;
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA384;
    public static final ASN1ObjectIdentifier ecdsa_plain_SHA512;
    public static final ASN1ObjectIdentifier ecdsa_plain_signatures;
    public static final ASN1ObjectIdentifier id_ecc;

    static {
        bsi_de = new ASN1ObjectIdentifier("0.4.0.127.0.7");
        id_ecc = bsi_de.branch("1.1");
        ecdsa_plain_signatures = id_ecc.branch("4.1");
        ecdsa_plain_SHA1 = ecdsa_plain_signatures.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        ecdsa_plain_SHA224 = ecdsa_plain_signatures.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        ecdsa_plain_SHA256 = ecdsa_plain_signatures.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        ecdsa_plain_SHA384 = ecdsa_plain_signatures.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        ecdsa_plain_SHA512 = ecdsa_plain_signatures.branch("5");
        ecdsa_plain_RIPEMD160 = ecdsa_plain_signatures.branch("6");
    }
}
