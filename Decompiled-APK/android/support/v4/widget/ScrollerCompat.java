package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ScrollerCompat {
    static final ScrollerCompatImpl IMPL;
    Object mScroller;

    interface ScrollerCompatImpl {
        void abortAnimation(Object obj);

        boolean computeScrollOffset(Object obj);

        Object createScroller(Context context, Interpolator interpolator);

        void fling(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

        void fling(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

        float getCurrVelocity(Object obj);

        int getCurrX(Object obj);

        int getCurrY(Object obj);

        int getFinalX(Object obj);

        int getFinalY(Object obj);

        boolean isFinished(Object obj);

        boolean isOverScrolled(Object obj);

        void notifyHorizontalEdgeReached(Object obj, int i, int i2, int i3);

        void notifyVerticalEdgeReached(Object obj, int i, int i2, int i3);

        void startScroll(Object obj, int i, int i2, int i3, int i4);

        void startScroll(Object obj, int i, int i2, int i3, int i4, int i5);
    }

    static class ScrollerCompatImplBase implements ScrollerCompatImpl {
        ScrollerCompatImplBase() {
        }

        public Object createScroller(Context context, Interpolator interpolator) {
            return interpolator != null ? new Scroller(context, interpolator) : new Scroller(context);
        }

        public boolean isFinished(Object obj) {
            return ((Scroller) obj).isFinished();
        }

        public int getCurrX(Object obj) {
            return ((Scroller) obj).getCurrX();
        }

        public int getCurrY(Object obj) {
            return ((Scroller) obj).getCurrY();
        }

        public float getCurrVelocity(Object obj) {
            return 0.0f;
        }

        public boolean computeScrollOffset(Object obj) {
            return ((Scroller) obj).computeScrollOffset();
        }

        public void startScroll(Object obj, int i, int i2, int i3, int i4) {
            ((Scroller) obj).startScroll(i, i2, i3, i4);
        }

        public void startScroll(Object obj, int i, int i2, int i3, int i4, int i5) {
            ((Scroller) obj).startScroll(i, i2, i3, i4, i5);
        }

        public void fling(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            ((Scroller) obj).fling(i, i2, i3, i4, i5, i6, i7, i8);
        }

        public void fling(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            ((Scroller) obj).fling(i, i2, i3, i4, i5, i6, i7, i8);
        }

        public void abortAnimation(Object obj) {
            ((Scroller) obj).abortAnimation();
        }

        public void notifyHorizontalEdgeReached(Object obj, int i, int i2, int i3) {
        }

        public void notifyVerticalEdgeReached(Object obj, int i, int i2, int i3) {
        }

        public boolean isOverScrolled(Object obj) {
            return false;
        }

        public int getFinalX(Object obj) {
            return ((Scroller) obj).getFinalX();
        }

        public int getFinalY(Object obj) {
            return ((Scroller) obj).getFinalY();
        }
    }

    static class ScrollerCompatImplGingerbread implements ScrollerCompatImpl {
        ScrollerCompatImplGingerbread() {
        }

        public Object createScroller(Context context, Interpolator interpolator) {
            return ScrollerCompatGingerbread.createScroller(context, interpolator);
        }

        public boolean isFinished(Object obj) {
            return ScrollerCompatGingerbread.isFinished(obj);
        }

        public int getCurrX(Object obj) {
            return ScrollerCompatGingerbread.getCurrX(obj);
        }

        public int getCurrY(Object obj) {
            return ScrollerCompatGingerbread.getCurrY(obj);
        }

        public float getCurrVelocity(Object obj) {
            return 0.0f;
        }

        public boolean computeScrollOffset(Object obj) {
            return ScrollerCompatGingerbread.computeScrollOffset(obj);
        }

        public void startScroll(Object obj, int i, int i2, int i3, int i4) {
            ScrollerCompatGingerbread.startScroll(obj, i, i2, i3, i4);
        }

        public void startScroll(Object obj, int i, int i2, int i3, int i4, int i5) {
            ScrollerCompatGingerbread.startScroll(obj, i, i2, i3, i4, i5);
        }

        public void fling(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            ScrollerCompatGingerbread.fling(obj, i, i2, i3, i4, i5, i6, i7, i8);
        }

        public void fling(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
            ScrollerCompatGingerbread.fling(obj, i, i2, i3, i4, i5, i6, i7, i8, i9, i10);
        }

        public void abortAnimation(Object obj) {
            ScrollerCompatGingerbread.abortAnimation(obj);
        }

        public void notifyHorizontalEdgeReached(Object obj, int i, int i2, int i3) {
            ScrollerCompatGingerbread.notifyHorizontalEdgeReached(obj, i, i2, i3);
        }

        public void notifyVerticalEdgeReached(Object obj, int i, int i2, int i3) {
            ScrollerCompatGingerbread.notifyVerticalEdgeReached(obj, i, i2, i3);
        }

        public boolean isOverScrolled(Object obj) {
            return ScrollerCompatGingerbread.isOverScrolled(obj);
        }

        public int getFinalX(Object obj) {
            return ScrollerCompatGingerbread.getFinalX(obj);
        }

        public int getFinalY(Object obj) {
            return ScrollerCompatGingerbread.getFinalY(obj);
        }
    }

    static class ScrollerCompatImplIcs extends ScrollerCompatImplGingerbread {
        ScrollerCompatImplIcs() {
        }

        public float getCurrVelocity(Object obj) {
            return ScrollerCompatIcs.getCurrVelocity(obj);
        }
    }

    static {
        int i = VERSION.SDK_INT;
        if (i >= 14) {
            IMPL = new ScrollerCompatImplIcs();
        } else if (i >= 9) {
            IMPL = new ScrollerCompatImplGingerbread();
        } else {
            IMPL = new ScrollerCompatImplBase();
        }
    }

    public static ScrollerCompat create(Context context) {
        return create(context, null);
    }

    public static ScrollerCompat create(Context context, Interpolator interpolator) {
        return new ScrollerCompat(context, interpolator);
    }

    ScrollerCompat(Context context, Interpolator interpolator) {
        this.mScroller = IMPL.createScroller(context, interpolator);
    }

    public boolean isFinished() {
        return IMPL.isFinished(this.mScroller);
    }

    public int getCurrX() {
        return IMPL.getCurrX(this.mScroller);
    }

    public int getCurrY() {
        return IMPL.getCurrY(this.mScroller);
    }

    public int getFinalX() {
        return IMPL.getFinalX(this.mScroller);
    }

    public int getFinalY() {
        return IMPL.getFinalY(this.mScroller);
    }

    public float getCurrVelocity() {
        return IMPL.getCurrVelocity(this.mScroller);
    }

    public boolean computeScrollOffset() {
        return IMPL.computeScrollOffset(this.mScroller);
    }

    public void startScroll(int i, int i2, int i3, int i4) {
        IMPL.startScroll(this.mScroller, i, i2, i3, i4);
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        IMPL.startScroll(this.mScroller, i, i2, i3, i4, i5);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        IMPL.fling(this.mScroller, i, i2, i3, i4, i5, i6, i7, i8);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        IMPL.fling(this.mScroller, i, i2, i3, i4, i5, i6, i7, i8, i9, i10);
    }

    public void abortAnimation() {
        IMPL.abortAnimation(this.mScroller);
    }

    public void notifyHorizontalEdgeReached(int i, int i2, int i3) {
        IMPL.notifyHorizontalEdgeReached(this.mScroller, i, i2, i3);
    }

    public void notifyVerticalEdgeReached(int i, int i2, int i3) {
        IMPL.notifyVerticalEdgeReached(this.mScroller, i, i2, i3);
    }

    public boolean isOverScrolled() {
        return IMPL.isOverScrolled(this.mScroller);
    }
}
