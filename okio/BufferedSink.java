package okio;

import java.io.OutputStream;
import java.nio.charset.Charset;

public interface BufferedSink extends Sink {
    Buffer buffer();

    BufferedSink emit();

    BufferedSink emitCompleteSegments();

    OutputStream outputStream();

    BufferedSink write(ByteString byteString);

    BufferedSink write(Source source, long j);

    BufferedSink write(byte[] bArr);

    BufferedSink write(byte[] bArr, int i, int i2);

    long writeAll(Source source);

    BufferedSink writeByte(int i);

    BufferedSink writeDecimalLong(long j);

    BufferedSink writeHexadecimalUnsignedLong(long j);

    BufferedSink writeInt(int i);

    BufferedSink writeIntLe(int i);

    BufferedSink writeLong(long j);

    BufferedSink writeLongLe(long j);

    BufferedSink writeShort(int i);

    BufferedSink writeShortLe(int i);

    BufferedSink writeString(String str, int i, int i2, Charset charset);

    BufferedSink writeString(String str, Charset charset);

    BufferedSink writeUtf8(String str);

    BufferedSink writeUtf8(String str, int i, int i2);

    BufferedSink writeUtf8CodePoint(int i);
}
