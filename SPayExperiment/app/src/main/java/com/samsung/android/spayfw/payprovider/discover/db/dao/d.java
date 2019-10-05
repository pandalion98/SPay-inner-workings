/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.io.UnsupportedEncodingException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardProfile;
import java.io.UnsupportedEncodingException;

public class d
extends b<DcCardProfile> {
    private Gson mGson = new GsonBuilder().create();

    public d(Context context) {
        super(context);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected ContentValues a(DcCardProfile dcCardProfile) {
        byte[] arrby;
        if (dcCardProfile == null) {
            Log.e("DCSDK_DcCardProfileDaoImpl", "getContentValues: Data null");
            return null;
        }
        if (dcCardProfile.getCardMasterId() == -1L) {
            Log.e("DCSDK_DcCardProfileDaoImpl", "getContentValues: INVALID_ROW_ID");
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("cardMasterId", Long.valueOf((long)dcCardProfile.getCardMasterId()));
        contentValues.put("dataId", Integer.valueOf((int)DcCommonDao.DetailDataId.sJ.cJ()));
        try {
            String string = this.mGson.toJson((Object)dcCardProfile);
            if (TextUtils.isEmpty((CharSequence)string)) {
                Log.e("DCSDK_DcCardProfileDaoImpl", "getContentValues: jsonData failed");
                return null;
            }
            arrby = string.getBytes("UTF8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        }
        contentValues.put("data", arrby);
        return contentValues;
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    protected DcCardProfile f(Cursor cursor) {
        if (cursor == null) {
            Log.e("DCSDK_DcCardProfileDaoImpl", "getContentValues: cursor null");
            return null;
        }
        byte[] arrby = cursor.getBlob(cursor.getColumnIndex("data"));
        if (arrby == null) {
            Log.e("DCSDK_DcCardProfileDaoImpl", "getContentValues: byteData null");
            return null;
        }
        try {
            String string = new String(arrby, "UTF8");
            DcCardProfile dcCardProfile = (DcCardProfile)this.mGson.fromJson(string, DcCardProfile.class);
            return dcCardProfile;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
            return null;
        }
    }

    @Override
    protected /* synthetic */ ContentValues getContentValues(Object object) {
        return this.a((DcCardProfile)object);
    }

    @Override
    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return this.f(cursor);
    }

    @Override
    protected String getTableName() {
        return "CardDetails";
    }
}

