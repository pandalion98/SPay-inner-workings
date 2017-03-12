package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Xfermode;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class Drawable {
    public static boolean DCS_FLAG_APPLY_SHADOW = false;
    static final Mode DEFAULT_TINT_MODE = Mode.SRC_IN;
    private static Class<?> SprClass = null;
    private static Method SprCreateFromStream = null;
    private static final Rect ZERO_BOUNDS_RECT = new Rect();
    protected static float mTempDensity = 1.0f;
    private Rect mBounds = ZERO_BOUNDS_RECT;
    private WeakReference<Callback> mCallback = null;
    private int mChangingConfigurations = 0;
    private String mImagePath;
    private int mLayoutDirection;
    private int mLevel = 0;
    private int[] mStateSet = StateSet.WILD_CARD;
    private boolean mVisible = true;

    public static abstract class ConstantState {
        public abstract int getChangingConfigurations();

        public abstract Drawable newDrawable();

        public Drawable newDrawable(Resources res) {
            if (res != null) {
                Drawable.mTempDensity = res.getDisplayMetrics().density;
            }
            return newDrawable();
        }

        public Drawable newDrawable(Resources res, Theme theme) {
            if (res != null) {
                Drawable.mTempDensity = res.getDisplayMetrics().density;
            }
            return newDrawable(null);
        }

        public int addAtlasableBitmaps(Collection<Bitmap> collection) {
            return 0;
        }

        protected final boolean isAtlasable(Bitmap bitmap) {
            return bitmap != null && bitmap.getConfig() == Config.ARGB_8888;
        }

        public boolean canApplyTheme() {
            return false;
        }
    }

    public interface Callback {
        void invalidateDrawable(Drawable drawable);

        void scheduleDrawable(Drawable drawable, Runnable runnable, long j);

        void unscheduleDrawable(Drawable drawable, Runnable runnable);
    }

    public abstract void draw(Canvas canvas);

    public abstract int getOpacity();

    public abstract void setAlpha(int i);

    public abstract void setColorFilter(ColorFilter colorFilter);

    public String getImagePath() {
        return this.mImagePath;
    }

    public void setImagePath(String imagePath) {
        if (Build.IS_SYSTEM_SECURE) {
            this.mImagePath = imagePath;
        }
    }

    public void setImagePath(TypedValue value) {
        if (Build.IS_SYSTEM_SECURE && value != null) {
            CharSequence path = value.coerceToString();
            if (path != null) {
                this.mImagePath = path.toString();
            }
        }
    }

    public boolean isImagePath() {
        return (this.mImagePath == null || this.mImagePath.isEmpty()) ? false : true;
    }

    public void setBounds(int left, int top, int right, int bottom) {
        Rect oldBounds = this.mBounds;
        if (oldBounds == ZERO_BOUNDS_RECT) {
            oldBounds = new Rect();
            this.mBounds = oldBounds;
        }
        if (oldBounds.left != left || oldBounds.top != top || oldBounds.right != right || oldBounds.bottom != bottom) {
            if (!oldBounds.isEmpty()) {
                invalidateSelf();
            }
            this.mBounds.set(left, top, right, bottom);
            onBoundsChange(this.mBounds);
        }
    }

    public void setBounds(Rect bounds) {
        setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public final void copyBounds(Rect bounds) {
        bounds.set(this.mBounds);
    }

    public final Rect copyBounds() {
        return new Rect(this.mBounds);
    }

    public final Rect getBounds() {
        if (this.mBounds == ZERO_BOUNDS_RECT) {
            this.mBounds = new Rect();
        }
        return this.mBounds;
    }

    public Rect getDirtyBounds() {
        return getBounds();
    }

    public void setChangingConfigurations(int configs) {
        this.mChangingConfigurations = configs;
    }

    public int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    @Deprecated
    public void setDither(boolean dither) {
    }

    public void setFilterBitmap(boolean filter) {
    }

    public boolean isFilterBitmap() {
        return false;
    }

    public final void setCallback(Callback cb) {
        this.mCallback = new WeakReference(cb);
    }

    public Callback getCallback() {
        if (this.mCallback != null) {
            return (Callback) this.mCallback.get();
        }
        return null;
    }

    public void invalidateSelf() {
        Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void scheduleSelf(Runnable what, long when) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleSelf(Runnable what) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    public int getLayoutDirection() {
        return this.mLayoutDirection;
    }

    public final boolean setLayoutDirection(int layoutDirection) {
        if (this.mLayoutDirection == layoutDirection) {
            return false;
        }
        this.mLayoutDirection = layoutDirection;
        return onLayoutDirectionChanged(layoutDirection);
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return false;
    }

    public int getAlpha() {
        return 255;
    }

    public void setXfermode(Xfermode mode) {
    }

    public void setColorFilter(int color, Mode mode) {
        setColorFilter(new PorterDuffColorFilter(color, mode));
    }

    public void setTint(int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }

    public void setTintList(ColorStateList tint) {
    }

    public void setTintMode(Mode tintMode) {
    }

    public ColorFilter getColorFilter() {
        return null;
    }

    public void clearColorFilter() {
        setColorFilter(null);
    }

    public void setHotspot(float x, float y) {
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
    }

    public void getHotspotBounds(Rect outRect) {
        outRect.set(getBounds());
    }

    public boolean isProjected() {
        return false;
    }

    public boolean isStateful() {
        return false;
    }

    public boolean setState(int[] stateSet) {
        if (Arrays.equals(this.mStateSet, stateSet)) {
            return false;
        }
        this.mStateSet = stateSet;
        return onStateChange(stateSet);
    }

    public int[] getState() {
        return this.mStateSet;
    }

    public void jumpToCurrentState() {
    }

    public Drawable getCurrent() {
        return this;
    }

    public final boolean setLevel(int level) {
        if (this.mLevel == level) {
            return false;
        }
        this.mLevel = level;
        return onLevelChange(level);
    }

    public final int getLevel() {
        return this.mLevel;
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = this.mVisible != visible;
        if (changed) {
            this.mVisible = visible;
            invalidateSelf();
        }
        return changed;
    }

    public final boolean isVisible() {
        return this.mVisible;
    }

    public void setAutoMirrored(boolean mirrored) {
    }

    public boolean isAutoMirrored() {
        return false;
    }

    public void applyTheme(Theme t) {
    }

    public boolean canApplyTheme() {
        return false;
    }

    public static int resolveOpacity(int op1, int op2) {
        if (op1 == op2) {
            return op1;
        }
        if (op1 == 0 || op2 == 0) {
            return 0;
        }
        if (op1 == -3 || op2 == -3) {
            return -3;
        }
        if (op1 == -2 || op2 == -2) {
            return -2;
        }
        return -1;
    }

    public Region getTransparentRegion() {
        return null;
    }

    protected boolean onStateChange(int[] state) {
        return false;
    }

    protected boolean onLevelChange(int level) {
        return false;
    }

    protected void onBoundsChange(Rect bounds) {
    }

    public int getIntrinsicWidth() {
        return -1;
    }

    public int getIntrinsicHeight() {
        return -1;
    }

    public int getMinimumWidth() {
        int intrinsicWidth = getIntrinsicWidth();
        return intrinsicWidth > 0 ? intrinsicWidth : 0;
    }

    public int getMinimumHeight() {
        int intrinsicHeight = getIntrinsicHeight();
        return intrinsicHeight > 0 ? intrinsicHeight : 0;
    }

    public boolean getPadding(Rect padding) {
        padding.set(0, 0, 0, 0);
        return false;
    }

    public Insets getOpticalInsets() {
        return Insets.NONE;
    }

    public void getOutline(Outline outline) {
        outline.setRect(getBounds());
        outline.setAlpha(0.0f);
    }

    public Drawable mutate() {
        return this;
    }

    public void clearMutated() {
    }

    public static Drawable createFromStream(InputStream is, String srcName) {
        Trace.traceBegin(8192, srcName != null ? srcName : "Unknown drawable");
        try {
            Drawable createFromResourceStream = createFromResourceStream(null, null, is, srcName);
            return createFromResourceStream;
        } finally {
            Trace.traceEnd(8192);
        }
    }

    public static Drawable createFromResourceStream(Resources res, TypedValue value, InputStream is, String srcName) {
        Trace.traceBegin(8192, srcName != null ? srcName : "Unknown drawable");
        try {
            Drawable createFromResourceStream = createFromResourceStream(res, value, is, srcName, null);
            return createFromResourceStream;
        } finally {
            Trace.traceEnd(8192);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.drawable.Drawable createFromResourceStream(android.content.res.Resources r13, android.util.TypedValue r14, java.io.InputStream r15, java.lang.String r16, android.graphics.BitmapFactory.Options r17) {
        /*
        if (r15 != 0) goto L_0x0004;
    L_0x0002:
        r9 = 0;
    L_0x0003:
        return r9;
    L_0x0004:
        if (r16 == 0) goto L_0x0020;
    L_0x0006:
        r1 = r16.isEmpty();	 Catch:{ Exception -> 0x0082 }
        if (r1 != 0) goto L_0x0020;
    L_0x000c:
        r1 = ".bmp";
        r0 = r16;
        r1 = r0.endsWith(r1);	 Catch:{ Exception -> 0x0082 }
        if (r1 != 0) goto L_0x0020;
    L_0x0016:
        r1 = ".spr";
        r0 = r16;
        r1 = r0.endsWith(r1);	 Catch:{ Exception -> 0x0082 }
        if (r1 == 0) goto L_0x0086;
    L_0x0020:
        r1 = r15.markSupported();	 Catch:{ Exception -> 0x0082 }
        if (r1 == 0) goto L_0x0086;
    L_0x0026:
        r1 = 3;
        r7 = new byte[r1];	 Catch:{ Exception -> 0x0082 }
        r1 = 0;
        r6 = 3;
        r15.read(r7, r1, r6);	 Catch:{ Exception -> 0x0082 }
        r15.reset();	 Catch:{ Exception -> 0x0082 }
        r1 = 0;
        r1 = r7[r1];	 Catch:{ Exception -> 0x0082 }
        r6 = 83;
        if (r1 != r6) goto L_0x0086;
    L_0x0038:
        r1 = 1;
        r1 = r7[r1];	 Catch:{ Exception -> 0x0082 }
        r6 = 80;
        if (r1 != r6) goto L_0x0086;
    L_0x003f:
        r1 = 2;
        r1 = r7[r1];	 Catch:{ Exception -> 0x0082 }
        r6 = 82;
        if (r1 != r6) goto L_0x0086;
    L_0x0046:
        r1 = SprClass;	 Catch:{ Exception -> 0x0082 }
        if (r1 != 0) goto L_0x0052;
    L_0x004a:
        r1 = "com.samsung.android.sdk.spr.drawable.SprDrawable";
        r1 = java.lang.Class.forName(r1);	 Catch:{ Exception -> 0x0082 }
        SprClass = r1;	 Catch:{ Exception -> 0x0082 }
    L_0x0052:
        r1 = SprCreateFromStream;	 Catch:{ Exception -> 0x0082 }
        if (r1 != 0) goto L_0x006d;
    L_0x0056:
        r1 = SprClass;	 Catch:{ Exception -> 0x0082 }
        r6 = "createFromStream";
        r10 = 2;
        r10 = new java.lang.Class[r10];	 Catch:{ Exception -> 0x0082 }
        r11 = 0;
        r12 = java.lang.String.class;
        r10[r11] = r12;	 Catch:{ Exception -> 0x0082 }
        r11 = 1;
        r12 = java.io.InputStream.class;
        r10[r11] = r12;	 Catch:{ Exception -> 0x0082 }
        r1 = r1.getMethod(r6, r10);	 Catch:{ Exception -> 0x0082 }
        SprCreateFromStream = r1;	 Catch:{ Exception -> 0x0082 }
    L_0x006d:
        r1 = SprCreateFromStream;	 Catch:{ Exception -> 0x0082 }
        r6 = SprClass;	 Catch:{ Exception -> 0x0082 }
        r10 = 2;
        r10 = new java.lang.Object[r10];	 Catch:{ Exception -> 0x0082 }
        r11 = 0;
        r10[r11] = r16;	 Catch:{ Exception -> 0x0082 }
        r11 = 1;
        r10[r11] = r15;	 Catch:{ Exception -> 0x0082 }
        r1 = r1.invoke(r6, r10);	 Catch:{ Exception -> 0x0082 }
        r1 = (android.graphics.drawable.Drawable) r1;	 Catch:{ Exception -> 0x0082 }
        r9 = r1;
        goto L_0x0003;
    L_0x0082:
        r8 = move-exception;
        r8.printStackTrace();
    L_0x0086:
        r4 = new android.graphics.Rect;
        r4.<init>();
        if (r17 != 0) goto L_0x0092;
    L_0x008d:
        r17 = new android.graphics.BitmapFactory$Options;
        r17.<init>();
    L_0x0092:
        if (r13 == 0) goto L_0x00cc;
    L_0x0094:
        r1 = r13.getDisplayMetrics();
        r1 = r1.noncompatDensityDpi;
    L_0x009a:
        r0 = r17;
        r0.inScreenDensity = r1;
        r0 = r17;
        r2 = android.graphics.BitmapFactory.decodeResourceStream(r13, r14, r15, r4, r0);
        if (r2 == 0) goto L_0x00cf;
    L_0x00a6:
        r3 = r2.getNinePatchChunk();
        if (r3 == 0) goto L_0x00b2;
    L_0x00ac:
        r1 = android.graphics.NinePatch.isNinePatchChunk(r3);
        if (r1 != 0) goto L_0x00b4;
    L_0x00b2:
        r3 = 0;
        r4 = 0;
    L_0x00b4:
        r5 = new android.graphics.Rect;
        r5.<init>();
        r2.getOpticalInsets(r5);
        r1 = r13;
        r6 = r16;
        r9 = drawableFromBitmap(r1, r2, r3, r4, r5, r6);
        if (r9 == 0) goto L_0x0003;
    L_0x00c5:
        r0 = r16;
        r9.setImagePath(r0);
        goto L_0x0003;
    L_0x00cc:
        r1 = android.util.DisplayMetrics.DENSITY_DEVICE;
        goto L_0x009a;
    L_0x00cf:
        r9 = 0;
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Drawable.createFromResourceStream(android.content.res.Resources, android.util.TypedValue, java.io.InputStream, java.lang.String, android.graphics.BitmapFactory$Options):android.graphics.drawable.Drawable");
    }

    public static Drawable createFromXml(Resources r, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createFromXml(r, parser, null);
    }

    public static Drawable createFromXml(Resources r, XmlPullParser parser, Theme theme) throws XmlPullParserException, IOException {
        int type;
        AttributeSet attrs = Xml.asAttributeSet(parser);
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        Drawable drawable = createFromXmlInner(r, parser, attrs, theme);
        if (drawable != null) {
            return drawable;
        }
        throw new RuntimeException("Unknown initial tag: " + parser.getName());
    }

    public static Drawable createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        return createFromXmlInner(r, parser, attrs, null);
    }

    public static Drawable createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        Drawable drawable;
        String name = parser.getName();
        Object obj = -1;
        switch (name.hashCode()) {
            case -1724158635:
                if (name.equals("transition")) {
                    obj = 4;
                    break;
                }
                break;
            case -1671889043:
                if (name.equals("nine-patch")) {
                    obj = 17;
                    break;
                }
                break;
            case -1493546681:
                if (name.equals("animation-list")) {
                    obj = 14;
                    break;
                }
                break;
            case -1388777169:
                if (name.equals("bitmap")) {
                    obj = 16;
                    break;
                }
                break;
            case -930826704:
                if (name.equals("ripple")) {
                    obj = 5;
                    break;
                }
                break;
            case -925180581:
                if (name.equals("rotate")) {
                    obj = 12;
                    break;
                }
                break;
            case -820387517:
                if (name.equals("vector")) {
                    obj = 8;
                    break;
                }
                break;
            case -510364471:
                if (name.equals("animated-selector")) {
                    obj = 1;
                    break;
                }
                break;
            case -94197862:
                if (name.equals("layer-list")) {
                    obj = 3;
                    break;
                }
                break;
            case 3056464:
                if (name.equals("clip")) {
                    obj = 11;
                    break;
                }
                break;
            case 94842723:
                if (name.equals(ColorsColumns.COLOR)) {
                    obj = 6;
                    break;
                }
                break;
            case 100360477:
                if (name.equals("inset")) {
                    obj = 15;
                    break;
                }
                break;
            case 109250890:
                if (name.equals(BatteryManager.EXTRA_SCALE)) {
                    obj = 10;
                    break;
                }
                break;
            case 109399969:
                if (name.equals("shape")) {
                    obj = 7;
                    break;
                }
                break;
            case 160680263:
                if (name.equals("level-list")) {
                    obj = 2;
                    break;
                }
                break;
            case 1191572447:
                if (name.equals("selector")) {
                    obj = null;
                    break;
                }
                break;
            case 2013827269:
                if (name.equals("animated-rotate")) {
                    obj = 13;
                    break;
                }
                break;
            case 2118620333:
                if (name.equals("animated-vector")) {
                    obj = 9;
                    break;
                }
                break;
        }
        switch (obj) {
            case null:
                drawable = new StateListDrawable();
                break;
            case 1:
                drawable = new AnimatedStateListDrawable();
                break;
            case 2:
                drawable = new LevelListDrawable();
                break;
            case 3:
                drawable = new LayerDrawable();
                break;
            case 4:
                drawable = new TransitionDrawable();
                break;
            case 5:
                drawable = new RippleDrawable();
                break;
            case 6:
                drawable = new ColorDrawable();
                break;
            case 7:
                drawable = new GradientDrawable();
                break;
            case 8:
                drawable = new VectorDrawable();
                break;
            case 9:
                drawable = new AnimatedVectorDrawable();
                break;
            case 10:
                drawable = new ScaleDrawable();
                break;
            case 11:
                drawable = new ClipDrawable();
                break;
            case 12:
                drawable = new RotateDrawable();
                break;
            case 13:
                drawable = new AnimatedRotateDrawable();
                break;
            case 14:
                drawable = new AnimationDrawable();
                break;
            case 15:
                drawable = new InsetDrawable();
                break;
            case 16:
                boolean isSpr = false;
                int id = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
                if (id != 0) {
                    InputStream inputStream = null;
                    byte[] b = new byte[3];
                    try {
                        inputStream = r.openRawResource(id);
                        inputStream.read(b, 0, 3);
                        isSpr = b[0] == (byte) 83 && b[1] == (byte) 80 && b[2] == (byte) 82;
                    } finally {
                        inputStream.close();
                    }
                }
                if (!isSpr) {
                    drawable = new BitmapDrawable();
                    break;
                }
                try {
                    drawable = (Drawable) Class.forName("com.samsung.android.sdk.spr.drawable.SprDrawable").newInstance();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new XmlPullParserException(parser.getPositionDescription() + ": unable to load spr." + name);
                }
                break;
            case 17:
                drawable = new NinePatchDrawable();
                break;
            default:
                throw new XmlPullParserException(parser.getPositionDescription() + ": invalid drawable tag " + name);
        }
        drawable.inflate(r, parser, attrs, theme);
        return drawable;
    }

    public static Drawable createFromPath(String pathName) {
        Drawable drawable = null;
        if (pathName != null) {
            Trace.traceBegin(8192, pathName);
            try {
                Bitmap bm = BitmapFactory.decodeFile(pathName);
                if (bm != null) {
                    drawable = drawableFromBitmap(null, bm, null, null, null, pathName);
                    if (drawable != null) {
                        drawable.setImagePath(pathName);
                    }
                    Trace.traceEnd(8192);
                } else {
                    Trace.traceEnd(8192);
                }
            } catch (Throwable th) {
                Trace.traceEnd(8192);
            }
        }
        return drawable;
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        inflate(r, parser, attrs, null);
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        TypedArray a;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.Drawable, 0, 0);
        } else {
            a = r.obtainAttributes(attrs, R.styleable.Drawable);
        }
        inflateWithAttributes(r, parser, a, 0);
        a.recycle();
    }

    void inflateWithAttributes(Resources r, XmlPullParser parser, TypedArray attrs, int visibleAttr) throws XmlPullParserException, IOException {
        this.mVisible = attrs.getBoolean(visibleAttr, this.mVisible);
    }

    public ConstantState getConstantState() {
        return null;
    }

    private static Drawable drawableFromBitmap(Resources res, Bitmap bm, byte[] np, Rect pad, Rect layoutBounds, String srcName) {
        if (np != null) {
            return new NinePatchDrawable(res, bm, np, pad, layoutBounds, srcName);
        }
        return new BitmapDrawable(res, bm);
    }

    PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter tintFilter, ColorStateList tint, Mode tintMode) {
        if (tint == null || tintMode == null) {
            return null;
        }
        int color = tint.getColorForState(getState(), 0);
        if (tintFilter == null) {
            return new PorterDuffColorFilter(color, tintMode);
        }
        tintFilter.setColor(color);
        tintFilter.setMode(tintMode);
        return tintFilter;
    }

    static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    public static Mode parseTintMode(int value, Mode defaultMode) {
        switch (value) {
            case 3:
                return Mode.SRC_OVER;
            case 5:
                return Mode.SRC_IN;
            case 9:
                return Mode.SRC_ATOP;
            case 14:
                return Mode.MULTIPLY;
            case 15:
                return Mode.SCREEN;
            case 16:
                return Mode.ADD;
            default:
                return defaultMode;
        }
    }
}
