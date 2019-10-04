/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.util.Arrays
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.Util;
import java.util.Arrays;

public final class ByteArrayWrapper {
    private final byte[] data;
    private final int hashcode;

    private ByteArrayWrapper(byte[] arrby) {
        this.data = arrby;
        this.hashcode = Arrays.hashCode((byte[])arrby);
    }

    public static ByteArrayWrapper copyOf(byte[] arrby) {
        if (arrby == null) {
            throw new NullPointerException();
        }
        return new ByteArrayWrapper(Util.copyByteArray(arrby));
    }

    public static ByteArrayWrapper wrapperAround(byte[] arrby) {
        if (arrby == null) {
            throw new NullPointerException();
        }
        return new ByteArrayWrapper(arrby);
    }

    public boolean equals(Object object) {
        if (!(object instanceof ByteArrayWrapper)) {
            return false;
        }
        return Arrays.equals((byte[])this.data, (byte[])((ByteArrayWrapper)object).data);
    }

    public int hashCode() {
        return this.hashcode;
    }
}

