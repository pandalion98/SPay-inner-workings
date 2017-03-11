package org.bouncycastle.asn1.cryptopro;

import com.americanexpress.sdkmodulelib.util.Constants;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public interface CryptoProObjectIdentifiers {
    public static final ASN1ObjectIdentifier GOST_id;
    public static final ASN1ObjectIdentifier gostR28147_gcfb;
    public static final ASN1ObjectIdentifier gostR3410_2001;
    public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_A;
    public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_B;
    public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_C;
    public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_XchA;
    public static final ASN1ObjectIdentifier gostR3410_2001_CryptoPro_XchB;
    public static final ASN1ObjectIdentifier gostR3410_94;
    public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_A;
    public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_B;
    public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_C;
    public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_D;
    public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_XchA;
    public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_XchB;
    public static final ASN1ObjectIdentifier gostR3410_94_CryptoPro_XchC;
    public static final ASN1ObjectIdentifier gostR3411;
    public static final ASN1ObjectIdentifier gostR3411Hmac;
    public static final ASN1ObjectIdentifier gostR3411_94_CryptoProParamSet;
    public static final ASN1ObjectIdentifier gostR3411_94_with_gostR3410_2001;
    public static final ASN1ObjectIdentifier gostR3411_94_with_gostR3410_94;
    public static final ASN1ObjectIdentifier gost_ElSgDH3410_1;
    public static final ASN1ObjectIdentifier gost_ElSgDH3410_default;
    public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_A_ParamSet;
    public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_B_ParamSet;
    public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_C_ParamSet;
    public static final ASN1ObjectIdentifier id_Gost28147_89_CryptoPro_D_ParamSet;

    static {
        GOST_id = new ASN1ObjectIdentifier("1.2.643.2.2");
        gostR3411 = GOST_id.branch("9");
        gostR3411Hmac = GOST_id.branch(Constants.CLIENT_MINOR_VERSION);
        gostR28147_gcfb = GOST_id.branch("21");
        id_Gost28147_89_CryptoPro_A_ParamSet = GOST_id.branch("31.1");
        id_Gost28147_89_CryptoPro_B_ParamSet = GOST_id.branch("31.2");
        id_Gost28147_89_CryptoPro_C_ParamSet = GOST_id.branch("31.3");
        id_Gost28147_89_CryptoPro_D_ParamSet = GOST_id.branch("31.4");
        gostR3410_94 = GOST_id.branch("20");
        gostR3410_2001 = GOST_id.branch("19");
        gostR3411_94_with_gostR3410_94 = GOST_id.branch(DiscoverInAppCryptoData.DEFAULT_ECI_INDICATOR);
        gostR3411_94_with_gostR3410_2001 = GOST_id.branch(TransactionInfo.VISA_TRANSACTIONSTATUS_DECLINED);
        gostR3411_94_CryptoProParamSet = GOST_id.branch("30.1");
        gostR3410_94_CryptoPro_A = GOST_id.branch("32.2");
        gostR3410_94_CryptoPro_B = GOST_id.branch("32.3");
        gostR3410_94_CryptoPro_C = GOST_id.branch("32.4");
        gostR3410_94_CryptoPro_D = GOST_id.branch("32.5");
        gostR3410_94_CryptoPro_XchA = GOST_id.branch("33.1");
        gostR3410_94_CryptoPro_XchB = GOST_id.branch("33.2");
        gostR3410_94_CryptoPro_XchC = GOST_id.branch("33.3");
        gostR3410_2001_CryptoPro_A = GOST_id.branch("35.1");
        gostR3410_2001_CryptoPro_B = GOST_id.branch("35.2");
        gostR3410_2001_CryptoPro_C = GOST_id.branch("35.3");
        gostR3410_2001_CryptoPro_XchA = GOST_id.branch("36.0");
        gostR3410_2001_CryptoPro_XchB = GOST_id.branch("36.1");
        gost_ElSgDH3410_default = GOST_id.branch("36.0");
        gost_ElSgDH3410_1 = GOST_id.branch("36.1");
    }
}
