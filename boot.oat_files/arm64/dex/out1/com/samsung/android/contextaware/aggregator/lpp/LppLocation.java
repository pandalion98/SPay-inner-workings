package com.samsung.android.contextaware.aggregator.lpp;

import android.location.Location;
import android.util.Log;
import com.samsung.android.contextaware.aggregator.lpp.algorithm.CoordinateTransform;

public class LppLocation {
    private static String TAG = "LppLocation";
    public long Capturedtime;
    private boolean Updated;
    private final double[] filteredVelocity;
    private Location loc;
    private double mOrgHei;
    private double mOrgLat;
    private double mOrgLon;
    private double mPosECEF_X;
    private double mPosECEF_Y;
    private double mPosECEF_Z;
    private double mPosEast;
    private double mPosNorth;
    private double mPosUp;
    private int movingStatus;

    public LppLocation(Location l) {
        this.mOrgLat = 0.0d;
        this.mOrgLon = 0.0d;
        this.mOrgHei = 0.0d;
        this.Updated = true;
        this.Capturedtime = 0;
        this.movingStatus = 2;
        this.filteredVelocity = new double[]{0.0d, 0.0d, 0.0d};
        if (l != null) {
            this.loc = new Location(l);
            this.Capturedtime = l.getTime();
            this.Updated = true;
            setOrigin(l.getLatitude(), l.getLongitude(), l.getAltitude());
            return;
        }
        this.loc = new Location("NOPROVIDER");
        this.Updated = true;
    }

    public LppLocation() {
        this.mOrgLat = 0.0d;
        this.mOrgLon = 0.0d;
        this.mOrgHei = 0.0d;
        this.Updated = true;
        this.Capturedtime = 0;
        this.movingStatus = 2;
        this.filteredVelocity = new double[]{0.0d, 0.0d, 0.0d};
        this.loc = new Location("NOPROVIDER");
        this.Updated = true;
    }

    public LppLocation(LppLocation l) {
        this.mOrgLat = 0.0d;
        this.mOrgLon = 0.0d;
        this.mOrgHei = 0.0d;
        this.Updated = true;
        this.Capturedtime = 0;
        this.movingStatus = 2;
        this.filteredVelocity = new double[]{0.0d, 0.0d, 0.0d};
        this.loc = new Location(l.loc);
        set(l);
    }

    public Location getLoc() {
        return this.loc;
    }

    public long getTime() {
        return this.loc.getTime();
    }

    public double getLatitude() {
        return this.loc.getLatitude();
    }

    public double getLongitude() {
        return this.loc.getLongitude();
    }

    public double getAltitude() {
        return this.loc.getAltitude();
    }

    public void setOrigin(double OrgLat, double OrgLon, double OrgHeight) {
        this.mOrgLat = OrgLat;
        this.mOrgLon = OrgLon;
        this.mOrgHei = OrgHeight;
        this.Updated = true;
    }

    public double getOriginLat() {
        return this.mOrgLat;
    }

    public double getOriginLon() {
        return this.mOrgLon;
    }

    public double getOriginAltitude() {
        return this.mOrgHei;
    }

    public void setSystemTime() {
        this.Capturedtime = System.nanoTime();
    }

    public long getSystemTime() {
        return this.Capturedtime;
    }

    public void set(Location l) {
        if (l != null) {
            this.loc.set(l);
            this.Capturedtime = l.getTime();
            setSystemTime();
            this.Updated = true;
        }
    }

    public void set(LppLocation lpploc) {
        this.loc.set(lpploc.loc);
        setOrigin(lpploc.getOriginLat(), lpploc.getOriginLon(), lpploc.getOriginAltitude());
        this.Capturedtime = lpploc.getSystemTime();
        this.movingStatus = lpploc.getMovingStatus();
        double[] filteredV = lpploc.getFilteredVelocity();
        this.filteredVelocity[0] = filteredV[0];
        this.filteredVelocity[1] = filteredV[1];
        this.filteredVelocity[2] = filteredV[2];
        this.Updated = true;
    }

