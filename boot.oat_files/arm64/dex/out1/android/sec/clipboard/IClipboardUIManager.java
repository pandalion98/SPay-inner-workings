package android.sec.clipboard;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IClipboardUIManager extends IInterface {

    public static abstract class Stub extends Binder implements IClipboardUIManager {
        private static final String DESCRIPTOR = "android.sec.clipboard.IClipboardUIManager";
        static final int TRANSACTION_dismiss = 2;
        static final int TRANSACTION_getClipboardUIMode = 8;
        static final int TRANSACTION_getIconXpos = 6;
        static final int TRANSACTION_getIconYpos = 7;
        static final int TRANSACTION_isShowing = 3;
        static final int TRANSACTION_setPasteTargetViewType = 4;
        static final int TRANSACTION_show = 1;
        static final int TRANSACTION_showFloatingIconForScrap = 5;

        private static class Proxy implements IClipboardUIManager {
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

            public void show(int clientId, IBinder client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeStrongBinder(client);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dismiss(int clientId, boolean immediate) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    if (immediate) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isShowing() throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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

            public void setPasteTargetViewType(int type, IClipboardDataPasteEvent clPasteEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeStrongBinder(clPasteEvent != null ? clPasteEvent.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showFloatingIconForScrap() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getIconXpos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public float getIconYpos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    float _result = _reply.readFloat();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getClipboardUIMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
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

        public static IClipboardUIManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IClipboardUIManager)) {
                return new Proxy(obj);
            }
            return (IClipboardUIManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int _arg1 = 0;
            float _result;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    show(data.readInt(), data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 2:
                    boolean _arg12;
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    }
                    dismiss(_arg0, _arg12);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = isShowing();
                    reply.writeNoException();
                    if (_result2) {
                        _arg1 = 1;
                    }
                    reply.writeInt(_arg1);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    setPasteTargetViewType(data.readInt(), android.sec.clipboard.IClipboardDataPasteEvent.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    showFloatingIconForScrap();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIconXpos();
                    reply.writeNoException();
                    reply.writeFloat(_result);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _result = getIconYpos();
                    reply.writeNoException();
                    reply.writeFloat(_result);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _result3 = getClipboardUIMode();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void dismiss(int i, boolean z) throws RemoteException;

    int getClipboardUIMode() throws RemoteException;

    float getIconXpos() throws RemoteException;

    float getIconYpos() throws RemoteException;

    boolean isShowing() throws RemoteException;

    void setPasteTargetViewType(int i, IClipboardDataPasteEvent iClipboardDataPasteEvent) throws RemoteException;

    void show(int i, IBinder iBinder) throws RemoteException;

    void showFloatingIconForScrap() throws RemoteException;
}
