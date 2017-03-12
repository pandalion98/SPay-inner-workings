package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataHolder implements SafeParcelable {
    public static final zzf CREATOR;
    private static final zza zzOc;
    boolean mClosed;
    private final int zzFG;
    private final int zzLs;
    private final String[] zzNU;
    Bundle zzNV;
    private final CursorWindow[] zzNW;
    private final Bundle zzNX;
    int[] zzNY;
    int zzNZ;
    private Object zzOa;
    private boolean zzOb;

    public static class zza {
        private final String[] zzNU;
        private final ArrayList<HashMap<String, Object>> zzOd;
        private final String zzOe;
        private final HashMap<Object, Integer> zzOf;
        private boolean zzOg;
        private String zzOh;

        private zza(String[] strArr, String str) {
            this.zzNU = (String[]) zzx.zzl(strArr);
            this.zzOd = new ArrayList();
            this.zzOe = str;
            this.zzOf = new HashMap();
            this.zzOg = false;
            this.zzOh = null;
        }
    }

    /* renamed from: com.google.android.gms.common.data.DataHolder.1 */
    static class C00901 extends zza {
        C00901(String[] strArr, String str) {
            super(str, null);
        }
    }

    public static class zzb extends RuntimeException {
        public zzb(String str) {
            super(str);
        }
    }

    static {
        CREATOR = new zzf();
        zzOc = new C00901(new String[0], null);
    }

    DataHolder(int i, String[] strArr, CursorWindow[] cursorWindowArr, int i2, Bundle bundle) {
        this.mClosed = false;
        this.zzOb = true;
        this.zzFG = i;
        this.zzNU = strArr;
        this.zzNW = cursorWindowArr;
        this.zzLs = i2;
        this.zzNX = bundle;
    }

    private DataHolder(zza com_google_android_gms_common_data_DataHolder_zza, int i, Bundle bundle) {
        this(com_google_android_gms_common_data_DataHolder_zza.zzNU, zza(com_google_android_gms_common_data_DataHolder_zza, -1), i, bundle);
    }

    public DataHolder(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.zzOb = true;
        this.zzFG = 1;
        this.zzNU = (String[]) zzx.zzl(strArr);
        this.zzNW = (CursorWindow[]) zzx.zzl(cursorWindowArr);
        this.zzLs = i;
        this.zzNX = bundle;
        zziy();
    }

    public static DataHolder zza(int i, Bundle bundle) {
        return new DataHolder(zzOc, i, bundle);
    }

    private static CursorWindow[] zza(zza com_google_android_gms_common_data_DataHolder_zza, int i) {
        int size;
        int i2 = 0;
        if (com_google_android_gms_common_data_DataHolder_zza.zzNU.length == 0) {
            return new CursorWindow[0];
        }
        List zzb = (i < 0 || i >= com_google_android_gms_common_data_DataHolder_zza.zzOd.size()) ? com_google_android_gms_common_data_DataHolder_zza.zzOd : com_google_android_gms_common_data_DataHolder_zza.zzOd.subList(0, i);
        int size2 = zzb.size();
        CursorWindow cursorWindow = new CursorWindow(false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(cursorWindow);
        cursorWindow.setNumColumns(com_google_android_gms_common_data_DataHolder_zza.zzNU.length);
        int i3 = 0;
        int i4 = 0;
        while (i3 < size2) {
            if (!cursorWindow.allocRow()) {
                Log.d("DataHolder", "Allocating additional cursor window for large data set (row " + i3 + ")");
                cursorWindow = new CursorWindow(false);
                cursorWindow.setStartPosition(i3);
                cursorWindow.setNumColumns(com_google_android_gms_common_data_DataHolder_zza.zzNU.length);
                arrayList.add(cursorWindow);
                if (!cursorWindow.allocRow()) {
                    Log.e("DataHolder", "Unable to allocate row to hold data.");
                    arrayList.remove(cursorWindow);
                    return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
                }
            }
            try {
                int i5;
                int i6;
                CursorWindow cursorWindow2;
                Map map = (Map) zzb.get(i3);
                boolean z = true;
                for (int i7 = 0; i7 < com_google_android_gms_common_data_DataHolder_zza.zzNU.length && z; i7++) {
                    String str = com_google_android_gms_common_data_DataHolder_zza.zzNU[i7];
                    Object obj = map.get(str);
                    if (obj == null) {
                        z = cursorWindow.putNull(i3, i7);
                    } else if (obj instanceof String) {
                        z = cursorWindow.putString((String) obj, i3, i7);
                    } else if (obj instanceof Long) {
                        z = cursorWindow.putLong(((Long) obj).longValue(), i3, i7);
                    } else if (obj instanceof Integer) {
                        z = cursorWindow.putLong((long) ((Integer) obj).intValue(), i3, i7);
                    } else if (obj instanceof Boolean) {
                        z = cursorWindow.putLong(((Boolean) obj).booleanValue() ? 1 : 0, i3, i7);
                    } else if (obj instanceof byte[]) {
                        z = cursorWindow.putBlob((byte[]) obj, i3, i7);
                    } else if (obj instanceof Double) {
                        z = cursorWindow.putDouble(((Double) obj).doubleValue(), i3, i7);
                    } else if (obj instanceof Float) {
                        z = cursorWindow.putDouble((double) ((Float) obj).floatValue(), i3, i7);
                    } else {
                        throw new IllegalArgumentException("Unsupported object for column " + str + ": " + obj);
                    }
                }
                if (z) {
                    i5 = i3;
                    i6 = 0;
                    cursorWindow2 = cursorWindow;
                } else if (i4 != 0) {
                    throw new zzb("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
                } else {
                    Log.d("DataHolder", "Couldn't populate window data for row " + i3 + " - allocating new window.");
                    cursorWindow.freeLastRow();
                    CursorWindow cursorWindow3 = new CursorWindow(false);
                    cursorWindow3.setStartPosition(i3);
                    cursorWindow3.setNumColumns(com_google_android_gms_common_data_DataHolder_zza.zzNU.length);
                    arrayList.add(cursorWindow3);
                    i5 = i3 - 1;
                    cursorWindow2 = cursorWindow3;
                    i6 = 1;
                }
                i4 = i6;
                cursorWindow = cursorWindow2;
                i3 = i5 + 1;
            } catch (RuntimeException e) {
                RuntimeException runtimeException = e;
                size = arrayList.size();
                while (i2 < size) {
                    ((CursorWindow) arrayList.get(i2)).close();
                    i2++;
                }
                throw runtimeException;
            }
        }
        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
    }

    public static DataHolder zzay(int i) {
        return zza(i, null);
    }

    private void zzg(String str, int i) {
        if (this.zzNV == null || !this.zzNV.containsKey(str)) {
            throw new IllegalArgumentException("No such column: " + str);
        } else if (isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        } else if (i < 0 || i >= this.zzNZ) {
            throw new CursorIndexOutOfBoundsException(i, this.zzNZ);
        }
    }

    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (CursorWindow close : this.zzNW) {
                    close.close();
                }
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    protected void finalize() {
        try {
            if (this.zzOb && this.zzNW.length > 0 && !isClosed()) {
                Log.e("DataBuffer", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (" + (this.zzOa == null ? "internal object: " + toString() : this.zzOa.toString()) + ")");
                close();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public int getCount() {
        return this.zzNZ;
    }

    public int getStatusCode() {
        return this.zzLs;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzf.zza(this, parcel, i);
    }

    public long zza(String str, int i, int i2) {
        zzg(str, i);
        return this.zzNW[i2].getLong(i, this.zzNV.getInt(str));
    }

    public void zza(String str, int i, int i2, CharArrayBuffer charArrayBuffer) {
        zzg(str, i);
        this.zzNW[i2].copyStringToBuffer(i, this.zzNV.getInt(str), charArrayBuffer);
    }

    public int zzax(int i) {
        int i2 = 0;
        boolean z = i >= 0 && i < this.zzNZ;
        zzx.zzN(z);
        while (i2 < this.zzNY.length) {
            if (i < this.zzNY[i2]) {
                i2--;
                break;
            }
            i2++;
        }
        return i2 == this.zzNY.length ? i2 - 1 : i2;
    }

    public int zzb(String str, int i, int i2) {
        zzg(str, i);
        return this.zzNW[i2].getInt(i, this.zzNV.getInt(str));
    }

    public boolean zzba(String str) {
        return this.zzNV.containsKey(str);
    }

    public String zzc(String str, int i, int i2) {
        zzg(str, i);
        return this.zzNW[i2].getString(i, this.zzNV.getInt(str));
    }

    public boolean zzd(String str, int i, int i2) {
        zzg(str, i);
        return Long.valueOf(this.zzNW[i2].getLong(i, this.zzNV.getInt(str))).longValue() == 1;
    }

    public float zze(String str, int i, int i2) {
        zzg(str, i);
        return this.zzNW[i2].getFloat(i, this.zzNV.getInt(str));
    }

    public byte[] zzf(String str, int i, int i2) {
        zzg(str, i);
        return this.zzNW[i2].getBlob(i, this.zzNV.getInt(str));
    }

    public Uri zzg(String str, int i, int i2) {
        String zzc = zzc(str, i, i2);
        return zzc == null ? null : Uri.parse(zzc);
    }

    public void zzg(Object obj) {
        this.zzOa = obj;
    }

    public boolean zzh(String str, int i, int i2) {
        zzg(str, i);
        return this.zzNW[i2].isNull(i, this.zzNV.getInt(str));
    }

    CursorWindow[] zziA() {
        return this.zzNW;
    }

    public Bundle zziu() {
        return this.zzNX;
    }

    public void zziy() {
        int i;
        int i2 = 0;
        this.zzNV = new Bundle();
        for (i = 0; i < this.zzNU.length; i++) {
            this.zzNV.putInt(this.zzNU[i], i);
        }
        this.zzNY = new int[this.zzNW.length];
        i = 0;
        while (i2 < this.zzNW.length) {
            this.zzNY[i2] = i;
            i += this.zzNW[i2].getNumRows() - (i - this.zzNW[i2].getStartPosition());
            i2++;
        }
        this.zzNZ = i;
    }

    String[] zziz() {
        return this.zzNU;
    }
}
