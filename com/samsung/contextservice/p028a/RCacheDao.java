package com.samsung.contextservice.p028a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.google.gson.Gson;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextclient.data.WifiSignature;
import com.samsung.contextservice.exception.InitializationException;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.p029b.GeoUtils;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.samsung.contextservice.a.d */
public class RCacheDao {
    private final DbAdapter GC;

    public RCacheDao(Context context) {
        if (context == null) {
            CSlog.m1409e("RCacheDao", "ctx is null");
        }
        this.GC = DbAdapter.au(context);
        if (this.GC == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    private long m1390a(String str, String str2, long j, long j2, ArrayList<Poi> arrayList, String str3) {
        long currentTimeMillis = System.currentTimeMillis();
        CSlog.m1408d("RCacheDao", "addCacheEntries()");
        if (arrayList == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        int i = 0;
        try {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Object obj = (Poi) it.next();
                contentValues.clear();
                contentValues.put("cacheid", str);
                contentValues.put("updatedat", Long.valueOf(j));
                contentValues.put("expireat", Long.valueOf(j2));
                contentValues.put("cachegeohash", str2);
                contentValues.put("geohash", GeoUtils.m1416d(obj.getLocation().getLatitude(), obj.getLocation().getLongitude()));
                contentValues.put("poiid", obj.getId());
                contentValues.put("poiname", obj.getName());
                if (obj.getTrigger() != null) {
                    int i2;
                    contentValues.put("poiradius", Double.valueOf(obj.getTrigger().getRadius()));
                    contentValues.put("poipurpose", obj.getTrigger().getPurpose());
                    contentValues.put("poistatus", obj.getTrigger().getStatus());
                    contentValues.put("lat", Double.valueOf(obj.getLocation().getLatitude()));
                    contentValues.put("lon", Double.valueOf(obj.getLocation().getLongitude()));
                    contentValues.put("alt", Double.valueOf(obj.getLocation().getAltitude()));
                    if (obj.getWifiSignature() != null) {
                        contentValues.put("wifi", gson.toJson(obj.getWifiSignature(), (Type) WifiSignature.class));
                    }
                    contentValues.put("poi", gson.toJson(obj, (Type) Poi.class));
                    if (str3 != null) {
                        contentValues.put("other", str3);
                    }
                    if (this.GC.m1388c("caches", contentValues) < 0) {
                        CSlog.m1409e("RCacheDao", "cannot add cache entries");
                        i2 = i;
                    } else {
                        i2 = i + 1;
                    }
                    i = i2;
                }
            }
            CSlog.m1408d("RCacheDao", "add " + i + " cache entries (expected " + arrayList.size() + " ) in " + (System.currentTimeMillis() - currentTimeMillis) + "millis");
            return (long) i;
        } catch (Throwable e) {
            CSlog.m1406c("RCacheDao", "add Cache Entry exception", e);
            return -1;
        }
    }

    private long m1392b(String str, String str2, long j, long j2, ArrayList<Poi> arrayList, String str3) {
        CSlog.m1408d("RCacheDao", "updateCacheEntries()");
        return m1390a(str, str2, j, j2, (ArrayList) arrayList, str3);
    }

    public void m1396a(ArrayList<Poi> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('(');
            int i = 0;
            while (i < arrayList.size()) {
                if (((Poi) arrayList.get(i)).getTrigger() != null && Poi.PURPOSE_DEAL.equalsIgnoreCase(((Poi) arrayList.get(i)).getTrigger().getPurpose())) {
                    stringBuilder.append("'");
                    stringBuilder.append(((Poi) arrayList.get(i)).getId());
                    stringBuilder.append("'");
                    if (i < arrayList.size() - 1) {
                        stringBuilder.append(',');
                    }
                }
                i++;
            }
            stringBuilder.append(')');
            try {
                this.GC.execSQL("DELETE FROM caches WHERE poiid in " + stringBuilder.toString() + " AND " + "poipurpose" + " == \"" + Poi.PURPOSE_DEAL + "\"");
            } catch (Throwable e) {
                CSlog.m1403a("RCacheDao", "cannot remove persistent pois", e);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.samsung.contextclient.data.Poi> m1395a(double r10, double r12, double r14, double r16, java.lang.String r18) {
        /*
        r9 = this;
        r2 = "RCacheDao";
        r3 = "queryPoisByGeoFence";
        com.samsung.contextservice.p029b.CSlog.m1408d(r2, r3);
        r2 = 4609047870845158746; // 0x3ff6a09e667f055a float:3.0107554E23 double:1.41421356237;
        r2 = r2 * r14;
        r2 = com.samsung.contextservice.p029b.GeoUtils.m1413b(r12, r14);
        r4 = com.samsung.contextservice.p029b.GeoUtils.m1414c(r10, r14);
        r6 = com.samsung.contextservice.p029b.GeoUtils.m1415c(r16);
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = " WHERE ABS( lat - ";
        r7 = r7.append(r8);
        r7 = r7.append(r10);
        r8 = " ) <= ";
        r7 = r7.append(r8);
        r2 = r7.append(r2);
        r3 = " AND ABS ( ";
        r2 = r2.append(r3);
        r3 = "lon";
        r2 = r2.append(r3);
        r3 = " - ";
        r2 = r2.append(r3);
        r2 = r2.append(r12);
        r3 = " ) <= ";
        r2 = r2.append(r3);
        r2 = r2.append(r4);
        r3 = " AND ";
        r2 = r2.append(r3);
        r3 = "poiradius";
        r2 = r2.append(r3);
        r3 = " > ";
        r2 = r2.append(r3);
        r3 = 0;
        r4 = r6[r3];
        r2 = r2.append(r4);
        r3 = " AND ";
        r2 = r2.append(r3);
        r3 = "poiradius";
        r2 = r2.append(r3);
        r3 = " <= ";
        r2 = r2.append(r3);
        r3 = 1;
        r4 = r6[r3];
        r2 = r2.append(r4);
        r2 = r2.toString();
        if (r18 == 0) goto L_0x00a3;
    L_0x008a:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r2 = r3.append(r2);
        r3 = " AND cacheid == ";
        r2 = r2.append(r3);
        r0 = r18;
        r2 = r2.append(r0);
        r2 = r2.toString();
    L_0x00a3:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = " SELECT DISTINCT poi FROM caches";
        r3 = r3.append(r4);
        r2 = r3.append(r2);
        r3 = " ORDER BY ";
        r2 = r2.append(r3);
        r3 = "_id";
        r2 = r2.append(r3);
        r3 = " ASC LIMIT ";
        r2 = r2.append(r3);
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r2 = r2.append(r3);
        r2 = r2.toString();
        r3 = "RCacheDao";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "SQL=";
        r4 = r4.append(r5);
        r4 = r4.append(r2);
        r4 = r4.toString();
        com.samsung.contextservice.p029b.CSlog.m1408d(r3, r4);
        r3 = 0;
        r4 = r9.GC;	 Catch:{ Exception -> 0x0135 }
        r5 = 0;
        r3 = r4.rawQuery(r2, r5);	 Catch:{ Exception -> 0x0135 }
        if (r3 == 0) goto L_0x012c;
    L_0x00f0:
        r2 = r3.getCount();	 Catch:{ Exception -> 0x0135 }
        if (r2 <= 0) goto L_0x012c;
    L_0x00f6:
        r2 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0135 }
        r4 = r3.getCount();	 Catch:{ Exception -> 0x0135 }
        r2.<init>(r4);	 Catch:{ Exception -> 0x0135 }
        r4 = new com.google.gson.Gson;	 Catch:{ Exception -> 0x0135 }
        r4.<init>();	 Catch:{ Exception -> 0x0135 }
        r5 = "poi";
        r5 = r3.getColumnIndex(r5);	 Catch:{ Exception -> 0x0135 }
        r6 = r3.moveToFirst();	 Catch:{ Exception -> 0x0135 }
        if (r6 == 0) goto L_0x0128;
    L_0x0110:
        r6 = r3.getString(r5);	 Catch:{ Exception -> 0x0135 }
        r7 = com.samsung.contextclient.data.Poi.class;
        r7 = r4.fromJson(r6, r7);	 Catch:{ Exception -> 0x0135 }
        r2.add(r7);	 Catch:{ Exception -> 0x0135 }
        r7 = "RCacheDao";
        com.samsung.contextservice.p029b.CSlog.m1408d(r7, r6);	 Catch:{ Exception -> 0x0135 }
        r6 = r3.moveToNext();	 Catch:{ Exception -> 0x0135 }
        if (r6 != 0) goto L_0x0110;
    L_0x0128:
        com.samsung.contextservice.p028a.DbAdapter.m1387a(r3);
    L_0x012b:
        return r2;
    L_0x012c:
        com.samsung.contextservice.p028a.DbAdapter.m1387a(r3);
    L_0x012f:
        r2 = new java.util.ArrayList;
        r2.<init>();
        goto L_0x012b;
    L_0x0135:
        r2 = move-exception;
        r4 = "RCacheDao";
        r5 = "query by goefence error";
        com.samsung.contextservice.p029b.CSlog.m1406c(r4, r5, r2);	 Catch:{ all -> 0x0141 }
        com.samsung.contextservice.p028a.DbAdapter.m1387a(r3);
        goto L_0x012f;
    L_0x0141:
        r2 = move-exception;
        com.samsung.contextservice.p028a.DbAdapter.m1387a(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.contextservice.a.d.a(double, double, double, double, java.lang.String):java.util.ArrayList<com.samsung.contextclient.data.Poi>");
    }

    private long m1391a(String str, String str2, long j, long j2, boolean z) {
        CSlog.m1408d("RCacheDao", "addCacheIndex()");
        ContentValues contentValues = new ContentValues();
        if (z) {
            try {
                contentValues.put("cachegeohash", str2);
                contentValues.put("empty", Boolean.valueOf(z));
                contentValues.put("expireat", Long.toString(j2));
            } catch (Throwable e) {
                CSlog.m1406c("RCacheDao", "cannot add cache index", e);
                return -1;
            }
        } else if (str != null) {
            contentValues.put("cachegeohash", str2);
            contentValues.put("empty", Boolean.valueOf(z));
            contentValues.put("cacheid", str);
            contentValues.put("updateat", Long.toString(j));
            contentValues.put("expireat", Long.toString(j2));
        } else {
            CSlog.m1408d("RCacheDao", "cannot add cache index, cache id is null");
            return -1;
        }
        long c = this.GC.m1388c("cacheindex", contentValues);
        CSlog.m1408d("RCacheDao", "add " + contentValues.toString());
        return c;
    }

    private long m1393b(String str, String str2, long j, long j2, boolean z) {
        CSlog.m1408d("RCacheDao", "updateCacheIndex()");
        ContentValues contentValues = new ContentValues();
        if (str2 == null) {
            return -1;
        }
        try {
            String str3 = "cachegeohash == ? ";
            String[] strArr = new String[]{str2};
            if (z) {
                contentValues.put("empty", Boolean.valueOf(z));
                contentValues.put("expireat", Long.toString(j2));
            } else if (str != null) {
                contentValues.put("empty", Boolean.valueOf(z));
                contentValues.put("cacheid", str);
                contentValues.put("updateat", Long.toString(j));
                contentValues.put("expireat", Long.toString(j2));
            } else {
                CSlog.m1408d("RCacheDao", "cannot add cache index, cache id is null");
                return -1;
            }
            long update = (long) this.GC.update("cacheindex", contentValues, str3, strArr);
            CSlog.m1408d("RCacheDao", "update " + contentValues.toString());
            return update;
        } catch (Throwable e) {
            CSlog.m1406c("RCacheDao", "cannot update cache index", e);
            return -1;
        }
    }

    public boolean m1397a(String str, boolean z, ArrayList<String> arrayList) {
        String str2;
        Cursor rawQuery;
        Throwable e;
        Cursor cursor = null;
        CSlog.m1408d("RCacheDao", "isGeoHashAvailable()");
        if (str != null) {
            if (z) {
                str2 = " WHERE cacheid=\"" + str + "\" AND " + "expireat" + " >= " + System.currentTimeMillis() + " LIMIT 1 ";
            } else {
                str2 = " WHERE cacheid=\"" + str + "\" LIMIT 1 ";
            }
        } else if (arrayList == null || arrayList.size() <= 0) {
            CSlog.m1408d("RCacheDao", "cacheid or geohash is null");
            return false;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('(');
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append("'");
                stringBuilder.append((String) arrayList.get(i));
                stringBuilder.append("'");
                if (i < arrayList.size() - 1) {
                    stringBuilder.append(',');
                }
            }
            stringBuilder.append(')');
            str2 = " WHERE cachegeohash in " + stringBuilder.toString() + " AND " + "expireat" + " >= " + System.currentTimeMillis() + " LIMIT 1 ";
        }
        try {
            str2 = "SELECT 1 FROM cacheindex" + str2;
            CSlog.m1408d("RCacheDao", "isGeoHashAvailable, sql=" + str2);
            rawQuery = this.GC.rawQuery(str2, null);
            if (rawQuery != null) {
                try {
                    if (rawQuery.getCount() > 0) {
                        CSlog.m1408d("RCacheDao", "cache is found");
                        DbAdapter.m1387a(rawQuery);
                        return true;
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        CSlog.m1403a("RCacheDao", "cannot find geohash", e);
                        DbAdapter.m1387a(rawQuery);
                        CSlog.m1408d("RCacheDao", "cache is not found");
                        return false;
                    } catch (Throwable th) {
                        e = th;
                        cursor = rawQuery;
                        DbAdapter.m1387a(cursor);
                        throw e;
                    }
                }
            }
            DbAdapter.m1387a(rawQuery);
        } catch (Exception e3) {
            e = e3;
            rawQuery = null;
            CSlog.m1403a("RCacheDao", "cannot find geohash", e);
            DbAdapter.m1387a(rawQuery);
            CSlog.m1408d("RCacheDao", "cache is not found");
            return false;
        } catch (Throwable th2) {
            e = th2;
            DbAdapter.m1387a(cursor);
            throw e;
        }
        CSlog.m1408d("RCacheDao", "cache is not found");
        return false;
    }

    public long[] bJ(String str) {
        Cursor rawQuery;
        Throwable e;
        Throwable th;
        long j = 0;
        CSlog.m1408d("RCacheDao", "getCacheUpdatedAtAndExpireAt()");
        if (str == null) {
            return null;
        }
        try {
            String str2 = "SELECT updateat , expireat FROM cacheindex" + (" WHERE cachegeohash=\"" + str + "\" LIMIT 1");
            CSlog.m1408d("RCacheDao", "getCacheUpdatedAtAndExpireAt, sql=" + str2);
            rawQuery = this.GC.rawQuery(str2, null);
            if (rawQuery != null) {
                try {
                    if (rawQuery.getCount() > 0 && rawQuery.moveToFirst()) {
                        long longValue;
                        int columnIndex = rawQuery.getColumnIndex("updateat");
                        int columnIndex2 = rawQuery.getColumnIndex("expireat");
                        str2 = rawQuery.getString(columnIndex);
                        String string = rawQuery.getString(columnIndex2);
                        CSlog.m1408d("RCacheDao", "getCacheUpdatedAt(), geohash " + str + " update at " + str2 + ", expire at " + string);
                        if (str2 != null) {
                            longValue = Long.valueOf(str2).longValue();
                        } else {
                            longValue = 0;
                        }
                        if (string != null) {
                            j = Long.valueOf(string).longValue();
                        }
                        long[] jArr = new long[]{longValue, j};
                        DbAdapter.m1387a(rawQuery);
                        return jArr;
                    }
                } catch (Exception e2) {
                    e = e2;
                    try {
                        CSlog.m1403a("RCacheDao", "cannot find getCacheUpdatedAtAndExpireAt", e);
                        DbAdapter.m1387a(rawQuery);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        DbAdapter.m1387a(rawQuery);
                        throw th;
                    }
                }
            }
            DbAdapter.m1387a(rawQuery);
            return null;
        } catch (Exception e3) {
            e = e3;
            rawQuery = null;
            CSlog.m1403a("RCacheDao", "cannot find getCacheUpdatedAtAndExpireAt", e);
            DbAdapter.m1387a(rawQuery);
            return null;
        } catch (Throwable e4) {
            rawQuery = null;
            th = e4;
            DbAdapter.m1387a(rawQuery);
            throw th;
        }
    }

    public boolean bK(String str) {
        if (str == null || str.length() < 3) {
            return false;
        }
        try {
            String str2 = "DELETE FROM cacheindex" + (" WHERE cachegeohash LIKE \"" + str.substring(0, 3) + "%\" AND " + "cachegeohash" + " != \"" + str + "\"");
            CSlog.m1408d("RCacheDao", "resolvedGeohashConflict" + str2);
            this.GC.execSQL(str2);
        } catch (Throwable e) {
            CSlog.m1403a("RCacheDao", "resolvedGeohashConflict", e);
        }
        return true;
    }

    public long m1394a(String str, String str2, long j, long j2, ArrayList<Poi> arrayList, boolean z) {
        CSlog.m1408d("RCacheDao", "updateCacheIndexAndEntriesIfPossible()");
        long b;
        if (arrayList != null) {
            try {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    CSlog.m1408d("RCacheDao", ((Poi) it.next()).toString());
                }
                if ("DEALS_AND_OFFERS".equals(str)) {
                    CSlog.m1408d("RCacheDao", "Add deals and offers pois");
                    if (!m1397a(str, false, null)) {
                        m1391a(str, str2, j, j2, z);
                    }
                    return m1392b(str, str2, j, j2, arrayList, null);
                } else if (bK(str2)) {
                    CSlog.m1408d("RCacheDao", "Add non-empty cache index");
                    m1391a(str, str2, j, j2, z);
                    b = m1392b(str, str2, j, j2, arrayList, null);
                    if (b != ((long) arrayList.size())) {
                        return b;
                    }
                    CSlog.m1408d("RCacheDao", "Cache update succeeds");
                    return b;
                }
            } catch (Exception e) {
                return -1;
            }
        } else if (bK(str2)) {
            b = m1393b(str, str2, j, j2, z);
            if (b > 0) {
                return b;
            }
            CSlog.m1408d("RCacheDao", "update the affected rows=" + b);
            b = m1391a(str, str2, j, j2, z);
            CSlog.m1408d("RCacheDao", "insert the affected row is " + b);
            return b;
        }
        return -1;
    }
}
