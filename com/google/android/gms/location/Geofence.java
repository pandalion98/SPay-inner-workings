package com.google.android.gms.location;

import android.os.SystemClock;
import com.google.android.gms.internal.zzpk;

public interface Geofence {
    public static final int GEOFENCE_TRANSITION_DWELL = 4;
    public static final int GEOFENCE_TRANSITION_ENTER = 1;
    public static final int GEOFENCE_TRANSITION_EXIT = 2;
    public static final long NEVER_EXPIRE = -1;

    public static final class Builder {
        private int zzalN;
        private long zzalO;
        private short zzalP;
        private double zzalQ;
        private double zzalR;
        private float zzalS;
        private int zzalT;
        private int zzalU;
        private String zzxv;

        public Builder() {
            this.zzxv = null;
            this.zzalN = 0;
            this.zzalO = Long.MIN_VALUE;
            this.zzalP = (short) -1;
            this.zzalT = 0;
            this.zzalU = -1;
        }

        public Geofence build() {
            if (this.zzxv == null) {
                throw new IllegalArgumentException("Request ID not set.");
            } else if (this.zzalN == 0) {
                throw new IllegalArgumentException("Transitions types not set.");
            } else if ((this.zzalN & Geofence.GEOFENCE_TRANSITION_DWELL) != 0 && this.zzalU < 0) {
                throw new IllegalArgumentException("Non-negative loitering delay needs to be set when transition types include GEOFENCE_TRANSITION_DWELLING.");
            } else if (this.zzalO == Long.MIN_VALUE) {
                throw new IllegalArgumentException("Expiration not set.");
            } else if (this.zzalP == (short) -1) {
                throw new IllegalArgumentException("Geofence region not set.");
            } else if (this.zzalT >= 0) {
                return new zzpk(this.zzxv, this.zzalN, (short) 1, this.zzalQ, this.zzalR, this.zzalS, this.zzalO, this.zzalT, this.zzalU);
            } else {
                throw new IllegalArgumentException("Notification responsiveness should be nonnegative.");
            }
        }

        public Builder setCircularRegion(double d, double d2, float f) {
            this.zzalP = (short) 1;
            this.zzalQ = d;
            this.zzalR = d2;
            this.zzalS = f;
            return this;
        }

        public Builder setExpirationDuration(long j) {
            if (j < 0) {
                this.zzalO = Geofence.NEVER_EXPIRE;
            } else {
                this.zzalO = SystemClock.elapsedRealtime() + j;
            }
            return this;
        }

        public Builder setLoiteringDelay(int i) {
            this.zzalU = i;
            return this;
        }

        public Builder setNotificationResponsiveness(int i) {
            this.zzalT = i;
            return this;
        }

        public Builder setRequestId(String str) {
            this.zzxv = str;
            return this;
        }

        public Builder setTransitionTypes(int i) {
            this.zzalN = i;
            return this;
        }
    }

    String getRequestId();
}
