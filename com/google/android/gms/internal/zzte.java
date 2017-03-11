package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public interface zzte extends IInterface {

    public static abstract class zza extends Binder implements zzte {

        private static class zza implements zzte {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public void zza(String str, zztj com_google_android_gms_internal_zztj, zztf com_google_android_gms_internal_zztf) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (com_google_android_gms_internal_zztj != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zztj.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (com_google_android_gms_internal_zztf != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zztf.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, zztj com_google_android_gms_internal_zztj, List<zztf> list) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (com_google_android_gms_internal_zztj != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zztj.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeTypedList(list);
                    this.zzle.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void zza(String str, zztj com_google_android_gms_internal_zztj, byte[] bArr) {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.playlog.internal.IPlayLogService");
                    obtain.writeString(str);
                    if (com_google_android_gms_internal_zztj != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zztj.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeByteArray(bArr);
                    this.zzle.transact(4, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static zzte zzcu(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.playlog.internal.IPlayLogService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzte)) ? new zza(iBinder) : (zzte) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            zztj com_google_android_gms_internal_zztj = null;
            String readString;
            switch (i) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    zztf zzev;
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    String readString2 = parcel.readString();
                    zztj zzew = parcel.readInt() != 0 ? zztj.CREATOR.zzew(parcel) : null;
                    if (parcel.readInt() != 0) {
                        zzev = zztf.CREATOR.zzev(parcel);
                    }
                    zza(readString2, zzew, zzev);
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zztj = zztj.CREATOR.zzew(parcel);
                    }
                    zza(readString, com_google_android_gms_internal_zztj, parcel.createTypedArrayList(zztf.CREATOR));
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.playlog.internal.IPlayLogService");
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zztj = zztj.CREATOR.zzew(parcel);
                    }
                    zza(readString, com_google_android_gms_internal_zztj, parcel.createByteArray());
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.playlog.internal.IPlayLogService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(String str, zztj com_google_android_gms_internal_zztj, zztf com_google_android_gms_internal_zztf);

    void zza(String str, zztj com_google_android_gms_internal_zztj, List<zztf> list);

    void zza(String str, zztj com_google_android_gms_internal_zztj, byte[] bArr);
}
