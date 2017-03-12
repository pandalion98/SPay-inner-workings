package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MotionEvent;

public class MotionEventCompat {
    public static final int ACTION_HOVER_ENTER = 9;
    public static final int ACTION_HOVER_EXIT = 10;
    public static final int ACTION_HOVER_MOVE = 7;
    public static final int ACTION_MASK = 255;
    public static final int ACTION_POINTER_DOWN = 5;
    public static final int ACTION_POINTER_INDEX_MASK = 65280;
    public static final int ACTION_POINTER_INDEX_SHIFT = 8;
    public static final int ACTION_POINTER_UP = 6;
    public static final int ACTION_SCROLL = 8;
    static final MotionEventVersionImpl IMPL;

    interface MotionEventVersionImpl {
        int findPointerIndex(MotionEvent motionEvent, int i);

        int getPointerCount(MotionEvent motionEvent);

        int getPointerId(MotionEvent motionEvent, int i);

        float getX(MotionEvent motionEvent, int i);

        float getY(MotionEvent motionEvent, int i);
    }

    static class BaseMotionEventVersionImpl implements MotionEventVersionImpl {
        BaseMotionEventVersionImpl() {
        }

        public int findPointerIndex(MotionEvent motionEvent, int i) {
            if (i == 0) {
                return 0;
            }
            return -1;
        }

        public int getPointerId(MotionEvent motionEvent, int i) {
            if (i == 0) {
                return 0;
            }
            throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
        }

        public float getX(MotionEvent motionEvent, int i) {
            if (i == 0) {
                return motionEvent.getX();
            }
            throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
        }

        public float getY(MotionEvent motionEvent, int i) {
            if (i == 0) {
                return motionEvent.getY();
            }
            throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
        }

        public int getPointerCount(MotionEvent motionEvent) {
            return 1;
        }
    }

    static class EclairMotionEventVersionImpl implements MotionEventVersionImpl {
        EclairMotionEventVersionImpl() {
        }

        public int findPointerIndex(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.findPointerIndex(motionEvent, i);
        }

        public int getPointerId(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.getPointerId(motionEvent, i);
        }

        public float getX(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.getX(motionEvent, i);
        }

        public float getY(MotionEvent motionEvent, int i) {
            return MotionEventCompatEclair.getY(motionEvent, i);
        }

        public int getPointerCount(MotionEvent motionEvent) {
            return MotionEventCompatEclair.getPointerCount(motionEvent);
        }
    }

    static {
        if (VERSION.SDK_INT >= ACTION_POINTER_DOWN) {
            IMPL = new EclairMotionEventVersionImpl();
        } else {
            IMPL = new BaseMotionEventVersionImpl();
        }
    }

    public static int getActionMasked(MotionEvent motionEvent) {
        return motionEvent.getAction() & ACTION_MASK;
    }

    public static int getActionIndex(MotionEvent motionEvent) {
        return (motionEvent.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_SCROLL;
    }

    public static int findPointerIndex(MotionEvent motionEvent, int i) {
        return IMPL.findPointerIndex(motionEvent, i);
    }

    public static int getPointerId(MotionEvent motionEvent, int i) {
        return IMPL.getPointerId(motionEvent, i);
    }

    public static float getX(MotionEvent motionEvent, int i) {
        return IMPL.getX(motionEvent, i);
    }

    public static float getY(MotionEvent motionEvent, int i) {
        return IMPL.getY(motionEvent, i);
    }

    public static int getPointerCount(MotionEvent motionEvent) {
        return IMPL.getPointerCount(motionEvent);
    }
}
