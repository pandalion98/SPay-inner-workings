package org.bouncycastle.asn1.cryptopro;

import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECCurve.Fp;

public class ECGOST3410NamedCurves {
    static final Hashtable names;
    static final Hashtable objIds;
    static final Hashtable params;

    static {
        objIds = new Hashtable();
        params = new Hashtable();
        names = new Hashtable();
        BigInteger bigInteger = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639319");
        BigInteger bigInteger2 = new BigInteger("115792089237316195423570985008687907853073762908499243225378155805079068850323");
        ECCurve fp = new Fp(bigInteger, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"), bigInteger2, ECConstants.ONE);
        params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, new ECDomainParameters(fp, fp.createPoint(new BigInteger(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612")), bigInteger2));
        bigInteger = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639319");
        bigInteger2 = new BigInteger("115792089237316195423570985008687907853073762908499243225378155805079068850323");
        fp = new Fp(bigInteger, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"), bigInteger2, ECConstants.ONE);
        params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, new ECDomainParameters(fp, fp.createPoint(new BigInteger(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612")), bigInteger2));
        bigInteger = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823193");
        bigInteger2 = new BigInteger("57896044618658097711785492504343953927102133160255826820068844496087732066703");
        fp = new Fp(bigInteger, new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823190"), new BigInteger("28091019353058090096996979000309560759124368558014865957655842872397301267595"), bigInteger2, ECConstants.ONE);
        params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, new ECDomainParameters(fp, fp.createPoint(new BigInteger(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND), new BigInteger("28792665814854611296992347458380284135028636778229113005756334730996303888124")), bigInteger2));
        bigInteger = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
        bigInteger2 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
        fp = new Fp(bigInteger, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"), bigInteger2, ECConstants.ONE);
        params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, new ECDomainParameters(fp, fp.createPoint(new BigInteger(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE), new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247")), bigInteger2));
        bigInteger = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
        bigInteger2 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
        fp = new Fp(bigInteger, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"), bigInteger2, ECConstants.ONE);
        params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, new ECDomainParameters(fp, fp.createPoint(new BigInteger(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE), new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247")), bigInteger2));
        objIds.put("GostR3410-2001-CryptoPro-A", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A);
        objIds.put("GostR3410-2001-CryptoPro-B", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B);
        objIds.put("GostR3410-2001-CryptoPro-C", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C);
        objIds.put("GostR3410-2001-CryptoPro-XchA", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA);
        objIds.put("GostR3410-2001-CryptoPro-XchB", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB);
        names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, "GostR3410-2001-CryptoPro-A");
        names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, "GostR3410-2001-CryptoPro-B");
        names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, "GostR3410-2001-CryptoPro-C");
        names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, "GostR3410-2001-CryptoPro-XchA");
        names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, "GostR3410-2001-CryptoPro-XchB");
    }

    public static ECDomainParameters getByName(String str) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier) objIds.get(str);
        return aSN1ObjectIdentifier != null ? (ECDomainParameters) params.get(aSN1ObjectIdentifier) : null;
    }

    public static ECDomainParameters getByOID(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (ECDomainParameters) params.get(aSN1ObjectIdentifier);
    }

    public static String getName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (String) names.get(aSN1ObjectIdentifier);
    }

    public static Enumeration getNames() {
        return objIds.keys();
    }

    public static ASN1ObjectIdentifier getOID(String str) {
        return (ASN1ObjectIdentifier) objIds.get(str);
    }
}
