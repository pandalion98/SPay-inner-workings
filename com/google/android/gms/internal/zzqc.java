package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.maps.model.GroundOverlayOptions;

public class zzqc extends zzqj implements PlaceLikelihood {
    private final Context mContext;

    public zzqc(DataHolder dataHolder, int i, Context context) {
        super(dataHolder, i);
        this.mContext = context;
    }

    public /* synthetic */ Object freeze() {
        return zzpX();
    }

    public float getLikelihood() {
        return zza("place_likelihood", (float) GroundOverlayOptions.NO_DIMENSION);
    }

    public Place getPlace() {
        return new zzqg(this.zzMd, this.zzNQ, this.mContext);
    }

    public PlaceLikelihood zzpX() {
        return zzqa.zza((zzpy) getPlace().freeze(), getLikelihood());
    }
}
