/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.tls;

public class ByteQueue {
    private static final int DEFAULT_CAPACITY = 1024;
    private int available = 0;
    private byte[] databuf;
    private int skipped = 0;

    public ByteQueue() {
        this(1024);
    }

    public ByteQueue(int n2) {
        this.databuf = new byte[n2];
    }

    public static int nextTwoPow(int n2) {
        int n3 = n2 | n2 >> 1;
        int n4 = n3 | n3 >> 2;
        int n5 = n4 | n4 >> 4;
        int n6 = n5 | n5 >> 8;
        return 1 + (n6 | n6 >> 16);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addData(byte[] arrby, int n2, int n3) {
        if (n3 + (this.skipped + this.available) > this.databuf.length) {
            int n4 = ByteQueue.nextTwoPow(n3 + this.available);
            if (n4 > this.databuf.length) {
                byte[] arrby2 = new byte[n4];
                System.arraycopy((Object)this.databuf, (int)this.skipped, (Object)arrby2, (int)0, (int)this.available);
                this.databuf = arrby2;
            } else {
                System.arraycopy((Object)this.databuf, (int)this.skipped, (Object)this.databuf, (int)0, (int)this.available);
            }
            this.skipped = 0;
        }
        System.arraycopy((Object)arrby, (int)n2, (Object)this.databuf, (int)(this.skipped + this.available), (int)n3);
        this.available = n3 + this.available;
    }

    public int available() {
        return this.available;
    }

    public void read(byte[] arrby, int n2, int n3, int n4) {
        if (arrby.length - n2 < n3) {
            throw new IllegalArgumentException("Buffer size of " + arrby.length + " is too small for a read of " + n3 + " bytes");
        }
        if (this.available - n4 < n3) {
            throw new IllegalStateException("Not enough data to read");
        }
        System.arraycopy((Object)this.databuf, (int)(n4 + this.skipped), (Object)arrby, (int)n2, (int)n3);
    }

    public void removeData(int n2) {
        if (n2 > this.available) {
            throw new IllegalStateException("Cannot remove " + n2 + " bytes, only got " + this.available);
        }
        this.available -= n2;
        this.skipped = n2 + this.skipped;
    }

    public void removeData(byte[] arrby, int n2, int n3, int n4) {
        this.read(arrby, n2, n3, n4);
        this.removeData(n4 + n3);
    }

    public byte[] removeData(int n2, int n3) {
        byte[] arrby = new byte[n2];
        this.removeData(arrby, 0, n2, n3);
        return arrby;
    }

    public int size() {
        return this.available;
    }
}

