package com.squareup.okhttp;

import com.squareup.okhttp.internal.Util;
import java.io.Closeable;
import java.io.File;
import java.nio.charset.Charset;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;

public abstract class RequestBody {

    /* renamed from: com.squareup.okhttp.RequestBody.1 */
    static class C06341 extends RequestBody {
        final /* synthetic */ ByteString val$content;
        final /* synthetic */ MediaType val$contentType;

        C06341(MediaType mediaType, ByteString byteString) {
            this.val$contentType = mediaType;
            this.val$content = byteString;
        }

        public MediaType contentType() {
            return this.val$contentType;
        }

        public long contentLength() {
            return (long) this.val$content.size();
        }

        public void writeTo(BufferedSink bufferedSink) {
            bufferedSink.write(this.val$content);
        }
    }

    /* renamed from: com.squareup.okhttp.RequestBody.2 */
    static class C06352 extends RequestBody {
        final /* synthetic */ int val$byteCount;
        final /* synthetic */ byte[] val$content;
        final /* synthetic */ MediaType val$contentType;
        final /* synthetic */ int val$offset;

        C06352(MediaType mediaType, int i, byte[] bArr, int i2) {
            this.val$contentType = mediaType;
            this.val$byteCount = i;
            this.val$content = bArr;
            this.val$offset = i2;
        }

        public MediaType contentType() {
            return this.val$contentType;
        }

        public long contentLength() {
            return (long) this.val$byteCount;
        }

        public void writeTo(BufferedSink bufferedSink) {
            bufferedSink.write(this.val$content, this.val$offset, this.val$byteCount);
        }
    }

    /* renamed from: com.squareup.okhttp.RequestBody.3 */
    static class C06363 extends RequestBody {
        final /* synthetic */ MediaType val$contentType;
        final /* synthetic */ File val$file;

        C06363(MediaType mediaType, File file) {
            this.val$contentType = mediaType;
            this.val$file = file;
        }

        public MediaType contentType() {
            return this.val$contentType;
        }

        public long contentLength() {
            return this.val$file.length();
        }

        public void writeTo(BufferedSink bufferedSink) {
            Closeable closeable = null;
            try {
                closeable = Okio.source(this.val$file);
                bufferedSink.writeAll(closeable);
            } finally {
                Util.closeQuietly(closeable);
            }
        }
    }

    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink bufferedSink);

    public long contentLength() {
        return -1;
    }

    public static RequestBody create(MediaType mediaType, String str) {
        Charset charset = Util.UTF_8;
        if (mediaType != null) {
            charset = mediaType.charset();
            if (charset == null) {
                charset = Util.UTF_8;
                mediaType = MediaType.parse(mediaType + "; charset=utf-8");
            }
        }
        return create(mediaType, str.getBytes(charset));
    }

    public static RequestBody create(MediaType mediaType, ByteString byteString) {
        return new C06341(mediaType, byteString);
    }

    public static RequestBody create(MediaType mediaType, byte[] bArr) {
        return create(mediaType, bArr, 0, bArr.length);
    }

    public static RequestBody create(MediaType mediaType, byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
        return new C06352(mediaType, i2, bArr, i);
    }

    public static RequestBody create(MediaType mediaType, File file) {
        if (file != null) {
            return new C06363(mediaType, file);
        }
        throw new NullPointerException("content == null");
    }
}
