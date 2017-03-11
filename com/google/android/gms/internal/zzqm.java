package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardProvisionData;
import java.util.List;
import org.bouncycastle.i18n.MessageBundle;

public class zzqm implements SafeParcelable {
    public static final zzqn CREATOR;
    final int zzFG;
    private final String zzapf;
    private final List<zza> zzapg;

    public static final class zza implements SafeParcelable {
        public static final zzql CREATOR;
        final int zzFG;
        private final String zzFQ;
        private final String zzSy;

        static {
            CREATOR = new zzql();
        }

        zza(int i, String str, String str2) {
            this.zzFG = i;
            this.zzSy = str;
            this.zzFQ = str2;
        }

        public int describeContents() {
            zzql com_google_android_gms_internal_zzql = CREATOR;
            return 0;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_internal_zzqm_zza = (zza) obj;
            return zzw.equal(this.zzSy, com_google_android_gms_internal_zzqm_zza.zzSy) && zzw.equal(this.zzFQ, com_google_android_gms_internal_zzqm_zza.zzFQ);
        }

        public String getTitle() {
            return this.zzSy;
        }

        public String getUri() {
            return this.zzFQ;
        }

        public int hashCode() {
            return zzw.hashCode(this.zzSy, this.zzFQ);
        }

        public String toString() {
            return zzw.zzk(this).zza(MessageBundle.TITLE_ENTRY, this.zzSy).zza("uri", this.zzFQ).toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzql com_google_android_gms_internal_zzql = CREATOR;
            zzql.zza(this, parcel, i);
        }
    }

    static {
        CREATOR = new zzqn();
    }

    zzqm(int i, String str, List<zza> list) {
        this.zzFG = i;
        this.zzapf = str;
        this.zzapg = list;
    }

    public int describeContents() {
        zzqn com_google_android_gms_internal_zzqn = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzqm)) {
            return false;
        }
        zzqm com_google_android_gms_internal_zzqm = (zzqm) obj;
        return zzw.equal(this.zzapf, com_google_android_gms_internal_zzqm.zzapf) && zzw.equal(this.zzapg, com_google_android_gms_internal_zzqm.zzapg);
    }

    public List<zza> getActions() {
        return this.zzapg;
    }

    public String getData() {
        return this.zzapf;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzapf, this.zzapg);
    }

    public String toString() {
        return zzw.zzk(this).zza(CardProvisionData.COL_DATA, this.zzapf).zza("actions", this.zzapg).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzqn com_google_android_gms_internal_zzqn = CREATOR;
        zzqn.zza(this, parcel, i);
    }
}
