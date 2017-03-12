package android.hardware.camera2.utils;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BinderHolder implements Parcelable {
    public static final Creator<BinderHolder> CREATOR = new Creator<BinderHolder>() {
        public BinderHolder createFromParcel(Parcel in) {
            return new BinderHolder(in);
        }

        public BinderHolder[] newArray(int size) {
            return new BinderHolder[size];
        }
    };
    private IBinder mBinder;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(this.mBinder);
    }

    public void readFromParcel(Parcel src) {
        this.mBinder = src.readStrongBinder();
    }

    public IBinder getBinder() {
        return this.mBinder;
    }

    public void setBinder(IBinder binder) {
        this.mBinder = binder;
    }

    public BinderHolder() {
        this.mBinder = null;
    }

    public BinderHolder(IBinder binder) {
        this.mBinder = null;
        this.mBinder = binder;
    }

    private BinderHolder(Parcel in) {
        this.mBinder = null;
        this.mBinder = in.readStrongBinder();
    }
}
