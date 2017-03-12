package com.android.internal.policy.multiwindow;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.NinePatchDrawable;
import android.view.WindowManager;
import com.android.internal.R;
import com.samsung.android.multiwindow.MultiWindowFeatures;

public class Border {
    public static final int DRAW_ALL = 15;
    public static final int DRAW_BOTTOM = 8;
    public static final int DRAW_LEFT = 1;
    public static final int DRAW_RIGHT = 2;
    public static final int DRAW_TOP = 4;
    private Context mContext;
    private int mDrawOption = 15;
    private int mInnerLineColor;
    private final boolean mIsSupportDrawableFrame;
    private final boolean mIsSupportUnfocusedLine;
    private final boolean mIsSupportUnfocusedShadow;
    private int mLastDrawnOption = 15;
    private boolean mLastFocus = false;
    private int mOutLineColor;
    private Bitmap mPopupWindowBorderBitmap;
    private NinePatchDrawable mPopupWindowBorderDrawable;
    private NinePatchDrawable mPopupWindowClippingBorderDrawable;
    private int mPopupWindowCocktailOverlapSize;
    private int mPopupWindowFocusColor;
    private int mPopupWindowFocusOutlineColor;
    private int mPopupWindowUnfocusColor;
    private int mPopupWindowUnfocusOutlineColor;
    private int mSplitUnfocusColor;
    private NinePatch mSplitUnfocusFrameBottom;
    private NinePatch mSplitUnfocusFrameLeft;
    private NinePatch mSplitUnfocusFrameRight;
    private NinePatch mSplitUnfocusFrameTop;
    private int mSplitUnfocusOutlineColor;
    private int mSplitUnfocusThickness;
    private float mThicknessBorderPaintInner;
    private float mThicknessBorderPaintOuter;
    private WindowManager mWindowManager;

