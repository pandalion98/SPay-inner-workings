package android.animation;

import android.animation.Animator.AnimatorListener;
import android.net.ProxyInfo;
import android.os.Looper;
import android.os.Trace;
import android.util.AndroidRuntimeException;
import android.view.Choreographer;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ValueAnimator extends Animator {
    private static final boolean DEBUG = false;
    public static final int INFINITE = -1;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    static final int RUNNING = 1;
    static final int SEEKED = 2;
    static final int STOPPED = 0;
    private static final String TAG = "ValueAnimator";
    protected static ThreadLocal<AnimationHandler> sAnimationHandler = new ThreadLocal();
    private static final TimeInterpolator sDefaultInterpolator = new AccelerateDecelerateInterpolator();
    private static float sDurationScale = 1.0f;
    private float mCurrentFraction = 0.0f;
    private int mCurrentIteration = 0;
    private long mDelayStartTime;
    private long mDuration = ((long) (300.0f * sDurationScale));
    boolean mInitialized = false;
    private TimeInterpolator mInterpolator = sDefaultInterpolator;
    private long mPauseTime;
    private boolean mPlayingBackwards = false;
    int mPlayingState = 0;
    private int mRepeatCount = 0;
    private int mRepeatMode = 1;
    private boolean mResumed = false;
    private boolean mReversing;
    private boolean mRunning = false;
    float mSeekFraction = -1.0f;
    private long mStartDelay = 0;
    private boolean mStartListenersCalled = false;
    long mStartTime;
    boolean mStartTimeCommitted;
    private boolean mStarted = false;
    private boolean mStartedDelay = false;
    private long mUnscaledDuration = 300;
    private long mUnscaledStartDelay = 0;
    ArrayList<AnimatorUpdateListener> mUpdateListeners = null;
    PropertyValuesHolder[] mValues;
    HashMap<String, PropertyValuesHolder> mValuesMap;

    protected static class AnimationHandler {
        final Runnable mAnimate;
        private boolean mAnimationScheduled;
        protected final ArrayList<ValueAnimator> mAnimations;
        private final Choreographer mChoreographer;
        final Runnable mCommit;
        protected final ArrayList<ValueAnimator> mDelayedAnims;
        private final ArrayList<ValueAnimator> mEndingAnims;
        private long mLastFrameTime;
        protected final ArrayList<ValueAnimator> mPendingAnimations;
        private final ArrayList<ValueAnimator> mReadyAnims;
        private final ArrayList<ValueAnimator> mTmpAnimations;

        private AnimationHandler() {
            this.mAnimations = new ArrayList();
            this.mTmpAnimations = new ArrayList();
            this.mPendingAnimations = new ArrayList();
            this.mDelayedAnims = new ArrayList();
            this.mEndingAnims = new ArrayList();
            this.mReadyAnims = new ArrayList();
            this.mAnimate = new Runnable() {
                public void run() {
                    AnimationHandler.this.mAnimationScheduled = false;
                    AnimationHandler.this.doAnimationFrame(AnimationHandler.this.mChoreographer.getFrameTime());
                }
            };
            this.mCommit = new Runnable() {
                public void run() {
                    AnimationHandler.this.commitAnimationFrame(AnimationHandler.this.mChoreographer.getFrameTime());
                }
            };
            this.mChoreographer = Choreographer.getInstance();
        }

        public void start() {
            scheduleAnimation();
        }

        void doAnimationFrame(long frameTime) {
            int i;
            this.mLastFrameTime = frameTime;
            while (this.mPendingAnimations.size() > 0) {
                ArrayList<ValueAnimator> pendingCopy = (ArrayList) this.mPendingAnimations.clone();
                this.mPendingAnimations.clear();
                int count = pendingCopy.size();
                for (i = 0; i < count; i++) {
                    ValueAnimator anim = (ValueAnimator) pendingCopy.get(i);
                    if (anim.mStartDelay == 0) {
                        anim.startAnimation(this);
                    } else {
                        this.mDelayedAnims.add(anim);
                    }
                }
            }
            int numDelayedAnims = this.mDelayedAnims.size();
            for (i = 0; i < numDelayedAnims; i++) {
                anim = (ValueAnimator) this.mDelayedAnims.get(i);
                if (anim.delayedAnimationFrame(frameTime)) {
                    this.mReadyAnims.add(anim);
                }
            }
            int numReadyAnims = this.mReadyAnims.size();
            if (numReadyAnims > 0) {
                for (i = 0; i < numReadyAnims; i++) {
                    anim = (ValueAnimator) this.mReadyAnims.get(i);
                    anim.startAnimation(this);
                    anim.mRunning = true;
                    this.mDelayedAnims.remove(anim);
                }
                this.mReadyAnims.clear();
            }
            int numAnims = this.mAnimations.size();
            for (i = 0; i < numAnims; i++) {
                this.mTmpAnimations.add(this.mAnimations.get(i));
            }
            for (i = 0; i < numAnims; i++) {
                anim = (ValueAnimator) this.mTmpAnimations.get(i);
                if (this.mAnimations.contains(anim) && anim.doAnimationFrame(frameTime)) {
                    this.mEndingAnims.add(anim);
                }
            }
            this.mTmpAnimations.clear();
            if (this.mEndingAnims.size() > 0) {
                for (i = 0; i < this.mEndingAnims.size(); i++) {
                    ((ValueAnimator) this.mEndingAnims.get(i)).endAnimation(this);
                }
                this.mEndingAnims.clear();
            }
            this.mChoreographer.postCallback(3, this.mCommit, null);
            if (!this.mAnimations.isEmpty() || !this.mDelayedAnims.isEmpty()) {
                scheduleAnimation();
            }
        }

        void commitAnimationFrame(long frameTime) {
            long adjustment = frameTime - this.mLastFrameTime;
            int numAnims = this.mAnimations.size();
            for (int i = 0; i < numAnims; i++) {
                ((ValueAnimator) this.mAnimations.get(i)).commitAnimationFrame(adjustment);
            }
        }

        private void scheduleAnimation() {
            if (!this.mAnimationScheduled) {
                this.mChoreographer.postCallback(1, this.mAnimate, null);
                this.mAnimationScheduled = true;
            }
        }
    }

    public interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimator valueAnimator);
    }

    public static void setDurationScale(float durationScale) {
        sDurationScale = durationScale;
    }

    public static float getDurationScale() {
        return sDurationScale;
    }

    public static ValueAnimator ofInt(int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(values);
        return anim;
    }

    public static ValueAnimator ofArgb(int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(values);
        anim.setEvaluator(ArgbEvaluator.getInstance());
        return anim;
    }

    public static ValueAnimator ofFloat(float... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setFloatValues(values);
        return anim;
    }

    public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setValues(values);
        return anim;
    }

    public static ValueAnimator ofObject(TypeEvaluator evaluator, Object... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    public void setIntValues(int... values) {
        if (values != null && values.length != 0) {
            if (this.mValues == null || this.mValues.length == 0) {
                setValues(PropertyValuesHolder.ofInt(ProxyInfo.LOCAL_EXCL_LIST, values));
            } else {
                this.mValues[0].setIntValues(values);
            }
            this.mInitialized = false;
        }
    }

    public void setFloatValues(float... values) {
        if (values != null && values.length != 0) {
            if (this.mValues == null || this.mValues.length == 0) {
                setValues(PropertyValuesHolder.ofFloat(ProxyInfo.LOCAL_EXCL_LIST, values));
            } else {
                this.mValues[0].setFloatValues(values);
            }
            this.mInitialized = false;
        }
    }

    public void setObjectValues(Object... values) {
        if (values != null && values.length != 0) {
            if (this.mValues == null || this.mValues.length == 0) {
                setValues(PropertyValuesHolder.ofObject(ProxyInfo.LOCAL_EXCL_LIST, null, values));
            } else {
                this.mValues[0].setObjectValues(values);
            }
            this.mInitialized = false;
        }
    }

    public void setValues(PropertyValuesHolder... values) {
        this.mValues = values;
        this.mValuesMap = new HashMap(numValues);
        for (PropertyValuesHolder valuesHolder : values) {
            this.mValuesMap.put(valuesHolder.getPropertyName(), valuesHolder);
        }
        this.mInitialized = false;
    }

    public PropertyValuesHolder[] getValues() {
        return this.mValues;
    }

    void initAnimation() {
        if (!this.mInitialized) {
            for (PropertyValuesHolder init : this.mValues) {
                init.init();
            }
            this.mInitialized = true;
        }
    }

    public ValueAnimator setDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Animators cannot have negative duration: " + duration);
        }
        this.mUnscaledDuration = duration;
        updateScaledDuration();
        return this;
    }

    private void updateScaledDuration() {
        this.mDuration = (long) (((float) this.mUnscaledDuration) * sDurationScale);
    }

    public long getDuration() {
        return this.mUnscaledDuration;
    }

    public void setCurrentPlayTime(long playTime) {
        setCurrentFraction(this.mUnscaledDuration > 0 ? ((float) playTime) / ((float) this.mUnscaledDuration) : 1.0f);
    }

    public void setCurrentFraction(float fraction) {
        initAnimation();
        if (fraction < 0.0f) {
            fraction = 0.0f;
        }
        int iteration = (int) fraction;
        if (fraction == 1.0f) {
            iteration--;
        } else if (fraction <= 1.0f) {
            this.mPlayingBackwards = this.mReversing;
        } else if (iteration < this.mRepeatCount + 1 || this.mRepeatCount == -1) {
            if (this.mRepeatMode == 2) {
                this.mPlayingBackwards = iteration % 2 != 0;
            }
            fraction %= 1.0f;
        } else {
            fraction = 1.0f;
            iteration--;
        }
        this.mCurrentIteration = iteration;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis() - ((long) (((float) this.mDuration) * fraction));
        this.mStartTimeCommitted = true;
        if (this.mPlayingState != 1) {
            this.mSeekFraction = fraction;
            this.mPlayingState = 2;
        }
        if (this.mPlayingBackwards) {
            fraction = 1.0f - fraction;
        }
        animateValue(fraction);
    }

    public long getCurrentPlayTime() {
        if (!this.mInitialized || this.mPlayingState == 0) {
            return 0;
        }
        return AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
    }

    public long getStartDelay() {
        return this.mUnscaledStartDelay;
    }

    public void setStartDelay(long startDelay) {
        this.mStartDelay = (long) (((float) startDelay) * sDurationScale);
        this.mUnscaledStartDelay = startDelay;
    }

    public static long getFrameDelay() {
        return Choreographer.getFrameDelay();
    }

    public static void setFrameDelay(long frameDelay) {
        Choreographer.setFrameDelay(frameDelay);
    }

    public Object getAnimatedValue() {
        if (this.mValues == null || this.mValues.length <= 0) {
            return null;
        }
        return this.mValues[0].getAnimatedValue();
    }

    public Object getAnimatedValue(String propertyName) {
        PropertyValuesHolder valuesHolder = (PropertyValuesHolder) this.mValuesMap.get(propertyName);
        if (valuesHolder != null) {
            return valuesHolder.getAnimatedValue();
        }
        return null;
    }

    public void setRepeatCount(int value) {
        this.mRepeatCount = value;
    }

    public int getRepeatCount() {
        return this.mRepeatCount;
    }

    public void setRepeatMode(int value) {
        this.mRepeatMode = value;
    }

    public int getRepeatMode() {
        return this.mRepeatMode;
    }

    public void addUpdateListener(AnimatorUpdateListener listener) {
        if (this.mUpdateListeners == null) {
            this.mUpdateListeners = new ArrayList();
        }
        this.mUpdateListeners.add(listener);
    }

    public void removeAllUpdateListeners() {
        if (this.mUpdateListeners != null) {
            this.mUpdateListeners.clear();
            this.mUpdateListeners = null;
        }
    }

    public void removeUpdateListener(AnimatorUpdateListener listener) {
        if (this.mUpdateListeners != null) {
            this.mUpdateListeners.remove(listener);
            if (this.mUpdateListeners.size() == 0) {
                this.mUpdateListeners = null;
            }
        }
    }

    public void setInterpolator(TimeInterpolator value) {
        if (value != null) {
            this.mInterpolator = value;
        } else {
            this.mInterpolator = new LinearInterpolator();
        }
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setEvaluator(TypeEvaluator value) {
        if (value != null && this.mValues != null && this.mValues.length > 0) {
            this.mValues[0].setEvaluator(value);
        }
    }

    private void notifyStartListeners() {
        if (!(this.mListeners == null || this.mStartListenersCalled)) {
            ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
            int numListeners = tmpListeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((AnimatorListener) tmpListeners.get(i)).onAnimationStart(this);
            }
        }
        this.mStartListenersCalled = true;
    }

    private void start(boolean playBackwards) {
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        }
        this.mReversing = playBackwards;
        this.mPlayingBackwards = playBackwards;
        if (playBackwards && this.mSeekFraction != -1.0f) {
            if (this.mSeekFraction == 0.0f && this.mCurrentIteration == 0) {
                this.mSeekFraction = 0.0f;
            } else if (this.mRepeatCount == -1) {
                this.mSeekFraction = 1.0f - (this.mSeekFraction % 1.0f);
            } else {
                this.mSeekFraction = ((float) (this.mRepeatCount + 1)) - (((float) this.mCurrentIteration) + this.mSeekFraction);
            }
            this.mCurrentIteration = (int) this.mSeekFraction;
            this.mSeekFraction %= 1.0f;
        }
        if (this.mCurrentIteration > 0 && this.mRepeatMode == 2 && (this.mCurrentIteration < this.mRepeatCount + 1 || this.mRepeatCount == -1)) {
            if (playBackwards) {
                boolean z;
                if (this.mCurrentIteration % 2 == 0) {
                    z = true;
                } else {
                    z = false;
                }
                this.mPlayingBackwards = z;
            } else {
                this.mPlayingBackwards = this.mCurrentIteration % 2 != 0;
            }
        }
        int prevPlayingState = this.mPlayingState;
        this.mPlayingState = 0;
        this.mStarted = true;
        this.mStartedDelay = false;
        this.mPaused = false;
        updateScaledDuration();
        AnimationHandler animationHandler = getOrCreateAnimationHandler();
        animationHandler.mPendingAnimations.add(this);
        if (this.mStartDelay == 0) {
            if (prevPlayingState != 2) {
                setCurrentPlayTime(0);
            }
            this.mPlayingState = 0;
            this.mRunning = true;
            notifyStartListeners();
        }
        animationHandler.start();
    }

    public void start() {
        start(false);
    }

    public void cancel() {
        AnimationHandler handler = getOrCreateAnimationHandler();
        if (this.mPlayingState != 0 || handler.mPendingAnimations.contains(this) || handler.mDelayedAnims.contains(this)) {
            if ((this.mStarted || this.mRunning) && this.mListeners != null) {
                if (!this.mRunning) {
                    notifyStartListeners();
                }
                Iterator i$ = ((ArrayList) this.mListeners.clone()).iterator();
                while (i$.hasNext()) {
                    ((AnimatorListener) i$.next()).onAnimationCancel(this);
                }
            }
            endAnimation(handler);
        }
    }

    public void end() {
        AnimationHandler handler = getOrCreateAnimationHandler();
        if (!handler.mAnimations.contains(this) && !handler.mPendingAnimations.contains(this)) {
            this.mStartedDelay = false;
            startAnimation(handler);
            this.mStarted = true;
        } else if (!this.mInitialized) {
            initAnimation();
        }
        animateValue(this.mPlayingBackwards ? 0.0f : 1.0f);
        endAnimation(handler);
    }

    public void resume() {
        if (this.mPaused) {
            this.mResumed = true;
        }
        super.resume();
    }

    public void pause() {
        boolean previouslyPaused = this.mPaused;
        super.pause();
        if (!previouslyPaused && this.mPaused) {
            this.mPauseTime = -1;
            this.mResumed = false;
        }
    }

    public boolean isRunning() {
        return this.mPlayingState == 1 || this.mRunning;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public void reverse() {
        boolean z = true;
        this.mPlayingBackwards = !this.mPlayingBackwards;
        if (this.mPlayingState == 1) {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStartTime = currentTime - (this.mDuration - (currentTime - this.mStartTime));
            this.mStartTimeCommitted = true;
            if (this.mReversing) {
                z = false;
            }
            this.mReversing = z;
        } else if (this.mStarted) {
            end();
        } else {
            start(true);
        }
    }

    public boolean canReverse() {
        return true;
    }

    protected void endAnimation(AnimationHandler handler) {
        handler.mAnimations.remove(this);
        handler.mPendingAnimations.remove(this);
        handler.mDelayedAnims.remove(this);
        this.mPlayingState = 0;
        this.mPaused = false;
        if ((this.mStarted || this.mRunning) && this.mListeners != null) {
            if (!this.mRunning) {
                notifyStartListeners();
            }
            ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
            int numListeners = tmpListeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((AnimatorListener) tmpListeners.get(i)).onAnimationEnd(this);
            }
        }
        this.mRunning = false;
        this.mStarted = false;
        this.mStartListenersCalled = false;
        this.mPlayingBackwards = false;
        this.mReversing = false;
        this.mCurrentIteration = 0;
        if (Trace.isTagEnabled(8)) {
            Trace.asyncTraceEnd(8, getNameForTrace(), System.identityHashCode(this));
        }
    }

    private void startAnimation(AnimationHandler handler) {
        if (Trace.isTagEnabled(8)) {
            Trace.asyncTraceBegin(8, getNameForTrace(), System.identityHashCode(this));
        }
        initAnimation();
        handler.mAnimations.add(this);
        if (this.mStartDelay > 0 && this.mListeners != null) {
            notifyStartListeners();
        }
    }

    String getNameForTrace() {
        return "animator";
    }

    private boolean delayedAnimationFrame(long currentTime) {
        if (!this.mStartedDelay) {
            this.mStartedDelay = true;
            this.mDelayStartTime = currentTime;
        }
        if (!this.mPaused) {
            if (this.mResumed) {
                this.mResumed = false;
                if (this.mPauseTime > 0) {
                    this.mDelayStartTime += currentTime - this.mPauseTime;
                }
            }
            if (currentTime - this.mDelayStartTime <= this.mStartDelay) {
                return false;
            }
            this.mStartTime = this.mDelayStartTime + this.mStartDelay;
            this.mStartTimeCommitted = true;
            this.mPlayingState = 1;
            return true;
        } else if (this.mPauseTime >= 0) {
            return false;
        } else {
            this.mPauseTime = currentTime;
            return false;
        }
    }

    void commitAnimationFrame(long adjustment) {
        if (!this.mStartTimeCommitted) {
            this.mStartTimeCommitted = true;
            if (this.mPlayingState == 1 && adjustment > 0) {
                this.mStartTime += adjustment;
            }
        }
    }

    boolean animationFrame(long currentTime) {
        boolean done = false;
        switch (this.mPlayingState) {
            case 1:
            case 2:
                float fraction = this.mDuration > 0 ? ((float) (currentTime - this.mStartTime)) / ((float) this.mDuration) : 1.0f;
                if (this.mDuration == 0 && this.mRepeatCount != -1) {
                    this.mCurrentIteration = this.mRepeatCount;
                    if (!this.mReversing) {
                        this.mPlayingBackwards = false;
                    }
                }
                if (fraction >= 1.0f) {
                    if (this.mCurrentIteration < this.mRepeatCount || this.mRepeatCount == -1) {
                        if (this.mListeners != null) {
                            int numListeners = this.mListeners.size();
                            for (int i = 0; i < numListeners; i++) {
                                ((AnimatorListener) this.mListeners.get(i)).onAnimationRepeat(this);
                            }
                        }
                        if (this.mRepeatMode == 2) {
                            this.mPlayingBackwards = !this.mPlayingBackwards;
                        }
                        this.mCurrentIteration += (int) fraction;
                        fraction %= 1.0f;
                        this.mStartTime += this.mDuration;
                    } else {
                        done = true;
                        fraction = Math.min(fraction, 1.0f);
                    }
                }
                if (this.mPlayingBackwards) {
                    fraction = 1.0f - fraction;
                }
                animateValue(fraction);
                break;
        }
        return done;
    }

    final boolean doAnimationFrame(long frameTime) {
        if (this.mPlayingState == 0) {
            this.mPlayingState = 1;
            if (this.mSeekFraction < 0.0f) {
                this.mStartTime = frameTime;
            } else {
                this.mStartTime = frameTime - ((long) (((float) this.mDuration) * this.mSeekFraction));
                this.mSeekFraction = -1.0f;
            }
            this.mStartTimeCommitted = false;
        }
        if (!this.mPaused) {
            if (this.mResumed) {
                this.mResumed = false;
                if (this.mPauseTime > 0) {
                    this.mStartTime += frameTime - this.mPauseTime;
                    this.mStartTimeCommitted = false;
                }
            }
            return animationFrame(Math.max(frameTime, this.mStartTime));
        } else if (this.mPauseTime >= 0) {
            return false;
        } else {
            this.mPauseTime = frameTime;
            return false;
        }
    }

    public float getAnimatedFraction() {
        return this.mCurrentFraction;
    }

    void animateValue(float fraction) {
        int i;
        fraction = this.mInterpolator.getInterpolation(fraction);
        this.mCurrentFraction = fraction;
        for (PropertyValuesHolder calculateValue : this.mValues) {
            calculateValue.calculateValue(fraction);
        }
        if (this.mUpdateListeners != null) {
            int numListeners = this.mUpdateListeners.size();
            for (i = 0; i < numListeners; i++) {
                ((AnimatorUpdateListener) this.mUpdateListeners.get(i)).onAnimationUpdate(this);
            }
        }
    }

    public ValueAnimator clone() {
        ValueAnimator anim = (ValueAnimator) super.clone();
        if (this.mUpdateListeners != null) {
            anim.mUpdateListeners = new ArrayList(this.mUpdateListeners);
        }
        anim.mSeekFraction = -1.0f;
        anim.mPlayingBackwards = false;
        anim.mReversing = false;
        anim.mCurrentIteration = 0;
        anim.mInitialized = false;
        anim.mPlayingState = 0;
        anim.mStartedDelay = false;
        anim.mStarted = false;
        anim.mRunning = false;
        anim.mPaused = false;
        anim.mResumed = false;
        anim.mStartListenersCalled = false;
        anim.mStartTime = 0;
        anim.mStartTimeCommitted = false;
        anim.mPauseTime = 0;
        anim.mCurrentFraction = 0.0f;
        anim.mDelayStartTime = 0;
        PropertyValuesHolder[] oldValues = this.mValues;
        if (oldValues != null) {
            int numValues = oldValues.length;
            anim.mValues = new PropertyValuesHolder[numValues];
            anim.mValuesMap = new HashMap(numValues);
            for (int i = 0; i < numValues; i++) {
                PropertyValuesHolder newValuesHolder = oldValues[i].clone();
                anim.mValues[i] = newValuesHolder;
                anim.mValuesMap.put(newValuesHolder.getPropertyName(), newValuesHolder);
            }
        }
        return anim;
    }

    public static int getCurrentAnimationsCount() {
        AnimationHandler handler = (AnimationHandler) sAnimationHandler.get();
        return handler != null ? handler.mAnimations.size() : 0;
    }

    public static void clearAllAnimations() {
        AnimationHandler handler = (AnimationHandler) sAnimationHandler.get();
        if (handler != null) {
            handler.mAnimations.clear();
            handler.mPendingAnimations.clear();
            handler.mDelayedAnims.clear();
        }
    }

    private static AnimationHandler getOrCreateAnimationHandler() {
        AnimationHandler handler = (AnimationHandler) sAnimationHandler.get();
        if (handler != null) {
            return handler;
        }
        handler = new AnimationHandler();
        sAnimationHandler.set(handler);
        return handler;
    }

    public String toString() {
        String returnVal = "ValueAnimator@" + Integer.toHexString(hashCode());
        if (this.mValues != null) {
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                returnVal = returnVal + "\n    " + propertyValuesHolder.toString();
            }
        }
        return returnVal;
    }

    public void setAllowRunningAsynchronously(boolean mayRunAsync) {
    }
}
