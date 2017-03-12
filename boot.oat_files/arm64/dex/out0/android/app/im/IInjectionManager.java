package android.app.im;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
import java.util.Map;

public interface IInjectionManager extends IInterface {

    public static abstract class Stub extends Binder implements IInjectionManager {
        private static final String DESCRIPTOR = "android.app.im.IInjectionManager";
        static final int TRANSACTION_getClassLibPath = 3;
        static final int TRANSACTION_getFeatureInfo = 1;
        static final int TRANSACTION_getPackageFeatures = 2;

        private static class Proxy implements IInjectionManager {
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

            public List getFeatureInfo(String packageName, String target, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(target);
                    _data.writeString(type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List _result = _reply.readArrayList(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getPackageFeatures(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    Map _result = _reply.readHashMap(getClass().getClassLoader());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Map getClassLibPath(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    Map _result = _reply.readHashMap(getClass().getClassLoader());
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

        public static IInjectionManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInjectionManager)) {
                return new Proxy(obj);
            }
            return (IInjectionManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Map _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    List _result2 = getFeatureInfo(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeList(_result2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getPackageFeatures(data.readString());
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getClassLibPath(data.readString());
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    Map getClassLibPath(String str) throws RemoteException;

    List getFeatureInfo(String str, String str2, String str3) throws RemoteException;

    Map getPackageFeatures(String str) throws RemoteException;
}
