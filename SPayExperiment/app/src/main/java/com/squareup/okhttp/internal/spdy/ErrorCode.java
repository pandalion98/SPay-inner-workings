/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.squareup.okhttp.internal.spdy;

public final class ErrorCode
extends Enum<ErrorCode> {
    private static final /* synthetic */ ErrorCode[] $VALUES;
    public static final /* enum */ ErrorCode CANCEL;
    public static final /* enum */ ErrorCode COMPRESSION_ERROR;
    public static final /* enum */ ErrorCode CONNECT_ERROR;
    public static final /* enum */ ErrorCode ENHANCE_YOUR_CALM;
    public static final /* enum */ ErrorCode FLOW_CONTROL_ERROR;
    public static final /* enum */ ErrorCode FRAME_TOO_LARGE;
    public static final /* enum */ ErrorCode HTTP_1_1_REQUIRED;
    public static final /* enum */ ErrorCode INADEQUATE_SECURITY;
    public static final /* enum */ ErrorCode INTERNAL_ERROR;
    public static final /* enum */ ErrorCode INVALID_CREDENTIALS;
    public static final /* enum */ ErrorCode INVALID_STREAM;
    public static final /* enum */ ErrorCode NO_ERROR;
    public static final /* enum */ ErrorCode PROTOCOL_ERROR;
    public static final /* enum */ ErrorCode REFUSED_STREAM;
    public static final /* enum */ ErrorCode STREAM_ALREADY_CLOSED;
    public static final /* enum */ ErrorCode STREAM_CLOSED;
    public static final /* enum */ ErrorCode STREAM_IN_USE;
    public static final /* enum */ ErrorCode UNSUPPORTED_VERSION;
    public final int httpCode;
    public final int spdyGoAwayCode;
    public final int spdyRstCode;

    static {
        NO_ERROR = new ErrorCode(0, -1, 0);
        PROTOCOL_ERROR = new ErrorCode(1, 1, 1);
        INVALID_STREAM = new ErrorCode(1, 2, -1);
        UNSUPPORTED_VERSION = new ErrorCode(1, 4, -1);
        STREAM_IN_USE = new ErrorCode(1, 8, -1);
        STREAM_ALREADY_CLOSED = new ErrorCode(1, 9, -1);
        INTERNAL_ERROR = new ErrorCode(2, 6, 2);
        FLOW_CONTROL_ERROR = new ErrorCode(3, 7, -1);
        STREAM_CLOSED = new ErrorCode(5, -1, -1);
        FRAME_TOO_LARGE = new ErrorCode(6, 11, -1);
        REFUSED_STREAM = new ErrorCode(7, 3, -1);
        CANCEL = new ErrorCode(8, 5, -1);
        COMPRESSION_ERROR = new ErrorCode(9, -1, -1);
        CONNECT_ERROR = new ErrorCode(10, -1, -1);
        ENHANCE_YOUR_CALM = new ErrorCode(11, -1, -1);
        INADEQUATE_SECURITY = new ErrorCode(12, -1, -1);
        HTTP_1_1_REQUIRED = new ErrorCode(13, -1, -1);
        INVALID_CREDENTIALS = new ErrorCode(-1, 10, -1);
        ErrorCode[] arrerrorCode = new ErrorCode[]{NO_ERROR, PROTOCOL_ERROR, INVALID_STREAM, UNSUPPORTED_VERSION, STREAM_IN_USE, STREAM_ALREADY_CLOSED, INTERNAL_ERROR, FLOW_CONTROL_ERROR, STREAM_CLOSED, FRAME_TOO_LARGE, REFUSED_STREAM, CANCEL, COMPRESSION_ERROR, CONNECT_ERROR, ENHANCE_YOUR_CALM, INADEQUATE_SECURITY, HTTP_1_1_REQUIRED, INVALID_CREDENTIALS};
        $VALUES = arrerrorCode;
    }

    private ErrorCode(int n3, int n4, int n5) {
        this.httpCode = n3;
        this.spdyRstCode = n4;
        this.spdyGoAwayCode = n5;
    }

    public static ErrorCode fromHttp2(int n2) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.httpCode != n2) continue;
            return errorCode;
        }
        return null;
    }

    public static ErrorCode fromSpdy3Rst(int n2) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.spdyRstCode != n2) continue;
            return errorCode;
        }
        return null;
    }

    public static ErrorCode fromSpdyGoAway(int n2) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.spdyGoAwayCode != n2) continue;
            return errorCode;
        }
        return null;
    }

    public static ErrorCode valueOf(String string) {
        return (ErrorCode)Enum.valueOf(ErrorCode.class, (String)string);
    }

    public static ErrorCode[] values() {
        return (ErrorCode[])$VALUES.clone();
    }
}

