/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.JsonSyntaxException
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Type
 */
package com.samsung.android.spayfw.payprovider.mastercard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class McCardProfileDaoImpl<T>
extends McCommonCardDaoImpl<MCBaseCardProfile<T>> {
    private static final String COL_DATA_ID = "dataId";
    private static final String TAG = "McCardProfileDaoImpl";
    private Type mType;

    public McCardProfileDaoImpl(Context context, Type type) {
        super(context);
        this.mType = type;
    }

    private ContentValues getContentValuesForProfileTable(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        String string;
        byte[] arrby;
        block5 : {
            if (contentValues == null || mCBaseCardProfile == null) {
                return null;
            }
            contentValues.clear();
            contentValues.put("cardMasterId", Long.valueOf((long)mCBaseCardProfile.getUniqueTokenReferenceId()));
            contentValues.put(COL_DATA_ID, Integer.valueOf((int)McCommonCardDaoImpl.CardInfoType.PROFILE_TABLE.getValue()));
            contentValues.put("data", mCBaseCardProfile.getTADataContainer());
            string = this.createGson(McCommonCardDaoImpl.CardInfoType.PROFILE_TABLE).toJson((Object)mCBaseCardProfile.getTAProfilesTable());
            if (!TextUtils.isEmpty((CharSequence)string)) break block5;
            return null;
        }
        try {
            arrby = string.getBytes("UTF8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(TAG, "getContentValuesForProfileTable: : " + unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            Log.e(TAG, "getContentValuesForProfileTable: : " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        contentValues.put("data", arrby);
        return contentValues;
    }

    private ContentValues getContentValuesForTa(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        if (contentValues == null || mCBaseCardProfile == null) {
            return null;
        }
        contentValues.clear();
        contentValues.put("cardMasterId", Long.valueOf((long)mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf((int)McCommonCardDaoImpl.CardInfoType.TA_DATA.getValue()));
        contentValues.put("data", mCBaseCardProfile.getTADataContainer());
        return contentValues;
    }

    private ContentValues getContentValuesForTaAtc(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        if (contentValues == null || mCBaseCardProfile == null) {
            return null;
        }
        contentValues.clear();
        contentValues.put("cardMasterId", Long.valueOf((long)mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf((int)McCommonCardDaoImpl.CardInfoType.TA_ATC_DATA.getValue()));
        contentValues.put("data", mCBaseCardProfile.getTaAtcContainer());
        return contentValues;
    }

    private ContentValues getContentValuesForUnusedDgiElemnet(ContentValues contentValues, MCBaseCardProfile<T> mCBaseCardProfile) {
        String string;
        byte[] arrby;
        block6 : {
            if (contentValues == null || mCBaseCardProfile == null || mCBaseCardProfile.getUnusedDGIElements() == null) {
                return null;
            }
            if (mCBaseCardProfile.getUnusedDGIElements() == null) {
                Log.d(TAG, "UnusedDGIElements is null");
            }
            contentValues.clear();
            contentValues.put("cardMasterId", Long.valueOf((long)mCBaseCardProfile.getUniqueTokenReferenceId()));
            contentValues.put(COL_DATA_ID, Integer.valueOf((int)McCommonCardDaoImpl.CardInfoType.UNUSED_DGI_ELEMENTS.getValue()));
            string = this.createGson(McCommonCardDaoImpl.CardInfoType.UNUSED_DGI_ELEMENTS).toJson((Object)mCBaseCardProfile.getUnusedDGIElements());
            if (!TextUtils.isEmpty((CharSequence)string)) break block6;
            return null;
        }
        try {
            arrby = string.getBytes("UTF8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(TAG, "getContentValuesForUnusedDgiElemnet: : " + unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            Log.e(TAG, "getContentValuesForUnusedDgiElemnet: : " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        contentValues.put("data", arrby);
        return contentValues;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private MCBaseCardProfile<T> getProfileData(long l2) {
        Object object;
        Cursor cursor;
        block4 : {
            Object object2;
            try {
                Cursor cursor2;
                cursor = cursor2 = this.query(l2, McCommonCardDaoImpl.CardInfoType.MCPSE_CARD_PROFILE, null);
                object = null;
                if (cursor == null) break block4;
            }
            catch (Throwable throwable) {
                void var3_8;
                cursor = null;
                if (cursor == null) throw var3_8;
                cursor.close();
                throw var3_8;
            }
            boolean bl = cursor.moveToFirst();
            object = null;
            if (!bl) break block4;
            object = object2 = this.getDataValues(cursor);
            {
                catch (Throwable throwable) {}
            }
        }
        if (cursor == null) return object;
        cursor.close();
        return object;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private byte[] getProvisionData(long l2, McCommonCardDaoImpl.CardInfoType cardInfoType) {
        byte[] arrby;
        Cursor cursor;
        block4 : {
            byte[] arrby2;
            try {
                Cursor cursor2;
                cursor = cursor2 = this.query(l2, cardInfoType, null);
                arrby = null;
                if (cursor == null) break block4;
            }
            catch (Throwable throwable) {
                void var4_9;
                cursor = null;
                if (cursor == null) throw var4_9;
                cursor.close();
                throw var4_9;
            }
            boolean bl = cursor.moveToFirst();
            arrby = null;
            if (!bl) break block4;
            arrby = arrby2 = cursor.getBlob(cursor.getColumnIndex("data"));
            {
                catch (Throwable throwable) {}
            }
        }
        if (cursor == null) return arrby;
        cursor.close();
        return arrby;
    }

    private Cursor query(long l2, McCommonCardDaoImpl.CardInfoType cardInfoType, String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            string = this.getQuerySearch(l2) + " and " + COL_DATA_ID + "=" + cardInfoType.getValue();
        }
        return this.query(string, null, this.getQuerySearch(l2) + " DESC");
    }

    private Cursor query(String string, String[] arrstring, String string2) {
        if (this.db == null) {
            return null;
        }
        return this.db.query(this.getTableName(), null, string, arrstring, null, null, string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected ContentValues getContentValues(MCBaseCardProfile<T> mCBaseCardProfile) {
        byte[] arrby;
        if (mCBaseCardProfile == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        Gson gson = this.createGson(McCommonCardDaoImpl.CardInfoType.MCPSE_CARD_PROFILE);
        contentValues.put("cardMasterId", Long.valueOf((long)mCBaseCardProfile.getUniqueTokenReferenceId()));
        contentValues.put(COL_DATA_ID, Integer.valueOf((int)McCommonCardDaoImpl.CardInfoType.MCPSE_CARD_PROFILE.getValue()));
        try {
            String string = gson.toJson(mCBaseCardProfile.getDigitalizedCardContainer(), this.mType);
            if (TextUtils.isEmpty((CharSequence)string)) return null;
            arrby = string.getBytes("UTF8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(TAG, "getContentValues: : " + unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            Log.e(TAG, "getContentValues: : " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        contentValues.put("data", arrby);
        return contentValues;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected ContentValues getContentValues(MCBaseCardProfile<T> mCBaseCardProfile, long l2) {
        ContentValues contentValues;
        if (mCBaseCardProfile == null || l2 < 0L || (contentValues = this.getContentValues(mCBaseCardProfile)) == null) {
            return null;
        }
        contentValues.put("cardMasterId", Long.valueOf((long)l2));
        return contentValues;
    }

    @Override
    public MCBaseCardProfile<T> getData(long l2) {
        MCBaseCardProfile<T> mCBaseCardProfile = this.getProfileData(l2);
        if (mCBaseCardProfile == null) {
            return mCBaseCardProfile;
        }
        mCBaseCardProfile.setTADataContainer(this.getTaData(l2));
        mCBaseCardProfile.setTAProfilesTable(this.getProfileTable(l2));
        mCBaseCardProfile.setTaAtcContainer(this.getTaAtcData(l2));
        mCBaseCardProfile.setUnusedDGIElements(this.getUnusedDgiElements(l2));
        return mCBaseCardProfile;
    }

    @Override
    protected MCBaseCardProfile<T> getDataValues(Cursor cursor) {
        Object object;
        if (cursor == null) {
            return null;
        }
        Gson gson = this.createGson(McCommonCardDaoImpl.CardInfoType.MCPSE_CARD_PROFILE);
        byte[] arrby = cursor.getBlob(cursor.getColumnIndex("data"));
        MCBaseCardProfile<Object> mCBaseCardProfile = new MCBaseCardProfile<Object>();
        try {
            object = gson.fromJson(new String(arrby, "UTF8"), this.mType);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(TAG, "getDataValues: : " + unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
            return null;
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            Log.e(TAG, "getDataValues: : " + jsonSyntaxException.getMessage());
            jsonSyntaxException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            Log.e(TAG, "getDataValues: : " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
        mCBaseCardProfile.setDigitalizedCardContainer(object);
        return mCBaseCardProfile;
    }

    public MCProfilesTable getProfileTable(long l2) {
        byte[] arrby = this.getProvisionData(l2, McCommonCardDaoImpl.CardInfoType.PROFILE_TABLE);
        if (arrby == null) {
            return null;
        }
        Gson gson = this.createGson(McCommonCardDaoImpl.CardInfoType.PROFILE_TABLE);
        try {
            MCProfilesTable mCProfilesTable = (MCProfilesTable)gson.fromJson(new String(arrby, "UTF8"), MCProfilesTable.class);
            return mCProfilesTable;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(TAG, "getProfileTable: : " + unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
            return null;
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            Log.e(TAG, "getProfileTable: : " + jsonSyntaxException.getMessage());
            jsonSyntaxException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            Log.e(TAG, "getProfileTable: : " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected String getQuerySearch(long l2) {
        return "cardMasterId = " + l2;
    }

    public byte[] getTaAtcData(long l2) {
        return this.getProvisionData(l2, McCommonCardDaoImpl.CardInfoType.TA_ATC_DATA);
    }

    public byte[] getTaData(long l2) {
        return this.getProvisionData(l2, McCommonCardDaoImpl.CardInfoType.TA_DATA);
    }

    @Override
    protected String getTableName() {
        return "CARD_PROVISION_DATA";
    }

    public MCUnusedDGIElements getUnusedDgiElements(long l2) {
        byte[] arrby = this.getProvisionData(l2, McCommonCardDaoImpl.CardInfoType.UNUSED_DGI_ELEMENTS);
        if (arrby == null) {
            Log.d(TAG, "getUnusedDgiElements : saved unused Element is null");
            return null;
        }
        Gson gson = this.createGson(McCommonCardDaoImpl.CardInfoType.UNUSED_DGI_ELEMENTS);
        try {
            MCUnusedDGIElements mCUnusedDGIElements = (MCUnusedDGIElements)gson.fromJson(new String(arrby, "UTF8"), MCUnusedDGIElements.class);
            return mCUnusedDGIElements;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.e(TAG, "getUnusedDgiElements: : " + unsupportedEncodingException.getMessage());
            unsupportedEncodingException.printStackTrace();
            return null;
        }
        catch (JsonSyntaxException jsonSyntaxException) {
            Log.e(TAG, "getUnusedDgiElements: : " + jsonSyntaxException.getMessage());
            jsonSyntaxException.printStackTrace();
            return null;
        }
        catch (Exception exception) {
            Log.e(TAG, "getUnusedDgiElements: : " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    /*
     * Exception decompiling
     */
    @Override
    public long saveData(MCBaseCardProfile<T> var1_1) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [6[CATCHBLOCK]], but top level block is 7[CATCHBLOCK]
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

    @Override
    public boolean updateData(MCBaseCardProfile<T> mCBaseCardProfile, long l2) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = this.getContentValues(mCBaseCardProfile);
        SQLiteDatabase sQLiteDatabase = this.db;
        String string = this.getTableName();
        String string2 = this.getQuerySearch(l2) + " AND " + COL_DATA_ID + " =?";
        String[] arrstring = new String[]{String.valueOf((int)McCommonCardDaoImpl.CardInfoType.MCPSE_CARD_PROFILE.getValue())};
        int n2 = sQLiteDatabase.update(string, contentValues, string2, arrstring);
        if (n2 != 1) {
            Log.e(TAG, "updateData: MultipleRows Updated: " + n2);
            return false;
        }
        return true;
    }

    public boolean updateTaData(byte[] arrby, long l2) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", arrby);
        SQLiteDatabase sQLiteDatabase = this.db;
        String string = this.getTableName();
        String string2 = this.getQuerySearch(l2) + " AND " + COL_DATA_ID + " =?";
        String[] arrstring = new String[]{String.valueOf((int)McCommonCardDaoImpl.CardInfoType.TA_DATA.getValue())};
        int n2 = sQLiteDatabase.update(string, contentValues, string2, arrstring);
        if (n2 != 1) {
            Log.e(TAG, "updateTaData: MultipleRows Updated: " + n2);
            return false;
        }
        return true;
    }

    public boolean updateUnusedDgiElements(byte[] arrby, long l2) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", arrby);
        SQLiteDatabase sQLiteDatabase = this.db;
        String string = this.getTableName();
        String string2 = this.getQuerySearch(l2) + " AND " + COL_DATA_ID + " =?";
        String[] arrstring = new String[]{String.valueOf((int)McCommonCardDaoImpl.CardInfoType.UNUSED_DGI_ELEMENTS.getValue())};
        int n2 = sQLiteDatabase.update(string, contentValues, string2, arrstring);
        if (n2 != 1) {
            Log.e(TAG, "updateUnusedDgiElements: MultipleRows Updated: " + n2);
            return false;
        }
        return true;
    }

    public boolean updateWrappedAtcData(byte[] arrby, long l2) {
        if (this.db == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", arrby);
        SQLiteDatabase sQLiteDatabase = this.db;
        String string = this.getTableName();
        String string2 = this.getQuerySearch(l2) + " AND " + COL_DATA_ID + " =?";
        String[] arrstring = new String[]{String.valueOf((int)McCommonCardDaoImpl.CardInfoType.TA_ATC_DATA.getValue())};
        int n2 = sQLiteDatabase.update(string, contentValues, string2, arrstring);
        if (n2 != 1) {
            Log.e(TAG, "updateWrappedAtcData: MultipleRows Updated: " + n2);
            return false;
        }
        return true;
    }
}

