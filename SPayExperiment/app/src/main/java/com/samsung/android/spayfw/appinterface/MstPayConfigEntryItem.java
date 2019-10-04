/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MstPayConfigEntryItem
implements Parcelable {
    public static final Parcelable.Creator<MstPayConfigEntryItem> CREATOR = new Parcelable.Creator<MstPayConfigEntryItem>(){

        public MstPayConfigEntryItem createFromParcel(Parcel parcel) {
            return new MstPayConfigEntryItem(parcel);
        }

        public MstPayConfigEntryItem[] newArray(int n2) {
            return new MstPayConfigEntryItem[n2];
        }
    };
    public static final String EXTRA_PARAMS_KEY_DATA_LENGTH = "dataLength";
    public static final String EXTRA_PARAMS_KEY_NAME_LENGTH = "nameLength";
    public static final String EXTRA_PARAMS_KEY_PAN_LENGTH = "panLength";
    public static final int MST_PAY_DIRECTION_REVERESE = 1;
    public static final int MST_PAY_DIRECTION_STRIGHT;
    private int direction;
    private Bundle extraParams;
    private int leadingZeros;
    private int trackIndex;
    private int trailingZeros;

    public MstPayConfigEntryItem() {
        this.direction = 1;
        this.leadingZeros = 30;
        this.trailingZeros = 30;
        this.trackIndex = 1;
        this.extraParams = null;
    }

    private MstPayConfigEntryItem(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public int getDirection() {
        return this.direction;
    }

    public Bundle getExtraParams() {
        return this.extraParams;
    }

    public int getLeadingZeros() {
        return this.leadingZeros;
    }

    public int getTrackIndex() {
        return this.trackIndex;
    }

    public int getTrailingZeros() {
        return this.trailingZeros;
    }

    public void readFromParcel(Parcel parcel) {
        this.direction = parcel.readInt();
        this.leadingZeros = parcel.readInt();
        this.trailingZeros = parcel.readInt();
        this.trackIndex = parcel.readInt();
        this.extraParams = parcel.readBundle();
    }

    public void setDirection(int n2) {
        this.direction = n2;
    }

    public void setExtraParams(Bundle bundle) {
        this.extraParams = bundle;
    }

    public void setLeadingZeros(int n2) {
        this.leadingZeros = n2;
    }

    public void setTrackIndex(int n2) {
        this.trackIndex = n2;
    }

    public void setTrailingZeros(int n2) {
        this.trailingZeros = n2;
    }

    public String toString() {
        return "MstPayConfigItem: direction: " + this.direction + " leadingZeros: " + this.leadingZeros + " trailingZeros:  " + this.trailingZeros + " trackIndex:  " + this.trackIndex + " extraParams: " + (Object)this.extraParams;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.direction);
        parcel.writeInt(this.leadingZeros);
        parcel.writeInt(this.trailingZeros);
        parcel.writeInt(this.trackIndex);
        parcel.writeBundle(this.extraParams);
    }

}

