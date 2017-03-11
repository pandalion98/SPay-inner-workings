package com.mastercard.mobile_api.payment;

import com.google.android.gms.location.places.Place;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUConstants.MCCountryCode;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Locale;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;

public final class CurrencyTable {
    private static Hashtable currencyTable;

    static {
        currencyTable = new Hashtable();
        currencyTable.put(new Integer(1924), "AED");
        currencyTable.put(new Integer(2417), "AFN");
        currencyTable.put(new Integer(8), EnrollCardInfo.CARD_PRESENTATION_MODE_ALL);
        currencyTable.put(new Integer(81), "AMD");
        currencyTable.put(new Integer(1330), "ANG");
        currencyTable.put(new Integer(2419), "AOA");
        currencyTable.put(new Integer(50), "ARS");
        currencyTable.put(new Integer(54), "AUD");
        currencyTable.put(new Integer(1331), "AWG");
        currencyTable.put(new Integer(2372), "AZN");
        currencyTable.put(new Integer(2423), "BAM");
        currencyTable.put(new Integer(82), "BBD");
        currencyTable.put(new Integer(80), "BDT");
        currencyTable.put(new Integer(2421), "BGN");
        currencyTable.put(new Integer(72), "BHD");
        currencyTable.put(new Integer(McTACommands.MOP_MC_TA_CMD_CLEAR_MST_DATA), "BIF");
        currencyTable.put(new Integer(96), "BMD");
        currencyTable.put(new Integer(CipherSuite.TLS_RSA_WITH_SEED_CBC_SHA), "BND");
        currencyTable.put(new Integer(CipherSuite.TLS_DH_DSS_WITH_AES_256_CBC_SHA256), "BOB");
        currencyTable.put(new Integer(2436), "BOV");
        currencyTable.put(new Integer(2438), "BRL");
        currencyTable.put(new Integer(68), "BSD");
        currencyTable.put(new Integer(100), "BTN");
        currencyTable.put(new Integer(114), "BWP");
        currencyTable.put(new Integer(2420), "BYR");
        currencyTable.put(new Integer(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA), "BZD");
        currencyTable.put(new Integer(292), "CAD");
        currencyTable.put(new Integer(2422), "CDF");
        currencyTable.put(new Integer(2375), "CHE");
        currencyTable.put(new Integer(1878), "CHF");
        currencyTable.put(new Integer(2376), "CHW");
        currencyTable.put(new Integer(2448), "CLF");
        currencyTable.put(new Integer(338), "CLP");
        currencyTable.put(new Integer(342), "CNY");
        currencyTable.put(new Integer(368), "COP");
        currencyTable.put(new Integer(2416), "COU");
        currencyTable.put(new Integer(392), "CRC");
        currencyTable.put(new Integer(2353), "CUC");
        currencyTable.put(new Integer(402), "CUP");
        currencyTable.put(new Integer(306), "CVE");
        currencyTable.put(new Integer(McTACommands.MOP_MC_TA_CMD_CRYPTO_COMPUTE_CC), "CZK");
        currencyTable.put(new Integer(610), "DJF");
        currencyTable.put(new Integer(520), "DKK");
        currencyTable.put(new Integer(532), "DOP");
        currencyTable.put(new Integer(18), "DZD");
        currencyTable.put(new Integer(563), "EEK");
        currencyTable.put(new Integer(2072), "EGP");
        currencyTable.put(new Integer(562), "ERN");
        currencyTable.put(new Integer(560), "ETB");
        currencyTable.put(new Integer(2424), "\u20ac");
        currencyTable.put(new Integer(578), "FJD");
        currencyTable.put(new Integer(568), "FKP");
        currencyTable.put(new Integer(MCCountryCode.UK), "\u00a3");
        currencyTable.put(new Integer(2433), "GEL");
        currencyTable.put(new Integer(2358), "GHS");
        currencyTable.put(new Integer(658), "GIP");
        currencyTable.put(new Integer(624), "GMD");
        currencyTable.put(new Integer(804), "GNF");
        currencyTable.put(new Integer(800), "GTQ");
        currencyTable.put(new Integer(808), "GYD");
        currencyTable.put(new Integer(836), "HKD");
        currencyTable.put(new Integer(832), "HNL");
        currencyTable.put(new Integer(401), "HRK");
        currencyTable.put(new Integer(818), "HTG");
        currencyTable.put(new Integer(840), "HUF");
        currencyTable.put(new Integer(864), "IDR");
        currencyTable.put(new Integer(886), "ILS");
        currencyTable.put(new Integer(854), "INR");
        currencyTable.put(new Integer(872), "IQD");
        currencyTable.put(new Integer(868), "IRR");
        currencyTable.put(new Integer(850), "ISK");
        currencyTable.put(new Integer(904), "JMD");
        currencyTable.put(new Integer(SkeinMac.SKEIN_1024), "JOD");
        currencyTable.put(new Integer(914), "JPY");
        currencyTable.put(new Integer(Place.TYPE_SUBPREMISE), "KES");
        currencyTable.put(new Integer(1047), "KGS");
        currencyTable.put(new Integer(278), "KHR");
        currencyTable.put(new Integer(372), "KMF");
        currencyTable.put(new Integer(1032), "KPW");
        currencyTable.put(new Integer(1040), "KRW");
        currencyTable.put(new Integer(1044), "KWD");
        currencyTable.put(new Integer(310), "KYD");
        currencyTable.put(new Integer(920), "KZT");
        currencyTable.put(new Integer(1048), "LAK");
        currencyTable.put(new Integer(1058), "LBP");
        currencyTable.put(new Integer(324), "LKR");
        currencyTable.put(new Integer(1072), "LRD");
        currencyTable.put(new Integer(1062), "LSL");
        currencyTable.put(new Integer(1088), "LTL");
        currencyTable.put(new Integer(1064), "LVL");
        currencyTable.put(new Integer(1076), "LYD");
        currencyTable.put(new Integer(1284), "MAD");
        currencyTable.put(new Integer(1176), "MDL");
        currencyTable.put(new Integer(2409), "MGA");
        currencyTable.put(new Integer(2055), "MKD");
        currencyTable.put(new Integer(McTACommands.MOP_MC_TA_CMD_PROCESS_MST), "MMK");
        currencyTable.put(new Integer(1174), "MNT");
        currencyTable.put(new Integer(1094), "MOP");
        currencyTable.put(new Integer(1144), "MRO");
        currencyTable.put(new Integer(1152), "MUR");
        currencyTable.put(new Integer(1122), "MVR");
        currencyTable.put(new Integer(1108), "MWK");
        currencyTable.put(new Integer(1156), "MXN");
        currencyTable.put(new Integer(2425), "MXV");
        currencyTable.put(new Integer(1112), "MYR");
        currencyTable.put(new Integer(2371), "MZN");
        currencyTable.put(new Integer(1302), "NAD");
        currencyTable.put(new Integer(1382), "NGN");
        currencyTable.put(new Integer(1368), "NIO");
        currencyTable.put(new Integer(1400), "NOK");
        currencyTable.put(new Integer(1316), "NPR");
        currencyTable.put(new Integer(1364), "NZD");
        currencyTable.put(new Integer(1298), "OMR");
        currencyTable.put(new Integer(1424), "PAB");
        currencyTable.put(new Integer(1540), "PEN");
        currencyTable.put(new Integer(1432), "PGK");
        currencyTable.put(new Integer(1544), "PHP");
        currencyTable.put(new Integer(1414), "PKR");
        currencyTable.put(new Integer(2437), "PLN");
        currencyTable.put(new Integer(1536), "PYG");
        currencyTable.put(new Integer(1588), "QAR");
        currencyTable.put(new Integer(2374), "RON");
        currencyTable.put(new Integer(2369), "RSD");
        currencyTable.put(new Integer(1603), "RUB");
        currencyTable.put(new Integer(1606), "RWF");
        currencyTable.put(new Integer(1666), "SAR");
        currencyTable.put(new Integer(CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA), "SBD");
        currencyTable.put(new Integer(1680), "SCR");
        currencyTable.put(new Integer(2360), "SDG");
        currencyTable.put(new Integer(1874), "SEK");
        currencyTable.put(new Integer(1794), "SGD");
        currencyTable.put(new Integer(1620), "SHP");
        currencyTable.put(new Integer(1795), "SKK");
        currencyTable.put(new Integer(1684), "SLL");
        currencyTable.put(new Integer(1798), "SOS");
        currencyTable.put(new Integer(2408), "SRD");
        currencyTable.put(new Integer(1832), "SSP");
        currencyTable.put(new Integer(1656), "STD");
        currencyTable.put(new Integer(1888), "SYP");
        currencyTable.put(new Integer(1864), "SZL");
        currencyTable.put(new Integer(1892), "THB");
        currencyTable.put(new Integer(2418), "TJS");
        currencyTable.put(new Integer(1941), "TMT");
        currencyTable.put(new Integer(1928), "TND");
        currencyTable.put(new Integer(1910), "TOP");
        currencyTable.put(new Integer(2377), "TRY");
        currencyTable.put(new Integer(1920), "TTD");
        currencyTable.put(new Integer(2305), "TWD");
        currencyTable.put(new Integer(2100), "TZS");
        currencyTable.put(new Integer(2432), "UAH");
        currencyTable.put(new Integer(PKIFailureInfo.wrongIntegrity), "UGX");
        currencyTable.put(new Integer(MCCountryCode.US), "$");
        currencyTable.put(new Integer(2455), "USN");
        currencyTable.put(new Integer(2456), "USS");
        currencyTable.put(new Integer(2368), "UYI");
        currencyTable.put(new Integer(2136), "UYU");
        currencyTable.put(new Integer(2144), "UZS");
        currencyTable.put(new Integer(2359), "VEF");
        currencyTable.put(new Integer(1796), "VND");
        currencyTable.put(new Integer(1352), "VUV");
        currencyTable.put(new Integer(2178), "WST");
        currencyTable.put(new Integer(2384), "XAF");
        currencyTable.put(new Integer(2401), "XAG");
        currencyTable.put(new Integer(2393), "XAU");
        currencyTable.put(new Integer(2389), "XBA");
        currencyTable.put(new Integer(2390), "XBB");
        currencyTable.put(new Integer(2391), "XBC");
        currencyTable.put(new Integer(2392), "XBD");
        currencyTable.put(new Integer(2385), "XCD");
        currencyTable.put(new Integer(2400), "XDR");
        currencyTable.put(new Integer(2386), "XOF");
        currencyTable.put(new Integer(2404), "XPD");
        currencyTable.put(new Integer(2387), "XPF");
        currencyTable.put(new Integer(2402), "XPT");
        currencyTable.put(new Integer(2403), "XTS");
        currencyTable.put(new Integer(2457), "XXX");
        currencyTable.put(new Integer(2182), "YER");
        currencyTable.put(new Integer(1808), "ZAR");
        currencyTable.put(new Integer(2196), "ZMK");
        currencyTable.put(new Integer(2354), "ZWL");
    }

    public static String getCurrency(int i) {
        return (String) currencyTable.get(new Integer(i));
    }

    public static void main(String[] strArr) {
        NumberFormat.getCurrencyInstance(Locale.ENGLISH);
        System.out.println("a = " + new DecimalFormat("#.00", new DecimalFormatSymbols()).format(499));
    }
}
