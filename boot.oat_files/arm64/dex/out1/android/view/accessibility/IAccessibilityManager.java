package android.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IWindow;
import android.view.MagnificationSpec;
import java.util.List;

public interface IAccessibilityManager extends IInterface {

    public static abstract class Stub extends Binder implements IAccessibilityManager {
        private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityManager";
        static final int TRANSACTION_OnStartGestureWakeup = 34;
        static final int TRANSACTION_OnStopGestureWakeup = 35;
        static final int TRANSACTION_addAccessibilityInteractionConnection = 6;
        static final int TRANSACTION_addClient = 1;
        static final int TRANSACTION_assistantMenuRegister = 23;
        static final int TRANSACTION_assistantMenuUpdate = 22;
        static final int TRANSACTION_disableMagnifier = 29;
        static final int TRANSACTION_enableMagnifier = 27;
        static final int TRANSACTION_enableMagnifierByDisplayID = 28;
        static final int TRANSACTION_getClearableActiveNotificationsCount = 36;
        static final int TRANSACTION_getEnabledAccessibilityServiceList = 4;
        static final int TRANSACTION_getInstalledAccessibilityServiceList = 3;
        static final int TRANSACTION_getWindowToken = 11;
        static final int TRANSACTION_interrupt = 5;
        static final int TRANSACTION_isColorBlind = 13;
        static final int TRANSACTION_isScreenCurtainRunning = 20;
        static final int TRANSACTION_isTwoFingerGestureRecognitionEnabled = 17;
        static final int TRANSACTION_isUniversalSwitchEnabled = 18;
        static final int TRANSACTION_openGlobalActions = 26;
        static final int TRANSACTION_reboot = 25;
        static final int TRANSACTION_registerGestureListenerForLauncher = 32;
        static final int TRANSACTION_registerUiTestAutomationService = 8;
        static final int TRANSACTION_removeAccessibilityInteractionConnection = 7;
        static final int TRANSACTION_sendAccessibilityEvent = 2;
        static final int TRANSACTION_setColorBlind = 12;
        static final int TRANSACTION_setMagnificationSpec = 30;
        static final int TRANSACTION_setScreenCurtain = 21;
        static final int TRANSACTION_setTalkbackMode = 19;
        static final int TRANSACTION_setTwoFingerGestureRecognitionEnabled = 31;
        static final int TRANSACTION_setmDNIeAccessibilityMode = 16;
        static final int TRANSACTION_setmDNIeColorBlind = 14;
        static final int TRANSACTION_setmDNIeNegative = 15;
        static final int TRANSACTION_shutdown = 24;
        static final int TRANSACTION_temporaryEnableAccessibilityStateUntilKeyguardRemoved = 10;
        static final int TRANSACTION_unregisterGestureListenerForLauncher = 33;
        static final int TRANSACTION_unregisterUiTestAutomationService = 9;

        private static class Proxy implements IAccessibilityManager {
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

