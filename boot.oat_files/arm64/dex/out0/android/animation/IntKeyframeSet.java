package android.animation;

import android.animation.Keyframes.IntKeyframes;
import java.util.List;

class IntKeyframeSet extends KeyframeSet implements IntKeyframes {
    private int deltaValue;
    private boolean firstTime = true;
    private int firstValue;
    private int lastValue;

    public IntKeyframeSet(IntKeyframe... keyframes) {
        super(keyframes);
    }

    public Object getValue(float fraction) {
        return Integer.valueOf(getIntValue(fraction));
    }

    public IntKeyframeSet clone() {
        List<Keyframe> keyframes = this.mKeyframes;
        int numKeyframes = this.mKeyframes.size();
        IntKeyframe[] newKeyframes = new IntKeyframe[numKeyframes];
        for (int i = 0; i < numKeyframes; i++) {
            newKeyframes[i] = (IntKeyframe) ((Keyframe) keyframes.get(i)).clone();
        }
        return new IntKeyframeSet(newKeyframes);
    }

    public void invalidateCache() {
        this.firstTime = true;
    }

    public int getIntValue(float fraction) {
        if (this.mNumKeyframes == 2) {
            if (this.firstTime) {
                this.firstTime = false;
                this.firstValue = ((IntKeyframe) this.mKeyframes.get(0)).getIntValue();
                this.lastValue = ((IntKeyframe) this.mKeyframes.get(1)).getIntValue();
                this.deltaValue = this.lastValue - this.firstValue;
            }
            if (this.mInterpolator != null) {
                fraction = this.mInterpolator.getInterpolation(fraction);
            }
            if (this.mEvaluator == null) {
                return this.firstValue + ((int) (((float) this.deltaValue) * fraction));
            }
            return ((Number) this.mEvaluator.evaluate(fraction, Integer.valueOf(this.firstValue), Integer.valueOf(this.lastValue))).intValue();
        } else if (fraction <= 0.0f) {
            prevKeyframe = (IntKeyframe) this.mKeyframes.get(0);
            nextKeyframe = (IntKeyframe) this.mKeyframes.get(1);
            prevValue = prevKeyframe.getIntValue();
            nextValue = nextKeyframe.getIntValue();
            prevFraction = prevKeyframe.getFraction();
            nextFraction = nextKeyframe.getFraction();
            interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            intervalFraction = (fraction - prevFraction) / (nextFraction - prevFraction);
            return this.mEvaluator == null ? ((int) (((float) (nextValue - prevValue)) * intervalFraction)) + prevValue : ((Number) this.mEvaluator.evaluate(intervalFraction, Integer.valueOf(prevValue), Integer.valueOf(nextValue))).intValue();
        } else if (fraction >= 1.0f) {
            prevKeyframe = (IntKeyframe) this.mKeyframes.get(this.mNumKeyframes - 2);
            nextKeyframe = (IntKeyframe) this.mKeyframes.get(this.mNumKeyframes - 1);
            prevValue = prevKeyframe.getIntValue();
            nextValue = nextKeyframe.getIntValue();
            prevFraction = prevKeyframe.getFraction();
            nextFraction = nextKeyframe.getFraction();
            interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            intervalFraction = (fraction - prevFraction) / (nextFraction - prevFraction);
            return this.mEvaluator == null ? ((int) (((float) (nextValue - prevValue)) * intervalFraction)) + prevValue : ((Number) this.mEvaluator.evaluate(intervalFraction, Integer.valueOf(prevValue), Integer.valueOf(nextValue))).intValue();
        } else {
            prevKeyframe = (IntKeyframe) this.mKeyframes.get(0);
            int i = 1;
            while (i < this.mNumKeyframes) {
                nextKeyframe = (IntKeyframe) this.mKeyframes.get(i);
                if (fraction < nextKeyframe.getFraction()) {
                    interpolator = nextKeyframe.getInterpolator();
                    intervalFraction = (fraction - prevKeyframe.getFraction()) / (nextKeyframe.getFraction() - prevKeyframe.getFraction());
                    prevValue = prevKeyframe.getIntValue();
                    nextValue = nextKeyframe.getIntValue();
                    if (interpolator != null) {
                        intervalFraction = interpolator.getInterpolation(intervalFraction);
                    }
                    return this.mEvaluator == null ? ((int) (((float) (nextValue - prevValue)) * intervalFraction)) + prevValue : ((Number) this.mEvaluator.evaluate(intervalFraction, Integer.valueOf(prevValue), Integer.valueOf(nextValue))).intValue();
                } else {
                    prevKeyframe = nextKeyframe;
                    i++;
                }
            }
            return ((Number) ((Keyframe) this.mKeyframes.get(this.mNumKeyframes - 1)).getValue()).intValue();
        }
    }

    public Class getType() {
        return Integer.class;
    }
}
