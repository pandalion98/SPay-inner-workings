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
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.dao.MCAbstractDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData;

public class McTdsMetaDataDaoImpl
extends MCAbstractDaoImpl<McTdsMetaData> {
    private static final String TAG = "McTdsMetaDataDaoImpl";
    private static final String TDS_TAG_ERROR = "e__McTdsMetaDataDaoImpl";
    private static final String TDS_TAG_INFO = "i__McTdsMetaDataDaoImpl";

    public McTdsMetaDataDaoImpl(Context context) {
        super(context);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean updateTds(ContentValues contentValues, String string) {
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = this;
        synchronized (mcTdsMetaDataDaoImpl) {
            boolean bl;
            block4 : {
                SQLiteDatabase sQLiteDatabase = this.db;
                bl = false;
                if (sQLiteDatabase != null) break block4;
                return bl;
            }
            int n2 = this.db.update(this.getTableName(), contentValues, string, null);
            c.i(TDS_TAG_INFO, "updateTds: rows affected:" + n2);
            long l2 = (long)n2 LCMP -1L;
            bl = false;
            if (l2 == false) return bl;
            return true;
        }
    }

    @Override
    protected ContentValues getContentValues(McTdsMetaData mcTdsMetaData) {
        if (mcTdsMetaData == null) {
            return null;
        }
        if (!mcTdsMetaData.validate()) {
            c.e(TDS_TAG_ERROR, "getContentValues: tds Data validation failed");
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("cardMasterId", Long.valueOf((long)mcTdsMetaData.getCardMasterId()));
        contentValues.put("tdsUrl", mcTdsMetaData.getTdsUrl());
        contentValues.put("tdsRegisterUrl", mcTdsMetaData.getTdsRegisterUrl());
        contentValues.put("paymentAppInstanceId", mcTdsMetaData.getPaymentAppInstanceId());
        contentValues.put("hash", mcTdsMetaData.getHash());
        contentValues.put("authCode", mcTdsMetaData.getAuthCode());
        contentValues.put("lastUpdateTag", mcTdsMetaData.getLastUpdateTag());
        return contentValues;
    }

    @Override
    protected ContentValues getContentValues(McTdsMetaData mcTdsMetaData, long l2) {
        return null;
    }

    @Override
    protected McTdsMetaData getDataValues(Cursor cursor) {
        if (cursor == null) {
            c.e(TDS_TAG_ERROR, "empty cursor");
            return null;
        }
        McTdsMetaData mcTdsMetaData = new McTdsMetaData();
        mcTdsMetaData.setCardMasterId(cursor.getLong(cursor.getColumnIndex("cardMasterId")));
        mcTdsMetaData.setTdsUrl(cursor.getString(cursor.getColumnIndex("tdsUrl")));
        mcTdsMetaData.setTdsRegisterUrl(cursor.getString(cursor.getColumnIndex("tdsRegisterUrl")));
        mcTdsMetaData.setPaymentAppInstanceId(cursor.getString(cursor.getColumnIndex("paymentAppInstanceId")));
        mcTdsMetaData.setHash(cursor.getString(cursor.getColumnIndex("hash")));
        mcTdsMetaData.setAuthCode(cursor.getString(cursor.getColumnIndex("authCode")));
        mcTdsMetaData.setLastUpdateTag(cursor.getString(cursor.getColumnIndex("lastUpdateTag")));
        return mcTdsMetaData;
    }

    @Override
    protected String getQuerySearch(long l2) {
        return "cardMasterId = " + l2;
    }

    @Override
    protected String getTableName() {
        return "TDS_METADATA";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean storeAuthCode(String string, long l2) {
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = this;
        synchronized (mcTdsMetaDataDaoImpl) {
            block4 : {
                if (!TextUtils.isEmpty((CharSequence)string)) break block4;
                c.e(TDS_TAG_ERROR, "storeAuthCode: Invalid params to store authCode.");
                return false;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("authCode", string);
            boolean bl = this.updateTds(contentValues, this.getQuerySearch(l2) + " OR " + "authCode" + " IS NOT NULL");
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean storeAuthCodeWithUrl(String string, long l2, String string2) {
        boolean bl = false;
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = this;
        synchronized (mcTdsMetaDataDaoImpl) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                if (!this.storeAuthCode(string, l2)) {
                    c.e(TDS_TAG_ERROR, "storeAuthCode: failed");
                    return false;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("tdsUrl", string2);
                boolean bl2 = this.updateTds(contentValues, this.getQuerySearch(l2));
                return bl2;
            }
            c.e(TDS_TAG_ERROR, "storeAuthCode: tdsUrl is empty discarding request");
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean updateUrl(String string, long l2, boolean bl) {
        McTdsMetaDataDaoImpl mcTdsMetaDataDaoImpl = this;
        synchronized (mcTdsMetaDataDaoImpl) {
            if (TextUtils.isEmpty((CharSequence)string) || l2 < 0L) {
                c.e(TDS_TAG_ERROR, "storeUrl: Invalid params to store url.");
                return false;
            }
            ContentValues contentValues = new ContentValues();
            if (bl) {
                contentValues.put("tdsRegisterUrl", string);
                return this.updateTds(contentValues, this.getQuerySearch(l2));
            } else {
                contentValues.put("tdsUrl", string);
            }
            return this.updateTds(contentValues, this.getQuerySearch(l2));
        }
    }
}

