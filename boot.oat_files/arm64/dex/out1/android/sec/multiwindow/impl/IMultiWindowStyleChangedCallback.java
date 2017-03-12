package android.sec.multiwindow.impl;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.samsung.android.multiwindow.MultiWindowStyle;

public interface IMultiWindowStyleChangedCallback extends IInterface {

    public static abstract class Stub extends Binder implements IMultiWindowStyleChangedCallback {
        private static final String DESCRIPTOR = "android.sec.multiwindow.impl.IMultiWindowStyleChangedCallback";
        static final int TRANSACTION_onMultiWindowStyleChanged = 1;

        private static class Proxy implements IMultiWindowStyleChangedCallback {
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

            public void onMultiWindowStyleChanged(ComponentName cmp, MultiWindowStyle style) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cmp != null) {
                        _data.writeInt(1);
                        cmp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (style != null) {
                        _data.writeInt(1);
                        style.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiWindowStyleChangedCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMultiWindowStyleChangedCallback)) {
                return new Proxy(obj);
            }
            return (IMultiWindowStyleChangedCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    ComponentName _arg0;
                    MultiWindowStyle _arg1;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = (MultiWindowStyle) MultiWindowStyle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    onMultiWindowStyleChanged(_arg0, _arg1);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onMultiWindowStyleChanged(ComponentName componentName, MultiWindowStyle multiWindowStyle) throws RemoteException;
}
