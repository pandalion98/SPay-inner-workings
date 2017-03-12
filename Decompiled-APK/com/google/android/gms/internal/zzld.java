package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;

public abstract class zzld {

    public interface zzb<I, O> {
        I convertBack(O o);

        int zzjx();

        int zzjy();
    }

    public static class zza<I, O> implements SafeParcelable {
        public static final zzlf CREATOR;
        private final int zzFG;
        protected final int zzQP;
        protected final boolean zzQQ;
        protected final int zzQR;
        protected final boolean zzQS;
        protected final String zzQT;
        protected final int zzQU;
        protected final Class<? extends zzld> zzQV;
        protected final String zzQW;
        private zzlh zzQX;
        private zzb<I, O> zzQY;

        static {
            CREATOR = new zzlf();
        }

        zza(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, zzky com_google_android_gms_internal_zzky) {
            this.zzFG = i;
            this.zzQP = i2;
            this.zzQQ = z;
            this.zzQR = i3;
            this.zzQS = z2;
            this.zzQT = str;
            this.zzQU = i4;
            if (str2 == null) {
                this.zzQV = null;
                this.zzQW = null;
            } else {
                this.zzQV = zzlk.class;
                this.zzQW = str2;
            }
            if (com_google_android_gms_internal_zzky == null) {
                this.zzQY = null;
            } else {
                this.zzQY = com_google_android_gms_internal_zzky.zzjv();
            }
        }

        protected zza(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends zzld> cls, zzb<I, O> com_google_android_gms_internal_zzld_zzb_I__O) {
            this.zzFG = 1;
            this.zzQP = i;
            this.zzQQ = z;
            this.zzQR = i2;
            this.zzQS = z2;
            this.zzQT = str;
            this.zzQU = i3;
            this.zzQV = cls;
            if (cls == null) {
                this.zzQW = null;
            } else {
                this.zzQW = cls.getCanonicalName();
            }
            this.zzQY = com_google_android_gms_internal_zzld_zzb_I__O;
        }

        public static zza zza(String str, int i, zzb<?, ?> com_google_android_gms_internal_zzld_zzb___, boolean z) {
            return new zza(com_google_android_gms_internal_zzld_zzb___.zzjx(), z, com_google_android_gms_internal_zzld_zzb___.zzjy(), false, str, i, null, com_google_android_gms_internal_zzld_zzb___);
        }

        public static <T extends zzld> zza<T, T> zza(String str, int i, Class<T> cls) {
            return new zza(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends zzld> zza<ArrayList<T>, ArrayList<T>> zzb(String str, int i, Class<T> cls) {
            return new zza(11, true, 11, true, str, i, cls, null);
        }

        public static zza<Integer, Integer> zzi(String str, int i) {
            return new zza(0, false, 0, false, str, i, null, null);
        }

        public static zza<Double, Double> zzj(String str, int i) {
            return new zza(4, false, 4, false, str, i, null, null);
        }

        public static zza<Boolean, Boolean> zzk(String str, int i) {
            return new zza(6, false, 6, false, str, i, null, null);
        }

        public static zza<String, String> zzl(String str, int i) {
            return new zza(7, false, 7, false, str, i, null, null);
        }

        public static zza<ArrayList<String>, ArrayList<String>> zzm(String str, int i) {
            return new zza(7, true, 7, true, str, i, null, null);
        }

        public I convertBack(O o) {
            return this.zzQY.convertBack(o);
        }

        public int describeContents() {
            zzlf com_google_android_gms_internal_zzlf = CREATOR;
            return 0;
        }

        public int getVersionCode() {
            return this.zzFG;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Field\n");
            stringBuilder.append("            versionCode=").append(this.zzFG).append('\n');
            stringBuilder.append("                 typeIn=").append(this.zzQP).append('\n');
            stringBuilder.append("            typeInArray=").append(this.zzQQ).append('\n');
            stringBuilder.append("                typeOut=").append(this.zzQR).append('\n');
            stringBuilder.append("           typeOutArray=").append(this.zzQS).append('\n');
            stringBuilder.append("        outputFieldName=").append(this.zzQT).append('\n');
            stringBuilder.append("      safeParcelFieldId=").append(this.zzQU).append('\n');
            stringBuilder.append("       concreteTypeName=").append(zzjI()).append('\n');
            if (zzjH() != null) {
                stringBuilder.append("     concreteType.class=").append(zzjH().getCanonicalName()).append('\n');
            }
            stringBuilder.append("          converterName=").append(this.zzQY == null ? "null" : this.zzQY.getClass().getCanonicalName()).append('\n');
            return stringBuilder.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            zzlf com_google_android_gms_internal_zzlf = CREATOR;
            zzlf.zza(this, parcel, i);
        }

        public void zza(zzlh com_google_android_gms_internal_zzlh) {
            this.zzQX = com_google_android_gms_internal_zzlh;
        }

        public zza<I, O> zzjC() {
            return new zza(this.zzFG, this.zzQP, this.zzQQ, this.zzQR, this.zzQS, this.zzQT, this.zzQU, this.zzQW, zzjK());
        }

        public boolean zzjD() {
            return this.zzQQ;
        }

        public boolean zzjE() {
            return this.zzQS;
        }

        public String zzjF() {
            return this.zzQT;
        }

        public int zzjG() {
            return this.zzQU;
        }

        public Class<? extends zzld> zzjH() {
            return this.zzQV;
        }

        String zzjI() {
            return this.zzQW == null ? null : this.zzQW;
        }

        public boolean zzjJ() {
            return this.zzQY != null;
        }

        zzky zzjK() {
            return this.zzQY == null ? null : zzky.zza(this.zzQY);
        }

        public Map<String, zza<?, ?>> zzjL() {
            zzx.zzl(this.zzQW);
            zzx.zzl(this.zzQX);
            return this.zzQX.zzbs(this.zzQW);
        }

        public int zzjx() {
            return this.zzQP;
        }

        public int zzjy() {
            return this.zzQR;
        }
    }

    private void zza(StringBuilder stringBuilder, zza com_google_android_gms_internal_zzld_zza, Object obj) {
        if (com_google_android_gms_internal_zzld_zza.zzjx() == 11) {
            stringBuilder.append(((zzld) com_google_android_gms_internal_zzld_zza.zzjH().cast(obj)).toString());
        } else if (com_google_android_gms_internal_zzld_zza.zzjx() == 7) {
            stringBuilder.append("\"");
            stringBuilder.append(zzma.zzbt((String) obj));
            stringBuilder.append("\"");
        } else {
            stringBuilder.append(obj);
        }
    }

    private void zza(StringBuilder stringBuilder, zza com_google_android_gms_internal_zzld_zza, ArrayList<Object> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(stringBuilder, com_google_android_gms_internal_zzld_zza, obj);
            }
        }
        stringBuilder.append("]");
    }

