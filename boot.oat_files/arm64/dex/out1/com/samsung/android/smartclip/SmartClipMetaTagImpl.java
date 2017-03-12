package com.samsung.android.smartclip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;

public class SmartClipMetaTagImpl extends SlookSmartClipMetaTag implements Parcelable {
    public static final Creator<SmartClipMetaTagImpl> CREATOR = new Creator<SmartClipMetaTagImpl>() {
        public SmartClipMetaTagImpl createFromParcel(Parcel in) {
            Log.d(SmartClipMetaTagImpl.TAG, "SmartClipMetaTagImpl.createFromParcel called");
            SmartClipMetaTagImpl data = new SmartClipMetaTagImpl(null, null);
            data.readFromParcel(in);
            return data;
        }

        public SmartClipMetaTagImpl[] newArray(int size) {
            return new SmartClipMetaTagImpl[size];
        }
    };
    public static final String TAG = "SmartClipMetaTagImpl";
    protected byte[] mExtraData = null;
    protected Parcelable mParcelableData = null;

    public byte[] getExtraData() {
        return this.mExtraData;
    }

    public Parcelable getParcelableData() {
        return this.mParcelableData;
    }

    public SmartClipMetaTagImpl(String tagType, String value) {
        super(tagType, value);
    }

    public SmartClipMetaTagImpl(String tagType, String value, byte[] extraData) {
        super(tagType, value);
        this.mExtraData = extraData;
    }

    public SmartClipMetaTagImpl(String tagType, String value, Parcelable parcelableData) {
        super(tagType, value);
        this.mParcelableData = parcelableData;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getType());
        out.writeString(getValue());
        if (this.mExtraData != null) {
            out.writeInt(this.mExtraData.length);
            out.writeByteArray(this.mExtraData);
        } else {
            out.writeInt(0);
        }
        if (this.mParcelableData != null) {
            out.writeInt(1);
            out.writeParcelable(this.mParcelableData, 0);
            return;
        }
        out.writeInt(0);
    }

    public void readFromParcel(Parcel in) {
        String type = in.readString();
        String value = in.readString();
        setType(type);
        setValue(value);
        int extraDataLen = in.readInt();
        if (extraDataLen > 0) {
            this.mExtraData = new byte[extraDataLen];
            in.readByteArray(this.mExtraData);
        } else {
            this.mExtraData = null;
        }
        if (in.readInt() != 0) {
            this.mParcelableData = in.readParcelable(null);
        } else {
            this.mParcelableData = null;
        }
    }
}
