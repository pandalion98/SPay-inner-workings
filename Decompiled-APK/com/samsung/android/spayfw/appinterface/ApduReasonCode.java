package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ApduReasonCode implements Parcelable {
    public static final Creator<ApduReasonCode> CREATOR;
    private Bundle extra;
    private short reasonCode;
    private short reasonCommand;

    /* renamed from: com.samsung.android.spayfw.appinterface.ApduReasonCode.1 */
    static class C03291 implements Creator<ApduReasonCode> {
        C03291() {
        }

        public ApduReasonCode createFromParcel(Parcel parcel) {
            return new ApduReasonCode(null);
        }

        public ApduReasonCode[] newArray(int i) {
            return new ApduReasonCode[i];
        }
    }

    static {
        CREATOR = new C03291();
    }

    public ApduReasonCode(short s, short s2) {
        this.reasonCommand = s;
        this.reasonCode = s2;
        this.extra = null;
    }

    private ApduReasonCode(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int getCommand() {
        return this.reasonCommand;
    }

    public int getCode() {
        return this.reasonCode;
    }

    public void setCommand(short s) {
        this.reasonCommand = s;
    }

    public void setCode(short s) {
        this.reasonCode = s;
    }

    public Bundle getExtraData() {
        return this.extra;
    }

    public void setExtraData(Bundle bundle) {
        this.extra = bundle;
    }

    public void readFromParcel(Parcel parcel) {
        this.reasonCommand = (short) parcel.readInt();
        this.reasonCode = (short) parcel.readInt();
        this.extra = parcel.readBundle();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.reasonCommand);
        parcel.writeInt(this.reasonCode);
        parcel.writeBundle(this.extra);
    }

    public void reset() {
        this.reasonCommand = (short) 1;
        this.reasonCode = ISO7816.SW_NO_ERROR;
        this.extra = null;
    }
}
