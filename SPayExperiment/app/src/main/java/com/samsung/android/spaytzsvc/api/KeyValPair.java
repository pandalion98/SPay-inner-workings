/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAStruct;
import javolution.io.Struct;

public class KeyValPair
extends TAStruct {
    private static final String TAG = "KeyValPair";
    private Blob key;
    private Blob value;

    public KeyValPair(int n2, int n3) {
        this.key = this.inner(new Blob(n2));
        this.value = this.inner(new Blob(n3));
    }

    public byte[] getKey() {
        return this.key.getData();
    }

    public byte[] getValue() {
        return this.value.getData();
    }

    public void setData(byte[] arrby, byte[] arrby2) {
        if (arrby == null || arrby.length == 0 || arrby2 == null) {
            return;
        }
        this.key.setData(arrby);
        this.value.setData(arrby2);
    }
}

