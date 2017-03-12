package com.squareup.okhttp;

public interface Interceptor {

    public interface Chain {
        Connection connection();

        Response proceed(Request request);

        Request request();
    }

    Response intercept(Chain chain);
}
