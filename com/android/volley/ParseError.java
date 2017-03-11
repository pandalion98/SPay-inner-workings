package com.android.volley;

public class ParseError extends VolleyError {
    public ParseError(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public ParseError(Throwable th) {
        super(th);
    }
}
