/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.nio.Buffer
 *  java.nio.ByteBuffer
 *  java.nio.ByteOrder
 */
package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spayfw.b.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javolution.io.Struct;

public class TAStruct
extends Struct {
    private static final String TAG = "TAStruct";

    @Override
    public ByteOrder byteOrder() {
        return ByteOrder.nativeOrder();
    }

    public TAStruct deserialize(byte[] arrby) {
        this.setByteBuffer(ByteBuffer.wrap((byte[])arrby).order(this.byteOrder()), 0);
        return this;
    }

    public void dumpBuffer() {
        ByteBuffer byteBuffer = this.getByteBuffer();
        byte[] arrby = new byte[byteBuffer.limit()];
        byteBuffer.get(arrby);
        Log.d(TAG, "Length = " + arrby.length);
        StringBuilder stringBuilder = new StringBuilder(100 + 3 * arrby.length);
        for (int i2 = 0; i2 < arrby.length; ++i2) {
            if (i2 > 0 && arrby[i2] != 0 && arrby[i2 - 1] == 0) {
                stringBuilder.append("\n");
            }
            Object[] arrobject = new Object[]{arrby[i2]};
            stringBuilder.append(String.format((String)"%02X ", (Object[])arrobject));
        }
        Log.d(TAG, stringBuilder.toString());
    }

    @Override
    public boolean isPacked() {
        return true;
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = this.getByteBuffer();
        byte[] arrby = new byte[byteBuffer.limit()];
        byteBuffer.position(0);
        byteBuffer.get(arrby);
        return arrby;
    }

    public TAStruct setData() {
        return this;
    }
}

