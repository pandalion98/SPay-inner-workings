package org.bouncycastle.util.io;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.crypto.macs.SkeinMac;

public final class Streams {
    private static int BUFFER_SIZE;

    static {
        BUFFER_SIZE = SkeinMac.SKEIN_512;
    }

    public static void drain(InputStream inputStream) {
        byte[] bArr = new byte[BUFFER_SIZE];
        do {
        } while (inputStream.read(bArr, 0, bArr.length) >= 0);
    }

    public static void pipeAll(InputStream inputStream, OutputStream outputStream) {
        byte[] bArr = new byte[BUFFER_SIZE];
        while (true) {
            int read = inputStream.read(bArr, 0, bArr.length);
            if (read >= 0) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public static long pipeAllLimited(InputStream inputStream, long j, OutputStream outputStream) {
        long j2 = 0;
        byte[] bArr = new byte[BUFFER_SIZE];
        while (true) {
            int read = inputStream.read(bArr, 0, bArr.length);
            if (read < 0) {
                return j2;
            }
            j2 += (long) read;
            if (j2 > j) {
                break;
            }
            outputStream.write(bArr, 0, read);
        }
        throw new StreamOverflowException("Data Overflow");
    }

    public static byte[] readAll(InputStream inputStream) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAll(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] readAllLimited(InputStream inputStream, int i) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pipeAllLimited(inputStream, (long) i, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static int readFully(InputStream inputStream, byte[] bArr) {
        return readFully(inputStream, bArr, 0, bArr.length);
    }

    public static int readFully(InputStream inputStream, byte[] bArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            int read = inputStream.read(bArr, i + i3, i2 - i3);
            if (read < 0) {
                break;
            }
            i3 += read;
        }
        return i3;
    }
}
