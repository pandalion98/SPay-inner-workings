package com.android.volley.p000a;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.macs.SkeinMac;

/* renamed from: com.android.volley.a.i */
public class PoolingByteArrayOutputStream extends ByteArrayOutputStream {
    private final ByteArrayPool bi;

    public PoolingByteArrayOutputStream(ByteArrayPool byteArrayPool, int i) {
        this.bi = byteArrayPool;
        this.buf = this.bi.m51b(Math.max(i, SkeinMac.SKEIN_256));
    }

    public void close() {
        this.bi.m50a(this.buf);
        this.buf = null;
        super.close();
    }

    public void finalize() {
        this.bi.m50a(this.buf);
    }

    private void m96d(int i) {
        if (this.count + i > this.buf.length) {
            Object b = this.bi.m51b((this.count + i) * 2);
            System.arraycopy(this.buf, 0, b, 0, this.count);
            this.bi.m50a(this.buf);
            this.buf = b;
        }
    }

    public synchronized void write(byte[] bArr, int i, int i2) {
        m96d(i2);
        super.write(bArr, i, i2);
    }

    public synchronized void write(int i) {
        m96d(1);
        super.write(i);
    }
}
