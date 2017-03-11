package com.samsung.android.spayfw.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BinAttribute {
    private static BinAttributeJson BIN_ATTR_JSON = null;
    private static final Set<BinAttribute> BIN_SET;
    private static final String CONFIG_FILE = "BinAttribute.json";
    private static final Map<String, BinAttribute[]> COUNTRY_MAP;
    private static final int FLAG_BILLING_INFO_REQUIRED = 1;
    private static final int FLAG_CVV_REQUIRED = 4;
    private static final int FLAG_EXPIRY_REQUIRED = 8;
    private static final int FLAG_ZIP_REQUIRED = 2;
    private static String SERVER_BIN_VERSION = null;
    private static final String TAG = "BinAttribute";
    private static final Gson sGson;
    private String bin;
    private String cardBrand;
    private int eczb;

    /* renamed from: com.samsung.android.spayfw.core.BinAttribute.1 */
    static class C04041 implements Comparator<BinAttribute> {
        C04041() {
        }

        public int compare(BinAttribute binAttribute, BinAttribute binAttribute2) {
            return Integer.parseInt(binAttribute2.bin) - Integer.parseInt(binAttribute.bin);
        }
    }

    private static final class BinAttributeJson {
        BinAttribute[] binAttributes;
        CountryAttributeJson[] countryAttributes;

        private BinAttributeJson() {
        }
    }

    private static final class CountryAttributeJson {
        BinAttribute[] binAttributes;
        String cc2;

        private CountryAttributeJson() {
        }
    }

    static {
        sGson = new GsonBuilder().disableHtmlEscaping().create();
        BIN_SET = new TreeSet(new C04041());
        COUNTRY_MAP = new HashMap();
        SERVER_BIN_VERSION = BuildConfig.FLAVOR;
    }

    public static final BinAttribute getBinAttribute(String str) {
        for (BinAttribute binAttribute : BIN_SET) {
            if (str.startsWith(binAttribute.getBin())) {
                Log.m285d(TAG, binAttribute.toString());
                String fP = Utils.fP();
                Log.m285d(TAG, "CC2 : " + fP);
                BinAttribute binAttribute2 = new BinAttribute(binAttribute.bin, binAttribute.cardBrand, binAttribute.eczb);
                if (COUNTRY_MAP.get(fP) == null) {
                    return binAttribute2;
                }
                BinAttribute binAttribute3;
                BinAttribute[] binAttributeArr = (BinAttribute[]) COUNTRY_MAP.get(fP);
                int length = binAttributeArr.length;
                int i = 0;
                while (i < length) {
                    binAttribute3 = binAttributeArr[i];
                    if (binAttribute3.bin == null || !str.startsWith(binAttribute3.bin)) {
                        i += FLAG_BILLING_INFO_REQUIRED;
                    } else {
                        binAttribute2.eczb = binAttribute3.eczb;
                        return binAttribute2;
                    }
                }
                length = binAttributeArr.length;
                i = 0;
                while (i < length) {
                    binAttribute3 = binAttributeArr[i];
                    if (binAttribute3.cardBrand == null || !binAttribute2.cardBrand.equals(binAttribute3.cardBrand)) {
                        i += FLAG_BILLING_INFO_REQUIRED;
                    } else {
                        binAttribute2.eczb = binAttribute3.eczb;
                        return binAttribute2;
                    }
                }
                length = binAttributeArr.length;
                for (i = 0; i < length; i += FLAG_BILLING_INFO_REQUIRED) {
                    binAttribute3 = binAttributeArr[i];
                    if (binAttribute3.cardBrand == null && binAttribute3.bin == null) {
                        binAttribute2.eczb = binAttribute3.eczb;
                        return binAttribute2;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static final Set<BinAttribute> getBinSet() {
        return BIN_SET;
    }

    public static final boolean matches(String str) {
        for (BinAttribute bin : BIN_SET) {
            if (str.startsWith(bin.getBin())) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void init(android.content.Context r9) {
        /*
        r2 = 0;
        r3 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x006d }
        r0 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x006d }
        r1 = r9.getAssets();	 Catch:{ Exception -> 0x006d }
        r4 = "BinAttribute.json";
        r1 = r1.open(r4);	 Catch:{ Exception -> 0x006d }
        r0.<init>(r1);	 Catch:{ Exception -> 0x006d }
        r3.<init>(r0);	 Catch:{ Exception -> 0x006d }
        r1 = 0;
        r0 = sGson;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r4 = com.samsung.android.spayfw.core.BinAttribute.BinAttributeJson.class;
        r0 = r0.fromJson(r3, r4);	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r0 = (com.samsung.android.spayfw.core.BinAttribute.BinAttributeJson) r0;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        BIN_ATTR_JSON = r0;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r0 = BIN_ATTR_JSON;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r4 = r0.binAttributes;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r5 = r4.length;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r0 = r2;
    L_0x0028:
        if (r0 >= r5) goto L_0x0034;
    L_0x002a:
        r6 = r4[r0];	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r7 = BIN_SET;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r7.add(r6);	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r0 = r0 + 1;
        goto L_0x0028;
    L_0x0034:
        r0 = "BinAttribute";
        r4 = BIN_SET;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r4 = r4.toString();	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r4);	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r0 = BIN_ATTR_JSON;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r4 = r0.countryAttributes;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r5 = r4.length;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r0 = r2;
    L_0x0045:
        if (r0 >= r5) goto L_0x0055;
    L_0x0047:
        r2 = r4[r0];	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r6 = COUNTRY_MAP;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r7 = r2.cc2;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r2 = r2.binAttributes;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r6.put(r7, r2);	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r0 = r0 + 1;
        goto L_0x0045;
    L_0x0055:
        r0 = "BinAttribute";
        r2 = COUNTRY_MAP;	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        r2 = r2.toString();	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ Throwable -> 0x007c, all -> 0x0093 }
        if (r3 == 0) goto L_0x0067;
    L_0x0062:
        if (r1 == 0) goto L_0x0078;
    L_0x0064:
        r3.close();	 Catch:{ Throwable -> 0x0068 }
    L_0x0067:
        return;
    L_0x0068:
        r0 = move-exception;
        r1.addSuppressed(r0);	 Catch:{ Exception -> 0x006d }
        goto L_0x0067;
    L_0x006d:
        r0 = move-exception;
        r1 = "BinAttribute";
        r2 = r0.getMessage();
        com.samsung.android.spayfw.p002b.Log.m284c(r1, r2, r0);
        goto L_0x0067;
    L_0x0078:
        r3.close();	 Catch:{ Exception -> 0x006d }
        goto L_0x0067;
    L_0x007c:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x007e }
    L_0x007e:
        r1 = move-exception;
        r8 = r1;
        r1 = r0;
        r0 = r8;
    L_0x0082:
        if (r3 == 0) goto L_0x0089;
    L_0x0084:
        if (r1 == 0) goto L_0x008f;
    L_0x0086:
        r3.close();	 Catch:{ Throwable -> 0x008a }
    L_0x0089:
        throw r0;	 Catch:{ Exception -> 0x006d }
    L_0x008a:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ Exception -> 0x006d }
        goto L_0x0089;
    L_0x008f:
        r3.close();	 Catch:{ Exception -> 0x006d }
        goto L_0x0089;
    L_0x0093:
        r0 = move-exception;
        goto L_0x0082;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.BinAttribute.init(android.content.Context):void");
    }

    private BinAttribute(String str, String str2, int i) {
        this.bin = str;
        this.cardBrand = str2;
        this.eczb = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BinAttribute)) {
            return false;
        }
        BinAttribute binAttribute = (BinAttribute) obj;
        if (this.bin == null) {
            if (binAttribute.bin != null) {
                return false;
            }
            return true;
        } else if (this.bin.equals(binAttribute.bin)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (this.bin == null ? 0 : this.bin.hashCode()) + 31;
    }

    public String toString() {
        return "BinAttribute{bin='" + this.bin + '\'' + ", cardBrand='" + this.cardBrand + '\'' + ", eczb=" + this.eczb + '}';
    }

    public String getBin() {
        return this.bin;
    }

    public boolean isCvvRequired() {
        return (this.eczb & FLAG_CVV_REQUIRED) != 0;
    }

    public boolean isExpiryRequired() {
        return (this.eczb & FLAG_EXPIRY_REQUIRED) != 0;
    }

    public boolean isZipRequired() {
        return (this.eczb & FLAG_ZIP_REQUIRED) != 0;
    }

    public boolean isBillingInfoRequired() {
        return (this.eczb & FLAG_BILLING_INFO_REQUIRED) != 0;
    }

    public String getCardBrand() {
        return this.cardBrand;
    }

    public static String getServerBinVersion() {
        return SERVER_BIN_VERSION;
    }

    public static void setServerBinVersion(String str) {
        SERVER_BIN_VERSION = str;
    }
}
