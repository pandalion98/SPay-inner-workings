package android.hardware.input;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.InputChannel;
import android.view.InputDevice;
import android.view.InputEvent;

public interface IInputManager extends IInterface {

    public static abstract class Stub extends Binder implements IInputManager {
        private static final String DESCRIPTOR = "android.hardware.input.IInputManager";
        static final int TRANSACTION_addKeyboardLayoutForInputDevice = 13;
        static final int TRANSACTION_addOrRemoveVirtualGamePadDevice = 41;
        static final int TRANSACTION_cancelVibrate = 19;
        static final int TRANSACTION_coverEventFinished = 26;
        static final int TRANSACTION_enablePatialScreen = 37;
        static final int TRANSACTION_getCurrentKeyboardLayoutForInputDevice = 10;
        static final int TRANSACTION_getInboundQueueLength = 25;
        static final int TRANSACTION_getInputDevice = 1;
        static final int TRANSACTION_getInputDeviceIds = 2;
        static final int TRANSACTION_getKeyboardLayout = 9;
        static final int TRANSACTION_getKeyboardLayouts = 8;
        static final int TRANSACTION_getKeyboardLayoutsForInputDevice = 12;
        static final int TRANSACTION_getScanCodeState = 27;
        static final int TRANSACTION_getSmartHallFlipState = 38;
        static final int TRANSACTION_getTouchCalibrationForInputDevice = 6;
        static final int TRANSACTION_hasKeys = 3;
        static final int TRANSACTION_injectInputEvent = 5;
        static final int TRANSACTION_isInTabletMode = 16;
        static final int TRANSACTION_monitorInput = 28;
        static final int TRANSACTION_registerHoveringSpenCustomIcon = 29;
        static final int TRANSACTION_registerInputDevicesChangedListener = 15;
        static final int TRANSACTION_registerMouseCustomIcon = 33;
        static final int TRANSACTION_registerTabletModeChangedListener = 17;
        static final int TRANSACTION_reloadMousePointerIcon = 35;
        static final int TRANSACTION_reloadMousePointerIconForBitmap = 36;
        static final int TRANSACTION_reloadPointerIcon = 31;
        static final int TRANSACTION_reloadPointerIconForBitmap = 32;
        static final int TRANSACTION_removeHoveringSpenCustomIcon = 30;
        static final int TRANSACTION_removeKeyboardLayoutForInputDevice = 14;
        static final int TRANSACTION_removeMouseCustomIcon = 34;
        static final int TRANSACTION_setCoverVerify = 24;
        static final int TRANSACTION_setCurrentKeyboardLayoutForInputDevice = 11;
        static final int TRANSACTION_setCustomHoverIcon = 21;
        static final int TRANSACTION_setEnableTSP = 42;
        static final int TRANSACTION_setFlipCoverTouchEnabled = 23;
        static final int TRANSACTION_setLedState = 20;
        static final int TRANSACTION_setMonitorChannelFilter = 40;
        static final int TRANSACTION_setStartedShutdown = 22;
        static final int TRANSACTION_setTouchCalibrationForInputDevice = 7;
        static final int TRANSACTION_setWakeKeyDynamically = 39;
        static final int TRANSACTION_tryPointerSpeed = 4;
        static final int TRANSACTION_vibrate = 18;

        private static class Proxy implements IInputManager {
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

