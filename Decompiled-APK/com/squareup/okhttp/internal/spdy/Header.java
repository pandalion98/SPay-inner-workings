package com.squareup.okhttp.internal.spdy;

import okio.ByteString;

public final class Header {
    public static final ByteString RESPONSE_STATUS;
    public static final ByteString TARGET_AUTHORITY;
    public static final ByteString TARGET_HOST;
    public static final ByteString TARGET_METHOD;
    public static final ByteString TARGET_PATH;
    public static final ByteString TARGET_SCHEME;
    public static final ByteString VERSION;
    final int hpackSize;
    public final ByteString name;
    public final ByteString value;

    static {
        RESPONSE_STATUS = ByteString.encodeUtf8(":status");
        TARGET_METHOD = ByteString.encodeUtf8(":method");
        TARGET_PATH = ByteString.encodeUtf8(":path");
        TARGET_SCHEME = ByteString.encodeUtf8(":scheme");
        TARGET_AUTHORITY = ByteString.encodeUtf8(":authority");
        TARGET_HOST = ByteString.encodeUtf8(":host");
        VERSION = ByteString.encodeUtf8(":version");
    }

    public Header(String str, String str2) {
        this(ByteString.encodeUtf8(str), ByteString.encodeUtf8(str2));
    }

    public Header(ByteString byteString, String str) {
        this(byteString, ByteString.encodeUtf8(str));
    }

    public Header(ByteString byteString, ByteString byteString2) {
        this.name = byteString;
        this.value = byteString2;
        this.hpackSize = (byteString.size() + 32) + byteString2.size();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Header)) {
            return false;
        }
        Header header = (Header) obj;
        if (this.name.equals(header.name) && this.value.equals(header.value)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((this.name.hashCode() + 527) * 31) + this.value.hashCode();
    }

    public String toString() {
        return String.format("%s: %s", new Object[]{this.name.utf8(), this.value.utf8()});
    }
}
