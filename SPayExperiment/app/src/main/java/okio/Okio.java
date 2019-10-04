/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.FileInputStream
 *  java.io.FileOutputStream
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.Socket
 *  java.nio.file.Files
 *  java.nio.file.OpenOption
 *  java.nio.file.Path
 *  java.util.logging.Level
 *  java.util.logging.Logger
 *  org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 */
package okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.RealBufferedSink;
import okio.RealBufferedSource;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class Okio {
    private static final Logger logger = Logger.getLogger((String)Okio.class.getName());

    private Okio() {
    }

    public static Sink appendingSink(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return Okio.sink((OutputStream)new FileOutputStream(file, true));
    }

    public static BufferedSink buffer(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        return new RealBufferedSink(sink);
    }

    public static BufferedSource buffer(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        return new RealBufferedSource(source);
    }

    public static Sink sink(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return Okio.sink((OutputStream)new FileOutputStream(file));
    }

    public static Sink sink(OutputStream outputStream) {
        return Okio.sink(outputStream, new Timeout());
    }

    private static Sink sink(final OutputStream outputStream, final Timeout timeout) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Sink(){

            @Override
            public void close() {
                outputStream.close();
            }

            @Override
            public void flush() {
                outputStream.flush();
            }

            @Override
            public Timeout timeout() {
                return timeout;
            }

            public String toString() {
                return "sink(" + (Object)outputStream + ")";
            }

            @Override
            public void write(Buffer buffer, long l2) {
                Util.checkOffsetAndCount(buffer.size, 0L, l2);
                while (l2 > 0L) {
                    timeout.throwIfReached();
                    Segment segment = buffer.head;
                    int n2 = (int)Math.min((long)l2, (long)(segment.limit - segment.pos));
                    outputStream.write(segment.data, segment.pos, n2);
                    segment.pos = n2 + segment.pos;
                    l2 -= (long)n2;
                    buffer.size -= (long)n2;
                    if (segment.pos != segment.limit) continue;
                    buffer.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
            }
        };
    }

    public static Sink sink(Socket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        AsyncTimeout asyncTimeout = Okio.timeout(socket);
        return asyncTimeout.sink(Okio.sink(socket.getOutputStream(), asyncTimeout));
    }

    @IgnoreJRERequirement
    public static /* varargs */ Sink sink(Path path, OpenOption ... arropenOption) {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return Okio.sink(Files.newOutputStream((Path)path, (OpenOption[])arropenOption));
    }

    public static Source source(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return Okio.source((InputStream)new FileInputStream(file));
    }

    public static Source source(InputStream inputStream) {
        return Okio.source(inputStream, new Timeout());
    }

    private static Source source(final InputStream inputStream, final Timeout timeout) {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Source(){

            @Override
            public void close() {
                inputStream.close();
            }

            @Override
            public long read(Buffer buffer, long l2) {
                if (l2 < 0L) {
                    throw new IllegalArgumentException("byteCount < 0: " + l2);
                }
                if (l2 == 0L) {
                    return 0L;
                }
                timeout.throwIfReached();
                Segment segment = buffer.writableSegment(1);
                int n2 = (int)Math.min((long)l2, (long)(2048 - segment.limit));
                int n3 = inputStream.read(segment.data, segment.limit, n2);
                if (n3 == -1) {
                    return -1L;
                }
                segment.limit = n3 + segment.limit;
                buffer.size += (long)n3;
                return n3;
            }

            @Override
            public Timeout timeout() {
                return timeout;
            }

            public String toString() {
                return "source(" + (Object)inputStream + ")";
            }
        };
    }

    public static Source source(Socket socket) {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        AsyncTimeout asyncTimeout = Okio.timeout(socket);
        return asyncTimeout.source(Okio.source(socket.getInputStream(), asyncTimeout));
    }

    @IgnoreJRERequirement
    public static /* varargs */ Source source(Path path, OpenOption ... arropenOption) {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return Okio.source(Files.newInputStream((Path)path, (OpenOption[])arropenOption));
    }

    private static AsyncTimeout timeout(final Socket socket) {
        return new AsyncTimeout(){

            @Override
            protected void timedOut() {
                try {
                    socket.close();
                    return;
                }
                catch (Exception exception) {
                    logger.log(Level.WARNING, "Failed to close timed out socket " + (Object)socket, (Throwable)exception);
                    return;
                }
            }
        };
    }

}

