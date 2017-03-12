package com.samsung.android.spayfw.payprovider.plcc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.p009a.DBHelperWrapper;
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.util.LogHelper;
import com.samsung.android.spayfw.utils.DBUtils;
import java.util.ArrayList;
import java.util.List;

public class PlccCardDetailsDaoImpl implements PlccCardDetailsDao {
    private static final String TAG = "PlccDBImpl";
    private static PlccCardDetailsDaoImpl sPlccCardDetailsDaoImpl;
    private SQLiteDatabase db;
    private DBHelperWrapper mPlccDBhelperWrapper;

    public static PlccCardDetailsDaoImpl getInstance(Context context) {
        if (sPlccCardDetailsDaoImpl == null) {
            sPlccCardDetailsDaoImpl = new PlccCardDetailsDaoImpl(context);
        }
        return sPlccCardDetailsDaoImpl;
    }

    private PlccCardDetailsDaoImpl(Context context) {
        this.mPlccDBhelperWrapper = null;
        this.mPlccDBhelperWrapper = DBHelperWrapper.m689b(context, null, 0, DBName.PlccCardData_enc);
        this.db = this.mPlccDBhelperWrapper.getWritableDatabase(DBUtils.getDbPassword());
    }

    public boolean addCard(PlccCard plccCard) {
        if (plccCard == null || plccCard.getProviderKey() == null) {
            return false;
        }
        this.db.beginTransaction();
        try {
            if (containsCard(plccCard, this.db)) {
                return false;
            }
            ContentValues transcationContentValues = getTranscationContentValues(plccCard);
            transcationContentValues.put(PlccCardSchema.COLUMN_NAME_PROVIDERKEY, plccCard.getProviderKey());
            LogHelper.m1079v("inserted row for cardToken=" + plccCard.getProviderKey() + "; result=" + this.db.insert(PlccCardSchema.TABLE_NAME, null, transcationContentValues));
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
            return true;
        } catch (Throwable e) {
            LogHelper.m1076e(TAG, "addCard exception");
            Log.m284c(TAG, e.getMessage(), e);
            return false;
        } finally {
            this.db.endTransaction();
        }
    }

    private ContentValues getTranscationContentValues(PlccCard plccCard) {
        ContentValues contentValues = new ContentValues();
        if (plccCard.getTzEncCard() != null) {
            contentValues.put(PlccCardSchema.COLUMN_NAME_TZ_DATA, plccCard.getTzEncCard());
        }
        if (plccCard.getTokenStatus() != null) {
            contentValues.put(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS, plccCard.getTokenStatus());
        }
        if (plccCard.getMerchantId() != null) {
            contentValues.put(PlccCardSchema.COLUMN_NAME_MERCHANTID, plccCard.getMerchantId());
        }
        if (plccCard.getDefaultSequenceConfig() != null) {
            contentValues.put(PlccCardSchema.COLUMN_NAME_DEFAULT_SEQUENCE_CONFIG, plccCard.getDefaultSequenceConfig());
        }
        if (plccCard.getTrTokenId() != null) {
            contentValues.put(PlccCardSchema.COLUMN_NAME_TR_TOKEN_ID, plccCard.getTrTokenId());
        }
        if (plccCard.getTimestamp() != null) {
            contentValues.put(PlccCardSchema.COLUMN_NAME_TIMESTAMP, plccCard.getTimestamp());
        }
        return contentValues;
    }

    public boolean updateCard(PlccCard plccCard) {
        SQLiteDatabase sQLiteDatabase = true;
        if (plccCard == null || plccCard.getProviderKey() == null) {
            return false;
        }
        this.db.beginTransaction();
        try {
            String[] strArr = new String[]{plccCard.getProviderKey()};
            this.db.update(PlccCardSchema.TABLE_NAME, getTranscationContentValues(plccCard), "providerKey = ?", strArr);
            this.db.setTransactionSuccessful();
            return sQLiteDatabase;
        } catch (Throwable e) {
            LogHelper.m1076e(TAG, "updateCard exception");
            Log.m284c(TAG, e.getMessage(), e);
            return false;
        } finally {
            sQLiteDatabase = this.db;
            sQLiteDatabase.endTransaction();
        }
    }

