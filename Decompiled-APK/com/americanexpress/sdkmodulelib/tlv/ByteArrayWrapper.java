package com.americanexpress.sdkmodulelib.tlv;

import java.util.Arrays;

public final class ByteArrayWrapper {
    private final byte[] data;
    private final int hashcode;

    private ByteArrayWrapper(byte[] bArr) {
        this.data = bArr;
        this.hashcode = Arrays.hashCode(bArr);
    }

    public static ByteArrayWrapper wrapperAround(byte[] bArr) {
        if (bArr != null) {
            return new ByteArrayWrapper(bArr);
        }
        throw new NullPointerException();
    }

    public static ByteArrayWrapper copyOf(byte[] bArr) {
        if (bArr != null) {
            return new ByteArrayWrapper(Util.copyByteArray(bArr));
        }
        throw new NullPointerException();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ByteArrayWrapper) {
            return Arrays.equals(this.data, ((ByteArrayWrapper) obj).data);
        }
        return false;
    }

    public int hashCode() {
        return this.hashcode;
    }
}
