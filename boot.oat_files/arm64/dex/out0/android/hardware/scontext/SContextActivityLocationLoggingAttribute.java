package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextActivityLocationLoggingAttribute extends SContextAttribute {
    private static final String TAG = "SContextActivityLocationLoggingAttribute";
    private int mAreaRadius = 150;
    private int mLppResolution = 0;
    private int mStayingRadius = 50;
    private int mStopPeriod = 60;
    private int mWaitPeriod = 120;

    SContextActivityLocationLoggingAttribute() {
        setAttribute();
    }

    public SContextActivityLocationLoggingAttribute(int stopPeriod, int waitPeriod, int statyingRadius, int areaRadius, int lppResolution) {
        this.mStopPeriod = stopPeriod;
        this.mWaitPeriod = waitPeriod;
        this.mStayingRadius = statyingRadius;
        this.mAreaRadius = areaRadius;
        this.mLppResolution = lppResolution;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mStopPeriod < 0) {
            Log.e(TAG, "The stop period is wrong.");
            return false;
        } else if (this.mWaitPeriod < 0) {
            Log.e(TAG, "The wait period is wrong.");
            return false;
        } else if (this.mStayingRadius < 0) {
            Log.e(TAG, "The staying radius is wrong.");
            return false;
        } else if (this.mAreaRadius < 0) {
            Log.e(TAG, "The area radius is wrong.");
            return false;
        } else if (this.mLppResolution >= 0 && this.mLppResolution <= 2) {
            return true;
        } else {
            Log.e(TAG, "The lpp resolution is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("stop_period", this.mStopPeriod);
        attribute.putInt("wait_period", this.mWaitPeriod);
        attribute.putInt("staying_radius", this.mStayingRadius);
        attribute.putInt("area_radius", this.mAreaRadius);
        attribute.putInt("lpp_resolution", this.mLppResolution);
        super.setAttribute(24, attribute);
    }
}
