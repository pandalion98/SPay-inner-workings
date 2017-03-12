package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Printer;

public class ActivityInfo extends ComponentInfo implements Parcelable {
    public static final int CONFIG_DENSITY = 4096;
    public static final int CONFIG_DISPLAYID = 1048576;
    public static final int CONFIG_FLIPFONT = 536870912;
    public static final int CONFIG_FONT_SCALE = 1073741824;
    public static final int CONFIG_KEYBOARD = 16;
    public static final int CONFIG_KEYBOARD_HIDDEN = 32;
    public static final int CONFIG_LAYOUT_DIRECTION = 8192;
    public static final int CONFIG_LOCALE = 4;
    public static final int CONFIG_MCC = 1;
    public static final int CONFIG_MNC = 2;
    public static int[] CONFIG_NATIVE_BITS = new int[]{2, 1, 4, 8, 16, 32, 64, 128, 2048, 4096, 512, 8192, 256, 16384, 32768, 0, 65536, 131072, 262144};
    public static final int CONFIG_NAVIGATION = 64;
    public static final int CONFIG_ORIENTATION = 128;
    public static final int CONFIG_SCREEN_LAYOUT = 256;
    public static final int CONFIG_SCREEN_SIZE = 1024;
    public static final int CONFIG_SHOW_BUTTON_BACKGROUND = 2097152;
    public static final int CONFIG_SMALLEST_SCREEN_SIZE = 2048;
    public static final int CONFIG_THEME_SEQ = 65536;
    public static final int CONFIG_TOUCHSCREEN = 8;
    public static final int CONFIG_UI_MODE = 512;
    public static final Creator<ActivityInfo> CREATOR = new Creator<ActivityInfo>() {
        public ActivityInfo createFromParcel(Parcel source) {
            return new ActivityInfo(source);
        }

        public ActivityInfo[] newArray(int size) {
            return new ActivityInfo[size];
        }
    };
    public static final int DOCUMENT_LAUNCH_ALWAYS = 2;
    public static final int DOCUMENT_LAUNCH_INTO_EXISTING = 1;
    public static final int DOCUMENT_LAUNCH_NEVER = 3;
    public static final int DOCUMENT_LAUNCH_NONE = 0;
    public static final int FLAG_ALLOW_EMBEDDED = Integer.MIN_VALUE;
    public static final int FLAG_ALLOW_TASK_REPARENTING = 64;
    public static final int FLAG_ALWAYS_RETAIN_TASK_STATE = 8;
    public static final int FLAG_AUTO_REMOVE_FROM_RECENTS = 8192;
    public static final int FLAG_CLEAR_TASK_ON_LAUNCH = 4;
    public static final int FLAG_EXCLUDE_FROM_RECENTS = 32;
    public static final int FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS = 256;
    public static final int FLAG_FINISH_ON_TASK_LAUNCH = 2;
    public static final int FLAG_HARDWARE_ACCELERATED = 512;
    public static final int FLAG_IMMERSIVE = 2048;
    public static final int FLAG_MULTIPROCESS = 1;
    public static final int FLAG_NO_HISTORY = 128;
    public static final int FLAG_PRIMARY_USER_ONLY = 536870912;
    public static final int FLAG_RELINQUISH_TASK_IDENTITY = 4096;
    public static final int FLAG_RESUME_WHILE_PAUSING = 16384;
    public static final int FLAG_SHOW_FOR_ALL_USERS = 1024;
    public static final int FLAG_SINGLE_USER = 1073741824;
    public static final int FLAG_STATE_NOT_NEEDED = 16;
    public static final int LAUNCH_MULTIPLE = 0;
    public static final int LAUNCH_SINGLE_INSTANCE = 3;
    public static final int LAUNCH_SINGLE_TASK = 2;
    public static final int LAUNCH_SINGLE_TOP = 1;
    public static final int LOCK_TASK_LAUNCH_MODE_ALWAYS = 2;
    public static final int LOCK_TASK_LAUNCH_MODE_DEFAULT = 0;
    public static final int LOCK_TASK_LAUNCH_MODE_IF_WHITELISTED = 3;
    public static final int LOCK_TASK_LAUNCH_MODE_NEVER = 1;
    public static final int PERSIST_ACROSS_REBOOTS = 2;
    public static final int PERSIST_NEVER = 1;
    public static final int PERSIST_ROOT_ONLY = 0;
    public static final int SCREEN_ORIENTATION_BEHIND = 3;
    public static final int SCREEN_ORIENTATION_FULL_SENSOR = 10;
    public static final int SCREEN_ORIENTATION_FULL_USER = 13;
    public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
    public static final int SCREEN_ORIENTATION_LOCKED = 14;
    public static final int SCREEN_ORIENTATION_NOSENSOR = 5;
    public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
    public static final int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
    public static final int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;
    public static final int SCREEN_ORIENTATION_SENSOR = 4;
    public static final int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6;
    public static final int SCREEN_ORIENTATION_SENSOR_PORTRAIT = 7;
    public static final int SCREEN_ORIENTATION_UNDEFINED = -2;
    public static final int SCREEN_ORIENTATION_UNSPECIFIED = -1;
    public static final int SCREEN_ORIENTATION_USER = 2;
    public static final int SCREEN_ORIENTATION_USER_LANDSCAPE = 11;
    public static final int SCREEN_ORIENTATION_USER_PORTRAIT = 12;
    public static final int UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW = 1;
    public int configChanges;
    public int documentLaunchMode;
    public int flags;
    private final boolean isElasticEnabled;
    public int launchMode;
    public int lockTaskLaunchMode;
    public int maxRecents;
    public String parentActivityName;
    public String permission;
    public int persistableMode;
    public boolean resizeable;
    public int screenOrientation;
    public int softInputMode;
    public String targetActivity;
    public String taskAffinity;
    public int theme;
    public int uiOptions;

