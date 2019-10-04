/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Comparator
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Set
 *  java.util.TreeSet
 */
package com.samsung.android.spayfw.core;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.utils.h;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BinAttribute {
    private static BinAttributeJson BIN_ATTR_JSON;
    private static final Set<BinAttribute> BIN_SET;
    private static final String CONFIG_FILE = "BinAttribute.json";
    private static final Map<String, BinAttribute[]> COUNTRY_MAP;
    private static final int FLAG_BILLING_INFO_REQUIRED = 1;
    private static final int FLAG_CVV_REQUIRED = 4;
    private static final int FLAG_EXPIRY_REQUIRED = 8;
    private static final int FLAG_ZIP_REQUIRED = 2;
    private static String SERVER_BIN_VERSION;
    private static final String TAG = "BinAttribute";
    private static final Gson sGson;
    private String bin;
    private String cardBrand;
    private int eczb;

    static {
        sGson = new GsonBuilder().disableHtmlEscaping().create();
        BIN_SET = new TreeSet((Comparator)new Comparator<BinAttribute>(){

            public int compare(BinAttribute binAttribute, BinAttribute binAttribute2) {
                return Integer.parseInt((String)binAttribute2.bin) - Integer.parseInt((String)binAttribute.bin);
            }
        });
        COUNTRY_MAP = new HashMap();
        SERVER_BIN_VERSION = "";
    }

    private BinAttribute(String string, String string2, int n2) {
        this.bin = string;
        this.cardBrand = string2;
        this.eczb = n2;
    }

    public static final BinAttribute getBinAttribute(String string) {
        for (BinAttribute binAttribute : BIN_SET) {
            if (!string.startsWith(binAttribute.getBin())) continue;
            c.d(TAG, binAttribute.toString());
            String string2 = h.fP();
            c.d(TAG, "CC2 : " + string2);
            BinAttribute binAttribute2 = new BinAttribute(binAttribute.bin, binAttribute.cardBrand, binAttribute.eczb);
            if (COUNTRY_MAP.get((Object)string2) != null) {
                BinAttribute[] arrbinAttribute = (BinAttribute[])COUNTRY_MAP.get((Object)string2);
                int n2 = arrbinAttribute.length;
                for (int i2 = 0; i2 < n2; ++i2) {
                    BinAttribute binAttribute3 = arrbinAttribute[i2];
                    if (binAttribute3.bin == null || !string.startsWith(binAttribute3.bin)) continue;
                    binAttribute2.eczb = binAttribute3.eczb;
                    return binAttribute2;
                }
                int n3 = arrbinAttribute.length;
                for (int i3 = 0; i3 < n3; ++i3) {
                    BinAttribute binAttribute4 = arrbinAttribute[i3];
                    if (binAttribute4.cardBrand == null || !binAttribute2.cardBrand.equals((Object)binAttribute4.cardBrand)) continue;
                    binAttribute2.eczb = binAttribute4.eczb;
                    return binAttribute2;
                }
                for (BinAttribute binAttribute5 : arrbinAttribute) {
                    if (binAttribute5.cardBrand != null || binAttribute5.bin != null) continue;
                    binAttribute2.eczb = binAttribute5.eczb;
                    return binAttribute2;
                }
                continue;
            }
            return binAttribute2;
        }
        return null;
    }

    public static final Set<BinAttribute> getBinSet() {
        return BIN_SET;
    }

    public static String getServerBinVersion() {
        return SERVER_BIN_VERSION;
    }

    /*
     * Exception decompiling
     */
    public static final void init(Context var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public static final boolean matches(String string) {
        Iterator iterator = BIN_SET.iterator();
        while (iterator.hasNext()) {
            if (!string.startsWith(((BinAttribute)iterator.next()).getBin())) continue;
            return true;
        }
        return false;
    }

    public static void setServerBinVersion(String string) {
        SERVER_BIN_VERSION = string;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) {
            return false;
        }
        if (!(object instanceof BinAttribute)) {
            return false;
        }
        BinAttribute binAttribute = (BinAttribute)object;
        if (this.bin == null) {
            if (binAttribute.bin == null) return true;
            return false;
        }
        if (!this.bin.equals((Object)binAttribute.bin)) return false;
        return true;
    }

    public String getBin() {
        return this.bin;
    }

    public String getCardBrand() {
        return this.cardBrand;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int hashCode() {
        int n2;
        if (this.bin == null) {
            n2 = 0;
            do {
                return n2 + 31;
                break;
            } while (true);
        }
        n2 = this.bin.hashCode();
        return n2 + 31;
    }

    public boolean isBillingInfoRequired() {
        return (1 & this.eczb) != 0;
    }

    public boolean isCvvRequired() {
        return (4 & this.eczb) != 0;
    }

    public boolean isExpiryRequired() {
        return (8 & this.eczb) != 0;
    }

    public boolean isZipRequired() {
        return (2 & this.eczb) != 0;
    }

    public String toString() {
        return "BinAttribute{bin='" + this.bin + '\'' + ", cardBrand='" + this.cardBrand + '\'' + ", eczb=" + this.eczb + '}';
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

}