    public double getPosEastLocal() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosEast;
    }

    public double getPosNorthLocal() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosNorth;
    }

    public double getPosUpLocal() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosUp;
    }

    public double getPosECEF_X() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosECEF_X;
    }

    public double getPosECEF_Y() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosECEF_Y;
    }

    public double getPosECEF_Z() {
        if (this.Updated) {
            CalCoordinate();
        }
        return this.mPosECEF_Z;
    }

    private void CalCoordinate() {
        this.Updated = false;
        llh = new double[3];
        double[] orgllh = new double[]{Math.toRadians(this.loc.getLatitude()), Math.toRadians(this.loc.getLongitude()), this.loc.getAltitude()};
        orgllh[0] = Math.toRadians(this.mOrgLat);
        orgllh[1] = Math.toRadians(this.mOrgLon);
        orgllh[2] = this.mOrgHei;
        double[] enu = CoordinateTransform.llh2enu(llh, orgllh);
        this.mPosEast = enu[0];
        this.mPosNorth = enu[1];
        this.mPosUp = enu[2];
        double[] XYZ = CoordinateTransform.llh2xyz(llh);
        this.mPosECEF_X = XYZ[0];
        this.mPosECEF_Y = XYZ[1];
        this.mPosECEF_Z = XYZ[2];
    }

    public void PosPropation(double bearing, double stepLength) {
        if (this.Updated) {
            CalCoordinate();
        }
        double HeadVec_E = Math.sin(bearing);
        double HeadVec_N = Math.cos(bearing);
        this.mPosEast += HeadVec_E * stepLength;
        this.mPosNorth += HeadVec_N * stepLength;
        orgllh = new double[3];
        double[] enu = new double[]{this.mPosEast, this.mPosNorth, this.mPosUp};
        orgllh[0] = Math.toRadians(this.mOrgLat);
        orgllh[1] = Math.toRadians(this.mOrgLon);
        orgllh[2] = this.mOrgHei;
        double[] llh = CoordinateTransform.enu2llh(enu, orgllh);
        this.loc.setLatitude(Math.toDegrees(llh[0]));
        this.loc.setLongitude(Math.toDegrees(llh[1]));
        this.loc.setAltitude(llh[2]);
        CalCoordinate();
    }

    public void setSystemTime(long systemtime) {
        if (this.Capturedtime == 0) {
            Log.e(TAG, "setSystemTime() - Abnormal method calling");
            return;
        }
        double timediff = (double) (systemtime - this.Capturedtime);
        if (timediff > 1.0E15d) {
            Log.e(TAG, "systemtime" + systemtime + "     Capturedtime" + this.Capturedtime);
            Log.e(TAG, "setSystemTime() - systemtime overflow or propagation error timediff" + timediff);
            return;
        }
        this.Capturedtime = systemtime;
        this.loc.setTime(this.loc.getTime() + ((long) (1.0E-6d * timediff)));
    }

    public void setMovingStatus(int MS) {
        this.movingStatus = MS;
    }

    public int getMovingStatus() {
        return this.movingStatus;
    }

    public float getAccuracy() {
        return this.loc.getAccuracy();
    }

    public void setLatitude(double d) {
        this.loc.setLatitude(d);
        this.Updated = true;
    }

    public void setLongitude(double d) {
        this.loc.setLongitude(d);
        this.Updated = true;
    }

    public void setAltitude(double d) {
        this.loc.setAltitude(d);
        this.Updated = true;
    }

    public double distanceTo(LppLocation lppLoc2) {
        LppLocation lppLoc3 = new LppLocation(lppLoc2);
        lppLoc3.setOrigin(getOriginLat(), getOriginLon(), getOriginAltitude());
        return Math.sqrt(((getPosEastLocal() - lppLoc3.getPosEastLocal()) * (getPosEastLocal() - lppLoc3.getPosEastLocal())) + ((getPosNorthLocal() - lppLoc3.getPosNorthLocal()) * (getPosNorthLocal() - lppLoc3.getPosNorthLocal())));
    }

    public void estimateVelocity(LppLocation filterdOlderLoc, LppLocation currentLoc) {
        double X_D1_DiffAvg;
        double Y_D1_DiffAvg;
        double norm;
        double X_D1_GPS;
        double Y_D1_GPS;
        double X_D1_Diff;
        double Y_D1_Diff;
        LppLocation lppLoc0 = new LppLocation(filterdOlderLoc);
        LppLocation lppLocation = new LppLocation(currentLoc);
        lppLoc0.setOrigin(getLatitude(), getLongitude(), getAltitude());
        lppLocation.setOrigin(getLatitude(), getLongitude(), getAltitude());
        long t0_long = lppLoc0.getTime();
        long t1_long = getTime();
        long t2_long = lppLocation.getTime();
        double t0 = ((double) t0_long) * 0.001d;
        double t1 = ((double) t1_long) * 0.001d;
        double t2 = ((double) t2_long) * 0.001d;
        if (t1_long == t0_long || t2_long == t1_long) {
            SendStatus("WARNING: estimateVelocity - abnormal t0, t1, t2");
            X_D1_DiffAvg = 0.0d;
            Y_D1_DiffAvg = 0.0d;
        } else {
            X_D1_DiffAvg = ((lppLocation.getPosEastLocal() / (t2 - t1)) + ((0.0d - lppLoc0.getPosEastLocal()) / (t1 - t0))) / 2.0d;
            Y_D1_DiffAvg = ((lppLocation.getPosNorthLocal() / (t2 - t1)) + ((0.0d - lppLoc0.getPosNorthLocal()) / (t1 - t0))) / 2.0d;
            norm = Math.sqrt((X_D1_DiffAvg * X_D1_DiffAvg) + (Y_D1_DiffAvg * Y_D1_DiffAvg));
            if (norm > 0.001d) {
                X_D1_DiffAvg /= norm;
                Y_D1_DiffAvg /= norm;
            } else {
                X_D1_DiffAvg = 0.0d;
                Y_D1_DiffAvg = 0.0d;
            }
        }
        if (this.loc.getSpeed() > 15.0f) {
            X_D1_GPS = Math.sin(((double) (this.loc.getBearing() / 180.0f)) * 3.141592653589793d);
            Y_D1_GPS = Math.cos(((double) (this.loc.getBearing() / 180.0f)) * 3.141592653589793d);
        } else {
            X_D1_GPS = 0.0d;
            Y_D1_GPS = 0.0d;
        }
        if (t2_long == t0_long) {
            SendStatus("WARNING: estimateVelocity - abnormal t0, t2");
            X_D1_Diff = 0.0d;
            Y_D1_Diff = 0.0d;
        } else {
            norm = lppLocation.distanceTo(lppLoc0);
            X_D1_Diff = ((lppLocation.getPosEastLocal() - lppLoc0.getPosEastLocal()) / (t2 - t0)) / norm;
            Y_D1_Diff = ((lppLocation.getPosNorthLocal() - lppLoc0.getPosNorthLocal()) / (t2 - t0)) / norm;
        }
        if (this.loc.getSpeed() > 15.0f) {
            this.filteredVelocity[0] = ((0.0d * X_D1_DiffAvg) + (0.7d * X_D1_GPS)) + (0.3d * X_D1_Diff);
            this.filteredVelocity[1] = ((0.0d * Y_D1_DiffAvg) + (0.7d * Y_D1_GPS)) + (0.3d * Y_D1_Diff);
            return;
        }
        this.filteredVelocity[0] = X_D1_DiffAvg;
        this.filteredVelocity[1] = Y_D1_DiffAvg;
    }

    public double[] getFilteredVelocity() {
        return this.filteredVelocity;
    }

    private void SendStatus(String string) {
        Log.d(TAG, string);
    }

    public void setPosENU(double PosEast, double PosNorth, double PosUp) {
        orgllh = new double[3];
        double[] enu = new double[]{PosEast, PosNorth, PosUp};
        orgllh[0] = Math.toRadians(this.mOrgLat);
        orgllh[1] = Math.toRadians(this.mOrgLon);
        orgllh[2] = this.mOrgHei;
        double[] llh = CoordinateTransform.enu2llh(enu, orgllh);
        this.loc.setLatitude(Math.toDegrees(llh[0]));
        this.loc.setLongitude(Math.toDegrees(llh[1]));
        this.loc.setAltitude(llh[2]);
        CalCoordinate();
    }
}
