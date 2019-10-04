/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  org.bouncycastle.math.ec.ECCurve
 *  org.bouncycastle.math.ec.ECCurve$Fp
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.asn1.cryptopro;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

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
        ECCurve.Fp fp = new ECCurve.Fp(bigInteger, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"), bigInteger2, ECConstants.ONE);
        ECDomainParameters eCDomainParameters = new ECDomainParameters((ECCurve)fp, fp.createPoint(new BigInteger("1"), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612")), bigInteger2);
        params.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, (Object)eCDomainParameters);
        BigInteger bigInteger3 = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639319");
        BigInteger bigInteger4 = new BigInteger("115792089237316195423570985008687907853073762908499243225378155805079068850323");
        ECCurve.Fp fp2 = new ECCurve.Fp(bigInteger3, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"), bigInteger4, ECConstants.ONE);
        ECDomainParameters eCDomainParameters2 = new ECDomainParameters((ECCurve)fp2, fp2.createPoint(new BigInteger("1"), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612")), bigInteger4);
        params.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, (Object)eCDomainParameters2);
        BigInteger bigInteger5 = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823193");
        BigInteger bigInteger6 = new BigInteger("57896044618658097711785492504343953927102133160255826820068844496087732066703");
        ECCurve.Fp fp3 = new ECCurve.Fp(bigInteger5, new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823190"), new BigInteger("28091019353058090096996979000309560759124368558014865957655842872397301267595"), bigInteger6, ECConstants.ONE);
        ECDomainParameters eCDomainParameters3 = new ECDomainParameters((ECCurve)fp3, fp3.createPoint(new BigInteger("1"), new BigInteger("28792665814854611296992347458380284135028636778229113005756334730996303888124")), bigInteger6);
        params.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, (Object)eCDomainParameters3);
        BigInteger bigInteger7 = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
        BigInteger bigInteger8 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
        ECCurve.Fp fp4 = new ECCurve.Fp(bigInteger7, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"), bigInteger8, ECConstants.ONE);
        ECDomainParameters eCDomainParameters4 = new ECDomainParameters((ECCurve)fp4, fp4.createPoint(new BigInteger("0"), new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247")), bigInteger8);
        params.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, (Object)eCDomainParameters4);
        BigInteger bigInteger9 = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
        BigInteger bigInteger10 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
        ECCurve.Fp fp5 = new ECCurve.Fp(bigInteger9, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"), bigInteger10, ECConstants.ONE);
        ECDomainParameters eCDomainParameters5 = new ECDomainParameters((ECCurve)fp5, fp5.createPoint(new BigInteger("0"), new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247")), bigInteger10);
        params.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, (Object)eCDomainParameters5);
        objIds.put((Object)"GostR3410-2001-CryptoPro-A", (Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A);
        objIds.put((Object)"GostR3410-2001-CryptoPro-B", (Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B);
        objIds.put((Object)"GostR3410-2001-CryptoPro-C", (Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C);
        objIds.put((Object)"GostR3410-2001-CryptoPro-XchA", (Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA);
        objIds.put((Object)"GostR3410-2001-CryptoPro-XchB", (Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB);
        names.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, (Object)"GostR3410-2001-CryptoPro-A");
        names.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, (Object)"GostR3410-2001-CryptoPro-B");
        names.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, (Object)"GostR3410-2001-CryptoPro-C");
        names.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, (Object)"GostR3410-2001-CryptoPro-XchA");
        names.put((Object)CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, (Object)"GostR3410-2001-CryptoPro-XchB");
    }

    public static ECDomainParameters getByName(String string) {
        ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)objIds.get((Object)string);
        if (aSN1ObjectIdentifier != null) {
            return (ECDomainParameters)params.get((Object)aSN1ObjectIdentifier);
        }
        return null;
    }

    public static ECDomainParameters getByOID(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (ECDomainParameters)params.get((Object)aSN1ObjectIdentifier);
    }

    public static String getName(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return (String)names.get((Object)aSN1ObjectIdentifier);
    }

    public static Enumeration getNames() {
        return objIds.keys();
    }

    public static ASN1ObjectIdentifier getOID(String string) {
        return (ASN1ObjectIdentifier)objIds.get((Object)string);
    }
}

