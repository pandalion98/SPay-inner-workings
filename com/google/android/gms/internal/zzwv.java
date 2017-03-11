package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class zzwv implements Cloneable {
    private zzwt<?, ?> zzaHI;
    private Object zzaHJ;
    private List<zzxa> zzaHK;

    zzwv() {
        this.zzaHK = new ArrayList();
    }

    private byte[] toByteArray() {
        byte[] bArr = new byte[zzc()];
        zza(zzwr.zzu(bArr));
        return bArr;
    }

    public /* synthetic */ Object clone() {
        return zzvP();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzwv)) {
            return false;
        }
        zzwv com_google_android_gms_internal_zzwv = (zzwv) obj;
        if (this.zzaHJ != null && com_google_android_gms_internal_zzwv.zzaHJ != null) {
            return this.zzaHI == com_google_android_gms_internal_zzwv.zzaHI ? !this.zzaHI.zzaHC.isArray() ? this.zzaHJ.equals(com_google_android_gms_internal_zzwv.zzaHJ) : this.zzaHJ instanceof byte[] ? Arrays.equals((byte[]) this.zzaHJ, (byte[]) com_google_android_gms_internal_zzwv.zzaHJ) : this.zzaHJ instanceof int[] ? Arrays.equals((int[]) this.zzaHJ, (int[]) com_google_android_gms_internal_zzwv.zzaHJ) : this.zzaHJ instanceof long[] ? Arrays.equals((long[]) this.zzaHJ, (long[]) com_google_android_gms_internal_zzwv.zzaHJ) : this.zzaHJ instanceof float[] ? Arrays.equals((float[]) this.zzaHJ, (float[]) com_google_android_gms_internal_zzwv.zzaHJ) : this.zzaHJ instanceof double[] ? Arrays.equals((double[]) this.zzaHJ, (double[]) com_google_android_gms_internal_zzwv.zzaHJ) : this.zzaHJ instanceof boolean[] ? Arrays.equals((boolean[]) this.zzaHJ, (boolean[]) com_google_android_gms_internal_zzwv.zzaHJ) : Arrays.deepEquals((Object[]) this.zzaHJ, (Object[]) com_google_android_gms_internal_zzwv.zzaHJ) : false;
        } else {
            if (this.zzaHK != null && com_google_android_gms_internal_zzwv.zzaHK != null) {
                return this.zzaHK.equals(com_google_android_gms_internal_zzwv.zzaHK);
            }
            try {
                return Arrays.equals(toByteArray(), com_google_android_gms_internal_zzwv.toByteArray());
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    void zza(zzwr com_google_android_gms_internal_zzwr) {
        if (this.zzaHJ != null) {
            this.zzaHI.zza(this.zzaHJ, com_google_android_gms_internal_zzwr);
            return;
        }
        for (zzxa zza : this.zzaHK) {
            zza.zza(com_google_android_gms_internal_zzwr);
        }
    }

    void zza(zzxa com_google_android_gms_internal_zzxa) {
        this.zzaHK.add(com_google_android_gms_internal_zzxa);
    }

    <T> T zzb(zzwt<?, T> com_google_android_gms_internal_zzwt___T) {
        if (this.zzaHJ == null) {
            this.zzaHI = com_google_android_gms_internal_zzwt___T;
            this.zzaHJ = com_google_android_gms_internal_zzwt___T.zzx(this.zzaHK);
            this.zzaHK = null;
        } else if (this.zzaHI != com_google_android_gms_internal_zzwt___T) {
            throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
        }
        return this.zzaHJ;
    }

    int zzc() {
        if (this.zzaHJ != null) {
            return this.zzaHI.zzF(this.zzaHJ);
        }
        int i = 0;
        for (zzxa zzc : this.zzaHK) {
            i = zzc.zzc() + i;
        }
        return i;
    }

    public final zzwv zzvP() {
        int i = 0;
        zzwv com_google_android_gms_internal_zzwv = new zzwv();
        try {
            com_google_android_gms_internal_zzwv.zzaHI = this.zzaHI;
            if (this.zzaHK == null) {
                com_google_android_gms_internal_zzwv.zzaHK = null;
            } else {
                com_google_android_gms_internal_zzwv.zzaHK.addAll(this.zzaHK);
            }
            if (this.zzaHJ != null) {
                if (this.zzaHJ instanceof zzwy) {
                    com_google_android_gms_internal_zzwv.zzaHJ = ((zzwy) this.zzaHJ).zzvN();
                } else if (this.zzaHJ instanceof byte[]) {
                    com_google_android_gms_internal_zzwv.zzaHJ = ((byte[]) this.zzaHJ).clone();
                } else if (this.zzaHJ instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.zzaHJ;
                    Object obj = new byte[bArr.length][];
                    com_google_android_gms_internal_zzwv.zzaHJ = obj;
                    for (int i2 = 0; i2 < bArr.length; i2++) {
                        obj[i2] = (byte[]) bArr[i2].clone();
                    }
                } else if (this.zzaHJ instanceof boolean[]) {
                    com_google_android_gms_internal_zzwv.zzaHJ = ((boolean[]) this.zzaHJ).clone();
                } else if (this.zzaHJ instanceof int[]) {
                    com_google_android_gms_internal_zzwv.zzaHJ = ((int[]) this.zzaHJ).clone();
                } else if (this.zzaHJ instanceof long[]) {
                    com_google_android_gms_internal_zzwv.zzaHJ = ((long[]) this.zzaHJ).clone();
                } else if (this.zzaHJ instanceof float[]) {
                    com_google_android_gms_internal_zzwv.zzaHJ = ((float[]) this.zzaHJ).clone();
                } else if (this.zzaHJ instanceof double[]) {
                    com_google_android_gms_internal_zzwv.zzaHJ = ((double[]) this.zzaHJ).clone();
                } else if (this.zzaHJ instanceof zzwy[]) {
                    zzwy[] com_google_android_gms_internal_zzwyArr = (zzwy[]) this.zzaHJ;
                    Object obj2 = new zzwy[com_google_android_gms_internal_zzwyArr.length];
                    com_google_android_gms_internal_zzwv.zzaHJ = obj2;
                    while (i < com_google_android_gms_internal_zzwyArr.length) {
                        obj2[i] = com_google_android_gms_internal_zzwyArr[i].zzvN();
                        i++;
                    }
                }
            }
            return com_google_android_gms_internal_zzwv;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
