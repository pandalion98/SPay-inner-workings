package com.samsung.android.spayfw.payprovider.discover.payment.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.utils.b */
public class TLVData {
    private HashMap<Integer, List<ByteBuffer>> wb;

    public TLVData() {
        this.wb = new HashMap();
    }

    public List<ByteBuffer> m1006O(int i) {
        return (List) this.wb.get(Integer.valueOf(i));
    }

    public void m1007a(int i, ByteBuffer byteBuffer) {
        if (((List) this.wb.get(Integer.valueOf(i))) == null) {
            this.wb.put(Integer.valueOf(i), new ArrayList());
        }
        ((List) this.wb.get(Integer.valueOf(i))).add(byteBuffer);
    }

    public HashMap<Integer, List<ByteBuffer>> ef() {
        return this.wb;
    }
}
