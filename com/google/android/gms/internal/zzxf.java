package com.google.android.gms.internal;

import android.support.v4.media.TransportMediator;
import com.google.android.gms.location.places.Place;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Arrays;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jcajce.spec.SkeinParameterSpec;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public interface zzxf {

    public static final class zza extends zzws<zza> {
        public String[] zzaIP;
        public String[] zzaIQ;
        public int[] zzaIR;

        public zza() {
            zzwo();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_internal_zzxf_zza = (zza) obj;
            return (zzww.equals(this.zzaIP, com_google_android_gms_internal_zzxf_zza.zzaIP) && zzww.equals(this.zzaIQ, com_google_android_gms_internal_zzxf_zza.zzaIQ) && zzww.equals(this.zzaIR, com_google_android_gms_internal_zzxf_zza.zzaIR)) ? zza((zzws) com_google_android_gms_internal_zzxf_zza) : false;
        }

        public int hashCode() {
            return ((((((zzww.hashCode(this.zzaIP) + 527) * 31) + zzww.hashCode(this.zzaIQ)) * 31) + zzww.hashCode(this.zzaIR)) * 31) + zzvL();
        }

        public zza zzK(zzwq com_google_android_gms_internal_zzwq) {
            while (true) {
                int zzvu = com_google_android_gms_internal_zzwq.zzvu();
                int zzc;
                Object obj;
                switch (zzvu) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        break;
                    case NamedCurve.sect283r1 /*10*/:
                        zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 10);
                        zzvu = this.zzaIP == null ? 0 : this.zzaIP.length;
                        obj = new String[(zzc + zzvu)];
                        if (zzvu != 0) {
                            System.arraycopy(this.zzaIP, 0, obj, 0, zzvu);
                        }
                        while (zzvu < obj.length - 1) {
                            obj[zzvu] = com_google_android_gms_internal_zzwq.readString();
                            com_google_android_gms_internal_zzwq.zzvu();
                            zzvu++;
                        }
                        obj[zzvu] = com_google_android_gms_internal_zzwq.readString();
                        this.zzaIP = obj;
                        continue;
                    case NamedCurve.secp192k1 /*18*/:
                        zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 18);
                        zzvu = this.zzaIQ == null ? 0 : this.zzaIQ.length;
                        obj = new String[(zzc + zzvu)];
                        if (zzvu != 0) {
                            System.arraycopy(this.zzaIQ, 0, obj, 0, zzvu);
                        }
                        while (zzvu < obj.length - 1) {
                            obj[zzvu] = com_google_android_gms_internal_zzwq.readString();
                            com_google_android_gms_internal_zzwq.zzvu();
                            zzvu++;
                        }
                        obj[zzvu] = com_google_android_gms_internal_zzwq.readString();
                        this.zzaIQ = obj;
                        continue;
                    case NamedCurve.secp384r1 /*24*/:
                        zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 24);
                        zzvu = this.zzaIR == null ? 0 : this.zzaIR.length;
                        obj = new int[(zzc + zzvu)];
                        if (zzvu != 0) {
                            System.arraycopy(this.zzaIR, 0, obj, 0, zzvu);
                        }
                        while (zzvu < obj.length - 1) {
                            obj[zzvu] = com_google_android_gms_internal_zzwq.zzvx();
                            com_google_android_gms_internal_zzwq.zzvu();
                            zzvu++;
                        }
                        obj[zzvu] = com_google_android_gms_internal_zzwq.zzvx();
                        this.zzaIR = obj;
                        continue;
                    case NamedCurve.brainpoolP256r1 /*26*/:
                        int zzip = com_google_android_gms_internal_zzwq.zzip(com_google_android_gms_internal_zzwq.zzvB());
                        zzc = com_google_android_gms_internal_zzwq.getPosition();
                        zzvu = 0;
                        while (com_google_android_gms_internal_zzwq.zzvG() > 0) {
                            com_google_android_gms_internal_zzwq.zzvx();
                            zzvu++;
                        }
                        com_google_android_gms_internal_zzwq.zzir(zzc);
                        zzc = this.zzaIR == null ? 0 : this.zzaIR.length;
                        Object obj2 = new int[(zzvu + zzc)];
                        if (zzc != 0) {
                            System.arraycopy(this.zzaIR, 0, obj2, 0, zzc);
                        }
                        while (zzc < obj2.length) {
                            obj2[zzc] = com_google_android_gms_internal_zzwq.zzvx();
                            zzc++;
                        }
                        this.zzaIR = obj2;
                        com_google_android_gms_internal_zzwq.zziq(zzip);
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

        public void zza(zzwr com_google_android_gms_internal_zzwr) {
            int i = 0;
            if (this.zzaIP != null && this.zzaIP.length > 0) {
                for (String str : this.zzaIP) {
                    if (str != null) {
                        com_google_android_gms_internal_zzwr.zzb(1, str);
                    }
                }
            }
            if (this.zzaIQ != null && this.zzaIQ.length > 0) {
                for (String str2 : this.zzaIQ) {
                    if (str2 != null) {
                        com_google_android_gms_internal_zzwr.zzb(2, str2);
                    }
                }
            }
            if (this.zzaIR != null && this.zzaIR.length > 0) {
                while (i < this.zzaIR.length) {
                    com_google_android_gms_internal_zzwr.zzy(3, this.zzaIR[i]);
                    i++;
                }
            }
            super.zza(com_google_android_gms_internal_zzwr);
        }

        public /* synthetic */ zzwy zzb(zzwq com_google_android_gms_internal_zzwq) {
            return zzK(com_google_android_gms_internal_zzwq);
        }

        protected int zzc() {
            int i;
            int i2;
            int i3;
            int i4 = 0;
            int zzc = super.zzc();
            if (this.zzaIP == null || this.zzaIP.length <= 0) {
                i = zzc;
            } else {
                i2 = 0;
                i3 = 0;
                for (String str : this.zzaIP) {
                    if (str != null) {
                        i3++;
                        i2 += zzwr.zzdM(str);
                    }
                }
                i = (zzc + i2) + (i3 * 1);
            }
            if (this.zzaIQ != null && this.zzaIQ.length > 0) {
                i3 = 0;
                zzc = 0;
                for (String str2 : this.zzaIQ) {
                    if (str2 != null) {
                        zzc++;
                        i3 += zzwr.zzdM(str2);
                    }
                }
                i = (i + i3) + (zzc * 1);
            }
            if (this.zzaIR == null || this.zzaIR.length <= 0) {
                return i;
            }
            i2 = 0;
            while (i4 < this.zzaIR.length) {
                i2 += zzwr.zziw(this.zzaIR[i4]);
                i4++;
            }
            return (i + i2) + (this.zzaIR.length * 1);
        }

        public zza zzwo() {
            this.zzaIP = zzxb.zzaHT;
            this.zzaIQ = zzxb.zzaHT;
            this.zzaIR = zzxb.zzaHO;
            this.zzaHB = null;
            this.zzaHM = -1;
            return this;
        }
    }

    public static final class zzb extends zzws<zzb> {
        public String version;
        public int zzaIS;
        public String zzaIT;

        public zzb() {
            zzwp();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzb)) {
                return false;
            }
            zzb com_google_android_gms_internal_zzxf_zzb = (zzb) obj;
            if (this.zzaIS != com_google_android_gms_internal_zzxf_zzb.zzaIS) {
                return false;
            }
            if (this.zzaIT == null) {
                if (com_google_android_gms_internal_zzxf_zzb.zzaIT != null) {
                    return false;
                }
            } else if (!this.zzaIT.equals(com_google_android_gms_internal_zzxf_zzb.zzaIT)) {
                return false;
            }
            if (this.version == null) {
                if (com_google_android_gms_internal_zzxf_zzb.version != null) {
                    return false;
                }
            } else if (!this.version.equals(com_google_android_gms_internal_zzxf_zzb.version)) {
                return false;
            }
            return zza((zzws) com_google_android_gms_internal_zzxf_zzb);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.zzaIT == null ? 0 : this.zzaIT.hashCode()) + ((this.zzaIS + 527) * 31)) * 31;
            if (this.version != null) {
                i = this.version.hashCode();
            }
            return ((hashCode + i) * 31) + zzvL();
        }

        public zzb zzL(zzwq com_google_android_gms_internal_zzwq) {
            while (true) {
                int zzvu = com_google_android_gms_internal_zzwq.zzvu();
                switch (zzvu) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        break;
                    case X509KeyUsage.keyAgreement /*8*/:
                        zzvu = com_google_android_gms_internal_zzwq.zzvx();
                        switch (zzvu) {
                            case ECCurve.COORD_AFFINE /*0*/:
                            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                            case CipherSpiExt.DECRYPT_MODE /*2*/:
                            case F2m.PPB /*3*/:
                            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                            case ECCurve.COORD_SKEWED /*7*/:
                            case X509KeyUsage.keyAgreement /*8*/:
                            case NamedCurve.sect283k1 /*9*/:
                            case NamedCurve.sect283r1 /*10*/:
                            case CertStatus.UNREVOKED /*11*/:
                            case CertStatus.UNDETERMINED /*12*/:
                            case NamedCurve.sect571k1 /*13*/:
                            case NamedCurve.sect571r1 /*14*/:
                            case NamedCurve.secp160k1 /*15*/:
                            case X509KeyUsage.dataEncipherment /*16*/:
                            case NamedCurve.secp160r2 /*17*/:
                            case NamedCurve.secp192k1 /*18*/:
                            case NamedCurve.secp192r1 /*19*/:
                            case SkeinParameterSpec.PARAM_TYPE_NONCE /*20*/:
                            case NamedCurve.secp224r1 /*21*/:
                            case NamedCurve.secp256k1 /*22*/:
                            case NamedCurve.secp256r1 /*23*/:
                            case NamedCurve.secp384r1 /*24*/:
                            case NamedCurve.secp521r1 /*25*/:
                                this.zzaIS = zzvu;
                                break;
                            default:
                                continue;
                        }
                    case NamedCurve.secp192k1 /*18*/:
                        this.zzaIT = com_google_android_gms_internal_zzwq.readString();
                        continue;
                    case NamedCurve.brainpoolP256r1 /*26*/:
                        this.version = com_google_android_gms_internal_zzwq.readString();
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

        public void zza(zzwr com_google_android_gms_internal_zzwr) {
            if (this.zzaIS != 0) {
                com_google_android_gms_internal_zzwr.zzy(1, this.zzaIS);
            }
            if (!this.zzaIT.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzwr.zzb(2, this.zzaIT);
            }
            if (!this.version.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzwr.zzb(3, this.version);
            }
            super.zza(com_google_android_gms_internal_zzwr);
        }

        public /* synthetic */ zzwy zzb(zzwq com_google_android_gms_internal_zzwq) {
            return zzL(com_google_android_gms_internal_zzwq);
        }

        protected int zzc() {
            int zzc = super.zzc();
            if (this.zzaIS != 0) {
                zzc += zzwr.zzA(1, this.zzaIS);
            }
            if (!this.zzaIT.equals(BuildConfig.FLAVOR)) {
                zzc += zzwr.zzj(2, this.zzaIT);
            }
            return !this.version.equals(BuildConfig.FLAVOR) ? zzc + zzwr.zzj(3, this.version) : zzc;
        }

        public zzb zzwp() {
            this.zzaIS = 0;
            this.zzaIT = BuildConfig.FLAVOR;
            this.version = BuildConfig.FLAVOR;
            this.zzaHB = null;
            this.zzaHM = -1;
            return this;
        }
    }

    public static final class zzc extends zzws<zzc> {
        public byte[] zzaIU;
        public byte[][] zzaIV;
        public boolean zzaIW;

        public zzc() {
            zzwq();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzc)) {
                return false;
            }
            zzc com_google_android_gms_internal_zzxf_zzc = (zzc) obj;
            return (Arrays.equals(this.zzaIU, com_google_android_gms_internal_zzxf_zzc.zzaIU) && zzww.zza(this.zzaIV, com_google_android_gms_internal_zzxf_zzc.zzaIV) && this.zzaIW == com_google_android_gms_internal_zzxf_zzc.zzaIW) ? zza((zzws) com_google_android_gms_internal_zzxf_zzc) : false;
        }

        public int hashCode() {
            return (((this.zzaIW ? 1231 : 1237) + ((((Arrays.hashCode(this.zzaIU) + 527) * 31) + zzww.zza(this.zzaIV)) * 31)) * 31) + zzvL();
        }

        public zzc zzM(zzwq com_google_android_gms_internal_zzwq) {
            while (true) {
                int zzvu = com_google_android_gms_internal_zzwq.zzvu();
                switch (zzvu) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        break;
                    case NamedCurve.sect283r1 /*10*/:
                        this.zzaIU = com_google_android_gms_internal_zzwq.readBytes();
                        continue;
                    case NamedCurve.secp192k1 /*18*/:
                        int zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 18);
                        zzvu = this.zzaIV == null ? 0 : this.zzaIV.length;
                        Object obj = new byte[(zzc + zzvu)][];
                        if (zzvu != 0) {
                            System.arraycopy(this.zzaIV, 0, obj, 0, zzvu);
                        }
                        while (zzvu < obj.length - 1) {
                            obj[zzvu] = com_google_android_gms_internal_zzwq.readBytes();
                            com_google_android_gms_internal_zzwq.zzvu();
                            zzvu++;
                        }
                        obj[zzvu] = com_google_android_gms_internal_zzwq.readBytes();
                        this.zzaIV = obj;
                        continue;
                    case NamedCurve.secp384r1 /*24*/:
                        this.zzaIW = com_google_android_gms_internal_zzwq.zzvy();
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

        public void zza(zzwr com_google_android_gms_internal_zzwr) {
            if (!Arrays.equals(this.zzaIU, zzxb.zzaHV)) {
                com_google_android_gms_internal_zzwr.zza(1, this.zzaIU);
            }
            if (this.zzaIV != null && this.zzaIV.length > 0) {
                for (byte[] bArr : this.zzaIV) {
                    if (bArr != null) {
                        com_google_android_gms_internal_zzwr.zza(2, bArr);
                    }
                }
            }
            if (this.zzaIW) {
                com_google_android_gms_internal_zzwr.zzb(3, this.zzaIW);
            }
            super.zza(com_google_android_gms_internal_zzwr);
        }

        public /* synthetic */ zzwy zzb(zzwq com_google_android_gms_internal_zzwq) {
            return zzM(com_google_android_gms_internal_zzwq);
        }

        protected int zzc() {
            int i = 0;
            int zzc = super.zzc();
            if (!Arrays.equals(this.zzaIU, zzxb.zzaHV)) {
                zzc += zzwr.zzb(1, this.zzaIU);
            }
            if (this.zzaIV != null && this.zzaIV.length > 0) {
                int i2 = 0;
                int i3 = 0;
                while (i < this.zzaIV.length) {
                    byte[] bArr = this.zzaIV[i];
                    if (bArr != null) {
                        i3++;
                        i2 += zzwr.zzw(bArr);
                    }
                    i++;
                }
                zzc = (zzc + i2) + (i3 * 1);
            }
            return this.zzaIW ? zzc + zzwr.zzc(3, this.zzaIW) : zzc;
        }

        public zzc zzwq() {
            this.zzaIU = zzxb.zzaHV;
            this.zzaIV = zzxb.zzaHU;
            this.zzaIW = false;
            this.zzaHB = null;
            this.zzaHM = -1;
            return this;
        }
    }

    public static final class zzd extends zzws<zzd> {
        public String tag;
        public long zzaIX;
        public int zzaIY;
        public int zzaIZ;
        public boolean zzaJa;
        public zze[] zzaJb;
        public zzb zzaJc;
        public byte[] zzaJd;
        public byte[] zzaJe;
        public byte[] zzaJf;
        public zza zzaJg;
        public String zzaJh;
        public long zzaJi;
        public zzc zzaJj;

        public zzd() {
            zzwr();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zzd)) {
                return false;
            }
            zzd com_google_android_gms_internal_zzxf_zzd = (zzd) obj;
            if (this.zzaIX != com_google_android_gms_internal_zzxf_zzd.zzaIX) {
                return false;
            }
            if (this.tag == null) {
                if (com_google_android_gms_internal_zzxf_zzd.tag != null) {
                    return false;
                }
            } else if (!this.tag.equals(com_google_android_gms_internal_zzxf_zzd.tag)) {
                return false;
            }
            if (this.zzaIY != com_google_android_gms_internal_zzxf_zzd.zzaIY || this.zzaIZ != com_google_android_gms_internal_zzxf_zzd.zzaIZ || this.zzaJa != com_google_android_gms_internal_zzxf_zzd.zzaJa || !zzww.equals(this.zzaJb, com_google_android_gms_internal_zzxf_zzd.zzaJb)) {
                return false;
            }
            if (this.zzaJc == null) {
                if (com_google_android_gms_internal_zzxf_zzd.zzaJc != null) {
                    return false;
                }
            } else if (!this.zzaJc.equals(com_google_android_gms_internal_zzxf_zzd.zzaJc)) {
                return false;
            }
            if (!Arrays.equals(this.zzaJd, com_google_android_gms_internal_zzxf_zzd.zzaJd) || !Arrays.equals(this.zzaJe, com_google_android_gms_internal_zzxf_zzd.zzaJe) || !Arrays.equals(this.zzaJf, com_google_android_gms_internal_zzxf_zzd.zzaJf)) {
                return false;
            }
            if (this.zzaJg == null) {
                if (com_google_android_gms_internal_zzxf_zzd.zzaJg != null) {
                    return false;
                }
            } else if (!this.zzaJg.equals(com_google_android_gms_internal_zzxf_zzd.zzaJg)) {
                return false;
            }
            if (this.zzaJh == null) {
                if (com_google_android_gms_internal_zzxf_zzd.zzaJh != null) {
                    return false;
                }
            } else if (!this.zzaJh.equals(com_google_android_gms_internal_zzxf_zzd.zzaJh)) {
                return false;
            }
            if (this.zzaJi != com_google_android_gms_internal_zzxf_zzd.zzaJi) {
                return false;
            }
            if (this.zzaJj == null) {
                if (com_google_android_gms_internal_zzxf_zzd.zzaJj != null) {
                    return false;
                }
            } else if (!this.zzaJj.equals(com_google_android_gms_internal_zzxf_zzd.zzaJj)) {
                return false;
            }
            return zza((zzws) com_google_android_gms_internal_zzxf_zzd);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((((this.zzaJh == null ? 0 : this.zzaJh.hashCode()) + (((this.zzaJg == null ? 0 : this.zzaJg.hashCode()) + (((((((((this.zzaJc == null ? 0 : this.zzaJc.hashCode()) + (((((this.zzaJa ? 1231 : 1237) + (((((((this.tag == null ? 0 : this.tag.hashCode()) + ((((int) (this.zzaIX ^ (this.zzaIX >>> 32))) + 527) * 31)) * 31) + this.zzaIY) * 31) + this.zzaIZ) * 31)) * 31) + zzww.hashCode(this.zzaJb)) * 31)) * 31) + Arrays.hashCode(this.zzaJd)) * 31) + Arrays.hashCode(this.zzaJe)) * 31) + Arrays.hashCode(this.zzaJf)) * 31)) * 31)) * 31) + ((int) (this.zzaJi ^ (this.zzaJi >>> 32)))) * 31;
            if (this.zzaJj != null) {
                i = this.zzaJj.hashCode();
            }
            return ((hashCode + i) * 31) + zzvL();
        }

        public zzd zzN(zzwq com_google_android_gms_internal_zzwq) {
            while (true) {
                int zzvu = com_google_android_gms_internal_zzwq.zzvu();
                switch (zzvu) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        break;
                    case X509KeyUsage.keyAgreement /*8*/:
                        this.zzaIX = com_google_android_gms_internal_zzwq.zzvw();
                        continue;
                    case NamedCurve.secp192k1 /*18*/:
                        this.tag = com_google_android_gms_internal_zzwq.readString();
                        continue;
                    case NamedCurve.brainpoolP256r1 /*26*/:
                        int zzc = zzxb.zzc(com_google_android_gms_internal_zzwq, 26);
                        zzvu = this.zzaJb == null ? 0 : this.zzaJb.length;
                        Object obj = new zze[(zzc + zzvu)];
                        if (zzvu != 0) {
                            System.arraycopy(this.zzaJb, 0, obj, 0, zzvu);
                        }
                        while (zzvu < obj.length - 1) {
                            obj[zzvu] = new zze();
                            com_google_android_gms_internal_zzwq.zza(obj[zzvu]);
                            com_google_android_gms_internal_zzwq.zzvu();
                            zzvu++;
                        }
                        obj[zzvu] = new zze();
                        com_google_android_gms_internal_zzwq.zza(obj[zzvu]);
                        this.zzaJb = obj;
                        continue;
                    case ECCKeyGenParameterSpec.DEFAULT_T /*50*/:
                        this.zzaJd = com_google_android_gms_internal_zzwq.readBytes();
                        continue;
                    case CipherSuite.TLS_DH_anon_WITH_AES_256_CBC_SHA /*58*/:
                        if (this.zzaJg == null) {
                            this.zzaJg = new zza();
                        }
                        com_google_android_gms_internal_zzwq.zza(this.zzaJg);
                        continue;
                    case CipherSuite.TLS_DH_DSS_WITH_CAMELLIA_128_CBC_SHA /*66*/:
                        this.zzaJe = com_google_android_gms_internal_zzwq.readBytes();
                        continue;
                    case Place.TYPE_PLACE_OF_WORSHIP /*74*/:
                        if (this.zzaJc == null) {
                            this.zzaJc = new zzb();
                        }
                        com_google_android_gms_internal_zzwq.zza(this.zzaJc);
                        continue;
                    case EACTags.APPLICATION_LABEL /*80*/:
                        this.zzaJa = com_google_android_gms_internal_zzwq.zzvy();
                        continue;
                    case EACTags.TRACK3_APPLICATION /*88*/:
                        this.zzaIY = com_google_android_gms_internal_zzwq.zzvx();
                        continue;
                    case Place.TYPE_ZOO /*96*/:
                        this.zzaIZ = com_google_android_gms_internal_zzwq.zzvx();
                        continue;
                    case CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256 /*106*/:
                        this.zzaJf = com_google_android_gms_internal_zzwq.readBytes();
                        continue;
                    case 114:
                        this.zzaJh = com_google_android_gms_internal_zzwq.readString();
                        continue;
                    case EACTags.COMPATIBLE_TAG_ALLOCATION_AUTHORITY /*120*/:
                        this.zzaJi = com_google_android_gms_internal_zzwq.zzvA();
                        continue;
                    case TransportMediator.KEYCODE_MEDIA_RECORD /*130*/:
                        if (this.zzaJj == null) {
                            this.zzaJj = new zzc();
                        }
                        com_google_android_gms_internal_zzwq.zza(this.zzaJj);
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

        public void zza(zzwr com_google_android_gms_internal_zzwr) {
            if (this.zzaIX != 0) {
                com_google_android_gms_internal_zzwr.zzb(1, this.zzaIX);
            }
            if (!this.tag.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzwr.zzb(2, this.tag);
            }
            if (this.zzaJb != null && this.zzaJb.length > 0) {
                for (zzwy com_google_android_gms_internal_zzwy : this.zzaJb) {
                    if (com_google_android_gms_internal_zzwy != null) {
                        com_google_android_gms_internal_zzwr.zza(3, com_google_android_gms_internal_zzwy);
                    }
                }
            }
            if (!Arrays.equals(this.zzaJd, zzxb.zzaHV)) {
                com_google_android_gms_internal_zzwr.zza(6, this.zzaJd);
            }
            if (this.zzaJg != null) {
                com_google_android_gms_internal_zzwr.zza(7, this.zzaJg);
            }
            if (!Arrays.equals(this.zzaJe, zzxb.zzaHV)) {
                com_google_android_gms_internal_zzwr.zza(8, this.zzaJe);
            }
            if (this.zzaJc != null) {
                com_google_android_gms_internal_zzwr.zza(9, this.zzaJc);
            }
            if (this.zzaJa) {
                com_google_android_gms_internal_zzwr.zzb(10, this.zzaJa);
            }
            if (this.zzaIY != 0) {
                com_google_android_gms_internal_zzwr.zzy(11, this.zzaIY);
            }
            if (this.zzaIZ != 0) {
                com_google_android_gms_internal_zzwr.zzy(12, this.zzaIZ);
            }
            if (!Arrays.equals(this.zzaJf, zzxb.zzaHV)) {
                com_google_android_gms_internal_zzwr.zza(13, this.zzaJf);
            }
            if (!this.zzaJh.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzwr.zzb(14, this.zzaJh);
            }
            if (this.zzaJi != 180000) {
                com_google_android_gms_internal_zzwr.zzc(15, this.zzaJi);
            }
            if (this.zzaJj != null) {
                com_google_android_gms_internal_zzwr.zza(16, this.zzaJj);
            }
            super.zza(com_google_android_gms_internal_zzwr);
        }

        public /* synthetic */ zzwy zzb(zzwq com_google_android_gms_internal_zzwq) {
            return zzN(com_google_android_gms_internal_zzwq);
        }

        protected int zzc() {
            int zzc = super.zzc();
            if (this.zzaIX != 0) {
                zzc += zzwr.zzd(1, this.zzaIX);
            }
            if (!this.tag.equals(BuildConfig.FLAVOR)) {
                zzc += zzwr.zzj(2, this.tag);
            }
            if (this.zzaJb != null && this.zzaJb.length > 0) {
                int i = zzc;
                for (zzwy com_google_android_gms_internal_zzwy : this.zzaJb) {
                    if (com_google_android_gms_internal_zzwy != null) {
                        i += zzwr.zzc(3, com_google_android_gms_internal_zzwy);
                    }
                }
                zzc = i;
            }
            if (!Arrays.equals(this.zzaJd, zzxb.zzaHV)) {
                zzc += zzwr.zzb(6, this.zzaJd);
            }
            if (this.zzaJg != null) {
                zzc += zzwr.zzc(7, this.zzaJg);
            }
            if (!Arrays.equals(this.zzaJe, zzxb.zzaHV)) {
                zzc += zzwr.zzb(8, this.zzaJe);
            }
            if (this.zzaJc != null) {
                zzc += zzwr.zzc(9, this.zzaJc);
            }
            if (this.zzaJa) {
                zzc += zzwr.zzc(10, this.zzaJa);
            }
            if (this.zzaIY != 0) {
                zzc += zzwr.zzA(11, this.zzaIY);
            }
            if (this.zzaIZ != 0) {
                zzc += zzwr.zzA(12, this.zzaIZ);
            }
            if (!Arrays.equals(this.zzaJf, zzxb.zzaHV)) {
                zzc += zzwr.zzb(13, this.zzaJf);
            }
            if (!this.zzaJh.equals(BuildConfig.FLAVOR)) {
                zzc += zzwr.zzj(14, this.zzaJh);
            }
            if (this.zzaJi != 180000) {
                zzc += zzwr.zze(15, this.zzaJi);
            }
            return this.zzaJj != null ? zzc + zzwr.zzc(16, this.zzaJj) : zzc;
        }

        public zzd zzwr() {
            this.zzaIX = 0;
            this.tag = BuildConfig.FLAVOR;
            this.zzaIY = 0;
            this.zzaIZ = 0;
            this.zzaJa = false;
            this.zzaJb = zze.zzws();
            this.zzaJc = null;
            this.zzaJd = zzxb.zzaHV;
            this.zzaJe = zzxb.zzaHV;
            this.zzaJf = zzxb.zzaHV;
            this.zzaJg = null;
            this.zzaJh = BuildConfig.FLAVOR;
            this.zzaJi = 180000;
            this.zzaJj = null;
            this.zzaHB = null;
            this.zzaHM = -1;
            return this;
        }
    }

    public static final class zze extends zzws<zze> {
        private static volatile zze[] zzaJk;
        public String value;
        public String zzfv;

        public zze() {
            zzwt();
        }

        public static zze[] zzws() {
            if (zzaJk == null) {
                synchronized (zzww.zzaHL) {
                    if (zzaJk == null) {
                        zzaJk = new zze[0];
                    }
                }
            }
            return zzaJk;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof zze)) {
                return false;
            }
            zze com_google_android_gms_internal_zzxf_zze = (zze) obj;
            if (this.zzfv == null) {
                if (com_google_android_gms_internal_zzxf_zze.zzfv != null) {
                    return false;
                }
            } else if (!this.zzfv.equals(com_google_android_gms_internal_zzxf_zze.zzfv)) {
                return false;
            }
            if (this.value == null) {
                if (com_google_android_gms_internal_zzxf_zze.value != null) {
                    return false;
                }
            } else if (!this.value.equals(com_google_android_gms_internal_zzxf_zze.value)) {
                return false;
            }
            return zza((zzws) com_google_android_gms_internal_zzxf_zze);
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.zzfv == null ? 0 : this.zzfv.hashCode()) + 527) * 31;
            if (this.value != null) {
                i = this.value.hashCode();
            }
            return ((hashCode + i) * 31) + zzvL();
        }

        public zze zzO(zzwq com_google_android_gms_internal_zzwq) {
            while (true) {
                int zzvu = com_google_android_gms_internal_zzwq.zzvu();
                switch (zzvu) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        break;
                    case NamedCurve.sect283r1 /*10*/:
                        this.zzfv = com_google_android_gms_internal_zzwq.readString();
                        continue;
                    case NamedCurve.secp192k1 /*18*/:
                        this.value = com_google_android_gms_internal_zzwq.readString();
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

        public void zza(zzwr com_google_android_gms_internal_zzwr) {
            if (!this.zzfv.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzwr.zzb(1, this.zzfv);
            }
            if (!this.value.equals(BuildConfig.FLAVOR)) {
                com_google_android_gms_internal_zzwr.zzb(2, this.value);
            }
            super.zza(com_google_android_gms_internal_zzwr);
        }

        public /* synthetic */ zzwy zzb(zzwq com_google_android_gms_internal_zzwq) {
            return zzO(com_google_android_gms_internal_zzwq);
        }

        protected int zzc() {
            int zzc = super.zzc();
            if (!this.zzfv.equals(BuildConfig.FLAVOR)) {
                zzc += zzwr.zzj(1, this.zzfv);
            }
            return !this.value.equals(BuildConfig.FLAVOR) ? zzc + zzwr.zzj(2, this.value) : zzc;
        }

        public zze zzwt() {
            this.zzfv = BuildConfig.FLAVOR;
            this.value = BuildConfig.FLAVOR;
            this.zzaHB = null;
            this.zzaHM = -1;
            return this;
        }
    }
}
