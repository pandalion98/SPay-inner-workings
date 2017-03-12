package android.database;

import android.content.res.Resources;
import android.database.sqlite.SQLiteClosable;
import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.LongSparseArray;
import dalvik.system.CloseGuard;

public class CursorWindow extends SQLiteClosable implements Parcelable {
    public static final Creator<CursorWindow> CREATOR = new Creator<CursorWindow>() {
        public CursorWindow createFromParcel(Parcel source) {
            return new CursorWindow(source);
        }

        public CursorWindow[] newArray(int size) {
            return new CursorWindow[size];
        }
    };
    private static final String STATS_TAG = "CursorWindowStats";
    private static int sCursorWindowSize = -1;
    private static final LongSparseArray<Integer> sWindowToPidMap = new LongSparseArray();
    private final CloseGuard mCloseGuard;
    private final String mName;
    private int mStartPos;
    public long mWindowPtr;

    private static native boolean nativeAllocRow(long j);

    private static native void nativeClear(long j);

    private static native void nativeCopyStringToBuffer(long j, int i, int i2, CharArrayBuffer charArrayBuffer);

    private static native long nativeCreate(String str, int i);

    private static native long nativeCreateFromParcel(Parcel parcel);

    private static native void nativeDispose(long j);

    private static native void nativeFreeLastRow(long j);

    private static native byte[] nativeGetBlob(long j, int i, int i2);

    private static native double nativeGetDouble(long j, int i, int i2);

    private static native long nativeGetLong(long j, int i, int i2);

    private static native String nativeGetName(long j);

    private static native int nativeGetNumRows(long j);

    private static native String nativeGetString(long j, int i, int i2);

    private static native int nativeGetType(long j, int i, int i2);

    private static native boolean nativePutBlob(long j, byte[] bArr, int i, int i2);

    private static native boolean nativePutDouble(long j, double d, int i, int i2);

    private static native boolean nativePutLong(long j, long j2, int i, int i2);

    private static native boolean nativePutNull(long j, int i, int i2);

    private static native boolean nativePutString(long j, String str, int i, int i2);

    private static native boolean nativeSetNumColumns(long j, int i);

    private static native void nativeWriteToParcel(long j, Parcel parcel);

    public CursorWindow(String name) {
        this.mCloseGuard = CloseGuard.get();
        this.mStartPos = 0;
        if (name == null || name.length() == 0) {
            name = "<unnamed>";
        }
        this.mName = name;
        if (sCursorWindowSize < 0) {
            sCursorWindowSize = Resources.getSystem().getInteger(17694839) * 1024;
        }
        this.mWindowPtr = nativeCreate(this.mName, sCursorWindowSize);
        if (this.mWindowPtr == 0) {
            throw new CursorWindowAllocationException("Cursor window allocation of " + (sCursorWindowSize / 1024) + " kb failed. " + printStats());
        }
        this.mCloseGuard.open("close");
        recordNewWindow(Binder.getCallingPid(), this.mWindowPtr);
    }

    public CursorWindow(String name, int windowSize) {
        this.mCloseGuard = CloseGuard.get();
        this.mStartPos = 0;
        if (name == null || name.length() == 0) {
            name = "<unnamed>";
        }
        this.mName = name;
        this.mWindowPtr = nativeCreate(this.mName, windowSize);
        if (this.mWindowPtr == 0) {
            throw new CursorWindowAllocationException("Cursor window allocation of " + (sCursorWindowSize / 1024) + " kb failed. " + printStats());
        }
        this.mCloseGuard.open("close");
        recordNewWindow(Binder.getCallingPid(), this.mWindowPtr);
    }

    @Deprecated
    public CursorWindow(boolean localWindow) {
        this((String) null);
    }

