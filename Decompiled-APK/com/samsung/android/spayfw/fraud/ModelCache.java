package com.samsung.android.spayfw.fraud;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p008e.p009a.DBHelperWrapper;
import com.samsung.android.spayfw.utils.DBUtils;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.fraud.g */
class ModelCache {
    static ModelCache nq;
    static HashMap<String, SoftReference<FraudModelInfo>> nr;

    /* renamed from: com.samsung.android.spayfw.fraud.g.a */
    static class ModelCache {
        private DBHelperWrapper ns;
        private SQLiteDatabase nt;
        private SQLiteDatabase nu;

        public ModelCache(Context context) {
            this.ns = DBHelperWrapper.m689b(context, null, 0, DBName.collector_enc);
            this.nt = this.ns.getReadableDatabase(DBUtils.getDbPassword());
            this.nu = this.ns.getWritableDatabase(DBUtils.getDbPassword());
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

        public FraudModelInfo ab(String str) {
            Throwable th;
            FraudModelInfo fraudModelInfo = null;
            Cursor rawQuery;
            try {
                rawQuery = this.nt.rawQuery("select modelbase, modelparams from modelinfo where modelid=?", new String[]{str});
                if (rawQuery != null) {
                    try {
                        if (rawQuery.moveToFirst()) {
                            fraudModelInfo = new FraudModelInfo();
                            fraudModelInfo.nh = str;
                            fraudModelInfo.ng = rawQuery.getString(0);
                            fraudModelInfo.ni = rawQuery.getBlob(1);
                            ad(str);
                            rawQuery.close();
                            if (rawQuery != null) {
                                rawQuery.close();
                            }
                            return fraudModelInfo;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        throw th;
                    }
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
                return fraudModelInfo;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                rawQuery = null;
                th = th4;
                if (rawQuery != null) {
                    rawQuery.close();
                }
                throw th;
            }
        }

        public List<String> ac(String str) {
            List linkedList = new LinkedList();
            Cursor rawQuery = this.nt.rawQuery("select modelid from activemodels where machineid=? order by modelIndex", new String[]{str});
            if (rawQuery != null) {
                while (rawQuery.moveToNext()) {
                    String string = rawQuery.getString(0);
                    linkedList.add(string);
                    ad(string);
                }
                rawQuery.close();
            }
            return linkedList;
        }

        private void ad(String str) {
            this.nu.execSQL("update modelinfo set lastaccesstime = current_timestamp where modelid=?", new String[]{str});
        }
    }

    static {
        nq = null;
        nr = new HashMap();
    }

    static void initialize(Context context) {
        nq = new ModelCache(context);
    }

    static FraudModelInfo ab(String str) {
        FraudModelInfo fraudModelInfo;
        if (nr.containsKey(str)) {
            fraudModelInfo = (FraudModelInfo) ((SoftReference) nr.get(str)).get();
            if (fraudModelInfo != null) {
                return fraudModelInfo;
            }
        }
        try {
            nq.beginTransaction();
            fraudModelInfo = nq.ab(str);
            nq.setTransactionSuccessful();
            if (fraudModelInfo == null) {
                return null;
            }
            nr.put(str, new SoftReference(fraudModelInfo));
            return fraudModelInfo;
        } finally {
            nq.endTransaction();
        }
    }

    static List<String> ac(String str) {
        try {
            nq.beginTransaction();
            List<String> ac = nq.ac(str);
            nq.setTransactionSuccessful();
            return ac;
        } finally {
            nq.endTransaction();
        }
    }
}
