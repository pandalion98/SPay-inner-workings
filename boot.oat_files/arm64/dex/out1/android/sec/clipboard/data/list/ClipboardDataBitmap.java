package android.sec.clipboard.data.list;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.sec.clipboard.data.ClipboardConstants;
import android.sec.clipboard.data.ClipboardData;
import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ClipboardDataBitmap extends ClipboardData {
    private static final String TAG = "ClipboardDataBitmap";
    private static final long serialVersionUID = 1;
    private String mExtraDataPath = "";
    private transient ParcelFileDescriptor mExtraParcelFd = null;
    private String mInitBaseValue = "";
    private boolean mInitBaseValueCheck = true;
    private String mValue = "";
    private String mValueUrl = "";

    public ClipboardDataBitmap() {
        super(3);
    }

    public void setExtraParcelFileDescriptor(ParcelFileDescriptor pfd) {
        this.mExtraParcelFd = pfd;
    }

    public ParcelFileDescriptor getExtraParcelFileDescriptor() {
        return this.mExtraParcelFd;
    }

    public boolean isValidData() {
        if (this.mValue == null) {
            return false;
        }
        return true;
    }

    public boolean SetAlternateFormat(int format, ClipboardData altData) {
        if (!super.SetAlternateFormat(format, altData) || this.mValue == null) {
            return false;
        }
        boolean Result;
        switch (format) {
            case 3:
                ((ClipboardDataBitmap) altData).setExtraParcelFileDescriptor(this.mExtraParcelFd);
                Result = ((ClipboardDataBitmap) altData).setBitmapPath(getBitmapPath(), GetHtmlUrl(), getExtraDataPath());
                break;
            default:
                Result = false;
                break;
        }
        return Result;
    }

    public void clearData() {
        this.mValue = "";
        this.mValueUrl = "";
        this.mExtraDataPath = "";
        this.mExtraParcelFd = null;
    }

    public boolean SetBitmap(Bitmap bitmap) {
        return false;
    }

    public boolean SetBitmapPath(String FilePath) {
        return setBitmapPath(FilePath);
    }

    public boolean setBitmapPath(String FilePath) {
        boolean Result = false;
        if (FilePath == null || FilePath.length() < 1) {
            return 0;
        }
        if (this.mInitBaseValueCheck) {
            this.mInitBaseValue = FilePath;
            this.mInitBaseValueCheck = false;
        }
        this.mValue = FilePath;
        if (new File(FilePath).isFile()) {
            Result = true;
        } else if (ClipboardConstants.DEBUG) {
            Log.e(TAG, "ClipboardDataBitmap : value is no file path ..check plz");
        }
        return Result;
    }

    public boolean setBitmapPath(String FilePath, String HtmlUrl, String ExtraDataPath) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "setBitmapPath");
        }
        boolean Result = false;
        if (FilePath == null || FilePath.length() < 1) {
            return 0;
        }
        if (this.mInitBaseValueCheck) {
            this.mInitBaseValue = FilePath;
            this.mInitBaseValueCheck = false;
        }
        this.mValue = FilePath;
        if (HtmlUrl != null && HtmlUrl.length() > 0) {
            if (ClipboardConstants.INFO_DEBUG) {
                Log.i(TAG, "HtmlUrl =" + HtmlUrl);
            }
            this.mValueUrl = HtmlUrl;
        }
        if (ExtraDataPath != null && ExtraDataPath.length() > 0) {
            if (ClipboardConstants.INFO_DEBUG) {
                Log.i(TAG, "ExtraDataPath =" + ExtraDataPath);
            }
            this.mExtraDataPath = ExtraDataPath;
        }
        if (new File(FilePath).isFile()) {
            if (HasExtraData()) {
                File ExtraDataFile = new File(ExtraDataPath);
                if (ExtraDataFile != null && ExtraDataFile.isFile()) {
                    Result = true;
                } else if (ClipboardConstants.DEBUG) {
                    Log.e(TAG, "ClipboardDataBitmap : ExtraDataPath is no file path ..check plz");
                }
            } else {
                Result = true;
            }
        } else if (ClipboardConstants.DEBUG) {
            Log.e(TAG, "ClipboardDataBitmap : value is no file path ..check plz");
        }
        return Result;
    }

    public boolean SetExtraDataPath(String FilePath) {
        return setExtraDataPath(FilePath);
    }

    public boolean setExtraDataPath(String FilePath) {
        boolean Result = false;
        if (FilePath == null || FilePath.length() < 1) {
            return 0;
        }
        this.mExtraDataPath = FilePath;
        if (new File(FilePath).isFile()) {
            Result = true;
        } else if (ClipboardConstants.DEBUG) {
            Log.e(TAG, "ClipboardDataBitmap : ExtraDataPath is no file path ..check plz");
        }
        return Result;
    }

    public String GetBitmapPath() {
        return this.mValue;
    }

    public String getBitmapPath() {
        return this.mValue;
    }

    public String GetHtmlUrl() {
        return this.mValueUrl;
    }

    public String GetExtraDataPath() {
        return getExtraDataPath();
    }

    public String getExtraDataPath() {
        return this.mExtraDataPath;
    }

    public String getInitBasePath() {
        return this.mInitBaseValue;
    }

    public boolean HasExtraData() {
        if (this.mExtraDataPath == null || this.mExtraDataPath.length() < 1) {
            return false;
        }
        return true;
    }

    public Bitmap GetBitmap() {
        return null;
    }

    public boolean equals(Object o) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "bitmap equals");
        }
        boolean result = false;
        if (!super.equals(o)) {
            return 0;
        }
        if (!(o instanceof ClipboardDataBitmap)) {
            return 0;
        }
        ClipboardDataBitmap trgData = (ClipboardDataBitmap) o;
        String trgBmp = trgData.getBitmapPath();
        String trgInitBasePath = trgData.getInitBasePath();
        if (trgInitBasePath != null && trgInitBasePath.compareTo(this.mInitBaseValue) == 0) {
            ParcelFileDescriptor pfd = trgData.getParcelFileDescriptor();
            if (pfd != null) {
                if (compareFile(this.mValue, pfd.getFileDescriptor())) {
                    result = true;
                    if (ClipboardConstants.DEBUG) {
                        Log.e(TAG, "bitmap equals");
                    }
                }
            } else if (compareFile(this.mValue, trgBmp)) {
                result = true;
                if (ClipboardConstants.DEBUG) {
                    Log.e(TAG, "bitmap equals");
                }
            }
        }
        return result;
    }

    private boolean compareFileSize(String f1, String f2) {
        File file1 = new File(f1);
        if (file1.length() != new File(f2).length() || file1.length() <= 1) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean compareFile(java.io.FileInputStream r25, java.io.FileInputStream r26) {
        /*
        r24 = this;
        r7 = 5;
        r8 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r2 = 0;
        r21 = r25.getChannel();	 Catch:{ IOException -> 0x00ce }
        r22 = r21.size();	 Catch:{ IOException -> 0x00ce }
        r0 = r22;
        r0 = (int) r0;	 Catch:{ IOException -> 0x00ce }
        r19 = r0;
        r21 = r26.getChannel();	 Catch:{ IOException -> 0x00ce }
        r22 = r21.size();	 Catch:{ IOException -> 0x00ce }
        r0 = r22;
        r9 = (int) r0;
        r0 = r19;
        if (r0 != r9) goto L_0x002e;
    L_0x0020:
        r21 = 1;
        r0 = r19;
        r1 = r21;
        if (r0 < r1) goto L_0x002e;
    L_0x0028:
        r21 = 1;
        r0 = r21;
        if (r9 >= r0) goto L_0x003c;
    L_0x002e:
        r21 = 0;
        r25.close();	 Catch:{ IOException -> 0x0037 }
        r26.close();	 Catch:{ IOException -> 0x0037 }
    L_0x0036:
        return r21;
    L_0x0037:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x0036;
    L_0x003c:
        r21 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r0 = r19;
        r1 = r21;
        if (r0 > r1) goto L_0x00b4;
    L_0x0044:
        r6 = r19;
    L_0x0046:
        r20 = r19 / r6;
        r21 = 5;
        r0 = r20;
        r1 = r21;
        if (r0 < r1) goto L_0x00b7;
    L_0x0050:
        r14 = 5;
    L_0x0051:
        r21 = r6 * r14;
        r20 = r19 - r21;
        r15 = r20 / r14;
        r3 = 1;
        r5 = 0;
        r4 = 0;
        r16 = 0;
        r0 = new byte[r6];	 Catch:{ IOException -> 0x00ce }
        r18 = r0;
        r0 = new byte[r6];	 Catch:{ IOException -> 0x00ce }
        r17 = r0;
        r5 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x00ce }
        r0 = r25;
        r5.<init>(r0);	 Catch:{ IOException -> 0x00ce }
        r4 = new java.io.BufferedInputStream;	 Catch:{ IOException -> 0x00ce }
        r0 = r26;
        r4.<init>(r0);	 Catch:{ IOException -> 0x00ce }
        r12 = 0;
    L_0x0073:
        if (r12 >= r14) goto L_0x00bf;
    L_0x0075:
        if (r3 == 0) goto L_0x00bf;
    L_0x0077:
        r21 = 0;
        r0 = r18;
        r1 = r21;
        r5.read(r0, r1, r6);	 Catch:{ IOException -> 0x00ce }
        r21 = 0;
        r0 = r17;
        r1 = r21;
        r4.read(r0, r1, r6);	 Catch:{ IOException -> 0x00ce }
        r21 = r6 + r15;
        r16 = r16 + r21;
        r0 = r16;
        r0 = (long) r0;	 Catch:{ IOException -> 0x00ce }
        r22 = r0;
        r0 = r22;
        r5.skip(r0);	 Catch:{ IOException -> 0x00ce }
        r0 = r16;
        r0 = (long) r0;	 Catch:{ IOException -> 0x00ce }
        r22 = r0;
        r0 = r22;
        r4.skip(r0);	 Catch:{ IOException -> 0x00ce }
        r13 = 0;
    L_0x00a2:
        if (r13 >= r6) goto L_0x00bc;
    L_0x00a4:
        if (r3 == 0) goto L_0x00bc;
    L_0x00a6:
        r21 = r18[r13];	 Catch:{ IOException -> 0x00ce }
        r22 = r17[r13];	 Catch:{ IOException -> 0x00ce }
        r0 = r21;
        r1 = r22;
        if (r0 != r1) goto L_0x00ba;
    L_0x00b0:
        r2 = 1;
    L_0x00b1:
        r13 = r13 + 1;
        goto L_0x00a2;
    L_0x00b4:
        r6 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        goto L_0x0046;
    L_0x00b7:
        r14 = r20;
        goto L_0x0051;
    L_0x00ba:
        r2 = 0;
        goto L_0x00b1;
    L_0x00bc:
        r12 = r12 + 1;
        goto L_0x0073;
    L_0x00bf:
        r25.close();	 Catch:{ IOException -> 0x00c9 }
        r26.close();	 Catch:{ IOException -> 0x00c9 }
    L_0x00c5:
        r21 = r2;
        goto L_0x0036;
    L_0x00c9:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x00c5;
    L_0x00ce:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ all -> 0x00df }
        r2 = 0;
        r25.close();	 Catch:{ IOException -> 0x00da }
        r26.close();	 Catch:{ IOException -> 0x00da }
        goto L_0x00c5;
    L_0x00da:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x00c5;
    L_0x00df:
        r21 = move-exception;
        r25.close();	 Catch:{ IOException -> 0x00e7 }
        r26.close();	 Catch:{ IOException -> 0x00e7 }
    L_0x00e6:
        throw r21;
    L_0x00e7:
        r11 = move-exception;
        r11.printStackTrace();
        goto L_0x00e6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.sec.clipboard.data.list.ClipboardDataBitmap.compareFile(java.io.FileInputStream, java.io.FileInputStream):boolean");
    }

    private boolean compareFile(String src, FileDescriptor fd) {
        FileNotFoundException e;
        try {
            FileInputStream fisSrc = new FileInputStream(src);
            FileInputStream fileInputStream;
            try {
                FileInputStream fisDest = new FileInputStream(fd);
                FileInputStream fileInputStream2 = fisDest;
                fileInputStream = fisSrc;
                return compareFile(fisSrc, fisDest);
            } catch (FileNotFoundException e2) {
                e = e2;
                fileInputStream = fisSrc;
                e.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e3) {
            e = e3;
            e.printStackTrace();
            return false;
        }
    }

    private boolean compareFile(String src, String dest) {
        FileNotFoundException e;
        try {
            FileInputStream fisSrc = new FileInputStream(src);
            FileInputStream fileInputStream;
            try {
                FileInputStream fisDest = new FileInputStream(dest);
                FileInputStream fileInputStream2 = fisDest;
                fileInputStream = fisSrc;
                return compareFile(fisSrc, fisDest);
            } catch (FileNotFoundException e2) {
                e = e2;
                fileInputStream = fisSrc;
                e.printStackTrace();
                return src.equals(dest);
            }
        } catch (FileNotFoundException e3) {
            e = e3;
            e.printStackTrace();
            return src.equals(dest);
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (ClipboardConstants.DEBUG) {
            Log.i(TAG, "Bitmap write to parcel");
        }
        super.writeToParcel(dest, flags);
        if (this.mClipdata == null) {
            CharSequence charSequence = "clipboarddragNdrop";
            this.mClipdata = new ClipData(charSequence, new String[]{"text/uri-list"}, new Item(Uri.fromFile(new File(this.mValue))));
        }
        dest.writeValue(this.mValue);
        dest.writeValue(this.mInitBaseValue);
        dest.writeValue(this.mValueUrl);
        dest.writeValue(this.mExtraDataPath);
        dest.writeValue(this.mClipdata);
        dest.writeValue(Boolean.valueOf(this.mIsProtected));
        if (this.mExtraParcelFd != null) {
            dest.writeInt(1);
            dest.writeFileDescriptor(this.mExtraParcelFd.getFileDescriptor());
            return;
        }
        dest.writeInt(0);
    }

    protected void readFromSource(Parcel source) {
        try {
            this.mValue = (String) source.readValue(String.class.getClassLoader());
            this.mInitBaseValue = (String) source.readValue(String.class.getClassLoader());
            this.mValueUrl = (String) source.readValue(String.class.getClassLoader());
            this.mExtraDataPath = (String) source.readValue(String.class.getClassLoader());
            this.mClipdata = (ClipData) source.readValue(ClipData.class.getClassLoader());
            this.mIsProtected = ((Boolean) source.readValue(Boolean.class.getClassLoader())).booleanValue();
            if (source.readInt() != 0) {
                this.mExtraParcelFd = new ParcelFileDescriptor(source.readRawFileDescriptor());
            }
            if (ClipboardConstants.INFO_DEBUG) {
                Log.i(TAG, "ClipboardDataBitmap : readFromSource : " + this.mValue);
            }
        } catch (Exception e) {
            Log.i(TAG, "readFromSource~Exception :" + e.getMessage());
        }
    }

    public String toString() {
        return "this Bitmap class. Value is " + (this.mValue.length() > 20 ? this.mValue.subSequence(0, 20) : this.mValue);
    }

    public boolean SetBitmapPath(String FilePath, String HtmlUrl, String ExtraDataPath) {
        return false;
    }

    protected void readFormSource(Parcel source) {
    }
}
