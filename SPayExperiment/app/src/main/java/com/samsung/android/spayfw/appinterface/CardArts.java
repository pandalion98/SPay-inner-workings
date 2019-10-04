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

public class CardArts
implements Parcelable {
    public static final Parcelable.Creator<CardArts> CREATOR = new Parcelable.Creator<CardArts>(){

        public CardArts createFromParcel(Parcel parcel) {
            return new CardArts(parcel);
        }

        public CardArts[] newArray(int n2) {
            return new CardArts[n2];
        }
    };
    public static final String TYPE_BMP = "image/bmp";
    public static final String TYPE_GIF = "image/gif";
    public static final String TYPE_JPEG = "image/jpeg";
    public static final String TYPE_JPG = "image/jpg";
    public static final String TYPE_PNG = "image/png";
    public static final String USAGE_BANK_APP_ICON = "BANK_APP_ICON";
    public static final String USAGE_BANK_ICON = "BANK_ICON";
    public static final String USAGE_BANK_LOGO = "BANK_LOGO";
    public static final String USAGE_BANK_SYMB = "BANK_SYMB";
    public static final String USAGE_CARDNETWORK_ICON = "CARD_ICON";
    public static final String USAGE_CARDNETWORK_LOGO = "CARD_LOGO";
    public static final String USAGE_CARDNETWORK_SYMB = "CARD_SYMB";
    private byte[] content;
    private ParcelFileDescriptor fd;
    private int height;
    private String type;
    private String usage;
    private int width;

    public CardArts() {
    }

    private CardArts(Parcel parcel) {
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

    public int getHeight() {
        return this.height;
    }

    public String getType() {
        return this.type;
    }

    public String getUsage() {
        return this.usage;
    }

    public int getWidth() {
        return this.width;
    }

    public void readFromParcel(Parcel parcel) {
        this.usage = parcel.readString();
        this.type = parcel.readString();
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.content = parcel.createByteArray();
        this.fd = (ParcelFileDescriptor)parcel.readParcelable(ParcelFileDescriptor.class.getClassLoader());
        this.copyContentFromFd();
    }

    public void setContent(byte[] arrby) {
        this.content = arrby;
    }

    public void setFd(ParcelFileDescriptor parcelFileDescriptor) {
        this.fd = parcelFileDescriptor;
    }

    public void setHeight(int n2) {
        this.height = n2;
    }

    public void setType(String string) {
        this.type = string;
    }

    public void setUsage(String string) {
        this.usage = string;
    }

    public void setWidth(int n2) {
        this.width = n2;
    }

    public String toString() {
        String string = "CardArts: usage: " + this.usage + " type: " + this.type + " width: " + this.width + " height: " + this.height;
        if (this.getContent() != null) {
            return string + "content has image";
        }
        return string + " content: null";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.usage);
        parcel.writeString(this.type);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
        parcel.writeByteArray(this.content);
        parcel.writeParcelable((Parcelable)this.fd, n2);
    }

}

