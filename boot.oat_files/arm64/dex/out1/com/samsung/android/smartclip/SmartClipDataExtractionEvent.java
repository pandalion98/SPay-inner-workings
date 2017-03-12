package com.samsung.android.smartclip;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmartClipDataExtractionEvent implements Parcelable {
    public static final Creator<SmartClipDataExtractionEvent> CREATOR = new Creator<SmartClipDataExtractionEvent>() {
        public SmartClipDataExtractionEvent createFromParcel(Parcel in) {
            SmartClipDataExtractionEvent data = new SmartClipDataExtractionEvent();
            data.readFromParcel(in);
            return data;
        }

        public SmartClipDataExtractionEvent[] newArray(int size) {
            return new SmartClipDataExtractionEvent[size];
        }
    };
    public static final int EXTRACTION_MODE_DRAG_AND_DROP = 2;
    public static final int EXTRACTION_MODE_FULL_SCREEN = 1;
    public static final int EXTRACTION_MODE_NORMAL = 0;
    public Rect mCropRect;
    public int mExtractionMode;
    public int mRequestId;
    public int mTargetWindowLayer;

    public SmartClipDataExtractionEvent() {
        this.mRequestId = 0;
        this.mExtractionMode = 0;
        this.mCropRect = new Rect();
        this.mTargetWindowLayer = -1;
    }

    public SmartClipDataExtractionEvent(int requestId, Rect cropRect) {
        this.mRequestId = 0;
        this.mExtractionMode = 0;
        this.mCropRect = new Rect();
        this.mTargetWindowLayer = -1;
        this.mRequestId = requestId;
        this.mCropRect = cropRect;
    }

    public SmartClipDataExtractionEvent(int requestId, Rect cropRect, int extractionMode) {
        this(requestId, cropRect);
        this.mExtractionMode = extractionMode;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mRequestId);
        out.writeInt(this.mExtractionMode);
        out.writeInt(this.mTargetWindowLayer);
        out.writeParcelable(this.mCropRect, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mRequestId = in.readInt();
        this.mExtractionMode = in.readInt();
        this.mTargetWindowLayer = in.readInt();
        this.mCropRect = (Rect) in.readParcelable(Rect.class.getClassLoader());
    }
}
