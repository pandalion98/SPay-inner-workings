package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextPedometerAttribute extends SContextAttribute {
    private static int MODE_EXERCISE = 1;
    private static int MODE_USER_INFO = 0;
    private static final String TAG = "SContextPedometerAttribute";
    private int mExerciseMode;
    private int mGender;
    private double mHeight;
    private int mMode;
    private double mWeight;

    SContextPedometerAttribute() {
        this.mGender = 1;
        this.mHeight = 170.0d;
        this.mWeight = 60.0d;
        this.mExerciseMode = -1;
        this.mMode = -1;
        this.mMode = MODE_USER_INFO;
        setAttribute();
    }

    public SContextPedometerAttribute(int gender, double height, double weight) {
        this.mGender = 1;
        this.mHeight = 170.0d;
        this.mWeight = 60.0d;
        this.mExerciseMode = -1;
        this.mMode = -1;
        this.mMode = MODE_USER_INFO;
        this.mGender = gender;
        this.mHeight = height;
        this.mWeight = weight;
        setAttribute();
    }

    public SContextPedometerAttribute(int exerciseMode) {
        this.mGender = 1;
        this.mHeight = 170.0d;
        this.mWeight = 60.0d;
        this.mExerciseMode = -1;
        this.mMode = -1;
        this.mMode = MODE_EXERCISE;
        this.mExerciseMode = exerciseMode;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mGender < 1 || this.mGender > 2) {
            Log.e(TAG, "The gender is wrong.");
            return false;
        } else if (this.mHeight <= 0.0d) {
            Log.e(TAG, "The height is wrong.");
            return false;
        } else if (this.mWeight <= 0.0d) {
            Log.e(TAG, "The weight is wrong.");
            return false;
        } else if (this.mExerciseMode >= -1 && this.mExerciseMode <= 2) {
            return true;
        } else {
            Log.e(TAG, "The exercise mode is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("mode", this.mMode);
        if (this.mMode == MODE_USER_INFO) {
            attribute.putInt("gender", this.mGender);
            attribute.putDouble("height", this.mHeight);
            attribute.putDouble("weight", this.mWeight);
        } else {
            attribute.putInt("exercise_mode", this.mExerciseMode);
        }
        super.setAttribute(2, attribute);
    }
}
