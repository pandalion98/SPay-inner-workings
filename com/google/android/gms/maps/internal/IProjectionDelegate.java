package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface IProjectionDelegate extends IInterface {

    public static abstract class zza extends Binder implements IProjectionDelegate {

        private static class zza implements IProjectionDelegate {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public LatLng fromScreenLocation(zzd com_google_android_gms_dynamic_zzd) {
                LatLng latLng = null;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IProjectionDelegate");
                    obtain.writeStrongBinder(com_google_android_gms_dynamic_zzd != null ? com_google_android_gms_dynamic_zzd.asBinder() : null);
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        latLng = LatLng.CREATOR.zzdW(obtain2);
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return latLng;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public LatLng fromScreenLocation2(zzy com_google_android_gms_maps_internal_zzy) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IProjectionDelegate");
                    if (com_google_android_gms_maps_internal_zzy != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_maps_internal_zzy.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    LatLng zzdW = obtain2.readInt() != 0 ? LatLng.CREATOR.zzdW(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return zzdW;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public VisibleRegion getVisibleRegion() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IProjectionDelegate");
                    this.zzle.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    VisibleRegion zzeg = obtain2.readInt() != 0 ? VisibleRegion.CREATOR.zzeg(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return zzeg;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd toScreenLocation(LatLng latLng) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IProjectionDelegate");
                    if (latLng != null) {
                        obtain.writeInt(1);
                        latLng.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzy toScreenLocation2(LatLng latLng) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IProjectionDelegate");
                    if (latLng != null) {
                        obtain.writeInt(1);
                        latLng.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    zzy zzdR = obtain2.readInt() != 0 ? zzy.CREATOR.zzdR(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return zzdR;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static IProjectionDelegate zzbQ(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IProjectionDelegate)) ? new zza(iBinder) : (IProjectionDelegate) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            LatLng latLng = null;
            LatLng fromScreenLocation;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
                    fromScreenLocation = fromScreenLocation(com.google.android.gms.dynamic.zzd.zza.zzau(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (fromScreenLocation != null) {
                        parcel2.writeInt(1);
                        fromScreenLocation.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    IBinder asBinder;
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
                    zzd toScreenLocation = toScreenLocation(parcel.readInt() != 0 ? LatLng.CREATOR.zzdW(parcel) : null);
                    parcel2.writeNoException();
                    if (toScreenLocation != null) {
                        asBinder = toScreenLocation.asBinder();
                    }
                    parcel2.writeStrongBinder(asBinder);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
                    VisibleRegion visibleRegion = getVisibleRegion();
                    parcel2.writeNoException();
                    if (visibleRegion != null) {
                        parcel2.writeInt(1);
                        visibleRegion.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    zzy zzdR;
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
                    if (parcel.readInt() != 0) {
                        zzdR = zzy.CREATOR.zzdR(parcel);
                    }
                    fromScreenLocation = fromScreenLocation2(zzdR);
                    parcel2.writeNoException();
                    if (fromScreenLocation != null) {
                        parcel2.writeInt(1);
                        fromScreenLocation.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
                    if (parcel.readInt() != 0) {
                        latLng = LatLng.CREATOR.zzdW(parcel);
                    }
                    zzy toScreenLocation2 = toScreenLocation2(latLng);
                    parcel2.writeNoException();
                    if (toScreenLocation2 != null) {
                        parcel2.writeInt(1);
                        toScreenLocation2.writeToParcel(parcel2, 1);
                    } else {
                        parcel2.writeInt(0);
                    }
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.internal.IProjectionDelegate");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    LatLng fromScreenLocation(zzd com_google_android_gms_dynamic_zzd);

    LatLng fromScreenLocation2(zzy com_google_android_gms_maps_internal_zzy);

    VisibleRegion getVisibleRegion();

    zzd toScreenLocation(LatLng latLng);

    zzy toScreenLocation2(LatLng latLng);
}
