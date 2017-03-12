package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextSLocationCoreAttribute extends SContextAttribute {
    private static final String TAG = "SContextSLocationCoreAttribute";
    private int mAccuracy = 0;
    private int mAction = -1;
    private int mFenceId = 0;
    private double mLatitude = 0.0d;
    private double mLongitude = 0.0d;
    private int mMin_Ditance = 0;
    private int mMin_Time = 0;
    private int mMode = -1;
    private int mRadius = 0;
    private int mStatus = 0;
    private int mSuccessGpsCnt = 0;
    private long mTimeStamp = 0;
    private int mTotalGpsCnt = 0;

    SContextSLocationCoreAttribute() {
        setAttribute();
    }

    public SContextSLocationCoreAttribute(int mode, int action) {
        this.mMode = mode;
        this.mAction = action;
        setAttribute();
    }

    public SContextSLocationCoreAttribute(int mode, int action, int fence_id, double latitude, double longitude, int radius, int total_gps_cnt, int success_gps_cnt) {
        this.mMode = mode;
        this.mAction = action;
        this.mFenceId = fence_id;
        this.mRadius = radius;
        this.mTotalGpsCnt = total_gps_cnt;
        this.mSuccessGpsCnt = success_gps_cnt;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        setAttribute();
    }

    public SContextSLocationCoreAttribute(int mode, int action, int fence_id) {
        this.mMode = mode;
        this.mAction = action;
        this.mFenceId = fence_id;
        setAttribute();
    }

    public SContextSLocationCoreAttribute(int mode, int action, int fence_id, int radius, int status) {
        this.mMode = mode;
        this.mAction = action;
        this.mFenceId = fence_id;
        this.mRadius = radius;
        this.mStatus = status;
        setAttribute();
    }

    public SContextSLocationCoreAttribute(int mode, int action, int min_distance, int min_time) {
        this.mMode = mode;
        this.mAction = action;
        this.mMin_Ditance = min_distance;
        this.mMin_Time = min_time;
        setAttribute();
    }

    public SContextSLocationCoreAttribute(int mode, int action, double latitude, double longitude, int accuracy, long timestamp) {
        this.mMode = mode;
        this.mAction = action;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAccuracy = accuracy;
        this.mTimeStamp = timestamp;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mMode < -1 || this.mMode > 1) {
            Log.d(TAG, "Mode value is wrong!!");
            return false;
        }
        if (this.mMode == 0) {
            if (this.mAction < -1 || this.mAction > 10) {
                Log.d(TAG, "Action value is wrong!!");
                return false;
            }
        } else if (this.mMode == 1 && (this.mAction < -1 || this.mAction > 14)) {
            Log.d(TAG, "Action value is wrong!!");
            return false;
        }
        if (this.mFenceId < 0) {
            Log.d(TAG, "FenceID is wrong!!");
            return false;
        } else if (this.mRadius < 0) {
            Log.d(TAG, "Radius is wrong!!");
            return false;
        } else if (this.mStatus < 0) {
            Log.d(TAG, "Status is wrong!1");
            return false;
        } else if (this.mTotalGpsCnt < 0) {
            Log.d(TAG, "TotalGpsCount is wrong!!");
            return false;
        } else if (this.mSuccessGpsCnt < 0) {
            Log.d(TAG, "Success gps count is wrong");
            return false;
        } else if (this.mMin_Ditance < 0) {
            Log.d(TAG, "Minimun distnace is wrong");
            return false;
        } else if (this.mMin_Time < 0) {
            Log.d(TAG, "Minimun time is wrong");
            return false;
        } else if (this.mAccuracy < 0) {
            Log.d(TAG, "Accuracy is wrong");
            return false;
        } else if (this.mTimeStamp < 0) {
            Log.d(TAG, "Timestamp is wrong");
            return false;
        } else if (this.mLongitude < -180.0d || this.mLongitude > 180.0d) {
            Log.d(TAG, "Longitude is wrong");
            return false;
        } else if (this.mLatitude >= -90.0d && this.mLatitude <= 90.0d) {
            return true;
        } else {
            Log.d(TAG, "Latitidue is wrong");
            return false;
        }
    }

    private void setAttribute() {
        StringBuffer sb = new StringBuffer();
        Bundle attribute = new Bundle();
        double[] doubleType;
        int[] intType;
        switch (this.mMode) {
            case 0:
                if (this.mAction != 1) {
                    if (this.mAction != 2) {
                        if (this.mAction != 7) {
                            if (this.mAction != 9) {
                                Log.d(TAG, "This Type is default attribute type");
                                break;
                            }
                            attribute.putIntArray("IntType", new int[]{this.mMin_Ditance, this.mMin_Time});
                            break;
                        }
                        attribute.putIntArray("IntType", new int[]{this.mFenceId, this.mRadius, this.mStatus});
                        break;
                    }
                    attribute.putIntArray("IntType", new int[]{this.mFenceId});
                    break;
                }
                doubleType = new double[2];
                intType = new int[]{this.mLatitude, this.mLongitude, this.mFenceId, this.mRadius};
                intType[2] = this.mTotalGpsCnt;
                intType[3] = this.mSuccessGpsCnt;
                attribute.putIntArray("IntType", intType);
                attribute.putDoubleArray("DoubleType", doubleType);
                break;
            case 1:
                if (this.mAction != 8) {
                    Log.d(TAG, "This Type is default attribute type");
                    break;
                }
                doubleType = new double[2];
                intType = new int[1];
                long[] longType = new long[]{this.mLatitude};
                doubleType[1] = this.mLongitude;
                intType[0] = this.mAccuracy;
                longType[0] = this.mTimeStamp;
                attribute.putDoubleArray("DoubleType", doubleType);
                attribute.putIntArray("IntType", intType);
                attribute.putLongArray("LongType", longType);
                break;
        }
        attribute.putInt("Mode", this.mMode);
        attribute.putInt("Action", this.mAction);
        Log.d(TAG, "setAttribute() mode : " + attribute.getInt("Mode") + " action : " + attribute.getInt("Action"));
        super.setAttribute(47, attribute);
    }
}
