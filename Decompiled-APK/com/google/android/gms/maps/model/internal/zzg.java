package com.google.android.gms.maps.model.internal;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.dynamic.zzd;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzg extends IInterface {

    public static abstract class zza extends Binder implements zzg {

        private static class zza implements zzg {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public zzd zzb(Bitmap bitmap) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    if (bitmap != null) {
                        obtain.writeInt(1);
                        bitmap.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd zzcs(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    obtain.writeString(str);
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd zzct(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    obtain.writeString(str);
                    this.zzle.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd zzcu(String str) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    obtain.writeString(str);
                    this.zzle.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd zze(float f) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    obtain.writeFloat(f);
                    this.zzle.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd zzgn(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    obtain.writeInt(i);
                    this.zzle.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public zzd zzqN() {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    this.zzle.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    zzd zzau = com.google.android.gms.dynamic.zzd.zza.zzau(obtain2.readStrongBinder());
                    return zzau;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzg zzbW(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzg)) ? new zza(iBinder) : (zzg) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            IBinder iBinder = null;
            zzd zzgn;
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    zzgn = zzgn(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(zzgn != null ? zzgn.asBinder() : null);
                    return true;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    zzgn = zzcs(parcel.readString());
                    parcel2.writeNoException();
                    if (zzgn != null) {
                        iBinder = zzgn.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    zzgn = zzct(parcel.readString());
                    parcel2.writeNoException();
                    if (zzgn != null) {
                        iBinder = zzgn.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    zzgn = zzqN();
                    parcel2.writeNoException();
                    if (zzgn != null) {
                        iBinder = zzgn.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    zzgn = zze(parcel.readFloat());
                    parcel2.writeNoException();
                    if (zzgn != null) {
                        iBinder = zzgn.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    zzgn = zzb(parcel.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (zzgn != null) {
                        iBinder = zzgn.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    zzgn = zzcu(parcel.readString());
                    parcel2.writeNoException();
                    if (zzgn != null) {
                        iBinder = zzgn.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.model.internal.IBitmapDescriptorFactoryDelegate");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    zzd zzb(Bitmap bitmap);

    zzd zzcs(String str);

    zzd zzct(String str);

    zzd zzcu(String str);

    zzd zze(float f);

    zzd zzgn(int i);

    zzd zzqN();
}
