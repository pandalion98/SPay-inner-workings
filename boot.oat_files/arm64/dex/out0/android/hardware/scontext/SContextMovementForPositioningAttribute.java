package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

@Deprecated
public class SContextMovementForPositioningAttribute extends SContextAttribute {
    private static final String TAG = "SContextMovementForPositioningAttribute";
    private double mMoveDistanceThrs = 100.0d;
    private int mMoveDurationThrs = 60;
    private int mMoveMinDurationThrs = 5;
    private int mNomoveDurationThrs = 60;

    SContextMovementForPositioningAttribute() {
        setAttribute();
    }

    public SContextMovementForPositioningAttribute(int nomoveDurationThrs, int moveDurationThrs, double moveDistanceThrs, int moveMinDurationThrs) {
        this.mNomoveDurationThrs = nomoveDurationThrs;
        this.mMoveDurationThrs = moveDurationThrs;
        this.mMoveDistanceThrs = moveDistanceThrs;
        this.mMoveMinDurationThrs = moveMinDurationThrs;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mNomoveDurationThrs < 0) {
            Log.e(TAG, "The nomove duration threshold is wrong.");
            return false;
        } else if (this.mMoveDurationThrs < 0) {
            Log.e(TAG, "The move duration threshold is wrong.");
            return false;
        } else if (this.mMoveDistanceThrs < 0.0d) {
            Log.e(TAG, "The move distance threshold is wrong.");
            return false;
        } else if (this.mMoveMinDurationThrs >= 0) {
            return true;
        } else {
            Log.e(TAG, "The move minimum duration threshold is wrong.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("nomove_duration_thrs", this.mNomoveDurationThrs);
        attribute.putInt("move_duration_thrs", this.mMoveDurationThrs);
        attribute.putDouble("move_distance_thrs", this.mMoveDistanceThrs);
        attribute.putInt("move_min_duration_trhs", this.mMoveMinDurationThrs);
        super.setAttribute(9, attribute);
    }
}
