package com.samsung.android.multiwindow;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MultiWindowTab implements Parcelable {
    public static final Creator<MultiWindowTab> CREATOR = new Creator<MultiWindowTab>() {
        public MultiWindowTab createFromParcel(Parcel in) {
            return new MultiWindowTab(in);
        }

        public MultiWindowTab[] newArray(int size) {
            return new MultiWindowTab[size];
        }
    };
    private ComponentName mComponentName;
    private int mStackId;

    public MultiWindowTab(int stackId, ComponentName componentName) {
        this.mStackId = stackId;
        this.mComponentName = componentName;
    }

    public MultiWindowTab(Parcel parcelledData) {
        readFromParcel(parcelledData);
    }

    public MultiWindowTab(MultiWindowTab style) {
        setTo(style);
    }

    public void setTo(MultiWindowTab other) {
        this.mStackId = other.mStackId;
        this.mComponentName = other.mComponentName;
    }

    public void setStackId(int stackId) {
        this.mStackId = stackId;
    }

    public int getStackId() {
        return this.mStackId;
    }

    public void setComponentName(ComponentName componentName) {
        this.mComponentName = componentName;
    }

    public ComponentName getComponentName() {
        return this.mComponentName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStackId);
        if (this.mComponentName != null) {
            dest.writeInt(1);
            this.mComponentName.writeToParcel(dest, flags);
            return;
        }
        dest.writeInt(0);
    }

    private void readFromParcel(Parcel parcelledData) {
        setStackId(parcelledData.readInt());
        if (parcelledData.readInt() != 0) {
            this.mComponentName = (ComponentName) ComponentName.CREATOR.createFromParcel(parcelledData);
        }
    }

    public String toString() {
        StringBuilder out = new StringBuilder(128);
        out.append(getClass().getSimpleName());
        out.append("{stackId=");
        out.append(this.mStackId);
        out.append(",bounds=");
        out.append(this.mComponentName.toString());
        out.append(",min_bound{port=(");
        out.append('}');
        return out.toString();
    }
}
