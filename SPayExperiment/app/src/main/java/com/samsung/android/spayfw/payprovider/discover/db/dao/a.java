/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Long
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.dao.b;
import com.samsung.android.spayfw.payprovider.discover.db.models.CardDetails;

public class a
extends b<CardDetails> {
    public a(Context context) {
        super(context);
    }

    protected ContentValues a(CardDetails cardDetails) {
        if (cardDetails == null) {
            c.e("DCSDK_CardDetailsDaoImpl", "getContentValues: Data null");
            return null;
        }
        if (cardDetails.getCardMasterId() == -1L) {
            c.e("DCSDK_CardDetailsDaoImpl", "getContentValues: INVALID_ROW_ID");
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("cardMasterId", Long.valueOf((long)cardDetails.getCardMasterId()));
        contentValues.put("dataId", Long.valueOf((long)cardDetails.getDataId()));
        contentValues.put("data", cardDetails.getData());
        return contentValues;
    }

    public boolean a(CardDetails cardDetails, long l2) {
        if (cardDetails == null) {
            throw new DcDbException("Invalid Input", 3);
        }
        if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        }
        String string = "cardMasterId = " + l2 + " and " + "dataId" + "=" + cardDetails.getDataId();
        if (this.db.update(this.getTableName(), this.a(cardDetails), string, null) != 1) {
            throw new DcDbException("Failed to update CardDetails record", 7);
        }
        return true;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public CardDetails b(long var1_1, long var3_2) {
        block8 : {
            var10_3 = "cardMasterId=" + var1_1 + " and " + "dataId" + "=" + var3_2;
            var8_5 = var11_4 = this.db.query(this.getTableName(), null, var10_3, null, null, null, null);
            var12_6 = null;
            if (var8_5 == null) break block8;
            try {
                var14_7 = var8_5.moveToFirst();
                var12_6 = null;
                if (!var14_7) break block8;
                var12_6 = var15_8 = this.b(var8_5);
            }
            catch (NullPointerException var13_14) {
                ** continue;
            }
        }
        if (var8_5 == null) return var12_6;
        var8_5.close();
        return var12_6;
        catch (NullPointerException var7_9) {
            block9 : {
                var8_5 = null;
                break block9;
                catch (Throwable var5_13) {
                    var6_11 = null;
                    ** GOTO lbl29
                }
            }
lbl22: // 2 sources:
            do {
                try {
                    c.e("DCSDK_CardDetailsDaoImpl", "NPE occured during getData");
                    throw new DcDbException("NP Exception Occurred", 6);
                }
                catch (Throwable var9_10) {
                    var6_11 = var8_5;
                    var5_12 = var9_10;
lbl29: // 2 sources:
                    if (var6_11 == null) throw var5_12;
                    var6_11.close();
                    throw var5_12;
                }
                break;
            } while (true);
        }
    }

    protected CardDetails b(Cursor cursor) {
        if (cursor == null) {
            c.e("DCSDK_CardDetailsDaoImpl", "getDataValues: cursor null");
            return null;
        }
        return new CardDetails(cursor.getLong(cursor.getColumnIndex("cardMasterId")), cursor.getLong(cursor.getColumnIndex("dataId")), cursor.getBlob(cursor.getColumnIndex("data")));
    }

    @Override
    protected /* synthetic */ ContentValues getContentValues(Object object) {
        return this.a((CardDetails)object);
    }

    @Override
    public /* synthetic */ Object getData(long l2) {
        return this.n(l2);
    }

    @Override
    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return this.b(cursor);
    }

    @Override
    protected String getTableName() {
        return "CardDetails";
    }

    public CardDetails n(long l2) {
        throw new DcDbException("Method Not Supported", 3);
    }

    @Override
    public /* synthetic */ boolean updateData(Object object, long l2) {
        return this.a((CardDetails)object, l2);
    }
}

