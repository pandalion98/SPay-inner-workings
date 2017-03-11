package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.text.ParseException;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class PDOLCheckEntry {
    public static final byte ALIAS_NOT_FOUND = (byte) -1;
    public static final byte PDOL_ACTION_TYPE_CONTINUE_MASK = (byte) 8;
    public static final byte PDOL_ACTION_TYPE_EXIT_MASK = (byte) 2;
    public static final byte PDOL_ACTION_TYPE_JUMP_TO_LINE_MASK = (byte) 1;
    public static final byte PDOL_ACTION_TYPE_RFU_MASK = (byte) 4;
    public static final byte PDOL_ACTION_TYPE_SET_VALUE_MASK = (byte) 8;
    public static final byte PDOL_DATA_TYPE_ALIAS_ID_MASK = (byte) 4;
    public static final byte PDOL_DATA_TYPE_INTERFACE_MASK = (byte) 8;
    public static final byte PDOL_DATA_TYPE_INTERNAL_CARD_MASK = (byte) 2;
    public static final byte PDOL_DATA_TYPE_PDOL_DATA_MASK = (byte) 1;
    public static final byte PDOL_LENGTH_DATA_OFFSET = (byte) 2;
    public static final byte PDOL_LENGTH_POSITION = (byte) 0;
    public static final byte PDOL_TEST_TYPE_ALWAYS = (byte) 0;
    public static final byte PDOL_TEST_TYPE_EQUAL_OR_GREATER = (byte) 1;
    public static final byte PDOL_TEST_TYPE_EXACT_MATCH = (byte) 4;
    public static final byte PDOL_TEST_TYPE_NEVER = (byte) 8;
    public static final byte PDOL_TEST_TYPE_NO_MATCH = (byte) 2;
    private static final String TAG = "DCSDK_PDOLCheckEntry";
    private ByteBuffer mBitMask;
    private byte[] mDataOffset;
    private byte mDataSize;
    private byte mDataType;
    private byte mMatchFound;
    private byte mMatchNotFound;
    private byte mNumMatchValues;
    private byte mResult;
    private byte mTestType;
    private ByteBuffer[] mValues;

    public PDOLCheckEntry(byte b, byte b2, byte[] bArr, byte b3, byte b4, byte b5, byte b6, ByteBuffer byteBuffer, byte b7, ByteBuffer[] byteBufferArr) {
        this.mDataType = b;
        this.mDataSize = b2;
        this.mDataOffset = bArr;
        this.mTestType = b3;
        this.mResult = b4;
        this.mMatchFound = b5;
        this.mMatchNotFound = b6;
        this.mBitMask = byteBuffer;
        this.mNumMatchValues = b7;
        this.mValues = byteBufferArr;
    }

    public byte getDataType() {
        return this.mDataType;
    }

    public byte getDataSize() {
        return this.mDataSize;
    }

    public byte[] getDataOffset() {
        return this.mDataOffset;
    }

    public byte getTestType() {
        return this.mTestType;
    }

    public byte getResult() {
        return this.mResult;
    }

    public byte getMatchFound() {
        return this.mMatchFound;
    }

    public byte getMatchNotFound() {
        return this.mMatchNotFound;
    }

    public ByteBuffer getBitMask() {
        return this.mBitMask;
    }

    public byte getNumberMatchValues() {
        return this.mNumMatchValues;
    }

    public void setNumberMatchValues(byte b) {
        this.mNumMatchValues = b;
    }

    public ByteBuffer[] getValues() {
        return this.mValues;
    }

    public void setValues(ByteBuffer[] byteBufferArr) {
        this.mValues = byteBufferArr;
    }

    public static PDOLCheckEntry[] parsePdolEntries(ByteBuffer byteBuffer) {
        Log.m287i(TAG, "PDOLCheckEntry, parsePdolEntries...");
        if (byteBuffer == null) {
            Log.m286e(TAG, "PDOLCheckEntry, parsePdolEntries, input buffer is null.");
            throw new ParseException("Input buffer is null", 0);
        }
        try {
            byte b = byteBuffer.getByte(0);
            PDOLCheckEntry[] pDOLCheckEntryArr = new PDOLCheckEntry[b];
            int i = 1;
            byte b2 = (byte) 0;
            while (i < byteBuffer.getSize() && b2 < b) {
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, offset: " + i);
                byte b3 = byteBuffer.getByte(i);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, sizeOfCheck: " + b3);
                byte b4 = (byte) (i + 1);
                if (b4 + b3 > byteBuffer.getSize()) {
                    Log.m286e(TAG, "PDOLCheckEntry, parsePdolEntries, entry size exceeds data length, entry size " + b3 + ", length " + byteBuffer.getSize());
                    throw new ParseException("Cannot parse", 0);
                }
                byte b5 = (byte) ((byteBuffer.getByte(b4) >> 4) & 15);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, dataType: " + b5);
                byte b6 = (byte) (byteBuffer.getByte(b4) & 15);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, dataSize: " + b6);
                b3 = (byte) (b4 + 1);
                byte[] bytes = byteBuffer.copyBytes(b3, b3 + 2).getBytes();
                b3 = (byte) (b3 + 2);
                byte b7 = (byte) ((byteBuffer.getByte(b3) >> 4) & 15);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, testType: " + b7);
                byte b8 = (byte) (byteBuffer.getByte(b3) & 15);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, result: " + b8);
                b3 = (byte) (b3 + 1);
                byte b9 = byteBuffer.getByte(b3);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, matchFound: " + (b9 & GF2Field.MASK));
                b3 = (byte) (b3 + 1);
                byte b10 = byteBuffer.getByte(b3);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, matchNotFound: " + (b10 & GF2Field.MASK));
                b3 = (byte) (b3 + 1);
                ByteBuffer copyBytes = byteBuffer.copyBytes(b3, b3 + b6);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, bitMask: " + copyBytes.toHexString());
                b3 = (byte) (b3 + b6);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, offset: " + b3);
                byte b11 = byteBuffer.getByte(b3);
                int i2 = b11 * b6;
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, numOfMatchValues: " + b11);
                Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, valuesLength: " + i2);
                i = (byte) (b3 + 1);
                ByteBuffer[] byteBufferArr = new ByteBuffer[b11];
                for (b3 = PDOL_TEST_TYPE_ALWAYS; b3 < b11; b3++) {
                    Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, value offset: " + i + "; offset + dataSize: " + (i + b6));
                    byteBufferArr[b3] = byteBuffer.copyBytes(i, i + b6);
                    Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, values[i]: " + byteBufferArr[b3].toHexString());
                    i = (byte) (i + b6);
                }
                int i3 = 0;
                while (true) {
                    int length = byteBufferArr.length;
                    if (i3 >= r0) {
                        break;
                    }
                    Log.m285d(TAG, "PDOLCheckEntry, parsePdolEntries, value: " + i3 + "; value = " + byteBufferArr[i3]);
                    i3++;
                }
                pDOLCheckEntryArr[b2] = new PDOLCheckEntry(b5, b6, bytes, b7, b8, b9, b10, copyBytes, b11, byteBufferArr);
                b2++;
            }
            return pDOLCheckEntryArr;
        } catch (NullPointerException e) {
            Log.m286e(TAG, "PDOLCheckEntry, parsePdolEntries, unexpected NullPointerException.");
            e.printStackTrace();
            throw new ParseException(BuildConfig.FLAVOR, 0);
        } catch (ArrayIndexOutOfBoundsException e2) {
            Log.m286e(TAG, "PDOLCheckEntry, parsePdolEntries, unexpected ArrayIndexOutOfBoundsException.");
            e2.printStackTrace();
            throw new ParseException(BuildConfig.FLAVOR, 0);
        }
    }
}
