package com.samsung.android.dualscreen;

import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public enum DualScreen implements Parcelable {
    MAIN(0),
    SUB(1),
    FULL(2),
    INPUT_METHOD(3),
    EXTERNAL(4),
    UNKNOWN(-1),
    EXPANDED(2);
    
    public static final Creator<DualScreen> CREATOR = null;
    private static final String TAG = "DualScreen";
    private int displayId;

    static {
        CREATOR = new Creator<DualScreen>() {
            public DualScreen createFromParcel(Parcel in) {
                int index = in.readInt();
                if (index >= 0 && index < DualScreen.values().length) {
                    return DualScreen.values()[index];
                }
                if (DualScreenManager.DEBUG_WARNING) {
                    Log.w(DualScreen.TAG, "wrong index !! index=" + index + " " + Debug.getCallers(12));
                }
                return DualScreen.MAIN;
            }

            public DualScreen[] newArray(int size) {
                return new DualScreen[size];
            }
        };
    }

    private DualScreen(int displayId) {
        this.displayId = displayId;
    }

    public static DualScreen displayIdToScreen(int displayId) {
        if (displayId == MAIN.getDisplayId()) {
            return MAIN;
        }
        if (displayId == SUB.getDisplayId()) {
            return SUB;
        }
        if (displayId == FULL.getDisplayId()) {
            return FULL;
        }
        if (displayId == INPUT_METHOD.getDisplayId()) {
            return INPUT_METHOD;
        }
        if (displayId == EXTERNAL.getDisplayId()) {
            return EXTERNAL;
        }
        return UNKNOWN;
    }

    public int getDisplayId() {
        return this.displayId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ordinal());
    }
}
