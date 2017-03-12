package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class EncryptedImage implements Parcelable {
    public static final Creator<EncryptedImage> CREATOR;
    private String content;
    private String usage;

    /* renamed from: com.samsung.android.spayfw.appinterface.EncryptedImage.1 */
    static class C03421 implements Creator<EncryptedImage> {
        C03421() {
        }

        public EncryptedImage createFromParcel(Parcel parcel) {
            return new EncryptedImage(parcel);
        }

        public EncryptedImage[] newArray(int i) {
            return new EncryptedImage[i];
        }
    }

    public String toString() {
        return "EncryptedImage [Usage=" + this.usage + ", content=" + this.content + "]";
    }

    static {
        CREATOR = new C03421();
    }

    public EncryptedImage(Parcel parcel) {
        readFromParcel(parcel);
    }

    public EncryptedImage(String str, String str2) {
        this.usage = str;
        this.content = str2;
    }

    public int describeContents() {
        return 0;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getContent() {
        return this.content;
    }

    public void readFromParcel(Parcel parcel) {
        this.usage = parcel.readString();
        this.content = parcel.readString();
    }

    public void setUsage(String str) {
        this.usage = str;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.usage);
        parcel.writeString(this.content);
    }
}
