package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class zzlh implements SafeParcelable {
    public static final zzli CREATOR;
    private final int zzFG;
    private final HashMap<String, Map<String, com.google.android.gms.internal.zzld.zza<?, ?>>> zzQZ;
    private final ArrayList<zza> zzRa;
    private final String zzRb;

    public static class zza implements SafeParcelable {
        public static final zzlj CREATOR;
        final String className;
        final int versionCode;
        final ArrayList<zzb> zzRc;

        static {
            CREATOR = new zzlj();
        }

        zza(int i, String str, ArrayList<zzb> arrayList) {
            this.versionCode = i;
            this.className = str;
            this.zzRc = arrayList;
        }

        zza(String str, Map<String, com.google.android.gms.internal.zzld.zza<?, ?>> map) {
            this.versionCode = 1;
            this.className = str;
            this.zzRc = zzE(map);
        }

        private static ArrayList<zzb> zzE(Map<String, com.google.android.gms.internal.zzld.zza<?, ?>> map) {
            if (map == null) {
                return null;
            }
            ArrayList<zzb> arrayList = new ArrayList();
            for (String str : map.keySet()) {
                arrayList.add(new zzb(str, (com.google.android.gms.internal.zzld.zza) map.get(str)));
            }
            return arrayList;
        }

        public int describeContents() {
            zzlj com_google_android_gms_internal_zzlj = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzlj com_google_android_gms_internal_zzlj = CREATOR;
            zzlj.zza(this, parcel, i);
        }

        HashMap<String, com.google.android.gms.internal.zzld.zza<?, ?>> zzjQ() {
            HashMap<String, com.google.android.gms.internal.zzld.zza<?, ?>> hashMap = new HashMap();
            int size = this.zzRc.size();
            for (int i = 0; i < size; i++) {
                zzb com_google_android_gms_internal_zzlh_zzb = (zzb) this.zzRc.get(i);
                hashMap.put(com_google_android_gms_internal_zzlh_zzb.zzfv, com_google_android_gms_internal_zzlh_zzb.zzRd);
            }
            return hashMap;
        }
    }

    public static class zzb implements SafeParcelable {
        public static final zzlg CREATOR;
        final int versionCode;
        final com.google.android.gms.internal.zzld.zza<?, ?> zzRd;
        final String zzfv;

        static {
            CREATOR = new zzlg();
        }

        zzb(int i, String str, com.google.android.gms.internal.zzld.zza<?, ?> com_google_android_gms_internal_zzld_zza___) {
            this.versionCode = i;
            this.zzfv = str;
            this.zzRd = com_google_android_gms_internal_zzld_zza___;
        }

        zzb(String str, com.google.android.gms.internal.zzld.zza<?, ?> com_google_android_gms_internal_zzld_zza___) {
            this.versionCode = 1;
            this.zzfv = str;
            this.zzRd = com_google_android_gms_internal_zzld_zza___;
        }

        public int describeContents() {
            zzlg com_google_android_gms_internal_zzlg = CREATOR;
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzlg com_google_android_gms_internal_zzlg = CREATOR;
            zzlg.zza(this, parcel, i);
        }
    }

    static {
        CREATOR = new zzli();
    }

    zzlh(int i, ArrayList<zza> arrayList, String str) {
        this.zzFG = i;
        this.zzRa = null;
        this.zzQZ = zzc(arrayList);
        this.zzRb = (String) zzx.zzl(str);
        zzjM();
    }

    public zzlh(Class<? extends zzld> cls) {
        this.zzFG = 1;
        this.zzRa = null;
        this.zzQZ = new HashMap();
        this.zzRb = cls.getCanonicalName();
    }

    private static HashMap<String, Map<String, com.google.android.gms.internal.zzld.zza<?, ?>>> zzc(ArrayList<zza> arrayList) {
        HashMap<String, Map<String, com.google.android.gms.internal.zzld.zza<?, ?>>> hashMap = new HashMap();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            zza com_google_android_gms_internal_zzlh_zza = (zza) arrayList.get(i);
            hashMap.put(com_google_android_gms_internal_zzlh_zza.className, com_google_android_gms_internal_zzlh_zza.zzjQ());
        }
        return hashMap;
    }

    public int describeContents() {
        zzli com_google_android_gms_internal_zzli = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : this.zzQZ.keySet()) {
            stringBuilder.append(str).append(":\n");
            Map map = (Map) this.zzQZ.get(str);
            for (String str2 : map.keySet()) {
                stringBuilder.append("  ").append(str2).append(": ");
                stringBuilder.append(map.get(str2));
            }
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzli com_google_android_gms_internal_zzli = CREATOR;
        zzli.zza(this, parcel, i);
    }

    public void zza(Class<? extends zzld> cls, Map<String, com.google.android.gms.internal.zzld.zza<?, ?>> map) {
        this.zzQZ.put(cls.getCanonicalName(), map);
    }

    public boolean zzb(Class<? extends zzld> cls) {
        return this.zzQZ.containsKey(cls.getCanonicalName());
    }

    public Map<String, com.google.android.gms.internal.zzld.zza<?, ?>> zzbs(String str) {
        return (Map) this.zzQZ.get(str);
    }

    public void zzjM() {
        for (String str : this.zzQZ.keySet()) {
            Map map = (Map) this.zzQZ.get(str);
            for (String str2 : map.keySet()) {
                ((com.google.android.gms.internal.zzld.zza) map.get(str2)).zza(this);
            }
        }
    }

    public void zzjN() {
        for (String str : this.zzQZ.keySet()) {
            Map map = (Map) this.zzQZ.get(str);
            HashMap hashMap = new HashMap();
            for (String str2 : map.keySet()) {
                hashMap.put(str2, ((com.google.android.gms.internal.zzld.zza) map.get(str2)).zzjC());
            }
            this.zzQZ.put(str, hashMap);
        }
    }

    ArrayList<zza> zzjO() {
        ArrayList<zza> arrayList = new ArrayList();
        for (String str : this.zzQZ.keySet()) {
            arrayList.add(new zza(str, (Map) this.zzQZ.get(str)));
        }
        return arrayList;
    }

    public String zzjP() {
        return this.zzRb;
    }
}
