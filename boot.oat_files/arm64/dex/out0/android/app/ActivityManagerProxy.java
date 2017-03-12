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
import android.app.IAppTask.Stub;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
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
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;
import android.os.SystemClock;
import android.service.voice.IVoiceInteractionSession;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.MemDumpInfo;
import com.android.internal.os.IResultReceiver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: ActivityManagerNative */
class ActivityManagerProxy implements IActivityManager {
    static final String TAG_TIMELINE = "Timeline";
    private IBinder mRemote;

    public ActivityManagerProxy(IBinder remote) {
        this.mRemote = remote;
    }

    public IBinder asBinder() {
        return this.mRemote;
    }

    public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        if (intent.getComponent() != null) {
            Log.i(TAG_TIMELINE, "Timeline: Activity_launch_request id:" + intent.getComponent().getPackageName() + " time:" + SystemClock.uptimeMillis());
        }
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeString(callingPackage);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(resultTo);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            data.writeInt(startFlags);
            if (profilerInfo != null) {
                data.writeInt(1);
                profilerInfo.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(3, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeString(callingPackage);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(resultTo);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            data.writeInt(startFlags);
            if (profilerInfo != null) {
                data.writeInt(1);
                profilerInfo.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(userId);
            this.mRemote.transact(153, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, ProfilerInfo profilerInfo, Bundle options, boolean ignoreTargetSecurity, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeString(callingPackage);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(resultTo);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            data.writeInt(startFlags);
            if (profilerInfo != null) {
                data.writeInt(1);
                profilerInfo.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(ignoreTargetSecurity ? 1 : 0);
            data.writeInt(userId);
            this.mRemote.transact(233, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeString(callingPackage);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(resultTo);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            data.writeInt(startFlags);
            if (profilerInfo != null) {
                data.writeInt(1);
                profilerInfo.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(userId);
            this.mRemote.transact(105, data, reply, 0);
            reply.readException();
            WaitResult result = (WaitResult) WaitResult.CREATOR.createFromParcel(reply);
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public int startActivityWithConfig(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration config, Bundle options, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeString(callingPackage);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(resultTo);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            data.writeInt(startFlags);
            config.writeToParcel(data, 0);
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(userId);
            this.mRemote.transact(3, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public int startActivityIntentSender(IApplicationThread caller, IntentSender intent, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            intent.writeToParcel(data, 0);
            if (fillInIntent != null) {
                data.writeInt(1);
                fillInIntent.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeString(resolvedType);
            data.writeStrongBinder(resultTo);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            data.writeInt(flagsMask);
            data.writeInt(flagsValues);
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(100, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public int startVoiceActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int startFlags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(callingPackage);
            data.writeInt(callingPid);
            data.writeInt(callingUid);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(session.asBinder());
            data.writeStrongBinder(interactor.asBinder());
            data.writeInt(startFlags);
            if (profilerInfo != null) {
                data.writeInt(1);
                profilerInfo.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(userId);
            this.mRemote.transact(219, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
        boolean z = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(callingActivity);
            intent.writeToParcel(data, 0);
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(67, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                z = false;
            }
            reply.recycle();
            data.recycle();
            return z;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(taskId);
            if (options == null) {
                data.writeInt(0);
            } else {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            }
            this.mRemote.transact(230, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean finishActivity(IBinder token, int resultCode, Intent resultData, boolean finishTask) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeInt(resultCode);
            if (resultData != null) {
                data.writeInt(1);
                resultData.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(finishTask ? 1 : 0);
            this.mRemote.transact(11, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void finishSubActivity(IBinder token, String resultWho, int requestCode) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            this.mRemote.transact(32, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean finishActivityAffinity(IBinder token) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(149, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(session.asBinder());
            this.mRemote.transact(224, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean releaseActivityInstance(IBinder token) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(236, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void releaseSomeActivities(IApplicationThread app) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(app.asBinder());
            this.mRemote.transact(237, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean willActivityBeVisible(IBinder token) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(106, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public Intent registerReceiver(IApplicationThread caller, String packageName, IIntentReceiver receiver, IntentFilter filter, String perm, int userId) throws RemoteException {
        IBinder iBinder = null;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            IBinder asBinder;
            data.writeInterfaceToken(IActivityManager.descriptor);
            if (caller != null) {
                asBinder = caller.asBinder();
            } else {
                asBinder = null;
            }
            data.writeStrongBinder(asBinder);
            data.writeString(packageName);
            if (receiver != null) {
                iBinder = receiver.asBinder();
            }
            data.writeStrongBinder(iBinder);
            filter.writeToParcel(data, 0);
            data.writeString(perm);
            data.writeInt(userId);
            this.mRemote.transact(12, data, reply, 0);
            reply.readException();
            Intent intent = null;
            if (reply.readInt() != 0) {
                intent = (Intent) Intent.CREATOR.createFromParcel(reply);
            }
            reply.recycle();
            data.recycle();
            return intent;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(receiver.asBinder());
            this.mRemote.transact(13, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(resultTo != null ? resultTo.asBinder() : null);
            data.writeInt(resultCode);
            data.writeString(resultData);
            data.writeBundle(map);
            data.writeStringArray(requiredPermissions);
            data.writeInt(appOp);
            data.writeBundle(options);
            data.writeInt(serialized ? 1 : 0);
            data.writeInt(sticky ? 1 : 0);
            data.writeInt(userId);
            this.mRemote.transact(14, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            intent.writeToParcel(data, 0);
            data.writeInt(userId);
            this.mRemote.transact(15, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
        int i = 1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(who);
            data.writeInt(resultCode);
            data.writeString(resultData);
            data.writeBundle(map);
            if (!abortBroadcast) {
                i = 0;
            }
            data.writeInt(i);
            data.writeInt(flags);
            this.mRemote.transact(16, data, reply, 1);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void attachApplication(IApplicationThread app) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(app.asBinder());
            this.mRemote.transact(17, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void activityIdle(IBinder token, Configuration config, boolean stopProfiling) throws RemoteException {
        int i = 1;
        Log.i(TAG_TIMELINE, "Timeline: Activity_idle id: " + token + " time:" + SystemClock.uptimeMillis());
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (config != null) {
                data.writeInt(1);
                config.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            if (!stopProfiling) {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(18, data, reply, 1);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void activityResumed(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(39, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void activityPaused(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(19, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void activityStopped(IBinder token, Bundle state, PersistableBundle persistentState, CharSequence description) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeBundle(state);
            data.writePersistableBundle(persistentState);
            TextUtils.writeToParcel(description, data, 0);
            this.mRemote.transact(20, data, reply, 1);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void activitySlept(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(123, data, reply, 1);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void activityDestroyed(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(62, data, reply, 1);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public String getCallingPackage(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(21, data, reply, 0);
            reply.readException();
            String res = reply.readString();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public ComponentName getCallingActivity(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(22, data, reply, 0);
            reply.readException();
            ComponentName res = ComponentName.readFromParcel(reply);
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public List<IAppTask> getAppTasks(String callingPackage) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(callingPackage);
            this.mRemote.transact(221, data, reply, 0);
            reply.readException();
            ArrayList<IAppTask> list = null;
            int N = reply.readInt();
            if (N >= 0) {
                list = new ArrayList();
                while (N > 0) {
                    list.add(Stub.asInterface(reply.readStrongBinder()));
                    N--;
                }
            }
            data.recycle();
            reply.recycle();
            return list;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public int addAppTask(IBinder activityToken, Intent intent, TaskDescription description, Bitmap thumbnail) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(activityToken);
            intent.writeToParcel(data, 0);
            description.writeToParcel(data, 0);
            thumbnail.writeToParcel(data, 0);
            this.mRemote.transact(234, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public Point getAppTaskThumbnailSize() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(235, data, reply, 0);
            reply.readException();
            Point size = (Point) Point.CREATOR.createFromParcel(reply);
            return size;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public List<RunningTaskInfo> getTasks(int maxNum, int flags) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(maxNum);
            data.writeInt(flags);
            this.mRemote.transact(23, data, reply, 0);
            reply.readException();
            ArrayList<RunningTaskInfo> list = null;
            int N = reply.readInt();
            if (N >= 0) {
                list = new ArrayList();
                while (N > 0) {
                    list.add((RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(reply));
                    N--;
                }
            }
            data.recycle();
            reply.recycle();
            return list;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public List getTasks(int maxNum, int flags, int displayId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(maxNum);
            data.writeInt(flags);
            data.writeInt(displayId);
            this.mRemote.transact(5003, data, reply, 0);
            reply.readException();
            ArrayList list = null;
            int N = reply.readInt();
            if (N >= 0) {
                list = new ArrayList();
                while (N > 0) {
                    list.add((RunningTaskInfo) RunningTaskInfo.CREATOR.createFromParcel(reply));
                    N--;
                }
            }
            data.recycle();
            reply.recycle();
            return list;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public List<RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(maxNum);
            data.writeInt(flags);
            data.writeInt(userId);
            this.mRemote.transact(60, data, reply, 0);
            reply.readException();
            ArrayList<RecentTaskInfo> list = reply.createTypedArrayList(RecentTaskInfo.CREATOR);
            return list;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public TaskThumbnail getTaskThumbnail(int id) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(id);
            this.mRemote.transact(82, data, reply, 0);
            reply.readException();
            TaskThumbnail taskThumbnail = null;
            if (reply.readInt() != 0) {
                taskThumbnail = (TaskThumbnail) TaskThumbnail.CREATOR.createFromParcel(reply);
            }
            data.recycle();
            reply.recycle();
            return taskThumbnail;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public List<RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(maxNum);
            data.writeInt(flags);
            this.mRemote.transact(81, data, reply, 0);
            reply.readException();
            ArrayList<RunningServiceInfo> list = null;
            int N = reply.readInt();
            if (N >= 0) {
                list = new ArrayList();
                while (N > 0) {
                    list.add((RunningServiceInfo) RunningServiceInfo.CREATOR.createFromParcel(reply));
                    N--;
                }
            }
            data.recycle();
            reply.recycle();
            return list;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public List<ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(77, data, reply, 0);
            reply.readException();
            ArrayList<ProcessErrorStateInfo> list = reply.createTypedArrayList(ProcessErrorStateInfo.CREATOR);
            return list;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public List<RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(83, data, reply, 0);
            reply.readException();
            ArrayList<RunningAppProcessInfo> list = reply.createTypedArrayList(RunningAppProcessInfo.CREATOR);
            return list;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public String getPackageFromAppProcesses(int pid) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(pid);
            this.mRemote.transact(IActivityManager.GET_PACKAGE_FROM_APP_PROCESSES_TRANSACTION, data, reply, 0);
            reply.readException();
            String res = reply.readString();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(108, data, reply, 0);
            reply.readException();
            ArrayList<ApplicationInfo> list = reply.createTypedArrayList(ApplicationInfo.CREATOR);
            return list;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void moveTaskToFront(int task, int flags, Bundle options) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(task);
            data.writeInt(flags);
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(24, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (nonRoot) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(75, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void moveTaskBackwards(int task) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(task);
            this.mRemote.transact(26, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(taskId);
            data.writeInt(stackId);
            if (toTop) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(169, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void resizeStack(int stackBoxId, Rect r) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(stackBoxId);
            r.writeToParcel(data, 0);
            this.mRemote.transact(170, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public List<StackInfo> getAllStackInfos() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(171, data, reply, 0);
            reply.readException();
            ArrayList<StackInfo> list = reply.createTypedArrayList(StackInfo.CREATOR);
            return list;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public StackInfo getStackInfo(int stackId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(stackId);
            this.mRemote.transact(173, data, reply, 0);
            reply.readException();
            StackInfo info = null;
            if (reply.readInt() != 0) {
                info = (StackInfo) StackInfo.CREATOR.createFromParcel(reply);
            }
            data.recycle();
            reply.recycle();
            return info;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isInHomeStack(int taskId) throws RemoteException {
        boolean isInHomeStack = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(taskId);
            this.mRemote.transact(213, data, reply, 0);
            reply.readException();
            if (reply.readInt() > 0) {
                isInHomeStack = true;
            }
            data.recycle();
            reply.recycle();
            return isInHomeStack;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void setFocusedStack(int stackId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(stackId);
            this.mRemote.transact(172, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int getFocusedStackId() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        this.mRemote.transact(IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION, data, reply, 0);
        reply.readException();
        int focusedStackId = reply.readInt();
        data.recycle();
        reply.recycle();
        return focusedStackId;
    }

    public void setFocusedStack(int stackId, boolean tapOutSide) throws RemoteException {
        int i = 1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(stackId);
            if (!tapOutSide) {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(1012, data, reply, 1);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(listener.asBinder());
        this.mRemote.transact(243, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (onlyRoot) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(27, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public ContentProviderHolder getContentProvider(IApplicationThread caller, String name, int userId, boolean stable) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeString(name);
            data.writeInt(userId);
            if (stable) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(29, data, reply, 0);
            reply.readException();
            ContentProviderHolder cph = null;
            if (reply.readInt() != 0) {
                cph = (ContentProviderHolder) ContentProviderHolder.CREATOR.createFromParcel(reply);
            }
            data.recycle();
            reply.recycle();
            return cph;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(name);
            data.writeInt(userId);
            data.writeStrongBinder(token);
            this.mRemote.transact(141, data, reply, 0);
            reply.readException();
            ContentProviderHolder cph = null;
            if (reply.readInt() != 0) {
                cph = (ContentProviderHolder) ContentProviderHolder.CREATOR.createFromParcel(reply);
            }
            data.recycle();
            reply.recycle();
            return cph;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> providers) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeTypedList(providers);
            this.mRemote.transact(30, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean refContentProvider(IBinder connection, int stable, int unstable) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(connection);
            data.writeInt(stable);
            data.writeInt(unstable);
            this.mRemote.transact(31, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void unstableProviderDied(IBinder connection) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(connection);
            this.mRemote.transact(151, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(connection);
            this.mRemote.transact(183, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(connection);
            if (stable) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(69, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(name);
            data.writeStrongBinder(token);
            this.mRemote.transact(142, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            service.writeToParcel(data, 0);
            this.mRemote.transact(33, data, reply, 0);
            reply.readException();
            PendingIntent res = PendingIntent.readPendingIntentOrNullFromParcel(reply);
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, String callingPackage, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            service.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeString(callingPackage);
            data.writeInt(userId);
            this.mRemote.transact(34, data, reply, 0);
            reply.readException();
            ComponentName res = ComponentName.readFromParcel(reply);
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, String callingPackage, int userId, IBinder callerActivityToken, int displayId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            service.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeString(callingPackage);
            data.writeInt(userId);
            data.writeStrongBinder(callerActivityToken);
            data.writeInt(displayId);
            this.mRemote.transact(5002, data, reply, 0);
            reply.readException();
            ComponentName res = ComponentName.readFromParcel(reply);
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            service.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeInt(userId);
            this.mRemote.transact(35, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            ComponentName.writeToParcel(className, data);
            data.writeStrongBinder(token);
            data.writeInt(startId);
            this.mRemote.transact(48, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, boolean removeNotification) throws RemoteException {
        int i = 1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            ComponentName.writeToParcel(className, data);
            data.writeStrongBinder(token);
            data.writeInt(id);
            if (notification != null) {
                data.writeInt(1);
                notification.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            if (!removeNotification) {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(74, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeStrongBinder(token);
            service.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeStrongBinder(connection.asBinder());
            data.writeInt(flags);
            data.writeString(callingPackage);
            data.writeInt(userId);
            this.mRemote.transact(36, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean unbindService(IServiceConnection connection) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(connection.asBinder());
            this.mRemote.transact(37, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            intent.writeToParcel(data, 0);
            data.writeStrongBinder(service);
            this.mRemote.transact(38, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void unbindFinished(IBinder token, Intent intent, boolean doRebind) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            intent.writeToParcel(data, 0);
            if (doRebind) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(72, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeInt(type);
            data.writeInt(startId);
            data.writeInt(res);
            this.mRemote.transact(61, data, reply, 1);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            service.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeString(callingPackage);
            this.mRemote.transact(85, data, reply, 0);
            reply.readException();
            IBinder binder = reply.readStrongBinder();
            return binder;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean bindBackupAgent(String packageName, int backupRestoreMode, int userId) throws RemoteException {
        boolean success = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeInt(backupRestoreMode);
            data.writeInt(userId);
            this.mRemote.transact(90, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                success = true;
            }
            reply.recycle();
            data.recycle();
            return success;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void clearPendingBackup() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(160, data, reply, 0);
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void backupAgentCreated(String packageName, IBinder agent) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeStrongBinder(agent);
            this.mRemote.transact(91, data, reply, 0);
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void unbindBackupAgent(ApplicationInfo app) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            app.writeToParcel(data, 0);
            this.mRemote.transact(92, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String instructionSet) throws RemoteException {
        IBinder iBinder = null;
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            IBinder asBinder;
            data.writeInterfaceToken(IActivityManager.descriptor);
            ComponentName.writeToParcel(className, data);
            data.writeString(profileFile);
            data.writeInt(flags);
            data.writeBundle(arguments);
            if (watcher != null) {
                asBinder = watcher.asBinder();
            } else {
                asBinder = null;
            }
            data.writeStrongBinder(asBinder);
            if (connection != null) {
                iBinder = connection.asBinder();
            }
            data.writeStrongBinder(iBinder);
            data.writeInt(userId);
            data.writeString(instructionSet);
            this.mRemote.transact(44, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            reply.recycle();
            data.recycle();
            return res;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(target != null ? target.asBinder() : null);
            data.writeInt(resultCode);
            data.writeBundle(results);
            this.mRemote.transact(45, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public Configuration getConfiguration() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(46, data, reply, 0);
            reply.readException();
            Configuration res = (Configuration) Configuration.CREATOR.createFromParcel(reply);
            return res;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void updateConfiguration(Configuration values) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            values.writeToParcel(data, 0);
            this.mRemote.transact(47, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void multiWindowSettingChanged(boolean value) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            if (value) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(1011, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void notifyMinimizeMultiWindow(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(1013, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void notifyDisplayFreezeStopped() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(1014, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void notifyCascadeStackRotated(int stackId, Rect bounds) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(stackId);
            bounds.writeToParcel(data, 0);
            this.mRemote.transact(1015, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeInt(requestedOrientation);
            this.mRemote.transact(70, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int getRequestedOrientation(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(71, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public ComponentName getActivityClassForToken(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(49, data, reply, 0);
            reply.readException();
            ComponentName res = ComponentName.readFromParcel(reply);
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public String getPackageForToken(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(50, data, reply, 0);
            reply.readException();
            String res = reply.readString();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(type);
            data.writeString(packageName);
            data.writeStrongBinder(token);
            data.writeString(resultWho);
            data.writeInt(requestCode);
            if (intents != null) {
                data.writeInt(1);
                data.writeTypedArray(intents, 0);
                data.writeStringArray(resolvedTypes);
            } else {
                data.writeInt(0);
            }
            data.writeInt(flags);
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(userId);
            this.mRemote.transact(63, data, reply, 0);
            reply.readException();
            IIntentSender res = IIntentSender.Stub.asInterface(reply.readStrongBinder());
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void cancelIntentSender(IIntentSender sender) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            this.mRemote.transact(64, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public IIntentSender grabIntentSender(IIntentSender sender, String packageName, long[] outTime) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            data.writeString(packageName);
            data.writeLongArray(outTime);
            this.mRemote.transact(6001, data, reply, 0);
            reply.readException();
            IIntentSender res = IIntentSender.Stub.asInterface(reply.readStrongBinder());
            reply.readLongArray(outTime);
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public Map getGrabedIntentSenders() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        Map<Long, PendingIntent> piMap = new HashMap();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(6002, data, reply, 0);
            reply.readException();
            Map res = reply.readHashMap(null);
            if (res.size() >= 0) {
                for (Long longValue : res.keySet()) {
                    long time = longValue.longValue();
                    piMap.put(Long.valueOf(time), new PendingIntent(IIntentSender.Stub.asInterface((IBinder) res.get(Long.valueOf(time)))));
                }
            }
            data.recycle();
            reply.recycle();
            return piMap;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public String getPackageForIntentSender(IIntentSender sender) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            this.mRemote.transact(65, data, reply, 0);
            reply.readException();
            String res = reply.readString();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int getUidForIntentSender(IIntentSender sender) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            this.mRemote.transact(93, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int getKidForIntentSender(IIntentSender sender) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            this.mRemote.transact(190, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
        int i = 1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i2;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(callingPid);
            data.writeInt(callingUid);
            data.writeInt(userId);
            if (allowAll) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            data.writeInt(i2);
            if (!requireFull) {
                i = 0;
            }
            data.writeInt(i);
            data.writeString(name);
            data.writeString(callerPackage);
            this.mRemote.transact(94, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setProcessLimit(int max) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(max);
            this.mRemote.transact(51, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int getProcessLimit() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(52, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setProcessForeground(IBinder token, int pid, boolean isForeground, boolean isCalledByNotificationManagerService) throws RemoteException {
        int i = 1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeInt(pid);
            data.writeInt(isForeground ? 1 : 0);
            if (!isCalledByNotificationManagerService) {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(IActivityManager.SET_PROCESS_FOREGROUND_EX_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setProcessForeground(IBinder token, int pid, boolean isForeground) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeInt(pid);
            if (isForeground) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(73, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int checkPermission(String permission, int pid, int uid) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(permission);
            data.writeInt(pid);
            data.writeInt(uid);
            this.mRemote.transact(53, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int checkPermissionWithToken(String permission, int pid, int uid, IBinder callerToken) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeString(permission);
        data.writeInt(pid);
        data.writeInt(uid);
        data.writeStrongBinder(callerToken);
        this.mRemote.transact(242, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean clearApplicationUserData(String packageName, IPackageDataObserver observer, int userId) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeStrongBinder(observer != null ? observer.asBinder() : null);
            data.writeInt(userId);
            this.mRemote.transact(78, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            uri.writeToParcel(data, 0);
            data.writeInt(pid);
            data.writeInt(uid);
            data.writeInt(mode);
            data.writeInt(userId);
            data.writeStrongBinder(callerToken);
            this.mRemote.transact(54, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller.asBinder());
            data.writeString(targetPkg);
            uri.writeToParcel(data, 0);
            data.writeInt(mode);
            data.writeInt(userId);
            this.mRemote.transact(55, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void revokeUriPermission(IApplicationThread caller, Uri uri, int mode, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller.asBinder());
            uri.writeToParcel(data, 0);
            data.writeInt(mode);
            data.writeInt(userId);
            this.mRemote.transact(56, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void takePersistableUriPermission(Uri uri, int mode, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            uri.writeToParcel(data, 0);
            data.writeInt(mode);
            data.writeInt(userId);
            this.mRemote.transact(180, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void releasePersistableUriPermission(Uri uri, int mode, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            uri.writeToParcel(data, 0);
            data.writeInt(mode);
            data.writeInt(userId);
            this.mRemote.transact(181, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public ParceledListSlice<UriPermission> getPersistedUriPermissions(String packageName, boolean incoming) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            if (incoming) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(182, data, reply, 0);
            reply.readException();
            ParceledListSlice<UriPermission> perms = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(reply);
            return perms;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(who.asBinder());
            if (waiting) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(58, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void getMemoryInfo(MemoryInfo outInfo) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(76, data, reply, 0);
            reply.readException();
            outInfo.readFromParcel(reply);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void unhandledBack() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(4, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public ParcelFileDescriptor openContentUri(Uri uri) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(5, data, reply, 0);
            reply.readException();
            ParcelFileDescriptor pfd = null;
            if (reply.readInt() != 0) {
                pfd = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(reply);
            }
            data.recycle();
            reply.recycle();
            return pfd;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void setLockScreenShown(boolean shown) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            if (shown) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(148, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
        int i = 1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeInt(waitForDebugger ? 1 : 0);
            if (!persistent) {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(42, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setAlwaysFinish(boolean enabled) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            if (enabled) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(43, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setActivityController(IActivityController watcher) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
            this.mRemote.transact(57, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void enterSafeMode() throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        this.mRemote.transact(66, data, null, 0);
        data.recycle();
    }

    public void noteWakeupAlarm(IIntentSender sender, int sourceUid, String sourcePkg, String tag) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        data.writeInt(sourceUid);
        data.writeString(sourcePkg);
        data.writeString(tag);
        this.mRemote.transact(68, data, null, 0);
        data.recycle();
    }

    public void noteAlarmStart(IIntentSender sender, int sourceUid, String tag) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        data.writeInt(sourceUid);
        data.writeString(tag);
        this.mRemote.transact(IActivityManager.NOTE_ALARM_START_TRANSACTION, data, null, 0);
        data.recycle();
    }

    public void noteAlarmFinish(IIntentSender sender, int sourceUid, String tag) throws RemoteException {
        Parcel data = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        data.writeInt(sourceUid);
        data.writeString(tag);
        this.mRemote.transact(IActivityManager.NOTE_ALARM_FINISH_TRANSACTION, data, null, 0);
        data.recycle();
    }

    public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeIntArray(pids);
            data.writeString(reason);
            if (secure) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(80, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean killProcessesBelowForeground(String reason) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(reason);
            this.mRemote.transact(144, data, reply, 0);
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean testIsSystemReady() {
        return true;
    }

    public void handleApplicationCrash(IBinder app, CrashInfo crashInfo) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(app);
            crashInfo.writeToParcel(data, 0);
            this.mRemote.transact(2, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean handleApplicationWtf(IBinder app, String tag, boolean system, CrashInfo crashInfo) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(app);
            data.writeString(tag);
            if (system) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            crashInfo.writeToParcel(data, 0);
            this.mRemote.transact(102, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            reply.recycle();
            data.recycle();
            return res;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void handleApplicationStrictModeViolation(IBinder app, int violationMask, ViolationInfo info) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(app);
            data.writeInt(violationMask);
            info.writeToParcel(data, 0);
            this.mRemote.transact(110, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void signalPersistentProcesses(int sig) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(sig);
            this.mRemote.transact(59, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeInt(userId);
            this.mRemote.transact(103, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void killAllBackgroundProcesses() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(140, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void forceStopPackage(String packageName, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeInt(userId);
            this.mRemote.transact(79, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void forceStopPackageByAdmin(String packageName, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeInt(userId);
            this.mRemote.transact(IActivityManager.FORCE_STOP_PACKAGE_BYADMIN_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean checkKnoxPermission(String srcPkgName, int srcUid, String destPkgName, int destUid, String methodMessage) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(srcPkgName);
            data.writeInt(srcUid);
            data.writeString(destPkgName);
            data.writeInt(destUid);
            data.writeString(methodMessage);
            this.mRemote.transact(1501, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean checkKnoxPermission(int srcPid, int srcUid, String destPkgName, int destUid, String methodMessage) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(srcPid);
            data.writeInt(srcUid);
            data.writeString(destPkgName);
            data.writeInt(destUid);
            data.writeString(methodMessage);
            this.mRemote.transact(IActivityManager.CHECK_KNOX_PERMISSION_BY_PID_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void unFreezeApp(String packageName, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeInt(userId);
            this.mRemote.transact(IActivityManager.UNFREEZE_APP, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void getSMpkgsList(List<String> smPackagesList) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStringList(smPackagesList);
            this.mRemote.transact(IActivityManager.GET_SMPKGS_LIST, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isAutoRunBlockedApp(String packageName) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            this.mRemote.transact(IActivityManager.AUTORUN_BLOCKED_APP, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void getMyMemoryState(RunningAppProcessInfo outInfo) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(143, data, reply, 0);
            reply.readException();
            outInfo.readFromParcel(reply);
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(84, data, reply, 0);
            reply.readException();
            ConfigurationInfo res = (ConfigurationInfo) ConfigurationInfo.CREATOR.createFromParcel(reply);
            return res;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(process);
            data.writeInt(userId);
            if (start) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            data.writeInt(profileType);
            if (profilerInfo != null) {
                data.writeInt(1);
                profilerInfo.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(86, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            reply.recycle();
            data.recycle();
            return res;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean shutdown(int timeout) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(timeout);
            this.mRemote.transact(87, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            reply.recycle();
            data.recycle();
            return res;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void stopAppSwitches() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(88, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void resumeAppSwitches() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(89, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void addPackageDependency(String packageName) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            this.mRemote.transact(95, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void killApplicationWithAppId(String pkg, int appid, String reason) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(pkg);
            data.writeInt(appid);
            data.writeString(reason);
            this.mRemote.transact(96, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void closeSystemDialogs(String reason) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(reason);
            this.mRemote.transact(97, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeIntArray(pids);
            this.mRemote.transact(98, data, reply, 0);
            reply.readException();
            Debug.MemoryInfo[] res = (Debug.MemoryInfo[]) reply.createTypedArray(Debug.MemoryInfo.CREATOR);
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void killApplicationProcess(String processName, int uid) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(processName);
            data.writeInt(uid);
            this.mRemote.transact(99, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void overridePendingTransition(IBinder token, String packageName, int enterAnim, int exitAnim) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeString(packageName);
            data.writeInt(enterAnim);
            data.writeInt(exitAnim);
            this.mRemote.transact(101, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isUserAMonkey() throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(104, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void setUserIsMonkey(boolean monkey) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            if (monkey) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(166, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void finishHeavyWeightApp() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(109, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean convertFromTranslucent(IBinder token) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(174, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean convertFromTranslucent(IBinder token, boolean skipSetWindowOpaque) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (skipSetWindowOpaque) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(IActivityManager.CONVERT_FROM_TRANSLUCENT_SKIPWINDOWOPAQUE_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean convertToTranslucent(IBinder token, ActivityOptions options) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (options == null) {
                data.writeInt(0);
            } else {
                data.writeInt(1);
                data.writeBundle(options.toBundle());
            }
            this.mRemote.transact(175, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public ActivityOptions getActivityOptions(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(220, data, reply, 0);
            reply.readException();
            Bundle bundle = reply.readBundle();
            ActivityOptions options = bundle == null ? null : new ActivityOptions(bundle);
            data.recycle();
            reply.recycle();
            return options;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void setImmersive(IBinder token, boolean immersive) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (immersive) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(112, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isImmersive(IBinder token) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(111, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 1) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isTopOfTask(IBinder token) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(225, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 1) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isTopActivityImmersive() throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(113, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 1) {
                res = false;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void crashApplication(int uid, int initialPid, String packageName, String message) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(uid);
            data.writeInt(initialPid);
            data.writeString(packageName);
            data.writeString(message);
            this.mRemote.transact(114, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            uri.writeToParcel(data, 0);
            data.writeInt(userId);
            this.mRemote.transact(115, data, reply, 0);
            reply.readException();
            String res = reply.readString();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public IBinder newUriPermissionOwner(String name) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(name);
            this.mRemote.transact(116, data, reply, 0);
            reply.readException();
            IBinder res = reply.readStrongBinder();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void grantUriPermissionFromOwner(IBinder owner, int fromUid, String targetPkg, Uri uri, int mode, int sourceUserId, int targetUserId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(owner);
            data.writeInt(fromUid);
            data.writeString(targetPkg);
            uri.writeToParcel(data, 0);
            data.writeInt(mode);
            data.writeInt(sourceUserId);
            data.writeInt(targetUserId);
            this.mRemote.transact(55, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void revokeUriPermissionFromOwner(IBinder owner, Uri uri, int mode, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(owner);
            if (uri != null) {
                data.writeInt(1);
                uri.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(mode);
            data.writeInt(userId);
            this.mRemote.transact(56, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public int checkGrantUriPermission(int callingUid, String targetPkg, Uri uri, int modeFlags, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(callingUid);
            data.writeString(targetPkg);
            uri.writeToParcel(data, 0);
            data.writeInt(modeFlags);
            data.writeInt(userId);
            this.mRemote.transact(119, data, reply, 0);
            reply.readException();
            int res = reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean dumpHeap(String process, int userId, boolean managed, String path, ParcelFileDescriptor fd) throws RemoteException {
        boolean res = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(process);
            data.writeInt(userId);
            if (managed) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            data.writeString(path);
            if (fd != null) {
                data.writeInt(1);
                fd.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(120, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                res = false;
            }
            reply.recycle();
            data.recycle();
            return res;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public int startActivities(IApplicationThread caller, String callingPackage, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(caller != null ? caller.asBinder() : null);
            data.writeString(callingPackage);
            data.writeTypedArray(intents, 0);
            data.writeStringArray(resolvedTypes);
            data.writeStrongBinder(resultTo);
            if (options != null) {
                data.writeInt(1);
                options.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            data.writeInt(userId);
            this.mRemote.transact(121, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public int getFrontActivityScreenCompatMode() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(124, data, reply, 0);
            reply.readException();
            int mode = reply.readInt();
            return mode;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(mode);
            this.mRemote.transact(125, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public int getPackageScreenCompatMode(String packageName) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            this.mRemote.transact(126, data, reply, 0);
            reply.readException();
            int mode = reply.readInt();
            return mode;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            data.writeInt(mode);
            this.mRemote.transact(127, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
        boolean ask = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            this.mRemote.transact(128, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                ask = true;
            }
            reply.recycle();
            data.recycle();
            return ask;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            if (ask) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(129, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean switchUser(int userid) throws RemoteException {
        boolean result = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(userid);
            this.mRemote.transact(130, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean startUserInBackground(int userid) throws RemoteException {
        boolean result = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(userid);
            this.mRemote.transact(212, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public int stopUser(int userid, IStopUserCallback callback) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(userid);
            data.writeStrongInterface(callback);
            this.mRemote.transact(154, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public UserInfo getCurrentUser() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(145, data, reply, 0);
            reply.readException();
            UserInfo userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(reply);
            return userInfo;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean isUserRunning(int userid, boolean orStopping) throws RemoteException {
        boolean result = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(userid);
            if (orStopping) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(122, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                result = false;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public int[] getRunningUserIds() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(157, data, reply, 0);
            reply.readException();
            int[] result = reply.createIntArray();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean removeTask(int taskId) throws RemoteException {
        boolean result = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(taskId);
            this.mRemote.transact(132, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean removeTask(int taskId, int flags) throws RemoteException {
        boolean result = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(taskId);
            data.writeInt(flags);
            this.mRemote.transact(IActivityManager.REMOVE_TASK_EXT_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(observer != null ? observer.asBinder() : null);
            this.mRemote.transact(133, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(observer != null ? observer.asBinder() : null);
            this.mRemote.transact(134, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void registerUidObserver(IUidObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        this.mRemote.transact(IActivityManager.REGISTER_UID_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void unregisterUidObserver(IUidObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        this.mRemote.transact(IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            this.mRemote.transact(135, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            this.mRemote.transact(152, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            this.mRemote.transact(161, data, reply, 0);
            reply.readException();
            Intent res = reply.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(reply) : null;
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(sender.asBinder());
            data.writeString(prefix);
            this.mRemote.transact(211, data, reply, 0);
            reply.readException();
            String res = reply.readString();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void updatePersistentConfiguration(Configuration values) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            values.writeToParcel(data, 0);
            this.mRemote.transact(136, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public long[] getProcessPss(int[] pids) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeIntArray(pids);
            this.mRemote.transact(137, data, reply, 0);
            reply.readException();
            long[] res = reply.createLongArray();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            TextUtils.writeToParcel(msg, data, 0);
            if (always) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(138, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void keyguardWaitingForActivityDrawn() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(232, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void keyguardGoingAway(boolean disableWindowAnimations, boolean keyguardGoingToNotificationShade) throws RemoteException {
        int i = 1;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeInt(disableWindowAnimations ? 1 : 0);
        if (!keyguardGoingToNotificationShade) {
            i = 0;
        }
        data.writeInt(i);
        this.mRemote.transact(IActivityManager.KEYGUARD_GOING_AWAY_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void keyguardWaitingForActivityDrawnTarget(Intent target) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            target.writeToParcel(data, 0);
            this.mRemote.transact(IActivityManager.KEYGUARD_WAITING_FOR_ACTIVITY_DRAWN_TARGET_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean shouldUpRecreateTask(IBinder token, String destAffinity) throws RemoteException {
        boolean result = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            data.writeString(destAffinity);
            this.mRemote.transact(146, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            }
            data.recycle();
            reply.recycle();
            return result;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData) throws RemoteException {
        boolean result = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            target.writeToParcel(data, 0);
            data.writeInt(resultCode);
            if (resultData != null) {
                data.writeInt(1);
                resultData.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(147, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 0) {
                result = false;
            }
            data.recycle();
            reply.recycle();
            return result;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(activityToken);
            this.mRemote.transact(150, data, reply, 0);
            reply.readException();
            int result = reply.readInt();
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(activityToken);
            this.mRemote.transact(164, data, reply, 0);
            reply.readException();
            String result = reply.readString();
            return result;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void registerUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(observer != null ? observer.asBinder() : null);
            this.mRemote.transact(155, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(observer != null ? observer.asBinder() : null);
            this.mRemote.transact(156, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean clearRecentTasks(int userid) throws RemoteException {
        boolean result = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(userid);
            this.mRemote.transact(188, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public void requestBugReport() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(158, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public long inputDispatchingTimedOut(int pid, boolean aboveSystem, String reason) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(pid);
            if (aboveSystem) {
                i = 1;
            }
            data.writeInt(i);
            data.writeString(reason);
            this.mRemote.transact(159, data, reply, 0);
            reply.readException();
            long res = (long) reply.readInt();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public Bundle getAssistContextExtras(int requestType) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(requestType);
            this.mRemote.transact(162, data, reply, 0);
            reply.readException();
            Bundle res = reply.readBundle();
            return res;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean requestAssistContextExtras(int requestType, IResultReceiver receiver, IBinder activityToken) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(requestType);
            data.writeStrongBinder(receiver.asBinder());
            data.writeStrongBinder(activityToken);
            this.mRemote.transact(IActivityManager.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void reportAssistContextExtras(IBinder token, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeBundle(extras);
        structure.writeToParcel(data, 0);
        content.writeToParcel(data, 0);
        if (referrer != null) {
            data.writeInt(1);
            referrer.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        this.mRemote.transact(163, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean launchAssistIntent(Intent intent, int requestType, String hint, int userHandle, Bundle args) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            intent.writeToParcel(data, 0);
            data.writeInt(requestType);
            data.writeString(hint);
            data.writeInt(userHandle);
            data.writeBundle(args);
            this.mRemote.transact(240, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        this.mRemote.transact(300, data, reply, 0);
        reply.readException();
        if (reply.readInt() != 0) {
            res = true;
        }
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean showAssistFromActivity(IBinder token, Bundle args) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeBundle(args);
        this.mRemote.transact(301, data, reply, 0);
        reply.readException();
        if (reply.readInt() != 0) {
            res = true;
        }
        data.recycle();
        reply.recycle();
        return res;
    }

    public void killUid(int appId, int userId, String reason) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(appId);
            data.writeInt(userId);
            data.writeString(reason);
            this.mRemote.transact(165, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void hang(IBinder who, boolean allowRestart) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(who);
            if (allowRestart) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(167, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void reportActivityFullyDrawn(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(177, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void notifyActivityDrawn(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(176, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void restart() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(178, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void performIdleMaintenance() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(179, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public IActivityContainer createVirtualActivityContainer(IBinder parentActivityToken, IActivityContainerCallback callback) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            IActivityContainer res;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(parentActivityToken);
            data.writeStrongBinder(callback == null ? null : callback.asBinder());
            this.mRemote.transact(168, data, reply, 0);
            reply.readException();
            if (reply.readInt() == 1) {
                res = IActivityContainer.Stub.asInterface(reply.readStrongBinder());
            } else {
                res = null;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void deleteActivityContainer(IActivityContainer activityContainer) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(activityContainer.asBinder());
            this.mRemote.transact(186, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public IActivityContainer createStackOnDisplay(int displayId) throws RemoteException {
        IActivityContainer res;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeInt(displayId);
        this.mRemote.transact(IActivityManager.CREATE_STACK_ON_DISPLAY, data, reply, 0);
        reply.readException();
        if (reply.readInt() == 1) {
            res = IActivityContainer.Stub.asInterface(reply.readStrongBinder());
        } else {
            res = null;
        }
        data.recycle();
        reply.recycle();
        return res;
    }

    public int getActivityDisplayId(IBinder activityToken) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(activityToken);
        this.mRemote.transact(185, data, reply, 0);
        reply.readException();
        int displayId = reply.readInt();
        data.recycle();
        reply.recycle();
        return displayId;
    }

    public void startLockTaskMode(int taskId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(taskId);
            this.mRemote.transact(214, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void startLockTaskMode(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(215, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void startLockTaskModeOnCurrent() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(222, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void stopLockTaskMode() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(216, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void stopLockTaskModeOnCurrent() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(223, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isInLockTaskMode() throws RemoteException {
        boolean isInLockTaskMode = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(217, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 1) {
                isInLockTaskMode = false;
            }
            data.recycle();
            reply.recycle();
            return isInLockTaskMode;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public int getLockTaskModeState() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        this.mRemote.transact(IActivityManager.GET_LOCK_TASK_MODE_STATE_TRANSACTION, data, reply, 0);
        reply.readException();
        int lockTaskModeState = reply.readInt();
        data.recycle();
        reply.recycle();
        return lockTaskModeState;
    }

    public void showLockTaskEscapeMessage(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        this.mRemote.transact(IActivityManager.SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION, data, reply, 1);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setTaskDescription(IBinder token, TaskDescription values) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        values.writeToParcel(data, 0);
        this.mRemote.transact(218, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setTaskResizeable(int taskId, boolean resizeable) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeInt(taskId);
        data.writeInt(resizeable ? 1 : 0);
        this.mRemote.transact(284, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void resizeTask(int taskId, Rect r) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeInt(taskId);
        r.writeToParcel(data, 0);
        this.mRemote.transact(IActivityManager.RESIZE_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public Bitmap getTaskDescriptionIcon(String filename) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(filename);
            this.mRemote.transact(239, data, reply, 0);
            reply.readException();
            Bitmap icon = reply.readInt() == 0 ? null : (Bitmap) Bitmap.CREATOR.createFromParcel(reply);
            data.recycle();
            reply.recycle();
            return icon;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void startInPlaceAnimationOnFrontMostApplication(ActivityOptions options) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        if (options == null) {
            data.writeInt(0);
        } else {
            data.writeInt(1);
            data.writeBundle(options.toBundle());
        }
        this.mRemote.transact(241, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean requestVisibleBehind(IBinder token, boolean visible) throws RemoteException {
        boolean success = true;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            int i;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (visible) {
                i = 1;
            } else {
                i = 0;
            }
            data.writeInt(i);
            this.mRemote.transact(226, data, reply, 0);
            reply.readException();
            if (reply.readInt() <= 0) {
                success = false;
            }
            data.recycle();
            reply.recycle();
            return success;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isBackgroundVisibleBehind(IBinder token) throws RemoteException {
        boolean visible = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            this.mRemote.transact(227, data, reply, 0);
            reply.readException();
            if (reply.readInt() > 0) {
                visible = true;
            }
            data.recycle();
            reply.recycle();
            return visible;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void backgroundResourcesReleased(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        this.mRemote.transact(228, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void notifyLaunchTaskBehindComplete(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        this.mRemote.transact(229, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void notifyEnterAnimationComplete(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        this.mRemote.transact(231, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void bootAnimationComplete() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(238, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeInt(uid);
        data.writeByteArray(firstPacket);
        this.mRemote.transact(IActivityManager.NOTIFY_CLEARTEXT_NETWORK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeString(processName);
        data.writeInt(uid);
        data.writeLong(maxMemSize);
        data.writeString(reportPackage);
        this.mRemote.transact(IActivityManager.SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void dumpHeapFinished(String path) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeString(path);
        this.mRemote.transact(IActivityManager.DUMP_HEAP_FINISHED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(session.asBinder());
        data.writeInt(keepAwake ? 1 : 0);
        this.mRemote.transact(IActivityManager.SET_VOICE_KEEP_AWAKE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeInt(userId);
        data.writeStringArray(packages);
        this.mRemote.transact(IActivityManager.UPDATE_LOCK_TASK_PACKAGES_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void updateDeviceOwner(String packageName) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeString(packageName);
        this.mRemote.transact(IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeString(callingPackage);
        this.mRemote.transact(IActivityManager.GET_PACKAGE_PROCESS_STATE_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean setProcessMemoryTrimLevel(String process, int userId, int level) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeString(process);
        data.writeInt(userId);
        data.writeInt(level);
        this.mRemote.transact(187, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        if (res != 0) {
            return true;
        }
        return false;
    }

    public boolean isRootVoiceInteraction(IBinder token) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeStrongBinder(token);
        this.mRemote.transact(302, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        if (res != 0) {
            return true;
        }
        return false;
    }

    public void updateKnoxContainerRuntimeState(int containerId, int runtimeState) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(containerId);
            data.writeInt(runtimeState);
            this.mRemote.transact(189, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void removeDeletedPkgsFromLru(String pkgName) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeString(pkgName);
        this.mRemote.transact(IActivityManager.REMOVE_DELETED_PACKAGE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setSNNEnable(boolean snnEnable) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        data.writeInt(snnEnable ? 1 : 0);
        this.mRemote.transact(IActivityManager.SET_SNN_ENABLE, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int isSNNEnable() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        this.mRemote.transact(IActivityManager.IS_SNN_ENABLE, data, reply, 0);
        reply.readException();
        int isSNNEnable = reply.readInt();
        reply.recycle();
        data.recycle();
        return isSNNEnable;
    }

    public ArrayList<MemDumpInfo> getDumpMemoryInfo() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(IActivityManager.GET_DUMP_MEMORYINFO, data, reply, 0);
            reply.readException();
            ArrayList<MemDumpInfo> MemDumpList = new ArrayList();
            reply.readList(MemDumpList, null);
            return MemDumpList;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public Configuration getConfiguration(int displayId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(displayId);
            this.mRemote.transact(5001, data, reply, 0);
            reply.readException();
            Configuration res = (Configuration) Configuration.CREATOR.createFromParcel(reply);
            return res;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void updateProcessConfiguration(int targetPid, int displayIdConfiguarationChanged, boolean forcedChange) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeInt(targetPid);
            data.writeInt(displayIdConfiguarationChanged);
            if (forcedChange) {
                data.writeInt(1);
            } else {
                data.writeInt(0);
            }
            this.mRemote.transact(5004, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void setBackWindowShown(boolean show) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            if (show) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(5005, data, reply, 0);
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public ArrayList<String> getAppLockedPackageList() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(IActivityManager.GET_APPLOCKED_PACKAGE_LIST_TRANSACTION, data, reply, 0);
            reply.readException();
            ArrayList<String> packageList = new ArrayList();
            reply.readList(packageList, null);
            return packageList;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public void setAppLockedUnLockPackage(String packageName) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            this.mRemote.transact(IActivityManager.SET_APPLOCKED_UNLOCK_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isAppLockedPackage(String packageName) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            this.mRemote.transact(IActivityManager.IS_APPLOCKED_PACKAGE_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void clearAppLockedUnLockedApp() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(IActivityManager.CLEAR_APPLOCKED_UNLOCKED_APP_TRANSACTION, data, reply, 0);
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public String getAppLockedLockType() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(IActivityManager.GET_APPLOCKED_LOCK_TYPE_TRANSACTION, data, reply, 0);
            reply.readException();
            String lockType = reply.readString();
            return lockType;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public String getAppLockedCheckAction() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            this.mRemote.transact(IActivityManager.GET_APPLOCKED_CHECK_ACTION_TRANSACTION, data, reply, 0);
            reply.readException();
            String lockType = reply.readString();
            return lockType;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    public void setAppLockedVerifying(String packageName, boolean verifying) throws RemoteException {
        int i = 0;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            if (verifying) {
                i = 1;
            }
            data.writeInt(i);
            this.mRemote.transact(IActivityManager.SET_APPLOCKED_VERIFYING_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    public boolean isAppLockedVerifying(String packageName) throws RemoteException {
        boolean res = false;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeString(packageName);
            this.mRemote.transact(IActivityManager.IS_APPLOCKED_VERIFYING_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                res = true;
            }
            data.recycle();
            reply.recycle();
            return res;
        } catch (Throwable th) {
            data.recycle();
            reply.recycle();
        }
    }

    public void enableSafeMode() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        this.mRemote.transact(IActivityManager.ENABLE_SAFE_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean setCustomImage(IBinder token, ParcelFileDescriptor fd, int rotation) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            boolean result;
            data.writeInterfaceToken(IActivityManager.descriptor);
            data.writeStrongBinder(token);
            if (fd != null) {
                data.writeInt(1);
                fd.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            data.writeInt(rotation);
            this.mRemote.transact(IActivityManager.SET_CUSTOM_IMAGE_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            } else {
                result = false;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public boolean setCustomImageForPackage(ComponentName component, int taskUserId, ParcelFileDescriptor fd, int rotation) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            boolean result;
            data.writeInterfaceToken(IActivityManager.descriptor);
            ComponentName.writeToParcel(component, data);
            data.writeInt(taskUserId);
            if (fd != null) {
                data.writeInt(1);
                fd.writeToParcel(data, 1);
            } else {
                data.writeInt(0);
            }
            data.writeInt(rotation);
            this.mRemote.transact(IActivityManager.SET_CUSTOM_IMAGE_FOR_PACKAGE_TRANSACTION, data, reply, 0);
            reply.readException();
            if (reply.readInt() != 0) {
                result = true;
            } else {
                result = false;
            }
            reply.recycle();
            data.recycle();
            return result;
        } catch (Throwable th) {
            reply.recycle();
            data.recycle();
        }
    }

    public String[] queryRegisteredReceiverPackages(Intent intent, String resolvedType, int userId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(IActivityManager.descriptor);
            intent.writeToParcel(data, 0);
            data.writeString(resolvedType);
            data.writeInt(userId);
            this.mRemote.transact(IActivityManager.QUERY_REGISTERED_RECEIVER_PACKAGES_TRANSACTION, data, reply, 0);
            reply.readException();
            String[] result = reply.readStringArray();
            return result;
        } finally {
            reply.recycle();
            data.recycle();
        }
    }
}
