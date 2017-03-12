package com.samsung.android.smartclip;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipDataElement;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTagArray;

public class SmartClipDataElementImpl implements SlookSmartClipDataElement {
    public static final int EXTRACTION_LEVEL_0 = 0;
    public static final int EXTRACTION_LEVEL_1 = 1;
    public static final int EXTRACTION_MODE_DRAG_AND_DROP = 2;
    public static final int EXTRACTION_MODE_NORMAL = 0;
    protected static final String TAG = "SmartClipDataElementImpl";
    protected SmartClipDataElementImpl mFirstChild;
    protected int mId;
    protected SmartClipDataElementImpl mLastChild;
    protected SmartClipDataElementImpl mNextSibling;
    protected SmartClipDataElementImpl mParent;
    protected SmartClipDataElementImpl mPrevSibling;
    protected Rect mRectOnScreen;
    protected SmartClipDataRepositoryImpl mRepository;
    public SmartClipMetaTagArrayImpl mTags;
    protected View mView;

    public void setMetaAreaRect(Rect rect) {
        this.mRectOnScreen = rect;
    }

    public Rect getMetaAreaRect() {
        return this.mRectOnScreen;
    }

    public SlookSmartClipMetaTagArray getAllTag() {
        if (this.mTags == null) {
            return new SmartClipMetaTagArrayImpl();
        }
        return this.mTags.getCopy();
    }

    public SlookSmartClipMetaTagArray getTagTable() {
        return this.mTags;
    }

    public boolean setTag(SlookSmartClipMetaTag metaTag) {
        if (metaTag == null) {
            return false;
        }
        if (this.mTags == null) {
            this.mTags = new SmartClipMetaTagArrayImpl();
        }
        if (metaTag.getType() == null) {
            return false;
        }
        this.mTags.removeTag(metaTag.getType());
        this.mTags.add(metaTag);
        return true;
    }

    public SlookSmartClipMetaTagArray getTag(String tagType) {
        if (this.mTags == null) {
            return new SmartClipMetaTagArrayImpl();
        }
        return this.mTags.getTag(tagType);
    }

    public void removeTag(String tagType) {
        if (this.mTags != null) {
            this.mTags.removeTag(tagType);
        }
    }

    public void addTag(SlookSmartClipMetaTag metaTag) {
        if (metaTag != null) {
            if (this.mTags == null) {
                this.mTags = new SmartClipMetaTagArrayImpl();
            }
            if (SmartClipMetaUtils.isValidMetaTag(metaTag)) {
                this.mTags.add(metaTag);
            }
        }
    }

    public void addTag(SmartClipMetaTagArrayImpl tagSet) {
        if (this.mTags == null) {
            this.mTags = new SmartClipMetaTagArrayImpl();
        }
        this.mTags.addAll(tagSet);
    }

    public void setTagTable(SmartClipMetaTagArrayImpl tagsArray) {
        this.mTags = tagsArray;
    }

    public SmartClipDataElementImpl() {
        this.mId = -1;
        this.mRectOnScreen = null;
        this.mView = null;
        this.mRepository = null;
        this.mTags = null;
        this.mParent = null;
        this.mFirstChild = null;
        this.mLastChild = null;
        this.mNextSibling = null;
        this.mPrevSibling = null;
    }

    public SmartClipDataElementImpl(SmartClipDataRepositoryImpl repository) {
        this.mId = -1;
        this.mRectOnScreen = null;
        this.mView = null;
        this.mRepository = null;
        this.mTags = null;
        this.mParent = null;
        this.mFirstChild = null;
        this.mLastChild = null;
        this.mNextSibling = null;
        this.mPrevSibling = null;
        this.mRepository = repository;
    }

    public SmartClipDataElementImpl(SmartClipDataRepositoryImpl repository, View view) {
        this(repository);
        this.mView = view;
    }

    public SmartClipDataElementImpl(SmartClipDataRepositoryImpl repository, Rect screenRect) {
        this(repository);
        this.mRectOnScreen = new Rect(screenRect);
    }

