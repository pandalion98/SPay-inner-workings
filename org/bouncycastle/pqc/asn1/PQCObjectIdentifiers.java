package org.bouncycastle.pqc.asn1;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.pqc.crypto.gmss.GMSSKeyPairGenerator;
import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceCCA2KeyFactorySpi;
import org.bouncycastle.pqc.jcajce.provider.mceliece.McElieceKeyFactorySpi;

public interface PQCObjectIdentifiers {
    public static final ASN1ObjectIdentifier gmss;
    public static final ASN1ObjectIdentifier gmssWithSha1;
    public static final ASN1ObjectIdentifier gmssWithSha224;
    public static final ASN1ObjectIdentifier gmssWithSha256;
    public static final ASN1ObjectIdentifier gmssWithSha384;
    public static final ASN1ObjectIdentifier gmssWithSha512;
    public static final ASN1ObjectIdentifier mcEliece;
    public static final ASN1ObjectIdentifier mcElieceCca2;
    public static final ASN1ObjectIdentifier rainbow;
    public static final ASN1ObjectIdentifier rainbowWithSha1;
    public static final ASN1ObjectIdentifier rainbowWithSha224;
    public static final ASN1ObjectIdentifier rainbowWithSha256;
    public static final ASN1ObjectIdentifier rainbowWithSha384;
    public static final ASN1ObjectIdentifier rainbowWithSha512;

    static {
        rainbow = new ASN1ObjectIdentifier("1.3.6.1.4.1.8301.3.1.3.5.3.2");
        rainbowWithSha1 = rainbow.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        rainbowWithSha224 = rainbow.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        rainbowWithSha256 = rainbow.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        rainbowWithSha384 = rainbow.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        rainbowWithSha512 = rainbow.branch("5");
        gmss = new ASN1ObjectIdentifier(GMSSKeyPairGenerator.OID);
        gmssWithSha1 = gmss.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        gmssWithSha224 = gmss.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        gmssWithSha256 = gmss.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        gmssWithSha384 = gmss.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        gmssWithSha512 = gmss.branch("5");
        mcEliece = new ASN1ObjectIdentifier(McElieceKeyFactorySpi.OID);
        mcElieceCca2 = new ASN1ObjectIdentifier(McElieceCCA2KeyFactorySpi.OID);
    }
}