    public static int activityInfoConfigToNative(int input) {
        int output = 0;
        for (int i = 0; i < CONFIG_NATIVE_BITS.length; i++) {
            if (((1 << i) & input) != 0) {
                output |= CONFIG_NATIVE_BITS[i];
            }
        }
        return output;
    }

    public static int activityInfoConfigNativeToJava(int input) {
        int output = 0;
        for (int i = 0; i < CONFIG_NATIVE_BITS.length; i++) {
            if ((CONFIG_NATIVE_BITS[i] & input) != 0) {
                output |= 1 << i;
            }
        }
        return output;
    }

    public int getRealConfigChanged() {
        return this.applicationInfo.targetSdkVersion < 13 ? (this.configChanges | 1024) | 2048 : this.configChanges;
    }

    public static final String lockTaskLaunchModeToString(int lockTaskLaunchMode) {
        switch (lockTaskLaunchMode) {
            case 0:
                return "LOCK_TASK_LAUNCH_MODE_DEFAULT";
            case 1:
                return "LOCK_TASK_LAUNCH_MODE_NEVER";
            case 2:
                return "LOCK_TASK_LAUNCH_MODE_ALWAYS";
            case 3:
                return "LOCK_TASK_LAUNCH_MODE_IF_WHITELISTED";
            default:
                return "unknown=" + lockTaskLaunchMode;
        }
    }

    public ActivityInfo() {
        this.screenOrientation = -1;
        this.uiOptions = 0;
        this.isElasticEnabled = true;
    }

    public ActivityInfo(ActivityInfo orig) {
        super((ComponentInfo) orig);
        this.screenOrientation = -1;
        this.uiOptions = 0;
        this.isElasticEnabled = true;
        this.theme = orig.theme;
        this.launchMode = orig.launchMode;
        this.documentLaunchMode = orig.documentLaunchMode;
        this.permission = orig.permission;
        this.taskAffinity = orig.taskAffinity;
        this.targetActivity = orig.targetActivity;
        this.flags = orig.flags;
        this.screenOrientation = orig.screenOrientation;
        this.configChanges = orig.configChanges;
        this.softInputMode = orig.softInputMode;
        this.uiOptions = orig.uiOptions;
        this.parentActivityName = orig.parentActivityName;
        this.persistableMode = orig.persistableMode;
        this.maxRecents = orig.maxRecents;
        this.resizeable = orig.resizeable;
        this.lockTaskLaunchMode = orig.lockTaskLaunchMode;
    }