    private CursorWindow(Parcel source) {
        this.mCloseGuard = CloseGuard.get();
        this.mStartPos = source.readInt();
        this.mWindowPtr = nativeCreateFromParcel(source);
        if (this.mWindowPtr == 0) {
            throw new CursorWindowAllocationException("Cursor window could not be created from binder.");
        }
        this.mName = nativeGetName(this.mWindowPtr);
        this.mCloseGuard.open("close");
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            dispose();
        } finally {
            super.finalize();
        }
    }

    private void dispose() {
        if (this.mCloseGuard != null) {
            this.mCloseGuard.close();
        }
        if (this.mWindowPtr != 0) {
            recordClosingOfWindow(this.mWindowPtr);
            nativeDispose(this.mWindowPtr);
            this.mWindowPtr = 0;
        }
    }

    public String getName() {
        return this.mName;
    }

    public void clear() {
        acquireReference();
        try {
            this.mStartPos = 0;
            nativeClear(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    public int getStartPosition() {
        return this.mStartPos;
    }

    public void setStartPosition(int pos) {
        this.mStartPos = pos;
    }

    public int getNumRows() {
        acquireReference();
        try {
            int nativeGetNumRows = nativeGetNumRows(this.mWindowPtr);
            return nativeGetNumRows;
        } finally {
            releaseReference();
        }
    }

    public boolean setNumColumns(int columnNum) {
        acquireReference();
        try {
            boolean nativeSetNumColumns = nativeSetNumColumns(this.mWindowPtr, columnNum);
            return nativeSetNumColumns;
        } finally {
            releaseReference();
        }
    }

    public boolean allocRow() {
        acquireReference();
        try {
            boolean nativeAllocRow = nativeAllocRow(this.mWindowPtr);
            return nativeAllocRow;
        } finally {
            releaseReference();
        }
    }

    public void freeLastRow() {
        acquireReference();
        try {
            nativeFreeLastRow(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    @Deprecated
    public boolean isNull(int row, int column) {
        return getType(row, column) == 0;
    }

    @Deprecated
    public boolean isBlob(int row, int column) {
        int type = getType(row, column);
        return type == 4 || type == 0;
    }

    @Deprecated
    public boolean isLong(int row, int column) {
        return getType(row, column) == 1;
    }

    @Deprecated
    public boolean isFloat(int row, int column) {
        return getType(row, column) == 2;
    }

    @Deprecated
    public boolean isString(int row, int column) {
        int type = getType(row, column);
        return type == 3 || type == 0;
    }

    public int getType(int row, int column) {
        acquireReference();
        try {
            int nativeGetType = nativeGetType(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetType;
        } finally {
            releaseReference();
        }
    }

    public byte[] getBlob(int row, int column) {
        acquireReference();
        try {
            byte[] nativeGetBlob = nativeGetBlob(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetBlob;
        } finally {
            releaseReference();
        }
    }

    public String getString(int row, int column) {
        acquireReference();
        try {
            String nativeGetString = nativeGetString(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetString;
        } finally {
            releaseReference();
        }
    }

    public void copyStringToBuffer(int row, int column, CharArrayBuffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("CharArrayBuffer should not be null");
        }
        acquireReference();
        try {
            nativeCopyStringToBuffer(this.mWindowPtr, row - this.mStartPos, column, buffer);
        } finally {
            releaseReference();
        }
    }

    public long getLong(int row, int column) {
        acquireReference();
        try {
            long nativeGetLong = nativeGetLong(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetLong;
        } finally {
            releaseReference();
        }
    }

    public double getDouble(int row, int column) {
        acquireReference();
        try {
            double nativeGetDouble = nativeGetDouble(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetDouble;
        } finally {
            releaseReference();
        }
    }

    public short getShort(int row, int column) {
        return (short) ((int) getLong(row, column));
    }

    public int getInt(int row, int column) {
        return (int) getLong(row, column);
    }

    public float getFloat(int row, int column) {
        return (float) getDouble(row, column);
    }

    public boolean putBlob(byte[] value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutBlob = nativePutBlob(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutBlob;
        } finally {
            releaseReference();
        }
    }

    public boolean putString(String value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutString = nativePutString(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutString;
        } finally {
            releaseReference();
        }
    }

    public boolean putLong(long value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutLong = nativePutLong(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutLong;
        } finally {
            releaseReference();
        }
    }

    public boolean putDouble(double value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutDouble = nativePutDouble(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutDouble;
        } finally {
            releaseReference();
        }
    }

    public boolean putNull(int row, int column) {
        acquireReference();
        try {
            boolean nativePutNull = nativePutNull(this.mWindowPtr, row - this.mStartPos, column);
            return nativePutNull;
        } finally {
            releaseReference();
        }
    }

    public static CursorWindow newFromParcel(Parcel p) {
        return (CursorWindow) CREATOR.createFromParcel(p);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        acquireReference();
        try {
            dest.writeInt(this.mStartPos);
            nativeWriteToParcel(this.mWindowPtr, dest);
            if ((flags & 1) != 0) {
                releaseReference();
            }
        } finally {
            releaseReference();
        }
    }

    protected void onAllReferencesReleased() {
        dispose();
    }

    private void recordNewWindow(int pid, long window) {
        synchronized (sWindowToPidMap) {
            sWindowToPidMap.put(window, Integer.valueOf(pid));
            if (Log.isLoggable(STATS_TAG, 2)) {
                Log.i(STATS_TAG, "Created a new Cursor. " + printStats());
            }
        }
    }

    private void recordClosingOfWindow(long window) {
        synchronized (sWindowToPidMap) {
            if (sWindowToPidMap.size() == 0) {
                return;
            }
            sWindowToPidMap.delete(window);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String printStats() {
        /*
        r15 = this;
        r14 = 980; // 0x3d4 float:1.373E-42 double:4.84E-321;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = android.os.Process.myPid();
        r10 = 0;
        r7 = new android.util.SparseIntArray;
        r7.<init>();
        r13 = sWindowToPidMap;
        monitor-enter(r13);
        r12 = sWindowToPidMap;	 Catch:{ all -> 0x0072 }
        r9 = r12.size();	 Catch:{ all -> 0x0072 }
        if (r9 != 0) goto L_0x0020;
    L_0x001c:
        r12 = "";
        monitor-exit(r13);	 Catch:{ all -> 0x0072 }
    L_0x001f:
        return r12;
    L_0x0020:
        r2 = 0;
    L_0x0021:
        if (r2 >= r9) goto L_0x003b;
    L_0x0023:
        r12 = sWindowToPidMap;	 Catch:{ all -> 0x0072 }
        r12 = r12.valueAt(r2);	 Catch:{ all -> 0x0072 }
        r12 = (java.lang.Integer) r12;	 Catch:{ all -> 0x0072 }
        r6 = r12.intValue();	 Catch:{ all -> 0x0072 }
        r11 = r7.get(r6);	 Catch:{ all -> 0x0072 }
        r11 = r11 + 1;
        r7.put(r6, r11);	 Catch:{ all -> 0x0072 }
        r2 = r2 + 1;
        goto L_0x0021;
    L_0x003b:
        monitor-exit(r13);	 Catch:{ all -> 0x0072 }
        r5 = r7.size();
        r1 = 0;
    L_0x0041:
        if (r1 >= r5) goto L_0x0093;
    L_0x0043:
        r12 = " (# cursors opened by ";
        r0.append(r12);
        r6 = r7.keyAt(r1);
        if (r6 != r3) goto L_0x0075;
    L_0x004e:
        r12 = "this proc=";
        r0.append(r12);
    L_0x0054:
        r4 = r7.get(r6);
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r12 = r12.append(r4);
        r13 = ")";
        r12 = r12.append(r13);
        r12 = r12.toString();
        r0.append(r12);
        r10 = r10 + r4;
        r1 = r1 + 1;
        goto L_0x0041;
    L_0x0072:
        r12 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x0072 }
        throw r12;
    L_0x0075:
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r13 = "pid ";
        r12 = r12.append(r13);
        r12 = r12.append(r6);
        r13 = "=";
        r12 = r12.append(r13);
        r12 = r12.toString();
        r0.append(r12);
        goto L_0x0054;
    L_0x0093:
        r12 = r0.length();
        if (r12 <= r14) goto L_0x00b7;
    L_0x0099:
        r12 = 0;
        r8 = r0.substring(r12, r14);
    L_0x009e:
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r13 = "# Open Cursors=";
        r12 = r12.append(r13);
        r12 = r12.append(r10);
        r12 = r12.append(r8);
        r12 = r12.toString();
        goto L_0x001f;
    L_0x00b7:
        r8 = r0.toString();
        goto L_0x009e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.CursorWindow.printStats():java.lang.String");
    }

    public String toString() {
        return getName() + " {" + Long.toHexString(this.mWindowPtr) + "}";
    }
}
