package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.zzc;
import com.google.android.gms.location.places.zzf;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public interface zzpt extends IInterface {

    public static abstract class zza extends Binder implements zzpt {

        private static class zza implements zzpt {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public void zza(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzpv != null ? com_google_android_gms_internal_zzpv.asBinder() : null);
                    this.zzle.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (placeFilter != null) {
                        obtain.writeInt(1);
                        placeFilter.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzpv != null ? com_google_android_gms_internal_zzpv.asBinder() : null);
                    this.zzle.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(PlaceReport placeReport, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (placeReport != null) {
                        obtain.writeInt(1);
                        placeReport.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzpv != null ? com_google_android_gms_internal_zzpv.asBinder() : null);
                    this.zzle.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzc com_google_android_gms_location_places_zzc, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (com_google_android_gms_location_places_zzc != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_location_places_zzc.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzpv != null ? com_google_android_gms_internal_zzpv.asBinder() : null);
                    this.zzle.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzf com_google_android_gms_location_places_zzf, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (com_google_android_gms_location_places_zzf != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_location_places_zzf.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzpv != null ? com_google_android_gms_internal_zzpv.asBinder() : null);
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    if (com_google_android_gms_internal_zzqh != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzqh.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (pendingIntent != null) {
                        obtain.writeInt(1);
                        pendingIntent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzpv != null ? com_google_android_gms_internal_zzpv.asBinder() : null);
                    this.zzle.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzpt zzbn(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzpt)) ? new zza(iBinder) : (zzpt) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            zzqh com_google_android_gms_internal_zzqh = null;
            switch (i) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zza(parcel.readInt() != 0 ? zzf.CREATOR.zzdC(parcel) : null, parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zza(parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zza(parcel.readInt() != 0 ? zzc.CREATOR.zzdA(parcel) : null, parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    zzb(parcel.readInt() != 0 ? zzqh.CREATOR.zzdI(parcel) : null, parcel.readInt() != 0 ? (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel) : null, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    PlaceFilter zzdB = parcel.readInt() != 0 ? PlaceFilter.CREATOR.zzdB(parcel) : null;
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(zzdB, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    PlaceReport createFromParcel = parcel.readInt() != 0 ? PlaceReport.CREATOR.createFromParcel(parcel) : null;
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzqh = zzqh.CREATOR.zzdI(parcel);
                    }
                    zza(createFromParcel, com_google_android_gms_internal_zzqh, com.google.android.gms.internal.zzpv.zza.zzbp(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.location.places.internal.IGooglePlaceDetectionService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv);

    void zza(PlaceFilter placeFilter, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(PlaceReport placeReport, zzqh com_google_android_gms_internal_zzqh, zzpv com_google_android_gms_internal_zzpv);

    void zza(zzc com_google_android_gms_location_places_zzc, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv);

    void zza(zzf com_google_android_gms_location_places_zzf, zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv);

    void zzb(zzqh com_google_android_gms_internal_zzqh, PendingIntent pendingIntent, zzpv com_google_android_gms_internal_zzpv);
}
