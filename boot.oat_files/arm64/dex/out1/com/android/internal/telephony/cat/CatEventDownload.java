package com.android.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CatEventDownload implements Parcelable {
    public static Creator<CatEventDownload> CREATOR = new Creator<CatEventDownload>() {
        public CatEventDownload createFromParcel(Parcel source) {
            int eventType = source.readInt();
            String language = source.readString();
            int browserTerminationCause = source.readInt();
            switch (eventType) {
                case 4:
                case 5:
                    return new CatEventDownload(eventType);
                case 7:
                    return new CatEventDownload(eventType, language);
                case 8:
                    return new CatEventDownload(eventType, browserTerminationCause);
                default:
                    return new CatEventDownload(254);
            }
        }

        public CatEventDownload[] newArray(int size) {
            return new CatEventDownload[size];
        }
    };
    public static final int EVENT_ACCESS_TECHNOLOGY_CHANGE = 11;
    public static final int EVENT_BROWSER_TERMINATION = 8;
    public static final int EVENT_BROWSING_STATUS = 15;
    public static final int EVENT_CALL_CONNECTED = 1;
    public static final int EVENT_CALL_DISCONNECTED = 2;
    public static final int EVENT_CARD_READER_STATUS = 6;
    public static final int EVENT_CHANNEL_STATUS = 10;
    public static final int EVENT_DATA_AVAILABLE = 9;
    public static final int EVENT_DISPLAY_PARAMETERS_CHANGE = 12;
    public static final int EVENT_FRAMES_INFORMATION_CHANGE = 16;
    public static final int EVENT_IDLE_SCREEN_AVAILABLE = 5;
    public static final int EVENT_LENGUAGE_SELECTION = 7;
    public static final int EVENT_LOCAL_CONNECTION = 13;
    public static final int EVENT_LOCATION_STATUS = 3;
    public static final int EVENT_MT_CALL = 0;
    public static final int EVENT_NETWORK_REJECTION = 18;
    public static final int EVENT_NETWORK_SEARCH_MODE_CHANGE = 14;
    public static final int EVENT_NOTHING = 254;
    public static final int EVENT_REMOVE = 255;
    public static final int EVENT_USER_ACTIVITY = 4;
    public static final int MAX_EVENTS_NUM = 20;
    public static final String STK_EVENT_ACTION = "com.samsung.intent.action.stk.event";
    public static final int TERMINATED_BY_ERROR = 1;
    public static final int TERMINATED_BY_USER = 0;
    public static final String UTK_EVENT_ACTION = "com.samsung.intent.action.utk.event";
    private int mBrowserTerminationCause = 0;
    private int mEvent = 254;
    private String mLanguage = null;

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mEvent);
        dest.writeString(this.mLanguage);
        dest.writeInt(this.mBrowserTerminationCause);
    }

    public int describeContents() {
        return 0;
    }

    public CatEventDownload(int event) {
        setEvent(event);
    }

    public CatEventDownload(int event, int browserTerminationCause) {
        setEvent(event);
        setBrowserTerminationCause(browserTerminationCause);
    }

    public CatEventDownload(int event, String language) {
        setEvent(event);
        setLenguage(language);
    }

    public void setEvent(int event) {
        this.mEvent = event;
    }

    public int getEvent() {
        return this.mEvent;
    }

    public void setLenguage(String language) {
        this.mLanguage = language;
    }

    public String getLanguage() {
        return this.mLanguage;
    }

    public int getBrowserTerminationCause() {
        return this.mBrowserTerminationCause;
    }

    public void setBrowserTerminationCause(int browserTerminationCause) {
        this.mBrowserTerminationCause = browserTerminationCause;
    }
}
