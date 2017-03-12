package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.appinterface.CardColors;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.p009a.DBHelperWrapper;
import com.samsung.android.spayfw.utils.DBUtils;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public abstract class DbAdapter {
    private DBHelperWrapper BT;
    private SQLiteDatabase Bq;

    /* renamed from: com.samsung.android.spayfw.storage.DbAdapter.1 */
    static /* synthetic */ class C05891 {
        static final /* synthetic */ int[] BU;

        static {
            BU = new int[ColumnType.values().length];
            try {
                BU[ColumnType.TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                BU[ColumnType.INTEGER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                BU[ColumnType.BLOB.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                BU[ColumnType.NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum ColumnType {
        INTEGER("INTEGER"),
        LONG("INTEGER"),
        REAL("REAL"),
        TEXT(CardColors.USAGE_TEXT),
        BLOB("BLOB"),
        NULL("NULL");
        
        private final String defaultValue;

        private ColumnType(String str) {
            this.defaultValue = str;
        }

        public final String fs() {
            return this.defaultValue;
        }
    }

    public static final void m1106a(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    protected DbAdapter(Context context) {
        this.BT = null;
        this.BT = DBHelperWrapper.m689b(context, "spayfw_enc.db", 1, DBName.spayfw_enc);
        this.Bq = this.BT.getWritableDatabase(DBUtils.getDbPassword());
    }

    public int m1112d(String str, String str2, String str3) {
        String str4;
        String[] strArr;
        Throwable th;
        if (str2 != null) {
            str4 = str2 + " = ?";
            strArr = str3 != null ? new String[]{str3} : null;
        } else {
            str2 = "_id";
            strArr = null;
            str4 = null;
        }
        Cursor query;
        try {
            synchronized (DbAdapter.class) {
                try {
                    query = this.Bq.query(str, new String[]{str2}, str4, strArr, null, null, null);
                    try {
                        if (query != null) {
                            try {
                                Log.m285d("DbAdapter", "get count by column:" + query.getCount());
                                int count = query.getCount();
                                m1106a(query);
                                return count;
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } else {
                            m1106a(query);
                            return 0;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    query = null;
                    throw th;
                }
            }
        } catch (Throwable th5) {
            th = th5;
            query = null;
            m1106a(query);
            throw th;
        }
    }

    public List<String> m1111a(String str, String str2, String str3, String str4) {
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
        Cursor query;
        try {
            synchronized (DbAdapter.class) {
                try {
                    query = this.Bq.query(str, strArr2, str5, strArr, null, null, null);
                    try {
                        ArrayList arrayList;
                        if (query != null) {
                            try {
                                if (query.getCount() > 0) {
                                    arrayList = new ArrayList(query.getCount());
                                    while (query.moveToNext()) {
                                        arrayList.add(query.getString(query.getColumnIndex(str2)));
                                    }
                                    m1106a(query);
                                    return arrayList;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                m1106a(query);
                                throw th;
                            }
                        }
                        arrayList = null;
                        m1106a(query);
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
            m1106a(query);
            throw th;
        }
    }

    public int m1108a(String str, ContentValues contentValues, String str2, String str3) {
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
            update = this.Bq.update(str, contentValues, str4, strArr);
        }
        return update;
    }

    public int m1109a(String str, String str2, Object obj, ColumnType columnType, String str3, String str4) {
        if (str2 == null || str3 == null) {
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        switch (C05891.BU[columnType.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                contentValues.put(str2, (String) obj);
                break;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                contentValues.put(str2, (Integer) obj);
                break;
            case F2m.PPB /*3*/:
                contentValues.put(str2, (byte[]) obj);
                break;
            default:
                return -1;
        }
        return m1108a(str, contentValues, str3, str4);
    }

    public int m1113e(String str, String str2, String str3) {
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
            delete = this.Bq.delete(str, str4, strArr);
        }
        return delete;
    }

    public int m1107a(String str, ContentValues contentValues) {
        if (contentValues == null) {
            return -1;
        }
        int replaceOrThrow;
        synchronized (DbAdapter.class) {
            replaceOrThrow = (int) this.Bq.replaceOrThrow(str, null, contentValues);
        }
        return replaceOrThrow;
    }

    public Cursor m1114f(String str, String str2, String str3) {
        Cursor query;
        String[] strArr = null;
        if (str2 != null) {
            String str4 = str2 + " = ?";
            if (str3 != null) {
                strArr = new String[]{str3};
            }
            synchronized (DbAdapter.class) {
                query = this.Bq.query(str, null, str4, strArr, null, null, null);
            }
        }
        return query;
    }

    public Cursor m1110a(String str, String[] strArr, String str2, String[] strArr2, String str3) {
        Cursor query;
        synchronized (DbAdapter.class) {
            query = this.Bq.query(str, strArr, str2, strArr2, null, null, str3);
        }
        return query;
    }

    public Cursor query(String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5) {
        Cursor query;
        synchronized (DbAdapter.class) {
            query = this.Bq.query(str, strArr, str2, strArr2, str3, str4, str5);
        }
        return query;
    }

    public int delete(String str, String str2, String[] strArr) {
        int delete;
        synchronized (DbAdapter.class) {
            delete = this.Bq.delete(str, str2, strArr);
        }
        return delete;
    }

    public void execSQL(String str) {
        synchronized (DbAdapter.class) {
            this.Bq.execSQL(str);
        }
    }
}
