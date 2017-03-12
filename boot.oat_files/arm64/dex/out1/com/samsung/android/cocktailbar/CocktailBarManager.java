package com.samsung.android.cocktailbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.secutil.Log;
import android.view.DragEvent;
import android.widget.RemoteViews;
import com.android.internal.R;
import com.samsung.android.cocktailbar.CocktailInfo.Builder;
import com.samsung.android.cocktailbar.ICocktailBarFeedsCallback.Stub;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CocktailBarManager {
    public static final String ACTION_COCKTAIL_BAR_COCKTAIL_UNINSTALLED = "com.samsung.android.app.cocktailbarservice.action.COCKTAIL_BAR_COCKTAIL_UNINSTALLED";
    @Deprecated
    public static final String ACTION_COCKTAIL_BAR_WAKE_UP_STATE = "com.samsung.android.app.cocktailbarservice.action.COCKTAIL_BAR_WAKE_UP_STATE";
    public static final String ACTION_COCKTAIL_DISABLED = "com.samsung.android.cocktail.action.COCKTAIL_DISABLED";
    public static final String ACTION_COCKTAIL_DROPED = "com.samsung.android.cocktail.action.COCKTAIL_DROPED";
    public static final String ACTION_COCKTAIL_ENABLED = "com.samsung.android.cocktail.action.COCKTAIL_ENABLED";
    public static final String ACTION_COCKTAIL_UPDATE = "com.samsung.android.cocktail.action.COCKTAIL_UPDATE";
    public static final String ACTION_COCKTAIL_UPDATE_FEEDS = "com.samsung.android.cocktail.action.COCKTAIL_UPDATE_FEEDS";
    public static final String ACTION_COCKTAIL_UPDATE_V2 = "com.samsung.android.cocktail.v2.action.COCKTAIL_UPDATE";
    public static final String ACTION_COCKTAIL_VISIBILITY_CHANGED = "com.samsung.android.cocktail.action.COCKTAIL_VISIBILITY_CHANGED";
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL = 65536;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_CALLING = 65538;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_COMMAND = 65543;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_HEADSET = 65541;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_INCOMING_CALL = 65537;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_NOTIFICATION = 65540;
    @Deprecated
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_SPEN = 65542;
    public static final int COCKTAIL_CATEGORY_CONTEXTUAL_TICKER = 65539;
    public static final int COCKTAIL_CATEGORY_GLOBAL = 1;
    public static final int COCKTAIL_DISPLAY_POLICY_ALL = 159;
    public static final int COCKTAIL_DISPLAY_POLICY_GENERAL = 1;
    public static final int COCKTAIL_DISPLAY_POLICY_INDEX_MODE = 16;
    public static final int COCKTAIL_DISPLAY_POLICY_LOCKSCREEN = 2;
    public static final int COCKTAIL_DISPLAY_POLICY_NOT_PROVISION = 128;
    public static final int COCKTAIL_DISPLAY_POLICY_SCOVER = 4;
    public static final int COCKTAIL_DISPLAY_POLICY_TABLE_MODE = 8;
    public static final int COCKTAIL_VISIBILITY_HIDE = 2;
    public static final int COCKTAIL_VISIBILITY_SHOW = 1;
    @Deprecated
    public static final String EXTRA_COCKTAIL_BAR_WAKE_UP_STATE = "cocktailbarWakeupState";
    public static final String EXTRA_COCKTAIL_ID = "cocktailId";
    public static final String EXTRA_COCKTAIL_IDS = "cocktailIds";
    @Deprecated
    public static final String EXTRA_COCKTAIL_PROVIDER = "cocktailProvider";
    public static final String EXTRA_COCKTAIL_VISIBILITY = "cocktailVisibility";
    public static final String EXTRA_DRAG_EVENT = "com.samsung.android.intent.extra.DRAG_EVENT";
    public static final int INVALID_COCKTAIL_ID = 0;
    public static final String META_DATA_COCKTAIL_PROVIDER = "com.samsung.android.cocktail.provider";
    private static final String TAG = "CocktailBarManager";
    public static final int TYPE_WAKEUP_GESTURE_PICKUP = 1;
    public static final int TYPE_WAKEUP_GESTURE_RUB = 2;
    private final CopyOnWriteArrayList<CocktailBarFeedsListenerDelegate> mCocktailBarFeedsListenerDelegates = new CopyOnWriteArrayList();
    private int mCocktailBarSize = -1;
    private final CopyOnWriteArrayList<CocktailBarStateListenerDelegate> mCocktailBarStateListenerDelegates = new CopyOnWriteArrayList();
    private Context mContext;
    private Object mFeedsListnerDelegatesLock = new Object();
    private final String mPackageName;
    private ICocktailBarService mService;
    private Object mStateListnerDelegatesLock = new Object();

    public static class CocktailBarFeedsListener {
        public void onFeedsUpdated(int cocktailId, List<FeedsInfo> list) {
        }
    }

    private class CocktailBarFeedsListenerDelegate extends Stub {
        private static final int MSG_LISTEN_COCKTAIL_BAR_FEED_UPDATED = 0;
        private Handler mHandler;
        private final CocktailBarFeedsListener mListener;

        public CocktailBarFeedsListenerDelegate(CocktailBarFeedsListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = new Handler(handler == null ? CocktailBarManager.this.mContext.getMainLooper() : handler.getLooper(), CocktailBarManager.this) {
                public void handleMessage(Message msg) {
                    if (CocktailBarFeedsListenerDelegate.this.mListener != null) {
                        switch (msg.what) {
                            case 0:
                                CocktailBarFeedsListenerDelegate.this.mListener.onFeedsUpdated(msg.arg1, (List) msg.obj);
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        public CocktailBarFeedsListener getListener() {
            return this.mListener;
        }

        public void onFeedsUpdated(int cocktailId, List<FeedsInfo> feedsInfoList) throws RemoteException {
            Message.obtain(this.mHandler, 0, cocktailId, 0, feedsInfoList).sendToTarget();
        }
    }

    public static class CocktailBarStateListener {
        public void onCocktailBarStateChanged(CocktailBarStateInfo stateInfo) {
        }

        public void onCocktailBarVisibilityChanged(int visibility) {
        }

        public void onCocktailBarBackgroundTypeChanged(int bgType) {
        }

        public void onCocktailBarPositionChanged(int position) {
        }

        public void onCocktailBarWindowTypeChanged(int windowType) {
        }
    }

    private class CocktailBarStateListenerDelegate extends ICocktailBarStateCallback.Stub {
        private static final int MSG_LISTEN_COCKTAIL_BAR_STATE_CHANGE = 0;
        private Handler mHandler;
        private final CocktailBarStateListener mListener;

        public CocktailBarStateListenerDelegate(CocktailBarStateListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = new Handler(handler == null ? CocktailBarManager.this.mContext.getMainLooper() : handler.getLooper(), CocktailBarManager.this) {
                public void handleMessage(Message msg) {
                    if (CocktailBarStateListenerDelegate.this.mListener != null) {
                        switch (msg.what) {
                            case 0:
                                CocktailBarStateInfo stateInfo = msg.obj;
                                if (stateInfo.changeFlag != 0) {
                                    CocktailBarStateListenerDelegate.this.mListener.onCocktailBarStateChanged(stateInfo);
                                    if ((stateInfo.changeFlag & 1) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarVisibilityChanged(stateInfo.visibility);
                                    }
                                    if ((stateInfo.changeFlag & 2) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarBackgroundTypeChanged(stateInfo.backgroundType);
                                    }
                                    if ((stateInfo.changeFlag & 4) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarPositionChanged(stateInfo.position);
                                    }
                                    if ((stateInfo.changeFlag & 128) != 0) {
                                        CocktailBarStateListenerDelegate.this.mListener.onCocktailBarWindowTypeChanged(stateInfo.windowType);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        public CocktailBarStateListener getListener() {
            return this.mListener;
        }

        public void onCocktailBarStateChanged(CocktailBarStateInfo stateInfo) throws RemoteException {
            Message.obtain(this.mHandler, 0, stateInfo).sendToTarget();
        }
    }

    public interface States {
        public static final int COCKTAIL_BAR_BACKGROUND_DIM = 2;
        public static final int COCKTAIL_BAR_BACKGROUND_OPAQUE = 1;
        public static final int COCKTAIL_BAR_BACKGROUND_TRANSPARENT = 3;
        public static final int COCKTAIL_BAR_BACKGROUND_UNKNOWN = 0;
        public static final int COCKTAIL_BAR_FULLSCREEN_TYPE = 2;
        public static final int COCKTAIL_BAR_LOCK_HIDE = 2;
        public static final int COCKTAIL_BAR_LOCK_NONE = 0;
        public static final int COCKTAIL_BAR_LOCK_RESTRICT = 4;
        public static final int COCKTAIL_BAR_LOCK_SHOW = 1;
        public static final int COCKTAIL_BAR_MINIMIZE_TYPE = 1;
        public static final int COCKTAIL_BAR_MODE_IMMERSIVE = 2;
        public static final int COCKTAIL_BAR_MODE_MULTITASKING = 1;
        public static final int COCKTAIL_BAR_MODE_UNKNOWN = 0;
        public static final int COCKTAIL_BAR_POSITION_BOTTOM = 4;
        public static final int COCKTAIL_BAR_POSITION_LEFT = 1;
        public static final int COCKTAIL_BAR_POSITION_RIGHT = 2;
        public static final int COCKTAIL_BAR_POSITION_TOP = 3;
        public static final int COCKTAIL_BAR_POSITION_UNKNOWN = 0;
        public static final int COCKTAIL_BAR_STATE_INVISIBLE = 2;
        public static final int COCKTAIL_BAR_STATE_VISIBLE = 1;
        public static final int COCKTAIL_BAR_UNKNOWN_TYPE = 0;
    }

    public interface SysFs {
        public static final int SYSFS_DEADZONE_ALL = 3;
        public static final int SYSFS_DEADZONE_CLEAR = 6;
        public static final int SYSFS_DEADZONE_LEFT = 1;
        public static final int SYSFS_DEADZONE_OFF = 0;
        public static final int SYSFS_DEADZONE_RIGHT = 2;
    }

    public interface WakeUp {
        public static final int REASON_BY_DISMISS_KEYGUARD = 3;
        public static final int REASON_BY_NONE = 0;
        public static final int REASON_BY_POWER_MANAGER = 4;
        public static final int REASON_BY_SCREEN_TURN_ON = 2;
        public static final int REASON_BY_WINDOW_POLICY = 1;
    }

    public interface WindowTypes {
        public static final int WINDOW_TYPE_COCKTAIL_BAR_BACKGROUND = 8;
        public static final int WINDOW_TYPE_IMMERSIVE = 2;
        public static final int WINDOW_TYPE_INPUT_METHOD = 4;
        public static final int WINDOW_TYPE_KEYGUARD = 5;
        public static final int WINDOW_TYPE_NORMAL = 1;
        public static final int WINDOW_TYPE_POPUP = 6;
        public static final int WINDOW_TYPE_RESERVE = 4096;
        public static final int WINDOW_TYPE_SCOVER = 7;
        public static final int WINDOW_TYPE_STATUS_BAR = 3;
    }

    public static CocktailBarManager getInstance(Context context) {
        return (CocktailBarManager) context.getSystemService("CocktailBarService");
    }

    public CocktailBarManager(Context context, ICocktailBarService service) {
        this.mContext = context;
        this.mPackageName = context.getOpPackageName();
        this.mService = service;
    }

    public Context getContext() {
        return this.mContext;
    }

    @Deprecated
    public int getCocktailId(ComponentName provider) {
        if (getService() == null || provider == null) {
            return 0;
        }
        try {
            return this.mService.getCocktailId(this.mPackageName, provider);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    private ICocktailBarService getService() {
        if (this.mService == null) {
            this.mService = ICocktailBarService.Stub.asInterface(ServiceManager.getService("CocktailBarService"));
        }
        return this.mService;
    }

    public int[] getCocktailIds(ComponentName provider) {
        if (getService() == null || provider == null) {
            return new int[]{0};
        }
        try {
            return this.mService.getCocktailIds(this.mPackageName, provider);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean isBoundCocktailPackage(String packageNamae, int userId) {
        if (getService() == null || packageNamae == null) {
            return false;
        }
        try {
            return this.mService.isBoundCocktailPackage(packageNamae, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean isEnabledCocktail(ComponentName provider) {
        if (getService() == null || provider == null) {
            return false;
        }
        try {
            return this.mService.isEnabledCocktail(this.mPackageName, provider);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void updateCocktail(int cocktailId, int displayPolicy, int category, RemoteViews contentView, Bundle contentInfo) {
        if (getService() != null) {
            try {
                this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(displayPolicy).setCategory(category).setContentView(contentView).setContentInfo(contentInfo).build(), cocktailId);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktail(int cocktailId, int displayPolicy, int category, RemoteViews contentView, RemoteViews helpView, Bundle contentInfo) {
        if (getService() != null) {
            try {
                this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(displayPolicy).setCategory(category).setContentView(contentView).setHelpView(helpView).setContentInfo(contentInfo).build(), cocktailId);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktail(int cocktailId, int displayPolicy, int category, RemoteViews contentView, RemoteViews helpView, Bundle contentInfo, ComponentName classInfo) {
        if (getService() != null) {
            try {
                this.mService.updateCocktail(this.mPackageName, new Builder(this.mContext).setOrientation(this.mContext.getResources().getConfiguration().orientation).setDiplayPolicy(displayPolicy).setCategory(category).setContentView(contentView).setHelpView(helpView).setContentInfo(contentInfo).setClassloader(classInfo).build(), cocktailId);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void partiallyUpdateCocktail(int cocktailId, RemoteViews contentView) {
        if (getService() != null) {
            try {
                this.mService.partiallyUpdateCocktail(this.mPackageName, contentView, cocktailId);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void partiallyUpdateHelpView(int cocktailId, RemoteViews helpViews) {
        if (getService() != null) {
            try {
                this.mService.partiallyUpdateHelpView(this.mPackageName, helpViews, cocktailId);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void showCocktail(int cocktailId) {
        if (getService() != null) {
            try {
                this.mService.showCocktail(this.mPackageName, cocktailId);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void closeCocktail(int cocktailId, int catetory) {
        if (getService() != null) {
            try {
                this.mService.closeCocktail(this.mPackageName, cocktailId, catetory);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void notifyCocktailViewDataChanged(int cocktailId, int viewId) {
        if (getService() != null) {
            try {
                this.mService.notifyCocktailViewDataChanged(this.mPackageName, cocktailId, viewId);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateFeeds(int cocktailId, List<FeedsInfo> feedsInfoList) {
        if (getService() != null) {
            if (feedsInfoList == null) {
                Log.e(TAG, "updateFeeds : feedsInfoList is null");
                return;
            }
            try {
                this.mService.updateFeeds(this.mPackageName, cocktailId, feedsInfoList);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void setEnabledCocktailIds(int[] cocktailIds) {
        if (getService() != null) {
            try {
                this.mService.setEnabledCocktailIds(cocktailIds);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public int[] getEnabledCocktailIds() {
        if (getService() == null) {
            return null;
        }
        try {
            return this.mService.getEnabledCocktailIds();
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void disableCocktail(ComponentName provider) {
        if (getService() != null) {
            try {
                this.mService.disableCocktail(this.mPackageName, provider);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public int[] getAllCocktailIds() {
        if (getService() == null) {
            return null;
        }
        try {
            return this.mService.getAllCocktailIds();
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public Cocktail getCocktail(int cocktailId) {
        if (getService() == null) {
            return null;
        }
        try {
            return this.mService.getCocktail(cocktailId);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean requestToUpdateCocktail(int cocktailId) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToUpdateCocktail(cocktailId);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean requestToDisableCocktail(int cocktailId) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToDisableCocktail(cocktailId);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean requestToUpdateCocktailByCategory(int category) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToUpdateCocktailByCategory(category);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public boolean requestToDisableCocktailByCategory(int category) {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.requestToDisableCocktailByCategory(category);
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void notifyKeyguardState(boolean enable) {
        if (getService() != null) {
            try {
                this.mService.notifyKeyguardState(enable);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void setDisableTickerView(int state) {
        if (getService() != null) {
            try {
                this.mService.setDisableTickerView(state);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void notifyCocktailVisibiltyChanged(int cocktailId, int visibility) {
        if (getService() != null) {
            long identityToken = Binder.clearCallingIdentity();
            try {
                this.mService.notifyCocktailVisibiltyChanged(cocktailId, visibility);
                Binder.restoreCallingIdentity(identityToken);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(identityToken);
            }
        }
    }

    public void sendDragEvent(int cocktailId, DragEvent event) {
        if (getService() != null) {
            long identityToken = Binder.clearCallingIdentity();
            try {
                this.mService.sendDragEvent(cocktailId, event);
                Binder.restoreCallingIdentity(identityToken);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(identityToken);
            }
        }
    }

    public boolean isAllowTransientBarCocktailBar() {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.isAllowTransientBarCocktailBar();
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void bindRemoteViewsService(String packageName, int cocktailId, Intent intent, IBinder connection) {
        if (getService() != null) {
            try {
                this.mService.bindRemoteViewsService(packageName, cocktailId, intent, connection);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void unbindRemoteViewsService(String packageName, int cocktailId, Intent intent) {
        if (getService() != null) {
            try {
                this.mService.unbindRemoteViewsService(packageName, cocktailId, intent);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public int getCocktailBarSize() {
        if (this.mCocktailBarSize < 0) {
            this.mCocktailBarSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.cocktail_bar_size);
        }
        return this.mCocktailBarSize;
    }

    public void updateWakeupGesture(int gestureType, boolean bEnable) {
        if (getService() != null) {
            try {
                this.mService.updateWakeupGesture(gestureType, bEnable);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateWakeupArea(int area) {
        if (getService() != null) {
            try {
                this.mService.updateWakeupArea(area);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateLongpressGesture(boolean bEnable) {
        if (getService() != null) {
            try {
                this.mService.updateLongpressGesture(bEnable);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateSysfsDeadZone(int deadzone) {
        if (getService() != null) {
            try {
                this.mService.updateSysfsDeadZone(deadzone);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateSysfsBarLength(int barLength) {
        if (getService() != null) {
            try {
                this.mService.updateSysfsBarLength(barLength);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateSysfsGripDisable(boolean bDisable) {
        if (getService() != null) {
            try {
                this.mService.updateSysfsGripDisable(bDisable);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void wakeupCocktailBar(boolean bEnable, int keyCode, int reason) {
        if (getService() != null) {
            try {
                this.mService.wakeupCocktailBar(bEnable, keyCode, reason);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void setCocktailBarWakeUpState(boolean enable) {
        if (getService() != null) {
            try {
                this.mService.setCocktailBarWakeUpState(enable);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public boolean getCocktaiBarWakeUpState() {
        if (getService() == null) {
            return false;
        }
        try {
            return this.mService.getCocktaiBarWakeUpState();
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    @Deprecated
    public boolean isCocktailBarShifted() {
        return false;
    }

    public boolean isImmersiveMode() {
        if (getService() == null) {
            return false;
        }
        try {
            if (this.mService.getWindowType() == 2) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void switchDefaultCocktail() {
        if (getService() != null) {
            try {
                this.mService.switchDefaultCocktail();
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void sendExtraDataToCocktailBar(Bundle extraData) {
        if (getService() != null) {
            try {
                this.mService.sendExtraDataToCocktailBar(extraData);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void removeCocktailUIService() {
        if (getService() != null) {
            try {
                this.mService.removeCocktailUIService();
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void cocktailBarshutdown() {
        if (getService() != null) {
            try {
                this.mService.cocktailBarshutdown(this.mPackageName);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void cocktailBarreboot() {
        if (getService() != null) {
            try {
                this.mService.cocktailBarreboot(this.mPackageName);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public int getCocktailBarVisibility() {
        if (getService() == null) {
            return 2;
        }
        try {
            return this.mService.getCocktailBarVisibility();
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public int getCocktailBarWindowType() {
        if (getService() == null) {
            return 0;
        }
        try {
            return this.mService.getCocktailBarStateInfo().windowType;
        } catch (RemoteException e) {
            throw new RuntimeException("CocktailBarService dead?", e);
        }
    }

    public void showAndLockCocktailBar() {
        if (getService() != null) {
            try {
                this.mService.showAndLockCocktailBar();
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void unlockCocktailBar(int visibility) {
        if (getService() != null) {
            try {
                this.mService.unlockCocktailBar(visibility);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void activateCocktailBar() {
        if (getService() != null) {
            try {
                this.mService.activateCocktailBar();
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void deactivateCocktailBar() {
        if (getService() != null) {
            try {
                this.mService.deactivateCocktailBar();
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktailBarVisibility(int visibility) {
        if (getService() != null) {
            try {
                this.mService.updateCocktailBarVisibility(visibility);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktailBarStateFromSystem(int windowType) {
        if (getService() != null) {
            try {
                this.mService.updateCocktailBarStateFromSystem(windowType);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void setCocktailBarStatus(boolean shift, boolean transparent) {
        if (getService() != null) {
            try {
                this.mService.setCocktailBarStatus(shift, transparent);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktailBarPosition(int position) {
        if (getService() != null) {
            try {
                this.mService.updateCocktailBarPosition(position);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void updateCocktailBarWindowType(int windowType) {
        if (getService() != null) {
            try {
                this.mService.updateCocktailBarWindowType(this.mContext.getPackageName(), windowType);
            } catch (RemoteException e) {
                throw new RuntimeException("CocktailBarService dead?", e);
            }
        }
    }

    public void registerListener(CocktailBarStateListener listener) {
        Throwable th;
        if (getService() != null) {
            if (listener == null) {
                Log.w(TAG, "registerListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                try {
                    CocktailBarStateListenerDelegate listenerDelegate;
                    CocktailBarStateListenerDelegate listenerDelegate2;
                    Iterator<CocktailBarStateListenerDelegate> i = this.mCocktailBarStateListenerDelegates.iterator();
                    while (i.hasNext()) {
                        CocktailBarStateListenerDelegate temp = (CocktailBarStateListenerDelegate) i.next();
                        if (temp.getListener().equals(listener)) {
                            listenerDelegate = temp;
                            break;
                        }
                    }
                    listenerDelegate = null;
                    if (listenerDelegate == null) {
                        try {
                            listenerDelegate2 = new CocktailBarStateListenerDelegate(listener, null);
                            this.mCocktailBarStateListenerDelegates.add(listenerDelegate2);
                        } catch (Throwable th2) {
                            th = th2;
                            listenerDelegate2 = listenerDelegate;
                            throw th;
                        }
                    }
                    listenerDelegate2 = listenerDelegate;
                    ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(listenerDelegate2 == null || cm == null)) {
                        this.mService.registerCocktailBarStateListenerCallback(listenerDelegate2, cm);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "registerListener : RemoteException : ", e);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
    }

    public void unregisterListener(CocktailBarStateListener listener) {
        if (getService() != null) {
            if (listener == null) {
                Log.w(TAG, "unregisterListener : listener is null");
                return;
            }
            synchronized (this.mStateListnerDelegatesLock) {
                CocktailBarStateListenerDelegate listenerDelegate = null;
                Iterator<CocktailBarStateListenerDelegate> i = this.mCocktailBarStateListenerDelegates.iterator();
                while (i.hasNext()) {
                    CocktailBarStateListenerDelegate temp = (CocktailBarStateListenerDelegate) i.next();
                    if (temp.getListener().equals(listener)) {
                        listenerDelegate = temp;
                        break;
                    }
                }
                if (listenerDelegate == null) {
                    Log.w(TAG, "unregisterListener : cannot find the listener");
                    return;
                }
                try {
                    this.mService.unregisterCocktailBarStateListenerCallback(listenerDelegate);
                    this.mCocktailBarStateListenerDelegates.remove(listenerDelegate);
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterListener : RemoteException : ", e);
                }
            }
        }
    }

    public void registerOnFeedsUpdatedListener(CocktailBarFeedsListener listener) {
        CocktailBarFeedsListenerDelegate listenerDelegate;
        Throwable th;
        if (getService() != null) {
            if (listener == null) {
                Log.w(TAG, "registerOnFeedsUpdatedListener : listener is null");
                return;
            }
            synchronized (this.mFeedsListnerDelegatesLock) {
                try {
                    CocktailBarFeedsListenerDelegate listenerDelegate2;
                    Iterator<CocktailBarFeedsListenerDelegate> i = this.mCocktailBarFeedsListenerDelegates.iterator();
                    while (i.hasNext()) {
                        CocktailBarFeedsListenerDelegate temp = (CocktailBarFeedsListenerDelegate) i.next();
                        if (temp.getListener().equals(listener)) {
                            listenerDelegate = temp;
                            break;
                        }
                    }
                    listenerDelegate = null;
                    if (listenerDelegate == null) {
                        try {
                            listenerDelegate2 = new CocktailBarFeedsListenerDelegate(listener, null);
                            this.mCocktailBarFeedsListenerDelegates.add(listenerDelegate2);
                        } catch (Throwable th2) {
                            th = th2;
                            listenerDelegate2 = listenerDelegate;
                            throw th;
                        }
                    }
                    listenerDelegate2 = listenerDelegate;
                    ComponentName cm = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (!(listenerDelegate2 == null || cm == null)) {
                        this.mService.registerCocktailBarFeedsListenerCallback(listenerDelegate2, cm);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "registerOnFeedsUpdatedListener : RemoteException : ", e);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
            }
        }
    }

    public void unregisterOnFeedsUpdatedListener(CocktailBarFeedsListener listener) {
        if (getService() != null) {
            if (listener == null) {
                Log.w(TAG, "unregisterOnFeedsUpdatedListener : listener is null");
                return;
            }
            synchronized (this.mFeedsListnerDelegatesLock) {
                CocktailBarFeedsListenerDelegate listenerDelegate = null;
                Iterator<CocktailBarFeedsListenerDelegate> i = this.mCocktailBarFeedsListenerDelegates.iterator();
                while (i.hasNext()) {
                    CocktailBarFeedsListenerDelegate temp = (CocktailBarFeedsListenerDelegate) i.next();
                    if (temp.getListener().equals(listener)) {
                        listenerDelegate = temp;
                        break;
                    }
                }
                if (listenerDelegate == null) {
                    Log.w(TAG, "unregisterOnFeedsUpdatedListener : cannot find the listener");
                    return;
                }
                try {
                    this.mService.unregisterCocktailBarFeedsListenerCallback(listenerDelegate);
                    this.mCocktailBarFeedsListenerDelegates.remove(listenerDelegate);
                } catch (RemoteException e) {
                    Log.e(TAG, "unregisterOnFeedsUpdatedListener : RemoteException : ", e);
                }
            }
        }
    }

    public void registerClient(IBinder client, ComponentName name) {
        if (getService() != null) {
            if (name == null || client == null) {
                Log.w(TAG, "registerClient : name or client is null");
                return;
            }
            try {
                this.mService.registerClient(client, name);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterClient(IBinder client) {
        if (getService() != null) {
            if (client == null) {
                Log.w(TAG, "unregisterClient : client is null");
                return;
            }
            try {
                this.mService.unregisterClient(client);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
