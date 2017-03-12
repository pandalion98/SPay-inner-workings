package com.google.android.gms.location.places;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzw.zza;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutocompleteFilter implements SafeParcelable {
    public static final zzb CREATOR;
    final int zzFG;
    final boolean zzanx;
    final List<Integer> zzany;
    private final Set<Integer> zzanz;

    static {
        CREATOR = new zzb();
    }

    AutocompleteFilter(int i, boolean z, Collection<Integer> collection) {
        this.zzFG = i;
        this.zzanx = z;
        this.zzany = collection == null ? Collections.emptyList() : new ArrayList(collection);
        if (this.zzany.isEmpty()) {
            this.zzanz = Collections.emptySet();
        } else {
            this.zzanz = Collections.unmodifiableSet(new HashSet(this.zzany));
        }
    }

    public static AutocompleteFilter create(Collection<Integer> collection) {
        return zza(true, collection);
    }

    public static AutocompleteFilter zza(boolean z, Collection<Integer> collection) {
        return new AutocompleteFilter(0, z, collection);
    }

    public int describeContents() {
        zzb com_google_android_gms_location_places_zzb = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AutocompleteFilter)) {
            return false;
        }
        AutocompleteFilter autocompleteFilter = (AutocompleteFilter) obj;
        return this.zzanz.equals(autocompleteFilter.zzanz) && this.zzanx == autocompleteFilter.zzanx;
    }

    public Set<Integer> getPlaceTypes() {
        return this.zzanz;
    }

    public int hashCode() {
        return zzw.hashCode(Boolean.valueOf(this.zzanx), this.zzanz);
    }

    public String toString() {
        zza zzk = zzw.zzk(this);
        if (!this.zzanx) {
            zzk.zza("restrictedToPlaces", Boolean.valueOf(this.zzanx));
        }
        zzk.zza("placeTypes", this.zzanz);
        return zzk.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzb com_google_android_gms_location_places_zzb = CREATOR;
        zzb.zza(this, parcel, i);
    }

    public boolean zzpF() {
        return this.zzanx;
    }
}
