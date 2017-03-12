package android.app;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioParameter;
import android.net.ProxyInfo;
import android.os.BatteryStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersonaManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.util.Slog;
import com.android.internal.os.TransferPipe;
import com.android.internal.util.FastPrintWriter;
import com.samsung.android.dualscreen.TaskInfo;
import com.samsung.android.multiwindow.MultiWindowStyle;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlSerializer;

public class ActivityManager {
    public static final String ACTION_REPORT_HEAP_LIMIT = "android.app.action.REPORT_HEAP_LIMIT";
    private static final String AMS_POLICY_ENFORCING = "persist.security.ams.enforcing";
    public static final int ASSIST_CONTEXT_BASIC = 0;
    public static final int ASSIST_CONTEXT_FULL = 1;
    public static final int BROADCAST_FAILED_USER_STOPPED = -2;
    public static final int BROADCAST_STICKY_CANT_HAVE_PERMISSION = -1;
    public static final int BROADCAST_SUCCESS = 0;
    public static final int COMPAT_MODE_ALWAYS = -1;
    public static final int COMPAT_MODE_DISABLED = 0;
    public static final int COMPAT_MODE_ENABLED = 1;
    public static final int COMPAT_MODE_NEVER = -2;
    public static final int COMPAT_MODE_TOGGLE = 2;
    public static final int COMPAT_MODE_UNKNOWN = -3;
    public static final int INTENT_SENDER_ACTIVITY = 2;
    public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;
    public static final int INTENT_SENDER_BROADCAST = 1;
    public static final int INTENT_SENDER_SERVICE = 4;
    public static final int LOCK_TASK_MODE_LOCKED = 1;
    public static final int LOCK_TASK_MODE_NONE = 0;
    public static final int LOCK_TASK_MODE_PINNED = 2;
    public static final String META_HOME_ALTERNATE = "android.app.home.alternate";
    public static final int MOVE_TASK_NO_USER_ACTION = 2;
    public static final int MOVE_TASK_WITH_HOME = 1;
    public static final int PROCESS_STATE_BACKUP = 8;
    public static final int PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 3;
    public static final int PROCESS_STATE_CACHED_ACTIVITY = 14;
    public static final int PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 15;
    public static final int PROCESS_STATE_CACHED_EMPTY = 16;
    public static final int PROCESS_STATE_FOREGROUND_SERVICE = 4;
    public static final int PROCESS_STATE_HEAVY_WEIGHT = 9;
    public static final int PROCESS_STATE_HOME = 12;
    public static final int PROCESS_STATE_IMPORTANT_BACKGROUND = 7;
    public static final int PROCESS_STATE_IMPORTANT_FOREGROUND = 6;
    public static final int PROCESS_STATE_LAST_ACTIVITY = 13;
    public static final int PROCESS_STATE_NONEXISTENT = -1;
    public static final int PROCESS_STATE_PERSISTENT = 0;
    public static final int PROCESS_STATE_PERSISTENT_UI = 1;
    public static final int PROCESS_STATE_RECEIVER = 11;
    public static final int PROCESS_STATE_SERVICE = 10;
    public static final int PROCESS_STATE_TOP = 2;
    public static final int PROCESS_STATE_TOP_SLEEPING = 5;
    public static final int RECENT_EXCEPT_EXTRA_DATA = 16;
    public static final int RECENT_IGNORE_HOME_STACK_TASKS = 8;
    public static final int RECENT_IGNORE_UNAVAILABLE = 2;
    public static final int RECENT_INCLUDE_PROFILES = 4;
    public static final int RECENT_WITH_EXCLUDED = 1;
    public static final int REMOVE_ALL_RECENT_TASKS = 8;
    public static final int REMOVE_ALL_RECENT_TASKS_EXCEPT_TOP_TASK = 32;
    public static final int REMOVE_TASK_EXCEPT_RECENTS = 16;
    public static final int REMOVE_TASK_IMMEDIATELY = 4;
    public static final int START_CANCELED = -6;
    public static final int START_CANCELED_BY_TEMPERATURE = -9;
    public static final int START_CLASS_NOT_FOUND = -2;
    public static final int START_DELIVERED_TO_TOP = 3;
    public static final int START_FLAG_DEBUG = 2;
    public static final int START_FLAG_ONLY_IF_NEEDED = 1;
    public static final int START_FLAG_OPENGL_TRACES = 4;
    public static final int START_FORWARD_AND_REQUEST_CONFLICT = -3;
    public static final int START_INTENT_NOT_RESOLVED = -1;
    public static final int START_NOT_ACTIVITY = -5;
    public static final int START_NOT_CURRENT_USER_ACTIVITY = -8;
    public static final int START_NOT_VOICE_COMPATIBLE = -7;
    public static final int START_PERMISSION_DENIED = -4;
    public static final int START_RETURN_INTENT_TO_CALLER = 1;
    public static final int START_RETURN_LOCK_TASK_MODE_VIOLATION = 5;
    public static final int START_SUCCESS = 0;
    public static final int START_SWITCHES_CANCELED = 4;
    public static final int START_TASK_TO_FRONT = 2;
    private static String TAG = "ActivityManager";
    public static final int USER_OP_IS_CURRENT = -2;
    public static final int USER_OP_SUCCESS = 0;
    public static final int USER_OP_UNKNOWN_USER = -1;
    private static int bbcId = 0;
    private static int gMaxRecentTasks = -1;
    private static boolean localLOGV = false;
    Point mAppTaskThumbnailSize;
    private final Context mContext;
    private final Handler mHandler;

    public static class AppTask {
        private IAppTask mAppTaskImpl;

        public AppTask(IAppTask task) {
            this.mAppTaskImpl = task;
        }

        public void finishAndRemoveTask() {
            try {
                this.mAppTaskImpl.finishAndRemoveTask();
            } catch (RemoteException e) {
                Slog.e(ActivityManager.TAG, "Invalid AppTask", e);
            }
        }

        public RecentTaskInfo getTaskInfo() {
            try {
                return this.mAppTaskImpl.getTaskInfo();
            } catch (RemoteException e) {
                Slog.e(ActivityManager.TAG, "Invalid AppTask", e);
                return null;
            }
        }

        public void moveToFront() {
            try {
                this.mAppTaskImpl.moveToFront();
            } catch (RemoteException e) {
                Slog.e(ActivityManager.TAG, "Invalid AppTask", e);
            }
        }

        public void startActivity(Context context, Intent intent, Bundle options) {
            ActivityThread thread = ActivityThread.currentActivityThread();
            thread.getInstrumentation().execStartActivityFromAppTask(context, thread.getApplicationThread(), this.mAppTaskImpl, intent, options);
        }

        public void setExcludeFromRecents(boolean exclude) {
            try {
                this.mAppTaskImpl.setExcludeFromRecents(exclude);
            } catch (RemoteException e) {
                Slog.e(ActivityManager.TAG, "Invalid AppTask", e);
            }
        }
    }

