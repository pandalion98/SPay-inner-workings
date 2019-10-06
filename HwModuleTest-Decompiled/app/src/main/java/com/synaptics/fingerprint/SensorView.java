package com.synaptics.fingerprint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.synaptics.fingerprint.ConfigReader.ConfigData;
import java.io.InputStream;

public class SensorView extends RelativeLayout {
    public static final int FP_SWIPE_BOTTOM_TO_TOP = 3;
    public static final int FP_SWIPE_LEFT_TO_RIGHT = 0;
    public static final int FP_SWIPE_RIGHT_TO_LEFT = 1;
    public static final int FP_SWIPE_TOP_TO_BOTTOM = 2;
    private static final String TAG = "SensorView";
    private Animation mBlinkAnimation;
    private Context mContext;
    private Bitmap mFingerOutlineBmp;
    private ImageView mFingerOutlineImage;
    private Bitmap mFingerSwipeBmp;
    private ImageView mFingerSwipeImage;
    private boolean mFingerSwipeOutlineVisible = false;
    private boolean mFingerSwipeVisible = false;
    private TranslateAnimation mOutlineAnimation;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    private View mSensorLED;
    private boolean mSensorVisible = false;
    private TranslateAnimation mSwipeAnimation;

    public SensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mBlinkAnimation = new AlphaAnimation(0.3f, 1.0f);
        this.mBlinkAnimation.setDuration(600);
        this.mBlinkAnimation.setInterpolator(new AccelerateInterpolator());
        this.mBlinkAnimation.setRepeatCount(-1);
        this.mBlinkAnimation.setRepeatMode(2);
        int vId = 200;
        setId(200);
        InputStream is = SensorView.class.getClassLoader().getResourceAsStream("res/drawable/finger_tip_outline.png");
        if (is != null) {
            this.mFingerOutlineBmp = BitmapFactory.decodeStream(is);
            this.mFingerOutlineImage = new ImageView(context);
            LayoutParams ilp = new LayoutParams(-2, -2);
            ilp.addRule(3, getId());
            this.mFingerOutlineImage.setLayoutParams(ilp);
            int vId2 = 200 + 1;
            this.mFingerOutlineImage.setId(200);
            this.mFingerOutlineImage.setImageBitmap(this.mFingerOutlineBmp);
            addView(this.mFingerOutlineImage);
            vId = vId2;
        }
        this.mSensorLED = new View(context);
        LayoutParams slp = new LayoutParams(25, 100);
        slp.addRule(3, getId());
        this.mSensorLED.setLayoutParams(slp);
        int vId3 = vId + 1;
        this.mSensorLED.setId(vId);
        this.mSensorLED.setBackgroundColor(-16711936);
        addView(this.mSensorLED);
        InputStream isFingerSwipe = SensorView.class.getClassLoader().getResourceAsStream("res/drawable/finger_tip.png");
        if (isFingerSwipe != null) {
            this.mFingerSwipeBmp = BitmapFactory.decodeStream(isFingerSwipe);
            this.mFingerSwipeImage = new ImageView(context);
            LayoutParams fslp = new LayoutParams(-2, -2);
            fslp.addRule(3, getId());
            this.mFingerSwipeImage.setLayoutParams(fslp);
            int i = vId3 + 1;
            this.mFingerSwipeImage.setId(vId3);
            this.mFingerSwipeImage.setImageBitmap(this.mFingerSwipeBmp);
            addView(this.mFingerSwipeImage);
            return;
        }
    }

    public boolean setLocation(ConfigData configData) {
        int i;
        Animation animation;
        ConfigData configData2 = configData;
        if (configData2 == null) {
            Log.e(TAG, "Invalid configurations");
            showAnimations(false);
            return false;
        }
        getScreenSize();
        configData.toStringDebug();
        this.mSensorVisible = configData2.sensorBar.visible;
        this.mFingerSwipeVisible = configData2.fingerSwipeAnimation.visible;
        this.mFingerSwipeOutlineVisible = configData2.fingerSwipeAnimation.outlineVisible;
        if (this.mSensorVisible) {
            LayoutParams params = (LayoutParams) this.mSensorLED.getLayoutParams();
            params.height = configData2.sensorBar.height;
            params.width = configData2.sensorBar.width;
            params.leftMargin = configData2.sensorBar.xPos;
            params.topMargin = configData2.sensorBar.yPos;
            if (params.leftMargin < 0) {
                params.leftMargin = 0;
            }
            if (params.leftMargin + params.width >= this.mScreenWidth) {
                params.leftMargin = this.mScreenWidth - params.width;
            }
            if (params.topMargin < 0) {
                params.topMargin = 0;
            }
            if (params.topMargin + params.height >= this.mScreenHeight) {
                params.topMargin = this.mScreenHeight - params.height;
            }
            this.mSensorLED.setLayoutParams(params);
            this.mSensorLED.startAnimation(this.mBlinkAnimation);
            this.mSensorLED.setVisibility(0);
        } else {
            this.mSensorLED.setAnimation(null);
            this.mSensorLED.setVisibility(4);
        }
        if (this.mFingerSwipeVisible) {
            int xPos = configData2.fingerSwipeAnimation.xPos;
            int yPos = configData2.fingerSwipeAnimation.yPos;
            float xScale = configData2.fingerSwipeAnimation.xScale;
            float yScale = configData2.fingerSwipeAnimation.yScale;
            int swipeOrientation = configData2.fingerSwipeAnimation.orientation;
            int rotate = -1;
            if (swipeOrientation == 1) {
                rotate = 90;
            } else if (swipeOrientation == 3) {
                rotate = 180;
            } else if (swipeOrientation == 0) {
                rotate = 270;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(xScale, yScale);
            if (rotate != -1) {
                matrix.postRotate((float) rotate);
            }
            Bitmap bitmapOrg = this.mFingerSwipeBmp;
            Bitmap bitmap = bitmapOrg;
            Bitmap bitmapResized = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmapResized);
            this.mFingerSwipeImage.setImageDrawable(bitmapDrawable);
            this.mFingerSwipeImage.setVisibility(0);
            int swipeImageWidth = bitmapDrawable.getIntrinsicWidth();
            int swipeImageHeight = bitmapDrawable.getIntrinsicHeight();
            int fromXDelta = xPos;
            int fromYDelta = yPos;
            int toXDelta = fromXDelta;
            int toYDelta = fromYDelta;
            int fingerOffset = configData2.fingerSwipeAnimation.offsetLength;
            int animationSpeed = configData2.fingerSwipeAnimation.animationSpeed;
            if (swipeOrientation == 2) {
                fromYDelta += fingerOffset;
                toYDelta = toYDelta + swipeImageHeight + fingerOffset;
            } else if (swipeOrientation == 3) {
                fromYDelta -= fingerOffset;
                toYDelta = (toYDelta - swipeImageHeight) - fingerOffset;
            } else if (swipeOrientation == 1) {
                fromXDelta -= fingerOffset;
                toXDelta = (toXDelta - swipeImageHeight) - fingerOffset;
            } else if (swipeOrientation == 0) {
                fromXDelta += fingerOffset;
                toXDelta = toXDelta + swipeImageHeight + fingerOffset;
            }
            Bitmap bitmapResized2 = bitmapResized;
            int fromXDelta2 = fromXDelta;
            int fromYDelta2 = fromYDelta;
            int toXDelta2 = toXDelta;
            int toYDelta2 = toYDelta;
            int i2 = fingerOffset;
            int i3 = xPos;
            int i4 = yPos;
            float f = xScale;
            float f2 = yScale;
            this.mSwipeAnimation = new TranslateAnimation((float) fromXDelta2, (float) toXDelta2, (float) fromYDelta2, (float) toYDelta2);
            this.mSwipeAnimation.setDuration((long) animationSpeed);
            this.mSwipeAnimation.setRepeatCount(-1);
            this.mFingerSwipeImage.startAnimation(this.mSwipeAnimation);
            if (this.mFingerSwipeOutlineVisible) {
                LayoutParams bgparams = (LayoutParams) this.mFingerOutlineImage.getLayoutParams();
                bgparams.height = swipeImageHeight + (2 * 20);
                bgparams.width = (2 * 20) + swipeImageWidth;
                this.mFingerOutlineImage.setLayoutParams(bgparams);
                Bitmap bmpOrg = this.mFingerOutlineBmp;
                BitmapDrawable bitmapDrawable2 = bitmapDrawable;
                Bitmap bitmap2 = bitmapResized2;
                int i5 = toXDelta2;
                this.mFingerOutlineImage.setImageDrawable(new BitmapDrawable(Bitmap.createBitmap(bmpOrg, 0, 0, bmpOrg.getWidth(), bmpOrg.getHeight(), matrix, true)));
                int i6 = toYDelta2;
                LayoutParams layoutParams = bgparams;
                int i7 = animationSpeed;
                int i8 = swipeImageWidth;
                this.mOutlineAnimation = new TranslateAnimation((float) fromXDelta2, (float) (fromXDelta2 - 20), (float) fromYDelta2, (float) (fromYDelta2 - 20));
                this.mOutlineAnimation.setFillAfter(true);
                this.mFingerOutlineImage.startAnimation(this.mOutlineAnimation);
                this.mFingerOutlineImage.setVisibility(0);
            }
            animation = null;
            i = 4;
        } else {
            animation = null;
            this.mFingerSwipeImage.setAnimation(null);
            i = 4;
            this.mFingerSwipeImage.setVisibility(4);
            this.mFingerOutlineImage.setAnimation(null);
            this.mFingerOutlineImage.setVisibility(4);
        }
        if (!this.mFingerSwipeOutlineVisible) {
            this.mFingerOutlineImage.setAnimation(animation);
            this.mFingerOutlineImage.setVisibility(i);
        }
        return true;
    }

    public void showAnimations(boolean visible) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("showAnimations: ");
        sb.append(visible);
        Log.i(str, sb.toString());
        if (visible) {
            if (this.mSensorLED != null && this.mSensorVisible) {
                this.mSensorLED.startAnimation(this.mBlinkAnimation);
                this.mSensorLED.setVisibility(0);
            }
            if (this.mFingerSwipeImage != null && this.mFingerSwipeVisible) {
                if (this.mSwipeAnimation != null) {
                    this.mFingerSwipeImage.startAnimation(this.mSwipeAnimation);
                }
                this.mFingerSwipeImage.setVisibility(0);
            }
            if (this.mFingerOutlineImage != null && this.mFingerSwipeVisible && this.mFingerSwipeOutlineVisible) {
                if (this.mOutlineAnimation != null) {
                    this.mFingerOutlineImage.startAnimation(this.mOutlineAnimation);
                }
                this.mFingerOutlineImage.setVisibility(0);
                return;
            }
            return;
        }
        if (this.mSensorLED != null) {
            this.mSensorLED.setAnimation(null);
            this.mSensorLED.setVisibility(4);
        }
        if (this.mFingerSwipeImage != null) {
            this.mFingerSwipeImage.setAnimation(null);
            this.mFingerSwipeImage.setVisibility(4);
        }
        if (this.mFingerOutlineImage != null) {
            this.mFingerOutlineImage.setAnimation(null);
            this.mFingerOutlineImage.setVisibility(4);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        getScreenSize();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    public void getScreenSize() {
        Display display = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        this.mScreenWidth = display.getWidth();
        this.mScreenHeight = display.getHeight();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int i = metrics.densityDpi;
        if (i == 120) {
            this.mScreenHeight = display.getHeight() - 19;
        } else if (i == 160) {
            this.mScreenHeight = display.getHeight() - 25;
        } else if (i != 240) {
            Log.w(TAG, "Unknown density");
        } else {
            this.mScreenHeight = display.getHeight() - 38;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("ScreenSize: ");
        sb.append(this.mScreenWidth);
        sb.append(", ");
        sb.append(this.mScreenHeight);
        Log.i(str, sb.toString());
    }
}
