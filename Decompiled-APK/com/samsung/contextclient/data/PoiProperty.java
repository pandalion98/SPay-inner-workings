package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PoiProperty implements Parcelable {
    public static final Creator<PoiProperty> CREATOR;
    private MstSequence sequences;

    /* renamed from: com.samsung.contextclient.data.PoiProperty.1 */
    static class C06091 implements Creator<PoiProperty> {
        C06091() {
        }

        public PoiProperty createFromParcel(Parcel parcel) {
            return new PoiProperty(parcel);
        }

        public PoiProperty[] newArray(int i) {
            return new PoiProperty[i];
        }
    }

    protected PoiProperty(Parcel parcel) {
        this.sequences = (MstSequence) parcel.readParcelable(MstSequence.class.getClassLoader());
    }

    static {
        CREATOR = new C06091();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.sequences, i);
    }
}
