package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.zzpy.zza;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class zzqg extends zzqj implements Place {
    private final String zzanM;
    private boolean zzaoI;
    private final zzqf zzaoL;

    public zzqg(DataHolder dataHolder, int i, Context context) {
        super(dataHolder, i);
        this.zzaoL = context != null ? zzqf.zzab(context) : null;
        this.zzaoI = zzj("place_is_logging_enabled", false);
        this.zzanM = zzz("place_id", BuildConfig.FLAVOR);
    }

    private void zzcn(String str) {
        if (this.zzaoI && this.zzaoL != null) {
            this.zzaoL.zzy(this.zzanM, str);
        }
    }

    public /* synthetic */ Object freeze() {
        return zzpV();
    }

    public CharSequence getAddress() {
        zzcn("getAddress");
        return zzz("place_address", BuildConfig.FLAVOR);
    }

    public String getId() {
        zzcn("getId");
        return this.zzanM;
    }

    public LatLng getLatLng() {
        zzcn("getLatLng");
        return (LatLng) zza("place_lat_lng", LatLng.CREATOR);
    }

    public Locale getLocale() {
        zzcn("getLocale");
        Object zzz = zzz("place_locale", BuildConfig.FLAVOR);
        return !TextUtils.isEmpty(zzz) ? new Locale(zzz) : Locale.getDefault();
    }

    public CharSequence getName() {
        zzcn("getName");
        return zzz("place_name", BuildConfig.FLAVOR);
    }

    public CharSequence getPhoneNumber() {
        zzcn("getPhoneNumber");
        return zzz("place_phone_number", BuildConfig.FLAVOR);
    }

    public List<Integer> getPlaceTypes() {
        zzcn("getPlaceTypes");
        return zza("place_types", Collections.emptyList());
    }

    public int getPriceLevel() {
        zzcn("getPriceLevel");
        return zzC("place_price_level", -1);
    }

    public float getRating() {
        zzcn("getRating");
        return zza("place_rating", (float) GroundOverlayOptions.NO_DIMENSION);
    }

    public LatLngBounds getViewport() {
        zzcn("getViewport");
        return (LatLngBounds) zza("place_viewport", LatLngBounds.CREATOR);
    }

    public Uri getWebsiteUri() {
        zzcn("getWebsiteUri");
        String zzz = zzz("place_website_uri", null);
        return zzz == null ? null : Uri.parse(zzz);
    }

    public boolean zzpI() {
        zzcn("isPermanentlyClosed");
        return zzj("place_is_permanently_closed", false);
    }

    public float zzpO() {
        zzcn("getLevelNumber");
        return zza("place_level_number", 0.0f);
    }

    public Place zzpV() {
        zza zzY = new zza().zzY(this.zzaoI);
        this.zzaoI = false;
        Place zzpW = zzY.zzcq(getAddress().toString()).zzp(zzb("place_attributions", Collections.emptyList())).zzco(getId()).zzX(zzpI()).zza(getLatLng()).zzc(zzpO()).zzcp(getName().toString()).zzcr(getPhoneNumber().toString()).zzfH(getPriceLevel()).zzd(getRating()).zzo(getPlaceTypes()).zza(getViewport()).zzk(getWebsiteUri()).zzpW();
        zzpW.setLocale(getLocale());
        zzpW.zza(this.zzaoL);
        return zzpW;
    }
}
