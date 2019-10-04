/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Long
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Arrays
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.dao.MCAbstractDaoImpl;
import java.util.Arrays;
import java.util.List;

public class McCardMasterDaoImpl
extends MCAbstractDaoImpl<McCardMaster> {
    private static final String DELIMETER = ", ";
    public static final int MC_DB_INDEX_INIT_VALUE = -1;
    private static final String TAG = "McCardMasterDaoImpl";

    public McCardMasterDaoImpl(Context context) {
        super(context);
    }

    private String convertToString(List<String> list) {
        if (list == null || list.size() < 1) {
            return null;
        }
        return TextUtils.join((CharSequence)DELIMETER, list);
    }

    private List<String> convertToStringList(String string) {
        if (string == null) {
            return null;
        }
        return Arrays.asList((Object[])TextUtils.split((String)string, (String)DELIMETER));
    }

    public long deleteStaleEnrollmentData() {
        if (this.db == null) {
            return -1L;
        }
        return this.db.delete(this.getTableName(), "isProvisioned > 1", null);
    }

    @Override
    protected ContentValues getContentValues(McCardMaster mcCardMaster) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tokenUniqueReference", mcCardMaster.getTokenUniqueReference());
        contentValues.put("mpaInstanceId", mcCardMaster.getMpaInstanceId());
        contentValues.put("status", mcCardMaster.getStatus());
        contentValues.put("suspendedBy", this.convertToString(mcCardMaster.getSuspendedBy()));
        contentValues.put("tokenPanSuffix", mcCardMaster.getTokenPanSuffix());
        contentValues.put("accountPanSuffix", mcCardMaster.getAccountPanSuffix());
        contentValues.put("tokenExpiry", mcCardMaster.getTokenExpiry());
        contentValues.put("isProvisioned", Long.valueOf((long)mcCardMaster.getProvisionedState()));
        contentValues.put("rgkDerivedKeys", mcCardMaster.getRgkDerivedkeys());
        return contentValues;
    }

    @Override
    protected ContentValues getContentValues(McCardMaster mcCardMaster, long l2) {
        return null;
    }

    @Override
    protected McCardMaster getDataValues(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        McCardMaster mcCardMaster = new McCardMaster();
        mcCardMaster.setTokenUniqueReference(cursor.getString(cursor.getColumnIndex("tokenUniqueReference")));
        mcCardMaster.setMpaInstanceId(cursor.getString(cursor.getColumnIndex("mpaInstanceId")));
        mcCardMaster.setStatus(cursor.getString(cursor.getColumnIndex("status")));
        mcCardMaster.setSuspendedBy(this.convertToStringList(cursor.getString(cursor.getColumnIndex("suspendedBy"))));
        mcCardMaster.setTokenPanSuffix(cursor.getString(cursor.getColumnIndex("tokenPanSuffix")));
        mcCardMaster.setAccountPanSuffix(cursor.getString(cursor.getColumnIndex("accountPanSuffix")));
        mcCardMaster.setTokenExpiry(cursor.getString(cursor.getColumnIndex("tokenExpiry")));
        mcCardMaster.setProvisionedState(cursor.getLong(cursor.getColumnIndex("isProvisioned")));
        mcCardMaster.setRgkDerivedkeys(cursor.getBlob(cursor.getColumnIndex("rgkDerivedKeys")));
        return mcCardMaster;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public long getDbIdFromTokenUniqueRefence(String var1_1) {
        block9 : {
            var2_2 = null;
            var6_3 = this.db.query(this.getTableName(), new String[]{"_id", "tokenUniqueReference"}, "tokenUniqueReference=?", new String[]{var1_1}, null, null, null, null);
            if (var6_3 == null) break block9;
            if (!var6_3.moveToFirst()) break block9;
            var7_4 = var6_3.getLong(var6_3.getColumnIndex("_id"));
            if (var6_3 == null) return var7_4;
            var6_3.close();
            return var7_4;
        }
        c.e("McCardMasterDaoImpl", " getDbIdFromTokenUniqueRefence : cursor is null");
        if (var6_3 == null) return -1L;
        var6_3.close();
        return -1L;
        catch (NullPointerException var4_5) {
            block10 : {
                var5_8 = null;
                break block10;
                catch (Throwable var3_9) {}
                ** GOTO lbl-1000
                catch (Throwable var3_11) {
                    var2_2 = var6_3;
                    ** GOTO lbl-1000
                }
                catch (NullPointerException var4_7) {
                    var5_8 = var6_3;
                }
            }
            try {
                var4_6.printStackTrace();
                c.d("McCardMasterDaoImpl", "NPE exception occured during isTokenProvisioned call");
                if (var5_8 == null) return -1L;
            }
            catch (Throwable var3_12) {
                var2_2 = var5_8;
            }
            var5_8.close();
            return -1L;
        }
lbl-1000: // 3 sources:
        {
            if (var2_2 == null) throw var3_10;
            var2_2.close();
            throw var3_10;
        }
    }

    @Override
    protected String getTableName() {
        return "CARD_MASTER";
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public boolean isTokenProvisioned(long var1_1) {
        block13 : {
            var3_2 = null;
            var6_4 = var8_3 = this.db.query(this.getTableName(), new String[]{"isProvisioned"}, this.getQuerySearch(var1_1), null, null, null, this.getQuerySearch(var1_1) + " DESC");
            var7_5 = false;
            if (var6_4 == null) ** GOTO lbl15
            try {
                var9_6 = var6_4.moveToFirst();
                var7_5 = false;
                if (!var9_6) ** GOTO lbl15
                var10_7 = var6_4.getLong(var6_4.getColumnIndex("isProvisioned"));
                if (var10_7 != 1L) break block13;
                var12_8 = true;
            }
            catch (NullPointerException var5_11) {
                ** continue;
            }
lbl13: // 2 sources:
            do {
                var7_5 = var12_8;
lbl15: // 3 sources:
                if (var6_4 != null) {
                    var6_4.close();
                }
                do {
                    return var7_5;
                    break;
                } while (true);
                break;
            } while (true);
        }
        var12_8 = false;
        ** while (true)
        catch (NullPointerException var5_9) {
            var6_4 = null;
lbl24: // 2 sources:
            do {
                var5_10.printStackTrace();
                c.d("McCardMasterDaoImpl", "NPE exception occured during isTokenProvisioned call");
                var7_5 = false;
                if (var6_4 == null) ** continue;
                var6_4.close();
                return false;
                break;
            } while (true);
        }
        catch (Throwable var4_12) lbl-1000: // 2 sources:
        {
            do {
                if (var3_2 != null) {
                    var3_2.close();
                }
                throw var4_13;
                break;
            } while (true);
        }
        {
            catch (Throwable var4_14) {
                var3_2 = var6_4;
                ** continue;
            }
        }
    }
}

