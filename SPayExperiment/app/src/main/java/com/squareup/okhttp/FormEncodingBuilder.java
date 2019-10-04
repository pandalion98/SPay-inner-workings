/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import okio.Buffer;
import okio.ByteString;

public final class FormEncodingBuilder {
    private static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    private final Buffer content = new Buffer();

    public FormEncodingBuilder add(String string, String string2) {
        if (this.content.size() > 0L) {
            this.content.writeByte(38);
        }
        HttpUrl.canonicalize(this.content, string, 0, string.length(), " \"'<>#&=", false, true);
        this.content.writeByte(61);
        HttpUrl.canonicalize(this.content, string2, 0, string2.length(), " \"'<>#&=", false, true);
        return this;
    }

    public FormEncodingBuilder addEncoded(String string, String string2) {
        if (this.content.size() > 0L) {
            this.content.writeByte(38);
        }
        HttpUrl.canonicalize(this.content, string, 0, string.length(), " \"'<>#&=", true, true);
        this.content.writeByte(61);
        HttpUrl.canonicalize(this.content, string2, 0, string2.length(), " \"'<>#&=", true, true);
        return this;
    }

    public RequestBody build() {
        if (this.content.size() == 0L) {
            throw new IllegalStateException("Form encoded body must have at least one part.");
        }
        return RequestBody.create(CONTENT_TYPE, this.content.snapshot());
    }
}

