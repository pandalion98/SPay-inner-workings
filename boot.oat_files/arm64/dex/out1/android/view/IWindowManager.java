package android.view;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;
import android.sec.easyonehand.IEasyOneHandWatcher;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.android.internal.app.IAssistScreenshotReceiver;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodClient;
import com.samsung.android.smartclip.SmartClipRemoteRequestInfo;

public interface IWindowManager extends IInterface {

    public static abstract class Stub extends Binder implements IWindowManager {
        private static final String DESCRIPTOR = "android.view.IWindowManager";
        static final int TRANSACTION_addAppToken = 22;
        static final int TRANSACTION_addWindowToken = 19;
        static final int TRANSACTION_addWindowTokenWithDisplayId = 21;
        static final int TRANSACTION_changeDisplayScale = 101;
        static final int TRANSACTION_clearForcedDisplayDensity = 13;
        static final int TRANSACTION_clearForcedDisplaySize = 9;
        static final int TRANSACTION_clearWindowContentFrameStats = 85;
        static final int TRANSACTION_closeSystemDialogs = 56;
        static final int TRANSACTION_cocktailBarVisibilityChanged = 104;
        static final int TRANSACTION_disableKeyguard = 46;
        static final int TRANSACTION_dismissKeyguard = 54;
        static final int TRANSACTION_dispatchSmartClipRemoteRequest = 100;
        static final int TRANSACTION_enableScreenIfNeeded = 84;
        static final int TRANSACTION_executeAppTransition = 35;
        static final int TRANSACTION_exitKeyguardSecurely = 48;
        static final int TRANSACTION_freezeRotation = 74;
        static final int TRANSACTION_getAnimationScale = 57;
        static final int TRANSACTION_getAnimationScales = 58;
        static final int TRANSACTION_getAppOrientation = 25;
        static final int TRANSACTION_getBaseDisplayDensity = 11;
        static final int TRANSACTION_getBaseDisplaySize = 7;
        static final int TRANSACTION_getCocktailBarFrame = 106;
        static final int TRANSACTION_getCurrentAnimatorScale = 61;
        static final int TRANSACTION_getInitialDisplayDensity = 10;
        static final int TRANSACTION_getInitialDisplaySize = 6;
        static final int TRANSACTION_getKeyguardPreviewLayoutResId = 110;
        static final int TRANSACTION_getPendingAppTransition = 28;
        static final int TRANSACTION_getPreferredOptionsPanelGravity = 72;
        static final int TRANSACTION_getPreferredOptionsPanelGravityTablet = 73;
        static final int TRANSACTION_getRotation = 69;
        static final int TRANSACTION_getWindowContentFrameStats = 86;
        static final int TRANSACTION_hasNavigationBar = 81;
        static final int TRANSACTION_inKeyguardRestrictedInputMode = 52;
        static final int TRANSACTION_inputMethodClientHasFocus = 5;
        static final int TRANSACTION_isCarModeBarVisible = 94;
        static final int TRANSACTION_isKeyguardLocked = 49;
        static final int TRANSACTION_isKeyguardSecure = 50;
        static final int TRANSACTION_isKeyguardShowingAndNotOccluded = 51;
        static final int TRANSACTION_isKeyguardShowingAndOccluded = 53;
        static final int TRANSACTION_isMetaKeyEventRequested = 98;
        static final int TRANSACTION_isNavigationBarVisible = 93;
        static final int TRANSACTION_isRotationFrozen = 76;
        static final int TRANSACTION_isSafeModeEnabled = 83;
        static final int TRANSACTION_isStatusBarVisible = 92;
        static final int TRANSACTION_isSystemKeyEventRequested = 96;
        static final int TRANSACTION_isViewServerRunning = 3;
        static final int TRANSACTION_keyguardGoingAway = 55;
        static final int TRANSACTION_lockNow = 82;
        static final int TRANSACTION_openSession = 4;
        static final int TRANSACTION_overridePendingAppTransition = 29;
        static final int TRANSACTION_overridePendingAppTransitionAspectScaledThumb = 33;
        static final int TRANSACTION_overridePendingAppTransitionClipReveal = 31;
        static final int TRANSACTION_overridePendingAppTransitionInPlace = 34;
        static final int TRANSACTION_overridePendingAppTransitionScaleUp = 30;
        static final int TRANSACTION_overridePendingAppTransitionThumb = 32;
        static final int TRANSACTION_overridePendingAppTransitionTranslate = 105;
        static final int TRANSACTION_pauseKeyDispatching = 16;
        static final int TRANSACTION_prepareAppTransition = 27;
        static final int TRANSACTION_reenableKeyguard = 47;
        static final int TRANSACTION_registerEasyOneHandWatcher = 102;
        static final int TRANSACTION_removeAdaptiveEvent = 88;
        static final int TRANSACTION_removeAppToken = 41;
        static final int TRANSACTION_removeRotationWatcher = 71;
        static final int TRANSACTION_removeWindowToken = 20;
        static final int TRANSACTION_requestAssistScreenshot = 77;
        static final int TRANSACTION_requestMetaKeyEvent = 97;
        static final int TRANSACTION_requestSystemKeyEvent = 95;
        static final int TRANSACTION_resumeKeyDispatching = 17;
        static final int TRANSACTION_screenshotApplications = 78;
        static final int TRANSACTION_setAdaptiveEvent = 87;
        static final int TRANSACTION_setAnimationScale = 59;
        static final int TRANSACTION_setAnimationScales = 60;
        static final int TRANSACTION_setAppOrientation = 24;
        static final int TRANSACTION_setAppStartingWindow = 36;
        static final int TRANSACTION_setAppTask = 23;
        static final int TRANSACTION_setAppVisibility = 38;
        static final int TRANSACTION_setAppWillBeHidden = 37;
        static final int TRANSACTION_setBendedPendingIntent = 90;
        static final int TRANSACTION_setBendedPendingIntentInSecure = 91;
        static final int TRANSACTION_setCurrentInputMethodClient = 99;
        static final int TRANSACTION_setEventDispatching = 18;
        static final int TRANSACTION_setFocusedApp = 26;
        static final int TRANSACTION_setForcedDisplayDensity = 12;
        static final int TRANSACTION_setForcedDisplayDensityNoFreeze = 68;
        static final int TRANSACTION_setForcedDisplayScalingMode = 14;
        static final int TRANSACTION_setForcedDisplaySize = 8;
        static final int TRANSACTION_setInTouchMode = 62;
        static final int TRANSACTION_setKeyguardPreview = 109;
        static final int TRANSACTION_setNewConfiguration = 43;
        static final int TRANSACTION_setOverscan = 15;
        static final int TRANSACTION_setReverseStartingWindowContentView = 108;
        static final int TRANSACTION_setScreenCaptureDisabled = 65;
        static final int TRANSACTION_setStartingWindowContentView = 107;
        static final int TRANSACTION_setStrictModeVisualIndicatorPreference = 64;
        static final int TRANSACTION_showStrictModeViolation = 63;
        static final int TRANSACTION_startAppFreezingScreen = 39;
        static final int TRANSACTION_startFreezingScreen = 44;
        static final int TRANSACTION_startViewServer = 1;
        static final int TRANSACTION_statusBarVisibilityChanged = 79;
        static final int TRANSACTION_statusBarVisibilityChangedToDisplay = 80;
        static final int TRANSACTION_stopAppFreezingScreen = 40;
        static final int TRANSACTION_stopFreezingScreen = 45;
        static final int TRANSACTION_stopViewServer = 2;
        static final int TRANSACTION_thawRotation = 75;
        static final int TRANSACTION_unregisterEasyOneHandWatcher = 103;
        static final int TRANSACTION_updateAdaptiveEvent = 89;
        static final int TRANSACTION_updateCurrentUserPolicy = 111;
        static final int TRANSACTION_updateDisplay = 67;
        static final int TRANSACTION_updateOrientationFromAppTokens = 42;
        static final int TRANSACTION_updateRotation = 66;
        static final int TRANSACTION_updateTspViewPolicy = 112;
        static final int TRANSACTION_watchRotation = 70;

