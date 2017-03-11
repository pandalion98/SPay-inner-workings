package com.samsung.android.spayfw.appinterface;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VerifyIdvInfo implements Parcelable {
    public static final Creator<VerifyIdvInfo> CREATOR;
    private String id;
    private Intent intent;
    private String type;
    private String value;

    /* renamed from: com.samsung.android.spayfw.appinterface.VerifyIdvInfo.1 */
    static class C04011 implements Creator<VerifyIdvInfo> {
        C04011() {
        }

        public VerifyIdvInfo createFromParcel(Parcel parcel) {
            return new VerifyIdvInfo(parcel);
        }

        public VerifyIdvInfo[] newArray(int i) {
            return new VerifyIdvInfo[i];
        }
    }

    static {
        CREATOR = new C04011();
    }

    public VerifyIdvInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.id = parcel.readString();
        this.type = parcel.readString();
        this.value = parcel.readString();
        this.intent = (Intent) parcel.readParcelable(ClassLoader.getSystemClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.type);
        parcel.writeString(this.value);
        parcel.writeParcelable(this.intent, i);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public String toString() {
        return "VerifyIdvInfo: id: " + this.id + " type: " + this.type + " value: " + this.value;
    }
}
