package com.samsung.android.spaytzsvc.api;

import com.samsung.android.spayfw.p002b.Log;
import java.nio.ByteBuffer;
import javolution.io.Struct.Unsigned32;

public class Blob extends TAStruct {
    private static final String TAG = "Blob";
    private ByteArray buf;
    private Unsigned32 len;

    private class ByteArray extends Member {
        public int length;
        private int mCurrentDataSize;

        public ByteArray(int i) {
            super(i << 3, 1);
            this.mCurrentDataSize = 0;
            this.length = 0;
            this.length = i;
        }

        public byte[] get(int i) {
            byte[] bArr;
            ByteBuffer byteBuffer = Blob.this.getByteBuffer();
            synchronized (byteBuffer) {
                byteBuffer.position(Blob.this.getByteBufferPosition() + offset());
                bArr = new byte[i];
                byteBuffer.get(bArr);
            }
            return bArr;
        }

        public void set(byte[] bArr) {
            ByteBuffer byteBuffer = Blob.this.getByteBuffer();
            synchronized (byteBuffer) {
                byteBuffer.position(Blob.this.getByteBufferPosition() + offset());
                byteBuffer.put(bArr);
                this.mCurrentDataSize = bArr.length;
            }
        }

        public void add(byte[] bArr) {
            ByteBuffer byteBuffer = Blob.this.getByteBuffer();
            synchronized (byteBuffer) {
                byteBuffer.position((Blob.this.getByteBufferPosition() + offset()) + this.mCurrentDataSize);
                byteBuffer.put(bArr);
                this.mCurrentDataSize += bArr.length;
            }
        }
    }

    public Blob(int i) {
        this.buf = null;
        this.len = new Unsigned32();
        this.buf = new ByteArray(i);
    }

    public void setData(byte[] bArr) {
        if (bArr != null) {
            try {
                if (bArr.length != 0) {
                    if (bArr.length > this.buf.length) {
                        Log.m290w(TAG, "setData: Input size is greater than the maximum allocated ... returning! b.len = " + bArr.length);
                        this.len.set(0);
                        throw new IllegalArgumentException("Blob:setData - Input size greater than max allowed");
                    }
                    this.len.set((long) bArr.length);
                    this.buf.set(bArr);
                    return;
                }
            } catch (Exception e) {
                Log.m286e(TAG, "setData exception!");
                e.printStackTrace();
                return;
            }
        }
        this.len.set(0);
    }

    public byte[] getData() {
        byte[] bArr = null;
        try {
            int i = (int) this.len.get();
            if (this.buf != null) {
                if (i > this.buf.length) {
                    Log.m290w(TAG, "getData: expected length (" + i + ") > buffer length (" + this.buf.length + ")");
                    throw new IllegalArgumentException("Blob:getData - size is larger than the real buffer length");
                }
                bArr = this.buf.get(i);
            }
        } catch (Exception e) {
            Log.m286e(TAG, "getData exception!");
            e.printStackTrace();
        }
        return bArr;
    }

    public void addData(byte[] bArr) {
        if (bArr != null) {
            try {
                if (bArr.length != 0) {
                    if (((int) this.len.get()) + bArr.length > this.buf.length) {
                        Log.m290w(TAG, "setData: Input size is greater than the maximum allocated ... returning! b.len = " + bArr.length);
                        return;
                    }
                    this.buf.add(bArr);
                    this.len.set(this.len.get() + ((long) bArr.length));
                }
            } catch (Exception e) {
                Log.m286e(TAG, "addData exception!");
                e.printStackTrace();
            }
        }
    }
}
