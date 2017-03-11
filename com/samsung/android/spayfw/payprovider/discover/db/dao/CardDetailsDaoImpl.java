package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.models.CardDetails;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardProvisionData;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.db.dao.a */
public class CardDetailsDaoImpl extends DcAbstractDaoImpl<CardDetails> {
    protected /* synthetic */ ContentValues getContentValues(Object obj) {
        return m878a((CardDetails) obj);
    }

    public /* synthetic */ Object getData(long j) {
        return m882n(j);
    }

    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return m881b(cursor);
    }

    public /* synthetic */ boolean updateData(Object obj, long j) {
        return m879a((CardDetails) obj, j);
    }

    public CardDetailsDaoImpl(Context context) {
        super(context);
    }

    protected ContentValues m878a(CardDetails cardDetails) {
        if (cardDetails == null) {
            Log.m286e("DCSDK_CardDetailsDaoImpl", "getContentValues: Data null");
            return null;
        } else if (cardDetails.getCardMasterId() == -1) {
            Log.m286e("DCSDK_CardDetailsDaoImpl", "getContentValues: INVALID_ROW_ID");
            return null;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(cardDetails.getCardMasterId()));
            contentValues.put(CardProvisionData.COL_DATA_ID, Long.valueOf(cardDetails.getDataId()));
            contentValues.put(CardProvisionData.COL_DATA, cardDetails.getData());
            return contentValues;
        }
    }

    protected CardDetails m881b(Cursor cursor) {
        if (cursor != null) {
            return new CardDetails(cursor.getLong(cursor.getColumnIndex(McDbContract.CARD_MASTER_ID)), cursor.getLong(cursor.getColumnIndex(CardProvisionData.COL_DATA_ID)), cursor.getBlob(cursor.getColumnIndex(CardProvisionData.COL_DATA)));
        }
        Log.m286e("DCSDK_CardDetailsDaoImpl", "getDataValues: cursor null");
        return null;
    }

    protected String getTableName() {
        return "CardDetails";
    }

    public CardDetails m882n(long j) {
        throw new DcDbException("Method Not Supported", 3);
    }

    public CardDetails m880b(long j, long j2) {
        Throwable th;
        Cursor cursor = null;
        Cursor query;
        try {
            CardDetails b;
            query = this.db.query(getTableName(), null, "cardMasterId=" + j + " and " + CardProvisionData.COL_DATA_ID + "=" + j2, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        b = m881b(query);
                    }
                } catch (NullPointerException e) {
                    try {
                        Log.m286e("DCSDK_CardDetailsDaoImpl", "NPE occured during getData");
                        throw new DcDbException("NP Exception Occurred", 6);
                    } catch (Throwable th2) {
                        cursor = query;
                        th = th2;
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
            return b;
        } catch (NullPointerException e2) {
            query = null;
            Log.m286e("DCSDK_CardDetailsDaoImpl", "NPE occured during getData");
            throw new DcDbException("NP Exception Occurred", 6);
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public boolean m879a(CardDetails cardDetails, long j) {
        if (cardDetails == null) {
            throw new DcDbException("Invalid Input", 3);
        } else if (this.db == null) {
            throw new DcDbException("DB Not Initialized", 1);
        } else {
            if (this.db.update(getTableName(), m878a(cardDetails), "cardMasterId = " + j + " and " + CardProvisionData.COL_DATA_ID + "=" + cardDetails.getDataId(), null) == 1) {
                return true;
            }
            throw new DcDbException("Failed to update CardDetails record", 7);
        }
    }
}
