/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;

public class SecuredObject
implements Parcelable {
    public static final Parcelable.Creator<SecuredObject> CREATOR = new Parcelable.Creator<SecuredObject>(){

        public SecuredObject createFromParcel(Parcel parcel) {
            return new SecuredObject(parcel);
        }

        public SecuredObject[] newArray(int n2) {
            return new SecuredObject[n2];
        }
    };
    private byte[] secObj;

    public SecuredObject() {
    }

    private SecuredObject(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public SecuredObject(byte[] arrby) {
        this.secObj = arrby;
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getSecureObjectData() {
        return this.secObj;
    }

    public void readFromParcel(Parcel parcel) {
        this.secObj = parcel.createByteArray();
    }

    public void setSecureObjectData(byte[] arrby) {
        this.secObj = arrby;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeByteArray(this.secObj);
    }

}

