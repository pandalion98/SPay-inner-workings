package com.samsung.android.service.gesture;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GestureEvent implements Parcelable {
    public static final Creator<GestureEvent> CREATOR = new Creator<GestureEvent>() {
        public GestureEvent createFromParcel(Parcel source) {
            return new GestureEvent(source);
        }

        public GestureEvent[] newArray(int size) {
            return new GestureEvent[size];
        }
    };
    public static final int GESTURE_EVENT_APPROACH = 2;
    public static final int GESTURE_EVENT_HANDSHAKE = 6;
    public static final int GESTURE_EVENT_HOVER = 5;
    public static final int GESTURE_EVENT_SWEEP_DOWN = 4;
    public static final int GESTURE_EVENT_SWEEP_LEFT = 1;
    public static final int GESTURE_EVENT_SWEEP_RIGHT = 0;
    public static final int GESTURE_EVENT_SWEEP_UP = 3;
    private static final int MAX_POOL_SIZE = 50;
    private static GestureEvent mPool;
    private static int mPoolSize = 0;
    private static final GestureEvent mPoolSync = new GestureEvent();
    private int mEvent;
    private GestureEvent mNextLink;
    private String mProvider;

    public GestureEvent() {
        this.mEvent = 0;
    }

    public GestureEvent(Parcel src) {
        readFromParcel(src);
    }

    public int getEvent() {
        return this.mEvent;
    }

    public void setEvent(int event) {
        this.mEvent = event;
    }

    public String getProvider() {
        return this.mProvider;
    }

    public void setProvider(String provider) {
        this.mProvider = provider;
    }

    @Deprecated
    public int describeContents() {
        return 0;
    }

    @Deprecated
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mEvent);
        dest.writeString(this.mProvider);
    }

    private void readFromParcel(Parcel src) {
        this.mEvent = src.readInt();
        this.mProvider = src.readString();
    }

    public static GestureEvent obtain() {
        synchronized (mPoolSync) {
            if (mPool != null) {
                GestureEvent e = mPool;
                mPool = e.mNextLink;
                e.mNextLink = null;
                mPoolSize--;
                return e;
            }
            return new GestureEvent();
        }
    }

    public static GestureEvent obtain(GestureEvent orig) {
        GestureEvent e = obtain();
        e.mEvent = orig.mEvent;
        e.mProvider = orig.mProvider;
        e.mNextLink = orig.mNextLink;
        return e;
    }

    public void recycle() {
        clearForRecycle();
        synchronized (mPoolSync) {
            if (mPoolSize < 50) {
                this.mNextLink = mPool;
                mPool = this;
                mPoolSize++;
            }
        }
    }

    void clearForRecycle() {
        this.mEvent = -1;
        this.mProvider = null;
        this.mNextLink = null;
    }
}
