package android.app;

import android.app.INotificationManager.Stub;
import android.app.Notification.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.hardware.Camera.Parameters;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.UserHandle;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.service.notification.IConditionListener;
import android.service.notification.StatusBarNotification;
import android.service.notification.ZenModeConfig;
import android.util.ArraySet;
import android.util.Log;
import com.sec.enterprise.knoxcustom.KnoxCustomManager;
import java.util.List;
import java.util.Objects;

public class NotificationManager {
    public static final String ACTION_EFFECTS_SUPPRESSOR_CHANGED = "android.os.action.ACTION_EFFECTS_SUPPRESSOR_CHANGED";
    public static final String ACTION_INTERRUPTION_FILTER_CHANGED = "android.app.action.INTERRUPTION_FILTER_CHANGED";
    public static final String ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED = "android.app.action.NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED";
    public static final String ACTION_NOTIFICATION_POLICY_CHANGED = "android.app.action.NOTIFICATION_POLICY_CHANGED";
    public static final int INTERRUPTION_FILTER_ALARMS = 4;
    public static final int INTERRUPTION_FILTER_ALL = 1;
    public static final int INTERRUPTION_FILTER_NONE = 3;
    public static final int INTERRUPTION_FILTER_PRIORITY = 2;
    public static final int INTERRUPTION_FILTER_UNKNOWN = 0;
    private static String TAG = "NotificationManager";
    private static boolean localLOGV = false;
    private static INotificationManager sService;
    private Context mContext;
    private EdgeNotificationManager mEdgeNotificationManager;

    private static class EdgeNotificationManager {
        private static final String EXTRA_SAMSUNG_NOTIFICATION_PENDINGINTENT = "samsung.notification.pendingIntent";
        private static final String EXTRA_SAMSUNG_NOTIFICATION_REMOVE_ALL = "samsung.notification.remove_all";
        private static final String EXTRA_SAMSUNG_NOTIFICATION_TYPE = "samsung.notification.type";
        private static final String EXTRA_SAMSUNG_NOTIFICATION_WHEN = "samsung.notification.when";
        private static final String EXTRA_SAMSUNG_PEOPLE_PENDINGINTENT = "samsung.people.pendingIntents";
        private static final String EXTRA_SAMSUNG_PEOPLE_SUBCATEGORY = "samsung.people.subcategory";
        private static final String EXTRA_SAMSUNG_PEOPLE_SUBTITLES = "samsung.people.subTitles";
        private static final String EXTRA_SAMSUNG_PEOPLE_TIMESTAMPS = "samsung.people.timestamps";
        private static final String EXTRA_SAMSUNG_PEOPLE_TITLES = "samsung.people.titles";
        private static final String EXTRA_SAMSUNG_PEOPLE_URIS = "samsung.people.uris";
        private static final String TAG = "NotificationManager.EdgeNotificationManager";
        private Context mContext;

        public EdgeNotificationManager(Context context) {
            this.mContext = context;
        }

        public void removeEdgeNotification(int id, Bundle extra) {
            Log.i(TAG, "removeEdgeNotification:" + id);
            if (extra == null || extra.getString(EXTRA_SAMSUNG_NOTIFICATION_TYPE) == null) {
                removeEdgeNotificationInternal(id, extra);
                return;
            }
            throw new IllegalArgumentException("The bundle has wrong value.");
        }

        public void postEdgeNotification(int id, Bundle extra) {
            Log.i(TAG, "postEdgeNotification:" + id);
            if (extra == null || extra.getString(EXTRA_SAMSUNG_NOTIFICATION_TYPE) != null) {
                throw new IllegalArgumentException("The bundle is null");
            }
            postEdgeNotificationInternal(id, extra);
        }

