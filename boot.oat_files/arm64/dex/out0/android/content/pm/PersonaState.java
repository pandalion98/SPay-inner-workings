package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum PersonaState implements Parcelable {
    INVALID(-1),
    CREATING(1),
    ACTIVE(0),
    LOCKED(2),
    SUPER_LOCKED(-1),
    LICENSE_LOCKED(9),
    ADMIN_LOCKED(8),
    ADMIN_LICENSE_LOCKED(-1),
    TERMINUS(-1),
    DELETING(3),
    TIMA_COMPROMISED(7),
    CONTAINER_APPS_URGENT_UPDATE(-1);
    
    public static final Creator<PersonaState> CREATOR = null;
    private int knox2_0_state_id;

    static {
        CREATOR = new Creator<PersonaState>() {
            public PersonaState createFromParcel(Parcel source) {
                return PersonaState.valueOf(source.readString());
            }

            public PersonaState[] newArray(int size) {
                return new PersonaState[size];
            }
        };
    }

    private PersonaState(int knox2_0_state_id) {
        this.knox2_0_state_id = -1;
        this.knox2_0_state_id = knox2_0_state_id;
    }

    public int getKnox2_0State() {
        return this.knox2_0_state_id;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }
}
