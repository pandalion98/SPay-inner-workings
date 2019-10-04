/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mobile_api.utils;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;

public class TLV {
    public static ByteArray create(byte by, ByteArray byteArray) {
        ByteArray byteArray2 = TLV.lengthBytes(byteArray);
        int n2 = byteArray2.getLength();
        int n3 = n2 + 1 + byteArray.getLength();
        ByteArray byteArray3 = ByteArrayFactory.getInstance().getByteArray(n3);
        byteArray3.setByte(0, by);
        byteArray3.copyBytes(byteArray2, 0, 1, n2);
        byteArray3.copyBytes(byteArray, 0, n2 + 1, byteArray.getLength());
        return byteArray3;
    }

    public static ByteArray create(ByteArray byteArray, ByteArray byteArray2) {
        ByteArray byteArray3 = ByteArrayFactory.getInstance().getFromByteArray(byteArray);
        byteArray3.append(TLV.lengthBytes(byteArray2));
        byteArray3.append(byteArray2);
        return byteArray3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ByteArray lengthBytes(ByteArray byteArray) {
        int n2 = byteArray.getLength();
        if (n2 <= 127) {
            ByteArray byteArray2 = ByteArrayFactory.getInstance().getByteArray(1);
            byteArray2.setByte(0, (byte)n2);
            return byteArray2;
        }
        if (n2 <= 255) {
            ByteArray byteArray3 = ByteArrayFactory.getInstance().getByteArray(2);
            byteArray3.setByte(0, (byte)-127);
            byteArray3.setByte(1, (byte)n2);
            return byteArray3;
        }
        if (n2 <= 65535) {
            ByteArray byteArray4 = ByteArrayFactory.getInstance().getByteArray(3);
            byteArray4.setByte(0, (byte)-126);
            byteArray4.setByte(1, (byte)n2);
        } else if (n2 <= 16777215) {
            ByteArray byteArray5 = ByteArrayFactory.getInstance().getByteArray(4);
            byteArray5.setByte(0, (byte)-125);
            byteArray5.setByte(1, (byte)n2);
            return byteArray5;
        }
        ByteArray byteArray6 = ByteArrayFactory.getInstance().getByteArray(4);
        byteArray6.setByte(0, (byte)-124);
        byteArray6.setByte(1, (byte)n2);
        return byteArray6;
    }
}

