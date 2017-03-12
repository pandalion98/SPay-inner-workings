package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.maps.model.internal.zzl;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzo extends IInterface {

    public static abstract class zza extends Binder implements zzo {

        private static class zza implements zzo {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public void zzb(zzl com_google_android_gms_maps_model_internal_zzl) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzl != null ? com_google_android_gms_maps_model_internal_zzl.asBinder() : null);
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzc(zzl com_google_android_gms_maps_model_internal_zzl) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzl != null ? com_google_android_gms_maps_model_internal_zzl.asBinder() : null);
                    this.zzle.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzd(zzl com_google_android_gms_maps_model_internal_zzl) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                    obtain.writeStrongBinder(com_google_android_gms_maps_model_internal_zzl != null ? com_google_android_gms_maps_model_internal_zzl.asBinder() : null);
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.maps.internal.IOnMarkerDragListener");
        }

        public static zzo zzbJ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IOnMarkerDragListener");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzo)) ? new zza(iBinder) : (zzo) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                    zzb(com.google.android.gms.maps.model.internal.zzl.zza.zzcb(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                    zzd(com.google.android.gms.maps.model.internal.zzl.zza.zzcb(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                    zzc(com.google.android.gms.maps.model.internal.zzl.zza.zzcb(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.internal.IOnMarkerDragListener");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zzb(zzl com_google_android_gms_maps_model_internal_zzl);

    void zzc(zzl com_google_android_gms_maps_model_internal_zzl);

    void zzd(zzl com_google_android_gms_maps_model_internal_zzl);
}
