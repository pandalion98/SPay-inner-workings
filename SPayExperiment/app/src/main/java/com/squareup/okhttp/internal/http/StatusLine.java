/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.net.ProtocolException
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Response;
import java.net.ProtocolException;

public final class StatusLine {
    public static final int HTTP_CONTINUE = 100;
    public static final int HTTP_PERM_REDIRECT = 308;
    public static final int HTTP_TEMP_REDIRECT = 307;
    public final int code;
    public final String message;
    public final Protocol protocol;

    public StatusLine(Protocol protocol, int n2, String string) {
        this.protocol = protocol;
        this.code = n2;
        this.message = string;
    }

    public static StatusLine get(Response response) {
        return new StatusLine(response.protocol(), response.code(), response.message());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static StatusLine parse(String string) {
        Protocol protocol;
        int n2;
        String string2;
        int n3 = 9;
        if (string.startsWith("HTTP/1.")) {
            if (string.length() < n3) throw new ProtocolException("Unexpected status line: " + string);
            if (string.charAt(8) != ' ') {
                throw new ProtocolException("Unexpected status line: " + string);
            }
            int n4 = -48 + string.charAt(7);
            if (n4 == 0) {
                protocol = Protocol.HTTP_1_0;
            } else {
                if (n4 != 1) throw new ProtocolException("Unexpected status line: " + string);
                protocol = Protocol.HTTP_1_1;
            }
        } else {
            if (!string.startsWith("ICY ")) throw new ProtocolException("Unexpected status line: " + string);
            protocol = Protocol.HTTP_1_0;
            n3 = 4;
        }
        if (string.length() < n3 + 3) {
            throw new ProtocolException("Unexpected status line: " + string);
        }
        int n5 = n3 + 3;
        try {
            n2 = Integer.parseInt((String)string.substring(n3, n5));
        }
        catch (NumberFormatException numberFormatException) {
            throw new ProtocolException("Unexpected status line: " + string);
        }
        if (string.length() <= n3 + 3) {
            string2 = "";
            return new StatusLine(protocol, n2, string2);
        }
        if (string.charAt(n3 + 3) != ' ') {
            throw new ProtocolException("Unexpected status line: " + string);
        }
        string2 = string.substring(n3 + 4);
        return new StatusLine(protocol, n2, string2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String string = this.protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
        stringBuilder.append(string);
        stringBuilder.append(' ').append(this.code);
        if (this.message != null) {
            stringBuilder.append(' ').append(this.message);
        }
        return stringBuilder.toString();
    }
}

