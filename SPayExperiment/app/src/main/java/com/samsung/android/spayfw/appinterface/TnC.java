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

public class TnC
implements Parcelable {
    public static final Parcelable.Creator<TnC> CREATOR = new Parcelable.Creator<TnC>(){

        public TnC createFromParcel(Parcel parcel) {
            return new TnC(parcel);
        }

        public TnC[] newArray(int n2) {
            return null;
        }
    };
    public static final String USAGE_PRIVACY = "PRIVACY";
    public static final String USAGE_SERVICE = "SERVICE";
    private byte[] content;
    private ParcelFileDescriptor fd;
    private String locale;
    private String type;
    private String url;
    private String usage;

    public TnC() {
    }

    private TnC(Parcel parcel) {
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

    public byte[] getContent() {
        return this.content;
    }

    public ParcelFileDescriptor getFd() {
        return this.fd;
    }

    public String getLocale() {
        return this.locale;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUsage() {
        return this.usage;
    }

    public void readFromParcel(Parcel parcel) {
        this.type = parcel.readString();
        this.locale = parcel.readString();
        this.usage = parcel.readString();
        this.content = parcel.createByteArray();
        this.fd = (ParcelFileDescriptor)parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.copyContentFromFd();
        this.url = parcel.readString();
    }

    public void setContent(byte[] arrby) {
        this.content = arrby;
    }

    public void setFd(ParcelFileDescriptor parcelFileDescriptor) {
        this.fd = parcelFileDescriptor;
    }

    public void setLocale(String string) {
        this.locale = string;
    }

    public void setType(String string) {
        this.type = string;
    }

    public void setUrl(String string) {
        this.url = string;
    }

    public void setUsage(String string) {
        this.usage = string;
    }

    public String toString() {
        String string = "TnC: type: " + this.type + " locale: " + this.locale + " usage: " + this.usage + "url: " + this.url;
        byte[] arrby = this.getContent();
        if (arrby != null) {
            return string + " content: " + new String(arrby);
        }
        return string + " content: null";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.type);
        parcel.writeString(this.locale);
        parcel.writeString(this.usage);
        parcel.writeByteArray(this.content);
        parcel.writeParcelable((Parcelable)this.fd, n2);
        parcel.writeString(this.url);
    }

}

