/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.List
 */
package com.samsung.android.spayfw.fraud;

import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.fraud.a.g;
import com.samsung.android.spayfw.fraud.d;
import com.samsung.android.spayfw.fraud.e;
import com.samsung.android.spayfw.utils.h;
import java.util.List;

public class b {
    private Context mContext;
    private g ne;
    private com.samsung.android.spayfw.fraud.a.a.b nf;

    public b(Context context) {
        this.mContext = context;
        this.ne = g.z(this.mContext);
        this.nf = com.samsung.android.spayfw.fraud.a.a.b.A(this.mContext);
    }

    private void bu() {
        this.ne = g.z(this.mContext);
    }

    private void bv() {
        this.nf = com.samsung.android.spayfw.fraud.a.a.b.A(this.mContext);
    }

    private String w(int n2) {
        if (n2 <= 0) {
            n2 = 0;
        }
        return Long.toString((long)(h.am(this.mContext) - 86400000L * (long)n2));
    }

    /*
     * Enabled aggressive block sorting
     */
    public int A(int n2) {
        Cursor cursor;
        int n3 = -1;
        if (this.ne == null) {
            this.bu();
            if (this.ne == null) {
                c.e("FraudDataProvider", "getProvisionedNameCount: cannot get db adapter");
                return n3;
            }
        }
        if (n2 >= 0) {
            g g2 = this.ne;
            String string = "SELECT COUNT(*) FROM (SELECT DISTINCT " + "first_name" + ", " + "last_name_hash" + " FROM fcard WHERE " + "attempt_time" + " >= ? )";
            String[] arrstring = new String[]{this.w(n2)};
            cursor = g2.rawQuery(string, arrstring);
        } else {
            cursor = this.ne.rawQuery("SELECT COUNT(*) FROM (SELECT DISTINCT " + "first_name" + ", " + "last_name_hash" + " FROM fcard)", null);
        }
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                n3 = cursor.getInt(0);
            }
            cursor.close();
        }
        c.d("FraudDataProvider", "number of names provisioned in last " + n2 + " days is " + n3);
        return n3;
    }

    public int B(int n2) {
        int n3 = -1;
        if (this.ne == null) {
            this.bu();
            if (this.ne == null) {
                c.e("FraudDataProvider", "getDistinctZipCodeCount: cannot get db adapter");
                return n3;
            }
        }
        g g2 = this.ne;
        String string = "SELECT COUNT(DISTINCT " + "avszip" + ") FROM fcard WHERE " + "attempt_time" + " >= ?";
        String[] arrstring = new String[]{this.w(n2)};
        Cursor cursor = g2.rawQuery(string, arrstring);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                n3 = cursor.getInt(0);
            }
            cursor.close();
        }
        c.d("FraudDataProvider", "number of zipcodes used in last " + n2 + " days is " + n3);
        return n3;
    }

    public int C(int n2) {
        int n3 = -1;
        if (this.nf == null) {
            this.bv();
            if (this.nf == null) {
                c.e("FraudDataProvider", "getAllResetCount: adapter is null");
                return n3;
            }
        }
        com.samsung.android.spayfw.fraud.a.a.b b2 = this.nf;
        String string = "SELECT Count(*) FROM " + "fdevice_info" + " WHERE " + "reason" + " in " + "( ? , ?) AND " + "time" + " >= ?";
        String[] arrstring = new String[]{"app_reset", "factory_reset", this.w(n2)};
        Cursor cursor = b2.rawQuery(string, arrstring);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                n3 = cursor.getInt(0);
            }
            cursor.close();
        }
        c.d("FraudDataProvider", "number of app and factory reset in last " + n2 + " days is " + n3);
        return n3;
    }

    public double D(int n2) {
        return 3.0;
    }

    public d E(int n2) {
        long l2 = System.currentTimeMillis();
        e.initialize(this.mContext);
        d d2 = (d)e.Y("enrollment").bx().get(0);
        long l3 = System.currentTimeMillis();
        c.d("FraudDataProvider", "the current device score is " + d2.nk + " the version is " + d2.nj + ". It takes " + (l3 - l2) + " ms");
        return d2;
    }

    public int bw() {
        Cursor cursor;
        c.d("FraudDataProvider", "getSuspendedCardsCount: entering .. ");
        int n2 = -1;
        if (this.ne == null) {
            this.bu();
            if (this.ne == null) {
                c.e("FraudDataProvider", "getSuspendedCardsCount: cannot get db adapter");
                return n2;
            }
        }
        if ((cursor = this.ne.rawQuery("SELECT COUNT(*) FROM " + "ftoken" + " WHERE " + "status" + " = " + 3, null)) != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                n2 = cursor.getInt(0);
            }
            cursor.close();
        }
        c.d("FraudDataProvider", "getSuspendedCardsCount: count = " + n2);
        return n2;
    }

    public int x(int n2) {
        int n3 = -1;
        if (this.ne == null) {
            this.bu();
            if (this.ne == null) {
                c.e("FraudDataProvider", "getProvisioningAttemptCount: cannot get db adapter");
                return n3;
            }
        }
        g g2 = this.ne;
        String[] arrstring = new String[]{this.w(n2)};
        Cursor cursor = g2.rawQuery("SELECT COUNT(*) FROM fcounter WHERE time >= ? ", arrstring);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                n3 = cursor.getInt(0);
            }
            cursor.close();
        }
        c.d("FraudDataProvider", "number of provisioning attempts in last " + n2 + " days is " + n3);
        return n3;
    }

    public int y(int n2) {
        int n3 = -1;
        if (this.ne == null) {
            this.bu();
            if (this.ne == null) {
                c.e("FraudDataProvider", "getProvisionedCardCount: cannot get db adapter");
                return n3;
            }
        }
        g g2 = this.ne;
        String string = "SELECT COUNT(*) FROM fcard WHERE attempt_time >= ? AND " + "result" + " = ? ";
        String[] arrstring = new String[]{this.w(n2), Long.toString((long)0L)};
        Cursor cursor = g2.rawQuery(string, arrstring);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                n3 = cursor.getInt(0);
            }
            cursor.close();
        }
        c.d("FraudDataProvider", "number of card provisioned in last " + n2 + " days is " + n3);
        return n3;
    }

    public int z(int n2) {
        int n3 = -1;
        if (this.ne == null) {
            this.bu();
            if (this.ne == null) {
                c.e("FraudDataProvider", "getProvisionedLastNameCount: cannot get db adapter");
                return n3;
            }
        }
        g g2 = this.ne;
        String string = "SELECT COUNT(DISTINCT " + "last_name_hash" + ") FROM fcard WHERE " + "attempt_time" + " >= ?";
        String[] arrstring = new String[]{this.w(n2)};
        Cursor cursor = g2.rawQuery(string, arrstring);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                n3 = cursor.getInt(0);
            }
            cursor.close();
        }
        c.d("FraudDataProvider", "number of last names provisioned in last " + n2 + " days is " + n3);
        return n3;
    }
}

