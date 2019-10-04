/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.SQLException
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.paywave.data.TVL;
import com.samsung.android.visasdk.storage.a;
import java.util.ArrayList;
import java.util.List;

public class e {
    private final a Gu;

    public e(Context context) {
        if (context == null) {
            com.samsung.android.visasdk.c.a.e("DbTvlDao", "ctx is null");
        }
        this.Gu = a.as(context);
        if (this.Gu == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public List<String> a(TokenKey var1_1, String var2_2) {
        block6 : {
            var3_3 = new String[]{"timestamp", "unpredictable_number", "atc", "transaction_type"};
            var6_4 = new String[]{Long.toString((long)var1_1.getTokenId()), var2_2};
            var7_5 = this.Gu.a("tbl_tvl", var3_3, "token_key = ? AND api = ? ", var6_4, "atc ASC");
            if (var7_5 == null) ** GOTO lbl25
            try {
                if (var7_5.getCount() <= 0) ** GOTO lbl25
                var8_6 = new ArrayList(var7_5.getCount());
                var9_7 = var7_5.getColumnIndex("timestamp");
                var10_8 = var7_5.getColumnIndex("unpredictable_number");
                var11_9 = var7_5.getColumnIndex("atc");
                var12_10 = var7_5.getColumnIndex("transaction_type");
lbl13: // 2 sources:
                do {
                    if (var7_5.moveToNext()) {
                        var13_11 = var7_5.getString(var10_8);
                        var14_12 = var7_5.getString(var12_10);
                        if (var13_11 != null && !"S".equals((Object)var14_12) && !"I".equals((Object)var14_12) && !"M".equals((Object)var14_12)) break block6;
                    }
                    ** GOTO lbl23
                    break;
                } while (true);
            }
            catch (Throwable var4_13) {
                block7 : {
                    var5_16 = var7_5;
                    break block7;
lbl23: // 1 sources:
                    a.a(var7_5);
                    return var8_6;
lbl25: // 2 sources:
                    a.a(var7_5);
                    return null;
                    catch (Throwable var4_15) {
                        var5_16 = null;
                    }
                }
                a.a(var5_16);
                throw var4_14;
            }
            var13_11 = "";
        }
        var8_6.add((Object)(var7_5.getLong(var9_7) + "|" + var13_11 + "|" + var7_5.getInt(var11_9) + "|" + var14_12));
        ** while (true)
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void a(TVL tVL) {
        e e2 = this;
        // MONITORENTER : e2
        if (tVL == null) {
            com.samsung.android.visasdk.c.a.e("DbTvlDao", "tvl is null");
            // MONITOREXIT : e2
            return;
        }
        if (this.b(tVL)) {
            com.samsung.android.visasdk.c.a.e("DbTvlDao", "tvl entry already exist tokenkey " + tVL.getTokenKey().getTokenId() + " atc: " + tVL.getAtc());
            return;
        }
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("token_key", Long.valueOf((long)tVL.getTokenKey().getTokenId()));
            contentValues.put("atc", Integer.valueOf((int)tVL.getAtc()));
            contentValues.put("api", tVL.getApi());
            contentValues.put("timestamp", Long.valueOf((long)tVL.getTimeStamp()));
            contentValues.put("transaction_type", tVL.getTransactionType());
            contentValues.put("unpredictable_number", tVL.getUnpredictableNumber());
            this.Gu.b("tbl_tvl", contentValues);
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new SQLException("cannot store tvl");
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public boolean b(TVL tVL) {
        Cursor cursor;
        block4 : {
            try {
                Cursor cursor2;
                String[] arrstring = new String[]{"timestamp", "unpredictable_number", "atc", "transaction_type"};
                String[] arrstring2 = new String[]{Long.toString((long)tVL.getTokenKey().getTokenId()), Integer.toString((int)tVL.getAtc())};
                cursor = cursor2 = this.Gu.a("tbl_tvl", arrstring, "token_key = ? AND atc = ? ", arrstring2, "atc DESC");
                if (cursor == null) break block4;
            }
            catch (Throwable throwable) {
                void var3_8;
                cursor = null;
                a.a(cursor);
                throw var3_8;
            }
            int n2 = cursor.getCount();
            if (n2 <= 0) break block4;
            {
                catch (Throwable throwable) {}
            }
            a.a(cursor);
            return true;
        }
        a.a(cursor);
        return false;
    }
}

