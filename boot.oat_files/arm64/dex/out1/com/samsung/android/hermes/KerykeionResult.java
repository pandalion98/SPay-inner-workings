package com.samsung.android.hermes;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KerykeionResult implements Parcelable {
    public static final Creator<KerykeionResult> CREATOR = new Creator<KerykeionResult>() {
        public KerykeionResult createFromParcel(Parcel in) {
            KerykeionResult data = new KerykeionResult();
            data.readFromParcel(in);
            return data;
        }

        public KerykeionResult[] newArray(int size) {
            return new KerykeionResult[size];
        }
    };
    private static final int HIDE_HERMES_UI = 2;
    private static final int SUPPORT_HERMES_UI = 1;
    private static final int USE_EXTRA = 2;
    private static final int USE_RESULT = 1;
    private float mAccuracy;
    private int mEndPos;
    private Object mExtraDatas;
    private Object mResult;
    private int mResultType;
    private String mSrc;
    private int mStartPos;
    private int mUIState;
    private int mUsingData;

    public KerykeionResult(int mResultType, String mSrc, Object mResult, Object mExtraDatas, int mStartPos, int mEndPos, float mAccuracy) {
        this.mResultType = mResultType;
        this.mSrc = mSrc;
        this.mResult = mResult;
        this.mExtraDatas = mExtraDatas;
        this.mStartPos = mStartPos;
        this.mEndPos = mEndPos;
        this.mAccuracy = mAccuracy;
    }

    public KerykeionResult(int mResultType, String mSrc, Object mResult, Object mExtraDatas, int mStartPos, int mEndPos, float mAccuracy, int mUIState, int mUsingData) {
        this.mResultType = mResultType;
        this.mSrc = mSrc;
        this.mResult = mResult;
        this.mExtraDatas = mExtraDatas;
        this.mStartPos = mStartPos;
        this.mEndPos = mEndPos;
        this.mAccuracy = mAccuracy;
        this.mUIState = mUIState;
        this.mUsingData = mUsingData;
    }

    public int getResultType() {
        return this.mResultType;
    }

    public String getSrc() {
        return this.mSrc;
    }

    public Object getResult() {
        return this.mResult;
    }

    public Object getExtraDatas() {
        return this.mExtraDatas;
    }

    public int getStartPos() {
        return this.mStartPos;
    }

    public int getEndPos() {
        return this.mEndPos;
    }

    public float getAccuracy() {
        return this.mAccuracy;
    }

    public Object getAdaptableData() {
        if (this.mUsingData == 2) {
            return this.mExtraDatas;
        }
        return this.mResult;
    }

    public boolean isPossibleToShow() {
        if (this.mUIState == 1) {
            return true;
        }
        return false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mResultType);
        out.writeString(this.mSrc);
        out.writeValue(this.mResult);
        out.writeValue(this.mExtraDatas);
        out.writeInt(this.mStartPos);
        out.writeInt(this.mEndPos);
        out.writeFloat(this.mAccuracy);
        out.writeInt(this.mUIState);
        out.writeInt(this.mUsingData);
    }

    public void readFromParcel(Parcel in) {
        this.mResultType = in.readInt();
        this.mSrc = in.readString();
        this.mResult = in.readValue(Object.class.getClassLoader());
        this.mExtraDatas = in.readValue(Object.class.getClassLoader());
        this.mStartPos = in.readInt();
        this.mEndPos = in.readInt();
        this.mAccuracy = in.readFloat();
        this.mUIState = in.readInt();
        this.mUsingData = in.readInt();
    }
}
