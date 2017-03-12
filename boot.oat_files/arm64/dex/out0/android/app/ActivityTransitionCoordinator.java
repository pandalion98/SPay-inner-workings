package android.app;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListenerAdapter;
import android.transition.TransitionSet;
import android.util.ArrayMap;
import android.view.GhostView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.samsung.android.multiwindow.MultiWindowStyle;
import java.util.ArrayList;
import java.util.Collection;

abstract class ActivityTransitionCoordinator extends ResultReceiver {
    protected static final String KEY_ELEVATION = "shared_element:elevation";
    protected static final String KEY_IMAGE_MATRIX = "shared_element:imageMatrix";
    static final String KEY_REMOTE_RECEIVER = "android:remoteReceiver";
    protected static final String KEY_SCALE_TYPE = "shared_element:scaleType";
    protected static final String KEY_SCREEN_BOTTOM = "shared_element:screenBottom";
    protected static final String KEY_SCREEN_LEFT = "shared_element:screenLeft";
    protected static final String KEY_SCREEN_RIGHT = "shared_element:screenRight";
    protected static final String KEY_SCREEN_TOP = "shared_element:screenTop";
    protected static final String KEY_SNAPSHOT = "shared_element:bitmap";
    protected static final String KEY_TRANSLATION_Z = "shared_element:translationZ";
    public static final int MSG_CANCEL = 106;
    public static final int MSG_EXIT_TRANSITION_COMPLETE = 104;
    public static final int MSG_HIDE_SHARED_ELEMENTS = 101;
    public static final int MSG_SET_REMOTE_RECEIVER = 100;
    public static final int MSG_SHARED_ELEMENT_DESTINATION = 107;
    public static final int MSG_START_EXIT_TRANSITION = 105;
    public static final int MSG_TAKE_SHARED_ELEMENTS = 103;
    protected static final ScaleType[] SCALE_TYPE_VALUES = ScaleType.values();
    private static final String TAG = "ActivityTransitionCoordinator";
    protected final ArrayList<String> mAllSharedElementNames;
    private final FixedEpicenterCallback mEpicenterCallback = new FixedEpicenterCallback();
    private ArrayList<GhostViewListeners> mGhostViewListeners = new ArrayList();
    protected final boolean mIsReturning;
    private boolean mIsStartingTransition;
    protected SharedElementCallback mListener;
    private ArrayMap<View, Float> mOriginalAlphas = new ArrayMap();
    private Runnable mPendingTransition;
    protected ResultReceiver mResultReceiver;
    protected final ArrayList<String> mSharedElementNames = new ArrayList();
    private ArrayList<Matrix> mSharedElementParentMatrices;
    private boolean mSharedElementTransitionComplete;
    protected final ArrayList<View> mSharedElements = new ArrayList();
    protected ArrayList<View> mTransitioningViews = new ArrayList();
    private boolean mViewsTransitionComplete;
    private Window mWindow;

    protected class ContinueTransitionListener extends TransitionListenerAdapter {
        protected ContinueTransitionListener() {
        }

        public void onTransitionStart(Transition transition) {
            ActivityTransitionCoordinator.this.mIsStartingTransition = false;
            Runnable pending = ActivityTransitionCoordinator.this.mPendingTransition;
            ActivityTransitionCoordinator.this.mPendingTransition = null;
            if (pending != null) {
                ActivityTransitionCoordinator.this.startTransition(pending);
            }
        }
    }

    private static class FixedEpicenterCallback extends EpicenterCallback {
        private Rect mEpicenter;

        private FixedEpicenterCallback() {
        }

        public void setEpicenter(Rect epicenter) {
            this.mEpicenter = epicenter;
        }

        public Rect onGetEpicenter(Transition transition) {
            return this.mEpicenter;
        }
    }

    private static class GhostViewListeners implements OnPreDrawListener {
        private ViewGroup mDecor;
        private Matrix mMatrix = new Matrix();
        private View mParent;
        private View mView;

        public GhostViewListeners(View view, View parent, ViewGroup decor) {
            this.mView = view;
            this.mParent = parent;
            this.mDecor = decor;
        }

