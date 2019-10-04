/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.IllegalStateException
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.CertificateException
 *  java.security.cert.CertificateFactory
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.Iterator
 *  java.util.List
 *  java.util.NoSuchElementException
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.okhttp.internal.InternalCache;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.CacheRequest;
import com.squareup.okhttp.internal.http.CacheStrategy;
import com.squareup.okhttp.internal.http.HttpMethod;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.StatusLine;
import com.squareup.okhttp.internal.io.FileSystem;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Cache {
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 201105;
    private final DiskLruCache cache;
    private int hitCount;
    final InternalCache internalCache = new InternalCache(){

        @Override
        public Response get(Request request) {
            return Cache.this.get(request);
        }

        @Override
        public CacheRequest put(Response response) {
            return Cache.this.put(response);
        }

        @Override
        public void remove(Request request) {
            Cache.this.remove(request);
        }

        @Override
        public void trackConditionalCacheHit() {
            Cache.this.trackConditionalCacheHit();
        }

        @Override
        public void trackResponse(CacheStrategy cacheStrategy) {
            Cache.this.trackResponse(cacheStrategy);
        }

        @Override
        public void update(Response response, Response response2) {
            Cache.this.update(response, response2);
        }
    };
    private int networkCount;
    private int requestCount;
    private int writeAbortCount;
    private int writeSuccessCount;

    public Cache(File file, long l2) {
        this.cache = DiskLruCache.create(FileSystem.SYSTEM, file, 201105, 2, l2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void abortQuietly(DiskLruCache.Editor editor) {
        if (editor == null) return;
        try {
            editor.abort();
            return;
        }
        catch (IOException iOException) {
            return;
        }
    }

    static /* synthetic */ int access$808(Cache cache) {
        int n2 = cache.writeSuccessCount;
        cache.writeSuccessCount = n2 + 1;
        return n2;
    }

    static /* synthetic */ int access$908(Cache cache) {
        int n2 = cache.writeAbortCount;
        cache.writeAbortCount = n2 + 1;
        return n2;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private CacheRequest put(Response response) {
        String string = response.request().method();
        if (HttpMethod.invalidatesCache(response.request().method())) {
            this.remove(response.request());
            return null;
        }
        if (!string.equals((Object)"GET")) return null;
        if (OkHeaders.hasVaryAll(response)) return null;
        Entry entry = new Entry(response);
        DiskLruCache.Editor editor = this.cache.edit(Cache.urlToKey(response.request()));
        if (editor == null) return null;
        entry.writeTo(editor);
        return new CacheRequestImpl(editor);
        catch (IOException iOException) {
            DiskLruCache.Editor editor2;
            block7 : {
                editor2 = null;
                break block7;
                catch (IOException iOException2) {
                    editor2 = editor;
                }
            }
            this.abortQuietly(editor2);
            return null;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int readInt(BufferedSource bufferedSource) {
        String string;
        long l2;
        try {
            l2 = bufferedSource.readDecimalLong();
            string = bufferedSource.readUtf8LineStrict();
            if (l2 < 0L || l2 > Integer.MAX_VALUE) throw new IOException("expected an int but was \"" + l2 + string + "\"");
        }
        catch (NumberFormatException numberFormatException) {
            throw new IOException(numberFormatException.getMessage());
        }
        if (string.isEmpty()) return (int)l2;
        throw new IOException("expected an int but was \"" + l2 + string + "\"");
    }

    private void remove(Request request) {
        this.cache.remove(Cache.urlToKey(request));
    }

    private void trackConditionalCacheHit() {
        Cache cache = this;
        synchronized (cache) {
            this.hitCount = 1 + this.hitCount;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void trackResponse(CacheStrategy cacheStrategy) {
        Cache cache = this;
        synchronized (cache) {
            this.requestCount = 1 + this.requestCount;
            if (cacheStrategy.networkRequest != null) {
                this.networkCount = 1 + this.networkCount;
            } else if (cacheStrategy.cacheResponse != null) {
                this.hitCount = 1 + this.hitCount;
            }
            return;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void update(Response response, Response response2) {
        Entry entry = new Entry(response2);
        DiskLruCache.Snapshot snapshot = ((CacheResponseBody)response.body()).snapshot;
        DiskLruCache.Editor editor = null;
        try {
            editor = snapshot.edit();
            if (editor == null) return;
        }
        catch (IOException iOException) {
            this.abortQuietly(editor);
            return;
        }
        entry.writeTo(editor);
        editor.commit();
    }

    private static String urlToKey(Request request) {
        return Util.md5Hex(request.urlString());
    }

    public void close() {
        this.cache.close();
    }

    public void delete() {
        this.cache.delete();
    }

    public void evictAll() {
        this.cache.evictAll();
    }

    public void flush() {
        this.cache.flush();
    }

    Response get(Request request) {
        Response response;
        Entry entry;
        DiskLruCache.Snapshot snapshot;
        block5 : {
            String string = Cache.urlToKey(request);
            try {
                snapshot = this.cache.get(string);
                if (snapshot != null) break block5;
                return null;
            }
            catch (IOException iOException) {
                return null;
            }
        }
        try {
            entry = new Entry(snapshot.getSource(0));
        }
        catch (IOException iOException) {
            Util.closeQuietly(snapshot);
            return null;
        }
        response = entry.response(request, snapshot);
        if (!entry.matches(request, response)) {
            Util.closeQuietly(response.body());
            return null;
        }
        return response;
    }

    public File getDirectory() {
        return this.cache.getDirectory();
    }

    public int getHitCount() {
        Cache cache = this;
        synchronized (cache) {
            int n2 = this.hitCount;
            return n2;
        }
    }

    public long getMaxSize() {
        return this.cache.getMaxSize();
    }

    public int getNetworkCount() {
        Cache cache = this;
        synchronized (cache) {
            int n2 = this.networkCount;
            return n2;
        }
    }

    public int getRequestCount() {
        Cache cache = this;
        synchronized (cache) {
            int n2 = this.requestCount;
            return n2;
        }
    }

    public long getSize() {
        return this.cache.size();
    }

    public int getWriteAbortCount() {
        Cache cache = this;
        synchronized (cache) {
            int n2 = this.writeAbortCount;
            return n2;
        }
    }

    public int getWriteSuccessCount() {
        Cache cache = this;
        synchronized (cache) {
            int n2 = this.writeSuccessCount;
            return n2;
        }
    }

    public boolean isClosed() {
        return this.cache.isClosed();
    }

    public Iterator<String> urls() {
        return new Iterator<String>(){
            boolean canRemove;
            final Iterator<DiskLruCache.Snapshot> delegate;
            String nextUrl;
            {
                this.delegate = Cache.this.cache.snapshots();
            }

            public boolean hasNext() {
                if (this.nextUrl != null) {
                    return true;
                }
                this.canRemove = false;
                while (this.delegate.hasNext()) {
                    DiskLruCache.Snapshot snapshot = (DiskLruCache.Snapshot)this.delegate.next();
                    try {
                        this.nextUrl = Okio.buffer(snapshot.getSource(0)).readUtf8LineStrict();
                        return true;
                    }
                    catch (IOException iOException) {}
                    continue;
                    finally {
                        snapshot.close();
                    }
                }
                return false;
            }

            public String next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                String string = this.nextUrl;
                this.nextUrl = null;
                this.canRemove = true;
                return string;
            }

            public void remove() {
                if (!this.canRemove) {
                    throw new IllegalStateException("remove() before next()");
                }
                this.delegate.remove();
            }
        };
    }

    private final class CacheRequestImpl
    implements CacheRequest {
        private Sink body;
        private Sink cacheOut;
        private boolean done;
        private final DiskLruCache.Editor editor;

        public CacheRequestImpl(final DiskLruCache.Editor editor) {
            this.editor = editor;
            this.cacheOut = editor.newSink(1);
            this.body = new ForwardingSink(this.cacheOut){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void close() {
                    Cache cache;
                    Cache cache2 = cache = Cache.this;
                    synchronized (cache2) {
                        if (CacheRequestImpl.this.done) {
                            return;
                        }
                        CacheRequestImpl.this.done = true;
                        Cache.access$808(Cache.this);
                    }
                    super.close();
                    editor.commit();
                }
            };
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void abort() {
            Cache cache;
            Cache cache2 = cache = Cache.this;
            synchronized (cache2) {
                if (this.done) {
                    return;
                }
                this.done = true;
                Cache.access$908(Cache.this);
            }
            Util.closeQuietly(this.cacheOut);
            try {
                this.editor.abort();
                return;
            }
            catch (IOException iOException) {
                return;
            }
        }

        @Override
        public Sink body() {
            return this.body;
        }

    }

    private static class CacheResponseBody
    extends ResponseBody {
        private final BufferedSource bodySource;
        private final String contentLength;
        private final String contentType;
        private final DiskLruCache.Snapshot snapshot;

        public CacheResponseBody(final DiskLruCache.Snapshot snapshot, String string, String string2) {
            this.snapshot = snapshot;
            this.contentType = string;
            this.contentLength = string2;
            this.bodySource = Okio.buffer(new ForwardingSource(snapshot.getSource(1)){

                @Override
                public void close() {
                    snapshot.close();
                    super.close();
                }
            });
        }

        @Override
        public long contentLength() {
            long l2 = -1L;
            try {
                if (this.contentLength != null) {
                    long l3;
                    l2 = l3 = Long.parseLong((String)this.contentLength);
                }
                return l2;
            }
            catch (NumberFormatException numberFormatException) {
                return l2;
            }
        }

        @Override
        public MediaType contentType() {
            if (this.contentType != null) {
                return MediaType.parse(this.contentType);
            }
            return null;
        }

        @Override
        public BufferedSource source() {
            return this.bodySource;
        }

    }

    private static final class Entry {
        private final int code;
        private final Handshake handshake;
        private final String message;
        private final Protocol protocol;
        private final String requestMethod;
        private final Headers responseHeaders;
        private final String url;
        private final Headers varyHeaders;

        public Entry(Response response) {
            this.url = response.request().urlString();
            this.varyHeaders = OkHeaders.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.responseHeaders = response.headers();
            this.handshake = response.handshake();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public Entry(Source source) {
            int n2 = 0;
            BufferedSource bufferedSource = Okio.buffer(source);
            this.url = bufferedSource.readUtf8LineStrict();
            this.requestMethod = bufferedSource.readUtf8LineStrict();
            Headers.Builder builder = new Headers.Builder();
            int n3 = Cache.readInt(bufferedSource);
            for (int i2 = 0; i2 < n3; ++i2) {
                builder.addLenient(bufferedSource.readUtf8LineStrict());
            }
            this.varyHeaders = builder.build();
            StatusLine statusLine = StatusLine.parse(bufferedSource.readUtf8LineStrict());
            this.protocol = statusLine.protocol;
            this.code = statusLine.code;
            this.message = statusLine.message;
            Headers.Builder builder2 = new Headers.Builder();
            int n4 = Cache.readInt(bufferedSource);
            while (n2 < n4) {
                builder2.addLenient(bufferedSource.readUtf8LineStrict());
                ++n2;
            }
            try {
                this.responseHeaders = builder2.build();
                if (this.isHttps()) {
                    String string = bufferedSource.readUtf8LineStrict();
                    if (string.length() > 0) {
                        throw new IOException("expected \"\" but was \"" + string + "\"");
                    }
                    this.handshake = Handshake.get(bufferedSource.readUtf8LineStrict(), this.readCertificateList(bufferedSource), this.readCertificateList(bufferedSource));
                    return;
                }
                this.handshake = null;
                return;
            }
            finally {
                source.close();
            }
        }

        private boolean isHttps() {
            return this.url.startsWith("https://");
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private List<Certificate> readCertificateList(BufferedSource bufferedSource) {
            CertificateFactory certificateFactory;
            int n2;
            List list;
            int n3 = Cache.readInt(bufferedSource);
            if (n3 == -1) {
                return Collections.emptyList();
            }
            try {
                certificateFactory = CertificateFactory.getInstance((String)"X.509");
                list = new ArrayList(n3);
                n2 = 0;
            }
            catch (CertificateException certificateException) {
                throw new IOException(certificateException.getMessage());
            }
            while (n2 < n3) {
                String string = bufferedSource.readUtf8LineStrict();
                Buffer buffer = new Buffer();
                buffer.write(ByteString.decodeBase64(string));
                list.add((Object)certificateFactory.generateCertificate(buffer.inputStream()));
                ++n2;
            }
            return list;
        }

        private void writeCertList(BufferedSink bufferedSink, List<Certificate> list) {
            bufferedSink.writeDecimalLong(list.size());
            bufferedSink.writeByte(10);
            int n2 = list.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                try {
                    bufferedSink.writeUtf8(ByteString.of(((Certificate)list.get(i2)).getEncoded()).base64());
                    bufferedSink.writeByte(10);
                }
                catch (CertificateEncodingException certificateEncodingException) {
                    throw new IOException(certificateEncodingException.getMessage());
                }
            }
        }

        public boolean matches(Request request, Response response) {
            return this.url.equals((Object)request.urlString()) && this.requestMethod.equals((Object)request.method()) && OkHeaders.varyMatches(response, this.varyHeaders, request);
        }

        public Response response(Request request, DiskLruCache.Snapshot snapshot) {
            String string = this.responseHeaders.get("Content-Type");
            String string2 = this.responseHeaders.get("Content-Length");
            Request request2 = new Request.Builder().url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build();
            return new Response.Builder().request(request2).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new CacheResponseBody(snapshot, string, string2)).handshake(this.handshake).build();
        }

        public void writeTo(DiskLruCache.Editor editor) {
            int n2 = 0;
            BufferedSink bufferedSink = Okio.buffer(editor.newSink(0));
            bufferedSink.writeUtf8(this.url);
            bufferedSink.writeByte(10);
            bufferedSink.writeUtf8(this.requestMethod);
            bufferedSink.writeByte(10);
            bufferedSink.writeDecimalLong(this.varyHeaders.size());
            bufferedSink.writeByte(10);
            int n3 = this.varyHeaders.size();
            for (int i2 = 0; i2 < n3; ++i2) {
                bufferedSink.writeUtf8(this.varyHeaders.name(i2));
                bufferedSink.writeUtf8(": ");
                bufferedSink.writeUtf8(this.varyHeaders.value(i2));
                bufferedSink.writeByte(10);
            }
            bufferedSink.writeUtf8(new StatusLine(this.protocol, this.code, this.message).toString());
            bufferedSink.writeByte(10);
            bufferedSink.writeDecimalLong(this.responseHeaders.size());
            bufferedSink.writeByte(10);
            int n4 = this.responseHeaders.size();
            while (n2 < n4) {
                bufferedSink.writeUtf8(this.responseHeaders.name(n2));
                bufferedSink.writeUtf8(": ");
                bufferedSink.writeUtf8(this.responseHeaders.value(n2));
                bufferedSink.writeByte(10);
                ++n2;
            }
            if (this.isHttps()) {
                bufferedSink.writeByte(10);
                bufferedSink.writeUtf8(this.handshake.cipherSuite());
                bufferedSink.writeByte(10);
                this.writeCertList(bufferedSink, this.handshake.peerCertificates());
                this.writeCertList(bufferedSink, this.handshake.localCertificates());
            }
            bufferedSink.close();
        }
    }

}