    public Border(Context context, WindowManager wm) {
        this.mContext = context;
        this.mWindowManager = wm;
        this.mIsSupportUnfocusedShadow = false;
        this.mIsSupportUnfocusedLine = MultiWindowFeatures.isSupportSimplificationUI(this.mContext);
        this.mIsSupportDrawableFrame = MultiWindowFeatures.isSupportSimplificationUI(this.mContext);
        if (this.mIsSupportUnfocusedShadow) {
            Bitmap leftUnfocus = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.multiwindow_frame_shadow_left);
            Bitmap rightUnfocus = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.multiwindow_frame_shadow_right);
            Bitmap topUnfocus = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.multiwindow_frame_shadow_top);
            Bitmap bottomUnfocus = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.multiwindow_frame_shadow_bottom);
            this.mSplitUnfocusFrameLeft = new NinePatch(leftUnfocus, leftUnfocus.getNinePatchChunk());
            this.mSplitUnfocusFrameRight = new NinePatch(rightUnfocus, rightUnfocus.getNinePatchChunk());
            this.mSplitUnfocusFrameTop = new NinePatch(topUnfocus, topUnfocus.getNinePatchChunk());
            this.mSplitUnfocusFrameBottom = new NinePatch(bottomUnfocus, bottomUnfocus.getNinePatchChunk());
            this.mSplitUnfocusThickness = leftUnfocus.getWidth();
        } else if (this.mIsSupportUnfocusedLine) {
            this.mSplitUnfocusThickness = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_unfocusline_thickness);
            this.mSplitUnfocusColor = this.mContext.getResources().getColor(R.color.multiwindow_unfocusline_color);
            this.mSplitUnfocusOutlineColor = this.mContext.getResources().getColor(R.color.multiwindow_unfocus_outline_color);
        } else if (MultiWindowFeatures.isSupportQuadView(this.mContext)) {
            this.mSplitUnfocusThickness = (int) this.mContext.getResources().getDimension(R.dimen.multiwindow_quadview_unfocusline_thickness);
            this.mSplitUnfocusColor = this.mContext.getResources().getColor(R.color.multiwindow_quadview_unfocusline_color);
        }
        this.mPopupWindowFocusColor = this.mContext.getResources().getColor(R.color.multiwindow_focusline_color_scaled_floating);
        this.mPopupWindowUnfocusColor = this.mContext.getResources().getColor(R.color.multiwindow_un_focusline_color_scaled_floating);
        this.mPopupWindowFocusOutlineColor = this.mContext.getResources().getColor(R.color.multiwindow_focus_outline_color_scaled_floating);
        this.mPopupWindowUnfocusOutlineColor = this.mContext.getResources().getColor(R.color.multiwindow_un_focus_outline_color_scaled_floating);
        if (this.mIsSupportDrawableFrame) {
            this.mPopupWindowBorderBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), R.drawable.multiwindow_popup_window_frame_normal);
            this.mPopupWindowBorderDrawable = new NinePatchDrawable(context.getResources(), new NinePatch(this.mPopupWindowBorderBitmap, this.mPopupWindowBorderBitmap.getNinePatchChunk()));
            this.mPopupWindowBorderDrawable.setTintMode(Mode.MULTIPLY);
            this.mPopupWindowBorderDrawable.setTint(this.mPopupWindowUnfocusColor);
            this.mPopupWindowClippingBorderDrawable = this.mPopupWindowBorderDrawable;
            this.mPopupWindowCocktailOverlapSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_popup_window_cocktail_overlap);
            return;
        }
        this.mThicknessBorderPaintInner = (float) context.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_mBorderPaintInner);
        this.mThicknessBorderPaintOuter = (float) context.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_mBorderPaintOuter);
        if (!this.mLastFocus) {
            this.mInnerLineColor = this.mPopupWindowUnfocusColor;
            this.mOutLineColor = this.mPopupWindowUnfocusOutlineColor;
        }
    }

    public void setDrawOption(int option) {
        this.mDrawOption = option;
    }

    public void setFocus(boolean focus) {
        setFocus(focus, false);
    }

    private void setFocus(boolean focus, boolean force) {
        if (this.mLastFocus != focus || force) {
            if (this.mIsSupportDrawableFrame) {
                this.mPopupWindowBorderDrawable.setTintMode(Mode.MULTIPLY);
                this.mPopupWindowClippingBorderDrawable.setTintMode(Mode.MULTIPLY);
                if (focus) {
                    this.mPopupWindowBorderDrawable.setTint(this.mPopupWindowFocusColor);
                    this.mPopupWindowClippingBorderDrawable.setTint(this.mPopupWindowFocusColor);
                } else {
                    this.mPopupWindowBorderDrawable.setTint(this.mPopupWindowUnfocusColor);
                    this.mPopupWindowClippingBorderDrawable.setTint(this.mPopupWindowUnfocusColor);
                }
            } else if (focus) {
                this.mInnerLineColor = this.mPopupWindowFocusColor;
                this.mOutLineColor = this.mPopupWindowFocusOutlineColor;
            } else {
                this.mInnerLineColor = this.mPopupWindowUnfocusColor;
                this.mOutLineColor = this.mPopupWindowUnfocusOutlineColor;
            }
            this.mLastFocus = focus;
        }
    }

    public void drawFocusBorder(Canvas canvas, int width, int height) {
        if (this.mIsSupportDrawableFrame) {
            drawBorderBitmap(canvas, width, height);
            return;
        }
        drawBorderLine(canvas, width, height, this.mThicknessBorderPaintInner, this.mInnerLineColor);
        drawBorderLine(canvas, width, height, this.mThicknessBorderPaintOuter, this.mOutLineColor);
    }

    private void drawBorderBitmap(Canvas canvas, int width, int height) {
        if (this.mLastDrawnOption != this.mDrawOption) {
            Bitmap clippingBorder = this.mPopupWindowBorderBitmap;
            switch ((this.mDrawOption ^ -1) & 15) {
                case 1:
                    clippingBorder = Bitmap.createBitmap(this.mPopupWindowBorderBitmap, this.mPopupWindowCocktailOverlapSize, 0, this.mPopupWindowBorderBitmap.getWidth() - this.mPopupWindowCocktailOverlapSize, this.mPopupWindowBorderBitmap.getHeight());
                    break;
                case 2:
                    clippingBorder = Bitmap.createBitmap(this.mPopupWindowBorderBitmap, 0, 0, this.mPopupWindowBorderBitmap.getWidth() - this.mPopupWindowCocktailOverlapSize, this.mPopupWindowBorderBitmap.getHeight());
                    break;
                case 4:
                    clippingBorder = Bitmap.createBitmap(this.mPopupWindowBorderBitmap, 0, this.mPopupWindowCocktailOverlapSize, this.mPopupWindowBorderBitmap.getWidth(), this.mPopupWindowBorderBitmap.getHeight() - this.mPopupWindowCocktailOverlapSize);
                    break;
                case 8:
                    clippingBorder = Bitmap.createBitmap(this.mPopupWindowBorderBitmap, 0, 0, this.mPopupWindowBorderBitmap.getWidth(), this.mPopupWindowBorderBitmap.getHeight() - this.mPopupWindowCocktailOverlapSize);
                    break;
            }
            this.mPopupWindowClippingBorderDrawable = new NinePatchDrawable(this.mContext.getResources(), new NinePatch(clippingBorder, this.mPopupWindowBorderBitmap.getNinePatchChunk()));
            setFocus(this.mLastFocus, true);
        }
        canvas.clipRect(0, 0, width, height);
        this.mPopupWindowClippingBorderDrawable.setBounds(0, 0, width, height);
        this.mPopupWindowClippingBorderDrawable.draw(canvas);
    }

    private void drawBorderLine(Canvas canvas, int width, int height, float thickness, int color) {
        float rotationAdjX = 0.0f;
        float rotationAdjY = 0.0f;
        switch (this.mWindowManager.getDefaultDisplay().getRotation()) {
            case 0:
            case 2:
                rotationAdjY = thickness;
                break;
            case 1:
            case 3:
                rotationAdjX = thickness;
                break;
        }
        if (thickness > 0.0f) {
            canvas.clipRect(0.0f, 0.0f, (float) width, (float) height, Op.REPLACE);
            canvas.clipRect(thickness, thickness, ((float) width) - thickness, ((float) height) - thickness, Op.DIFFERENCE);
            if ((this.mDrawOption & 4) == 0) {
                canvas.clipRect(rotationAdjX, 0.0f, ((float) width) - rotationAdjX, thickness, Op.DIFFERENCE);
            }
            if ((this.mDrawOption & 8) == 0) {
                canvas.clipRect(rotationAdjX, ((float) height) - thickness, ((float) width) - rotationAdjX, (float) height, Op.DIFFERENCE);
            }
            if ((this.mDrawOption & 1) == 0) {
                canvas.clipRect(0.0f, rotationAdjY, thickness, ((float) height) - rotationAdjY, Op.DIFFERENCE);
            }
            if ((this.mDrawOption & 2) == 0) {
                canvas.clipRect(((float) width) - thickness, rotationAdjY, (float) width, ((float) height) - rotationAdjY, Op.DIFFERENCE);
            }
            canvas.drawColor(color);
        }
    }

    public void drawUnfocusBorder(Canvas canvas, int width, int height, int zone) {
        Rect mTmpDrawRect = new Rect();
        Configuration conf = this.mContext.getResources().getConfiguration();
        if (this.mIsSupportUnfocusedShadow) {
            if (MultiWindowFeatures.isSupportQuadView(this.mContext)) {
                mTmpDrawRect.set(0, 0, this.mSplitUnfocusThickness, height);
                canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                canvas.drawPatch(this.mSplitUnfocusFrameLeft, mTmpDrawRect, null);
                mTmpDrawRect.set(0, 0, width, this.mSplitUnfocusThickness);
                canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                canvas.drawPatch(this.mSplitUnfocusFrameTop, mTmpDrawRect, null);
                mTmpDrawRect.set(width - this.mSplitUnfocusThickness, 0, width, height);
                canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                canvas.drawPatch(this.mSplitUnfocusFrameRight, mTmpDrawRect, null);
                mTmpDrawRect.set(0, height - this.mSplitUnfocusThickness, width, height);
                canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                canvas.drawPatch(this.mSplitUnfocusFrameBottom, mTmpDrawRect, null);
            } else if (zone == 3) {
                if (conf.orientation == 2) {
                    mTmpDrawRect.set(width - this.mSplitUnfocusThickness, 0, width, height);
                    canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                    canvas.drawPatch(this.mSplitUnfocusFrameLeft, mTmpDrawRect, null);
                    return;
                }
                mTmpDrawRect.set(0, height - this.mSplitUnfocusThickness, width, height);
                canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                canvas.drawPatch(this.mSplitUnfocusFrameTop, mTmpDrawRect, null);
            } else if (conf.orientation == 2) {
                mTmpDrawRect.set(0, 0, this.mSplitUnfocusThickness, height);
                canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                canvas.drawPatch(this.mSplitUnfocusFrameRight, mTmpDrawRect, null);
            } else {
                mTmpDrawRect.set(0, 0, width, this.mSplitUnfocusThickness);
                canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
                canvas.drawPatch(this.mSplitUnfocusFrameBottom, mTmpDrawRect, null);
            }
        } else if (this.mIsSupportUnfocusedLine) {
            int color;
            if (zone == 3) {
                color = this.mSplitUnfocusColor;
                if (conf.orientation == 2) {
                    mTmpDrawRect.set(width - this.mSplitUnfocusThickness, 0, width, height);
                } else {
                    mTmpDrawRect.set(0, height - this.mSplitUnfocusThickness, width, height);
                }
            } else {
                color = this.mSplitUnfocusOutlineColor;
                if (conf.orientation == 2) {
                    mTmpDrawRect.set(0, 0, this.mSplitUnfocusThickness, height);
                } else {
                    mTmpDrawRect.set(0, 0, width, this.mSplitUnfocusThickness);
                }
            }
            canvas.clipRect(mTmpDrawRect, Op.INTERSECT);
            canvas.drawColor(color);
        } else if (MultiWindowFeatures.isSupportQuadView(this.mContext)) {
            RectF mTmpDrawFloatRect = new RectF();
            if (conf.orientation == 2) {
                if (zone == 1) {
                    mTmpDrawFloatRect.set((float) (width - (this.mSplitUnfocusThickness / 2)), 0.0f, (float) width, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                    mTmpDrawFloatRect.set(0.0f, (float) (height - (this.mSplitUnfocusThickness / 2)), (float) width, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                } else if (zone == 2) {
                    mTmpDrawFloatRect.set((float) (width - (this.mSplitUnfocusThickness / 2)), 0.0f, (float) width, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                    mTmpDrawFloatRect.set(0.0f, 0.0f, (float) width, ((float) this.mSplitUnfocusThickness) / 2.0f);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                } else if (zone == 4) {
                    mTmpDrawFloatRect.set(0.0f, 0.0f, ((float) this.mSplitUnfocusThickness) / 2.0f, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                    mTmpDrawFloatRect.set(0.0f, (float) (height - (this.mSplitUnfocusThickness / 2)), (float) width, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                } else if (zone == 8) {
                    mTmpDrawFloatRect.set(0.0f, 0.0f, ((float) this.mSplitUnfocusThickness) / 2.0f, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                    mTmpDrawFloatRect.set(0.0f, 0.0f, (float) width, ((float) this.mSplitUnfocusThickness) / 2.0f);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                } else if (zone == 3) {
                    mTmpDrawFloatRect.set((float) (width - (this.mSplitUnfocusThickness / 2)), 0.0f, (float) width, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                } else if (zone == 12) {
                    mTmpDrawFloatRect.set(0.0f, 0.0f, ((float) this.mSplitUnfocusThickness) / 2.0f, (float) height);
                    drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                }
            } else if (zone == 1) {
                mTmpDrawFloatRect.set((float) (width - (this.mSplitUnfocusThickness / 2)), 0.0f, (float) width, (float) height);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                mTmpDrawFloatRect.set(0.0f, (float) (height - (this.mSplitUnfocusThickness / 2)), (float) width, (float) height);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
            } else if (zone == 2) {
                mTmpDrawFloatRect.set(0.0f, 0.0f, ((float) this.mSplitUnfocusThickness) / 2.0f, (float) height);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                mTmpDrawFloatRect.set(0.0f, (float) (height - (this.mSplitUnfocusThickness / 2)), (float) width, (float) height);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
            } else if (zone == 4) {
                mTmpDrawFloatRect.set((float) (width - (this.mSplitUnfocusThickness / 2)), 0.0f, (float) width, (float) height);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                mTmpDrawFloatRect.set(0.0f, 0.0f, (float) width, ((float) this.mSplitUnfocusThickness) / 2.0f);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
            } else if (zone == 8) {
                mTmpDrawFloatRect.set(0.0f, 0.0f, ((float) this.mSplitUnfocusThickness) / 2.0f, (float) height);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
                mTmpDrawFloatRect.set(0.0f, 0.0f, (float) width, ((float) this.mSplitUnfocusThickness) / 2.0f);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
            } else if (zone == 3) {
                mTmpDrawFloatRect.set(0.0f, (float) (height - (this.mSplitUnfocusThickness / 2)), (float) width, (float) height);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
            } else if (zone == 12) {
                mTmpDrawFloatRect.set(0.0f, 0.0f, (float) width, ((float) this.mSplitUnfocusThickness) / 2.0f);
                drawZoneBasedUnfocusBorder(canvas, mTmpDrawFloatRect);
            }
        }
    }

    private void drawZoneBasedUnfocusBorder(Canvas canvas, RectF mTmpDrawFloatRect) {
        canvas.clipRect(mTmpDrawFloatRect, Op.REPLACE);
        canvas.drawColor(this.mSplitUnfocusColor);
    }
}
