package com.google.android.gms.internal;

public abstract class zzws<M extends zzws<M>> extends zzwy {
    protected zzwu zzaHB;

    public /* synthetic */ Object clone() {
        return zzvM();
    }

    public final <T> T zza(zzwt<M, T> com_google_android_gms_internal_zzwt_M__T) {
        if (this.zzaHB == null) {
            return null;
        }
        zzwv zziE = this.zzaHB.zziE(zzxb.zziI(com_google_android_gms_internal_zzwt_M__T.tag));
        return zziE != null ? zziE.zzb(com_google_android_gms_internal_zzwt_M__T) : null;
    }

    public void zza(zzwr com_google_android_gms_internal_zzwr) {
        if (this.zzaHB != null) {
            for (int i = 0; i < this.zzaHB.size(); i++) {
                this.zzaHB.zziF(i).zza(com_google_android_gms_internal_zzwr);
            }
        }
    }

    protected final boolean zza(zzwq com_google_android_gms_internal_zzwq, int i) {
        int position = com_google_android_gms_internal_zzwq.getPosition();
        if (!com_google_android_gms_internal_zzwq.zzin(i)) {
            return false;
        }
        int zziI = zzxb.zziI(i);
        zzxa com_google_android_gms_internal_zzxa = new zzxa(i, com_google_android_gms_internal_zzwq.zzx(position, com_google_android_gms_internal_zzwq.getPosition() - position));
        zzwv com_google_android_gms_internal_zzwv = null;
        if (this.zzaHB == null) {
            this.zzaHB = new zzwu();
        } else {
            com_google_android_gms_internal_zzwv = this.zzaHB.zziE(zziI);
        }
        if (com_google_android_gms_internal_zzwv == null) {
            com_google_android_gms_internal_zzwv = new zzwv();
            this.zzaHB.zza(zziI, com_google_android_gms_internal_zzwv);
        }
        com_google_android_gms_internal_zzwv.zza(com_google_android_gms_internal_zzxa);
        return true;
    }

    protected final boolean zza(M m) {
        return (this.zzaHB == null || this.zzaHB.isEmpty()) ? m.zzaHB == null || m.zzaHB.isEmpty() : this.zzaHB.equals(m.zzaHB);
    }

    protected int zzc() {
        int i = 0;
        if (this.zzaHB == null) {
            return 0;
        }
        int i2 = 0;
        while (i < this.zzaHB.size()) {
            i2 += this.zzaHB.zziF(i).zzc();
            i++;
        }
        return i2;
    }

    protected final int zzvL() {
        return (this.zzaHB == null || this.zzaHB.isEmpty()) ? 0 : this.zzaHB.hashCode();
    }

    public M zzvM() {
        zzws com_google_android_gms_internal_zzws = (zzws) super.zzvN();
        zzww.zza(this, com_google_android_gms_internal_zzws);
        return com_google_android_gms_internal_zzws;
    }

    public /* synthetic */ zzwy zzvN() {
        return zzvM();
    }
}
