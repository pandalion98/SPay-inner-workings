/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.payprovider.discover.db.dao.b;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;

public class c
extends b<DcCardMaster> {
    public c(Context context) {
        super(context);
    }

    protected ContentValues a(DcCardMaster dcCardMaster) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tokenId", dcCardMaster.getTokenId());
        contentValues.put("status", dcCardMaster.getStatus());
        contentValues.put("dpanSuffix", dcCardMaster.getDpanSuffix());
        contentValues.put("fpanSuffix", dcCardMaster.getFpanSuffix());
        contentValues.put("tokenProvisionStatus", Integer.valueOf((int)dcCardMaster.getIsProvisioned()));
        contentValues.put("sessionKeys", dcCardMaster.getSessionKeys());
        contentValues.put("paginationTS", dcCardMaster.getPaginationTS());
        contentValues.put("remainingOtpkCount", Long.valueOf((long)dcCardMaster.getRemainingOtpkCount()));
        contentValues.put("replenishmentThreshold", Long.valueOf((long)dcCardMaster.getReplenishmentThreshold()));
        contentValues.put("number1", Long.valueOf((long)dcCardMaster.getNum1()));
        contentValues.put("number2", Long.valueOf((long)dcCardMaster.getNum2()));
        contentValues.put("data1", dcCardMaster.getData1());
        contentValues.put("data2", dcCardMaster.getData2());
        contentValues.put("data3", dcCardMaster.getData3());
        contentValues.put("blob1", dcCardMaster.getBlob1());
        contentValues.put("blob2", dcCardMaster.getBlob2());
        contentValues.put("blob3", dcCardMaster.getBlob3());
        return contentValues;
    }

    @Override
    protected void d(Cursor cursor) {
        com.samsung.android.spayfw.b.c.d("DCSDK_DcCardMasterDaoImpl", "Rec: " + cursor.getString(cursor.getColumnIndex("tokenId")) + ", " + cursor.getString(cursor.getColumnIndex("status")) + ", " + cursor.getString(cursor.getColumnIndex("dpanSuffix")) + ", " + cursor.getString(cursor.getColumnIndex("tokenProvisionStatus")));
    }

    protected DcCardMaster e(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        DcCardMaster dcCardMaster = new DcCardMaster();
        dcCardMaster.setTokenId(cursor.getString(cursor.getColumnIndex("tokenId")));
        dcCardMaster.setStatus(cursor.getString(cursor.getColumnIndex("status")));
        dcCardMaster.setDpanSuffix(cursor.getString(cursor.getColumnIndex("dpanSuffix")));
        dcCardMaster.setFpanSuffix(cursor.getString(cursor.getColumnIndex("fpanSuffix")));
        dcCardMaster.setIsProvisioned(cursor.getInt(cursor.getColumnIndex("tokenProvisionStatus")));
        dcCardMaster.setSessionKeys(cursor.getBlob(cursor.getColumnIndex("sessionKeys")));
        dcCardMaster.setPaginationTS(cursor.getString(cursor.getColumnIndex("paginationTS")));
        dcCardMaster.setRemainingOtpkCount(cursor.getInt(cursor.getColumnIndex("remainingOtpkCount")));
        dcCardMaster.setReplenishmentThreshold(cursor.getInt(cursor.getColumnIndex("replenishmentThreshold")));
        dcCardMaster.setNum1(cursor.getInt(cursor.getColumnIndex("number1")));
        dcCardMaster.setNum2(cursor.getInt(cursor.getColumnIndex("number2")));
        dcCardMaster.setData1(cursor.getString(cursor.getColumnIndex("data1")));
        dcCardMaster.setData2(cursor.getString(cursor.getColumnIndex("data2")));
        dcCardMaster.setData3(cursor.getString(cursor.getColumnIndex("data3")));
        dcCardMaster.setBlob1(cursor.getBlob(cursor.getColumnIndex("blob1")));
        dcCardMaster.setBlob2(cursor.getBlob(cursor.getColumnIndex("blob2")));
        dcCardMaster.setBlob3(cursor.getBlob(cursor.getColumnIndex("blob3")));
        return dcCardMaster;
    }

    @Override
    protected /* synthetic */ ContentValues getContentValues(Object object) {
        return this.a((DcCardMaster)object);
    }

    @Override
    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return this.e(cursor);
    }

    @Override
    protected String getTableName() {
        return "CardMaster";
    }
}

