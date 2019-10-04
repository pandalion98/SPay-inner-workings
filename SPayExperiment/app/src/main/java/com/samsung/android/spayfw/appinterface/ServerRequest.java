/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

public class ServerRequest
implements Parcelable {
    public static final Parcelable.Creator<ServerRequest> CREATOR = new Parcelable.Creator<ServerRequest>(){

        public ServerRequest createFromParcel(Parcel parcel) {
            return new ServerRequest(parcel);
        }

        public ServerRequest[] newArray(int n2) {
            return new ServerRequest[n2];
        }
    };
    public static final int REQUEST_METHOD_DELETE = 2;
    public static final int REQUEST_METHOD_GET = 0;
    public static final int REQUEST_METHOD_POST = 1;
    public static final int SERVICE_TYPE_ANALYTICS = 2;
    public static final int SERVICE_TYPE_CASH_CARD = 3;
    public static final int SERVICE_TYPE_PAYMENT_SERVICE = 4;
    public static final int SERVICE_TYPE_PROMOTIONS = 1;
    public static final int SERVICE_TYPE_REWARDS = 5;
    public static final int SERVICE_TYPE_TOKEN_REQUESTER;
    private static final int[] VALID_REQUEST_METHODS;
    private static final int[] VALID_SERVICE_TYPES;
    private String body = null;
    private ParcelFileDescriptor fd;
    private Bundle headers = null;
    private String relativeUrl = null;
    private int requestMethod = -1;
    private int serviceType = -1;

    static {
        VALID_SERVICE_TYPES = new int[]{0, 1, 2, 3, 4, 5};
        VALID_REQUEST_METHODS = new int[]{0, 1, 2};
    }

    public ServerRequest() {
    }

    private ServerRequest(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private static boolean isValidType(int[] arrn, int n2) {
        int n3 = arrn.length;
        int n4 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    bl = false;
                    if (n4 >= n3) break block3;
                    if (arrn[n4] != n2) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n4;
        } while (true);
    }

    /*
     * Exception decompiling
     */
    private void readContentFromFd() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [20[TRYBLOCK]], but top level block is 5[TRYBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public int describeContents() {
        return 0;
    }

    public String getBody() {
        return this.body;
    }

    public Bundle getHeaders() {
        return this.headers;
    }

    public String getRelativeUrl() {
        return this.relativeUrl;
    }

    public int getRequestMethod() {
        return this.requestMethod;
    }

    public int getServiceType() {
        return this.serviceType;
    }

    public void readFromParcel(Parcel parcel) {
        this.serviceType = parcel.readInt();
        this.requestMethod = parcel.readInt();
        this.relativeUrl = parcel.readString();
        this.headers = parcel.readBundle();
        this.fd = (ParcelFileDescriptor)parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.readContentFromFd();
    }

    public void setBody(String string) {
        this.body = string;
    }

    public void setFd(ParcelFileDescriptor parcelFileDescriptor) {
        this.fd = parcelFileDescriptor;
    }

    public void setHeaders(Bundle bundle) {
        this.headers = bundle;
    }

    public void setRelativeUrl(String string) {
        this.relativeUrl = string;
    }

    public void setRequestMethod(int n2) {
        if (!ServerRequest.isValidType(VALID_REQUEST_METHODS, n2)) {
            throw new IllegalArgumentException(n2 + " is not a valid request method");
        }
        this.requestMethod = n2;
    }

    public void setServiceType(int n2) {
        if (!ServerRequest.isValidType(VALID_SERVICE_TYPES, n2)) {
            throw new IllegalArgumentException(n2 + " is not a valid service type");
        }
        this.serviceType = n2;
    }

    public String toString() {
        return "ServerRequest: serviceType: " + this.serviceType + " requestMethod: " + this.requestMethod + " relativeUrl: " + this.relativeUrl + " body: " + this.body + " headers: " + (Object)this.headers;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.serviceType);
        parcel.writeInt(this.requestMethod);
        parcel.writeString(this.relativeUrl);
        parcel.writeBundle(this.headers);
        parcel.writeParcelable((Parcelable)this.fd, n2);
    }

}

