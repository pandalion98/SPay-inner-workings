/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp.internal.spdy;

public final class HeadersMode
extends Enum<HeadersMode> {
    private static final /* synthetic */ HeadersMode[] $VALUES;
    public static final /* enum */ HeadersMode HTTP_20_HEADERS;
    public static final /* enum */ HeadersMode SPDY_HEADERS;
    public static final /* enum */ HeadersMode SPDY_REPLY;
    public static final /* enum */ HeadersMode SPDY_SYN_STREAM;

    static {
        SPDY_SYN_STREAM = new HeadersMode();
        SPDY_REPLY = new HeadersMode();
        SPDY_HEADERS = new HeadersMode();
        HTTP_20_HEADERS = new HeadersMode();
        HeadersMode[] arrheadersMode = new HeadersMode[]{SPDY_SYN_STREAM, SPDY_REPLY, SPDY_HEADERS, HTTP_20_HEADERS};
        $VALUES = arrheadersMode;
    }

    public static HeadersMode valueOf(String string) {
        return (HeadersMode)Enum.valueOf(HeadersMode.class, (String)string);
    }

    public static HeadersMode[] values() {
        return (HeadersMode[])$VALUES.clone();
    }

    public boolean failIfHeadersAbsent() {
        return this == SPDY_HEADERS;
    }

    public boolean failIfHeadersPresent() {
        return this == SPDY_REPLY;
    }

    public boolean failIfStreamAbsent() {
        return this == SPDY_REPLY || this == SPDY_HEADERS;
    }

    public boolean failIfStreamPresent() {
        return this == SPDY_SYN_STREAM;
    }
}

