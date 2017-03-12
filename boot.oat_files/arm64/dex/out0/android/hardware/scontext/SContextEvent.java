package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SContextEvent implements Parcelable {
    static final Creator<SContextEvent> CREATOR = new Creator<SContextEvent>() {
        public SContextEvent createFromParcel(Parcel in) {
            return new SContextEvent(in);
        }

        public SContextEvent[] newArray(int size) {
            return new SContextEvent[size];
        }
    };
    private SContextEventContext mDuplicatedEventContext;
    private SContextEventContext mEventContext;
    public SContext scontext;
    public long timestamp;

    public SContextEvent() {
        this.scontext = new SContext();
        this.timestamp = 0;
    }

    public SContextEvent(Parcel src) {
        readFromParcel(src);
    }

    public SContextApproach getApproachContext() {
        return (SContextApproach) this.mEventContext;
    }

    public SContextPedometer getPedometerContext() {
        return (SContextPedometer) this.mEventContext;
    }

    SContextStepCountAlert getStepCountAlertContext() {
        return (SContextStepCountAlert) this.mEventContext;
    }

    @Deprecated
    public SContextMotion getMotionContext() {
        return (SContextMotion) this.mEventContext;
    }

    public SContextMovement getMovementContext() {
        return (SContextMovement) this.mEventContext;
    }

    public SContextAutoRotation getAutoRotationContext() {
        return (SContextAutoRotation) this.mEventContext;
    }

    public SContextAirMotion getAirMotionContext() {
        return (SContextAirMotion) this.mEventContext;
    }

    @Deprecated
    public SContextEnvironment getEnvironmentContext() {
        return (SContextEnvironment) this.mEventContext;
    }

    @Deprecated
    public SContextMovementForPositioning getMovementForPositioningContext() {
        return (SContextMovementForPositioning) this.mEventContext;
    }

    @Deprecated
    public SContextCurrentStatusForPositioning getCurrentStatusForPositioningContext() {
        return (SContextCurrentStatusForPositioning) this.mEventContext;
    }

    public SContextCallPose getCallPoseContext() {
        return (SContextCallPose) this.mEventContext;
    }

    public SContextShakeMotion getShakeMotionContext() {
        return (SContextShakeMotion) this.mEventContext;
    }

    public SContextFlipCoverAction getFlipCoverActionContext() {
        return (SContextFlipCoverAction) this.mEventContext;
    }

    public SContextGyroTemperature getGyroTemperatureContext() {
        return (SContextGyroTemperature) this.mEventContext;
    }

    public SContextPutDownMotion getPutDownMotionContext() {
        return (SContextPutDownMotion) this.mEventContext;
    }

    public SContextWakeUpVoice getWakeUpVoiceContext() {
        return (SContextWakeUpVoice) this.mEventContext;
    }

    public SContextBounceShortMotion getBounceShortMotionContext() {
        return (SContextBounceShortMotion) this.mEventContext;
    }

    public SContextBounceLongMotion getBounceLongMotionContext() {
        return (SContextBounceLongMotion) this.mEventContext;
    }

    @Deprecated
    public SContextWristUpMotion getWristUpMotionContext() {
        return (SContextWristUpMotion) this.mEventContext;
    }

    public SContextFlatMotion getFlatMotionContext() {
        return (SContextFlatMotion) this.mEventContext;
    }

    @Deprecated
    public SContextMovementAlert getMovementAlertContext() {
        return (SContextMovementAlert) this.mEventContext;
    }

    @Deprecated
    public SContextTestFlatMotion getTestFlatMotionContext() {
        return (SContextTestFlatMotion) this.mEventContext;
    }

    public SContextDevicePosition getDevicePositionContext() {
        return (SContextDevicePosition) this.mEventContext;
    }

    @Deprecated
    SContextTemperatureAlert getTemperatureAlertContext() {
        return (SContextTemperatureAlert) this.mEventContext;
    }

    public SContextActivityLocationLogging getActivityLocationLoggingContext() {
        return (SContextActivityLocationLogging) this.mEventContext;
    }

    public SContextActivityTracker getActivityTrackerContext() {
        return (SContextActivityTracker) this.mEventContext;
    }

    public SContextActivityBatch getActivityBatchContext() {
        return (SContextActivityBatch) this.mEventContext;
    }

    public SContextActivityNotification getActivityNotificationContext() {
        return (SContextActivityNotification) this.mEventContext;
    }

    public SContextSpecificPoseAlert getSpecificPoseAlertContext() {
        return (SContextSpecificPoseAlert) this.mEventContext;
    }

    @Deprecated
    public SContextSleepMonitor getSleepMonitorContext() {
        return (SContextSleepMonitor) this.mEventContext;
    }

    public SContextActivityNotificationEx getActivityNotificationExContext() {
        return (SContextActivityNotificationEx) this.mEventContext;
    }

    @Deprecated
    SContextCaptureMotion getCaptureMotionContext() {
        return (SContextCaptureMotion) this.mEventContext;
    }

    SContextCallMotion getCallMotionContext() {
        return (SContextCallMotion) this.mEventContext;
    }

    public SContextStepLevelMonitor getStepLevelMonitorContext() {
        return (SContextStepLevelMonitor) this.mEventContext;
    }

    public SContextActiveTimeMonitor getActiveTimeMonitorContext() {
        return (SContextActiveTimeMonitor) this.mEventContext;
    }

    public SContextInactiveTimer getInactiveTimerContext() {
        return (SContextInactiveTimer) this.mEventContext;
    }

    public SContextFlatMotionForTableMode getFlatMotioForTableModeContext() {
        return (SContextFlatMotionForTableMode) this.mEventContext;
    }

    public SContextAutoBrightness getAutoBrightnessContext() {
        return (SContextAutoBrightness) this.mEventContext;
    }

    public SContextExercise getExerciseContext() {
        return (SContextExercise) this.mEventContext;
    }

    public SContextAbnormalPressure getAbnormalPressureContext() {
        return (SContextAbnormalPressure) this.mEventContext;
    }

    public SContextPhoneStatusMonitor getPhoneStatusMonitorContext() {
        return (SContextPhoneStatusMonitor) this.mEventContext;
    }

    public SContextHallSensor getHallSensorContext() {
        return (SContextHallSensor) this.mEventContext;
    }

    public SContextEnvironmentAdaptiveDisplay getEnvironmentAdaptiveDisplayContext() {
        return (SContextEnvironmentAdaptiveDisplay) this.mEventContext;
    }

    public SContextDualDisplayAngle getDualDisplayAngleContext() {
        return (SContextDualDisplayAngle) this.mEventContext;
    }

    public SContextWirelessChargingDetection getWirelessChargingDetectionContext() {
        return (SContextWirelessChargingDetection) this.mEventContext;
    }

    public SContextSLocationCore getSLocationCoreContext() {
        return (SContextSLocationCore) this.mEventContext;
    }

    public SContextMainScreenDetection getMainScreenDetectionContext() {
        return (SContextMainScreenDetection) this.mDuplicatedEventContext;
    }

    public SContextFlipMotion getFlipMotionContext() {
        return (SContextFlipMotion) this.mEventContext;
    }

    public SContextAnyMotionDetector getAnyMotionDetectorContext() {
        return (SContextAnyMotionDetector) this.mEventContext;
    }

    public SContextDevicePhysicalContextMonitor getDevicePhysicalContextMonitorContext() {
        return (SContextDevicePhysicalContextMonitor) this.mEventContext;
    }

    public SContextSensorStatusCheck getSensorStatusCheckContext() {
        return (SContextSensorStatusCheck) this.mEventContext;
    }

    public void setSContextEvent(int event, Bundle context) {
        this.scontext.setType(event);
        this.timestamp = System.nanoTime();
        switch (event) {
            case 1:
                this.mEventContext = new SContextApproach();
                this.mEventContext.setValues(context);
                return;
            case 2:
                this.mEventContext = new SContextPedometer();
                this.mEventContext.setValues(context);
                return;
            case 3:
                this.mEventContext = new SContextStepCountAlert();
                this.mEventContext.setValues(context);
                return;
            case 4:
                this.mEventContext = new SContextMotion();
                this.mEventContext.setValues(context);
                return;
            case 5:
                this.mEventContext = new SContextMovement();
                this.mEventContext.setValues(context);
                return;
            case 6:
                this.mEventContext = new SContextAutoRotation();
                this.mEventContext.setValues(context);
                return;
            case 7:
                this.mEventContext = new SContextAirMotion();
                this.mEventContext.setValues(context);
                return;
            case 8:
                this.mEventContext = new SContextEnvironment();
                this.mEventContext.setValues(context);
                return;
            case 9:
                this.mEventContext = new SContextMovementForPositioning();
                this.mEventContext.setValues(context);
                return;
            case 10:
                this.mEventContext = new SContextCurrentStatusForPositioning();
                this.mEventContext.setValues(context);
                return;
            case 11:
                this.mEventContext = new SContextCallPose();
                this.mEventContext.setValues(context);
                return;
            case 12:
                this.mEventContext = new SContextShakeMotion();
                this.mEventContext.setValues(context);
                return;
            case 13:
                this.mEventContext = new SContextFlipCoverAction();
                this.mEventContext.setValues(context);
                return;
            case 14:
                this.mEventContext = new SContextGyroTemperature();
                this.mEventContext.setValues(context);
                return;
            case 15:
                this.mEventContext = new SContextPutDownMotion();
                this.mEventContext.setValues(context);
                return;
            case 16:
                this.mEventContext = new SContextWakeUpVoice();
                this.mEventContext.setValues(context);
                return;
            case 17:
                this.mEventContext = new SContextBounceShortMotion();
                this.mEventContext.setValues(context);
                return;
            case 18:
                this.mEventContext = new SContextBounceLongMotion();
                this.mEventContext.setValues(context);
                return;
            case 19:
                this.mEventContext = new SContextWristUpMotion();
                this.mEventContext.setValues(context);
                return;
            case 20:
                this.mEventContext = new SContextFlatMotion();
                this.mEventContext.setValues(context);
                return;
            case 21:
                this.mEventContext = new SContextMovementAlert();
                this.mEventContext.setValues(context);
                return;
            case 22:
                this.mEventContext = new SContextDevicePosition();
                this.mEventContext.setValues(context);
                return;
            case 23:
                this.mEventContext = new SContextTemperatureAlert();
                this.mEventContext.setValues(context);
                return;
            case 24:
                this.mEventContext = new SContextActivityLocationLogging();
                this.mEventContext.setValues(context);
                return;
            case 25:
                this.mEventContext = new SContextActivityTracker();
                this.mEventContext.setValues(context);
                return;
            case 26:
                this.mEventContext = new SContextActivityBatch();
                this.mEventContext.setValues(context);
                return;
            case 27:
                this.mEventContext = new SContextActivityNotification();
                this.mEventContext.setValues(context);
                return;
            case 28:
                this.mEventContext = new SContextSpecificPoseAlert();
                this.mEventContext.setValues(context);
                return;
            case 29:
                this.mEventContext = new SContextSleepMonitor();
                this.mEventContext.setValues(context);
                return;
            case 30:
                this.mEventContext = new SContextActivityNotificationEx();
                this.mEventContext.setValues(context);
                return;
            case 31:
                this.mEventContext = new SContextCaptureMotion();
                this.mEventContext.setValues(context);
                return;
            case 32:
                this.mEventContext = new SContextCallMotion();
                this.mEventContext.setValues(context);
                return;
            case 33:
                this.mEventContext = new SContextStepLevelMonitor();
                this.mEventContext.setValues(context);
                return;
            case 34:
                this.mEventContext = new SContextActiveTimeMonitor();
                this.mEventContext.setValues(context);
                return;
            case 35:
                this.mEventContext = new SContextInactiveTimer();
                this.mEventContext.setValues(context);
                return;
            case 36:
                this.mEventContext = new SContextFlatMotionForTableMode();
                this.mEventContext.setValues(context);
                return;
            case 39:
                this.mEventContext = new SContextAutoBrightness();
                this.mEventContext.setValues(context);
                return;
            case 40:
                this.mEventContext = new SContextExercise();
                this.mEventContext.setValues(context);
                return;
            case 41:
                this.mEventContext = new SContextAbnormalPressure();
                this.mEventContext.setValues(context);
                return;
            case 42:
                this.mEventContext = new SContextPhoneStatusMonitor();
                this.mEventContext.setValues(context);
                return;
            case 43:
                this.mEventContext = new SContextHallSensor();
                this.mEventContext.setValues(context);
                return;
            case 44:
                this.mEventContext = new SContextEnvironmentAdaptiveDisplay();
                this.mEventContext.setValues(context);
                return;
            case 45:
                this.mEventContext = new SContextDualDisplayAngle();
                this.mEventContext.setValues(context);
                return;
            case 46:
                this.mEventContext = new SContextWirelessChargingDetection();
                this.mEventContext.setValues(context);
                return;
            case 47:
                this.mEventContext = new SContextSLocationCore();
                this.mEventContext.setValues(context);
                return;
            case 49:
                this.mEventContext = new SContextFlipMotion();
                this.mEventContext.setValues(context);
                this.mDuplicatedEventContext = new SContextMainScreenDetection();
                this.mDuplicatedEventContext.setValues(context);
                return;
            case 50:
                this.mEventContext = new SContextAnyMotionDetector();
                this.mEventContext.setValues(context);
                return;
            case 51:
                this.mEventContext = new SContextDevicePhysicalContextMonitor();
                this.mEventContext.setValues(context);
                return;
            case 52:
                this.mEventContext = new SContextSensorStatusCheck();
                this.mEventContext.setValues(context);
                return;
            default:
                return;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.timestamp);
        dest.writeParcelable(this.scontext, flags);
        dest.writeParcelable(this.mEventContext, flags);
        if (this.scontext.getType() == 49) {
            dest.writeParcelable(this.mDuplicatedEventContext, flags);
        }
    }

    private void readFromParcel(Parcel src) {
        this.timestamp = src.readLong();
        this.scontext = (SContext) src.readParcelable(SContext.class.getClassLoader());
        this.mEventContext = (SContextEventContext) src.readParcelable(SContextEventContext.class.getClassLoader());
        if (this.scontext.getType() == 49) {
            this.mDuplicatedEventContext = (SContextEventContext) src.readParcelable(SContextEventContext.class.getClassLoader());
        }
    }
}
