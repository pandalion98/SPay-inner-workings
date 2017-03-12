package com.android.internal.policy.multiwindow;

import android.graphics.Rect;

public class EdgeInspector {
    public static final int BOTTOM = 2;
    public static final int LEFT = 4;
    public static final int NONE = 0;
    public static final int RIGHT = 8;
    public static final int TOP = 1;
    private Rect mBound;
    private int mDir = 0;
    private int mFilter = 15;
    private boolean mIsCandidate = false;
    private boolean mOnlyConerResizable = false;
    private Rect mPadding;

    public EdgeInspector(Rect bound, Rect padding, boolean onlyCornerResizable) {
        set(bound, padding);
        this.mOnlyConerResizable = onlyCornerResizable;
    }

    public boolean isCorner() {
        if (((this.mDir & 4) == 0 && (this.mDir & 8) == 0) || ((this.mDir & 2) == 0 && (this.mDir & 1) == 0)) {
            return false;
        }
        return true;
    }

    public void set(Rect bound, Rect padding) {
        this.mBound = bound;
        this.mPadding = padding;
        this.mIsCandidate = false;
    }

    public void setFilter(int filter) {
        this.mFilter = filter;
    }

    public boolean isEdge() {
        return this.mDir != 0;
    }

    public boolean isEdge(int direction) {
        return (this.mDir & direction) == direction;
    }

    public boolean isCandidate() {
        return this.mIsCandidate;
    }

    public void clear() {
        this.mDir = 0;
        this.mIsCandidate = false;
    }

    public void check(int x, int y) {
        if (this.mBound != null && this.mPadding != null) {
            if ((this.mDir & 8) == 0 && this.mBound.left + this.mPadding.left > x) {
                this.mDir |= 4;
            } else if ((this.mDir & 4) == 0 && this.mBound.right - this.mPadding.right < x) {
                this.mDir |= 8;
            }
            if ((this.mDir & 2) == 0 && this.mBound.top + this.mPadding.top > y) {
                this.mDir |= 1;
            } else if ((this.mDir & 1) == 0 && this.mBound.bottom - this.mPadding.bottom < y) {
                this.mDir |= 2;
            }
            if (!isCorner()) {
                if (this.mBound.left + (this.mPadding.left * 2) > x) {
                    this.mIsCandidate = true;
                } else if (this.mBound.right - (this.mPadding.right * 2) < x) {
                    this.mIsCandidate = true;
                }
                if (!this.mIsCandidate) {
                    if (this.mBound.top + (this.mPadding.top * 2) > y) {
                        this.mIsCandidate = true;
                    } else if (this.mBound.bottom - (this.mPadding.bottom * 2) < y) {
                        this.mIsCandidate = true;
                    }
                }
            }
            if (!this.mOnlyConerResizable) {
                this.mDir &= -2;
                this.mDir &= -5;
            }
            this.mDir &= this.mFilter;
        }
    }
}
