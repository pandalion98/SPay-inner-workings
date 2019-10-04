/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InterruptedIOException
 *  java.io.UnsupportedEncodingException
 *  java.lang.ArrayIndexOutOfBoundsException
 *  java.lang.AssertionError
 *  java.lang.Class
 *  java.lang.Error
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.lang.reflect.Array
 *  java.net.ServerSocket
 *  java.net.Socket
 *  java.net.URI
 *  java.net.URL
 *  java.nio.charset.Charset
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.ThreadFactory
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

public final class Util {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final Charset UTF_8 = Charset.forName((String)"UTF-8");

    private Util() {
    }

    public static void checkOffsetAndCount(long l2, long l3, long l4) {
        if ((l3 | l4) < 0L || l3 > l2 || l2 - l3 < l4) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void closeAll(Closeable closeable, Closeable closeable2) {
        Throwable throwable;
        block9 : {
            throwable = null;
            try {
                closeable.close();
            }
            catch (Throwable throwable2) {}
            try {
                closeable2.close();
            }
            catch (Throwable throwable3) {
                if (throwable != null) break block9;
                throwable = throwable3;
            }
        }
        if (throwable == null) {
            return;
        }
        if (throwable instanceof IOException) {
            throw (IOException)throwable;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException)throwable;
        }
        if (throwable instanceof Error) {
            throw (Error)throwable;
        }
        throw new AssertionError((Object)throwable);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
            return;
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket == null) return;
        try {
            serverSocket.close();
            return;
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void closeQuietly(Socket socket) {
        if (socket == null) return;
        try {
            socket.close();
            return;
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            return;
        }
    }

    public static boolean discard(Source source, int n2, TimeUnit timeUnit) {
        try {
            boolean bl = Util.skipAll(source, n2, timeUnit);
            return bl;
        }
        catch (IOException iOException) {
            return false;
        }
    }

    public static boolean equal(Object object, Object object2) {
        return object == object2 || object != null && object.equals(object2);
    }

    public static int getDefaultPort(String string) {
        if ("http".equals((Object)string)) {
            return 80;
        }
        if ("https".equals((Object)string)) {
            return 443;
        }
        return -1;
    }

    private static int getEffectivePort(String string, int n2) {
        if (n2 != -1) {
            return n2;
        }
        return Util.getDefaultPort(string);
    }

    public static int getEffectivePort(URI uRI) {
        return Util.getEffectivePort(uRI.getScheme(), uRI.getPort());
    }

    public static int getEffectivePort(URL uRL) {
        return Util.getEffectivePort(uRL.getProtocol(), uRL.getPort());
    }

    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList((List)new ArrayList(list));
    }

    public static /* varargs */ <T> List<T> immutableList(T ... arrT) {
        return Collections.unmodifiableList((List)Arrays.asList((Object[])((Object[])arrT.clone())));
    }

    public static <K, V> Map<K, V> immutableMap(Map<K, V> map) {
        return Collections.unmodifiableMap((Map)new LinkedHashMap(map));
    }

    private static <T> List<T> intersect(T[] arrT, T[] arrT2) {
        ArrayList arrayList = new ArrayList();
        block0 : for (T t2 : arrT) {
            int n2 = arrT2.length;
            int n3 = 0;
            do {
                if (n3 >= n2) continue block0;
                T t3 = arrT2[n3];
                if (t2.equals(t3)) {
                    arrayList.add(t3);
                    continue block0;
                }
                ++n3;
            } while (true);
        }
        return arrayList;
    }

    public static <T> T[] intersect(Class<T> class_, T[] arrT, T[] arrT2) {
        List<T> list = Util.intersect(arrT, arrT2);
        return list.toArray((Object[])Array.newInstance(class_, (int)list.size()));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String md5Hex(String string) {
        void var1_3;
        try {
            return ByteString.of(MessageDigest.getInstance((String)"MD5").digest(string.getBytes("UTF-8"))).hex();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            do {
                throw new AssertionError((Object)var1_3);
                break;
            } while (true);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)var1_3);
        }
    }

    public static ByteString sha1(ByteString byteString) {
        try {
            ByteString byteString2 = ByteString.of(MessageDigest.getInstance((String)"SHA-1").digest(byteString.toByteArray()));
            return byteString2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new AssertionError((Object)noSuchAlgorithmException);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String shaBase64(String string) {
        void var1_3;
        try {
            return ByteString.of(MessageDigest.getInstance((String)"SHA-1").digest(string.getBytes("UTF-8"))).base64();
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            do {
                throw new AssertionError((Object)var1_3);
                break;
            } while (true);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError((Object)var1_3);
        }
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static boolean skipAll(Source source, int n2, TimeUnit timeUnit) {
        long l2;
        long l3;
        block7 : {
            l3 = System.nanoTime();
            l2 = source.timeout().hasDeadline() ? source.timeout().deadlineNanoTime() - l3 : Long.MAX_VALUE;
            source.timeout().deadlineNanoTime(l3 + Math.min((long)l2, (long)timeUnit.toNanos((long)n2)));
            try {
                Buffer buffer = new Buffer();
                while (source.read(buffer, 2048L) != -1L) {
                    buffer.clear();
                }
            }
            catch (InterruptedIOException interruptedIOException) {
                if (l2 == Long.MAX_VALUE) {
                    source.timeout().clearDeadline();
                    return false;
                }
                break block7;
            }
            if (l2 == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
                return true;
            }
            source.timeout().deadlineNanoTime(l2 + l3);
            return true;
        }
        source.timeout().deadlineNanoTime(l2 + l3);
        return false;
        catch (Throwable throwable) {
            if (l2 == Long.MAX_VALUE) {
                source.timeout().clearDeadline();
                throw throwable;
            }
            source.timeout().deadlineNanoTime(l2 + l3);
            throw throwable;
        }
    }

    public static ThreadFactory threadFactory(final String string, final boolean bl) {
        return new ThreadFactory(){

            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, string);
                thread.setDaemon(bl);
                return thread;
            }
        };
    }

}

