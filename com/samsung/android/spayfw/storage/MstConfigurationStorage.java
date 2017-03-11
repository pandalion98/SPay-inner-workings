package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CacheMetaData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Sequence;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Wifi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: com.samsung.android.spayfw.storage.c */
public class MstConfigurationStorage extends DbAdapter {
    private static MstConfigurationStorage Cc;

    /* renamed from: com.samsung.android.spayfw.storage.c.a */
    public static final class MstConfigurationStorage {
        String Cd;
        int count;
        String mstSequenceId;

        public MstConfigurationStorage(String str, String str2) {
            this.Cd = str;
            this.mstSequenceId = str2;
        }

        public String getMstSequenceId() {
            return this.mstSequenceId;
        }

        public int getCount() {
            return this.count;
        }

        public String toString() {
            return "ScanSequenceCount{scanId='" + this.Cd + '\'' + ", mstSequenceId='" + this.mstSequenceId + '\'' + ", count=" + this.count + '}';
        }
    }

    public static final synchronized MstConfigurationStorage ac(Context context) {
        MstConfigurationStorage mstConfigurationStorage;
        synchronized (MstConfigurationStorage.class) {
            if (Cc == null) {
                Cc = new MstConfigurationStorage(context);
            }
            mstConfigurationStorage = Cc;
        }
        return mstConfigurationStorage;
    }

    private MstConfigurationStorage(Context context) {
        super(context);
        execSQL("CREATE TABLE IF NOT EXISTS rsc_cache_meta_data (id TEXT NOT NULL, type TEXT NOT NULL, href TEXT NOT NULL, updated_at TEXT NOT NULL, PRIMARY KEY (id, type) )");
        execSQL("CREATE TABLE IF NOT EXISTS rsc_cache_wifi_scan (cache_id TEXT NOT NULL, scan_id TEXT NOT NULL, store_name TEXT NOT NULL, bssid TEXT NOT NULL, ssid TEXT NOT NULL, rssi INTEGER, frequency INTEGER, distance REAL, mst_sequence_id TEXT NOT NULL )");
        execSQL("CREATE TABLE IF NOT EXISTS rsc_cache_mst_sequence (key TEXT NOT NULL, mst_sequence_id TEXT PRIMARY KEY, transmit INTEGER, idle INTEGER, config TEXT NOT NULL, cache_id TEXT NOT NULL, scan_id TEXT NOT NULL )");
        Log.m287i("MstConfigurationStorage", "Create RscCache Tables If Not Exists");
    }

