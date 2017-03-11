package com.samsung.android.spaytzsvc.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javolution.io.Struct.Unsigned32;

public class KeyValPairs extends TAStruct {
    private Unsigned32 count;
    private KeyValPair[] keyvalpairs;

    public KeyValPairs(int i, int i2, int i3) {
        this.count = new Unsigned32();
        this.keyvalpairs = new KeyValPair[i];
        for (int i4 = 0; i4 < i; i4++) {
            this.keyvalpairs[i4] = (KeyValPair) inner(new KeyValPair(i2, i3));
        }
    }

    public void setData(Map<byte[], byte[]> map) {
        if (map == null || map.size() == 0) {
            this.count.set(0);
            return;
        }
        this.count.set((long) map.size());
        int i = 0;
        for (Entry entry : map.entrySet()) {
            int i2 = i + 1;
            this.keyvalpairs[i].setData((byte[]) entry.getKey(), (byte[]) entry.getValue());
            i = i2;
        }
    }

    public Map<byte[], byte[]> getData() {
        int count = getCount();
        if (count == 0) {
            return null;
        }
        Map<byte[], byte[]> hashMap = new HashMap();
        for (int i = 0; i < count; i++) {
            hashMap.put(this.keyvalpairs[i].getKey(), this.keyvalpairs[i].getValue());
        }
        return hashMap;
    }

    public int getCount() {
        return (int) this.count.get();
    }
}
