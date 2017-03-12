package com.absolute.android.persistence;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IABTDownloadReceiver extends IInterface {

    public static abstract class Stub extends Binder implements IABTDownloadReceiver {
        private static final String DESCRIPTOR = "com.absolute.android.persistence.IABTDownloadReceiver";
        static final int TRANSACTION_onDownloadProgress = 1;
        static final int TRANSACTION_onDownloadResult = 2;

        private static class Proxy implements IABTDownloadReceiver {
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

            public void onDownloadProgress(String packageName, int version, int totalBytes, int downloadedBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(version);
                    _data.writeInt(totalBytes);
                    _data.writeInt(downloadedBytes);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onDownloadResult(boolean succeeded, String packageName, int version, String apkPath, String errorMessage) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (succeeded) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(packageName);
                    _data.writeInt(version);
                    _data.writeString(apkPath);
                    _data.writeString(errorMessage);
                    this.mRemote.transact(2, _data, _reply, 0);
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

        public static IABTDownloadReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IABTDownloadReceiver)) {
                return new Proxy(obj);
            }
            return (IABTDownloadReceiver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onDownloadProgress(data.readString(), data.readInt(), data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onDownloadResult(data.readInt() != 0, data.readString(), data.readInt(), data.readString(), data.readString());
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

    void onDownloadProgress(String str, int i, int i2, int i3) throws RemoteException;

    void onDownloadResult(boolean z, String str, int i, String str2, String str3) throws RemoteException;
}
