package org.bouncycastle.util.io;

import java.io.OutputStream;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.util.Arrays;

public class BufferingOutputStream extends OutputStream {
    private final byte[] buf;
    private int bufOff;
    private final OutputStream other;

    public BufferingOutputStream(OutputStream outputStream) {
        this.other = outputStream;
        this.buf = new byte[PKIFailureInfo.certConfirmed];
    }

    public BufferingOutputStream(OutputStream outputStream, int i) {
        this.other = outputStream;
        this.buf = new byte[i];
    }

    public void close() {
        flush();
        this.other.close();
    }

    public void flush() {
        this.other.write(this.buf, 0, this.bufOff);
        this.bufOff = 0;
        Arrays.fill(this.buf, (byte) 0);
    }

    public void write(int i) {
        byte[] bArr = this.buf;
        int i2 = this.bufOff;
        this.bufOff = i2 + 1;
        bArr[i2] = (byte) i;
        if (this.bufOff == this.buf.length) {
            flush();
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        if (i2 < this.buf.length - this.bufOff) {
            System.arraycopy(bArr, i, this.buf, this.bufOff, i2);
            this.bufOff += i2;
            return;
        }
        int length = this.buf.length - this.bufOff;
        System.arraycopy(bArr, i, this.buf, this.bufOff, length);
        this.bufOff += length;
        flush();
        int i3 = i + length;
        length = i2 - length;
        while (length >= this.buf.length) {
            this.other.write(bArr, i3, this.buf.length);
            i3 += this.buf.length;
            length -= this.buf.length;
        }
        if (length > 0) {
            System.arraycopy(bArr, i3, this.buf, this.bufOff, length);
            this.bufOff = length + this.bufOff;
        }
    }
}
