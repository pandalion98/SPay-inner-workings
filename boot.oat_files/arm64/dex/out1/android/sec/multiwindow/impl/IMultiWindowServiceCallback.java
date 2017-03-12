package android.sec.multiwindow.impl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMultiWindowServiceCallback extends IInterface {

    public static abstract class Stub extends Binder implements IMultiWindowServiceCallback {
        private static final String DESCRIPTOR = "android.sec.multiwindow.impl.IMultiWindowServiceCallback";
        static final int TRANSACTION_onArrangeStateUpdate = 2;
        static final int TRANSACTION_onMinimizeUpdate = 1;

        private static class Proxy implements IMultiWindowServiceCallback {
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

            public void onMinimizeUpdate(float x, float y) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onArrangeStateUpdate(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiWindowServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMultiWindowServiceCallback)) {
                return new Proxy(obj);
            }
            return (IMultiWindowServiceCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onMinimizeUpdate(data.readFloat(), data.readFloat());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onArrangeStateUpdate(data.readInt());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onArrangeStateUpdate(int i) throws RemoteException;

    void onMinimizeUpdate(float f, float f2) throws RemoteException;
}
