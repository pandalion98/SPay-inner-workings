package com.samsung.android.multidisplay.dualscreen;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.dualscreen.DualScreen;

public interface IDualScreenCallbacks extends IInterface {

    public static abstract class Stub extends Binder implements IDualScreenCallbacks {
        private static final String DESCRIPTOR = "com.samsung.android.multidisplay.dualscreen.IDualScreenCallbacks";
        static final int TRANSACTION_screenFocusChanged = 1;

        private static class Proxy implements IDualScreenCallbacks {
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

            public void screenFocusChanged(DualScreen focusedScreen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (focusedScreen != null) {
                        _data.writeInt(1);
                        focusedScreen.writeToParcel(_data, 0);
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

        public static IDualScreenCallbacks asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDualScreenCallbacks)) {
                return new Proxy(obj);
            }
            return (IDualScreenCallbacks) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    DualScreen _arg0;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (DualScreen) DualScreen.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    screenFocusChanged(_arg0);
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

    void screenFocusChanged(DualScreen dualScreen) throws RemoteException;
}
