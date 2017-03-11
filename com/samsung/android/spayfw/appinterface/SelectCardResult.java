package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SelectCardResult implements Parcelable {
    public static final Creator<SelectCardResult> CREATOR;
    public static final int SELECTCARD_FAIL = -1;
    public static final int SELECTCARD_SUCCESS = 0;
    public static final int SELECTCARD_SUCCESS_USE_PREVIOUS = 1;
    private int mstTransmitTime;
    private byte[] nonce;
    private int status;
    private String taid;

    /* renamed from: com.samsung.android.spayfw.appinterface.SelectCardResult.1 */
    static class C03841 implements Creator<SelectCardResult> {
        C03841() {
        }

        public SelectCardResult createFromParcel(Parcel parcel) {
            return new SelectCardResult(null);
        }

        public SelectCardResult[] newArray(int i) {
            return null;
        }
    }

    static {
        CREATOR = new C03841();
    }

    private SelectCardResult(Parcel parcel) {
        readFromParcel(parcel);
    }

    public SelectCardResult(String str, byte[] bArr) {
        this.taid = str;
        this.nonce = bArr;
    }

    public int describeContents() {
        return SELECTCARD_SUCCESS;
    }

    public int getStatus() {
        return this.status;
    }

    public byte[] getNonce() {
        return this.nonce;
    }

    public String getTaid() {
        return this.taid;
    }

    public int getMstTransmitTime() {
        return this.mstTransmitTime;
    }

    public void readFromParcel(Parcel parcel) {
        this.status = parcel.readInt();
        this.taid = parcel.readString();
        this.nonce = parcel.createByteArray();
        this.mstTransmitTime = parcel.readInt();
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setNonce(byte[] bArr) {
        this.nonce = bArr;
    }

    public void setTaid(String str) {
        this.taid = str;
    }

    public void setMstTransmitTime(int i) {
        this.mstTransmitTime = i;
    }

    public String toString() {
        return "SelectCardResult: status: " + this.status + "taid: " + this.taid + " nonce: " + this.nonce + " mstTransmitTime: " + this.mstTransmitTime;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeString(this.taid);
        parcel.writeByteArray(this.nonce);
        parcel.writeInt(this.mstTransmitTime);
    }
}
