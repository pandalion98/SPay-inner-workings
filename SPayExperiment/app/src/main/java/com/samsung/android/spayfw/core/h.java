/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.core;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.MstPayConfig;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.b.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class h {
    private static Map<String, b> jv = new HashMap();
    private static int jw = -1;
    private static int jx = 1;

    public static String[] C(String string) {
        if (string != null) {
            Object[] arrobject = string.replaceAll("\\t\\n\\r\\f", "").trim().split(";");
            for (int i2 = 0; i2 < arrobject.length; ++i2) {
                arrobject[i2] = arrobject[i2].trim();
            }
            Log.d("PayConfigurator", "getSanitizedSequence: sequence.arr = " + Arrays.toString((Object[])arrobject) + "; len = " + arrobject.length);
            return arrobject;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static a D(String string) {
        String string2 = string.substring(0, -1 + string.length());
        Log.d("PayConfigurator", "mstConfig :" + string2);
        a a2 = new a();
        String[] arrstring = string2.split(",");
        int n2 = 0;
        while (n2 < arrstring.length) {
            String string3 = arrstring[n2].substring(1);
            switch (string3.charAt(0)) {
                case 't': {
                    a2.track = h.E(string3);
                    Log.d("PayConfigurator", "Track: " + a2.track);
                    break;
                }
                case 'r': {
                    a2.jy = Integer.parseInt((String)string3.substring(1));
                    break;
                }
                case 'L': {
                    if (string3.charAt(1) == 'Z') {
                        a2.leadingZeros = Integer.parseInt((String)string3.substring(2));
                    }
                    if (string3.charAt(1) == 'P') {
                        a2.extraParams.putInt("panLength", Integer.parseInt((String)string3.substring(2)));
                        Log.d("PayConfigurator", "PAN Length:" + Integer.parseInt((String)string3.substring(2)));
                        break;
                    }
                    if (string3.charAt(1) == 'N') {
                        a2.extraParams.putInt("nameLength", Integer.parseInt((String)string3.substring(2)));
                        Log.d("PayConfigurator", "Name Length:" + Integer.parseInt((String)string3.substring(2)));
                        break;
                    }
                    if (string3.charAt(1) != 'D') break;
                    a2.extraParams.putInt("dataLength", Integer.parseInt((String)string3.substring(2)));
                    Log.d("PayConfigurator", "Data Length:" + Integer.parseInt((String)string3.substring(2)));
                    break;
                }
                case 'T': {
                    if (string3.charAt(1) != 'Z') break;
                    a2.trailingZeros = Integer.parseInt((String)string3.substring(2));
                    break;
                }
                case 'R': {
                    a2.reverse = true;
                    break;
                }
                case 'D': {
                    a2.delay = Integer.parseInt((String)string3.substring(1));
                    break;
                }
            }
            ++n2;
        }
        return a2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int E(String string) {
        if (string.length() > 2 && string.charAt(1) == '1') {
            int n2 = -1;
            switch (string.hashCode()) {
                case 113105: {
                    if (!string.equals((Object)"t1n")) break;
                    n2 = 0;
                    break;
                }
            }
            switch (n2) {
                default: {
                    Log.w("PayConfigurator", "getTrackValue: incorrect track, defaulting to track 1 : " + string);
                    return 1;
                }
                case 0: 
            }
            return 255;
        }
        return Integer.parseInt((String)string.substring(1));
    }

    public static int av() {
        return jw;
    }

    public static int aw() {
        return jx;
    }

    public static PayConfig b(String string, String string2) {
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty()) {
            Log.e("PayConfigurator", "getPayConfigByType: configType or cardBrand is null or empty!");
            return null;
        }
        if (jv == null || jv.get((Object)string) == null) {
            Log.e("PayConfigurator", "getPayConfigByType: mTrackDataHash=null or value=null for key = " + string);
            return null;
        }
        String string3 = string2 + "_" + string;
        Log.i("PayConfigurator", "getPayConfigByType: cardBrandConfigType : " + string3);
        if (jv.containsKey((Object)string3)) {
            return ((b)h.jv.get((Object)string3)).js;
        }
        return ((b)h.jv.get((Object)string)).js;
    }

    public static int c(String string, String string2) {
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty()) {
            Log.e("PayConfigurator", "getMstTransmitTimeForType: configType or cardBrand is null or empty!");
            return -1;
        }
        if (jv == null || jv.get((Object)string) == null) {
            Log.e("PayConfigurator", "getMstTransmitTimeForType: mTrackDataHash=null or value=null for key = " + string);
            return -1;
        }
        String string3 = string2 + "_" + string;
        if (jv.containsKey((Object)string3)) {
            return ((b)h.jv.get((Object)string3)).jt;
        }
        return ((b)h.jv.get((Object)string)).jt;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static PayConfig c(String[] arrstring) {
        PayConfig payConfig = new PayConfig();
        ArrayList arrayList = new ArrayList();
        MstPayConfig mstPayConfig = new MstPayConfig();
        mstPayConfig.setMstPayConfigEntry((List<MstPayConfigEntry>)arrayList);
        payConfig.setPayType(2);
        payConfig.setMstPayConfig(mstPayConfig);
        payConfig.setPayIdleTime(1500);
        int n2 = 0;
        block0 : while (arrstring.length > n2) {
            ArrayList arrayList2 = new ArrayList();
            int n3 = n2;
            do {
                Log.d("PayConfigurator", "getPayConfig: i = " + n3 + "; mMstSequence = " + arrstring[n3]);
                n2 = n3 + 1;
                a a2 = h.D(arrstring[n3]);
                MstPayConfigEntryItem mstPayConfigEntryItem = new MstPayConfigEntryItem();
                if (!a2.reverse) {
                    mstPayConfigEntryItem.setDirection(0);
                } else {
                    mstPayConfigEntryItem.setDirection(1);
                }
                mstPayConfigEntryItem.setTrackIndex(a2.track);
                mstPayConfigEntryItem.setLeadingZeros(a2.leadingZeros);
                mstPayConfigEntryItem.setTrailingZeros(a2.trailingZeros);
                mstPayConfigEntryItem.setExtraParams(a2.extraParams);
                arrayList2.add((Object)mstPayConfigEntryItem);
                if (a2.delay != 0 || arrstring.length <= n2) {
                    MstPayConfigEntry mstPayConfigEntry = new MstPayConfigEntry();
                    mstPayConfigEntry.setDelayBetweenRepeat(a2.delay);
                    mstPayConfigEntry.setBaudRate(a2.jy);
                    mstPayConfigEntry.setMstPayConfigEntry((List<MstPayConfigEntryItem>)arrayList2);
                    arrayList.add((Object)mstPayConfigEntry);
                    continue block0;
                }
                n3 = n2;
            } while (true);
            break;
        }
        return payConfig;
    }

    public static String f(String string, String string2) {
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty()) {
            Log.e("PayConfigurator", "getMstSequenceIdForType: configType or cardBrand is null or empty!");
            return null;
        }
        if (jv == null || jv.get((Object)string) == null) {
            Log.e("PayConfigurator", "getMstSequenceIdForType: mTrackDataHash=null or value=null for key = " + string);
            return null;
        }
        String string3 = string2 + "_" + string;
        if (jv.containsKey((Object)string3)) {
            return ((b)h.jv.get((Object)string3)).mstSequenceId;
        }
        return ((b)h.jv.get((Object)string)).mstSequenceId;
    }

    /*
     * Exception decompiling
     */
    static void k(Context var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: First case is not immediately after switch.
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:358)
        // org.benf.cfr.reader.b.a.a.b.as.a(SwitchReplacer.java:328)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:462)
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

    private static class a {
        int delay = 0;
        Bundle extraParams = new Bundle();
        int jy = 0;
        int leadingZeros = 0;
        boolean reverse = false;
        int track = 0;
        int trailingZeros = 0;
    }

    private static class b {
        PayConfig js;
        int jt;
        String mstSequenceId;

        private b() {
        }
    }

}

