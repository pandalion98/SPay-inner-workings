/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 */
package com.americanexpress.sdkmodulelib.tlv;

import com.americanexpress.sdkmodulelib.tlv.Tag;
import com.americanexpress.sdkmodulelib.tlv.Util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BERTLV {
    private int length;
    private byte[] rawEncodedLengthBytes;
    private Tag tag;
    private byte[] valueBytes;

    public BERTLV(Tag tag, int n2, byte[] arrby, byte[] arrby2) {
        if (n2 != arrby2.length) {
            throw new IllegalArgumentException("length != bytes.length");
        }
        this.tag = tag;
        this.rawEncodedLengthBytes = arrby;
        this.valueBytes = arrby2;
        this.length = n2;
    }

    public BERTLV(Tag tag, byte[] arrby) {
        this.tag = tag;
        this.rawEncodedLengthBytes = BERTLV.encodeLength(arrby.length);
        this.valueBytes = arrby;
        this.length = arrby.length;
    }

    public static byte[] encodeLength(int n2) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static void main(String[] arrstring) {
    }

    public int getLength() {
        return this.length;
    }

    public byte[] getRawEncodedLengthBytes() {
        return this.rawEncodedLengthBytes;
    }

    public Tag getTag() {
        return this.tag;
    }

    public byte[] getTagBytes() {
        return this.tag.getTagBytes();
    }

    public byte[] getValueBytes() {
        return this.valueBytes;
    }

    public ByteArrayInputStream getValueStream() {
        return new ByteArrayInputStream(this.valueBytes);
    }

    public byte[] toBERTLVByteArray() {
        byte[] arrby = this.tag.getTagBytes();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(arrby.length + this.rawEncodedLengthBytes.length + this.valueBytes.length);
        byteArrayOutputStream.write(arrby, 0, arrby.length);
        byteArrayOutputStream.write(this.rawEncodedLengthBytes, 0, this.rawEncodedLengthBytes.length);
        byteArrayOutputStream.write(this.valueBytes, 0, this.valueBytes.length);
        return byteArrayOutputStream.toByteArray();
    }

    public String toString() {
        return "BER-TLV[" + Util.byteArrayToHexString(this.getTagBytes()) + ", " + Util.int2Hex(this.length) + " (raw " + Util.byteArrayToHexString(this.rawEncodedLengthBytes) + ")" + ", " + Util.byteArrayToHexString(this.valueBytes) + "]";
    }
}

