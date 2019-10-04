/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.ref.SoftReference
 *  java.util.HashMap
 *  java.util.LinkedList
 *  java.util.List
 */
package com.samsung.android.spayfw.fraud;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.fraud.c;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class g {
    static a nq = null;
    static HashMap<String, SoftReference<c>> nr = new HashMap();

    static c ab(String string) {
        c c2;
        c c3;
        if (nr.containsKey((Object)string) && (c3 = (c)((SoftReference)nr.get((Object)string)).get()) != null) {
            return c3;
        }
        try {
            nq.beginTransaction();
            c2 = nq.ab(string);
            nq.setTransactionSuccessful();
            if (c2 == null) {
                return null;
            }
        }
        finally {
            nq.endTransaction();
        }
        nr.put((Object)string, (Object)new SoftReference((Object)c2));
        return c2;
    }

    static List<String> ac(String string) {
        try {
            nq.beginTransaction();
            List<String> list = nq.ac(string);
            nq.setTransactionSuccessful();
            return list;
        }
        finally {
            nq.endTransaction();
        }
    }

    static void initialize(Context context) {
        nq = new a(context);
    }

    static class a {
        private com.samsung.android.spayfw.e.a.a ns;
        private SQLiteDatabase nt;
        private SQLiteDatabase nu;

        public a(Context context) {
            this.ns = com.samsung.android.spayfw.e.a.a.b(context, null, 0, DBName.ot);
            this.nt = this.ns.getReadableDatabase(com.samsung.android.spayfw.utils.c.getDbPassword());
            this.nu = this.ns.getWritableDatabase(com.samsung.android.spayfw.utils.c.getDbPassword());
        }

        private void ad(String string) {
            this.nu.execSQL("update modelinfo set lastaccesstime = current_timestamp where modelid=?", (Object[])new String[]{string});
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public c ab(String string) {
            Cursor cursor;
            c c2;
            block6 : {
                block5 : {
                    Cursor cursor2;
                    cursor = cursor2 = this.nt.rawQuery("select modelbase, modelparams from modelinfo where modelid=?", new String[]{string});
                    if (cursor == null) break block5;
                    boolean bl = cursor.moveToFirst();
                    if (bl) break block6;
                    {
                        catch (Throwable throwable) {}
                    }
                }
                c2 = null;
                if (cursor == null) return c2;
                cursor.close();
                return c2;
            }
            c2 = new c();
            c2.nh = string;
            c2.ng = cursor.getString(0);
            c2.ni = cursor.getBlob(1);
            this.ad(string);
            cursor.close();
            if (cursor == null) return c2;
            cursor.close();
            return c2;
            catch (Throwable throwable) {
                cursor = null;
                Throwable throwable2 = throwable;
                if (cursor == null) throw throwable2;
                cursor.close();
                throw throwable2;
            }
        }

        public List<String> ac(String string) {
            LinkedList linkedList = new LinkedList();
            Cursor cursor = this.nt.rawQuery("select modelid from activemodels where machineid=? order by modelIndex", new String[]{string});
            if (cursor == null) {
                return linkedList;
            }
            while (cursor.moveToNext()) {
                String string2 = cursor.getString(0);
                linkedList.add((Object)string2);
                this.ad(string2);
            }
            cursor.close();
            return linkedList;
        }

        public void beginTransaction() {
            this.nu.beginTransaction();
        }

        public void endTransaction() {
            this.nu.endTransaction();
        }

        public void setTransactionSuccessful() {
            this.nu.setTransactionSuccessful();
        }
    }

}

