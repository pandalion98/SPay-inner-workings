package com.android.internal.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.provider.Settings$System;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewOutlineProvider;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.android.internal.R;

public class PlatLogoActivity extends Activity {
    PathInterpolator mInterpolator = new PathInterpolator(0.0f, 0.0f, 0.5f, 1.0f);
    int mKeyCount;
    FrameLayout mLayout;
    int mTapCount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mLayout = new FrameLayout(this);
        setContentView(this.mLayout);
    }

    public void onAttachedToWindow() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final float dp = dm.density;
        int size = (int) (Math.min((float) Math.min(dm.widthPixels, dm.heightPixels), 600.0f * dp) - (100.0f * dp));
        final View im = new View(this);
        im.setTranslationZ(20.0f);
        im.setScaleX(0.5f);
        im.setScaleY(0.5f);
        im.setAlpha(0.0f);
        im.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                int pad = (int) (8.0f * dp);
                outline.setOval(pad, pad, view.getWidth() - pad, view.getHeight() - pad);
            }
        });
        float hue = (float) Math.random();
        final Paint bgPaint = new Paint();
        bgPaint.setColor(Color.HSBtoColor(hue, 0.4f, 1.0f));
        final Paint fgPaint = new Paint();
        fgPaint.setColor(Color.HSBtoColor(hue, 0.5f, 1.0f));
        final Drawable M = getDrawable(R.drawable.platlogo_m);
        im.setBackground(new RippleDrawable(ColorStateList.valueOf(-1), new Drawable() {
            public void setAlpha(int alpha) {
            }

            public void setColorFilter(ColorFilter colorFilter) {
            }

            public int getOpacity() {
                return -3;
            }

            public void draw(Canvas c) {
                float r = ((float) c.getWidth()) / 2.0f;
                c.drawCircle(r, r, r, bgPaint);
                c.drawArc(0.0f, 0.0f, 2.0f * r, 2.0f * r, 135.0f, 180.0f, false, fgPaint);
                M.setBounds(0, 0, c.getWidth(), c.getHeight());
                M.draw(c);
            }
        }, null));
        im.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        im.setClickable(true);
        im.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlatLogoActivity.this.mTapCount == 0) {
                    PlatLogoActivity.this.showMarshmallow(im);
                }
                im.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        if (PlatLogoActivity.this.mTapCount < 5) {
                            return false;
                        }
                        ContentResolver cr = PlatLogoActivity.this.getContentResolver();
                        if (Settings$System.getLong(cr, Settings$System.EGG_MODE, 0) == 0) {
                            try {
                                Settings$System.putLong(cr, Settings$System.EGG_MODE, System.currentTimeMillis());
                            } catch (RuntimeException e) {
                                Log.e("PlatLogoActivity", "Can't write settings", e);
                            }
                        }
                        im.post(new Runnable() {
                            public void run() {
                                try {
                                    PlatLogoActivity.this.startActivity(new Intent("android.intent.action.MAIN").setFlags(276856832).addCategory("com.android.internal.category.PLATLOGO"));
                                } catch (ActivityNotFoundException e) {
                                    Log.e("PlatLogoActivity", "No more eggs.");
                                }
                                PlatLogoActivity.this.finish();
                            }
                        });
                        return true;
                    }
                });
                PlatLogoActivity platLogoActivity = PlatLogoActivity.this;
                platLogoActivity.mTapCount++;
            }
        });
        im.setFocusable(true);
        im.requestFocus();
        im.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 4 || event.getAction() != 0) {
                    return false;
                }
                if (PlatLogoActivity.this.mKeyCount == 0) {
                    PlatLogoActivity.this.showMarshmallow(im);
                }
                PlatLogoActivity platLogoActivity = PlatLogoActivity.this;
                platLogoActivity.mKeyCount++;
                if (PlatLogoActivity.this.mKeyCount > 2) {
                    if (PlatLogoActivity.this.mTapCount > 5) {
                        im.performLongClick();
                    } else {
                        im.performClick();
                    }
                }
                return true;
            }
        });
        this.mLayout.addView(im, new LayoutParams(size, size, 17));
        im.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setInterpolator(this.mInterpolator).setDuration(500).setStartDelay(800).start();
    }

    public void showMarshmallow(View im) {
        Drawable fg = getDrawable(R.drawable.platlogo);
        fg.setBounds(0, 0, im.getWidth(), im.getHeight());
        fg.setAlpha(0);
        im.getOverlay().add(fg);
        Animator fadeIn = ObjectAnimator.ofInt(fg, "alpha", new int[]{255});
        fadeIn.setInterpolator(this.mInterpolator);
        fadeIn.setDuration(300);
        fadeIn.start();
    }
}
