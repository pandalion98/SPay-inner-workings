package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.samsung.android.spayfw.appinterface.PushMessage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public final class zzpy implements SafeParcelable, Place {
    public static final zzpz CREATOR;
    private final String mName;
    private final String zzFA;
    final int zzFG;
    private final String zzYy;
    private final LatLng zzant;
    private final List<Integer> zzanu;
    private final String zzanv;
    private final Uri zzanw;
    private final String zzaoA;
    private final boolean zzaoB;
    private final float zzaoC;
    private final int zzaoD;
    private final long zzaoE;
    private final List<Integer> zzaoF;
    private final String zzaoG;
    private final List<String> zzaoH;
    final boolean zzaoI;
    private final Map<Integer, String> zzaoJ;
    private final TimeZone zzaoK;
    private zzqf zzaoL;
    private Locale zzaoq;
    private final Bundle zzaow;
    @Deprecated
    private final zzqd zzaox;
    private final float zzaoy;
    private final LatLngBounds zzaoz;

    public static class zza {
        private String mName;
        private String zzFA;
        private int zzFG;
        private String zzYy;
        private LatLng zzant;
        private String zzanv;
        private Uri zzanw;
        private String zzaoA;
        private boolean zzaoB;
        private float zzaoC;
        private int zzaoD;
        private long zzaoE;
        private String zzaoG;
        private List<String> zzaoH;
        private boolean zzaoI;
        private Bundle zzaoM;
        private List<Integer> zzaoN;
        private float zzaoy;
        private LatLngBounds zzaoz;

        public zza() {
            this.zzFG = 0;
        }

        public zza zzX(boolean z) {
            this.zzaoB = z;
            return this;
        }

        public zza zzY(boolean z) {
            this.zzaoI = z;
            return this;
        }

        public zza zza(LatLng latLng) {
            this.zzant = latLng;
            return this;
        }

        public zza zza(LatLngBounds latLngBounds) {
            this.zzaoz = latLngBounds;
            return this;
        }

        public zza zzc(float f) {
            this.zzaoy = f;
            return this;
        }

        public zza zzco(String str) {
            this.zzFA = str;
            return this;
        }

        public zza zzcp(String str) {
            this.mName = str;
            return this;
        }

        public zza zzcq(String str) {
            this.zzYy = str;
            return this;
        }

        public zza zzcr(String str) {
            this.zzanv = str;
            return this;
        }

        public zza zzd(float f) {
            this.zzaoC = f;
            return this;
        }

        public zza zzfH(int i) {
            this.zzaoD = i;
            return this;
        }

        public zza zzk(Uri uri) {
            this.zzanw = uri;
            return this;
        }

        public zza zzo(List<Integer> list) {
            this.zzaoN = list;
            return this;
        }

        public zza zzp(List<String> list) {
            this.zzaoH = list;
            return this;
        }

        public zzpy zzpW() {
            return new zzpy(this.zzFG, this.zzFA, this.zzaoN, Collections.emptyList(), this.zzaoM, this.mName, this.zzYy, this.zzanv, this.zzaoG, this.zzaoH, this.zzant, this.zzaoy, this.zzaoz, this.zzaoA, this.zzanw, this.zzaoB, this.zzaoC, this.zzaoD, this.zzaoE, this.zzaoI, zzqd.zza(this.mName, this.zzYy, this.zzanv, this.zzaoG, this.zzaoH));
        }
    }

    static {
        CREATOR = new zzpz();
    }

    zzpy(int i, String str, List<Integer> list, List<Integer> list2, Bundle bundle, String str2, String str3, String str4, String str5, List<String> list3, LatLng latLng, float f, LatLngBounds latLngBounds, String str6, Uri uri, boolean z, float f2, int i2, long j, boolean z2, zzqd com_google_android_gms_internal_zzqd) {
        List emptyList;
        this.zzFG = i;
        this.zzFA = str;
        this.zzanu = Collections.unmodifiableList(list);
        this.zzaoF = list2;
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.zzaow = bundle;
        this.mName = str2;
        this.zzYy = str3;
        this.zzanv = str4;
        this.zzaoG = str5;
        if (list3 == null) {
            emptyList = Collections.emptyList();
        }
        this.zzaoH = emptyList;
        this.zzant = latLng;
        this.zzaoy = f;
        this.zzaoz = latLngBounds;
        if (str6 == null) {
            str6 = "UTC";
        }
        this.zzaoA = str6;
        this.zzanw = uri;
        this.zzaoB = z;
        this.zzaoC = f2;
        this.zzaoD = i2;
        this.zzaoE = j;
        this.zzaoJ = Collections.unmodifiableMap(new HashMap());
        this.zzaoK = null;
        this.zzaoq = null;
        this.zzaoI = z2;
        this.zzaox = com_google_android_gms_internal_zzqd;
    }

    private void zzcn(String str) {
        if (this.zzaoI && this.zzaoL != null) {
            this.zzaoL.zzy(this.zzFA, str);
        }
    }

    public int describeContents() {
        zzpz com_google_android_gms_internal_zzpz = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzpy)) {
            return false;
        }
        zzpy com_google_android_gms_internal_zzpy = (zzpy) obj;
        return this.zzFA.equals(com_google_android_gms_internal_zzpy.zzFA) && zzw.equal(this.zzaoq, com_google_android_gms_internal_zzpy.zzaoq) && this.zzaoE == com_google_android_gms_internal_zzpy.zzaoE;
    }

    public /* synthetic */ Object freeze() {
        return zzpV();
    }

    public String getAddress() {
        zzcn("getAddress");
        return this.zzYy;
    }

    public String getId() {
        zzcn("getId");
        return this.zzFA;
    }

    public LatLng getLatLng() {
        zzcn("getLatLng");
        return this.zzant;
    }

    public Locale getLocale() {
        zzcn("getLocale");
        return this.zzaoq;
    }

    public String getName() {
        zzcn("getName");
        return this.mName;
    }

    public String getPhoneNumber() {
        zzcn("getPhoneNumber");
        return this.zzanv;
    }

    public List<Integer> getPlaceTypes() {
        zzcn("getPlaceTypes");
        return this.zzanu;
    }

    public int getPriceLevel() {
        zzcn("getPriceLevel");
        return this.zzaoD;
    }

    public float getRating() {
        zzcn("getRating");
        return this.zzaoC;
    }

    public LatLngBounds getViewport() {
        zzcn("getViewport");
        return this.zzaoz;
    }

    public Uri getWebsiteUri() {
        zzcn("getWebsiteUri");
        return this.zzanw;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzFA, this.zzaoq, Long.valueOf(this.zzaoE));
    }

    public boolean isDataValid() {
        return true;
    }

    public void setLocale(Locale locale) {
        this.zzaoq = locale;
    }

    public String toString() {
        return zzw.zzk(this).zza(PushMessage.JSON_KEY_ID, this.zzFA).zza("placeTypes", this.zzanu).zza("locale", this.zzaoq).zza("name", this.mName).zza("address", this.zzYy).zza("phoneNumber", this.zzanv).zza("latlng", this.zzant).zza("viewport", this.zzaoz).zza("websiteUri", this.zzanw).zza("isPermanentlyClosed", Boolean.valueOf(this.zzaoB)).zza("priceLevel", Integer.valueOf(this.zzaoD)).zza("timestampSecs", Long.valueOf(this.zzaoE)).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzpz com_google_android_gms_internal_zzpz = CREATOR;
        zzpz.zza(this, parcel, i);
    }

    public void zza(zzqf com_google_android_gms_internal_zzqf) {
        this.zzaoL = com_google_android_gms_internal_zzqf;
    }

    public boolean zzpI() {
        zzcn("isPermanentlyClosed");
        return this.zzaoB;
    }

    public List<Integer> zzpN() {
        zzcn("getTypesDeprecated");
        return this.zzaoF;
    }

    public float zzpO() {
        zzcn("getLevelNumber");
        return this.zzaoy;
    }

    public String zzpP() {
        zzcn("getRegularOpenHours");
        return this.zzaoG;
    }

    public List<String> zzpQ() {
        zzcn("getAttributions");
        return this.zzaoH;
    }

    public long zzpR() {
        return this.zzaoE;
    }

    public Bundle zzpS() {
        return this.zzaow;
    }

    public String zzpT() {
        return this.zzaoA;
    }

    @Deprecated
    public zzqd zzpU() {
        zzcn("getLocalization");
        return this.zzaox;
    }

    public Place zzpV() {
        return this;
    }
}
