package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CardColors implements Parcelable {
    public static final Creator<CardColors> CREATOR;
    public static final String USAGE_BACKGROUND = "BACKGROUND";
    public static final String USAGE_FOREGROUND = "FOREGROUND";
    public static final String USAGE_TEXT = "TEXT";
    private String code;
    private String usage;

    /* renamed from: com.samsung.android.spayfw.appinterface.CardColors.1 */
    static class C03341 implements Creator<CardColors> {
        C03341() {
        }

        public CardColors createFromParcel(Parcel parcel) {
            return new CardColors(null);
        }

        public CardColors[] newArray(int i) {
            return new CardColors[i];
        }
    }

    static {
        CREATOR = new C03341();
    }

    private CardColors(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getCode() {
        return this.code;
    }

    public String getUsage() {
        return this.usage;
    }

    public void readFromParcel(Parcel parcel) {
        this.usage = parcel.readString();
        this.code = parcel.readString();
    }

    public void setCode(String str) {
        this.code = str;
    }

    public void setUsage(String str) {
        this.usage = str;
    }

    public String toString() {
        return "CardColors: usage: " + this.usage + " code: " + this.code;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.usage);
        parcel.writeString(this.code);
    }
}
