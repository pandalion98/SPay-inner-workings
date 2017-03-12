package android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.IntProperty;
import android.util.MathUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroupOverlay;
import com.android.internal.R;
import com.android.internal.widget.AutoScrollHelper;

class HorizontalFastScroller {
    private static final int[] ATTRS = new int[]{R.attr.fastScrollTextColor, R.attr.fastScrollThumbDrawable, R.attr.fastScrollTrackDrawable, R.attr.fastScrollPreviewBackgroundLeft, R.attr.fastScrollPreviewBackgroundRight, R.attr.fastScrollOverlayPosition};
    private static Property<View, Integer> BOTTOM = new IntProperty<View>("bottom") {
        public void setValue(View object, int value) {
            object.setBottom(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getBottom());
        }
    };
    private static final int DURATION_CROSS_FADE = 50;
    private static final int DURATION_FADE_IN = 150;
    private static final int DURATION_FADE_OUT = 300;
    private static final int DURATION_RESIZE = 100;
    private static final long FADE_TIMEOUT = 1500;
    private static Property<View, Integer> LEFT = new IntProperty<View>("left") {
        public void setValue(View object, int value) {
            object.setLeft(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getLeft());
        }
    };
    private static final int MIN_PAGES = 4;
    private static final int OVERLAY_AT_THUMB = 1;
    private static final int OVERLAY_FLOATING = 0;
    private static final int OVERLAY_POSITION = 5;
    private static final int PREVIEW_BACKGROUND_LEFT = 3;
    private static final int PREVIEW_BACKGROUND_RIGHT = 4;
    private static final int PREVIEW_LEFT = 0;
    private static final int PREVIEW_RIGHT = 1;
    private static Property<View, Integer> RIGHT = new IntProperty<View>("right") {
        public void setValue(View object, int value) {
            object.setRight(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getRight());
        }
    };
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_NONE = 0;
    private static final int STATE_VISIBLE = 1;
    private static final long TAP_TIMEOUT = ((long) ViewConfiguration.getTapTimeout());
    private static final int TEXT_COLOR = 0;
    private static final int THUMB_DRAWABLE = 1;
    private static Property<View, Integer> TOP = new IntProperty<View>("top") {
        public void setValue(View object, int value) {
            object.setTop(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(object.getTop());
        }
    };
    private static final int TRACK_DRAWABLE = 2;
    private boolean mAlwaysShow;
    private final Rect mContainerRect = new Rect();
    private int mCurrentSection = -1;
    private AnimatorSet mDecorAnimation;
    private final Runnable mDeferHide = new Runnable() {
        public void run() {
            HorizontalFastScroller.this.setState(0);
        }
    };
    private final Runnable mDeferStartDrag = new Runnable() {
        public void run() {
            if (HorizontalFastScroller.this.mList.isAttachedToWindow()) {
                HorizontalFastScroller.this.beginDrag();
                HorizontalFastScroller.this.scrollTo(HorizontalFastScroller.this.getPosFromMotionEvent(HorizontalFastScroller.this.mInitialTouchY));
            }
            HorizontalFastScroller.this.mHasPendingDrag = false;
        }
    };
    private boolean mEnabled;
    private int mFirstVisibleItem;
    private boolean mHasPendingDrag;
    private final boolean mHasTrackImage;
    private int mHeaderCount;
    private final int mHeight;
    private float mInitialTouchY;
    private boolean mLayoutFromRight;
    private final AbsHorizontalListView mList;
    private BaseAdapter mListAdapter;
    private boolean mLongList;
    private boolean mMatchDragPosition;
    private final ViewGroupOverlay mOverlay;
    private int mOverlayPosition;
    private AnimatorSet mPreviewAnimation;
    private final ImageView mPreviewImage;
    private final int mPreviewPadding;
    private final int[] mPreviewResId = new int[2];
    private final TextView mPrimaryText;
    private int mScaledTouchSlop;
    private int mScrollBarStyle;
    private boolean mScrollCompleted;
    private int mScrollbarPosition = -1;
    private final TextView mSecondaryText;
    private SectionIndexer mSectionIndexer;
    private Object[] mSections;
    private boolean mShowingPreview;
    private boolean mShowingPrimary;
    private int mState;
    private final AnimatorListener mSwitchPrimaryListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animation) {
            HorizontalFastScroller.this.mShowingPrimary = !HorizontalFastScroller.this.mShowingPrimary;
        }
    };
    private final Rect mTempBounds = new Rect();
    private final Rect mTempMargins = new Rect();
    private final ImageView mThumbImage;
    private final ImageView mTrackImage;
    private boolean mUpdatingLayout;
    private final int mWidth;

    public HorizontalFastScroller(AbsHorizontalListView listView) {
        this.mList = listView;
        this.mOverlay = listView.getOverlay();
        Context context = listView.getContext();
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Resources res = context.getResources();
        TypedArray ta = context.getTheme().obtainStyledAttributes(ATTRS);
        ImageView trackImage = new ImageView(context);
        this.mTrackImage = trackImage;
        int width = 0;
        int height = 0;
        Drawable trackDrawable = ta.getDrawable(2);
        if (trackDrawable != null) {
            this.mHasTrackImage = true;
            trackImage.setBackground(trackDrawable);
            this.mOverlay.add(trackImage);
            width = Math.max(0, trackDrawable.getIntrinsicWidth());
            height = Math.max(0, trackDrawable.getIntrinsicHeight());
        } else {
            this.mHasTrackImage = false;
        }
        ImageView thumbImage = new ImageView(context);
        this.mThumbImage = thumbImage;
        Drawable thumbDrawable = ta.getDrawable(1);
        if (!(thumbDrawable == null || trackDrawable == null)) {
            thumbImage.setImageDrawable(thumbDrawable);
            this.mOverlay.add(thumbImage);
            width = Math.max(width, thumbDrawable.getIntrinsicWidth());
            height = Math.max(height, trackDrawable.getIntrinsicHeight());
        }
        if (thumbDrawable != null && (thumbDrawable.getIntrinsicWidth() <= 0 || thumbDrawable.getIntrinsicHeight() <= 0)) {
            int minWidth = res.getDimensionPixelSize(R.dimen.fastscroll_thumb_width);
            int minHeight = res.getDimensionPixelSize(R.dimen.fastscroll_thumb_height);
            thumbImage.setMinimumWidth(minWidth);
            thumbImage.setMinimumHeight(res.getDimensionPixelSize(R.dimen.fastscroll_thumb_height));
            width = Math.max(width, minWidth);
            height = Math.max(height, minHeight);
        }
        this.mWidth = width;
        this.mHeight = height;
        int previewSize = res.getDimensionPixelSize(R.dimen.fastscroll_overlay_size);
        this.mPreviewImage = new ImageView(context);
        this.mPreviewImage.setMinimumWidth(previewSize);
        this.mPreviewImage.setMinimumHeight(previewSize);
        this.mPreviewImage.setAlpha(0.0f);
        this.mOverlay.add(this.mPreviewImage);
        this.mPreviewPadding = res.getDimensionPixelSize(R.dimen.fastscroll_overlay_padding);
        int textMinSize = Math.max(0, previewSize - this.mPreviewPadding);
        this.mPrimaryText = createPreviewTextView(context, ta);
        this.mPrimaryText.setMinimumWidth(textMinSize);
        this.mPrimaryText.setMinimumHeight(textMinSize);
        this.mOverlay.add(this.mPrimaryText);
        this.mSecondaryText = createPreviewTextView(context, ta);
        this.mSecondaryText.setMinimumWidth(textMinSize);
        this.mSecondaryText.setMinimumHeight(textMinSize);
        this.mOverlay.add(this.mSecondaryText);
        this.mPreviewResId[0] = ta.getResourceId(3, 0);
        this.mPreviewResId[1] = ta.getResourceId(4, 0);
        this.mOverlayPosition = ta.getInt(5, 0);
        ta.recycle();
        this.mScrollBarStyle = listView.getScrollBarStyle();
        this.mScrollCompleted = true;
        this.mState = 1;
        this.mMatchDragPosition = context.getApplicationInfo().targetSdkVersion >= 11;
        getSectionsFromIndexer();
        refreshDrawablePressedState();
        updateLongList(listView.getChildCount(), listView.getCount());
        setScrollbarPosition(this.mList.getVerticalScrollbarPosition());
        postAutoHide();
    }

    public void remove() {
        this.mOverlay.remove(this.mTrackImage);
        this.mOverlay.remove(this.mThumbImage);
        this.mOverlay.remove(this.mPreviewImage);
        this.mOverlay.remove(this.mPrimaryText);
        this.mOverlay.remove(this.mSecondaryText);
    }

    public void setEnabled(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            onStateDependencyChanged();
        }
    }

    public boolean isEnabled() {
        return this.mEnabled && (this.mLongList || this.mAlwaysShow);
    }

    public void setAlwaysShow(boolean alwaysShow) {
        if (this.mAlwaysShow != alwaysShow) {
            this.mAlwaysShow = alwaysShow;
            onStateDependencyChanged();
        }
    }

    public boolean isAlwaysShowEnabled() {
        return this.mAlwaysShow;
    }

    private void onStateDependencyChanged() {
        if (!isEnabled()) {
            stop();
        } else if (isAlwaysShowEnabled()) {
            setState(1);
        } else if (this.mState == 1) {
            postAutoHide();
        }
        this.mList.resolvePadding();
    }

    public void setScrollBarStyle(int style) {
        if (this.mScrollBarStyle != style) {
            this.mScrollBarStyle = style;
            updateLayout();
        }
    }

    public void stop() {
        setState(0);
    }

    public void setScrollbarPosition(int position) {
        int i = 1;
        if (position == 0) {
            position = this.mList.isLayoutRtl() ? 1 : 2;
        }
        if (this.mScrollbarPosition != position) {
            boolean z;
            this.mScrollbarPosition = position;
            if (position != 1) {
                z = true;
            } else {
                z = false;
            }
            this.mLayoutFromRight = z;
            int[] iArr = this.mPreviewResId;
            if (!this.mLayoutFromRight) {
                i = 0;
            }
            this.mPreviewImage.setBackgroundResource(iArr[i]);
            Drawable background = this.mPreviewImage.getBackground();
            if (background != null) {
                Rect padding = this.mTempBounds;
                background.getPadding(padding);
                padding.offset(this.mPreviewPadding, this.mPreviewPadding);
                this.mPreviewImage.setPadding(padding.left, padding.top, padding.right, padding.bottom);
            }
            updateLayout();
        }
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateLayout();
    }

    public void onItemCountChanged(int totalItemCount) {
        int visibleItemCount = this.mList.getChildCount();
        if ((totalItemCount - visibleItemCount > 0) && this.mState != 2) {
            setThumbPos(getPosFromItemCount(this.mList.getFirstVisiblePosition(), visibleItemCount, totalItemCount));
        }
        updateLongList(visibleItemCount, totalItemCount);
    }

    private void updateLongList(int visibleItemCount, int totalItemCount) {
        boolean longList = visibleItemCount > 0 && totalItemCount / visibleItemCount >= 4;
        if (this.mLongList != longList) {
            this.mLongList = longList;
            onStateDependencyChanged();
        }
    }

    private TextView createPreviewTextView(Context context, TypedArray ta) {
        LayoutParams params = new LayoutParams(-2, -2);
        Resources res = context.getResources();
        int minSize = res.getDimensionPixelSize(R.dimen.fastscroll_overlay_size);
        ColorStateList textColor = ta.getColorStateList(0);
        float textSize = (float) res.getDimensionPixelSize(R.dimen.fastscroll_overlay_text_size);
        TextView textView = new TextView(context);
        textView.setLayoutParams(params);
        textView.setTextColor(textColor);
        textView.setTextSize(0, textSize);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.MIDDLE);
        textView.setGravity(17);
        textView.setAlpha(0.0f);
        textView.setLayoutDirection(this.mList.getLayoutDirection());
        return textView;
    }

    public void updateLayout() {
        if (!this.mUpdatingLayout) {
            this.mUpdatingLayout = true;
            updateContainerRect();
            layoutThumb();
            layoutTrack();
            Rect bounds = this.mTempBounds;
            measurePreview(this.mPrimaryText, bounds);
            applyLayout(this.mPrimaryText, bounds);
            measurePreview(this.mSecondaryText, bounds);
            applyLayout(this.mSecondaryText, bounds);
            if (this.mPreviewImage != null) {
                bounds.left -= this.mPreviewImage.getPaddingLeft();
                bounds.top -= this.mPreviewImage.getPaddingTop();
                bounds.right += this.mPreviewImage.getPaddingRight();
                bounds.bottom += this.mPreviewImage.getPaddingBottom();
                applyLayout(this.mPreviewImage, bounds);
            }
            this.mUpdatingLayout = false;
        }
    }

    private void applyLayout(View view, Rect bounds) {
        view.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
        view.setPivotX(this.mLayoutFromRight ? (float) (bounds.right - bounds.left) : 0.0f);
    }

    private void measurePreview(View v, Rect out) {
        Rect margins = this.mTempMargins;
        if (this.mPreviewImage != null) {
            margins.left = this.mPreviewImage.getPaddingLeft();
            margins.top = this.mPreviewImage.getPaddingTop();
            margins.right = this.mPreviewImage.getPaddingRight();
            margins.bottom = this.mPreviewImage.getPaddingBottom();
        }
        if (this.mOverlayPosition == 1) {
            measureViewToSide(v, this.mThumbImage, margins, out);
        } else {
            measureFloating(v, margins, out);
        }
    }

    private void measureViewToSide(View view, View adjacent, Rect margins, Rect out) {
        int marginLeft;
        int marginTop;
        int marginRight;
        int maxWidth;
        int right;
        int left;
        if (margins == null) {
            marginLeft = 0;
            marginTop = 0;
            marginRight = 0;
        } else {
            marginLeft = margins.left;
            marginTop = margins.top;
            marginRight = margins.right;
        }
        Rect container = this.mContainerRect;
        int containerWidth = container.width();
        if (adjacent == null) {
            maxWidth = containerWidth;
        } else if (this.mLayoutFromRight) {
            maxWidth = adjacent.getLeft();
        } else {
            maxWidth = containerWidth - adjacent.getRight();
        }
        view.measure(MeasureSpec.makeMeasureSpec((maxWidth - marginLeft) - marginRight, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(0, 0));
        int width = view.getMeasuredWidth();
        if (this.mLayoutFromRight) {
            right = (adjacent == null ? container.right : adjacent.getLeft()) - marginRight;
            left = right - width;
        } else {
            left = (adjacent == null ? container.left : adjacent.getRight()) + marginLeft;
            right = left + width;
        }
        int top = marginTop;
        out.set(left, top, right, top + view.getMeasuredHeight());
    }

    private void measureFloating(View preview, Rect margins, Rect out) {
        int marginLeft;
        int marginTop;
        int marginRight;
        if (margins == null) {
            marginLeft = 0;
            marginTop = 0;
            marginRight = 0;
        } else {
            marginLeft = margins.left;
            marginTop = margins.top;
            marginRight = margins.right;
        }
        Rect container = this.mContainerRect;
        int containerWidth = container.width();
        preview.measure(MeasureSpec.makeMeasureSpec((containerWidth - marginLeft) - marginRight, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(0, 0));
        int containerHeight = container.height();
        int width = preview.getMeasuredWidth();
        int top = ((containerHeight / 10) + marginTop) + container.top;
        int left = ((containerWidth - width) / 2) + container.left;
        Rect rect = out;
        rect.set(left, top, left + width, top + preview.getMeasuredHeight());
    }

    private void updateContainerRect() {
        AbsHorizontalListView list = this.mList;
        list.resolvePadding();
        Rect container = this.mContainerRect;
        container.left = 0;
        container.top = 0;
        container.right = list.getWidth();
        container.bottom = list.getHeight();
        int scrollbarStyle = this.mScrollBarStyle;
        if (scrollbarStyle == 16777216 || scrollbarStyle == 0) {
            container.left += list.getPaddingLeft();
            container.top += list.getPaddingTop();
            container.right -= list.getPaddingRight();
            container.bottom -= list.getPaddingBottom();
            if (scrollbarStyle == 16777216) {
                int width = getWidth();
                if (this.mScrollbarPosition == 2) {
                    container.right += width;
                } else {
                    container.left -= width;
                }
            }
        }
    }

    private void layoutThumb() {
        Rect bounds = this.mTempBounds;
        measureViewToSide(this.mThumbImage, null, null, bounds);
        applyLayout(this.mThumbImage, bounds);
    }

    private void layoutTrack() {
        int left = 0;
        View track = this.mTrackImage;
        View thumb = this.mThumbImage;
        Rect container = this.mContainerRect;
        track.measure(MeasureSpec.makeMeasureSpec(container.width(), Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(0, 0));
        int trackWidth = track.getMeasuredWidth();
        int thumbHalfHeight = thumb == null ? 0 : thumb.getHeight() / 2;
        if (thumb != null) {
            left = thumb.getLeft() + ((thumb.getWidth() - trackWidth) / 2);
        }
        track.layout(left, container.top + thumbHalfHeight, left + trackWidth, container.bottom - thumbHalfHeight);
    }

    private void setState(int state) {
        if (this.mList != null) {
            this.mList.removeCallbacks(this.mDeferHide);
        }
        if (this.mAlwaysShow && state == 0) {
            state = 1;
        }
        if (state != this.mState) {
            switch (state) {
                case 0:
                    transitionToHidden();
                    break;
                case 1:
                    transitionToVisible();
                    break;
                case 2:
                    if (!transitionPreviewLayout(this.mCurrentSection)) {
                        transitionToVisible();
                        break;
                    } else {
                        transitionToDragging();
                        break;
                    }
            }
            this.mState = state;
            refreshDrawablePressedState();
        }
    }

    private void refreshDrawablePressedState() {
        boolean isPressed = this.mState == 2;
        this.mThumbImage.setPressed(isPressed);
        this.mTrackImage.setPressed(isPressed);
    }

    private void transitionToHidden() {
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator fadeOut = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(300);
        Animator slideOut = groupAnimatorOfFloat(View.TRANSLATION_X, this.mLayoutFromRight ? (float) this.mThumbImage.getWidth() : (float) (-this.mThumbImage.getWidth()), this.mThumbImage, this.mTrackImage).setDuration(300);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{fadeOut, slideOut});
        this.mDecorAnimation.start();
        this.mShowingPreview = false;
    }

    private void transitionToVisible() {
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator fadeIn = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        Animator fadeOut = groupAnimatorOfFloat(View.ALPHA, 0.0f, this.mPreviewImage, this.mPrimaryText, this.mSecondaryText).setDuration(300);
        Animator slideIn = groupAnimatorOfFloat(View.TRANSLATION_X, 0.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{fadeIn, fadeOut, slideIn});
        this.mDecorAnimation.start();
        this.mShowingPreview = false;
    }

    private void transitionToDragging() {
        if (this.mDecorAnimation != null) {
            this.mDecorAnimation.cancel();
        }
        Animator fadeIn = groupAnimatorOfFloat(View.ALPHA, 1.0f, this.mThumbImage, this.mTrackImage, this.mPreviewImage).setDuration(150);
        Animator slideIn = groupAnimatorOfFloat(View.TRANSLATION_X, 0.0f, this.mThumbImage, this.mTrackImage).setDuration(150);
        this.mDecorAnimation = new AnimatorSet();
        this.mDecorAnimation.playTogether(new Animator[]{fadeIn, slideIn});
        this.mDecorAnimation.start();
        this.mShowingPreview = true;
    }

    private void postAutoHide() {
        if (this.mList != null) {
            this.mList.removeCallbacks(this.mDeferHide);
            this.mList.postDelayed(this.mDeferHide, FADE_TIMEOUT);
        }
    }

    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean hasMoreItems = false;
        if (isEnabled()) {
            if (totalItemCount - visibleItemCount > 0) {
                hasMoreItems = true;
            }
            if (hasMoreItems && this.mState != 2) {
                setThumbPos(getPosFromItemCount(firstVisibleItem, visibleItemCount, totalItemCount));
            }
            this.mScrollCompleted = true;
            if (this.mFirstVisibleItem != firstVisibleItem) {
                this.mFirstVisibleItem = firstVisibleItem;
                if (this.mState != 2) {
                    setState(1);
                    postAutoHide();
                    return;
                }
                return;
            }
            return;
        }
        setState(0);
    }

    private void getSectionsFromIndexer() {
        this.mSectionIndexer = null;
        Adapter adapter = this.mList.getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            this.mHeaderCount = ((HeaderViewListAdapter) adapter).getHeadersCount();
            adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        if (adapter instanceof ExpandableListConnector) {
            ExpandableListAdapter expAdapter = ((ExpandableListConnector) adapter).getAdapter();
            if (expAdapter instanceof SectionIndexer) {
                this.mSectionIndexer = (SectionIndexer) expAdapter;
                this.mListAdapter = (BaseAdapter) adapter;
                this.mSections = this.mSectionIndexer.getSections();
            }
        } else if (adapter instanceof SectionIndexer) {
            this.mListAdapter = (BaseAdapter) adapter;
            this.mSectionIndexer = (SectionIndexer) adapter;
            this.mSections = this.mSectionIndexer.getSections();
        } else {
            this.mListAdapter = (BaseAdapter) adapter;
            this.mSections = null;
        }
    }

    public void onSectionsChanged() {
        this.mListAdapter = null;
    }

    private void scrollTo(float position) {
        int sectionCount;
        int sectionIndex;
        this.mScrollCompleted = false;
        int count = this.mList.getCount();
        Object[] sections = this.mSections;
        if (sections == null) {
            sectionCount = 0;
        } else {
            sectionCount = sections.length;
        }
        if (this.mSectionIndexer == null || sections == null || sectionCount <= 1) {
            int index = MathUtils.constrain((int) (((float) count) * position), 0, count - 1);
            if (this.mList instanceof HorizontalListView) {
                ((HorizontalListView) this.mList).setSelectionFromTop(this.mHeaderCount + index, 0);
            } else {
                this.mList.setSelection(this.mHeaderCount + index);
            }
            sectionIndex = -1;
        } else {
            int exactSection = MathUtils.constrain((int) (((float) sectionCount) * position), 0, sectionCount - 1);
            int targetSection = exactSection;
            int targetIndex = this.mSectionIndexer.getPositionForSection(targetSection);
            sectionIndex = targetSection;
            int nextIndex = count;
            int prevIndex = targetIndex;
            int prevSection = targetSection;
            int nextSection = targetSection + 1;
            if (targetSection < sectionCount - 1) {
                nextIndex = this.mSectionIndexer.getPositionForSection(targetSection + 1);
            }
            if (nextIndex == targetIndex) {
                while (targetSection > 0) {
                    targetSection--;
                    prevIndex = this.mSectionIndexer.getPositionForSection(targetSection);
                    if (prevIndex == targetIndex) {
                        if (targetSection == 0) {
                            sectionIndex = 0;
                            break;
                        }
                    }
                    prevSection = targetSection;
                    sectionIndex = targetSection;
                    break;
                }
            }
            int nextNextSection = nextSection + 1;
            while (nextNextSection < sectionCount && this.mSectionIndexer.getPositionForSection(nextNextSection) == nextIndex) {
                nextNextSection++;
                nextSection++;
            }
            float prevPosition = ((float) prevSection) / ((float) sectionCount);
            float nextPosition = ((float) nextSection) / ((float) sectionCount);
            float snapThreshold = count == 0 ? AutoScrollHelper.NO_MAX : 0.125f / ((float) count);
            if (prevSection != exactSection || position - prevPosition >= snapThreshold) {
                targetIndex = prevIndex + ((int) ((((float) (nextIndex - prevIndex)) * (position - prevPosition)) / (nextPosition - prevPosition)));
            } else {
                targetIndex = prevIndex;
            }
            targetIndex = MathUtils.constrain(targetIndex, 0, count - 1);
            if (this.mList instanceof HorizontalListView) {
                ((HorizontalListView) this.mList).setSelectionFromTop(this.mHeaderCount + targetIndex, 0);
            } else {
                this.mList.setSelection(this.mHeaderCount + targetIndex);
            }
        }
        if (this.mCurrentSection != sectionIndex) {
            this.mCurrentSection = sectionIndex;
            boolean hasPreview = transitionPreviewLayout(sectionIndex);
            if (!this.mShowingPreview && hasPreview) {
                transitionToDragging();
            } else if (this.mShowingPreview && !hasPreview) {
                transitionToVisible();
            }
        }
    }

    private boolean transitionPreviewLayout(int sectionIndex) {
        TextView showing;
        View target;
        Object[] sections = this.mSections;
        String text = null;
        if (sections != null && sectionIndex >= 0 && sectionIndex < sections.length) {
            Object section = sections[sectionIndex];
            if (section != null) {
                text = section.toString();
            }
        }
        Rect bounds = this.mTempBounds;
        ImageView preview = this.mPreviewImage;
        if (this.mShowingPrimary) {
            showing = this.mPrimaryText;
            target = this.mSecondaryText;
        } else {
            showing = this.mSecondaryText;
            target = this.mPrimaryText;
        }
        target.setText((CharSequence) text);
        measurePreview(target, bounds);
        applyLayout(target, bounds);
        if (this.mPreviewAnimation != null) {
            this.mPreviewAnimation.cancel();
        }
        Animator showTarget = animateAlpha(target, 1.0f).setDuration(50);
        Animator hideShowing = animateAlpha(showing, 0.0f).setDuration(50);
        hideShowing.addListener(this.mSwitchPrimaryListener);
        bounds.left -= this.mPreviewImage.getPaddingLeft();
        bounds.top -= this.mPreviewImage.getPaddingTop();
        bounds.right += this.mPreviewImage.getPaddingRight();
        bounds.bottom += this.mPreviewImage.getPaddingBottom();
        Animator resizePreview = animateBounds(preview, bounds);
        resizePreview.setDuration(100);
        this.mPreviewAnimation = new AnimatorSet();
        Builder builder = this.mPreviewAnimation.play(hideShowing).with(showTarget);
        builder.with(resizePreview);
        int previewWidth = (preview.getWidth() - preview.getPaddingLeft()) - preview.getPaddingRight();
        int targetWidth = target.getWidth();
        if (targetWidth > previewWidth) {
            target.setScaleX(((float) previewWidth) / ((float) targetWidth));
            builder.with(animateScaleX(target, 1.0f).setDuration(100));
        } else {
            target.setScaleX(1.0f);
        }
        int showingWidth = showing.getWidth();
        if (showingWidth > targetWidth) {
            builder.with(animateScaleX(showing, ((float) targetWidth) / ((float) showingWidth)).setDuration(100));
        }
        this.mPreviewAnimation.start();
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        return true;
    }

    private void setThumbPos(float position) {
        Rect container = this.mContainerRect;
        int top = container.top;
        int bottom = container.bottom;
        ImageView trackImage = this.mTrackImage;
        ImageView thumbImage = this.mThumbImage;
        float min = (float) trackImage.getTop();
        float thumbMiddle = (position * (((float) trackImage.getBottom()) - min)) + min;
        thumbImage.setTranslationY(thumbMiddle - ((float) (thumbImage.getHeight() / 2)));
        float previewPos = this.mOverlayPosition == 1 ? thumbMiddle : 0.0f;
        ImageView previewImage = this.mPreviewImage;
        float previewHalfHeight = ((float) previewImage.getHeight()) / 2.0f;
        float previewTop = MathUtils.constrain(previewPos, ((float) top) + previewHalfHeight, ((float) bottom) - previewHalfHeight) - previewHalfHeight;
        previewImage.setTranslationY(previewTop);
        this.mPrimaryText.setTranslationY(previewTop);
        this.mSecondaryText.setTranslationY(previewTop);
    }

    private float getPosFromMotionEvent(float y) {
        Rect container = this.mContainerRect;
        int top = container.top;
        int bottom = container.bottom;
        ImageView trackImage = this.mTrackImage;
        float min = (float) trackImage.getTop();
        float offset = min;
        float range = ((float) trackImage.getBottom()) - min;
        if (range <= 0.0f) {
            return 0.0f;
        }
        return MathUtils.constrain((y - offset) / range, 0.0f, 1.0f);
    }

    private float getPosFromItemCount(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mSectionIndexer == null || this.mListAdapter == null) {
            getSectionsFromIndexer();
        }
        boolean hasSections = (this.mSectionIndexer == null || this.mSections == null || this.mSections.length <= 0) ? false : true;
        if (!hasSections || !this.mMatchDragPosition) {
            return ((float) firstVisibleItem) / ((float) (totalItemCount - visibleItemCount));
        }
        firstVisibleItem -= this.mHeaderCount;
        if (firstVisibleItem < 0) {
            return 0.0f;
        }
        float incrementalPos;
        int positionsInSection;
        float posWithinSection;
        totalItemCount -= this.mHeaderCount;
        View child = this.mList.getChildAt(0);
        if (child == null || child.getHeight() == 0) {
            incrementalPos = 0.0f;
        } else {
            incrementalPos = ((float) (this.mList.getPaddingTop() - child.getTop())) / ((float) child.getHeight());
        }
        int section = this.mSectionIndexer.getSectionForPosition(firstVisibleItem);
        int sectionPos = this.mSectionIndexer.getPositionForSection(section);
        int sectionCount = this.mSections.length;
        if (section < sectionCount - 1) {
            int nextSectionPos;
            if (section + 1 < sectionCount) {
                nextSectionPos = this.mSectionIndexer.getPositionForSection(section + 1);
            } else {
                nextSectionPos = totalItemCount - 1;
            }
            positionsInSection = nextSectionPos - sectionPos;
        } else {
            positionsInSection = totalItemCount - sectionPos;
        }
        if (positionsInSection == 0) {
            posWithinSection = 0.0f;
        } else {
            posWithinSection = ((((float) firstVisibleItem) + incrementalPos) - ((float) sectionPos)) / ((float) positionsInSection);
        }
        float result = (((float) section) + posWithinSection) / ((float) sectionCount);
        if (firstVisibleItem <= 0 || firstVisibleItem + visibleItemCount != totalItemCount) {
            return result;
        }
        View lastChild = this.mList.getChildAt(visibleItemCount - 1);
        return result + ((1.0f - result) * (((float) ((this.mList.getHeight() - this.mList.getPaddingBottom()) - lastChild.getTop())) / ((float) lastChild.getHeight())));
    }

    private void cancelFling() {
        MotionEvent cancelFling = MotionEvent.obtain(0, 0, 3, 0.0f, 0.0f, 0);
        if (this.mList != null) {
            this.mList.onTouchEvent(cancelFling);
        }
        cancelFling.recycle();
    }

    private void cancelPendingDrag() {
        if (this.mList != null) {
            this.mList.removeCallbacks(this.mDeferStartDrag);
        }
        this.mHasPendingDrag = false;
    }

    private void startPendingDrag() {
        this.mHasPendingDrag = true;
        this.mList.postDelayed(this.mDeferStartDrag, TAP_TIMEOUT);
    }

    private void beginDrag() {
        setState(2);
        if (this.mListAdapter == null && this.mList != null) {
            getSectionsFromIndexer();
        }
        if (this.mList != null) {
            this.mList.requestDisallowInterceptTouchEvent(true);
            this.mList.reportScrollStateChange(1);
        }
        cancelFling();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }
        switch (ev.getActionMasked()) {
            case 0:
                if (!isPointInside(ev.getX(), ev.getY())) {
                    return false;
                }
                if (this.mList.isInScrollingContainer()) {
                    this.mInitialTouchY = ev.getY();
                    startPendingDrag();
                    return false;
                }
                beginDrag();
                return true;
            case 1:
            case 3:
                cancelPendingDrag();
                return false;
            case 2:
                if (isPointInside(ev.getX(), ev.getY())) {
                    return false;
                }
                cancelPendingDrag();
                return false;
            default:
                return false;
        }
    }

    public boolean onInterceptHoverEvent(MotionEvent ev) {
        if (isEnabled()) {
            int actionMasked = ev.getActionMasked();
            if ((actionMasked == 9 || actionMasked == 7) && this.mState == 0 && isPointInside(ev.getX(), ev.getY())) {
                setState(1);
                postAutoHide();
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent me) {
        if (!isEnabled()) {
            return false;
        }
        float pos;
        switch (me.getActionMasked()) {
            case 1:
                if (this.mHasPendingDrag) {
                    beginDrag();
                    pos = getPosFromMotionEvent(me.getY());
                    setThumbPos(pos);
                    scrollTo(pos);
                    cancelPendingDrag();
                }
                if (this.mState != 2) {
                    return false;
                }
                if (this.mList != null) {
                    this.mList.requestDisallowInterceptTouchEvent(false);
                    this.mList.reportScrollStateChange(0);
                }
                setState(1);
                postAutoHide();
                return true;
            case 2:
                if (this.mHasPendingDrag && Math.abs(me.getY() - this.mInitialTouchY) > ((float) this.mScaledTouchSlop)) {
                    setState(2);
                    if (this.mListAdapter == null && this.mList != null) {
                        getSectionsFromIndexer();
                    }
                    if (this.mList != null) {
                        this.mList.requestDisallowInterceptTouchEvent(true);
                        this.mList.reportScrollStateChange(1);
                    }
                    cancelFling();
                    cancelPendingDrag();
                }
                if (this.mState != 2) {
                    return false;
                }
                pos = getPosFromMotionEvent(me.getY());
                setThumbPos(pos);
                if (this.mScrollCompleted) {
                    scrollTo(pos);
                }
                return true;
            case 3:
                cancelPendingDrag();
                return false;
            default:
                return false;
        }
    }

    private boolean isPointInside(float x, float y) {
        return isPointInsideX(x) && (this.mHasTrackImage || isPointInsideY(y));
    }

    private boolean isPointInsideX(float x) {
        if (this.mLayoutFromRight) {
            if (x >= ((float) this.mThumbImage.getLeft())) {
                return true;
            }
            return false;
        } else if (x > ((float) this.mThumbImage.getRight())) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isPointInsideY(float y) {
        float offset = this.mThumbImage.getTranslationY();
        return y >= ((float) this.mThumbImage.getTop()) + offset && y <= ((float) this.mThumbImage.getBottom()) + offset;
    }

    private static Animator groupAnimatorOfFloat(Property<View, Float> property, float value, View... views) {
        AnimatorSet animSet = new AnimatorSet();
        Builder builder = null;
        for (int i = views.length - 1; i >= 0; i--) {
            Animator anim = ObjectAnimator.ofFloat(views[i], property, new float[]{value});
            if (builder == null) {
                builder = animSet.play(anim);
            } else {
                builder.with(anim);
            }
        }
        return animSet;
    }

    private static Animator animateScaleX(View v, float target) {
        return ObjectAnimator.ofFloat(v, View.SCALE_X, new float[]{target});
    }

    private static Animator animateAlpha(View v, float alpha) {
        return ObjectAnimator.ofFloat(v, View.ALPHA, new float[]{alpha});
    }

    private static Animator animateBounds(View v, Rect bounds) {
        PropertyValuesHolder left = PropertyValuesHolder.ofInt(LEFT, new int[]{bounds.left});
        PropertyValuesHolder top = PropertyValuesHolder.ofInt(TOP, new int[]{bounds.top});
        PropertyValuesHolder right = PropertyValuesHolder.ofInt(RIGHT, new int[]{bounds.right});
        PropertyValuesHolder bottom = PropertyValuesHolder.ofInt(BOTTOM, new int[]{bounds.bottom});
        return ObjectAnimator.ofPropertyValuesHolder(v, new PropertyValuesHolder[]{left, top, right, bottom});
    }
}
