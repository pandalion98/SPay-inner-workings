package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class zzw {

    public static final class zza {
        private final List<String> zzQr;
        private final Object zzzF;

        private zza(Object obj) {
            this.zzzF = zzx.zzl(obj);
            this.zzQr = new ArrayList();
        }

        public String toString() {
            StringBuilder append = new StringBuilder(100).append(this.zzzF.getClass().getSimpleName()).append('{');
            int size = this.zzQr.size();
            for (int i = 0; i < size; i++) {
                append.append((String) this.zzQr.get(i));
                if (i < size - 1) {
                    append.append(", ");
                }
            }
            return append.append('}').toString();
        }

        public zza zza(String str, Object obj) {
            this.zzQr.add(((String) zzx.zzl(str)) + "=" + String.valueOf(obj));
            return this;
        }
    }

    public static boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static zza zzk(Object obj) {
        return new zza(null);
    }
}
