package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;

public class PayConfig implements Parcelable {
    public static final Creator<PayConfig> CREATOR;
    private MstPayConfig mstPayConfig;
    private int mstTransmitTime;
    private int payIdleTime;
    private int payType;

    /* renamed from: com.samsung.android.spayfw.appinterface.PayConfig.1 */
    static class C03731 implements Creator<PayConfig> {
        C03731() {
        }

        public PayConfig createFromParcel(Parcel parcel) {
            return new PayConfig(null);
        }

        public PayConfig[] newArray(int i) {
            return new PayConfig[i];
        }
    }

    static {
        CREATOR = new C03731();
    }

    private PayConfig(Parcel parcel) {
        this.payIdleTime = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        readFromParcel(parcel);
    }

    public PayConfig() {
        this.payIdleTime = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
    }

    public int describeContents() {
        return 0;
    }

    public int getPayType() {
        return this.payType;
    }

    public int getPayIdleTime() {
        return this.payIdleTime;
    }

    public MstPayConfig getMstPayConfig() {
        return this.mstPayConfig;
    }

    public int getMstTransmitTime() {
        return this.mstTransmitTime;
    }

    public void readFromParcel(Parcel parcel) {
        this.payType = parcel.readInt();
        this.payIdleTime = parcel.readInt();
        this.mstPayConfig = (MstPayConfig) parcel.readParcelable(getClass().getClassLoader());
        this.mstTransmitTime = parcel.readInt();
    }

    public void setPayType(int i) {
        this.payType = i;
    }

    public void setPayIdleTime(int i) {
        this.payIdleTime = i;
    }

    public void setMstPayConfig(MstPayConfig mstPayConfig) {
        this.mstPayConfig = mstPayConfig;
    }

    public void setMstTransmitTime(int i) {
        this.mstTransmitTime = i;
    }

    public String toString() {
        String str = "PayConfig:  payType: " + this.payType + " payIdleTime: " + this.payIdleTime + " mstTransmitTime: " + this.mstTransmitTime;
        if (this.mstPayConfig != null) {
            return str + this.mstPayConfig.toString();
        }
        return str + " = mstPayConfig: null";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.payType);
        parcel.writeInt(this.payIdleTime);
        parcel.writeParcelable(this.mstPayConfig, i);
        parcel.writeInt(this.mstTransmitTime);
    }
}
