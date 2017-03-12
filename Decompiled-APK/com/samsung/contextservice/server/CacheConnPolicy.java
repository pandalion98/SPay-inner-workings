package com.samsung.contextservice.server;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.contextservice.p029b.CSlog;

/* renamed from: com.samsung.contextservice.server.a */
public class CacheConnPolicy extends RemoteConnPolicy {
    public CacheConnPolicy(Context context, String str, long j, int i) {
        super(context, str, 2, "cachegap", "cachecap");
    }

    protected long gt() {
        long j = 3600000;
        try {
            j = ServerConfig.gB().getMinContextCallDelay();
        } catch (Throwable e) {
            CSlog.m1406c("RemoteConnPolicy", "cannot get min delay", e);
        }
        return j;
    }

    protected int gu() {
        int i = 2;
        try {
            i = ServerConfig.gB().getContextCacheQuotaPerDay();
        } catch (Throwable e) {
            CSlog.m1406c("RemoteConnPolicy", "cannot get cap", e);
        }
        return i;
    }

    protected synchronized void execute() {
        if (m1423e(System.currentTimeMillis(), (long) gu())) {
            if (gy()) {
                CSlog.m1408d("RemoteConnPolicy", "cache polling reach maximum, cap=" + gu());
            } else {
                while (!isEmpty()) {
                    try {
                        RequestBundle gx = gx();
                        if (gx != null) {
                            CacheRequest cacheRequest = (CacheRequest) gx.getRequest();
                            C0413a gz = gx.gz();
                            if (!(cacheRequest == null || gz == null)) {
                                cacheRequest.m836a(gz);
                                CSlog.m1408d("RemoteConnPolicy", "Cache queue items number:" + size() + ", lastPing=" + gw());
                                clear();
                                break;
                            }
                        }
                    } catch (Throwable e) {
                        CSlog.m1403a("RemoteConnPolicy", "Cache execute,", e);
                    }
                }
            }
        }
    }
}
