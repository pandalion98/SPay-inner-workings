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
 *  java.lang.Throwable
 *  java.util.ArrayList
 */
package com.samsung.android.spayfw.fraud.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.fraud.a.a.a;
import com.samsung.android.spayfw.fraud.a.a.b;
import com.samsung.android.spayfw.utils.h;
import java.util.ArrayList;

public class f {
    private static f nZ;
    private static g ne;
    private static b nf;
    private Context mContext;

    private f(Context context) {
        this.mContext = context;
        nf = b.A(context);
        ne = g.z(context);
    }

    private long a(d.a a2) {
        if (a2 == null) {
            return -1L;
        }
        if (this.bH() == null) {
            Log.e("FraudDao", "addTokenStatusHistory: cannot get db adapter");
            return -1L;
        }
        return ne.a(a2);
    }

    public static final void a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    private g bH() {
        if (ne == null) {
            ne = g.z(this.mContext);
        }
        return ne;
    }

    private b bI() {
        if (nf == null) {
            nf = b.A(this.mContext);
        }
        return nf;
    }

    private String w(int n2) {
        if (n2 <= 0) {
            n2 = 0;
        }
        return Long.toString((long)(h.am(this.mContext) - 86400000L * (long)n2));
    }

    public static f y(Context context) {
        if (nZ == null) {
            nZ = new f(context);
        }
        return nZ;
    }

