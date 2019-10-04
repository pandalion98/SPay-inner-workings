/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package org.bouncycastle.util.encoders;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Encoder;
import org.bouncycastle.util.encoders.EncoderException;

public class Base64 {
    private static final Encoder encoder = new Base64Encoder();

    public static int decode(String string, OutputStream outputStream) {
        return encoder.decode(string, outputStream);
    }

    public static byte[] decode(String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(3 * (string.length() / 4));
        try {
            encoder.decode(string, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new DecoderException("unable to decode base64 string: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decode(byte[] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(3 * (arrby.length / 4));
        try {
            encoder.decode(arrby, 0, arrby.length, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new DecoderException("unable to decode base64 data: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static int encode(byte[] arrby, int n, int n2, OutputStream outputStream) {
        return encoder.encode(arrby, n, n2, outputStream);
    }

    public static int encode(byte[] arrby, OutputStream outputStream) {
        return encoder.encode(arrby, 0, arrby.length, outputStream);
    }

    public static byte[] encode(byte[] arrby) {
        return Base64.encode(arrby, 0, arrby.length);
    }

    public static byte[] encode(byte[] arrby, int n, int n2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4 * ((n2 + 2) / 3));
        try {
            encoder.encode(arrby, n, n2, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new EncoderException("exception encoding base64 string: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String toBase64String(byte[] arrby) {
        return Base64.toBase64String(arrby, 0, arrby.length);
    }

    public static String toBase64String(byte[] arrby, int n, int n2) {
        return Strings.fromByteArray(Base64.encode(arrby, n, n2));
    }
}

