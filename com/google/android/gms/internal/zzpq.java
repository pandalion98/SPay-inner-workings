package com.google.android.gms.internal;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.zzpo.zza;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Collections;
import java.util.List;

public class zzpq extends zzqj implements AutocompletePrediction {
    public zzpq(DataHolder dataHolder, int i) {
        super(dataHolder, i);
    }

    public /* synthetic */ Object freeze() {
        return zzpL();
    }

    public String getDescription() {
        return zzz("ap_description", BuildConfig.FLAVOR);
    }

    public List<zza> getMatchedSubstrings() {
        return zza("ap_matched_subscriptions", zza.CREATOR, Collections.emptyList());
    }

    public String getPlaceId() {
        return zzz("ap_place_id", null);
    }

    public List<Integer> getPlaceTypes() {
        return zza("ap_place_types", Collections.emptyList());
    }

    public AutocompletePrediction zzpL() {
        return zzpo.zza(getDescription(), getPlaceId(), getPlaceTypes(), getMatchedSubstrings(), zzpM());
    }

    public int zzpM() {
        return zzC("ap_personalization_type", 6);
    }
}
