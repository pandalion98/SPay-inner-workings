package com.goodix.cap.fingerprint.utils;

public class TestParamEncoder {
    private static final String TAG = "TestParamEncoder";
    public static final int TEST_ENCODE_SIZEOF_DOUBLE = 16;
    public static final int TEST_ENCODE_SIZEOF_FLOAT = 12;
    public static final int TEST_ENCODE_SIZEOF_INT16 = 6;
    public static final int TEST_ENCODE_SIZEOF_INT32 = 8;
    public static final int TEST_ENCODE_SIZEOF_INT64 = 12;
    public static final int TEST_ENCODE_SIZEOF_INT8 = 5;

    public static final int testEncodeSizeOfArray(int len) {
        return 8 + len;
    }

    public static int encodeInt8(byte[] buf, int offset, int key, byte value) {
        if (buf == null) {
            return offset;
        }
        int offset2 = offset + 1;
        buf[offset] = (byte) (key & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((key >> 8) & 255);
        int offset4 = offset3 + 1;
        buf[offset3] = (byte) ((key >> 16) & 255);
        int offset5 = offset4 + 1;
        buf[offset4] = (byte) ((key >> 24) & 255);
        int offset6 = offset5 + 1;
        buf[offset5] = value;
        return offset6;
    }

    public static int encodeInt16(byte[] buf, int offset, int key, short value) {
        if (buf == null) {
            return offset;
        }
        int offset2 = offset + 1;
        buf[offset] = (byte) (key & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((key >> 8) & 255);
        int offset4 = offset3 + 1;
        buf[offset3] = (byte) ((key >> 16) & 255);
        int offset5 = offset4 + 1;
        buf[offset4] = (byte) ((key >> 24) & 255);
        int offset6 = offset5 + 1;
        buf[offset5] = (byte) (value & 255);
        int offset7 = offset6 + 1;
        buf[offset6] = (byte) ((value >> 8) & 255);
        return offset7;
    }

    public static int encodeInt32(byte[] buf, int offset, int key, int value) {
        if (buf == null) {
            return offset;
        }
        int offset2 = offset + 1;
        buf[offset] = (byte) (key & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((key >> 8) & 255);
        int offset4 = offset3 + 1;
        buf[offset3] = (byte) ((key >> 16) & 255);
        int offset5 = offset4 + 1;
        buf[offset4] = (byte) ((key >> 24) & 255);
        int offset6 = offset5 + 1;
        buf[offset5] = (byte) (value & 255);
        int offset7 = offset6 + 1;
        buf[offset6] = (byte) ((value >> 8) & 255);
        int offset8 = offset7 + 1;
        buf[offset7] = (byte) ((value >> 16) & 255);
        int offset9 = offset8 + 1;
        buf[offset8] = (byte) ((value >> 24) & 255);
        return offset9;
    }

    public static int encodeInt64(byte[] buf, int offset, int key, long value) {
        if (buf == null) {
            return offset;
        }
        int offset2 = offset + 1;
        buf[offset] = (byte) (key & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((key >> 8) & 255);
        int offset4 = offset3 + 1;
        buf[offset3] = (byte) ((key >> 16) & 255);
        int offset5 = offset4 + 1;
        buf[offset4] = (byte) ((key >> 24) & 255);
        int offset6 = offset5 + 1;
        buf[offset5] = (byte) ((int) (value & 255));
        int offset7 = offset6 + 1;
        buf[offset6] = (byte) ((int) ((value >> 8) & 255));
        int offset8 = offset7 + 1;
        buf[offset7] = (byte) ((int) ((value >> 16) & 255));
        int offset9 = offset8 + 1;
        buf[offset8] = (byte) ((int) ((value >> 24) & 255));
        int offset10 = offset9 + 1;
        buf[offset9] = (byte) ((int) ((value >> 32) & 255));
        int offset11 = offset10 + 1;
        buf[offset10] = (byte) ((int) ((value >> 40) & 255));
        int offset12 = offset11 + 1;
        buf[offset11] = (byte) ((int) ((value >> 48) & 255));
        int offset13 = offset12 + 1;
        buf[offset12] = (byte) ((int) (255 & (value >> 56)));
        return offset13;
    }

    public static int encodeFloat(byte[] buf, int offset, int key, float value) {
        if (buf == null) {
            return offset;
        }
        int offset2 = offset + 1;
        buf[offset] = (byte) (key & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((key >> 8) & 255);
        int offset4 = offset3 + 1;
        buf[offset3] = (byte) ((key >> 16) & 255);
        int offset5 = offset4 + 1;
        buf[offset4] = (byte) ((key >> 24) & 255);
        int offset6 = offset5 + 1;
        buf[offset5] = (byte) (4 & 255);
        int offset7 = offset6 + 1;
        buf[offset6] = (byte) ((4 >> 8) & 255);
        int offset8 = offset7 + 1;
        buf[offset7] = (byte) ((4 >> 16) & 255);
        int offset9 = offset8 + 1;
        buf[offset8] = (byte) ((4 >> 24) & 255);
        int offset10 = Float.floatToIntBits(value);
        int offset11 = offset9 + 1;
        buf[offset9] = (byte) (offset10 & 255);
        int offset12 = offset11 + 1;
        buf[offset11] = (byte) ((offset10 >> 8) & 255);
        int offset13 = offset12 + 1;
        buf[offset12] = (byte) ((offset10 >> 16) & 255);
        int offset14 = offset13 + 1;
        buf[offset13] = (byte) ((offset10 >> 24) & 255);
        return offset14;
    }

    public static int encodeDouble(byte[] buf, int offset, int key, double value) {
        if (buf == null) {
            return offset;
        }
        int offset2 = offset + 1;
        buf[offset] = (byte) (key & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((key >> 8) & 255);
        int offset4 = offset3 + 1;
        buf[offset3] = (byte) ((key >> 16) & 255);
        int offset5 = offset4 + 1;
        buf[offset4] = (byte) ((key >> 24) & 255);
        int offset6 = offset5 + 1;
        buf[offset5] = (byte) (8 & 255);
        int offset7 = offset6 + 1;
        buf[offset6] = (byte) ((8 >> 8) & 255);
        int offset8 = offset7 + 1;
        buf[offset7] = (byte) ((8 >> 16) & 255);
        int offset9 = offset8 + 1;
        buf[offset8] = (byte) ((8 >> 24) & 255);
        long valueInt = Double.doubleToLongBits(value);
        int offset10 = offset9 + 1;
        buf[offset9] = (byte) ((int) (valueInt & 255));
        int offset11 = offset10 + 1;
        buf[offset10] = (byte) ((int) ((valueInt >> 8) & 255));
        int offset12 = offset11 + 1;
        buf[offset11] = (byte) ((int) ((valueInt >> 16) & 255));
        int offset13 = offset12 + 1;
        buf[offset12] = (byte) ((int) ((valueInt >> 24) & 255));
        int offset14 = offset13 + 1;
        buf[offset13] = (byte) ((int) ((valueInt >> 32) & 255));
        int offset15 = offset14 + 1;
        buf[offset14] = (byte) ((int) ((valueInt >> 40) & 255));
        int offset16 = offset15 + 1;
        buf[offset15] = (byte) ((int) ((valueInt >> 48) & 255));
        int offset17 = offset16 + 1;
        buf[offset16] = (byte) ((int) (255 & (valueInt >> 56)));
        return offset17;
    }

    public static int encodeArray(byte[] buf, int offset, int key, byte[] array, int size) {
        if (buf == null || array == null) {
            return offset;
        }
        int offset2 = offset + 1;
        buf[offset] = (byte) (key & 255);
        int offset3 = offset2 + 1;
        buf[offset2] = (byte) ((key >> 8) & 255);
        int offset4 = offset3 + 1;
        buf[offset3] = (byte) ((key >> 16) & 255);
        int offset5 = offset4 + 1;
        buf[offset4] = (byte) ((key >> 24) & 255);
        int offset6 = offset5 + 1;
        buf[offset5] = (byte) (size & 255);
        int offset7 = offset6 + 1;
        buf[offset6] = (byte) ((size >> 8) & 255);
        int offset8 = offset7 + 1;
        buf[offset7] = (byte) ((size >> 16) & 255);
        int offset9 = offset8 + 1;
        buf[offset8] = (byte) ((size >> 24) & 255);
        System.arraycopy(array, 0, buf, offset9, size);
        return offset9 + size;
    }
}
