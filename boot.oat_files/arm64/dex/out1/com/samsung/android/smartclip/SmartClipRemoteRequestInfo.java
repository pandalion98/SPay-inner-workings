package com.samsung.android.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipRemoteRequestInfo implements Parcelable {
    public static final Creator<SmartClipRemoteRequestInfo> CREATOR = new Creator<SmartClipRemoteRequestInfo>() {
        public SmartClipRemoteRequestInfo createFromParcel(Parcel in) {
            SmartClipRemoteRequestInfo data = new SmartClipRemoteRequestInfo();
            data.readFromParcel(in);
            return data;
        }

        public SmartClipRemoteRequestInfo[] newArray(int size) {
            return new SmartClipRemoteRequestInfo[size];
        }
    };
    public static final int REQUEST_TYPE_AIR_BUTTON_HIT_TEST = 2;
    public static final int REQUEST_TYPE_INJECT_INPUT_EVENT = 3;
    public static final int REQUEST_TYPE_INVALID = 0;
    public static final int REQUEST_TYPE_SCROLLABLE_AREA_INFO = 4;
    public static final int REQUEST_TYPE_SCROLLABLE_VIEW_INFO = 5;
    public static final int REQUEST_TYPE_SMART_CLIP_META_EXTRACTION = 1;
    public int mCallerPid = 0;
    public int mCallerUid = 0;
    public Parcelable mRequestData;
    public int mRequestId = 0;
    public int mRequestType = 0;
    public int mTargetWindowLayer = -1;

    public SmartClipRemoteRequestInfo(int requestId, int requestType, Parcelable requestData) {
        this.mRequestId = requestId;
        this.mRequestType = requestType;
        this.mRequestData = requestData;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCallerPid);
        out.writeInt(this.mCallerUid);
        out.writeInt(this.mRequestId);
        out.writeInt(this.mRequestType);
        out.writeParcelable(this.mRequestData, flags);
        out.writeInt(this.mTargetWindowLayer);
    }

    public void readFromParcel(Parcel in) {
        this.mCallerPid = in.readInt();
        this.mCallerUid = in.readInt();
        this.mRequestId = in.readInt();
        this.mRequestType = in.readInt();
        this.mRequestData = in.readParcelable(null);
        this.mTargetWindowLayer = in.readInt();
    }
}
