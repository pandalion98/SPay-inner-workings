package com.samsung.android.spayfw.fraud.p011a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.fraud.p011a.FTokenRecord.FTokenRecord;
import com.samsung.android.spayfw.fraud.p011a.p012a.FDeviceRecord;
import com.samsung.android.spayfw.fraud.p011a.p012a.FraudEfsAdapter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import com.samsung.android.spayfw.utils.Utils;
import java.util.ArrayList;

/* renamed from: com.samsung.android.spayfw.fraud.a.f */
public class FraudDao {
    private static FraudDao nZ;
    private static FraudDbAdapter ne;
    private static FraudEfsAdapter nf;
    private Context mContext;

    private FraudDao(Context context) {
        this.mContext = context;
        nf = FraudEfsAdapter.m693A(context);
        ne = FraudDbAdapter.m709z(context);
    }

    private FraudDbAdapter bH() {
        if (ne == null) {
            ne = FraudDbAdapter.m709z(this.mContext);
        }
        return ne;
    }

    private FraudEfsAdapter bI() {
        if (nf == null) {
            nf = FraudEfsAdapter.m693A(this.mContext);
        }
        return nf;
    }

    public static FraudDao m701y(Context context) {
        if (nZ == null) {
            nZ = new FraudDao(context);
        }
        return nZ;
    }

    public static final void m699a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private String m700w(int i) {
        if (i <= 0) {
            i = 0;
        }
        return Long.toString(Utils.am(this.mContext) - (((long) i) * 86400000));
    }

    public long m705a(FCardRecord fCardRecord) {
        long j = -1;
        if (fCardRecord != null) {
            if (bH() == null) {
                Log.m286e("FraudDao", "addCardRecord: cannot get db adapter");
            } else {
                String[] strArr = new String[]{fCardRecord.getAccountId(), fCardRecord.bD()};
                Cursor a = ne.m713a("fcard", "account_id = ? AND four_digits = ?", strArr);
                if (a != null) {
                    try {
                        if (a.getCount() == 0) {
                            j = ne.m711a((FBaseRecord) fCardRecord);
                        } else if (a.getCount() == 1) {
                            a.moveToNext();
                            j = a.getLong(a.getColumnIndex(PushMessage.JSON_KEY_ID));
                            ContentValues bC = fCardRecord.bC();
                            ne.m710a("fcard", bC, "id = ? ", new String[]{Long.toString(j)});
                            FraudDao.m699a(a);
                        } else {
                            Log.m286e("FraudDao", "more than one card is found");
                            FraudDao.m699a(a);
                        }
                    } finally {
                        FraudDao.m699a(a);
                    }
                }
            }
        }
        return j;
    }

    public long m708a(FTokenRecord fTokenRecord) {
        long j = -1;
        if (fTokenRecord != null) {
            if (bH() == null) {
                Log.m286e("FraudDao", "addTokenRecord: cannot get db adapter");
            } else {
                String[] strArr = new String[]{fTokenRecord.bF(), Long.toString(fTokenRecord.bG())};
                Cursor a = ne.m713a("ftoken", "token_ref_key = ? AND card_id = ?", strArr);
                if (a == null) {
                    Log.m286e("FraudDao", "addTokenRecord: cursor is null");
                } else {
                    try {
                        if (a.getCount() == 0) {
                            Log.m285d("FraudDao", "adding a new token record");
                            j = ne.m711a((FBaseRecord) fTokenRecord);
                        } else if (a.getCount() == 1) {
                            a.moveToNext();
                            j = a.getLong(a.getColumnIndex(PushMessage.JSON_KEY_ID));
                            Log.m285d("FraudDao", "get an existing token record");
                        } else {
                            Log.m286e("FraudDao", "more than one token is found");
                            FraudDao.m699a(a);
                        }
                        FraudDao.m699a(a);
                        if (j > 0) {
                            m698a(new FTokenRecord(0, j, fTokenRecord.getStatus(), Utils.am(this.mContext)));
                        }
                        Log.m285d("FraudDao", "a new ftoken is added");
                    } catch (Throwable th) {
                        FraudDao.m699a(a);
                    }
                }
            }
        }
        return j;
    }

    private long m698a(FTokenRecord fTokenRecord) {
        if (fTokenRecord == null) {
            return -1;
        }
        if (bH() != null) {
            return ne.m711a((FBaseRecord) fTokenRecord);
        }
        Log.m286e("FraudDao", "addTokenStatusHistory: cannot get db adapter");
        return -1;
    }

