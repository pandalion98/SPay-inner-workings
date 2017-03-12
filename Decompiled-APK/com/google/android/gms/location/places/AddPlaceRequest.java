package com.google.android.gms.location.places;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class AddPlaceRequest implements SafeParcelable {
    public static final Creator<AddPlaceRequest> CREATOR;
    private final String mName;
    final int zzFG;
    private final String zzYy;
    private final LatLng zzant;
    private final List<Integer> zzanu;
    private final String zzanv;
    private final Uri zzanw;

    static {
        CREATOR = new zza();
    }

    AddPlaceRequest(int i, String str, LatLng latLng, String str2, List<Integer> list, String str3, Uri uri) {
        this.zzFG = i;
        this.mName = zzx.zzbn(str);
        this.zzant = (LatLng) zzx.zzl(latLng);
        this.zzYy = str2;
        this.zzanu = new ArrayList(list);
        boolean z = (TextUtils.isEmpty(str3) && uri == null) ? false : true;
        zzx.zzb(z, (Object) "One of phone number or URI should be provided.");
        this.zzanv = str3;
        this.zzanw = uri;
    }

    public AddPlaceRequest(String str, LatLng latLng, String str2, List<Integer> list, Uri uri) {
        this(str, latLng, str2, list, null, (Uri) zzx.zzl(uri));
    }

    public AddPlaceRequest(String str, LatLng latLng, String str2, List<Integer> list, String str3) {
        this(str, latLng, str2, list, zzx.zzbn(str3), null);
    }

    public AddPlaceRequest(String str, LatLng latLng, String str2, List<Integer> list, String str3, Uri uri) {
        this(0, str, latLng, str2, list, str3, uri);
    }

    public int describeContents() {
        return 0;
    }

    public String getAddress() {
        return this.zzYy;
    }

    public LatLng getLatLng() {
        return this.zzant;
    }

    public String getName() {
        return this.mName;
    }

    public String getPhoneNumber() {
        return this.zzanv;
    }

    public List<Integer> getPlaceTypes() {
        return this.zzanu;
    }

    public Uri getWebsiteUri() {
        return this.zzanw;
    }

    public String toString() {
        return zzw.zzk(this).zza("name", this.mName).zza("latLng", this.zzant).zza("address", this.zzYy).zza("placeTypes", this.zzanu).zza("phoneNumer", this.zzanv).zza("websiteUri", this.zzanw).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zza.zza(this, parcel, i);
    }
}
