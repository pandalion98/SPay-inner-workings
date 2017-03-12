package com.samsung.android.cocktailbar;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ICocktailBarFeedsCallback extends IInterface {

    public static abstract class Stub extends Binder implements ICocktailBarFeedsCallback {
        private static final String DESCRIPTOR = "com.samsung.android.cocktailbar.ICocktailBarFeedsCallback";
        static final int TRANSACTION_onFeedsUpdated = 1;

        private static class Proxy implements ICocktailBarFeedsCallback {
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

            public void onFeedsUpdated(int cocktailId, List<FeedsInfo> feedsInfoList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cocktailId);
                    _data.writeTypedList(feedsInfoList);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICocktailBarFeedsCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ICocktailBarFeedsCallback)) {
                return new Proxy(obj);
            }
            return (ICocktailBarFeedsCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onFeedsUpdated(data.readInt(), data.createTypedArrayList(FeedsInfo.CREATOR));
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onFeedsUpdated(int i, List<FeedsInfo> list) throws RemoteException;
}
