package com.samsung.android.fingerprint;

import android.os.Bundle;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.SparseArray;
import java.lang.reflect.Field;

public final class FingerprintEvent implements Parcelable {
    public static final String CAPTURE_STATUS_KEY_BADSWIPES = "badSwipes";
    public static final String CAPTURE_STATUS_KEY_IMAGEQUALITY = "imageQuality";
    public static final String CAPTURE_STATUS_KEY_IMAGEQUALITY_FEEDBACK = "imageQualityFeedback";
    public static final String CAPTURE_STATUS_KEY_IMAGEQUALITY_RSRC_ID = "imageQualityRsrcId";
    public static final String CAPTURE_STATUS_KEY_PROGRESS = "progress";
    public static final String CAPTURE_STATUS_KEY_TEMPLATERESULT = "templateResult";
    public static final String CAPTURE_STATUS_KEY_TOTALSWIPES = "totalSwipes";
    public static final Creator<FingerprintEvent> CREATOR = new Creator<FingerprintEvent>() {
        public FingerprintEvent createFromParcel(Parcel in) {
            return new FingerprintEvent(in);
        }

        public FingerprintEvent[] newArray(int size) {
            return new FingerprintEvent[size];
        }
    };
    private static final boolean DEBUG;
    public static final int EVENT_ENROLL_FINISHED = 4;
    public static final int EVENT_ENROLL_READY = 1;
    public static final int EVENT_ENROLL_SETTLED = 5;
    public static final int EVENT_ENROLL_STARTED = 2;
    public static final int EVENT_ENROLL_STATUS = 3;
    public static final int EVENT_FACTORY_APP = 999;
    public static final int EVENT_FINGER_REMOVED = 1011;
    public static final int EVENT_FINGER_SWIPE_SPEED = 1013;
    public static final int EVENT_GESTURE = 1010;
    public static final int EVENT_IDENTIFY_COMPLETED = 16;
    public static final int EVENT_IDENTIFY_FINISHED = 13;
    public static final int EVENT_IDENTIFY_READY = 11;
    public static final int EVENT_IDENTIFY_SETTLED = 15;
    public static final int EVENT_IDENTIFY_STARTED = 12;
    public static final int EVENT_IDENTIFY_STATUS = 14;
    public static final int EVENT_IDLE = 10000;
    public static final int EVENT_NAVIGATION_EVENT = 1000;
    public static final int EVENT_SERVICE_DIED = 100000;
    public static final int EVENT_SERVICE_PREPARED = 200000;
    public static final String FACTORY_APP_ID = "FACTORY_APP_ID";
    public static final String IDENTIFY_INFO_KEY_FINGERINDEX = "fingerIndex";
    public static final String IDENTIFY_INFO_KEY_PERMISSION = "permission";
    public static final String IDENTIFY_INFO_KEY_USER_ID = "userId";
    public static final int IMAGE_QUALITY_BAD_SWIPE = 8192;
    public static final int IMAGE_QUALITY_EXTRACTION_FAILURE = 1073741824;
    public static final int IMAGE_QUALITY_FINGER_OFFSET = 4096;
    public static final int IMAGE_QUALITY_FINGER_OFFSET_TOO_FAR_LEFT = 131072;
    public static final int IMAGE_QUALITY_FINGER_OFFSET_TOO_FAR_RIGHT = 262144;
    public static final int IMAGE_QUALITY_FINGER_TOO_THIN = 33554432;
    public static final int IMAGE_QUALITY_GOOD = 0;
    public static final int IMAGE_QUALITY_NO_OVERLAP = 1342177280;
    public static final int IMAGE_QUALITY_PATAIL_TOUCH = 1610612736;
    public static final int IMAGE_QUALITY_PRESSURE_TOO_HARD = 524288;
    public static final int IMAGE_QUALITY_PRESSURE_TOO_LIGHT = 65536;
    public static final int IMAGE_QUALITY_REVERSE_MOTION = 3;
    public static final int IMAGE_QUALITY_SAME_AS_PREVIOUS = 805306368;
    public static final int IMAGE_QUALITY_SKEW_TOO_LARGE = 32768;
    public static final int IMAGE_QUALITY_SOMETHING_ON_THE_SENSOR = 512;
    public static final int IMAGE_QUALITY_STICTION = 1;
    public static final int IMAGE_QUALITY_TOO_FAST = 2;
    public static final int IMAGE_QUALITY_TOO_SHORT = 4;
    public static final int IMAGE_QUALITY_TOO_SLOW = 16;
    public static final int IMAGE_QUALITY_WET_FINGER = 16777216;
    public static final int IMAGE_QUALITY_WRONG_FINGER = 536870912;
    public static final int NOTIFY_ACTIVITY_DESTROYED = 3;
    public static final int NOTIFY_ACTIVITY_PAUSED = 1;
    public static final int NOTIFY_ACTIVITY_STOPPED = 2;
    public static final int NOTIFY_DIALOG_DISMISS_REQUEST = 4;
    public static final int RESULT_DATABASE_FAILURE = -4;
    public static final int RESULT_FAILED = -1;
    public static final int RESULT_INVALID_TOKEN = -3;
    public static final int RESULT_IN_PROGRESS = -2;
    public static final int RESULT_NO_REGISTERED_FINGER = -5;
    public static final int RESULT_OK = 0;
    public static final int RESULT_SUCCESS = 0;
    public static final int STATUS_ALREADY_REGISTERED = 1;
    public static final int STATUS_ALTERNATIVE_PASSWORD = 100;
    public static final int STATUS_BACKUP_BUTTON_PRESSED = 9;
    public static final int STATUS_CAPTURE_COMPLETED = 21;
    public static final int STATUS_CAPTURE_FAILED = 20;
    public static final int STATUS_DEVICE_NEED_RECAL = 110;
    public static final int STATUS_ENROLL_FAILURE_SERVICE_FAILURE = 120;
    public static final int STATUS_GOOD = 0;
    public static final int STATUS_IDENTIFY_FAILURE_DATABASE_FAILURE = 122;
    public static final int STATUS_IDENTIFY_FAILURE_SENSOR_CHANGED = 123;
    public static final int STATUS_IDENTIFY_FAILURE_SERVICE_FAILURE = 121;
    public static final int STATUS_OPERATION_DENIED = 51;
    public static final int STATUS_QUALITY_FAILED = 12;
    public static final int STATUS_SENSOR_CHANGED = 14;
    public static final int STATUS_SENSOR_ERROR = 7;
    public static final int STATUS_TIMEOUT = 4;
    public static final int STATUS_UNKNOWN = 16;
    public static final int STATUS_USER_CANCELLED = 8;
    public static final int STATUS_USER_CANCELLED_BY_TOUCH_OUTSIDE = 13;
    public static final int STATUS_USER_VERIFICATION_FAILED = 11;
    public static final int SWIPE_DIRECTION_DOWN = 1;
    public static final int SWIPE_DIRECTION_LEFT = 3;
    public static final int SWIPE_DIRECTION_REST = 5;
    public static final int SWIPE_DIRECTION_RIGHT = 4;
    public static final int SWIPE_DIRECTION_UNKNOWN = -1;
    public static final int SWIPE_DIRECTION_UP = 0;
    public static final String SWIPE_KEY_DIRECTION = "direction";
    public static final String SWIPE_KEY_SPEED = "swipeSpeed";
    private static final String TAG = "FPMS_FingerprintEvent";
    private static final SparseArray<String> eventIdNameMap = new SparseArray();
    private static final SparseArray<String> eventImageQualityNameMap = new SparseArray();
    private static final SparseArray<String> eventResultNameMap = new SparseArray();
    private static final SparseArray<String> eventStatusNameMap = new SparseArray();
    private static final SparseArray<String> eventSwipeDirectionMap = new SparseArray();
    public final Bundle eventData = new Bundle();
    public int eventId = 0;
    public int eventResult = 0;
    public int eventStatus = 0;

