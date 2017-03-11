package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.text.ParseException;

public class DiscoverIDDTag {
    public static final int ACCESS_CONDITION_GPO_REQUIRED = 64;
    private static final int IDDT_DATA_LENGTH = 3;
    public static final ByteBuffer IDDT_DF01;
    public static final ByteBuffer IDDT_DF02;
    public static final ByteBuffer IDDT_DF03;
    private static final int IDDT_TAG_LENGTH = 2;
    private static final String TAG = "DCSDK_DiscoverIDDTag";
    private byte mAccess;
    private ByteBuffer mData;
    private byte mSize;
    private ByteBuffer mTag;

    static {
        IDDT_DF01 = ByteBuffer.fromHexString("DF01");
        IDDT_DF02 = ByteBuffer.fromHexString("DF02");
        IDDT_DF03 = ByteBuffer.fromHexString("DF03");
    }

    public DiscoverIDDTag(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        if (byteBuffer == null || byteBuffer.getSize() < IDDT_DATA_LENGTH) {
            Log.m286e(TAG, "DiscoverIDDTag, data is empty or null...");
            throw new ParseException("DiscoverIDDTag, data is empty or null.", 0);
        } else if (byteBuffer2 == null || byteBuffer2.getSize() != IDDT_TAG_LENGTH) {
            Log.m286e(TAG, "DiscoverIDDTag, tag is empty or has a wrong length...");
            throw new ParseException("DiscoverIDDTag, tag is empty or has a wrong length.", 0);
        } else {
            this.mTag = byteBuffer2;
            parse(byteBuffer);
        }
    }

    private void parse(ByteBuffer byteBuffer) {
        this.mSize = byteBuffer.getByte(0);
        this.mAccess = byteBuffer.getByte(1);
        Log.m285d(TAG, "DiscoverIDDTag, tag size: " + this.mSize);
        Log.m285d(TAG, "DiscoverIDDTag, tag access: " + this.mAccess);
        if (this.mSize != byteBuffer.getSize() - 2) {
            Log.m286e(TAG, "DiscoverIDDTag, wrong length defined by the tag, tag size = " + this.mSize + ", actual length = " + (byteBuffer.getSize() - 2));
            throw new ParseException("DiscoverIDDTag, wrong length defined by the tag, tag size = " + this.mSize + ", actual length = " + (byteBuffer.getSize() - 2), 0);
        }
        this.mData = byteBuffer.copyBytes(IDDT_TAG_LENGTH, byteBuffer.getSize());
        Log.m285d(TAG, "DiscoverIDDTag, tag data: " + this.mData.toHexString());
    }

    public ByteBuffer getTag() {
        return this.mTag;
    }

    public byte getAccess() {
        return this.mAccess;
    }

    public byte getSize() {
        return this.mSize;
    }

    public ByteBuffer getData() {
        return this.mData;
    }
}
