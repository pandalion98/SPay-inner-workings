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
import org.bouncycastle.util.encoders.DecoderException;
import org.bouncycastle.util.encoders.Encoder;
import org.bouncycastle.util.encoders.EncoderException;
import org.bouncycastle.util.encoders.UrlBase64Encoder;

public class UrlBase64 {
    private static final Encoder encoder = new UrlBase64Encoder();

    public static int decode(String string, OutputStream outputStream) {
        return encoder.decode(string, outputStream);
    }

    public static int decode(byte[] arrby, OutputStream outputStream) {
        return encoder.decode(arrby, 0, arrby.length, outputStream);
    }

    public static byte[] decode(String string) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.decode(string, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new DecoderException("exception decoding URL safe base64 string: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decode(byte[] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.decode(arrby, 0, arrby.length, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new DecoderException("exception decoding URL safe base64 string: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static int encode(byte[] arrby, OutputStream outputStream) {
        return encoder.encode(arrby, 0, arrby.length, outputStream);
    }

    public static byte[] encode(byte[] arrby) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encoder.encode(arrby, 0, arrby.length, (OutputStream)byteArrayOutputStream);
        }
        catch (Exception exception) {
            throw new EncoderException("exception encoding URL safe base64 data: " + exception.getMessage(), exception);
        }
        return byteArrayOutputStream.toByteArray();
    }
}

