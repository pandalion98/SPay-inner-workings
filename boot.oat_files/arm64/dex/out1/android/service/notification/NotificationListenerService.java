package android.service.notification;

import android.app.INotificationManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.notification.INotificationListener.Stub;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class NotificationListenerService extends Service {
    private static final String EDGE_NOTIFICATION_PROCESS = "com.samsung.android.service.peoplestripe";
    public static final int HINT_HOST_DISABLE_EFFECTS = 1;
    public static final int INTERRUPTION_FILTER_ALARMS = 4;
    public static final int INTERRUPTION_FILTER_ALL = 1;
    public static final int INTERRUPTION_FILTER_NONE = 3;
    public static final int INTERRUPTION_FILTER_PRIORITY = 2;
    public static final int INTERRUPTION_FILTER_UNKNOWN = 0;
    public static final String SERVICE_INTERFACE = "android.service.notification.NotificationListenerService";
    public static final int TRIM_FULL = 0;
    public static final int TRIM_LIGHT = 1;
    private final String TAG = (NotificationListenerService.class.getSimpleName() + "[" + getClass().getSimpleName() + "]");
    private int mCurrentUser;
    private INotificationManager mNoMan;
    private RankingMap mRankingMap;
    private Context mSystemContext;
    private INotificationListenerWrapper mWrapper = null;

    private class INotificationListenerWrapper extends Stub {
        private INotificationListenerWrapper() {
        }

        public void onNotificationPosted(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                try {
                    Builder.rebuild(NotificationListenerService.this.getContext(), sbn.getNotification());
                    NotificationListenerService.this.createLegacyIconExtras(sbn.getNotification());
                } catch (IllegalArgumentException e) {
                    sbn = null;
                    Log.w(NotificationListenerService.this.TAG, "onNotificationPosted: can't rebuild notification from " + sbn.getPackageName());
                }
                synchronized (NotificationListenerService.this.mWrapper) {
                    NotificationListenerService.this.applyUpdate(update);
                    if (sbn != null) {
                        try {
                            NotificationListenerService.this.onNotificationPosted(sbn, NotificationListenerService.this.mRankingMap);
                        } catch (Throwable t) {
                            Log.w(NotificationListenerService.this.TAG, "Error running onNotificationPosted", t);
                        }
                    } else {
                        NotificationListenerService.this.onNotificationRankingUpdate(NotificationListenerService.this.mRankingMap);
                    }
                }
            } catch (RemoteException e2) {
                Log.w(NotificationListenerService.this.TAG, "onNotificationPosted: Error receiving StatusBarNotification", e2);
            }
        }

        public void onNotificationRemoved(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                synchronized (NotificationListenerService.this.mWrapper) {
                    NotificationListenerService.this.applyUpdate(update);
                    try {
                        NotificationListenerService.this.onNotificationRemoved(sbn, NotificationListenerService.this.mRankingMap);
                    } catch (Throwable t) {
                        Log.w(NotificationListenerService.this.TAG, "Error running onNotificationRemoved", t);
                    }
                }
            } catch (RemoteException e) {
                Log.w(NotificationListenerService.this.TAG, "onNotificationRemoved: Error receiving StatusBarNotification", e);
            }
        }

        public void onListenerConnected(NotificationRankingUpdate update) {
            synchronized (NotificationListenerService.this.mWrapper) {
                NotificationListenerService.this.applyUpdate(update);
                try {
                    NotificationListenerService.this.onListenerConnected();
                } catch (Throwable t) {
                    Log.w(NotificationListenerService.this.TAG, "Error running onListenerConnected", t);
                }
            }
        }

        public void onNotificationRankingUpdate(NotificationRankingUpdate update) throws RemoteException {
            synchronized (NotificationListenerService.this.mWrapper) {
                NotificationListenerService.this.applyUpdate(update);
                try {
                    NotificationListenerService.this.onNotificationRankingUpdate(NotificationListenerService.this.mRankingMap);
                } catch (Throwable t) {
                    Log.w(NotificationListenerService.this.TAG, "Error running onNotificationRankingUpdate", t);
                }
            }
        }

        public void onListenerHintsChanged(int hints) throws RemoteException {
            try {
                NotificationListenerService.this.onListenerHintsChanged(hints);
            } catch (Throwable t) {
                Log.w(NotificationListenerService.this.TAG, "Error running onListenerHintsChanged", t);
            }
        }

        public void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {
            try {
                NotificationListenerService.this.onInterruptionFilterChanged(interruptionFilter);
            } catch (Throwable t) {
                Log.w(NotificationListenerService.this.TAG, "Error running onInterruptionFilterChanged", t);
            }
        }

        public void onEdgeNotificationPosted(String pkg, int id, Bundle extra) {
            try {
                NotificationListenerService.this.onEdgeNotificationPosted(pkg, id, extra);
            } catch (Throwable t) {
                Log.w(NotificationListenerService.this.TAG, "Error running onInterruptionFilterChanged", t);
            }
        }

        public void onEdgeNotificationRemoved(String pkg, int id, Bundle extra) {
            try {
                NotificationListenerService.this.onEdgeNotificationRemoved(pkg, id, extra);
            } catch (Throwable t) {
                Log.w(NotificationListenerService.this.TAG, "Error running onInterruptionFilterChanged", t);
            }
        }
    }

    public static class Ranking {
        public static final int VISIBILITY_NO_OVERRIDE = -1000;
        private boolean mIsAmbient;
        private String mKey;
        private boolean mMatchesInterruptionFilter;
        private int mRank = -1;
        private int mVisibilityOverride;

        public String getKey() {
            return this.mKey;
        }

        public int getRank() {
            return this.mRank;
        }

        public boolean isAmbient() {
            return this.mIsAmbient;
        }

        public int getVisibilityOverride() {
            return this.mVisibilityOverride;
        }

        public boolean matchesInterruptionFilter() {
            return this.mMatchesInterruptionFilter;
        }

        private void populate(String key, int rank, boolean isAmbient, boolean matchesInterruptionFilter, int visibilityOverride) {
            this.mKey = key;
            this.mRank = rank;
            this.mIsAmbient = isAmbient;
            this.mMatchesInterruptionFilter = matchesInterruptionFilter;
            this.mVisibilityOverride = visibilityOverride;
        }
    }

    public static class RankingMap implements Parcelable {
        public static final Creator<RankingMap> CREATOR = new Creator<RankingMap>() {
            public RankingMap createFromParcel(Parcel source) {
                return new RankingMap((NotificationRankingUpdate) source.readParcelable(null));
            }

            public RankingMap[] newArray(int size) {
                return new RankingMap[size];
            }
        };
        private ArraySet<Object> mIntercepted;
        private final NotificationRankingUpdate mRankingUpdate;
        private ArrayMap<String, Integer> mRanks;
        private ArrayMap<String, Integer> mVisibilityOverrides;

        private RankingMap(NotificationRankingUpdate rankingUpdate) {
            this.mRankingUpdate = rankingUpdate;
        }

        public String[] getOrderedKeys() {
            return this.mRankingUpdate.getOrderedKeys();
        }

        public boolean getRanking(String key, Ranking outRanking) {
            boolean z;
            int rank = getRank(key);
            boolean isAmbient = isAmbient(key);
            if (isIntercepted(key)) {
                z = false;
            } else {
                z = true;
            }
            outRanking.populate(key, rank, isAmbient, z, getVisibilityOverride(key));
            if (rank >= 0) {
                return true;
            }
            return false;
        }

        private int getRank(String key) {
            synchronized (this) {
                if (this.mRanks == null) {
                    buildRanksLocked();
                }
            }
            Integer rank = (Integer) this.mRanks.get(key);
            return rank != null ? rank.intValue() : -1;
        }

        private boolean isAmbient(String key) {
            int firstAmbientIndex = this.mRankingUpdate.getFirstAmbientIndex();
            if (firstAmbientIndex < 0) {
                return false;
            }
            int rank = getRank(key);
            if (rank < 0 || rank < firstAmbientIndex) {
                return false;
            }
            return true;
        }

        private boolean isIntercepted(String key) {
            synchronized (this) {
                if (this.mIntercepted == null) {
                    buildInterceptedSetLocked();
                }
            }
            return this.mIntercepted.contains(key);
        }

        private int getVisibilityOverride(String key) {
            synchronized (this) {
                if (this.mVisibilityOverrides == null) {
                    buildVisibilityOverridesLocked();
                }
            }
            Integer overide = (Integer) this.mVisibilityOverrides.get(key);
            if (overide == null) {
                return -1000;
            }
            return overide.intValue();
        }

        private void buildRanksLocked() {
            String[] orderedKeys = this.mRankingUpdate.getOrderedKeys();
            this.mRanks = new ArrayMap(orderedKeys.length);
            for (int i = 0; i < orderedKeys.length; i++) {
                this.mRanks.put(orderedKeys[i], Integer.valueOf(i));
            }
        }

        private void buildInterceptedSetLocked() {
            String[] dndInterceptedKeys = this.mRankingUpdate.getInterceptedKeys();
            this.mIntercepted = new ArraySet(dndInterceptedKeys.length);
            Collections.addAll(this.mIntercepted, dndInterceptedKeys);
        }

        private void buildVisibilityOverridesLocked() {
            Bundle visibilityBundle = this.mRankingUpdate.getVisibilityOverrides();
            this.mVisibilityOverrides = new ArrayMap(visibilityBundle.size());
            for (String key : visibilityBundle.keySet()) {
                this.mVisibilityOverrides.put(key, Integer.valueOf(visibilityBundle.getInt(key)));
            }
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.mRankingUpdate, flags);
        }
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        onNotificationPosted(sbn);
    }

    public void onEdgeNotificationPosted(String pkg, int id, Bundle extra) {
    }

    public void onEdgeNotificationRemoved(String pkg, int id, Bundle extra) {
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        onNotificationRemoved(sbn);
    }

    public void onListenerConnected() {
    }

    public void onNotificationRankingUpdate(RankingMap rankingMap) {
    }

    public void onListenerHintsChanged(int hints) {
    }

    public void onInterruptionFilterChanged(int interruptionFilter) {
    }

    private final INotificationManager getNotificationInterface() {
        if (this.mNoMan == null) {
            this.mNoMan = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        }
        return this.mNoMan;
    }

    public final void cancelNotification(String pkg, String tag, int id) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationFromListener(this.mWrapper, pkg, tag, id);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void cancelNotification(String key) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationsFromListener(this.mWrapper, new String[]{key});
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void cancelAllNotifications() {
        cancelNotifications(null);
    }

    public final void cancelNotifications(String[] keys) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationsFromListener(this.mWrapper, keys);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void setNotificationsShown(String[] keys) {
        if (isBound()) {
            try {
                getNotificationInterface().setNotificationsShownFromListener(this.mWrapper, keys);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void setOnNotificationPostedTrim(int trim) {
        if (isBound()) {
            try {
                getNotificationInterface().setOnNotificationPostedTrimFromListener(this.mWrapper, trim);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public StatusBarNotification[] getActiveNotifications() {
        return getActiveNotifications(null, 0);
    }

    public StatusBarNotification[] getActiveNotifications(int trim) {
        return getActiveNotifications(null, trim);
    }

    public StatusBarNotification[] getActiveNotifications(String[] keys) {
        return getActiveNotifications(keys, 0);
    }

    public StatusBarNotification[] getActiveNotifications(String[] keys, int trim) {
        if (!isBound()) {
            return null;
        }
        try {
            List<StatusBarNotification> list = getNotificationInterface().getActiveNotificationsFromListener(this.mWrapper, keys, trim).getList();
            ArrayList<StatusBarNotification> corruptNotifications = null;
            int N = list.size();
            for (int i = 0; i < N; i++) {
                StatusBarNotification sbn = (StatusBarNotification) list.get(i);
                Notification notification = sbn.getNotification();
                try {
                    Builder.rebuild(getContext(), notification);
                    createLegacyIconExtras(notification);
                } catch (IllegalArgumentException e) {
                    if (corruptNotifications == null) {
                        corruptNotifications = new ArrayList(N);
                    }
                    corruptNotifications.add(sbn);
                    Log.w(this.TAG, "onNotificationPosted: can't rebuild notification from " + sbn.getPackageName());
                }
            }
            if (corruptNotifications != null) {
                list.removeAll(corruptNotifications);
            }
            return (StatusBarNotification[]) list.toArray(new StatusBarNotification[list.size()]);
        } catch (RemoteException ex) {
            Log.v(this.TAG, "Unable to contact notification manager", ex);
            return null;
        }
    }

    public final int getCurrentListenerHints() {
        int i = 0;
        if (isBound()) {
            try {
                i = getNotificationInterface().getHintsFromListener(this.mWrapper);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
        return i;
    }

    public final int getCurrentInterruptionFilter() {
        int i = 0;
        if (isBound()) {
            try {
                i = getNotificationInterface().getInterruptionFilterFromListener(this.mWrapper);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
        return i;
    }

    public final void requestListenerHints(int hints) {
        if (isBound()) {
            try {
                getNotificationInterface().requestHintsFromListener(this.mWrapper, hints);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void requestInterruptionFilter(int interruptionFilter) {
        if (isBound()) {
            try {
                getNotificationInterface().requestInterruptionFilterFromListener(this.mWrapper, interruptionFilter);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public RankingMap getCurrentRanking() {
        return this.mRankingMap;
    }

    public IBinder onBind(Intent intent) {
        if (this.mWrapper == null) {
            this.mWrapper = new INotificationListenerWrapper();
        }
        return this.mWrapper;
    }

    private boolean isBound() {
        if (this.mWrapper != null) {
            return true;
        }
        Log.w(this.TAG, "Notification listener service not yet bound.");
        return false;
    }

    public void registerAsSystemService(Context context, ComponentName componentName, int currentUser) throws RemoteException {
        this.mSystemContext = context;
        if (this.mWrapper == null) {
            this.mWrapper = new INotificationListenerWrapper();
        }
        getNotificationInterface().registerListener(this.mWrapper, componentName, currentUser);
        this.mCurrentUser = currentUser;
    }

    public void unregisterAsSystemService() throws RemoteException {
        if (this.mWrapper != null) {
            getNotificationInterface().unregisterListener(this.mWrapper, this.mCurrentUser);
        }
    }

    private void createLegacyIconExtras(Notification n) {
        Icon smallIcon = n.getSmallIcon();
        Icon largeIcon = n.getLargeIcon();
        if (smallIcon != null && smallIcon.getType() == 2) {
            n.extras.putInt("android.icon", smallIcon.getResId());
            n.icon = smallIcon.getResId();
        }
        if (largeIcon != null) {
            Drawable d = largeIcon.loadDrawable(getContext());
            if (d != null && (d instanceof BitmapDrawable)) {
                Bitmap largeIconBits = ((BitmapDrawable) d).getBitmap();
                n.extras.putParcelable("android.largeIcon", largeIconBits);
                n.largeIcon = largeIconBits;
            }
        }
    }

    private void applyUpdate(NotificationRankingUpdate update) {
        this.mRankingMap = new RankingMap(update);
    }

    private Context getContext() {
        if (this.mSystemContext != null) {
            return this.mSystemContext;
        }
        return this;
    }
}
