package android.content.pm;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PersonaEvent implements Parcelable {
    public static final Creator<PersonaEvent> CREATOR = new Creator<PersonaEvent>() {
        public PersonaEvent createFromParcel(Parcel source) {
            return new PersonaEvent(source);
        }

        public PersonaEvent[] newArray(int size) {
            return new PersonaEvent[size];
        }
    };
    public static final int EVENT_CREATE_COMPLETE = 1;
    public static final int EVENT_INVALID = -1;
    public static final int EVENT_KLMS_LOCK = 12;
    public static final int EVENT_KLMS_UNLOCK = 13;
    public static final int EVENT_LOCK = 0;
    public static final int EVENT_SETUP_COMPLETE = 2;
    public static final int EVENT_UNLOCK = 3;
    public static final int KNOX_EVENT_ADMIN_LOCK = 11;
    public static final int KNOX_EVENT_ADMIN_UNLOCK = 6;
    public static final int KNOX_EVENT_DELETE_PERSONA = 8;
    public static final int KNOX_EVENT_DISABLE_KEYGUARD = 14;
    public static final int KNOX_EVENT_ENABLE_KEYGUARD = 15;
    public static final int KNOX_EVENT_FORCE_PASSWORD_RESET = 4;
    public static final int KNOX_EVENT_PASSWORD_CHANGE_SUCCESS = 5;
    public static final int KNOX_EVENT_TIMA_COMPROMISE = 10;
    public static final int KNOX_EVENT_UPGRADE_COMPLETE = 7;
    public static final int KNOX_EVENT_UPGRADE_PERSONA = 9;
    private static final String[] readableStrings = new String[]{"KNOX_EVENT_LOCK", "KNOX_EVENT_CREATE_COMPLETE", "KNOX_EVENT_SETUP_COMPLETE", "KNOX_EVENT_UNLOCK", "KNOX_EVENT_FORCE_PASSWORD_RESET", "KNOX_EVENT_PASSWORD_CHANGE_SUCCESS", "KNOX_EVENT_ADMIN_UNLOCK", "KNOX_EVENT_UPGRADE_COMPLETE", "KNOX_EVENT_DELETE_PERSONA", "KNOX_EVENT_UPGRADE_PERSONA", "KNOX_EVENT_TIMA_COMPROMISE", "KNOX_EVENT_ADMIN_LOCK", "KNOX_EVENT_KLMS_LOCK", "KNOX_EVENT_KLMS_UNLOCK", "KNOX_EVENT_DISABLE_KEYGUARD", "KNOX_EVENT_ENABLE_KEYGUARD"};
    private int event;
    private Bundle extras;

    public static String toReadableString(int id) {
        if (id < 0 || id >= readableStrings.length) {
            return "KNOX_EVENT_INVALID";
        }
        return readableStrings[id];
    }

    public PersonaEvent(int event) {
        this.event = event;
    }

    public PersonaEvent(Parcel source) {
        this.event = source.readInt();
        this.extras = source.readBundle();
    }

    public int getEvent() {
        return this.event;
    }

    public PersonaEvent setEvent(int event) {
        this.event = event;
        return this;
    }

    public PersonaEvent putExtra(String name, String value) {
        if (this.extras == null) {
            this.extras = new Bundle();
        }
        this.extras.putString(name, value);
        return this;
    }

    public PersonaEvent putExtra(String name, int value) {
        if (this.extras == null) {
            this.extras = new Bundle();
        }
        this.extras.putInt(name, value);
        return this;
    }

    public String getStringExtra(String name) {
        return this.extras == null ? null : this.extras.getString(name);
    }

    public int getIntExtra(String name, int defaultValue) {
        return this.extras == null ? defaultValue : this.extras.getInt(name, defaultValue);
    }

    public int describeContents() {
        return this.extras != null ? this.extras.describeContents() : 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.event);
        dest.writeBundle(this.extras);
    }
}
