package com.samsung.android.spayfw.payprovider.discover.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcPaymentProfile;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.db.dao.e */
public class DcPaymentProfileDaoImpl extends DcAbstractDaoImpl<DcPaymentProfile> {
    protected /* synthetic */ ContentValues getContentValues(Object obj) {
        return m888a((DcPaymentProfile) obj);
    }

    public /* synthetic */ Object getData(long j) {
        return m892p(j);
    }

    protected /* synthetic */ Object getDataValues(Cursor cursor) {
        return m890g(cursor);
    }

    public DcPaymentProfileDaoImpl(Context context) {
        super(context);
    }

    public DcPaymentProfile m892p(long j) {
        throw new DcDbException("Method Not Supported", 3);
    }

    protected ContentValues m888a(DcPaymentProfile dcPaymentProfile) {
        if (dcPaymentProfile == null) {
            Log.m286e("DCSDK_DcPaymentProfileDaoImpl", "getContentValues: Data null");
            return null;
        } else if (dcPaymentProfile.getCardMasterId() == -1) {
            Log.m286e("DCSDK_DcPaymentProfileDaoImpl", "getContentValues: INVALID_ROW_ID");
            return null;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(McDbContract.CARD_MASTER_ID, Long.valueOf(dcPaymentProfile.getCardMasterId()));
            contentValues.put("payProfileId", Long.valueOf(dcPaymentProfile.getProfileId()));
            contentValues.put("ctq", dcPaymentProfile.getCtq());
            contentValues.put("auc", dcPaymentProfile.getAuc());
            contentValues.put("pru", dcPaymentProfile.getPru());
            contentValues.put("aip", dcPaymentProfile.getAip());
            contentValues.put("afl", dcPaymentProfile.getAfl());
            contentValues.put("cpr", dcPaymentProfile.getCpr());
            contentValues.put("arm", dcPaymentProfile.getCrm());
            contentValues.put("cvm", dcPaymentProfile.getCvm());
            contentValues.put("cl", dcPaymentProfile.getCl());
            return contentValues;
        }
    }

    protected DcPaymentProfile m890g(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        DcPaymentProfile dcPaymentProfile = new DcPaymentProfile();
        dcPaymentProfile.setRowId(cursor.getLong(cursor.getColumnIndex("_id")));
        dcPaymentProfile.setCardMasterId(cursor.getLong(cursor.getColumnIndex(McDbContract.CARD_MASTER_ID)));
        dcPaymentProfile.setProfileId(cursor.getLong(cursor.getColumnIndex("payProfileId")));
        dcPaymentProfile.setCtq(cursor.getBlob(cursor.getColumnIndex("ctq")));
        dcPaymentProfile.setAuc(cursor.getBlob(cursor.getColumnIndex("auc")));
        dcPaymentProfile.setPru(cursor.getBlob(cursor.getColumnIndex("pru")));
        dcPaymentProfile.setAip(cursor.getBlob(cursor.getColumnIndex("aip")));
        dcPaymentProfile.setAfl(cursor.getBlob(cursor.getColumnIndex("afl")));
        dcPaymentProfile.setCpr(cursor.getBlob(cursor.getColumnIndex("cpr")));
        dcPaymentProfile.setCrm(cursor.getBlob(cursor.getColumnIndex("arm")));
        dcPaymentProfile.setCvm(cursor.getBlob(cursor.getColumnIndex("cvm")));
        dcPaymentProfile.setCl(cursor.getBlob(cursor.getColumnIndex("cl")));
        return dcPaymentProfile;
    }

    protected String getTableName() {
        return "PaymentProfiles";
    }

    public List<DcPaymentProfile> m891o(long j) {
        Throwable th;
        Cursor cursor = null;
        Cursor query;
        try {
            query = this.db.query(getTableName(), null, "cardMasterId=" + j, null, null, null, "cardMasterId DESC");
            try {
                List<DcPaymentProfile> c = m875c(query);
                if (query != null) {
                    query.close();
                }
                return c;
            } catch (NullPointerException e) {
                try {
                    Log.m286e("DCSDK_DcPaymentProfileDaoImpl", "NPE occured during getData");
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
        } catch (NullPointerException e2) {
            query = null;
            Log.m286e("DCSDK_DcPaymentProfileDaoImpl", "NPE occured during getData");
            throw new DcDbException("NP Exception Occurred", 6);
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public DcPaymentProfile m889c(long j, long j2) {
        Throwable th;
        Cursor cursor = null;
        Cursor query;
        try {
            DcPaymentProfile g;
            query = this.db.query(getTableName(), null, "cardMasterId=" + j + " AND " + "payProfileId" + "=" + j2, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        g = m890g(query);
                    }
                } catch (NullPointerException e) {
                    try {
                        Log.m286e("DCSDK_DcPaymentProfileDaoImpl", "NPE occured during getData");
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
            return g;
        } catch (NullPointerException e2) {
            query = null;
            Log.m286e("DCSDK_DcPaymentProfileDaoImpl", "NPE occured during getData");
            throw new DcDbException("NP Exception Occurred", 6);
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }
}
