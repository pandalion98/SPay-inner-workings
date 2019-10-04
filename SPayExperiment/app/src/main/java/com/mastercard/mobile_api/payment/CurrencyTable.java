/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintStream
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.text.DecimalFormat
 *  java.text.DecimalFormatSymbols
 *  java.text.NumberFormat
 *  java.util.Hashtable
 *  java.util.Locale
 */
package com.mastercard.mobile_api.payment;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Locale;

public final class CurrencyTable {
    private static Hashtable currencyTable = new Hashtable();

    static {
        currencyTable.put((Object)new Integer(1924), (Object)"AED");
        currencyTable.put((Object)new Integer(2417), (Object)"AFN");
        currencyTable.put((Object)new Integer(8), (Object)"ALL");
        currencyTable.put((Object)new Integer(81), (Object)"AMD");
        currencyTable.put((Object)new Integer(1330), (Object)"ANG");
        currencyTable.put((Object)new Integer(2419), (Object)"AOA");
        currencyTable.put((Object)new Integer(50), (Object)"ARS");
        currencyTable.put((Object)new Integer(54), (Object)"AUD");
        currencyTable.put((Object)new Integer(1331), (Object)"AWG");
        currencyTable.put((Object)new Integer(2372), (Object)"AZN");
        currencyTable.put((Object)new Integer(2423), (Object)"BAM");
        currencyTable.put((Object)new Integer(82), (Object)"BBD");
        currencyTable.put((Object)new Integer(80), (Object)"BDT");
        currencyTable.put((Object)new Integer(2421), (Object)"BGN");
        currencyTable.put((Object)new Integer(72), (Object)"BHD");
        currencyTable.put((Object)new Integer(264), (Object)"BIF");
        currencyTable.put((Object)new Integer(96), (Object)"BMD");
        currencyTable.put((Object)new Integer(150), (Object)"BND");
        currencyTable.put((Object)new Integer(104), (Object)"BOB");
        currencyTable.put((Object)new Integer(2436), (Object)"BOV");
        currencyTable.put((Object)new Integer(2438), (Object)"BRL");
        currencyTable.put((Object)new Integer(68), (Object)"BSD");
        currencyTable.put((Object)new Integer(100), (Object)"BTN");
        currencyTable.put((Object)new Integer(114), (Object)"BWP");
        currencyTable.put((Object)new Integer(2420), (Object)"BYR");
        currencyTable.put((Object)new Integer(132), (Object)"BZD");
        currencyTable.put((Object)new Integer(292), (Object)"CAD");
        currencyTable.put((Object)new Integer(2422), (Object)"CDF");
        currencyTable.put((Object)new Integer(2375), (Object)"CHE");
        currencyTable.put((Object)new Integer(1878), (Object)"CHF");
        currencyTable.put((Object)new Integer(2376), (Object)"CHW");
        currencyTable.put((Object)new Integer(2448), (Object)"CLF");
        currencyTable.put((Object)new Integer(338), (Object)"CLP");
        currencyTable.put((Object)new Integer(342), (Object)"CNY");
        currencyTable.put((Object)new Integer(368), (Object)"COP");
        currencyTable.put((Object)new Integer(2416), (Object)"COU");
        currencyTable.put((Object)new Integer(392), (Object)"CRC");
        currencyTable.put((Object)new Integer(2353), (Object)"CUC");
        currencyTable.put((Object)new Integer(402), (Object)"CUP");
        currencyTable.put((Object)new Integer(306), (Object)"CVE");
        currencyTable.put((Object)new Integer(515), (Object)"CZK");
        currencyTable.put((Object)new Integer(610), (Object)"DJF");
        currencyTable.put((Object)new Integer(520), (Object)"DKK");
        currencyTable.put((Object)new Integer(532), (Object)"DOP");
        currencyTable.put((Object)new Integer(18), (Object)"DZD");
        currencyTable.put((Object)new Integer(563), (Object)"EEK");
        currencyTable.put((Object)new Integer(2072), (Object)"EGP");
        currencyTable.put((Object)new Integer(562), (Object)"ERN");
        currencyTable.put((Object)new Integer(560), (Object)"ETB");
        currencyTable.put((Object)new Integer(2424), (Object)"\u20ac");
        currencyTable.put((Object)new Integer(578), (Object)"FJD");
        currencyTable.put((Object)new Integer(568), (Object)"FKP");
        currencyTable.put((Object)new Integer(2086), (Object)"\u00a3");
        currencyTable.put((Object)new Integer(2433), (Object)"GEL");
        currencyTable.put((Object)new Integer(2358), (Object)"GHS");
        currencyTable.put((Object)new Integer(658), (Object)"GIP");
        currencyTable.put((Object)new Integer(624), (Object)"GMD");
        currencyTable.put((Object)new Integer(804), (Object)"GNF");
        currencyTable.put((Object)new Integer(800), (Object)"GTQ");
        currencyTable.put((Object)new Integer(808), (Object)"GYD");
        currencyTable.put((Object)new Integer(836), (Object)"HKD");
        currencyTable.put((Object)new Integer(832), (Object)"HNL");
        currencyTable.put((Object)new Integer(401), (Object)"HRK");
        currencyTable.put((Object)new Integer(818), (Object)"HTG");
        currencyTable.put((Object)new Integer(840), (Object)"HUF");
        currencyTable.put((Object)new Integer(864), (Object)"IDR");
        currencyTable.put((Object)new Integer(886), (Object)"ILS");
        currencyTable.put((Object)new Integer(854), (Object)"INR");
        currencyTable.put((Object)new Integer(872), (Object)"IQD");
        currencyTable.put((Object)new Integer(868), (Object)"IRR");
        currencyTable.put((Object)new Integer(850), (Object)"ISK");
        currencyTable.put((Object)new Integer(904), (Object)"JMD");
        currencyTable.put((Object)new Integer(1024), (Object)"JOD");
        currencyTable.put((Object)new Integer(914), (Object)"JPY");
        currencyTable.put((Object)new Integer(1028), (Object)"KES");
        currencyTable.put((Object)new Integer(1047), (Object)"KGS");
        currencyTable.put((Object)new Integer(278), (Object)"KHR");
        currencyTable.put((Object)new Integer(372), (Object)"KMF");
        currencyTable.put((Object)new Integer(1032), (Object)"KPW");
        currencyTable.put((Object)new Integer(1040), (Object)"KRW");
        currencyTable.put((Object)new Integer(1044), (Object)"KWD");
        currencyTable.put((Object)new Integer(310), (Object)"KYD");
        currencyTable.put((Object)new Integer(920), (Object)"KZT");
        currencyTable.put((Object)new Integer(1048), (Object)"LAK");
        currencyTable.put((Object)new Integer(1058), (Object)"LBP");
        currencyTable.put((Object)new Integer(324), (Object)"LKR");
        currencyTable.put((Object)new Integer(1072), (Object)"LRD");
        currencyTable.put((Object)new Integer(1062), (Object)"LSL");
        currencyTable.put((Object)new Integer(1088), (Object)"LTL");
        currencyTable.put((Object)new Integer(1064), (Object)"LVL");
        currencyTable.put((Object)new Integer(1076), (Object)"LYD");
        currencyTable.put((Object)new Integer(1284), (Object)"MAD");
        currencyTable.put((Object)new Integer(1176), (Object)"MDL");
        currencyTable.put((Object)new Integer(2409), (Object)"MGA");
        currencyTable.put((Object)new Integer(2055), (Object)"MKD");
        currencyTable.put((Object)new Integer(260), (Object)"MMK");
        currencyTable.put((Object)new Integer(1174), (Object)"MNT");
        currencyTable.put((Object)new Integer(1094), (Object)"MOP");
        currencyTable.put((Object)new Integer(1144), (Object)"MRO");
        currencyTable.put((Object)new Integer(1152), (Object)"MUR");
        currencyTable.put((Object)new Integer(1122), (Object)"MVR");
        currencyTable.put((Object)new Integer(1108), (Object)"MWK");
        currencyTable.put((Object)new Integer(1156), (Object)"MXN");
        currencyTable.put((Object)new Integer(2425), (Object)"MXV");
        currencyTable.put((Object)new Integer(1112), (Object)"MYR");
        currencyTable.put((Object)new Integer(2371), (Object)"MZN");
        currencyTable.put((Object)new Integer(1302), (Object)"NAD");
        currencyTable.put((Object)new Integer(1382), (Object)"NGN");
        currencyTable.put((Object)new Integer(1368), (Object)"NIO");
        currencyTable.put((Object)new Integer(1400), (Object)"NOK");
        currencyTable.put((Object)new Integer(1316), (Object)"NPR");
        currencyTable.put((Object)new Integer(1364), (Object)"NZD");
        currencyTable.put((Object)new Integer(1298), (Object)"OMR");
        currencyTable.put((Object)new Integer(1424), (Object)"PAB");
        currencyTable.put((Object)new Integer(1540), (Object)"PEN");
        currencyTable.put((Object)new Integer(1432), (Object)"PGK");
        currencyTable.put((Object)new Integer(1544), (Object)"PHP");
        currencyTable.put((Object)new Integer(1414), (Object)"PKR");
        currencyTable.put((Object)new Integer(2437), (Object)"PLN");
        currencyTable.put((Object)new Integer(1536), (Object)"PYG");
        currencyTable.put((Object)new Integer(1588), (Object)"QAR");
        currencyTable.put((Object)new Integer(2374), (Object)"RON");
        currencyTable.put((Object)new Integer(2369), (Object)"RSD");
        currencyTable.put((Object)new Integer(1603), (Object)"RUB");
        currencyTable.put((Object)new Integer(1606), (Object)"RWF");
        currencyTable.put((Object)new Integer(1666), (Object)"SAR");
        currencyTable.put((Object)new Integer(144), (Object)"SBD");
        currencyTable.put((Object)new Integer(1680), (Object)"SCR");
        currencyTable.put((Object)new Integer(2360), (Object)"SDG");
        currencyTable.put((Object)new Integer(1874), (Object)"SEK");
        currencyTable.put((Object)new Integer(1794), (Object)"SGD");
        currencyTable.put((Object)new Integer(1620), (Object)"SHP");
        currencyTable.put((Object)new Integer(1795), (Object)"SKK");
        currencyTable.put((Object)new Integer(1684), (Object)"SLL");
        currencyTable.put((Object)new Integer(1798), (Object)"SOS");
        currencyTable.put((Object)new Integer(2408), (Object)"SRD");
        currencyTable.put((Object)new Integer(1832), (Object)"SSP");
        currencyTable.put((Object)new Integer(1656), (Object)"STD");
        currencyTable.put((Object)new Integer(1888), (Object)"SYP");
        currencyTable.put((Object)new Integer(1864), (Object)"SZL");
        currencyTable.put((Object)new Integer(1892), (Object)"THB");
        currencyTable.put((Object)new Integer(2418), (Object)"TJS");
        currencyTable.put((Object)new Integer(1941), (Object)"TMT");
        currencyTable.put((Object)new Integer(1928), (Object)"TND");
        currencyTable.put((Object)new Integer(1910), (Object)"TOP");
        currencyTable.put((Object)new Integer(2377), (Object)"TRY");
        currencyTable.put((Object)new Integer(1920), (Object)"TTD");
        currencyTable.put((Object)new Integer(2305), (Object)"TWD");
        currencyTable.put((Object)new Integer(2100), (Object)"TZS");
        currencyTable.put((Object)new Integer(2432), (Object)"UAH");
        currencyTable.put((Object)new Integer(2048), (Object)"UGX");
        currencyTable.put((Object)new Integer(2112), (Object)"$");
        currencyTable.put((Object)new Integer(2455), (Object)"USN");
        currencyTable.put((Object)new Integer(2456), (Object)"USS");
        currencyTable.put((Object)new Integer(2368), (Object)"UYI");
        currencyTable.put((Object)new Integer(2136), (Object)"UYU");
        currencyTable.put((Object)new Integer(2144), (Object)"UZS");
        currencyTable.put((Object)new Integer(2359), (Object)"VEF");
        currencyTable.put((Object)new Integer(1796), (Object)"VND");
        currencyTable.put((Object)new Integer(1352), (Object)"VUV");
        currencyTable.put((Object)new Integer(2178), (Object)"WST");
        currencyTable.put((Object)new Integer(2384), (Object)"XAF");
        currencyTable.put((Object)new Integer(2401), (Object)"XAG");
        currencyTable.put((Object)new Integer(2393), (Object)"XAU");
        currencyTable.put((Object)new Integer(2389), (Object)"XBA");
        currencyTable.put((Object)new Integer(2390), (Object)"XBB");
        currencyTable.put((Object)new Integer(2391), (Object)"XBC");
        currencyTable.put((Object)new Integer(2392), (Object)"XBD");
        currencyTable.put((Object)new Integer(2385), (Object)"XCD");
        currencyTable.put((Object)new Integer(2400), (Object)"XDR");
        currencyTable.put((Object)new Integer(2386), (Object)"XOF");
        currencyTable.put((Object)new Integer(2404), (Object)"XPD");
        currencyTable.put((Object)new Integer(2387), (Object)"XPF");
        currencyTable.put((Object)new Integer(2402), (Object)"XPT");
        currencyTable.put((Object)new Integer(2403), (Object)"XTS");
        currencyTable.put((Object)new Integer(2457), (Object)"XXX");
        currencyTable.put((Object)new Integer(2182), (Object)"YER");
        currencyTable.put((Object)new Integer(1808), (Object)"ZAR");
        currencyTable.put((Object)new Integer(2196), (Object)"ZMK");
        currencyTable.put((Object)new Integer(2354), (Object)"ZWL");
    }

    public static String getCurrency(int n2) {
        return (String)currencyTable.get((Object)new Integer(n2));
    }

    public static void main(String[] arrstring) {
        NumberFormat.getCurrencyInstance((Locale)Locale.ENGLISH);
        DecimalFormat decimalFormat = new DecimalFormat("#.00", new DecimalFormatSymbols());
        System.out.println("a = " + decimalFormat.format(499L));
    }
}

