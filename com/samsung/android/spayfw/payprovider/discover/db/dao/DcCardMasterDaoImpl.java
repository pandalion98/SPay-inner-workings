package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.db.dao.c */
public class DcCardMasterDaoImpl extends DcAbstractDaoImpl<DcCardMaster> {
    protected /* synthetic */ ContentValues getContentValues(Object obj) {
        return m883a((DcCardMaster) obj);
    }

    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return m885e(cursor);
    }

    public DcCardMasterDaoImpl(Context context) {
        super(context);
    }

    protected ContentValues m883a(DcCardMaster dcCardMaster) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PaymentFramework.EXTRA_TOKEN_ID, dcCardMaster.getTokenId());
        contentValues.put(CardMaster.COL_STATUS, dcCardMaster.getStatus());
        contentValues.put("dpanSuffix", dcCardMaster.getDpanSuffix());
        contentValues.put("fpanSuffix", dcCardMaster.getFpanSuffix());
        contentValues.put("tokenProvisionStatus", Integer.valueOf(dcCardMaster.getIsProvisioned()));
        contentValues.put("sessionKeys", dcCardMaster.getSessionKeys());
        contentValues.put("paginationTS", dcCardMaster.getPaginationTS());
        contentValues.put("remainingOtpkCount", Long.valueOf(dcCardMaster.getRemainingOtpkCount()));
        contentValues.put("replenishmentThreshold", Long.valueOf(dcCardMaster.getReplenishmentThreshold()));
        contentValues.put("number1", Long.valueOf(dcCardMaster.getNum1()));
        contentValues.put("number2", Long.valueOf(dcCardMaster.getNum2()));
        contentValues.put("data1", dcCardMaster.getData1());
        contentValues.put("data2", dcCardMaster.getData2());
        contentValues.put("data3", dcCardMaster.getData3());
        contentValues.put("blob1", dcCardMaster.getBlob1());
        contentValues.put("blob2", dcCardMaster.getBlob2());
        contentValues.put("blob3", dcCardMaster.getBlob3());
        return contentValues;
    }

    protected DcCardMaster m885e(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        DcCardMaster dcCardMaster = new DcCardMaster();
        dcCardMaster.setTokenId(cursor.getString(cursor.getColumnIndex(PaymentFramework.EXTRA_TOKEN_ID)));
        dcCardMaster.setStatus(cursor.getString(cursor.getColumnIndex(CardMaster.COL_STATUS)));
        dcCardMaster.setDpanSuffix(cursor.getString(cursor.getColumnIndex("dpanSuffix")));
        dcCardMaster.setFpanSuffix(cursor.getString(cursor.getColumnIndex("fpanSuffix")));
        dcCardMaster.setIsProvisioned(cursor.getInt(cursor.getColumnIndex("tokenProvisionStatus")));
        dcCardMaster.setSessionKeys(cursor.getBlob(cursor.getColumnIndex("sessionKeys")));
        dcCardMaster.setPaginationTS(cursor.getString(cursor.getColumnIndex("paginationTS")));
        dcCardMaster.setRemainingOtpkCount((long) cursor.getInt(cursor.getColumnIndex("remainingOtpkCount")));
        dcCardMaster.setReplenishmentThreshold((long) cursor.getInt(cursor.getColumnIndex("replenishmentThreshold")));
        dcCardMaster.setNum1((long) cursor.getInt(cursor.getColumnIndex("number1")));
        dcCardMaster.setNum2((long) cursor.getInt(cursor.getColumnIndex("number2")));
        dcCardMaster.setData1(cursor.getString(cursor.getColumnIndex("data1")));
        dcCardMaster.setData2(cursor.getString(cursor.getColumnIndex("data2")));
        dcCardMaster.setData3(cursor.getString(cursor.getColumnIndex("data3")));
        dcCardMaster.setBlob1(cursor.getBlob(cursor.getColumnIndex("blob1")));
        dcCardMaster.setBlob2(cursor.getBlob(cursor.getColumnIndex("blob2")));
        dcCardMaster.setBlob3(cursor.getBlob(cursor.getColumnIndex("blob3")));
        return dcCardMaster;
    }

    protected void m884d(Cursor cursor) {
        Log.m285d("DCSDK_DcCardMasterDaoImpl", "Rec: " + cursor.getString(cursor.getColumnIndex(PaymentFramework.EXTRA_TOKEN_ID)) + ", " + cursor.getString(cursor.getColumnIndex(CardMaster.COL_STATUS)) + ", " + cursor.getString(cursor.getColumnIndex("dpanSuffix")) + ", " + cursor.getString(cursor.getColumnIndex("tokenProvisionStatus")));
    }

    protected String getTableName() {
        return "CardMaster";
    }
}