        public View getView() {
            return this.mView;
        }

        public boolean onPreDraw() {
            GhostView ghostView = GhostView.getGhost(this.mView);
            if (ghostView == null) {
                this.mParent.getViewTreeObserver().removeOnPreDrawListener(this);
            } else {
                GhostView.calculateMatrix(this.mView, this.mDecor, this.mMatrix);
                ghostView.setMatrix(this.mMatrix);
            }
            return true;
        }
    }

    static class SharedElementOriginalState {
        int mBottom;
        float mElevation;
        int mLeft;
        Matrix mMatrix;
        int mMeasuredHeight;
        int mMeasuredWidth;
        int mRight;
        ScaleType mScaleType;
        int mTop;
        float mTranslationZ;

        SharedElementOriginalState() {
        }
    }

    protected abstract Transition getViewsTransition();

    public ActivityTransitionCoordinator(Window window, ArrayList<String> allSharedElementNames, SharedElementCallback listener, boolean isReturning) {
        super(new Handler());
        this.mWindow = window;
        this.mListener = listener;
        this.mAllSharedElementNames = allSharedElementNames;
        this.mIsReturning = isReturning;
    }

    protected void viewsReady(ArrayMap<String, View> sharedElements) {
        sharedElements.retainAll(this.mAllSharedElementNames);
        if (this.mListener != null) {
            this.mListener.onMapSharedElements(this.mAllSharedElementNames, sharedElements);
        }
        setSharedElements(sharedElements);
        if (!(getViewsTransition() == null || this.mTransitioningViews == null)) {
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.captureTransitioningViews(this.mTransitioningViews);
            }
            this.mTransitioningViews.removeAll(this.mSharedElements);
        }
        setEpicenter();
    }

    private void setSharedElements(ArrayMap<String, View> sharedElements) {
        boolean isFirstRun = true;
        while (!sharedElements.isEmpty()) {
            for (int i = sharedElements.size() - 1; i >= 0; i--) {
                View view = (View) sharedElements.valueAt(i);
                String name = (String) sharedElements.keyAt(i);
                if (isFirstRun && (view == null || !view.isAttachedToWindow() || name == null)) {
                    sharedElements.removeAt(i);
                } else if (!isNested(view, sharedElements)) {
                    this.mSharedElementNames.add(name);
                    this.mSharedElements.add(view);
                    sharedElements.removeAt(i);
                }
            }
            isFirstRun = false;
        }
    }

    private static boolean isNested(View view, ArrayMap<String, View> sharedElements) {
        ViewParent parent = view.getParent();
        while (parent instanceof View) {
            View parentView = (View) parent;
            if (sharedElements.containsValue(parentView)) {
                return true;
            }
            parent = parentView.getParent();
        }
        return false;
    }

    protected void stripOffscreenViews() {
        if (this.mTransitioningViews != null) {
            Rect r = new Rect();
            for (int i = this.mTransitioningViews.size() - 1; i >= 0; i--) {
                View view = (View) this.mTransitioningViews.get(i);
                if (!view.getGlobalVisibleRect(r)) {
                    this.mTransitioningViews.remove(i);
                    showView(view, true);
                }
            }
        }
    }

    protected Window getWindow() {
        return this.mWindow;
    }

    public ViewGroup getDecor() {
        return this.mWindow == null ? null : (ViewGroup) this.mWindow.getDecorView();
    }

    protected void setEpicenter() {
        View epicenter = null;
        if (!(this.mAllSharedElementNames.isEmpty() || this.mSharedElementNames.isEmpty())) {
            int index = this.mSharedElementNames.indexOf(this.mAllSharedElementNames.get(0));
            if (index >= 0) {
                epicenter = (View) this.mSharedElements.get(index);
            }
        }
        setEpicenter(epicenter);
    }

    private void setEpicenter(View view) {
        if (view == null) {
            this.mEpicenterCallback.setEpicenter(null);
            return;
        }
        Rect epicenter = new Rect();
        view.getBoundsOnScreen(epicenter);
        this.mEpicenterCallback.setEpicenter(epicenter);
    }

    public ArrayList<String> getAcceptedNames() {
        return this.mSharedElementNames;
    }

    public ArrayList<String> getMappedNames() {
        ArrayList<String> names = new ArrayList(this.mSharedElements.size());
        for (int i = 0; i < this.mSharedElements.size(); i++) {
            names.add(((View) this.mSharedElements.get(i)).getTransitionName());
        }
        return names;
    }

    public ArrayList<View> copyMappedViews() {
        return new ArrayList(this.mSharedElements);
    }

    public ArrayList<String> getAllSharedElementNames() {
        return this.mAllSharedElementNames;
    }

    protected Transition setTargets(Transition transition, boolean add) {
        if (transition == null || (add && (this.mTransitioningViews == null || this.mTransitioningViews.isEmpty()))) {
            return null;
        }
        Transition set = new TransitionSet();
        if (this.mTransitioningViews != null) {
            for (int i = this.mTransitioningViews.size() - 1; i >= 0; i--) {
                View view = (View) this.mTransitioningViews.get(i);
                if (add) {
                    set.addTarget(view);
                } else {
                    set.excludeTarget(view, true);
                }
            }
        }
        set.addTransition(transition);
        if (add || this.mTransitioningViews == null || this.mTransitioningViews.isEmpty()) {
            return set;
        }
        return new TransitionSet().addTransition(set);
    }

    protected Transition configureTransition(Transition transition, boolean includeTransitioningViews) {
        if (transition == null) {
            return transition;
        }
        transition = transition.clone();
        transition.setEpicenterCallback(this.mEpicenterCallback);
        return setTargets(transition, includeTransitioningViews);
    }

    protected static Transition mergeTransitions(Transition transition1, Transition transition2) {
        if (transition1 == null) {
            return transition2;
        }
        if (transition2 == null) {
            return transition1;
        }
        Transition transitionSet = new TransitionSet();
        transitionSet.addTransition(transition1);
        transitionSet.addTransition(transition2);
        return transitionSet;
    }

    protected ArrayMap<String, View> mapSharedElements(ArrayList<String> accepted, ArrayList<View> localViews) {
        ArrayMap<String, View> sharedElements = new ArrayMap();
        if (accepted != null) {
            for (int i = 0; i < accepted.size(); i++) {
                sharedElements.put(accepted.get(i), localViews.get(i));
            }
        } else {
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.findNamedViews(sharedElements);
            }
        }
        return sharedElements;
    }

    protected void setResultReceiver(ResultReceiver resultReceiver) {
        this.mResultReceiver = resultReceiver;
    }

    private void setSharedElementState(View view, String name, Bundle transitionArgs, Matrix tempMatrix, RectF tempRect, int[] decorLoc) {
        Bundle sharedElementBundle = transitionArgs.getBundle(name);
        if (sharedElementBundle != null) {
            if (view instanceof ImageView) {
                int scaleTypeInt = sharedElementBundle.getInt(KEY_SCALE_TYPE, -1);
                if (scaleTypeInt >= 0) {
                    ImageView imageView = (ImageView) view;
                    ScaleType scaleType = SCALE_TYPE_VALUES[scaleTypeInt];
                    imageView.setScaleType(scaleType);
                    if (scaleType == ScaleType.MATRIX) {
                        tempMatrix.setValues(sharedElementBundle.getFloatArray(KEY_IMAGE_MATRIX));
                        imageView.setImageMatrix(tempMatrix);
                    }
                }
            }
            view.setTranslationZ(sharedElementBundle.getFloat(KEY_TRANSLATION_Z));
            view.setElevation(sharedElementBundle.getFloat(KEY_ELEVATION));
            float left = sharedElementBundle.getFloat(KEY_SCREEN_LEFT);
            float top = sharedElementBundle.getFloat(KEY_SCREEN_TOP);
            float right = sharedElementBundle.getFloat(KEY_SCREEN_RIGHT);
            float bottom = sharedElementBundle.getFloat(KEY_SCREEN_BOTTOM);
            if (decorLoc != null) {
                left -= (float) decorLoc[0];
                top -= (float) decorLoc[1];
                right -= (float) decorLoc[0];
                bottom -= (float) decorLoc[1];
            } else {
                getSharedElementParentMatrix(view, tempMatrix);
                tempRect.set(left, top, right, bottom);
                tempMatrix.mapRect(tempRect);
                float leftInParent = tempRect.left;
                float topInParent = tempRect.top;
                view.getInverseMatrix().mapRect(tempRect);
                float width = tempRect.width();
                float height = tempRect.height();
                view.setLeft(0);
                view.setTop(0);
                view.setRight(Math.round(width));
                view.setBottom(Math.round(height));
                tempRect.set(0.0f, 0.0f, width, height);
                view.getMatrix().mapRect(tempRect);
                left = leftInParent - tempRect.left;
                top = topInParent - tempRect.top;
                right = left + width;
                bottom = top + height;
            }
            int x = Math.round(left);
            int y = Math.round(top);
            int width2 = Math.round(right) - x;
            int height2 = Math.round(bottom) - y;
            view.measure(MeasureSpec.makeMeasureSpec(width2, 1073741824), MeasureSpec.makeMeasureSpec(height2, 1073741824));
            view.layout(x, y, x + width2, y + height2);
        }
    }

    private void setSharedElementMatrices() {
        int numSharedElements = this.mSharedElements.size();
        if (numSharedElements > 0) {
            this.mSharedElementParentMatrices = new ArrayList(numSharedElements);
        }
        int i = 0;
        while (i < numSharedElements) {
            ViewGroup parent = (ViewGroup) ((View) this.mSharedElements.get(i)).getParent();
            if (parent != null) {
                Matrix matrix = new Matrix();
                parent.transformMatrixToLocal(matrix);
                matrix.postTranslate((float) parent.getScrollX(), (float) parent.getScrollY());
                this.mSharedElementParentMatrices.add(matrix);
            } else {
                this.mSharedElements.remove(i);
                this.mSharedElementNames.remove(i);
                i--;
                numSharedElements--;
            }
            i++;
        }
    }

    private void getSharedElementParentMatrix(View view, Matrix matrix) {
        int index;
        if (this.mSharedElementParentMatrices == null) {
            index = -1;
        } else {
            index = this.mSharedElements.indexOf(view);
        }
        if (index < 0) {
            matrix.reset();
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof ViewGroup) {
                ViewGroup parent = (ViewGroup) viewParent;
                parent.transformMatrixToLocal(matrix);
                matrix.postTranslate((float) parent.getScrollX(), (float) parent.getScrollY());
                return;
            }
            return;
        }
        matrix.set((Matrix) this.mSharedElementParentMatrices.get(index));
    }

    protected ArrayList<SharedElementOriginalState> setSharedElementState(Bundle sharedElementState, ArrayList<View> snapshots) {
        ArrayList<SharedElementOriginalState> originalImageState = new ArrayList();
        if (sharedElementState != null) {
            Matrix tempMatrix = new Matrix();
            RectF tempRect = new RectF();
            int numSharedElements = this.mSharedElements.size();
            for (int i = 0; i < numSharedElements; i++) {
                View sharedElement = (View) this.mSharedElements.get(i);
                String name = (String) this.mSharedElementNames.get(i);
                originalImageState.add(getOldSharedElementState(sharedElement, name, sharedElementState));
                setSharedElementState(sharedElement, name, sharedElementState, tempMatrix, tempRect, null);
            }
        }
        if (this.mListener != null) {
            this.mListener.onSharedElementStart(this.mSharedElementNames, this.mSharedElements, snapshots);
        }
        return originalImageState;
    }

    protected void notifySharedElementEnd(ArrayList<View> snapshots) {
        if (this.mListener != null) {
            this.mListener.onSharedElementEnd(this.mSharedElementNames, this.mSharedElements, snapshots);
        }
    }

    protected void scheduleSetSharedElementEnd(final ArrayList<View> snapshots) {
        final View decorView = getDecor();
        if (decorView != null) {
            decorView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                    ActivityTransitionCoordinator.this.notifySharedElementEnd(snapshots);
                    return true;
                }
            });
        }
    }

    private static SharedElementOriginalState getOldSharedElementState(View view, String name, Bundle transitionArgs) {
        SharedElementOriginalState state = new SharedElementOriginalState();
        state.mLeft = view.getLeft();
        state.mTop = view.getTop();
        state.mRight = view.getRight();
        state.mBottom = view.getBottom();
        state.mMeasuredWidth = view.getMeasuredWidth();
        state.mMeasuredHeight = view.getMeasuredHeight();
        state.mTranslationZ = view.getTranslationZ();
        state.mElevation = view.getElevation();
        if (view instanceof ImageView) {
            Bundle bundle = transitionArgs.getBundle(name);
            if (bundle != null && bundle.getInt(KEY_SCALE_TYPE, -1) >= 0) {
                ImageView imageView = (ImageView) view;
                state.mScaleType = imageView.getScaleType();
                if (state.mScaleType == ScaleType.MATRIX) {
                    state.mMatrix = new Matrix(imageView.getImageMatrix());
                }
            }
        }
        return state;
    }

    protected ArrayList<View> createSnapshots(Bundle state, Collection<String> names) {
        int numSharedElements = names.size();
        ArrayList<View> snapshots = new ArrayList(numSharedElements);
        if (numSharedElements != 0) {
            Context context = getWindow().getContext();
            int[] decorLoc = new int[2];
            ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.getLocationOnScreen(decorLoc);
            }
            Matrix tempMatrix = new Matrix();
            for (String name : names) {
                Bundle sharedElementBundle = state.getBundle(name);
                View snapshot = null;
                if (sharedElementBundle != null) {
                    Parcelable parcelable = sharedElementBundle.getParcelable(KEY_SNAPSHOT);
                    if (!(parcelable == null || this.mListener == null)) {
                        snapshot = this.mListener.onCreateSnapshotView(context, parcelable);
                    }
                    if (snapshot != null) {
                        setSharedElementState(snapshot, name, state, tempMatrix, null, decorLoc);
                    }
                }
                snapshots.add(snapshot);
            }
        }
        return snapshots;
    }

    protected static void setOriginalSharedElementState(ArrayList<View> sharedElements, ArrayList<SharedElementOriginalState> originalState) {
        for (int i = 0; i < originalState.size(); i++) {
            View view = (View) sharedElements.get(i);
            SharedElementOriginalState state = (SharedElementOriginalState) originalState.get(i);
            if ((view instanceof ImageView) && state.mScaleType != null) {
                ImageView imageView = (ImageView) view;
                imageView.setScaleType(state.mScaleType);
                if (state.mScaleType == ScaleType.MATRIX) {
                    imageView.setImageMatrix(state.mMatrix);
                }
            }
            view.setElevation(state.mElevation);
            view.setTranslationZ(state.mTranslationZ);
            view.measure(MeasureSpec.makeMeasureSpec(state.mMeasuredWidth, 1073741824), MeasureSpec.makeMeasureSpec(state.mMeasuredHeight, 1073741824));
            view.layout(state.mLeft, state.mTop, state.mRight, state.mBottom);
        }
    }

    protected Bundle captureSharedElementState() {
        Bundle bundle = new Bundle();
        RectF tempBounds = new RectF();
        Matrix tempMatrix = new Matrix();
        for (int i = 0; i < this.mSharedElements.size(); i++) {
            captureSharedElementState((View) this.mSharedElements.get(i), (String) this.mSharedElementNames.get(i), bundle, tempMatrix, tempBounds);
        }
        return bundle;
    }

    protected void clearState() {
        this.mWindow = null;
        this.mSharedElements.clear();
        this.mTransitioningViews = null;
        this.mOriginalAlphas.clear();
        this.mResultReceiver = null;
        this.mPendingTransition = null;
        this.mListener = null;
        this.mSharedElementParentMatrices = null;
    }

    protected long getFadeDuration() {
        return getWindow().getTransitionBackgroundFadeDuration();
    }

    protected void hideViews(ArrayList<View> views) {
        int count = views.size();
        for (int i = 0; i < count; i++) {
            View view = (View) views.get(i);
            if (!this.mOriginalAlphas.containsKey(view)) {
                this.mOriginalAlphas.put(view, Float.valueOf(view.getAlpha()));
            }
            view.setAlpha(0.0f);
        }
    }

    protected void showViews(ArrayList<View> views, boolean setTransitionAlpha) {
        int count = views.size();
        for (int i = 0; i < count; i++) {
            showView((View) views.get(i), setTransitionAlpha);
        }
    }

    private void showView(View view, boolean setTransitionAlpha) {
        Float alpha = (Float) this.mOriginalAlphas.remove(view);
        if (alpha != null) {
            view.setAlpha(alpha.floatValue());
        }
        if (setTransitionAlpha) {
            view.setTransitionAlpha(1.0f);
        }
    }

    protected void captureSharedElementState(View view, String name, Bundle transitionArgs, Matrix tempMatrix, RectF tempBounds) {
        Bundle sharedElementBundle = new Bundle();
        tempMatrix.reset();
        view.transformMatrixToGlobal(tempMatrix);
        tempBounds.set(0.0f, 0.0f, (float) view.getWidth(), (float) view.getHeight());
        tempMatrix.mapRect(tempBounds);
        sharedElementBundle.putFloat(KEY_SCREEN_LEFT, tempBounds.left);
        sharedElementBundle.putFloat(KEY_SCREEN_RIGHT, tempBounds.right);
        sharedElementBundle.putFloat(KEY_SCREEN_TOP, tempBounds.top);
        sharedElementBundle.putFloat(KEY_SCREEN_BOTTOM, tempBounds.bottom);
        sharedElementBundle.putFloat(KEY_TRANSLATION_Z, view.getTranslationZ());
        sharedElementBundle.putFloat(KEY_ELEVATION, view.getElevation());
        Parcelable bitmap = null;
        if (this.mListener != null) {
            bitmap = this.mListener.onCaptureSharedElementSnapshot(view, tempMatrix, tempBounds);
        }
        if (bitmap != null) {
            sharedElementBundle.putParcelable(KEY_SNAPSHOT, bitmap);
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            sharedElementBundle.putInt(KEY_SCALE_TYPE, scaleTypeToInt(imageView.getScaleType()));
            if (imageView.getScaleType() == ScaleType.MATRIX) {
                float[] matrix = new float[9];
                imageView.getImageMatrix().getValues(matrix);
                sharedElementBundle.putFloatArray(KEY_IMAGE_MATRIX, matrix);
            }
        }
        transitionArgs.putBundle(name, sharedElementBundle);
    }

    protected void startTransition(Runnable runnable) {
        if (this.mIsStartingTransition) {
            this.mPendingTransition = runnable;
            return;
        }
        this.mIsStartingTransition = true;
        runnable.run();
    }

    protected void transitionStarted() {
        this.mIsStartingTransition = false;
    }

    protected boolean cancelPendingTransitions() {
        this.mPendingTransition = null;
        return this.mIsStartingTransition;
    }

    protected void moveSharedElementsToOverlay() {
        if (this.mWindow != null && this.mWindow.getSharedElementsUseOverlay()) {
            setSharedElementMatrices();
            int numSharedElements = this.mSharedElements.size();
            ViewGroup decor = getDecor();
            if (decor != null) {
                MultiWindowStyle multiWindowStyle = decor.getViewRootImpl() != null ? decor.getViewRootImpl().getMultiWindowStyle() : null;
                if (multiWindowStyle != null) {
                    boolean isPenWindow = multiWindowStyle.getType() == 2 && multiWindowStyle.isEnabled(2048);
                    if (isPenWindow) {
                        View contentRoot = this.mWindow.getContentRootContainer();
                        if (contentRoot instanceof ViewGroup) {
                            decor = (ViewGroup) contentRoot;
                        }
                    }
                }
                boolean moveWithParent = moveSharedElementWithParent();
                Matrix tempMatrix = new Matrix();
                for (int i = 0; i < numSharedElements; i++) {
                    View view = (View) this.mSharedElements.get(i);
                    tempMatrix.reset();
                    ((Matrix) this.mSharedElementParentMatrices.get(i)).invert(tempMatrix);
                    decor.transformMatrixToLocal(tempMatrix);
                    GhostView.addGhost(view, decor, tempMatrix);
                    ViewGroup parent = (ViewGroup) view.getParent();
                    if (moveWithParent && !isInTransitionGroup(parent, decor)) {
                        GhostViewListeners listener = new GhostViewListeners(view, parent, decor);
                        parent.getViewTreeObserver().addOnPreDrawListener(listener);
                        this.mGhostViewListeners.add(listener);
                    }
                }
            }
        }
    }

    protected boolean moveSharedElementWithParent() {
        return true;
    }

    public static boolean isInTransitionGroup(ViewParent viewParent, ViewGroup decor) {
        if (viewParent == decor || !(viewParent instanceof ViewGroup)) {
            return false;
        }
        ViewGroup parent = (ViewGroup) viewParent;
        boolean isPenWindow = false;
        MultiWindowStyle multiWindowStyle = parent.getViewRootImpl() != null ? parent.getViewRootImpl().getMultiWindowStyle() : null;
        if (multiWindowStyle != null) {
            if (multiWindowStyle.getType() == 2 && multiWindowStyle.isEnabled(2048)) {
                isPenWindow = true;
            } else {
                isPenWindow = false;
            }
        }
        if (!parent.isTransitionGroup() || isPenWindow) {
            return isInTransitionGroup(parent.getParent(), decor);
        }
        return true;
    }

    protected void moveSharedElementsFromOverlay() {
        int i;
        int numListeners = this.mGhostViewListeners.size();
        for (i = 0; i < numListeners; i++) {
            GhostViewListeners listener = (GhostViewListeners) this.mGhostViewListeners.get(i);
            ((ViewGroup) listener.getView().getParent()).getViewTreeObserver().removeOnPreDrawListener(listener);
        }
        this.mGhostViewListeners.clear();
        if (this.mWindow != null && this.mWindow.getSharedElementsUseOverlay()) {
            ViewGroup decor = getDecor();
            if (decor != null) {
                ViewGroupOverlay overlay = decor.getOverlay();
                int count = this.mSharedElements.size();
                for (i = 0; i < count; i++) {
                    GhostView.removeGhost((View) this.mSharedElements.get(i));
                }
            }
        }
    }

    protected void setGhostVisibility(int visibility) {
        int numSharedElements = this.mSharedElements.size();
        for (int i = 0; i < numSharedElements; i++) {
            GhostView ghostView = GhostView.getGhost((View) this.mSharedElements.get(i));
            if (ghostView != null) {
                ghostView.setVisibility(visibility);
            }
        }
    }

    protected void scheduleGhostVisibilityChange(final int visibility) {
        final View decorView = getDecor();
        if (decorView != null) {
            decorView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                    ActivityTransitionCoordinator.this.setGhostVisibility(visibility);
                    return true;
                }
            });
        }
    }

    protected boolean isViewsTransitionComplete() {
        return this.mViewsTransitionComplete;
    }

    protected void viewsTransitionComplete() {
        this.mViewsTransitionComplete = true;
        startInputWhenTransitionsComplete();
    }

    protected void sharedElementTransitionComplete() {
        this.mSharedElementTransitionComplete = true;
        startInputWhenTransitionsComplete();
    }

    private void startInputWhenTransitionsComplete() {
        if (this.mViewsTransitionComplete && this.mSharedElementTransitionComplete) {
            View decor = getDecor();
            if (decor != null) {
                decor.getViewRootImpl().setPausedForTransition(false);
            }
            onTransitionsComplete();
        }
    }

    protected void pauseInput() {
        View decor = getDecor();
        ViewRootImpl viewRoot = decor == null ? null : decor.getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.setPausedForTransition(true);
        }
    }

    protected void onTransitionsComplete() {
    }

    private static int scaleTypeToInt(ScaleType scaleType) {
        for (int i = 0; i < SCALE_TYPE_VALUES.length; i++) {
            if (scaleType == SCALE_TYPE_VALUES[i]) {
                return i;
            }
        }
        return -1;
    }
}
