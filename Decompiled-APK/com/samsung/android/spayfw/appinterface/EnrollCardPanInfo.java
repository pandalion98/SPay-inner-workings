package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class EnrollCardPanInfo extends EnrollCardInfo implements Parcelable {
    public static final Creator<EnrollCardPanInfo> CREATOR;
    private static final String TAG = "EnrollCardPanInfo";
    private String cvv;
    private String expMonth;
    private String expYear;
    private String pan;

    /* renamed from: com.samsung.android.spayfw.appinterface.EnrollCardPanInfo.1 */
    static class C03461 implements Creator<EnrollCardPanInfo> {
        C03461() {
        }

        public EnrollCardPanInfo createFromParcel(Parcel parcel) {
            return new EnrollCardPanInfo(parcel);
        }

        public EnrollCardPanInfo[] newArray(int i) {
            return new EnrollCardPanInfo[i];
        }
    }

    static {
        CREATOR = new C03461();
    }

    public EnrollCardPanInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    public EnrollCardPanInfo() {
        super(1);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Log.d(TAG, "EnrollCardPanInfo: writeToParcel");
        super.writeToParcel(parcel, i);
        parcel.writeString(this.pan);
        parcel.writeString(this.expMonth);
        parcel.writeString(this.expYear);
        parcel.writeString(this.cvv);
    }

    public void readFromParcel(Parcel parcel) {
        Log.d(TAG, "EnrollCardPanInfo: readFromParcel");
        super.readFromParcel(parcel);
        this.pan = parcel.readString();
        this.expMonth = parcel.readString();
        this.expYear = parcel.readString();
        this.cvv = parcel.readString();
    }

    public String toString() {
        return "EnrollCardPanInfo{cvv='" + this.cvv + '\'' + ", expMonth='" + this.expMonth + '\'' + ", expYear='" + this.expYear + '\'' + ", pan='" + this.pan + '\'' + ", name='" + this.name + '\'' + "} " + super.toString();
    }

    protected void clearSensitiveData() {
        Log.d(TAG, "clearSensitiveData: ");
        clearMemory(this.pan);
        clearMemory(this.name);
        clearMemory(this.cvv);
        clearMemory(this.expMonth);
        clearMemory(this.expYear);
    }

    public String getCVV() {
        return this.cvv;
    }

    public void setCVV(String str) {
        this.cvv = str;
    }

    public String getExpMonth() {
        return this.expMonth;
    }

    public void setExpMonth(String str) {
        this.expMonth = str;
    }

    public String getExpYear() {
        return this.expYear;
    }

    public void setExpYear(String str) {
        this.expYear = str;
    }

    public String getPAN() {
        return this.pan;
    }

    public void setPAN(String str) {
        this.pan = str;
    }
}
