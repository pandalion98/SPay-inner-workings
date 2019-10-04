/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  com.google.gson.Gson
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Throwable
 *  java.lang.reflect.Type
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package com.samsung.contextservice.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.google.gson.Gson;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import com.samsung.contextclient.data.PoiTrigger;
import com.samsung.contextclient.data.WifiSignature;
import com.samsung.contextservice.a.a;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.exception.InitializationException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

public class d {
    private final a GC;

    public d(Context context) {
        if (context == null) {
            b.e("RCacheDao", "ctx is null");
        }
        this.GC = a.au(context);
        if (this.GC == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private long a(String string, String string2, long l2, long l3, ArrayList<Poi> arrayList, String string3) {
        Iterator iterator;
        long l4 = System.currentTimeMillis();
        b.d("RCacheDao", "addCacheEntries()");
        if (arrayList == null) {
            return -1L;
        }
        ContentValues contentValues = new ContentValues();
        Gson gson = new Gson();
        int n2 = 0;
        try {
            iterator = arrayList.iterator();
        }
        catch (Exception exception) {
            b.c("RCacheDao", "add Cache Entry exception", exception);
            return -1L;
        }
        do {
            int n3;
            block8 : {
                block7 : {
                    block6 : {
                        if (!iterator.hasNext()) break block6;
                        Poi poi = (Poi)iterator.next();
                        contentValues.clear();
                        contentValues.put("cacheid", string);
                        contentValues.put("updatedat", Long.valueOf((long)l2));
                        contentValues.put("expireat", Long.valueOf((long)l3));
                        contentValues.put("cachegeohash", string2);
                        contentValues.put("geohash", com.samsung.contextservice.b.d.d(poi.getLocation().getLatitude(), poi.getLocation().getLongitude()));
                        contentValues.put("poiid", poi.getId());
                        contentValues.put("poiname", poi.getName());
                        if (poi.getTrigger() == null) continue;
                        contentValues.put("poiradius", Double.valueOf((double)poi.getTrigger().getRadius()));
                        contentValues.put("poipurpose", poi.getTrigger().getPurpose());
                        contentValues.put("poistatus", poi.getTrigger().getStatus());
                        contentValues.put("lat", Double.valueOf((double)poi.getLocation().getLatitude()));
                        contentValues.put("lon", Double.valueOf((double)poi.getLocation().getLongitude()));
                        contentValues.put("alt", Double.valueOf((double)poi.getLocation().getAltitude()));
                        if (poi.getWifiSignature() != null) {
                            contentValues.put("wifi", gson.toJson((Object)poi.getWifiSignature(), WifiSignature.class));
                        }
                        contentValues.put("poi", gson.toJson((Object)poi, Poi.class));
                        if (string3 != null) {
                            contentValues.put("other", string3);
                        }
                        if (this.GC.c("caches", contentValues) >= 0L) break block7;
                        b.e("RCacheDao", "cannot add cache entries");
                        n3 = n2;
                        break block8;
                    }
                    b.d("RCacheDao", "add " + n2 + " cache entries (expected " + arrayList.size() + " ) in " + (System.currentTimeMillis() - l4) + "millis");
                    return n2;
                }
                n3 = n2 + 1;
            }
            n2 = n3;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private long a(String var1_1, String var2_2, long var3_3, long var5_4, boolean var7_5) {
        b.d("RCacheDao", "addCacheIndex()");
        var8_6 = new ContentValues();
        if (!var7_5) ** GOTO lbl9
        try {
            block6 : {
                block5 : {
                    var8_6.put("cachegeohash", var2_2);
                    var8_6.put("empty", Boolean.valueOf((boolean)var7_5));
                    var8_6.put("expireat", Long.toString((long)var5_4));
                    break block5;
lbl9: // 1 sources:
                    if (var1_1 == null) break block6;
                    var8_6.put("cachegeohash", var2_2);
                    var8_6.put("empty", Boolean.valueOf((boolean)var7_5));
                    var8_6.put("cacheid", var1_1);
                    var8_6.put("updateat", Long.toString((long)var3_3));
                    var8_6.put("expireat", Long.toString((long)var5_4));
                }
                var10_7 = this.GC.c("cacheindex", var8_6);
                b.d("RCacheDao", "add " + var8_6.toString());
                return var10_7;
            }
            b.d("RCacheDao", "cannot add cache index, cache id is null");
            return -1L;
        }
        catch (Exception var9_8) {
            b.c("RCacheDao", "cannot add cache index", var9_8);
            return -1L;
        }
    }

    private long b(String string, String string2, long l2, long l3, ArrayList<Poi> arrayList, String string3) {
        b.d("RCacheDao", "updateCacheEntries()");
        return this.a(string, string2, l2, l3, arrayList, string3);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private long b(String string, String string2, long l2, long l3, boolean bl) {
        b.d("RCacheDao", "updateCacheIndex()");
        ContentValues contentValues = new ContentValues();
        if (string2 == null) {
            return -1L;
        }
        try {
            block9 : {
                String[] arrstring;
                block8 : {
                    block7 : {
                        arrstring = new String[]{string2};
                        if (!bl) break block7;
                        contentValues.put("empty", Boolean.valueOf((boolean)bl));
                        contentValues.put("expireat", Long.toString((long)l3));
                        break block8;
                    }
                    if (string == null) break block9;
                    contentValues.put("empty", Boolean.valueOf((boolean)bl));
                    contentValues.put("cacheid", string);
                    contentValues.put("updateat", Long.toString((long)l2));
                    contentValues.put("expireat", Long.toString((long)l3));
                }
                long l4 = this.GC.update("cacheindex", contentValues, "cachegeohash == ? ", arrstring);
                b.d("RCacheDao", "update " + contentValues.toString());
                return l4;
            }
            b.d("RCacheDao", "cannot add cache index, cache id is null");
            return -1L;
        }
        catch (Exception exception) {
            b.c("RCacheDao", "cannot update cache index", exception);
            return -1L;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public long a(String string, String string2, long l2, long l3, ArrayList<Poi> arrayList, boolean bl) {
        long l4;
        b.d("RCacheDao", "updateCacheIndexAndEntriesIfPossible()");
        if (arrayList != null) {
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                b.d("RCacheDao", ((Poi)iterator.next()).toString());
            }
            if ("DEALS_AND_OFFERS".equals((Object)string)) {
                b.d("RCacheDao", "Add deals and offers pois");
                if (this.a(string, false, null)) return this.b(string, string2, l2, l3, arrayList, null);
                this.a(string, string2, l2, l3, bl);
                return this.b(string, string2, l2, l3, arrayList, null);
            }
            if (!this.bK(string2)) return -1L;
            b.d("RCacheDao", "Add non-empty cache index");
            this.a(string, string2, l2, l3, bl);
            l4 = this.b(string, string2, l2, l3, arrayList, null);
            if (l4 != (long)arrayList.size()) return l4;
            b.d("RCacheDao", "Cache update succeeds");
            return l4;
        }
        try {
            if (!this.bK(string2)) return -1L;
            l4 = this.b(string, string2, l2, l3, bl);
            if (l4 > 0L) return l4;
        }
        catch (Exception exception) {
            return -1L;
        }
        b.d("RCacheDao", "update the affected rows=" + l4);
        long l5 = this.a(string, string2, l2, l3, bl);
        b.d("RCacheDao", "insert the affected row is " + l5);
        return l5;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public ArrayList<Poi> a(double d2, double d3, double d4, double d5, String string) {
        Cursor cursor;
        block8 : {
            ArrayList arrayList;
            block9 : {
                b.d("RCacheDao", "queryPoisByGeoFence");
                1.41421356237 * d4;
                double d6 = com.samsung.contextservice.b.d.b(d3, d4);
                double d7 = com.samsung.contextservice.b.d.c(d2, d4);
                double[] arrd = com.samsung.contextservice.b.d.c(d5);
                String string2 = " WHERE ABS( lat - " + d2 + " ) <= " + d6 + " AND ABS ( " + "lon" + " - " + d3 + " ) <= " + d7 + " AND " + "poiradius" + " > " + arrd[0] + " AND " + "poiradius" + " <= " + arrd[1];
                if (string != null) {
                    string2 = string2 + " AND cacheid == " + string;
                }
                String string3 = " SELECT DISTINCT poi FROM caches" + string2 + " ORDER BY " + "_id" + " ASC LIMIT " + 1000;
                b.d("RCacheDao", "SQL=" + string3);
                cursor = null;
                cursor = this.GC.rawQuery(string3, null);
                if (cursor == null) break block8;
                try {
                    boolean bl;
                    if (cursor.getCount() <= 0) break block8;
                    arrayList = new ArrayList(cursor.getCount());
                    Gson gson = new Gson();
                    int n2 = cursor.getColumnIndex("poi");
                    if (!cursor.moveToFirst()) break block9;
                    do {
                        String string4 = cursor.getString(n2);
                        arrayList.add(gson.fromJson(string4, Poi.class));
                        b.d("RCacheDao", string4);
                    } while (bl = cursor.moveToNext());
                }
                catch (Exception exception) {
                    try {
                        b.c("RCacheDao", "query by goefence error", exception);
                    }
                    catch (Throwable throwable) {
                        a.a(cursor);
                        throw throwable;
                    }
                    a.a(cursor);
                    return new ArrayList();
                }
            }
            a.a(cursor);
            return arrayList;
        }
        a.a(cursor);
        do {
            return new ArrayList();
            break;
        } while (true);
    }

    public void a(ArrayList<Poi> arrayList) {
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            if (((Poi)arrayList.get(i2)).getTrigger() == null || !"DEALS".equalsIgnoreCase(((Poi)arrayList.get(i2)).getTrigger().getPurpose())) continue;
            stringBuilder.append("'");
            stringBuilder.append(((Poi)arrayList.get(i2)).getId());
            stringBuilder.append("'");
            if (i2 >= -1 + arrayList.size()) continue;
            stringBuilder.append(',');
        }
        stringBuilder.append(')');
        String string = "DELETE FROM caches WHERE poiid in " + stringBuilder.toString() + " AND " + "poipurpose" + " == \"" + "DEALS" + "\"";
        try {
            this.GC.execSQL(string);
            return;
        }
        catch (Exception exception) {
            b.a("RCacheDao", "cannot remove persistent pois", exception);
            return;
        }
    }

    /*
     * Exception decompiling
     */
    public boolean a(String var1_1, boolean var2_2, ArrayList<String> var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 10[SIMPLE_IF_TAKEN]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
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

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public long[] bJ(String var1_1) {
        block14 : {
            block13 : {
                var2_2 = 0L;
                b.d("RCacheDao", "getCacheUpdatedAtAndExpireAt()");
                if (var1_1 == null) {
                    return null;
                }
                var4_3 = " WHERE cachegeohash=\"" + var1_1 + "\" LIMIT 1";
                var9_4 = "SELECT updateat , expireat FROM cacheindex" + var4_3;
                b.d("RCacheDao", "getCacheUpdatedAtAndExpireAt, sql=" + var9_4);
                var6_6 = var10_5 = this.GC.rawQuery(var9_4, null);
                if (var6_6 == null) break block13;
                if (var6_6.getCount() <= 0 || !var6_6.moveToFirst()) break block13;
                var11_7 = var6_6.getColumnIndex("updateat");
                var12_8 = var6_6.getColumnIndex("expireat");
                var13_9 = var6_6.getString(var11_7);
                var14_10 = var6_6.getString(var12_8);
                b.d("RCacheDao", "getCacheUpdatedAt(), geohash " + var1_1 + " update at " + var13_9 + ", expire at " + var14_10);
                if (var13_9 == null) break block14;
                try {
                    var15_11 = Long.valueOf((String)var13_9);
                }
                catch (Exception var8_15) {
                    ** continue;
                }
lbl21: // 2 sources:
                do {
                    if (var14_10 == null) ** GOTO lbl25
                    var2_2 = Long.valueOf((String)var14_10);
lbl25: // 2 sources:
                    var17_12 = new long[]{var15_11, var2_2};
                    a.a(var6_6);
                    return var17_12;
                    break;
                } while (true);
            }
            a.a(var6_6);
            return null;
            catch (Exception var8_13) {
                var6_6 = null;
lbl33: // 2 sources:
                do {
                    b.a("RCacheDao", "cannot find getCacheUpdatedAtAndExpireAt", (Throwable)var8_14);
                    a.a(var6_6);
                    return null;
                    break;
                } while (true);
            }
            catch (Throwable var5_16) {
                var6_6 = null;
                var7_17 = var5_16;
lbl41: // 2 sources:
                do {
                    a.a(var6_6);
                    throw var7_17;
                    break;
                } while (true);
            }
            {
                catch (Throwable var7_18) {
                    ** continue;
                }
            }
        }
        var15_11 = var2_2;
        ** while (true)
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean bK(String string) {
        if (string == null || string.length() < 3) {
            return false;
        }
        try {
            String string2 = string.substring(0, 3);
            String string3 = " WHERE cachegeohash LIKE \"" + string2 + "%\" AND " + "cachegeohash" + " != \"" + string + "\"";
            String string4 = "DELETE FROM cacheindex" + string3;
            b.d("RCacheDao", "resolvedGeohashConflict" + string4);
            this.GC.execSQL(string4);
            do {
                return true;
                break;
            } while (true);
        }
        catch (Exception exception) {
            b.a("RCacheDao", "resolvedGeohashConflict", exception);
            return true;
        }
    }
}

