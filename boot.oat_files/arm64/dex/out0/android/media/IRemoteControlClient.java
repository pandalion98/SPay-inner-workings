package android.media;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRemoteControlClient extends IInterface {

    public static abstract class Stub extends Binder implements IRemoteControlClient {
        private static final String DESCRIPTOR = "android.media.IRemoteControlClient";
        static final int TRANSACTION_enableRemoteControlDisplay = 8;
        static final int TRANSACTION_getNowPlayingEntries = 13;
        static final int TRANSACTION_informationRequestForDisplay = 2;
        static final int TRANSACTION_onInformationRequested = 1;
        static final int TRANSACTION_plugRemoteControlDisplay = 4;
        static final int TRANSACTION_seekTo = 9;
        static final int TRANSACTION_setBitmapSizeForDisplay = 6;
        static final int TRANSACTION_setBrowsedPlayer = 12;
        static final int TRANSACTION_setCurrentClientGenerationId = 3;
        static final int TRANSACTION_setPlayItem = 11;
        static final int TRANSACTION_setWantsSyncForDisplay = 7;
        static final int TRANSACTION_unplugRemoteControlDisplay = 5;
        static final int TRANSACTION_updateMetadata = 10;

        private static class Proxy implements IRemoteControlClient {
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

            public void onInformationRequested(int generationId, int infoFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(generationId);
                    _data.writeInt(infoFlags);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void informationRequestForDisplay(IRemoteControlDisplay rcd, int w, int h) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rcd != null) {
                        iBinder = rcd.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(w);
                    _data.writeInt(h);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setCurrentClientGenerationId(int clientGeneration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientGeneration);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void plugRemoteControlDisplay(IRemoteControlDisplay rcd, int w, int h) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rcd != null) {
                        iBinder = rcd.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(w);
                    _data.writeInt(h);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void unplugRemoteControlDisplay(IRemoteControlDisplay rcd) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rcd != null) {
                        iBinder = rcd.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setBitmapSizeForDisplay(IRemoteControlDisplay rcd, int w, int h) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rcd != null) {
                        iBinder = rcd.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeInt(w);
                    _data.writeInt(h);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setWantsSyncForDisplay(IRemoteControlDisplay rcd, boolean wantsSync) throws RemoteException {
                IBinder iBinder = null;
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rcd != null) {
                        iBinder = rcd.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (!wantsSync) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void enableRemoteControlDisplay(IRemoteControlDisplay rcd, boolean enabled) throws RemoteException {
                IBinder iBinder = null;
                int i = 1;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (rcd != null) {
                        iBinder = rcd.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void seekTo(int clientGeneration, long timeMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientGeneration);
                    _data.writeLong(timeMs);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void updateMetadata(int clientGeneration, int key, Rating value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientGeneration);
                    _data.writeInt(key);
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setPlayItem(int scope, long uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(scope);
                    _data.writeLong(uid);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void setBrowsedPlayer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void getNowPlayingEntries() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteControlClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRemoteControlClient)) {
                return new Proxy(obj);
            }
            return (IRemoteControlClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1 = false;
            IRemoteControlDisplay _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onInformationRequested(data.readInt(), data.readInt());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    informationRequestForDisplay(android.media.IRemoteControlDisplay.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    setCurrentClientGenerationId(data.readInt());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    plugRemoteControlDisplay(android.media.IRemoteControlDisplay.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    unplugRemoteControlDisplay(android.media.IRemoteControlDisplay.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    setBitmapSizeForDisplay(android.media.IRemoteControlDisplay.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = android.media.IRemoteControlDisplay.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    }
                    setWantsSyncForDisplay(_arg0, _arg1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = android.media.IRemoteControlDisplay.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    }
                    enableRemoteControlDisplay(_arg0, _arg1);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    seekTo(data.readInt(), data.readLong());
                    return true;
                case 10:
                    Rating _arg2;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    int _arg12 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (Rating) Rating.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    updateMetadata(_arg02, _arg12, _arg2);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    setPlayItem(data.readInt(), data.readLong());
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    setBrowsedPlayer();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    getNowPlayingEntries();
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void enableRemoteControlDisplay(IRemoteControlDisplay iRemoteControlDisplay, boolean z) throws RemoteException;

    void getNowPlayingEntries() throws RemoteException;

    void informationRequestForDisplay(IRemoteControlDisplay iRemoteControlDisplay, int i, int i2) throws RemoteException;

    void onInformationRequested(int i, int i2) throws RemoteException;

    void plugRemoteControlDisplay(IRemoteControlDisplay iRemoteControlDisplay, int i, int i2) throws RemoteException;

    void seekTo(int i, long j) throws RemoteException;

    void setBitmapSizeForDisplay(IRemoteControlDisplay iRemoteControlDisplay, int i, int i2) throws RemoteException;

    void setBrowsedPlayer() throws RemoteException;

    void setCurrentClientGenerationId(int i) throws RemoteException;

    void setPlayItem(int i, long j) throws RemoteException;

    void setWantsSyncForDisplay(IRemoteControlDisplay iRemoteControlDisplay, boolean z) throws RemoteException;

    void unplugRemoteControlDisplay(IRemoteControlDisplay iRemoteControlDisplay) throws RemoteException;

    void updateMetadata(int i, int i2, Rating rating) throws RemoteException;
}
