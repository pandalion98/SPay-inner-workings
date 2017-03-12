package android.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.SharedElementCallback.OnSharedElementsReadyListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.Transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import java.util.ArrayList;

class EnterTransitionCoordinator extends ActivityTransitionCoordinator {
    private static final int MIN_ANIMATION_FRAMES = 2;
    private static final String TAG = "EnterTransitionCoordinator";
    private Activity mActivity;
    private boolean mAreViewsReady;
    private ObjectAnimator mBackgroundAnimator;
    private Transition mEnterViewsTransition;
    private boolean mHasStopped;
    private boolean mIsCanceled;
    private boolean mIsExitTransitionComplete;
    private boolean mIsReadyForTransition;
    private boolean mIsViewsTransitionStarted;
    private boolean mSharedElementTransitionStarted;
    private Bundle mSharedElementsBundle;
    private boolean mWasOpaque;

    public EnterTransitionCoordinator(Activity activity, ResultReceiver resultReceiver, ArrayList<String> sharedElementNames, boolean isReturning) {
        super(activity.getWindow(), sharedElementNames, getListener(activity, isReturning), isReturning);
        this.mActivity = activity;
        setResultReceiver(resultReceiver);
        prepareEnter();
        Bundle resultReceiverBundle = new Bundle();
        resultReceiverBundle.putParcelable("android:remoteReceiver", this);
        this.mResultReceiver.send(100, resultReceiverBundle);
        final View decorView = getDecor();
        if (decorView != null) {
            decorView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (EnterTransitionCoordinator.this.mIsReadyForTransition) {
                        decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    return EnterTransitionCoordinator.this.mIsReadyForTransition;
                }
            });
        }
    }

    public void viewInstancesReady(ArrayList<String> accepted, ArrayList<String> localNames, ArrayList<View> localViews) {
        boolean remap = false;
        for (int i = 0; i < localViews.size(); i++) {
            View view = (View) localViews.get(i);
            if (!TextUtils.equals(view.getTransitionName(), (CharSequence) localNames.get(i)) || !view.isAttachedToWindow()) {
                remap = true;
                break;
            }
        }
        if (remap) {
            triggerViewsReady(mapNamedElements(accepted, localNames));
        } else {
            triggerViewsReady(mapSharedElements(accepted, localViews));
        }
    }

    public void namedViewsReady(ArrayList<String> accepted, ArrayList<String> localNames) {
        triggerViewsReady(mapNamedElements(accepted, localNames));
    }

    public Transition getEnterViewsTransition() {
        return this.mEnterViewsTransition;
    }

    protected void viewsReady(ArrayMap<String, View> sharedElements) {
        super.viewsReady(sharedElements);
        this.mIsReadyForTransition = true;
        hideViews(this.mSharedElements);
        if (!(getViewsTransition() == null || this.mTransitioningViews == null)) {
            hideViews(this.mTransitioningViews);
        }
        if (this.mIsReturning) {
            sendSharedElementDestination();
        } else {
            moveSharedElementsToOverlay();
        }
        if (this.mSharedElementsBundle != null) {
            onTakeSharedElements();
        }
    }

    private void triggerViewsReady(final ArrayMap<String, View> sharedElements) {
        if (!this.mAreViewsReady) {
            this.mAreViewsReady = true;
            final ViewGroup decor = getDecor();
            if (decor == null || (decor.isAttachedToWindow() && (sharedElements.isEmpty() || !((View) sharedElements.valueAt(0)).isLayoutRequested()))) {
                viewsReady(sharedElements);
            } else {
                decor.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        decor.getViewTreeObserver().removeOnPreDrawListener(this);
                        EnterTransitionCoordinator.this.viewsReady(sharedElements);
                        return true;
                    }
                });
            }
        }
    }

    private ArrayMap<String, View> mapNamedElements(ArrayList<String> accepted, ArrayList<String> localNames) {
        ArrayMap<String, View> sharedElements = new ArrayMap();
        ViewGroup decorView = getDecor();
        if (decorView != null) {
            decorView.findNamedViews(sharedElements);
        }
        if (accepted != null) {
            for (int i = 0; i < localNames.size(); i++) {
                String localName = (String) localNames.get(i);
                String acceptedName = (String) accepted.get(i);
                if (!(localName == null || localName.equals(acceptedName))) {
                    View view = (View) sharedElements.remove(localName);
                    if (view != null) {
                        sharedElements.put(acceptedName, view);
                    }
                }
            }
        }
        return sharedElements;
    }

    private void sendSharedElementDestination() {
        final View decorView = getDecor();
        if (getWindow() != null) {
            boolean allReady;
            if (allowOverlappingTransitions() && getEnterViewsTransition() != null) {
                allReady = false;
            } else if (decorView == null) {
                allReady = true;
            } else {
                allReady = !decorView.isLayoutRequested();
                if (allReady) {
                    for (int i = 0; i < this.mSharedElements.size(); i++) {
                        if (((View) this.mSharedElements.get(i)).isLayoutRequested()) {
                            allReady = false;
                            break;
                        }
                    }
                }
            }
            if (allReady) {
                if (this.mResultReceiver != null) {
                    Bundle state = captureSharedElementState();
                    moveSharedElementsToOverlay();
                    this.mResultReceiver.send(107, state);
                }
            } else if (decorView != null) {
                decorView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (EnterTransitionCoordinator.this.mResultReceiver != null) {
                            Bundle state = EnterTransitionCoordinator.this.captureSharedElementState();
                            EnterTransitionCoordinator.this.moveSharedElementsToOverlay();
                            EnterTransitionCoordinator.this.mResultReceiver.send(107, state);
                        }
                        return true;
                    }
                });
            }
            if (allowOverlappingTransitions()) {
                startEnterTransitionOnly();
            }
        }
    }

    private static SharedElementCallback getListener(Activity activity, boolean isReturning) {
        return isReturning ? activity.mExitTransitionListener : activity.mEnterTransitionListener;
    }

    protected void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case 103:
                if (!this.mIsCanceled) {
                    this.mSharedElementsBundle = resultData;
                    onTakeSharedElements();
                    return;
                }
                return;
            case 104:
                if (!this.mIsCanceled) {
                    this.mIsExitTransitionComplete = true;
                    if (this.mSharedElementTransitionStarted) {
                        onRemoteExitTransitionComplete();
                        return;
                    }
                    return;
                }
                return;
            case 106:
                cancel();
                return;
            default:
                return;
        }
    }

    private void cancel() {
        if (!this.mIsCanceled) {
            this.mIsCanceled = true;
            if (getViewsTransition() == null || this.mIsViewsTransitionStarted) {
                showViews(this.mSharedElements, true);
            } else if (this.mTransitioningViews != null) {
                this.mTransitioningViews.addAll(this.mSharedElements);
            }
            this.mSharedElementNames.clear();
            this.mSharedElements.clear();
            this.mAllSharedElementNames.clear();
            startSharedElementTransition(null);
            onRemoteExitTransitionComplete();
        }
    }

    public boolean isReturning() {
        return this.mIsReturning;
    }

    protected void prepareEnter() {
        ViewGroup decorView = getDecor();
        if (this.mActivity != null && decorView != null) {
            this.mActivity.overridePendingTransition(0, 0);
            if (this.mIsReturning) {
                this.mActivity = null;
                return;
            }
            this.mWasOpaque = this.mActivity.convertToTranslucent(null, null);
            Drawable background = decorView.getBackground();
            if (background != null) {
                getWindow().setBackgroundDrawable(null);
                background = background.mutate();
                background.setAlpha(0);
                getWindow().setBackgroundDrawable(background);
            }
        }
    }

    protected Transition getViewsTransition() {
        Window window = getWindow();
        if (window == null) {
            return null;
        }
        if (this.mIsReturning) {
            return window.getReenterTransition();
        }
        return window.getEnterTransition();
    }

    protected Transition getSharedElementTransition() {
        Window window = getWindow();
        if (window == null) {
            return null;
        }
        if (this.mIsReturning) {
            return window.getSharedElementReenterTransition();
        }
        return window.getSharedElementEnterTransition();
    }

    private void startSharedElementTransition(Bundle sharedElementState) {
        boolean startEnterTransition = true;
        ViewGroup decorView = getDecor();
        if (decorView != null) {
            ArrayList<String> rejectedNames = new ArrayList(this.mAllSharedElementNames);
            rejectedNames.removeAll(this.mSharedElementNames);
            ArrayList<View> rejectedSnapshots = createSnapshots(sharedElementState, rejectedNames);
            if (this.mListener != null) {
                this.mListener.onRejectSharedElements(rejectedSnapshots);
            }
            removeNullViews(rejectedSnapshots);
            startRejectedAnimations(rejectedSnapshots);
            ArrayList<View> sharedElementSnapshots = createSnapshots(sharedElementState, this.mSharedElementNames);
            showViews(this.mSharedElements, true);
            scheduleSetSharedElementEnd(sharedElementSnapshots);
            ArrayList<SharedElementOriginalState> originalImageViewState = setSharedElementState(sharedElementState, sharedElementSnapshots);
            requestLayoutForSharedElements();
            if (!allowOverlappingTransitions() || this.mIsReturning) {
                startEnterTransition = false;
            }
            setGhostVisibility(4);
            scheduleGhostVisibilityChange(4);
            pauseInput();
            Transition transition = beginTransition(decorView, startEnterTransition, true);
            scheduleGhostVisibilityChange(0);
            setGhostVisibility(0);
            if (startEnterTransition) {
                startEnterTransition(transition);
            }
            ActivityTransitionCoordinator.setOriginalSharedElementState(this.mSharedElements, originalImageViewState);
            if (this.mResultReceiver != null) {
                decorView.postOnAnimation(new Runnable() {
                    int mAnimations;

                    public void run() {
                        int i = this.mAnimations;
                        this.mAnimations = i + 1;
                        if (i < 2) {
                            View decorView = EnterTransitionCoordinator.this.getDecor();
                            if (decorView != null) {
                                decorView.postOnAnimation(this);
                            }
                        } else if (EnterTransitionCoordinator.this.mResultReceiver != null) {
                            EnterTransitionCoordinator.this.mResultReceiver.send(101, null);
                            EnterTransitionCoordinator.this.mResultReceiver = null;
                        }
                    }
                });
            }
        }
    }

    private static void removeNullViews(ArrayList<View> views) {
        if (views != null) {
            for (int i = views.size() - 1; i >= 0; i--) {
                if (views.get(i) == null) {
                    views.remove(i);
                }
            }
        }
    }

    private void onTakeSharedElements() {
        if (this.mIsReadyForTransition && this.mSharedElementsBundle != null) {
            final Bundle sharedElementState = this.mSharedElementsBundle;
            this.mSharedElementsBundle = null;
            OnSharedElementsReadyListener listener = new OnSharedElementsReadyListener() {
                public void onSharedElementsReady() {
                    final View decorView = EnterTransitionCoordinator.this.getDecor();
                    if (decorView != null) {
                        decorView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                            public boolean onPreDraw() {
                                decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                                EnterTransitionCoordinator.this.startTransition(new Runnable() {
                                    public void run() {
                                        EnterTransitionCoordinator.this.startSharedElementTransition(sharedElementState);
                                    }
                                });
                                return false;
                            }
                        });
                        decorView.invalidate();
                    }
                }
            };
            if (this.mListener == null) {
                listener.onSharedElementsReady();
            } else {
                this.mListener.onSharedElementsArrived(this.mSharedElementNames, this.mSharedElements, listener);
            }
        }
    }

    private void requestLayoutForSharedElements() {
        int numSharedElements = this.mSharedElements.size();
        for (int i = 0; i < numSharedElements; i++) {
            ((View) this.mSharedElements.get(i)).requestLayout();
        }
    }

    private Transition beginTransition(ViewGroup decorView, boolean startEnterTransition, boolean startSharedElementTransition) {
        Transition sharedElementTransition = null;
        if (startSharedElementTransition) {
            if (!this.mSharedElementNames.isEmpty()) {
                sharedElementTransition = configureTransition(getSharedElementTransition(), false);
            }
            if (sharedElementTransition == null) {
                sharedElementTransitionStarted();
                sharedElementTransitionComplete();
            } else {
                sharedElementTransition.addListener(new TransitionListenerAdapter() {
                    public void onTransitionStart(Transition transition) {
                        EnterTransitionCoordinator.this.sharedElementTransitionStarted();
                    }

                    public void onTransitionEnd(Transition transition) {
                        transition.removeListener(this);
                        EnterTransitionCoordinator.this.sharedElementTransitionComplete();
                    }
                });
            }
        }
        Transition viewsTransition = null;
        if (startEnterTransition) {
            this.mIsViewsTransitionStarted = true;
            if (!(this.mTransitioningViews == null || this.mTransitioningViews.isEmpty())) {
                viewsTransition = configureTransition(getViewsTransition(), true);
                if (!(viewsTransition == null || this.mIsReturning)) {
                    stripOffscreenViews();
                }
            }
            if (viewsTransition == null) {
                viewsTransitionComplete();
            } else {
                viewsTransition.forceVisibility(4, true);
                final ArrayList<View> transitioningViews = this.mTransitioningViews;
                viewsTransition.addListener(new ContinueTransitionListener() {
                    public void onTransitionStart(Transition transition) {
                        EnterTransitionCoordinator.this.mEnterViewsTransition = transition;
                        if (transitioningViews != null) {
                            EnterTransitionCoordinator.this.showViews(transitioningViews, false);
                        }
                        super.onTransitionStart(transition);
                    }

                    public void onTransitionEnd(Transition transition) {
                        EnterTransitionCoordinator.this.mEnterViewsTransition = null;
                        transition.removeListener(this);
                        EnterTransitionCoordinator.this.viewsTransitionComplete();
                        super.onTransitionEnd(transition);
                    }
                });
            }
        }
        Transition transition = ActivityTransitionCoordinator.mergeTransitions(sharedElementTransition, viewsTransition);
        if (transition != null) {
            transition.addListener(new ContinueTransitionListener());
            TransitionManager.beginDelayedTransition(decorView, transition);
            if (startSharedElementTransition && !this.mSharedElementNames.isEmpty()) {
                ((View) this.mSharedElements.get(0)).invalidate();
            } else if (!(!startEnterTransition || this.mTransitioningViews == null || this.mTransitioningViews.isEmpty())) {
                ((View) this.mTransitioningViews.get(0)).invalidate();
            }
        } else {
            transitionStarted();
        }
        return transition;
    }

    protected void onTransitionsComplete() {
        moveSharedElementsFromOverlay();
    }

    private void sharedElementTransitionStarted() {
        this.mSharedElementTransitionStarted = true;
        if (this.mIsExitTransitionComplete) {
            send(104, null);
        }
    }

    private void startEnterTransition(Transition transition) {
        ViewGroup decorView = getDecor();
        if (!this.mIsReturning && decorView != null) {
            Drawable background = decorView.getBackground();
            if (background != null) {
                Object background2 = background.mutate();
                getWindow().setBackgroundDrawable(background2);
                this.mBackgroundAnimator = ObjectAnimator.ofInt(background2, "alpha", 255);
                this.mBackgroundAnimator.setDuration(getFadeDuration());
                this.mBackgroundAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        EnterTransitionCoordinator.this.makeOpaque();
                    }
                });
                this.mBackgroundAnimator.start();
            } else if (transition != null) {
                transition.addListener(new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition transition) {
                        transition.removeListener(this);
                        EnterTransitionCoordinator.this.makeOpaque();
                    }
                });
            } else {
                makeOpaque();
            }
        }
    }

    public void stop() {
        if (this.mBackgroundAnimator != null) {
            this.mBackgroundAnimator.end();
            this.mBackgroundAnimator = null;
        } else if (this.mWasOpaque) {
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                Drawable drawable = decorView.getBackground();
                if (drawable != null) {
                    drawable.setAlpha(1);
                }
            }
        }
        makeOpaque();
        this.mIsCanceled = true;
        this.mResultReceiver = null;
        this.mActivity = null;
        moveSharedElementsFromOverlay();
        if (this.mTransitioningViews != null) {
            showViews(this.mTransitioningViews, true);
        }
        showViews(this.mSharedElements, true);
        clearState();
    }

    public boolean cancelEnter() {
        setGhostVisibility(4);
        this.mHasStopped = true;
        this.mIsCanceled = true;
        this.mResultReceiver = null;
        if (this.mBackgroundAnimator != null) {
            this.mBackgroundAnimator.cancel();
            this.mBackgroundAnimator = null;
        }
        this.mActivity = null;
        clearState();
        return super.cancelPendingTransitions();
    }

    private void makeOpaque() {
        if (!this.mHasStopped && this.mActivity != null) {
            if (this.mWasOpaque) {
                this.mActivity.convertFromTranslucent();
            }
            this.mActivity = null;
        }
    }

    private boolean allowOverlappingTransitions() {
        return this.mIsReturning ? getWindow().getAllowReturnTransitionOverlap() : getWindow().getAllowEnterTransitionOverlap();
    }

    private void startRejectedAnimations(final ArrayList<View> rejectedSnapshots) {
        if (rejectedSnapshots != null && !rejectedSnapshots.isEmpty()) {
            final ViewGroup decorView = getDecor();
            if (decorView != null) {
                ViewGroupOverlay overlay = decorView.getOverlay();
                ObjectAnimator animator = null;
                int numRejected = rejectedSnapshots.size();
                for (int i = 0; i < numRejected; i++) {
                    Object snapshot = (View) rejectedSnapshots.get(i);
                    overlay.add(snapshot);
                    animator = ObjectAnimator.ofFloat(snapshot, View.ALPHA, 1.0f, 0.0f);
                    animator.start();
                }
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        ViewGroupOverlay overlay = decorView.getOverlay();
                        int numRejected = rejectedSnapshots.size();
                        for (int i = 0; i < numRejected; i++) {
                            overlay.remove((View) rejectedSnapshots.get(i));
                        }
                    }
                });
            }
        }
    }

    protected void onRemoteExitTransitionComplete() {
        if (!allowOverlappingTransitions()) {
            startEnterTransitionOnly();
        }
    }

    private void startEnterTransitionOnly() {
        startTransition(new Runnable() {
            public void run() {
                ViewGroup decorView = EnterTransitionCoordinator.this.getDecor();
                if (decorView != null) {
                    EnterTransitionCoordinator.this.startEnterTransition(EnterTransitionCoordinator.this.beginTransition(decorView, true, false));
                }
            }
        });
    }
}
