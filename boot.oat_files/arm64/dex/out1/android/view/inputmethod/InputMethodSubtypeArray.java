package android.view.inputmethod;

import android.os.Parcel;
import android.util.Slog;
import java.util.List;

public class InputMethodSubtypeArray {
    private static final String TAG = "InputMethodSubtypeArray";
    private volatile byte[] mCompressedData;
    private final int mCount;
    private volatile int mDecompressedSize;
    private volatile InputMethodSubtype[] mInstance;
    private final Object mLockObject = new Object();

    public InputMethodSubtypeArray(List<InputMethodSubtype> subtypes) {
        if (subtypes == null) {
            this.mCount = 0;
            return;
        }
        this.mCount = subtypes.size();
        this.mInstance = (InputMethodSubtype[]) subtypes.toArray(new InputMethodSubtype[this.mCount]);
    }

    public InputMethodSubtypeArray(Parcel source) {
        this.mCount = source.readInt();
        if (this.mCount > 0) {
            this.mDecompressedSize = source.readInt();
            this.mCompressedData = source.createByteArray();
        }
    }

    public void writeToParcel(Parcel dest) {
        if (this.mCount == 0) {
            dest.writeInt(this.mCount);
            return;
        }
        byte[] compressedData = this.mCompressedData;
        int decompressedSize = this.mDecompressedSize;
        if (compressedData == null && decompressedSize == 0) {
            synchronized (this.mLockObject) {
                compressedData = this.mCompressedData;
                decompressedSize = this.mDecompressedSize;
                if (compressedData == null && decompressedSize == 0) {
                    byte[] decompressedData = marshall(this.mInstance);
                    compressedData = compress(decompressedData);
                    if (compressedData == null) {
                        decompressedSize = -1;
                        Slog.i(TAG, "Failed to compress data.");
                    } else {
                        decompressedSize = decompressedData.length;
                    }
                    this.mDecompressedSize = decompressedSize;
                    this.mCompressedData = compressedData;
                }
            }
        }
        if (compressedData == null || decompressedSize <= 0) {
            Slog.i(TAG, "Unexpected state. Behaving as an empty array.");
            dest.writeInt(0);
            return;
        }
        dest.writeInt(this.mCount);
        dest.writeInt(decompressedSize);
        dest.writeByteArray(compressedData);
    }

    public InputMethodSubtype get(int index) {
        if (index < 0 || this.mCount <= index) {
            throw new ArrayIndexOutOfBoundsException();
        }
        InputMethodSubtype[] instance = this.mInstance;
        if (instance == null) {
            synchronized (this.mLockObject) {
                instance = this.mInstance;
                if (instance == null) {
                    byte[] decompressedData = decompress(this.mCompressedData, this.mDecompressedSize);
                    this.mCompressedData = null;
                    this.mDecompressedSize = 0;
                    if (decompressedData != null) {
                        instance = unmarshall(decompressedData);
                    } else {
                        Slog.e(TAG, "Failed to decompress data. Returns null as fallback.");
                        instance = new InputMethodSubtype[this.mCount];
                    }
                    this.mInstance = instance;
                }
            }
        }
        return instance[index];
    }

    public int getCount() {
        return this.mCount;
    }