    public int a(long l2, ContentValues contentValues) {
        if (l2 < 0L) {
            return -1;
        }
        if (this.bH() == null) {
            Log.e("FraudDao", "updateCardTokenId: cannot get db adapter");
            return -1;
        }
        String[] arrstring = new String[]{Long.toString((long)l2)};
        return ne.a("fcard", contentValues, "id = ?", arrstring);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int a(String string, int n2) {
        if (string == null) {
            Log.e("FraudDao", "updateTokenRecord: tokenRefKey is null");
            return -1;
        }
        if (this.bH() == null) {
            Log.e("FraudDao", "updateTokenRecord: cannot get db adapter");
            return -1;
        }
        String[] arrstring = new String[]{e.b(string, e.salt)};
        Cursor cursor = ne.a("ftoken", "token_ref_key = ?", arrstring);
        if (cursor == null) return -1;
        try {
            if (cursor.getCount() == 1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("status", Integer.valueOf((int)n2));
                cursor.moveToNext();
                long l2 = cursor.getLong(cursor.getColumnIndex("id"));
                ne.a("ftoken", contentValues, "token_ref_key = ?", arrstring);
                this.a(new d.a(0L, l2, n2, h.am(this.mContext)));
                Log.d("FraudDao", "update token status history for token index " + l2);
            } else {
                Log.e("FraudDao", "the number of tokens is wrong:" + cursor.getCount());
            }
            int n3 = cursor.getCount();
            return n3;
        }
        finally {
            f.a(cursor);
        }
    }

    public long a(a a2) {
        if (this.bI() == null) {
            Log.e("FraudDao", "addDeviceRecord: cannot get db adapter");
            return -1L;
        }
        String string = "time < ? OR id not in (select id from " + a2.bB() + " order by " + "time" + " desc limit ? )";
        String[] arrstring = new String[]{this.w(90), "1000"};
        int n2 = nf.b(a2.bB(), string, arrstring);
        Log.d("FraudDao", "addDeviceRecord: delCount = " + n2);
        return nf.a(a2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long a(com.samsung.android.spayfw.fraud.a.b b2) {
        if (b2 == null) {
            return -1L;
        }
        if (this.bH() == null) {
            Log.e("FraudDao", "addCardRecord: cannot get db adapter");
            return -1L;
        }
        String[] arrstring = new String[]{b2.getAccountId(), b2.bD()};
        Cursor cursor = ne.a("fcard", "account_id = ? AND four_digits = ?", arrstring);
        if (cursor == null) return -1L;
        try {
            if (cursor.getCount() == 0) {
                long l2 = ne.a(b2);
                return l2;
            }
            if (cursor.getCount() == 1) {
                cursor.moveToNext();
                long l3 = cursor.getLong(cursor.getColumnIndex("id"));
                ContentValues contentValues = b2.bC();
                g g2 = ne;
                String[] arrstring2 = new String[]{Long.toString((long)l3)};
                g2.a("fcard", contentValues, "id = ? ", arrstring2);
                return l3;
            }
            Log.e("FraudDao", "more than one card is found");
            return -1L;
        }
        finally {
            f.a(cursor);
        }
    }

    public long a(c c2) {
        if (c2 == null) {
            return -1L;
        }
        if (this.bH() == null) {
            Log.e("FraudDao", "addProvisionCounter: cannot get db adapter");
            return -1L;
        }
        return ne.a(c2);
    }

    public long a(d.b b2) {
        if (b2 == null) {
            Log.e("FraudDao", "addTransactionDetail: td is null");
            return -1L;
        }
        if (this.bH() == null) {
            Log.e("FraudDao", "addTransactionDetail: cannot get db adapter");
            return -1L;
        }
        return ne.a(b2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long a(d d2) {
        block10 : {
            long l2;
            block11 : {
                if (d2 == null) {
                    return -1L;
                }
                if (this.bH() == null) {
                    Log.e("FraudDao", "addTokenRecord: cannot get db adapter");
                    return -1L;
                }
                String[] arrstring = new String[]{d2.bF(), Long.toString((long)d2.bG())};
                Cursor cursor = ne.a("ftoken", "token_ref_key = ? AND card_id = ?", arrstring);
                if (cursor == null) {
                    Log.e("FraudDao", "addTokenRecord: cursor is null");
                    return -1L;
                }
                if (cursor.getCount() == 0) {
                    long l3;
                    Log.d("FraudDao", "adding a new token record");
                    l2 = l3 = ne.a(d2);
                } else {
                    if (cursor.getCount() != 1) break block10;
                    cursor.moveToNext();
                    l2 = cursor.getLong(cursor.getColumnIndex("id"));
                    Log.d("FraudDao", "get an existing token record");
                }
                if (l2 <= 0L) break block11;
                this.a(new d.a(0L, l2, d2.getStatus(), h.am(this.mContext)));
            }
            Log.d("FraudDao", "a new ftoken is added");
            return l2;
        }
        Log.e("FraudDao", "more than one token is found");
        return -1L;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public d ae(String var1_1) {
        block9 : {
            block8 : {
                if (this.bH() == null) {
                    Log.e("FraudDao", "getTokenRecord: cannot get db adapter");
                    return null;
                }
                var2_2 = new String[]{e.b(var1_1, e.salt)};
                var5_3 = f.ne.a("ftoken", "token_ref_key = ?", var2_2);
                if (var5_3 == null) break block8;
                try {
                    if (var5_3.getCount() <= 0) break block8;
                    var7_4 = new ArrayList(var5_3.getCount());
                    while (var5_3.moveToNext()) {
                        var7_4.add((Object)new d(false, var5_3.getLong(0), var5_3.getString(1), var5_3.getInt(2), var5_3.getLong(3)));
                    }
                    ** GOTO lbl18
                }
                catch (Throwable var3_5) {
                    block10 : {
                        var4_8 = var5_3;
                        break block10;
lbl18: // 1 sources:
                        var6_9 = var7_4;
                        break block9;
                        catch (Throwable var3_7) {
                            var4_8 = null;
                        }
                    }
                    f.a(var4_8);
                    throw var3_6;
                }
            }
            var6_9 = null;
        }
        f.a(var5_3);
        if (var6_9 == null) {
            Log.e("FraudDao", "cannot create token record list");
            return null;
        }
        if (var6_9.size() < 1) {
            Log.e("FraudDao", "cannot find token record");
            return null;
        }
        if (var6_9.size() <= 1) return (d)var6_9.get(0);
        Log.e("FraudDao", "dupicated token record " + var6_9.size());
        return (d)var6_9.get(0);
    }
}

