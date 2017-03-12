package com.samsung.contextclient.p027a;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.Poi;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.contextclient.a.a */
public interface IPoiListener extends IInterface {

    /* renamed from: com.samsung.contextclient.a.a.a */
    public static abstract class IPoiListener extends Binder implements IPoiListener {

        /* renamed from: com.samsung.contextclient.a.a.a.a */
        private static class IPoiListener implements IPoiListener {
            private IBinder mRemote;

            IPoiListener(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void m1384a(Poi poi, Location location) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.samsung.contextclient.listener.IPoiListener");
                    if (poi != null) {
                        obtain.writeInt(1);
                        poi.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (location != null) {
                        obtain.writeInt(1);
                        location.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public IPoiListener() {
            attachInterface(this, "com.samsung.contextclient.listener.IPoiListener");
        }

        public static IPoiListener m1385b(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.samsung.contextclient.listener.IPoiListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPoiListener)) {
                return new IPoiListener(iBinder);
            }
            return (IPoiListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    Poi poi;
                    Location location;
                    parcel.enforceInterface("com.samsung.contextclient.listener.IPoiListener");
                    if (parcel.readInt() != 0) {
                        poi = (Poi) Poi.CREATOR.createFromParcel(parcel);
                    } else {
                        poi = null;
                    }
                    if (parcel.readInt() != 0) {
                        location = (Location) Location.CREATOR.createFromParcel(parcel);
                    } else {
                        location = null;
                    }
                    m1383a(poi, location);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.samsung.contextclient.listener.IPoiListener");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void m1383a(Poi poi, Location location);
}