        private static class Proxy implements IWindowManager {
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

            public boolean startViewServer(int port) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(port);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopViewServer() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            public boolean isViewServerRunning() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public IWindowSession openSession(IWindowSessionCallback callback, IInputMethodClient client, IInputContext inputContext) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    IBinder asBinder;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        asBinder = callback.asBinder();
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    if (client != null) {
                        asBinder = client.asBinder();
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    if (inputContext != null) {
                        iBinder = inputContext.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    IWindowSession _result = android.view.IWindowSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean inputMethodClientHasFocus(IInputMethodClient client) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
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

            public void getInitialDisplaySize(int displayId, Point size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        size.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getBaseDisplaySize(int displayId, Point size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        size.readFromParcel(_reply);
                    }
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForcedDisplaySize(int displayId, int width, int height) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearForcedDisplaySize(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInitialDisplayDensity(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getBaseDisplayDensity(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForcedDisplayDensity(int displayId, int density) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(density);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearForcedDisplayDensity(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForcedDisplayScalingMode(int displayId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(mode);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setOverscan(int displayId, int left, int top, int right, int bottom) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(left);
                    _data.writeInt(top);
                    _data.writeInt(right);
                    _data.writeInt(bottom);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void pauseKeyDispatching(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resumeKeyDispatching(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setEventDispatching(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addWindowToken(IBinder token, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(type);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeWindowToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addWindowTokenWithDisplayId(IBinder token, int type, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(type);
                    _data.writeInt(displayId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addAppToken(int addPos, IApplicationToken token, int groupId, int stackId, int requestedOrientation, boolean fullscreen, boolean showWhenLocked, int userId, int configChanges, boolean voiceInteraction, boolean launchTaskBehind) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(addPos);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(groupId);
                    _data.writeInt(stackId);
                    _data.writeInt(requestedOrientation);
                    _data.writeInt(fullscreen ? 1 : 0);
                    _data.writeInt(showWhenLocked ? 1 : 0);
                    _data.writeInt(userId);
                    _data.writeInt(configChanges);
                    _data.writeInt(voiceInteraction ? 1 : 0);
                    _data.writeInt(launchTaskBehind ? 1 : 0);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppTask(IBinder token, int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(taskId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppOrientation(IApplicationToken token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(requestedOrientation);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAppOrientation(IApplicationToken token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFocusedApp(IBinder token, boolean moveFocusNow) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (moveFocusNow) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepareAppTransition(int transit, boolean alwaysKeepCurrent) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(transit);
                    if (alwaysKeepCurrent) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPendingAppTransition() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransition(String packageName, int enterAnim, int exitAnim, IRemoteCallback startedCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(enterAnim);
                    _data.writeInt(exitAnim);
                    _data.writeStrongBinder(startedCallback != null ? startedCallback.asBinder() : null);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionScaleUp(int startX, int startY, int startWidth, int startHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startX);
                    _data.writeInt(startY);
                    _data.writeInt(startWidth);
                    _data.writeInt(startHeight);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionClipReveal(int startX, int startY, int startWidth, int startHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startX);
                    _data.writeInt(startY);
                    _data.writeInt(startWidth);
                    _data.writeInt(startHeight);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionThumb(Bitmap srcThumb, int startX, int startY, IRemoteCallback startedCallback, IRemoteCallback transitCallback, boolean scaleUp) throws RemoteException {
                IBinder iBinder = null;
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (srcThumb != null) {
                        _data.writeInt(1);
                        srcThumb.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(startX);
                    _data.writeInt(startY);
                    _data.writeStrongBinder(startedCallback != null ? startedCallback.asBinder() : null);
                    if (transitCallback != null) {
                        iBinder = transitCallback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (!scaleUp) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionAspectScaledThumb(Bitmap srcThumb, int startX, int startY, int targetWidth, int targetHeight, IRemoteCallback startedCallback, IRemoteCallback transitCallback, boolean scaleUp) throws RemoteException {
                IBinder iBinder = null;
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (srcThumb != null) {
                        _data.writeInt(1);
                        srcThumb.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(startX);
                    _data.writeInt(startY);
                    _data.writeInt(targetWidth);
                    _data.writeInt(targetHeight);
                    _data.writeStrongBinder(startedCallback != null ? startedCallback.asBinder() : null);
                    if (transitCallback != null) {
                        iBinder = transitCallback.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (!scaleUp) {
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

            public void overridePendingAppTransitionInPlace(String packageName, int anim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(anim);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void executeAppTransition() throws RemoteException {
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

            public void setAppStartingWindow(IBinder token, String pkg, int theme, CompatibilityInfo compatInfo, CharSequence nonLocalizedLabel, int labelRes, int icon, int logo, int windowFlags, IBinder transferFrom, boolean createIfNeeded) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(pkg);
                    _data.writeInt(theme);
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (nonLocalizedLabel != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(nonLocalizedLabel, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(labelRes);
                    _data.writeInt(icon);
                    _data.writeInt(logo);
                    _data.writeInt(windowFlags);
                    _data.writeStrongBinder(transferFrom);
                    _data.writeInt(createIfNeeded ? 1 : 0);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppWillBeHidden(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppVisibility(IBinder token, boolean visible) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (visible) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startAppFreezingScreen(IBinder token, int configChanges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(configChanges);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopAppFreezingScreen(IBinder token, boolean force) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (force) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAppToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Configuration updateOrientationFromAppTokens(Configuration currentConfig, IBinder freezeThisOneIfNeeded) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Configuration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (currentConfig != null) {
                        _data.writeInt(1);
                        currentConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(freezeThisOneIfNeeded);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Configuration) Configuration.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNewConfiguration(Configuration config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startFreezingScreen(int exitAnim, int enterAnim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(exitAnim);
                    _data.writeInt(enterAnim);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopFreezingScreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableKeyguard(IBinder token, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(tag);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reenableKeyguard(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void exitKeyguardSecurely(IOnKeyguardExitResult callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isKeyguardLocked() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
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

            public boolean isKeyguardSecure() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(50, _data, _reply, 0);
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

            public boolean isKeyguardShowingAndNotOccluded() throws RemoteException {
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

            public boolean inKeyguardRestrictedInputMode() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(52, _data, _reply, 0);
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

            public boolean isKeyguardShowingAndOccluded() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(53, _data, _reply, 0);
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

            public void dismissKeyguard() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void keyguardGoingAway(boolean disableWindowAnimations, boolean keyguardGoingToNotificationShade) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(disableWindowAnimations ? 1 : 0);
                    if (!keyguardGoingToNotificationShade) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getAnimationScale(int which) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float[] getAnimationScales() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    float[] _result = _reply.createFloatArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAnimationScale(int which, float scale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(which);
                    _data.writeFloat(scale);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAnimationScales(float[] scales) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloatArray(scales);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getCurrentAnimatorScale() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInTouchMode(boolean showFocus) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (showFocus) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showStrictModeViolation(boolean on) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (on) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStrictModeVisualIndicatorPreference(String enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(enabled);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setScreenCaptureDisabled(int userId, boolean disabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (disabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateRotation(boolean alwaysSendConfiguration, boolean forceRelayout) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(alwaysSendConfiguration ? 1 : 0);
                    if (!forceRelayout) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setForcedDisplayDensityNoFreeze(int displayId, int density) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(density);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getRotation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int watchRotation(IRotationWatcher watcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeRotationWatcher(IRotationWatcher watcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredOptionsPanelGravity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPreferredOptionsPanelGravityTablet() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void freezeRotation(int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rotation);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void thawRotation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRotationFrozen() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(76, _data, _reply, 0);
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

            public boolean requestAssistScreenshot(IAssistScreenshotReceiver receiver) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(77, _data, _reply, 0);
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

            public Bitmap screenshotApplications(IBinder appToken, int displayId, int maxWidth, int maxHeight) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Bitmap _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(appToken);
                    _data.writeInt(displayId);
                    _data.writeInt(maxWidth);
                    _data.writeInt(maxHeight);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void statusBarVisibilityChanged(int visibility) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visibility);
                    this.mRemote.transact(79, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void statusBarVisibilityChangedToDisplay(int visibility, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visibility);
                    _data.writeInt(displayId);
                    this.mRemote.transact(80, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public boolean hasNavigationBar() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(81, _data, _reply, 0);
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

            public void lockNow(Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSafeModeEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(83, _data, _reply, 0);
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

            public void enableScreenIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean clearWindowContentFrameStats(IBinder token) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(85, _data, _reply, 0);
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

            public WindowContentFrameStats getWindowContentFrameStats(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    WindowContentFrameStats _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (WindowContentFrameStats) WindowContentFrameStats.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAdaptiveEvent(String requestClass, RemoteViews smallView, RemoteViews bigView) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(requestClass);
                    if (smallView != null) {
                        _data.writeInt(1);
                        smallView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bigView != null) {
                        _data.writeInt(1);
                        bigView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAdaptiveEvent(String requestClass) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(requestClass);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateAdaptiveEvent(String requestClass, RemoteViews smallView, RemoteViews bigView) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(requestClass);
                    if (smallView != null) {
                        _data.writeInt(1);
                        smallView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bigView != null) {
                        _data.writeInt(1);
                        bigView.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBendedPendingIntent(PendingIntent p, Intent fillInIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fillInIntent != null) {
                        _data.writeInt(1);
                        fillInIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setBendedPendingIntentInSecure(PendingIntent p, Intent fillInIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (p != null) {
                        _data.writeInt(1);
                        p.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (fillInIntent != null) {
                        _data.writeInt(1);
                        fillInIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isStatusBarVisible() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(92, _data, _reply, 0);
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

            public boolean isNavigationBarVisible() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(93, _data, _reply, 0);
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

            public boolean isCarModeBarVisible() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(94, _data, _reply, 0);
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

            public boolean requestSystemKeyEvent(int keyCode, ComponentName componentName, boolean request) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(keyCode);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(request ? 1 : 0);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSystemKeyEventRequested(int keyCode, ComponentName componentName) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(keyCode);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestMetaKeyEvent(ComponentName componentName, boolean request) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!request) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isMetaKeyEventRequested(ComponentName componentName) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCurrentInputMethodClient(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dispatchSmartClipRemoteRequest(int targetX, int targetY, SmartClipRemoteRequestInfo request, IBinder skipWindowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(targetX);
                    _data.writeInt(targetY);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(skipWindowToken);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void changeDisplayScale(float scale, float offsetX, float offsetY, boolean registerInput, IInputFilter filter) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(scale);
                    _data.writeFloat(offsetX);
                    _data.writeFloat(offsetY);
                    if (registerInput) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(filter != null ? filter.asBinder() : null);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerEasyOneHandWatcher(IEasyOneHandWatcher watcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterEasyOneHandWatcher(IEasyOneHandWatcher watcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cocktailBarVisibilityChanged(boolean visibility) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!visibility) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(104, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void overridePendingAppTransitionTranslate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Rect getCocktailBarFrame() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Rect _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Rect) Rect.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th) {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStartingWindowContentView(String pkgName, int resId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(resId);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setReverseStartingWindowContentView(String pkgName, int resId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(resId);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setKeyguardPreview(String pkgName, int resId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(resId);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getKeyguardPreviewLayoutResId(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateCurrentUserPolicy(int newUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newUserId);
                    this.mRemote.transact(111, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void updateTspViewPolicy(int viewPolicy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(viewPolicy);
                    this.mRemote.transact(112, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWindowManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWindowManager)) {
                return new Proxy(obj);
            }
            return (IWindowManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _result;
            int _arg0;
            Point _arg1;
            int _result2;
            Bitmap _arg02;
            Configuration _arg03;
            float _result3;
            String _arg04;
            RemoteViews _arg12;
            RemoteViews _arg2;
            PendingIntent _arg05;
            Intent _arg13;
            ComponentName _arg14;
            ComponentName _arg06;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = startViewServer(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = stopViewServer();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isViewServerRunning();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IWindowSession _result4 = openSession(android.view.IWindowSessionCallback.Stub.asInterface(data.readStrongBinder()), com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder()), com.android.internal.view.IInputContext.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(_result4 != null ? _result4.asBinder() : null);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = inputMethodClientHasFocus(com.android.internal.view.IInputMethodClient.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = new Point();
                    getInitialDisplaySize(_arg0, _arg1);
                    reply.writeNoException();
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = new Point();
                    getBaseDisplaySize(_arg0, _arg1);
                    reply.writeNoException();
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        _arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setForcedDisplaySize(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    clearForcedDisplaySize(data.readInt());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getInitialDisplayDensity(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getBaseDisplayDensity(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    setForcedDisplayDensity(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    clearForcedDisplayDensity(data.readInt());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    setForcedDisplayScalingMode(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    setOverscan(data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    pauseKeyDispatching(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    resumeKeyDispatching(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    setEventDispatching(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    addWindowToken(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    removeWindowToken(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    addWindowTokenWithDisplayId(data.readStrongBinder(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    addAppToken(data.readInt(), android.view.IApplicationToken.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0, data.readInt() != 0, data.readInt(), data.readInt(), data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    setAppTask(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    setAppOrientation(android.view.IApplicationToken.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAppOrientation(android.view.IApplicationToken.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    setFocusedApp(data.readStrongBinder(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    prepareAppTransition(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPendingAppTransition();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    overridePendingAppTransition(data.readString(), data.readInt(), data.readInt(), android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    overridePendingAppTransitionScaleUp(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    overridePendingAppTransitionClipReveal(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    overridePendingAppTransitionThumb(_arg02, data.readInt(), data.readInt(), android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()), android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    overridePendingAppTransitionAspectScaledThumb(_arg02, data.readInt(), data.readInt(), data.readInt(), data.readInt(), android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()), android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    overridePendingAppTransitionInPlace(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    executeAppTransition();
                    reply.writeNoException();
                    return true;
                case 36:
                    CompatibilityInfo _arg3;
                    CharSequence _arg4;
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg07 = data.readStrongBinder();
                    String _arg15 = data.readString();
                    int _arg22 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (CompatibilityInfo) CompatibilityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg4 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    setAppStartingWindow(_arg07, _arg15, _arg22, _arg3, _arg4, data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readStrongBinder(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    setAppWillBeHidden(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    setAppVisibility(data.readStrongBinder(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    startAppFreezingScreen(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    stopAppFreezingScreen(data.readStrongBinder(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    removeAppToken(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Configuration) Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    Configuration _result5 = updateOrientationFromAppTokens(_arg03, data.readStrongBinder());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Configuration) Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    setNewConfiguration(_arg03);
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    startFreezingScreen(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    stopFreezingScreen();
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    disableKeyguard(data.readStrongBinder(), data.readString());
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    reenableKeyguard(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    exitKeyguardSecurely(android.view.IOnKeyguardExitResult.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isKeyguardLocked();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isKeyguardSecure();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isKeyguardShowingAndNotOccluded();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    _result = inKeyguardRestrictedInputMode();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isKeyguardShowingAndOccluded();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    dismissKeyguard();
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    keyguardGoingAway(data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    closeSystemDialogs(data.readString());
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getAnimationScale(data.readInt());
                    reply.writeNoException();
                    reply.writeFloat(_result3);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    float[] _result6 = getAnimationScales();
                    reply.writeNoException();
                    reply.writeFloatArray(_result6);
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    setAnimationScale(data.readInt(), data.readFloat());
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    setAnimationScales(data.createFloatArray());
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getCurrentAnimatorScale();
                    reply.writeNoException();
                    reply.writeFloat(_result3);
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    setInTouchMode(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    showStrictModeViolation(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    setStrictModeVisualIndicatorPreference(data.readString());
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    setScreenCaptureDisabled(data.readInt(), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    updateRotation(data.readInt() != 0, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    updateDisplay();
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    setForcedDisplayDensityNoFreeze(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getRotation();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = watchRotation(android.view.IRotationWatcher.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    removeRotationWatcher(android.view.IRotationWatcher.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPreferredOptionsPanelGravity();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPreferredOptionsPanelGravityTablet();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    freezeRotation(data.readInt());
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    thawRotation();
                    reply.writeNoException();
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isRotationFrozen();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    _result = requestAssistScreenshot(com.android.internal.app.IAssistScreenshotReceiver.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    Bitmap _result7 = screenshotApplications(data.readStrongBinder(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    statusBarVisibilityChanged(data.readInt());
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    statusBarVisibilityChangedToDisplay(data.readInt(), data.readInt());
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    _result = hasNavigationBar();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 82:
                    Bundle _arg08;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    lockNow(_arg08);
                    reply.writeNoException();
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isSafeModeEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    enableScreenIfNeeded();
                    reply.writeNoException();
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    _result = clearWindowContentFrameStats(data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    WindowContentFrameStats _result8 = getWindowContentFrameStats(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    _arg04 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    setAdaptiveEvent(_arg04, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    removeAdaptiveEvent(data.readString());
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    _arg04 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    updateAdaptiveEvent(_arg04, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setBendedPendingIntent(_arg05, _arg13);
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    setBendedPendingIntentInSecure(_arg05, _arg13);
                    reply.writeNoException();
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isStatusBarVisible();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isNavigationBarVisible();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isCarModeBarVisible();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    _result = requestSystemKeyEvent(_arg0, _arg14, data.readInt() != 0);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    _result = isSystemKeyEventRequested(_arg0, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    requestMetaKeyEvent(_arg06, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    _result = isMetaKeyEventRequested(_arg06);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    setCurrentInputMethodClient(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 100:
                    SmartClipRemoteRequestInfo _arg23;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    int _arg16 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg23 = (SmartClipRemoteRequestInfo) SmartClipRemoteRequestInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    dispatchSmartClipRemoteRequest(_arg0, _arg16, _arg23, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    changeDisplayScale(data.readFloat(), data.readFloat(), data.readFloat(), data.readInt() != 0, android.view.IInputFilter.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    registerEasyOneHandWatcher(android.sec.easyonehand.IEasyOneHandWatcher.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterEasyOneHandWatcher(android.sec.easyonehand.IEasyOneHandWatcher.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    cocktailBarVisibilityChanged(data.readInt() != 0);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    overridePendingAppTransitionTranslate();
                    reply.writeNoException();
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    Rect _result9 = getCocktailBarFrame();
                    reply.writeNoException();
                    if (_result9 != null) {
                        reply.writeInt(1);
                        _result9.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    setStartingWindowContentView(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    setReverseStartingWindowContentView(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    setKeyguardPreview(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getKeyguardPreviewLayoutResId(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    updateCurrentUserPolicy(data.readInt());
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    updateTspViewPolicy(data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addAppToken(int i, IApplicationToken iApplicationToken, int i2, int i3, int i4, boolean z, boolean z2, int i5, int i6, boolean z3, boolean z4) throws RemoteException;

    void addWindowToken(IBinder iBinder, int i) throws RemoteException;

    void addWindowTokenWithDisplayId(IBinder iBinder, int i, int i2) throws RemoteException;

    void changeDisplayScale(float f, float f2, float f3, boolean z, IInputFilter iInputFilter) throws RemoteException;

    void clearForcedDisplayDensity(int i) throws RemoteException;

    void clearForcedDisplaySize(int i) throws RemoteException;

    boolean clearWindowContentFrameStats(IBinder iBinder) throws RemoteException;

    void closeSystemDialogs(String str) throws RemoteException;

    void cocktailBarVisibilityChanged(boolean z) throws RemoteException;

    void disableKeyguard(IBinder iBinder, String str) throws RemoteException;

    void dismissKeyguard() throws RemoteException;

    void dispatchSmartClipRemoteRequest(int i, int i2, SmartClipRemoteRequestInfo smartClipRemoteRequestInfo, IBinder iBinder) throws RemoteException;

    void enableScreenIfNeeded() throws RemoteException;

    void executeAppTransition() throws RemoteException;

    void exitKeyguardSecurely(IOnKeyguardExitResult iOnKeyguardExitResult) throws RemoteException;

    void freezeRotation(int i) throws RemoteException;

    float getAnimationScale(int i) throws RemoteException;

    float[] getAnimationScales() throws RemoteException;

    int getAppOrientation(IApplicationToken iApplicationToken) throws RemoteException;

    int getBaseDisplayDensity(int i) throws RemoteException;

    void getBaseDisplaySize(int i, Point point) throws RemoteException;

    Rect getCocktailBarFrame() throws RemoteException;

    float getCurrentAnimatorScale() throws RemoteException;

    int getInitialDisplayDensity(int i) throws RemoteException;

    void getInitialDisplaySize(int i, Point point) throws RemoteException;

    int getKeyguardPreviewLayoutResId(String str) throws RemoteException;

    int getPendingAppTransition() throws RemoteException;

    int getPreferredOptionsPanelGravity() throws RemoteException;

    int getPreferredOptionsPanelGravityTablet() throws RemoteException;

    int getRotation() throws RemoteException;

    WindowContentFrameStats getWindowContentFrameStats(IBinder iBinder) throws RemoteException;

    boolean hasNavigationBar() throws RemoteException;

    boolean inKeyguardRestrictedInputMode() throws RemoteException;

    boolean inputMethodClientHasFocus(IInputMethodClient iInputMethodClient) throws RemoteException;

    boolean isCarModeBarVisible() throws RemoteException;

    boolean isKeyguardLocked() throws RemoteException;

    boolean isKeyguardSecure() throws RemoteException;

    boolean isKeyguardShowingAndNotOccluded() throws RemoteException;

    boolean isKeyguardShowingAndOccluded() throws RemoteException;

    boolean isMetaKeyEventRequested(ComponentName componentName) throws RemoteException;

    boolean isNavigationBarVisible() throws RemoteException;

    boolean isRotationFrozen() throws RemoteException;

    boolean isSafeModeEnabled() throws RemoteException;

    boolean isStatusBarVisible() throws RemoteException;

    boolean isSystemKeyEventRequested(int i, ComponentName componentName) throws RemoteException;

    boolean isViewServerRunning() throws RemoteException;

    void keyguardGoingAway(boolean z, boolean z2) throws RemoteException;

    void lockNow(Bundle bundle) throws RemoteException;

    IWindowSession openSession(IWindowSessionCallback iWindowSessionCallback, IInputMethodClient iInputMethodClient, IInputContext iInputContext) throws RemoteException;

    void overridePendingAppTransition(String str, int i, int i2, IRemoteCallback iRemoteCallback) throws RemoteException;

    void overridePendingAppTransitionAspectScaledThumb(Bitmap bitmap, int i, int i2, int i3, int i4, IRemoteCallback iRemoteCallback, IRemoteCallback iRemoteCallback2, boolean z) throws RemoteException;

    void overridePendingAppTransitionClipReveal(int i, int i2, int i3, int i4) throws RemoteException;

    void overridePendingAppTransitionInPlace(String str, int i) throws RemoteException;

    void overridePendingAppTransitionScaleUp(int i, int i2, int i3, int i4) throws RemoteException;

    void overridePendingAppTransitionThumb(Bitmap bitmap, int i, int i2, IRemoteCallback iRemoteCallback, IRemoteCallback iRemoteCallback2, boolean z) throws RemoteException;

    void overridePendingAppTransitionTranslate() throws RemoteException;

    void pauseKeyDispatching(IBinder iBinder) throws RemoteException;

    void prepareAppTransition(int i, boolean z) throws RemoteException;

    void reenableKeyguard(IBinder iBinder) throws RemoteException;

    void registerEasyOneHandWatcher(IEasyOneHandWatcher iEasyOneHandWatcher) throws RemoteException;

    void removeAdaptiveEvent(String str) throws RemoteException;

    void removeAppToken(IBinder iBinder) throws RemoteException;

    void removeRotationWatcher(IRotationWatcher iRotationWatcher) throws RemoteException;

    void removeWindowToken(IBinder iBinder) throws RemoteException;

    boolean requestAssistScreenshot(IAssistScreenshotReceiver iAssistScreenshotReceiver) throws RemoteException;

    void requestMetaKeyEvent(ComponentName componentName, boolean z) throws RemoteException;

    boolean requestSystemKeyEvent(int i, ComponentName componentName, boolean z) throws RemoteException;

    void resumeKeyDispatching(IBinder iBinder) throws RemoteException;

    Bitmap screenshotApplications(IBinder iBinder, int i, int i2, int i3) throws RemoteException;

    void setAdaptiveEvent(String str, RemoteViews remoteViews, RemoteViews remoteViews2) throws RemoteException;

    void setAnimationScale(int i, float f) throws RemoteException;

    void setAnimationScales(float[] fArr) throws RemoteException;

    void setAppOrientation(IApplicationToken iApplicationToken, int i) throws RemoteException;

    void setAppStartingWindow(IBinder iBinder, String str, int i, CompatibilityInfo compatibilityInfo, CharSequence charSequence, int i2, int i3, int i4, int i5, IBinder iBinder2, boolean z) throws RemoteException;

    void setAppTask(IBinder iBinder, int i) throws RemoteException;

    void setAppVisibility(IBinder iBinder, boolean z) throws RemoteException;

    void setAppWillBeHidden(IBinder iBinder) throws RemoteException;

    void setBendedPendingIntent(PendingIntent pendingIntent, Intent intent) throws RemoteException;

    void setBendedPendingIntentInSecure(PendingIntent pendingIntent, Intent intent) throws RemoteException;

    void setCurrentInputMethodClient(IBinder iBinder) throws RemoteException;

    void setEventDispatching(boolean z) throws RemoteException;

    void setFocusedApp(IBinder iBinder, boolean z) throws RemoteException;

    void setForcedDisplayDensity(int i, int i2) throws RemoteException;

    void setForcedDisplayDensityNoFreeze(int i, int i2) throws RemoteException;

    void setForcedDisplayScalingMode(int i, int i2) throws RemoteException;

    void setForcedDisplaySize(int i, int i2, int i3) throws RemoteException;

    void setInTouchMode(boolean z) throws RemoteException;

    void setKeyguardPreview(String str, int i) throws RemoteException;

    void setNewConfiguration(Configuration configuration) throws RemoteException;

    void setOverscan(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void setReverseStartingWindowContentView(String str, int i) throws RemoteException;

    void setScreenCaptureDisabled(int i, boolean z) throws RemoteException;

    void setStartingWindowContentView(String str, int i) throws RemoteException;

    void setStrictModeVisualIndicatorPreference(String str) throws RemoteException;

    void showStrictModeViolation(boolean z) throws RemoteException;

    void startAppFreezingScreen(IBinder iBinder, int i) throws RemoteException;

    void startFreezingScreen(int i, int i2) throws RemoteException;

    boolean startViewServer(int i) throws RemoteException;

    void statusBarVisibilityChanged(int i) throws RemoteException;

    void statusBarVisibilityChangedToDisplay(int i, int i2) throws RemoteException;

    void stopAppFreezingScreen(IBinder iBinder, boolean z) throws RemoteException;

    void stopFreezingScreen() throws RemoteException;

    boolean stopViewServer() throws RemoteException;

    void thawRotation() throws RemoteException;

    void unregisterEasyOneHandWatcher(IEasyOneHandWatcher iEasyOneHandWatcher) throws RemoteException;

    void updateAdaptiveEvent(String str, RemoteViews remoteViews, RemoteViews remoteViews2) throws RemoteException;

    void updateCurrentUserPolicy(int i) throws RemoteException;

    void updateDisplay() throws RemoteException;

    Configuration updateOrientationFromAppTokens(Configuration configuration, IBinder iBinder) throws RemoteException;

    void updateRotation(boolean z, boolean z2) throws RemoteException;

    void updateTspViewPolicy(int i) throws RemoteException;

    int watchRotation(IRotationWatcher iRotationWatcher) throws RemoteException;
}
