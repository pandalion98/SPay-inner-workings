/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.nio.Buffer
 *  java.nio.ByteBuffer
 */
package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spayfw.b.Log;

import java.nio.ByteBuffer;
import javolution.io.Struct;

public class Blob
extends TAStruct {
    private static final String TAG = "Blob";
    private ByteArray buf = null;
    private Struct.Unsigned32 len = new Struct.Unsigned32();

    public Blob(int n2) {
        this.buf = new ByteArray(n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void addData(byte[] arrby) {
        if (arrby == null) return;
        {
            try {
                if (arrby.length == 0) {
                    return;
                }
                if ((int)this.len.get() + arrby.length > this.buf.length) {
                    Log.w(TAG, "setData: Input size is greater than the maximum allocated ... returning! b.len = " + arrby.length);
                    return;
                }
                this.buf.add(arrby);
                this.len.set(this.len.get() + (long)arrby.length);
                return;
            }
            catch (Exception exception) {
                Log.e(TAG, "addData exception!");
                exception.printStackTrace();
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] getData() {
        try {
            int n2 = (int)this.len.get();
            if (this.buf == null) {
                return null;
            }
            if (n2 <= this.buf.length) return this.buf.get(n2);
            Log.w(TAG, "getData: expected length (" + n2 + ") > buffer length (" + this.buf.length + ")");
            throw new IllegalArgumentException("Blob:getData - size is larger than the real buffer length");
        }
        catch (Exception exception) {
            Log.e(TAG, "getData exception!");
            exception.printStackTrace();
            return null;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void setData(byte[] var1_1) {
        if (var1_1 == null) ** GOTO lbl4
        try {
            block7 : {
                if (var1_1.length != 0) break block7;
lbl4: // 2 sources:
                this.len.set(0L);
                return;
            }
            if (var1_1.length > this.buf.length) {
                Log.w("Blob", "setData: Input size is greater than the maximum allocated ... returning! b.len = " + var1_1.length);
                this.len.set(0L);
                throw new IllegalArgumentException("Blob:setData - Input size greater than max allowed");
            }
            this.len.set(var1_1.length);
            this.buf.set(var1_1);
            return;
        }
        catch (Exception var2_2) {
            Log.e("Blob", "setData exception!");
            var2_2.printStackTrace();
            return;
        }
    }

    private class ByteArray
    extends Struct.Member {
        public int length;
        private int mCurrentDataSize;

        public ByteArray(int n2) {
            super(n2 << 3, 1);
            this.mCurrentDataSize = 0;
            this.length = 0;
            this.length = n2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void add(byte[] arrby) {
            ByteBuffer byteBuffer;
            ByteBuffer byteBuffer2 = byteBuffer = Blob.this.getByteBuffer();
            synchronized (byteBuffer2) {
                byteBuffer.position(Blob.this.getByteBufferPosition() + this.offset() + this.mCurrentDataSize);
                byteBuffer.put(arrby);
                this.mCurrentDataSize += arrby.length;
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public byte[] get(int n2) {
            ByteBuffer byteBuffer;
            ByteBuffer byteBuffer2 = byteBuffer = Blob.this.getByteBuffer();
            synchronized (byteBuffer2) {
                byteBuffer.position(Blob.this.getByteBufferPosition() + this.offset());
                byte[] arrby = new byte[n2];
                byteBuffer.get(arrby);
                return arrby;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void set(byte[] arrby) {
            ByteBuffer byteBuffer;
            ByteBuffer byteBuffer2 = byteBuffer = Blob.this.getByteBuffer();
            synchronized (byteBuffer2) {
                byteBuffer.position(Blob.this.getByteBufferPosition() + this.offset());
                byteBuffer.put(arrby);
                this.mCurrentDataSize = arrby.length;
                return;
            }
        }
    }

}

