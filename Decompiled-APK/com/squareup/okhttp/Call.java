package com.squareup.okhttp;

import com.squareup.okhttp.Interceptor.Chain;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.NamedRunnable;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.RequestException;
import com.squareup.okhttp.internal.http.RouteException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;

public class Call {
    volatile boolean canceled;
    private final OkHttpClient client;
    HttpEngine engine;
    private boolean executed;
    Request originalRequest;

    class ApplicationInterceptorChain implements Chain {
        private final boolean forWebSocket;
        private final int index;
        private final Request request;

        ApplicationInterceptorChain(int i, Request request, boolean z) {
            this.index = i;
            this.request = request;
            this.forWebSocket = z;
        }

        public Connection connection() {
            return null;
        }

        public Request request() {
            return this.request;
        }

        public Response proceed(Request request) {
            if (this.index >= Call.this.client.interceptors().size()) {
                return Call.this.getResponse(request, this.forWebSocket);
            }
            return ((Interceptor) Call.this.client.interceptors().get(this.index)).intercept(new ApplicationInterceptorChain(this.index + 1, request, this.forWebSocket));
        }
    }

    final class AsyncCall extends NamedRunnable {
        private final boolean forWebSocket;
        private final Callback responseCallback;

        private AsyncCall(Callback callback, boolean z) {
            super("OkHttp %s", r5.originalRequest.urlString());
            this.responseCallback = callback;
            this.forWebSocket = z;
        }

        String host() {
            return Call.this.originalRequest.url().getHost();
        }

        Request request() {
            return Call.this.originalRequest;
        }

        Object tag() {
            return Call.this.originalRequest.tag();
        }

        void cancel() {
            Call.this.cancel();
        }

        Call get() {
            return Call.this;
        }

        protected void execute() {
            Throwable e;
            Object obj = 1;
            Object obj2 = null;
            try {
                Response access$100 = Call.this.getResponseWithInterceptorChain(this.forWebSocket);
                if (Call.this.canceled) {
                    try {
                        this.responseCallback.onFailure(Call.this.originalRequest, new IOException("Canceled"));
                    } catch (IOException e2) {
                        e = e2;
                        if (obj == null) {
                            try {
                                Internal.logger.log(Level.INFO, "Callback failure for " + Call.this.toLoggableString(), e);
                            } catch (Throwable th) {
                                Call.this.client.getDispatcher().finished(this);
                            }
                        } else {
                            this.responseCallback.onFailure(Call.this.engine.getRequest(), e);
                        }
                        Call.this.client.getDispatcher().finished(this);
                    }
                }
                this.responseCallback.onResponse(access$100);
                Call.this.client.getDispatcher().finished(this);
            } catch (IOException e3) {
                e = e3;
                obj = obj2;
                if (obj == null) {
                    this.responseCallback.onFailure(Call.this.engine.getRequest(), e);
                } else {
                    Internal.logger.log(Level.INFO, "Callback failure for " + Call.this.toLoggableString(), e);
                }
                Call.this.client.getDispatcher().finished(this);
            }
        }
    }

    Call(OkHttpClient okHttpClient, Request request) {
        this.client = okHttpClient.copyWithDefaults();
        this.originalRequest = request;
    }

    public Response execute() {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        try {
            this.client.getDispatcher().executed(this);
            Response responseWithInterceptorChain = getResponseWithInterceptorChain(false);
            if (responseWithInterceptorChain != null) {
                return responseWithInterceptorChain;
            }
            throw new IOException("Canceled");
        } finally {
            this.client.getDispatcher().finished(this);
        }
    }

    Object tag() {
        return this.originalRequest.tag();
    }

    public void enqueue(Callback callback) {
        enqueue(callback, false);
    }

    void enqueue(Callback callback, boolean z) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        this.client.getDispatcher().enqueue(new AsyncCall(callback, z, null));
    }

    public void cancel() {
        this.canceled = true;
        if (this.engine != null) {
            this.engine.disconnect();
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    private String toLoggableString() {
        String str = this.canceled ? "canceled call" : "call";
        try {
            str = str + " to " + new URL(this.originalRequest.url(), "/...").toString();
        } catch (MalformedURLException e) {
        }
        return str;
    }

    private Response getResponseWithInterceptorChain(boolean z) {
        return new ApplicationInterceptorChain(0, this.originalRequest, z).proceed(this.originalRequest);
    }

    Response getResponse(Request request, boolean z) {
        Request build;
        HttpEngine recover;
        RequestBody body = request.body();
        if (body != null) {
            Builder newBuilder = request.newBuilder();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                newBuilder.header("Content-Type", contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                newBuilder.header("Content-Length", Long.toString(contentLength));
                newBuilder.removeHeader("Transfer-Encoding");
            } else {
                newBuilder.header("Transfer-Encoding", "chunked");
                newBuilder.removeHeader("Content-Length");
            }
            build = newBuilder.build();
        } else {
            build = request;
        }
        this.engine = new HttpEngine(this.client, build, false, false, z, null, null, null, null);
        int i = 0;
        while (!this.canceled) {
            try {
                this.engine.sendRequest();
                this.engine.readResponse();
                Response response = this.engine.getResponse();
                build = this.engine.followUpRequest();
                if (build == null) {
                    if (!z) {
                        this.engine.releaseConnection();
                    }
                    return response;
                }
                int i2 = i + 1;
                if (i2 > 20) {
                    throw new ProtocolException("Too many follow-up requests: " + i2);
                }
                if (!this.engine.sameConnection(build.url())) {
                    this.engine.releaseConnection();
                }
                this.engine = new HttpEngine(this.client, build, false, false, z, this.engine.close(), null, null, response);
                i = i2;
            } catch (RequestException e) {
                throw e.getCause();
            } catch (RouteException e2) {
                recover = this.engine.recover(e2);
                if (recover != null) {
                    this.engine = recover;
                } else {
                    throw e2.getLastConnectException();
                }
            } catch (IOException e3) {
                recover = this.engine.recover(e3, null);
                if (recover != null) {
                    this.engine = recover;
                } else {
                    throw e3;
                }
            }
        }
        this.engine.releaseConnection();
        throw new IOException("Canceled");
    }
}
