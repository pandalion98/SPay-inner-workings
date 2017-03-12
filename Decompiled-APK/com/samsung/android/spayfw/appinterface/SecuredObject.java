package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SecuredObject implements Parcelable {
    public static final Creator<SecuredObject> CREATOR;
    private byte[] secObj;

    /* renamed from: com.samsung.android.spayfw.appinterface.SecuredObject.1 */
    static class C03821 implements Creator<SecuredObject> {
        C03821() {
        }

        public SecuredObject createFromParcel(Parcel parcel) {
            return new SecuredObject(null);
        }

        public SecuredObject[] newArray(int i) {
            return new SecuredObject[i];
        }
    }

    public SecuredObject(byte[] bArr) {
        this.secObj = bArr;
    }

    public byte[] getSecureObjectData() {
        return this.secObj;
    }

    public void setSecureObjectData(byte[] bArr) {
        this.secObj = bArr;
    }

    private SecuredObject(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.secObj = parcel.createByteArray();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.secObj);
    }

    static {
        CREATOR = new C03821();
    }
}
