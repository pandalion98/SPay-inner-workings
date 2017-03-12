package com.samsung.android.hermes.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class HermesHtml extends HermesObject implements Parcelable {
    public static final Creator<HermesHtml> CREATOR = new Creator<HermesHtml>() {
        public HermesHtml createFromParcel(Parcel in) {
            HermesHtml data = new HermesHtml();
            data.readFromParcel(in);
            return data;
        }

        public HermesHtml[] newArray(int size) {
            return new HermesHtml[size];
        }
    };
    private String mHtml = null;
    private String mText = null;

    public int describeContents() {
        return 0;
    }

    public String getText() {
        return this.mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getHtml() {
        return this.mHtml;
    }

    public void setHtml(String mHtml) {
        this.mHtml = mHtml;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mText);
        out.writeString(this.mHtml);
    }

    public void readFromParcel(Parcel in) {
        this.mText = in.readString();
        this.mHtml = in.readString();
    }
}
