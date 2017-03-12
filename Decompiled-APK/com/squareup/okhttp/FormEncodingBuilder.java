package com.squareup.okhttp;

import okio.Buffer;

public final class FormEncodingBuilder {
    private static final MediaType CONTENT_TYPE;
    private final Buffer content;

    public FormEncodingBuilder() {
        this.content = new Buffer();
    }

    static {
        CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    }

    public FormEncodingBuilder add(String str, String str2) {
        if (this.content.size() > 0) {
            this.content.writeByte(38);
        }
        HttpUrl.canonicalize(this.content, str, 0, str.length(), " \"'<>#&=", false, true);
        this.content.writeByte(61);
        HttpUrl.canonicalize(this.content, str2, 0, str2.length(), " \"'<>#&=", false, true);
        return this;
    }

    public FormEncodingBuilder addEncoded(String str, String str2) {
        if (this.content.size() > 0) {
            this.content.writeByte(38);
        }
        HttpUrl.canonicalize(this.content, str, 0, str.length(), " \"'<>#&=", true, true);
        this.content.writeByte(61);
        HttpUrl.canonicalize(this.content, str2, 0, str2.length(), " \"'<>#&=", true, true);
        return this;
    }

    public RequestBody build() {
        if (this.content.size() != 0) {
            return RequestBody.create(CONTENT_TYPE, this.content.snapshot());
        }
        throw new IllegalStateException("Form encoded body must have at least one part.");
    }
}
