/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment.utils;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class b {
    private HashMap<Integer, List<ByteBuffer>> wb = new HashMap();

    public List<ByteBuffer> O(int n2) {
        return (List)this.wb.get((Object)n2);
    }

    public void a(int n2, ByteBuffer byteBuffer) {
        if ((List)this.wb.get((Object)n2) == null) {
            this.wb.put((Object)n2, (Object)new ArrayList());
        }
        ((List)this.wb.get((Object)n2)).add((Object)byteBuffer);
    }

    public HashMap<Integer, List<ByteBuffer>> ef() {
        return this.wb;
    }
}

