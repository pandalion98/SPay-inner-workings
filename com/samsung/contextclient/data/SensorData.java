package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class SensorData implements Parcelable {
    public static final Creator<SensorData> CREATOR;
    private ArrayList<WifiSignature> wifiSignatures;

    /* renamed from: com.samsung.contextclient.data.SensorData.1 */
    static class C06131 implements Creator<SensorData> {
        C06131() {
        }

        public SensorData createFromParcel(Parcel parcel) {
            return new SensorData(parcel);
        }

        public SensorData[] newArray(int i) {
            return new SensorData[i];
        }
    }

    protected SensorData(Parcel parcel) {
        this.wifiSignatures = parcel.createTypedArrayList(WifiSignature.CREATOR);
    }

    static {
        CREATOR = new C06131();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.wifiSignatures);
    }
}
