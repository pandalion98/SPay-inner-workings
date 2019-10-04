/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.charset.Charset
 */
package okio;

import java.io.InputStream;
import java.nio.charset.Charset;
import okio.Buffer;
import okio.ByteString;
import okio.Sink;
import okio.Source;

public interface BufferedSource
extends Source {
    public Buffer buffer();

    public boolean exhausted();

    public long indexOf(byte var1);

    public long indexOf(byte var1, long var2);

    public long indexOfElement(ByteString var1);

    public long indexOfElement(ByteString var1, long var2);

    public InputStream inputStream();

    public int read(byte[] var1);

    public int read(byte[] var1, int var2, int var3);

    public long readAll(Sink var1);

    public byte readByte();

    public byte[] readByteArray();

    public byte[] readByteArray(long var1);

    public ByteString readByteString();

    public ByteString readByteString(long var1);

    public long readDecimalLong();

    public void readFully(Buffer var1, long var2);

    public void readFully(byte[] var1);

    public long readHexadecimalUnsignedLong();

    public int readInt();

    public int readIntLe();

    public long readLong();

    public long readLongLe();

    public short readShort();

    public short readShortLe();

    public String readString(long var1, Charset var3);

    public String readString(Charset var1);

    public String readUtf8();

    public String readUtf8(long var1);

    public int readUtf8CodePoint();

    public String readUtf8Line();

    public String readUtf8LineStrict();

    public boolean request(long var1);

    public void require(long var1);

    public void skip(long var1);
}

