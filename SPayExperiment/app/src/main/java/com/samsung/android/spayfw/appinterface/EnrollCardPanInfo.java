/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;

public class EnrollCardPanInfo
extends EnrollCardInfo
implements Parcelable {
    public static final Parcelable.Creator<EnrollCardPanInfo> CREATOR = new Parcelable.Creator<EnrollCardPanInfo>(){

        public EnrollCardPanInfo createFromParcel(Parcel parcel) {
            return new EnrollCardPanInfo(parcel);
        }

        public EnrollCardPanInfo[] newArray(int n2) {
            return new EnrollCardPanInfo[n2];
        }
    };
    private static final String TAG = "EnrollCardPanInfo";
    private String cvv;
    private String expMonth;
    private String expYear;
    private String pan;

    public EnrollCardPanInfo() {
        super(1);
    }

    public EnrollCardPanInfo(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    @Override
    protected void clearSensitiveData() {
        Log.d((String)TAG, (String)"clearSensitiveData: ");
        this.clearMemory(this.pan);
        this.clearMemory(this.name);
        this.clearMemory(this.cvv);
        this.clearMemory(this.expMonth);
        this.clearMemory(this.expYear);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCVV() {
        return this.cvv;
    }

    public String getExpMonth() {
        return this.expMonth;
    }

    public String getExpYear() {
        return this.expYear;
    }

    public String getPAN() {
        return this.pan;
    }

    @Override
    public void readFromParcel(Parcel parcel) {
        Log.d((String)TAG, (String)"EnrollCardPanInfo: readFromParcel");
        super.readFromParcel(parcel);
        this.pan = parcel.readString();
        this.expMonth = parcel.readString();
        this.expYear = parcel.readString();
        this.cvv = parcel.readString();
    }

    public void setCVV(String string) {
        this.cvv = string;
    }

    public void setExpMonth(String string) {
        this.expMonth = string;
    }

    public void setExpYear(String string) {
        this.expYear = string;
    }

    public void setPAN(String string) {
        this.pan = string;
    }

    @Override
    public String toString() {
        return "EnrollCardPanInfo{cvv='" + this.cvv + '\'' + ", expMonth='" + this.expMonth + '\'' + ", expYear='" + this.expYear + '\'' + ", pan='" + this.pan + '\'' + ", name='" + this.name + '\'' + "} " + super.toString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int n2) {
        Log.d((String)TAG, (String)"EnrollCardPanInfo: writeToParcel");
        super.writeToParcel(parcel, n2);
        parcel.writeString(this.pan);
        parcel.writeString(this.expMonth);
        parcel.writeString(this.expYear);
        parcel.writeString(this.cvv);
    }

}

