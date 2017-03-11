package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TnC implements Parcelable {
    public static final Creator<TnC> CREATOR;
    public static final String USAGE_PRIVACY = "PRIVACY";
    public static final String USAGE_SERVICE = "SERVICE";
    private byte[] content;
    private ParcelFileDescriptor fd;
    private String locale;
    private String type;
    private String url;
    private String usage;

    /* renamed from: com.samsung.android.spayfw.appinterface.TnC.1 */
    static class C03911 implements Creator<TnC> {
        C03911() {
        }

        public TnC createFromParcel(Parcel parcel) {
            return new TnC(null);
        }

        public TnC[] newArray(int i) {
            return null;
        }
    }

    static {
        CREATOR = new C03911();
    }

    private TnC(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getType() {
        return this.type;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getUrl() {
        return this.url;
    }

    public void readFromParcel(Parcel parcel) {
        this.type = parcel.readString();
        this.locale = parcel.readString();
        this.usage = parcel.readString();
        this.content = parcel.createByteArray();
        this.fd = (ParcelFileDescriptor) parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        copyContentFromFd();
        this.url = parcel.readString();
    }

    public void setContent(byte[] bArr) {
        this.content = bArr;
    }

    public void setLocale(String str) {
        this.locale = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setUsage(String str) {
        this.usage = str;
    }

    public void setFd(ParcelFileDescriptor parcelFileDescriptor) {
        this.fd = parcelFileDescriptor;
    }

    public ParcelFileDescriptor getFd() {
        return this.fd;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String toString() {
        String str = "TnC: type: " + this.type + " locale: " + this.locale + " usage: " + this.usage + "url: " + this.url;
        byte[] content = getContent();
        if (content != null) {
            return str + " content: " + new String(content);
        }
        return str + " content: null";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.type);
        parcel.writeString(this.locale);
        parcel.writeString(this.usage);
        parcel.writeByteArray(this.content);
        parcel.writeParcelable(this.fd, i);
        parcel.writeString(this.url);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void copyContentFromFd() {
        /*
        r11 = this;
        r2 = 0;
        r0 = r11.fd;
        if (r0 == 0) goto L_0x0055;
    L_0x0005:
        r0 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r0];
        r3 = r11.fd;	 Catch:{ IOException -> 0x0051 }
        r1 = 0;
        r4 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
        r5 = r3.getFileDescriptor();	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
        r4.<init>(r5);	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
        r5 = 0;
        r6 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x0036, all -> 0x007c }
        r6.<init>();	 Catch:{ Throwable -> 0x0036, all -> 0x007c }
        r7 = 0;
    L_0x001c:
        r8 = r4.read(r0);	 Catch:{ Throwable -> 0x0028, all -> 0x00ad }
        r9 = -1;
        if (r8 == r9) goto L_0x0056;
    L_0x0023:
        r9 = 0;
        r6.write(r0, r9, r8);	 Catch:{ Throwable -> 0x0028, all -> 0x00ad }
        goto L_0x001c;
    L_0x0028:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x002a }
    L_0x002a:
        r1 = move-exception;
        r10 = r1;
        r1 = r0;
        r0 = r10;
    L_0x002e:
        if (r6 == 0) goto L_0x0035;
    L_0x0030:
        if (r1 == 0) goto L_0x0088;
    L_0x0032:
        r6.close();	 Catch:{ Throwable -> 0x0083, all -> 0x007c }
    L_0x0035:
        throw r0;	 Catch:{ Throwable -> 0x0036, all -> 0x007c }
    L_0x0036:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0038 }
    L_0x0038:
        r1 = move-exception;
        r10 = r1;
        r1 = r0;
        r0 = r10;
    L_0x003c:
        if (r4 == 0) goto L_0x0043;
    L_0x003e:
        if (r1 == 0) goto L_0x009c;
    L_0x0040:
        r4.close();	 Catch:{ Throwable -> 0x0097, all -> 0x0091 }
    L_0x0043:
        throw r0;	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
    L_0x0044:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0046 }
    L_0x0046:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x0049:
        if (r3 == 0) goto L_0x0050;
    L_0x004b:
        if (r2 == 0) goto L_0x00a9;
    L_0x004d:
        r3.close();	 Catch:{ Throwable -> 0x00a4 }
    L_0x0050:
        throw r0;	 Catch:{ IOException -> 0x0051 }
    L_0x0051:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0055:
        return;
    L_0x0056:
        r0 = r6.toByteArray();	 Catch:{ Throwable -> 0x0028, all -> 0x00ad }
        r11.content = r0;	 Catch:{ Throwable -> 0x0028, all -> 0x00ad }
        if (r6 == 0) goto L_0x0063;
    L_0x005e:
        if (r2 == 0) goto L_0x007f;
    L_0x0060:
        r6.close();	 Catch:{ Throwable -> 0x0077, all -> 0x007c }
    L_0x0063:
        if (r4 == 0) goto L_0x006a;
    L_0x0065:
        if (r2 == 0) goto L_0x0093;
    L_0x0067:
        r4.close();	 Catch:{ Throwable -> 0x008c, all -> 0x0091 }
    L_0x006a:
        if (r3 == 0) goto L_0x0055;
    L_0x006c:
        if (r2 == 0) goto L_0x00a0;
    L_0x006e:
        r3.close();	 Catch:{ Throwable -> 0x0072 }
        goto L_0x0055;
    L_0x0072:
        r0 = move-exception;
        r1.addSuppressed(r0);	 Catch:{ IOException -> 0x0051 }
        goto L_0x0055;
    L_0x0077:
        r0 = move-exception;
        r7.addSuppressed(r0);	 Catch:{ Throwable -> 0x0036, all -> 0x007c }
        goto L_0x0063;
    L_0x007c:
        r0 = move-exception;
        r1 = r2;
        goto L_0x003c;
    L_0x007f:
        r6.close();	 Catch:{ Throwable -> 0x0036, all -> 0x007c }
        goto L_0x0063;
    L_0x0083:
        r5 = move-exception;
        r1.addSuppressed(r5);	 Catch:{ Throwable -> 0x0036, all -> 0x007c }
        goto L_0x0035;
    L_0x0088:
        r6.close();	 Catch:{ Throwable -> 0x0036, all -> 0x007c }
        goto L_0x0035;
    L_0x008c:
        r0 = move-exception;
        r5.addSuppressed(r0);	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
        goto L_0x006a;
    L_0x0091:
        r0 = move-exception;
        goto L_0x0049;
    L_0x0093:
        r4.close();	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
        goto L_0x006a;
    L_0x0097:
        r4 = move-exception;
        r1.addSuppressed(r4);	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
        goto L_0x0043;
    L_0x009c:
        r4.close();	 Catch:{ Throwable -> 0x0044, all -> 0x0091 }
        goto L_0x0043;
    L_0x00a0:
        r3.close();	 Catch:{ IOException -> 0x0051 }
        goto L_0x0055;
    L_0x00a4:
        r1 = move-exception;
        r2.addSuppressed(r1);	 Catch:{ IOException -> 0x0051 }
        goto L_0x0050;
    L_0x00a9:
        r3.close();	 Catch:{ IOException -> 0x0051 }
        goto L_0x0050;
    L_0x00ad:
        r0 = move-exception;
        r1 = r2;
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.appinterface.TnC.copyContentFromFd():void");
    }
}
