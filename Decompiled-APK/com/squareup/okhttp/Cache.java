package com.squareup.okhttp;

import com.squareup.okhttp.Headers.Builder;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.okhttp.internal.DiskLruCache.Editor;
import com.squareup.okhttp.internal.DiskLruCache.Snapshot;
import com.squareup.okhttp.internal.InternalCache;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.CacheRequest;
import com.squareup.okhttp.internal.http.CacheStrategy;
import com.squareup.okhttp.internal.http.HttpMethod;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.StatusLine;
import com.squareup.okhttp.internal.io.FileSystem;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
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
    final InternalCache internalCache;
    private int networkCount;
    private int requestCount;
    private int writeAbortCount;
    private int writeSuccessCount;

    /* renamed from: com.squareup.okhttp.Cache.1 */
    class C06211 implements InternalCache {
        C06211() {
        }

        public Response get(Request request) {
            return Cache.this.get(request);
        }

        public CacheRequest put(Response response) {
            return Cache.this.put(response);
        }

        public void remove(Request request) {
            Cache.this.remove(request);
        }

        public void update(Response response, Response response2) {
            Cache.this.update(response, response2);
        }

        public void trackConditionalCacheHit() {
            Cache.this.trackConditionalCacheHit();
        }

        public void trackResponse(CacheStrategy cacheStrategy) {
            Cache.this.trackResponse(cacheStrategy);
        }
    }

    /* renamed from: com.squareup.okhttp.Cache.2 */
    class C06222 implements Iterator<String> {
        boolean canRemove;
        final Iterator<Snapshot> delegate;
        String nextUrl;

        C06222() {
            this.delegate = Cache.this.cache.snapshots();
        }

        public boolean hasNext() {
            if (this.nextUrl != null) {
                return true;
            }
            this.canRemove = false;
            while (this.delegate.hasNext()) {
                Snapshot snapshot = (Snapshot) this.delegate.next();
                try {
                    this.nextUrl = Okio.buffer(snapshot.getSource(Cache.ENTRY_METADATA)).readUtf8LineStrict();
                    snapshot.close();
                    return true;
                } catch (IOException e) {
                    snapshot.close();
                } catch (Throwable th) {
                    snapshot.close();
                }
            }
            return false;
        }

        public String next() {
            if (hasNext()) {
                String str = this.nextUrl;
                this.nextUrl = null;
                this.canRemove = true;
                return str;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.canRemove) {
                this.delegate.remove();
                return;
            }
            throw new IllegalStateException("remove() before next()");
        }
    }

    private final class CacheRequestImpl implements CacheRequest {
        private Sink body;
        private Sink cacheOut;
        private boolean done;
        private final Editor editor;

        /* renamed from: com.squareup.okhttp.Cache.CacheRequestImpl.1 */
        class C06231 extends ForwardingSink {
            final /* synthetic */ Editor val$editor;
            final /* synthetic */ Cache val$this$0;

            C06231(Sink sink, Cache cache, Editor editor) {
                this.val$this$0 = cache;
                this.val$editor = editor;
                super(sink);
            }

            public void close() {
                synchronized (Cache.this) {
                    if (CacheRequestImpl.this.done) {
                        return;
                    }
                    CacheRequestImpl.this.done = true;
                    Cache.this.writeSuccessCount = Cache.this.writeSuccessCount + Cache.ENTRY_BODY;
                    super.close();
                    this.val$editor.commit();
                }
            }
        }

        public CacheRequestImpl(Editor editor) {
            this.editor = editor;
            this.cacheOut = editor.newSink(Cache.ENTRY_BODY);
            this.body = new C06231(this.cacheOut, Cache.this, editor);
        }

        public void abort() {
            synchronized (Cache.this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                Cache.this.writeAbortCount = Cache.this.writeAbortCount + Cache.ENTRY_BODY;
                Util.closeQuietly(this.cacheOut);
                try {
                    this.editor.abort();
                } catch (IOException e) {
                }
            }
        }

        public Sink body() {
            return this.body;
        }
    }

    private static class CacheResponseBody extends ResponseBody {
        private final BufferedSource bodySource;
        private final String contentLength;
        private final String contentType;
        private final Snapshot snapshot;

        /* renamed from: com.squareup.okhttp.Cache.CacheResponseBody.1 */
        class C06241 extends ForwardingSource {
            final /* synthetic */ Snapshot val$snapshot;

            C06241(Source source, Snapshot snapshot) {
                this.val$snapshot = snapshot;
                super(source);
            }

            public void close() {
                this.val$snapshot.close();
                super.close();
            }
        }

        public CacheResponseBody(Snapshot snapshot, String str, String str2) {
            this.snapshot = snapshot;
            this.contentType = str;
            this.contentLength = str2;
            this.bodySource = Okio.buffer(new C06241(snapshot.getSource(Cache.ENTRY_BODY), snapshot));
        }

        public MediaType contentType() {
            return this.contentType != null ? MediaType.parse(this.contentType) : null;
        }

        public long contentLength() {
            long j = -1;
            try {
                if (this.contentLength != null) {
                    j = Long.parseLong(this.contentLength);
                }
            } catch (NumberFormatException e) {
            }
            return j;
        }

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

        public Entry(Source source) {
            int i = Cache.ENTRY_METADATA;
            try {
                BufferedSource buffer = Okio.buffer(source);
                this.url = buffer.readUtf8LineStrict();
                this.requestMethod = buffer.readUtf8LineStrict();
                Builder builder = new Builder();
                int access$1000 = Cache.readInt(buffer);
                for (int i2 = Cache.ENTRY_METADATA; i2 < access$1000; i2 += Cache.ENTRY_BODY) {
                    builder.addLenient(buffer.readUtf8LineStrict());
                }
                this.varyHeaders = builder.build();
                StatusLine parse = StatusLine.parse(buffer.readUtf8LineStrict());
                this.protocol = parse.protocol;
                this.code = parse.code;
                this.message = parse.message;
                Builder builder2 = new Builder();
                int access$10002 = Cache.readInt(buffer);
                while (i < access$10002) {
                    builder2.addLenient(buffer.readUtf8LineStrict());
                    i += Cache.ENTRY_BODY;
                }
                this.responseHeaders = builder2.build();
                if (isHttps()) {
                    String readUtf8LineStrict = buffer.readUtf8LineStrict();
                    if (readUtf8LineStrict.length() > 0) {
                        throw new IOException("expected \"\" but was \"" + readUtf8LineStrict + "\"");
                    }
                    this.handshake = Handshake.get(buffer.readUtf8LineStrict(), readCertificateList(buffer), readCertificateList(buffer));
                } else {
                    this.handshake = null;
                }
                source.close();
            } catch (Throwable th) {
                source.close();
            }
        }

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

        public void writeTo(Editor editor) {
            int i;
            int i2 = Cache.ENTRY_METADATA;
            BufferedSink buffer = Okio.buffer(editor.newSink(Cache.ENTRY_METADATA));
            buffer.writeUtf8(this.url);
            buffer.writeByte(10);
            buffer.writeUtf8(this.requestMethod);
            buffer.writeByte(10);
            buffer.writeDecimalLong((long) this.varyHeaders.size());
            buffer.writeByte(10);
            int size = this.varyHeaders.size();
            for (i = Cache.ENTRY_METADATA; i < size; i += Cache.ENTRY_BODY) {
                buffer.writeUtf8(this.varyHeaders.name(i));
                buffer.writeUtf8(": ");
                buffer.writeUtf8(this.varyHeaders.value(i));
                buffer.writeByte(10);
            }
            buffer.writeUtf8(new StatusLine(this.protocol, this.code, this.message).toString());
            buffer.writeByte(10);
            buffer.writeDecimalLong((long) this.responseHeaders.size());
            buffer.writeByte(10);
            i = this.responseHeaders.size();
            while (i2 < i) {
                buffer.writeUtf8(this.responseHeaders.name(i2));
                buffer.writeUtf8(": ");
                buffer.writeUtf8(this.responseHeaders.value(i2));
                buffer.writeByte(10);
                i2 += Cache.ENTRY_BODY;
            }
            if (isHttps()) {
                buffer.writeByte(10);
                buffer.writeUtf8(this.handshake.cipherSuite());
                buffer.writeByte(10);
                writeCertList(buffer, this.handshake.peerCertificates());
                writeCertList(buffer, this.handshake.localCertificates());
            }
            buffer.close();
        }

        private boolean isHttps() {
            return this.url.startsWith("https://");
        }

        private List<Certificate> readCertificateList(BufferedSource bufferedSource) {
            int access$1000 = Cache.readInt(bufferedSource);
            if (access$1000 == -1) {
                return Collections.emptyList();
            }
            try {
                CertificateFactory instance = CertificateFactory.getInstance("X.509");
                List<Certificate> arrayList = new ArrayList(access$1000);
                for (int i = Cache.ENTRY_METADATA; i < access$1000; i += Cache.ENTRY_BODY) {
                    String readUtf8LineStrict = bufferedSource.readUtf8LineStrict();
                    Buffer buffer = new Buffer();
                    buffer.write(ByteString.decodeBase64(readUtf8LineStrict));
                    arrayList.add(instance.generateCertificate(buffer.inputStream()));
                }
                return arrayList;
            } catch (CertificateException e) {
                throw new IOException(e.getMessage());
            }
        }

        private void writeCertList(BufferedSink bufferedSink, List<Certificate> list) {
            try {
                bufferedSink.writeDecimalLong((long) list.size());
                bufferedSink.writeByte(10);
                int size = list.size();
                for (int i = Cache.ENTRY_METADATA; i < size; i += Cache.ENTRY_BODY) {
                    bufferedSink.writeUtf8(ByteString.of(((Certificate) list.get(i)).getEncoded()).base64());
                    bufferedSink.writeByte(10);
                }
            } catch (CertificateEncodingException e) {
                throw new IOException(e.getMessage());
            }
        }

        public boolean matches(Request request, Response response) {
            return this.url.equals(request.urlString()) && this.requestMethod.equals(request.method()) && OkHeaders.varyMatches(response, this.varyHeaders, request);
        }

        public Response response(Request request, Snapshot snapshot) {
            String str = this.responseHeaders.get("Content-Type");
            String str2 = this.responseHeaders.get("Content-Length");
            return new Response.Builder().request(new Request.Builder().url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build()).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new CacheResponseBody(snapshot, str, str2)).handshake(this.handshake).build();
        }
    }

    public Cache(File file, long j) {
        this.internalCache = new C06211();
        this.cache = DiskLruCache.create(FileSystem.SYSTEM, file, VERSION, ENTRY_COUNT, j);
    }

    private static String urlToKey(Request request) {
        return Util.md5Hex(request.urlString());
    }

    Response get(Request request) {
        try {
            Closeable closeable = this.cache.get(urlToKey(request));
            if (closeable == null) {
                return null;
            }
            try {
                Entry entry = new Entry(closeable.getSource(ENTRY_METADATA));
                Response response = entry.response(request, closeable);
                if (entry.matches(request, response)) {
                    return response;
                }
                Util.closeQuietly(response.body());
                return null;
            } catch (IOException e) {
                Util.closeQuietly(closeable);
                return null;
            }
        } catch (IOException e2) {
            return null;
        }
    }

    private CacheRequest put(Response response) {
        Editor editor;
        String method = response.request().method();
        if (HttpMethod.invalidatesCache(response.request().method())) {
            try {
                remove(response.request());
                return null;
            } catch (IOException e) {
                return null;
            }
        } else if (!method.equals("GET") || OkHeaders.hasVaryAll(response)) {
            return null;
        } else {
            Entry entry = new Entry(response);
            try {
                Editor edit = this.cache.edit(urlToKey(response.request()));
                if (edit == null) {
                    return null;
                }
                try {
                    entry.writeTo(edit);
                    return new CacheRequestImpl(edit);
                } catch (IOException e2) {
                    editor = edit;
                    abortQuietly(editor);
                    return null;
                }
            } catch (IOException e3) {
                editor = null;
                abortQuietly(editor);
                return null;
            }
        }
    }

    private void remove(Request request) {
        this.cache.remove(urlToKey(request));
    }

    private void update(Response response, Response response2) {
        Entry entry = new Entry(response2);
        try {
            Editor edit = ((CacheResponseBody) response.body()).snapshot.edit();
            if (edit != null) {
                entry.writeTo(edit);
                edit.commit();
            }
        } catch (IOException e) {
            abortQuietly(null);
        }
    }

    private void abortQuietly(Editor editor) {
        if (editor != null) {
            try {
                editor.abort();
            } catch (IOException e) {
            }
        }
    }

    public void delete() {
        this.cache.delete();
    }

    public void evictAll() {
        this.cache.evictAll();
    }

    public Iterator<String> urls() {
        return new C06222();
    }

    public synchronized int getWriteAbortCount() {
        return this.writeAbortCount;
    }

    public synchronized int getWriteSuccessCount() {
        return this.writeSuccessCount;
    }

    public long getSize() {
        return this.cache.size();
    }

    public long getMaxSize() {
        return this.cache.getMaxSize();
    }

    public void flush() {
        this.cache.flush();
    }

    public void close() {
        this.cache.close();
    }

    public File getDirectory() {
        return this.cache.getDirectory();
    }

    public boolean isClosed() {
        return this.cache.isClosed();
    }

    private synchronized void trackResponse(CacheStrategy cacheStrategy) {
        this.requestCount += ENTRY_BODY;
        if (cacheStrategy.networkRequest != null) {
            this.networkCount += ENTRY_BODY;
        } else if (cacheStrategy.cacheResponse != null) {
            this.hitCount += ENTRY_BODY;
        }
    }

    private synchronized void trackConditionalCacheHit() {
        this.hitCount += ENTRY_BODY;
    }

    public synchronized int getNetworkCount() {
        return this.networkCount;
    }

    public synchronized int getHitCount() {
        return this.hitCount;
    }

    public synchronized int getRequestCount() {
        return this.requestCount;
    }

    private static int readInt(BufferedSource bufferedSource) {
        try {
            long readDecimalLong = bufferedSource.readDecimalLong();
            String readUtf8LineStrict = bufferedSource.readUtf8LineStrict();
            if (readDecimalLong >= 0 && readDecimalLong <= 2147483647L && readUtf8LineStrict.isEmpty()) {
                return (int) readDecimalLong;
            }
            throw new IOException("expected an int but was \"" + readDecimalLong + readUtf8LineStrict + "\"");
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
    }
}
