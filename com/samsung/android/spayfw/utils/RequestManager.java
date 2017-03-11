package com.samsung.android.spayfw.utils;

import android.content.Context;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.p000a.BasicNetwork;
import com.android.volley.p000a.DiskBasedCache;
import com.android.volley.p000a.HurlStack;
import com.android.volley.p000a.Volley;
import java.io.File;

/* renamed from: com.samsung.android.spayfw.utils.e */
public class RequestManager {
    private static final String TAG;
    private static RequestQueue aD;

    static {
        TAG = RequestManager.class.getSimpleName();
    }

    private RequestManager() {
    }

    public static void m1267a(Context context, Integer num) {
        if (aD == null) {
            aD = RequestManager.m1268b(context, num);
        }
    }

    public static RequestQueue fK() {
        if (aD != null) {
            return aD;
        }
        throw new IllegalStateException("Not initialized");
    }

    static RequestQueue m1268b(Context context, Integer num) {
        File file;
        RequestQueue requestQueue;
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            file = new File(externalCacheDir, "volley");
        } else {
            file = new File(context.getCacheDir(), "volley");
        }
        Network basicNetwork = new BasicNetwork(new HurlStack());
        Cache diskBasedCache = new DiskBasedCache(file, 314572800);
        diskBasedCache.initialize();
        if (num == null || num.intValue() <= 0) {
            requestQueue = new RequestQueue(diskBasedCache, basicNetwork);
        } else {
            requestQueue = new RequestQueue(diskBasedCache, basicNetwork, num.intValue());
        }
        requestQueue.start();
        Volley.m103a(context);
        return requestQueue;
    }
}
