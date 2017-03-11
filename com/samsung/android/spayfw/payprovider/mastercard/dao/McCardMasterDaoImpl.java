package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import java.util.Arrays;
import java.util.List;

public class McCardMasterDaoImpl extends MCAbstractDaoImpl<McCardMaster> {
    private static final String DELIMETER = ", ";
    public static final int MC_DB_INDEX_INIT_VALUE = -1;
    private static final String TAG = "McCardMasterDaoImpl";

    public McCardMasterDaoImpl(Context context) {
        super(context);
    }

    protected ContentValues getContentValues(McCardMaster mcCardMaster) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardMaster.COL_TOKEN_UNIQUE_REFERENCE, mcCardMaster.getTokenUniqueReference());
        contentValues.put(CardMaster.COL_MPA_INSTANCE_ID, mcCardMaster.getMpaInstanceId());
        contentValues.put(CardMaster.COL_STATUS, mcCardMaster.getStatus());
        contentValues.put(CardMaster.COL_SUSPENDEDBY, convertToString(mcCardMaster.getSuspendedBy()));
        contentValues.put(CardMaster.COL_TOKEN_PAN_SUFFIX, mcCardMaster.getTokenPanSuffix());
        contentValues.put(CardMaster.COL_ACCOUNT_PAN_SUFFIX, mcCardMaster.getAccountPanSuffix());
        contentValues.put(CardMaster.COL_TOKEN_EXPIRY, mcCardMaster.getTokenExpiry());
        contentValues.put(CardMaster.COL_TOKEN_PROVISIONED, Long.valueOf(mcCardMaster.getProvisionedState()));
        contentValues.put(CardMaster.COL_RGK_KEYS, mcCardMaster.getRgkDerivedkeys());
        return contentValues;
    }

    protected McCardMaster getDataValues(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        McCardMaster mcCardMaster = new McCardMaster();
        mcCardMaster.setTokenUniqueReference(cursor.getString(cursor.getColumnIndex(CardMaster.COL_TOKEN_UNIQUE_REFERENCE)));
        mcCardMaster.setMpaInstanceId(cursor.getString(cursor.getColumnIndex(CardMaster.COL_MPA_INSTANCE_ID)));
        mcCardMaster.setStatus(cursor.getString(cursor.getColumnIndex(CardMaster.COL_STATUS)));
        mcCardMaster.setSuspendedBy(convertToStringList(cursor.getString(cursor.getColumnIndex(CardMaster.COL_SUSPENDEDBY))));
        mcCardMaster.setTokenPanSuffix(cursor.getString(cursor.getColumnIndex(CardMaster.COL_TOKEN_PAN_SUFFIX)));
        mcCardMaster.setAccountPanSuffix(cursor.getString(cursor.getColumnIndex(CardMaster.COL_ACCOUNT_PAN_SUFFIX)));
        mcCardMaster.setTokenExpiry(cursor.getString(cursor.getColumnIndex(CardMaster.COL_TOKEN_EXPIRY)));
        mcCardMaster.setProvisionedState(cursor.getLong(cursor.getColumnIndex(CardMaster.COL_TOKEN_PROVISIONED)));
        mcCardMaster.setRgkDerivedkeys(cursor.getBlob(cursor.getColumnIndex(CardMaster.COL_RGK_KEYS)));
        return mcCardMaster;
    }

    protected String getTableName() {
        return CardMaster.TABLE_NAME;
    }

    private String convertToString(List<String> list) {
        if (list == null || list.size() < 1) {
            return null;
        }
        return TextUtils.join(DELIMETER, list);
    }

    private List<String> convertToStringList(String str) {
        if (str == null) {
            return null;
        }
        return Arrays.asList(TextUtils.split(str, DELIMETER));
    }

    protected ContentValues getContentValues(McCardMaster mcCardMaster, long j) {
        return null;
    }

    public boolean isTokenProvisioned(long j) {
        NullPointerException e;
        Throwable th;
        Cursor cursor = null;
        boolean z = false;
        Cursor query;
        try {
            query = this.db.query(getTableName(), new String[]{CardMaster.COL_TOKEN_PROVISIONED}, getQuerySearch(j), null, null, null, getQuerySearch(j) + " DESC");
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        z = query.getLong(query.getColumnIndex(CardMaster.COL_TOKEN_PROVISIONED)) == 1;
                    }
                } catch (NullPointerException e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        Log.m285d(TAG, "NPE exception occured during isTokenProvisioned call");
                        if (query != null) {
                            query.close();
                        }
                        return z;
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = query;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
            }
            if (query != null) {
                query.close();
            }
        } catch (NullPointerException e3) {
            e = e3;
            query = null;
            e.printStackTrace();
            Log.m285d(TAG, "NPE exception occured during isTokenProvisioned call");
            if (query != null) {
                query.close();
            }
            return z;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return z;
    }

    public long getDbIdFromTokenUniqueRefence(String str) {
        NullPointerException e;
        Cursor cursor;
        Throwable th;
        Cursor cursor2 = null;
        try {
            Cursor query = this.db.query(getTableName(), new String[]{"_id", CardMaster.COL_TOKEN_UNIQUE_REFERENCE}, "tokenUniqueReference=?", new String[]{str}, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        long j = query.getLong(query.getColumnIndex("_id"));
                        if (query == null) {
                            return j;
                        }
                        query.close();
                        return j;
                    }
                } catch (NullPointerException e2) {
                    e = e2;
                    cursor = query;
                    try {
                        e.printStackTrace();
                        Log.m285d(TAG, "NPE exception occured during isTokenProvisioned call");
                        if (cursor != null) {
                            cursor.close();
                        }
                        return -1;
                    } catch (Throwable th2) {
                        th = th2;
                        cursor2 = cursor;
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    cursor2 = query;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            }
            Log.m286e(TAG, " getDbIdFromTokenUniqueRefence : cursor is null");
            if (query != null) {
                query.close();
            }
            return -1;
        } catch (NullPointerException e3) {
            e = e3;
            cursor = null;
            e.printStackTrace();
            Log.m285d(TAG, "NPE exception occured during isTokenProvisioned call");
            if (cursor != null) {
                cursor.close();
            }
            return -1;
        } catch (Throwable th4) {
            th = th4;
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    public long deleteStaleEnrollmentData() {
        if (this.db == null) {
            return -1;
        }
        return (long) this.db.delete(getTableName(), "isProvisioned > 1", null);
    }
}
