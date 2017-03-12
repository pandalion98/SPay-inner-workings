package com.americanexpress.sdkmodulelib.tlv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BERTLV {
    private int length;
    private byte[] rawEncodedLengthBytes;
    private Tag tag;
    private byte[] valueBytes;

    public BERTLV(Tag tag, int i, byte[] bArr, byte[] bArr2) {
        if (i != bArr2.length) {
            throw new IllegalArgumentException("length != bytes.length");
        }
        this.tag = tag;
        this.rawEncodedLengthBytes = bArr;
        this.valueBytes = bArr2;
        this.length = i;
    }

    public BERTLV(Tag tag, byte[] bArr) {
        this.tag = tag;
        this.rawEncodedLengthBytes = encodeLength(bArr.length);
        this.valueBytes = bArr;
        this.length = bArr.length;
    }

    public static byte[] encodeLength(int i) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public byte[] getTagBytes() {
        return this.tag.getTagBytes();
    }

    public byte[] getRawEncodedLengthBytes() {
        return this.rawEncodedLengthBytes;
    }

    public byte[] getValueBytes() {
        return this.valueBytes;
    }

    public ByteArrayInputStream getValueStream() {
        return new ByteArrayInputStream(this.valueBytes);
    }

    public byte[] toBERTLVByteArray() {
        byte[] tagBytes = this.tag.getTagBytes();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((tagBytes.length + this.rawEncodedLengthBytes.length) + this.valueBytes.length);
        byteArrayOutputStream.write(tagBytes, 0, tagBytes.length);
        byteArrayOutputStream.write(this.rawEncodedLengthBytes, 0, this.rawEncodedLengthBytes.length);
        byteArrayOutputStream.write(this.valueBytes, 0, this.valueBytes.length);
        return byteArrayOutputStream.toByteArray();
    }

    public String toString() {
        return "BER-TLV[" + Util.byteArrayToHexString(getTagBytes()) + ", " + Util.int2Hex(this.length) + " (raw " + Util.byteArrayToHexString(this.rawEncodedLengthBytes) + ")" + ", " + Util.byteArrayToHexString(this.valueBytes) + "]";
    }

    public Tag getTag() {
        return this.tag;
    }

    public int getLength() {
        return this.length;
    }

    public static void main(String[] strArr) {
    }
}
