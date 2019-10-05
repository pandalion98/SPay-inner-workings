/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.ArrayList
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.d.a;

import android.content.Context;
import android.net.Uri;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.c.a.h;
import com.samsung.sensorframework.sda.d.c;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class a
extends b {
    protected static final Object lock = new Object();
    protected ArrayList<HashMap<String, String>> If;

    public a(Context context) {
        super(context);
    }

    static /* synthetic */ String a(a a2) {
        return a2.he();
    }

    static /* synthetic */ Context b(a a2) {
        return a2.HR;
    }

    static /* synthetic */ String c(a a2) {
        return a2.he();
    }

    @Override
    protected boolean hc() {
        new Thread(){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            public void run() {
                a.this.If = new ArrayList();
                try {
                    arrstring = a.this.hj();
                    n2 = arrstring.length;
                    i2 = 0;
                    block7 : do {
                        block13 : {
                            block12 : {
                                if (i2 >= n2) break block12;
                                uri = Uri.parse((String)arrstring[i2]);
                                arrstring2 = a.this.hk();
                                string = a.a(a.this);
                                arrobject = new Object[]{c.ap(a.this.getSensorType())};
                                Log.d(string, String.format((String)"Sensing content reader: %s", (Object[])arrobject));
                                cursor = a.b(a.this).getContentResolver().query(uri, arrstring2, null, null, null);
                                if (cursor == null) ** GOTO lbl28
                                break block13;
                            }
                            a.this.ho();
                            return;
                        }
                        cursor.moveToFirst();
                        Log.d(a.c(a.this), "Total entries in the cursor: " + cursor.getCount());
                        do {
                            if (!cursor.isAfterLast()) {
                                hashMap = new HashMap();
                                n3 = arrstring2.length;
                            } else {
                                cursor.close();
lbl28: // 2 sources:
                                ++i2;
                                continue block7;
                            }
                            for (i3 = 0; i3 < n3; ++i3) {
                                string2 = arrstring2[i3];
                                hashMap.put((Object)string2, (Object)cursor.getString(cursor.getColumnIndex(string2)));
                            }
                            a.this.If.add((Object)hashMap);
                            cursor.moveToNext();
                            continue;
                            break;
                        } while (true);
                        break;
                    } while (true);
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return;
                    }
                }
                finally {
                    a.this.ho();
                }
            }
        }.start();
        return true;
    }

    @Override
    protected void hd() {
    }

    protected abstract String[] hj();

    protected abstract String[] hk();

    @Override
    protected com.samsung.sensorframework.sda.b.a hl() {
        return ((h)super.hi()).a(this.Jn, this.getSensorType(), this.If, this.Id);
    }

    @Override
    protected void hm() {
    }

}

