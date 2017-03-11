package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardProvisionData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class McCardProfileDaoImpl<T> extends McCommonCardDaoImpl<MCBaseCardProfile<T>> {
    private static final String COL_DATA_ID = "dataId";
    private static final String TAG = "McCardProfileDaoImpl";
    private Type mType;

    public McCardProfileDaoImpl(Context context, Type type) {
        super(context);
        this.mType = type;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long saveData(com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile<T> r15) {
        /*
        r14 = this;
        r0 = -1;
        r2 = r14.db;
        if (r2 != 0) goto L_0x000e;
    L_0x0006:
        r2 = "McCardProfileDaoImpl";
        r3 = "saveData :NPE";
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);
    L_0x000d:
        return r0;
    L_0x000e:
        r2 = r14.db;
        r2.beginTransaction();
        r2 = r14.getContentValues(r15);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        if (r2 != 0) goto L_0x0047;
    L_0x0019:
        r2 = new java.lang.Exception;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r3 = "Card Profile Data null";
        r2.<init>(r3);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        throw r2;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
    L_0x0021:
        r2 = move-exception;
        r3 = "McCardProfileDaoImpl";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009f }
        r4.<init>();	 Catch:{ all -> 0x009f }
        r5 = "saveData: SQLException during insert";
        r4 = r4.append(r5);	 Catch:{ all -> 0x009f }
        r5 = r2.getMessage();	 Catch:{ all -> 0x009f }
        r4 = r4.append(r5);	 Catch:{ all -> 0x009f }
        r4 = r4.toString();	 Catch:{ all -> 0x009f }
        com.samsung.android.spayfw.p002b.Log.m286e(r3, r4);	 Catch:{ all -> 0x009f }
        r2.printStackTrace();	 Catch:{ all -> 0x009f }
        r2 = r14.db;
        r2.endTransaction();
        goto L_0x000d;
    L_0x0047:
        r3 = r14.db;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r4 = r14.getTableName();	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r5 = 0;
        r4 = r3.insertOrThrow(r4, r5, r2);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r2 = r14.getContentValuesForTa(r2, r15);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        if (r2 != 0) goto L_0x0086;
    L_0x0058:
        r2 = new java.lang.Exception;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r3 = "Ta Data null";
        r2.<init>(r3);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        throw r2;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
    L_0x0060:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ all -> 0x009f }
        r3 = "McCardProfileDaoImpl";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009f }
        r4.<init>();	 Catch:{ all -> 0x009f }
        r5 = "saveData :Exception occured during saveData: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x009f }
        r2 = r2.getMessage();	 Catch:{ all -> 0x009f }
        r2 = r4.append(r2);	 Catch:{ all -> 0x009f }
        r2 = r2.toString();	 Catch:{ all -> 0x009f }
        com.samsung.android.spayfw.p002b.Log.m286e(r3, r2);	 Catch:{ all -> 0x009f }
        r2 = r14.db;
        r2.endTransaction();
        goto L_0x000d;
    L_0x0086:
        r3 = r14.db;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r6 = r14.getTableName();	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r7 = 0;
        r6 = r3.insertOrThrow(r6, r7, r2);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r2 = r14.getContentValuesForProfileTable(r2, r15);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        if (r2 != 0) goto L_0x00a6;
    L_0x0097:
        r2 = new java.lang.Exception;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r3 = "Profiles table null";
        r2.<init>(r3);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        throw r2;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
    L_0x009f:
        r0 = move-exception;
        r1 = r14.db;
        r1.endTransaction();
        throw r0;
    L_0x00a6:
        r3 = r14.db;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r8 = r14.getTableName();	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r9 = 0;
        r8 = r3.insertOrThrow(r8, r9, r2);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r2 = r14.getContentValuesForTaAtc(r2, r15);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        if (r2 != 0) goto L_0x00bf;
    L_0x00b7:
        r2 = new java.lang.Exception;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r3 = "Atc Data null";
        r2.<init>(r3);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        throw r2;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
    L_0x00bf:
        r3 = r14.db;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r10 = r14.getTableName();	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r11 = 0;
        r10 = r3.insertOrThrow(r10, r11, r2);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r2 = r14.getContentValuesForUnusedDgiElemnet(r2, r15);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        if (r2 == 0) goto L_0x00e7;
    L_0x00d0:
        r3 = r14.db;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r12 = r14.getTableName();	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r13 = 0;
        r2 = r3.insertOrThrow(r12, r13, r2);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r2 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
        if (r2 != 0) goto L_0x00e7;
    L_0x00df:
        r2 = new java.lang.Exception;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r3 = "unusedDGIelement Saved data failed";
        r2.<init>(r3);	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        throw r2;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
    L_0x00e7:
        r2 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r2 == 0) goto L_0x00fe;
    L_0x00eb:
        r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1));
        if (r2 == 0) goto L_0x00fe;
    L_0x00ef:
        r2 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1));
        if (r2 == 0) goto L_0x00fe;
    L_0x00f3:
        r2 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1));
        if (r2 == 0) goto L_0x00fe;
    L_0x00f7:
        r2 = r14.db;	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r2.setTransactionSuccessful();	 Catch:{ SQLException -> 0x0021, Exception -> 0x0060 }
        r0 = 0;
    L_0x00fe:
        r2 = r14.db;
        r2.endTransaction();
        goto L_0x000d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl.saveData(com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile):long");
    }

    public MCBaseCardProfile<T> getData(long j) {
        MCBaseCardProfile<T> profileData = getProfileData(j);
        if (profileData != null) {
            profileData.setTADataContainer(getTaData(j));
            profileData.setTAProfilesTable(getProfileTable(j));
            profileData.setTaAtcContainer(getTaAtcData(j));
            profileData.setUnusedDGIElements(getUnusedDgiElements(j));
        }
        return profileData;
    }

    public boolean updateData(MCBaseCardProfile<T> mCBaseCardProfile, long j) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = getContentValues((MCBaseCardProfile) mCBaseCardProfile);
        int update = this.db.update(getTableName(), contentValues, getQuerySearch(j) + " AND " + COL_DATA_ID + " =?", new String[]{String.valueOf(CardInfoType.MCPSE_CARD_PROFILE.getValue())});
        if (update == 1) {
            return true;
        }
        Log.m286e(TAG, "updateData: MultipleRows Updated: " + update);
        return false;
    }

    public boolean updateTaData(byte[] bArr, long j) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardProvisionData.COL_DATA, bArr);
        int update = this.db.update(getTableName(), contentValues, getQuerySearch(j) + " AND " + COL_DATA_ID + " =?", new String[]{String.valueOf(CardInfoType.TA_DATA.getValue())});
        if (update == 1) {
            return true;
        }
        Log.m286e(TAG, "updateTaData: MultipleRows Updated: " + update);
        return false;
    }

    public boolean updateWrappedAtcData(byte[] bArr, long j) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardProvisionData.COL_DATA, bArr);
        int update = this.db.update(getTableName(), contentValues, getQuerySearch(j) + " AND " + COL_DATA_ID + " =?", new String[]{String.valueOf(CardInfoType.TA_ATC_DATA.getValue())});
        if (update == 1) {
            return true;
        }
        Log.m286e(TAG, "updateWrappedAtcData: MultipleRows Updated: " + update);
        return false;
    }

    public byte[] getTaData(long j) {
        return getProvisionData(j, CardInfoType.TA_DATA);
    }

    public byte[] getTaAtcData(long j) {
        return getProvisionData(j, CardInfoType.TA_ATC_DATA);
    }

    public boolean updateUnusedDgiElements(byte[] bArr, long j) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(CardProvisionData.COL_DATA, bArr);
        int update = this.db.update(getTableName(), contentValues, getQuerySearch(j) + " AND " + COL_DATA_ID + " =?", new String[]{String.valueOf(CardInfoType.UNUSED_DGI_ELEMENTS.getValue())});
        if (update == 1) {
            return true;
        }
        Log.m286e(TAG, "updateUnusedDgiElements: MultipleRows Updated: " + update);
        return false;
    }

    public MCUnusedDGIElements getUnusedDgiElements(long j) {
        byte[] provisionData = getProvisionData(j, CardInfoType.UNUSED_DGI_ELEMENTS);
        if (provisionData == null) {
            Log.m285d(TAG, "getUnusedDgiElements : saved unused Element is null");
            return null;
        }
        try {
            return (MCUnusedDGIElements) createGson(CardInfoType.UNUSED_DGI_ELEMENTS).fromJson(new String(provisionData, "UTF8"), MCUnusedDGIElements.class);
        } catch (UnsupportedEncodingException e) {
            Log.m286e(TAG, "getUnusedDgiElements: : " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e2) {
            Log.m286e(TAG, "getUnusedDgiElements: : " + e2.getMessage());
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            Log.m286e(TAG, "getUnusedDgiElements: : " + e3.getMessage());
            e3.printStackTrace();
            return null;
        }
    }

    public MCProfilesTable getProfileTable(long j) {
        byte[] provisionData = getProvisionData(j, CardInfoType.PROFILE_TABLE);
        if (provisionData == null) {
            return null;
        }
        try {
            return (MCProfilesTable) createGson(CardInfoType.PROFILE_TABLE).fromJson(new String(provisionData, "UTF8"), MCProfilesTable.class);
        } catch (UnsupportedEncodingException e) {
            Log.m286e(TAG, "getProfileTable: : " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e2) {
            Log.m286e(TAG, "getProfileTable: : " + e2.getMessage());
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            Log.m286e(TAG, "getProfileTable: : " + e3.getMessage());
            e3.printStackTrace();
            return null;
        }
    }

    private byte[] getProvisionData(long j, CardInfoType cardInfoType) {
        Throwable th;
        byte[] bArr = null;
        Cursor query;
        try {
            query = query(j, cardInfoType, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        bArr = query.getBlob(query.getColumnIndex(CardProvisionData.COL_DATA));
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return bArr;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    private ContentValues getContentValuesForTa(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        if (contentValues == null || mCBaseCardProfile == null) {
            return null;
        }
        contentValues.clear();
        contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf(CardInfoType.TA_DATA.getValue()));
        contentValues.put(CardProvisionData.COL_DATA, mCBaseCardProfile.getTADataContainer());
        return contentValues;
    }

    private ContentValues getContentValuesForTaAtc(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        if (contentValues == null || mCBaseCardProfile == null) {
            return null;
        }
        contentValues.clear();
        contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf(CardInfoType.TA_ATC_DATA.getValue()));
        contentValues.put(CardProvisionData.COL_DATA, mCBaseCardProfile.getTaAtcContainer());
        return contentValues;
    }

    private ContentValues getContentValuesForUnusedDgiElemnet(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        if (contentValues == null || mCBaseCardProfile == null || mCBaseCardProfile.getUnusedDGIElements() == null) {
            return null;
        }
        if (mCBaseCardProfile.getUnusedDGIElements() == null) {
            Log.m285d(TAG, "UnusedDGIElements is null");
        }
        contentValues.clear();
        contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf(CardInfoType.UNUSED_DGI_ELEMENTS.getValue()));
        try {
            Object toJson = createGson(CardInfoType.UNUSED_DGI_ELEMENTS).toJson(mCBaseCardProfile.getUnusedDGIElements());
            if (TextUtils.isEmpty(toJson)) {
                return null;
            }
            contentValues.put(CardProvisionData.COL_DATA, toJson.getBytes("UTF8"));
            return contentValues;
        } catch (UnsupportedEncodingException e) {
            Log.m286e(TAG, "getContentValuesForUnusedDgiElemnet: : " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            Log.m286e(TAG, "getContentValuesForUnusedDgiElemnet: : " + e2.getMessage());
            e2.printStackTrace();
            return null;
        }
    }

    private ContentValues getContentValuesForProfileTable(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        if (contentValues == null || mCBaseCardProfile == null) {
            return null;
        }
        contentValues.clear();
        contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf(CardInfoType.PROFILE_TABLE.getValue()));
        contentValues.put(CardProvisionData.COL_DATA, mCBaseCardProfile.getTADataContainer());
        try {
            Object toJson = createGson(CardInfoType.PROFILE_TABLE).toJson(mCBaseCardProfile.getTAProfilesTable());
            if (TextUtils.isEmpty(toJson)) {
                return null;
            }
            contentValues.put(CardProvisionData.COL_DATA, toJson.getBytes("UTF8"));
            return contentValues;
        } catch (UnsupportedEncodingException e) {
            Log.m286e(TAG, "getContentValuesForProfileTable: : " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            Log.m286e(TAG, "getContentValuesForProfileTable: : " + e2.getMessage());
            e2.printStackTrace();
            return null;
        }
    }

    private MCBaseCardProfile<T> getProfileData(long j) {
        Cursor query;
        Throwable th;
        MCBaseCardProfile<T> mCBaseCardProfile = null;
        try {
            query = query(j, CardInfoType.MCPSE_CARD_PROFILE, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        mCBaseCardProfile = getDataValues(query);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return mCBaseCardProfile;
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
    }

    private Cursor query(long j, CardInfoType cardInfoType, String str) {
        if (TextUtils.isEmpty(str)) {
            str = getQuerySearch(j) + " and " + COL_DATA_ID + "=" + cardInfoType.getValue();
        }
        return query(str, null, getQuerySearch(j) + " DESC");
    }

    private Cursor query(String str, String[] strArr, String str2) {
        if (this.db == null) {
            return null;
        }
        return this.db.query(getTableName(), null, str, strArr, null, null, str2);
    }

    protected ContentValues getContentValues(MCBaseCardProfile<T> mCBaseCardProfile) {
        if (mCBaseCardProfile == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        Gson createGson = createGson(CardInfoType.MCPSE_CARD_PROFILE);
        contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf(CardInfoType.MCPSE_CARD_PROFILE.getValue()));
        try {
            Object toJson = createGson.toJson(mCBaseCardProfile.getDigitalizedCardContainer(), this.mType);
            if (TextUtils.isEmpty(toJson)) {
                return null;
            }
            contentValues.put(CardProvisionData.COL_DATA, toJson.getBytes("UTF8"));
            return contentValues;
        } catch (UnsupportedEncodingException e) {
            Log.m286e(TAG, "getContentValues: : " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            Log.m286e(TAG, "getContentValues: : " + e2.getMessage());
            e2.printStackTrace();
            return null;
        }
    }

    protected String getQuerySearch(long j) {
        return "cardMasterId = " + j;
    }

    protected MCBaseCardProfile<T> getDataValues(Cursor cursor) {
        MCBaseCardProfile<T> mCBaseCardProfile = null;
        if (cursor == null) {
            return mCBaseCardProfile;
        }
        Gson createGson = createGson(CardInfoType.MCPSE_CARD_PROFILE);
        byte[] blob = cursor.getBlob(cursor.getColumnIndex(CardProvisionData.COL_DATA));
        MCBaseCardProfile<T> mCBaseCardProfile2 = new MCBaseCardProfile();
        try {
            mCBaseCardProfile2.setDigitalizedCardContainer(createGson.fromJson(new String(blob, "UTF8"), this.mType));
            return mCBaseCardProfile2;
        } catch (UnsupportedEncodingException e) {
            Log.m286e(TAG, "getDataValues: : " + e.getMessage());
            e.printStackTrace();
            return mCBaseCardProfile;
        } catch (JsonSyntaxException e2) {
            Log.m286e(TAG, "getDataValues: : " + e2.getMessage());
            e2.printStackTrace();
            return mCBaseCardProfile;
        } catch (Exception e3) {
            Log.m286e(TAG, "getDataValues: : " + e3.getMessage());
            e3.printStackTrace();
            return mCBaseCardProfile;
        }
    }

    protected String getTableName() {
        return CardProvisionData.TABLE_NAME;
    }

    protected ContentValues getContentValues(MCBaseCardProfile<T> mCBaseCardProfile, long j) {
        if (mCBaseCardProfile == null || j < 0) {
            return null;
        }
        ContentValues contentValues = getContentValues((MCBaseCardProfile) mCBaseCardProfile);
        if (contentValues == null) {
            return null;
        }
        contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(j));
        return contentValues;
    }
}
