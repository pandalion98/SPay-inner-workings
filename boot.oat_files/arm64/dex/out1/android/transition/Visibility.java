package android.transition;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.Animator.AnimatorPauseListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Transition.TransitionListenerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R;

public abstract class Visibility extends Transition {
    public static final int MODE_IN = 1;
    public static final int MODE_OUT = 2;
    private static final String PROPNAME_PARENT = "android:visibility:parent";
    private static final String PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation";
    static final String PROPNAME_VISIBILITY = "android:visibility:visibility";
    private static final String[] sTransitionProperties = new String[]{PROPNAME_VISIBILITY, PROPNAME_PARENT};
    private int mForcedEndVisibility = -1;
    private int mForcedStartVisibility = -1;
    private int mMode = 3;

    private static class DisappearListener extends TransitionListenerAdapter implements AnimatorListener, AnimatorPauseListener {
        boolean mCanceled = false;
        private final int mFinalVisibility;
        private boolean mFinalVisibilitySet = false;
        private final boolean mIsForcedVisibility;
        private boolean mLayoutSuppressed;
        private final ViewGroup mParent;
        private final View mView;

        public DisappearListener(View view, int finalVisibility, boolean isForcedVisibility) {
            this.mView = view;
            this.mIsForcedVisibility = isForcedVisibility;
            this.mFinalVisibility = finalVisibility;
            this.mParent = (ViewGroup) view.getParent();
            suppressLayout(true);
        }

        public void onAnimationPause(Animator animation) {
            if (!this.mCanceled && !this.mIsForcedVisibility) {
                this.mView.setTransitionVisibility(this.mFinalVisibility);
            }
        }

        public void onAnimationResume(Animator animation) {
            if (!this.mCanceled && !this.mIsForcedVisibility) {
                this.mView.setTransitionVisibility(0);
            }
        }

        public void onAnimationCancel(Animator animation) {
            this.mCanceled = true;
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            hideViewWhenNotCanceled();
        }

        public void onTransitionEnd(Transition transition) {
            hideViewWhenNotCanceled();
        }

        public void onTransitionPause(Transition transition) {
            suppressLayout(false);
        }

        public void onTransitionResume(Transition transition) {
            suppressLayout(true);
        }

        private void hideViewWhenNotCanceled() {
            if (!this.mCanceled) {
                if (this.mIsForcedVisibility) {
                    this.mView.setTransitionAlpha(0.0f);
                } else if (!this.mFinalVisibilitySet) {
                    this.mView.setTransitionVisibility(this.mFinalVisibility);
                    if (this.mParent != null) {
                        this.mParent.invalidate();
                    }
                    this.mFinalVisibilitySet = true;
                }
            }
            suppressLayout(false);
        }

        private void suppressLayout(boolean suppress) {
            if (this.mLayoutSuppressed != suppress && this.mParent != null && !this.mIsForcedVisibility) {
                this.mLayoutSuppressed = suppress;
                this.mParent.suppressLayout(suppress);
            }
        }
    }

    private static class VisibilityInfo {
        ViewGroup endParent;
        int endVisibility;
        boolean fadeIn;
        ViewGroup startParent;
        int startVisibility;
        boolean visibilityChange;

        private VisibilityInfo() {
        }
    }

