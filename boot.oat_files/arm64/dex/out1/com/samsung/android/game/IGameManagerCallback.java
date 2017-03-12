package com.samsung.android.game;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGameManagerCallback extends IInterface {

    public static abstract class Stub extends Binder implements IGameManagerCallback {
        private static final String DESCRIPTOR = "com.samsung.android.game.IGameManagerCallback";
        static final int TRANSACTION_onGameAdded = 3;
        static final int TRANSACTION_onGamePause = 2;
        static final int TRANSACTION_onGameResume = 1;
        static final int TRANSACTION_onModeChanged = 4;

        private static class Proxy implements IGameManagerCallback {
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

            public void onGameResume(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onGamePause(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onGameAdded(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onModeChanged(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGameManagerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGameManagerCallback)) {
                return new Proxy(obj);
            }
            return (IGameManagerCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onGameResume(data.readString());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onGamePause(data.readString());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onGameAdded(data.readString());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onModeChanged(data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onGameAdded(String str) throws RemoteException;

    void onGamePause(String str) throws RemoteException;

    void onGameResume(String str) throws RemoteException;

    void onModeChanged(int i) throws RemoteException;
}
