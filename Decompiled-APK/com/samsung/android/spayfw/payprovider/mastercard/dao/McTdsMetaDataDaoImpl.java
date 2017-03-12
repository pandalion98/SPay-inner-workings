package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.TdsMetaData;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData;

public class McTdsMetaDataDaoImpl extends MCAbstractDaoImpl<McTdsMetaData> {
    private static final String TAG = "McTdsMetaDataDaoImpl";
    private static final String TDS_TAG_ERROR = "e__McTdsMetaDataDaoImpl";
    private static final String TDS_TAG_INFO = "i__McTdsMetaDataDaoImpl";

    public McTdsMetaDataDaoImpl(Context context) {
        super(context);
    }

    protected ContentValues getContentValues(McTdsMetaData mcTdsMetaData) {
        if (mcTdsMetaData == null) {
            return null;
        }
        if (mcTdsMetaData.validate()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(mcTdsMetaData.getCardMasterId()));
            contentValues.put(TdsMetaData.COL_TDS_URL, mcTdsMetaData.getTdsUrl());
            contentValues.put(TdsMetaData.COL_TDS_REGISTRATION_URL, mcTdsMetaData.getTdsRegisterUrl());
            contentValues.put(TdsMetaData.COL_PAYMENT_APP_INSTANCE_ID, mcTdsMetaData.getPaymentAppInstanceId());
            contentValues.put(TdsMetaData.COL_HASH, mcTdsMetaData.getHash());
            contentValues.put(TdsMetaData.COL_AUTH_CODE, mcTdsMetaData.getAuthCode());
            contentValues.put(TdsMetaData.COL_LAST_UPDATE_TAG, mcTdsMetaData.getLastUpdateTag());
            return contentValues;
        }
        Log.m286e(TDS_TAG_ERROR, "getContentValues: tds Data validation failed");
        return null;
    }

    protected String getTableName() {
        return TdsMetaData.TABLE_NAME;
    }

    protected McTdsMetaData getDataValues(Cursor cursor) {
        if (cursor == null) {
            Log.m286e(TDS_TAG_ERROR, "empty cursor");
            return null;
        }
        McTdsMetaData mcTdsMetaData = new McTdsMetaData();
        mcTdsMetaData.setCardMasterId(cursor.getLong(cursor.getColumnIndex(McDbContract.CARD_MASTER_ID)));
        mcTdsMetaData.setTdsUrl(cursor.getString(cursor.getColumnIndex(TdsMetaData.COL_TDS_URL)));
        mcTdsMetaData.setTdsRegisterUrl(cursor.getString(cursor.getColumnIndex(TdsMetaData.COL_TDS_REGISTRATION_URL)));
        mcTdsMetaData.setPaymentAppInstanceId(cursor.getString(cursor.getColumnIndex(TdsMetaData.COL_PAYMENT_APP_INSTANCE_ID)));
        mcTdsMetaData.setHash(cursor.getString(cursor.getColumnIndex(TdsMetaData.COL_HASH)));
        mcTdsMetaData.setAuthCode(cursor.getString(cursor.getColumnIndex(TdsMetaData.COL_AUTH_CODE)));
        mcTdsMetaData.setLastUpdateTag(cursor.getString(cursor.getColumnIndex(TdsMetaData.COL_LAST_UPDATE_TAG)));
        return mcTdsMetaData;
    }

    protected ContentValues getContentValues(McTdsMetaData mcTdsMetaData, long j) {
        return null;
    }

    public synchronized boolean updateUrl(String str, long j, boolean z) {
        boolean z2;
        if (TextUtils.isEmpty(str) || j < 0) {
            Log.m286e(TDS_TAG_ERROR, "storeUrl: Invalid params to store url.");
            z2 = false;
        } else {
            ContentValues contentValues = new ContentValues();
            if (z) {
                contentValues.put(TdsMetaData.COL_TDS_REGISTRATION_URL, str);
            } else {
                contentValues.put(TdsMetaData.COL_TDS_URL, str);
            }
            z2 = updateTds(contentValues, getQuerySearch(j));
        }
        return z2;
    }

    public synchronized boolean storeAuthCodeWithUrl(String str, long j, String str2) {
        boolean z = false;
        synchronized (this) {
            if (TextUtils.isEmpty(str2)) {
                Log.m286e(TDS_TAG_ERROR, "storeAuthCode: tdsUrl is empty discarding request");
            } else if (storeAuthCode(str, j)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TdsMetaData.COL_TDS_URL, str2);
                z = updateTds(contentValues, getQuerySearch(j));
            } else {
                Log.m286e(TDS_TAG_ERROR, "storeAuthCode: failed");
            }
        }
        return z;
    }

    public synchronized boolean storeAuthCode(String str, long j) {
        boolean z;
        if (TextUtils.isEmpty(str)) {
            Log.m286e(TDS_TAG_ERROR, "storeAuthCode: Invalid params to store authCode.");
            z = false;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TdsMetaData.COL_AUTH_CODE, str);
            z = updateTds(contentValues, getQuerySearch(j) + " OR " + TdsMetaData.COL_AUTH_CODE + " IS NOT NULL");
        }
        return z;
    }

    private synchronized boolean updateTds(ContentValues contentValues, String str) {
        boolean z = false;
        synchronized (this) {
            if (this.db != null) {
                int update = this.db.update(getTableName(), contentValues, str, null);
                Log.m287i(TDS_TAG_INFO, "updateTds: rows affected:" + update);
                if (((long) update) != -1) {
                    z = true;
                }
            }
        }
        return z;
    }

    protected String getQuerySearch(long j) {
        return "cardMasterId = " + j;
    }
}
