package android.app;

import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager.StackInfo;
import android.app.ActivityManager.TaskDescription;
import android.app.ActivityManager.TaskThumbnail;
import android.app.ApplicationErrorReport.CrashInfo;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.ContentProviderNative;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.UriPermission;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;
import android.service.voice.IVoiceInteractionSession;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.MemDumpInfo;
import com.android.internal.os.IResultReceiver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IActivityManager extends IInterface {
    public static final int ACTIVITY_DESTROYED_TRANSACTION = 62;
    public static final int ACTIVITY_IDLE_TRANSACTION = 18;
    public static final int ACTIVITY_PAUSED_TRANSACTION = 19;
    public static final int ACTIVITY_RESUMED_TRANSACTION = 39;
    public static final int ACTIVITY_SLEPT_TRANSACTION = 123;
    public static final int ACTIVITY_STOPPED_TRANSACTION = 20;
    public static final int ADD_APP_TASK_TRANSACTION = 234;
    public static final int ADD_PACKAGE_DEPENDENCY_TRANSACTION = 95;
    public static final int APP_NOT_RESPONDING_VIA_PROVIDER_TRANSACTION = 183;
    public static final int ATTACH_APPLICATION_TRANSACTION = 17;
    public static final int AUTORUN_BLOCKED_APP = 1204;
    public static final int BACKGROUND_RESOURCES_RELEASED_TRANSACTION = 228;
    public static final int BACKUP_AGENT_CREATED_TRANSACTION = 91;
    public static final int BIND_SERVICE_TRANSACTION = 36;
    public static final int BOOT_ANIMATION_COMPLETE_TRANSACTION = 238;
    public static final int BROADCAST_INTENT_TRANSACTION = 14;
    public static final int CANCEL_INTENT_SENDER_TRANSACTION = 64;
    public static final int CHECK_GRANT_URI_PERMISSION_TRANSACTION = 119;
    public static final int CHECK_KNOX_PERMISSION_BY_PID_TRANSACTION = 1502;
    public static final int CHECK_KNOX_PERMISSION_BY_PKGNAME_TRANSACTION = 1501;
    public static final int CHECK_PERMISSION_TRANSACTION = 53;
    public static final int CHECK_PERMISSION_WITH_TOKEN_TRANSACTION = 242;
    public static final int CHECK_URI_PERMISSION_TRANSACTION = 54;
    public static final int CLEAR_APPLOCKED_UNLOCKED_APP_TRANSACTION = 7004;
    public static final int CLEAR_APP_DATA_TRANSACTION = 78;
    public static final int CLEAR_PENDING_BACKUP_TRANSACTION = 160;
    public static final int CLEAR_RECENT_TASKS_TRANSACTION = 188;
    public static final int CLOSE_SYSTEM_DIALOGS_TRANSACTION = 97;
    public static final int CONVERT_FROM_TRANSLUCENT_SKIPWINDOWOPAQUE_TRANSACTION = 1103;
    public static final int CONVERT_FROM_TRANSLUCENT_TRANSACTION = 174;
    public static final int CONVERT_TO_TRANSLUCENT_TRANSACTION = 175;
    public static final int CRASH_APPLICATION_TRANSACTION = 114;
    public static final int CREATE_STACK_ON_DISPLAY = 282;
    public static final int CREATE_VIRTUAL_ACTIVITY_CONTAINER_TRANSACTION = 168;
    public static final int DELETE_ACTIVITY_CONTAINER_TRANSACTION = 186;
    public static final int DUMP_HEAP_FINISHED_TRANSACTION = 289;
    public static final int DUMP_HEAP_TRANSACTION = 120;
    public static final int ENABLE_SAFE_MODE_TRANSACTION = 6021;
    public static final int ENTER_SAFE_MODE_TRANSACTION = 66;
    public static final int FINISH_ACTIVITY_AFFINITY_TRANSACTION = 149;
    public static final int FINISH_ACTIVITY_TRANSACTION = 11;
    public static final int FINISH_HEAVY_WEIGHT_APP_TRANSACTION = 109;
    public static final int FINISH_INSTRUMENTATION_TRANSACTION = 45;
    public static final int FINISH_RECEIVER_TRANSACTION = 16;
    public static final int FINISH_SUB_ACTIVITY_TRANSACTION = 32;
    public static final int FINISH_VOICE_TASK_TRANSACTION = 224;
    public static final int FORCE_STOP_PACKAGE_BYADMIN_TRANSACTION = 1201;
    public static final int FORCE_STOP_PACKAGE_TRANSACTION = 79;
    public static final int GET_ACTIVITY_CLASS_FOR_TOKEN_TRANSACTION = 49;
    public static final int GET_ACTIVITY_DISPLAY_ID_TRANSACTION = 185;
    public static final int GET_ACTIVITY_OPTIONS_TRANSACTION = 220;
    public static final int GET_ALL_STACK_INFOS_TRANSACTION = 171;
    public static final int GET_APPLOCKED_CHECK_ACTION_TRANSACTION = 7006;
    public static final int GET_APPLOCKED_LOCK_TYPE_TRANSACTION = 7005;
    public static final int GET_APPLOCKED_PACKAGE_LIST_TRANSACTION = 7001;
    public static final int GET_APP_TASKS_TRANSACTION = 221;
    public static final int GET_APP_TASK_THUMBNAIL_SIZE_TRANSACTION = 235;
    public static final int GET_ASSIST_CONTEXT_EXTRAS_TRANSACTION = 162;
    public static final int GET_CALLING_ACTIVITY_TRANSACTION = 22;
    public static final int GET_CALLING_PACKAGE_TRANSACTION = 21;
    public static final int GET_CONFIGURATION_BY_DISPLAYID_TRANSACTION = 5001;
    public static final int GET_CONFIGURATION_TRANSACTION = 46;
    public static final int GET_CONTENT_PROVIDER_EXTERNAL_TRANSACTION = 141;
    public static final int GET_CONTENT_PROVIDER_TRANSACTION = 29;
    public static final int GET_CURRENT_USER_TRANSACTION = 145;
    public static final int GET_DEVICE_CONFIGURATION_TRANSACTION = 84;
    public static final int GET_DUMP_MEMORYINFO = 4061;
    public static final int GET_FOCUSED_STACK_ID_TRANSACTION = 283;
    public static final int GET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION = 124;
    public static final int GET_GRABED_INTENT_SENDERS_TRANSACTION = 6002;
    public static final int GET_INTENT_FOR_INTENT_SENDER_TRANSACTION = 161;
    public static final int GET_INTENT_SENDER_TRANSACTION = 63;
    public static final int GET_KID_FOR_INTENT_SENDER_TRANSACTION = 190;
    public static final int GET_LAUNCHED_FROM_PACKAGE_TRANSACTION = 164;
    public static final int GET_LAUNCHED_FROM_UID_TRANSACTION = 150;
    public static final int GET_LOCK_TASK_MODE_STATE_TRANSACTION = 287;
    public static final int GET_MEMORY_INFO_TRANSACTION = 76;
    public static final int GET_MY_MEMORY_STATE_TRANSACTION = 143;
    public static final int GET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION = 128;
    public static final int GET_PACKAGE_FOR_INTENT_SENDER_TRANSACTION = 65;
    public static final int GET_PACKAGE_FOR_TOKEN_TRANSACTION = 50;
    public static final int GET_PACKAGE_FROM_APP_PROCESSES_TRANSACTION = 3102;
    public static final int GET_PACKAGE_PROCESS_STATE_TRANSACTION = 294;
    public static final int GET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION = 126;
    public static final int GET_PERSISTED_URI_PERMISSIONS_TRANSACTION = 182;
    public static final int GET_PROCESSES_IN_ERROR_STATE_TRANSACTION = 77;
    public static final int GET_PROCESS_LIMIT_TRANSACTION = 52;
    public static final int GET_PROCESS_MEMORY_INFO_TRANSACTION = 98;
    public static final int GET_PROCESS_PSS_TRANSACTION = 137;
    public static final int GET_PROVIDER_MIME_TYPE_TRANSACTION = 115;
    public static final int GET_RECENT_TASKS_TRANSACTION = 60;
    public static final int GET_REQUESTED_ORIENTATION_TRANSACTION = 71;
    public static final int GET_RUNNING_APP_PROCESSES_TRANSACTION = 83;
    public static final int GET_RUNNING_EXTERNAL_APPLICATIONS_TRANSACTION = 108;
    public static final int GET_RUNNING_SERVICE_CONTROL_PANEL_TRANSACTION = 33;
    public static final int GET_RUNNING_USER_IDS_TRANSACTION = 157;
    public static final int GET_SERVICES_TRANSACTION = 81;
    public static final int GET_SMPKGS_LIST = 1203;
    public static final int GET_STACK_INFO_TRANSACTION = 173;
    public static final int GET_TAG_FOR_INTENT_SENDER_TRANSACTION = 211;
    public static final int GET_TASKS_BY_DISPLAY_TRANSACTION = 5003;
    public static final int GET_TASKS_TRANSACTION = 23;
    public static final int GET_TASK_DESCRIPTION_ICON_TRANSACTION = 239;
    public static final int GET_TASK_FOR_ACTIVITY_TRANSACTION = 27;
    public static final int GET_TASK_THUMBNAIL_TRANSACTION = 82;
    public static final int GET_UID_FOR_INTENT_SENDER_TRANSACTION = 93;
    public static final int GRAB_INTENT_SENDER_TRANSACTION = 6001;
    public static final int GRANT_URI_PERMISSION_FROM_OWNER_TRANSACTION = 117;
    public static final int GRANT_URI_PERMISSION_TRANSACTION = 55;
    public static final int HANDLE_APPLICATION_CRASH_TRANSACTION = 2;
    public static final int HANDLE_APPLICATION_STRICT_MODE_VIOLATION_TRANSACTION = 110;
    public static final int HANDLE_APPLICATION_WTF_TRANSACTION = 102;
    public static final int HANDLE_INCOMING_USER_TRANSACTION = 94;
    public static final int HANG_TRANSACTION = 167;
    public static final int INPUT_DISPATCHING_TIMED_OUT_TRANSACTION = 159;
    public static final int IS_APPLOCKED_PACKAGE_TRANSACTION = 7003;
    public static final int IS_APPLOCKED_VERIFYING_TRANSACTION = 7008;
    public static final int IS_BACKGROUND_VISIBLE_BEHIND_TRANSACTION = 227;
    public static final int IS_IMMERSIVE_TRANSACTION = 111;
    public static final int IS_INTENT_SENDER_AN_ACTIVITY_TRANSACTION = 152;
    public static final int IS_INTENT_SENDER_TARGETED_TO_PACKAGE_TRANSACTION = 135;
    public static final int IS_IN_HOME_STACK_TRANSACTION = 213;
    public static final int IS_IN_LOCK_TASK_MODE_TRANSACTION = 217;
    public static final int IS_ROOT_VOICE_INTERACTION_TRANSACTION = 302;
    public static final int IS_SCREEN_CAPTURE_ALLOWED_ON_CURRENT_ACTIVITY_TRANSACTION = 300;
    public static final int IS_SNN_ENABLE = 4053;
    public static final int IS_TOP_ACTIVITY_IMMERSIVE_TRANSACTION = 113;
    public static final int IS_TOP_OF_TASK_TRANSACTION = 225;
    public static final int IS_USER_A_MONKEY_TRANSACTION = 104;
    public static final int IS_USER_RUNNING_TRANSACTION = 122;
    public static final int KEYGUARD_GOING_AWAY_TRANSACTION = 297;
    public static final int KEYGUARD_WAITING_FOR_ACTIVITY_DRAWN_TARGET_TRANSACTION = 1102;
    public static final int KEYGUARD_WAITING_FOR_ACTIVITY_DRAWN_TRANSACTION = 232;
    public static final int KILL_ALL_BACKGROUND_PROCESSES_TRANSACTION = 140;
    public static final int KILL_APPLICATION_PROCESS_TRANSACTION = 99;
    public static final int KILL_APPLICATION_WITH_APPID_TRANSACTION = 96;
    public static final int KILL_BACKGROUND_PROCESSES_TRANSACTION = 103;
    public static final int KILL_PIDS_TRANSACTION = 80;
    public static final int KILL_PROCESSES_BELOW_FOREGROUND_TRANSACTION = 144;
    public static final int KILL_UID_TRANSACTION = 165;
    public static final int KNOX_CONTAINER_RUNTIME_STATE_TRANSACTION = 189;
    public static final int LAUNCH_ASSIST_INTENT_TRANSACTION = 240;
    public static final int MOVE_ACTIVITY_TASK_TO_BACK_TRANSACTION = 75;
    public static final int MOVE_TASK_BACKWARDS_TRANSACTION = 26;
    public static final int MOVE_TASK_TO_FRONT_TRANSACTION = 24;
    public static final int MOVE_TASK_TO_STACK_TRANSACTION = 169;
    public static final int MULTIWINDOW_MINIMIZE_TRANSITION = 1013;
    public static final int MULTIWINDOW_SETTING_CHANGED_TRANSACTION = 1011;
    public static final int NAVIGATE_UP_TO_TRANSACTION = 147;
    public static final int NEW_URI_PERMISSION_OWNER_TRANSACTION = 116;
    public static final int NOTE_ALARM_FINISH_TRANSACTION = 293;
    public static final int NOTE_ALARM_START_TRANSACTION = 292;
    public static final int NOTE_WAKEUP_ALARM_TRANSACTION = 68;
    public static final int NOTIFY_ACTIVITY_DRAWN_TRANSACTION = 176;
    public static final int NOTIFY_CASCADE_STACK_ROTATED = 1015;
    public static final int NOTIFY_CLEARTEXT_NETWORK_TRANSACTION = 281;
    public static final int NOTIFY_DISPLAY_FREEZE_STOPPED_TRANSITION = 1014;
    public static final int NOTIFY_ENTER_ANIMATION_COMPLETE_TRANSACTION = 231;
    public static final int NOTIFY_LAUNCH_TASK_BEHIND_COMPLETE_TRANSACTION = 229;
    public static final int OPEN_CONTENT_URI_TRANSACTION = 5;
    public static final int OVERRIDE_PENDING_TRANSITION_TRANSACTION = 101;
    public static final int PEEK_SERVICE_TRANSACTION = 85;
    public static final int PERFORM_IDLE_MAINTENANCE_TRANSACTION = 179;
    public static final int PROFILE_CONTROL_TRANSACTION = 86;
    public static final int PUBLISH_CONTENT_PROVIDERS_TRANSACTION = 30;
    public static final int PUBLISH_SERVICE_TRANSACTION = 38;
    public static final int QUERY_REGISTERED_RECEIVER_PACKAGES_TRANSACTION = 6031;
    public static final int REF_CONTENT_PROVIDER_TRANSACTION = 31;
    public static final int REGISTER_PROCESS_OBSERVER_TRANSACTION = 133;
    public static final int REGISTER_RECEIVER_TRANSACTION = 12;
    public static final int REGISTER_TASK_STACK_LISTENER_TRANSACTION = 243;
    public static final int REGISTER_UID_OBSERVER_TRANSACTION = 298;
    public static final int REGISTER_USER_SWITCH_OBSERVER_TRANSACTION = 155;
    public static final int RELEASE_ACTIVITY_INSTANCE_TRANSACTION = 236;
    public static final int RELEASE_PERSISTABLE_URI_PERMISSION_TRANSACTION = 181;
    public static final int RELEASE_SOME_ACTIVITIES_TRANSACTION = 237;
    public static final int REMOVE_CONTENT_PROVIDER_EXTERNAL_TRANSACTION = 142;
    public static final int REMOVE_CONTENT_PROVIDER_TRANSACTION = 69;
    public static final int REMOVE_DELETED_PACKAGE_TRANSACTION = 4051;
    public static final int REMOVE_TASK_EXT_TRANSACTION = 1104;
    public static final int REMOVE_TASK_TRANSACTION = 132;
    public static final int REPORT_ACTIVITY_FULLY_DRAWN_TRANSACTION = 177;
    public static final int REPORT_ASSIST_CONTEXT_EXTRAS_TRANSACTION = 163;
    public static final int REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION = 285;
    public static final int REQUEST_BUG_REPORT_TRANSACTION = 158;
    public static final int REQUEST_VISIBLE_BEHIND_TRANSACTION = 226;
    public static final int RESIZE_STACK_TRANSACTION = 170;
    public static final int RESIZE_TASK_TRANSACTION = 286;
    public static final int RESTART_TRANSACTION = 178;
    public static final int RESUME_APP_SWITCHES_TRANSACTION = 89;
    public static final int REVOKE_URI_PERMISSION_FROM_OWNER_TRANSACTION = 118;
    public static final int REVOKE_URI_PERMISSION_TRANSACTION = 56;
    public static final int SERVICE_DONE_EXECUTING_TRANSACTION = 61;
    public static final int SET_ACTIVITY_CONTROLLER_TRANSACTION = 57;
    public static final int SET_ALWAYS_FINISH_TRANSACTION = 43;
    public static final int SET_APPLOCKED_UNLOCK_TRANSACTION = 7002;
    public static final int SET_APPLOCKED_VERIFYING_TRANSACTION = 7007;
    public static final int SET_BACKWINDOW_SHOW_TRANSACTION = 5005;
    public static final int SET_CUSTOM_IMAGE_FOR_PACKAGE_TRANSACTION = 6042;
    public static final int SET_CUSTOM_IMAGE_TRANSACTION = 6041;
    public static final int SET_DEBUG_APP_TRANSACTION = 42;
    public static final int SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION = 288;
    public static final int SET_FOCUSED_STACK_BY_TAP_OUTSIDE_TRANSACTION = 1012;
    public static final int SET_FOCUSED_STACK_TRANSACTION = 172;
    public static final int SET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION = 125;
    public static final int SET_IMMERSIVE_TRANSACTION = 112;
    public static final int SET_LOCK_SCREEN_SHOWN_TRANSACTION = 148;
    public static final int SET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION = 129;
    public static final int SET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION = 127;
    public static final int SET_PROCESS_FOREGROUND_EX_TRANSACTION = 3101;
    public static final int SET_PROCESS_FOREGROUND_TRANSACTION = 73;
    public static final int SET_PROCESS_LIMIT_TRANSACTION = 51;
    public static final int SET_PROCESS_MEMORY_TRIM_TRANSACTION = 187;
    public static final int SET_REQUESTED_ORIENTATION_TRANSACTION = 70;
    public static final int SET_SERVICE_FOREGROUND_TRANSACTION = 74;
    public static final int SET_SNN_ENABLE = 4052;
    public static final int SET_TASK_DESCRIPTION_TRANSACTION = 218;
    public static final int SET_TASK_RESIZEABLE_TRANSACTION = 284;
    public static final int SET_USER_IS_MONKEY_TRANSACTION = 166;
    public static final int SET_VOICE_KEEP_AWAKE_TRANSACTION = 290;
    public static final int SHOULD_UP_RECREATE_TASK_TRANSACTION = 146;
    public static final int SHOW_ASSIST_FROM_ACTIVITY_TRANSACTION = 301;
    public static final int SHOW_BOOT_MESSAGE_TRANSACTION = 138;
    public static final int SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION = 295;
    public static final int SHOW_WAITING_FOR_DEBUGGER_TRANSACTION = 58;
    public static final int SHUTDOWN_TRANSACTION = 87;
    public static final int SIGNAL_PERSISTENT_PROCESSES_TRANSACTION = 59;
    public static final int START_ACTIVITIES_TRANSACTION = 121;
    public static final int START_ACTIVITY_AND_WAIT_TRANSACTION = 105;
    public static final int START_ACTIVITY_AS_CALLER_TRANSACTION = 233;
    public static final int START_ACTIVITY_AS_USER_TRANSACTION = 153;
    public static final int START_ACTIVITY_FROM_RECENTS_TRANSACTION = 230;
    public static final int START_ACTIVITY_INTENT_SENDER_TRANSACTION = 100;
    public static final int START_ACTIVITY_TRANSACTION = 3;
    public static final int START_ACTIVITY_WITH_CONFIG_TRANSACTION = 107;
    public static final int START_BACKUP_AGENT_TRANSACTION = 90;
    public static final int START_INSTRUMENTATION_TRANSACTION = 44;
    public static final int START_IN_PLACE_ANIMATION_TRANSACTION = 241;
    public static final int START_LOCK_TASK_BY_CURRENT_TRANSACTION = 222;
    public static final int START_LOCK_TASK_BY_TASK_ID_TRANSACTION = 214;
    public static final int START_LOCK_TASK_BY_TOKEN_TRANSACTION = 215;
    public static final int START_NEXT_MATCHING_ACTIVITY_TRANSACTION = 67;
    public static final int START_SERVICE_TRANSACTION = 34;
    public static final int START_SERVICE_WITH_ACTIVITY_TOKEN_TRANSACTION = 5002;
    public static final int START_USER_IN_BACKGROUND_TRANSACTION = 212;
    public static final int START_VOICE_ACTIVITY_TRANSACTION = 219;
    public static final int STOP_APP_SWITCHES_TRANSACTION = 88;
    public static final int STOP_LOCK_TASK_BY_CURRENT_TRANSACTION = 223;
    public static final int STOP_LOCK_TASK_MODE_TRANSACTION = 216;
    public static final int STOP_SERVICE_TOKEN_TRANSACTION = 48;
    public static final int STOP_SERVICE_TRANSACTION = 35;
    public static final int STOP_USER_TRANSACTION = 154;
    public static final int SWITCH_STACK_TRANSACTION = 1010;
    public static final int SWITCH_USER_TRANSACTION = 130;
    public static final int TAKE_PERSISTABLE_URI_PERMISSION_TRANSACTION = 180;
    public static final int UNBIND_BACKUP_AGENT_TRANSACTION = 92;
    public static final int UNBIND_FINISHED_TRANSACTION = 72;
    public static final int UNBIND_SERVICE_TRANSACTION = 37;
    public static final int UNBROADCAST_INTENT_TRANSACTION = 15;
    public static final int UNFREEZE_APP = 1202;
    public static final int UNHANDLED_BACK_TRANSACTION = 4;
    public static final int UNREGISTER_PROCESS_OBSERVER_TRANSACTION = 134;
    public static final int UNREGISTER_RECEIVER_TRANSACTION = 13;
    public static final int UNREGISTER_UID_OBSERVER_TRANSACTION = 299;
    public static final int UNREGISTER_USER_SWITCH_OBSERVER_TRANSACTION = 156;
    public static final int UNSTABLE_PROVIDER_DIED_TRANSACTION = 151;
    public static final int UPDATE_CONFIGURATION_TRANSACTION = 47;
    public static final int UPDATE_DEVICE_OWNER_TRANSACTION = 296;
    public static final int UPDATE_LOCK_TASK_PACKAGES_TRANSACTION = 291;
    public static final int UPDATE_PERSISTENT_CONFIGURATION_TRANSACTION = 136;
    public static final int UPDATE_PROCESS_CONFIGURATION_TRANSACTION = 5004;
    public static final int WILL_ACTIVITY_BE_VISIBLE_TRANSACTION = 106;
    public static final int ___AVAILABLE_1___ = 131;
    public static final String descriptor = "android.app.IActivityManager";

    public static class ContentProviderHolder implements Parcelable {
        public static final Creator<ContentProviderHolder> CREATOR = new Creator<ContentProviderHolder>() {
            public ContentProviderHolder createFromParcel(Parcel source) {
                return new ContentProviderHolder(source);
            }

            public ContentProviderHolder[] newArray(int size) {
                return new ContentProviderHolder[size];
            }
        };
        public IBinder connection;
        public final ProviderInfo info;
        public boolean noReleaseNeeded;
        public IContentProvider provider;

        public ContentProviderHolder(ProviderInfo _info) {
            this.info = _info;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i = 0;
            this.info.writeToParcel(dest, 0);
            if (this.provider != null) {
                dest.writeStrongBinder(this.provider.asBinder());
            } else {
                dest.writeStrongBinder(null);
            }
            dest.writeStrongBinder(this.connection);
            if (this.noReleaseNeeded) {
                i = 1;
            }
            dest.writeInt(i);
        }

        private ContentProviderHolder(Parcel source) {
            this.info = (ProviderInfo) ProviderInfo.CREATOR.createFromParcel(source);
            this.provider = ContentProviderNative.asInterface(source.readStrongBinder());
            this.connection = source.readStrongBinder();
            this.noReleaseNeeded = source.readInt() != 0;
        }
    }

    public static class WaitResult implements Parcelable {
        public static final Creator<WaitResult> CREATOR = new Creator<WaitResult>() {
            public WaitResult createFromParcel(Parcel source) {
                return new WaitResult(source);
            }

            public WaitResult[] newArray(int size) {
                return new WaitResult[size];
            }
        };
        public int result;
        public long thisTime;
        public boolean timeout;
        public long totalTime;
        public ComponentName who;

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.result);
            dest.writeInt(this.timeout ? 1 : 0);
            ComponentName.writeToParcel(this.who, dest);
            dest.writeLong(this.thisTime);
            dest.writeLong(this.totalTime);
        }

        private WaitResult(Parcel source) {
            this.result = source.readInt();
            this.timeout = source.readInt() != 0;
            this.who = ComponentName.readFromParcel(source);
            this.thisTime = source.readLong();
            this.totalTime = source.readLong();
        }
    }

    void activityDestroyed(IBinder iBinder) throws RemoteException;

    void activityIdle(IBinder iBinder, Configuration configuration, boolean z) throws RemoteException;

    void activityPaused(IBinder iBinder) throws RemoteException;

    void activityResumed(IBinder iBinder) throws RemoteException;

    void activitySlept(IBinder iBinder) throws RemoteException;

    void activityStopped(IBinder iBinder, Bundle bundle, PersistableBundle persistableBundle, CharSequence charSequence) throws RemoteException;

    int addAppTask(IBinder iBinder, Intent intent, TaskDescription taskDescription, Bitmap bitmap) throws RemoteException;

    void addPackageDependency(String str) throws RemoteException;

    void appNotRespondingViaProvider(IBinder iBinder) throws RemoteException;

    void attachApplication(IApplicationThread iApplicationThread) throws RemoteException;

    void backgroundResourcesReleased(IBinder iBinder) throws RemoteException;

    void backupAgentCreated(String str, IBinder iBinder) throws RemoteException;

    boolean bindBackupAgent(String str, int i, int i2) throws RemoteException;

    int bindService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, int i2) throws RemoteException;

    void bootAnimationComplete() throws RemoteException;

    int broadcastIntent(IApplicationThread iApplicationThread, Intent intent, String str, IIntentReceiver iIntentReceiver, int i, String str2, Bundle bundle, String[] strArr, int i2, Bundle bundle2, boolean z, boolean z2, int i3) throws RemoteException;

    void cancelIntentSender(IIntentSender iIntentSender) throws RemoteException;

    int checkGrantUriPermission(int i, String str, Uri uri, int i2, int i3) throws RemoteException;

    boolean checkKnoxPermission(int i, int i2, String str, int i3, String str2) throws RemoteException;

    boolean checkKnoxPermission(String str, int i, String str2, int i2, String str3) throws RemoteException;

    int checkPermission(String str, int i, int i2) throws RemoteException;

    int checkPermissionWithToken(String str, int i, int i2, IBinder iBinder) throws RemoteException;

    int checkUriPermission(Uri uri, int i, int i2, int i3, int i4, IBinder iBinder) throws RemoteException;

    void clearAppLockedUnLockedApp() throws RemoteException;

    boolean clearApplicationUserData(String str, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    void clearPendingBackup() throws RemoteException;

    boolean clearRecentTasks(int i) throws RemoteException;

    void closeSystemDialogs(String str) throws RemoteException;

    boolean convertFromTranslucent(IBinder iBinder) throws RemoteException;

    boolean convertFromTranslucent(IBinder iBinder, boolean z) throws RemoteException;

    boolean convertToTranslucent(IBinder iBinder, ActivityOptions activityOptions) throws RemoteException;

    void crashApplication(int i, int i2, String str, String str2) throws RemoteException;

    IActivityContainer createStackOnDisplay(int i) throws RemoteException;

    IActivityContainer createVirtualActivityContainer(IBinder iBinder, IActivityContainerCallback iActivityContainerCallback) throws RemoteException;

    void deleteActivityContainer(IActivityContainer iActivityContainer) throws RemoteException;

    boolean dumpHeap(String str, int i, boolean z, String str2, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void dumpHeapFinished(String str) throws RemoteException;

    void enableSafeMode() throws RemoteException;

    void enterSafeMode() throws RemoteException;

    boolean finishActivity(IBinder iBinder, int i, Intent intent, boolean z) throws RemoteException;

    boolean finishActivityAffinity(IBinder iBinder) throws RemoteException;

    void finishHeavyWeightApp() throws RemoteException;

    void finishInstrumentation(IApplicationThread iApplicationThread, int i, Bundle bundle) throws RemoteException;

    void finishReceiver(IBinder iBinder, int i, String str, Bundle bundle, boolean z, int i2) throws RemoteException;

    void finishSubActivity(IBinder iBinder, String str, int i) throws RemoteException;

    void finishVoiceTask(IVoiceInteractionSession iVoiceInteractionSession) throws RemoteException;

    void forceStopPackage(String str, int i) throws RemoteException;

    void forceStopPackageByAdmin(String str, int i) throws RemoteException;

    ComponentName getActivityClassForToken(IBinder iBinder) throws RemoteException;

    int getActivityDisplayId(IBinder iBinder) throws RemoteException;

    ActivityOptions getActivityOptions(IBinder iBinder) throws RemoteException;

    List<StackInfo> getAllStackInfos() throws RemoteException;

    String getAppLockedCheckAction() throws RemoteException;

    String getAppLockedLockType() throws RemoteException;

    ArrayList<String> getAppLockedPackageList() throws RemoteException;

    Point getAppTaskThumbnailSize() throws RemoteException;

    List<IAppTask> getAppTasks(String str) throws RemoteException;

    Bundle getAssistContextExtras(int i) throws RemoteException;

    ComponentName getCallingActivity(IBinder iBinder) throws RemoteException;

    String getCallingPackage(IBinder iBinder) throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    Configuration getConfiguration(int i) throws RemoteException;

    ContentProviderHolder getContentProvider(IApplicationThread iApplicationThread, String str, int i, boolean z) throws RemoteException;

    ContentProviderHolder getContentProviderExternal(String str, int i, IBinder iBinder) throws RemoteException;

    UserInfo getCurrentUser() throws RemoteException;

    ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException;

    ArrayList<MemDumpInfo> getDumpMemoryInfo() throws RemoteException;

    int getFocusedStackId() throws RemoteException;

    int getFrontActivityScreenCompatMode() throws RemoteException;

    Map getGrabedIntentSenders() throws RemoteException;

    Intent getIntentForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    IIntentSender getIntentSender(int i, String str, IBinder iBinder, String str2, int i2, Intent[] intentArr, String[] strArr, int i3, Bundle bundle, int i4) throws RemoteException;

    int getKidForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    int getLaunchedFromUid(IBinder iBinder) throws RemoteException;

    int getLockTaskModeState() throws RemoteException;

    void getMemoryInfo(MemoryInfo memoryInfo) throws RemoteException;

    void getMyMemoryState(RunningAppProcessInfo runningAppProcessInfo) throws RemoteException;

    boolean getPackageAskScreenCompat(String str) throws RemoteException;

    String getPackageForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    String getPackageForToken(IBinder iBinder) throws RemoteException;

    String getPackageFromAppProcesses(int i) throws RemoteException;

    int getPackageProcessState(String str, String str2) throws RemoteException;

    int getPackageScreenCompatMode(String str) throws RemoteException;

    ParceledListSlice<UriPermission> getPersistedUriPermissions(String str, boolean z) throws RemoteException;

    int getProcessLimit() throws RemoteException;

    Debug.MemoryInfo[] getProcessMemoryInfo(int[] iArr) throws RemoteException;

    long[] getProcessPss(int[] iArr) throws RemoteException;

    List<ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException;

    String getProviderMimeType(Uri uri, int i) throws RemoteException;

    List<RecentTaskInfo> getRecentTasks(int i, int i2, int i3) throws RemoteException;

    int getRequestedOrientation(IBinder iBinder) throws RemoteException;

    List<RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

    List<ApplicationInfo> getRunningExternalApplications() throws RemoteException;

    PendingIntent getRunningServiceControlPanel(ComponentName componentName) throws RemoteException;

    int[] getRunningUserIds() throws RemoteException;

    void getSMpkgsList(List<String> list) throws RemoteException;

    List<RunningServiceInfo> getServices(int i, int i2) throws RemoteException;

    StackInfo getStackInfo(int i) throws RemoteException;

    String getTagForIntentSender(IIntentSender iIntentSender, String str) throws RemoteException;

    Bitmap getTaskDescriptionIcon(String str) throws RemoteException;

    int getTaskForActivity(IBinder iBinder, boolean z) throws RemoteException;

    TaskThumbnail getTaskThumbnail(int i) throws RemoteException;

    List<RunningTaskInfo> getTasks(int i, int i2) throws RemoteException;

    List<RunningTaskInfo> getTasks(int i, int i2, int i3) throws RemoteException;

    int getUidForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    IIntentSender grabIntentSender(IIntentSender iIntentSender, String str, long[] jArr) throws RemoteException;

    void grantUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void grantUriPermissionFromOwner(IBinder iBinder, int i, String str, Uri uri, int i2, int i3, int i4) throws RemoteException;

    void handleApplicationCrash(IBinder iBinder, CrashInfo crashInfo) throws RemoteException;

    void handleApplicationStrictModeViolation(IBinder iBinder, int i, ViolationInfo violationInfo) throws RemoteException;

    boolean handleApplicationWtf(IBinder iBinder, String str, boolean z, CrashInfo crashInfo) throws RemoteException;

    int handleIncomingUser(int i, int i2, int i3, boolean z, boolean z2, String str, String str2) throws RemoteException;

    void hang(IBinder iBinder, boolean z) throws RemoteException;

    long inputDispatchingTimedOut(int i, boolean z, String str) throws RemoteException;

    boolean isAppLockedPackage(String str) throws RemoteException;

    boolean isAppLockedVerifying(String str) throws RemoteException;

    boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException;

    boolean isAutoRunBlockedApp(String str) throws RemoteException;

    boolean isBackgroundVisibleBehind(IBinder iBinder) throws RemoteException;

    boolean isImmersive(IBinder iBinder) throws RemoteException;

    boolean isInHomeStack(int i) throws RemoteException;

    boolean isInLockTaskMode() throws RemoteException;

    boolean isIntentSenderAnActivity(IIntentSender iIntentSender) throws RemoteException;

    boolean isIntentSenderTargetedToPackage(IIntentSender iIntentSender) throws RemoteException;

    boolean isRootVoiceInteraction(IBinder iBinder) throws RemoteException;

    int isSNNEnable() throws RemoteException;

    boolean isTopActivityImmersive() throws RemoteException;

    boolean isTopOfTask(IBinder iBinder) throws RemoteException;

    boolean isUserAMonkey() throws RemoteException;

    boolean isUserRunning(int i, boolean z) throws RemoteException;

    void keyguardGoingAway(boolean z, boolean z2) throws RemoteException;

    void keyguardWaitingForActivityDrawn() throws RemoteException;

    void keyguardWaitingForActivityDrawnTarget(Intent intent) throws RemoteException;

    void killAllBackgroundProcesses() throws RemoteException;

    void killApplicationProcess(String str, int i) throws RemoteException;

    void killApplicationWithAppId(String str, int i, String str2) throws RemoteException;

    void killBackgroundProcesses(String str, int i) throws RemoteException;

    boolean killPids(int[] iArr, String str, boolean z) throws RemoteException;

    boolean killProcessesBelowForeground(String str) throws RemoteException;

    void killUid(int i, int i2, String str) throws RemoteException;

    boolean launchAssistIntent(Intent intent, int i, String str, int i2, Bundle bundle) throws RemoteException;

    boolean moveActivityTaskToBack(IBinder iBinder, boolean z) throws RemoteException;

    void moveTaskBackwards(int i) throws RemoteException;

    void moveTaskToFront(int i, int i2, Bundle bundle) throws RemoteException;

    void moveTaskToStack(int i, int i2, boolean z) throws RemoteException;

    void multiWindowSettingChanged(boolean z) throws RemoteException;

    boolean navigateUpTo(IBinder iBinder, Intent intent, int i, Intent intent2) throws RemoteException;

    IBinder newUriPermissionOwner(String str) throws RemoteException;

    void noteAlarmFinish(IIntentSender iIntentSender, int i, String str) throws RemoteException;

    void noteAlarmStart(IIntentSender iIntentSender, int i, String str) throws RemoteException;

    void noteWakeupAlarm(IIntentSender iIntentSender, int i, String str, String str2) throws RemoteException;

    void notifyActivityDrawn(IBinder iBinder) throws RemoteException;

    void notifyCascadeStackRotated(int i, Rect rect) throws RemoteException;

    void notifyCleartextNetwork(int i, byte[] bArr) throws RemoteException;

    void notifyDisplayFreezeStopped() throws RemoteException;

    void notifyEnterAnimationComplete(IBinder iBinder) throws RemoteException;

    void notifyLaunchTaskBehindComplete(IBinder iBinder) throws RemoteException;

    void notifyMinimizeMultiWindow(IBinder iBinder) throws RemoteException;

    ParcelFileDescriptor openContentUri(Uri uri) throws RemoteException;

    void overridePendingTransition(IBinder iBinder, String str, int i, int i2) throws RemoteException;

    IBinder peekService(Intent intent, String str, String str2) throws RemoteException;

    void performIdleMaintenance() throws RemoteException;

    boolean profileControl(String str, int i, boolean z, ProfilerInfo profilerInfo, int i2) throws RemoteException;

    void publishContentProviders(IApplicationThread iApplicationThread, List<ContentProviderHolder> list) throws RemoteException;

    void publishService(IBinder iBinder, Intent intent, IBinder iBinder2) throws RemoteException;

    String[] queryRegisteredReceiverPackages(Intent intent, String str, int i) throws RemoteException;

    boolean refContentProvider(IBinder iBinder, int i, int i2) throws RemoteException;

    void registerProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    Intent registerReceiver(IApplicationThread iApplicationThread, String str, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String str2, int i) throws RemoteException;

    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void registerUidObserver(IUidObserver iUidObserver) throws RemoteException;

    void registerUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) throws RemoteException;

    boolean releaseActivityInstance(IBinder iBinder) throws RemoteException;

    void releasePersistableUriPermission(Uri uri, int i, int i2) throws RemoteException;

    void releaseSomeActivities(IApplicationThread iApplicationThread) throws RemoteException;

    void removeContentProvider(IBinder iBinder, boolean z) throws RemoteException;

    void removeContentProviderExternal(String str, IBinder iBinder) throws RemoteException;

    void removeDeletedPkgsFromLru(String str) throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    boolean removeTask(int i, int i2) throws RemoteException;

    void reportActivityFullyDrawn(IBinder iBinder) throws RemoteException;

    void reportAssistContextExtras(IBinder iBinder, Bundle bundle, AssistStructure assistStructure, AssistContent assistContent, Uri uri) throws RemoteException;

    boolean requestAssistContextExtras(int i, IResultReceiver iResultReceiver, IBinder iBinder) throws RemoteException;

    void requestBugReport() throws RemoteException;

    boolean requestVisibleBehind(IBinder iBinder, boolean z) throws RemoteException;

    void resizeStack(int i, Rect rect) throws RemoteException;

    void resizeTask(int i, Rect rect) throws RemoteException;

    void restart() throws RemoteException;

    void resumeAppSwitches() throws RemoteException;

    void revokeUriPermission(IApplicationThread iApplicationThread, Uri uri, int i, int i2) throws RemoteException;

    void revokeUriPermissionFromOwner(IBinder iBinder, Uri uri, int i, int i2) throws RemoteException;

    void serviceDoneExecuting(IBinder iBinder, int i, int i2, int i3) throws RemoteException;

    void setActivityController(IActivityController iActivityController) throws RemoteException;

    void setAlwaysFinish(boolean z) throws RemoteException;

    void setAppLockedUnLockPackage(String str) throws RemoteException;

    void setAppLockedVerifying(String str, boolean z) throws RemoteException;

    void setBackWindowShown(boolean z) throws RemoteException;

    boolean setCustomImage(IBinder iBinder, ParcelFileDescriptor parcelFileDescriptor, int i) throws RemoteException;

    boolean setCustomImageForPackage(ComponentName componentName, int i, ParcelFileDescriptor parcelFileDescriptor, int i2) throws RemoteException;

    void setDebugApp(String str, boolean z, boolean z2) throws RemoteException;

    void setDumpHeapDebugLimit(String str, int i, long j, String str2) throws RemoteException;

    void setFocusedStack(int i) throws RemoteException;

    void setFocusedStack(int i, boolean z) throws RemoteException;

    void setFrontActivityScreenCompatMode(int i) throws RemoteException;

    void setImmersive(IBinder iBinder, boolean z) throws RemoteException;

    void setLockScreenShown(boolean z) throws RemoteException;

    void setPackageAskScreenCompat(String str, boolean z) throws RemoteException;

    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    void setProcessForeground(IBinder iBinder, int i, boolean z) throws RemoteException;

    void setProcessForeground(IBinder iBinder, int i, boolean z, boolean z2) throws RemoteException;

    void setProcessLimit(int i) throws RemoteException;

    boolean setProcessMemoryTrimLevel(String str, int i, int i2) throws RemoteException;

    void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException;

    void setSNNEnable(boolean z) throws RemoteException;

    void setServiceForeground(ComponentName componentName, IBinder iBinder, int i, Notification notification, boolean z) throws RemoteException;

    void setTaskDescription(IBinder iBinder, TaskDescription taskDescription) throws RemoteException;

    void setTaskResizeable(int i, boolean z) throws RemoteException;

    void setUserIsMonkey(boolean z) throws RemoteException;

    void setVoiceKeepAwake(IVoiceInteractionSession iVoiceInteractionSession, boolean z) throws RemoteException;

    boolean shouldUpRecreateTask(IBinder iBinder, String str) throws RemoteException;

    boolean showAssistFromActivity(IBinder iBinder, Bundle bundle) throws RemoteException;

    void showBootMessage(CharSequence charSequence, boolean z) throws RemoteException;

    void showLockTaskEscapeMessage(IBinder iBinder) throws RemoteException;

    void showWaitingForDebugger(IApplicationThread iApplicationThread, boolean z) throws RemoteException;

    boolean shutdown(int i) throws RemoteException;

    void signalPersistentProcesses(int i) throws RemoteException;

    int startActivities(IApplicationThread iApplicationThread, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i) throws RemoteException;

    int startActivity(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    WaitResult startActivityAndWait(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityAsCaller(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, boolean z, int i3) throws RemoteException;

    int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    int startActivityIntentSender(IApplicationThread iApplicationThread, IntentSender intentSender, Intent intent, String str, IBinder iBinder, String str2, int i, int i2, int i3, Bundle bundle) throws RemoteException;

    int startActivityWithConfig(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, Configuration configuration, Bundle bundle, int i3) throws RemoteException;

    void startInPlaceAnimationOnFrontMostApplication(ActivityOptions activityOptions) throws RemoteException;

    boolean startInstrumentation(ComponentName componentName, String str, int i, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i2, String str2) throws RemoteException;

    void startLockTaskMode(int i) throws RemoteException;

    void startLockTaskMode(IBinder iBinder) throws RemoteException;

    void startLockTaskModeOnCurrent() throws RemoteException;

    boolean startNextMatchingActivity(IBinder iBinder, Intent intent, Bundle bundle) throws RemoteException;

    ComponentName startService(IApplicationThread iApplicationThread, Intent intent, String str, String str2, int i) throws RemoteException;

    ComponentName startService(IApplicationThread iApplicationThread, Intent intent, String str, String str2, int i, IBinder iBinder, int i2) throws RemoteException;

    boolean startUserInBackground(int i) throws RemoteException;

    int startVoiceActivity(String str, int i, int i2, Intent intent, String str2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i3, ProfilerInfo profilerInfo, Bundle bundle, int i4) throws RemoteException;

    void stopAppSwitches() throws RemoteException;

    void stopLockTaskMode() throws RemoteException;

    void stopLockTaskModeOnCurrent() throws RemoteException;

    int stopService(IApplicationThread iApplicationThread, Intent intent, String str, int i) throws RemoteException;

    boolean stopServiceToken(ComponentName componentName, IBinder iBinder, int i) throws RemoteException;

    int stopUser(int i, IStopUserCallback iStopUserCallback) throws RemoteException;

    boolean switchUser(int i) throws RemoteException;

    void takePersistableUriPermission(Uri uri, int i, int i2) throws RemoteException;

    boolean testIsSystemReady();

    void unFreezeApp(String str, int i) throws RemoteException;

    void unbindBackupAgent(ApplicationInfo applicationInfo) throws RemoteException;

    void unbindFinished(IBinder iBinder, Intent intent, boolean z) throws RemoteException;

    boolean unbindService(IServiceConnection iServiceConnection) throws RemoteException;

    void unbroadcastIntent(IApplicationThread iApplicationThread, Intent intent, int i) throws RemoteException;

    void unhandledBack() throws RemoteException;

    void unregisterProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    void unregisterReceiver(IIntentReceiver iIntentReceiver) throws RemoteException;

    void unregisterUidObserver(IUidObserver iUidObserver) throws RemoteException;

    void unregisterUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) throws RemoteException;

    void unstableProviderDied(IBinder iBinder) throws RemoteException;

    void updateConfiguration(Configuration configuration) throws RemoteException;

    void updateDeviceOwner(String str) throws RemoteException;

    void updateKnoxContainerRuntimeState(int i, int i2) throws RemoteException;

    void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    void updatePersistentConfiguration(Configuration configuration) throws RemoteException;

    void updateProcessConfiguration(int i, int i2, boolean z) throws RemoteException;

    boolean willActivityBeVisible(IBinder iBinder) throws RemoteException;
}