        public void postEdgeNotificationByNormal(int id, Notification notification) {
            if (notification.extras != null && notification.extras.getStringArrayList(EXTRA_SAMSUNG_PEOPLE_URIS) != null) {
                Log.i(TAG, "postEdgeNotificationByNormal");
                Bundle extra = new Bundle(notification.extras);
                extra.putString(EXTRA_SAMSUNG_NOTIFICATION_TYPE, Parameters.FOCUS_MODE_NORMAL);
                extra.putParcelable(EXTRA_SAMSUNG_NOTIFICATION_PENDINGINTENT, notification.contentIntent);
                extra.putLong(EXTRA_SAMSUNG_NOTIFICATION_WHEN, notification.when);
                notification.extras.remove(EXTRA_SAMSUNG_PEOPLE_URIS);
                notification.extras.remove(EXTRA_SAMSUNG_PEOPLE_TITLES);
                notification.extras.remove(EXTRA_SAMSUNG_PEOPLE_SUBTITLES);
                notification.extras.remove(EXTRA_SAMSUNG_PEOPLE_PENDINGINTENT);
                notification.extras.remove(EXTRA_SAMSUNG_PEOPLE_TIMESTAMPS);
                notification.extras.remove(EXTRA_SAMSUNG_PEOPLE_SUBCATEGORY);
                postEdgeNotificationInternal(id, extra);
            }
        }

        public void removeEdgeNotificationByNormal(int id) {
            Bundle extra = new Bundle();
            extra.putString(EXTRA_SAMSUNG_NOTIFICATION_TYPE, Parameters.FOCUS_MODE_NORMAL);
            removeEdgeNotificationInternal(id, extra);
        }

        public void removeEdgeNotificationAllByNormal() {
            Bundle extra = new Bundle();
            extra.putString(EXTRA_SAMSUNG_NOTIFICATION_TYPE, Parameters.FOCUS_MODE_NORMAL);
            extra.putBoolean(EXTRA_SAMSUNG_NOTIFICATION_REMOVE_ALL, true);
            removeEdgeNotificationInternal(0, extra);
        }

