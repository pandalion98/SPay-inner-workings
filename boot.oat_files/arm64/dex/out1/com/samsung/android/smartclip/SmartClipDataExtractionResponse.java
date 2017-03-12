package com.samsung.android.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipDataExtractionResponse implements Parcelable {
    public static final Creator<SmartClipDataExtractionResponse> CREATOR = new Creator<SmartClipDataExtractionResponse>() {
        public SmartClipDataExtractionResponse createFromParcel(Parcel in) {
            SmartClipDataExtractionResponse data = new SmartClipDataExtractionResponse(0, 0, null);
            data.readFromParcel(in);
            return data;
        }

        public SmartClipDataExtractionResponse[] newArray(int size) {
            return new SmartClipDataExtractionResponse[size];
        }
    };
    public int mExtractionMode = 0;
    public SmartClipDataRepositoryImpl mRepository = null;
    public int mRequestId = 0;

    public SmartClipDataExtractionResponse(int requestId, int extractionMode, SmartClipDataRepositoryImpl repository) {
        this.mRequestId = requestId;
        this.mExtractionMode = extractionMode;
        this.mRepository = repository;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mRequestId);
        out.writeInt(this.mExtractionMode);
        out.writeParcelable(this.mRepository, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mRequestId = in.readInt();
        this.mExtractionMode = in.readInt();
        this.mRepository = (SmartClipDataRepositoryImpl) in.readParcelable(SmartClipDataRepositoryImpl.class.getClassLoader());
    }
}
