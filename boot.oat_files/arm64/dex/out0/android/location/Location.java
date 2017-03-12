package android.location;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.Printer;
import android.util.TimeUtils;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class Location implements Parcelable {
    public static final Creator<Location> CREATOR = new Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            boolean z;
            boolean z2 = true;
            Location l = new Location(in.readString());
            l.mTime = in.readLong();
            l.mElapsedRealtimeNanos = in.readLong();
            l.mLatitude = in.readDouble();
            l.mLongitude = in.readDouble();
            l.mHasAltitude = in.readInt() != 0;
            l.mAltitude = in.readDouble();
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            l.mHasSpeed = z;
            l.mSpeed = in.readFloat();
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            l.mHasBearing = z;
            l.mBearing = in.readFloat();
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            l.mHasAccuracy = z;
            l.mAccuracy = in.readFloat();
            l.mExtras = in.readBundle();
            if (in.readInt() == 0) {
                z2 = false;
            }
            l.mIsFromMockProvider = z2;
            return l;
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    public static final String EXTRA_COARSE_LOCATION = "coarseLocation";
    public static final String EXTRA_NO_GPS_LOCATION = "noGPSLocation";
    public static final int FORMAT_DEGREES = 0;
    public static final int FORMAT_MINUTES = 1;
    public static final int FORMAT_SECONDS = 2;
    private float mAccuracy;
    private double mAltitude;
    private float mBearing;
    private float mDistance;
    private long mElapsedRealtimeNanos;
    private Bundle mExtras;
    private boolean mHasAccuracy;
    private boolean mHasAltitude;
    private boolean mHasBearing;
    private boolean mHasSpeed;
    private float mInitialBearing;
    private boolean mIsFromMockProvider;
    private double mLat1;
    private double mLat2;
    private double mLatitude;
    private double mLon1;
    private double mLon2;
    private double mLongitude;
    private String mProvider;
    private final float[] mResults;
    private float mSpeed;
    private long mTime;

    public Location(String provider) {
        this.mTime = 0;
        this.mElapsedRealtimeNanos = 0;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mHasAltitude = false;
        this.mAltitude = 0.0d;
        this.mHasSpeed = false;
        this.mSpeed = 0.0f;
        this.mHasBearing = false;
        this.mBearing = 0.0f;
        this.mHasAccuracy = false;
        this.mAccuracy = 0.0f;
        this.mExtras = null;
        this.mIsFromMockProvider = false;
        this.mLat1 = 0.0d;
        this.mLon1 = 0.0d;
        this.mLat2 = 0.0d;
        this.mLon2 = 0.0d;
        this.mDistance = 0.0f;
        this.mInitialBearing = 0.0f;
        this.mResults = new float[2];
        this.mProvider = provider;
    }

    public Location(Location l) {
        this.mTime = 0;
        this.mElapsedRealtimeNanos = 0;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mHasAltitude = false;
        this.mAltitude = 0.0d;
        this.mHasSpeed = false;
        this.mSpeed = 0.0f;
        this.mHasBearing = false;
        this.mBearing = 0.0f;
        this.mHasAccuracy = false;
        this.mAccuracy = 0.0f;
        this.mExtras = null;
        this.mIsFromMockProvider = false;
        this.mLat1 = 0.0d;
        this.mLon1 = 0.0d;
        this.mLat2 = 0.0d;
        this.mLon2 = 0.0d;
        this.mDistance = 0.0f;
        this.mInitialBearing = 0.0f;
        this.mResults = new float[2];
        set(l);
    }

    public void set(Location l) {
        this.mProvider = l.mProvider;
        this.mTime = l.mTime;
        this.mElapsedRealtimeNanos = l.mElapsedRealtimeNanos;
        this.mLatitude = l.mLatitude;
        this.mLongitude = l.mLongitude;
        this.mHasAltitude = l.mHasAltitude;
        this.mAltitude = l.mAltitude;
        this.mHasSpeed = l.mHasSpeed;
        this.mSpeed = l.mSpeed;
        this.mHasBearing = l.mHasBearing;
        this.mBearing = l.mBearing;
        this.mHasAccuracy = l.mHasAccuracy;
        this.mAccuracy = l.mAccuracy;
        this.mExtras = l.mExtras == null ? null : new Bundle(l.mExtras);
        this.mIsFromMockProvider = l.mIsFromMockProvider;
    }

    public void reset() {
        this.mProvider = null;
        this.mTime = 0;
        this.mElapsedRealtimeNanos = 0;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mHasAltitude = false;
        this.mAltitude = 0.0d;
        this.mHasSpeed = false;
        this.mSpeed = 0.0f;
        this.mHasBearing = false;
        this.mBearing = 0.0f;
        this.mHasAccuracy = false;
        this.mAccuracy = 0.0f;
        this.mExtras = null;
        this.mIsFromMockProvider = false;
    }

    public static String convert(double coordinate, int outputType) {
        if (coordinate < -180.0d || coordinate > 180.0d || Double.isNaN(coordinate)) {
            throw new IllegalArgumentException("coordinate=" + coordinate);
        } else if (outputType == 0 || outputType == 1 || outputType == 2) {
            StringBuilder sb = new StringBuilder();
            if (coordinate < 0.0d) {
                sb.append('-');
                coordinate = -coordinate;
            }
            DecimalFormat df = new DecimalFormat("###.#####");
            if (outputType == 1 || outputType == 2) {
                int degrees = (int) Math.floor(coordinate);
                sb.append(degrees);
                sb.append(':');
                coordinate = (coordinate - ((double) degrees)) * 60.0d;
                if (outputType == 2) {
                    int minutes = (int) Math.floor(coordinate);
                    sb.append(minutes);
                    sb.append(':');
                    coordinate = (coordinate - ((double) minutes)) * 60.0d;
                }
            }
            sb.append(df.format(coordinate));
            return sb.toString();
        } else {
            throw new IllegalArgumentException("outputType=" + outputType);
        }
    }

    public static double convert(String coordinate) {
        if (coordinate == null) {
            throw new NullPointerException("coordinate");
        }
        boolean negative = false;
        if (coordinate.charAt(0) == '-') {
            coordinate = coordinate.substring(1);
            negative = true;
        }
        StringTokenizer st = new StringTokenizer(coordinate, ":");
        int tokens = st.countTokens();
        if (tokens < 1) {
            throw new IllegalArgumentException("coordinate=" + coordinate);
        }
        try {
            String degrees = st.nextToken();
            double val;
            if (tokens == 1) {
                val = Double.parseDouble(degrees);
                if (negative) {
                    return -val;
                }
                return val;
            }
            double min;
            String minutes = st.nextToken();
            int deg = Integer.parseInt(degrees);
            double sec = 0.0d;
            if (st.hasMoreTokens()) {
                min = (double) Integer.parseInt(minutes);
                sec = Double.parseDouble(st.nextToken());
            } else {
                min = Double.parseDouble(minutes);
            }
            boolean isNegative180 = negative && deg == 180 && min == 0.0d && sec == 0.0d;
            if (((double) deg) < 0.0d || (deg > 179 && !isNegative180)) {
                throw new IllegalArgumentException("coordinate=" + coordinate);
            } else if (min < 0.0d || min > 59.0d) {
                throw new IllegalArgumentException("coordinate=" + coordinate);
            } else if (sec < 0.0d || sec > 59.0d) {
                throw new IllegalArgumentException("coordinate=" + coordinate);
            } else {
                val = (((((double) deg) * 3600.0d) + (60.0d * min)) + sec) / 3600.0d;
                return negative ? -val : val;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("coordinate=" + coordinate);
        }
    }

    private static void computeDistanceAndBearing(double lat1, double lon1, double lat2, double lon2, float[] results) {
        lat2 *= 0.017453292519943295d;
        double f = (6378137.0d - 6356752.3142d) / 6378137.0d;
        double aSqMinusBSqOverBSq = ((6378137.0d * 6378137.0d) - (6356752.3142d * 6356752.3142d)) / (6356752.3142d * 6356752.3142d);
        double L = (lon2 * 0.017453292519943295d) - (lon1 * 0.017453292519943295d);
        double A = 0.0d;
        double U1 = Math.atan((1.0d - f) * Math.tan(lat1 * 0.017453292519943295d));
        double U2 = Math.atan((1.0d - f) * Math.tan(lat2));
        double cosU1 = Math.cos(U1);
        double cosU2 = Math.cos(U2);
        double sinU1 = Math.sin(U1);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = sinU1 * sinU2;
        double sigma = 0.0d;
        double deltaSigma = 0.0d;
        double cosLambda = 0.0d;
        double sinLambda = 0.0d;
        double lambda = L;
        for (int iter = 0; iter < 20; iter++) {
            double cos2SM;
            double lambdaOrig = lambda;
            cosLambda = Math.cos(lambda);
            sinLambda = Math.sin(lambda);
            double t1 = cosU2 * sinLambda;
            double t2 = (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda);
            double sinSigma = Math.sqrt((t1 * t1) + (t2 * t2));
            double cosSigma = sinU1sinU2 + (cosU1cosU2 * cosLambda);
            sigma = Math.atan2(sinSigma, cosSigma);
            double sinAlpha = sinSigma == 0.0d ? 0.0d : (cosU1cosU2 * sinLambda) / sinSigma;
            double cosSqAlpha = 1.0d - (sinAlpha * sinAlpha);
            if (cosSqAlpha == 0.0d) {
                cos2SM = 0.0d;
            } else {
                cos2SM = cosSigma - ((2.0d * sinU1sinU2) / cosSqAlpha);
            }
            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq;
            A = 1.0d + ((uSquared / 16384.0d) * (4096.0d + ((-768.0d + ((320.0d - (175.0d * uSquared)) * uSquared)) * uSquared)));
            double B = (uSquared / 1024.0d) * (256.0d + ((-128.0d + ((74.0d - (47.0d * uSquared)) * uSquared)) * uSquared));
            double C = ((f / 16.0d) * cosSqAlpha) * (4.0d + ((4.0d - (3.0d * cosSqAlpha)) * f));
            double cos2SMSq = cos2SM * cos2SM;
            deltaSigma = (B * sinSigma) * (((B / 4.0d) * (((-1.0d + (2.0d * cos2SMSq)) * cosSigma) - ((((B / 6.0d) * cos2SM) * (-3.0d + ((4.0d * sinSigma) * sinSigma))) * (-3.0d + (4.0d * cos2SMSq))))) + cos2SM);
            lambda = L + ((((1.0d - C) * f) * sinAlpha) * (((C * sinSigma) * (((C * cosSigma) * (-1.0d + ((2.0d * cos2SM) * cos2SM))) + cos2SM)) + sigma));
            if (Math.abs((lambda - lambdaOrig) / lambda) < 1.0E-12d) {
                break;
            }
        }
        results[0] = (float) ((6356752.3142d * A) * (sigma - deltaSigma));
        if (results.length > 1) {
            results[1] = (float) (((double) ((float) Math.atan2(cosU2 * sinLambda, (cosU1 * sinU2) - ((sinU1 * cosU2) * cosLambda)))) * 57.29577951308232d);
            if (results.length > 2) {
                results[2] = (float) (((double) ((float) Math.atan2(cosU1 * sinLambda, ((-sinU1) * cosU2) + ((cosU1 * sinU2) * cosLambda)))) * 57.29577951308232d);
            }
        }
    }

    public static void distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results) {
        if (results == null || results.length < 1) {
            throw new IllegalArgumentException("results is null or has length < 1");
        }
        computeDistanceAndBearing(startLatitude, startLongitude, endLatitude, endLongitude, results);
    }

    public float distanceTo(Location dest) {
        float f;
        synchronized (this.mResults) {
            if (!(this.mLatitude == this.mLat1 && this.mLongitude == this.mLon1 && dest.mLatitude == this.mLat2 && dest.mLongitude == this.mLon2)) {
                computeDistanceAndBearing(this.mLatitude, this.mLongitude, dest.mLatitude, dest.mLongitude, this.mResults);
                this.mLat1 = this.mLatitude;
                this.mLon1 = this.mLongitude;
                this.mLat2 = dest.mLatitude;
                this.mLon2 = dest.mLongitude;
                this.mDistance = this.mResults[0];
                this.mInitialBearing = this.mResults[1];
            }
            f = this.mDistance;
        }
        return f;
    }

    public float bearingTo(Location dest) {
        float f;
        synchronized (this.mResults) {
            if (!(this.mLatitude == this.mLat1 && this.mLongitude == this.mLon1 && dest.mLatitude == this.mLat2 && dest.mLongitude == this.mLon2)) {
                computeDistanceAndBearing(this.mLatitude, this.mLongitude, dest.mLatitude, dest.mLongitude, this.mResults);
                this.mLat1 = this.mLatitude;
                this.mLon1 = this.mLongitude;
                this.mLat2 = dest.mLatitude;
                this.mLon2 = dest.mLongitude;
                this.mDistance = this.mResults[0];
                this.mInitialBearing = this.mResults[1];
            }
            f = this.mInitialBearing;
        }
        return f;
    }

    public String getProvider() {
        return this.mProvider;
    }

    public void setProvider(String provider) {
        this.mProvider = provider;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public long getElapsedRealtimeNanos() {
        return this.mElapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long time) {
        this.mElapsedRealtimeNanos = time;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public boolean hasAltitude() {
        return this.mHasAltitude;
    }

    public double getAltitude() {
        return this.mAltitude;
    }

    public void setAltitude(double altitude) {
        this.mAltitude = altitude;
        this.mHasAltitude = true;
    }

    public void removeAltitude() {
        this.mAltitude = 0.0d;
        this.mHasAltitude = false;
    }

    public boolean hasSpeed() {
        return this.mHasSpeed;
    }

    public float getSpeed() {
        return this.mSpeed;
    }

    public void setSpeed(float speed) {
        this.mSpeed = speed;
        this.mHasSpeed = true;
    }

    public void removeSpeed() {
        this.mSpeed = 0.0f;
        this.mHasSpeed = false;
    }

    public boolean hasBearing() {
        return this.mHasBearing;
    }

    public float getBearing() {
        return this.mBearing;
    }

    public void setBearing(float bearing) {
        while (bearing < 0.0f) {
            bearing += 360.0f;
        }
        while (bearing >= 360.0f) {
            bearing -= 360.0f;
        }
        this.mBearing = bearing;
        this.mHasBearing = true;
    }

    public void removeBearing() {
        this.mBearing = 0.0f;
        this.mHasBearing = false;
    }

    public boolean hasAccuracy() {
        return this.mHasAccuracy;
    }

    public float getAccuracy() {
        return this.mAccuracy;
    }

    public void setAccuracy(float accuracy) {
        this.mAccuracy = accuracy;
        this.mHasAccuracy = true;
    }

    public void removeAccuracy() {
        this.mAccuracy = 0.0f;
        this.mHasAccuracy = false;
    }

    public boolean isComplete() {
        if (this.mProvider == null || !this.mHasAccuracy || this.mTime == 0 || this.mElapsedRealtimeNanos == 0) {
            return false;
        }
        return true;
    }

    public void makeComplete() {
        if (this.mProvider == null) {
            this.mProvider = "?";
        }
        if (!this.mHasAccuracy) {
            this.mHasAccuracy = true;
            this.mAccuracy = SensorManager.LIGHT_CLOUDY;
        }
        if (this.mTime == 0) {
            this.mTime = System.currentTimeMillis();
        }
        if (this.mElapsedRealtimeNanos == 0) {
            this.mElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        }
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public void setExtras(Bundle extras) {
        this.mExtras = extras == null ? null : new Bundle(extras);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Location[");
        s.append(this.mProvider);
        s.append(String.format(" %.6f,%.6f", new Object[]{Double.valueOf(this.mLatitude), Double.valueOf(this.mLongitude)}));
        if (this.mHasAccuracy) {
            s.append(String.format(" acc=%.0f", new Object[]{Float.valueOf(this.mAccuracy)}));
        } else {
            s.append(" acc=???");
        }
        if (this.mTime == 0) {
            s.append(" t=?!?");
        }
        if (this.mElapsedRealtimeNanos == 0) {
            s.append(" et=?!?");
        } else {
            s.append(" et=");
            TimeUtils.formatDuration(this.mElapsedRealtimeNanos / 1000000, s);
        }
        if (this.mHasAltitude) {
            s.append(" alt=").append(this.mAltitude);
        }
        if (this.mHasSpeed) {
            s.append(" vel=").append(this.mSpeed);
        }
        if (this.mHasBearing) {
            s.append(" bear=").append(this.mBearing);
        }
        if (this.mIsFromMockProvider) {
            s.append(" mock");
        }
        if (this.mExtras != null) {
            s.append(" {").append(this.mExtras).append('}');
        }
        s.append(']');
        return s.toString();
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + toString());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        int i;
        int i2 = 1;
        parcel.writeString(this.mProvider);
        parcel.writeLong(this.mTime);
        parcel.writeLong(this.mElapsedRealtimeNanos);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeInt(this.mHasAltitude ? 1 : 0);
        parcel.writeDouble(this.mAltitude);
        if (this.mHasSpeed) {
            i = 1;
        } else {
            i = 0;
        }
        parcel.writeInt(i);
        parcel.writeFloat(this.mSpeed);
        if (this.mHasBearing) {
            i = 1;
        } else {
            i = 0;
        }
        parcel.writeInt(i);
        parcel.writeFloat(this.mBearing);
        if (this.mHasAccuracy) {
            i = 1;
        } else {
            i = 0;
        }
        parcel.writeInt(i);
        parcel.writeFloat(this.mAccuracy);
        parcel.writeBundle(this.mExtras);
        if (!this.mIsFromMockProvider) {
            i2 = 0;
        }
        parcel.writeInt(i2);
    }

    public Location getExtraLocation(String key) {
        if (this.mExtras != null) {
            Parcelable value = this.mExtras.getParcelable(key);
            if (value instanceof Location) {
                return (Location) value;
            }
        }
        return null;
    }

    public void setExtraLocation(String key, Location value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putParcelable(key, value);
    }

    public boolean isFromMockProvider() {
        return this.mIsFromMockProvider;
    }

    public void setIsFromMockProvider(boolean isFromMockProvider) {
        this.mIsFromMockProvider = isFromMockProvider;
    }
}
