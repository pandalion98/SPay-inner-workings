package com.android.volley;

import android.annotation.TargetApi;
import android.net.TrafficStats;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

/* renamed from: com.android.volley.f */
public class NetworkDispatcher extends Thread {
    private final Cache ag;
    private final ResponseDelivery ah;
    private volatile boolean ai;
    private final BlockingQueue<Request<?>> au;
    private final Network av;

    public NetworkDispatcher(BlockingQueue<Request<?>> blockingQueue, Network network, Cache cache, ResponseDelivery responseDelivery) {
        this.ai = false;
        this.au = blockingQueue;
        this.av = network;
        this.ag = cache;
        this.ah = responseDelivery;
    }

    public void quit() {
        this.ai = true;
        interrupt();
    }

    @TargetApi(14)
    private void m119b(Request<?> request) {
        if (VERSION.SDK_INT >= 14) {
            TrafficStats.setThreadStatsTag(request.m24k());
        }
    }

    public void run() {
        Process.setThreadPriority(10);
        while (true) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            try {
                Request request = (Request) this.au.take();
                try {
                    request.m22c("network-queue-take");
                    if (request.isCanceled()) {
                        request.m23f("network-discard-cancelled");
                    } else {
                        m119b(request);
                        NetworkResponse a = this.av.m41a(request);
                        request.m22c("network-http-complete");
                        if (a.ax && request.m38y()) {
                            request.m23f("not-modified");
                        } else {
                            Response a2 = request.m17a(a);
                            request.m22c("network-parse-complete");
                            if (request.m34u() && a2.aY != null) {
                                this.ag.m57a(request.m25l(), a2.aY);
                                request.m22c("network-cache-written");
                            }
                            request.m37x();
                            this.ah.m114a(request, a2);
                        }
                    }
                } catch (VolleyError e) {
                    e.m9a(SystemClock.elapsedRealtime() - elapsedRealtime);
                    m120b(request, e);
                } catch (Throwable e2) {
                    VolleyLog.m130a(e2, "Unhandled exception %s", e2.toString());
                    VolleyError volleyError = new VolleyError(e2);
                    volleyError.m9a(SystemClock.elapsedRealtime() - elapsedRealtime);
                    this.ah.m113a(request, volleyError);
                }
            } catch (InterruptedException e3) {
                if (this.ai) {
                    return;
                }
            }
        }
    }

    private void m120b(Request<?> request, VolleyError volleyError) {
        this.ah.m113a((Request) request, request.m19b(volleyError));
    }
}
