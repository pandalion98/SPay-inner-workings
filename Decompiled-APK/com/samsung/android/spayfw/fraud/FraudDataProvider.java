package com.samsung.android.spayfw.fraud;

import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.fraud.p011a.FraudDbAdapter;
import com.samsung.android.spayfw.fraud.p011a.p012a.FraudEfsAdapter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import com.samsung.android.spayfw.utils.Utils;

/* renamed from: com.samsung.android.spayfw.fraud.b */
public class FraudDataProvider {
    private Context mContext;
    private FraudDbAdapter ne;
    private FraudEfsAdapter nf;

    public FraudDataProvider(Context context) {
        this.mContext = context;
        this.ne = FraudDbAdapter.m709z(this.mContext);
        this.nf = FraudEfsAdapter.m693A(this.mContext);
    }

    private void bu() {
        this.ne = FraudDbAdapter.m709z(this.mContext);
    }

    private void bv() {
        this.nf = FraudEfsAdapter.m693A(this.mContext);
    }

    private String m737w(int i) {
        if (i <= 0) {
            i = 0;
        }
        return Long.toString(Utils.am(this.mContext) - (((long) i) * 86400000));
    }

    public int m743x(int i) {
        int i2 = -1;
        if (this.ne == null) {
            bu();
            if (this.ne == null) {
                Log.m286e("FraudDataProvider", "getProvisioningAttemptCount: cannot get db adapter");
                return i2;
            }
        }
        Cursor rawQuery = this.ne.rawQuery("SELECT COUNT(*) FROM fcounter WHERE time >= ? ", new String[]{m737w(i)});
        if (rawQuery != null) {
            if (rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                i2 = rawQuery.getInt(0);
            }
            rawQuery.close();
        }
        Log.m285d("FraudDataProvider", "number of provisioning attempts in last " + i + " days is " + i2);
        return i2;
    }

    public int m744y(int i) {
        int i2 = -1;
        if (this.ne == null) {
            bu();
            if (this.ne == null) {
                Log.m286e("FraudDataProvider", "getProvisionedCardCount: cannot get db adapter");
                return i2;
            }
        }
        Cursor rawQuery = this.ne.rawQuery("SELECT COUNT(*) FROM fcard WHERE attempt_time >= ? AND " + "result" + " = ? ", new String[]{m737w(i), Long.toString(0)});
        if (rawQuery != null) {
            if (rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                i2 = rawQuery.getInt(0);
            }
            rawQuery.close();
        }
        Log.m285d("FraudDataProvider", "number of card provisioned in last " + i + " days is " + i2);
        return i2;
    }

    public int m745z(int i) {
        int i2 = -1;
        if (this.ne == null) {
            bu();
            if (this.ne == null) {
                Log.m286e("FraudDataProvider", "getProvisionedLastNameCount: cannot get db adapter");
                return i2;
            }
        }
        Cursor rawQuery = this.ne.rawQuery("SELECT COUNT(DISTINCT " + "last_name_hash" + ") FROM fcard WHERE " + "attempt_time" + " >= ?", new String[]{m737w(i)});
        if (rawQuery != null) {
            if (rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                i2 = rawQuery.getInt(0);
            }
            rawQuery.close();
        }
        Log.m285d("FraudDataProvider", "number of last names provisioned in last " + i + " days is " + i2);
        return i2;
    }

    public int m738A(int i) {
        Cursor rawQuery;
        int i2 = -1;
        if (this.ne == null) {
            bu();
            if (this.ne == null) {
                Log.m286e("FraudDataProvider", "getProvisionedNameCount: cannot get db adapter");
                return i2;
            }
        }
        String str = "first_name";
        String str2 = "last_name_hash";
        if (i >= 0) {
            rawQuery = this.ne.rawQuery("SELECT COUNT(*) FROM (SELECT DISTINCT " + str + ", " + str2 + " FROM fcard WHERE " + "attempt_time" + " >= ? )", new String[]{m737w(i)});
        } else {
            rawQuery = this.ne.rawQuery("SELECT COUNT(*) FROM (SELECT DISTINCT " + str + ", " + str2 + " FROM fcard)", null);
        }
        if (rawQuery != null) {
            if (rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                i2 = rawQuery.getInt(0);
            }
            rawQuery.close();
        }
        Log.m285d("FraudDataProvider", "number of names provisioned in last " + i + " days is " + i2);
        return i2;
    }

    public int m739B(int i) {
        int i2 = -1;
        if (this.ne == null) {
            bu();
            if (this.ne == null) {
                Log.m286e("FraudDataProvider", "getDistinctZipCodeCount: cannot get db adapter");
                return i2;
            }
        }
        Cursor rawQuery = this.ne.rawQuery("SELECT COUNT(DISTINCT " + "avszip" + ") FROM fcard WHERE " + "attempt_time" + " >= ?", new String[]{m737w(i)});
        if (rawQuery != null) {
            if (rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                i2 = rawQuery.getInt(0);
            }
            rawQuery.close();
        }
        Log.m285d("FraudDataProvider", "number of zipcodes used in last " + i + " days is " + i2);
        return i2;
    }

    public int m740C(int i) {
        int i2 = -1;
        String str = "fdevice_info";
        String str2 = "reason";
        if (this.nf == null) {
            bv();
            if (this.nf == null) {
                Log.m286e("FraudDataProvider", "getAllResetCount: adapter is null");
                return i2;
            }
        }
        Cursor rawQuery = this.nf.rawQuery("SELECT Count(*) FROM " + str + " WHERE " + str2 + " in " + "( ? , ?) AND " + "time" + " >= ?", new String[]{"app_reset", "factory_reset", m737w(i)});
        if (rawQuery != null) {
            if (rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                i2 = rawQuery.getInt(0);
            }
            rawQuery.close();
        }
        Log.m285d("FraudDataProvider", "number of app and factory reset in last " + i + " days is " + i2);
        return i2;
    }

    public double m741D(int i) {
        return 3.0d;
    }

    public FraudModelSnapshot m742E(int i) {
        long currentTimeMillis = System.currentTimeMillis();
        FraudModule.initialize(this.mContext);
        FraudModelSnapshot fraudModelSnapshot = (FraudModelSnapshot) FraudModule.m746Y(PushMessage.JSON_KEY_ENROLLMENT).bx().get(0);
        Log.m285d("FraudDataProvider", "the current device score is " + fraudModelSnapshot.nk + " the version is " + fraudModelSnapshot.nj + ". It takes " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        return fraudModelSnapshot;
    }

    public int bw() {
        Log.m285d("FraudDataProvider", "getSuspendedCardsCount: entering .. ");
        int i = -1;
        if (this.ne == null) {
            bu();
            if (this.ne == null) {
                Log.m286e("FraudDataProvider", "getSuspendedCardsCount: cannot get db adapter");
                return i;
            }
        }
        Cursor rawQuery = this.ne.rawQuery("SELECT COUNT(*) FROM " + "ftoken" + " WHERE " + CardMaster.COL_STATUS + " = " + 3, null);
        if (rawQuery != null) {
            if (rawQuery.getCount() > 0) {
                rawQuery.moveToFirst();
                i = rawQuery.getInt(0);
            }
            rawQuery.close();
        }
        Log.m285d("FraudDataProvider", "getSuspendedCardsCount: count = " + i);
        return i;
    }
}
