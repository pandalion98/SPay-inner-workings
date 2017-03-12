package android.app.epm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPluginManagerCallback extends IInterface {

    public static abstract class Stub extends Binder implements IPluginManagerCallback {
        private static final String DESCRIPTOR = "android.app.epm.IPluginManagerCallback";
        static final int TRANSACTION_onInstallCompleted = 2;
        static final int TRANSACTION_onInstallProgress = 1;
        static final int TRANSACTION_onStateChangeCompleted = 6;
        static final int TRANSACTION_onStateChangeProgress = 5;
        static final int TRANSACTION_onUninstallCompleted = 4;
        static final int TRANSACTION_onUninstallProgress = 3;

        private static class Proxy implements IPluginManagerCallback {
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

            public void onInstallProgress(String pkg, int progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(progress);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onInstallCompleted(String packageName, int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(errorCode);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onUninstallProgress(String pkg, int progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(progress);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onUninstallCompleted(String packageName, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(result);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onStateChangeProgress(String pkg, int progress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(progress);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onStateChangeCompleted(String packageName, String type, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    _data.writeInt(result);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPluginManagerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPluginManagerCallback)) {
                return new Proxy(obj);
            }
            return (IPluginManagerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onInstallProgress(data.readString(), data.readInt());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onInstallCompleted(data.readString(), data.readInt());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onUninstallProgress(data.readString(), data.readInt());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onUninstallCompleted(data.readString(), data.readInt());
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onStateChangeProgress(data.readString(), data.readInt());
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onStateChangeCompleted(data.readString(), data.readString(), data.readInt());
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onInstallCompleted(String str, int i) throws RemoteException;

    void onInstallProgress(String str, int i) throws RemoteException;

    void onStateChangeCompleted(String str, String str2, int i) throws RemoteException;

    void onStateChangeProgress(String str, int i) throws RemoteException;

    void onUninstallCompleted(String str, int i) throws RemoteException;

    void onUninstallProgress(String str, int i) throws RemoteException;
}