    public static class MemoryInfo implements Parcelable {
        public static final Creator<MemoryInfo> CREATOR = new Creator<MemoryInfo>() {
            public MemoryInfo createFromParcel(Parcel source) {
                return new MemoryInfo(source);
            }

            public MemoryInfo[] newArray(int size) {
                return new MemoryInfo[size];
            }
        };
        public long availMem;
        public long foregroundAppThreshold;
        public long hiddenAppThreshold;
        public boolean lowMemory;
        public long secondaryServerThreshold;
        public long threshold;
        public long totalMem;
        public long visibleAppThreshold;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.availMem);
            dest.writeLong(this.totalMem);
            dest.writeLong(this.threshold);
            dest.writeInt(this.lowMemory ? 1 : 0);
            dest.writeLong(this.hiddenAppThreshold);
            dest.writeLong(this.secondaryServerThreshold);
            dest.writeLong(this.visibleAppThreshold);
            dest.writeLong(this.foregroundAppThreshold);
        }

        public void readFromParcel(Parcel source) {
            this.availMem = source.readLong();
            this.totalMem = source.readLong();
            this.threshold = source.readLong();
            this.lowMemory = source.readInt() != 0;
            this.hiddenAppThreshold = source.readLong();
            this.secondaryServerThreshold = source.readLong();
            this.visibleAppThreshold = source.readLong();
            this.foregroundAppThreshold = source.readLong();
        }

        private MemoryInfo(Parcel source) {
            readFromParcel(source);
        }
    }

    public static class ProcessErrorStateInfo implements Parcelable {
        public static final int CRASHED = 1;
        public static final Creator<ProcessErrorStateInfo> CREATOR = new Creator<ProcessErrorStateInfo>() {
            public ProcessErrorStateInfo createFromParcel(Parcel source) {
                return new ProcessErrorStateInfo(source);
            }

            public ProcessErrorStateInfo[] newArray(int size) {
                return new ProcessErrorStateInfo[size];
            }
        };
        public static final int NOT_RESPONDING = 2;
        public static final int NO_ERROR = 0;
        public int condition;
        public byte[] crashData;
        public String longMsg;
        public int pid;
        public String processName;
        public String shortMsg;
        public String stackTrace;
        public String tag;
        public int uid;

        public ProcessErrorStateInfo() {
            this.crashData = null;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.condition);
            dest.writeString(this.processName);
            dest.writeInt(this.pid);
            dest.writeInt(this.uid);
            dest.writeString(this.tag);
            dest.writeString(this.shortMsg);
            dest.writeString(this.longMsg);
            dest.writeString(this.stackTrace);
        }

        public void readFromParcel(Parcel source) {
            this.condition = source.readInt();
            this.processName = source.readString();
            this.pid = source.readInt();
            this.uid = source.readInt();
            this.tag = source.readString();
            this.shortMsg = source.readString();
            this.longMsg = source.readString();
            this.stackTrace = source.readString();
        }

        private ProcessErrorStateInfo(Parcel source) {
            this.crashData = null;
            readFromParcel(source);
        }
    }

    public static class RecentTaskInfo implements Parcelable {
        public static final Creator<RecentTaskInfo> CREATOR = new Creator<RecentTaskInfo>() {
            public RecentTaskInfo createFromParcel(Parcel source) {
                return new RecentTaskInfo(source);
            }

            public RecentTaskInfo[] newArray(int size) {
                return new RecentTaskInfo[size];
            }
        };
        public int affiliatedTaskColor;
        public int affiliatedTaskId;
        public ComponentName baseActivity;
        public Intent baseIntent;
        public CharSequence description;
        public int displayId;
        public TaskInfo dualScreenTaskInfo;
        public long firstActiveTime;
        public boolean fullscreen;
        public int id;
        public boolean isSecretMode;
        public long lastActiveTime;
        public MultiWindowStyle multiWindowStyle;
        public int numActivities;
        public ComponentName origActivity;
        public int persistentId;
        public int stackId;
        public TaskDescription taskDescription;
        public ComponentName topActivity;
        public int userId;

        public RecentTaskInfo() {
            this.multiWindowStyle = null;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i = 1;
            dest.writeInt(this.id);
            dest.writeInt(this.persistentId);
            if (this.baseIntent != null) {
                dest.writeInt(1);
                this.baseIntent.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            ComponentName.writeToParcel(this.origActivity, dest);
            if (this.multiWindowStyle != null) {
                dest.writeInt(1);
                this.multiWindowStyle.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            TextUtils.writeToParcel(this.description, dest, 1);
            if (this.taskDescription != null) {
                dest.writeInt(1);
                this.taskDescription.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(this.stackId);
            dest.writeInt(this.userId);
            dest.writeLong(this.firstActiveTime);
            dest.writeLong(this.lastActiveTime);
            dest.writeInt(this.affiliatedTaskId);
            dest.writeInt(this.affiliatedTaskColor);
            ComponentName.writeToParcel(this.baseActivity, dest);
            ComponentName.writeToParcel(this.topActivity, dest);
            dest.writeInt(this.numActivities);
            dest.writeInt(this.isSecretMode ? 1 : 0);
            if (!this.fullscreen) {
                i = 0;
            }
            dest.writeInt(i);
        }

        public void readFromParcel(Parcel source) {
            TaskDescription taskDescription;
            boolean z = true;
            this.id = source.readInt();
            this.persistentId = source.readInt();
            this.baseIntent = source.readInt() > 0 ? (Intent) Intent.CREATOR.createFromParcel(source) : null;
            this.origActivity = ComponentName.readFromParcel(source);
            if (source.readInt() != 0) {
                this.multiWindowStyle = new MultiWindowStyle(source);
            } else {
                this.multiWindowStyle = null;
            }
            this.description = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
            if (source.readInt() > 0) {
                taskDescription = (TaskDescription) TaskDescription.CREATOR.createFromParcel(source);
            } else {
                taskDescription = null;
            }
            this.taskDescription = taskDescription;
            this.stackId = source.readInt();
            this.userId = source.readInt();
            this.firstActiveTime = source.readLong();
            this.lastActiveTime = source.readLong();
            this.affiliatedTaskId = source.readInt();
            this.affiliatedTaskColor = source.readInt();
            this.baseActivity = ComponentName.readFromParcel(source);
            this.topActivity = ComponentName.readFromParcel(source);
            this.numActivities = source.readInt();
            this.isSecretMode = source.readInt() == 1;
            if (source.readInt() != 1) {
                z = false;
            }
            this.fullscreen = z;
        }

        private RecentTaskInfo(Parcel source) {
            this.multiWindowStyle = null;
            readFromParcel(source);
        }
    }

    public static class RunningAppProcessInfo implements Parcelable {
        public static final Creator<RunningAppProcessInfo> CREATOR = new Creator<RunningAppProcessInfo>() {
            public RunningAppProcessInfo createFromParcel(Parcel source) {
                return new RunningAppProcessInfo(source);
            }

            public RunningAppProcessInfo[] newArray(int size) {
                return new RunningAppProcessInfo[size];
            }
        };
        public static final int FLAG_CANT_SAVE_STATE = 1;
        public static final int FLAG_HAS_ACTIVITIES = 4;
        public static final int FLAG_PERSISTENT = 2;
        public static final int IMPORTANCE_BACKGROUND = 400;
        public static final int IMPORTANCE_CANT_SAVE_STATE = 170;
        public static final int IMPORTANCE_EMPTY = 500;
        public static final int IMPORTANCE_FOREGROUND = 100;
        public static final int IMPORTANCE_FOREGROUND_SERVICE = 125;
        public static final int IMPORTANCE_GONE = 1000;
        public static final int IMPORTANCE_PERCEPTIBLE = 130;
        public static final int IMPORTANCE_SERVICE = 300;
        public static final int IMPORTANCE_TOP_SLEEPING = 150;
        public static final int IMPORTANCE_VISIBLE = 200;
        public static final int REASON_PROVIDER_IN_USE = 1;
        public static final int REASON_SERVICE_IN_USE = 2;
        public static final int REASON_UNKNOWN = 0;
        public int flags;
        public int importance;
        public int importanceReasonCode;
        public ComponentName importanceReasonComponent;
        public int importanceReasonImportance;
        public int importanceReasonPid;
        public int lastTrimLevel;
        public int lru;
        public int pid;
        public String[] pkgList;
        public String processName;
        public int processState;
        public int uid;

        public static int procStateToImportance(int procState) {
            if (procState == -1) {
                return 1000;
            }
            if (procState >= 12) {
                return 400;
            }
            if (procState >= 10) {
                return 300;
            }
            if (procState > 9) {
                return 170;
            }
            if (procState >= 7) {
                return 130;
            }
            if (procState >= 6) {
                return 200;
            }
            if (procState >= 5) {
                return 150;
            }
            if (procState >= 4) {
                return 125;
            }
            return 100;
        }

        public RunningAppProcessInfo() {
            this.importance = 100;
            this.importanceReasonCode = 0;
            this.processState = 6;
        }

        public RunningAppProcessInfo(String pProcessName, int pPid, String[] pArr) {
            this.processName = pProcessName;
            this.pid = pPid;
            this.pkgList = pArr;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.processName);
            dest.writeInt(this.pid);
            dest.writeInt(this.uid);
            dest.writeStringArray(this.pkgList);
            dest.writeInt(this.flags);
            dest.writeInt(this.lastTrimLevel);
            dest.writeInt(this.importance);
            dest.writeInt(this.lru);
            dest.writeInt(this.importanceReasonCode);
            dest.writeInt(this.importanceReasonPid);
            ComponentName.writeToParcel(this.importanceReasonComponent, dest);
            dest.writeInt(this.importanceReasonImportance);
            dest.writeInt(this.processState);
        }

        public void readFromParcel(Parcel source) {
            this.processName = source.readString();
            this.pid = source.readInt();
            this.uid = source.readInt();
            this.pkgList = source.readStringArray();
            this.flags = source.readInt();
            this.lastTrimLevel = source.readInt();
            this.importance = source.readInt();
            this.lru = source.readInt();
            this.importanceReasonCode = source.readInt();
            this.importanceReasonPid = source.readInt();
            this.importanceReasonComponent = ComponentName.readFromParcel(source);
            this.importanceReasonImportance = source.readInt();
            this.processState = source.readInt();
        }

        private RunningAppProcessInfo(Parcel source) {
            readFromParcel(source);
        }
    }

    public static class RunningServiceInfo implements Parcelable {
        public static final Creator<RunningServiceInfo> CREATOR = new Creator<RunningServiceInfo>() {
            public RunningServiceInfo createFromParcel(Parcel source) {
                return new RunningServiceInfo(source);
            }

            public RunningServiceInfo[] newArray(int size) {
                return new RunningServiceInfo[size];
            }
        };
        public static final int FLAG_FOREGROUND = 2;
        public static final int FLAG_PERSISTENT_PROCESS = 8;
        public static final int FLAG_STARTED = 1;
        public static final int FLAG_SYSTEM_PROCESS = 4;
        public long activeSince;
        public int clientCount;
        public int clientLabel;
        public String clientPackage;
        public int crashCount;
        public int flags;
        public boolean foreground;
        public long lastActivityTime;
        public int pid;
        public String process;
        public long restarting;
        public ComponentName service;
        public boolean started;
        public int uid;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i = 1;
            ComponentName.writeToParcel(this.service, dest);
            dest.writeInt(this.pid);
            dest.writeInt(this.uid);
            dest.writeString(this.process);
            dest.writeInt(this.foreground ? 1 : 0);
            dest.writeLong(this.activeSince);
            if (!this.started) {
                i = 0;
            }
            dest.writeInt(i);
            dest.writeInt(this.clientCount);
            dest.writeInt(this.crashCount);
            dest.writeLong(this.lastActivityTime);
            dest.writeLong(this.restarting);
            dest.writeInt(this.flags);
            dest.writeString(this.clientPackage);
            dest.writeInt(this.clientLabel);
        }

        public void readFromParcel(Parcel source) {
            boolean z = true;
            this.service = ComponentName.readFromParcel(source);
            this.pid = source.readInt();
            this.uid = source.readInt();
            this.process = source.readString();
            this.foreground = source.readInt() != 0;
            this.activeSince = source.readLong();
            if (source.readInt() == 0) {
                z = false;
            }
            this.started = z;
            this.clientCount = source.readInt();
            this.crashCount = source.readInt();
            this.lastActivityTime = source.readLong();
            this.restarting = source.readLong();
            this.flags = source.readInt();
            this.clientPackage = source.readString();
            this.clientLabel = source.readInt();
        }

        private RunningServiceInfo(Parcel source) {
            readFromParcel(source);
        }
    }

    public static class RunningTaskInfo implements Parcelable {
        public static final Creator<RunningTaskInfo> CREATOR = new Creator<RunningTaskInfo>() {
            public RunningTaskInfo createFromParcel(Parcel source) {
                return new RunningTaskInfo(source);
            }

            public RunningTaskInfo[] newArray(int size) {
                return new RunningTaskInfo[size];
            }
        };
        public ComponentName baseActivity;
        public CharSequence description;
        public int displayId;
        public int id;
        public boolean isExpandHomeType;
        public boolean isHomeType;
        public long lastActiveElapsedTime;
        public long lastActiveTime;
        public MultiWindowStyle multiWindowStyle;
        public int numActivities;
        public int numRunning;
        public ComponentName sourceActivity;
        public String taskAffinity;
        public Bitmap thumbnail;
        public ComponentName topActivity;
        public int userId;
        public int windowMode;

        public RunningTaskInfo() {
            this.multiWindowStyle = new MultiWindowStyle();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i = 1;
            dest.writeInt(this.id);
            ComponentName.writeToParcel(this.baseActivity, dest);
            ComponentName.writeToParcel(this.topActivity, dest);
            if (this.thumbnail != null) {
                dest.writeInt(1);
                this.thumbnail.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            TextUtils.writeToParcel(this.description, dest, 1);
            dest.writeInt(this.numActivities);
            dest.writeInt(this.numRunning);
            if (!this.isHomeType) {
                i = 0;
            }
            dest.writeInt(i);
            ComponentName.writeToParcel(this.sourceActivity, dest);
            this.multiWindowStyle.writeToParcel(dest, flags);
            dest.writeInt(this.userId);
            dest.writeString(this.taskAffinity);
        }

        public void readFromParcel(Parcel source) {
            this.id = source.readInt();
            this.baseActivity = ComponentName.readFromParcel(source);
            this.topActivity = ComponentName.readFromParcel(source);
            if (source.readInt() != 0) {
                this.thumbnail = (Bitmap) Bitmap.CREATOR.createFromParcel(source);
            } else {
                this.thumbnail = null;
            }
            this.description = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
            this.numActivities = source.readInt();
            this.numRunning = source.readInt();
            this.isHomeType = source.readInt() != 0;
            this.sourceActivity = ComponentName.readFromParcel(source);
            this.multiWindowStyle = new MultiWindowStyle(source);
            this.userId = source.readInt();
            this.taskAffinity = source.readString();
        }

        private RunningTaskInfo(Parcel source) {
            this.multiWindowStyle = new MultiWindowStyle();
            readFromParcel(source);
        }
    }

    public static class StackInfo implements Parcelable {
        public static final Creator<StackInfo> CREATOR = new Creator<StackInfo>() {
            public StackInfo createFromParcel(Parcel source) {
                return new StackInfo(source);
            }

            public StackInfo[] newArray(int size) {
                return new StackInfo[size];
            }
        };
        public Rect bounds;
        public int displayId;
        public boolean isIsolated;
        public int stackId;
        public int stackType;
        public int[] taskIds;
        public String[] taskNames;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.stackId);
            dest.writeInt(this.stackType);
            dest.writeInt(this.isIsolated ? 1 : 0);
            dest.writeInt(this.bounds.left);
            dest.writeInt(this.bounds.top);
            dest.writeInt(this.bounds.right);
            dest.writeInt(this.bounds.bottom);
            dest.writeIntArray(this.taskIds);
            dest.writeStringArray(this.taskNames);
            dest.writeInt(this.displayId);
        }

        public void readFromParcel(Parcel source) {
            boolean z = true;
            this.stackId = source.readInt();
            this.stackType = source.readInt();
            if (source.readInt() != 1) {
                z = false;
            }
            this.isIsolated = z;
            this.bounds = new Rect(source.readInt(), source.readInt(), source.readInt(), source.readInt());
            this.taskIds = source.createIntArray();
            this.taskNames = source.createStringArray();
            this.displayId = source.readInt();
        }

        public StackInfo() {
            this.bounds = new Rect();
        }

        private StackInfo(Parcel source) {
            this.bounds = new Rect();
            readFromParcel(source);
        }

        public String toString(String prefix) {
            StringBuilder sb = new StringBuilder(256);
            sb.append(prefix);
            sb.append("Stack id=");
            sb.append(this.stackId);
            sb.append(" stackType=");
            sb.append(this.stackType);
            sb.append(" bounds=");
            sb.append(this.bounds.toShortString());
            sb.append(" displayId=");
            sb.append(this.displayId);
            sb.append("\n");
            prefix = prefix + "  ";
            for (int i = 0; i < this.taskIds.length; i++) {
                sb.append(prefix);
                sb.append("taskId=");
                sb.append(this.taskIds[i]);
                sb.append(": ");
                sb.append(this.taskNames[i]);
                sb.append("\n");
            }
            return sb.toString();
        }

        public String toString() {
            return toString(ProxyInfo.LOCAL_EXCL_LIST);
        }
    }

    public static class TaskDescription implements Parcelable {
        private static final String ATTR_TASKDESCRIPTIONCOLOR = "task_description_color";
        private static final String ATTR_TASKDESCRIPTIONICONFILENAME = "task_description_icon_filename";
        private static final String ATTR_TASKDESCRIPTIONLABEL = "task_description_label";
        private static final String ATTR_TASKDESCRIPTIONTEXTCOLOR = "task_description_text_color";
        public static final String ATTR_TASKDESCRIPTION_PREFIX = "task_description_";
        public static final Creator<TaskDescription> CREATOR = new Creator<TaskDescription>() {
            public TaskDescription createFromParcel(Parcel source) {
                return new TaskDescription(source);
            }

            public TaskDescription[] newArray(int size) {
                return new TaskDescription[size];
            }
        };
        private int mColorPrimary;
        private Bitmap mIcon;
        private String mIconFilename;
        private String mLabel;
        private int mTextColorPrimary;

        public TaskDescription(String label, Bitmap icon, int colorPrimary) {
            if (colorPrimary == 0 || Color.alpha(colorPrimary) == 255) {
                this.mLabel = label;
                this.mIcon = icon;
                this.mColorPrimary = colorPrimary;
                return;
            }
            throw new RuntimeException("A TaskDescription's primary color should be opaque");
        }

        public TaskDescription(String label, int colorPrimary, String iconFilename) {
            this(label, null, colorPrimary);
            this.mIconFilename = iconFilename;
        }

        public TaskDescription(String label, Bitmap icon) {
            this(label, icon, 0);
        }

        public TaskDescription(String label) {
            this(label, null, 0);
        }

        public TaskDescription() {
            this(null, null, 0);
        }

        public TaskDescription(TaskDescription td) {
            this.mLabel = td.mLabel;
            this.mIcon = td.mIcon;
            this.mColorPrimary = td.mColorPrimary;
            this.mTextColorPrimary = td.mTextColorPrimary;
            this.mIconFilename = td.mIconFilename;
        }

        private TaskDescription(Parcel source) {
            readFromParcel(source);
        }

        public void setLabel(String label) {
            this.mLabel = label;
        }

        public void setPrimaryColor(int primaryColor) {
            if (primaryColor == 0 || Color.alpha(primaryColor) == 255) {
                this.mColorPrimary = primaryColor;
                return;
            }
            throw new RuntimeException("A TaskDescription's primary color should be opaque");
        }

        public void setTextPrimaryColor(int textPrimaryColor) {
            this.mTextColorPrimary = textPrimaryColor;
        }

        public void setIcon(Bitmap icon) {
            this.mIcon = icon;
        }

        public void setIconFilename(String iconFilename) {
            this.mIconFilename = iconFilename;
            this.mIcon = null;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public Bitmap getIcon() {
            if (this.mIcon != null) {
                return this.mIcon;
            }
            return loadTaskDescriptionIcon(this.mIconFilename);
        }

        public String getIconFilename() {
            return this.mIconFilename;
        }

        public Bitmap getInMemoryIcon() {
            return this.mIcon;
        }

        public static Bitmap loadTaskDescriptionIcon(String iconFilename) {
            if (iconFilename != null) {
                try {
                    return ActivityManagerNative.getDefault().getTaskDescriptionIcon(iconFilename);
                } catch (RemoteException e) {
                }
            }
            return null;
        }

        public int getPrimaryColor() {
            return this.mColorPrimary;
        }

        public int getTextPrimaryColor() {
            return this.mTextColorPrimary;
        }

        public void saveToXml(XmlSerializer out) throws IOException {
            if (this.mLabel != null) {
                out.attribute(null, ATTR_TASKDESCRIPTIONLABEL, this.mLabel);
            }
            if (this.mColorPrimary != 0) {
                out.attribute(null, ATTR_TASKDESCRIPTIONCOLOR, Integer.toHexString(this.mColorPrimary));
            }
            if (this.mTextColorPrimary != 0) {
                out.attribute(null, ATTR_TASKDESCRIPTIONTEXTCOLOR, Integer.toHexString(this.mTextColorPrimary));
            }
            if (this.mIconFilename != null) {
                out.attribute(null, ATTR_TASKDESCRIPTIONICONFILENAME, this.mIconFilename);
            }
        }

        public void restoreFromXml(String attrName, String attrValue) {
            if (ATTR_TASKDESCRIPTIONLABEL.equals(attrName)) {
                setLabel(attrValue);
            } else if (ATTR_TASKDESCRIPTIONCOLOR.equals(attrName)) {
                setPrimaryColor((int) Long.parseLong(attrValue, 16));
            } else if (ATTR_TASKDESCRIPTIONTEXTCOLOR.equals(attrName)) {
                setTextPrimaryColor((int) Long.parseLong(attrValue, 16));
            } else if (ATTR_TASKDESCRIPTIONICONFILENAME.equals(attrName)) {
                setIconFilename(attrValue);
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            if (this.mLabel == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                dest.writeString(this.mLabel);
            }
            if (this.mIcon == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                this.mIcon.writeToParcel(dest, 0);
            }
            dest.writeInt(this.mColorPrimary);
            dest.writeInt(this.mTextColorPrimary);
            if (this.mIconFilename == null) {
                dest.writeInt(0);
                return;
            }
            dest.writeInt(1);
            dest.writeString(this.mIconFilename);
        }

        public void readFromParcel(Parcel source) {
            Bitmap bitmap;
            String str = null;
            this.mLabel = source.readInt() > 0 ? source.readString() : null;
            if (source.readInt() > 0) {
                bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(source);
            } else {
                bitmap = null;
            }
            this.mIcon = bitmap;
            this.mColorPrimary = source.readInt();
            this.mTextColorPrimary = source.readInt();
            if (source.readInt() > 0) {
                str = source.readString();
            }
            this.mIconFilename = str;
        }

        public String toString() {
            return "TaskDescription Label: " + this.mLabel + " Icon: " + this.mIcon + " colorPrimary: " + this.mColorPrimary;
        }
    }

    public static class TaskThumbnail implements Parcelable {
        public static final Creator<TaskThumbnail> CREATOR = new Creator<TaskThumbnail>() {
            public TaskThumbnail createFromParcel(Parcel source) {
                return new TaskThumbnail(source);
            }

            public TaskThumbnail[] newArray(int size) {
                return new TaskThumbnail[size];
            }
        };
        public Bitmap mainThumbnail;
        public ParcelFileDescriptor thumbnailFileDescriptor;

        public int describeContents() {
            if (this.thumbnailFileDescriptor != null) {
                return this.thumbnailFileDescriptor.describeContents();
            }
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            if (this.mainThumbnail != null) {
                dest.writeInt(1);
                this.mainThumbnail.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            if (this.thumbnailFileDescriptor != null) {
                dest.writeInt(1);
                this.thumbnailFileDescriptor.writeToParcel(dest, flags);
                return;
            }
            dest.writeInt(0);
        }

        public void readFromParcel(Parcel source) {
            if (source.readInt() != 0) {
                this.mainThumbnail = (Bitmap) Bitmap.CREATOR.createFromParcel(source);
            } else {
                this.mainThumbnail = null;
            }
            if (source.readInt() != 0) {
                this.thumbnailFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(source);
            } else {
                this.thumbnailFileDescriptor = null;
            }
        }

        private TaskThumbnail(Parcel source) {
            readFromParcel(source);
        }
    }

    ActivityManager(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        if (((PersonaManager) this.mContext.getSystemService(Context.PERSONA_SERVICE)) != null) {
            bbcId = PersonaManager.getBbcEnabled();
        }
    }

    public int getFrontActivityScreenCompatMode() {
        try {
            return ActivityManagerNative.getDefault().getFrontActivityScreenCompatMode();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setFrontActivityScreenCompatMode(int mode) {
        try {
            ActivityManagerNative.getDefault().setFrontActivityScreenCompatMode(mode);
        } catch (RemoteException e) {
        }
    }

    public int getPackageScreenCompatMode(String packageName) {
        try {
            return ActivityManagerNative.getDefault().getPackageScreenCompatMode(packageName);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setPackageScreenCompatMode(String packageName, int mode) {
        try {
            ActivityManagerNative.getDefault().setPackageScreenCompatMode(packageName, mode);
        } catch (RemoteException e) {
        }
    }

    public boolean getPackageAskScreenCompat(String packageName) {
        try {
            return ActivityManagerNative.getDefault().getPackageAskScreenCompat(packageName);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setPackageAskScreenCompat(String packageName, boolean ask) {
        try {
            ActivityManagerNative.getDefault().setPackageAskScreenCompat(packageName, ask);
        } catch (RemoteException e) {
        }
    }

    public int getMemoryClass() {
        return staticGetMemoryClass();
    }

    public static int staticGetMemoryClass() {
        String vmHeapSize = SystemProperties.get("dalvik.vm.heapgrowthlimit", ProxyInfo.LOCAL_EXCL_LIST);
        if (vmHeapSize == null || ProxyInfo.LOCAL_EXCL_LIST.equals(vmHeapSize)) {
            return staticGetLargeMemoryClass();
        }
        return Integer.parseInt(vmHeapSize.substring(0, vmHeapSize.length() - 1));
    }

    public boolean isSNNEnabled() {
        try {
            if (ActivityManagerNative.getDefault().isSNNEnable() == 1) {
                return true;
            }
        } catch (RemoteException e) {
        }
        return false;
    }

    public void setSNNEnable(boolean snnEnable) {
        try {
            ActivityManagerNative.getDefault().setSNNEnable(snnEnable);
        } catch (RemoteException e) {
        }
    }

    public int getLargeMemoryClass() {
        return staticGetLargeMemoryClass();
    }

    public static int staticGetLargeMemoryClass() {
        String vmHeapSize = SystemProperties.get("dalvik.vm.heapsize", "16m");
        return Integer.parseInt(vmHeapSize.substring(0, vmHeapSize.length() - 1));
    }

    public boolean isLowRamDevice() {
        return isLowRamDeviceStatic();
    }

    public static boolean isLowRamDeviceStatic() {
        return AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(SystemProperties.get("ro.config.low_ram", AudioParameter.AUDIO_PARAMETER_VALUE_false));
    }

    public static boolean isHighEndGfx() {
        return (isLowRamDeviceStatic() || Resources.getSystem().getBoolean(17956889)) ? false : true;
    }

    public static int getMaxRecentTasksStatic() {
        if (gMaxRecentTasks >= 0) {
            return gMaxRecentTasks;
        }
        int i = isLowRamDeviceStatic() ? 50 : 100;
        gMaxRecentTasks = i;
        return i;
    }

    public static int getDefaultAppRecentsLimitStatic() {
        return getMaxRecentTasksStatic() / 6;
    }

    public static int getMaxAppRecentsLimitStatic() {
        return getMaxRecentTasksStatic() / 2;
    }

    @Deprecated
    public List<RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().getRecentTasks(maxNum, flags, UserHandle.myUserId());
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<RecentTaskInfo> getRecentTasksForUser(int maxNum, int flags, int userId) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().getRecentTasks(maxNum, flags, userId);
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<AppTask> getAppTasks() {
        ArrayList<AppTask> tasks = new ArrayList();
        try {
            List<IAppTask> appTasks = ActivityManagerNative.getDefault().getAppTasks(this.mContext.getPackageName());
            int numAppTasks = appTasks.size();
            for (int i = 0; i < numAppTasks; i++) {
                tasks.add(new AppTask((IAppTask) appTasks.get(i)));
            }
            return tasks;
        } catch (RemoteException e) {
            return null;
        }
    }

    public Size getAppTaskThumbnailSize() {
        Size size;
        synchronized (this) {
            ensureAppTaskThumbnailSizeLocked();
            size = new Size(this.mAppTaskThumbnailSize.x, this.mAppTaskThumbnailSize.y);
        }
        return size;
    }

    private void ensureAppTaskThumbnailSizeLocked() {
        if (this.mAppTaskThumbnailSize == null) {
            try {
                this.mAppTaskThumbnailSize = ActivityManagerNative.getDefault().getAppTaskThumbnailSize();
            } catch (RemoteException e) {
                throw new IllegalStateException("System dead?", e);
            }
        }
    }

    public int addAppTask(Activity activity, Intent intent, TaskDescription description, Bitmap thumbnail) {
        synchronized (this) {
            ensureAppTaskThumbnailSizeLocked();
            Point size = this.mAppTaskThumbnailSize;
        }
        int tw = thumbnail.getWidth();
        int th = thumbnail.getHeight();
        if (!(tw == size.x && th == size.y)) {
            float scale;
            Bitmap bm = Bitmap.createBitmap(size.x, size.y, thumbnail.getConfig());
            float dx = 0.0f;
            if (size.x * tw > size.y * th) {
                scale = ((float) size.x) / ((float) th);
                dx = (((float) size.y) - (((float) tw) * scale)) * 0.5f;
            } else {
                scale = ((float) size.y) / ((float) tw);
                float dy = (((float) size.x) - (((float) th) * scale)) * 0.5f;
            }
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate((float) ((int) (0.5f + dx)), 0.0f);
            Canvas canvas = new Canvas(bm);
            canvas.drawBitmap(thumbnail, matrix, null);
            canvas.setBitmap(null);
            thumbnail = bm;
        }
        if (description == null) {
            description = new TaskDescription();
        }
        try {
            return ActivityManagerNative.getDefault().addAppTask(activity.getActivityToken(), intent, description, thumbnail);
        } catch (RemoteException e) {
            throw new IllegalStateException("System dead?", e);
        }
    }

    @Deprecated
    public List<RunningTaskInfo> getRunningTasks(int maxNum) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().getTasks(maxNum, 0);
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<RunningTaskInfo> getRunningTasks(int maxNum, int displayId) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().getTasks(maxNum, 0, displayId);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean removeTask(int taskId) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().removeTask(taskId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean removeTask(int taskId, int flags) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().removeTask(taskId, flags);
        } catch (RemoteException e) {
            return false;
        }
    }

    public TaskThumbnail getTaskThumbnail(int id) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().getTaskThumbnail(id);
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isInHomeStack(int taskId) {
        try {
            return ActivityManagerNative.getDefault().isInHomeStack(taskId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void moveTaskToFront(int taskId, int flags) {
        moveTaskToFront(taskId, flags, null);
    }

    public void moveTaskToFront(int taskId, int flags, Bundle options) {
        try {
            ActivityManagerNative.getDefault().moveTaskToFront(taskId, flags, options);
        } catch (RemoteException e) {
        }
    }

    public List<RunningServiceInfo> getRunningServices(int maxNum) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().getServices(maxNum, 0);
        } catch (RemoteException e) {
            return null;
        }
    }

    public PendingIntent getRunningServiceControlPanel(ComponentName service) throws SecurityException {
        try {
            return ActivityManagerNative.getDefault().getRunningServiceControlPanel(service);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void getMemoryInfo(MemoryInfo outInfo) {
        try {
            ActivityManagerNative.getDefault().getMemoryInfo(outInfo);
        } catch (RemoteException e) {
        }
    }

    public boolean clearApplicationUserData(String packageName, IPackageDataObserver observer) {
        try {
            return ActivityManagerNative.getDefault().clearApplicationUserData(packageName, observer, UserHandle.myUserId());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean clearApplicationUserData() {
        return clearApplicationUserData(this.mContext.getPackageName(), null);
    }

    public List<ProcessErrorStateInfo> getProcessesInErrorState() {
        try {
            return ActivityManagerNative.getDefault().getProcessesInErrorState();
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<ApplicationInfo> getRunningExternalApplications() {
        try {
            return ActivityManagerNative.getDefault().getRunningExternalApplications();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean setProcessMemoryTrimLevel(String process, int userId, int level) {
        try {
            return ActivityManagerNative.getDefault().setProcessMemoryTrimLevel(process, userId, level);
        } catch (RemoteException e) {
            return false;
        }
    }

    public List<RunningAppProcessInfo> getRunningAppProcesses() {
        try {
            return ActivityManagerNative.getDefault().getRunningAppProcesses();
        } catch (RemoteException e) {
            return null;
        }
    }

    public int getPackageImportance(String packageName) {
        try {
            return RunningAppProcessInfo.procStateToImportance(ActivityManagerNative.getDefault().getPackageProcessState(packageName, this.mContext.getOpPackageName()));
        } catch (RemoteException e) {
            return 1000;
        }
    }

    public static void getMyMemoryState(RunningAppProcessInfo outState) {
        try {
            ActivityManagerNative.getDefault().getMyMemoryState(outState);
        } catch (RemoteException e) {
        }
    }

    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) {
        try {
            return ActivityManagerNative.getDefault().getProcessMemoryInfo(pids);
        } catch (RemoteException e) {
            return null;
        }
    }

    @Deprecated
    public void restartPackage(String packageName) {
        killBackgroundProcesses(packageName);
    }

    public void killBackgroundProcesses(String packageName) {
        try {
            ActivityManagerNative.getDefault().killBackgroundProcesses(packageName, UserHandle.myUserId());
        } catch (RemoteException e) {
        }
    }

    public void killUid(int uid, String reason) {
        try {
            ActivityManagerNative.getDefault().killUid(UserHandle.getAppId(uid), UserHandle.getUserId(uid), reason);
        } catch (RemoteException e) {
            Log.e(TAG, "Couldn't kill uid:" + uid, e);
        }
    }

    public void forceStopPackageAsUser(String packageName, int userId) {
        try {
            ActivityManagerNative.getDefault().forceStopPackage(packageName, userId);
        } catch (RemoteException e) {
        }
    }

    public void forceStopPackage(String packageName) {
        forceStopPackageAsUser(packageName, UserHandle.myUserId());
    }

    public ConfigurationInfo getDeviceConfigurationInfo() {
        try {
            return ActivityManagerNative.getDefault().getDeviceConfigurationInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public int getLauncherLargeIconDensity() {
        Resources res = this.mContext.getResources();
        int density = res.getDisplayMetrics().densityDpi;
        if (res.getConfiguration().smallestScreenWidthDp < CalendarColumns.CAL_ACCESS_EDITOR) {
            return density;
        }
        switch (density) {
            case 120:
                return 160;
            case 160:
                return 240;
            case 213:
                return 320;
            case 240:
                return 320;
            case 320:
                return 480;
            case 480:
                return 640;
            default:
                return (int) ((((float) density) * 1.5f) + 0.5f);
        }
    }

    public int getLauncherLargeIconSize() {
        return getLauncherLargeIconSizeInner(this.mContext);
    }

    static int getLauncherLargeIconSizeInner(Context context) {
        Resources res = context.getResources();
        int size = res.getDimensionPixelSize(R.dimen.app_icon_size);
        if (res.getConfiguration().smallestScreenWidthDp < CalendarColumns.CAL_ACCESS_EDITOR) {
            return size;
        }
        switch (res.getDisplayMetrics().densityDpi) {
            case 120:
                return (size * 160) / 120;
            case 160:
                return (size * 240) / 160;
            case 213:
                return (size * 320) / 240;
            case 240:
                return (size * 320) / 240;
            case 320:
                return (size * 480) / 320;
            case 480:
                return ((size * 320) * 2) / 480;
            default:
                return (int) ((((float) size) * 1.5f) + 0.5f);
        }
    }

    public static boolean isUserAMonkey() {
        try {
            return ActivityManagerNative.getDefault().isUserAMonkey();
        } catch (RemoteException e) {
            return false;
        }
    }

    public static boolean isRunningInTestHarness() {
        return SystemProperties.getBoolean("ro.test_harness", false);
    }

    public static boolean isContainerAllowedPackage(String allowContainerCategory, int category) {
        if (allowRuleCheck(allowContainerCategory, category)) {
            return true;
        }
        return false;
    }

    public static boolean allowRuleCheck(String srcAllowCategory, int destCategory) {
        for (String item : srcAllowCategory.split(",")) {
            try {
                if (item.contains("-")) {
                    String[] range = item.split("-");
                    if (destCategory >= Integer.parseInt(range[0]) && destCategory <= Integer.parseInt(range[1])) {
                        return true;
                    }
                } else if (destCategory == Integer.parseInt(item)) {
                    return true;
                }
            } catch (NumberFormatException e) {
                Slog.e(TAG, "allowRuleCheck, numberformatexception found" + e);
            }
        }
        return false;
    }

    public static boolean allowRuleCheck(String srcAllowCategory, int srcCategory, String destAllowCategory, int destCategory) {
        String[] srcCategories = srcAllowCategory.split(",");
        String[] destCategories = destAllowCategory.split(",");
        for (String srcItem : srcCategories) {
            for (String destItem : destCategories) {
                try {
                    String[] range;
                    if (srcItem.contains("-")) {
                        range = srcItem.split("-");
                        if (destCategory >= Integer.parseInt(range[0]) && destCategory <= Integer.parseInt(range[1])) {
                            return true;
                        }
                    } else if (destItem.contains("-")) {
                        range = destItem.split("-");
                        if (srcCategory >= Integer.parseInt(range[0]) && srcCategory <= Integer.parseInt(range[1])) {
                            return true;
                        }
                    } else if ((String.valueOf(srcCategory) + ":" + srcItem).equals(destItem + ":" + String.valueOf(destCategory))) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    Slog.e(TAG, "allowRuleCheck, numberformatexception found" + e);
                }
            }
        }
        return false;
    }

    private static int checkContainerAppPermission(Intent intent, String srcPackageName, int uid, int owningUid, String destPackageName, boolean allowLaunchIntent) {
        String[] src_destPackageName = null;
        String method = "checkContainerAppPermission";
        if (owningUid == -1 || uid == -1) {
            return 0;
        }
        if (srcPackageName == null || destPackageName == null) {
            return 0;
        }
        int amsEnforceValue = Integer.parseInt(SystemProperties.get(AMS_POLICY_ENFORCING, "0"));
        boolean isSourceTrusted = false;
        boolean isDestTrusted = false;
        boolean isPermissionGranted;
        try {
            ApplicationInfo srcAppInfo = AppGlobals.getPackageManager().getApplicationInfo(srcPackageName, 128, UserHandle.getUserId(uid));
            ApplicationInfo destAppInfo = AppGlobals.getPackageManager().getApplicationInfo(destPackageName, 128, UserHandle.getUserId(owningUid));
            if (srcAppInfo == null || destAppInfo == null) {
                return 0;
            }
            if (intent != null && intent.getComponent() != null && intent.getComponent().flattenToShortString().contains("SwitchB2BActivity") && destPackageName.equals(PersonaManager.KNOX_SWITCHER_PKG) && intent.getAction().equals(Intent.ACTION_MAIN)) {
                Slog.w(TAG, "Proxy App Authenticated based on intent type.");
                return 0;
            }
            String src_allowCategory;
            int dst_category;
            int src_category;
            int userId = getCurrentUser();
            if (bbcId <= 0 || userId != bbcId) {
                src_allowCategory = srcAppInfo.allowCategory;
                src_destPackageName = srcAppInfo.destPackageName;
                dst_category = destAppInfo.category;
                src_category = srcAppInfo.category;
            } else {
                src_allowCategory = srcAppInfo.bbcallowCategory;
                dst_category = destAppInfo.bbccategory;
                src_category = srcAppInfo.bbccategory;
            }
            boolean isDestPkgAllowed = true;
            if (!UserHandle.isSameUser(uid, owningUid) && UserHandle.getUserId(owningUid) > 99 && UserHandle.getUserId(owningUid) < 195) {
                if ((srcAppInfo.flags & 129) == 0) {
                    if (!"platform".equalsIgnoreCase(srcAppInfo.seinfo)) {
                        isDestPkgAllowed = false;
                    } else if (!checkDestPkgNameInList(destPackageName, src_destPackageName)) {
                        isDestPkgAllowed = false;
                    }
                } else if (!checkDestPkgNameInList(destPackageName, src_destPackageName)) {
                    isDestPkgAllowed = false;
                }
                if (!isDestPkgAllowed) {
                    Slog.d(TAG, "isDestPkgAllowed=" + isDestPkgAllowed + ": srcPkgName=" + srcPackageName + ", srcUid=" + uid + ", destPkgName=" + destPackageName + ", destUid=" + owningUid);
                    isDestPkgAllowed = true;
                }
            }
            String src_allowContainerCategory = srcAppInfo.allowContainerCategory;
            String dst_allowContainerCategory = destAppInfo.allowContainerCategory;
            boolean isAllowCategoryPass = allowRuleCheck(src_allowCategory, dst_category);
            if (!(src_allowContainerCategory == null || src_allowContainerCategory.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
                isSourceTrusted = isContainerAllowedPackage(src_allowContainerCategory, dst_category);
            }
            if (!(dst_allowContainerCategory == null || dst_allowContainerCategory.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
                isDestTrusted = isContainerAllowedPackage(dst_allowContainerCategory, src_category);
            }
            if (destAppInfo.allowedAgentType != 1) {
                isPermissionGranted = ((srcAppInfo.agentType & destAppInfo.allowedAgentType) != 0 && isAllowCategoryPass) || srcPackageName.equals(destPackageName);
                (isPermissionGranted ? "AGENT GRANTED" : "AGENT DENIED ") + " - src:" + srcAppInfo.packageName + " dst:" + destAppInfo.packageName + " src agentType:" + srcAppInfo.agentType + " dest allowedAgentType:" + destAppInfo.allowedAgentType;
            } else if (isAllowCategoryPass || isSourceTrusted || isDestTrusted || allowLaunchIntent || uid == 0 || ((uid == 1000 || UserHandle.getAppId(uid) == 1000) && "android".equals(srcPackageName))) {
                isPermissionGranted = true;
            } else {
                isPermissionGranted = false;
            }
            if (!(isPermissionGranted && isDestPkgAllowed) && (isSourceTrusted || isDestTrusted)) {
                Slog.e(TAG, "Denial occuring with trusted src or dest");
            }
            switch (amsEnforceValue) {
                case 0:
                case 2:
                    isPermissionGranted = true;
                    break;
                case 3:
                    isPermissionGranted = isPermissionGranted && isDestPkgAllowed;
                    break;
            }
            if (isPermissionGranted) {
                return 0;
            }
            Slog.d(TAG, "isPermissionGranted is false");
            return -1;
        } catch (RemoteException e) {
            Slog.e(TAG, "Could not check permissionManager enabled.", e);
            isPermissionGranted = false;
        }
    }

    private static boolean checkDestPkgNameInList(String destPkgName, String[] destPkgList) {
        if ((Integer.parseInt(SystemProperties.get(AMS_POLICY_ENFORCING, "0")) & 2) != 2) {
            return true;
        }
        if (destPkgList == null || destPkgList.length == 0) {
            return false;
        }
        boolean result = false;
        int i = 0;
        while (!result && i < destPkgList.length) {
            if ("allknoxpackages".equalsIgnoreCase(destPkgList[i]) || destPkgList[i].equalsIgnoreCase(destPkgName)) {
                result = true;
            }
            i++;
        }
        return result;
    }

    public static int checkComponentPermission(Intent intent, String permission, String srcPackageName, int uid, int owningUid, String destPackageName, boolean exported, boolean allowLaunchIntent) {
        if (uid == 0 || "android".equals(srcPackageName)) {
            return 0;
        }
        if (checkContainerAppPermission(intent, srcPackageName, uid, owningUid, destPackageName, allowLaunchIntent) == -1) {
            Slog.d(TAG, "return DENIED");
            return -1;
        } else if (uid == 1000 || UserHandle.getAppId(uid) == 1000) {
            return 0;
        } else {
            if (UserHandle.isIsolated(uid)) {
                return -1;
            }
            if (owningUid >= 0 && UserHandle.isSameApp(uid, owningUid)) {
                return 0;
            }
            if (!exported) {
                return -1;
            }
            if (permission == null) {
                return 0;
            }
            try {
                return AppGlobals.getPackageManager().checkUidPermission(permission, uid);
            } catch (RemoteException e) {
                Slog.e(TAG, "PackageManager is dead?!?", e);
                return -1;
            }
        }
    }

    public static int checkComponentPermission(String permission, int uid, int owningUid, boolean exported) {
        int i = -1;
        int appId = UserHandle.getAppId(uid);
        if (appId == 0 || appId == 1000) {
            return 0;
        }
        if (UserHandle.isIsolated(uid)) {
            return i;
        }
        if (owningUid >= 0 && UserHandle.isSameApp(uid, owningUid)) {
            return 0;
        }
        if (!exported) {
            return i;
        }
        if (permission == null) {
            return 0;
        }
        try {
            return AppGlobals.getPackageManager().checkUidPermission(permission, uid);
        } catch (RemoteException e) {
            Slog.e(TAG, "PackageManager is dead?!?", e);
            return i;
        }
    }

    public static int checkUidPermission(String permission, int uid) {
        try {
            return AppGlobals.getPackageManager().checkUidPermission(permission, uid);
        } catch (RemoteException e) {
            Slog.e(TAG, "PackageManager is dead?!?", e);
            return -1;
        }
    }

    public static int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) {
        if (UserHandle.getUserId(callingUid) != userId) {
            try {
                userId = ActivityManagerNative.getDefault().handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull, name, callerPackage);
            } catch (RemoteException e) {
                throw new SecurityException("Failed calling activity manager", e);
            }
        }
        return userId;
    }

    public static int getCurrentUser() {
        try {
            UserInfo ui = ActivityManagerNative.getDefault().getCurrentUser();
            if (ui != null) {
                return ui.id;
            }
            return 0;
        } catch (RemoteException e) {
            return 0;
        }
    }

    public boolean switchUser(int userid) {
        try {
            return ActivityManagerNative.getDefault().switchUser(userid);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isUserRunning(int userid) {
        boolean z = false;
        try {
            z = ActivityManagerNative.getDefault().isUserRunning(userid, false);
        } catch (RemoteException e) {
        }
        return z;
    }

    public void dumpPackageState(FileDescriptor fd, String packageName) {
        dumpPackageStateStatic(fd, packageName);
    }

    public static void dumpPackageStateStatic(FileDescriptor fd, String packageName) {
        PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd));
        dumpService(pw, fd, "package", new String[]{packageName});
        pw.println();
        dumpService(pw, fd, Context.ACTIVITY_SERVICE, new String[]{"-a", "package", packageName});
        pw.println();
        dumpService(pw, fd, "meminfo", new String[]{"--local", "--package", packageName});
        pw.println();
        dumpService(pw, fd, "procstats", new String[]{packageName});
        pw.println();
        dumpService(pw, fd, Context.USAGE_STATS_SERVICE, new String[]{"--packages", packageName});
        pw.println();
        dumpService(pw, fd, BatteryStats.SERVICE_NAME, new String[]{packageName});
        pw.flush();
    }

    private static void dumpService(PrintWriter pw, FileDescriptor fd, String name, String[] args) {
        Throwable e;
        pw.print("DUMP OF SERVICE ");
        pw.print(name);
        pw.println(":");
        IBinder service = ServiceManager.checkService(name);
        if (service == null) {
            pw.println("  (Service not found)");
            return;
        }
        TransferPipe tp = null;
        try {
            pw.flush();
            TransferPipe tp2 = new TransferPipe();
            try {
                tp2.setBufferPrefix("  ");
                service.dumpAsync(tp2.getWriteFd().getFileDescriptor(), args);
                tp2.go(fd, 10000);
                tp = tp2;
            } catch (Throwable th) {
                e = th;
                tp = tp2;
                if (tp != null) {
                    tp.kill();
                }
                pw.println("Failure dumping service:");
                e.printStackTrace(pw);
            }
        } catch (Throwable th2) {
            e = th2;
            if (tp != null) {
                tp.kill();
            }
            pw.println("Failure dumping service:");
            e.printStackTrace(pw);
        }
    }

    public void setWatchHeapLimit(long pssSize) {
        try {
            ActivityManagerNative.getDefault().setDumpHeapDebugLimit(null, 0, pssSize, this.mContext.getPackageName());
        } catch (RemoteException e) {
        }
    }

    public void clearWatchHeapLimit() {
        try {
            ActivityManagerNative.getDefault().setDumpHeapDebugLimit(null, 0, 0, null);
        } catch (RemoteException e) {
        }
    }

    public void startLockTaskMode(int taskId) {
        try {
            ActivityManagerNative.getDefault().startLockTaskMode(taskId);
        } catch (RemoteException e) {
        }
    }

    public void stopLockTaskMode() {
        try {
            ActivityManagerNative.getDefault().stopLockTaskMode();
        } catch (RemoteException e) {
        }
    }

    public boolean isInLockTaskMode() {
        return getLockTaskModeState() != 0;
    }

    public int getLockTaskModeState() {
        try {
            return ActivityManagerNative.getDefault().getLockTaskModeState();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public Map getGrabedIntentSenders() {
        try {
            return ActivityManagerNative.getDefault().getGrabedIntentSenders();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void enableSafeMode() {
        try {
            ActivityManagerNative.getDefault().enableSafeMode();
        } catch (RemoteException e) {
        }
    }

    public String[] queryRegisteredReceiverPackages(Intent intent) {
        if (intent == null) {
            return new String[0];
        }
        try {
            return ActivityManagerNative.getDefault().queryRegisteredReceiverPackages(intent, intent.resolveTypeIfNeeded(this.mContext.getContentResolver()), this.mContext.getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public ArrayList<String> getAppLockedPackageList() {
        try {
            return ActivityManagerNative.getDefault().getAppLockedPackageList();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setAppLockedUnLockPackage(String packageName) {
        try {
            ActivityManagerNative.getDefault().setAppLockedUnLockPackage(packageName);
        } catch (RemoteException e) {
        }
    }

    public boolean isAppLockedPackage(String packageName) {
        try {
            return ActivityManagerNative.getDefault().isAppLockedPackage(packageName);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void clearAppLockedUnLockedApp() {
        try {
            ActivityManagerNative.getDefault().clearAppLockedUnLockedApp();
        } catch (RemoteException e) {
        }
    }

    public String getAppLockedLockType() {
        try {
            return ActivityManagerNative.getDefault().getAppLockedLockType();
        } catch (RemoteException e) {
            return null;
        }
    }

    public String getAppLockedCheckAction() {
        try {
            return ActivityManagerNative.getDefault().getAppLockedCheckAction();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setAppLockedVerifying(String pkgName, boolean verifying) {
        try {
            ActivityManagerNative.getDefault().setAppLockedVerifying(pkgName, verifying);
        } catch (RemoteException e) {
        }
    }

    public boolean isAppLockedVerifying(String pkgName) {
        try {
            return ActivityManagerNative.getDefault().isAppLockedVerifying(pkgName);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setCustomImageForPackage(ComponentName component, int taskUserId, FileDescriptor fd, int rotation) {
        boolean z = false;
        try {
            z = ActivityManagerNative.getDefault().setCustomImageForPackage(component, taskUserId, fd != null ? ParcelFileDescriptor.dup(fd) : null, rotation);
        } catch (RemoteException e) {
            Slog.e(TAG, "CustomStartingWindow failed to set custom image", e);
        } catch (IOException e2) {
            Slog.e(TAG, "CustomStartingWindow failed to make ParcelFileDescriptor", e2);
        }
        return z;
    }
}
