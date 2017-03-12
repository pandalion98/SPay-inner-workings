package android.os;

import android.app.SearchManager;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import java.io.PrintWriter;

public final class UserHandle implements Parcelable {
    public static final UserHandle ALL = new UserHandle(-1);
    public static final Creator<UserHandle> CREATOR = new Creator<UserHandle>() {
        public UserHandle createFromParcel(Parcel in) {
            return new UserHandle(in);
        }

        public UserHandle[] newArray(int size) {
            return new UserHandle[size];
        }
    };
    public static final UserHandle CURRENT = new UserHandle(-2);
    public static final UserHandle CURRENT_OR_SELF = new UserHandle(-3);
    public static final boolean MU_ENABLED = true;
    public static final UserHandle OWNER = new UserHandle(0);
    public static final int PER_USER_RANGE = 100000;
    public static final int USER_ALL = -1;
    public static final int USER_ALL_EXCEPT_KNOX = -4;
    public static final int USER_CURRENT = -2;
    public static final int USER_CURRENT_OR_SELF = -3;
    public static final int USER_NULL = -10000;
    public static final int USER_OWNER = 0;
    private static final SparseArray<UserHandle> userHandles = new SparseArray();
    final int mHandle;

    public static final boolean isSameUser(int uid1, int uid2) {
        return getUserId(uid1) == getUserId(uid2);
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

    public static final int getUserId(int uid) {
        return uid / 100000;
    }

    public static final int getCallingUserId() {
        return getUserId(Binder.getCallingUid());
    }

    public static final UserHandle getCallingUserHandle() {
        int userId = getUserId(Binder.getCallingUid());
        UserHandle userHandle = (UserHandle) userHandles.get(userId);
        if (userHandle != null) {
            return userHandle;
        }
        userHandle = new UserHandle(userId);
        userHandles.put(userId, userHandle);
        return userHandle;
    }

    public static final int getUid(int userId, int appId) {
        return (userId * 100000) + (appId % 100000);
    }

    public static final int getAppId(int uid) {
        return uid % 100000;
    }

    public static final int getUserGid(int userId) {
        return getUid(userId, Process.SHARED_USER_GID);
    }

    public static final int getSharedAppGid(int id) {
        return (50000 + (id % 100000)) - 10000;
    }

    public static final int getAppIdFromSharedAppGid(int gid) {
        int appId = (getAppId(gid) + 10000) - 50000;
        if (appId < 0 || appId >= 50000) {
            return -1;
        }
        return appId;
    }

    public static void formatUid(StringBuilder sb, int uid) {
        if (uid < 10000) {
            sb.append(uid);
            return;
        }
        sb.append('u');
        sb.append(getUserId(uid));
        int appId = getAppId(uid);
        if (appId >= Process.FIRST_ISOLATED_UID && appId <= Process.LAST_ISOLATED_UID) {
            sb.append('i');
            sb.append(appId - Process.FIRST_ISOLATED_UID);
        } else if (appId >= 10000) {
            sb.append('a');
            sb.append(appId - 10000);
        } else {
            sb.append(SearchManager.MENU_KEY);
            sb.append(appId);
        }
    }

    public static String formatUid(int uid) {
        StringBuilder sb = new StringBuilder();
        formatUid(sb, uid);
        return sb.toString();
    }

    public static void formatUid(PrintWriter pw, int uid) {
        if (uid < 10000) {
            pw.print(uid);
            return;
        }
        pw.print('u');
        pw.print(getUserId(uid));
        int appId = getAppId(uid);
        if (appId >= Process.FIRST_ISOLATED_UID && appId <= Process.LAST_ISOLATED_UID) {
            pw.print('i');
            pw.print(appId - Process.FIRST_ISOLATED_UID);
        } else if (appId >= 10000) {
            pw.print('a');
            pw.print(appId - 10000);
        } else {
            pw.print(SearchManager.MENU_KEY);
            pw.print(appId);
        }
    }

    public static final int myUserId() {
        return getUserId(Process.myUid());
    }

    public final boolean isOwner() {
        return equals(OWNER);
    }

    public UserHandle(int h) {
        this.mHandle = h;
    }

    public int getIdentifier() {
        return this.mHandle;
    }

    public String toString() {
        return "UserHandle{" + this.mHandle + "}";
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            if (this.mHandle == ((UserHandle) obj).mHandle) {
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

    public static void writeToParcel(UserHandle h, Parcel out) {
        if (h != null) {
            h.writeToParcel(out, 0);
        } else {
            out.writeInt(-10000);
        }
    }

    public static UserHandle readFromParcel(Parcel in) {
        int h = in.readInt();
        return h != -10000 ? new UserHandle(h) : null;
    }

    public UserHandle(Parcel in) {
        this.mHandle = in.readInt();
    }
}
