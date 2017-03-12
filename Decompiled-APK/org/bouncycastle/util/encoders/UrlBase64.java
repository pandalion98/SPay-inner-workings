package org.bouncycastle.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class UrlBase64 {
    private static final Encoder encoder;

    static {
        encoder = new UrlBase64Encoder();
    }

    public static int decode(String str, OutputStream outputStream) {
        return encoder.decode(str, outputStream);
    }

    public static int decode(byte[] bArr, OutputStream outputStream) {
        return encoder.decode(bArr, 0, bArr.length, outputStream);
    }

    public static byte[] decode(String str) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.decode(str, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            throw new DecoderException("exception decoding URL safe base64 string: " + e.getMessage(), e);
        }
    }

    public static byte[] decode(byte[] bArr) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.decode(bArr, 0, bArr.length, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            throw new DecoderException("exception decoding URL safe base64 string: " + e.getMessage(), e);
        }
    }

    public static int encode(byte[] bArr, OutputStream outputStream) {
        return encoder.encode(bArr, 0, bArr.length, outputStream);
    }

    public static byte[] encode(byte[] bArr) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.encode(bArr, 0, bArr.length, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable e) {
            throw new EncoderException("exception encoding URL safe base64 data: " + e.getMessage(), e);
        }
    }
}
