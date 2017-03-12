package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BinderWrapper implements Parcelable {
    public static final Creator<BinderWrapper> CREATOR;
    private IBinder zzPp;

    /* renamed from: com.google.android.gms.common.internal.BinderWrapper.1 */
    static class C00911 implements Creator<BinderWrapper> {
        C00911() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return zzD(parcel);
        }

        public /* synthetic */ Object[] newArray(int i) {
            return zzaG(i);
        }

        public BinderWrapper zzD(Parcel parcel) {
            return new BinderWrapper(null);
        }

        public BinderWrapper[] zzaG(int i) {
            return new BinderWrapper[i];
        }
    }

    static {
        CREATOR = new C00911();
    }

    public BinderWrapper() {
        this.zzPp = null;
    }

    public BinderWrapper(IBinder iBinder) {
        this.zzPp = null;
        this.zzPp = iBinder;
    }

    private BinderWrapper(Parcel parcel) {
        this.zzPp = null;
        this.zzPp = parcel.readStrongBinder();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.zzPp);
    }
}
