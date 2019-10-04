/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 */
package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spaytzsvc.api.KeyValPair;
import com.samsung.android.spaytzsvc.api.TAStruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javolution.io.Struct;

public class KeyValPairs
extends TAStruct {
    private Struct.Unsigned32 count = new Struct.Unsigned32();
    private KeyValPair[] keyvalpairs;

    public KeyValPairs(int n2, int n3, int n4) {
        this.keyvalpairs = new KeyValPair[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.keyvalpairs[i2] = this.inner(new KeyValPair(n3, n4));
        }
    }

    public int getCount() {
        return (int)this.count.get();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Map<byte[], byte[]> getData() {
        int n2 = this.getCount();
        if (n2 == 0) {
            return null;
        }
        HashMap hashMap = new HashMap();
        int n3 = 0;
        while (n3 < n2) {
            hashMap.put((Object)this.keyvalpairs[n3].getKey(), (Object)this.keyvalpairs[n3].getValue());
            ++n3;
        }
        return hashMap;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setData(Map<byte[], byte[]> map) {
        if (map == null || map.size() == 0) {
            this.count.set(0L);
            return;
        } else {
            this.count.set(map.size());
            Iterator iterator = map.entrySet().iterator();
            int n2 = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                KeyValPair[] arrkeyValPair = this.keyvalpairs;
                int n3 = n2 + 1;
                arrkeyValPair[n2].setData((byte[])entry.getKey(), (byte[])entry.getValue());
                n2 = n3;
            }
        }
    }
}