    public Visibility(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VisibilityTransition);
        int mode = a.getInt(0, 0);
        a.recycle();
        if (mode != 0) {
            setMode(mode);
        }
    }

    public void setMode(int mode) {
        if ((mode & -4) != 0) {
            throw new IllegalArgumentException("Only MODE_IN and MODE_OUT flags are allowed");
        }
        this.mMode = mode;
    }

    public int getMode() {
        return this.mMode;
    }

    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    private void captureValues(TransitionValues transitionValues, int forcedVisibility) {
        int visibility;
        if (forcedVisibility != -1) {
            visibility = forcedVisibility;
        } else {
            visibility = transitionValues.view.getVisibility();
        }
        transitionValues.values.put(PROPNAME_VISIBILITY, Integer.valueOf(visibility));
        transitionValues.values.put(PROPNAME_PARENT, transitionValues.view.getParent());
        int[] loc = new int[2];
        transitionValues.view.getLocationOnScreen(loc);
        transitionValues.values.put(PROPNAME_SCREEN_LOCATION, loc);
    }

    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues, this.mForcedStartVisibility);
    }

    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues, this.mForcedEndVisibility);
    }

    public void forceVisibility(int visibility, boolean isStartValue) {
        if (isStartValue) {
            this.mForcedStartVisibility = visibility;
        } else {
            this.mForcedEndVisibility = visibility;
        }
    }

    public boolean isVisible(TransitionValues values) {
        if (values == null) {
            return false;
        }
        boolean z = ((Integer) values.values.get(PROPNAME_VISIBILITY)).intValue() == 0 && ((View) values.values.get(PROPNAME_PARENT)) != null;
        return z;
    }

    private static VisibilityInfo getVisibilityChangeInfo(TransitionValues startValues, TransitionValues endValues) {
        VisibilityInfo visInfo = new VisibilityInfo();
        visInfo.visibilityChange = false;
        visInfo.fadeIn = false;
        if (startValues == null || !startValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visInfo.startVisibility = -1;
            visInfo.startParent = null;
        } else {
            visInfo.startVisibility = ((Integer) startValues.values.get(PROPNAME_VISIBILITY)).intValue();
            visInfo.startParent = (ViewGroup) startValues.values.get(PROPNAME_PARENT);
        }
        if (endValues == null || !endValues.values.containsKey(PROPNAME_VISIBILITY)) {
            visInfo.endVisibility = -1;
            visInfo.endParent = null;
        } else {
            visInfo.endVisibility = ((Integer) endValues.values.get(PROPNAME_VISIBILITY)).intValue();
            visInfo.endParent = (ViewGroup) endValues.values.get(PROPNAME_PARENT);
        }
        if (startValues == null || endValues == null) {
            if (startValues == null && visInfo.endVisibility == 0) {
                visInfo.fadeIn = true;
                visInfo.visibilityChange = true;
            } else if (endValues == null && visInfo.startVisibility == 0) {
                visInfo.fadeIn = false;
                visInfo.visibilityChange = true;
            }
        } else if (!(visInfo.startVisibility == visInfo.endVisibility && visInfo.startParent == visInfo.endParent)) {
            if (visInfo.startVisibility != visInfo.endVisibility) {
                if (visInfo.startVisibility == 0) {
                    visInfo.fadeIn = false;
                    visInfo.visibilityChange = true;
                } else if (visInfo.endVisibility == 0) {
                    visInfo.fadeIn = true;
                    visInfo.visibilityChange = true;
                }
            } else if (visInfo.startParent != visInfo.endParent) {
                if (visInfo.endParent == null) {
                    visInfo.fadeIn = false;
                    visInfo.visibilityChange = true;
                } else if (visInfo.startParent == null) {
                    visInfo.fadeIn = true;
                    visInfo.visibilityChange = true;
                }
            }
        }
        return visInfo;
    }

    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        VisibilityInfo visInfo = getVisibilityChangeInfo(startValues, endValues);
        if (!visInfo.visibilityChange || (visInfo.startParent == null && visInfo.endParent == null)) {
            return null;
        }
        if (visInfo.fadeIn) {
            return onAppear(sceneRoot, startValues, visInfo.startVisibility, endValues, visInfo.endVisibility);
        }
        return onDisappear(sceneRoot, startValues, visInfo.startVisibility, endValues, visInfo.endVisibility);
    }

    public Animator onAppear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        boolean isForcedVisibility = false;
        if ((this.mMode & 1) != 1 || endValues == null) {
            return null;
        }
        if (startValues == null) {
            View endParent = (View) endValues.view.getParent();
            if (getVisibilityChangeInfo(getMatchedTransitionValues(endParent, false), getTransitionValues(endParent, false)).visibilityChange) {
                return null;
            }
        }
        if (!(this.mForcedStartVisibility == -1 && this.mForcedEndVisibility == -1)) {
            isForcedVisibility = true;
        }
        if (isForcedVisibility) {
            endValues.view.setTransitionAlpha(1.0f);
        }
        return onAppear(sceneRoot, endValues.view, startValues, endValues);
    }

    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }

    public Animator onDisappear(ViewGroup sceneRoot, TransitionValues startValues, int startVisibility, TransitionValues endValues, int endVisibility) {
        if ((this.mMode & 2) != 2) {
            return null;
        }
        View startView = startValues != null ? startValues.view : null;
        View endView = endValues != null ? endValues.view : null;
        View overlayView = null;
        View viewToKeep = null;
        if (endView == null || endView.getParent() == null) {
            if (endView != null) {
                overlayView = endView;
            } else if (startView != null) {
                if (startView.getParent() == null) {
                    overlayView = startView;
                } else if (startView.getParent() instanceof View) {
                    View startParent = (View) startView.getParent();
                    if (!getVisibilityChangeInfo(getTransitionValues(startParent, true), getMatchedTransitionValues(startParent, true)).visibilityChange) {
                        overlayView = TransitionUtils.copyViewImage(sceneRoot, startView, startParent);
                    } else if (startParent.getParent() == null) {
                        int id = startParent.getId();
                        if (!(id == -1 || sceneRoot.findViewById(id) == null || !this.mCanRemoveViews)) {
                            overlayView = startView;
                        }
                    }
                }
            }
        } else if (endVisibility == 4) {
            viewToKeep = endView;
        } else if (startView == endView) {
            viewToKeep = endView;
        } else {
            overlayView = startView;
        }
        int finalVisibility = endVisibility;
        final ViewGroup finalSceneRoot = sceneRoot;
        Animator animator;
        if (overlayView != null) {
            int[] screenLoc = (int[]) startValues.values.get(PROPNAME_SCREEN_LOCATION);
            int screenX = screenLoc[0];
            int screenY = screenLoc[1];
            int[] loc = new int[2];
            sceneRoot.getLocationOnScreen(loc);
            overlayView.offsetLeftAndRight((screenX - loc[0]) - overlayView.getLeft());
            overlayView.offsetTopAndBottom((screenY - loc[1]) - overlayView.getTop());
            sceneRoot.getOverlay().add(overlayView);
            animator = onDisappear(sceneRoot, overlayView, startValues, endValues);
            if (animator == null) {
                sceneRoot.getOverlay().remove(overlayView);
                return animator;
            }
            final View finalOverlayView = overlayView;
            addListener(new TransitionListenerAdapter() {
                public void onTransitionEnd(Transition transition) {
                    finalSceneRoot.getOverlay().remove(finalOverlayView);
                }
            });
            return animator;
        } else if (viewToKeep == null) {
            return null;
        } else {
            int originalVisibility = -1;
            boolean isForcedVisibility = (this.mForcedStartVisibility == -1 && this.mForcedEndVisibility == -1) ? false : true;
            if (!isForcedVisibility) {
                originalVisibility = viewToKeep.getVisibility();
                viewToKeep.setTransitionVisibility(0);
            }
            animator = onDisappear(sceneRoot, viewToKeep, startValues, endValues);
            if (animator != null) {
                DisappearListener disappearListener = new DisappearListener(viewToKeep, finalVisibility, isForcedVisibility);
                animator.addListener(disappearListener);
                animator.addPauseListener(disappearListener);
                addListener(disappearListener);
                return animator;
            } else if (isForcedVisibility) {
                return animator;
            } else {
                viewToKeep.setTransitionVisibility(originalVisibility);
                return animator;
            }
        }
    }

    public boolean isTransitionRequired(TransitionValues startValues, TransitionValues newValues) {
        if (startValues == null && newValues == null) {
            return false;
        }
        if (startValues != null && newValues != null && newValues.values.containsKey(PROPNAME_VISIBILITY) != startValues.values.containsKey(PROPNAME_VISIBILITY)) {
            return false;
        }
        VisibilityInfo changeInfo = getVisibilityChangeInfo(startValues, newValues);
        if (!changeInfo.visibilityChange) {
            return false;
        }
        if (changeInfo.startVisibility == 0 || changeInfo.endVisibility == 0) {
            return true;
        }
        return false;
    }

    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        return null;
    }
}