    private static byte[] marshall(InputMethodSubtype[] array) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.writeTypedArray(array, 0);
            byte[] marshall = parcel.marshall();
            return marshall;
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    private static InputMethodSubtype[] unmarshall(byte[] data) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.unmarshall(data, 0, data.length);
            parcel.setDataPosition(0);
            InputMethodSubtype[] inputMethodSubtypeArr = (InputMethodSubtype[]) parcel.createTypedArray(InputMethodSubtype.CREATOR);
            return inputMethodSubtypeArr;
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] compress(byte[] r9) {
        /*
        r5 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x0039 }
        r1.<init>();	 Catch:{ Exception -> 0x0039 }
        r6 = 0;
        r3 = new java.util.zip.GZIPOutputStream;	 Catch:{ Throwable -> 0x002b, all -> 0x0047 }
        r3.<init>(r1);	 Catch:{ Throwable -> 0x002b, all -> 0x0047 }
        r7 = 0;
        r3.write(r9);	 Catch:{ Throwable -> 0x0053, all -> 0x0073 }
        r3.finish();	 Catch:{ Throwable -> 0x0053, all -> 0x0073 }
        r4 = r1.toByteArray();	 Catch:{ Throwable -> 0x0053, all -> 0x0073 }
        if (r3 == 0) goto L_0x001e;
    L_0x0019:
        if (r5 == 0) goto L_0x0043;
    L_0x001b:
        r3.close();	 Catch:{ Throwable -> 0x0026, all -> 0x0047 }
    L_0x001e:
        if (r1 == 0) goto L_0x0025;
    L_0x0020:
        if (r5 == 0) goto L_0x004f;
    L_0x0022:
        r1.close();	 Catch:{ Throwable -> 0x004a }
    L_0x0025:
        return r4;
    L_0x0026:
        r2 = move-exception;
        r7.addSuppressed(r2);	 Catch:{ Throwable -> 0x002b, all -> 0x0047 }
        goto L_0x001e;
    L_0x002b:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x002d }
    L_0x002d:
        r6 = move-exception;
        r8 = r6;
        r6 = r4;
        r4 = r8;
    L_0x0031:
        if (r1 == 0) goto L_0x0038;
    L_0x0033:
        if (r6 == 0) goto L_0x006f;
    L_0x0035:
        r1.close();	 Catch:{ Throwable -> 0x006a }
    L_0x0038:
        throw r4;	 Catch:{ Exception -> 0x0039 }
    L_0x0039:
        r0 = move-exception;
        r4 = "InputMethodSubtypeArray";
        r6 = "Failed to compress the data.";
        android.util.Slog.e(r4, r6, r0);
        r4 = r5;
        goto L_0x0025;
    L_0x0043:
        r3.close();	 Catch:{ Throwable -> 0x002b, all -> 0x0047 }
        goto L_0x001e;
    L_0x0047:
        r4 = move-exception;
        r6 = r5;
        goto L_0x0031;
    L_0x004a:
        r2 = move-exception;
        r6.addSuppressed(r2);	 Catch:{ Exception -> 0x0039 }
        goto L_0x0025;
    L_0x004f:
        r1.close();	 Catch:{ Exception -> 0x0039 }
        goto L_0x0025;
    L_0x0053:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0055 }
    L_0x0055:
        r6 = move-exception;
        r8 = r6;
        r6 = r4;
        r4 = r8;
    L_0x0059:
        if (r3 == 0) goto L_0x0060;
    L_0x005b:
        if (r6 == 0) goto L_0x0066;
    L_0x005d:
        r3.close();	 Catch:{ Throwable -> 0x0061, all -> 0x0047 }
    L_0x0060:
        throw r4;	 Catch:{ Throwable -> 0x002b, all -> 0x0047 }
    L_0x0061:
        r2 = move-exception;
        r6.addSuppressed(r2);	 Catch:{ Throwable -> 0x002b, all -> 0x0047 }
        goto L_0x0060;
    L_0x0066:
        r3.close();	 Catch:{ Throwable -> 0x002b, all -> 0x0047 }
        goto L_0x0060;
    L_0x006a:
        r2 = move-exception;
        r6.addSuppressed(r2);	 Catch:{ Exception -> 0x0039 }
        goto L_0x0038;
    L_0x006f:
        r1.close();	 Catch:{ Exception -> 0x0039 }
        goto L_0x0038;
    L_0x0073:
        r4 = move-exception;
        r6 = r5;
        goto L_0x0059;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodSubtypeArray.compress(byte[]):byte[]");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] decompress(byte[] r13, int r14) {
        /*
        r8 = 0;
        r1 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x0043 }
        r1.<init>(r13);	 Catch:{ Exception -> 0x0043 }
        r9 = 0;
        r6 = new java.util.zip.GZIPInputStream;	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        r6.<init>(r1);	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        r10 = 0;
        r4 = new byte[r14];	 Catch:{ Throwable -> 0x007e, all -> 0x009e }
        r5 = 0;
    L_0x0010:
        r11 = r4.length;	 Catch:{ Throwable -> 0x007e, all -> 0x009e }
        if (r5 >= r11) goto L_0x001c;
    L_0x0013:
        r11 = r4.length;	 Catch:{ Throwable -> 0x007e, all -> 0x009e }
        r3 = r11 - r5;
        r2 = r6.read(r4, r5, r3);	 Catch:{ Throwable -> 0x007e, all -> 0x009e }
        if (r2 >= 0) goto L_0x002e;
    L_0x001c:
        if (r14 == r5) goto L_0x005d;
    L_0x001e:
        if (r6 == 0) goto L_0x0025;
    L_0x0020:
        if (r8 == 0) goto L_0x004d;
    L_0x0022:
        r6.close();	 Catch:{ Throwable -> 0x0030, all -> 0x0051 }
    L_0x0025:
        if (r1 == 0) goto L_0x002c;
    L_0x0027:
        if (r8 == 0) goto L_0x0059;
    L_0x0029:
        r1.close();	 Catch:{ Throwable -> 0x0054 }
    L_0x002c:
        r4 = r8;
    L_0x002d:
        return r4;
    L_0x002e:
        r5 = r5 + r2;
        goto L_0x0010;
    L_0x0030:
        r7 = move-exception;
        r10.addSuppressed(r7);	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        goto L_0x0025;
    L_0x0035:
        r9 = move-exception;
        throw r9;	 Catch:{ all -> 0x0037 }
    L_0x0037:
        r10 = move-exception;
        r12 = r10;
        r10 = r9;
        r9 = r12;
    L_0x003b:
        if (r1 == 0) goto L_0x0042;
    L_0x003d:
        if (r10 == 0) goto L_0x009a;
    L_0x003f:
        r1.close();	 Catch:{ Throwable -> 0x0095 }
    L_0x0042:
        throw r9;	 Catch:{ Exception -> 0x0043 }
    L_0x0043:
        r0 = move-exception;
        r9 = "InputMethodSubtypeArray";
        r10 = "Failed to decompress the data.";
        android.util.Slog.e(r9, r10, r0);
        r4 = r8;
        goto L_0x002d;
    L_0x004d:
        r6.close();	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        goto L_0x0025;
    L_0x0051:
        r9 = move-exception;
        r10 = r8;
        goto L_0x003b;
    L_0x0054:
        r7 = move-exception;
        r9.addSuppressed(r7);	 Catch:{ Exception -> 0x0043 }
        goto L_0x002c;
    L_0x0059:
        r1.close();	 Catch:{ Exception -> 0x0043 }
        goto L_0x002c;
    L_0x005d:
        if (r6 == 0) goto L_0x0064;
    L_0x005f:
        if (r8 == 0) goto L_0x0076;
    L_0x0061:
        r6.close();	 Catch:{ Throwable -> 0x0071, all -> 0x0051 }
    L_0x0064:
        if (r1 == 0) goto L_0x002d;
    L_0x0066:
        if (r8 == 0) goto L_0x007a;
    L_0x0068:
        r1.close();	 Catch:{ Throwable -> 0x006c }
        goto L_0x002d;
    L_0x006c:
        r7 = move-exception;
        r9.addSuppressed(r7);	 Catch:{ Exception -> 0x0043 }
        goto L_0x002d;
    L_0x0071:
        r7 = move-exception;
        r10.addSuppressed(r7);	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        goto L_0x0064;
    L_0x0076:
        r6.close();	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        goto L_0x0064;
    L_0x007a:
        r1.close();	 Catch:{ Exception -> 0x0043 }
        goto L_0x002d;
    L_0x007e:
        r9 = move-exception;
        throw r9;	 Catch:{ all -> 0x0080 }
    L_0x0080:
        r10 = move-exception;
        r12 = r10;
        r10 = r9;
        r9 = r12;
    L_0x0084:
        if (r6 == 0) goto L_0x008b;
    L_0x0086:
        if (r10 == 0) goto L_0x0091;
    L_0x0088:
        r6.close();	 Catch:{ Throwable -> 0x008c, all -> 0x0051 }
    L_0x008b:
        throw r9;	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
    L_0x008c:
        r7 = move-exception;
        r10.addSuppressed(r7);	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        goto L_0x008b;
    L_0x0091:
        r6.close();	 Catch:{ Throwable -> 0x0035, all -> 0x0051 }
        goto L_0x008b;
    L_0x0095:
        r7 = move-exception;
        r10.addSuppressed(r7);	 Catch:{ Exception -> 0x0043 }
        goto L_0x0042;
    L_0x009a:
        r1.close();	 Catch:{ Exception -> 0x0043 }
        goto L_0x0042;
    L_0x009e:
        r9 = move-exception;
        r10 = r8;
        goto L_0x0084;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodSubtypeArray.decompress(byte[], int):byte[]");
    }
}
