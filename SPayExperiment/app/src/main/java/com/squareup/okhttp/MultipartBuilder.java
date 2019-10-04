/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.UUID
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

public final class MultipartBuilder {
    public static final MediaType ALTERNATIVE;
    private static final byte[] COLONSPACE;
    private static final byte[] CRLF;
    private static final byte[] DASHDASH;
    public static final MediaType DIGEST;
    public static final MediaType FORM;
    public static final MediaType MIXED;
    public static final MediaType PARALLEL;
    private final ByteString boundary;
    private final List<RequestBody> partBodies = new ArrayList();
    private final List<Headers> partHeaders = new ArrayList();
    private MediaType type = MIXED;

    static {
        MIXED = MediaType.parse("multipart/mixed");
        ALTERNATIVE = MediaType.parse("multipart/alternative");
        DIGEST = MediaType.parse("multipart/digest");
        PARALLEL = MediaType.parse("multipart/parallel");
        FORM = MediaType.parse("multipart/form-data");
        COLONSPACE = new byte[]{58, 32};
        CRLF = new byte[]{13, 10};
        DASHDASH = new byte[]{45, 45};
    }

    public MultipartBuilder() {
        this(UUID.randomUUID().toString());
    }

    public MultipartBuilder(String string) {
        this.boundary = ByteString.encodeUtf8(string);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static StringBuilder appendQuotedString(StringBuilder stringBuilder, String string) {
        stringBuilder.append('\"');
        int n2 = 0;
        int n3 = string.length();
        do {
            if (n2 >= n3) {
                stringBuilder.append('\"');
                return stringBuilder;
            }
            char c2 = string.charAt(n2);
            switch (c2) {
                default: {
                    stringBuilder.append(c2);
                    break;
                }
                case '\n': {
                    stringBuilder.append("%0A");
                    break;
                }
                case '\r': {
                    stringBuilder.append("%0D");
                    break;
                }
                case '\"': {
                    stringBuilder.append("%22");
                }
            }
            ++n2;
        } while (true);
    }

    public MultipartBuilder addFormDataPart(String string, String string2) {
        return this.addFormDataPart(string, null, RequestBody.create(null, string2));
    }

    public MultipartBuilder addFormDataPart(String string, String string2, RequestBody requestBody) {
        if (string == null) {
            throw new NullPointerException("name == null");
        }
        StringBuilder stringBuilder = new StringBuilder("form-data; name=");
        MultipartBuilder.appendQuotedString(stringBuilder, string);
        if (string2 != null) {
            stringBuilder.append("; filename=");
            MultipartBuilder.appendQuotedString(stringBuilder, string2);
        }
        String[] arrstring = new String[]{"Content-Disposition", stringBuilder.toString()};
        return this.addPart(Headers.of(arrstring), requestBody);
    }

    public MultipartBuilder addPart(Headers headers, RequestBody requestBody) {
        if (requestBody == null) {
            throw new NullPointerException("body == null");
        }
        if (headers != null && headers.get("Content-Type") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Type");
        }
        if (headers != null && headers.get("Content-Length") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Length");
        }
        this.partHeaders.add((Object)headers);
        this.partBodies.add((Object)requestBody);
        return this;
    }

    public MultipartBuilder addPart(RequestBody requestBody) {
        return this.addPart(null, requestBody);
    }

    public RequestBody build() {
        if (this.partHeaders.isEmpty()) {
            throw new IllegalStateException("Multipart body must have at least one part.");
        }
        return new MultipartRequestBody(this.type, this.boundary, this.partHeaders, this.partBodies);
    }

    public MultipartBuilder type(MediaType mediaType) {
        if (mediaType == null) {
            throw new NullPointerException("type == null");
        }
        if (!mediaType.type().equals((Object)"multipart")) {
            throw new IllegalArgumentException("multipart != " + mediaType);
        }
        this.type = mediaType;
        return this;
    }

    private static final class MultipartRequestBody
    extends RequestBody {
        private final ByteString boundary;
        private long contentLength = -1L;
        private final MediaType contentType;
        private final List<RequestBody> partBodies;
        private final List<Headers> partHeaders;

        public MultipartRequestBody(MediaType mediaType, ByteString byteString, List<Headers> list, List<RequestBody> list2) {
            if (mediaType == null) {
                throw new NullPointerException("type == null");
            }
            this.boundary = byteString;
            this.contentType = MediaType.parse(mediaType + "; boundary=" + byteString.utf8());
            this.partHeaders = Util.immutableList(list);
            this.partBodies = Util.immutableList(list2);
        }

        /*
         * Enabled aggressive block sorting
         */
        private long writeOrCountBytes(BufferedSink bufferedSink, boolean bl) {
            Buffer buffer;
            long l2 = 0L;
            if (bl) {
                Buffer buffer2;
                buffer = buffer2 = new Buffer();
                bufferedSink = buffer2;
            } else {
                buffer = null;
            }
            int n2 = this.partHeaders.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                MediaType mediaType;
                long l3;
                long l4;
                Headers headers = (Headers)this.partHeaders.get(i2);
                RequestBody requestBody = (RequestBody)this.partBodies.get(i2);
                bufferedSink.write(DASHDASH);
                bufferedSink.write(this.boundary);
                bufferedSink.write(CRLF);
                if (headers != null) {
                    int n3 = headers.size();
                    for (int i3 = 0; i3 < n3; ++i3) {
                        bufferedSink.writeUtf8(headers.name(i3)).write(COLONSPACE).writeUtf8(headers.value(i3)).write(CRLF);
                    }
                }
                if ((mediaType = requestBody.contentType()) != null) {
                    bufferedSink.writeUtf8("Content-Type: ").writeUtf8(mediaType.toString()).write(CRLF);
                }
                if ((l4 = requestBody.contentLength()) != -1L) {
                    bufferedSink.writeUtf8("Content-Length: ").writeDecimalLong(l4).write(CRLF);
                } else if (bl) {
                    buffer.clear();
                    return -1L;
                }
                bufferedSink.write(CRLF);
                if (bl) {
                    l3 = l4 + l2;
                } else {
                    ((RequestBody)this.partBodies.get(i2)).writeTo(bufferedSink);
                    l3 = l2;
                }
                bufferedSink.write(CRLF);
                l2 = l3;
            }
            bufferedSink.write(DASHDASH);
            bufferedSink.write(this.boundary);
            bufferedSink.write(DASHDASH);
            bufferedSink.write(CRLF);
            if (!bl) return l2;
            long l5 = l2 + buffer.size();
            buffer.clear();
            return l5;
        }

        @Override
        public long contentLength() {
            long l2;
            long l3 = this.contentLength;
            if (l3 != -1L) {
                return l3;
            }
            this.contentLength = l2 = this.writeOrCountBytes(null, true);
            return l2;
        }

        @Override
        public MediaType contentType() {
            return this.contentType;
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) {
            this.writeOrCountBytes(bufferedSink, false);
        }
    }

}

