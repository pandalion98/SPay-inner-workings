package com.samsung.android.multidisplay.virtualscreen;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VirtualScreenLaunchParams implements Parcelable {
    public static final Creator<VirtualScreenLaunchParams> CREATOR = new Creator<VirtualScreenLaunchParams>() {
        public VirtualScreenLaunchParams createFromParcel(Parcel in) {
            return new VirtualScreenLaunchParams(in);
        }

        public VirtualScreenLaunchParams[] newArray(int size) {
            return new VirtualScreenLaunchParams[size];
        }
    };
    public static final int FLAG_BASE_ACTIVITY = 1;
    public static final int FLAG_CLEAR_TASKS = 2;
    public static final int FLAG_NO_ANIMATION = 4;
    public static final int FLAG_RECREATE_VIRTUALSCREEN = 8;
    public Rect mBounds;
    public int mDisplayId;
    public int mFlags;

    public VirtualScreenLaunchParams() {
        this.mBounds = null;
        this.mDisplayId = -1;
        this.mBounds = null;
        this.mFlags = 0;
    }

    public VirtualScreenLaunchParams(Parcel in) {
        this.mBounds = null;
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mDisplayId);
        out.writeParcelable(this.mBounds, 0);
        out.writeInt(this.mFlags);
    }

    public void readFromParcel(Parcel in) {
        this.mDisplayId = in.readInt();
        this.mBounds = (Rect) in.readParcelable(getClass().getClassLoader());
        this.mFlags = in.readInt();
    }

    public String toString() {
        StringBuilder b = new StringBuilder(128);
        b.append("Params{");
        b.append("mDisplayId=").append(this.mDisplayId);
        b.append(", ").append(this.mBounds);
        b.append(", mFlags=0x").append(String.format("%08x", new Object[]{Integer.valueOf(this.mFlags)}));
        b.append(")}");
        return b.toString();
    }
}
