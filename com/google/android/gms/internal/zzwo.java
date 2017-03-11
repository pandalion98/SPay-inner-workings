package com.google.android.gms.internal;

import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;

public final class zzwo extends zzws<zzwo> {
    public String[] zzaHl;
    public int[] zzaHm;
    public byte[][] zzaHn;

    public zzwo() {
        zzvt();
    }

    public static zzwo zzr(byte[] bArr) {
        return (zzwo) zzwy.zza(new zzwo(), bArr);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzwo)) {
            return false;
        }
        zzwo com_google_android_gms_internal_zzwo = (zzwo) obj;
        return (zzww.equals(this.zzaHl, com_google_android_gms_internal_zzwo.zzaHl) && zzww.equals(this.zzaHm, com_google_android_gms_internal_zzwo.zzaHm) && zzww.zza(this.zzaHn, com_google_android_gms_internal_zzwo.zzaHn)) ? zza((zzws) com_google_android_gms_internal_zzwo) : false;
    }

    public int hashCode() {
        return ((((((zzww.hashCode(this.zzaHl) + 527) * 31) + zzww.hashCode(this.zzaHm)) * 31) + zzww.zza(this.zzaHn)) * 31) + zzvL();
    }

    public void zza(zzwr com_google_android_gms_internal_zzwr) {
        int i = 0;
        if (this.zzaHl != null && this.zzaHl.length > 0) {
            for (String str : this.zzaHl) {
                if (str != null) {
                    com_google_android_gms_internal_zzwr.zzb(1, str);
                }
            }
        }
        if (this.zzaHm != null && this.zzaHm.length > 0) {
            for (int zzy : this.zzaHm) {
                com_google_android_gms_internal_zzwr.zzy(2, zzy);
            }
        }
        if (this.zzaHn != null && this.zzaHn.length > 0) {
            while (i < this.zzaHn.length) {
                byte[] bArr = this.zzaHn[i];
                if (bArr != null) {
                    com_google_android_gms_internal_zzwr.zza(3, bArr);
                }
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_zzwr);
    }

    public /* synthetic */ zzwy zzb(zzwq com_google_android_gms_internal_zzwq) {
        return zzy(com_google_android_gms_internal_zzwq);
    }

    protected int zzc() {
        int i;
        int i2;
        int i3;
        int i4 = 0;
        int zzc = super.zzc();
        if (this.zzaHl == null || this.zzaHl.length <= 0) {
            i = zzc;
        } else {
            i2 = 0;
            i3 = 0;
            for (String str : this.zzaHl) {
                if (str != null) {
                    i3++;
                    i2 += zzwr.zzdM(str);
                }
            }
            i = (zzc + i2) + (i3 * 1);
        }
        if (this.zzaHm != null && this.zzaHm.length > 0) {
            i3 = 0;
            for (int zzc2 : this.zzaHm) {
                i3 += zzwr.zziw(zzc2);
            }
            i = (i + i3) + (this.zzaHm.length * 1);
        }
        if (this.zzaHn == null || this.zzaHn.length <= 0) {
            return i;
        }
        i2 = 0;
        i3 = 0;
        while (i4 < this.zzaHn.length) {
            byte[] bArr = this.zzaHn[i4];
            if (bArr != null) {
                i3++;
                i2 += zzwr.zzw(bArr);
            }
            i4++;
        }
        return (i + i2) + (i3 * 1);
    }

    public zzwo zzvt() {
        this.zzaHl = zzxb.zzaHT;
        this.zzaHm = zzxb.zzaHO;
        this.zzaHn = zzxb.zzaHU;
        this.zzaHB = null;
        this.zzaHM = -1;
        return this;
    }

    public zzwo zzy(zzwq com_google_android_gms_internal_zzwq) {
        while (true) {
            int zzvu = com_google_android_gms_internal_zzwq.zzvu();
            int zzc;
            Object obj;
            switch (zzvu) {
                case ECCurve.COORD_AFFINE /*0*/:
                    break;
                case NamedCurve.sect283r1 /*10*/:
                    zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 10);
                    zzvu = this.zzaHl == null ? 0 : this.zzaHl.length;
                    obj = new String[(zzc + zzvu)];
                    if (zzvu != 0) {
                        System.arraycopy(this.zzaHl, 0, obj, 0, zzvu);
                    }
                    while (zzvu < obj.length - 1) {
                        obj[zzvu] = com_google_android_gms_internal_zzwq.readString();
                        com_google_android_gms_internal_zzwq.zzvu();
                        zzvu++;
                    }
                    obj[zzvu] = com_google_android_gms_internal_zzwq.readString();
                    this.zzaHl = obj;
                    continue;
                case X509KeyUsage.dataEncipherment /*16*/:
                    zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 16);
                    zzvu = this.zzaHm == null ? 0 : this.zzaHm.length;
                    obj = new int[(zzc + zzvu)];
                    if (zzvu != 0) {
                        System.arraycopy(this.zzaHm, 0, obj, 0, zzvu);
                    }
                    while (zzvu < obj.length - 1) {
                        obj[zzvu] = com_google_android_gms_internal_zzwq.zzvx();
                        com_google_android_gms_internal_zzwq.zzvu();
                        zzvu++;
                    }
                    obj[zzvu] = com_google_android_gms_internal_zzwq.zzvx();
                    this.zzaHm = obj;
                    continue;
                case NamedCurve.secp192k1 /*18*/:
                    int zzip = com_google_android_gms_internal_zzwq.zzip(com_google_android_gms_internal_zzwq.zzvB());
                    zzc = com_google_android_gms_internal_zzwq.getPosition();
                    zzvu = 0;
                    while (com_google_android_gms_internal_zzwq.zzvG() > 0) {
                        com_google_android_gms_internal_zzwq.zzvx();
                        zzvu++;
                    }
                    com_google_android_gms_internal_zzwq.zzir(zzc);
                    zzc = this.zzaHm == null ? 0 : this.zzaHm.length;
                    Object obj2 = new int[(zzvu + zzc)];
                    if (zzc != 0) {
                        System.arraycopy(this.zzaHm, 0, obj2, 0, zzc);
                    }
                    while (zzc < obj2.length) {
                        obj2[zzc] = com_google_android_gms_internal_zzwq.zzvx();
                        zzc++;
                    }
                    this.zzaHm = obj2;
                    com_google_android_gms_internal_zzwq.zziq(zzip);
                    continue;
                case NamedCurve.brainpoolP256r1 /*26*/:
                    zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 26);
                    zzvu = this.zzaHn == null ? 0 : this.zzaHn.length;
                    obj = new byte[(zzc + zzvu)][];
                    if (zzvu != 0) {
                        System.arraycopy(this.zzaHn, 0, obj, 0, zzvu);
                    }
                    while (zzvu < obj.length - 1) {
                        obj[zzvu] = com_google_android_gms_internal_zzwq.readBytes();
                        com_google_android_gms_internal_zzwq.zzvu();
                        zzvu++;
                    }
                    obj[zzvu] = com_google_android_gms_internal_zzwq.readBytes();
                    this.zzaHn = obj;
                    continue;
                default:
                    if (!zza(com_google_android_gms_internal_zzwq, zzvu)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
