package com.google.android.gms.internal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.crypto.tls.NamedCurve;

public class zzwt<M extends zzws<M>, T> {
    public final int tag;
    protected final int type;
    protected final Class<T> zzaHC;
    protected final boolean zzaHD;

    private zzwt(int i, Class<T> cls, int i2, boolean z) {
        this.type = i;
        this.zzaHC = cls;
        this.tag = i2;
        this.zzaHD = z;
    }

    @Deprecated
    public static <M extends zzws<M>, T extends zzwy> zzwt<M, T> zza(int i, Class<T> cls, int i2) {
        return new zzwt(i, cls, i2, false);
    }

    private T zzy(List<zzxa> list) {
        int i;
        int i2 = 0;
        List arrayList = new ArrayList();
        for (i = 0; i < list.size(); i++) {
            zzxa com_google_android_gms_internal_zzxa = (zzxa) list.get(i);
            if (com_google_android_gms_internal_zzxa.zzaHN.length != 0) {
                zza(com_google_android_gms_internal_zzxa, arrayList);
            }
        }
        i = arrayList.size();
        if (i == 0) {
            return null;
        }
        T cast = this.zzaHC.cast(Array.newInstance(this.zzaHC.getComponentType(), i));
        while (i2 < i) {
            Array.set(cast, i2, arrayList.get(i2));
            i2++;
        }
        return cast;
    }

    private T zzz(List<zzxa> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.zzaHC.cast(zzz(zzwq.zzt(((zzxa) list.get(list.size() - 1)).zzaHN)));
    }

    int zzF(Object obj) {
        return this.zzaHD ? zzG(obj) : zzH(obj);
    }

    protected int zzG(Object obj) {
        int i = 0;
        int length = Array.getLength(obj);
        for (int i2 = 0; i2 < length; i2++) {
            if (Array.get(obj, i2) != null) {
                i += zzH(Array.get(obj, i2));
            }
        }
        return i;
    }

    protected int zzH(Object obj) {
        int zziI = zzxb.zziI(this.tag);
        switch (this.type) {
            case NamedCurve.sect283r1 /*10*/:
                return zzwr.zzb(zziI, (zzwy) obj);
            case CertStatus.UNREVOKED /*11*/:
                return zzwr.zzc(zziI, (zzwy) obj);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }

    protected void zza(zzxa com_google_android_gms_internal_zzxa, List<Object> list) {
        list.add(zzz(zzwq.zzt(com_google_android_gms_internal_zzxa.zzaHN)));
    }

    void zza(Object obj, zzwr com_google_android_gms_internal_zzwr) {
        if (this.zzaHD) {
            zzc(obj, com_google_android_gms_internal_zzwr);
        } else {
            zzb(obj, com_google_android_gms_internal_zzwr);
        }
    }

    protected void zzb(Object obj, zzwr com_google_android_gms_internal_zzwr) {
        try {
            com_google_android_gms_internal_zzwr.zziA(this.tag);
            switch (this.type) {
                case NamedCurve.sect283r1 /*10*/:
                    zzwy com_google_android_gms_internal_zzwy = (zzwy) obj;
                    int zziI = zzxb.zziI(this.tag);
                    com_google_android_gms_internal_zzwr.zzb(com_google_android_gms_internal_zzwy);
                    com_google_android_gms_internal_zzwr.zzC(zziI, 4);
                    return;
                case CertStatus.UNREVOKED /*11*/:
                    com_google_android_gms_internal_zzwr.zzc((zzwy) obj);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException(e);
    }

    protected void zzc(Object obj, zzwr com_google_android_gms_internal_zzwr) {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                zzb(obj2, com_google_android_gms_internal_zzwr);
            }
        }
    }

    final T zzx(List<zzxa> list) {
        return list == null ? null : this.zzaHD ? zzy(list) : zzz((List) list);
    }

    protected Object zzz(zzwq com_google_android_gms_internal_zzwq) {
        Class componentType = this.zzaHD ? this.zzaHC.getComponentType() : this.zzaHC;
        try {
            zzwy com_google_android_gms_internal_zzwy;
            switch (this.type) {
                case NamedCurve.sect283r1 /*10*/:
                    com_google_android_gms_internal_zzwy = (zzwy) componentType.newInstance();
                    com_google_android_gms_internal_zzwq.zza(com_google_android_gms_internal_zzwy, zzxb.zziI(this.tag));
                    return com_google_android_gms_internal_zzwy;
                case CertStatus.UNREVOKED /*11*/:
                    com_google_android_gms_internal_zzwy = (zzwy) componentType.newInstance();
                    com_google_android_gms_internal_zzwq.zza(com_google_android_gms_internal_zzwy);
                    return com_google_android_gms_internal_zzwy;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e);
        } catch (Throwable e2) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e2);
        } catch (Throwable e22) {
            throw new IllegalArgumentException("Error reading extension field", e22);
        }
    }
}
