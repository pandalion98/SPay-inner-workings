package android.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity.TranslucentConversionListener;
import android.app.SharedElementCallback.OnSharedElementsReadyListener;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.transition.Transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.util.ArrayList;

class ExitTransitionCoordinator extends ActivityTransitionCoordinator {
    private static final long MAX_WAIT_MS = 1000;
    private static final String TAG = "ExitTransitionCoordinator";
    private Activity mActivity;
    private ObjectAnimator mBackgroundAnimator;
    private boolean mExitNotified;
    private Bundle mExitSharedElementBundle;
    private Handler mHandler;
    private boolean mIsBackgroundReady;
    private boolean mIsCanceled;
    private boolean mIsExitStarted;
    private boolean mIsHidden;
    private Bundle mSharedElementBundle;
    private boolean mSharedElementNotified;
    private boolean mSharedElementsHidden;

    public ExitTransitionCoordinator(Activity activity, ArrayList<String> names, ArrayList<String> accepted, ArrayList<View> mapped, boolean isReturning) {
        super(activity.getWindow(), names, getListener(activity, isReturning), isReturning);
        viewsReady(mapSharedElements(accepted, mapped));
        stripOffscreenViews();
        this.mIsBackgroundReady = !isReturning;
        this.mActivity = activity;
    }

    private static SharedElementCallback getListener(Activity activity, boolean isReturning) {
        return isReturning ? activity.mEnterTransitionListener : activity.mExitTransitionListener;
    }