    public SmartClipDataElementImpl(SmartClipDataRepositoryImpl repository, View view, Rect screenRect) {
        this(repository, view);
        this.mRectOnScreen = new Rect(screenRect);
    }

    public void clearMetaData() {
        this.mRectOnScreen = null;
        setTagTable(null);
    }

    public void setView(View view) {
        this.mView = view;
    }

    public View getView() {
        return this.mView;
    }

    public void setDataRepository(SmartClipDataRepositoryImpl repository) {
        this.mRepository = repository;
    }

    public SmartClipDataRepositoryImpl getDataRepository() {
        return this.mRepository;
    }

    public int getExtractionMode() {
        if (this.mRepository == null) {
            return 0;
        }
        SmartClipDataCropperImpl cropper = (SmartClipDataCropperImpl) this.mRepository.getSmartClipDataCropper();
        if (cropper != null) {
            return cropper.getExtractionMode();
        }
        return 0;
    }

    public int getExtractionLevel() {
        if (this.mRepository == null) {
            return 0;
        }
        SmartClipDataCropperImpl cropper = (SmartClipDataCropperImpl) this.mRepository.getSmartClipDataCropper();
        if (cropper != null) {
            return cropper.getExtractionLevel();
        }
        return 0;
    }

    public boolean isEmptyTag(boolean includeChild) {
        if (includeChild) {
            SmartClipDataElementImpl element = this;
            while (element != null) {
                if (element.mTags != null && element.mTags.size() > 0) {
                    return false;
                }
                element = element.traverseNextElement(this);
            }
            return true;
        } else if (this.mTags == null || this.mTags.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public SlookSmartClipDataElement createChildInstance() {
        SlookSmartClipDataElement newElement = newInstance();
        addChild(newElement);
        return newElement;
    }

    public SlookSmartClipDataElement newInstance() {
        return new SmartClipDataElementImpl(this.mRepository);
    }

    public boolean addChild(SlookSmartClipDataElement childToAdd) {
        if (childToAdd == null) {
            return false;
        }
        SmartClipDataElementImpl child = (SmartClipDataElementImpl) childToAdd;
        if (this.mFirstChild == null) {
            this.mFirstChild = child;
            this.mLastChild = child;
            child.setNextSibling(null);
            child.setPrevSibling(null);
            child.setParent(this);
            return true;
        } else if (this.mLastChild == null) {
            return false;
        } else {
            SmartClipDataElementImpl lastChild = this.mLastChild;
            this.mLastChild = child;
            lastChild.setNextSibling(child);
            child.setPrevSibling(lastChild);
            child.setParent(this);
            return true;
        }
    }

    public boolean removeChild(SlookSmartClipDataElement childToRemove) {
        if (childToRemove == null) {
            return false;
        }
        SmartClipDataElementImpl child = (SmartClipDataElementImpl) childToRemove;
        if (child.getParent() != this) {
            Log.e(TAG, "removeChild : Incorrect parent of SlookSmartClipDataElement. element=" + child);
            child.dump();
            return false;
        }
        if (this.mFirstChild == child) {
            this.mFirstChild = child.getNextSibling();
        }
        if (this.mLastChild == child) {
            this.mLastChild = child.getPrevSibling();
        }
        if (child.getPrevSibling() != null) {
            child.getPrevSibling().setNextSibling(child.getNextSibling());
        }
        if (child.getNextSibling() != null) {
            child.getNextSibling().setPrevSibling(child.getPrevSibling());
        }
        return true;
    }

    private void setPrevSibling(SmartClipDataElementImpl sibling) {
        this.mPrevSibling = sibling;
    }

    private void setNextSibling(SmartClipDataElementImpl sibling) {
        this.mNextSibling = sibling;
    }

    private void setParent(SmartClipDataElementImpl parent) {
        this.mParent = parent;
    }

    public SmartClipDataElementImpl getParent() {
        return this.mParent;
    }

    public SmartClipDataElementImpl getFirstChild() {
        return this.mFirstChild;
    }

    public SmartClipDataElementImpl getLastChild() {
        return this.mLastChild;
    }

    public SmartClipDataElementImpl getNextSibling() {
        return this.mNextSibling;
    }

    public SmartClipDataElementImpl getPrevSibling() {
        return this.mPrevSibling;
    }

    public int getChildCount() {
        int childCount = 0;
        for (SmartClipDataElementImpl element = this.mFirstChild; element != null; element = element.getNextSibling()) {
            childCount++;
        }
        return childCount;
    }

    public int getParentCount() {
        int parentCount = 0;
        for (SmartClipDataElementImpl element = getParent(); element != null; element = element.getParent()) {
            parentCount++;
        }
        return parentCount;
    }

    public SmartClipDataElementImpl traverseNextElement(SmartClipDataElementImpl topMostNodeOfTraverse) {
        if (this.mFirstChild != null) {
            return this.mFirstChild;
        }
        if (this == topMostNodeOfTraverse) {
            return null;
        }
        if (this.mNextSibling != null) {
            return this.mNextSibling;
        }
        SmartClipDataElementImpl n = this;
        while (n != null && n.mNextSibling == null && (topMostNodeOfTraverse == null || n.mParent != topMostNodeOfTraverse)) {
            n = n.mParent;
        }
        if (n != null) {
            return n.mNextSibling;
        }
        return null;
    }

    public String getDumpString(boolean addIndent, boolean showValue) {
        int i;
        String result = new String();
        int parentCount = getParentCount();
        if (addIndent) {
            for (i = 0; i < parentCount; i++) {
                result = result + "\t";
            }
        }
        if (this.mRectOnScreen != null) {
            result = result + "Rect(" + this.mRectOnScreen.left + ", " + this.mRectOnScreen.top + ", " + this.mRectOnScreen.right + ", " + this.mRectOnScreen.bottom + ")\t";
        } else {
            result = result + "mRectOnScreen(NULL)\t";
        }
        if (this.mView != null) {
            result = result + this.mView.getClass().getSimpleName();
            int resId = this.mView.getId();
            if (resId == -1 || resId < 0) {
                result = result + "@" + Integer.toHexString(this.mView.hashCode()) + "\t";
            } else {
                try {
                    result = result + "/" + this.mView.getResources().getResourceEntryName(resId) + "\t";
                } catch (Exception e) {
                    result = result + "@" + Integer.toHexString(this.mView.hashCode()) + "\t";
                }
            }
            Drawable background = this.mView.getBackground();
            if (!(background == null || !background.isVisible() || background.getOpacity() == -2)) {
                result = result + "Opacity BG(" + background.getOpacity() + ")\t";
            }
        }
        if (this.mTags == null) {
            return result + "No meta tag\t";
        }
        int tagCount = this.mTags.size();
        for (i = 0; i < tagCount; i++) {
            SlookSmartClipMetaTag tag = (SlookSmartClipMetaTag) this.mTags.get(i);
            String type = tag.getType();
            String value = tag.getValue();
            String extra = "";
            if (value == null) {
                value = new String("null");
            }
            if (tag instanceof SmartClipMetaTagImpl) {
                SmartClipMetaTagImpl tagImpl = (SmartClipMetaTagImpl) tag;
                if (tagImpl.getExtraData() != null) {
                    extra = ", Extra data size = " + tagImpl.getExtraData().length;
                }
                if (tagImpl.getParcelableData() != null) {
                    extra = (extra + ", Extra parcelable = ") + tagImpl.getParcelableData().toString();
                }
            }
            if (showValue) {
                result = result + type + "(" + value + extra + ")\t";
            } else {
                result = result + type + "\t";
            }
        }
        return result;
    }

    public boolean dump() {
        Log.e(TAG, getDumpString(true, true));
        for (SmartClipDataElementImpl element = getFirstChild(); element != null; element = element.getNextSibling()) {
            element.dump();
        }
        return true;
    }
}