            public int addClient(IAccessibilityManagerClient client, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean sendAccessibilityEvent(AccessibilityEvent uiEvent, int userId) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uiEvent != null) {
                        _data.writeInt(1);
                        uiEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(2, _data, _reply, 0);
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

            public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    List<AccessibilityServiceInfo> _result = _reply.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(feedbackType);
                    _data.writeInt(userId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<AccessibilityServiceInfo> _result = _reply.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void interrupt(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addAccessibilityInteractionConnection(IWindow windowToken, IAccessibilityInteractionConnection connection, int userId) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    IBinder asBinder;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (windowToken != null) {
                        asBinder = windowToken.asBinder();
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    if (connection != null) {
                        iBinder = connection.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeAccessibilityInteractionConnection(IWindow windowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken != null ? windowToken.asBinder() : null);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerUiTestAutomationService(IBinder owner, IAccessibilityServiceClient client, AccessibilityServiceInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(owner);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterUiTestAutomationService(IAccessibilityServiceClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName service, boolean touchExplorationEnabled) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!touchExplorationEnabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder getWindowToken(int windowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setColorBlind(boolean enable, float userParameter) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeFloat(userParameter);
                    this.mRemote.transact(12, _data, _reply, 0);
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

            public boolean isColorBlind(int[] nums) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(nums);
                    this.mRemote.transact(13, _data, _reply, 0);
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

            public boolean setmDNIeColorBlind(boolean enable, int[] result) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeIntArray(result);
                    this.mRemote.transact(14, _data, _reply, 0);
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

            public boolean setmDNIeNegative(boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(15, _data, _reply, 0);
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

            public boolean setmDNIeAccessibilityMode(int mode, boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(16, _data, _reply, 0);
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

            public boolean isTwoFingerGestureRecognitionEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
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

            public boolean isUniversalSwitchEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
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

            public void setTalkbackMode() throws RemoteException {
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

            public boolean isScreenCurtainRunning() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
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

            public void setScreenCurtain() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void assistantMenuUpdate(Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void assistantMenuRegister(IBinder iBinder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown(boolean isConfirm) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isConfirm) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reboot(boolean isConfirm) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isConfirm) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void openGlobalActions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableMagnifier(int width, int height, float scale) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeFloat(scale);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableMagnifierByDisplayID(int width, int height, float scale, int displayid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeFloat(scale);
                    _data.writeInt(displayid);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void disableMagnifier() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMagnificationSpec(MagnificationSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (spec != null) {
                        _data.writeInt(1);
                        spec.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTwoFingerGestureRecognitionEnabled(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean registerGestureListenerForLauncher(IBinder iBinder) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(32, _data, _reply, 0);
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

            public void unregisterGestureListenerForLauncher() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean OnStartGestureWakeup() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
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

            public boolean OnStopGestureWakeup() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
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

            public int getClearableActiveNotificationsCount(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccessibilityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAccessibilityManager)) {
                return new Proxy(obj);
            }
            return (IAccessibilityManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int _result;
            boolean _result2;
            List<AccessibilityServiceInfo> _result3;
            boolean _arg1;
            boolean _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _result = addClient(android.view.accessibility.IAccessibilityManagerClient.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    AccessibilityEvent _arg02;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (AccessibilityEvent) AccessibilityEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _result2 = sendAccessibilityEvent(_arg02, data.readInt());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getInstalledAccessibilityServiceList(data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getEnabledAccessibilityServiceList(data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result3);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    interrupt(data.readInt());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = addAccessibilityInteractionConnection(android.view.IWindow.Stub.asInterface(data.readStrongBinder()), android.view.accessibility.IAccessibilityInteractionConnection.Stub.asInterface(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    removeAccessibilityInteractionConnection(android.view.IWindow.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 8:
                    AccessibilityServiceInfo _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg03 = data.readStrongBinder();
                    IAccessibilityServiceClient _arg12 = android.accessibilityservice.IAccessibilityServiceClient.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg2 = (AccessibilityServiceInfo) AccessibilityServiceInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    registerUiTestAutomationService(_arg03, _arg12, _arg2);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterUiTestAutomationService(android.accessibilityservice.IAccessibilityServiceClient.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 10:
                    ComponentName _arg04;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    temporaryEnableAccessibilityStateUntilKeyguardRemoved(_arg04, _arg1);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _result4 = getWindowToken(data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(_result4);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    _result2 = setColorBlind(_arg0, data.readFloat());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isColorBlind(data.createIntArray());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    _result2 = setmDNIeColorBlind(_arg0, data.createIntArray());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    _result2 = setmDNIeNegative(_arg0);
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _result2 = setmDNIeAccessibilityMode(_arg05, _arg1);
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isTwoFingerGestureRecognitionEnabled();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isUniversalSwitchEnabled();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    setTalkbackMode();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isScreenCurtainRunning();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    setScreenCurtain();
                    reply.writeNoException();
                    return true;
                case 22:
                    Bundle _arg06;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    assistantMenuUpdate(_arg06);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    assistantMenuRegister(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    shutdown(_arg0);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    reboot(_arg0);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    openGlobalActions();
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    enableMagnifier(data.readInt(), data.readInt(), data.readFloat());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    enableMagnifierByDisplayID(data.readInt(), data.readInt(), data.readFloat(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    disableMagnifier();
                    reply.writeNoException();
                    return true;
                case 30:
                    MagnificationSpec _arg07;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = (MagnificationSpec) MagnificationSpec.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    setMagnificationSpec(_arg07);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    setTwoFingerGestureRecognitionEnabled(_arg0);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = registerGestureListenerForLauncher(data.readStrongBinder());
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterGestureListenerForLauncher();
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = OnStartGestureWakeup();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = OnStopGestureWakeup();
                    reply.writeNoException();
                    if (_result2) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getClearableActiveNotificationsCount(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean OnStartGestureWakeup() throws RemoteException;

    boolean OnStopGestureWakeup() throws RemoteException;

    int addAccessibilityInteractionConnection(IWindow iWindow, IAccessibilityInteractionConnection iAccessibilityInteractionConnection, int i) throws RemoteException;

    int addClient(IAccessibilityManagerClient iAccessibilityManagerClient, int i) throws RemoteException;

    void assistantMenuRegister(IBinder iBinder) throws RemoteException;

    void assistantMenuUpdate(Bundle bundle) throws RemoteException;

    void disableMagnifier() throws RemoteException;

    void enableMagnifier(int i, int i2, float f) throws RemoteException;

    void enableMagnifierByDisplayID(int i, int i2, float f, int i3) throws RemoteException;

    int getClearableActiveNotificationsCount(String str) throws RemoteException;

    List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int i, int i2) throws RemoteException;

    List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int i) throws RemoteException;

    IBinder getWindowToken(int i) throws RemoteException;

    void interrupt(int i) throws RemoteException;

    boolean isColorBlind(int[] iArr) throws RemoteException;

    boolean isScreenCurtainRunning() throws RemoteException;

    boolean isTwoFingerGestureRecognitionEnabled() throws RemoteException;

    boolean isUniversalSwitchEnabled() throws RemoteException;

    void openGlobalActions() throws RemoteException;

    void reboot(boolean z) throws RemoteException;

    boolean registerGestureListenerForLauncher(IBinder iBinder) throws RemoteException;

    void registerUiTestAutomationService(IBinder iBinder, IAccessibilityServiceClient iAccessibilityServiceClient, AccessibilityServiceInfo accessibilityServiceInfo) throws RemoteException;

    void removeAccessibilityInteractionConnection(IWindow iWindow) throws RemoteException;

    boolean sendAccessibilityEvent(AccessibilityEvent accessibilityEvent, int i) throws RemoteException;

    boolean setColorBlind(boolean z, float f) throws RemoteException;

    void setMagnificationSpec(MagnificationSpec magnificationSpec) throws RemoteException;

    void setScreenCurtain() throws RemoteException;

    void setTalkbackMode() throws RemoteException;

    void setTwoFingerGestureRecognitionEnabled(boolean z) throws RemoteException;

    boolean setmDNIeAccessibilityMode(int i, boolean z) throws RemoteException;

    boolean setmDNIeColorBlind(boolean z, int[] iArr) throws RemoteException;

    boolean setmDNIeNegative(boolean z) throws RemoteException;

    void shutdown(boolean z) throws RemoteException;

    void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName componentName, boolean z) throws RemoteException;

    void unregisterGestureListenerForLauncher() throws RemoteException;

    void unregisterUiTestAutomationService(IAccessibilityServiceClient iAccessibilityServiceClient) throws RemoteException;
}
