package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MstPayConfigEntryItem implements Parcelable {
    public static final Creator<MstPayConfigEntryItem> CREATOR;
    public static final String EXTRA_PARAMS_KEY_DATA_LENGTH = "dataLength";
    public static final String EXTRA_PARAMS_KEY_NAME_LENGTH = "nameLength";
    public static final String EXTRA_PARAMS_KEY_PAN_LENGTH = "panLength";
    public static final int MST_PAY_DIRECTION_REVERESE = 1;
    public static final int MST_PAY_DIRECTION_STRIGHT = 0;
    private int direction;
    private Bundle extraParams;
    private int leadingZeros;
    private int trackIndex;
    private int trailingZeros;

    /* renamed from: com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem.1 */
    static class C03711 implements Creator<MstPayConfigEntryItem> {
        C03711() {
        }

        public MstPayConfigEntryItem createFromParcel(Parcel parcel) {
            return new MstPayConfigEntryItem(null);
        }

        public MstPayConfigEntryItem[] newArray(int i) {
            return new MstPayConfigEntryItem[i];
        }
    }

    static {
        CREATOR = new C03711();
    }

    private MstPayConfigEntryItem(Parcel parcel) {
        readFromParcel(parcel);
    }

    public MstPayConfigEntryItem() {
        this.direction = MST_PAY_DIRECTION_REVERESE;
        this.leadingZeros = 30;
        this.trailingZeros = 30;
        this.trackIndex = MST_PAY_DIRECTION_REVERESE;
        this.extraParams = null;
    }

    public int describeContents() {
        return 0;
    }

    public int getDirection() {
        return this.direction;
    }

    public int getLeadingZeros() {
        return this.leadingZeros;
    }

    public int getTrailingZeros() {
        return this.trailingZeros;
    }

    public int getTrackIndex() {
        return this.trackIndex;
    }

    public Bundle getExtraParams() {
        return this.extraParams;
    }

    public void readFromParcel(Parcel parcel) {
        this.direction = parcel.readInt();
        this.leadingZeros = parcel.readInt();
        this.trailingZeros = parcel.readInt();
        this.trackIndex = parcel.readInt();
        this.extraParams = parcel.readBundle();
    }

    public void setDirection(int i) {
        this.direction = i;
    }

    public void setLeadingZeros(int i) {
        this.leadingZeros = i;
    }

    public void setTrailingZeros(int i) {
        this.trailingZeros = i;
    }

    public void setTrackIndex(int i) {
        this.trackIndex = i;
    }

    public void setExtraParams(Bundle bundle) {
        this.extraParams = bundle;
    }

    public String toString() {
        return "MstPayConfigItem: direction: " + this.direction + " leadingZeros: " + this.leadingZeros + " trailingZeros:  " + this.trailingZeros + " trackIndex:  " + this.trackIndex + " extraParams: " + this.extraParams;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.direction);
        parcel.writeInt(this.leadingZeros);
        parcel.writeInt(this.trailingZeros);
        parcel.writeInt(this.trackIndex);
        parcel.writeBundle(this.extraParams);
    }
}
