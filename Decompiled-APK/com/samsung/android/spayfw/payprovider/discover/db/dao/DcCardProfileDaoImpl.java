package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcCommonDao.DetailDataId;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardProvisionData;
import java.io.UnsupportedEncodingException;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.db.dao.d */
public class DcCardProfileDaoImpl extends DcAbstractDaoImpl<DcCardProfile> {
    private Gson mGson;

    protected /* synthetic */ ContentValues getContentValues(Object obj) {
        return m886a((DcCardProfile) obj);
    }

    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return m887f(cursor);
    }

    public DcCardProfileDaoImpl(Context context) {
        super(context);
        this.mGson = new GsonBuilder().create();
    }

    protected ContentValues m886a(DcCardProfile dcCardProfile) {
        ContentValues contentValues = null;
        if (dcCardProfile == null) {
            Log.m286e("DCSDK_DcCardProfileDaoImpl", "getContentValues: Data null");
            return contentValues;
        }
        if (dcCardProfile.getCardMasterId() == -1) {
            Log.m286e("DCSDK_DcCardProfileDaoImpl", "getContentValues: INVALID_ROW_ID");
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(McDbContract.CARD_MASTER_ID, Long.valueOf(dcCardProfile.getCardMasterId()));
        contentValues2.put(CardProvisionData.COL_DATA_ID, Integer.valueOf(DetailDataId.DC_CARD_PROFILE.cJ()));
        try {
            Object toJson = this.mGson.toJson((Object) dcCardProfile);
            if (TextUtils.isEmpty(toJson)) {
                Log.m286e("DCSDK_DcCardProfileDaoImpl", "getContentValues: jsonData failed");
                return contentValues;
            }
            contentValues2.put(CardProvisionData.COL_DATA, toJson.getBytes("UTF8"));
            return contentValues2;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return contentValues;
        } catch (Exception e2) {
            e2.printStackTrace();
            return contentValues;
        }
    }

    protected DcCardProfile m887f(Cursor cursor) {
        if (cursor == null) {
            Log.m286e("DCSDK_DcCardProfileDaoImpl", "getContentValues: cursor null");
            return null;
        }
        byte[] blob = cursor.getBlob(cursor.getColumnIndex(CardProvisionData.COL_DATA));
        if (blob == null) {
            Log.m286e("DCSDK_DcCardProfileDaoImpl", "getContentValues: byteData null");
            return null;
        }
        try {
            return (DcCardProfile) this.mGson.fromJson(new String(blob, "UTF8"), DcCardProfile.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String getTableName() {
        return "CardDetails";
    }
}
