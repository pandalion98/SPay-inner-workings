package android.sec.clipboard;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.sec.clipboard.data.ClipboardData;

public interface IClipboardDataPasteEvent extends IInterface {

    public static abstract class Stub extends Binder implements IClipboardDataPasteEvent {
        private static final String DESCRIPTOR = "android.sec.clipboard.IClipboardDataPasteEvent";
        static final int TRANSACTION_onClipboardDataPaste = 1;

        private static class Proxy implements IClipboardDataPasteEvent {
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

            public void onClipboardDataPaste(ClipboardData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IClipboardDataPasteEvent asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IClipboardDataPasteEvent)) {
                return new Proxy(obj);
            }
            return (IClipboardDataPasteEvent) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    ClipboardData _arg0;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (ClipboardData) ClipboardData.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onClipboardDataPaste(_arg0);
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

    void onClipboardDataPaste(ClipboardData clipboardData) throws RemoteException;
}
