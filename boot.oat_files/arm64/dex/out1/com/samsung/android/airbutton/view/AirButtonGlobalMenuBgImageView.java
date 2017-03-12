package com.samsung.android.airbutton.view;

import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

public class AirButtonGlobalMenuBgImageView extends ImageView {
    public AirButtonGlobalMenuBgImageView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void startOpenAnimation() {
    }

    public void startCloseAnimation(int startDelay, AnimatorListener mFinishListener) {
    }

    public void setAnimationArc(float animationArc) {
    }
}
