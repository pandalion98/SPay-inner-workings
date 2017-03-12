package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardProvisionData;

public class zze<T extends SafeParcelable> extends AbstractDataBuffer<T> {
    private static final String[] zzNS;
    private final Creator<T> zzNT;

    static {
        zzNS = new String[]{CardProvisionData.COL_DATA};
    }

    public zze(DataHolder dataHolder, Creator<T> creator) {
        super(dataHolder);
        this.zzNT = creator;
    }

    public /* synthetic */ Object get(int i) {
        return zzaw(i);
    }

    public T zzaw(int i) {
        byte[] zzf = this.zzMd.zzf(CardProvisionData.COL_DATA, i, this.zzMd.zzax(i));
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(zzf, 0, zzf.length);
        obtain.setDataPosition(0);
        SafeParcelable safeParcelable = (SafeParcelable) this.zzNT.createFromParcel(obtain);
        obtain.recycle();
        return safeParcelable;
    }
}
