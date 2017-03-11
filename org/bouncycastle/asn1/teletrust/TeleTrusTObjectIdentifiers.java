package org.bouncycastle.asn1.teletrust;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface TeleTrusTObjectIdentifiers {
    public static final ASN1ObjectIdentifier brainpoolP160r1;
    public static final ASN1ObjectIdentifier brainpoolP160t1;
    public static final ASN1ObjectIdentifier brainpoolP192r1;
    public static final ASN1ObjectIdentifier brainpoolP192t1;
    public static final ASN1ObjectIdentifier brainpoolP224r1;
    public static final ASN1ObjectIdentifier brainpoolP224t1;
    public static final ASN1ObjectIdentifier brainpoolP256r1;
    public static final ASN1ObjectIdentifier brainpoolP256t1;
    public static final ASN1ObjectIdentifier brainpoolP320r1;
    public static final ASN1ObjectIdentifier brainpoolP320t1;
    public static final ASN1ObjectIdentifier brainpoolP384r1;
    public static final ASN1ObjectIdentifier brainpoolP384t1;
    public static final ASN1ObjectIdentifier brainpoolP512r1;
    public static final ASN1ObjectIdentifier brainpoolP512t1;
    public static final ASN1ObjectIdentifier ecSign;
    public static final ASN1ObjectIdentifier ecSignWithRipemd160;
    public static final ASN1ObjectIdentifier ecSignWithSha1;
    public static final ASN1ObjectIdentifier ecc_brainpool;
    public static final ASN1ObjectIdentifier ellipticCurve;
    public static final ASN1ObjectIdentifier ripemd128;
    public static final ASN1ObjectIdentifier ripemd160;
    public static final ASN1ObjectIdentifier ripemd256;
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd128;
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd160;
    public static final ASN1ObjectIdentifier rsaSignatureWithripemd256;
    public static final ASN1ObjectIdentifier teleTrusTAlgorithm;
    public static final ASN1ObjectIdentifier teleTrusTRSAsignatureAlgorithm;
    public static final ASN1ObjectIdentifier versionOne;

    static {
        teleTrusTAlgorithm = new ASN1ObjectIdentifier("1.3.36.3");
        ripemd160 = teleTrusTAlgorithm.branch("2.1");
        ripemd128 = teleTrusTAlgorithm.branch("2.2");
        ripemd256 = teleTrusTAlgorithm.branch("2.3");
        teleTrusTRSAsignatureAlgorithm = teleTrusTAlgorithm.branch("3.1");
        rsaSignatureWithripemd160 = teleTrusTRSAsignatureAlgorithm.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        rsaSignatureWithripemd128 = teleTrusTRSAsignatureAlgorithm.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        rsaSignatureWithripemd256 = teleTrusTRSAsignatureAlgorithm.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        ecSign = teleTrusTAlgorithm.branch("3.2");
        ecSignWithSha1 = ecSign.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        ecSignWithRipemd160 = ecSign.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        ecc_brainpool = teleTrusTAlgorithm.branch("3.2.8");
        ellipticCurve = ecc_brainpool.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        versionOne = ellipticCurve.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        brainpoolP160r1 = versionOne.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        brainpoolP160t1 = versionOne.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        brainpoolP192r1 = versionOne.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        brainpoolP192t1 = versionOne.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        brainpoolP224r1 = versionOne.branch("5");
        brainpoolP224t1 = versionOne.branch("6");
        brainpoolP256r1 = versionOne.branch("7");
        brainpoolP256t1 = versionOne.branch("8");
        brainpoolP320r1 = versionOne.branch("9");
        brainpoolP320t1 = versionOne.branch(Constants.CLIENT_MINOR_VERSION);
        brainpoolP384r1 = versionOne.branch(HCEClientConstants.API_INDEX_TOKEN_REFRESH_STATUS);
        brainpoolP384t1 = versionOne.branch("12");
        brainpoolP512r1 = versionOne.branch("13");
        brainpoolP512t1 = versionOne.branch("14");
    }
}