        private void postEdgeNotificationInternal(int id, Bundle extra) {
            try {
                NotificationManager.getService().enqueueEdgeNotification(this.mContext.getPackageName(), this.mContext.getOpPackageName(), id, extra, UserHandle.myUserId());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void removeEdgeNotificationInternal(int id, Bundle extra) {
            try {
                NotificationManager.getService().removeEdgeNotification(this.mContext.getPackageName(), id, extra, UserHandle.myUserId());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Policy implements Parcelable {
        private static final int[] ALL_PRIORITY_CATEGORIES = new int[]{1, 2, 4, 8, 16};
        public static final Creator<Policy> CREATOR = new Creator<Policy>() {
            public Policy createFromParcel(Parcel in) {
                return new Policy(in);
            }

            public Policy[] newArray(int size) {
                return new Policy[size];
            }
        };
        public static final int PRIORITY_CATEGORY_CALLS = 8;
        public static final int PRIORITY_CATEGORY_EVENTS = 2;
        public static final int PRIORITY_CATEGORY_MESSAGES = 4;
        public static final int PRIORITY_CATEGORY_REMINDERS = 1;
        public static final int PRIORITY_CATEGORY_REPEAT_CALLERS = 16;
        public static final int PRIORITY_SENDERS_ANY = 0;
        public static final int PRIORITY_SENDERS_CONTACTS = 1;
        public static final int PRIORITY_SENDERS_STARRED = 2;
        public final int priorityCallSenders;
        public final int priorityCategories;
        public final int priorityMessageSenders;

        public Policy(int priorityCategories, int priorityCallSenders, int priorityMessageSenders) {
            this.priorityCategories = priorityCategories;
            this.priorityCallSenders = priorityCallSenders;
            this.priorityMessageSenders = priorityMessageSenders;
        }

        public Policy(Parcel source) {
            this(source.readInt(), source.readInt(), source.readInt());
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.priorityCategories);
            dest.writeInt(this.priorityCallSenders);
            dest.writeInt(this.priorityMessageSenders);
        }

        public int describeContents() {
            return 0;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(this.priorityCategories), Integer.valueOf(this.priorityCallSenders), Integer.valueOf(this.priorityMessageSenders)});
        }

        public boolean equals(Object o) {
            if (!(o instanceof Policy)) {
                return false;
            }
            if (o == this) {
                return true;
            }
            Policy other = (Policy) o;
            if (other.priorityCategories == this.priorityCategories && other.priorityCallSenders == this.priorityCallSenders && other.priorityMessageSenders == this.priorityMessageSenders) {
                return true;
            }
            return false;
        }

        public String toString() {
            return "NotificationManager.Policy[priorityCategories=" + priorityCategoriesToString(this.priorityCategories) + ",priorityCallSenders=" + prioritySendersToString(this.priorityCallSenders) + ",priorityMessageSenders=" + prioritySendersToString(this.priorityMessageSenders) + "]";
        }

        public static String priorityCategoriesToString(int priorityCategories) {
            if (priorityCategories == 0) {
                return ProxyInfo.LOCAL_EXCL_LIST;
            }
            StringBuilder sb = new StringBuilder();
            for (int priorityCategory : ALL_PRIORITY_CATEGORIES) {
                if ((priorityCategories & priorityCategory) != 0) {
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(priorityCategoryToString(priorityCategory));
                }
                priorityCategories &= priorityCategory ^ -1;
            }
            if (priorityCategories != 0) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append("PRIORITY_CATEGORY_UNKNOWN_").append(priorityCategories);
            }
            return sb.toString();
        }

        private static String priorityCategoryToString(int priorityCategory) {
            switch (priorityCategory) {
                case 1:
                    return "PRIORITY_CATEGORY_REMINDERS";
                case 2:
                    return "PRIORITY_CATEGORY_EVENTS";
                case 4:
                    return "PRIORITY_CATEGORY_MESSAGES";
                case 8:
                    return "PRIORITY_CATEGORY_CALLS";
                case 16:
                    return "PRIORITY_CATEGORY_REPEAT_CALLERS";
                default:
                    return "PRIORITY_CATEGORY_UNKNOWN_" + priorityCategory;
            }
        }

        public static String prioritySendersToString(int prioritySenders) {
            switch (prioritySenders) {
                case 0:
                    return "PRIORITY_SENDERS_ANY";
                case 1:
                    return "PRIORITY_SENDERS_CONTACTS";
                case 2:
                    return "PRIORITY_SENDERS_STARRED";
                default:
                    return "PRIORITY_SENDERS_UNKNOWN_" + prioritySenders;
            }
        }
    }

