/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.util.io;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.util.io.StreamOverflowException;

public final class Streams {
    private static int BUFFER_SIZE = 512;

    public static void drain(InputStream inputStream) {
        byte[] arrby = new byte[BUFFER_SIZE];
        while (inputStream.read(arrby, 0, arrby.length) >= 0) {
        }
    }

    public static void pipeAll(InputStream inputStream, OutputStream outputStream) {
        int n;
        byte[] arrby = new byte[BUFFER_SIZE];
        while ((n = inputStream.read(arrby, 0, arrby.length)) >= 0) {
            outputStream.write(arrby, 0, n);
        }
    }

    public static long pipeAllLimited(InputStream inputStream, long l, OutputStream outputStream) {
        int n;
        long l2 = 0L;
        byte[] arrby = new byte[BUFFER_SIZE];
        while ((n = inputStream.read(arrby, 0, arrby.length)) >= 0) {
            if ((l2 += (long)n) > l) {
                throw new StreamOverflowException("Data Overflow");
            }
            outputStream.write(arrby, 0, n);
        }
        return l2;
    }

    public static byte[] readAll(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Streams.pipeAll(inputStream, (OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] readAllLimited(InputStream inputStream, int n) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Streams.pipeAllLimited(inputStream, n, (OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int readFully(InputStream inputStream, byte[] arrby) {
        return Streams.readFully(inputStream, arrby, 0, arrby.length);
    }

    public static int readFully(InputStream inputStream, byte[] arrby, int n, int n2) {
        int n3 = 0;
        int n4;
        while (n3 < n2 && (n4 = inputStream.read(arrby, n + n3, n2 - n3)) >= 0) {
            n3 += n4;
        }
        return n3;
    }
}

