package com.google.android.gms.common.images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.internal.zzkj;
import com.google.android.gms.internal.zzkk;
import com.google.android.gms.internal.zzkl;
import com.google.android.gms.internal.zzkm;
import java.lang.ref.WeakReference;

public abstract class zza {
    final zza zzOA;
    protected int zzOB;
    protected int zzOC;
    protected boolean zzOD;
    protected OnImageLoadedListener zzOE;
    private boolean zzOF;
    private boolean zzOG;
    private boolean zzOH;
    protected int zzOI;

    static final class zza {
        public final Uri uri;

        public zza(Uri uri) {
            this.uri = uri;
        }

        public boolean equals(Object obj) {
            return !(obj instanceof zza) ? false : this == obj ? true : zzw.equal(((zza) obj).uri, this.uri);
        }

        public int hashCode() {
            return zzw.hashCode(this.uri);
        }
    }

    public static final class zzb extends zza {
        private WeakReference<ImageView> zzOJ;

        public zzb(ImageView imageView, int i) {
            super(null, i);
            com.google.android.gms.common.internal.zzb.zzh(imageView);
            this.zzOJ = new WeakReference(imageView);
        }

        public zzb(ImageView imageView, Uri uri) {
            super(uri, 0);
            com.google.android.gms.common.internal.zzb.zzh(imageView);
            this.zzOJ = new WeakReference(imageView);
        }

        private void zza(ImageView imageView, Drawable drawable, boolean z, boolean z2, boolean z3) {
            Object obj = (z2 || z3) ? null : 1;
            if (obj != null && (imageView instanceof zzkl)) {
                int zziK = ((zzkl) imageView).zziK();
                if (this.zzOC != 0 && zziK == this.zzOC) {
                    return;
                }
            }
            boolean zzc = zzc(z, z2);
            Drawable newDrawable = (!this.zzOD || drawable == null) ? drawable : drawable.getConstantState().newDrawable();
            if (zzc) {
                newDrawable = zza(imageView.getDrawable(), newDrawable);
            }
            imageView.setImageDrawable(newDrawable);
            if (imageView instanceof zzkl) {
                zzkl com_google_android_gms_internal_zzkl = (zzkl) imageView;
                com_google_android_gms_internal_zzkl.zzi(z3 ? this.zzOA.uri : null);
                com_google_android_gms_internal_zzkl.zzaE(obj != null ? this.zzOC : 0);
            }
            if (zzc) {
                ((zzkj) newDrawable).startTransition(250);
            }
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof zzb)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            ImageView imageView = (ImageView) this.zzOJ.get();
            ImageView imageView2 = (ImageView) ((zzb) obj).zzOJ.get();
            boolean z = (imageView2 == null || imageView == null || !zzw.equal(imageView2, imageView)) ? false : true;
            return z;
        }

        public int hashCode() {
            return 0;
        }

        protected void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
            ImageView imageView = (ImageView) this.zzOJ.get();
            if (imageView != null) {
                zza(imageView, drawable, z, z2, z3);
            }
        }
    }

    public static final class zzc extends zza {
        private WeakReference<OnImageLoadedListener> zzOK;

        public zzc(OnImageLoadedListener onImageLoadedListener, Uri uri) {
            super(uri, 0);
            com.google.android.gms.common.internal.zzb.zzh(onImageLoadedListener);
            this.zzOK = new WeakReference(onImageLoadedListener);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof zzc)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            zzc com_google_android_gms_common_images_zza_zzc = (zzc) obj;
            OnImageLoadedListener onImageLoadedListener = (OnImageLoadedListener) this.zzOK.get();
            OnImageLoadedListener onImageLoadedListener2 = (OnImageLoadedListener) com_google_android_gms_common_images_zza_zzc.zzOK.get();
            boolean z = onImageLoadedListener2 != null && onImageLoadedListener != null && zzw.equal(onImageLoadedListener2, onImageLoadedListener) && zzw.equal(com_google_android_gms_common_images_zza_zzc.zzOA, this.zzOA);
            return z;
        }

        public int hashCode() {
            return zzw.hashCode(this.zzOA);
        }

        protected void zza(Drawable drawable, boolean z, boolean z2, boolean z3) {
            if (!z2) {
                OnImageLoadedListener onImageLoadedListener = (OnImageLoadedListener) this.zzOK.get();
                if (onImageLoadedListener != null) {
                    onImageLoadedListener.onImageLoaded(this.zzOA.uri, drawable, z3);
                }
            }
        }
    }

    public zza(Uri uri, int i) {
        this.zzOB = 0;
        this.zzOC = 0;
        this.zzOD = false;
        this.zzOF = true;
        this.zzOG = false;
        this.zzOH = true;
        this.zzOA = new zza(uri);
        this.zzOC = i;
    }

    private Drawable zza(Context context, zzkm com_google_android_gms_internal_zzkm, int i) {
        Resources resources = context.getResources();
        if (this.zzOI <= 0) {
            return resources.getDrawable(i);
        }
        com.google.android.gms.internal.zzkm.zza com_google_android_gms_internal_zzkm_zza = new com.google.android.gms.internal.zzkm.zza(i, this.zzOI);
        Drawable drawable = (Drawable) com_google_android_gms_internal_zzkm.get(com_google_android_gms_internal_zzkm_zza);
        if (drawable != null) {
            return drawable;
        }
        drawable = resources.getDrawable(i);
        if ((this.zzOI & 1) != 0) {
            drawable = zza(resources, drawable);
        }
        com_google_android_gms_internal_zzkm.put(com_google_android_gms_internal_zzkm_zza, drawable);
        return drawable;
    }

    protected Drawable zza(Resources resources, Drawable drawable) {
        return zzkk.zza(resources, drawable);
    }

    protected zzkj zza(Drawable drawable, Drawable drawable2) {
        if (drawable == null) {
            drawable = null;
        } else if (drawable instanceof zzkj) {
            drawable = ((zzkj) drawable).zziI();
        }
        return new zzkj(drawable, drawable2);
    }

    void zza(Context context, Bitmap bitmap, boolean z) {
        com.google.android.gms.common.internal.zzb.zzh(bitmap);
        if ((this.zzOI & 1) != 0) {
            bitmap = zzkk.zza(bitmap);
        }
        Drawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        if (this.zzOE != null) {
            this.zzOE.onImageLoaded(this.zzOA.uri, bitmapDrawable, true);
        }
        zza(bitmapDrawable, z, false, true);
    }

    void zza(Context context, zzkm com_google_android_gms_internal_zzkm) {
        if (this.zzOH) {
            Drawable drawable = null;
            if (this.zzOB != 0) {
                drawable = zza(context, com_google_android_gms_internal_zzkm, this.zzOB);
            }
            zza(drawable, false, true, false);
        }
    }

    void zza(Context context, zzkm com_google_android_gms_internal_zzkm, boolean z) {
        Drawable drawable = null;
        if (this.zzOC != 0) {
            drawable = zza(context, com_google_android_gms_internal_zzkm, this.zzOC);
        }
        if (this.zzOE != null) {
            this.zzOE.onImageLoaded(this.zzOA.uri, drawable, false);
        }
        zza(drawable, z, false, false);
    }

    protected abstract void zza(Drawable drawable, boolean z, boolean z2, boolean z3);

    public void zzaC(int i) {
        this.zzOC = i;
    }

    protected boolean zzc(boolean z, boolean z2) {
        return this.zzOF && !z2 && (!z || this.zzOG);
    }
}
