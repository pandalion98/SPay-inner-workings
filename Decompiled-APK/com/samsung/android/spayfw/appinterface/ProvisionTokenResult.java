package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class ProvisionTokenResult implements Parcelable {
    public static final String BUNDLE_KEY_LOYALTY_RESPONSE_DATA = "loyaltyResponseData";
    public static final String BUNDLE_KEY_RESPONSE_DATA_FD = "loyaltyResponseDataFd";
    public static final String BUNDLE_KEY_RESPONSE_DATA_FILE_PATH = "loyaltyResponseDataFilePath";
    public static final Creator<ProvisionTokenResult> CREATOR;
    Bundle bundle;
    private List<IdvMethod> idv;
    private Token token;

    /* renamed from: com.samsung.android.spayfw.appinterface.ProvisionTokenResult.1 */
    static class C03781 implements Creator<ProvisionTokenResult> {
        C03781() {
        }

        public ProvisionTokenResult createFromParcel(Parcel parcel) {
            return new ProvisionTokenResult(null);
        }

        public ProvisionTokenResult[] newArray(int i) {
            return new ProvisionTokenResult[i];
        }
    }

    static {
        CREATOR = new C03781();
    }

    private ProvisionTokenResult(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public ProvisionTokenResult() {
        this.idv = new ArrayList();
    }

    public int describeContents() {
        return 0;
    }

    public List<IdvMethod> getIdv() {
        return this.idv;
    }

    public Token getToken() {
        return this.token;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void readFromParcel(Parcel parcel) {
        parcel.readList(this.idv, getClass().getClassLoader());
        this.token = (Token) parcel.readParcelable(getClass().getClassLoader());
        this.bundle = parcel.readBundle();
        copyContentFromFd();
    }

    public void setIdv(List<IdvMethod> list) {
        this.idv = list;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void copyContentFromFd() {
        /*
        r11 = this;
        r2 = 0;
        r0 = r11.bundle;
        if (r0 == 0) goto L_0x00c1;
    L_0x0005:
        r0 = r11.bundle;
        r1 = "loyaltyResponseDataFd";
        r0 = r0.getParcelable(r1);
        r0 = (android.os.ParcelFileDescriptor) r0;
        r3 = r0;
    L_0x0010:
        if (r3 == 0) goto L_0x0060;
    L_0x0012:
        r0 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r0];
        r1 = 0;
        r4 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        r5 = r3.getFileDescriptor();	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        r4.<init>(r5);	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        r5 = 0;
        r6 = new java.io.ByteArrayOutputStream;	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        r6.<init>();	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        r7 = 0;
    L_0x0027:
        r8 = r4.read(r0);	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        r9 = -1;
        if (r8 == r9) goto L_0x0061;
    L_0x002e:
        r9 = 0;
        r6.write(r0, r9, r8);	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        goto L_0x0027;
    L_0x0033:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0035 }
    L_0x0035:
        r1 = move-exception;
        r10 = r1;
        r1 = r0;
        r0 = r10;
    L_0x0039:
        if (r6 == 0) goto L_0x0040;
    L_0x003b:
        if (r1 == 0) goto L_0x0098;
    L_0x003d:
        r6.close();	 Catch:{ Throwable -> 0x0093, all -> 0x008c }
    L_0x0040:
        throw r0;	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
    L_0x0041:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0043 }
    L_0x0043:
        r1 = move-exception;
        r10 = r1;
        r1 = r0;
        r0 = r10;
    L_0x0047:
        if (r4 == 0) goto L_0x004e;
    L_0x0049:
        if (r1 == 0) goto L_0x00ac;
    L_0x004b:
        r4.close();	 Catch:{ Throwable -> 0x00a7, all -> 0x00a1 }
    L_0x004e:
        throw r0;	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
    L_0x004f:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0051 }
    L_0x0051:
        r1 = move-exception;
        r2 = r0;
        r0 = r1;
    L_0x0054:
        if (r3 == 0) goto L_0x005b;
    L_0x0056:
        if (r2 == 0) goto L_0x00b9;
    L_0x0058:
        r3.close();	 Catch:{ Throwable -> 0x00b4 }
    L_0x005b:
        throw r0;	 Catch:{ IOException -> 0x005c }
    L_0x005c:
        r0 = move-exception;
        r0.printStackTrace();
    L_0x0060:
        return;
    L_0x0061:
        r0 = r6.toString();	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        r8 = r11.bundle;	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        r9 = "loyaltyResponseData";
        r8.putString(r9, r0);	 Catch:{ Throwable -> 0x0033, all -> 0x00bd }
        if (r6 == 0) goto L_0x0073;
    L_0x006e:
        if (r2 == 0) goto L_0x008f;
    L_0x0070:
        r6.close();	 Catch:{ Throwable -> 0x0087, all -> 0x008c }
    L_0x0073:
        if (r4 == 0) goto L_0x007a;
    L_0x0075:
        if (r2 == 0) goto L_0x00a3;
    L_0x0077:
        r4.close();	 Catch:{ Throwable -> 0x009c, all -> 0x00a1 }
    L_0x007a:
        if (r3 == 0) goto L_0x0060;
    L_0x007c:
        if (r2 == 0) goto L_0x00b0;
    L_0x007e:
        r3.close();	 Catch:{ Throwable -> 0x0082 }
        goto L_0x0060;
    L_0x0082:
        r0 = move-exception;
        r1.addSuppressed(r0);	 Catch:{ IOException -> 0x005c }
        goto L_0x0060;
    L_0x0087:
        r0 = move-exception;
        r7.addSuppressed(r0);	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0073;
    L_0x008c:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0047;
    L_0x008f:
        r6.close();	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0073;
    L_0x0093:
        r5 = move-exception;
        r1.addSuppressed(r5);	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0040;
    L_0x0098:
        r6.close();	 Catch:{ Throwable -> 0x0041, all -> 0x008c }
        goto L_0x0040;
    L_0x009c:
        r0 = move-exception;
        r5.addSuppressed(r0);	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x007a;
    L_0x00a1:
        r0 = move-exception;
        goto L_0x0054;
    L_0x00a3:
        r4.close();	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x007a;
    L_0x00a7:
        r4 = move-exception;
        r1.addSuppressed(r4);	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x004e;
    L_0x00ac:
        r4.close();	 Catch:{ Throwable -> 0x004f, all -> 0x00a1 }
        goto L_0x004e;
    L_0x00b0:
        r3.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x0060;
    L_0x00b4:
        r1 = move-exception;
        r2.addSuppressed(r1);	 Catch:{ IOException -> 0x005c }
        goto L_0x005b;
    L_0x00b9:
        r3.close();	 Catch:{ IOException -> 0x005c }
        goto L_0x005b;
    L_0x00bd:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0039;
    L_0x00c1:
        r3 = r2;
        goto L_0x0010;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.appinterface.ProvisionTokenResult.copyContentFromFd():void");
    }

    public String toString() {
        String str = "ProvisionTokenResult:";
        if (this.token != null) {
            str = str + this.token.toString();
        } else {
            str = str + " token: null";
        }
        if (this.idv != null) {
            str = str + this.idv.toString();
        } else {
            str = str + "idv : null ";
        }
        if (this.bundle != null) {
            return str + this.bundle.toString();
        }
        return str + "bundle : null ";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.idv);
        parcel.writeParcelable(this.token, i);
        parcel.writeBundle(this.bundle);
    }
}
