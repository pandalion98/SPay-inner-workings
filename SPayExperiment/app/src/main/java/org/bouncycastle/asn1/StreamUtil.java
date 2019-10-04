/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.FileInputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.Runtime
 *  java.nio.channels.FileChannel
 */
package org.bouncycastle.asn1;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.LimitedInputStream;

class StreamUtil {
    private static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();

    StreamUtil() {
    }

    static int calculateBodyLength(int n2) {
        int n3 = 1;
        if (n2 > 127) {
            int n4 = n3;
            while ((n2 >>>= 8) != 0) {
                ++n4;
            }
            for (int i2 = 8 * (n4 - 1); i2 >= 0; i2 -= 8) {
                int n5 = n3 + 1;
                n3 = n5;
            }
        }
        return n3;
    }

    static int calculateTagLength(int n2) {
        if (n2 >= 31) {
            if (n2 < 128) {
                return 2;
            }
            byte[] arrby = new byte[5];
            int n3 = -1 + arrby.length;
            arrby[n3] = (byte)(n2 & 127);
            do {
                arrby[--n3] = (byte)(128 | (n2 >>= 7) & 127);
            } while (n2 > 127);
            return 1 + (arrby.length - n3);
        }
        return 1;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static int findLimit(InputStream inputStream) {
        if (inputStream instanceof LimitedInputStream) {
            return ((LimitedInputStream)inputStream).getRemaining();
        }
        if (inputStream instanceof ASN1InputStream) {
            return ((ASN1InputStream)inputStream).getLimit();
        }
        if (inputStream instanceof ByteArrayInputStream) {
            return ((ByteArrayInputStream)inputStream).available();
        }
        if (inputStream instanceof FileInputStream) {
            long l2;
            try {
                long l3;
                FileChannel fileChannel = ((FileInputStream)inputStream).getChannel();
                l2 = fileChannel != null ? (l3 = fileChannel.size()) : Integer.MAX_VALUE;
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (l2 < Integer.MAX_VALUE) {
                return (int)l2;
            }
        }
        if (MAX_MEMORY > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)MAX_MEMORY;
    }
}

