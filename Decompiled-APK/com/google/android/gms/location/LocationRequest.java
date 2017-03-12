package com.google.android.gms.location;

import android.os.Parcel;
import android.os.SystemClock;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.samsung.android.spayfw.cncc.CNCCCommands;

public final class LocationRequest implements SafeParcelable {
    public static final zzg CREATOR;
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    int mPriority;
    private final int zzFG;
    boolean zzabz;
    long zzalO;
    long zzamf;
    long zzamg;
    int zzamh;
    float zzami;
    long zzamj;

    static {
        CREATOR = new zzg();
    }

    public LocationRequest() {
        this.zzFG = 1;
        this.mPriority = PRIORITY_BALANCED_POWER_ACCURACY;
        this.zzamf = 3600000;
        this.zzamg = 600000;
        this.zzabz = false;
        this.zzalO = Long.MAX_VALUE;
        this.zzamh = CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        this.zzami = 0.0f;
        this.zzamj = 0;
    }

    LocationRequest(int i, int i2, long j, long j2, boolean z, long j3, int i3, float f, long j4) {
        this.zzFG = i;
        this.mPriority = i2;
        this.zzamf = j;
        this.zzamg = j2;
        this.zzabz = z;
        this.zzalO = j3;
        this.zzamh = i3;
        this.zzami = f;
        this.zzamj = j4;
    }

    public static LocationRequest create() {
        return new LocationRequest();
    }

    private static void zzA(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("invalid interval: " + j);
        }
    }

    private static void zza(float f) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("invalid displacement: " + f);
        }
    }

    private static void zzfj(int i) {
        switch (i) {
            case PRIORITY_HIGH_ACCURACY /*100*/:
            case PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
            case PRIORITY_LOW_POWER /*104*/:
            case PRIORITY_NO_POWER /*105*/:
            default:
                throw new IllegalArgumentException("invalid quality: " + i);
        }
    }

    public static String zzfk(int i) {
        switch (i) {
            case PRIORITY_HIGH_ACCURACY /*100*/:
                return "PRIORITY_HIGH_ACCURACY";
            case PRIORITY_BALANCED_POWER_ACCURACY /*102*/:
                return "PRIORITY_BALANCED_POWER_ACCURACY";
            case PRIORITY_LOW_POWER /*104*/:
                return "PRIORITY_LOW_POWER";
            case PRIORITY_NO_POWER /*105*/:
                return "PRIORITY_NO_POWER";
            default:
                return "???";
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocationRequest)) {
            return false;
        }
        LocationRequest locationRequest = (LocationRequest) obj;
        return this.mPriority == locationRequest.mPriority && this.zzamf == locationRequest.zzamf && this.zzamg == locationRequest.zzamg && this.zzabz == locationRequest.zzabz && this.zzalO == locationRequest.zzalO && this.zzamh == locationRequest.zzamh && this.zzami == locationRequest.zzami;
    }

    public long getExpirationTime() {
        return this.zzalO;
    }

    public long getFastestInterval() {
        return this.zzamg;
    }

    public long getInterval() {
        return this.zzamf;
    }

    public int getNumUpdates() {
        return this.zzamh;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public float getSmallestDisplacement() {
        return this.zzami;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.mPriority), Long.valueOf(this.zzamf), Long.valueOf(this.zzamg), Boolean.valueOf(this.zzabz), Long.valueOf(this.zzalO), Integer.valueOf(this.zzamh), Float.valueOf(this.zzami));
    }

    public LocationRequest setExpirationDuration(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (j > Long.MAX_VALUE - elapsedRealtime) {
            this.zzalO = Long.MAX_VALUE;
        } else {
            this.zzalO = elapsedRealtime + j;
        }
        if (this.zzalO < 0) {
            this.zzalO = 0;
        }
        return this;
    }

    public LocationRequest setExpirationTime(long j) {
        this.zzalO = j;
        if (this.zzalO < 0) {
            this.zzalO = 0;
        }
        return this;
    }

    public LocationRequest setFastestInterval(long j) {
        zzA(j);
        this.zzabz = true;
        this.zzamg = j;
        return this;
    }

    public LocationRequest setInterval(long j) {
        zzA(j);
        this.zzamf = j;
        if (!this.zzabz) {
            this.zzamg = (long) (((double) this.zzamf) / 6.0d);
        }
        return this;
    }

    public LocationRequest setNumUpdates(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("invalid numUpdates: " + i);
        }
        this.zzamh = i;
        return this;
    }

    public LocationRequest setPriority(int i) {
        zzfj(i);
        this.mPriority = i;
        return this;
    }

    public LocationRequest setSmallestDisplacement(float f) {
        zza(f);
        this.zzami = f;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request[").append(zzfk(this.mPriority));
        if (this.mPriority != PRIORITY_NO_POWER) {
            stringBuilder.append(" requested=");
            stringBuilder.append(this.zzamf + "ms");
        }
        stringBuilder.append(" fastest=");
        stringBuilder.append(this.zzamg + "ms");
        if (this.zzamj > this.zzamf) {
            stringBuilder.append(" maxWait=");
            stringBuilder.append(this.zzamj + "ms");
        }
        if (this.zzalO != Long.MAX_VALUE) {
            long elapsedRealtime = this.zzalO - SystemClock.elapsedRealtime();
            stringBuilder.append(" expireIn=");
            stringBuilder.append(elapsedRealtime + "ms");
        }
        if (this.zzamh != CNCCCommands.CMD_CNCC_CMD_UNKNOWN) {
            stringBuilder.append(" num=").append(this.zzamh);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzg.zza(this, parcel, i);
    }
}
