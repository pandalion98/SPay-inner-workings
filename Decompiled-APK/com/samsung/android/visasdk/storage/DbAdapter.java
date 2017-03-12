package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p008e.p009a.DBHelperWrapper;
import com.samsung.android.spayfw.utils.DBUtils;
import com.samsung.android.visasdk.facade.VisaPaymentSDKImpl;
import com.samsung.android.visasdk.p025c.Log;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.visasdk.storage.a */
public class DbAdapter extends DbOpenHelper {
    private static DbAdapter Gt;
    private DBHelperWrapper Gs;
    private SQLiteDatabase db;

    public static synchronized DbAdapter as(Context context) {
        DbAdapter dbAdapter;
        synchronized (DbAdapter.class) {
            if (Gt == null) {
                if (context == null) {
                    Log.m1301e("DbAdapter", "context is null");
                    dbAdapter = null;
                } else {
                    Gt = new DbAdapter(context.getApplicationContext());
                }
            }
            dbAdapter = Gt;
        }
        return dbAdapter;
    }

    private DbAdapter(Context context) {
        super(context);
        this.Gs = null;
        this.Gs = DBHelperWrapper.m689b(context, "cbp_jan_enc.db", 3, DBName.cbp_jan_enc);
        this.Gs.m690b(this);
        this.db = this.Gs.getWritableDatabase(DBUtils.getDbPassword());
        VisaPaymentSDKImpl.resetDbPassword();
    }

    public static final void m1356a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public List<String> m1359a(String str, String str2, String str3, String str4) {
        Cursor query;
        Throwable th;
        List<String> list = null;
        if (str2 == null) {
            return null;
        }
        String str5;
        String[] strArr;
        String[] strArr2 = new String[]{str2};
        if (str3 != null) {
            str5 = str3 + " = ?";
            strArr = str4 != null ? new String[]{str4} : null;
        } else {
            strArr = null;
            str5 = null;
        }
        try {
            synchronized (DbAdapter.class) {
                try {
                    query = this.db.query(str, strArr2, str5, strArr, null, null, null);
                    try {
                        ArrayList arrayList;
                        if (query != null) {
                            try {
                                if (query.getCount() > 0) {
                                    arrayList = new ArrayList(query.getCount());
                                    while (query.moveToNext()) {
                                        arrayList.add(query.getString(query.getColumnIndex(str2)));
                                    }
                                    DbAdapter.m1356a(query);
                                    return arrayList;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                DbAdapter.m1356a(query);
                                throw th;
                            }
                        }
                        arrayList = null;
                        DbAdapter.m1356a(query);
                        return arrayList;
                    } catch (Throwable th3) {
                        th = th3;
                        Object obj = query;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            th = th4;
                            Object obj2 = list;
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    throw th;
                }
            }
        } catch (Throwable th6) {
            th = th6;
            query = null;
            DbAdapter.m1356a(query);
            throw th;
        }
    }

    public List<byte[]> m1361b(String str, String str2, String str3, String str4) {
        Cursor query;
        Throwable th;
        List<byte[]> list = null;
        if (str2 == null) {
            return null;
        }
        String str5;
        String[] strArr;
        String[] strArr2 = new String[]{str2};
        if (str3 != null) {
            str5 = str3 + " = ?";
            strArr = str4 != null ? new String[]{str4} : null;
        } else {
            strArr = null;
            str5 = null;
        }
        try {
            synchronized (DbAdapter.class) {
                try {
                    query = this.db.query(str, strArr2, str5, strArr, null, null, null);
                    try {
                        ArrayList arrayList;
                        if (query != null) {
                            try {
                                if (query.getCount() > 0) {
                                    arrayList = new ArrayList(query.getCount());
                                    while (query.moveToNext()) {
                                        arrayList.add(query.getBlob(query.getColumnIndex(str2)));
                                    }
                                    DbAdapter.m1356a(query);
                                    return arrayList;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                DbAdapter.m1356a(query);
                                throw th;
                            }
                        }
                        arrayList = null;
                        DbAdapter.m1356a(query);
                        return arrayList;
                    } catch (Throwable th3) {
                        th = th3;
                        Object obj = query;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            th = th4;
                            Object obj2 = list;
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    throw th;
                }
            }
        } catch (Throwable th6) {
            th = th6;
            query = null;
            DbAdapter.m1356a(query);
            throw th;
        }
    }

    public int m1357a(String str, ContentValues contentValues, String str2, String str3) {
        String[] strArr = null;
        if (contentValues == null) {
            return -1;
        }
        String str4;
        int update;
        if (str2 != null) {
            str4 = str2 + " = ?";
            if (str3 != null) {
                strArr = new String[]{str3};
            }
        } else {
            str4 = null;
        }
        synchronized (DbAdapter.class) {
            update = this.db.update(str, contentValues, str4, strArr);
        }
        return update;
    }

    public int m1362e(String str, String str2, String str3) {
        String str4;
        int delete;
        String[] strArr = null;
        if (str2 != null) {
            str4 = str2 + " = ?";
            if (str3 != null) {
                strArr = new String[]{str3};
            }
        } else {
            str4 = null;
        }
        synchronized (DbAdapter.class) {
            delete = this.db.delete(str, str4, strArr);
        }
        return delete;
    }

    public int m1360b(String str, ContentValues contentValues) {
        if (contentValues == null) {
            return -1;
        }
        int insertOrThrow;
        synchronized (DbAdapter.class) {
            insertOrThrow = (int) this.db.insertOrThrow(str, null, contentValues);
        }
        return insertOrThrow;
    }

    public Cursor m1363f(String str, String str2, String str3) {
        Cursor query;
        String[] strArr = null;
        if (str2 != null) {
            String str4 = str2 + " = ?";
            if (str3 != null) {
                strArr = new String[]{str3};
            }
            synchronized (DbAdapter.class) {
                query = this.db.query(str, null, str4, strArr, null, null, null);
            }
        }
        return query;
    }

    public Cursor m1358a(String str, String[] strArr, String str2, String[] strArr2, String str3) {
        Cursor query;
        synchronized (DbAdapter.class) {
            query = this.db.query(str, strArr, str2, strArr2, null, null, str3);
        }
        return query;
    }
}
