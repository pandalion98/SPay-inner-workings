package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class zzqf {
    private static final String TAG;
    private static final long zzaoU;
    private static zzqf zzaoV;
    private final Context mContext;
    private final Handler mHandler;
    private final Runnable zzaoW;
    private ArrayList<String> zzaoX;
    private ArrayList<String> zzaoY;
    private final Object zznh;

    private class zza implements Runnable {
        final /* synthetic */ zzqf zzaoZ;

        private zza(zzqf com_google_android_gms_internal_zzqf) {
            this.zzaoZ = com_google_android_gms_internal_zzqf;
        }

        public void run() {
            synchronized (this.zzaoZ.zznh) {
                Intent intent = new Intent("com.google.android.location.places.METHOD_CALL");
                intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
                intent.putStringArrayListExtra("PLACE_IDS", this.zzaoZ.zzaoX);
                intent.putStringArrayListExtra("METHOD_NAMES", this.zzaoZ.zzaoY);
                this.zzaoZ.mContext.sendBroadcast(intent);
                this.zzaoZ.zzaoX = null;
                this.zzaoZ.zzaoY = null;
            }
        }
    }

    static {
        TAG = zzqf.class.getSimpleName();
        zzaoU = TimeUnit.SECONDS.toMillis(1);
    }

    private zzqf(Context context) {
        this((Context) zzx.zzl(context), new Handler(Looper.getMainLooper()));
    }

    zzqf(Context context, Handler handler) {
        this.zzaoW = new zza();
        this.zznh = new Object();
        this.zzaoX = null;
        this.zzaoY = null;
        this.mContext = context;
        this.mHandler = handler;
    }

    public static synchronized zzqf zzab(Context context) {
        zzqf com_google_android_gms_internal_zzqf;
        synchronized (zzqf.class) {
            zzx.zzl(context);
            if (VERSION.SDK_INT < 14) {
                com_google_android_gms_internal_zzqf = null;
            } else {
                if (zzaoV == null) {
                    zzaoV = new zzqf(context.getApplicationContext());
                }
                com_google_android_gms_internal_zzqf = zzaoV;
            }
        }
        return com_google_android_gms_internal_zzqf;
    }

    public void zzy(String str, String str2) {
        synchronized (this.zznh) {
            if (this.zzaoX == null) {
                this.zzaoX = new ArrayList();
                this.zzaoY = new ArrayList();
                this.mHandler.postDelayed(this.zzaoW, zzaoU);
            }
            this.zzaoX.add(str);
            this.zzaoY.add(str2);
            if (this.zzaoX.size() >= 10000) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Event buffer full, flushing");
                }
                this.zzaoW.run();
                this.mHandler.removeCallbacks(this.zzaoW);
                return;
            }
        }
    }
}
