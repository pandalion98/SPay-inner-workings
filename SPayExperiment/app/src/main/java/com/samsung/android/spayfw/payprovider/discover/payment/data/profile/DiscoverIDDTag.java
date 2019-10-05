/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.text.ParseException;

public class DiscoverIDDTag {
    public static final int ACCESS_CONDITION_GPO_REQUIRED = 64;
    private static final int IDDT_DATA_LENGTH = 3;
    public static final ByteBuffer IDDT_DF01 = ByteBuffer.fromHexString("DF01");
    public static final ByteBuffer IDDT_DF02 = ByteBuffer.fromHexString("DF02");
    public static final ByteBuffer IDDT_DF03 = ByteBuffer.fromHexString("DF03");
    private static final int IDDT_TAG_LENGTH = 2;
    private static final String TAG = "DCSDK_DiscoverIDDTag";
    private byte mAccess;
    private ByteBuffer mData;
    private byte mSize;
    private ByteBuffer mTag;

    public DiscoverIDDTag(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        if (byteBuffer == null || byteBuffer.getSize() < 3) {
            Log.e(TAG, "DiscoverIDDTag, data is empty or null...");
            throw new ParseException("DiscoverIDDTag, data is empty or null.", 0);
        }
        if (byteBuffer2 == null || byteBuffer2.getSize() != 2) {
            Log.e(TAG, "DiscoverIDDTag, tag is empty or has a wrong length...");
            throw new ParseException("DiscoverIDDTag, tag is empty or has a wrong length.", 0);
        }
        this.mTag = byteBuffer2;
        this.parse(byteBuffer);
    }

    private void parse(ByteBuffer byteBuffer) {
        this.mSize = byteBuffer.getByte(0);
        this.mAccess = byteBuffer.getByte(1);
        Log.d(TAG, "DiscoverIDDTag, tag size: " + this.mSize);
        Log.d(TAG, "DiscoverIDDTag, tag access: " + this.mAccess);
        if (this.mSize != -2 + byteBuffer.getSize()) {
            Log.e(TAG, "DiscoverIDDTag, wrong length defined by the tag, tag size = " + this.mSize + ", actual length = " + (-2 + byteBuffer.getSize()));
            throw new ParseException("DiscoverIDDTag, wrong length defined by the tag, tag size = " + this.mSize + ", actual length = " + (-2 + byteBuffer.getSize()), 0);
        }
        this.mData = byteBuffer.copyBytes(2, byteBuffer.getSize());
        Log.d(TAG, "DiscoverIDDTag, tag data: " + this.mData.toHexString());
    }

    public byte getAccess() {
        return this.mAccess;
    }

    public ByteBuffer getData() {
        return this.mData;
    }

    public byte getSize() {
        return this.mSize;
    }

    public ByteBuffer getTag() {
        return this.mTag;
    }
}

