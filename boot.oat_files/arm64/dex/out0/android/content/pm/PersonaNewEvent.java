package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum PersonaNewEvent implements Parcelable {
    USER_LOCK,
    USER_UNLOCK,
    ADMIN_LOCK,
    ADMIN_UNLOCK,
    LICENSE_LOCK,
    LICENSE_UNLOCK,
    REMOVE,
    TIMA_COMPROMISED,
    URGENT_UPDATE;
    
    public static final Creator<PersonaNewEvent> CREATOR = null;

    static {
        CREATOR = new Creator<PersonaNewEvent>() {
            public PersonaNewEvent createFromParcel(Parcel source) {
                return PersonaNewEvent.valueOf(source.readString());
            }

            public PersonaNewEvent[] newArray(int size) {
                return new PersonaNewEvent[size];
            }
        };
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }
}
