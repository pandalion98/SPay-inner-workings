package com.americanexpress.mobilepayments.hceclient.utils.json;

public class ParseException extends Exception {
    public static final int ERROR_UNEXPECTED_CHAR = 0;
    public static final int ERROR_UNEXPECTED_EXCEPTION = 2;
    public static final int ERROR_UNEXPECTED_TOKEN = 1;
    private static final long serialVersionUID = -7880698968187728547L;
    private int errorType;
    private int position;
    private Object unexpectedObject;

    public ParseException(int i, int i2, Object obj) {
        this.position = i;
        this.errorType = i2;
        this.unexpectedObject = obj;
    }

    public String getMessage() {
        StringBuffer stringBuffer = new StringBuffer();
        switch (this.errorType) {
            case ERROR_UNEXPECTED_CHAR /*0*/:
                stringBuffer.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
                break;
            case ERROR_UNEXPECTED_TOKEN /*1*/:
                stringBuffer.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
                break;
            case ERROR_UNEXPECTED_EXCEPTION /*2*/:
                stringBuffer.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
                break;
            default:
                stringBuffer.append("Unkown error at position ").append(this.position).append(".");
                break;
        }
        return stringBuffer.toString();
    }
}
