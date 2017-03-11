package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.google.android.gms.common.api.Scope;
import java.util.List;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public interface zzuy extends IInterface {

    public static abstract class zza extends Binder implements zzuy {
        public zza() {
            attachInterface(this, "com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    zza(parcel.readString(), parcel.createTypedArrayList(Scope.CREATOR), com.google.android.gms.internal.zzva.zza.zzcF(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case F2m.PPB /*3*/:
                    parcel.enforceInterface("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    zza(parcel.readString(), parcel.readString(), com.google.android.gms.internal.zzva.zza.zzcF(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void zza(String str, String str2, zzva com_google_android_gms_internal_zzva);

    void zza(String str, List<Scope> list, zzva com_google_android_gms_internal_zzva);
}
