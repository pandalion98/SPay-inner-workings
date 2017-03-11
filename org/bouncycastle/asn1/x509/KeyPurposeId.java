package org.bouncycastle.asn1.x509;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;

public class KeyPurposeId extends ASN1Object {
    public static final KeyPurposeId anyExtendedKeyUsage;
    private static final ASN1ObjectIdentifier id_kp;
    public static final KeyPurposeId id_kp_OCSPSigning;
    public static final KeyPurposeId id_kp_capwapAC;
    public static final KeyPurposeId id_kp_capwapWTP;
    public static final KeyPurposeId id_kp_clientAuth;
    public static final KeyPurposeId id_kp_codeSigning;
    public static final KeyPurposeId id_kp_dvcs;
    public static final KeyPurposeId id_kp_eapOverLAN;
    public static final KeyPurposeId id_kp_eapOverPPP;
    public static final KeyPurposeId id_kp_emailProtection;
    public static final KeyPurposeId id_kp_ipsecEndSystem;
    public static final KeyPurposeId id_kp_ipsecIKE;
    public static final KeyPurposeId id_kp_ipsecTunnel;
    public static final KeyPurposeId id_kp_ipsecUser;
    public static final KeyPurposeId id_kp_sbgpCertAAServerAuth;
    public static final KeyPurposeId id_kp_scvpClient;
    public static final KeyPurposeId id_kp_scvpServer;
    public static final KeyPurposeId id_kp_scvp_responder;
    public static final KeyPurposeId id_kp_serverAuth;
    public static final KeyPurposeId id_kp_smartcardlogon;
    public static final KeyPurposeId id_kp_timeStamping;
    private ASN1ObjectIdentifier id;

    static {
        id_kp = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.3");
        anyExtendedKeyUsage = new KeyPurposeId(Extension.extendedKeyUsage.branch(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE));
        id_kp_serverAuth = new KeyPurposeId(id_kp.branch(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND));
        id_kp_clientAuth = new KeyPurposeId(id_kp.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_REFUNDED));
        id_kp_codeSigning = new KeyPurposeId(id_kp.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED));
        id_kp_emailProtection = new KeyPurposeId(id_kp.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR));
        id_kp_ipsecEndSystem = new KeyPurposeId(id_kp.branch("5"));
        id_kp_ipsecTunnel = new KeyPurposeId(id_kp.branch("6"));
        id_kp_ipsecUser = new KeyPurposeId(id_kp.branch("7"));
        id_kp_timeStamping = new KeyPurposeId(id_kp.branch("8"));
        id_kp_OCSPSigning = new KeyPurposeId(id_kp.branch("9"));
        id_kp_dvcs = new KeyPurposeId(id_kp.branch(Constants.CLIENT_MINOR_VERSION));
        id_kp_sbgpCertAAServerAuth = new KeyPurposeId(id_kp.branch(HCEClientConstants.API_INDEX_TOKEN_REFRESH_STATUS));
        id_kp_scvp_responder = new KeyPurposeId(id_kp.branch("12"));
        id_kp_eapOverPPP = new KeyPurposeId(id_kp.branch("13"));
        id_kp_eapOverLAN = new KeyPurposeId(id_kp.branch("14"));
        id_kp_scvpServer = new KeyPurposeId(id_kp.branch("15"));
        id_kp_scvpClient = new KeyPurposeId(id_kp.branch("16"));
        id_kp_ipsecIKE = new KeyPurposeId(id_kp.branch("17"));
        id_kp_capwapAC = new KeyPurposeId(id_kp.branch("18"));
        id_kp_capwapWTP = new KeyPurposeId(id_kp.branch("19"));
        id_kp_smartcardlogon = new KeyPurposeId(new ASN1ObjectIdentifier("1.3.6.1.4.1.311.20.2.2"));
    }

    public KeyPurposeId(String str) {
        this(new ASN1ObjectIdentifier(str));
    }

    private KeyPurposeId(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        this.id = aSN1ObjectIdentifier;
    }

    public static KeyPurposeId getInstance(Object obj) {
        return obj instanceof KeyPurposeId ? (KeyPurposeId) obj : obj != null ? new KeyPurposeId(ASN1ObjectIdentifier.getInstance(obj)) : null;
    }

    public String getId() {
        return this.id.getId();
    }

    public ASN1Primitive toASN1Primitive() {
        return this.id;
    }
}
