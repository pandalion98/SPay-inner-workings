package android.net.wifi.passpoint;

import android.net.wifi.ScanResult;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IWifiPasspointManager extends IInterface {

    public static abstract class Stub extends Binder implements IWifiPasspointManager {
        private static final String DESCRIPTOR = "android.net.wifi.passpoint.IWifiPasspointManager";
        static final int TRANSACTION_addCredential = 5;
        static final int TRANSACTION_getCredentials = 4;
        static final int TRANSACTION_getMessenger = 1;
        static final int TRANSACTION_getPasspointState = 2;
        static final int TRANSACTION_removeCredential = 7;
        static final int TRANSACTION_requestCredentialMatch = 3;
        static final int TRANSACTION_updateCredential = 6;

        private static class Proxy implements IWifiPasspointManager {
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

            public Messenger getMessenger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    Messenger _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Messenger) Messenger.CREATOR.createFromParcel(_reply);
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

            public int getPasspointState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<WifiPasspointPolicy> requestCredentialMatch(List<ScanResult> requested) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(requested);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    List<WifiPasspointPolicy> _result = _reply.createTypedArrayList(WifiPasspointPolicy.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<WifiPasspointCredential> getCredentials() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    List<WifiPasspointCredential> _result = _reply.createTypedArrayList(WifiPasspointCredential.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean addCredential(WifiPasspointCredential cred) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cred != null) {
                        _data.writeInt(1);
                        cred.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
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

            public boolean updateCredential(WifiPasspointCredential cred) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cred != null) {
                        _data.writeInt(1);
                        cred.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
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

            public boolean removeCredential(WifiPasspointCredential cred) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cred != null) {
                        _data.writeInt(1);
                        cred.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, _reply, 0);
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

        public static IWifiPasspointManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWifiPasspointManager)) {
                return new Proxy(obj);
            }
            return (IWifiPasspointManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            WifiPasspointCredential _arg0;
            boolean _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    Messenger _result2 = getMessenger();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _result3 = getPasspointState();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    List<WifiPasspointPolicy> _result4 = requestCredentialMatch(data.createTypedArrayList(ScanResult.CREATOR));
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    List<WifiPasspointCredential> _result5 = getCredentials();
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (WifiPasspointCredential) WifiPasspointCredential.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = addCredential(_arg0);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (WifiPasspointCredential) WifiPasspointCredential.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = updateCredential(_arg0);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (WifiPasspointCredential) WifiPasspointCredential.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    _result = removeCredential(_arg0);
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

    boolean addCredential(WifiPasspointCredential wifiPasspointCredential) throws RemoteException;

    List<WifiPasspointCredential> getCredentials() throws RemoteException;

    Messenger getMessenger() throws RemoteException;

    int getPasspointState() throws RemoteException;

    boolean removeCredential(WifiPasspointCredential wifiPasspointCredential) throws RemoteException;

    List<WifiPasspointPolicy> requestCredentialMatch(List<ScanResult> list) throws RemoteException;

    boolean updateCredential(WifiPasspointCredential wifiPasspointCredential) throws RemoteException;
}
