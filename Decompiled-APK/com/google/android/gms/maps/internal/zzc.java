package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.model.internal.zzg;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzc extends IInterface {

    public static abstract class zza extends Binder implements zzc {

        private static class zza implements zzc {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public IMapViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, GoogleMapOptions googleMapOptions) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    if (googleMapOptions != null) {
                        obtain.writeInt(1);
                        googleMapOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapViewDelegate zzbz = com.google.android.gms.maps.internal.IMapViewDelegate.zza.zzbz(obtain2.readStrongBinder());
                    return zzbz;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IStreetViewPanoramaViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, StreetViewPanoramaOptions streetViewPanoramaOptions) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    if (streetViewPanoramaOptions != null) {
                        obtain.writeInt(1);
                        streetViewPanoramaOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    IStreetViewPanoramaViewDelegate zzbU = com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate.zza.zzbU(obtain2.readStrongBinder());
                    return zzbU;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(zzd com_google_android_gms_dynamic_zzd, int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    obtain.writeInt(i);
                    this.zzle.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzi(zzd com_google_android_gms_dynamic_zzd) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IMapFragmentDelegate zzj(zzd com_google_android_gms_dynamic_zzd) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapFragmentDelegate zzby = com.google.android.gms.maps.internal.IMapFragmentDelegate.zza.zzby(obtain2.readStrongBinder());
                    return zzby;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IStreetViewPanoramaFragmentDelegate zzk(zzd com_google_android_gms_dynamic_zzd) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzle.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    IStreetViewPanoramaFragmentDelegate zzbT = com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate.zza.zzbT(obtain2.readStrongBinder());
                    return zzbT;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ICameraUpdateFactoryDelegate zzqA() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.zzle.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    ICameraUpdateFactoryDelegate zzbr = com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate.zza.zzbr(obtain2.readStrongBinder());
                    return zzbr;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzg zzqB() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.zzle.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    zzg zzbW = com.google.android.gms.maps.model.internal.zzg.zza.zzbW(obtain2.readStrongBinder());
                    return zzbW;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzc zzbt(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzc)) ? new zza(iBinder) : (zzc) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            IBinder iBinder = null;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    zzi(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapFragmentDelegate zzj = zzj(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (zzj != null) {
                        iBinder = zzj.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapViewDelegate zza = zza(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()), parcel.readInt() != 0 ? GoogleMapOptions.CREATOR.zzdP(parcel) : null);
                    parcel2.writeNoException();
                    if (zza != null) {
                        iBinder = zza.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    ICameraUpdateFactoryDelegate zzqA = zzqA();
                    parcel2.writeNoException();
                    if (zzqA != null) {
                        iBinder = zzqA.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    zzg zzqB = zzqB();
                    parcel2.writeNoException();
                    if (zzqB != null) {
                        iBinder = zzqB.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    zzb(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IStreetViewPanoramaViewDelegate zza2 = zza(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()), parcel.readInt() != 0 ? StreetViewPanoramaOptions.CREATOR.zzdQ(parcel) : null);
                    parcel2.writeNoException();
                    if (zza2 != null) {
                        iBinder = zza2.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IStreetViewPanoramaFragmentDelegate zzk = zzk(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (zzk != null) {
                        iBinder = zzk.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.internal.ICreator");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    IMapViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, GoogleMapOptions googleMapOptions);

    IStreetViewPanoramaViewDelegate zza(zzd com_google_android_gms_dynamic_zzd, StreetViewPanoramaOptions streetViewPanoramaOptions);

    void zzb(zzd com_google_android_gms_dynamic_zzd, int i);

    void zzi(zzd com_google_android_gms_dynamic_zzd);

    IMapFragmentDelegate zzj(zzd com_google_android_gms_dynamic_zzd);

    IStreetViewPanoramaFragmentDelegate zzk(zzd com_google_android_gms_dynamic_zzd);

    ICameraUpdateFactoryDelegate zzqA();

    zzg zzqB();
}