    public List<PlccCard> listCard() {
        List<PlccCard> arrayList = new ArrayList();
        this.db.beginTransaction();
        Cursor cursor = null;
        try {
            cursor = this.db.rawQuery("SELECT * FROM entry", new String[0]);
            if (cursor == null) {
                this.db.endTransaction();
                if (cursor != null) {
                    cursor.close();
                }
            } else {
                int columnIndex = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_PROVIDERKEY);
                int columnIndex2 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TZ_DATA);
                int columnIndex3 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_MERCHANTID);
                int columnIndex4 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS);
                int columnIndex5 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_DEFAULT_SEQUENCE_CONFIG);
                int columnIndex6 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TR_TOKEN_ID);
                int columnIndex7 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TIMESTAMP);
                while (cursor.moveToNext()) {
                    PlccCard plccCard = new PlccCard();
                    plccCard.setProviderKey(cursor.getString(columnIndex));
                    plccCard.setMerchantId(cursor.getString(columnIndex3));
                    plccCard.setTokenStatus(cursor.getString(columnIndex4));
                    String string = cursor.getString(columnIndex2);
                    plccCard.setDefaultSequenceConfig(cursor.getString(columnIndex5));
                    plccCard.setTrTokenId(cursor.getString(columnIndex6));
                    plccCard.setTimestamp(cursor.getString(columnIndex7));
                    plccCard.setTzEncCard(string);
                    arrayList.add(plccCard);
                }
                this.db.setTransactionSuccessful();
                this.db.endTransaction();
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Throwable e) {
            LogHelper.m1076e(TAG, "listCard exception");
            Log.m284c(TAG, e.getMessage(), e);
            this.db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            this.db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    public boolean updateSequenceConfig(String str, String str2) {
        SQLiteDatabase sQLiteDatabase = null;
        if (str == null || str2 == null) {
            return false;
        }
        this.db.beginTransaction();
        try {
            String[] strArr = new String[]{str};
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlccCardSchema.COLUMN_NAME_DEFAULT_SEQUENCE_CONFIG, str2);
            this.db.update(PlccCardSchema.TABLE_NAME, contentValues, "merchantID = ?", strArr);
            this.db.setTransactionSuccessful();
            return true;
        } catch (Throwable e) {
            LogHelper.m1076e(TAG, "updateSequenceConfig exception");
            Log.m284c(TAG, e.getMessage(), e);
            return sQLiteDatabase;
        } finally {
            sQLiteDatabase = this.db;
            sQLiteDatabase.endTransaction();
        }
    }

    private boolean containsCard(PlccCard plccCard, SQLiteDatabase sQLiteDatabase) {
        Throwable th;
        Cursor cursor;
        boolean z = true;
        Cursor cursor2 = null;
        try {
            cursor2 = sQLiteDatabase.rawQuery("SELECT COUNT(*) FROM entry WHERE providerKey = ?", new String[]{plccCard.getProviderKey()});
            if (cursor2 != null) {
                try {
                    if (cursor2.moveToFirst()) {
                        if (cursor2.getInt(0) <= 0) {
                            z = false;
                        }
                        if (cursor2 == null) {
                            return z;
                        }
                        cursor2.close();
                        return z;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = cursor2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursor2 != null) {
                cursor2.close();
            }
            return false;
        } catch (Throwable th3) {
            th = th3;
            cursor = cursor2;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean removeCard(com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard r8) {
        /*
        r7 = this;
        r0 = 1;
        r1 = 0;
        r2 = r8.getProviderKey();
        if (r2 != 0) goto L_0x0009;
    L_0x0008:
        return r1;
    L_0x0009:
        r3 = "providerKey = ?";
        r4 = 1;
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x0035 }
        r5 = 0;
        r4[r5] = r2;	 Catch:{ Exception -> 0x0035 }
        r5 = r7.db;	 Catch:{ Exception -> 0x0035 }
        r6 = "entry";
        r3 = r5.delete(r6, r3, r4);	 Catch:{ Exception -> 0x0035 }
        if (r3 == 0) goto L_0x0033;
    L_0x001b:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "delete row for cardToken=";
        r1 = r1.append(r3);
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.payprovider.plcc.util.LogHelper.m1079v(r1);
        r1 = r0;
        goto L_0x0008;
    L_0x0033:
        r0 = r1;
        goto L_0x001b;
    L_0x0035:
        r0 = move-exception;
        r3 = "PlccDBImpl";
        r4 = "removeCard exception";
        com.samsung.android.spayfw.payprovider.plcc.util.LogHelper.m1076e(r3, r4);	 Catch:{ all -> 0x005d }
        r3 = "PlccDBImpl";
        r4 = r0.getMessage();	 Catch:{ all -> 0x005d }
        com.samsung.android.spayfw.p002b.Log.m284c(r3, r4, r0);	 Catch:{ all -> 0x005d }
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = "delete row for cardToken=";
        r0 = r0.append(r3);
        r0 = r0.append(r2);
        r0 = r0.toString();
        com.samsung.android.spayfw.payprovider.plcc.util.LogHelper.m1079v(r0);
        goto L_0x0008;
    L_0x005d:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "delete row for cardToken=";
        r1 = r1.append(r3);
        r1 = r1.append(r2);
        r1 = r1.toString();
        com.samsung.android.spayfw.payprovider.plcc.util.LogHelper.m1079v(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.plcc.db.PlccCardDetailsDaoImpl.removeCard(com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard):boolean");
    }

    public PlccCard selectCard(String str) {
        PlccCard plccCard = new PlccCard();
        if (str != null) {
            String str2 = "SELECT * FROM entry WHERE providerKey = ?";
            String[] strArr = new String[]{str};
            this.db.beginTransaction();
            Cursor cursor = null;
            try {
                cursor = this.db.rawQuery(str2, strArr);
                if (cursor == null) {
                    this.db.endTransaction();
                    if (cursor != null) {
                        cursor.close();
                    }
                } else {
                    int columnIndex = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_PROVIDERKEY);
                    int columnIndex2 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TR_TOKEN_ID);
                    int columnIndex3 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TZ_DATA);
                    int columnIndex4 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_MERCHANTID);
                    int columnIndex5 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TOKEN_STATUS);
                    int columnIndex6 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_DEFAULT_SEQUENCE_CONFIG);
                    int columnIndex7 = cursor.getColumnIndex(PlccCardSchema.COLUMN_NAME_TIMESTAMP);
                    while (cursor.moveToNext()) {
                        plccCard.setProviderKey(cursor.getString(columnIndex));
                        plccCard.setMerchantId(cursor.getString(columnIndex4));
                        plccCard.setTokenStatus(cursor.getString(columnIndex5));
                        plccCard.setTrTokenId(cursor.getString(columnIndex2));
                        plccCard.setTzEncCard(cursor.getString(columnIndex3));
                        plccCard.setDefaultSequenceConfig(cursor.getString(columnIndex6));
                        plccCard.setTimestamp(cursor.getString(columnIndex7));
                    }
                    this.db.setTransactionSuccessful();
                    this.db.endTransaction();
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } catch (Throwable e) {
                Log.m285d(TAG, "selectCard exception");
                Log.m284c(TAG, e.getMessage(), e);
                this.db.endTransaction();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                this.db.endTransaction();
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return plccCard;
    }

    public String getMstConfig(String str) {
        return selectCard(str).getDefaultSequenceConfig();
    }
}
