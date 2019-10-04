/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.WifiSignature;
import java.util.ArrayList;
import java.util.List;

public class SensorData
implements Parcelable {
    public static final Parcelable.Creator<SensorData> CREATOR = new Parcelable.Creator<SensorData>(){

        public SensorData createFromParcel(Parcel parcel) {
            return new SensorData(parcel);
        }

        public SensorData[] newArray(int n2) {
            return new SensorData[n2];
        }
    };
    private ArrayList<WifiSignature> wifiSignatures;

    protected SensorData(Parcel parcel) {
        this.wifiSignatures = parcel.createTypedArrayList(WifiSignature.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeTypedList(this.wifiSignatures);
    }

}

