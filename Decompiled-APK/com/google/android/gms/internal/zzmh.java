package com.google.android.gms.internal;

import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzx;
import java.util.List;

public final class zzmh {
    public static Scope[] zzi(List<String> list) {
        zzx.zzb((Object) list, (Object) "scopeStrings can't be null.");
        Scope[] scopeArr = new Scope[list.size()];
        int size = list.size();
        for (int i = 0; i < size; i++) {
            scopeArr[i] = new Scope((String) list.get(i));
        }
        return scopeArr;
    }
}
