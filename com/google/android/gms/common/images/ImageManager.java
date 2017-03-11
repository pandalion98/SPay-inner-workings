package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.internal.zzkm;
import com.google.android.gms.internal.zzkv;
import com.google.android.gms.internal.zzme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public final class ImageManager {
    private static final Object zzOl;
    private static HashSet<Uri> zzOm;
    private static ImageManager zzOn;
    private static ImageManager zzOo;
    private final Context mContext;
    private final Handler mHandler;
    private final ExecutorService zzOp;
    private final zzb zzOq;
    private final zzkm zzOr;
    private final Map<zza, ImageReceiver> zzOs;
    private final Map<Uri, ImageReceiver> zzOt;
    private final Map<Uri, Long> zzOu;

    private final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        private final ArrayList<zza> zzOv;
        final /* synthetic */ ImageManager zzOw;

        ImageReceiver(ImageManager imageManager, Uri uri) {
            this.zzOw = imageManager;
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
            this.zzOv = new ArrayList();
        }

        public void onReceiveResult(int i, Bundle bundle) {
            this.zzOw.zzOp.execute(new zzc(this.zzOw, this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }

        public void zzb(zza com_google_android_gms_common_images_zza) {
            com.google.android.gms.common.internal.zzb.zzbd("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zzOv.add(com_google_android_gms_common_images_zza);
        }

        public void zzc(zza com_google_android_gms_common_images_zza) {
            com.google.android.gms.common.internal.zzb.zzbd("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zzOv.remove(com_google_android_gms_common_images_zza);
        }

        public void zziH() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.putExtra("com.google.android.gms.extras.uri", this.mUri);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            this.zzOw.mContext.sendBroadcast(intent);
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    private static final class zza {
        static int zza(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    private static final class zzb extends zzkv<zza, Bitmap> {
        public zzb(Context context) {
            super(zzO(context));
        }

        private static int zzO(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            int memoryClass = (((context.getApplicationInfo().flags & PKIFailureInfo.badCertTemplate) != 0 ? 1 : null) == null || !zzme.zzkd()) ? activityManager.getMemoryClass() : zza.zza(activityManager);
            return (int) (((float) (memoryClass * PKIFailureInfo.badCertTemplate)) * 0.33f);
        }

        protected /* synthetic */ void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
            zza(z, (zza) obj, (Bitmap) obj2, (Bitmap) obj3);
        }

        protected /* synthetic */ int sizeOf(Object obj, Object obj2) {
            return zza((zza) obj, (Bitmap) obj2);
        }

        protected int zza(zza com_google_android_gms_common_images_zza_zza, Bitmap bitmap) {
            return bitmap.getHeight() * bitmap.getRowBytes();
        }

        protected void zza(boolean z, zza com_google_android_gms_common_images_zza_zza, Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, com_google_android_gms_common_images_zza_zza, bitmap, bitmap2);
        }
    }

    private final class zzc implements Runnable {
        private final Uri mUri;
        final /* synthetic */ ImageManager zzOw;
        private final ParcelFileDescriptor zzOx;

        public zzc(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.zzOw = imageManager;
            this.mUri = uri;
            this.zzOx = parcelFileDescriptor;
        }

        public void run() {
            com.google.android.gms.common.internal.zzb.zzbe("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            boolean z = false;
            Bitmap bitmap = null;
            if (this.zzOx != null) {
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(this.zzOx.getFileDescriptor());
                } catch (Throwable e) {
                    Log.e("ImageManager", "OOM while loading bitmap for uri: " + this.mUri, e);
                    z = true;
                }
                try {
                    this.zzOx.close();
                } catch (Throwable e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.zzOw.mHandler.post(new zzf(this.zzOw, this.mUri, bitmap, z, countDownLatch));
            try {
                countDownLatch.await();
            } catch (InterruptedException e3) {
                Log.w("ImageManager", "Latch interrupted while posting " + this.mUri);
            }
        }
    }

    private final class zzd implements Runnable {
        final /* synthetic */ ImageManager zzOw;
        private final zza zzOy;

        public zzd(ImageManager imageManager, zza com_google_android_gms_common_images_zza) {
            this.zzOw = imageManager;
            this.zzOy = com_google_android_gms_common_images_zza;
        }

        public void run() {
            com.google.android.gms.common.internal.zzb.zzbd("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) this.zzOw.zzOs.get(this.zzOy);
            if (imageReceiver != null) {
                this.zzOw.zzOs.remove(this.zzOy);
                imageReceiver.zzc(this.zzOy);
            }
            zza com_google_android_gms_common_images_zza_zza = this.zzOy.zzOA;
            if (com_google_android_gms_common_images_zza_zza.uri == null) {
                this.zzOy.zza(this.zzOw.mContext, this.zzOw.zzOr, true);
                return;
            }
            Bitmap zza = this.zzOw.zza(com_google_android_gms_common_images_zza_zza);
            if (zza != null) {
                this.zzOy.zza(this.zzOw.mContext, zza, true);
                return;
            }
            Long l = (Long) this.zzOw.zzOu.get(com_google_android_gms_common_images_zza_zza.uri);
            if (l != null) {
                if (SystemClock.elapsedRealtime() - l.longValue() < 3600000) {
                    this.zzOy.zza(this.zzOw.mContext, this.zzOw.zzOr, true);
                    return;
                }
                this.zzOw.zzOu.remove(com_google_android_gms_common_images_zza_zza.uri);
            }
            this.zzOy.zza(this.zzOw.mContext, this.zzOw.zzOr);
            imageReceiver = (ImageReceiver) this.zzOw.zzOt.get(com_google_android_gms_common_images_zza_zza.uri);
            if (imageReceiver == null) {
                imageReceiver = new ImageReceiver(this.zzOw, com_google_android_gms_common_images_zza_zza.uri);
                this.zzOw.zzOt.put(com_google_android_gms_common_images_zza_zza.uri, imageReceiver);
            }
            imageReceiver.zzb(this.zzOy);
            if (!(this.zzOy instanceof com.google.android.gms.common.images.zza.zzc)) {
                this.zzOw.zzOs.put(this.zzOy, imageReceiver);
            }
            synchronized (ImageManager.zzOl) {
                if (!ImageManager.zzOm.contains(com_google_android_gms_common_images_zza_zza.uri)) {
                    ImageManager.zzOm.add(com_google_android_gms_common_images_zza_zza.uri);
                    imageReceiver.zziH();
                }
            }
        }
    }

    private static final class zze implements ComponentCallbacks2 {
        private final zzb zzOq;

        public zze(zzb com_google_android_gms_common_images_ImageManager_zzb) {
            this.zzOq = com_google_android_gms_common_images_ImageManager_zzb;
        }

        public void onConfigurationChanged(Configuration configuration) {
        }

        public void onLowMemory() {
            this.zzOq.evictAll();
        }

        public void onTrimMemory(int i) {
            if (i >= 60) {
                this.zzOq.evictAll();
            } else if (i >= 20) {
                this.zzOq.trimToSize(this.zzOq.size() / 2);
            }
        }
    }

    private final class zzf implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        final /* synthetic */ ImageManager zzOw;
        private boolean zzOz;
        private final CountDownLatch zzmx;

        public zzf(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.zzOw = imageManager;
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzOz = z;
            this.zzmx = countDownLatch;
        }

        private void zza(ImageReceiver imageReceiver, boolean z) {
            ArrayList zza = imageReceiver.zzOv;
            int size = zza.size();
            for (int i = 0; i < size; i++) {
                zza com_google_android_gms_common_images_zza = (zza) zza.get(i);
                if (z) {
                    com_google_android_gms_common_images_zza.zza(this.zzOw.mContext, this.mBitmap, false);
                } else {
                    this.zzOw.zzOu.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                    com_google_android_gms_common_images_zza.zza(this.zzOw.mContext, this.zzOw.zzOr, false);
                }
                if (!(com_google_android_gms_common_images_zza instanceof com.google.android.gms.common.images.zza.zzc)) {
                    this.zzOw.zzOs.remove(com_google_android_gms_common_images_zza);
                }
            }
        }

        public void run() {
            com.google.android.gms.common.internal.zzb.zzbd("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.mBitmap != null;
            if (this.zzOw.zzOq != null) {
                if (this.zzOz) {
                    this.zzOw.zzOq.evictAll();
                    System.gc();
                    this.zzOz = false;
                    this.zzOw.mHandler.post(this);
                    return;
                } else if (z) {
                    this.zzOw.zzOq.put(new zza(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) this.zzOw.zzOt.remove(this.mUri);
            if (imageReceiver != null) {
                zza(imageReceiver, z);
            }
            this.zzmx.countDown();
            synchronized (ImageManager.zzOl) {
                ImageManager.zzOm.remove(this.mUri);
            }
        }
    }

    static {
        zzOl = new Object();
        zzOm = new HashSet();
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.zzOp = Executors.newFixedThreadPool(4);
        if (z) {
            this.zzOq = new zzb(this.mContext);
            if (zzme.zzkg()) {
                zziE();
            }
        } else {
            this.zzOq = null;
        }
        this.zzOr = new zzkm();
        this.zzOs = new HashMap();
        this.zzOt = new HashMap();
        this.zzOu = new HashMap();
    }

    public static ImageManager create(Context context) {
        return zzb(context, false);
    }

    private Bitmap zza(zza com_google_android_gms_common_images_zza_zza) {
        return this.zzOq == null ? null : (Bitmap) this.zzOq.get(com_google_android_gms_common_images_zza_zza);
    }

    public static ImageManager zzb(Context context, boolean z) {
        if (z) {
            if (zzOo == null) {
                zzOo = new ImageManager(context, true);
            }
            return zzOo;
        }
        if (zzOn == null) {
            zzOn = new ImageManager(context, false);
        }
        return zzOn;
    }

    private void zziE() {
        this.mContext.registerComponentCallbacks(new zze(this.zzOq));
    }

    public void loadImage(ImageView imageView, int i) {
        zza(new com.google.android.gms.common.images.zza.zzb(imageView, i));
    }

    public void loadImage(ImageView imageView, Uri uri) {
        zza(new com.google.android.gms.common.images.zza.zzb(imageView, uri));
    }

    public void loadImage(ImageView imageView, Uri uri, int i) {
        zza com_google_android_gms_common_images_zza_zzb = new com.google.android.gms.common.images.zza.zzb(imageView, uri);
        com_google_android_gms_common_images_zza_zzb.zzaC(i);
        zza(com_google_android_gms_common_images_zza_zzb);
    }

    public void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        zza(new com.google.android.gms.common.images.zza.zzc(onImageLoadedListener, uri));
    }

    public void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        zza com_google_android_gms_common_images_zza_zzc = new com.google.android.gms.common.images.zza.zzc(onImageLoadedListener, uri);
        com_google_android_gms_common_images_zza_zzc.zzaC(i);
        zza(com_google_android_gms_common_images_zza_zzc);
    }

    public void zza(zza com_google_android_gms_common_images_zza) {
        com.google.android.gms.common.internal.zzb.zzbd("ImageManager.loadImage() must be called in the main thread");
        new zzd(this, com_google_android_gms_common_images_zza).run();
    }
}
