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
import android.app.IActivityManager.ContentProviderHolder;
import android.app.IActivityManager.WaitResult;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver.Stub;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.UriPermission;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode.ViolationInfo;
import android.service.voice.IVoiceInteractionSession;
import android.text.TextUtils;
import android.util.Singleton;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.MemDumpInfo;
import com.android.internal.os.IResultReceiver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ActivityManagerNative extends Binder implements IActivityManager {
    private static final Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>() {
        protected IActivityManager create() {
            return ActivityManagerNative.asInterface(ServiceManager.getService(Context.ACTIVITY_SERVICE));
        }
    };
    static boolean sSystemReady = false;

    public static IActivityManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IActivityManager in = (IActivityManager) obj.queryLocalInterface(IActivityManager.descriptor);
        return in == null ? new ActivityManagerProxy(obj) : in;
    }

    public static IActivityManager getDefault() {
        return (IActivityManager) gDefault.get();
    }

    public static boolean isSystemReady() {
        if (!sSystemReady) {
            sSystemReady = getDefault().testIsSystemReady();
        }
        return sSystemReady;
    }

    public static void broadcastStickyIntent(Intent intent, String permission, int userId) {
        broadcastStickyIntent(intent, permission, -1, userId);
    }

    public static void broadcastStickyIntent(Intent intent, String permission, int appOp, int userId) {
        try {
            getDefault().broadcastIntent(null, intent, null, null, -1, null, null, null, appOp, null, false, true, userId);
        } catch (RemoteException e) {
        }
    }

    public static void noteWakeupAlarm(PendingIntent ps, int sourceUid, String sourcePkg, String tag) {
        try {
            getDefault().noteWakeupAlarm(ps.getTarget(), sourceUid, sourcePkg, tag);
        } catch (RemoteException e) {
        }
    }

    public static void noteAlarmStart(PendingIntent ps, int sourceUid, String tag) {
        try {
            getDefault().noteAlarmStart(ps.getTarget(), sourceUid, tag);
        } catch (RemoteException e) {
        }
    }

    public static void noteAlarmFinish(PendingIntent ps, int sourceUid, String tag) {
        try {
            getDefault().noteAlarmFinish(ps.getTarget(), sourceUid, tag);
        } catch (RemoteException e) {
        }
    }

    public ActivityManagerNative() {
        attachInterface(this, IActivityManager.descriptor);
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        int result;
        IBinder token;
        Intent resultData;
        int resultCode;
        boolean res;
        IBinder b;
        IApplicationThread app;
        String packageName;
        Intent intent;
        int res2;
        Configuration config;
        String res3;
        ComponentName cn;
        List<RunningTaskInfo> list;
        int N;
        int i;
        ContentProviderHolder cph;
        IIntentSender res4;
        boolean result2;
        boolean success;
        boolean isit;
        int mode;
        boolean ask;
        IActivityContainer activityContainer;
        boolean converted;
        Bundle bundle;
        ActivityOptions options;
        ActivityOptions activityOptions;
        switch (code) {
            case 2:
                data.enforceInterface(IActivityManager.descriptor);
                handleApplicationCrash(data.readStrongBinder(), new CrashInfo(data));
                reply.writeNoException();
                return true;
            case 3:
                data.enforceInterface(IActivityManager.descriptor);
                result = startActivity(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readStrongBinder(), data.readString(), data.readInt(), data.readInt(), data.readInt() != 0 ? (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 4:
                data.enforceInterface(IActivityManager.descriptor);
                unhandledBack();
                reply.writeNoException();
                return true;
            case 5:
                data.enforceInterface(IActivityManager.descriptor);
                ParcelFileDescriptor pfd = openContentUri(Uri.parse(data.readString()));
                reply.writeNoException();
                if (pfd != null) {
                    reply.writeInt(1);
                    pfd.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 11:
                data.enforceInterface(IActivityManager.descriptor);
                token = data.readStrongBinder();
                resultData = null;
                resultCode = data.readInt();
                if (data.readInt() != 0) {
                    resultData = (Intent) Intent.CREATOR.createFromParcel(data);
                }
                res = finishActivity(token, resultCode, resultData, data.readInt() != 0);
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 12:
                data.enforceInterface(IActivityManager.descriptor);
                b = data.readStrongBinder();
                app = b != null ? ApplicationThreadNative.asInterface(b) : null;
                packageName = data.readString();
                b = data.readStrongBinder();
                intent = registerReceiver(app, packageName, b != null ? Stub.asInterface(b) : null, (IntentFilter) IntentFilter.CREATOR.createFromParcel(data), data.readString(), data.readInt());
                reply.writeNoException();
                if (intent != null) {
                    reply.writeInt(1);
                    intent.writeToParcel(reply, 0);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 13:
                data.enforceInterface(IActivityManager.descriptor);
                b = data.readStrongBinder();
                if (b == null) {
                    return true;
                }
                unregisterReceiver(Stub.asInterface(b));
                reply.writeNoException();
                return true;
            case 14:
                data.enforceInterface(IActivityManager.descriptor);
                b = data.readStrongBinder();
                app = b != null ? ApplicationThreadNative.asInterface(b) : null;
                intent = (Intent) Intent.CREATOR.createFromParcel(data);
                String resolvedType = data.readString();
                b = data.readStrongBinder();
                res2 = broadcastIntent(app, intent, resolvedType, b != null ? Stub.asInterface(b) : null, data.readInt(), data.readString(), data.readBundle(), data.readStringArray(), data.readInt(), data.readBundle(), data.readInt() != 0, data.readInt() != 0, data.readInt());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 15:
                data.enforceInterface(IActivityManager.descriptor);
                b = data.readStrongBinder();
                unbroadcastIntent(b != null ? ApplicationThreadNative.asInterface(b) : null, (Intent) Intent.CREATOR.createFromParcel(data), data.readInt());
                reply.writeNoException();
                return true;
            case 16:
                data.enforceInterface(IActivityManager.descriptor);
                IBinder who = data.readStrongBinder();
                resultCode = data.readInt();
                String resultData2 = data.readString();
                Bundle resultExtras = data.readBundle();
                boolean resultAbort = data.readInt() != 0;
                int intentFlags = data.readInt();
                if (who != null) {
                    finishReceiver(who, resultCode, resultData2, resultExtras, resultAbort, intentFlags);
                }
                reply.writeNoException();
                return true;
            case 17:
                data.enforceInterface(IActivityManager.descriptor);
                app = ApplicationThreadNative.asInterface(data.readStrongBinder());
                if (app != null) {
                    attachApplication(app);
                }
                reply.writeNoException();
                return true;
            case 18:
                data.enforceInterface(IActivityManager.descriptor);
                token = data.readStrongBinder();
                config = null;
                if (data.readInt() != 0) {
                    config = (Configuration) Configuration.CREATOR.createFromParcel(data);
                }
                boolean stopProfiling = data.readInt() != 0;
                if (token != null) {
                    activityIdle(token, config, stopProfiling);
                }
                reply.writeNoException();
                return true;
            case 19:
                data.enforceInterface(IActivityManager.descriptor);
                activityPaused(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 20:
                data.enforceInterface(IActivityManager.descriptor);
                activityStopped(data.readStrongBinder(), data.readBundle(), data.readPersistableBundle(), (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case 21:
                data.enforceInterface(IActivityManager.descriptor);
                token = data.readStrongBinder();
                res3 = token != null ? getCallingPackage(token) : null;
                reply.writeNoException();
                reply.writeString(res3);
                return true;
            case 22:
                data.enforceInterface(IActivityManager.descriptor);
                cn = getCallingActivity(data.readStrongBinder());
                reply.writeNoException();
                ComponentName.writeToParcel(cn, reply);
                return true;
            case 23:
                data.enforceInterface(IActivityManager.descriptor);
                list = getTasks(data.readInt(), data.readInt());
                reply.writeNoException();
                N = list != null ? list.size() : -1;
                reply.writeInt(N);
                for (i = 0; i < N; i++) {
                    ((RunningTaskInfo) list.get(i)).writeToParcel(reply, 0);
                }
                return true;
            case 24:
                data.enforceInterface(IActivityManager.descriptor);
                moveTaskToFront(data.readInt(), data.readInt(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                reply.writeNoException();
                return true;
            case 26:
                data.enforceInterface(IActivityManager.descriptor);
                moveTaskBackwards(data.readInt());
                reply.writeNoException();
                return true;
            case 27:
                data.enforceInterface(IActivityManager.descriptor);
                token = data.readStrongBinder();
                res2 = token != null ? getTaskForActivity(token, data.readInt() != 0) : -1;
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 29:
                data.enforceInterface(IActivityManager.descriptor);
                cph = getContentProvider(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), data.readInt(), data.readInt() != 0);
                reply.writeNoException();
                if (cph != null) {
                    reply.writeInt(1);
                    cph.writeToParcel(reply, 0);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 30:
                data.enforceInterface(IActivityManager.descriptor);
                publishContentProviders(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.createTypedArrayList(ContentProviderHolder.CREATOR));
                reply.writeNoException();
                return true;
            case 31:
                data.enforceInterface(IActivityManager.descriptor);
                res = refContentProvider(data.readStrongBinder(), data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 32:
                data.enforceInterface(IActivityManager.descriptor);
                finishSubActivity(data.readStrongBinder(), data.readString(), data.readInt());
                reply.writeNoException();
                return true;
            case 33:
                data.enforceInterface(IActivityManager.descriptor);
                PendingIntent pi = getRunningServiceControlPanel((ComponentName) ComponentName.CREATOR.createFromParcel(data));
                reply.writeNoException();
                PendingIntent.writePendingIntentOrNullToParcel(pi, reply);
                return true;
            case 34:
                data.enforceInterface(IActivityManager.descriptor);
                cn = startService(ApplicationThreadNative.asInterface(data.readStrongBinder()), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readString(), data.readInt());
                reply.writeNoException();
                ComponentName.writeToParcel(cn, reply);
                return true;
            case 35:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = stopService(ApplicationThreadNative.asInterface(data.readStrongBinder()), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readInt());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 36:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = bindService(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readStrongBinder(), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), IServiceConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString(), data.readInt());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 37:
                data.enforceInterface(IActivityManager.descriptor);
                res = unbindService(IServiceConnection.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 38:
                data.enforceInterface(IActivityManager.descriptor);
                publishService(data.readStrongBinder(), (Intent) Intent.CREATOR.createFromParcel(data), data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 39:
                data.enforceInterface(IActivityManager.descriptor);
                activityResumed(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 42:
                data.enforceInterface(IActivityManager.descriptor);
                setDebugApp(data.readString(), data.readInt() != 0, data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 43:
                data.enforceInterface(IActivityManager.descriptor);
                setAlwaysFinish(data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 44:
                data.enforceInterface(IActivityManager.descriptor);
                res = startInstrumentation(ComponentName.readFromParcel(data), data.readString(), data.readInt(), data.readBundle(), IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder()), IUiAutomationConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 45:
                data.enforceInterface(IActivityManager.descriptor);
                finishInstrumentation(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readInt(), data.readBundle());
                reply.writeNoException();
                return true;
            case 46:
                data.enforceInterface(IActivityManager.descriptor);
                config = getConfiguration();
                reply.writeNoException();
                config.writeToParcel(reply, 0);
                return true;
            case 47:
                data.enforceInterface(IActivityManager.descriptor);
                updateConfiguration((Configuration) Configuration.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case 48:
                data.enforceInterface(IActivityManager.descriptor);
                res = stopServiceToken(ComponentName.readFromParcel(data), data.readStrongBinder(), data.readInt());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 49:
                data.enforceInterface(IActivityManager.descriptor);
                cn = getActivityClassForToken(data.readStrongBinder());
                reply.writeNoException();
                ComponentName.writeToParcel(cn, reply);
                return true;
            case 50:
                data.enforceInterface(IActivityManager.descriptor);
                token = data.readStrongBinder();
                reply.writeNoException();
                reply.writeString(getPackageForToken(token));
                return true;
            case 51:
                data.enforceInterface(IActivityManager.descriptor);
                setProcessLimit(data.readInt());
                reply.writeNoException();
                return true;
            case 52:
                data.enforceInterface(IActivityManager.descriptor);
                int limit = getProcessLimit();
                reply.writeNoException();
                reply.writeInt(limit);
                return true;
            case 53:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = checkPermission(data.readString(), data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 54:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = checkUriPermission((Uri) Uri.CREATOR.createFromParcel(data), data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 55:
                data.enforceInterface(IActivityManager.descriptor);
                grantUriPermission(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), (Uri) Uri.CREATOR.createFromParcel(data), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 56:
                data.enforceInterface(IActivityManager.descriptor);
                revokeUriPermission(ApplicationThreadNative.asInterface(data.readStrongBinder()), (Uri) Uri.CREATOR.createFromParcel(data), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 57:
                data.enforceInterface(IActivityManager.descriptor);
                setActivityController(IActivityController.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case 58:
                data.enforceInterface(IActivityManager.descriptor);
                showWaitingForDebugger(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 59:
                data.enforceInterface(IActivityManager.descriptor);
                signalPersistentProcesses(data.readInt());
                reply.writeNoException();
                return true;
            case 60:
                data.enforceInterface(IActivityManager.descriptor);
                List<RecentTaskInfo> list2 = getRecentTasks(data.readInt(), data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeTypedList(list2);
                return true;
            case 61:
                data.enforceInterface(IActivityManager.descriptor);
                serviceDoneExecuting(data.readStrongBinder(), data.readInt(), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 62:
                data.enforceInterface(IActivityManager.descriptor);
                activityDestroyed(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 63:
                Intent[] requestIntents;
                String[] requestResolvedTypes;
                data.enforceInterface(IActivityManager.descriptor);
                int type = data.readInt();
                packageName = data.readString();
                token = data.readStrongBinder();
                String resultWho = data.readString();
                int requestCode = data.readInt();
                if (data.readInt() != 0) {
                    requestIntents = (Intent[]) data.createTypedArray(Intent.CREATOR);
                    requestResolvedTypes = data.createStringArray();
                } else {
                    requestIntents = null;
                    requestResolvedTypes = null;
                }
                res4 = getIntentSender(type, packageName, token, resultWho, requestCode, requestIntents, requestResolvedTypes, data.readInt(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeStrongBinder(res4 != null ? res4.asBinder() : null);
                return true;
            case 64:
                data.enforceInterface(IActivityManager.descriptor);
                cancelIntentSender(IIntentSender.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case 65:
                data.enforceInterface(IActivityManager.descriptor);
                res3 = getPackageForIntentSender(IIntentSender.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeString(res3);
                return true;
            case 66:
                data.enforceInterface(IActivityManager.descriptor);
                enterSafeMode();
                reply.writeNoException();
                return true;
            case 67:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = startNextMatchingActivity(data.readStrongBinder(), (Intent) Intent.CREATOR.createFromParcel(data), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case 68:
                data.enforceInterface(IActivityManager.descriptor);
                noteWakeupAlarm(IIntentSender.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString(), data.readString());
                reply.writeNoException();
                return true;
            case 69:
                data.enforceInterface(IActivityManager.descriptor);
                removeContentProvider(data.readStrongBinder(), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 70:
                data.enforceInterface(IActivityManager.descriptor);
                setRequestedOrientation(data.readStrongBinder(), data.readInt());
                reply.writeNoException();
                return true;
            case 71:
                data.enforceInterface(IActivityManager.descriptor);
                int req = getRequestedOrientation(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(req);
                return true;
            case 72:
                data.enforceInterface(IActivityManager.descriptor);
                unbindFinished(data.readStrongBinder(), (Intent) Intent.CREATOR.createFromParcel(data), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 73:
                data.enforceInterface(IActivityManager.descriptor);
                setProcessForeground(data.readStrongBinder(), data.readInt(), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 74:
                data.enforceInterface(IActivityManager.descriptor);
                ComponentName className = ComponentName.readFromParcel(data);
                token = data.readStrongBinder();
                int id = data.readInt();
                Notification notification = null;
                if (data.readInt() != 0) {
                    notification = (Notification) Notification.CREATOR.createFromParcel(data);
                }
                setServiceForeground(className, token, id, notification, data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 75:
                data.enforceInterface(IActivityManager.descriptor);
                res = moveActivityTaskToBack(data.readStrongBinder(), data.readInt() != 0);
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 76:
                data.enforceInterface(IActivityManager.descriptor);
                MemoryInfo mi = new MemoryInfo();
                getMemoryInfo(mi);
                reply.writeNoException();
                mi.writeToParcel(reply, 0);
                return true;
            case 77:
                data.enforceInterface(IActivityManager.descriptor);
                List<ProcessErrorStateInfo> list3 = getProcessesInErrorState();
                reply.writeNoException();
                reply.writeTypedList(list3);
                return true;
            case 78:
                data.enforceInterface(IActivityManager.descriptor);
                res = clearApplicationUserData(data.readString(), IPackageDataObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 79:
                data.enforceInterface(IActivityManager.descriptor);
                forceStopPackage(data.readString(), data.readInt());
                reply.writeNoException();
                return true;
            case 80:
                data.enforceInterface(IActivityManager.descriptor);
                res = killPids(data.createIntArray(), data.readString(), data.readInt() != 0);
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 81:
                data.enforceInterface(IActivityManager.descriptor);
                List<RunningServiceInfo> list4 = getServices(data.readInt(), data.readInt());
                reply.writeNoException();
                N = list4 != null ? list4.size() : -1;
                reply.writeInt(N);
                for (i = 0; i < N; i++) {
                    ((RunningServiceInfo) list4.get(i)).writeToParcel(reply, 0);
                }
                return true;
            case 82:
                data.enforceInterface(IActivityManager.descriptor);
                TaskThumbnail taskThumbnail = getTaskThumbnail(data.readInt());
                reply.writeNoException();
                if (taskThumbnail != null) {
                    reply.writeInt(1);
                    taskThumbnail.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 83:
                data.enforceInterface(IActivityManager.descriptor);
                List<RunningAppProcessInfo> list5 = getRunningAppProcesses();
                reply.writeNoException();
                reply.writeTypedList(list5);
                return true;
            case 84:
                data.enforceInterface(IActivityManager.descriptor);
                ConfigurationInfo config2 = getDeviceConfigurationInfo();
                reply.writeNoException();
                config2.writeToParcel(reply, 0);
                return true;
            case 85:
                data.enforceInterface(IActivityManager.descriptor);
                IBinder binder = peekService((Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readString());
                reply.writeNoException();
                reply.writeStrongBinder(binder);
                return true;
            case 86:
                data.enforceInterface(IActivityManager.descriptor);
                res = profileControl(data.readString(), data.readInt(), data.readInt() != 0, data.readInt() != 0 ? (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 87:
                data.enforceInterface(IActivityManager.descriptor);
                res = shutdown(data.readInt());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 88:
                data.enforceInterface(IActivityManager.descriptor);
                stopAppSwitches();
                reply.writeNoException();
                return true;
            case 89:
                data.enforceInterface(IActivityManager.descriptor);
                resumeAppSwitches();
                reply.writeNoException();
                return true;
            case 90:
                data.enforceInterface(IActivityManager.descriptor);
                success = bindBackupAgent(data.readString(), data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeInt(success ? 1 : 0);
                return true;
            case 91:
                data.enforceInterface(IActivityManager.descriptor);
                backupAgentCreated(data.readString(), data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 92:
                data.enforceInterface(IActivityManager.descriptor);
                unbindBackupAgent((ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case 93:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = getUidForIntentSender(IIntentSender.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 94:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = handleIncomingUser(data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt() != 0, data.readString(), data.readString());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 95:
                data.enforceInterface(IActivityManager.descriptor);
                addPackageDependency(data.readString());
                reply.writeNoException();
                return true;
            case 96:
                data.enforceInterface(IActivityManager.descriptor);
                killApplicationWithAppId(data.readString(), data.readInt(), data.readString());
                reply.writeNoException();
                return true;
            case 97:
                data.enforceInterface(IActivityManager.descriptor);
                closeSystemDialogs(data.readString());
                reply.writeNoException();
                return true;
            case 98:
                data.enforceInterface(IActivityManager.descriptor);
                Debug.MemoryInfo[] res5 = getProcessMemoryInfo(data.createIntArray());
                reply.writeNoException();
                reply.writeTypedArray(res5, 1);
                return true;
            case 99:
                data.enforceInterface(IActivityManager.descriptor);
                killApplicationProcess(data.readString(), data.readInt());
                reply.writeNoException();
                return true;
            case 100:
                data.enforceInterface(IActivityManager.descriptor);
                app = ApplicationThreadNative.asInterface(data.readStrongBinder());
                IntentSender intent2 = (IntentSender) IntentSender.CREATOR.createFromParcel(data);
                Intent fillInIntent = null;
                if (data.readInt() != 0) {
                    fillInIntent = (Intent) Intent.CREATOR.createFromParcel(data);
                }
                result = startActivityIntentSender(app, intent2, fillInIntent, data.readString(), data.readStrongBinder(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null);
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 101:
                data.enforceInterface(IActivityManager.descriptor);
                overridePendingTransition(data.readStrongBinder(), data.readString(), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 102:
                data.enforceInterface(IActivityManager.descriptor);
                res = handleApplicationWtf(data.readStrongBinder(), data.readString(), data.readInt() != 0, new CrashInfo(data));
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 103:
                data.enforceInterface(IActivityManager.descriptor);
                killBackgroundProcesses(data.readString(), data.readInt());
                reply.writeNoException();
                return true;
            case 104:
                data.enforceInterface(IActivityManager.descriptor);
                boolean areThey = isUserAMonkey();
                reply.writeNoException();
                reply.writeInt(areThey ? 1 : 0);
                return true;
            case 105:
                data.enforceInterface(IActivityManager.descriptor);
                WaitResult result3 = startActivityAndWait(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readStrongBinder(), data.readString(), data.readInt(), data.readInt(), data.readInt() != 0 ? (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                result3.writeToParcel(reply, 0);
                return true;
            case 106:
                data.enforceInterface(IActivityManager.descriptor);
                res = willActivityBeVisible(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 107:
                data.enforceInterface(IActivityManager.descriptor);
                result = startActivityWithConfig(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readStrongBinder(), data.readString(), data.readInt(), data.readInt(), (Configuration) Configuration.CREATOR.createFromParcel(data), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 108:
                data.enforceInterface(IActivityManager.descriptor);
                List<ApplicationInfo> list6 = getRunningExternalApplications();
                reply.writeNoException();
                reply.writeTypedList(list6);
                return true;
            case 109:
                data.enforceInterface(IActivityManager.descriptor);
                finishHeavyWeightApp();
                reply.writeNoException();
                return true;
            case 110:
                data.enforceInterface(IActivityManager.descriptor);
                handleApplicationStrictModeViolation(data.readStrongBinder(), data.readInt(), new ViolationInfo(data));
                reply.writeNoException();
                return true;
            case 111:
                data.enforceInterface(IActivityManager.descriptor);
                isit = isImmersive(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(isit ? 1 : 0);
                return true;
            case 112:
                data.enforceInterface(IActivityManager.descriptor);
                setImmersive(data.readStrongBinder(), data.readInt() == 1);
                reply.writeNoException();
                return true;
            case 113:
                data.enforceInterface(IActivityManager.descriptor);
                isit = isTopActivityImmersive();
                reply.writeNoException();
                reply.writeInt(isit ? 1 : 0);
                return true;
            case 114:
                data.enforceInterface(IActivityManager.descriptor);
                crashApplication(data.readInt(), data.readInt(), data.readString(), data.readString());
                reply.writeNoException();
                return true;
            case 115:
                data.enforceInterface(IActivityManager.descriptor);
                String type2 = getProviderMimeType((Uri) Uri.CREATOR.createFromParcel(data), data.readInt());
                reply.writeNoException();
                reply.writeString(type2);
                return true;
            case 116:
                data.enforceInterface(IActivityManager.descriptor);
                IBinder perm = newUriPermissionOwner(data.readString());
                reply.writeNoException();
                reply.writeStrongBinder(perm);
                return true;
            case 117:
                data.enforceInterface(IActivityManager.descriptor);
                grantUriPermissionFromOwner(data.readStrongBinder(), data.readInt(), data.readString(), (Uri) Uri.CREATOR.createFromParcel(data), data.readInt(), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 118:
                data.enforceInterface(IActivityManager.descriptor);
                IBinder owner = data.readStrongBinder();
                Uri uri = null;
                if (data.readInt() != 0) {
                    uri = (Uri) Uri.CREATOR.createFromParcel(data);
                }
                revokeUriPermissionFromOwner(owner, uri, data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 119:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = checkGrantUriPermission(data.readInt(), data.readString(), (Uri) Uri.CREATOR.createFromParcel(data), data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 120:
                data.enforceInterface(IActivityManager.descriptor);
                res = dumpHeap(data.readString(), data.readInt(), data.readInt() != 0, data.readString(), data.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data) : null);
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 121:
                data.enforceInterface(IActivityManager.descriptor);
                result = startActivities(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), (Intent[]) data.createTypedArray(Intent.CREATOR), data.createStringArray(), data.readStrongBinder(), data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 122:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = isUserRunning(data.readInt(), data.readInt() != 0);
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case 123:
                data.enforceInterface(IActivityManager.descriptor);
                activitySlept(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 124:
                data.enforceInterface(IActivityManager.descriptor);
                mode = getFrontActivityScreenCompatMode();
                reply.writeNoException();
                reply.writeInt(mode);
                return true;
            case 125:
                data.enforceInterface(IActivityManager.descriptor);
                mode = data.readInt();
                setFrontActivityScreenCompatMode(mode);
                reply.writeNoException();
                reply.writeInt(mode);
                return true;
            case 126:
                data.enforceInterface(IActivityManager.descriptor);
                mode = getPackageScreenCompatMode(data.readString());
                reply.writeNoException();
                reply.writeInt(mode);
                return true;
            case 127:
                data.enforceInterface(IActivityManager.descriptor);
                setPackageScreenCompatMode(data.readString(), data.readInt());
                reply.writeNoException();
                return true;
            case 128:
                data.enforceInterface(IActivityManager.descriptor);
                ask = getPackageAskScreenCompat(data.readString());
                reply.writeNoException();
                reply.writeInt(ask ? 1 : 0);
                return true;
            case 129:
                data.enforceInterface(IActivityManager.descriptor);
                setPackageAskScreenCompat(data.readString(), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 130:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = switchUser(data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case 132:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = removeTask(data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case 133:
                data.enforceInterface(IActivityManager.descriptor);
                registerProcessObserver(IProcessObserver.Stub.asInterface(data.readStrongBinder()));
                return true;
            case 134:
                data.enforceInterface(IActivityManager.descriptor);
                unregisterProcessObserver(IProcessObserver.Stub.asInterface(data.readStrongBinder()));
                return true;
            case 135:
                data.enforceInterface(IActivityManager.descriptor);
                res = isIntentSenderTargetedToPackage(IIntentSender.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 136:
                data.enforceInterface(IActivityManager.descriptor);
                updatePersistentConfiguration((Configuration) Configuration.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case 137:
                data.enforceInterface(IActivityManager.descriptor);
                long[] pss = getProcessPss(data.createIntArray());
                reply.writeNoException();
                reply.writeLongArray(pss);
                return true;
            case 138:
                data.enforceInterface(IActivityManager.descriptor);
                showBootMessage((CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 140:
                data.enforceInterface(IActivityManager.descriptor);
                killAllBackgroundProcesses();
                reply.writeNoException();
                return true;
            case 141:
                data.enforceInterface(IActivityManager.descriptor);
                cph = getContentProviderExternal(data.readString(), data.readInt(), data.readStrongBinder());
                reply.writeNoException();
                if (cph != null) {
                    reply.writeInt(1);
                    cph.writeToParcel(reply, 0);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 142:
                data.enforceInterface(IActivityManager.descriptor);
                removeContentProviderExternal(data.readString(), data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 143:
                data.enforceInterface(IActivityManager.descriptor);
                RunningAppProcessInfo info = new RunningAppProcessInfo();
                getMyMemoryState(info);
                reply.writeNoException();
                info.writeToParcel(reply, 0);
                return true;
            case 144:
                data.enforceInterface(IActivityManager.descriptor);
                res = killProcessesBelowForeground(data.readString());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 145:
                data.enforceInterface(IActivityManager.descriptor);
                UserInfo userInfo = getCurrentUser();
                reply.writeNoException();
                userInfo.writeToParcel(reply, 0);
                return true;
            case 146:
                data.enforceInterface(IActivityManager.descriptor);
                res = shouldUpRecreateTask(data.readStrongBinder(), data.readString());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 147:
                data.enforceInterface(IActivityManager.descriptor);
                token = data.readStrongBinder();
                Intent target = (Intent) Intent.CREATOR.createFromParcel(data);
                resultCode = data.readInt();
                resultData = null;
                if (data.readInt() != 0) {
                    resultData = (Intent) Intent.CREATOR.createFromParcel(data);
                }
                res = navigateUpTo(token, target, resultCode, resultData);
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 148:
                data.enforceInterface(IActivityManager.descriptor);
                setLockScreenShown(data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 149:
                data.enforceInterface(IActivityManager.descriptor);
                res = finishActivityAffinity(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 150:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = getLaunchedFromUid(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 151:
                data.enforceInterface(IActivityManager.descriptor);
                unstableProviderDied(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 152:
                data.enforceInterface(IActivityManager.descriptor);
                res = isIntentSenderAnActivity(IIntentSender.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 153:
                data.enforceInterface(IActivityManager.descriptor);
                result = startActivityAsUser(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readStrongBinder(), data.readString(), data.readInt(), data.readInt(), data.readInt() != 0 ? (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 154:
                data.enforceInterface(IActivityManager.descriptor);
                result = stopUser(data.readInt(), IStopUserCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 155:
                data.enforceInterface(IActivityManager.descriptor);
                registerUserSwitchObserver(IUserSwitchObserver.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case 156:
                data.enforceInterface(IActivityManager.descriptor);
                unregisterUserSwitchObserver(IUserSwitchObserver.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case 157:
                data.enforceInterface(IActivityManager.descriptor);
                int[] result4 = getRunningUserIds();
                reply.writeNoException();
                reply.writeIntArray(result4);
                return true;
            case 158:
                data.enforceInterface(IActivityManager.descriptor);
                requestBugReport();
                reply.writeNoException();
                return true;
            case 159:
                data.enforceInterface(IActivityManager.descriptor);
                long res6 = inputDispatchingTimedOut(data.readInt(), data.readInt() != 0, data.readString());
                reply.writeNoException();
                reply.writeLong(res6);
                return true;
            case 161:
                data.enforceInterface(IActivityManager.descriptor);
                intent = getIntentForIntentSender(IIntentSender.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                if (intent != null) {
                    reply.writeInt(1);
                    intent.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 162:
                data.enforceInterface(IActivityManager.descriptor);
                Bundle res7 = getAssistContextExtras(data.readInt());
                reply.writeNoException();
                reply.writeBundle(res7);
                return true;
            case 163:
                data.enforceInterface(IActivityManager.descriptor);
                reportAssistContextExtras(data.readStrongBinder(), data.readBundle(), (AssistStructure) AssistStructure.CREATOR.createFromParcel(data), (AssistContent) AssistContent.CREATOR.createFromParcel(data), data.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(data) : null);
                reply.writeNoException();
                return true;
            case 164:
                data.enforceInterface(IActivityManager.descriptor);
                res3 = getLaunchedFromPackage(data.readStrongBinder());
                reply.writeNoException();
                reply.writeString(res3);
                return true;
            case 165:
                data.enforceInterface(IActivityManager.descriptor);
                killUid(data.readInt(), data.readInt(), data.readString());
                reply.writeNoException();
                return true;
            case 166:
                data.enforceInterface(IActivityManager.descriptor);
                setUserIsMonkey(data.readInt() == 1);
                reply.writeNoException();
                return true;
            case 167:
                data.enforceInterface(IActivityManager.descriptor);
                hang(data.readStrongBinder(), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 168:
                data.enforceInterface(IActivityManager.descriptor);
                activityContainer = createVirtualActivityContainer(data.readStrongBinder(), IActivityContainerCallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                if (activityContainer != null) {
                    reply.writeInt(1);
                    reply.writeStrongBinder(activityContainer.asBinder());
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 169:
                data.enforceInterface(IActivityManager.descriptor);
                moveTaskToStack(data.readInt(), data.readInt(), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 170:
                data.enforceInterface(IActivityManager.descriptor);
                resizeStack(data.readInt(), (Rect) Rect.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case 171:
                data.enforceInterface(IActivityManager.descriptor);
                List<StackInfo> list7 = getAllStackInfos();
                reply.writeNoException();
                reply.writeTypedList(list7);
                return true;
            case 172:
                data.enforceInterface(IActivityManager.descriptor);
                setFocusedStack(data.readInt());
                reply.writeNoException();
                return true;
            case 173:
                data.enforceInterface(IActivityManager.descriptor);
                StackInfo info2 = getStackInfo(data.readInt());
                reply.writeNoException();
                if (info2 != null) {
                    reply.writeInt(1);
                    info2.writeToParcel(reply, 0);
                } else {
                    reply.writeInt(0);
                }
                return true;
            case 174:
                data.enforceInterface(IActivityManager.descriptor);
                converted = convertFromTranslucent(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(converted ? 1 : 0);
                return true;
            case 175:
                data.enforceInterface(IActivityManager.descriptor);
                token = data.readStrongBinder();
                if (data.readInt() == 0) {
                    bundle = null;
                } else {
                    bundle = data.readBundle();
                }
                if (bundle == null) {
                    options = null;
                } else {
                    activityOptions = new ActivityOptions(bundle);
                }
                converted = convertToTranslucent(token, options);
                reply.writeNoException();
                reply.writeInt(converted ? 1 : 0);
                return true;
            case 176:
                data.enforceInterface(IActivityManager.descriptor);
                notifyActivityDrawn(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 177:
                data.enforceInterface(IActivityManager.descriptor);
                reportActivityFullyDrawn(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 178:
                data.enforceInterface(IActivityManager.descriptor);
                restart();
                reply.writeNoException();
                return true;
            case 179:
                data.enforceInterface(IActivityManager.descriptor);
                performIdleMaintenance();
                reply.writeNoException();
                return true;
            case 180:
                data.enforceInterface(IActivityManager.descriptor);
                takePersistableUriPermission((Uri) Uri.CREATOR.createFromParcel(data), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 181:
                data.enforceInterface(IActivityManager.descriptor);
                releasePersistableUriPermission((Uri) Uri.CREATOR.createFromParcel(data), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 182:
                data.enforceInterface(IActivityManager.descriptor);
                ParceledListSlice<UriPermission> perms = getPersistedUriPermissions(data.readString(), data.readInt() != 0);
                reply.writeNoException();
                perms.writeToParcel(reply, 1);
                return true;
            case 183:
                data.enforceInterface(IActivityManager.descriptor);
                appNotRespondingViaProvider(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 185:
                data.enforceInterface(IActivityManager.descriptor);
                int displayId = getActivityDisplayId(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(displayId);
                return true;
            case 186:
                data.enforceInterface(IActivityManager.descriptor);
                deleteActivityContainer(IActivityContainer.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case 187:
                data.enforceInterface(IActivityManager.descriptor);
                res = setProcessMemoryTrimLevel(data.readString(), data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 188:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = clearRecentTasks(data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case 189:
                data.enforceInterface(IActivityManager.descriptor);
                updateKnoxContainerRuntimeState(data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            case 190:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = getKidForIntentSender(IIntentSender.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 211:
                data.enforceInterface(IActivityManager.descriptor);
                String tag = getTagForIntentSender(IIntentSender.Stub.asInterface(data.readStrongBinder()), data.readString());
                reply.writeNoException();
                reply.writeString(tag);
                return true;
            case 212:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = startUserInBackground(data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case 213:
                data.enforceInterface(IActivityManager.descriptor);
                boolean isInHomeStack = isInHomeStack(data.readInt());
                reply.writeNoException();
                reply.writeInt(isInHomeStack ? 1 : 0);
                return true;
            case 214:
                data.enforceInterface(IActivityManager.descriptor);
                startLockTaskMode(data.readInt());
                reply.writeNoException();
                return true;
            case 215:
                data.enforceInterface(IActivityManager.descriptor);
                startLockTaskMode(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 216:
                data.enforceInterface(IActivityManager.descriptor);
                stopLockTaskMode();
                reply.writeNoException();
                return true;
            case 217:
                data.enforceInterface(IActivityManager.descriptor);
                boolean isInLockTaskMode = isInLockTaskMode();
                reply.writeNoException();
                reply.writeInt(isInLockTaskMode ? 1 : 0);
                return true;
            case 218:
                data.enforceInterface(IActivityManager.descriptor);
                setTaskDescription(data.readStrongBinder(), (TaskDescription) TaskDescription.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case 219:
                data.enforceInterface(IActivityManager.descriptor);
                result = startVoiceActivity(data.readString(), data.readInt(), data.readInt(), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder()), IVoiceInteractor.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt() != 0 ? (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 220:
                data.enforceInterface(IActivityManager.descriptor);
                options = getActivityOptions(data.readStrongBinder());
                reply.writeNoException();
                reply.writeBundle(options == null ? null : options.toBundle());
                return true;
            case 221:
                data.enforceInterface(IActivityManager.descriptor);
                List<IAppTask> list8 = getAppTasks(data.readString());
                reply.writeNoException();
                N = list8 != null ? list8.size() : -1;
                reply.writeInt(N);
                for (i = 0; i < N; i++) {
                    reply.writeStrongBinder(((IAppTask) list8.get(i)).asBinder());
                }
                return true;
            case 222:
                data.enforceInterface(IActivityManager.descriptor);
                startLockTaskModeOnCurrent();
                reply.writeNoException();
                return true;
            case 223:
                data.enforceInterface(IActivityManager.descriptor);
                stopLockTaskModeOnCurrent();
                reply.writeNoException();
                return true;
            case 224:
                data.enforceInterface(IActivityManager.descriptor);
                finishVoiceTask(IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case 225:
                data.enforceInterface(IActivityManager.descriptor);
                boolean isTopOfTask = isTopOfTask(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(isTopOfTask ? 1 : 0);
                return true;
            case 226:
                data.enforceInterface(IActivityManager.descriptor);
                success = requestVisibleBehind(data.readStrongBinder(), data.readInt() > 0);
                reply.writeNoException();
                reply.writeInt(success ? 1 : 0);
                return true;
            case 227:
                data.enforceInterface(IActivityManager.descriptor);
                boolean enabled = isBackgroundVisibleBehind(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(enabled ? 1 : 0);
                return true;
            case 228:
                data.enforceInterface(IActivityManager.descriptor);
                backgroundResourcesReleased(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 229:
                data.enforceInterface(IActivityManager.descriptor);
                notifyLaunchTaskBehindComplete(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 230:
                data.enforceInterface(IActivityManager.descriptor);
                result = startActivityFromRecents(data.readInt(), data.readInt() == 0 ? null : (Bundle) Bundle.CREATOR.createFromParcel(data));
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 231:
                data.enforceInterface(IActivityManager.descriptor);
                notifyEnterAnimationComplete(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 232:
                data.enforceInterface(IActivityManager.descriptor);
                keyguardWaitingForActivityDrawn();
                reply.writeNoException();
                return true;
            case 233:
                data.enforceInterface(IActivityManager.descriptor);
                result = startActivityAsCaller(ApplicationThreadNative.asInterface(data.readStrongBinder()), data.readString(), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readStrongBinder(), data.readString(), data.readInt(), data.readInt(), data.readInt() != 0 ? (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(data) : null, data.readInt() != 0, data.readInt());
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            case 234:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = addAppTask(data.readStrongBinder(), (Intent) Intent.CREATOR.createFromParcel(data), (TaskDescription) TaskDescription.CREATOR.createFromParcel(data), (Bitmap) Bitmap.CREATOR.createFromParcel(data));
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 235:
                data.enforceInterface(IActivityManager.descriptor);
                Point size = getAppTaskThumbnailSize();
                reply.writeNoException();
                size.writeToParcel(reply, 0);
                return true;
            case 236:
                data.enforceInterface(IActivityManager.descriptor);
                res = releaseActivityInstance(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 237:
                data.enforceInterface(IActivityManager.descriptor);
                releaseSomeActivities(ApplicationThreadNative.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case 238:
                data.enforceInterface(IActivityManager.descriptor);
                bootAnimationComplete();
                reply.writeNoException();
                return true;
            case 239:
                data.enforceInterface(IActivityManager.descriptor);
                Bitmap icon = getTaskDescriptionIcon(data.readString());
                reply.writeNoException();
                if (icon == null) {
                    reply.writeInt(0);
                } else {
                    reply.writeInt(1);
                    icon.writeToParcel(reply, 0);
                }
                return true;
            case 240:
                data.enforceInterface(IActivityManager.descriptor);
                res = launchAssistIntent((Intent) Intent.CREATOR.createFromParcel(data), data.readInt(), data.readString(), data.readInt(), data.readBundle());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 241:
                data.enforceInterface(IActivityManager.descriptor);
                if (data.readInt() == 0) {
                    bundle = null;
                } else {
                    bundle = data.readBundle();
                }
                if (bundle == null) {
                    options = null;
                } else {
                    activityOptions = new ActivityOptions(bundle);
                }
                startInPlaceAnimationOnFrontMostApplication(options);
                reply.writeNoException();
                return true;
            case 242:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = checkPermissionWithToken(data.readString(), data.readInt(), data.readInt(), data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case 243:
                data.enforceInterface(IActivityManager.descriptor);
                registerTaskStackListener(ITaskStackListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            case IActivityManager.NOTIFY_CLEARTEXT_NETWORK_TRANSACTION /*281*/:
                data.enforceInterface(IActivityManager.descriptor);
                notifyCleartextNetwork(data.readInt(), data.createByteArray());
                reply.writeNoException();
                return true;
            case IActivityManager.CREATE_STACK_ON_DISPLAY /*282*/:
                data.enforceInterface(IActivityManager.descriptor);
                activityContainer = createStackOnDisplay(data.readInt());
                reply.writeNoException();
                if (activityContainer != null) {
                    reply.writeInt(1);
                    reply.writeStrongBinder(activityContainer.asBinder());
                } else {
                    reply.writeInt(0);
                }
                return true;
            case IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION /*283*/:
                data.enforceInterface(IActivityManager.descriptor);
                int focusedStackId = getFocusedStackId();
                reply.writeNoException();
                reply.writeInt(focusedStackId);
                return true;
            case 284:
                data.enforceInterface(IActivityManager.descriptor);
                setTaskResizeable(data.readInt(), data.readInt() == 1);
                reply.writeNoException();
                return true;
            case IActivityManager.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION /*285*/:
                data.enforceInterface(IActivityManager.descriptor);
                res = requestAssistContextExtras(data.readInt(), IResultReceiver.Stub.asInterface(data.readStrongBinder()), data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case IActivityManager.RESIZE_TASK_TRANSACTION /*286*/:
                data.enforceInterface(IActivityManager.descriptor);
                resizeTask(data.readInt(), (Rect) Rect.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case IActivityManager.GET_LOCK_TASK_MODE_STATE_TRANSACTION /*287*/:
                data.enforceInterface(IActivityManager.descriptor);
                int lockTaskModeState = getLockTaskModeState();
                reply.writeNoException();
                reply.writeInt(lockTaskModeState);
                return true;
            case IActivityManager.SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION /*288*/:
                data.enforceInterface(IActivityManager.descriptor);
                setDumpHeapDebugLimit(data.readString(), data.readInt(), data.readLong(), data.readString());
                reply.writeNoException();
                return true;
            case IActivityManager.DUMP_HEAP_FINISHED_TRANSACTION /*289*/:
                data.enforceInterface(IActivityManager.descriptor);
                dumpHeapFinished(data.readString());
                reply.writeNoException();
                return true;
            case IActivityManager.SET_VOICE_KEEP_AWAKE_TRANSACTION /*290*/:
                data.enforceInterface(IActivityManager.descriptor);
                setVoiceKeepAwake(IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case IActivityManager.UPDATE_LOCK_TASK_PACKAGES_TRANSACTION /*291*/:
                data.enforceInterface(IActivityManager.descriptor);
                updateLockTaskPackages(data.readInt(), data.readStringArray());
                reply.writeNoException();
                return true;
            case IActivityManager.NOTE_ALARM_START_TRANSACTION /*292*/:
                data.enforceInterface(IActivityManager.descriptor);
                noteAlarmStart(IIntentSender.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString());
                reply.writeNoException();
                return true;
            case IActivityManager.NOTE_ALARM_FINISH_TRANSACTION /*293*/:
                data.enforceInterface(IActivityManager.descriptor);
                noteAlarmFinish(IIntentSender.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString());
                reply.writeNoException();
                return true;
            case IActivityManager.GET_PACKAGE_PROCESS_STATE_TRANSACTION /*294*/:
                data.enforceInterface(IActivityManager.descriptor);
                res2 = getPackageProcessState(data.readString(), data.readString());
                reply.writeNoException();
                reply.writeInt(res2);
                return true;
            case IActivityManager.SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION /*295*/:
                data.enforceInterface(IActivityManager.descriptor);
                showLockTaskEscapeMessage(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION /*296*/:
                data.enforceInterface(IActivityManager.descriptor);
                updateDeviceOwner(data.readString());
                reply.writeNoException();
                return true;
            case IActivityManager.KEYGUARD_GOING_AWAY_TRANSACTION /*297*/:
                data.enforceInterface(IActivityManager.descriptor);
                keyguardGoingAway(data.readInt() != 0, data.readInt() != 0);
                reply.writeNoException();
                return true;
            case IActivityManager.REGISTER_UID_OBSERVER_TRANSACTION /*298*/:
                data.enforceInterface(IActivityManager.descriptor);
                registerUidObserver(IUidObserver.Stub.asInterface(data.readStrongBinder()));
                return true;
            case IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION /*299*/:
                data.enforceInterface(IActivityManager.descriptor);
                unregisterUidObserver(IUidObserver.Stub.asInterface(data.readStrongBinder()));
                return true;
            case 300:
                data.enforceInterface(IActivityManager.descriptor);
                res = isAssistDataAllowedOnCurrentActivity();
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 301:
                data.enforceInterface(IActivityManager.descriptor);
                res = showAssistFromActivity(data.readStrongBinder(), data.readBundle());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 302:
                data.enforceInterface(IActivityManager.descriptor);
                res = isRootVoiceInteraction(data.readStrongBinder());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case 1011:
                data.enforceInterface(IActivityManager.descriptor);
                multiWindowSettingChanged(data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 1012:
                data.enforceInterface(IActivityManager.descriptor);
                setFocusedStack(data.readInt(), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case 1013:
                data.enforceInterface(IActivityManager.descriptor);
                notifyMinimizeMultiWindow(data.readStrongBinder());
                reply.writeNoException();
                return true;
            case 1014:
                data.enforceInterface(IActivityManager.descriptor);
                notifyDisplayFreezeStopped();
                reply.writeNoException();
                return true;
            case 1015:
                data.enforceInterface(IActivityManager.descriptor);
                notifyCascadeStackRotated(data.readInt(), (Rect) Rect.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case IActivityManager.KEYGUARD_WAITING_FOR_ACTIVITY_DRAWN_TARGET_TRANSACTION /*1102*/:
                data.enforceInterface(IActivityManager.descriptor);
                keyguardWaitingForActivityDrawnTarget((Intent) Intent.CREATOR.createFromParcel(data));
                reply.writeNoException();
                return true;
            case IActivityManager.CONVERT_FROM_TRANSLUCENT_SKIPWINDOWOPAQUE_TRANSACTION /*1103*/:
                data.enforceInterface(IActivityManager.descriptor);
                converted = convertFromTranslucent(data.readStrongBinder(), data.readInt() == 1);
                reply.writeNoException();
                reply.writeInt(converted ? 1 : 0);
                return true;
            case IActivityManager.REMOVE_TASK_EXT_TRANSACTION /*1104*/:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = removeTask(data.readInt(), data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case IActivityManager.FORCE_STOP_PACKAGE_BYADMIN_TRANSACTION /*1201*/:
                data.enforceInterface(IActivityManager.descriptor);
                forceStopPackageByAdmin(data.readString(), data.readInt());
                reply.writeNoException();
                return true;
            case IActivityManager.UNFREEZE_APP /*1202*/:
                data.enforceInterface(IActivityManager.descriptor);
                unFreezeApp(data.readString(), data.readInt());
                reply.writeNoException();
                return true;
            case IActivityManager.GET_SMPKGS_LIST /*1203*/:
                data.enforceInterface(IActivityManager.descriptor);
                List smPackagesList = new ArrayList();
                data.readStringList(smPackagesList);
                getSMpkgsList(smPackagesList);
                reply.writeNoException();
                return true;
            case IActivityManager.AUTORUN_BLOCKED_APP /*1204*/:
                data.enforceInterface(IActivityManager.descriptor);
                ask = isAutoRunBlockedApp(data.readString());
                reply.writeNoException();
                reply.writeInt(ask ? 1 : 0);
                return true;
            case 1501:
                data.enforceInterface(IActivityManager.descriptor);
                res = checkKnoxPermission(data.readString(), data.readInt(), data.readString(), data.readInt(), data.readString());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case IActivityManager.CHECK_KNOX_PERMISSION_BY_PID_TRANSACTION /*1502*/:
                data.enforceInterface(IActivityManager.descriptor);
                res = checkKnoxPermission(data.readInt(), data.readInt(), data.readString(), data.readInt(), data.readString());
                reply.writeNoException();
                reply.writeInt(res ? 1 : 0);
                return true;
            case IActivityManager.SET_PROCESS_FOREGROUND_EX_TRANSACTION /*3101*/:
                data.enforceInterface(IActivityManager.descriptor);
                setProcessForeground(data.readStrongBinder(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                reply.writeNoException();
                return true;
            case IActivityManager.GET_PACKAGE_FROM_APP_PROCESSES_TRANSACTION /*3102*/:
                data.enforceInterface(IActivityManager.descriptor);
                int pid = data.readInt();
                reply.writeNoException();
                reply.writeString(getPackageFromAppProcesses(pid));
                return true;
            case IActivityManager.REMOVE_DELETED_PACKAGE_TRANSACTION /*4051*/:
                data.enforceInterface(IActivityManager.descriptor);
                removeDeletedPkgsFromLru(data.readString());
                reply.writeNoException();
                return true;
            case IActivityManager.SET_SNN_ENABLE /*4052*/:
                data.enforceInterface(IActivityManager.descriptor);
                boolean snnEnable = false;
                if (data.readInt() == 1) {
                    snnEnable = true;
                }
                setSNNEnable(snnEnable);
                reply.writeNoException();
                return true;
            case IActivityManager.IS_SNN_ENABLE /*4053*/:
                data.enforceInterface(IActivityManager.descriptor);
                int isSNNEnable = isSNNEnable();
                reply.writeNoException();
                reply.writeInt(isSNNEnable);
                return true;
            case IActivityManager.GET_DUMP_MEMORYINFO /*4061*/:
                data.enforceInterface(IActivityManager.descriptor);
                ArrayList<MemDumpInfo> MemDumpList = getDumpMemoryInfo();
                reply.writeNoException();
                reply.writeList(MemDumpList);
                return true;
            case 5001:
                data.enforceInterface(IActivityManager.descriptor);
                config = getConfiguration(data.readInt());
                reply.writeNoException();
                config.writeToParcel(reply, 0);
                return true;
            case 5002:
                data.enforceInterface(IActivityManager.descriptor);
                cn = startService(ApplicationThreadNative.asInterface(data.readStrongBinder()), (Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readString(), data.readInt(), data.readStrongBinder(), data.readInt());
                reply.writeNoException();
                ComponentName.writeToParcel(cn, reply);
                return true;
            case 5003:
                data.enforceInterface(IActivityManager.descriptor);
                list = getTasks(data.readInt(), data.readInt(), data.readInt());
                reply.writeNoException();
                N = list != null ? list.size() : -1;
                reply.writeInt(N);
                for (i = 0; i < N; i++) {
                    ((RunningTaskInfo) list.get(i)).writeToParcel(reply, 0);
                }
                return true;
            case 5004:
                data.enforceInterface(IActivityManager.descriptor);
                updateProcessConfiguration(data.readInt(), data.readInt(), data.readInt() == 1);
                reply.writeNoException();
                return true;
            case 5005:
                data.enforceInterface(IActivityManager.descriptor);
                setBackWindowShown(data.readInt() == 1);
                reply.writeNoException();
                return true;
            case 6001:
                data.enforceInterface(IActivityManager.descriptor);
                IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                packageName = data.readString();
                long[] t = data.createLongArray();
                res4 = grabIntentSender(r, packageName, t);
                reply.writeNoException();
                reply.writeStrongBinder(res4 != null ? res4.asBinder() : null);
                reply.writeLongArray(t);
                return true;
            case 6002:
                data.enforceInterface(IActivityManager.descriptor);
                Map res8 = getGrabedIntentSenders();
                reply.writeNoException();
                reply.writeMap(res8);
                return true;
            case IActivityManager.ENABLE_SAFE_MODE_TRANSACTION /*6021*/:
                data.enforceInterface(IActivityManager.descriptor);
                enableSafeMode();
                reply.writeNoException();
                return true;
            case IActivityManager.QUERY_REGISTERED_RECEIVER_PACKAGES_TRANSACTION /*6031*/:
                data.enforceInterface(IActivityManager.descriptor);
                String[] res9 = queryRegisteredReceiverPackages((Intent) Intent.CREATOR.createFromParcel(data), data.readString(), data.readInt());
                reply.writeNoException();
                reply.writeStringArray(res9);
                return true;
            case IActivityManager.SET_CUSTOM_IMAGE_TRANSACTION /*6041*/:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = setCustomImage(data.readStrongBinder(), data.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case IActivityManager.SET_CUSTOM_IMAGE_FOR_PACKAGE_TRANSACTION /*6042*/:
                data.enforceInterface(IActivityManager.descriptor);
                result2 = setCustomImageForPackage(ComponentName.readFromParcel(data), data.readInt(), data.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data) : null, data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case IActivityManager.GET_APPLOCKED_PACKAGE_LIST_TRANSACTION /*7001*/:
                data.enforceInterface(IActivityManager.descriptor);
                ArrayList<String> packagelist = getAppLockedPackageList();
                reply.writeNoException();
                reply.writeList(packagelist);
                return true;
            case IActivityManager.SET_APPLOCKED_UNLOCK_TRANSACTION /*7002*/:
                data.enforceInterface(IActivityManager.descriptor);
                setAppLockedUnLockPackage(data.readString());
                reply.writeNoException();
                return true;
            case IActivityManager.IS_APPLOCKED_PACKAGE_TRANSACTION /*7003*/:
                data.enforceInterface(IActivityManager.descriptor);
                ask = isAppLockedPackage(data.readString());
                reply.writeNoException();
                reply.writeInt(ask ? 1 : 0);
                return true;
            case IActivityManager.CLEAR_APPLOCKED_UNLOCKED_APP_TRANSACTION /*7004*/:
                data.enforceInterface(IActivityManager.descriptor);
                clearAppLockedUnLockedApp();
                reply.writeNoException();
                return true;
            case IActivityManager.GET_APPLOCKED_LOCK_TYPE_TRANSACTION /*7005*/:
                data.enforceInterface(IActivityManager.descriptor);
                String lockType = getAppLockedLockType();
                reply.writeNoException();
                reply.writeString(lockType);
                return true;
            case IActivityManager.GET_APPLOCKED_CHECK_ACTION_TRANSACTION /*7006*/:
                data.enforceInterface(IActivityManager.descriptor);
                String check = getAppLockedCheckAction();
                reply.writeNoException();
                reply.writeString(check);
                return true;
            case IActivityManager.SET_APPLOCKED_VERIFYING_TRANSACTION /*7007*/:
                data.enforceInterface(IActivityManager.descriptor);
                setAppLockedVerifying(data.readString(), data.readInt() != 0);
                reply.writeNoException();
                return true;
            case IActivityManager.IS_APPLOCKED_VERIFYING_TRANSACTION /*7008*/:
                data.enforceInterface(IActivityManager.descriptor);
                ask = isAppLockedVerifying(data.readString());
                reply.writeNoException();
                reply.writeInt(ask ? 1 : 0);
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }

    public IBinder asBinder() {
        return this;
    }
}
