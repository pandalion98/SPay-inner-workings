package com.samsung.android.hermes.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class HermesObject implements Parcelable {
    public static final Creator<HermesObject> CREATOR = new Creator<HermesObject>() {
        public HermesObject createFromParcel(Parcel in) {
            HermesObject data = new HermesObject();
            data.readFromParcel(in);
            return data;
        }

        public HermesObject[] newArray(int size) {
            return new HermesObject[size];
        }
    };
    private Object obj = null;

    public int describeContents() {
        return 0;
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }

    public Object getObject() {
        return this.obj;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable((HermesObject) this.obj, flags);
    }

    public void readFromParcel(Parcel in) {
        this.obj = in.readParcelable(HermesObject.class.getClassLoader());
    }
}
