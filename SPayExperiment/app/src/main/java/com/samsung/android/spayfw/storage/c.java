/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  java.lang.Class
 *  java.lang.Double
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CacheMetaData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Wifi;
import com.samsung.android.spayfw.storage.DbAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class c
extends DbAdapter {
    private static c Cc;

    private c(Context context) {
        super(context);
        this.execSQL("CREATE TABLE IF NOT EXISTS rsc_cache_meta_data (id TEXT NOT NULL, type TEXT NOT NULL, href TEXT NOT NULL, updated_at TEXT NOT NULL, PRIMARY KEY (id, type) )");
        this.execSQL("CREATE TABLE IF NOT EXISTS rsc_cache_wifi_scan (cache_id TEXT NOT NULL, scan_id TEXT NOT NULL, store_name TEXT NOT NULL, bssid TEXT NOT NULL, ssid TEXT NOT NULL, rssi INTEGER, frequency INTEGER, distance REAL, mst_sequence_id TEXT NOT NULL )");
        this.execSQL("CREATE TABLE IF NOT EXISTS rsc_cache_mst_sequence (key TEXT NOT NULL, mst_sequence_id TEXT PRIMARY KEY, transmit INTEGER, idle INTEGER, config TEXT NOT NULL, cache_id TEXT NOT NULL, scan_id TEXT NOT NULL )");
        com.samsung.android.spayfw.b.c.i("MstConfigurationStorage", "Create RscCache Tables If Not Exists");
    }

    public static final c ac(Context context) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (Cc == null) {
                Cc = new c(context);
            }
            c c2 = Cc;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return c2;
        }
    }

    public int a(String string, String string2, Sequence sequence) {
        com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "addWifiScan : " + string + " " + string2 + " " + sequence);
        if (string == null || string2 == null || sequence == null) {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "cacheId/scanId/sequence is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", sequence.getKey());
        contentValues.put("cache_id", string);
        contentValues.put("config", sequence.getConfig());
        contentValues.put("idle", Integer.valueOf((int)sequence.getIdle()));
        contentValues.put("transmit", Integer.valueOf((int)sequence.getTransmit()));
        contentValues.put("mst_sequence_id", sequence.getMstSequenceId());
        contentValues.put("scan_id", string2);
        return this.a("rsc_cache_mst_sequence", contentValues);
    }

    public int a(String string, String string2, String string3, String string4, Wifi wifi) {
        com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "addWifiScan : " + string + " " + string2 + " " + wifi);
        if (string == null || string2 == null || wifi == null) {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "cacheId/scanId/Wifi is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("bssid", wifi.getBssid());
        contentValues.put("ssid", wifi.getSsid());
        contentValues.put("rssi", wifi.getRssi());
        contentValues.put("frequency", wifi.getFrequency());
        contentValues.put("distance", wifi.getDistance());
        contentValues.put("scan_id", string2);
        contentValues.put("cache_id", string);
        contentValues.put("mst_sequence_id", string3);
        contentValues.put("store_name", string4);
        return this.a("rsc_cache_wifi_scan", contentValues);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public Map<String, a> a(List<Wifi> var1_1, double var2_2) {
        com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "getMatchingScanSequenceCounts : " + var1_1 + " " + var2_2);
        if (var1_1 == null || var2_2 <= 0.0) {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "localWifis or distanceRange is NULL or Less than/equal 0");
            return null;
        }
        var4_3 = new StringBuilder();
        var5_4 = new ArrayList();
        for (Wifi var20_6 : var1_1) {
            var4_3.append("(bssid LIKE ? AND distance BETWEEN ? AND ? ) OR ");
            var22_7 = var20_6.getBssid();
            var5_4.add((Object)("%" + var22_7.substring(0, -2 + var22_7.length()) + "%"));
            var24_8 = Double.parseDouble((String)var20_6.getDistance());
            var26_9 = var24_8 - var2_2;
            var28_10 = var24_8 + var2_2;
            var5_4.add((Object)String.valueOf((double)var26_9));
            var5_4.add((Object)String.valueOf((double)var28_10));
        }
        var4_3.append("0");
        var8_11 = (String[])var5_4.toArray((Object[])new String[var5_4.size()]);
        var9_12 = var4_3.toString();
        var10_13 = new String[]{"scan_id", "mst_sequence_id"};
        var11_14 = new HashMap();
        var14_15 = this.a("rsc_cache_wifi_scan", var10_13, var9_12, var8_11, null);
        if (var14_15 == null) ** GOTO lbl30
        {
            catch (Throwable var12_22) {
                var13_23 = null;
                ** GOTO lbl49
            }
        }
        try {
            block12 : {
                if (var14_15.getCount() > 0) break block12;
lbl30: // 2 sources:
                com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "Cursor is NULL");
                c.a(var14_15);
                return null;
            }
            while (var14_15.moveToNext()) {
                var15_16 = var14_15.getString(var14_15.getColumnIndex("scan_id"));
                var16_17 = var14_15.getString(var14_15.getColumnIndex("mst_sequence_id"));
                if (var11_14.containsKey((Object)var15_16)) {
                    var19_19 = (a)var11_14.get((Object)var15_16);
                    var19_19.count = 1 + var19_19.count;
                    continue;
                }
                var17_18 = new a(var15_16, var16_17);
                var17_18.count = 1 + var17_18.count;
                var11_14.put((Object)var15_16, (Object)var17_18);
            }
        }
        catch (Throwable var12_20) {
            var13_23 = var14_15;
lbl49: // 2 sources:
            c.a(var13_23);
            throw var12_21;
        }
        c.a(var14_15);
        return var11_14;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Sequence bn(String string) {
        Cursor cursor;
        Cursor cursor2;
        block6 : {
            cursor = null;
            com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "getMstSequence : " + string);
            if (string == null) {
                com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "mstSeqId is NULL");
                return null;
            }
            String[] arrstring = new String[]{string};
            try {
                cursor2 = this.a("rsc_cache_mst_sequence", null, "mst_sequence_id=?", arrstring, null);
                if (cursor2 == null) break block6;
            }
            catch (Throwable throwable) {}
            if (cursor2.getCount() <= 0 || !cursor2.moveToNext()) break block6;
            String string2 = cursor2.getString(cursor2.getColumnIndex("config"));
            String string3 = cursor2.getString(cursor2.getColumnIndex("key"));
            int n2 = cursor2.getInt(cursor2.getColumnIndex("idle"));
            int n3 = cursor2.getInt(cursor2.getColumnIndex("transmit"));
            Sequence sequence = new Sequence(string3, cursor2.getString(cursor2.getColumnIndex("mst_sequence_id")), n3, n2, string2);
            c.a(cursor2);
            return sequence;
        }
        try {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "Cursor is NULL");
        }
        catch (Throwable throwable) {
            cursor = cursor2;
        }
        c.a(cursor2);
        return null;
        {
            void var4_11;
            c.a(cursor);
            throw var4_11;
        }
    }

    public int bo(String string) {
        com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "removeWifiScan : " + string);
        if (string == null) {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "cacheId is NULL");
            return -1;
        }
        return this.delete("rsc_cache_wifi_scan", "cache_id=?", new String[]{string});
    }

    public int c(CacheMetaData cacheMetaData) {
        com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "addCache : " + cacheMetaData);
        if (cacheMetaData == null) {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "CacheMetaData is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", cacheMetaData.getId());
        contentValues.put("type", cacheMetaData.getType());
        contentValues.put("href", cacheMetaData.getHref());
        contentValues.put("updated_at", cacheMetaData.getUpdatedAt());
        return this.a("rsc_cache_meta_data", contentValues);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public Map<String, a> ft() {
        block6 : {
            com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "getAllScanSequenceCounts");
            var1_1 = new HashMap();
            var4_2 = this.query("rsc_cache_wifi_scan", new String[]{"scan_id", "mst_sequence_id", "COUNT(scan_id) AS scan_count"}, null, null, "scan_id", null, null);
            if (var4_2 == null) break block6;
            if (var4_2.getCount() <= 0) break block6;
            while (var4_2.moveToNext()) {
                var5_3 = var4_2.getString(var4_2.getColumnIndex("scan_id"));
                var6_4 = new a(var5_3, var4_2.getString(var4_2.getColumnIndex("mst_sequence_id")));
                var6_4.count = var4_2.getInt(var4_2.getColumnIndex("scan_count"));
                var1_1.put((Object)var5_3, (Object)var6_4);
            }
            c.a(var4_2);
            return var1_1;
        }
        try {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "Cursor is NULL");
            ** GOTO lbl23
        }
        catch (Throwable var2_5) {
            block7 : {
                var3_8 = var4_2;
                break block7;
lbl23: // 1 sources:
                c.a(var4_2);
                return null;
                catch (Throwable var2_7) {
                    var3_8 = null;
                }
            }
            c.a(var3_8);
            throw var2_6;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public CacheMetaData z(String string, String string2) {
        Cursor cursor;
        Cursor cursor2;
        block6 : {
            cursor = null;
            com.samsung.android.spayfw.b.c.d("MstConfigurationStorage", "getCacheMetaData : " + string + " " + string2);
            if (string == null || string2 == null) {
                com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "Id or Type is NULL");
                return null;
            }
            String[] arrstring = new String[]{string, string2};
            try {
                cursor2 = this.a("rsc_cache_meta_data", null, "id=? AND type=?", arrstring, null);
                if (cursor2 == null) break block6;
            }
            catch (Throwable throwable) {}
            if (cursor2.getCount() <= 0 || !cursor2.moveToNext()) break block6;
            String string3 = cursor2.getString(cursor2.getColumnIndex("updated_at"));
            CacheMetaData cacheMetaData = new CacheMetaData(string, string2, cursor2.getString(cursor2.getColumnIndex("href")), string3);
            c.a(cursor2);
            return cacheMetaData;
        }
        try {
            com.samsung.android.spayfw.b.c.e("MstConfigurationStorage", "Cursor is NULL");
        }
        catch (Throwable throwable) {
            cursor = cursor2;
        }
        c.a(cursor2);
        return null;
        {
            void var5_9;
            c.a(cursor);
            throw var5_9;
        }
    }

    public static final class a {
        String Cd;
        int count;
        String mstSequenceId;

        public a(String string, String string2) {
            this.Cd = string;
            this.mstSequenceId = string2;
        }

        public int getCount() {
            return this.count;
        }

        public String getMstSequenceId() {
            return this.mstSequenceId;
        }

        public String toString() {
            return "ScanSequenceCount{scanId='" + this.Cd + '\'' + ", mstSequenceId='" + this.mstSequenceId + '\'' + ", count=" + this.count + '}';
        }
    }

}

