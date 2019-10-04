/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

public class ServerResponseData
implements Parcelable {
    public static final Parcelable.Creator<ServerResponseData> CREATOR = new Parcelable.Creator<ServerResponseData>(){

        public ServerResponseData createFromParcel(Parcel parcel) {
            return new ServerResponseData(parcel);
        }

        public ServerResponseData[] newArray(int n2) {
            return new ServerResponseData[n2];
        }
    };
    private String content;
    private ParcelFileDescriptor fd;

    public ServerResponseData() {
    }

    private ServerResponseData(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    /*
     * Exception decompiling
     */
    private void copyContentFromFd() {
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

    public String getContent() {
        return this.content;
    }

    public ParcelFileDescriptor getFd() {
        return this.fd;
    }

    public void readFromParcel(Parcel parcel) {
        this.content = parcel.readString();
        this.fd = (ParcelFileDescriptor)parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.copyContentFromFd();
    }

    public void setContent(String string) {
        this.content = string;
    }

    public void setFd(ParcelFileDescriptor parcelFileDescriptor) {
        this.fd = parcelFileDescriptor;
    }

    public String toString() {
        return "Server Response: content: " + this.content;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.content);
        parcel.writeParcelable((Parcelable)this.fd, n2);
    }

}

