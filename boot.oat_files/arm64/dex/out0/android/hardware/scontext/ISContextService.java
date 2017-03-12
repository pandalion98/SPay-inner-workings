package android.hardware.scontext;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.Map;

public interface ISContextService extends IInterface {

    public static abstract class Stub extends Binder implements ISContextService {
        private static final String DESCRIPTOR = "android.hardware.scontext.ISContextService";
        static final int TRANSACTION_changeParameters = 4;
        static final int TRANSACTION_getAvailableServiceMap = 5;
        static final int TRANSACTION_getMotionRecognitionService = 9;
        static final int TRANSACTION_initializeService = 3;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_requestHistoryData = 8;
        static final int TRANSACTION_requestToUpdate = 7;
        static final int TRANSACTION_setReferenceData = 6;
        static final int TRANSACTION_unregisterCallback = 2;

        private static class Proxy implements ISContextService {
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

            public void registerCallback(IBinder binder, int service, SContextAttribute property) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(service);
                    if (property != null) {
                        _data.writeInt(1);
                        property.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unregisterCallback(IBinder binder, int service) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(service);
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

            public void initializeService(IBinder binder, int service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(service);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean changeParameters(IBinder binder, int service, SContextAttribute attribute) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(service);
                    if (attribute != null) {
                        _data.writeInt(1);
                        attribute.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
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

            public Map getAvailableServiceMap() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    Map _result = _reply.readHashMap(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setReferenceData(int data_type, byte[] data) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(data_type);
                    _data.writeByteArray(data);
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

            public void requestToUpdate(IBinder binder, int service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(service);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestHistoryData(IBinder binder, int service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(binder);
                    _data.writeInt(service);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder getMotionRecognitionService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
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

        public static ISContextService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISContextService)) {
                return new Proxy(obj);
            }
            return (ISContextService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = 0;
            IBinder _arg0;
            int _arg1;
            SContextAttribute _arg2;
            boolean _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    _arg1 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (SContextAttribute) SContextAttribute.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    registerCallback(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = unregisterCallback(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    initializeService(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readStrongBinder();
                    _arg1 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (SContextAttribute) SContextAttribute.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    _result = changeParameters(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    Map _result2 = getAvailableServiceMap();
                    reply.writeNoException();
                    reply.writeMap(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = setReferenceData(data.readInt(), data.createByteArray());
                    reply.writeNoException();
                    if (_result) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    requestToUpdate(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    requestHistoryData(data.readStrongBinder(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _result3 = getMotionRecognitionService();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result3);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    boolean changeParameters(IBinder iBinder, int i, SContextAttribute sContextAttribute) throws RemoteException;

    Map getAvailableServiceMap() throws RemoteException;

    IBinder getMotionRecognitionService() throws RemoteException;

    void initializeService(IBinder iBinder, int i) throws RemoteException;

    void registerCallback(IBinder iBinder, int i, SContextAttribute sContextAttribute) throws RemoteException;

    void requestHistoryData(IBinder iBinder, int i) throws RemoteException;

    void requestToUpdate(IBinder iBinder, int i) throws RemoteException;

    boolean setReferenceData(int i, byte[] bArr) throws RemoteException;

    boolean unregisterCallback(IBinder iBinder, int i) throws RemoteException;
}
