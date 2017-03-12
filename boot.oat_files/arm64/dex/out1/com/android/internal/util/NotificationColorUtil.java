package com.android.internal.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.VectorDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.Pair;
import com.android.internal.R;
import java.util.Arrays;
import java.util.WeakHashMap;

public class NotificationColorUtil {
    private static final String TAG = "NotificationColorUtil";
    private static NotificationColorUtil sInstance;
    private static final Object sLock = new Object();
    private final WeakHashMap<Bitmap, Pair<Boolean, Integer>> mGrayscaleBitmapCache = new WeakHashMap();
    private final int mGrayscaleIconMaxSize;
    private final ImageUtils mImageUtils = new ImageUtils();

    public static NotificationColorUtil getInstance(Context context) {
        NotificationColorUtil notificationColorUtil;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new NotificationColorUtil(context);
            }
            notificationColorUtil = sInstance;
        }
        return notificationColorUtil;
    }

    private NotificationColorUtil(Context context) {
        this.mGrayscaleIconMaxSize = context.getResources().getDimensionPixelSize(R.dimen.notification_large_icon_width);
    }

    public boolean isGrayscaleIcon(Bitmap bitmap) {
        if (bitmap.getWidth() > this.mGrayscaleIconMaxSize || bitmap.getHeight() > this.mGrayscaleIconMaxSize) {
            return false;
        }
        synchronized (sLock) {
            Pair<Boolean, Integer> cached = (Pair) this.mGrayscaleBitmapCache.get(bitmap);
            boolean result;
            if (cached == null || ((Integer) cached.second).intValue() != bitmap.getGenerationId()) {
                int generationId;
                synchronized (this.mImageUtils) {
                    result = this.mImageUtils.isGrayscale(bitmap);
                    generationId = bitmap.getGenerationId();
                }
                synchronized (sLock) {
                    this.mGrayscaleBitmapCache.put(bitmap, Pair.create(Boolean.valueOf(result), Integer.valueOf(generationId)));
                }
                return result;
            }
            result = ((Boolean) cached.first).booleanValue();
            return result;
        }
    }

    public boolean isGrayscaleIcon(Drawable d) {
        if (d == null) {
            return false;
        }
        if (d instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) d;
            if (bd.getBitmap() == null || !isGrayscaleIcon(bd.getBitmap())) {
                return false;
            }
            return true;
        } else if (d instanceof AnimationDrawable) {
            AnimationDrawable ad = (AnimationDrawable) d;
            if (ad.getNumberOfFrames() <= 0 || !isGrayscaleIcon(ad.getFrame(0))) {
                return false;
            }
            return true;
        } else if (d instanceof VectorDrawable) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGrayscaleIcon(Context context, Icon icon) {
        if (icon == null) {
            return false;
        }
        switch (icon.getType()) {
            case 1:
                return isGrayscaleIcon(icon.getBitmap());
            case 2:
                return isGrayscaleIcon(context, icon.getResId());
            default:
                return false;
        }
    }

    public boolean isGrayscaleIcon(Context context, int drawableResId) {
        boolean z = false;
        if (drawableResId != 0) {
            try {
                z = isGrayscaleIcon(context.getDrawable(drawableResId));
            } catch (NotFoundException e) {
                Log.e(TAG, "Drawable not found: " + drawableResId);
            }
        }
        return z;
    }

    public CharSequence invertCharSequenceColors(CharSequence charSequence) {
        if (!(charSequence instanceof Spanned)) {
            return charSequence;
        }
        Spanned ss = (Spanned) charSequence;
        Object[] spans = ss.getSpans(0, ss.length(), Object.class);
        CharSequence spannableStringBuilder = new SpannableStringBuilder(ss.toString());
        for (Object span : spans) {
            Object resultSpan = span;
            if (span instanceof TextAppearanceSpan) {
                resultSpan = processTextAppearanceSpan((TextAppearanceSpan) span);
            }
            spannableStringBuilder.setSpan(resultSpan, ss.getSpanStart(span), ss.getSpanEnd(span), ss.getSpanFlags(span));
        }
        return spannableStringBuilder;
    }

    private TextAppearanceSpan processTextAppearanceSpan(TextAppearanceSpan span) {
        ColorStateList colorStateList = span.getTextColor();
        if (colorStateList != null) {
            int[] colors = colorStateList.getColors();
            boolean changed = false;
            for (int i = 0; i < colors.length; i++) {
                if (ImageUtils.isGrayscale(colors[i])) {
                    if (!changed) {
                        colors = Arrays.copyOf(colors, colors.length);
                    }
                    colors[i] = processColor(colors[i]);
                    changed = true;
                }
            }
            if (changed) {
                return new TextAppearanceSpan(span.getFamily(), span.getTextStyle(), span.getTextSize(), new ColorStateList(colorStateList.getStates(), colors), span.getLinkTextColor());
            }
        }
        return span;
    }

    private int processColor(int color) {
        return Color.argb(Color.alpha(color), 255 - Color.red(color), 255 - Color.green(color), 255 - Color.blue(color));
    }
}
