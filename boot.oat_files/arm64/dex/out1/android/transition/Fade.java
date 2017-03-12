package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.transition.Transition.TransitionListenerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import com.android.internal.R;

public class Fade extends Visibility {
    private static boolean DBG = false;
    public static final int IN = 1;
    private static final String LOG_TAG = "Fade";
    public static final int OUT = 2;

    private static class FadeAnimatorListener extends AnimatorListenerAdapter {
        private boolean mLayerTypeChanged = false;
        private float mPausedAlpha = LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        private final View mView;

        public FadeAnimatorListener(View view) {
            this.mView = view;
        }

        public void onAnimationStart(Animator animator) {
            if (this.mView.hasOverlappingRendering() && this.mView.getLayerType() == 0) {
                this.mLayerTypeChanged = true;
                this.mView.setLayerType(2, null);
            }
        }

        public void onAnimationEnd(Animator animator) {
            this.mView.setTransitionAlpha(1.0f);
            if (this.mLayerTypeChanged) {
                this.mView.setLayerType(0, null);
            }
        }

        public void onAnimationPause(Animator animator) {
            this.mPausedAlpha = this.mView.getTransitionAlpha();
            this.mView.setTransitionAlpha(1.0f);
        }

        public void onAnimationResume(Animator animator) {
            this.mView.setTransitionAlpha(this.mPausedAlpha);
        }
    }

    public Fade(int fadingMode) {
        setMode(fadingMode);
    }

    public Fade(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMode(context.obtainStyledAttributes(attrs, R.styleable.Fade).getInt(0, getMode()));
    }

    private Animator createAnimation(final View view, float startAlpha, float endAlpha) {
        if (startAlpha == endAlpha) {
            return null;
        }
        view.setTransitionAlpha(startAlpha);
        Animator anim = ObjectAnimator.ofFloat(view, "transitionAlpha", new float[]{endAlpha});
        if (DBG) {
            Log.d(LOG_TAG, "Created animator " + anim);
        }
        FadeAnimatorListener listener = new FadeAnimatorListener(view);
        anim.addListener(listener);
        anim.addPauseListener(listener);
        addListener(new TransitionListenerAdapter() {
            public void onTransitionEnd(Transition transition) {
                view.setTransitionAlpha(1.0f);
                transition.removeListener(this);
            }
        });
        return anim;
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        if (DBG) {
            Log.d(LOG_TAG, "Fade.onAppear: startView, startVis, endView, endVis = " + (startValues != null ? startValues.view : null) + ", " + view);
        }
        return createAnimation(view, 0.0f, 1.0f);
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return createAnimation(view, 1.0f, 0.0f);
    }
}
