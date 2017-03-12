package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.maps.model.internal.zzl;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzd extends IInterface {

    public static abstract class zza extends Binder implements zzd {

        private static class zza implements zzd {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public com.google.android.gms.dynamic.zzd zzf(zzl com_google_android_gms_maps_model_internal_zzl) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzl != null ? com_google_android_gms_maps_model_internal_zzl.asBinder() : null);
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    com.google.android.gms.dynamic.zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public com.google.android.gms.dynamic.zzd zzg(zzl com_google_android_gms_maps_model_internal_zzl) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzl != null ? com_google_android_gms_maps_model_internal_zzl.asBinder() : null);
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    com.google.android.gms.dynamic.zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.maps.internal.IInfoWindowAdapter");
        }

        public static zzd zzbv(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzd)) ? new zza(iBinder) : (zzd) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            IBinder iBinder = null;
            com.google.android.gms.dynamic.zzd zzf;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    zzf = zzf(com.google.android.gms.maps.model.internal.zzl.zza.zzcb(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (zzf != null) {
                        iBinder = zzf.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    zzf = zzg(com.google.android.gms.maps.model.internal.zzl.zza.zzcb(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (zzf != null) {
                        iBinder = zzf.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    com.google.android.gms.dynamic.zzd zzf(zzl com_google_android_gms_maps_model_internal_zzl);

    com.google.android.gms.dynamic.zzd zzg(zzl com_google_android_gms_maps_model_internal_zzl);
}