    public int m1248c(CacheMetaData cacheMetaData) {
        Log.m285d("MstConfigurationStorage", "addCache : " + cacheMetaData);
        if (cacheMetaData == null) {
            Log.m286e("MstConfigurationStorage", "CacheMetaData is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(PushMessage.JSON_KEY_ID, cacheMetaData.getId());
        contentValues.put("type", cacheMetaData.getType());
        contentValues.put("href", cacheMetaData.getHref());
        contentValues.put("updated_at", cacheMetaData.getUpdatedAt());
        return m1107a("rsc_cache_meta_data", contentValues);
    }

    public CacheMetaData m1249z(String str, String str2) {
        Throwable th;
        Cursor cursor = null;
        Log.m285d("MstConfigurationStorage", "getCacheMetaData : " + str + " " + str2);
        if (str == null || str2 == null) {
            Log.m286e("MstConfigurationStorage", "Id or Type is NULL");
            return null;
        }
        try {
            Cursor a = m1110a("rsc_cache_meta_data", null, "id=? AND type=?", new String[]{str, str2}, null);
            if (a != null) {
                try {
                    if (a.getCount() > 0 && a.moveToNext()) {
                        CacheMetaData cacheMetaData = new CacheMetaData(str, str2, a.getString(a.getColumnIndex("href")), a.getString(a.getColumnIndex("updated_at")));
                        DbAdapter.m1106a(a);
                        return cacheMetaData;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = a;
                    DbAdapter.m1106a(cursor);
                    throw th;
                }
            }
            Log.m286e("MstConfigurationStorage", "Cursor is NULL");
            DbAdapter.m1106a(a);
            return null;
        } catch (Throwable th3) {
            th = th3;
            DbAdapter.m1106a(cursor);
            throw th;
        }
    }

    public Sequence bn(String str) {
        Throwable th;
        Cursor cursor = null;
        Log.m285d("MstConfigurationStorage", "getMstSequence : " + str);
        if (str == null) {
            Log.m286e("MstConfigurationStorage", "mstSeqId is NULL");
            return null;
        }
        try {
            Cursor a = m1110a("rsc_cache_mst_sequence", null, "mst_sequence_id=?", new String[]{str}, null);
            if (a != null) {
                try {
                    if (a.getCount() > 0 && a.moveToNext()) {
                        String string = a.getString(a.getColumnIndex("config"));
                        String string2 = a.getString(a.getColumnIndex("key"));
                        int i = a.getInt(a.getColumnIndex("idle"));
                        Sequence sequence = new Sequence(string2, a.getString(a.getColumnIndex("mst_sequence_id")), a.getInt(a.getColumnIndex("transmit")), i, string);
                        DbAdapter.m1106a(a);
                        return sequence;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = a;
                    DbAdapter.m1106a(cursor);
                    throw th;
                }
            }
            Log.m286e("MstConfigurationStorage", "Cursor is NULL");
            DbAdapter.m1106a(a);
            return null;
        } catch (Throwable th3) {
            th = th3;
            DbAdapter.m1106a(cursor);
            throw th;
        }
    }

    public int bo(String str) {
        Log.m285d("MstConfigurationStorage", "removeWifiScan : " + str);
        if (str == null) {
            Log.m286e("MstConfigurationStorage", "cacheId is NULL");
            return -1;
        }
        return delete("rsc_cache_wifi_scan", "cache_id=?", new String[]{str});
    }

    public int m1246a(String str, String str2, String str3, String str4, Wifi wifi) {
        Log.m285d("MstConfigurationStorage", "addWifiScan : " + str + " " + str2 + " " + wifi);
        if (str == null || str2 == null || wifi == null) {
            Log.m286e("MstConfigurationStorage", "cacheId/scanId/Wifi is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("bssid", wifi.getBssid());
        contentValues.put("ssid", wifi.getSsid());
        contentValues.put("rssi", wifi.getRssi());
        contentValues.put("frequency", wifi.getFrequency());
        contentValues.put("distance", wifi.getDistance());
        contentValues.put("scan_id", str2);
        contentValues.put("cache_id", str);
        contentValues.put("mst_sequence_id", str3);
        contentValues.put("store_name", str4);
        return m1107a("rsc_cache_wifi_scan", contentValues);
    }

    public int m1245a(String str, String str2, Sequence sequence) {
        Log.m285d("MstConfigurationStorage", "addWifiScan : " + str + " " + str2 + " " + sequence);
        if (str == null || str2 == null || sequence == null) {
            Log.m286e("MstConfigurationStorage", "cacheId/scanId/sequence is NULL");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("key", sequence.getKey());
        contentValues.put("cache_id", str);
        contentValues.put("config", sequence.getConfig());
        contentValues.put("idle", Integer.valueOf(sequence.getIdle()));
        contentValues.put("transmit", Integer.valueOf(sequence.getTransmit()));
        contentValues.put("mst_sequence_id", sequence.getMstSequenceId());
        contentValues.put("scan_id", str2);
        return m1107a("rsc_cache_mst_sequence", contentValues);
    }

    public Map<String, MstConfigurationStorage> m1247a(List<Wifi> list, double d) {
        Throwable th;
        Cursor cursor = null;
        Log.m285d("MstConfigurationStorage", "getMatchingScanSequenceCounts : " + list + " " + d);
        if (list == null || d <= 0.0d) {
            Log.m286e("MstConfigurationStorage", "localWifis or distanceRange is NULL or Less than/equal 0");
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList arrayList = new ArrayList();
        for (Wifi wifi : list) {
            stringBuilder.append("(bssid LIKE ? AND distance BETWEEN ? AND ? ) OR ");
            String bssid = wifi.getBssid();
            arrayList.add("%" + bssid.substring(0, bssid.length() - 2) + "%");
            double parseDouble = Double.parseDouble(wifi.getDistance());
            double d2 = parseDouble - d;
            parseDouble += d;
            arrayList.add(String.valueOf(d2));
            arrayList.add(String.valueOf(parseDouble));
        }
        stringBuilder.append(TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE);
        String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        String stringBuilder2 = stringBuilder.toString();
        String[] strArr2 = new String[]{"scan_id", "mst_sequence_id"};
        try {
            Map<String, MstConfigurationStorage> hashMap = new HashMap();
            Cursor a = m1110a("rsc_cache_wifi_scan", strArr2, stringBuilder2, strArr, null);
            if (a != null) {
                try {
                    if (a.getCount() > 0) {
                        while (a.moveToNext()) {
                            String string = a.getString(a.getColumnIndex("scan_id"));
                            String string2 = a.getString(a.getColumnIndex("mst_sequence_id"));
                            if (hashMap.containsKey(string)) {
                                MstConfigurationStorage mstConfigurationStorage = (MstConfigurationStorage) hashMap.get(string);
                                mstConfigurationStorage.count++;
                            } else {
                                MstConfigurationStorage mstConfigurationStorage2 = new MstConfigurationStorage(string, string2);
                                mstConfigurationStorage2.count++;
                                hashMap.put(string, mstConfigurationStorage2);
                            }
                        }
                        DbAdapter.m1106a(a);
                        return hashMap;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = a;
                }
            }
            Log.m286e("MstConfigurationStorage", "Cursor is NULL");
            DbAdapter.m1106a(a);
            return null;
        } catch (Throwable th3) {
            th = th3;
            DbAdapter.m1106a(cursor);
            throw th;
        }
    }

    public Map<String, MstConfigurationStorage> ft() {
        Throwable th;
        Cursor cursor = null;
        Log.m285d("MstConfigurationStorage", "getAllScanSequenceCounts");
        try {
            Map<String, MstConfigurationStorage> hashMap = new HashMap();
            Cursor query = query("rsc_cache_wifi_scan", new String[]{"scan_id", "mst_sequence_id", "COUNT(scan_id) AS scan_count"}, null, null, "scan_id", null, null);
            if (query != null) {
                try {
                    if (query.getCount() > 0) {
                        while (query.moveToNext()) {
                            String string = query.getString(query.getColumnIndex("scan_id"));
                            MstConfigurationStorage mstConfigurationStorage = new MstConfigurationStorage(string, query.getString(query.getColumnIndex("mst_sequence_id")));
                            mstConfigurationStorage.count = query.getInt(query.getColumnIndex("scan_count"));
                            hashMap.put(string, mstConfigurationStorage);
                        }
                        DbAdapter.m1106a(query);
                        return hashMap;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = query;
                    DbAdapter.m1106a(cursor);
                    throw th;
                }
            }
            Log.m286e("MstConfigurationStorage", "Cursor is NULL");
            DbAdapter.m1106a(query);
            return null;
        } catch (Throwable th3) {
            th = th3;
            DbAdapter.m1106a(cursor);
            throw th;
        }
    }
}
