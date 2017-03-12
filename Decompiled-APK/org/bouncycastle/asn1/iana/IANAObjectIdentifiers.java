package org.bouncycastle.asn1.iana;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface IANAObjectIdentifiers {
    public static final ASN1ObjectIdentifier SNMPv2;
    public static final ASN1ObjectIdentifier _private;
    public static final ASN1ObjectIdentifier directory;
    public static final ASN1ObjectIdentifier experimental;
    public static final ASN1ObjectIdentifier hmacMD5;
    public static final ASN1ObjectIdentifier hmacRIPEMD160;
    public static final ASN1ObjectIdentifier hmacSHA1;
    public static final ASN1ObjectIdentifier hmacTIGER;
    public static final ASN1ObjectIdentifier internet;
    public static final ASN1ObjectIdentifier ipsec;
    public static final ASN1ObjectIdentifier isakmpOakley;
    public static final ASN1ObjectIdentifier mail;
    public static final ASN1ObjectIdentifier mgmt;
    public static final ASN1ObjectIdentifier pkix;
    public static final ASN1ObjectIdentifier security;
    public static final ASN1ObjectIdentifier security_mechanisms;
    public static final ASN1ObjectIdentifier security_nametypes;

    static {
        internet = new ASN1ObjectIdentifier("1.3.6.1");
        directory = internet.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        mgmt = internet.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        experimental = internet.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        _private = internet.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        security = internet.branch("5");
        SNMPv2 = internet.branch("6");
        mail = internet.branch("7");
        security_mechanisms = security.branch("5");
        security_nametypes = security.branch("6");
        pkix = security_mechanisms.branch("6");
        ipsec = security_mechanisms.branch("8");
        isakmpOakley = ipsec.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        hmacMD5 = isakmpOakley.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        hmacSHA1 = isakmpOakley.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        hmacTIGER = isakmpOakley.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        hmacRIPEMD160 = isakmpOakley.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
    }
}
