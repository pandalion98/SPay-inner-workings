package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SContextAttribute implements Parcelable {
    public static final Creator<SContextAttribute> CREATOR = new Creator<SContextAttribute>() {
        public SContextAttribute createFromParcel(Parcel in) {
            return new SContextAttribute(in);
        }

        public SContextAttribute[] newArray(int size) {
            return new SContextAttribute[size];
        }
    };
    private Bundle mActivityLocationLogging = new Bundle();
    private Bundle mActivityNotification = new Bundle();
    private Bundle mActivityNotificationEx = new Bundle();
    private Bundle mApproach = new Bundle();
    private Bundle mAutoBrightness = new Bundle();
    private Bundle mAutoRotation = new Bundle();
    private Bundle mDevicePhysicalContextMonitor = new Bundle();
    private Bundle mDualDisplayAngle = new Bundle();
    private Bundle mEnvironment = new Bundle();
    private Bundle mEnvironmentAdaptiveDisplay = new Bundle();
    private Bundle mExercise = new Bundle();
    private Bundle mFlatMotionForTableMode = new Bundle();
    private Bundle mHallSensor = new Bundle();
    private Bundle mInactiveTimer = new Bundle();
    private Bundle mMovementForPositioning = new Bundle();
    private Bundle mPedometer = new Bundle();
    private Bundle mShakeMotion = new Bundle();
    private Bundle mSleepMonitor = new Bundle();
    private Bundle mSlocationCore = new Bundle();
    private Bundle mSpecificPoseAlert = new Bundle();
    private Bundle mStepCountAlert = new Bundle();
    private Bundle mStepLevelMonitor = new Bundle();
    private Bundle mTemperatureAlert = new Bundle();
    private Bundle mWakeUpVoice = new Bundle();

    SContextAttribute() {
    }

    SContextAttribute(Parcel src) {
        readFromParcel(src);
    }

    boolean checkAttribute() {
        return true;
    }

    public Bundle getAttribute(int service) {
        switch (service) {
            case 1:
                return this.mApproach;
            case 2:
                return this.mPedometer;
            case 3:
                return this.mStepCountAlert;
            case 6:
                return this.mAutoRotation;
            case 8:
                return this.mEnvironment;
            case 9:
                return this.mMovementForPositioning;
            case 12:
                return this.mShakeMotion;
            case 16:
                return this.mWakeUpVoice;
            case 23:
                return this.mTemperatureAlert;
            case 24:
                return this.mActivityLocationLogging;
            case 27:
                return this.mActivityNotification;
            case 28:
                return this.mSpecificPoseAlert;
            case 29:
                return this.mSleepMonitor;
            case 30:
                return this.mActivityNotificationEx;
            case 33:
                return this.mStepLevelMonitor;
            case 35:
                return this.mInactiveTimer;
            case 36:
                return this.mFlatMotionForTableMode;
            case 39:
                return this.mAutoBrightness;
            case 40:
                return this.mExercise;
            case 43:
                return this.mHallSensor;
            case 44:
                return this.mEnvironmentAdaptiveDisplay;
            case 45:
                return this.mDualDisplayAngle;
            case 47:
                return this.mSlocationCore;
            case 51:
                return this.mDevicePhysicalContextMonitor;
            default:
                return null;
        }
    }

    void setAttribute(int service, Bundle attribute) {
        switch (service) {
            case 1:
                this.mApproach = attribute;
                return;
            case 2:
                this.mPedometer = attribute;
                return;
            case 3:
                this.mStepCountAlert = attribute;
                return;
            case 6:
                this.mAutoRotation = attribute;
                return;
            case 8:
                this.mEnvironment = attribute;
                return;
            case 9:
                this.mMovementForPositioning = attribute;
                return;
            case 12:
                this.mShakeMotion = attribute;
                return;
            case 16:
                this.mWakeUpVoice = attribute;
                return;
            case 23:
                this.mTemperatureAlert = attribute;
                return;
            case 24:
                this.mActivityLocationLogging = attribute;
                return;
            case 27:
                this.mActivityNotification = attribute;
                return;
            case 28:
                this.mSpecificPoseAlert = attribute;
                return;
            case 29:
                this.mSleepMonitor = attribute;
                return;
            case 30:
                this.mActivityNotificationEx = attribute;
                return;
            case 33:
                this.mStepLevelMonitor = attribute;
                return;
            case 35:
                this.mInactiveTimer = attribute;
                return;
            case 36:
                this.mFlatMotionForTableMode = attribute;
                return;
            case 39:
                this.mAutoBrightness = attribute;
                return;
            case 40:
                this.mExercise = attribute;
                return;
            case 43:
                this.mHallSensor = attribute;
                return;
            case 44:
                this.mEnvironmentAdaptiveDisplay = attribute;
                return;
            case 45:
                this.mDualDisplayAngle = attribute;
                return;
            case 47:
                this.mSlocationCore = attribute;
                return;
            case 51:
                this.mDevicePhysicalContextMonitor = attribute;
                return;
            default:
                return;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mApproach);
        dest.writeBundle(this.mPedometer);
        dest.writeBundle(this.mStepCountAlert);
        dest.writeBundle(this.mAutoRotation);
        dest.writeBundle(this.mEnvironment);
        dest.writeBundle(this.mMovementForPositioning);
        dest.writeBundle(this.mShakeMotion);
        dest.writeBundle(this.mWakeUpVoice);
        dest.writeBundle(this.mTemperatureAlert);
        dest.writeBundle(this.mActivityLocationLogging);
        dest.writeBundle(this.mActivityNotification);
        dest.writeBundle(this.mSpecificPoseAlert);
        dest.writeBundle(this.mSleepMonitor);
        dest.writeBundle(this.mActivityNotificationEx);
        dest.writeBundle(this.mStepLevelMonitor);
        dest.writeBundle(this.mInactiveTimer);
        dest.writeBundle(this.mFlatMotionForTableMode);
        dest.writeBundle(this.mAutoBrightness);
        dest.writeBundle(this.mExercise);
        dest.writeBundle(this.mEnvironmentAdaptiveDisplay);
        dest.writeBundle(this.mDualDisplayAngle);
        dest.writeBundle(this.mHallSensor);
        dest.writeBundle(this.mSlocationCore);
        dest.writeBundle(this.mDevicePhysicalContextMonitor);
    }

    private void readFromParcel(Parcel src) {
        this.mApproach = src.readBundle();
        this.mPedometer = src.readBundle();
        this.mStepCountAlert = src.readBundle();
        this.mAutoRotation = src.readBundle();
        this.mEnvironment = src.readBundle();
        this.mMovementForPositioning = src.readBundle();
        this.mShakeMotion = src.readBundle();
        this.mWakeUpVoice = src.readBundle();
        this.mTemperatureAlert = src.readBundle();
        this.mActivityLocationLogging = src.readBundle();
        this.mActivityNotification = src.readBundle();
        this.mSpecificPoseAlert = src.readBundle();
        this.mSleepMonitor = src.readBundle();
        this.mActivityNotificationEx = src.readBundle();
        this.mStepLevelMonitor = src.readBundle();
        this.mInactiveTimer = src.readBundle();
        this.mFlatMotionForTableMode = src.readBundle();
        this.mAutoBrightness = src.readBundle();
        this.mExercise = src.readBundle();
        this.mEnvironmentAdaptiveDisplay = src.readBundle();
        this.mDualDisplayAngle = src.readBundle();
        this.mHallSensor = src.readBundle();
        this.mSlocationCore = src.readBundle();
        this.mDevicePhysicalContextMonitor = src.readBundle();
    }
}
