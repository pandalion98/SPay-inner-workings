package android.graphics;

import android.graphics.Path.Direction;

public final class Outline {
    public float mAlpha;
    public Path mPath;
    public float mRadius;
    public Rect mRect;

    public Outline(Outline src) {
        set(src);
    }

    public void setEmpty() {
        this.mPath = null;
        this.mRect = null;
        this.mRadius = 0.0f;
    }

    public boolean isEmpty() {
        return this.mRect == null && this.mPath == null;
    }

    public boolean canClip() {
        return (isEmpty() || this.mRect == null) ? false : true;
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void set(Outline src) {
        if (src.mPath != null) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            this.mPath.set(src.mPath);
            this.mRect = null;
        }
        if (src.mRect != null) {
            if (this.mRect == null) {
                this.mRect = new Rect();
            }
            this.mRect.set(src.mRect);
        }
        this.mRadius = src.mRadius;
        this.mAlpha = src.mAlpha;
    }

    public void setRect(int left, int top, int right, int bottom) {
        setRoundRect(left, top, right, bottom, 0.0f);
    }

    public void setRect(Rect rect) {
        setRect(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setRoundRect(int left, int top, int right, int bottom, float radius) {
        if (left >= right || top >= bottom) {
            setEmpty();
            return;
        }
        if (this.mRect == null) {
            this.mRect = new Rect();
        }
        this.mRect.set(left, top, right, bottom);
        this.mRadius = radius;
        this.mPath = null;
    }

    public void setRoundRect(Rect rect, float radius) {
        setRoundRect(rect.left, rect.top, rect.right, rect.bottom, radius);
    }

    public void setOval(int left, int top, int right, int bottom) {
        if (left >= right || top >= bottom) {
            setEmpty();
        } else if (bottom - top == right - left) {
            setRoundRect(left, top, right, bottom, ((float) (bottom - top)) / 2.0f);
        } else {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            this.mPath.reset();
            this.mPath.addOval((float) left, (float) top, (float) right, (float) bottom, Direction.CW);
            this.mRect = null;
        }
    }

    public void setOval(Rect rect) {
        setOval(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void setConvexPath(Path convexPath) {
        if (convexPath.isEmpty()) {
            setEmpty();
        } else if (convexPath.isConvex()) {
            if (this.mPath == null) {
                this.mPath = new Path();
            }
            this.mPath.set(convexPath);
            this.mRect = null;
            this.mRadius = -1.0f;
        } else {
            throw new IllegalArgumentException("path must be convex");
        }
    }

    public void offset(int dx, int dy) {
        if (this.mRect != null) {
            this.mRect.offset(dx, dy);
        } else if (this.mPath != null) {
            this.mPath.offset((float) dx, (float) dy);
        }
    }
}
