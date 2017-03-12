package com.android.internal.statusbar;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IStatusBarService extends IInterface {

    public static abstract class Stub extends Binder implements IStatusBarService {
        private static final String DESCRIPTOR = "com.android.internal.statusbar.IStatusBarService";
        static final int TRANSACTION_appTransitionCancelled = 38;
        static final int TRANSACTION_appTransitionPending = 37;
        static final int TRANSACTION_appTransitionStarting = 39;
        static final int TRANSACTION_applyEDMPolicy = 42;
        static final int TRANSACTION_cancelPreloadRecentApps = 36;
        static final int TRANSACTION_clearNotificationEffects = 20;
        static final int TRANSACTION_collapsePanels = 2;
        static final int TRANSACTION_disable = 3;
        static final int TRANSACTION_disable2 = 5;
        static final int TRANSACTION_disable2ForUser = 6;
        static final int TRANSACTION_disableAsUser = 41;
        static final int TRANSACTION_disableForUser = 4;
        static final int TRANSACTION_enableSignals = 52;
        static final int TRANSACTION_expandNotificationsPanel = 1;
        static final int TRANSACTION_expandSettingsPanel = 14;
        static final int TRANSACTION_getDisableFlags = 7;
        static final int TRANSACTION_getPanelExpandState = 45;
        static final int TRANSACTION_getQuickSettingPanelExpandState = 51;
        static final int TRANSACTION_hideRecentApps = 33;
        static final int TRANSACTION_notifyRecentPanelVisiblity = 48;
        static final int TRANSACTION_onClearAllNotifications = 24;
        static final int TRANSACTION_onNotificationActionClick = 22;
        static final int TRANSACTION_onNotificationClear = 25;
        static final int TRANSACTION_onNotificationClearWithReason = 49;
        static final int TRANSACTION_onNotificationClick = 21;
        static final int TRANSACTION_onNotificationError = 23;
        static final int TRANSACTION_onNotificationExpansionChanged = 27;
        static final int TRANSACTION_onNotificationVisibilityChanged = 26;
        static final int TRANSACTION_onPanelHidden = 19;
        static final int TRANSACTION_onPanelRevealed = 18;
        static final int TRANSACTION_preloadRecentApps = 35;
        static final int TRANSACTION_registerStatusBar = 16;
        static final int TRANSACTION_registerStatusBarToDisplay = 17;
        static final int TRANSACTION_removeIcon = 10;
        static final int TRANSACTION_requestStatusBarOpen = 53;
        static final int TRANSACTION_setCallBackground = 46;
        static final int TRANSACTION_setCurrentUser = 15;
        static final int TRANSACTION_setIcon = 8;
        static final int TRANSACTION_setIconVisibility = 9;
        static final int TRANSACTION_setImeWindowStatus = 13;
        static final int TRANSACTION_setMultiWindowBg = 47;
        static final int TRANSACTION_setPanelExpandState = 44;
        static final int TRANSACTION_setQuickSettingPanelExpandState = 50;
        static final int TRANSACTION_setSystemUiVisibility = 28;
        static final int TRANSACTION_setSystemUiVisibilityToDisplay = 30;
        static final int TRANSACTION_setWindowState = 29;
        static final int TRANSACTION_setWindowStateToDisplay = 31;
        static final int TRANSACTION_showRecentApps = 32;
        static final int TRANSACTION_startAssist = 40;
        static final int TRANSACTION_toggleNotificationPanel = 43;
        static final int TRANSACTION_toggleRecentApps = 34;
        static final int TRANSACTION_topAppWindowChanged = 11;
        static final int TRANSACTION_topAppWindowChangedToDisplay = 12;

        private static class Proxy implements IStatusBarService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void expandNotificationsPanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void collapsePanels() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disable(int what, IBinder token, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableForUser(int what, IBinder token, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disable2(int what, IBinder token, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disable2ForUser(int what, IBinder token, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDisableFlags() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIcon(String slot, String iconPackage, int iconId, int iconLevel, String contentDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    _data.writeString(iconPackage);
                    _data.writeInt(iconId);
                    _data.writeInt(iconLevel);
                    _data.writeString(contentDescription);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIconVisibility(String slot, boolean visible) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    if (visible) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeIcon(String slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void topAppWindowChanged(boolean menuVisible) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (menuVisible) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void topAppWindowChangedToDisplay(boolean menuVisible, int displayId) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (menuVisible) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(displayId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setImeWindowStatus(IBinder token, int vis, int backDisposition, boolean showImeSwitcher) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(vis);
                    _data.writeInt(backDisposition);
                    if (showImeSwitcher) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void expandSettingsPanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCurrentUser(int newUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newUserId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerStatusBar(IStatusBar callbacks, StatusBarIconList iconList, int[] switches, List<IBinder> binders) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callbacks != null ? callbacks.asBinder() : null);
                    if (switches == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(switches.length);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        iconList.readFromParcel(_reply);
                    }
                    _reply.readIntArray(switches);
                    _reply.readBinderList(binders);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerStatusBarToDisplay(IStatusBar callbacks, StatusBarIconList iconList, int[] switches, List<IBinder> binders, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callbacks != null ? callbacks.asBinder() : null);
                    if (switches == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(switches.length);
                    }
                    _data.writeInt(displayId);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        iconList.readFromParcel(_reply);
                    }
                    _reply.readIntArray(switches);
                    _reply.readBinderList(binders);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onPanelRevealed(boolean clearNotificationEffects, int numItems) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (clearNotificationEffects) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(numItems);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onPanelHidden() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearNotificationEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationClick(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationActionClick(String key, int actionIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(actionIndex);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationError(String pkg, String tag, int id, int uid, int initialPid, String message, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    _data.writeInt(uid);
                    _data.writeInt(initialPid);
                    _data.writeString(message);
                    _data.writeInt(userId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onClearAllNotifications(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationClear(String pkg, String tag, int id, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    _data.writeInt(userId);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationVisibilityChanged(NotificationVisibility[] newlyVisibleKeys, NotificationVisibility[] noLongerVisibleKeys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(newlyVisibleKeys, 0);
                    _data.writeTypedArray(noLongerVisibleKeys, 0);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationExpansionChanged(String key, boolean userAction, boolean expanded) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(userAction ? 1 : 0);
                    if (!expanded) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemUiVisibility(int vis, int mask, String cause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vis);
                    _data.writeInt(mask);
                    _data.writeString(cause);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWindowState(int window, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(window);
                    _data.writeInt(state);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSystemUiVisibilityToDisplay(int vis, int mask, String cause, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vis);
                    _data.writeInt(mask);
                    _data.writeString(cause);
                    _data.writeInt(displayId);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWindowStateToDisplay(int window, int state, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(window);
                    _data.writeInt(state);
                    _data.writeInt(displayId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showRecentApps(boolean triggeredFromAltTab) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (triggeredFromAltTab) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hideRecentApps(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(triggeredFromAltTab ? 1 : 0);
                    if (!triggeredFromHomeKey) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void toggleRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void preloadRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelPreloadRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void appTransitionPending() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void appTransitionCancelled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void appTransitionStarting(long statusBarAnimationsStartTime, long statusBarAnimationsDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(statusBarAnimationsStartTime);
                    _data.writeLong(statusBarAnimationsDuration);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startAssist(Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableAsUser(int userId, int what, IBinder token, String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(what);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void applyEDMPolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void toggleNotificationPanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPanelExpandState(boolean state) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getPanelExpandState() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCallBackground(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMultiWindowBg(boolean on) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (on) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyRecentPanelVisiblity(boolean state) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onNotificationClearWithReason(String pkg, String tag, int id, int userId, int reasonClear) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    _data.writeInt(userId);
                    _data.writeInt(reasonClear);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setQuickSettingPanelExpandState(boolean state) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getQuickSettingPanelExpandState() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableSignals(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestStatusBarOpen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatusBarService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStatusBarService)) {
                return new Proxy(obj);
            }
            return (IStatusBarService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IStatusBar _arg0;
            StatusBarIconList _arg1;
            int _arg2_length;
            int[] _arg2;
            List<IBinder> _arg3;
            boolean _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    expandNotificationsPanel();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    collapsePanels();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    disable(data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    disableForUser(data.readInt(), data.readStrongBinder(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    disable2(data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    disable2ForUser(data.readInt(), data.readStrongBinder(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = getDisableFlags();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setIcon(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setIconVisibility(data.readString(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    removeIcon(data.readString());
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    topAppWindowChanged(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    topAppWindowChangedToDisplay(data.readInt() != 0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    setImeWindowStatus(data.readStrongBinder(), data.readInt(), data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    expandSettingsPanel();
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    setCurrentUser(data.readInt());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = com.android.internal.statusbar.IStatusBar.Stub.asInterface(data.readStrongBinder());
                    _arg1 = new StatusBarIconList();
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        _arg2 = new int[_arg2_length];
                    }
                    _arg3 = new ArrayList();
                    registerStatusBar(_arg0, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    reply.writeIntArray(_arg2);
                    reply.writeBinderList(_arg3);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = com.android.internal.statusbar.IStatusBar.Stub.asInterface(data.readStrongBinder());
                    _arg1 = new StatusBarIconList();
                    _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        _arg2 = new int[_arg2_length];
                    }
                    _arg3 = new ArrayList();
                    registerStatusBarToDisplay(_arg0, _arg1, _arg2, _arg3, data.readInt());
                    reply.writeNoException();
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    reply.writeIntArray(_arg2);
                    reply.writeBinderList(_arg3);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    onPanelRevealed(data.readInt() != 0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    onPanelHidden();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    clearNotificationEffects();
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    onNotificationClick(data.readString());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    onNotificationActionClick(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    onNotificationError(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    onClearAllNotifications(data.readInt());
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    onNotificationClear(data.readString(), data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    onNotificationVisibilityChanged((NotificationVisibility[]) data.createTypedArray(NotificationVisibility.CREATOR), (NotificationVisibility[]) data.createTypedArray(NotificationVisibility.CREATOR));
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    onNotificationExpansionChanged(data.readString(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    setSystemUiVisibility(data.readInt(), data.readInt(), data.readString());
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    setWindowState(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    setSystemUiVisibilityToDisplay(data.readInt(), data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    setWindowStateToDisplay(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    showRecentApps(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    hideRecentApps(data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    toggleRecentApps();
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    preloadRecentApps();
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    cancelPreloadRecentApps();
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    appTransitionPending();
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    appTransitionCancelled();
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    appTransitionStarting(data.readLong(), data.readLong());
                    reply.writeNoException();
                    return true;
                case 40:
                    Bundle _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    startAssist(_arg02);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    disableAsUser(data.readInt(), data.readInt(), data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    applyEDMPolicy();
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    toggleNotificationPanel();
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    setPanelExpandState(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getPanelExpandState();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    setCallBackground(data.readInt());
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    setMultiWindowBg(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    notifyRecentPanelVisiblity(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    onNotificationClearWithReason(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    setQuickSettingPanelExpandState(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getQuickSettingPanelExpandState();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    enableSignals(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    requestStatusBarOpen();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void appTransitionCancelled() throws RemoteException;

    void appTransitionPending() throws RemoteException;

    void appTransitionStarting(long j, long j2) throws RemoteException;

    void applyEDMPolicy() throws RemoteException;

    void cancelPreloadRecentApps() throws RemoteException;

    void clearNotificationEffects() throws RemoteException;

    void collapsePanels() throws RemoteException;

    void disable(int i, IBinder iBinder, String str) throws RemoteException;

    void disable2(int i, IBinder iBinder, String str) throws RemoteException;

    void disable2ForUser(int i, IBinder iBinder, String str, int i2) throws RemoteException;

    void disableAsUser(int i, int i2, IBinder iBinder, String str) throws RemoteException;

    void disableForUser(int i, IBinder iBinder, String str, int i2) throws RemoteException;

    void enableSignals(boolean z) throws RemoteException;

    void expandNotificationsPanel() throws RemoteException;

    void expandSettingsPanel() throws RemoteException;

    int getDisableFlags() throws RemoteException;

    boolean getPanelExpandState() throws RemoteException;

    boolean getQuickSettingPanelExpandState() throws RemoteException;

    void hideRecentApps(boolean z, boolean z2) throws RemoteException;

    void notifyRecentPanelVisiblity(boolean z) throws RemoteException;

    void onClearAllNotifications(int i) throws RemoteException;

    void onNotificationActionClick(String str, int i) throws RemoteException;

    void onNotificationClear(String str, String str2, int i, int i2) throws RemoteException;

    void onNotificationClearWithReason(String str, String str2, int i, int i2, int i3) throws RemoteException;

    void onNotificationClick(String str) throws RemoteException;

    void onNotificationError(String str, String str2, int i, int i2, int i3, String str3, int i4) throws RemoteException;

    void onNotificationExpansionChanged(String str, boolean z, boolean z2) throws RemoteException;

    void onNotificationVisibilityChanged(NotificationVisibility[] notificationVisibilityArr, NotificationVisibility[] notificationVisibilityArr2) throws RemoteException;

    void onPanelHidden() throws RemoteException;

    void onPanelRevealed(boolean z, int i) throws RemoteException;

    void preloadRecentApps() throws RemoteException;

    void registerStatusBar(IStatusBar iStatusBar, StatusBarIconList statusBarIconList, int[] iArr, List<IBinder> list) throws RemoteException;

    void registerStatusBarToDisplay(IStatusBar iStatusBar, StatusBarIconList statusBarIconList, int[] iArr, List<IBinder> list, int i) throws RemoteException;

    void removeIcon(String str) throws RemoteException;

    void requestStatusBarOpen() throws RemoteException;

    void setCallBackground(int i) throws RemoteException;

    void setCurrentUser(int i) throws RemoteException;

    void setIcon(String str, String str2, int i, int i2, String str3) throws RemoteException;

    void setIconVisibility(String str, boolean z) throws RemoteException;

    void setImeWindowStatus(IBinder iBinder, int i, int i2, boolean z) throws RemoteException;

    void setMultiWindowBg(boolean z) throws RemoteException;

    void setPanelExpandState(boolean z) throws RemoteException;

    void setQuickSettingPanelExpandState(boolean z) throws RemoteException;

    void setSystemUiVisibility(int i, int i2, String str) throws RemoteException;

    void setSystemUiVisibilityToDisplay(int i, int i2, String str, int i3) throws RemoteException;

    void setWindowState(int i, int i2) throws RemoteException;

    void setWindowStateToDisplay(int i, int i2, int i3) throws RemoteException;

    void showRecentApps(boolean z) throws RemoteException;

    void startAssist(Bundle bundle) throws RemoteException;

    void toggleNotificationPanel() throws RemoteException;

    void toggleRecentApps() throws RemoteException;

    void topAppWindowChanged(boolean z) throws RemoteException;

    void topAppWindowChangedToDisplay(boolean z, int i) throws RemoteException;
}