    public final int getThemeResource() {
        if (this.theme != 0) {
            if (this.resIdOffset > 0 && (this.theme >> 24) == 127) {
                this.theme -= this.resIdOffset << 24;
            }
            return this.theme;
        }
        if (this.resIdOffset > 0 && (this.applicationInfo.theme >> 24) == 127) {
            ApplicationInfo applicationInfo = this.applicationInfo;
            applicationInfo.theme -= this.resIdOffset << 24;
        }
        return this.applicationInfo.theme;
    }

    private String persistableModeToString() {
        switch (this.persistableMode) {
            case 0:
                return "PERSIST_ROOT_ONLY";
            case 1:
                return "PERSIST_NEVER";
            case 2:
                return "PERSIST_ACROSS_REBOOTS";
            default:
                return "UNKNOWN=" + this.persistableMode;
        }
    }

    public void dump(Printer pw, String prefix) {
        super.dumpFront(pw, prefix);
        if (this.permission != null) {
            pw.println(prefix + "permission=" + this.permission);
        }
        pw.println(prefix + "taskAffinity=" + this.taskAffinity + " targetActivity=" + this.targetActivity + " persistableMode=" + persistableModeToString());
        if (!(this.launchMode == 0 && this.flags == 0 && this.theme == 0)) {
            pw.println(prefix + "launchMode=" + this.launchMode + " flags=0x" + Integer.toHexString(this.flags) + " theme=0x" + Integer.toHexString(this.theme));
        }
        if (!(this.screenOrientation == -1 && this.configChanges == 0 && this.softInputMode == 0)) {
            pw.println(prefix + "screenOrientation=" + this.screenOrientation + " configChanges=0x" + Integer.toHexString(this.configChanges) + " softInputMode=0x" + Integer.toHexString(this.softInputMode));
        }
        if (this.uiOptions != 0) {
            pw.println(prefix + " uiOptions=0x" + Integer.toHexString(this.uiOptions));
        }
        pw.println(prefix + "resizeable=" + this.resizeable + " lockTaskLaunchMode=" + lockTaskLaunchModeToString(this.lockTaskLaunchMode));
        super.dumpBack(pw, prefix);
    }

    public String toString() {
        return "ActivityInfo{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.name + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeInt(this.theme);
        dest.writeInt(this.launchMode);
        dest.writeInt(this.documentLaunchMode);
        dest.writeString(this.permission);
        dest.writeString(this.taskAffinity);
        dest.writeString(this.targetActivity);
        dest.writeInt(this.flags);
        dest.writeInt(this.screenOrientation);
        dest.writeInt(this.configChanges);
        dest.writeInt(this.softInputMode);
        dest.writeInt(this.uiOptions);
        dest.writeString(this.parentActivityName);
        dest.writeInt(this.persistableMode);
        dest.writeInt(this.maxRecents);
        dest.writeInt(this.resizeable ? 1 : 0);
        dest.writeInt(this.lockTaskLaunchMode);
    }

    private ActivityInfo(Parcel source) {
        boolean z = true;
        super(source);
        this.screenOrientation = -1;
        this.uiOptions = 0;
        this.isElasticEnabled = true;
        this.theme = source.readInt();
        this.launchMode = source.readInt();
        this.documentLaunchMode = source.readInt();
        this.permission = source.readString();
        this.taskAffinity = source.readString();
        this.targetActivity = source.readString();
        this.flags = source.readInt();
        this.screenOrientation = source.readInt();
        this.configChanges = source.readInt();
        this.softInputMode = source.readInt();
        this.uiOptions = source.readInt();
        this.parentActivityName = source.readString();
        this.persistableMode = source.readInt();
        this.maxRecents = source.readInt();
        if (source.readInt() != 1) {
            z = false;
        }
        this.resizeable = z;
        this.lockTaskLaunchMode = source.readInt();
    }
}
