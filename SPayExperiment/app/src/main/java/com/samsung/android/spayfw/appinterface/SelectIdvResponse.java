/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class SelectIdvResponse
implements Parcelable {
    public static final Parcelable.Creator<SelectIdvResponse> CREATOR = new Parcelable.Creator<SelectIdvResponse>(){

        public SelectIdvResponse createFromParcel(Parcel parcel) {
            return new SelectIdvResponse(parcel);
        }

        public SelectIdvResponse[] newArray(int n2) {
            return new SelectIdvResponse[n2];
        }
    };
    private int codeLength;
    private long expiryTime;
    private String id;
    private Intent intent;
    private int maxRequest;
    private int maxRetry;

    public SelectIdvResponse() {
    }

    public SelectIdvResponse(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.id = parcel.readString();
        this.expiryTime = parcel.readLong();
        this.codeLength = parcel.readInt();
        this.maxRetry = parcel.readInt();
        this.maxRequest = parcel.readInt();
        this.intent = (Intent)parcel.readParcelable(ClassLoader.getSystemClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public int getCodeLength() {
        return this.codeLength;
    }

    public long getExpirationTime() {
        return this.expiryTime;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public int getMaxRequest() {
        return this.maxRequest;
    }

    public int getMaxRetry() {
        return this.maxRetry;
    }

    public String getMethodId() {
        return this.id;
    }

    public void setCodeLength(int n2) {
        this.codeLength = n2;
    }

    public void setExpirationTime(long l2) {
        this.expiryTime = l2;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setMaxRequest(int n2) {
        this.maxRequest = n2;
    }

    public void setMaxRetry(int n2) {
        this.maxRetry = n2;
    }

    public void setMethodId(String string) {
        this.id = string;
    }

    public String toString() {
        return "SelectIdvResponse: methodId: " + this.id + " expiryTime: " + this.expiryTime + " codeLength: " + this.codeLength + " maxRetry: " + this.maxRetry + "maxRequest :" + this.maxRequest;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.id);
        parcel.writeLong(this.expiryTime);
        parcel.writeInt(this.codeLength);
        parcel.writeInt(this.maxRetry);
        parcel.writeInt(this.maxRequest);
        parcel.writeParcelable((Parcelable)this.intent, n2);
    }

}

