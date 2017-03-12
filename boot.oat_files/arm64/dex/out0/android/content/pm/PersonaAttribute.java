package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum PersonaAttribute implements Parcelable {
    UPGRADING,
    PASSWORD_CHANGE_REQUEST,
    RESETTING,
    DISABLE_KNOX_KEYGUARD,
    PASSWORD_CHANGE_REQUEST_ENFORCED,
    QUICK_UNLOCK_NOT_AVAILABLE,
    MY_KNOX;
    
    public static final Creator<PersonaAttribute> CREATOR = null;

    static {
        CREATOR = new Creator<PersonaAttribute>() {
            public PersonaAttribute createFromParcel(Parcel source) {
                return PersonaAttribute.valueOf(source.readString());
            }

            public PersonaAttribute[] newArray(int size) {
                return new PersonaAttribute[size];
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
