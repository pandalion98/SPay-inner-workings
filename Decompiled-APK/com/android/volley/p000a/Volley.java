package com.android.volley.p000a;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build.VERSION;
import com.android.volley.RequestQueue;
import java.io.File;

/* renamed from: com.android.volley.a.k */
public class Volley {
    public static RequestQueue m104a(Context context, HttpStack httpStack) {
        File file = new File(context.getCacheDir(), "volley");
        String str = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            packageName = String.valueOf(String.valueOf(packageName));
            str = new StringBuilder(packageName.length() + 12).append(packageName).append("/").append(packageInfo.versionCode).toString();
        } catch (NameNotFoundException e) {
        }
        if (httpStack == null) {
            if (VERSION.SDK_INT >= 9) {
                httpStack = new HurlStack();
            } else {
                httpStack = new HttpClientStack(AndroidHttpClient.newInstance(str));
            }
        }
        RequestQueue requestQueue = new RequestQueue(new DiskBasedCache(file), new BasicNetwork(httpStack));
        requestQueue.start();
        return requestQueue;
    }

    public static RequestQueue m103a(Context context) {
        return Volley.m104a(context, null);
    }
}
