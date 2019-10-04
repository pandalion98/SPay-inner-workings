/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.charset.Charset
 */
package com.squareup.okhttp;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.internal.Util;
import java.io.File;
import java.nio.charset.Charset;
import okio.BufferedSink;
import okio.ByteString;
import okio.Okio;
import okio.Source;

public abstract class RequestBody {
    public static RequestBody create(final MediaType mediaType, final File file) {
        if (file == null) {
            throw new NullPointerException("content == null");
        }
        return new RequestBody(){

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) {
                Source source = null;
                try {
                    source = Okio.source(file);
                    bufferedSink.writeAll(source);
                    return;
                }
                finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    public static RequestBody create(MediaType mediaType, String string) {
        Charset charset = Util.UTF_8;
        if (mediaType != null && (charset = mediaType.charset()) == null) {
            charset = Util.UTF_8;
            mediaType = MediaType.parse(mediaType + "; charset=utf-8");
        }
        return RequestBody.create(mediaType, string.getBytes(charset));
    }

    public static RequestBody create(final MediaType mediaType, final ByteString byteString) {
        return new RequestBody(){

            @Override
            public long contentLength() {
                return byteString.size();
            }

            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) {
                bufferedSink.write(byteString);
            }
        };
    }

    public static RequestBody create(MediaType mediaType, byte[] arrby) {
        return RequestBody.create(mediaType, arrby, 0, arrby.length);
    }

    public static RequestBody create(final MediaType mediaType, final byte[] arrby, final int n2, final int n3) {
        if (arrby == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount(arrby.length, n2, n3);
        return new RequestBody(){

            @Override
            public long contentLength() {
                return n3;
            }

            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public void writeTo(BufferedSink bufferedSink) {
                bufferedSink.write(arrby, n2, n3);
            }
        };
    }

    public long contentLength() {
        return -1L;
    }

    public abstract MediaType contentType();

    public abstract void writeTo(BufferedSink var1);

}

