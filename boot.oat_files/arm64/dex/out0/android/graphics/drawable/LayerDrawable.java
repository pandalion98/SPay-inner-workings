package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import android.view.Gravity;
import com.android.internal.R;
import java.io.IOException;
import java.util.Collection;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LayerDrawable extends Drawable implements Callback {
    public static final int PADDING_MODE_NEST = 0;
    public static final int PADDING_MODE_STACK = 1;
    private static final int UNDEFINED_INSET = Integer.MIN_VALUE;
    private Rect mHotspotBounds;
    LayerState mLayerState;
    private boolean mMutated;
    private int[] mPaddingB;
    private int[] mPaddingL;
    private int[] mPaddingR;
    private int[] mPaddingT;
    private final Rect mTmpContainer;
    private final Rect mTmpOutRect;
    private final Rect mTmpRect;

    static class ChildDrawable {
        public Drawable mDrawable;
        public int mGravity = 0;
        public int mHeight = -1;
        public int mId = -1;
        public int mInsetB;
        public int mInsetE = Integer.MIN_VALUE;
        public int mInsetL;
        public int mInsetR;
        public int mInsetS = Integer.MIN_VALUE;
        public int mInsetT;
        public int[] mThemeAttrs;
        public int mWidth = -1;

        ChildDrawable() {
        }

        ChildDrawable(ChildDrawable orig, LayerDrawable owner, Resources res) {
            Drawable clone;
            Drawable dr = orig.mDrawable;
            if (dr != null) {
                ConstantState cs = dr.getConstantState();
                if (res != null) {
                    clone = cs.newDrawable(res);
                } else {
                    clone = cs.newDrawable();
                }
                clone.setCallback(owner);
                clone.setLayoutDirection(dr.getLayoutDirection());
                clone.setBounds(dr.getBounds());
                clone.setLevel(dr.getLevel());
            } else {
                clone = null;
            }
            this.mDrawable = clone;
            this.mThemeAttrs = orig.mThemeAttrs;
            this.mInsetL = orig.mInsetL;
            this.mInsetT = orig.mInsetT;
            this.mInsetR = orig.mInsetR;
            this.mInsetB = orig.mInsetB;
            this.mInsetS = orig.mInsetS;
            this.mInsetE = orig.mInsetE;
            this.mWidth = orig.mWidth;
            this.mHeight = orig.mHeight;
            this.mGravity = orig.mGravity;
            this.mId = orig.mId;
        }

        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mDrawable != null && this.mDrawable.canApplyTheme());
        }
    }

    static class LayerState extends ConstantState {
        private boolean mAutoMirrored = false;
        int mChangingConfigurations;
        ChildDrawable[] mChildren;
        int mChildrenChangingConfigurations;
        private boolean mHaveIsStateful;
        private boolean mHaveOpacity;
        private boolean mIsStateful;
        int mNum;
        private int mOpacity;
        int mOpacityOverride = 0;
        int mPaddingBottom = -1;
        int mPaddingEnd = -1;
        int mPaddingLeft = -1;
        private int mPaddingMode = 0;
        int mPaddingRight = -1;
        int mPaddingStart = -1;
        int mPaddingTop = -1;
        int[] mThemeAttrs;

        LayerState(LayerState orig, LayerDrawable owner, Resources res) {
            if (orig != null) {
                ChildDrawable[] origChildDrawable = orig.mChildren;
                int N = orig.mNum;
                this.mNum = N;
                this.mChildren = new ChildDrawable[N];
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;
                for (int i = 0; i < N; i++) {
                    this.mChildren[i] = new ChildDrawable(origChildDrawable[i], owner, res);
                }
                this.mHaveOpacity = orig.mHaveOpacity;
                this.mOpacity = orig.mOpacity;
                this.mHaveIsStateful = orig.mHaveIsStateful;
                this.mIsStateful = orig.mIsStateful;
                this.mAutoMirrored = orig.mAutoMirrored;
                this.mPaddingMode = orig.mPaddingMode;
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mPaddingTop = orig.mPaddingTop;
                this.mPaddingBottom = orig.mPaddingBottom;
                this.mPaddingLeft = orig.mPaddingLeft;
                this.mPaddingRight = orig.mPaddingRight;
                this.mPaddingStart = orig.mPaddingStart;
                this.mPaddingEnd = orig.mPaddingEnd;
                this.mOpacityOverride = orig.mOpacityOverride;
                return;
            }
            this.mNum = 0;
            this.mChildren = null;
        }

        public boolean canApplyTheme() {
            if (this.mThemeAttrs != null || super.canApplyTheme()) {
                return true;
            }
            ChildDrawable[] array = this.mChildren;
            int N = this.mNum;
            for (int i = 0; i < N; i++) {
                if (array[i].canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        public Drawable newDrawable() {
            return new LayerDrawable(this, null);
        }

        public Drawable newDrawable(Resources res) {
            return new LayerDrawable(this, res);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final int getOpacity() {
            if (this.mHaveOpacity) {
                return this.mOpacity;
            }
            int i;
            int op;
            ChildDrawable[] array = this.mChildren;
            int N = this.mNum;
            int firstIndex = -1;
            for (i = 0; i < N; i++) {
                if (array[i].mDrawable != null) {
                    firstIndex = i;
                    break;
                }
            }
            if (firstIndex >= 0) {
                op = array[firstIndex].mDrawable.getOpacity();
            } else {
                op = -2;
            }
            for (i = firstIndex + 1; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    op = Drawable.resolveOpacity(op, dr.getOpacity());
                }
            }
            this.mOpacity = op;
            this.mHaveOpacity = true;
            return op;
        }

        public final boolean isStateful() {
            if (this.mHaveIsStateful) {
                return this.mIsStateful;
            }
            ChildDrawable[] array = this.mChildren;
            int N = this.mNum;
            boolean isStateful = false;
            for (int i = 0; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null && dr.isStateful()) {
                    isStateful = true;
                    break;
                }
            }
            this.mIsStateful = isStateful;
            this.mHaveIsStateful = true;
            return isStateful;
        }

        public final boolean canConstantState() {
            ChildDrawable[] array = this.mChildren;
            int N = this.mNum;
            for (int i = 0; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null && dr.getConstantState() == null) {
                    return false;
                }
            }
            return true;
        }

        public void invalidateCache() {
            this.mHaveOpacity = false;
            this.mHaveIsStateful = false;
        }

        public int addAtlasableBitmaps(Collection<Bitmap> atlasList) {
            ChildDrawable[] array = this.mChildren;
            int N = this.mNum;
            int pixelCount = 0;
            for (int i = 0; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    ConstantState state = dr.getConstantState();
                    if (state != null) {
                        pixelCount += state.addAtlasableBitmaps(atlasList);
                    }
                }
            }
            return pixelCount;
        }
    }

    public LayerDrawable(Drawable[] layers) {
        this(layers, null);
    }

    LayerDrawable(Drawable[] layers, LayerState state) {
        this(state, null);
        if (layers == null) {
            throw new IllegalArgumentException("layers must be non-null");
        }
        int length = layers.length;
        ChildDrawable[] r = new ChildDrawable[length];
        for (int i = 0; i < length; i++) {
            r[i] = new ChildDrawable();
            r[i].mDrawable = layers[i];
            layers[i].setCallback(this);
            LayerState layerState = this.mLayerState;
            layerState.mChildrenChangingConfigurations |= layers[i].getChangingConfigurations();
        }
        this.mLayerState.mNum = length;
        this.mLayerState.mChildren = r;
        ensurePadding();
        refreshPadding();
    }

    LayerDrawable() {
        this((LayerState) null, null);
    }

    LayerDrawable(LayerState state, Resources res) {
        this.mTmpRect = new Rect();
        this.mTmpOutRect = new Rect();
        this.mTmpContainer = new Rect();
        this.mLayerState = createConstantState(state, res);
        if (this.mLayerState.mNum > 0) {
            ensurePadding();
            refreshPadding();
        }
    }

    LayerState createConstantState(LayerState state, Resources res) {
        return new LayerState(state, this, res);
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.LayerDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        inflateLayers(r, parser, attrs, theme);
        ensurePadding();
        refreshPadding();
    }

    private void updateStateFromTypedArray(TypedArray a) {
        LayerState state = this.mLayerState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    state.mPaddingLeft = a.getDimensionPixelOffset(attr, state.mPaddingLeft);
                    break;
                case 1:
                    state.mPaddingTop = a.getDimensionPixelOffset(attr, state.mPaddingTop);
                    break;
                case 2:
                    state.mPaddingRight = a.getDimensionPixelOffset(attr, state.mPaddingRight);
                    break;
                case 3:
                    state.mPaddingBottom = a.getDimensionPixelOffset(attr, state.mPaddingBottom);
                    break;
                case 4:
                    state.mOpacityOverride = a.getInt(attr, state.mOpacityOverride);
                    break;
                case 5:
                    state.mPaddingStart = a.getDimensionPixelOffset(attr, state.mPaddingStart);
                    break;
                case 6:
                    state.mPaddingEnd = a.getDimensionPixelOffset(attr, state.mPaddingEnd);
                    break;
                case 7:
                    state.mAutoMirrored = a.getBoolean(attr, state.mAutoMirrored);
                    break;
                case 8:
                    state.mPaddingMode = a.getInteger(attr, state.mPaddingMode);
                    break;
                default:
                    break;
            }
        }
    }

    private void inflateLayers(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        LayerState state = this.mLayerState;
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int type = parser.next();
            if (type != 1) {
                int depth = parser.getDepth();
                if (depth < innerDepth && type == 3) {
                    return;
                }
                if (type == 2 && depth <= innerDepth && parser.getName().equals("item")) {
                    ChildDrawable layer = new ChildDrawable();
                    TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.LayerDrawableItem);
                    updateLayerFromTypedArray(layer, a);
                    a.recycle();
                    if (layer.mDrawable == null && (layer.mThemeAttrs == null || layer.mThemeAttrs[4] == 0)) {
                        do {
                            type = parser.next();
                        } while (type == 4);
                        if (type != 2) {
                            throw new XmlPullParserException(parser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or " + "child tag defining a drawable");
                        }
                        layer.mDrawable = Drawable.createFromXmlInner(r, parser, attrs, theme);
                    }
                    if (layer.mDrawable != null) {
                        state.mChildrenChangingConfigurations |= layer.mDrawable.getChangingConfigurations();
                        layer.mDrawable.setCallback(this);
                    }
                    addLayer(layer);
                }
            } else {
                return;
            }
        }
    }

    private void updateLayerFromTypedArray(ChildDrawable layer, TypedArray a) {
        LayerState state = this.mLayerState;
        state.mChildrenChangingConfigurations |= a.getChangingConfigurations();
        layer.mThemeAttrs = a.extractThemeAttrs();
        layer.mInsetL = a.getDimensionPixelOffset(5, layer.mInsetL);
        layer.mInsetT = a.getDimensionPixelOffset(6, layer.mInsetT);
        layer.mInsetR = a.getDimensionPixelOffset(7, layer.mInsetR);
        layer.mInsetB = a.getDimensionPixelOffset(8, layer.mInsetB);
        layer.mInsetS = a.getDimensionPixelOffset(9, layer.mInsetS);
        layer.mInsetE = a.getDimensionPixelOffset(10, layer.mInsetE);
        layer.mWidth = a.getDimensionPixelSize(3, layer.mWidth);
        layer.mHeight = a.getDimensionPixelSize(2, layer.mHeight);
        layer.mGravity = a.getInteger(0, layer.mGravity);
        layer.mId = a.getResourceId(1, layer.mId);
        Drawable dr = a.getDrawable(4);
        if (dr != null) {
            layer.mDrawable = dr;
        }
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        LayerState state = this.mLayerState;
        if (state != null) {
            TypedArray a;
            if (state.mThemeAttrs != null) {
                a = t.resolveAttributes(state.mThemeAttrs, R.styleable.LayerDrawable);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            ChildDrawable[] array = state.mChildren;
            int N = state.mNum;
            for (int i = 0; i < N; i++) {
                ChildDrawable layer = array[i];
                if (layer.mThemeAttrs != null) {
                    a = t.resolveAttributes(layer.mThemeAttrs, R.styleable.LayerDrawableItem);
                    updateLayerFromTypedArray(layer, a);
                    a.recycle();
                }
                Drawable d = layer.mDrawable;
                if (d != null && d.canApplyTheme()) {
                    d.applyTheme(t);
                    state.mChildrenChangingConfigurations |= d.getChangingConfigurations();
                }
            }
            ensurePadding();
        }
    }

    public boolean canApplyTheme() {
        return (this.mLayerState != null && this.mLayerState.canApplyTheme()) || super.canApplyTheme();
    }

    public boolean isProjected() {
        if (super.isProjected()) {
            return true;
        }
        ChildDrawable[] layers = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            if (layers[i].mDrawable.isProjected()) {
                return true;
            }
        }
        return false;
    }

    int addLayer(ChildDrawable layer) {
        int N;
        LayerState st = this.mLayerState;
        if (st.mChildren != null) {
            N = st.mChildren.length;
        } else {
            N = 0;
        }
        int i = st.mNum;
        if (i >= N) {
            ChildDrawable[] nu = new ChildDrawable[(N + 10)];
            if (i > 0) {
                System.arraycopy(st.mChildren, 0, nu, 0, i);
            }
            st.mChildren = nu;
        }
        st.mChildren[i] = layer;
        st.mNum++;
        st.invalidateCache();
        return i;
    }

    ChildDrawable addLayer(Drawable dr, int[] themeAttrs, int id, int left, int top, int right, int bottom) {
        ChildDrawable childDrawable = createLayer(dr);
        childDrawable.mId = id;
        childDrawable.mThemeAttrs = themeAttrs;
        childDrawable.mDrawable.setAutoMirrored(isAutoMirrored());
        childDrawable.mInsetL = left;
        childDrawable.mInsetT = top;
        childDrawable.mInsetR = right;
        childDrawable.mInsetB = bottom;
        addLayer(childDrawable);
        LayerState layerState = this.mLayerState;
        layerState.mChildrenChangingConfigurations |= dr.getChangingConfigurations();
        dr.setCallback(this);
        return childDrawable;
    }

    private ChildDrawable createLayer(Drawable dr) {
        ChildDrawable layer = new ChildDrawable();
        layer.mDrawable = dr;
        return layer;
    }

    public int addLayer(Drawable dr) {
        ChildDrawable layer = createLayer(dr);
        int index = addLayer(layer);
        ensurePadding();
        refreshChildPadding(index, layer);
        return index;
    }

    public Drawable findDrawableByLayerId(int id) {
        ChildDrawable[] layers = this.mLayerState.mChildren;
        for (int i = this.mLayerState.mNum - 1; i >= 0; i--) {
            if (layers[i].mId == id) {
                return layers[i].mDrawable;
            }
        }
        return null;
    }

    public void setId(int index, int id) {
        this.mLayerState.mChildren[index].mId = id;
    }

    public int getId(int index) {
        if (index < this.mLayerState.mNum) {
            return this.mLayerState.mChildren[index].mId;
        }
        throw new IndexOutOfBoundsException();
    }

    public int getNumberOfLayers() {
        return this.mLayerState.mNum;
    }

    public boolean setDrawableByLayerId(int id, Drawable drawable) {
        int index = findIndexByLayerId(id);
        if (index < 0) {
            return false;
        }
        setDrawable(index, drawable);
        return true;
    }

    public int findIndexByLayerId(int id) {
        ChildDrawable[] layers = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            if (layers[i].mId == id) {
                return i;
            }
        }
        return -1;
    }

    public void setDrawable(int index, Drawable drawable) {
        if (index >= this.mLayerState.mNum) {
            throw new IndexOutOfBoundsException();
        }
        ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        if (childDrawable.mDrawable != null) {
            if (drawable != null) {
                drawable.setBounds(childDrawable.mDrawable.getBounds());
            }
            childDrawable.mDrawable.setCallback(null);
        }
        if (drawable != null) {
            drawable.setCallback(this);
        }
        childDrawable.mDrawable = drawable;
        this.mLayerState.invalidateCache();
        refreshChildPadding(index, childDrawable);
    }

    public Drawable getDrawable(int index) {
        if (index < this.mLayerState.mNum) {
            return this.mLayerState.mChildren[index].mDrawable;
        }
        throw new IndexOutOfBoundsException();
    }

    public void setLayerSize(int index, int w, int h) {
        ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mWidth = w;
        childDrawable.mHeight = h;
    }

    public void setLayerWidth(int index, int w) {
        this.mLayerState.mChildren[index].mWidth = w;
    }

    public int getLayerWidth(int index) {
        return this.mLayerState.mChildren[index].mWidth;
    }

    public void setLayerHeight(int index, int h) {
        this.mLayerState.mChildren[index].mHeight = h;
    }

    public int getLayerHeight(int index) {
        return this.mLayerState.mChildren[index].mHeight;
    }

    public void setLayerGravity(int index, int gravity) {
        this.mLayerState.mChildren[index].mGravity = gravity;
    }

    public int getLayerGravity(int index) {
        return this.mLayerState.mChildren[index].mGravity;
    }

    public void setLayerInset(int index, int l, int t, int r, int b) {
        setLayerInsetInternal(index, l, t, r, b, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public void setLayerInsetRelative(int index, int s, int t, int e, int b) {
        setLayerInsetInternal(index, 0, t, 0, b, s, e);
    }

    public void setLayerInsetLeft(int index, int l) {
        this.mLayerState.mChildren[index].mInsetL = l;
    }

    public int getLayerInsetLeft(int index) {
        return this.mLayerState.mChildren[index].mInsetL;
    }

    public void setLayerInsetRight(int index, int r) {
        this.mLayerState.mChildren[index].mInsetR = r;
    }

    public int getLayerInsetRight(int index) {
        return this.mLayerState.mChildren[index].mInsetR;
    }

    public void setLayerInsetTop(int index, int t) {
        this.mLayerState.mChildren[index].mInsetT = t;
    }

    public int getLayerInsetTop(int index) {
        return this.mLayerState.mChildren[index].mInsetT;
    }

    public void setLayerInsetBottom(int index, int b) {
        this.mLayerState.mChildren[index].mInsetB = b;
    }

    public int getLayerInsetBottom(int index) {
        return this.mLayerState.mChildren[index].mInsetB;
    }

    public void setLayerInsetStart(int index, int s) {
        this.mLayerState.mChildren[index].mInsetS = s;
    }

    public int getLayerInsetStart(int index) {
        return this.mLayerState.mChildren[index].mInsetS;
    }

    public void setLayerInsetEnd(int index, int e) {
        this.mLayerState.mChildren[index].mInsetE = e;
    }

    public int getLayerInsetEnd(int index) {
        return this.mLayerState.mChildren[index].mInsetE;
    }

    private void setLayerInsetInternal(int index, int l, int t, int r, int b, int s, int e) {
        ChildDrawable childDrawable = this.mLayerState.mChildren[index];
        childDrawable.mInsetL = l;
        childDrawable.mInsetT = t;
        childDrawable.mInsetR = r;
        childDrawable.mInsetB = b;
        childDrawable.mInsetS = s;
        childDrawable.mInsetE = e;
    }

    public void setPaddingMode(int mode) {
        if (this.mLayerState.mPaddingMode != mode) {
            this.mLayerState.mPaddingMode = mode;
        }
    }

    public int getPaddingMode() {
        return this.mLayerState.mPaddingMode;
    }

    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }

    public void draw(Canvas canvas) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.draw(canvas);
            }
        }
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mLayerState.getChangingConfigurations();
    }

    public boolean getPadding(Rect padding) {
        int paddingRtlLeft;
        int paddingRtlRight;
        LayerState layerState = this.mLayerState;
        if (layerState.mPaddingMode == 0) {
            computeNestedPadding(padding);
        } else {
            computeStackedPadding(padding);
        }
        if (layerState.mPaddingTop >= 0) {
            padding.top = layerState.mPaddingTop;
        }
        if (layerState.mPaddingBottom >= 0) {
            padding.bottom = layerState.mPaddingBottom;
        }
        if (getLayoutDirection() == 1) {
            paddingRtlLeft = layerState.mPaddingEnd;
            paddingRtlRight = layerState.mPaddingStart;
        } else {
            paddingRtlLeft = layerState.mPaddingStart;
            paddingRtlRight = layerState.mPaddingEnd;
        }
        int paddingLeft = paddingRtlLeft >= 0 ? paddingRtlLeft : layerState.mPaddingLeft;
        if (paddingLeft >= 0) {
            padding.left = paddingLeft;
        }
        int paddingRight = paddingRtlRight >= 0 ? paddingRtlRight : layerState.mPaddingRight;
        if (paddingRight >= 0) {
            padding.right = paddingRight;
        }
        if (padding.left == 0 && padding.top == 0 && padding.right == 0 && padding.bottom == 0) {
            return false;
        }
        return true;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        LayerState layerState = this.mLayerState;
        layerState.mPaddingLeft = left;
        layerState.mPaddingTop = top;
        layerState.mPaddingRight = right;
        layerState.mPaddingBottom = bottom;
        layerState.mPaddingStart = -1;
        layerState.mPaddingEnd = -1;
    }

    public void setPaddingRelative(int start, int top, int end, int bottom) {
        LayerState layerState = this.mLayerState;
        layerState.mPaddingStart = start;
        layerState.mPaddingTop = top;
        layerState.mPaddingEnd = end;
        layerState.mPaddingBottom = bottom;
        layerState.mPaddingLeft = -1;
        layerState.mPaddingRight = -1;
    }

    public int getLeftPadding() {
        return this.mLayerState.mPaddingLeft;
    }

    public int getRightPadding() {
        return this.mLayerState.mPaddingRight;
    }

    public int getStartPadding() {
        return this.mLayerState.mPaddingStart;
    }

    public int getEndPadding() {
        return this.mLayerState.mPaddingEnd;
    }

    public int getTopPadding() {
        return this.mLayerState.mPaddingTop;
    }

    public int getBottomPadding() {
        return this.mLayerState.mPaddingBottom;
    }

    private void computeNestedPadding(Rect padding) {
        padding.left = 0;
        padding.top = 0;
        padding.right = 0;
        padding.bottom = 0;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            refreshChildPadding(i, array[i]);
            padding.left += this.mPaddingL[i];
            padding.top += this.mPaddingT[i];
            padding.right += this.mPaddingR[i];
            padding.bottom += this.mPaddingB[i];
        }
    }

    private void computeStackedPadding(Rect padding) {
        padding.left = 0;
        padding.top = 0;
        padding.right = 0;
        padding.bottom = 0;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            refreshChildPadding(i, array[i]);
            padding.left = Math.max(padding.left, this.mPaddingL[i]);
            padding.top = Math.max(padding.top, this.mPaddingT[i]);
            padding.right = Math.max(padding.right, this.mPaddingR[i]);
            padding.bottom = Math.max(padding.bottom, this.mPaddingB[i]);
        }
    }

    public void getOutline(Outline outline) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.getOutline(outline);
                if (!outline.isEmpty()) {
                    return;
                }
            }
        }
    }

    public void setHotspot(float x, float y) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setHotspot(x, y);
            }
        }
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setHotspotBounds(left, top, right, bottom);
            }
        }
        if (this.mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(left, top, right, bottom);
        } else {
            this.mHotspotBounds.set(left, top, right, bottom);
        }
    }

    public void getHotspotBounds(Rect outRect) {
        if (this.mHotspotBounds != null) {
            outRect.set(this.mHotspotBounds);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setVisible(visible, restart);
            }
        }
        return changed;
    }

    public void setDither(boolean dither) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setDither(dither);
            }
        }
    }

    public void setAlpha(int alpha) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setAlpha(alpha);
            }
        }
    }

    public int getAlpha() {
        Drawable dr = getFirstNonNullDrawable();
        if (dr != null) {
            return dr.getAlpha();
        }
        return super.getAlpha();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setColorFilter(colorFilter);
            }
        }
    }

    public void setTintList(ColorStateList tint) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintList(tint);
            }
        }
    }

    public void setTintMode(Mode tintMode) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintMode(tintMode);
            }
        }
    }

    private Drawable getFirstNonNullDrawable() {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                return dr;
            }
        }
        return null;
    }

    public void setOpacity(int opacity) {
        this.mLayerState.mOpacityOverride = opacity;
    }

    public int getOpacity() {
        if (this.mLayerState.mOpacityOverride != 0) {
            return this.mLayerState.mOpacityOverride;
        }
        return this.mLayerState.getOpacity();
    }

    public void setAutoMirrored(boolean mirrored) {
        this.mLayerState.mAutoMirrored = mirrored;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setAutoMirrored(mirrored);
            }
        }
    }

    public boolean isAutoMirrored() {
        return this.mLayerState.mAutoMirrored;
    }

    public boolean isStateful() {
        return this.mLayerState.isStateful();
    }

    protected boolean onStateChange(int[] state) {
        boolean changed = false;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null && dr.isStateful() && dr.setState(state)) {
                refreshChildPadding(i, array[i]);
                changed = true;
            }
        }
        if (changed) {
            updateLayerBounds(getBounds());
        }
        return changed;
    }

    protected boolean onLevelChange(int level) {
        boolean changed = false;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null && dr.setLevel(level)) {
                refreshChildPadding(i, array[i]);
                changed = true;
            }
        }
        if (changed) {
            updateLayerBounds(getBounds());
        }
        return changed;
    }

    protected void onBoundsChange(Rect bounds) {
        updateLayerBounds(bounds);
    }

    private void updateLayerBounds(Rect bounds) {
        int padL = 0;
        int padT = 0;
        int padR = 0;
        int padB = 0;
        Rect outRect = this.mTmpOutRect;
        int layoutDirection = getLayoutDirection();
        boolean nest = this.mLayerState.mPaddingMode == 0;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            ChildDrawable r = array[i];
            Drawable d = r.mDrawable;
            if (d != null) {
                int insetL;
                int insetR;
                int h;
                Rect container = this.mTmpContainer;
                container.set(d.getBounds());
                if (layoutDirection == 1) {
                    insetL = r.mInsetE == Integer.MIN_VALUE ? r.mInsetL : r.mInsetE;
                    insetR = r.mInsetS == Integer.MIN_VALUE ? r.mInsetR : r.mInsetS;
                } else {
                    insetL = r.mInsetS == Integer.MIN_VALUE ? r.mInsetL : r.mInsetS;
                    if (r.mInsetE == Integer.MIN_VALUE) {
                        insetR = r.mInsetR;
                    } else {
                        insetR = r.mInsetE;
                    }
                }
                container.set((bounds.left + insetL) + padL, (bounds.top + r.mInsetT) + padT, (bounds.right - insetR) - padR, (bounds.bottom - r.mInsetB) - padB);
                int gravity = resolveGravity(r.mGravity, r.mWidth, r.mHeight, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                int w = r.mWidth < 0 ? d.getIntrinsicWidth() : r.mWidth;
                if (r.mHeight < 0) {
                    h = d.getIntrinsicHeight();
                } else {
                    h = r.mHeight;
                }
                Gravity.apply(gravity, w, h, container, outRect, layoutDirection);
                d.setBounds(outRect);
                if (nest) {
                    padL += this.mPaddingL[i];
                    padR += this.mPaddingR[i];
                    padT += this.mPaddingT[i];
                    padB += this.mPaddingB[i];
                }
            }
        }
    }

    private static int resolveGravity(int gravity, int width, int height, int intrinsicWidth, int intrinsicHeight) {
        if (!Gravity.isHorizontal(gravity)) {
            if (width < 0) {
                gravity |= 7;
            } else {
                gravity |= 8388611;
            }
        }
        if (!Gravity.isVertical(gravity)) {
            if (height < 0) {
                gravity |= 112;
            } else {
                gravity |= 48;
            }
        }
        if (width < 0 && intrinsicWidth < 0) {
            gravity |= 7;
        }
        if (height >= 0 || intrinsicHeight >= 0) {
            return gravity;
        }
        return gravity | 112;
    }

    public int getIntrinsicWidth() {
        int width = -1;
        int padL = 0;
        int padR = 0;
        boolean nest = this.mLayerState.mPaddingMode == 0;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            ChildDrawable r = array[i];
            if (r.mDrawable != null) {
                int insetL;
                int insetR;
                if (getLayoutDirection() == 1) {
                    insetL = r.mInsetE == Integer.MIN_VALUE ? r.mInsetL : r.mInsetE;
                    insetR = r.mInsetS == Integer.MIN_VALUE ? r.mInsetR : r.mInsetS;
                } else {
                    insetL = r.mInsetS == Integer.MIN_VALUE ? r.mInsetL : r.mInsetS;
                    insetR = r.mInsetE == Integer.MIN_VALUE ? r.mInsetR : r.mInsetE;
                }
                int w = ((((r.mWidth < 0 ? r.mDrawable.getIntrinsicWidth() : r.mWidth) + insetL) + insetR) + padL) + padR;
                if (w > width) {
                    width = w;
                }
                if (nest) {
                    padL += this.mPaddingL[i];
                    padR += this.mPaddingR[i];
                }
            }
        }
        return width;
    }

    public int getIntrinsicHeight() {
        int height = -1;
        int padT = 0;
        int padB = 0;
        boolean nest = this.mLayerState.mPaddingMode == 0;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            ChildDrawable r = array[i];
            if (r.mDrawable != null) {
                int h = (((r.mInsetT + (r.mHeight < 0 ? r.mDrawable.getIntrinsicHeight() : r.mHeight)) + r.mInsetB) + padT) + padB;
                if (h > height) {
                    height = h;
                }
                if (nest) {
                    padT += this.mPaddingT[i];
                    padB += this.mPaddingB[i];
                }
            }
        }
        return height;
    }

    private boolean refreshChildPadding(int i, ChildDrawable r) {
        if (r.mDrawable != null) {
            Rect rect = this.mTmpRect;
            r.mDrawable.getPadding(rect);
            if (!(rect.left == this.mPaddingL[i] && rect.top == this.mPaddingT[i] && rect.right == this.mPaddingR[i] && rect.bottom == this.mPaddingB[i])) {
                this.mPaddingL[i] = rect.left;
                this.mPaddingT[i] = rect.top;
                this.mPaddingR[i] = rect.right;
                this.mPaddingB[i] = rect.bottom;
                return true;
            }
        }
        return false;
    }

    void ensurePadding() {
        int N = this.mLayerState.mNum;
        if (this.mPaddingL == null || this.mPaddingL.length < N) {
            this.mPaddingL = new int[N];
            this.mPaddingT = new int[N];
            this.mPaddingR = new int[N];
            this.mPaddingB = new int[N];
        }
    }

    void refreshPadding() {
        int N = this.mLayerState.mNum;
        ChildDrawable[] array = this.mLayerState.mChildren;
        for (int i = 0; i < N; i++) {
            refreshChildPadding(i, array[i]);
        }
    }

    public ConstantState getConstantState() {
        if (!this.mLayerState.canConstantState()) {
            return null;
        }
        this.mLayerState.mChangingConfigurations = getChangingConfigurations();
        return this.mLayerState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mLayerState = createConstantState(this.mLayerState, null);
            ChildDrawable[] array = this.mLayerState.mChildren;
            int N = this.mLayerState.mNum;
            for (int i = 0; i < N; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.mutate();
                }
            }
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.clearMutated();
            }
        }
        this.mMutated = false;
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        boolean changed = false;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int N = this.mLayerState.mNum;
        for (int i = 0; i < N; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                changed |= dr.setLayoutDirection(layoutDirection);
            }
        }
        updateLayerBounds(getBounds());
        return changed;
    }
}
