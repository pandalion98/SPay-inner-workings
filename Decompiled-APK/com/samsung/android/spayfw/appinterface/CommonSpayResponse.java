package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CommonSpayResponse implements Parcelable {
    public static final Creator<CommonSpayResponse> CREATOR;
    private Bundle dataBundle;
    private byte[] dataByte;
    private int status;

    /* renamed from: com.samsung.android.spayfw.appinterface.CommonSpayResponse.1 */
    static class C03401 implements Creator<CommonSpayResponse> {
        C03401() {
        }

        public CommonSpayResponse[] newArray(int i) {
            return new CommonSpayResponse[i];
        }

        public CommonSpayResponse createFromParcel(Parcel parcel) {
            return new CommonSpayResponse(null);
        }
    }

    static {
        CREATOR = new C03401();
    }

    private CommonSpayResponse(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public byte[] getDataByte() {
        return this.dataByte;
    }

    public void setData(byte[] bArr) {
        this.dataByte = bArr;
    }

    public Bundle getDataBundle() {
        return this.dataBundle;
    }

    public void setData(Bundle bundle) {
        this.dataBundle = bundle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.status);
        parcel.writeByteArray(this.dataByte);
        parcel.writeBundle(this.dataBundle);
    }

    private void readFromParcel(Parcel parcel) {
        this.status = parcel.readInt();
        this.dataByte = parcel.createByteArray();
        this.dataBundle = parcel.readBundle();
    }
}
