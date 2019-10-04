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
import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Encoder;
import org.bouncycastle.util.encoders.EncoderException;
import org.bouncycastle.util.encoders.HexEncoder;

public class Hex {
    private static final Encoder encoder = new HexEncoder();

    public static int decode(String string, OutputStream outputStream) {
        return encoder.decode(string, outputStream);
    }

    public static byte[] decode(String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.decode(string, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new DecoderException("exception decoding Hex string: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decode(byte[] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.decode(arrby, 0, arrby.length, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new DecoderException("exception decoding Hex data: " + exception.getMessage(), exception);
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
        return Hex.encode(arrby, 0, arrby.length);
    }

    public static byte[] encode(byte[] arrby, int n, int n2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.encode(arrby, n, n2, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new EncoderException("exception encoding Hex string: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String toHexString(byte[] arrby) {
        return Hex.toHexString(arrby, 0, arrby.length);
    }

    public static String toHexString(byte[] arrby, int n, int n2) {
        return Strings.fromByteArray(Hex.encode(arrby, n, n2));
    }
}

