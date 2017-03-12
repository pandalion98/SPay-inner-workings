package android.os;

import android.os.Parcelable.Creator;

public final class PersonaHandle implements Parcelable {
    public static final PersonaHandle ALL = new PersonaHandle(-1);
    public static final Creator<PersonaHandle> CREATOR = new Creator<PersonaHandle>() {
        public PersonaHandle createFromParcel(Parcel in) {
            return new PersonaHandle(in);
        }

        public PersonaHandle[] newArray(int size) {
            return new PersonaHandle[size];
        }
    };
    public static final PersonaHandle CURRENT = new PersonaHandle(-2);
    public static final PersonaHandle CURRENT_OR_SELF = new PersonaHandle(-3);
    public static final boolean MU_ENABLED = true;
    public static final PersonaHandle OWNER = new PersonaHandle(0);
    public static final int PER_USER_RANGE = 100000;
    public static final int USER_ALL = -1;
    public static final int USER_CURRENT = -2;
    public static final int USER_CURRENT_OR_SELF = -3;
    public static final int USER_NULL = -10000;
    public static final int USER_OWNER = 0;
    final int mHandle;

    public static final boolean isSame(int uid1, int uid2) {
        return getPersonaId(uid1) == getPersonaId(uid2);
    }

    public static final boolean isSameApp(int uid1, int uid2) {
        return getAppId(uid1) == getAppId(uid2);
    }

    public static final boolean isIsolated(int uid) {
        if (uid <= 0) {
            return false;
        }
        int appId = getAppId(uid);
        if (appId < Process.FIRST_ISOLATED_UID || appId > Process.LAST_ISOLATED_UID) {
            return false;
        }
        return true;
    }

    public static boolean isApp(int uid) {
        if (uid <= 0) {
            return false;
        }
        int appId = getAppId(uid);
        if (appId < 10000 || appId > Process.LAST_APPLICATION_UID) {
            return false;
        }
        return true;
    }

    public static final int getPersonaId(int uid) {
        return uid / 100000;
    }

    public static final int getCallingPersonaId() {
        return getPersonaId(Binder.getCallingUid());
    }

    public static final int getUid(int userId, int appId) {
        return (userId * 100000) + (appId % 100000);
    }

    public static final int getAppId(int uid) {
        return uid % 100000;
    }

    public static final int getSharedAppGid(int id) {
        return (50000 + (id % 100000)) - 10000;
    }

    public static final int myPersonaId() {
        return getPersonaId(Process.myUid());
    }

    public PersonaHandle(int h) {
        this.mHandle = h;
    }

    public int getIdentifier() {
        return this.mHandle;
    }

    public String toString() {
        return "PersonaHandle{" + this.mHandle + "}";
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            if (this.mHandle == ((PersonaHandle) obj).mHandle) {
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return this.mHandle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mHandle);
    }

    public static void writeToParcel(PersonaHandle h, Parcel out) {
        if (h != null) {
            h.writeToParcel(out, 0);
        } else {
            out.writeInt(-10000);
        }
    }

    public static PersonaHandle readFromParcel(Parcel in) {
        int h = in.readInt();
        return h != -10000 ? new PersonaHandle(h) : null;
    }

    public PersonaHandle(Parcel in) {
        this.mHandle = in.readInt();
    }
}
