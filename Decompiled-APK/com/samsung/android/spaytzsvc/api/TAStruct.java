package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spayfw.p002b.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javolution.io.Struct;

public class TAStruct extends Struct {
    private static final String TAG = "TAStruct";

    public boolean isPacked() {
        return true;
    }

    public ByteOrder byteOrder() {
        return ByteOrder.nativeOrder();
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = getByteBuffer();
        byte[] bArr = new byte[byteBuffer.limit()];
        byteBuffer.position(0);
        byteBuffer.get(bArr);
        return bArr;
    }

    public TAStruct deserialize(byte[] bArr) {
        setByteBuffer(ByteBuffer.wrap(bArr).order(byteOrder()), 0);
        return this;
    }

    public TAStruct setData() {
        return this;
    }

    public void dumpBuffer() {
        ByteBuffer byteBuffer = getByteBuffer();
        byte[] bArr = new byte[byteBuffer.limit()];
        byteBuffer.get(bArr);
        Log.m285d(TAG, "Length = " + bArr.length);
        StringBuilder stringBuilder = new StringBuilder((bArr.length * 3) + 100);
        int i = 0;
        while (i < bArr.length) {
            if (i > 0 && bArr[i] != null && bArr[i - 1] == null) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(String.format("%02X ", new Object[]{Byte.valueOf(bArr[i])}));
            i++;
        }
        Log.m285d(TAG, stringBuilder.toString());
    }
}
