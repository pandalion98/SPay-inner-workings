package android.hardware.scontext;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SContext implements Parcelable {
    public static final Creator<SContext> CREATOR = new Creator<SContext>() {
        public SContext createFromParcel(Parcel in) {
            return new SContext(in);
        }

        public SContext[] newArray(int size) {
            return new SContext[size];
        }
    };
    static final int REPORTING_MODE_BATCH = 5;
    static final int REPORTING_MODE_CONTINUOUS = 1;
    static final int REPORTING_MODE_ONE_SHOT = 2;
    static final int REPORTING_MODE_ON_CHANGE = 3;
    static final int REPORTING_MODE_ON_CHANGE_AND_INITIAL_INFO = 4;
    public static final int TYPE_ABNORMAL_PRESSURE = 41;
    public static final int TYPE_ACTIVE_TIME_MONITOR = 34;
    public static final int TYPE_ACTIVITY_BATCH = 26;
    public static final int TYPE_ACTIVITY_LOCATION_LOGGING = 24;
    public static final int TYPE_ACTIVITY_NOTIFICATION = 27;
    public static final int TYPE_ACTIVITY_NOTIFICATION_EX = 30;
    public static final int TYPE_ACTIVITY_TRACKER = 25;
    public static final int TYPE_AIRMOTION = 7;
    public static final int TYPE_ANY_MOTION_DETECTOR = 50;
    public static final int TYPE_APPROACH = 1;
    public static final int TYPE_AUTO_BRIGHTNESS = 39;
    public static final int TYPE_AUTO_ROTATION = 6;
    @Deprecated
    public static final int TYPE_BOTTOM_FLAT_DETECTOR = 38;
    public static final int TYPE_BOUNCE_LONG_MOTION = 18;
    public static final int TYPE_BOUNCE_SHORT_MOTION = 17;
    public static final int TYPE_CALL_MOTION = 32;
    public static final int TYPE_CALL_POSE = 11;
    @Deprecated
    public static final int TYPE_CAPTURE_MOTION = 31;
    @Deprecated
    public static final int TYPE_CARRYING_STATUS_MONITOR = 37;
    @Deprecated
    public static final int TYPE_CURRENT_STATUS_FOR_POSITIONING = 10;
    public static final int TYPE_DEVICE_PHYSICAL_CONTEXT_MONITOR = 51;
    public static final int TYPE_DEVICE_POSITION = 22;
    public static final int TYPE_DUAL_DISPLAY_ANGLE = 45;
    @Deprecated
    public static final int TYPE_ENVIRONMENT = 8;
    public static final int TYPE_ENVIRONMENT_ADAPTIVE_DISPLAY = 44;
    public static final int TYPE_EXERCISE = 40;
    public static final int TYPE_FLAT_MOTION = 20;
    public static final int TYPE_FLAT_MOTION_FOR_TABLE_MODE = 36;
    public static final int TYPE_FLIP_COVER_ACTION = 13;
    public static final int TYPE_FLIP_MOTION = 49;
    public static final int TYPE_GYRO_TEMPERATURE = 14;
    public static final int TYPE_HALL_SENSOR = 43;
    public static final int TYPE_INACTIVE_TIMER = 35;
    public static final int TYPE_INTERRUPT_GYRO = 48;
    public static final int TYPE_MAIN_SCREEN_DETECTION = 49;
    @Deprecated
    public static final int TYPE_MOTION = 4;
    public static final int TYPE_MOVEMENT = 5;
    @Deprecated
    public static final int TYPE_MOVEMENT_ALERT = 21;
    @Deprecated
    public static final int TYPE_MOVEMENT_FOR_POSITIONING = 9;
    public static final int TYPE_PEDOMETER = 2;
    public static final int TYPE_PHONE_STATUS_MONITOR = 42;
    public static final int TYPE_PUT_DOWN_MOTION = 15;
    public static final int TYPE_SENSOR_STATUS_CHECK = 52;
    public static final int TYPE_SHAKE_MOTION = 12;
    @Deprecated
    public static final int TYPE_SLEEP_MONITOR = 29;
    public static final int TYPE_SLOCATION_CORE = 47;
    public static final int TYPE_SPECIFIC_POSE_ALERT = 28;
    public static final int TYPE_STEP_COUNT_ALERT = 3;
    public static final int TYPE_STEP_LEVEL_MONITOR = 33;
    @Deprecated
    public static final int TYPE_TEMPERATURE_ALERT = 23;
    @Deprecated
    public static final int TYPE_TEST_FLAT_MOTION = 22;
    public static final int TYPE_WAKE_UP_VOICE = 16;
    public static final int TYPE_WIRELESS_CHARGING_DETECTION = 46;
    @Deprecated
    public static final int TYPE_WRIST_UP_MOTION = 19;
    private static final String[] sServiceList = new String[]{"Approach", "Pedometer", "S Count Alert", "Motion", "Movement", "Auto Rotation", "Air Motion", "Environment", "Movemnt For Positioning", "Current Status For Positioning", "Call Pose", "Shake Motion", "Flip Cover Action", "Gyro Temperature", "Put Down Motion", "Wake Up Voice", "Bounce Short Motion", "Bounce Long Motion", "Wrist Up Motion", "Flat Motion", "Movement Alert", "Device Position", "Temperature Alert", "Activity Location Logging", "Activity Tracker", "Activity Batch", "Activity Notification", "Specific Pose Alert", "Sleep Monitor", "Activity Notification Ex", "Capture Motion", "Call Motion", "Step Level Monitor", "Acitve Time Monitor", "Inactive Timer", "Flat Motion For Table Mode", "Carrying Status Monitor", "Bottom Flat Detector", "Auto Brightness", "Exercise", "Abnormal Pressure", "Phone Status Monitor", "Hall Sensor", "Environment Adaptive Display", "Dual Display Angle", "Wireless Charging Detection", "Slocation Core", "Interrupt Gyro", "Flip Motion", "Any Motion Detector", "Device Physical Context Monitor", "Sensor Status Check"};
    private static final int[] sServiceReportingModes = new int[]{3, 3, 2, 3, 2, 4, 3, 1, 3, 3, 3, 3, 3, 1, 3, 3, 3, 3, 3, 4, 2, 4, 2, 3, 3, 5, 4, 3, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 4, 4, 3, 3, 4, 2, 3, 4};
    private int mType;

    SContext() {
        this.mType = 0;
    }

    SContext(Parcel src) {
        readFromParcel(src);
    }

    public static String getServiceName(int service) {
        if (service <= sServiceList.length) {
            return sServiceList[service - 1];
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }

    public static int getReportingMode(int service) {
        if (service <= sServiceReportingModes.length) {
            return sServiceReportingModes[service - 1];
        }
        return 0;
    }

    public int getType() {
        return this.mType;
    }

    void setType(int type) {
        this.mType = type;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
    }

    private void readFromParcel(Parcel src) {
        this.mType = src.readInt();
    }
}
