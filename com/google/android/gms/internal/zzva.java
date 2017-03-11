package com.google.android.gms.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.common.internal.zzc;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.zzy;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public interface zzva extends IInterface {

    public static abstract class zza extends Binder implements zzva {

        private static class zza implements zzva {
            private IBinder zzle;

            zza(IBinder iBinder) {
                this.zzle = iBinder;
            }

            public IBinder asBinder() {
                return this.zzle;
            }

            public void zza(int i, Account account, zzuz com_google_android_gms_internal_zzuz) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(i);
                    if (account != null) {
                        obtain.writeInt(1);
                        account.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzuz != null ? com_google_android_gms_internal_zzuz.asBinder() : null);
                    this.zzle.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzc com_google_android_gms_common_internal_zzc, zzuz com_google_android_gms_internal_zzuz) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (com_google_android_gms_common_internal_zzc != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_common_internal_zzc.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_internal_zzuz != null ? com_google_android_gms_internal_zzuz.asBinder() : null);
                    this.zzle.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzq com_google_android_gms_common_internal_zzq, int i, boolean z) {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzq != null ? com_google_android_gms_common_internal_zzq.asBinder() : null);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.zzle.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzy com_google_android_gms_common_internal_zzy, zzt com_google_android_gms_common_internal_zzt) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (com_google_android_gms_common_internal_zzy != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_common_internal_zzy.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(com_google_android_gms_common_internal_zzt != null ? com_google_android_gms_common_internal_zzt.asBinder() : null);
                    this.zzle.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(zzuw com_google_android_gms_internal_zzuw) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (com_google_android_gms_internal_zzuw != null) {
                        obtain.writeInt(1);
                        com_google_android_gms_internal_zzuw.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzle.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzaf(boolean z) {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.zzle.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzhb(int i) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.signin.internal.ISignInService");
                    obtain.writeInt(i);
                    this.zzle.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static zzva zzcF(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzva)) ? new zza(iBinder) : (zzva) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            boolean z = false;
            Account account = null;
            switch (i) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    zzc com_google_android_gms_common_internal_zzc;
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_common_internal_zzc = (zzc) zzc.CREATOR.createFromParcel(parcel);
                    }
                    zza(com_google_android_gms_common_internal_zzc, com.google.android.gms.internal.zzuz.zza.zzcE(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case F2m.PPB /*3*/:
                    zzuw com_google_android_gms_internal_zzuw;
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_internal_zzuw = (zzuw) zzuw.CREATOR.createFromParcel(parcel);
                    }
                    zza(com_google_android_gms_internal_zzuw);
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    zzaf(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    zzy com_google_android_gms_common_internal_zzy;
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    if (parcel.readInt() != 0) {
                        com_google_android_gms_common_internal_zzy = (zzy) zzy.CREATOR.createFromParcel(parcel);
                    }
                    zza(com_google_android_gms_common_internal_zzy, com.google.android.gms.common.internal.zzt.zza.zzV(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case ECCurve.COORD_SKEWED /*7*/:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    zzhb(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case X509KeyUsage.keyAgreement /*8*/:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        account = (Account) Account.CREATOR.createFromParcel(parcel);
                    }
                    zza(readInt, account, com.google.android.gms.internal.zzuz.zza.zzcE(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case NamedCurve.sect283k1 /*9*/:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.ISignInService");
                    zzq zzR = com.google.android.gms.common.internal.zzq.zza.zzR(parcel.readStrongBinder());
                    int readInt2 = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    zza(zzR, readInt2, z);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.signin.internal.ISignInService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(int i, Account account, zzuz com_google_android_gms_internal_zzuz);

    void zza(zzc com_google_android_gms_common_internal_zzc, zzuz com_google_android_gms_internal_zzuz);

    void zza(zzq com_google_android_gms_common_internal_zzq, int i, boolean z);

    void zza(zzy com_google_android_gms_common_internal_zzy, zzt com_google_android_gms_common_internal_zzt);

    void zza(zzuw com_google_android_gms_internal_zzuw);

    void zzaf(boolean z);

    void zzhb(int i);
}
