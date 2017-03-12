package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class zzuw implements SafeParcelable {
    public static final Creator<zzuw> CREATOR;
    final int zzFG;
    final boolean zzawX;
    final List<Scope> zzawY;

    static {
        CREATOR = new zzux();
    }

    zzuw(int i, boolean z, List<Scope> list) {
        this.zzFG = i;
        this.zzawX = z;
        this.zzawY = list;
    }

    public zzuw(boolean z, Set<Scope> set) {
        this(1, z, zza(set));
    }

    private static List<Scope> zza(Set<Scope> set) {
        return set == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(set));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzux.zza(this, parcel, i);
    }
}
