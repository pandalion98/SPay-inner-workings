package com.android.volley;

import android.os.Handler;
import java.util.concurrent.Executor;

/* renamed from: com.android.volley.d */
public class ExecutorDelivery implements ResponseDelivery {
    private final Executor ap;

    /* renamed from: com.android.volley.d.1 */
    class ExecutorDelivery implements Executor {
        final /* synthetic */ Handler aq;
        final /* synthetic */ ExecutorDelivery ar;

        ExecutorDelivery(ExecutorDelivery executorDelivery, Handler handler) {
            this.ar = executorDelivery;
            this.aq = handler;
        }

        public void execute(Runnable runnable) {
            this.aq.post(runnable);
        }
    }

    /* renamed from: com.android.volley.d.a */
    private class ExecutorDelivery implements Runnable {
        final /* synthetic */ ExecutorDelivery ar;
        private final Request as;
        private final Response at;
        private final Runnable mRunnable;

        public ExecutorDelivery(ExecutorDelivery executorDelivery, Request request, Response response, Runnable runnable) {
            this.ar = executorDelivery;
            this.as = request;
            this.at = response;
            this.mRunnable = runnable;
        }

        public void run() {
            if (this.as.isCanceled()) {
                this.as.m23f("canceled-at-delivery");
                return;
            }
            if (this.at.isSuccess()) {
                this.as.m18a(this.at.result);
            } else {
                this.as.m21c(this.at.aZ);
            }
            if (this.at.ba) {
                this.as.m22c("intermediate-response");
            } else {
                this.as.m23f("done");
            }
            if (this.mRunnable != null) {
                this.mRunnable.run();
            }
        }
    }

    public ExecutorDelivery(Handler handler) {
        this.ap = new ExecutorDelivery(this, handler);
    }

    public void m117a(Request<?> request, Response<?> response) {
        m118a(request, response, null);
    }

    public void m118a(Request<?> request, Response<?> response, Runnable runnable) {
        request.m37x();
        request.m22c("post-response");
        this.ap.execute(new ExecutorDelivery(this, request, response, runnable));
    }

    public void m116a(Request<?> request, VolleyError volleyError) {
        request.m22c("post-error");
        this.ap.execute(new ExecutorDelivery(this, request, Response.m125d(volleyError), null));
    }
}
