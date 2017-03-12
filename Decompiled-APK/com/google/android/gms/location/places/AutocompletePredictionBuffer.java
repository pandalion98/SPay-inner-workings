package com.google.android.gms.location.places;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzpq;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;

public class AutocompletePredictionBuffer extends AbstractDataBuffer<AutocompletePrediction> implements Result {
    public AutocompletePredictionBuffer(DataHolder dataHolder) {
        super(dataHolder);
    }

    public AutocompletePrediction get(int i) {
        return new zzpq(this.zzMd, i);
    }

    public Status getStatus() {
        return new Status(this.zzMd.getStatusCode());
    }

    public String toString() {
        return zzw.zzk(this).zza(CardMaster.COL_STATUS, getStatus()).toString();
    }
}
