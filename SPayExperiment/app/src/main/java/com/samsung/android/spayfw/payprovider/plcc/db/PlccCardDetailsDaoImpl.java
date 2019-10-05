/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.plcc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.e.a.a;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.util.LogHelper;
import java.util.ArrayList;
import java.util.List;

public class PlccCardDetailsDaoImpl
implements PlccCardDetailsDao {
    private static final String TAG = "PlccDBImpl";
    private static PlccCardDetailsDaoImpl sPlccCardDetailsDaoImpl;
    private SQLiteDatabase db;
    private a mPlccDBhelperWrapper = null;

    private PlccCardDetailsDaoImpl(Context context) {
        this.mPlccDBhelperWrapper = a.b(context, null, 0, DBName.ox);
        this.db = this.mPlccDBhelperWrapper.getWritableDatabase(com.samsung.android.spayfw.utils.c.getDbPassword());
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean containsCard(PlccCard plccCard, SQLiteDatabase sQLiteDatabase) {
        Cursor cursor;
        block4 : {
            boolean bl;
            block5 : {
                bl = true;
                String[] arrstring = new String[bl];
                arrstring[0] = plccCard.getProviderKey();
                cursor = sQLiteDatabase.rawQuery("SELECT COUNT(*) FROM entry WHERE providerKey = ?", arrstring);
                if (cursor == null) break block4;
                if (!cursor.moveToFirst()) break block4;
                int n2 = cursor.getInt(0);
                if (n2 > 0) break block5;
                bl = false;
            }
            if (cursor == null) return bl;
            cursor.close();
            return bl;
        }
        if (cursor == null) return false;
        cursor.close();
        return false;
        catch (Throwable throwable) {
            void var5_8;
            Cursor cursor2;
            block6 : {
                cursor2 = null;
                break block6;
                catch (Throwable throwable2) {
                    cursor2 = cursor;
                }
            }
            if (cursor2 == null) throw var5_8;
            cursor2.close();
            throw var5_8;
        }
    }

    public static PlccCardDetailsDaoImpl getInstance(Context context) {
        if (sPlccCardDetailsDaoImpl == null) {
            sPlccCardDetailsDaoImpl = new PlccCardDetailsDaoImpl(context);
        }
        return sPlccCardDetailsDaoImpl;
    }

    private ContentValues getTranscationContentValues(PlccCard plccCard) {
        ContentValues contentValues = new ContentValues();
        if (plccCard.getTzEncCard() != null) {
            contentValues.put("tzData", plccCard.getTzEncCard());
        }
        if (plccCard.getTokenStatus() != null) {
            contentValues.put("tokenStatus", plccCard.getTokenStatus());
        }
        if (plccCard.getMerchantId() != null) {
            contentValues.put("merchantID", plccCard.getMerchantId());
        }
        if (plccCard.getDefaultSequenceConfig() != null) {
            contentValues.put("defaultSequenceConfig", plccCard.getDefaultSequenceConfig());
        }
        if (plccCard.getTrTokenId() != null) {
            contentValues.put("trTokenId", plccCard.getTrTokenId());
        }
        if (plccCard.getTimestamp() != null) {
            contentValues.put("timeStamp", plccCard.getTimestamp());
        }
        return contentValues;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean addCard(PlccCard plccCard) {
        block7 : {
            if (plccCard == null) {
                return false;
            }
            if (plccCard.getProviderKey() == null) return false;
            this.db.beginTransaction();
            boolean bl = this.containsCard(plccCard, this.db);
            if (!bl) break block7;
            this.db.endTransaction();
            return false;
        }
        try {
            ContentValues contentValues = this.getTranscationContentValues(plccCard);
            contentValues.put("providerKey", plccCard.getProviderKey());
            long l2 = this.db.insert("entry", null, contentValues);
            LogHelper.v("inserted row for cardToken=" + plccCard.getProviderKey() + "; result=" + l2);
            this.db.setTransactionSuccessful();
        }
        catch (Exception exception) {
            LogHelper.e(TAG, "addCard exception");
            Log.c(TAG, exception.getMessage(), exception);
            return false;
        }
        finally {
            this.db.endTransaction();
        }
        this.db.endTransaction();
        return true;
    }

    @Override
    public String getMstConfig(String string) {
        return this.selectCard(string).getDefaultSequenceConfig();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public List<PlccCard> listCard() {
        Cursor cursor;
        ArrayList arrayList;
        block11 : {
            Cursor cursor2;
            arrayList = new ArrayList();
            this.db.beginTransaction();
            cursor = null;
            cursor = cursor2 = this.db.rawQuery("SELECT * FROM entry", new String[0]);
            if (cursor != null) break block11;
            this.db.endTransaction();
            if (cursor == null) return arrayList;
            {
                cursor.close();
                return arrayList;
            }
        }
        try {
            int n2 = cursor.getColumnIndex("providerKey");
            int n3 = cursor.getColumnIndex("tzData");
            int n4 = cursor.getColumnIndex("merchantID");
            int n5 = cursor.getColumnIndex("tokenStatus");
            int n6 = cursor.getColumnIndex("defaultSequenceConfig");
            int n7 = cursor.getColumnIndex("trTokenId");
            int n8 = cursor.getColumnIndex("timeStamp");
            while (cursor.moveToNext()) {
                PlccCard plccCard = new PlccCard();
                plccCard.setProviderKey(cursor.getString(n2));
                plccCard.setMerchantId(cursor.getString(n4));
                plccCard.setTokenStatus(cursor.getString(n5));
                String string = cursor.getString(n3);
                plccCard.setDefaultSequenceConfig(cursor.getString(n6));
                plccCard.setTrTokenId(cursor.getString(n7));
                plccCard.setTimestamp(cursor.getString(n8));
                plccCard.setTzEncCard(string);
                arrayList.add((Object)plccCard);
            }
            try {
                this.db.setTransactionSuccessful();
            }
            catch (Exception exception) {}
            LogHelper.e(TAG, "listCard exception");
            Log.c(TAG, exception.getMessage(), exception);
            this.db.endTransaction();
            if (cursor == null) return arrayList;
            {
                cursor.close();
                return arrayList;
            }
        }
        catch (Throwable throwable) {
            this.db.endTransaction();
            if (cursor == null) throw throwable;
            {
                cursor.close();
            }
            throw throwable;
        }
        this.db.endTransaction();
        if (cursor == null) return arrayList;
        {
            cursor.close();
            return arrayList;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean removeCard(PlccCard plccCard) {
        boolean bl;
        String string;
        block6 : {
            bl = true;
            string = plccCard.getProviderKey();
            if (string == null) {
                return false;
            }
            try {
                String[] arrstring = new String[]{string};
                int n2 = this.db.delete("entry", "providerKey = ?", arrstring);
                if (n2 != 0) break block6;
                bl = false;
            }
            catch (Exception exception) {
                LogHelper.e(TAG, "removeCard exception");
                Log.c(TAG, exception.getMessage(), exception);
                return false;
            }
            finally {
                LogHelper.v("delete row for cardToken=" + string);
            }
        }
        LogHelper.v("delete row for cardToken=" + string);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public PlccCard selectCard(String string) {
        PlccCard plccCard;
        Cursor cursor;
        block14 : {
            Cursor cursor2;
            plccCard = new PlccCard();
            if (string == null) return plccCard;
            String[] arrstring = new String[]{string};
            this.db.beginTransaction();
            cursor = null;
            cursor = cursor2 = this.db.rawQuery("SELECT * FROM entry WHERE providerKey = ?", arrstring);
            if (cursor != null) break block14;
            this.db.endTransaction();
            if (cursor == null) return plccCard;
            {
                cursor.close();
                return plccCard;
            }
        }
        try {
            int n2 = cursor.getColumnIndex("providerKey");
            int n3 = cursor.getColumnIndex("trTokenId");
            int n4 = cursor.getColumnIndex("tzData");
            int n5 = cursor.getColumnIndex("merchantID");
            int n6 = cursor.getColumnIndex("tokenStatus");
            int n7 = cursor.getColumnIndex("defaultSequenceConfig");
            int n8 = cursor.getColumnIndex("timeStamp");
            while (cursor.moveToNext()) {
                plccCard.setProviderKey(cursor.getString(n2));
                plccCard.setMerchantId(cursor.getString(n5));
                plccCard.setTokenStatus(cursor.getString(n6));
                plccCard.setTrTokenId(cursor.getString(n3));
                plccCard.setTzEncCard(cursor.getString(n4));
                plccCard.setDefaultSequenceConfig(cursor.getString(n7));
                plccCard.setTimestamp(cursor.getString(n8));
            }
            try {
                this.db.setTransactionSuccessful();
            }
            catch (Exception exception) {}
            Log.d(TAG, "selectCard exception");
            Log.c(TAG, exception.getMessage(), exception);
            this.db.endTransaction();
            if (cursor == null) return plccCard;
            {
                cursor.close();
                return plccCard;
            }
        }
        catch (Throwable throwable) {
            this.db.endTransaction();
            if (cursor == null) throw throwable;
            {
                cursor.close();
            }
            throw throwable;
        }
        this.db.endTransaction();
        if (cursor == null) {
            return plccCard;
        }
        cursor.close();
        return plccCard;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean updateCard(PlccCard plccCard) {
        if (plccCard == null) {
            return false;
        }
        if (plccCard.getProviderKey() == null) return false;
        this.db.beginTransaction();
        try {
            ContentValues contentValues = this.getTranscationContentValues(plccCard);
            String[] arrstring = new String[]{plccCard.getProviderKey()};
            this.db.update("entry", contentValues, "providerKey = ?", arrstring);
            this.db.setTransactionSuccessful();
            return true;
        }
        catch (Exception exception) {
            LogHelper.e(TAG, "updateCard exception");
            Log.c(TAG, exception.getMessage(), exception);
            return false;
        }
        finally {
            this.db.endTransaction();
        }
    }

    @Override
    public boolean updateSequenceConfig(String string, String string2) {
        if (string == null || string2 == null) {
            return false;
        }
        this.db.beginTransaction();
        try {
            String[] arrstring = new String[]{string};
            ContentValues contentValues = new ContentValues();
            contentValues.put("defaultSequenceConfig", string2);
            this.db.update("entry", contentValues, "merchantID = ?", arrstring);
            this.db.setTransactionSuccessful();
            return true;
        }
        catch (Exception exception) {
            LogHelper.e(TAG, "updateSequenceConfig exception");
            Log.c(TAG, exception.getMessage(), exception);
            return false;
        }
        finally {
            this.db.endTransaction();
        }
    }
}

