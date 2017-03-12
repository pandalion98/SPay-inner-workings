package com.android.volley;

import android.os.Process;
import com.android.volley.Cache.Cache;
import java.util.concurrent.BlockingQueue;

/* renamed from: com.android.volley.b */
public class CacheDispatcher extends Thread {
    private static final boolean DEBUG;
    private final BlockingQueue<Request<?>> ae;
    private final BlockingQueue<Request<?>> af;
    private final Cache ag;
    private final ResponseDelivery ah;
    private volatile boolean ai;

    /* renamed from: com.android.volley.b.1 */
    class CacheDispatcher implements Runnable {
        final /* synthetic */ Request aj;
        final /* synthetic */ CacheDispatcher ak;

        CacheDispatcher(CacheDispatcher cacheDispatcher, Request request) {
            this.ak = cacheDispatcher;
            this.aj = request;
        }

        public void run() {
            try {
                this.ak.af.put(this.aj);
            } catch (InterruptedException e) {
            }
        }
    }

    static {
        DEBUG = VolleyLog.DEBUG;
    }

    public CacheDispatcher(BlockingQueue<Request<?>> blockingQueue, BlockingQueue<Request<?>> blockingQueue2, Cache cache, ResponseDelivery responseDelivery) {
        this.ai = false;
        this.ae = blockingQueue;
        this.af = blockingQueue2;
        this.ag = cache;
        this.ah = responseDelivery;
    }

    public void quit() {
        this.ai = true;
        interrupt();
    }

    public void run() {
        if (DEBUG) {
            VolleyLog.m129a("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.ag.initialize();
        while (true) {
            try {
                Request request = (Request) this.ae.take();
                request.m22c("cache-queue-take");
                if (request.isCanceled()) {
                    request.m23f("cache-discard-canceled");
                } else {
                    Cache a = this.ag.m56a(request.m25l());
                    if (a == null) {
                        request.m22c("cache-miss");
                        this.af.put(request);
                    } else if (a.m39f()) {
                        request.m22c("cache-hit-expired");
                        request.m14a(a);
                        this.af.put(request);
                    } else {
                        request.m22c("cache-hit");
                        Response a2 = request.m17a(new NetworkResponse(a.data, a.ad));
                        request.m22c("cache-hit-parsed");
                        if (a.m40g()) {
                            request.m22c("cache-hit-refresh-needed");
                            request.m14a(a);
                            a2.ba = true;
                            this.ah.m115a(request, a2, new CacheDispatcher(this, request));
                        } else {
                            this.ah.m114a(request, a2);
                        }
                    }
                }
            } catch (InterruptedException e) {
                if (this.ai) {
                    return;
                }
            }
        }
    }
}
