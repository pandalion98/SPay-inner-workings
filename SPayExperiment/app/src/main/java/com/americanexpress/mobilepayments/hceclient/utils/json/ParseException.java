/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 */
package com.americanexpress.mobilepayments.hceclient.utils.json;

public class ParseException
extends Exception {
    public static final int ERROR_UNEXPECTED_CHAR = 0;
    public static final int ERROR_UNEXPECTED_EXCEPTION = 2;
    public static final int ERROR_UNEXPECTED_TOKEN = 1;
    private static final long serialVersionUID = -7880698968187728547L;
    private int errorType;
    private int position;
    private Object unexpectedObject;

    public ParseException(int n2, int n3, Object object) {
        this.position = n2;
        this.errorType = n3;
        this.unexpectedObject = object;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String getMessage() {
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.errorType) {
            default: {
                stringBuffer.append("Unkown error at position ").append(this.position).append(".");
                do {
                    return stringBuffer.toString();
                    break;
                } while (true);
            }
            case 0: {
                stringBuffer.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
                return stringBuffer.toString();
            }
            case 1: {
                stringBuffer.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
                return stringBuffer.toString();
            }
            case 2: 
        }
        stringBuffer.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
        return stringBuffer.toString();
    }
}

