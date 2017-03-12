package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzld.zzb;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class zzla implements SafeParcelable, zzb<String, Integer> {
    public static final zzlb CREATOR;
    private final int zzFG;
    private final HashMap<String, Integer> zzQK;
    private final HashMap<Integer, String> zzQL;
    private final ArrayList<zza> zzQM;

    public static final class zza implements SafeParcelable {
        public static final zzlc CREATOR;
        final int versionCode;
        final String zzQN;
        final int zzQO;

        static {
            CREATOR = new zzlc();
        }

        zza(int i, String str, int i2) {
            this.versionCode = i;
            this.zzQN = str;
            this.zzQO = i2;
        }

        zza(String str, int i) {
            this.versionCode = 1;
            this.zzQN = str;
            this.zzQO = i;
        }

        public int describeContents() {
            zzlc com_google_android_gms_internal_zzlc = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzlc com_google_android_gms_internal_zzlc = CREATOR;
            zzlc.zza(this, parcel, i);
        }
    }

    static {
        CREATOR = new zzlb();
    }

    public zzla() {
        this.zzFG = 1;
        this.zzQK = new HashMap();
        this.zzQL = new HashMap();
        this.zzQM = null;
    }

    zzla(int i, ArrayList<zza> arrayList) {
        this.zzFG = i;
        this.zzQK = new HashMap();
        this.zzQL = new HashMap();
        this.zzQM = null;
        zzb((ArrayList) arrayList);
    }

    private void zzb(ArrayList<zza> arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            zza com_google_android_gms_internal_zzla_zza = (zza) it.next();
            zzh(com_google_android_gms_internal_zzla_zza.zzQN, com_google_android_gms_internal_zzla_zza.zzQO);
        }
    }

    public /* synthetic */ Object convertBack(Object obj) {
        return zzb((Integer) obj);
    }

    public int describeContents() {
        zzlb com_google_android_gms_internal_zzlb = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzlb com_google_android_gms_internal_zzlb = CREATOR;
        zzlb.zza(this, parcel, i);
    }

    public String zzb(Integer num) {
        String str = (String) this.zzQL.get(num);
        return (str == null && this.zzQK.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    public zzla zzh(String str, int i) {
        this.zzQK.put(str, Integer.valueOf(i));
        this.zzQL.put(Integer.valueOf(i), str);
        return this;
    }

    ArrayList<zza> zzjw() {
        ArrayList<zza> arrayList = new ArrayList();
        for (String str : this.zzQK.keySet()) {
            arrayList.add(new zza(str, ((Integer) this.zzQK.get(str)).intValue()));
        }
        return arrayList;
    }

    public int zzjx() {
        return 7;
    }

    public int zzjy() {
        return 0;
    }
}