    public static INotificationManager getService() {
        if (sService != null) {
            return sService;
        }
        sService = Stub.asInterface(ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        return sService;
    }

    NotificationManager(Context context, Handler handler) {
        this.mContext = context;
    }

    public static NotificationManager from(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notify(int id, Notification notification) {
        notify(null, id, notification);
    }

    public void notify(String tag, int id, Notification notification) {
        if (this.mEdgeNotificationManager != null) {
            this.mEdgeNotificationManager.postEdgeNotificationByNormal(id, notification);
        }
        if (!knox_getSealedState() || (knox_getSealedHideNotificationMessages() & 8) == 0) {
            int[] idOut = new int[1];
            INotificationManager service = getService();
            String pkg = this.mContext.getPackageName();
            if (notification.sound != null) {
                notification.sound = notification.sound.getCanonicalUri();
                if (StrictMode.vmFileUriExposureEnabled()) {
                    notification.sound.checkFileUriExposed("Notification.sound");
                }
            }
            fixLegacySmallIcon(notification, pkg);
            if (this.mContext.getApplicationInfo().targetSdkVersion > 22 && notification.getSmallIcon() == null) {
                throw new IllegalArgumentException("Invalid notification (no valid small icon): " + notification);
            } else if ((notification.secFlags & 2) == 0 || (notification.secFlags & 1) == 0) {
                if (localLOGV) {
                    Log.v(TAG, pkg + ": notify(" + id + ", " + notification + ")");
                }
                Notification stripped = notification.clone();
                Builder.stripForDelivery(stripped);
                try {
                    service.enqueueNotificationWithTag(pkg, this.mContext.getOpPackageName(), tag, id, stripped, idOut, UserHandle.myUserId());
                    if (id != idOut[0]) {
                        Log.w(TAG, "notify: id corrupted: sent " + id + ", got back " + idOut[0]);
                    }
                } catch (RemoteException e) {
                }
            } else {
                throw new IllegalArgumentException("Invalid notification (nothing will be shown for this notification): " + notification);
            }
        }
    }

    public void notifyAsUser(String tag, int id, Notification notification, UserHandle user) {
        if (this.mEdgeNotificationManager != null) {
            this.mEdgeNotificationManager.postEdgeNotificationByNormal(id, notification);
        }
        int[] idOut = new int[1];
        INotificationManager service = getService();
        String pkg = this.mContext.getPackageName();
        if (notification.sound != null) {
            notification.sound = notification.sound.getCanonicalUri();
            if (StrictMode.vmFileUriExposureEnabled()) {
                notification.sound.checkFileUriExposed("Notification.sound");
            }
        }
        fixLegacySmallIcon(notification, pkg);
        if (localLOGV) {
            Log.v(TAG, pkg + ": notify(" + id + ", " + notification + ")");
        }
        Notification stripped = notification.clone();
        Builder.stripForDelivery(stripped);
        try {
            service.enqueueNotificationWithTag(pkg, this.mContext.getOpPackageName(), tag, id, stripped, idOut, user.getIdentifier());
            if (id != idOut[0]) {
                Log.w(TAG, "notify: id corrupted: sent " + id + ", got back " + idOut[0]);
            }
        } catch (RemoteException e) {
        }
    }

    private void fixLegacySmallIcon(Notification n, String pkg) {
        if (n.getSmallIcon() == null && n.icon != 0) {
            n.setSmallIcon(Icon.createWithResource(pkg, n.icon));
        }
    }

    public void cancel(int id) {
        cancel(null, id);
    }

    public void cancel(String tag, int id) {
        INotificationManager service = getService();
        String pkg = this.mContext.getPackageName();
        if (localLOGV) {
            Log.v(TAG, pkg + ": cancel(" + id + ")");
        }
        try {
            service.cancelNotificationWithTag(pkg, tag, id, UserHandle.myUserId());
        } catch (RemoteException e) {
        }
        if (this.mEdgeNotificationManager != null) {
            this.mEdgeNotificationManager.removeEdgeNotificationByNormal(id);
        }
    }

    public void cancelAsUser(String tag, int id, UserHandle user) {
        INotificationManager service = getService();
        String pkg = this.mContext.getPackageName();
        if (localLOGV) {
            Log.v(TAG, pkg + ": cancel(" + id + ")");
        }
        try {
            service.cancelNotificationWithTag(pkg, tag, id, user.getIdentifier());
        } catch (RemoteException e) {
        }
        if (this.mEdgeNotificationManager != null) {
            this.mEdgeNotificationManager.removeEdgeNotificationByNormal(id);
        }
    }

    public void cancelAll() {
        INotificationManager service = getService();
        String pkg = this.mContext.getPackageName();
        if (localLOGV) {
            Log.v(TAG, pkg + ": cancelAll()");
        }
        try {
            service.cancelAllNotifications(pkg, UserHandle.myUserId());
        } catch (RemoteException e) {
        }
        if (this.mEdgeNotificationManager != null) {
            this.mEdgeNotificationManager.removeEdgeNotificationAllByNormal();
        }
    }

    public ComponentName getEffectsSuppressor() {
        try {
            return getService().getEffectsSuppressor();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean matchesCallFilter(Bundle extras) {
        try {
            return getService().matchesCallFilter(extras);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean matchesMessageFilter(Bundle extras) {
        try {
            return getService().matchesMessageFilter(extras);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isSystemConditionProviderEnabled(String path) {
        try {
            return getService().isSystemConditionProviderEnabled(path);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setZenMode(int mode, Uri conditionId, String reason) {
        try {
            getService().setZenMode(mode, conditionId, reason);
        } catch (RemoteException e) {
        }
    }

    public boolean setZenModeConfig(ZenModeConfig config, String reason) {
        try {
            return getService().setZenModeConfig(config, reason);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void requestZenModeConditions(IConditionListener listener, int relevance) {
        try {
            getService().requestZenModeConditions(listener, relevance);
        } catch (RemoteException e) {
        }
    }

    public int getZenMode() {
        try {
            return getService().getZenMode();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public ZenModeConfig getZenModeConfig() {
        try {
            return getService().getZenModeConfig();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isNotificationPolicyAccessGranted() {
        try {
            return getService().isNotificationPolicyAccessGranted(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isNotificationPolicyAccessGrantedForPackage(String pkg) {
        try {
            return getService().isNotificationPolicyAccessGrantedForPackage(pkg);
        } catch (RemoteException e) {
            return false;
        }
    }

    public Policy getNotificationPolicy() {
        try {
            return getService().getNotificationPolicy(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setNotificationPolicy(Policy policy) {
        checkRequired("policy", policy);
        try {
            getService().setNotificationPolicy(this.mContext.getOpPackageName(), policy);
        } catch (RemoteException e) {
        }
    }

    public void setNotificationPolicyAccessGranted(String pkg, boolean granted) {
        try {
            getService().setNotificationPolicyAccessGranted(pkg, granted);
        } catch (RemoteException e) {
        }
    }

    public ArraySet<String> getPackagesRequestingNotificationPolicyAccess() {
        try {
            String[] pkgs = getService().getPackagesRequestingNotificationPolicyAccess();
            if (pkgs != null && pkgs.length > 0) {
                ArraySet<String> arraySet = new ArraySet(pkgs.length);
                for (Object add : pkgs) {
                    arraySet.add(add);
                }
                return arraySet;
            }
        } catch (RemoteException e) {
        }
        return new ArraySet();
    }

    private static void checkRequired(String name, Object value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " is required");
        }
    }

    public StatusBarNotification[] getActiveNotifications() {
        try {
            List<StatusBarNotification> list = getService().getAppActiveNotifications(this.mContext.getPackageName(), UserHandle.myUserId()).getList();
            return (StatusBarNotification[]) list.toArray(new StatusBarNotification[list.size()]);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to talk to notification manager. Woe!", e);
            return new StatusBarNotification[0];
        }
    }

    public final int getCurrentInterruptionFilter() {
        try {
            return zenModeToInterruptionFilter(getService().getZenMode());
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to talk to notification manager. Woe!", e);
            return 0;
        }
    }

    public final void setInterruptionFilter(int interruptionFilter) {
        try {
            getService().setInterruptionFilter(this.mContext.getOpPackageName(), interruptionFilter);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to talk to notification manager. Woe!", e);
        }
    }

    public static int zenModeToInterruptionFilter(int zen) {
        switch (zen) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return 0;
        }
    }

    public static int zenModeFromInterruptionFilter(int interruptionFilter, int defValue) {
        switch (interruptionFilter) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            default:
                return defValue;
        }
    }

    public void postEdgeNotification(int id, Bundle extra) {
        if (this.mEdgeNotificationManager != null) {
            this.mEdgeNotificationManager.postEdgeNotification(id, extra);
        }
    }

    public void removeEdgeNotification(int id, Bundle extra) {
        if (this.mEdgeNotificationManager != null) {
            this.mEdgeNotificationManager.removeEdgeNotification(id, extra);
        }
    }

    private boolean knox_getSealedState() {
        KnoxCustomManager knoxCustomManager = EnterpriseDeviceManager.getInstance().getKnoxCustomManager();
        return knoxCustomManager != null && knoxCustomManager.getSealedState();
    }

    private int knox_getSealedHideNotificationMessages() {
        KnoxCustomManager knoxCustomManager = EnterpriseDeviceManager.getInstance().getKnoxCustomManager();
        if (knoxCustomManager != null) {
            return knoxCustomManager.getSealedHideNotificationMessages();
        }
        return 0;
    }
}