    static {
        boolean z;
        if (Debug.isProductShip() == 0) {
            z = true;
        } else {
            z = false;
        }
        DEBUG = z;
        generateNameMap(eventIdNameMap, "EVENT_");
        generateNameMap(eventResultNameMap, "RESULT_");
        generateNameMap(eventStatusNameMap, "STATUS_");
        generateNameMap(eventImageQualityNameMap, "IMAGE_");
        generateNameMap(eventSwipeDirectionMap, "SWIPE_DIRECTION_");
    }

    public FingerprintEvent(int eventId) {
        this.eventId = eventId;
    }

    FingerprintEvent(Parcel in) {
        this.eventId = in.readInt();
        this.eventResult = in.readInt();
        this.eventStatus = in.readInt();
        this.eventData.readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.eventId);
        dest.writeInt(this.eventResult);
        dest.writeInt(this.eventStatus);
        this.eventData.writeToParcel(dest, flags);
    }

    public int getBadSwipes() {
        return this.eventData.getInt(CAPTURE_STATUS_KEY_BADSWIPES);
    }

    public int getImageQuality() {
        return this.eventData.getInt(CAPTURE_STATUS_KEY_IMAGEQUALITY);
    }

    public String getImageQualityFeedback() {
        return this.eventData.getString(CAPTURE_STATUS_KEY_IMAGEQUALITY_FEEDBACK);
    }

    public int getImageQualityRsrcId() {
        return this.eventData.getInt(CAPTURE_STATUS_KEY_IMAGEQUALITY_RSRC_ID);
    }

    public int getProgress() {
        return this.eventData.getInt(CAPTURE_STATUS_KEY_PROGRESS);
    }

    public int getTemplateResult() {
        return this.eventData.getInt(CAPTURE_STATUS_KEY_TEMPLATERESULT);
    }

    public int getTotalSwipes() {
        return this.eventData.getInt(CAPTURE_STATUS_KEY_TOTALSWIPES);
    }

    public int getIdentifiedUserId() {
        return this.eventData.getInt(IDENTIFY_INFO_KEY_USER_ID, -1);
    }

    public int getFingerIndex() {
        return this.eventData.getInt(IDENTIFY_INFO_KEY_FINGERINDEX, 0);
    }

    private int getSwipeDirection() {
        return this.eventData.getInt(SWIPE_KEY_DIRECTION, -1);
    }

    public int getSwipeSpeed() {
        return this.eventData.getInt(SWIPE_KEY_SPEED, 0);
    }

    private static void generateNameMap(SparseArray<String> map, String prefix) {
        if (prefix != null) {
            for (Field field : FingerprintEvent.class.getFields()) {
                if (field.getName().startsWith(prefix)) {
                    try {
                        map.put(field.getInt(null), field.getName());
                    } catch (Exception e) {
                        Log.e(TAG, "generateNameMap: failed ");
                        if (DEBUG && e != null) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public String getEventIdName() {
        return (String) eventIdNameMap.get(this.eventId);
    }

    public String getEventResultName() {
        return (String) eventResultNameMap.get(this.eventResult);
    }

    public String getEventStatusName() {
        return (String) eventStatusNameMap.get(this.eventStatus);
    }

    public String getImageQualityName() {
        return (String) eventImageQualityNameMap.get(getImageQuality());
    }

    public String getSwipeDirectionName() {
        return (String) eventSwipeDirectionMap.get(getSwipeDirection());
    }
}
