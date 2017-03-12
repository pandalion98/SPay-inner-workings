package com.samsung.android.spayfw.p008e.p009a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperCallback;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperInterface;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p003c.p004a.SdlDBHelperInterfaceImpl;
import com.samsung.android.spayfw.p006d.p007a.SeDBHelperInterfaceImpl;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;
import java.util.HashMap;

/* renamed from: com.samsung.android.spayfw.e.a.a */
public class DBHelperWrapper {
    private static HashMap<DBName, DBHelperWrapper> Di;
    private static Context mContext;
    private DBHelperInterface Dj;

    static {
        Di = new HashMap();
    }

    private DBHelperWrapper(Context context, String str, int i, DBName dBName) {
        this.Dj = null;
        if (Platformutils.fT()) {
            Log.d("DBHelperWrapper", "Sem Device");
            this.Dj = SeDBHelperInterfaceImpl.m682a(context, str, i, dBName);
            return;
        }
        Log.d("DBHelperWrapper", "not Sem Device");
        this.Dj = SdlDBHelperInterfaceImpl.m294a(context, str, i, dBName);
    }

    public SQLiteDatabase getWritableDatabase(byte[] bArr) {
        return this.Dj.getWritableDatabase(bArr);
    }

    public SQLiteDatabase getReadableDatabase(byte[] bArr) {
        return this.Dj.getReadableDatabase(bArr);
    }

    public void m690b(DBHelperCallback dBHelperCallback) {
        this.Dj.m291a(dBHelperCallback);
    }

    public static synchronized DBHelperWrapper m689b(Context context, String str, int i, DBName dBName) {
        DBHelperWrapper dBHelperWrapper;
        synchronized (DBHelperWrapper.class) {
            Log.d("DBHelperWrapper", "getInstance()");
            if (!Di.containsKey(dBName) || Di.get(dBName) == null) {
                Log.d("DBHelperWrapper", "Create new instance");
                mContext = context.getApplicationContext();
                dBHelperWrapper = new DBHelperWrapper(mContext, str, i, dBName);
                Di.put(dBName, dBHelperWrapper);
            } else {
                Log.d("DBHelperWrapper", "Return existing instance");
                dBHelperWrapper = (DBHelperWrapper) Di.get(dBName);
            }
        }
        return dBHelperWrapper;
    }
}
