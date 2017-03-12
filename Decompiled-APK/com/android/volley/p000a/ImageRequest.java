package com.android.volley.p000a;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView.ScaleType;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.Response.Response;
import com.android.volley.VolleyLog;
import com.google.android.gms.location.LocationStatusCodes;

/* renamed from: com.android.volley.a.h */
public class ImageRequest extends Request<Bitmap> {
    private static final Object bz;
    private final Response<Bitmap> bx;
    private final Config by;
    private final int mMaxHeight;
    private final int mMaxWidth;
    private ScaleType mScaleType;

    static {
        bz = new Object();
    }

    public ImageRequest(String str, Response<Bitmap> response, int i, int i2, ScaleType scaleType, Config config, Response response2) {
        super(0, str, response2);
        m16a(new DefaultRetryPolicy(LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, 2, 2.0f));
        this.bx = response;
        this.by = config;
        this.mMaxWidth = i;
        this.mMaxHeight = i2;
        this.mScaleType = scaleType;
    }

    public Priority m95v() {
        return Priority.LOW;
    }

    private static int m90a(int i, int i2, int i3, int i4, ScaleType scaleType) {
        if (i == 0 && i2 == 0) {
            return i3;
        }
        if (scaleType == ScaleType.FIT_XY) {
            if (i == 0) {
                return i3;
            }
            return i;
        } else if (i == 0) {
            return (int) ((((double) i2) / ((double) i4)) * ((double) i3));
        } else {
            if (i2 == 0) {
                return i;
            }
            double d = ((double) i4) / ((double) i3);
            if (scaleType == ScaleType.CENTER_CROP) {
                if (((double) i) * d < ((double) i2)) {
                    return (int) (((double) i2) / d);
                }
                return i;
            } else if (((double) i) * d > ((double) i2)) {
                return (int) (((double) i2) / d);
            } else {
                return i;
            }
        }
    }

    protected com.android.volley.Response<Bitmap> m92a(NetworkResponse networkResponse) {
        com.android.volley.Response<Bitmap> c;
        synchronized (bz) {
            try {
                c = m91c(networkResponse);
            } catch (Throwable e) {
                VolleyLog.m132c("Caught OOM for %d byte image, url=%s", Integer.valueOf(networkResponse.data.length), getUrl());
                c = com.android.volley.Response.m125d(new ParseError(e));
            }
        }
        return c;
    }

    private com.android.volley.Response<Bitmap> m91c(NetworkResponse networkResponse) {
        Object decodeByteArray;
        byte[] bArr = networkResponse.data;
        Options options = new Options();
        if (this.mMaxWidth == 0 && this.mMaxHeight == 0) {
            options.inPreferredConfig = this.by;
            decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
        } else {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            int i = options.outWidth;
            int i2 = options.outHeight;
            int a = ImageRequest.m90a(this.mMaxWidth, this.mMaxHeight, i, i2, this.mScaleType);
            int a2 = ImageRequest.m90a(this.mMaxHeight, this.mMaxWidth, i2, i, this.mScaleType);
            options.inJustDecodeBounds = false;
            options.inSampleSize = ImageRequest.m89a(i, i2, a, a2);
            Bitmap decodeByteArray2 = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            if (decodeByteArray2 == null || (decodeByteArray2.getWidth() <= a && decodeByteArray2.getHeight() <= a2)) {
                Bitmap bitmap = decodeByteArray2;
            } else {
                decodeByteArray = Bitmap.createScaledBitmap(decodeByteArray2, a, a2, true);
                decodeByteArray2.recycle();
            }
        }
        if (decodeByteArray == null) {
            return com.android.volley.Response.m125d(new ParseError(networkResponse));
        }
        return com.android.volley.Response.m124a(decodeByteArray, HttpHeaderParser.m79b(networkResponse));
    }

    protected void m93a(Bitmap bitmap) {
        this.bx.m98b(bitmap);
    }

    static int m89a(int i, int i2, int i3, int i4) {
        float f = 1.0f;
        while (((double) (f * 2.0f)) <= Math.min(((double) i) / ((double) i3), ((double) i2) / ((double) i4))) {
            f *= 2.0f;
        }
        return (int) f;
    }
}
