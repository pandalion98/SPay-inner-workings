/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;

public class EncryptedImage
implements Parcelable {
    public static final Parcelable.Creator<EncryptedImage> CREATOR = new Parcelable.Creator<EncryptedImage>(){

        public EncryptedImage createFromParcel(Parcel parcel) {
            return new EncryptedImage(parcel);
        }

        public EncryptedImage[] newArray(int n2) {
            return new EncryptedImage[n2];
        }
    };
    private String content;
    private String usage;

    public EncryptedImage(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public EncryptedImage(String string, String string2) {
        this.usage = string;
        this.content = string2;
    }

    public int describeContents() {
        return 0;
    }

    public String getContent() {
        return this.content;
    }

    public String getUsage() {
        return this.usage;
    }

    public void readFromParcel(Parcel parcel) {
        this.usage = parcel.readString();
        this.content = parcel.readString();
    }

    public void setContent(String string) {
        this.content = string;
    }

    public void setUsage(String string) {
        this.usage = string;
    }

    public String toString() {
        return "EncryptedImage [Usage=" + this.usage + ", content=" + this.content + "]";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.usage);
        parcel.writeString(this.content);
    }

}

