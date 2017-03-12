package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBluetoothManager extends IInterface {

    public static abstract class Stub extends Binder implements IBluetoothManager {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothManager";
        static final int TRANSACTION_bindBluetoothProfileService = 11;
        static final int TRANSACTION_disable = 9;
        static final int TRANSACTION_dumpInFile = 28;
        static final int TRANSACTION_enable = 6;
        static final int TRANSACTION_enableForChinaModel = 7;
        static final int TRANSACTION_enableNoAutoConnect = 8;
        static final int TRANSACTION_getAddress = 13;
        static final int TRANSACTION_getBleAppCount = 16;
        static final int TRANSACTION_getBluetoothGatt = 10;
        static final int TRANSACTION_getName = 14;
        static final int TRANSACTION_getSamsungBleAppCount = 20;
        static final int TRANSACTION_getSettingsName = 26;
        static final int TRANSACTION_isBleAppPresent = 23;
        static final int TRANSACTION_isBleScanAlwaysAvailable = 15;
        static final int TRANSACTION_isEnabled = 5;
        static final int TRANSACTION_putLogs = 27;
        static final int TRANSACTION_registerAdapter = 1;
        static final int TRANSACTION_registerStateChangeCallback = 3;
        static final int TRANSACTION_registerStateDisableBleCallback = 17;
        static final int TRANSACTION_sendDisableBleCallback = 19;
        static final int TRANSACTION_setBtEnableFlag = 24;
        static final int TRANSACTION_shutdown = 25;
        static final int TRANSACTION_unbindBluetoothProfileService = 12;
        static final int TRANSACTION_unregisterAdapter = 2;
        static final int TRANSACTION_unregisterStateChangeCallback = 4;
        static final int TRANSACTION_unregisterStateDisableBleCallback = 18;
        static final int TRANSACTION_updateBleAppCount = 21;
        static final int TRANSACTION_updateSBleAppCount = 22;

        private static class Proxy implements IBluetoothManager {
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

            public IBluetooth registerAdapter(IBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    IBluetooth _result = android.bluetooth.IBluetooth.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterAdapter(IBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnabled() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public boolean enable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
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

            public boolean enableForChinaModel() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
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

            public boolean enableNoAutoConnect() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
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

            public boolean disable(boolean persist) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (persist) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(9, _data, _reply, 0);
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

            public IBluetoothGatt getBluetoothGatt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    IBluetoothGatt _result = android.bluetooth.IBluetoothGatt.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean bindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    _data.writeStrongBinder(proxy != null ? proxy.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
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

            public void unbindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    _data.writeStrongBinder(proxy != null ? proxy.asBinder() : null);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBleScanAlwaysAvailable() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
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

            public int getBleAppCount() throws RemoteException {
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

            public void registerStateDisableBleCallback(IBluetoothDisableBleCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterStateDisableBleCallback(IBluetoothDisableBleCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendDisableBleCallback() throws RemoteException {
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

            public int getSamsungBleAppCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int updateBleAppCount(IBinder b, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(b);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int updateSBleAppCount(IBinder b, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(b);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBleAppPresent() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
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

            public boolean setBtEnableFlag(boolean quietmode) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    int i;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (quietmode) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(24, _data, _reply, 0);
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

            public boolean shutdown() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
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

            public String getSettingsName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void putLogs(String cmd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(cmd);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dumpInFile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
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

        public static IBluetoothManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothManager)) {
                return new Proxy(obj);
            }
            return (IBluetoothManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            IBinder iBinder = null;
            int i = 0;
            boolean _result;
            boolean _arg0;
            String _result2;
            int _result3;
            IBinder _arg02;
            boolean _arg1;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetooth _result4 = registerAdapter(android.bluetooth.IBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result4 != null) {
                        iBinder = _result4.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterAdapter(android.bluetooth.IBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    registerStateChangeCallback(android.bluetooth.IBluetoothStateChangeCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterStateChangeCallback(android.bluetooth.IBluetoothStateChangeCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isEnabled();
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enable();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enableForChinaModel();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _result = enableNoAutoConnect();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    _result = disable(_arg0);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IBluetoothGatt _result5 = getBluetoothGatt();
                    reply.writeNoException();
                    if (_result5 != null) {
                        iBinder = _result5.asBinder();
                    }
                    reply.writeStrongBinder(iBinder);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _result = bindBluetoothProfileService(data.readInt(), android.bluetooth.IBluetoothProfileServiceConnection.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    unbindBluetoothProfileService(data.readInt(), android.bluetooth.IBluetoothProfileServiceConnection.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getAddress();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getName();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isBleScanAlwaysAvailable();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getBleAppCount();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    registerStateDisableBleCallback(android.bluetooth.IBluetoothDisableBleCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterStateDisableBleCallback(android.bluetooth.IBluetoothDisableBleCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    sendDisableBleCallback();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _result3 = getSamsungBleAppCount();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _result3 = updateBleAppCount(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    } else {
                        _arg1 = false;
                    }
                    _result3 = updateSBleAppCount(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    _result = isBleAppPresent();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    _result = setBtEnableFlag(_arg0);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    _result = shutdown();
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getSettingsName();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    putLogs(data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    dumpInFile();
                    reply.writeNoException();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean bindBluetoothProfileService(int i, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) throws RemoteException;

    boolean disable(boolean z) throws RemoteException;

    void dumpInFile() throws RemoteException;

    boolean enable() throws RemoteException;

    boolean enableForChinaModel() throws RemoteException;

    boolean enableNoAutoConnect() throws RemoteException;

    String getAddress() throws RemoteException;

    int getBleAppCount() throws RemoteException;

    IBluetoothGatt getBluetoothGatt() throws RemoteException;

    String getName() throws RemoteException;

    int getSamsungBleAppCount() throws RemoteException;

    String getSettingsName() throws RemoteException;

    boolean isBleAppPresent() throws RemoteException;

    boolean isBleScanAlwaysAvailable() throws RemoteException;

    boolean isEnabled() throws RemoteException;

    void putLogs(String str) throws RemoteException;

    IBluetooth registerAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) throws RemoteException;

    void registerStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) throws RemoteException;

    void registerStateDisableBleCallback(IBluetoothDisableBleCallback iBluetoothDisableBleCallback) throws RemoteException;

    void sendDisableBleCallback() throws RemoteException;

    boolean setBtEnableFlag(boolean z) throws RemoteException;

    boolean shutdown() throws RemoteException;

    void unbindBluetoothProfileService(int i, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) throws RemoteException;

    void unregisterAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) throws RemoteException;

    void unregisterStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) throws RemoteException;

    void unregisterStateDisableBleCallback(IBluetoothDisableBleCallback iBluetoothDisableBleCallback) throws RemoteException;

    int updateBleAppCount(IBinder iBinder, boolean z) throws RemoteException;

    int updateSBleAppCount(IBinder iBinder, boolean z) throws RemoteException;
}
