/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.MstSequence;

public class PoiProperty
implements Parcelable {
    public static final Parcelable.Creator<PoiProperty> CREATOR = new Parcelable.Creator<PoiProperty>(){

        public PoiProperty createFromParcel(Parcel parcel) {
            return new PoiProperty(parcel);
        }

        public PoiProperty[] newArray(int n2) {
            return new PoiProperty[n2];
        }
    };
    private MstSequence sequences;

    protected PoiProperty(Parcel parcel) {
        this.sequences = (MstSequence)parcel.readParcelable(MstSequence.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeParcelable((Parcelable)this.sequences, n2);
    }

}

