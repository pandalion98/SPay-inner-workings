/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class PDOLCheckEntry {
    public static final byte ALIAS_NOT_FOUND = -1;
    public static final byte PDOL_ACTION_TYPE_CONTINUE_MASK = 8;
    public static final byte PDOL_ACTION_TYPE_EXIT_MASK = 2;
    public static final byte PDOL_ACTION_TYPE_JUMP_TO_LINE_MASK = 1;
    public static final byte PDOL_ACTION_TYPE_RFU_MASK = 4;
    public static final byte PDOL_ACTION_TYPE_SET_VALUE_MASK = 8;
    public static final byte PDOL_DATA_TYPE_ALIAS_ID_MASK = 4;
    public static final byte PDOL_DATA_TYPE_INTERFACE_MASK = 8;
    public static final byte PDOL_DATA_TYPE_INTERNAL_CARD_MASK = 2;
    public static final byte PDOL_DATA_TYPE_PDOL_DATA_MASK = 1;
    public static final byte PDOL_LENGTH_DATA_OFFSET = 2;
    public static final byte PDOL_LENGTH_POSITION = 0;
    public static final byte PDOL_TEST_TYPE_ALWAYS = 0;
    public static final byte PDOL_TEST_TYPE_EQUAL_OR_GREATER = 1;
    public static final byte PDOL_TEST_TYPE_EXACT_MATCH = 4;
    public static final byte PDOL_TEST_TYPE_NEVER = 8;
    public static final byte PDOL_TEST_TYPE_NO_MATCH = 2;
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

    public PDOLCheckEntry(byte by, byte by2, byte[] arrby, byte by3, byte by4, byte by5, byte by6, ByteBuffer byteBuffer, byte by7, ByteBuffer[] arrbyteBuffer) {
        this.mDataType = by;
        this.mDataSize = by2;
        this.mDataOffset = arrby;
        this.mTestType = by3;
        this.mResult = by4;
        this.mMatchFound = by5;
        this.mMatchNotFound = by6;
        this.mBitMask = byteBuffer;
        this.mNumMatchValues = by7;
        this.mValues = arrbyteBuffer;
    }

    /*
     * Exception decompiling
     */
    public static PDOLCheckEntry[] parsePdolEntries(ByteBuffer var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 15[WHILELOOP]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public ByteBuffer getBitMask() {
        return this.mBitMask;
    }

    public byte[] getDataOffset() {
        return this.mDataOffset;
    }

    public byte getDataSize() {
        return this.mDataSize;
    }

    public byte getDataType() {
        return this.mDataType;
    }

    public byte getMatchFound() {
        return this.mMatchFound;
    }

    public byte getMatchNotFound() {
        return this.mMatchNotFound;
    }

    public byte getNumberMatchValues() {
        return this.mNumMatchValues;
    }

    public byte getResult() {
        return this.mResult;
    }

    public byte getTestType() {
        return this.mTestType;
    }

    public ByteBuffer[] getValues() {
        return this.mValues;
    }

    public void setNumberMatchValues(byte by) {
        this.mNumMatchValues = by;
    }

    public void setValues(ByteBuffer[] arrbyteBuffer) {
        this.mValues = arrbyteBuffer;
    }
}