    public long m707a(FTokenRecord fTokenRecord) {
        if (fTokenRecord == null) {
            Log.m286e("FraudDao", "addTransactionDetail: td is null");
            return -1;
        } else if (bH() != null) {
            return ne.m712a(fTokenRecord);
        } else {
            Log.m286e("FraudDao", "addTransactionDetail: cannot get db adapter");
            return -1;
        }
    }

    public int m703a(String str, int i) {
        int i2 = -1;
        if (str == null) {
            Log.m286e("FraudDao", "updateTokenRecord: tokenRefKey is null");
        } else if (bH() == null) {
            Log.m286e("FraudDao", "updateTokenRecord: cannot get db adapter");
        } else {
            String str2 = "token_ref_key = ?";
            String[] strArr = new String[]{FraudConstant.m697b(str, FraudConstant.salt)};
            Cursor a = ne.m713a("ftoken", str2, strArr);
            if (a != null) {
                try {
                    if (a.getCount() == 1) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(CardMaster.COL_STATUS, Integer.valueOf(i));
                        a.moveToNext();
                        long j = a.getLong(a.getColumnIndex(PushMessage.JSON_KEY_ID));
                        ne.m710a("ftoken", contentValues, str2, strArr);
                        m698a(new FTokenRecord(0, j, i, Utils.am(this.mContext)));
                        Log.m285d("FraudDao", "update token status history for token index " + j);
                    } else {
                        Log.m286e("FraudDao", "the number of tokens is wrong:" + a.getCount());
                    }
                    i2 = a.getCount();
                } finally {
                    FraudDao.m699a(a);
                }
            }
        }
        return i2;
    }

    public int m702a(long j, ContentValues contentValues) {
        if (j < 0) {
            return -1;
        }
        if (bH() == null) {
            Log.m286e("FraudDao", "updateCardTokenId: cannot get db adapter");
            return -1;
        }
        String[] strArr = new String[]{Long.toString(j)};
        return ne.m710a("fcard", contentValues, "id = ?", strArr);
    }

    public FTokenRecord ae(String str) {
        Throwable th;
        Cursor cursor = null;
        if (bH() == null) {
            Log.m286e("FraudDao", "getTokenRecord: cannot get db adapter");
            return null;
        }
        try {
            ArrayList arrayList;
            String[] strArr = new String[]{FraudConstant.m697b(str, FraudConstant.salt)};
            Cursor a = ne.m713a("ftoken", "token_ref_key = ?", strArr);
            if (a != null) {
                try {
                    if (a.getCount() > 0) {
                        ArrayList arrayList2 = new ArrayList(a.getCount());
                        while (a.moveToNext()) {
                            arrayList2.add(new FTokenRecord(false, a.getLong(0), a.getString(1), a.getInt(2), a.getLong(3)));
                        }
                        arrayList = arrayList2;
                        FraudDao.m699a(a);
                        if (arrayList == null) {
                            Log.m286e("FraudDao", "cannot create token record list");
                            return null;
                        } else if (arrayList.size() >= 1) {
                            Log.m286e("FraudDao", "cannot find token record");
                            return null;
                        } else {
                            if (arrayList.size() > 1) {
                                Log.m286e("FraudDao", "dupicated token record " + arrayList.size());
                            }
                            return (FTokenRecord) arrayList.get(0);
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = a;
                    FraudDao.m699a(cursor);
                    throw th;
                }
            }
            arrayList = null;
            FraudDao.m699a(a);
            if (arrayList == null) {
                Log.m286e("FraudDao", "cannot create token record list");
                return null;
            } else if (arrayList.size() >= 1) {
                if (arrayList.size() > 1) {
                    Log.m286e("FraudDao", "dupicated token record " + arrayList.size());
                }
                return (FTokenRecord) arrayList.get(0);
            } else {
                Log.m286e("FraudDao", "cannot find token record");
                return null;
            }
        } catch (Throwable th3) {
            th = th3;
            FraudDao.m699a(cursor);
            throw th;
        }
    }

    public long m704a(FDeviceRecord fDeviceRecord) {
        if (bI() == null) {
            Log.m286e("FraudDao", "addDeviceRecord: cannot get db adapter");
            return -1;
        }
        Log.m285d("FraudDao", "addDeviceRecord: delCount = " + nf.m695b(fDeviceRecord.bB(), "time < ? OR id not in (select id from " + fDeviceRecord.bB() + " order by " + "time" + " desc limit ? )", new String[]{m700w(90), "1000"}));
        return nf.m694a(fDeviceRecord);
    }

    public long m706a(FCounterRecord fCounterRecord) {
        if (fCounterRecord == null) {
            return -1;
        }
        if (bH() != null) {
            return ne.m711a((FBaseRecord) fCounterRecord);
        }
        Log.m286e("FraudDao", "addProvisionCounter: cannot get db adapter");
        return -1;
    }
}
