package org.bouncycastle.asn1.misc;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface MiscObjectIdentifiers {
    public static final ASN1ObjectIdentifier entrust;
    public static final ASN1ObjectIdentifier entrustVersionExtension;
    public static final ASN1ObjectIdentifier netscape;
    public static final ASN1ObjectIdentifier netscapeBaseURL;
    public static final ASN1ObjectIdentifier netscapeCARevocationURL;
    public static final ASN1ObjectIdentifier netscapeCApolicyURL;
    public static final ASN1ObjectIdentifier netscapeCertComment;
    public static final ASN1ObjectIdentifier netscapeCertType;
    public static final ASN1ObjectIdentifier netscapeRenewalURL;
    public static final ASN1ObjectIdentifier netscapeRevocationURL;
    public static final ASN1ObjectIdentifier netscapeSSLServerName;
    public static final ASN1ObjectIdentifier novell;
    public static final ASN1ObjectIdentifier novellSecurityAttribs;
    public static final ASN1ObjectIdentifier verisign;
    public static final ASN1ObjectIdentifier verisignBitString_6_13;
    public static final ASN1ObjectIdentifier verisignCzagExtension;
    public static final ASN1ObjectIdentifier verisignDnbDunsNumber;
    public static final ASN1ObjectIdentifier verisignIssStrongCrypto;
    public static final ASN1ObjectIdentifier verisignOnSiteJurisdictionHash;
    public static final ASN1ObjectIdentifier verisignPrivate_6_9;

    static {
        netscape = new ASN1ObjectIdentifier("2.16.840.1.113730.1");
        netscapeCertType = netscape.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        netscapeBaseURL = netscape.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        netscapeRevocationURL = netscape.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        netscapeCARevocationURL = netscape.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        netscapeRenewalURL = netscape.branch("7");
        netscapeCApolicyURL = netscape.branch("8");
        netscapeSSLServerName = netscape.branch("12");
        netscapeCertComment = netscape.branch("13");
        verisign = new ASN1ObjectIdentifier("2.16.840.1.113733.1");
        verisignCzagExtension = verisign.branch("6.3");
        verisignPrivate_6_9 = verisign.branch("6.9");
        verisignOnSiteJurisdictionHash = verisign.branch("6.11");
        verisignBitString_6_13 = verisign.branch("6.13");
        verisignDnbDunsNumber = verisign.branch("6.15");
        verisignIssStrongCrypto = verisign.branch("8.1");
        novell = new ASN1ObjectIdentifier("2.16.840.1.113719");
        novellSecurityAttribs = novell.branch("1.9.4.1");
        entrust = new ASN1ObjectIdentifier("1.2.840.113533.7");
        entrustVersionExtension = entrust.branch("65.0");
    }
}
