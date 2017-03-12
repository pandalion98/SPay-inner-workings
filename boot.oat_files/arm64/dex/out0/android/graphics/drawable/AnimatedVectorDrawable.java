package android.graphics.drawable;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatedVectorDrawable extends Drawable implements Animatable2 {
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVectorDrawable";
    private static final String TARGET = "target";
    private AnimatedVectorDrawableState mAnimatedVectorState;
    private ArrayList<AnimationCallback> mAnimationCallbacks;
    private AnimatorListener mAnimatorListener;
    private final AnimatorSet mAnimatorSet;
    private final Callback mCallback;
    private boolean mHasAnimatorSet;
    private boolean mMutated;
    private Resources mRes;

    private static class AnimatedVectorDrawableState extends ConstantState {
        ArrayList<Animator> mAnimators;
        int mChangingConfigurations;
        ArrayList<PendingAnimator> mPendingAnims;
        ArrayMap<Animator, String> mTargetNameMap;
        VectorDrawable mVectorDrawable;

        private static class PendingAnimator {
            public final int animResId;
            public final float pathErrorScale;
            public final String target;

            public PendingAnimator(int animResId, float pathErrorScale, String target) {
                this.animResId = animResId;
                this.pathErrorScale = pathErrorScale;
                this.target = target;
            }

            public Animator newInstance(Resources res, Theme theme) {
                return AnimatorInflater.loadAnimator(res, theme, this.animResId, this.pathErrorScale);
            }
        }

        public AnimatedVectorDrawableState(AnimatedVectorDrawableState copy, Callback owner, Resources res) {
            if (copy != null) {
                this.mChangingConfigurations = copy.mChangingConfigurations;
                if (copy.mVectorDrawable != null) {
                    ConstantState cs = copy.mVectorDrawable.getConstantState();
                    if (res != null) {
                        this.mVectorDrawable = (VectorDrawable) cs.newDrawable(res);
                    } else {
                        this.mVectorDrawable = (VectorDrawable) cs.newDrawable();
                    }
                    this.mVectorDrawable = (VectorDrawable) this.mVectorDrawable.mutate();
                    this.mVectorDrawable.setCallback(owner);
                    this.mVectorDrawable.setLayoutDirection(copy.mVectorDrawable.getLayoutDirection());
                    this.mVectorDrawable.setBounds(copy.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                if (copy.mAnimators != null) {
                    this.mAnimators = new ArrayList(copy.mAnimators);
                }
                if (copy.mTargetNameMap != null) {
                    this.mTargetNameMap = new ArrayMap(copy.mTargetNameMap);
                }
                if (copy.mPendingAnims != null) {
                    this.mPendingAnims = new ArrayList(copy.mPendingAnims);
                    return;
                }
                return;
            }
            this.mVectorDrawable = new VectorDrawable();
        }

        public boolean canApplyTheme() {
            return (this.mVectorDrawable != null && this.mVectorDrawable.canApplyTheme()) || this.mPendingAnims != null || super.canApplyTheme();
        }

        public Drawable newDrawable() {
            return new AnimatedVectorDrawable(this, null);
        }

        public Drawable newDrawable(Resources res) {
            return new AnimatedVectorDrawable(this, res);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }

        public void addPendingAnimator(int resId, float pathErrorScale, String target) {
            if (this.mPendingAnims == null) {
                this.mPendingAnims = new ArrayList(1);
            }
            this.mPendingAnims.add(new PendingAnimator(resId, pathErrorScale, target));
        }

        public void addTargetAnimator(String targetName, Animator animator) {
            if (this.mAnimators == null) {
                this.mAnimators = new ArrayList(1);
                this.mTargetNameMap = new ArrayMap(1);
            }
            this.mAnimators.add(animator);
            this.mTargetNameMap.put(animator, targetName);
        }

        public void prepareLocalAnimators(AnimatorSet animatorSet, Resources res) {
            if (this.mPendingAnims != null) {
                if (res != null) {
                    inflatePendingAnimators(res, null);
                } else {
                    Log.e(AnimatedVectorDrawable.LOGTAG, "Failed to load animators. Either the AnimatedVectorDrawable must be created using a Resources object or applyTheme() must be called with a non-null Theme object.");
                }
                this.mPendingAnims = null;
            }
            int count = this.mAnimators == null ? 0 : this.mAnimators.size();
            if (count > 0) {
                Builder builder = animatorSet.play(prepareLocalAnimator(0));
                for (int i = 1; i < count; i++) {
                    builder.with(prepareLocalAnimator(i));
                }
            }
        }

        private Animator prepareLocalAnimator(int index) {
            Animator animator = (Animator) this.mAnimators.get(index);
            Animator localAnimator = animator.clone();
            localAnimator.setTarget(this.mVectorDrawable.getTargetByName((String) this.mTargetNameMap.get(animator)));
            return localAnimator;
        }

        public void inflatePendingAnimators(Resources res, Theme t) {
            ArrayList<PendingAnimator> pendingAnims = this.mPendingAnims;
            if (pendingAnims != null) {
                this.mPendingAnims = null;
                int count = pendingAnims.size();
                for (int i = 0; i < count; i++) {
                    PendingAnimator pendingAnimator = (PendingAnimator) pendingAnims.get(i);
                    addTargetAnimator(pendingAnimator.target, pendingAnimator.newInstance(res, t));
                }
            }
        }
    }

    public AnimatedVectorDrawable() {
        this(null, null);
    }

    private AnimatedVectorDrawable(AnimatedVectorDrawableState state, Resources res) {
        this.mAnimatorSet = new AnimatorSet();
        this.mAnimationCallbacks = null;
        this.mAnimatorListener = null;
        this.mCallback = new Callback() {
            public void invalidateDrawable(Drawable who) {
                AnimatedVectorDrawable.this.invalidateSelf();
            }

            public void scheduleDrawable(Drawable who, Runnable what, long when) {
                AnimatedVectorDrawable.this.scheduleSelf(what, when);
            }

            public void unscheduleDrawable(Drawable who, Runnable what) {
                AnimatedVectorDrawable.this.unscheduleSelf(what);
            }
        };
        this.mAnimatedVectorState = new AnimatedVectorDrawableState(state, this.mCallback, res);
        this.mRes = res;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mAnimatedVectorState = new AnimatedVectorDrawableState(this.mAnimatedVectorState, this.mCallback, this.mRes);
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        if (this.mAnimatedVectorState.mVectorDrawable != null) {
            this.mAnimatedVectorState.mVectorDrawable.clearMutated();
        }
        this.mMutated = false;
    }

    public ConstantState getConstantState() {
        this.mAnimatedVectorState.mChangingConfigurations = getChangingConfigurations();
        return this.mAnimatedVectorState;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mAnimatedVectorState.getChangingConfigurations();
    }

    public void draw(Canvas canvas) {
        this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
        if (isStarted()) {
            invalidateSelf();
        }
    }

    protected void onBoundsChange(Rect bounds) {
        this.mAnimatedVectorState.mVectorDrawable.setBounds(bounds);
    }

    protected boolean onStateChange(int[] state) {
        return this.mAnimatedVectorState.mVectorDrawable.setState(state);
    }

    protected boolean onLevelChange(int level) {
        return this.mAnimatedVectorState.mVectorDrawable.setLevel(level);
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mAnimatedVectorState.mVectorDrawable.setLayoutDirection(layoutDirection);
    }

    public int getAlpha() {
        return this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }

    public void setAlpha(int alpha) {
        this.mAnimatedVectorState.mVectorDrawable.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }

    public void setTintList(ColorStateList tint) {
        this.mAnimatedVectorState.mVectorDrawable.setTintList(tint);
    }

    public void setHotspot(float x, float y) {
        this.mAnimatedVectorState.mVectorDrawable.setHotspot(x, y);
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        this.mAnimatedVectorState.mVectorDrawable.setHotspotBounds(left, top, right, bottom);
    }

    public void setTintMode(Mode tintMode) {
        this.mAnimatedVectorState.mVectorDrawable.setTintMode(tintMode);
    }

    public boolean setVisible(boolean visible, boolean restart) {
        this.mAnimatedVectorState.mVectorDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart);
    }

    public boolean isStateful() {
        return this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }

    public int getOpacity() {
        return this.mAnimatedVectorState.mVectorDrawable.getOpacity();
    }

    public int getIntrinsicWidth() {
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }

    public void getOutline(Outline outline) {
        this.mAnimatedVectorState.mVectorDrawable.getOutline(outline);
    }

    public Insets getOpticalInsets() {
        return this.mAnimatedVectorState.mVectorDrawable.getOpticalInsets();
    }

    public void inflate(Resources res, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        AnimatedVectorDrawableState state = this.mAnimatedVectorState;
        int eventType = parser.getEventType();
        float pathErrorScale = 1.0f;
        while (eventType != 1) {
            if (eventType == 2) {
                String tagName = parser.getName();
                TypedArray a;
                if (ANIMATED_VECTOR.equals(tagName)) {
                    a = Drawable.obtainAttributes(res, theme, attrs, R.styleable.AnimatedVectorDrawable);
                    int drawableRes = a.getResourceId(0, 0);
                    if (drawableRes != 0) {
                        VectorDrawable vectorDrawable = (VectorDrawable) res.getDrawable(drawableRes, theme).mutate();
                        vectorDrawable.setAllowCaching(false);
                        vectorDrawable.setCallback(this.mCallback);
                        pathErrorScale = vectorDrawable.getPixelSize();
                        if (state.mVectorDrawable != null) {
                            state.mVectorDrawable.setCallback(null);
                        }
                        state.mVectorDrawable = vectorDrawable;
                    }
                    a.recycle();
                } else if (TARGET.equals(tagName)) {
                    a = Drawable.obtainAttributes(res, theme, attrs, R.styleable.AnimatedVectorDrawableTarget);
                    String target = a.getString(0);
                    int animResId = a.getResourceId(1, 0);
                    if (animResId != 0) {
                        if (theme != null) {
                            state.addTargetAnimator(target, AnimatorInflater.loadAnimator(res, theme, animResId, pathErrorScale));
                        } else {
                            state.addPendingAnimator(animResId, pathErrorScale, target);
                        }
                    }
                    a.recycle();
                }
            }
            eventType = parser.next();
        }
        if (state.mPendingAnims == null) {
            res = null;
        }
        this.mRes = res;
    }

    public boolean canApplyTheme() {
        return (this.mAnimatedVectorState != null && this.mAnimatedVectorState.canApplyTheme()) || super.canApplyTheme();
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        VectorDrawable vectorDrawable = this.mAnimatedVectorState.mVectorDrawable;
        if (vectorDrawable != null && vectorDrawable.canApplyTheme()) {
            vectorDrawable.applyTheme(t);
        }
        if (t != null) {
            this.mAnimatedVectorState.inflatePendingAnimators(t.getResources(), t);
        }
        if (this.mAnimatedVectorState.mPendingAnims == null) {
            this.mRes = null;
        }
    }

    public boolean isRunning() {
        return this.mAnimatorSet.isRunning();
    }

    private boolean isStarted() {
        return this.mAnimatorSet.isStarted();
    }

    public void reset() {
        start();
        this.mAnimatorSet.cancel();
    }

    public void start() {
        ensureAnimatorSet();
        if (!isStarted()) {
            this.mAnimatorSet.start();
            invalidateSelf();
        }
    }

    private void ensureAnimatorSet() {
        if (!this.mHasAnimatorSet) {
            this.mAnimatedVectorState.prepareLocalAnimators(this.mAnimatorSet, this.mRes);
            this.mHasAnimatorSet = true;
            this.mRes = null;
        }
    }

    public void stop() {
        this.mAnimatorSet.end();
    }

    public void reverse() {
        ensureAnimatorSet();
        if (canReverse()) {
            this.mAnimatorSet.reverse();
            invalidateSelf();
            return;
        }
        Log.w(LOGTAG, "AnimatedVectorDrawable can't reverse()");
    }

    public boolean canReverse() {
        return this.mAnimatorSet.canReverse();
    }

    public void registerAnimationCallback(AnimationCallback callback) {
        if (callback != null) {
            if (this.mAnimationCallbacks == null) {
                this.mAnimationCallbacks = new ArrayList();
            }
            this.mAnimationCallbacks.add(callback);
            if (this.mAnimatorListener == null) {
                this.mAnimatorListener = new AnimatorListenerAdapter() {
                    public void onAnimationStart(Animator animation) {
                        ArrayList<AnimationCallback> tmpCallbacks = new ArrayList(AnimatedVectorDrawable.this.mAnimationCallbacks);
                        int size = tmpCallbacks.size();
                        for (int i = 0; i < size; i++) {
                            ((AnimationCallback) tmpCallbacks.get(i)).onAnimationStart(AnimatedVectorDrawable.this);
                        }
                    }

                    public void onAnimationEnd(Animator animation) {
                        ArrayList<AnimationCallback> tmpCallbacks = new ArrayList(AnimatedVectorDrawable.this.mAnimationCallbacks);
                        int size = tmpCallbacks.size();
                        for (int i = 0; i < size; i++) {
                            ((AnimationCallback) tmpCallbacks.get(i)).onAnimationEnd(AnimatedVectorDrawable.this);
                        }
                    }
                };
            }
            this.mAnimatorSet.addListener(this.mAnimatorListener);
        }
    }

    private void removeAnimatorSetListener() {
        if (this.mAnimatorListener != null) {
            this.mAnimatorSet.removeListener(this.mAnimatorListener);
            this.mAnimatorListener = null;
        }
    }

    public boolean unregisterAnimationCallback(AnimationCallback callback) {
        if (this.mAnimationCallbacks == null || callback == null) {
            return false;
        }
        boolean removed = this.mAnimationCallbacks.remove(callback);
        if (this.mAnimationCallbacks.size() != 0) {
            return removed;
        }
        removeAnimatorSetListener();
        return removed;
    }

    public void clearAnimationCallbacks() {
        removeAnimatorSetListener();
        if (this.mAnimationCallbacks != null) {
            this.mAnimationCallbacks.clear();
        }
    }
}