            public InputDevice getInputDevice(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    InputDevice _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (InputDevice) InputDevice.CREATOR.createFromParcel(_reply);
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

            public int[] getInputDeviceIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasKeys(int deviceId, int sourceMask, int[] keyCodes, boolean[] keyExists) throws RemoteException {
                boolean z = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(sourceMask);
                    _data.writeIntArray(keyCodes);
                    if (keyExists == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(keyExists.length);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    _reply.readBooleanArray(keyExists);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void tryPointerSpeed(int speed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(speed);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean injectInputEvent(InputEvent ev, int mode) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ev != null) {
                        _data.writeInt(1);
                        ev.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    this.mRemote.transact(5, _data, _reply, 0);
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

            public TouchCalibration getTouchCalibrationForInputDevice(String inputDeviceDescriptor, int rotation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    TouchCalibration _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputDeviceDescriptor);
                    _data.writeInt(rotation);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (TouchCalibration) TouchCalibration.CREATOR.createFromParcel(_reply);
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

            public void setTouchCalibrationForInputDevice(String inputDeviceDescriptor, int rotation, TouchCalibration calibration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputDeviceDescriptor);
                    _data.writeInt(rotation);
                    if (calibration != null) {
                        _data.writeInt(1);
                        calibration.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeyboardLayout[] getKeyboardLayouts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    KeyboardLayout[] _result = (KeyboardLayout[]) _reply.createTypedArray(KeyboardLayout.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public KeyboardLayout getKeyboardLayout(String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    KeyboardLayout _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keyboardLayoutDescriptor);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (KeyboardLayout) KeyboardLayout.CREATOR.createFromParcel(_reply);
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

            public String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(keyboardLayoutDescriptor);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier identifier) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(keyboardLayoutDescriptor);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier identifier, String keyboardLayoutDescriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (identifier != null) {
                        _data.writeInt(1);
                        identifier.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(keyboardLayoutDescriptor);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerInputDevicesChangedListener(IInputDevicesChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isInTabletMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTabletModeChangedListener(ITabletModeChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void vibrate(int deviceId, long[] pattern, int repeat, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeLongArray(pattern);
                    _data.writeInt(repeat);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelVibrate(int deviceId, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setLedState(boolean on) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (on) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCustomHoverIcon(Bitmap icon, int hotSpotX, int hotSpotY) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(hotSpotX);
                    _data.writeInt(hotSpotY);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStartedShutdown(boolean isStarted) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isStarted) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFlipCoverTouchEnabled(boolean enabled) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enabled) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCoverVerify(int verify) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(verify);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getInboundQueueLength() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void coverEventFinished() throws RemoteException {
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

            public int getScanCodeState(int deviceId, int sourceMask, int scanCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    _data.writeInt(sourceMask);
                    _data.writeInt(scanCode);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public InputChannel monitorInput(String inputChannelName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    InputChannel _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(inputChannelName);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (InputChannel) InputChannel.CREATOR.createFromParcel(_reply);
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

            public int registerHoveringSpenCustomIcon(Bitmap bitmap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bitmap != null) {
                        _data.writeInt(1);
                        bitmap.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeHoveringSpenCustomIcon(int customIconId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(customIconId);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reloadPointerIcon(int pointerType, int iconType, int customIconId, Point hotSpotPoint, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pointerType);
                    _data.writeInt(iconType);
                    _data.writeInt(customIconId);
                    if (hotSpotPoint != null) {
                        _data.writeInt(1);
                        hotSpotPoint.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flag);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int reloadPointerIconForBitmap(int pointerType, int iconType, Bitmap bitmap, Point hotSpotPoint, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pointerType);
                    _data.writeInt(iconType);
                    if (bitmap != null) {
                        _data.writeInt(1);
                        bitmap.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (hotSpotPoint != null) {
                        _data.writeInt(1);
                        hotSpotPoint.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flag);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int registerMouseCustomIcon(Bitmap bitmap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bitmap != null) {
                        _data.writeInt(1);
                        bitmap.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeMouseCustomIcon(int customIconId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(customIconId);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reloadMousePointerIcon(int pointerType, int iconType, int customIconId, Point hotSpotPoint, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pointerType);
                    _data.writeInt(iconType);
                    _data.writeInt(customIconId);
                    if (hotSpotPoint != null) {
                        _data.writeInt(1);
                        hotSpotPoint.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flag);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int reloadMousePointerIconForBitmap(int pointerType, int iconType, Bitmap bitmap, Point hotSpotPoint, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pointerType);
                    _data.writeInt(iconType);
                    if (bitmap != null) {
                        _data.writeInt(1);
                        bitmap.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (hotSpotPoint != null) {
                        _data.writeInt(1);
                        hotSpotPoint.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flag);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enablePatialScreen(boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getSmartHallFlipState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setWakeKeyDynamically(String packagename, boolean isPut, String keycodes) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packagename);
                    if (isPut) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(keycodes);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMonitorChannelFilter(InputChannel inputChannel, int monitorFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (inputChannel != null) {
                        _data.writeInt(1);
                        inputChannel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(monitorFilter);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addOrRemoveVirtualGamePadDevice(boolean add) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (add) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setEnableTSP(int cmdtype, boolean enable) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmdtype);
                    if (enable) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(42, _data, _reply, 0);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInputManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInputManager)) {
                return new Proxy(obj);
            }
            return (IInputManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            int _arg0;
            int _arg1;
            boolean _result;
            String _arg02;
            InputDeviceIdentifier _arg03;
            int _result2;
            boolean _arg04;
            Bitmap _arg05;
            int _arg2;
            Point _arg3;
            Bitmap _arg22;
            boolean _arg12;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    InputDevice _result3 = getInputDevice(data.readInt());
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _result4 = getInputDeviceIds();
                    reply.writeNoException();
                    reply.writeIntArray(_result4);
                    return true;
                case 3:
                    boolean[] _arg32;
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readInt();
                    int[] _arg23 = data.createIntArray();
                    int _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg32 = null;
                    } else {
                        _arg32 = new boolean[_arg3_length];
                    }
                    _result = hasKeys(_arg0, _arg1, _arg23, _arg32);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    reply.writeBooleanArray(_arg32);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    tryPointerSpeed(data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    InputEvent _arg06;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = (InputEvent) InputEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    _result = injectInputEvent(_arg06, data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    TouchCalibration _result5 = getTouchCalibrationForInputDevice(data.readString(), data.readInt());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 7:
                    TouchCalibration _arg24;
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    _arg1 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg24 = (TouchCalibration) TouchCalibration.CREATOR.createFromParcel(data);
                    } else {
                        _arg24 = null;
                    }
                    setTouchCalibrationForInputDevice(_arg02, _arg1, _arg24);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    KeyboardLayout[] _result6 = getKeyboardLayouts();
                    reply.writeNoException();
                    reply.writeTypedArray(_result6, 1);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    KeyboardLayout _result7 = getKeyboardLayout(data.readString());
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    String _result8 = getCurrentKeyboardLayoutForInputDevice(_arg03);
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    setCurrentKeyboardLayoutForInputDevice(_arg03, data.readString());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    String[] _result9 = getKeyboardLayoutsForInputDevice(_arg03);
                    reply.writeNoException();
                    reply.writeStringArray(_result9);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    addKeyboardLayoutForInputDevice(_arg03, data.readString());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (InputDeviceIdentifier) InputDeviceIdentifier.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    removeKeyboardLayoutForInputDevice(_arg03, data.readString());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    registerInputDevicesChangedListener(android.hardware.input.IInputDevicesChangedListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = isInTabletMode();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    registerTabletModeChangedListener(android.hardware.input.ITabletModeChangedListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    vibrate(data.readInt(), data.createLongArray(), data.readInt(), data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    cancelVibrate(data.readInt(), data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = true;
                    } else {
                        _arg04 = false;
                    }
                    _result2 = setLedState(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    setCustomHoverIcon(_arg05, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = true;
                    } else {
                        _arg04 = false;
                    }
                    setStartedShutdown(_arg04);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = true;
                    } else {
                        _arg04 = false;
                    }
                    setFlipCoverTouchEnabled(_arg04);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    setCoverVerify(data.readInt());
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getInboundQueueLength();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    coverEventFinished();
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getScanCodeState(data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    InputChannel _result10 = monitorInput(data.readString());
                    reply.writeNoException();
                    if (_result10 != null) {
                        reply.writeInt(1);
                        _result10.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    _result2 = registerHoveringSpenCustomIcon(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    removeHoveringSpenCustomIcon(data.readInt());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    reloadPointerIcon(_arg0, _arg1, _arg2, _arg3, data.readInt());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg22 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _result2 = reloadPointerIconForBitmap(_arg0, _arg1, _arg22, _arg3, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    _result2 = registerMouseCustomIcon(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    removeMouseCustomIcon(data.readInt());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readInt();
                    _arg2 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg3 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    reloadMousePointerIcon(_arg0, _arg1, _arg2, _arg3, data.readInt());
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    _arg1 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg22 = (Bitmap) Bitmap.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = (Point) Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    _result2 = reloadMousePointerIconForBitmap(_arg0, _arg1, _arg22, _arg3, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = true;
                    } else {
                        _arg04 = false;
                    }
                    enablePatialScreen(_arg04);
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getSmartHallFlipState();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    setWakeKeyDynamically(_arg02, _arg12, data.readString());
                    reply.writeNoException();
                    return true;
                case 40:
                    InputChannel _arg07;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = (InputChannel) InputChannel.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    setMonitorChannelFilter(_arg07, data.readInt());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = true;
                    } else {
                        _arg04 = false;
                    }
                    _result2 = addOrRemoveVirtualGamePadDevice(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    _result = setEnableTSP(_arg0, _arg12);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void addKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) throws RemoteException;

    int addOrRemoveVirtualGamePadDevice(boolean z) throws RemoteException;

    void cancelVibrate(int i, IBinder iBinder) throws RemoteException;

    void coverEventFinished() throws RemoteException;

    void enablePatialScreen(boolean z) throws RemoteException;

    String getCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier) throws RemoteException;

    int getInboundQueueLength() throws RemoteException;

    InputDevice getInputDevice(int i) throws RemoteException;

    int[] getInputDeviceIds() throws RemoteException;

    KeyboardLayout getKeyboardLayout(String str) throws RemoteException;

    KeyboardLayout[] getKeyboardLayouts() throws RemoteException;

    String[] getKeyboardLayoutsForInputDevice(InputDeviceIdentifier inputDeviceIdentifier) throws RemoteException;

    int getScanCodeState(int i, int i2, int i3) throws RemoteException;

    int getSmartHallFlipState() throws RemoteException;

    TouchCalibration getTouchCalibrationForInputDevice(String str, int i) throws RemoteException;

    boolean hasKeys(int i, int i2, int[] iArr, boolean[] zArr) throws RemoteException;

    boolean injectInputEvent(InputEvent inputEvent, int i) throws RemoteException;

    int isInTabletMode() throws RemoteException;

    InputChannel monitorInput(String str) throws RemoteException;

    int registerHoveringSpenCustomIcon(Bitmap bitmap) throws RemoteException;

    void registerInputDevicesChangedListener(IInputDevicesChangedListener iInputDevicesChangedListener) throws RemoteException;

    int registerMouseCustomIcon(Bitmap bitmap) throws RemoteException;

    void registerTabletModeChangedListener(ITabletModeChangedListener iTabletModeChangedListener) throws RemoteException;

    void reloadMousePointerIcon(int i, int i2, int i3, Point point, int i4) throws RemoteException;

    int reloadMousePointerIconForBitmap(int i, int i2, Bitmap bitmap, Point point, int i3) throws RemoteException;

    void reloadPointerIcon(int i, int i2, int i3, Point point, int i4) throws RemoteException;

    int reloadPointerIconForBitmap(int i, int i2, Bitmap bitmap, Point point, int i3) throws RemoteException;

    void removeHoveringSpenCustomIcon(int i) throws RemoteException;

    void removeKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) throws RemoteException;

    void removeMouseCustomIcon(int i) throws RemoteException;

    void setCoverVerify(int i) throws RemoteException;

    void setCurrentKeyboardLayoutForInputDevice(InputDeviceIdentifier inputDeviceIdentifier, String str) throws RemoteException;

    void setCustomHoverIcon(Bitmap bitmap, int i, int i2) throws RemoteException;

    boolean setEnableTSP(int i, boolean z) throws RemoteException;

    void setFlipCoverTouchEnabled(boolean z) throws RemoteException;

    int setLedState(boolean z) throws RemoteException;

    void setMonitorChannelFilter(InputChannel inputChannel, int i) throws RemoteException;

    void setStartedShutdown(boolean z) throws RemoteException;

    void setTouchCalibrationForInputDevice(String str, int i, TouchCalibration touchCalibration) throws RemoteException;

    void setWakeKeyDynamically(String str, boolean z, String str2) throws RemoteException;

    void tryPointerSpeed(int i) throws RemoteException;

    void vibrate(int i, long[] jArr, int i2, IBinder iBinder) throws RemoteException;
}
