package com.samsung.android.hermes;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KerykeionPosition implements Parcelable {
    public static final Creator<KerykeionPosition> CREATOR = new Creator<KerykeionPosition>() {
        public KerykeionPosition createFromParcel(Parcel in) {
            KerykeionPosition data = new KerykeionPosition();
            data.readFromParcel(in);
            return data;
        }

        public KerykeionPosition[] newArray(int size) {
            return new KerykeionPosition[size];
        }
    };
    private int bottom;
    private int left;
    private int right;
    private int top;

    public KerykeionPosition(Rect position) {
        if (position != null) {
            this.left = position.left;
            this.top = position.top;
            this.right = position.right;
            this.bottom = position.bottom;
        }
    }

    public Rect getRect() {
        return new Rect(this.left, this.top, this.right, this.bottom);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.left);
        out.writeInt(this.top);
        out.writeInt(this.right);
        out.writeInt(this.bottom);
    }

    public void readFromParcel(Parcel in) {
        this.left = in.readInt();
        this.top = in.readInt();
        this.right = in.readInt();
        this.bottom = in.readInt();
    }
}
