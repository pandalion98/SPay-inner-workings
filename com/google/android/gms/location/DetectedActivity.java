package com.google.android.gms.location;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Comparator;

public class DetectedActivity implements SafeParcelable {
    public static final DetectedActivityCreator CREATOR;
    public static final int IN_VEHICLE = 0;
    public static final int ON_BICYCLE = 1;
    public static final int ON_FOOT = 2;
    public static final int RUNNING = 8;
    public static final int STILL = 3;
    public static final int TILTING = 5;
    public static final int UNKNOWN = 4;
    public static final int WALKING = 7;
    public static final Comparator<DetectedActivity> zzalK;
    private final int zzFG;
    int zzalL;
    int zzalM;

    /* renamed from: com.google.android.gms.location.DetectedActivity.1 */
    static class C02361 implements Comparator<DetectedActivity> {
        C02361() {
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return zza((DetectedActivity) obj, (DetectedActivity) obj2);
        }

        public int zza(DetectedActivity detectedActivity, DetectedActivity detectedActivity2) {
            int compareTo = Integer.valueOf(detectedActivity2.getConfidence()).compareTo(Integer.valueOf(detectedActivity.getConfidence()));
            return compareTo == 0 ? Integer.valueOf(detectedActivity.getType()).compareTo(Integer.valueOf(detectedActivity2.getType())) : compareTo;
        }
    }

    static {
        zzalK = new C02361();
        CREATOR = new DetectedActivityCreator();
    }

    public DetectedActivity(int i, int i2) {
        this.zzFG = ON_BICYCLE;
        this.zzalL = i;
        this.zzalM = i2;
    }

    public DetectedActivity(int i, int i2, int i3) {
        this.zzFG = i;
        this.zzalL = i2;
        this.zzalM = i3;
    }

    private int zzff(int i) {
        return i > 9 ? UNKNOWN : i;
    }

    public int describeContents() {
        return IN_VEHICLE;
    }

    public int getConfidence() {
        return this.zzalM;
    }

    public int getType() {
        return zzff(this.zzalL);
    }

    public int getVersionCode() {
        return this.zzFG;
    }

    public String toString() {
        return "DetectedActivity [type=" + getType() + ", confidence=" + this.zzalM + "]";
    }

    public void writeToParcel(Parcel parcel, int i) {
        DetectedActivityCreator.zza(this, parcel, i);
    }
}
