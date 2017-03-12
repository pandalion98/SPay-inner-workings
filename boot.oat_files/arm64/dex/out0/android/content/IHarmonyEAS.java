package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IHarmonyEAS extends IInterface {

    public static abstract class Stub extends Binder implements IHarmonyEAS {
        private static final String DESCRIPTOR = "android.content.IHarmonyEAS";
        static final int TRANSACTION_getHashValueFromPackageName = 7;
        static final int TRANSACTION_getPackageNameFromHash = 5;
        static final int TRANSACTION_getThirdPartyApps = 6;
        static final int TRANSACTION_getUnknownSourcesPackages = 2;
        static final int TRANSACTION_isPackageUnknownSource = 3;
        static final int TRANSACTION_removeInstallationPackage = 8;
        static final int TRANSACTION_setInstallationPackageHashValue = 4;
        static final int TRANSACTION_setInstallationPackageUnknown = 1;

        private static class Proxy implements IHarmonyEAS {
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

            public void setInstallationPackageUnknown(String packageName, int source, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(source);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getUnknownSourcesPackages(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isPackageUnknownSource(String packageName, int userHandle) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
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

            public void setInstallationPackageHashValue(String packageName, String hashValue, int source, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(hashValue);
                    _data.writeInt(source);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPackageNameFromHash(String hashValue, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(hashValue);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getThirdPartyApps(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getHashValueFromPackageName(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeInstallationPackage(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    this.mRemote.transact(8, _data, _reply, 0);
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

        public static IHarmonyEAS asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IHarmonyEAS)) {
                return new Proxy(obj);
            }
            return (IHarmonyEAS) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            List<String> _result;
            String _result2;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    setInstallationPackageUnknown(data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getUnknownSourcesPackages(data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result3 = isPackageUnknownSource(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    setInstallationPackageHashValue(data.readString(), data.readString(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getPackageNameFromHash(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getThirdPartyApps(data.readInt());
                    reply.writeNoException();
                    reply.writeStringList(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result2 = getHashValueFromPackageName(data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    removeInstallationPackage(data.readString(), data.readInt());
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

    String getHashValueFromPackageName(String str, int i) throws RemoteException;

    String getPackageNameFromHash(String str, int i) throws RemoteException;

    List<String> getThirdPartyApps(int i) throws RemoteException;

    List<String> getUnknownSourcesPackages(int i) throws RemoteException;

    boolean isPackageUnknownSource(String str, int i) throws RemoteException;

    void removeInstallationPackage(String str, int i) throws RemoteException;

    void setInstallationPackageHashValue(String str, String str2, int i, int i2) throws RemoteException;

    void setInstallationPackageUnknown(String str, int i, int i2) throws RemoteException;
}
