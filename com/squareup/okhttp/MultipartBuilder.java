package com.squareup.okhttp;

import com.google.android.gms.location.places.Place;
import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.squareup.okhttp.internal.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import org.bouncycastle.crypto.tls.NamedCurve;

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
    private final List<RequestBody> partBodies;
    private final List<Headers> partHeaders;
    private MediaType type;

    private static final class MultipartRequestBody extends RequestBody {
        private final ByteString boundary;
        private long contentLength;
        private final MediaType contentType;
        private final List<RequestBody> partBodies;
        private final List<Headers> partHeaders;

        public MultipartRequestBody(MediaType mediaType, ByteString byteString, List<Headers> list, List<RequestBody> list2) {
            this.contentLength = -1;
            if (mediaType == null) {
                throw new NullPointerException("type == null");
            }
            this.boundary = byteString;
            this.contentType = MediaType.parse(mediaType + "; boundary=" + byteString.utf8());
            this.partHeaders = Util.immutableList((List) list);
            this.partBodies = Util.immutableList((List) list2);
        }

        public MediaType contentType() {
            return this.contentType;
        }

        public long contentLength() {
            long j = this.contentLength;
            if (j != -1) {
                return j;
            }
            j = writeOrCountBytes(null, true);
            this.contentLength = j;
            return j;
        }

        private long writeOrCountBytes(BufferedSink bufferedSink, boolean z) {
            Buffer buffer;
            long j = 0;
            if (z) {
                Buffer buffer2 = new Buffer();
                buffer = buffer2;
                bufferedSink = buffer2;
            } else {
                buffer = null;
            }
            int size = this.partHeaders.size();
            int i = 0;
            while (i < size) {
                Headers headers = (Headers) this.partHeaders.get(i);
                RequestBody requestBody = (RequestBody) this.partBodies.get(i);
                bufferedSink.write(MultipartBuilder.DASHDASH);
                bufferedSink.write(this.boundary);
                bufferedSink.write(MultipartBuilder.CRLF);
                if (headers != null) {
                    int size2 = headers.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        bufferedSink.writeUtf8(headers.name(i2)).write(MultipartBuilder.COLONSPACE).writeUtf8(headers.value(i2)).write(MultipartBuilder.CRLF);
                    }
                }
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    bufferedSink.writeUtf8("Content-Type: ").writeUtf8(contentType.toString()).write(MultipartBuilder.CRLF);
                }
                long contentLength = requestBody.contentLength();
                if (contentLength != -1) {
                    bufferedSink.writeUtf8("Content-Length: ").writeDecimalLong(contentLength).write(MultipartBuilder.CRLF);
                } else if (z) {
                    buffer.clear();
                    return -1;
                }
                bufferedSink.write(MultipartBuilder.CRLF);
                if (z) {
                    contentLength += j;
                } else {
                    ((RequestBody) this.partBodies.get(i)).writeTo(bufferedSink);
                    contentLength = j;
                }
                bufferedSink.write(MultipartBuilder.CRLF);
                i++;
                j = contentLength;
            }
            bufferedSink.write(MultipartBuilder.DASHDASH);
            bufferedSink.write(this.boundary);
            bufferedSink.write(MultipartBuilder.DASHDASH);
            bufferedSink.write(MultipartBuilder.CRLF);
            if (!z) {
                return j;
            }
            j += buffer.size();
            buffer.clear();
            return j;
        }

        public void writeTo(BufferedSink bufferedSink) {
            writeOrCountBytes(bufferedSink, false);
        }
    }

    static {
        MIXED = MediaType.parse("multipart/mixed");
        ALTERNATIVE = MediaType.parse("multipart/alternative");
        DIGEST = MediaType.parse("multipart/digest");
        PARALLEL = MediaType.parse("multipart/parallel");
        FORM = MediaType.parse("multipart/form-data");
        COLONSPACE = new byte[]{(byte) 58, VerifyPINApdu.INS};
        CRLF = new byte[]{(byte) 13, (byte) 10};
        DASHDASH = new byte[]{SetResetParamApdu.INS, SetResetParamApdu.INS};
    }

    public MultipartBuilder() {
        this(UUID.randomUUID().toString());
    }

    public MultipartBuilder(String str) {
        this.type = MIXED;
        this.partHeaders = new ArrayList();
        this.partBodies = new ArrayList();
        this.boundary = ByteString.encodeUtf8(str);
    }

    public MultipartBuilder type(MediaType mediaType) {
        if (mediaType == null) {
            throw new NullPointerException("type == null");
        } else if (mediaType.type().equals("multipart")) {
            this.type = mediaType;
            return this;
        } else {
            throw new IllegalArgumentException("multipart != " + mediaType);
        }
    }

    public MultipartBuilder addPart(RequestBody requestBody) {
        return addPart(null, requestBody);
    }

    public MultipartBuilder addPart(Headers headers, RequestBody requestBody) {
        if (requestBody == null) {
            throw new NullPointerException("body == null");
        } else if (headers != null && headers.get("Content-Type") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Type");
        } else if (headers == null || headers.get("Content-Length") == null) {
            this.partHeaders.add(headers);
            this.partBodies.add(requestBody);
            return this;
        } else {
            throw new IllegalArgumentException("Unexpected header: Content-Length");
        }
    }

    private static StringBuilder appendQuotedString(StringBuilder stringBuilder, String str) {
        stringBuilder.append('\"');
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case NamedCurve.sect283r1 /*10*/:
                    stringBuilder.append("%0A");
                    break;
                case NamedCurve.sect571k1 /*13*/:
                    stringBuilder.append("%0D");
                    break;
                case Place.TYPE_ESTABLISHMENT /*34*/:
                    stringBuilder.append("%22");
                    break;
                default:
                    stringBuilder.append(charAt);
                    break;
            }
        }
        stringBuilder.append('\"');
        return stringBuilder;
    }

    public MultipartBuilder addFormDataPart(String str, String str2) {
        return addFormDataPart(str, null, RequestBody.create(null, str2));
    }

    public MultipartBuilder addFormDataPart(String str, String str2, RequestBody requestBody) {
        if (str == null) {
            throw new NullPointerException("name == null");
        }
        StringBuilder stringBuilder = new StringBuilder("form-data; name=");
        appendQuotedString(stringBuilder, str);
        if (str2 != null) {
            stringBuilder.append("; filename=");
            appendQuotedString(stringBuilder, str2);
        }
        return addPart(Headers.of("Content-Disposition", stringBuilder.toString()), requestBody);
    }

    public RequestBody build() {
        if (!this.partHeaders.isEmpty()) {
            return new MultipartRequestBody(this.type, this.boundary, this.partHeaders, this.partBodies);
        }
        throw new IllegalStateException("Multipart body must have at least one part.");
    }
}
