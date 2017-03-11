package com.samsung.android.spayfw.appinterface;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SelectIdvResponse implements Parcelable {
    public static final Creator<SelectIdvResponse> CREATOR;
    private int codeLength;
    private long expiryTime;
    private String id;
    private Intent intent;
    private int maxRequest;
    private int maxRetry;

    /* renamed from: com.samsung.android.spayfw.appinterface.SelectIdvResponse.1 */
    static class C03851 implements Creator<SelectIdvResponse> {
        C03851() {
        }

        public SelectIdvResponse createFromParcel(Parcel parcel) {
            return new SelectIdvResponse(parcel);
        }

        public SelectIdvResponse[] newArray(int i) {
            return new SelectIdvResponse[i];
        }
    }

    static {
        CREATOR = new C03851();
    }

    public SelectIdvResponse(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.id = parcel.readString();
        this.expiryTime = parcel.readLong();
        this.codeLength = parcel.readInt();
        this.maxRetry = parcel.readInt();
        this.maxRequest = parcel.readInt();
        this.intent = (Intent) parcel.readParcelable(ClassLoader.getSystemClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeLong(this.expiryTime);
        parcel.writeInt(this.codeLength);
        parcel.writeInt(this.maxRetry);
        parcel.writeInt(this.maxRequest);
        parcel.writeParcelable(this.intent, i);
    }

    public int getCodeLength() {
        return this.codeLength;
    }

    public void setCodeLength(int i) {
        this.codeLength = i;
    }

    public long getExpirationTime() {
        return this.expiryTime;
    }

    public void setExpirationTime(long j) {
        this.expiryTime = j;
    }

    public String getMethodId() {
        return this.id;
    }

    public void setMethodId(String str) {
        this.id = str;
    }

    public int getMaxRetry() {
        return this.maxRetry;
    }

    public void setMaxRetry(int i) {
        this.maxRetry = i;
    }

    public int getMaxRequest() {
        return this.maxRequest;
    }

    public void setMaxRequest(int i) {
        this.maxRequest = i;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String toString() {
        return "SelectIdvResponse: methodId: " + this.id + " expiryTime: " + this.expiryTime + " codeLength: " + this.codeLength + " maxRetry: " + this.maxRetry + "maxRequest :" + this.maxRequest;
    }
}
