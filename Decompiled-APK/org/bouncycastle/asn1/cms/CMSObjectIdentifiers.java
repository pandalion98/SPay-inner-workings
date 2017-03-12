package org.bouncycastle.asn1.cms;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public interface CMSObjectIdentifiers {
    public static final ASN1ObjectIdentifier authEnvelopedData;
    public static final ASN1ObjectIdentifier authenticatedData;
    public static final ASN1ObjectIdentifier compressedData;
    public static final ASN1ObjectIdentifier data;
    public static final ASN1ObjectIdentifier digestedData;
    public static final ASN1ObjectIdentifier encryptedData;
    public static final ASN1ObjectIdentifier envelopedData;
    public static final ASN1ObjectIdentifier id_ri;
    public static final ASN1ObjectIdentifier id_ri_ocsp_response;
    public static final ASN1ObjectIdentifier id_ri_scvp;
    public static final ASN1ObjectIdentifier signedAndEnvelopedData;
    public static final ASN1ObjectIdentifier signedData;
    public static final ASN1ObjectIdentifier timestampedData;

    static {
        data = PKCSObjectIdentifiers.data;
        signedData = PKCSObjectIdentifiers.signedData;
        envelopedData = PKCSObjectIdentifiers.envelopedData;
        signedAndEnvelopedData = PKCSObjectIdentifiers.signedAndEnvelopedData;
        digestedData = PKCSObjectIdentifiers.digestedData;
        encryptedData = PKCSObjectIdentifiers.encryptedData;
        authenticatedData = PKCSObjectIdentifiers.id_ct_authData;
        compressedData = PKCSObjectIdentifiers.id_ct_compressedData;
        authEnvelopedData = PKCSObjectIdentifiers.id_ct_authEnvelopedData;
        timestampedData = PKCSObjectIdentifiers.id_ct_timestampedData;
        id_ri = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.16");
        id_ri_ocsp_response = id_ri.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED);
        id_ri_scvp = id_ri.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
    }
}
