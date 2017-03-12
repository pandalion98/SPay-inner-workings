package android.view;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FocusFinder {
    private static final ThreadLocal<FocusFinder> tlFocusFinder = new ThreadLocal<FocusFinder>() {
        protected FocusFinder initialValue() {
            return new FocusFinder();
        }
    };
    final Rect mBestCandidateRect;
    final Rect mFocusedRect;
    final Rect mOtherRect;
    final SequentialFocusComparator mSequentialFocusComparator;
    private final ArrayList<View> mTempList;

    private static final class SequentialFocusComparator implements Comparator<View> {
        private final Rect mFirstRect;
        private boolean mIsLayoutRtl;
        private ViewGroup mRoot;
        private final Rect mSecondRect;

        private SequentialFocusComparator() {
            this.mFirstRect = new Rect();
            this.mSecondRect = new Rect();
        }

        public void recycle() {
            this.mRoot = null;
        }

        public void setRoot(ViewGroup root) {
            this.mRoot = root;
        }

        public void setIsLayoutRtl(boolean b) {
            this.mIsLayoutRtl = b;
        }

        public int compare(View first, View second) {
            int i = 1;
            if (first == second) {
                return 0;
            }
            getRect(first, this.mFirstRect);
            getRect(second, this.mSecondRect);
            if (this.mFirstRect.top < this.mSecondRect.top) {
                return -1;
            }
            if (this.mFirstRect.top > this.mSecondRect.top) {
                return 1;
            }
            if (this.mFirstRect.left < this.mSecondRect.left) {
                if (!this.mIsLayoutRtl) {
                    i = -1;
                }
                return i;
            } else if (this.mFirstRect.left > this.mSecondRect.left) {
                if (this.mIsLayoutRtl) {
                    return -1;
                }
                return 1;
            } else if (this.mFirstRect.bottom < this.mSecondRect.bottom) {
                return -1;
            } else {
                if (this.mFirstRect.bottom > this.mSecondRect.bottom) {
                    return 1;
                }
                if (this.mFirstRect.right < this.mSecondRect.right) {
                    if (!this.mIsLayoutRtl) {
                        i = -1;
                    }
                    return i;
                } else if (this.mFirstRect.right <= this.mSecondRect.right) {
                    return 0;
                } else {
                    if (this.mIsLayoutRtl) {
                        return -1;
                    }
                    return 1;
                }
            }
        }

        private void getRect(View view, Rect rect) {
            view.getDrawingRect(rect);
            this.mRoot.offsetDescendantRectToMyCoords(view, rect);
        }
    }

    public static FocusFinder getInstance() {
        return (FocusFinder) tlFocusFinder.get();
    }

    private FocusFinder() {
        this.mFocusedRect = new Rect();
        this.mOtherRect = new Rect();
        this.mBestCandidateRect = new Rect();
        this.mSequentialFocusComparator = new SequentialFocusComparator();
        this.mTempList = new ArrayList();
    }

    public final View findNextFocus(ViewGroup root, View focused, int direction) {
        return findNextFocus(root, focused, null, direction);
    }

    public View findNextFocusFromRect(ViewGroup root, Rect focusedRect, int direction) {
        this.mFocusedRect.set(focusedRect);
        return findNextFocus(root, null, this.mFocusedRect, direction);
    }

    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction) {
        View next = null;
        if (focused != null) {
            next = findNextUserSpecifiedFocus(root, focused, direction);
        }
        if (next != null) {
            return next;
        }
        ArrayList<View> focusables = this.mTempList;
        try {
            focusables.clear();
            root.addFocusables(focusables, direction);
            if (!focusables.isEmpty()) {
                next = findNextFocus(root, focused, focusedRect, direction, focusables);
            }
            focusables.clear();
            return next;
        } catch (Throwable th) {
            focusables.clear();
        }
    }

    private View findNextUserSpecifiedFocus(ViewGroup root, View focused, int direction) {
        View userSetNextFocus = focused.findUserSetNextFocus(root, direction);
        return (userSetNextFocus == null || !userSetNextFocus.isFocusable() || (userSetNextFocus.isInTouchMode() && !userSetNextFocus.isFocusableInTouchMode())) ? null : userSetNextFocus;
    }

    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction, ArrayList<View> focusables) {
        if (focused == null) {
            if (focusedRect == null) {
                focusedRect = this.mFocusedRect;
                switch (direction) {
                    case 1:
                        if (!root.isLayoutRtl()) {
                            setFocusBottomRight(root, focusedRect);
                            break;
                        }
                        setFocusTopLeft(root, focusedRect);
                        break;
                    case 2:
                        if (!root.isLayoutRtl()) {
                            setFocusTopLeft(root, focusedRect);
                            break;
                        }
                        setFocusBottomRight(root, focusedRect);
                        break;
                    case 17:
                    case 33:
                        setFocusBottomRight(root, focusedRect);
                        break;
                    case 66:
                    case 130:
                        setFocusTopLeft(root, focusedRect);
                        break;
                    default:
                        break;
                }
            }
        }
        if (focusedRect == null) {
            focusedRect = this.mFocusedRect;
        }
        focused.getFocusedRect(focusedRect);
        root.offsetDescendantRectToMyCoords(focused, focusedRect);
        switch (direction) {
            case 1:
            case 2:
                return findNextFocusInRelativeDirection(focusables, root, focused, focusedRect, direction);
            case 17:
            case 33:
            case 66:
            case 130:
                return findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect, direction);
            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    private View findNextFocusInRelativeDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        try {
            this.mSequentialFocusComparator.setRoot(root);
            this.mSequentialFocusComparator.setIsLayoutRtl(root.isLayoutRtl());
            Collections.sort(focusables, this.mSequentialFocusComparator);
            int count = focusables.size();
            switch (direction) {
                case 1:
                    return getPreviousFocusable(focused, focusables, count);
                case 2:
                    return getNextFocusable(focused, focusables, count);
                default:
                    return (View) focusables.get(count - 1);
            }
        } finally {
            this.mSequentialFocusComparator.recycle();
        }
    }

    private void setFocusBottomRight(ViewGroup root, Rect focusedRect) {
        int rootBottom = root.getScrollY() + root.getHeight();
        int rootRight = root.getScrollX() + root.getWidth();
        focusedRect.set(rootRight, rootBottom, rootRight, rootBottom);
    }

    private void setFocusTopLeft(ViewGroup root, Rect focusedRect) {
        int rootTop = root.getScrollY();
        int rootLeft = root.getScrollX();
        focusedRect.set(rootLeft, rootTop, rootLeft, rootTop);
    }

    View findNextFocusInAbsoluteDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        this.mBestCandidateRect.set(focusedRect);
        switch (direction) {
            case 17:
                this.mBestCandidateRect.offset(focusedRect.width() + 1, 0);
                break;
            case 33:
                this.mBestCandidateRect.offset(0, focusedRect.height() + 1);
                break;
            case 66:
                this.mBestCandidateRect.offset(-(focusedRect.width() + 1), 0);
                break;
            case 130:
                this.mBestCandidateRect.offset(0, -(focusedRect.height() + 1));
                break;
        }
        View closest = null;
        int numFocusables = focusables.size();
        for (int i = 0; i < numFocusables; i++) {
            View focusable = (View) focusables.get(i);
            if (!(focusable == focused || focusable == root)) {
                focusable.getFocusedRect(this.mOtherRect);
                root.offsetDescendantRectToMyCoords(focusable, this.mOtherRect);
                if (isBetterCandidate(direction, focusedRect, this.mOtherRect, this.mBestCandidateRect)) {
                    this.mBestCandidateRect.set(this.mOtherRect);
                    closest = focusable;
                }
            }
        }
        return closest;
    }

    private static View getNextFocusable(View focused, ArrayList<View> focusables, int count) {
        if (focused != null) {
            int position = focusables.lastIndexOf(focused);
            if (position >= 0 && position + 1 < count) {
                return (View) focusables.get(position + 1);
            }
        }
        if (focusables.isEmpty()) {
            return null;
        }
        return (View) focusables.get(0);
    }

    private static View getPreviousFocusable(View focused, ArrayList<View> focusables, int count) {
        if (focused != null) {
            int position = focusables.indexOf(focused);
            if (position > 0) {
                return (View) focusables.get(position - 1);
            }
        }
        if (focusables.isEmpty()) {
            return null;
        }
        return (View) focusables.get(count - 1);
    }

    boolean isBetterCandidate(int direction, Rect source, Rect rect1, Rect rect2) {
        if (!isCandidate(source, rect1, direction)) {
            return false;
        }
        if (!isCandidate(source, rect2, direction) || beamBeats(direction, source, rect1, rect2)) {
            return true;
        }
        if (beamBeats(direction, source, rect2, rect1)) {
            return false;
        }
        if (getWeightedDistanceFor(majorAxisDistance(direction, source, rect1), minorAxisDistance(direction, source, rect1)) >= getWeightedDistanceFor(majorAxisDistance(direction, source, rect2), minorAxisDistance(direction, source, rect2))) {
            return false;
        }
        return true;
    }

    boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        boolean rect1InSrcBeam = beamsOverlap(direction, source, rect1);
        if (beamsOverlap(direction, source, rect2) || !rect1InSrcBeam) {
            return false;
        }
        if (!isToDirectionOf(direction, source, rect2) || direction == 17 || direction == 66 || majorAxisDistance(direction, source, rect1) < majorAxisDistanceToFarEdge(direction, source, rect2)) {
            return true;
        }
        return false;
    }

    long getWeightedDistanceFor(int majorAxisDistance, int minorAxisDistance) {
        return ((13 * ((long) majorAxisDistance)) * ((long) majorAxisDistance)) + ((long) (minorAxisDistance * minorAxisDistance));
    }

    boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
        switch (direction) {
            case 17:
                if ((srcRect.right > destRect.right || srcRect.left >= destRect.right) && srcRect.left > destRect.left) {
                    return true;
                }
                return false;
            case 33:
                if ((srcRect.bottom > destRect.bottom || srcRect.top >= destRect.bottom) && srcRect.top > destRect.top) {
                    return true;
                }
                return false;
            case 66:
                if ((srcRect.left < destRect.left || srcRect.right <= destRect.left) && srcRect.right < destRect.right) {
                    return true;
                }
                return false;
            case 130:
                return (srcRect.top < destRect.top || srcRect.bottom <= destRect.top) && srcRect.bottom < destRect.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
        switch (direction) {
            case 17:
            case 66:
                if (rect2.bottom < rect1.top || rect2.top > rect1.bottom) {
                    return false;
                }
                return true;
            case 33:
            case 130:
                return rect2.right >= rect1.left && rect2.left <= rect1.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    boolean isToDirectionOf(int direction, Rect src, Rect dest) {
        switch (direction) {
            case 17:
                if (src.left >= dest.right) {
                    return true;
                }
                return false;
            case 33:
                if (src.top < dest.bottom) {
                    return false;
                }
                return true;
            case 66:
                if (src.right > dest.left) {
                    return false;
                }
                return true;
            case 130:
                return src.bottom <= dest.top;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int majorAxisDistance(int direction, Rect source, Rect dest) {
        return Math.max(0, majorAxisDistanceRaw(direction, source, dest));
    }

    static int majorAxisDistanceRaw(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 17:
                return source.left - dest.right;
            case 33:
                return source.top - dest.bottom;
            case 66:
                return dest.left - source.right;
            case 130:
                return dest.top - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    static int majorAxisDistanceToFarEdgeRaw(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 17:
                return source.left - dest.left;
            case 33:
                return source.top - dest.top;
            case 66:
                return dest.right - source.right;
            case 130:
                return dest.bottom - source.bottom;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    static int minorAxisDistance(int direction, Rect source, Rect dest) {
        switch (direction) {
            case 17:
            case 66:
                return Math.abs((source.top + (source.height() / 2)) - (dest.top + (dest.height() / 2)));
            case 33:
            case 130:
                return Math.abs((source.left + (source.width() / 2)) - (dest.left + (dest.width() / 2)));
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    public View findNearestTouchable(ViewGroup root, int x, int y, int direction, int[] deltas) {
        ArrayList<View> touchables = root.getTouchables();
        int minDistance = Integer.MAX_VALUE;
        View closest = null;
        int numTouchables = touchables.size();
        int edgeSlop = ViewConfiguration.get(root.mContext).getScaledEdgeSlop();
        Rect closestBounds = new Rect();
        Rect touchableBounds = this.mOtherRect;
        for (int i = 0; i < numTouchables; i++) {
            View touchable = (View) touchables.get(i);
            touchable.getDrawingRect(touchableBounds);
            root.offsetRectBetweenParentAndChild(touchable, touchableBounds, true, true);
            if (isTouchCandidate(x, y, touchableBounds, direction)) {
                int distance = Integer.MAX_VALUE;
                switch (direction) {
                    case 17:
                        distance = (x - touchableBounds.right) + 1;
                        break;
                    case 33:
                        distance = (y - touchableBounds.bottom) + 1;
                        break;
                    case 66:
                        distance = touchableBounds.left;
                        break;
                    case 130:
                        distance = touchableBounds.top;
                        break;
                }
                if (distance < edgeSlop && (closest == null || closestBounds.contains(touchableBounds) || (!touchableBounds.contains(closestBounds) && distance < minDistance))) {
                    minDistance = distance;
                    closest = touchable;
                    closestBounds.set(touchableBounds);
                    switch (direction) {
                        case 17:
                            deltas[0] = -distance;
                            break;
                        case 33:
                            deltas[1] = -distance;
                            break;
                        case 66:
                            deltas[0] = distance;
                            break;
                        case 130:
                            deltas[1] = distance;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return closest;
    }

    private boolean isTouchCandidate(int x, int y, Rect destRect, int direction) {
        switch (direction) {
            case 17:
                if (destRect.left > x || destRect.top > y || y > destRect.bottom) {
                    return false;
                }
                return true;
            case 33:
                if (destRect.top > y || destRect.left > x || x > destRect.right) {
                    return false;
                }
                return true;
            case 66:
                if (destRect.left < x || destRect.top > y || y > destRect.bottom) {
                    return false;
                }
                return true;
            case 130:
                return destRect.top >= y && destRect.left <= x && x <= destRect.right;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }
}
