package com.android.volley;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/* renamed from: com.android.volley.h */
public class RequestQueue {
    private AtomicInteger aQ;
    private final Map<String, Queue<Request<?>>> aR;
    private final Set<Request<?>> aS;
    private final PriorityBlockingQueue<Request<?>> aT;
    private final PriorityBlockingQueue<Request<?>> aU;
    private NetworkDispatcher[] aV;
    private CacheDispatcher aW;
    private List<RequestQueue> aX;
    private final Cache ag;
    private final ResponseDelivery ah;
    private final Network av;

    /* renamed from: com.android.volley.h.a */
    public interface RequestQueue<T> {
        void m121g(Request<T> request);
    }

    public RequestQueue(Cache cache, Network network, int i, ResponseDelivery responseDelivery) {
        this.aQ = new AtomicInteger();
        this.aR = new HashMap();
        this.aS = new HashSet();
        this.aT = new PriorityBlockingQueue();
        this.aU = new PriorityBlockingQueue();
        this.aX = new ArrayList();
        this.ag = cache;
        this.av = network;
        this.aV = new NetworkDispatcher[i];
        this.ah = responseDelivery;
    }

    public RequestQueue(Cache cache, Network network, int i) {
        this(cache, network, i, new ExecutorDelivery(new Handler(Looper.getMainLooper())));
    }

    public RequestQueue(Cache cache, Network network) {
        this(cache, network, 4);
    }

    public void start() {
        stop();
        this.aW = new CacheDispatcher(this.aT, this.aU, this.ag, this.ah);
        this.aW.start();
        for (int i = 0; i < this.aV.length; i++) {
            NetworkDispatcher networkDispatcher = new NetworkDispatcher(this.aU, this.av, this.ag, this.ah);
            this.aV[i] = networkDispatcher;
            networkDispatcher.start();
        }
    }

    public void stop() {
        if (this.aW != null) {
            this.aW.quit();
        }
        for (int i = 0; i < this.aV.length; i++) {
            if (this.aV[i] != null) {
                this.aV[i].quit();
            }
        }
    }

    public int getSequenceNumber() {
        return this.aQ.incrementAndGet();
    }

    public <T> Request<T> m122e(Request<T> request) {
        request.m15a(this);
        synchronized (this.aS) {
            this.aS.add(request);
        }
        request.m13a(getSequenceNumber());
        request.m22c("add-to-queue");
        if (request.m34u()) {
            synchronized (this.aR) {
                String l = request.m25l();
                if (this.aR.containsKey(l)) {
                    Queue queue = (Queue) this.aR.get(l);
                    if (queue == null) {
                        queue = new LinkedList();
                    }
                    queue.add(request);
                    this.aR.put(l, queue);
                    if (VolleyLog.DEBUG) {
                        VolleyLog.m129a("Request for cacheKey=%s is in flight, putting on hold.", l);
                    }
                } else {
                    this.aR.put(l, null);
                    this.aT.add(request);
                }
            }
        } else {
            this.aU.add(request);
        }
        return request;
    }

    <T> void m123f(Request<T> request) {
        synchronized (this.aS) {
            this.aS.remove(request);
        }
        synchronized (this.aX) {
            for (RequestQueue g : this.aX) {
                g.m121g(request);
            }
        }
        if (request.m34u()) {
            synchronized (this.aR) {
                Queue queue = (Queue) this.aR.remove(request.m25l());
                if (queue != null) {
                    if (VolleyLog.DEBUG) {
                        VolleyLog.m129a("Releasing %d waiting requests for cacheKey=%s.", Integer.valueOf(queue.size()), r2);
                    }
                    this.aT.addAll(queue);
                }
            }
        }
    }
}
