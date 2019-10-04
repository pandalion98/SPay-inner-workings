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

public class CardColors
implements Parcelable {
    public static final Parcelable.Creator<CardColors> CREATOR = new Parcelable.Creator<CardColors>(){

        public CardColors createFromParcel(Parcel parcel) {
            return new CardColors(parcel);
        }

        public CardColors[] newArray(int n2) {
            return new CardColors[n2];
        }
    };
    public static final String USAGE_BACKGROUND = "BACKGROUND";
    public static final String USAGE_FOREGROUND = "FOREGROUND";
    public static final String USAGE_TEXT = "TEXT";
    private String code;
    private String usage;

    public CardColors() {
    }

    private CardColors(Parcel parcel) {
        this.readFromParcel(parcel);
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

    public void setCode(String string) {
        this.code = string;
    }

    public void setUsage(String string) {
        this.usage = string;
    }

    public String toString() {
        return "CardColors: usage: " + this.usage + " code: " + this.code;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.usage);
        parcel.writeString(this.code);
    }

}

