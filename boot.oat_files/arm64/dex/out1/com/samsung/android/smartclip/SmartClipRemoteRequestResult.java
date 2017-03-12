package com.samsung.android.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipRemoteRequestResult implements Parcelable {
    public static final Creator<SmartClipRemoteRequestResult> CREATOR = new Creator<SmartClipRemoteRequestResult>() {
        public SmartClipRemoteRequestResult createFromParcel(Parcel in) {
            SmartClipRemoteRequestResult data = new SmartClipRemoteRequestResult(0, 0, null);
            data.readFromParcel(in);
            return data;
        }

        public SmartClipRemoteRequestResult[] newArray(int size) {
            return new SmartClipRemoteRequestResult[size];
        }
    };
    public int mRequestId = 0;
    public int mRequestType = 0;
    public Parcelable mResultData = null;

    public SmartClipRemoteRequestResult(int requestId, int requestType, Parcelable resultData) {
        this.mRequestId = requestId;
        this.mResultData = resultData;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mRequestId);
        out.writeInt(this.mRequestType);
        out.writeParcelable(this.mResultData, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mRequestId = in.readInt();
        this.mRequestType = in.readInt();
        this.mResultData = in.readParcelable(null);
    }
}
