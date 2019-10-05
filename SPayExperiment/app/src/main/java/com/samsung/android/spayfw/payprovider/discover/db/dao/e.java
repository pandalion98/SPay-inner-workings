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
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcPaymentProfile;
import java.util.List;

public class e
extends b<DcPaymentProfile> {
    public e(Context context) {
        super(context);
    }

    protected ContentValues a(DcPaymentProfile dcPaymentProfile) {
        if (dcPaymentProfile == null) {
            Log.e("DCSDK_DcPaymentProfileDaoImpl", "getContentValues: Data null");
            return null;
        }
        if (dcPaymentProfile.getCardMasterId() == -1L) {
            Log.e("DCSDK_DcPaymentProfileDaoImpl", "getContentValues: INVALID_ROW_ID");
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("cardMasterId", Long.valueOf((long)dcPaymentProfile.getCardMasterId()));
        contentValues.put("payProfileId", Long.valueOf((long)dcPaymentProfile.getProfileId()));
        contentValues.put("ctq", dcPaymentProfile.getCtq());
        contentValues.put("auc", dcPaymentProfile.getAuc());
        contentValues.put("pru", dcPaymentProfile.getPru());
        contentValues.put("aip", dcPaymentProfile.getAip());
        contentValues.put("afl", dcPaymentProfile.getAfl());
        contentValues.put("cpr", dcPaymentProfile.getCpr());
        contentValues.put("arm", dcPaymentProfile.getCrm());
        contentValues.put("cvm", dcPaymentProfile.getCvm());
        contentValues.put("cl", dcPaymentProfile.getCl());
        return contentValues;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public DcPaymentProfile c(long var1_1, long var3_2) {
        block8 : {
            var8_4 = var10_3 = this.db.query(this.getTableName(), null, "cardMasterId=" + var1_1 + " AND " + "payProfileId" + "=" + var3_2, null, null, null, null);
            var11_5 = null;
            if (var8_4 == null) break block8;
            try {
                var13_6 = var8_4.moveToFirst();
                var11_5 = null;
                if (!var13_6) break block8;
                var11_5 = var14_7 = this.g(var8_4);
            }
            catch (NullPointerException var12_13) {
                ** continue;
            }
        }
        if (var8_4 == null) return var11_5;
        var8_4.close();
        return var11_5;
        catch (NullPointerException var7_8) {
            block9 : {
                var8_4 = null;
                break block9;
                catch (Throwable var5_12) {
                    var6_10 = null;
                    ** GOTO lbl28
                }
            }
lbl21: // 2 sources:
            do {
                try {
                    Log.e("DCSDK_DcPaymentProfileDaoImpl", "NPE occured during getData");
                    throw new DcDbException("NP Exception Occurred", 6);
                }
                catch (Throwable var9_9) {
                    var6_10 = var8_4;
                    var5_11 = var9_9;
lbl28: // 2 sources:
                    if (var6_10 == null) throw var5_11;
                    var6_10.close();
                    throw var5_11;
                }
                break;
            } while (true);
        }
    }

    protected DcPaymentProfile g(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        DcPaymentProfile dcPaymentProfile = new DcPaymentProfile();
        dcPaymentProfile.setRowId(cursor.getLong(cursor.getColumnIndex("_id")));
        dcPaymentProfile.setCardMasterId(cursor.getLong(cursor.getColumnIndex("cardMasterId")));
        dcPaymentProfile.setProfileId(cursor.getLong(cursor.getColumnIndex("payProfileId")));
        dcPaymentProfile.setCtq(cursor.getBlob(cursor.getColumnIndex("ctq")));
        dcPaymentProfile.setAuc(cursor.getBlob(cursor.getColumnIndex("auc")));
        dcPaymentProfile.setPru(cursor.getBlob(cursor.getColumnIndex("pru")));
        dcPaymentProfile.setAip(cursor.getBlob(cursor.getColumnIndex("aip")));
        dcPaymentProfile.setAfl(cursor.getBlob(cursor.getColumnIndex("afl")));
        dcPaymentProfile.setCpr(cursor.getBlob(cursor.getColumnIndex("cpr")));
        dcPaymentProfile.setCrm(cursor.getBlob(cursor.getColumnIndex("arm")));
        dcPaymentProfile.setCvm(cursor.getBlob(cursor.getColumnIndex("cvm")));
        dcPaymentProfile.setCl(cursor.getBlob(cursor.getColumnIndex("cl")));
        return dcPaymentProfile;
    }

    @Override
    protected /* synthetic */ ContentValues getContentValues(Object object) {
        return this.a((DcPaymentProfile)object);
    }

    @Override
    public /* synthetic */ Object getData(long l2) {
        return this.p(l2);
    }

    @Override
    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return this.g(cursor);
    }

    @Override
    protected String getTableName() {
        return "PaymentProfiles";
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public List<DcPaymentProfile> o(long var1_1) {
        var6_3 = var8_2 = this.db.query(this.getTableName(), null, "cardMasterId=" + var1_1, null, null, null, "cardMasterId DESC");
        try {
            var10_4 = this.c(var6_3);
            if (var6_3 == null) return var10_4;
        }
        catch (NullPointerException var9_10) {
            ** continue;
        }
        var6_3.close();
        return var10_4;
        catch (NullPointerException var5_5) {
            block8 : {
                var6_3 = null;
                break block8;
                catch (Throwable var3_9) {
                    var4_7 = null;
                    ** GOTO lbl22
                }
            }
lbl15: // 2 sources:
            do {
                try {
                    Log.e("DCSDK_DcPaymentProfileDaoImpl", "NPE occured during getData");
                    throw new DcDbException("NP Exception Occurred", 6);
                }
                catch (Throwable var7_6) {
                    var4_7 = var6_3;
                    var3_8 = var7_6;
lbl22: // 2 sources:
                    if (var4_7 == null) throw var3_8;
                    var4_7.close();
                    throw var3_8;
                }
                break;
            } while (true);
        }
    }

    public DcPaymentProfile p(long l2) {
        throw new DcDbException("Method Not Supported", 3);
    }
}