    protected void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case 100:
                stopCancel();
                this.mResultReceiver = (ResultReceiver) resultData.getParcelable("android:remoteReceiver");
                if (this.mIsCanceled) {
                    this.mResultReceiver.send(106, null);
                    this.mResultReceiver = null;
                    return;
                }
                notifyComplete();
                return;
            case 101:
                stopCancel();
                if (!this.mIsCanceled) {
                    hideSharedElements();
                    return;
                }
                return;
            case 105:
                this.mHandler.removeMessages(106);
                startExit();
                return;
            case 107:
                this.mExitSharedElementBundle = resultData;
                sharedElementExitBack();
                return;
            default:
                return;
        }
    }

    private void stopCancel() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(106);
        }
    }

    private void delayCancel() {
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessageDelayed(106, MAX_WAIT_MS);
        }
    }

    public void resetViews() {
        if (this.mTransitioningViews != null) {
            showViews(this.mTransitioningViews, true);
        }
        showViews(this.mSharedElements, true);
        this.mIsHidden = true;
        ViewGroup decorView = getDecor();
        if (!(this.mIsReturning || decorView == null)) {
            decorView.suppressLayout(false);
        }
        moveSharedElementsFromOverlay();
        clearState();
    }

    private void sharedElementExitBack() {
        final ViewGroup decorView = getDecor();
        if (decorView != null) {
            decorView.suppressLayout(true);
        }
        if (decorView == null || this.mExitSharedElementBundle == null || this.mExitSharedElementBundle.isEmpty() || this.mSharedElements.isEmpty() || getSharedElementTransition() == null) {
            sharedElementTransitionComplete();
        } else {
            startTransition(new Runnable() {
                public void run() {
                    ExitTransitionCoordinator.this.startSharedElementExit(decorView);
                }
            });
        }
    }

    private void startSharedElementExit(final ViewGroup decorView) {
        Transition transition = getSharedElementExitTransition();
        transition.addListener(new TransitionListenerAdapter() {
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                if (ExitTransitionCoordinator.this.isViewsTransitionComplete()) {
                    ExitTransitionCoordinator.this.delayCancel();
                }
            }
        });
        final ArrayList<View> sharedElementSnapshots = createSnapshots(this.mExitSharedElementBundle, this.mSharedElementNames);
        decorView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                ExitTransitionCoordinator.this.setSharedElementState(ExitTransitionCoordinator.this.mExitSharedElementBundle, sharedElementSnapshots);
                return true;
            }
        });
        setGhostVisibility(4);
        scheduleGhostVisibilityChange(4);
        if (this.mListener != null) {
            this.mListener.onSharedElementEnd(this.mSharedElementNames, this.mSharedElements, sharedElementSnapshots);
        }
        TransitionManager.beginDelayedTransition(decorView, transition);
        scheduleGhostVisibilityChange(0);
        setGhostVisibility(0);
        decorView.invalidate();
    }

    private void hideSharedElements() {
        moveSharedElementsFromOverlay();
        if (!this.mIsHidden) {
            hideViews(this.mSharedElements);
        }
        this.mSharedElementsHidden = true;
        finishIfNecessary();
    }

    public void startExit() {
        if (!this.mIsExitStarted) {
            this.mIsExitStarted = true;
            pauseInput();
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.suppressLayout(true);
            }
            moveSharedElementsToOverlay();
            startTransition(new Runnable() {
                public void run() {
                    ExitTransitionCoordinator.this.beginTransitions();
                }
            });
        }
    }

    public void startExit(int resultCode, Intent data) {
        boolean targetsM = true;
        if (!this.mIsExitStarted) {
            this.mIsExitStarted = true;
            pauseInput();
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.suppressLayout(true);
            }
            this.mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    ExitTransitionCoordinator.this.mIsCanceled = true;
                    ExitTransitionCoordinator.this.finish();
                }
            };
            delayCancel();
            moveSharedElementsToOverlay();
            if (decorView != null && decorView.getBackground() == null) {
                getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
            }
            if (decorView != null && decorView.getContext().getApplicationInfo().targetSdkVersion < 23) {
                targetsM = false;
            }
            this.mActivity.convertToTranslucent(new TranslucentConversionListener() {
                public void onTranslucentConversionComplete(boolean drawComplete) {
                    if (!ExitTransitionCoordinator.this.mIsCanceled) {
                        ExitTransitionCoordinator.this.fadeOutBackground();
                    }
                }
            }, ActivityOptions.makeSceneTransitionAnimation(this.mActivity, this, targetsM ? this.mSharedElementNames : this.mAllSharedElementNames, resultCode, data));
            startTransition(new Runnable() {
                public void run() {
                    ExitTransitionCoordinator.this.startExitTransition();
                }
            });
        }
    }

    public void stop() {
        if (this.mIsReturning && this.mActivity != null) {
            this.mActivity.convertToTranslucent(null, null);
            finish();
        }
    }

    private void startExitTransition() {
        Transition transition = getExitTransition();
        ViewGroup decorView = getDecor();
        if (transition == null || decorView == null || this.mTransitioningViews == null) {
            transitionStarted();
            return;
        }
        TransitionManager.beginDelayedTransition(decorView, transition);
        ((View) this.mTransitioningViews.get(0)).invalidate();
    }

    private void fadeOutBackground() {
        if (this.mBackgroundAnimator == null) {
            ViewGroup decor = getDecor();
            if (decor != null) {
                Drawable background = decor.getBackground();
                if (background != null) {
                    Object background2 = background.mutate();
                    getWindow().setBackgroundDrawable(background2);
                    this.mBackgroundAnimator = ObjectAnimator.ofInt(background2, "alpha", 0);
                    this.mBackgroundAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            ExitTransitionCoordinator.this.mBackgroundAnimator = null;
                            if (!ExitTransitionCoordinator.this.mIsCanceled) {
                                ExitTransitionCoordinator.this.mIsBackgroundReady = true;
                                ExitTransitionCoordinator.this.notifyComplete();
                            }
                        }
                    });
                    this.mBackgroundAnimator.setDuration(getFadeDuration());
                    this.mBackgroundAnimator.start();
                    return;
                }
            }
            this.mIsBackgroundReady = true;
        }
    }

    private Transition getExitTransition() {
        Transition viewsTransition = null;
        if (!(this.mTransitioningViews == null || this.mTransitioningViews.isEmpty())) {
            viewsTransition = configureTransition(getViewsTransition(), true);
        }
        if (viewsTransition == null) {
            viewsTransitionComplete();
        } else {
            final ArrayList<View> transitioningViews = this.mTransitioningViews;
            viewsTransition.addListener(new ContinueTransitionListener() {
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                    ExitTransitionCoordinator.this.viewsTransitionComplete();
                    if (ExitTransitionCoordinator.this.mIsHidden && transitioningViews != null) {
                        ExitTransitionCoordinator.this.showViews(transitioningViews, true);
                    }
                    if (ExitTransitionCoordinator.this.mSharedElementBundle != null) {
                        ExitTransitionCoordinator.this.delayCancel();
                    }
                    super.onTransitionEnd(transition);
                }
            });
            viewsTransition.forceVisibility(4, false);
        }
        return viewsTransition;
    }

    private Transition getSharedElementExitTransition() {
        Transition sharedElementTransition = null;
        if (!this.mSharedElements.isEmpty()) {
            sharedElementTransition = configureTransition(getSharedElementTransition(), false);
        }
        if (sharedElementTransition == null) {
            sharedElementTransitionComplete();
        } else {
            sharedElementTransition.addListener(new ContinueTransitionListener() {
                public void onTransitionEnd(Transition transition) {
                    transition.removeListener(this);
                    ExitTransitionCoordinator.this.sharedElementTransitionComplete();
                    if (ExitTransitionCoordinator.this.mIsHidden) {
                        ExitTransitionCoordinator.this.showViews(ExitTransitionCoordinator.this.mSharedElements, true);
                    }
                }
            });
            ((View) this.mSharedElements.get(0)).invalidate();
        }
        return sharedElementTransition;
    }

    private void beginTransitions() {
        Transition transition = ActivityTransitionCoordinator.mergeTransitions(getSharedElementExitTransition(), getExitTransition());
        ViewGroup decorView = getDecor();
        if (transition == null || decorView == null) {
            transitionStarted();
            return;
        }
        setGhostVisibility(4);
        scheduleGhostVisibilityChange(4);
        TransitionManager.beginDelayedTransition(decorView, transition);
        scheduleGhostVisibilityChange(0);
        setGhostVisibility(0);
        decorView.invalidate();
    }

    protected boolean isReadyToNotify() {
        return (this.mSharedElementBundle == null || this.mResultReceiver == null || !this.mIsBackgroundReady) ? false : true;
    }

    protected void sharedElementTransitionComplete() {
        this.mSharedElementBundle = this.mExitSharedElementBundle == null ? captureSharedElementState() : captureExitSharedElementsState();
        super.sharedElementTransitionComplete();
    }

    private Bundle captureExitSharedElementsState() {
        Bundle bundle = new Bundle();
        RectF bounds = new RectF();
        Matrix matrix = new Matrix();
        for (int i = 0; i < this.mSharedElements.size(); i++) {
            String name = (String) this.mSharedElementNames.get(i);
            Bundle sharedElementState = this.mExitSharedElementBundle.getBundle(name);
            if (sharedElementState != null) {
                bundle.putBundle(name, sharedElementState);
            } else {
                captureSharedElementState((View) this.mSharedElements.get(i), name, bundle, matrix, bounds);
            }
        }
        return bundle;
    }

    protected void onTransitionsComplete() {
        notifyComplete();
    }

    protected void notifyComplete() {
        if (!isReadyToNotify()) {
            return;
        }
        if (this.mSharedElementNotified) {
            notifyExitComplete();
            return;
        }
        this.mSharedElementNotified = true;
        delayCancel();
        if (this.mListener == null) {
            this.mResultReceiver.send(103, this.mSharedElementBundle);
            notifyExitComplete();
            return;
        }
        final ResultReceiver resultReceiver = this.mResultReceiver;
        final Bundle sharedElementBundle = this.mSharedElementBundle;
        this.mListener.onSharedElementsArrived(this.mSharedElementNames, this.mSharedElements, new OnSharedElementsReadyListener() {
            public void onSharedElementsReady() {
                resultReceiver.send(103, sharedElementBundle);
                ExitTransitionCoordinator.this.notifyExitComplete();
            }
        });
    }

    private void notifyExitComplete() {
        if (!this.mExitNotified && isViewsTransitionComplete()) {
            this.mExitNotified = true;
            this.mResultReceiver.send(104, null);
            this.mResultReceiver = null;
            ViewGroup decorView = getDecor();
            if (!(this.mIsReturning || decorView == null)) {
                decorView.suppressLayout(false);
            }
            finishIfNecessary();
        }
    }

    private void finishIfNecessary() {
        if (this.mIsReturning && this.mExitNotified && this.mActivity != null && (this.mSharedElements.isEmpty() || this.mSharedElementsHidden)) {
            finish();
        }
        if (!this.mIsReturning && this.mExitNotified) {
            this.mActivity = null;
        }
    }

    private void finish() {
        stopCancel();
        if (this.mActivity != null) {
            this.mActivity.mActivityTransitionState.clear();
            this.mActivity.finish();
            this.mActivity.overridePendingTransition(0, 0);
            this.mActivity = null;
        }
        this.mHandler = null;
        this.mSharedElementBundle = null;
        if (this.mBackgroundAnimator != null) {
            this.mBackgroundAnimator.cancel();
            this.mBackgroundAnimator = null;
        }
        this.mExitSharedElementBundle = null;
        clearState();
    }

    protected boolean moveSharedElementWithParent() {
        return !this.mIsReturning;
    }

    protected Transition getViewsTransition() {
        if (this.mIsReturning) {
            return getWindow().getReturnTransition();
        }
        return getWindow().getExitTransition();
    }

    protected Transition getSharedElementTransition() {
        if (this.mIsReturning) {
            return getWindow().getSharedElementReturnTransition();
        }
        return getWindow().getSharedElementExitTransition();
    }
}