    public String toString() {
        Map zzjz = zzjz();
        StringBuilder stringBuilder = new StringBuilder(100);
        for (String str : zzjz.keySet()) {
            zza com_google_android_gms_internal_zzld_zza = (zza) zzjz.get(str);
            if (zza(com_google_android_gms_internal_zzld_zza)) {
                Object zza = zza(com_google_android_gms_internal_zzld_zza, zzb(com_google_android_gms_internal_zzld_zza));
                if (stringBuilder.length() == 0) {
                    stringBuilder.append("{");
                } else {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"").append(str).append("\":");
                if (zza != null) {
                    switch (com_google_android_gms_internal_zzld_zza.zzjy()) {
                        case X509KeyUsage.keyAgreement /*8*/:
                            stringBuilder.append("\"").append(zzlt.zze((byte[]) zza)).append("\"");
                            break;
                        case NamedCurve.sect283k1 /*9*/:
                            stringBuilder.append("\"").append(zzlt.zzf((byte[]) zza)).append("\"");
                            break;
                        case NamedCurve.sect283r1 /*10*/:
                            zzmb.zza(stringBuilder, (HashMap) zza);
                            break;
                        default:
                            if (!com_google_android_gms_internal_zzld_zza.zzjD()) {
                                zza(stringBuilder, com_google_android_gms_internal_zzld_zza, zza);
                                break;
                            }
                            zza(stringBuilder, com_google_android_gms_internal_zzld_zza, (ArrayList) zza);
                            break;
                    }
                }
                stringBuilder.append("null");
            }
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append("{}");
        }
        return stringBuilder.toString();
    }

    protected <O, I> I zza(zza<I, O> com_google_android_gms_internal_zzld_zza_I__O, Object obj) {
        return com_google_android_gms_internal_zzld_zza_I__O.zzQY != null ? com_google_android_gms_internal_zzld_zza_I__O.convertBack(obj) : obj;
    }

    protected boolean zza(zza com_google_android_gms_internal_zzld_zza) {
        return com_google_android_gms_internal_zzld_zza.zzjy() == 11 ? com_google_android_gms_internal_zzld_zza.zzjE() ? zzbr(com_google_android_gms_internal_zzld_zza.zzjF()) : zzbq(com_google_android_gms_internal_zzld_zza.zzjF()) : zzbp(com_google_android_gms_internal_zzld_zza.zzjF());
    }

    protected Object zzb(zza com_google_android_gms_internal_zzld_zza) {
        String zzjF = com_google_android_gms_internal_zzld_zza.zzjF();
        if (com_google_android_gms_internal_zzld_zza.zzjH() == null) {
            return zzbo(com_google_android_gms_internal_zzld_zza.zzjF());
        }
        zzx.zza(zzbo(com_google_android_gms_internal_zzld_zza.zzjF()) == null, "Concrete field shouldn't be value object: %s", com_google_android_gms_internal_zzld_zza.zzjF());
        Map zzjB = com_google_android_gms_internal_zzld_zza.zzjE() ? zzjB() : zzjA();
        if (zzjB != null) {
            return zzjB.get(zzjF);
        }
        try {
            return getClass().getMethod("get" + Character.toUpperCase(zzjF.charAt(0)) + zzjF.substring(1), new Class[0]).invoke(this, new Object[0]);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Object zzbo(String str);

    protected abstract boolean zzbp(String str);

    protected boolean zzbq(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean zzbr(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    public HashMap<String, Object> zzjA() {
        return null;
    }

    public HashMap<String, Object> zzjB() {
        return null;
    }

    public abstract Map<String, zza<?, ?>> zzjz();
}
