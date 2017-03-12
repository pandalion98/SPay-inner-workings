package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePrediction.Substring;
import java.util.List;

public class zzpo implements SafeParcelable, AutocompletePrediction {
    public static final Creator<zzpo> CREATOR;
    final int zzFG;
    final String zzZO;
    final String zzanM;
    final List<Integer> zzanu;
    final List<zza> zzaoi;
    final int zzaoj;

    public static class zza implements SafeParcelable, Substring {
        public static final Creator<zza> CREATOR;
        final int mLength;
        final int mOffset;
        final int zzFG;

        static {
            CREATOR = new zzqk();
        }

        public zza(int i, int i2, int i3) {
            this.zzFG = i;
            this.mOffset = i2;
            this.mLength = i3;
        }

        public int describeContents() {
            return 0;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_internal_zzpo_zza = (zza) obj;
            return zzw.equal(Integer.valueOf(this.mOffset), Integer.valueOf(com_google_android_gms_internal_zzpo_zza.mOffset)) && zzw.equal(Integer.valueOf(this.mLength), Integer.valueOf(com_google_android_gms_internal_zzpo_zza.mLength));
        }

        public int getLength() {
            return this.mLength;
        }

        public int getOffset() {
            return this.mOffset;
        }

        public int hashCode() {
            return zzw.hashCode(Integer.valueOf(this.mOffset), Integer.valueOf(this.mLength));
        }

        public String toString() {
            return zzw.zzk(this).zza("offset", Integer.valueOf(this.mOffset)).zza("length", Integer.valueOf(this.mLength)).toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzqk.zza(this, parcel, i);
        }
    }

    static {
        CREATOR = new zzpp();
    }

    zzpo(int i, String str, String str2, List<Integer> list, List<zza> list2, int i2) {
        this.zzFG = i;
        this.zzZO = str;
        this.zzanM = str2;
        this.zzanu = list;
        this.zzaoi = list2;
        this.zzaoj = i2;
    }

    public static zzpo zza(String str, String str2, List<Integer> list, List<zza> list2, int i) {
        return new zzpo(0, (String) zzx.zzl(str), str2, list, list2, i);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzpo)) {
            return false;
        }
        zzpo com_google_android_gms_internal_zzpo = (zzpo) obj;
        return zzw.equal(this.zzZO, com_google_android_gms_internal_zzpo.zzZO) && zzw.equal(this.zzanM, com_google_android_gms_internal_zzpo.zzanM) && zzw.equal(this.zzanu, com_google_android_gms_internal_zzpo.zzanu) && zzw.equal(this.zzaoi, com_google_android_gms_internal_zzpo.zzaoi) && zzw.equal(Integer.valueOf(this.zzaoj), Integer.valueOf(com_google_android_gms_internal_zzpo.zzaoj));
    }

    public /* synthetic */ Object freeze() {
        return zzpL();
    }

    public String getDescription() {
        return this.zzZO;
    }

    public List<? extends Substring> getMatchedSubstrings() {
        return this.zzaoi;
    }

    public String getPlaceId() {
        return this.zzanM;
    }

    public List<Integer> getPlaceTypes() {
        return this.zzanu;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzZO, this.zzanM, this.zzanu, this.zzaoi, Integer.valueOf(this.zzaoj));
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return zzw.zzk(this).zza("description", this.zzZO).zza("placeId", this.zzanM).zza("placeTypes", this.zzanu).zza("substrings", this.zzaoi).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzpp.zza(this, parcel, i);
    }

    public AutocompletePrediction zzpL() {
        return this;
    }
}
