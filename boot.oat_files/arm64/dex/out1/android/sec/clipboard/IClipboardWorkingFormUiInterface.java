package android.sec.clipboard;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.sec.clipboard.data.IClipboardDataList;

public interface IClipboardWorkingFormUiInterface extends IInterface {

    public static abstract class Stub extends Binder implements IClipboardWorkingFormUiInterface {
        private static final String DESCRIPTOR = "android.sec.clipboard.IClipboardWorkingFormUiInterface";
        static final int TRANSACTION_setClipboardDataListChange = 1;
        static final int TRANSACTION_setClipboardDataMgr = 2;
        static final int TRANSACTION_setClipboardDataUiEvent = 3;

        private static class Proxy implements IClipboardWorkingFormUiInterface {
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

            public void setClipboardDataListChange(int changeFlag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(changeFlag);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setClipboardDataMgr(IClipboardDataList clipboardDataMgr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clipboardDataMgr != null ? clipboardDataMgr.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setClipboardDataUiEvent(IClipboardDataUiEvent clbEvent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(clbEvent != null ? clbEvent.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
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

        public static IClipboardWorkingFormUiInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IClipboardWorkingFormUiInterface)) {
                return new Proxy(obj);
            }
            return (IClipboardWorkingFormUiInterface) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    setClipboardDataListChange(data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    setClipboardDataMgr(android.sec.clipboard.data.IClipboardDataList.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    setClipboardDataUiEvent(android.sec.clipboard.IClipboardDataUiEvent.Stub.asInterface(data.readStrongBinder()));
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

    void setClipboardDataListChange(int i) throws RemoteException;

    void setClipboardDataMgr(IClipboardDataList iClipboardDataList) throws RemoteException;

    void setClipboardDataUiEvent(IClipboardDataUiEvent iClipboardDataUiEvent) throws RemoteException;
}
