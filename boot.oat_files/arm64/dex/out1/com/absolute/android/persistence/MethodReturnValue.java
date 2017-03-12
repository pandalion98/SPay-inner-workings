package com.absolute.android.persistence;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MethodReturnValue implements Parcelable {
    public static final Creator<MethodReturnValue> CREATOR = new Creator<MethodReturnValue>() {
        public MethodReturnValue createFromParcel(Parcel in) {
            return new MethodReturnValue(in);
        }

        public MethodReturnValue[] newArray(int size) {
            return new MethodReturnValue[size];
        }
    };
    private String m_apkOrJarPath;
    private Object m_returnValue;

    public MethodReturnValue(Object returnValue, String apkOrJarPath) {
        this.m_returnValue = returnValue;
        this.m_apkOrJarPath = apkOrJarPath;
    }

    public Object getReturnValue() {
        return this.m_returnValue;
    }

    public String getApkOrJarPath() {
        return this.m_apkOrJarPath;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.m_apkOrJarPath);
        dest.writeValue(this.m_returnValue);
    }

    private MethodReturnValue(Parcel source) {
        this.m_apkOrJarPath = source.readString();
        if (this.m_apkOrJarPath == null) {
            this.m_returnValue = source.readValue(null);
            return;
        }
        throw new IllegalArgumentException("MethodReturnValue apkOrJarPath is not null");
    }

    private MethodReturnValue() {
    }
}
