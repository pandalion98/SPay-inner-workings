/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalStateException
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.MalformedURLException
 *  java.net.ProtocolException
 *  java.net.URL
 *  java.util.List
 *  java.util.logging.Level
 *  java.util.logging.Logger
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.NamedRunnable;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.RequestException;
import com.squareup.okhttp.internal.http.RetryableSink;
import com.squareup.okhttp.internal.http.RouteException;
import com.squareup.okhttp.internal.http.RouteSelector;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Sink;

public class Call {
    volatile boolean canceled;
    private final OkHttpClient client;
    HttpEngine engine;
    private boolean executed;
    Request originalRequest;

    Call(OkHttpClient okHttpClient, Request request) {
        this.client = okHttpClient.copyWithDefaults();
        this.originalRequest = request;
    }

    static /* synthetic */ Response access$100(Call call, boolean bl) {
        return call.getResponseWithInterceptorChain(bl);
    }

    static /* synthetic */ String access$200(Call call) {
        return call.toLoggableString();
    }

    private Response getResponseWithInterceptorChain(boolean bl) {
        return new ApplicationInterceptorChain(0, this.originalRequest, bl).proceed(this.originalRequest);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private String toLoggableString() {
        String string = this.canceled ? "canceled call" : "call";
        try {
            String string2 = new URL(this.originalRequest.url(), "/...").toString();
            return string + " to " + string2;
        }
        catch (MalformedURLException malformedURLException) {
            return string;
        }
    }

    public void cancel() {
        this.canceled = true;
        if (this.engine != null) {
            this.engine.disconnect();
        }
    }

    public void enqueue(Callback callback) {
        this.enqueue(callback, false);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void enqueue(Callback callback, boolean bl) {
        Call call = this;
        synchronized (call) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        this.client.getDispatcher().enqueue(new AsyncCall(callback, bl));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Response execute() {
        Call call = this;
        synchronized (call) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        try {
            this.client.getDispatcher().executed(this);
            Response response = this.getResponseWithInterceptorChain(false);
            if (response != null) return response;
            throw new IOException("Canceled");
        }
        finally {
            this.client.getDispatcher().finished(this);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    Response getResponse(Request request, boolean bl) {
        Request request2;
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            long l2;
            Request.Builder builder = request.newBuilder();
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null) {
                builder.header("Content-Type", mediaType.toString());
            }
            if ((l2 = requestBody.contentLength()) != -1L) {
                builder.header("Content-Length", Long.toString((long)l2));
                builder.removeHeader("Transfer-Encoding");
            } else {
                builder.header("Transfer-Encoding", "chunked");
                builder.removeHeader("Content-Length");
            }
            request2 = builder.build();
        } else {
            request2 = request;
        }
        this.engine = new HttpEngine(this.client, request2, false, false, bl, null, null, null, null);
        int n2 = 0;
        do {
            Request request3;
            Response response;
            block17 : {
                if (this.canceled) {
                    this.engine.releaseConnection();
                    throw new IOException("Canceled");
                }
                try {
                    this.engine.sendRequest();
                    this.engine.readResponse();
                }
                catch (RequestException requestException) {
                    throw requestException.getCause();
                }
                catch (RouteException routeException) {
                    HttpEngine httpEngine = this.engine.recover(routeException);
                    if (httpEngine == null) {
                        throw routeException.getLastConnectException();
                    }
                    this.engine = httpEngine;
                }
                catch (IOException iOException) {
                    HttpEngine httpEngine = this.engine.recover(iOException, null);
                    if (httpEngine == null) {
                        throw iOException;
                    }
                    this.engine = httpEngine;
                }
                response = this.engine.getResponse();
                request3 = this.engine.followUpRequest();
                if (request3 == null) {
                    if (!bl) {
                        this.engine.releaseConnection();
                    }
                    return response;
                }
                break block17;
                continue;
            }
            int n3 = n2 + 1;
            if (n3 > 20) {
                throw new ProtocolException("Too many follow-up requests: " + n3);
            }
            if (!this.engine.sameConnection(request3.url())) {
                this.engine.releaseConnection();
            }
            Connection connection = this.engine.close();
            this.engine = new HttpEngine(this.client, request3, false, false, bl, connection, null, null, response);
            n2 = n3;
        } while (true);
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    Object tag() {
        return this.originalRequest.tag();
    }

    class ApplicationInterceptorChain
    implements Interceptor.Chain {
        private final boolean forWebSocket;
        private final int index;
        private final Request request;

        ApplicationInterceptorChain(int n2, Request request, boolean bl) {
            this.index = n2;
            this.request = request;
            this.forWebSocket = bl;
        }

        @Override
        public Connection connection() {
            return null;
        }

        @Override
        public Response proceed(Request request) {
            if (this.index < Call.this.client.interceptors().size()) {
                ApplicationInterceptorChain applicationInterceptorChain = new ApplicationInterceptorChain(1 + this.index, request, this.forWebSocket);
                return ((Interceptor)Call.this.client.interceptors().get(this.index)).intercept(applicationInterceptorChain);
            }
            return Call.this.getResponse(request, this.forWebSocket);
        }

        @Override
        public Request request() {
            return this.request;
        }
    }

    final class AsyncCall
    extends NamedRunnable {
        private final boolean forWebSocket;
        private final Callback responseCallback;

        private AsyncCall(Callback callback, boolean bl) {
            Object[] arrobject = new Object[]{Call.this.originalRequest.urlString()};
            super("OkHttp %s", arrobject);
            this.responseCallback = callback;
            this.forWebSocket = bl;
        }

        void cancel() {
            Call.this.cancel();
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive exception aggregation
         */
        @Override
        protected void execute() {
            var1_1 = true;
            try {
                block14 : {
                    var4_2 = Call.access$100(Call.this, this.forWebSocket);
                    var5_3 = Call.this.canceled;
                    if (!var5_3) break block14;
                    this.responseCallback.onFailure(Call.this.originalRequest, new IOException("Canceled"));
lbl9: // 2 sources:
                    do {
                        return;
                        break;
                    } while (true);
                }
                try {
                    this.responseCallback.onResponse(var4_2);
                    ** continue;
                }
                catch (IOException var2_4) lbl-1000: // 2 sources:
                {
                    do {
                        if (var1_1) {
                            Internal.logger.log(Level.INFO, "Callback failure for " + Call.access$200(Call.this), (Throwable)var2_5);
lbl19: // 2 sources:
                            do {
                                Call.access$300(Call.this).getDispatcher().finished(this);
                                return;
                                break;
                            } while (true);
                        }
                        this.responseCallback.onFailure(Call.this.engine.getRequest(), (IOException)var2_5);
                        ** continue;
                        break;
                    } while (true);
                }
            }
            finally {
                Call.access$300(Call.this).getDispatcher().finished(this);
            }
            {
                catch (IOException var2_6) {
                    var1_1 = false;
                    ** continue;
                }
            }
        }

        Call get() {
            return Call.this;
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
    }

}

