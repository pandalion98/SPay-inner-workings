/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.charset.Charset
 */
package okio;

import java.io.OutputStream;
import java.nio.charset.Charset;
import okio.Buffer;
import okio.ByteString;
import okio.Sink;
import okio.Source;

public interface BufferedSink
extends Sink {
    public Buffer buffer();

    public BufferedSink emit();

    public BufferedSink emitCompleteSegments();

    public OutputStream outputStream();

    public BufferedSink write(ByteString var1);

    public BufferedSink write(Source var1, long var2);

    public BufferedSink write(byte[] var1);

    public BufferedSink write(byte[] var1, int var2, int var3);

    public long writeAll(Source var1);

    public BufferedSink writeByte(int var1);

    public BufferedSink writeDecimalLong(long var1);

    public BufferedSink writeHexadecimalUnsignedLong(long var1);

    public BufferedSink writeInt(int var1);

    public BufferedSink writeIntLe(int var1);

    public BufferedSink writeLong(long var1);

    public BufferedSink writeLongLe(long var1);

    public BufferedSink writeShort(int var1);

    public BufferedSink writeShortLe(int var1);

    public BufferedSink writeString(String var1, int var2, int var3, Charset var4);

    public BufferedSink writeString(String var1, Charset var2);

    public BufferedSink writeUtf8(String var1);

    public BufferedSink writeUtf8(String var1, int var2, int var3);

    public BufferedSink writeUtf8CodePoint(int var1);
}

