package com.samsung.android.smartclip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTag;
import com.samsung.android.sdk.look.smartclip.SlookSmartClipMetaTagArray;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmartClipDataRepositoryImpl implements SmartClipDataRepository, Parcelable {
    public static final String CONTENT_TYPE_AUDIO = "music";
    public static final String CONTENT_TYPE_DEFAULT = "image";
    public static final String CONTENT_TYPE_IMAGE = "image";
    public static final String CONTENT_TYPE_NAMECARD = "namecard";
    public static final String CONTENT_TYPE_VIDEO = "video";
    public static final String CONTENT_TYPE_WEB = "web";
    public static final String CONTENT_TYPE_YOUTUBE = "youtube";
    public static final Creator<SmartClipDataRepositoryImpl> CREATOR = new Creator<SmartClipDataRepositoryImpl>() {
        public SmartClipDataRepositoryImpl createFromParcel(Parcel in) {
            Log.d(SmartClipDataRepositoryImpl.TAG, "SmartClipDataRepositoryImpl.createFromParcel called");
            SmartClipDataRepositoryImpl data = new SmartClipDataRepositoryImpl(null);
            data.readFromParcel(in);
            return data;
        }

        public SmartClipDataRepositoryImpl[] newArray(int size) {
            return new SmartClipDataRepositoryImpl[size];
        }
    };
    protected static final String FIELD_CAPTURED_IMAGE_PATH = "captured_image_path";
    protected static final String FIELD_CAPTURED_IMAGE_STYLE = "captured_image_style";
    protected static final String FIELD_CONTENT_RECT = "content_rect";
    protected static final String FIELD_CONTENT_TYPE = "content_type";
    protected static final String FIELD_META_TAGS = "meta_tags";
    protected static final String FIELD_META_TAG_EXTRA_DATA = "meta_tag_extra_value";
    protected static final String FIELD_META_TAG_TYPE = "meta_tag_type";
    protected static final String FIELD_META_TAG_VALUE = "meta_tag_value";
    protected static final String FIELD_REPOSITORY_ID = "repository_id";
    public static final int IMAGE_STYLE_LASSO = 0;
    public static final int IMAGE_STYLE_PIN_MODE = 3;
    public static final int IMAGE_STYLE_RECTANGLE = 1;
    public static final int IMAGE_STYLE_SEGMENTATION = 2;
    protected static final String TAG = "SmartClipDataRepositoryImpl";
    protected String mAppPackageName;
    protected String mCapturedImageFilePath;
    protected int mCapturedImageFileStyle;
    protected Rect mContentRect;
    protected String mContentType;
    protected SmartClipDataCropper mCropper;
    private int mPenWindowBorder;
    protected String mRepositoryId;
    protected SmartClipDataRootElement mRootElement;
    private RectF mScaleRect;
    protected SmartClipMetaTagArrayImpl mTags;
    protected int mTargetWindowLayer;
    private Rect mWinFrameRect;

    public SmartClipDataRepositoryImpl() {
        this(null);
    }

    public SmartClipDataRepositoryImpl(SmartClipDataCropper cropper) {
        this(cropper, new Rect(0, 0, 0, 0), new RectF(0.0f, 0.0f, 1.0f, 1.0f));
    }

    public SmartClipDataRepositoryImpl(SmartClipDataCropper cropper, Rect winFrameRect, RectF scaleRect) {
        this(cropper, new Rect(0, 0, 0, 0), new RectF(0.0f, 0.0f, 1.0f, 1.0f), 0);
    }

    public SmartClipDataRepositoryImpl(SmartClipDataCropper cropper, Rect winFrameRect, RectF scaleRect, int penWindowBorderWidth) {
        this.mRootElement = new SmartClipDataRootElement();
        this.mContentType = null;
        this.mContentRect = null;
        this.mTags = null;
        this.mCropper = null;
        this.mCapturedImageFilePath = null;
        this.mCapturedImageFileStyle = 1;
        this.mAppPackageName = null;
        this.mTargetWindowLayer = -1;
        this.mWinFrameRect = null;
        this.mScaleRect = null;
        this.mPenWindowBorder = 0;
        this.mCropper = cropper;
        this.mWinFrameRect = new Rect(winFrameRect);
        this.mScaleRect = new RectF(scaleRect);
        this.mPenWindowBorder = penWindowBorderWidth;
    }

    public SmartClipDataCropper getSmartClipDataCropper() {
        return this.mCropper;
    }

    public String getCapturedImageFilePath() {
        return this.mCapturedImageFilePath;
    }

    public int getCapturedImageFileStyle() {
        return this.mCapturedImageFileStyle;
    }

    public String getAppPackageName() {
        return this.mAppPackageName;
    }

    public void setCapturedImage(String capturedImageFilePath) {
        this.mCapturedImageFilePath = capturedImageFilePath;
        this.mCapturedImageFileStyle = 1;
    }

    public void setCapturedImage(String capturedImageFilePath, int imageStyle) {
        this.mCapturedImageFilePath = capturedImageFilePath;
        this.mCapturedImageFileStyle = imageStyle;
    }

    public void setCapturedImage(String capturedImageFilePath, Bitmap capturedImageBitmap) {
        setCapturedImage(capturedImageFilePath);
    }

    public void setAppPackageName(String packageName) {
        this.mAppPackageName = packageName;
    }

    public boolean determineContentType() {
        String contentType;
        boolean bHaveBrowserView = false;
        boolean bHaveYoutubeView = false;
        boolean bHaveAudioFilePath = false;
        boolean bHaveVideoFilePath = false;
        boolean bHaveImageFilePath = false;
        SmartClipDataElementImpl element = this.mRootElement;
        while (element != null) {
            View view = element.getView();
            if (view != null) {
                boolean bHaveUrlTag = false;
                Iterator i$ = getMetaTag("url").iterator();
                while (i$.hasNext()) {
                    String url = ((SlookSmartClipMetaTag) i$.next()).getValue();
                    if (url != null && !url.isEmpty()) {
                        bHaveUrlTag = true;
                        break;
                    }
                }
                if (getMetaTag(SmartClipMetaTagType.FILE_PATH_AUDIO).size() > 0) {
                    bHaveAudioFilePath = true;
                }
                if (getMetaTag(SmartClipMetaTagType.FILE_PATH_VIDEO).size() > 0) {
                    bHaveVideoFilePath = true;
                }
                if (getMetaTag(SmartClipMetaTagType.FILE_PATH_IMAGE).size() > 0) {
                    bHaveImageFilePath = true;
                }
                if (bHaveUrlTag) {
                    if ((view instanceof WebView) || view.getClass().getName().equals("android.webkitsec.WebView") || view.getClass().getName().equals("org.chromium.content.browser.ChromeView") || view.getClass().getName().equals("org.samsung.content.sbrowser.SbrContentView") || view.getClass().getName().equals("com.sec.chromium.content.browser.SbrContentView") || view.getClass().getName().equals("org.chromium.content.browser.JellyBeanContentView")) {
                        bHaveBrowserView = true;
                    } else if (this.mAppPackageName != null && this.mAppPackageName.equals("com.google.android.youtube") && view.getClass().getName().endsWith("PlayerView")) {
                        bHaveYoutubeView = true;
                    }
                }
                if (getMetaTag(SmartClipMetaTagType.HTML).size() > 0) {
                    bHaveBrowserView = true;
                }
            }
            element = element.traverseNextElement(this.mRootElement);
        }
        if (bHaveAudioFilePath) {
            contentType = CONTENT_TYPE_AUDIO;
        } else if (bHaveVideoFilePath) {
            contentType = CONTENT_TYPE_VIDEO;
        } else if (bHaveImageFilePath) {
            contentType = "image";
        } else if (bHaveYoutubeView) {
            contentType = CONTENT_TYPE_YOUTUBE;
        } else if (bHaveBrowserView) {
            contentType = CONTENT_TYPE_WEB;
        } else {
            contentType = "image";
        }
        this.mContentType = contentType;
        return true;
    }

    public SmartClipDataRootElement getRootElement() {
        return this.mRootElement;
    }

    public SmartClipDataElementImpl getTopmostElement() {
        return this.mRootElement;
    }

    public String getContentType() {
        return this.mContentType;
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    public String getRepositoryId() {
        return this.mRepositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.mRepositoryId = repositoryId;
    }

    public int getWindowLayer() {
        return this.mTargetWindowLayer;
    }

    public void setWindowLayer(int layer) {
        this.mTargetWindowLayer = layer;
    }

    public String getMergedPlainTextTag() {
        if (this.mRootElement == null) {
            return null;
        }
        return this.mRootElement.collectPlainTextTag();
    }

    public Rect getContentRect() {
        if (this.mContentRect != null) {
            return this.mContentRect;
        }
        SmartClipDataElementImpl element = this.mRootElement;
        Rect contentRect = new Rect(99999, 99999, 0, 0);
        while (element != null) {
            if (element.getChildCount() != 1) {
                Rect rect;
                if (element.getChildCount() > 1) {
                    for (SmartClipDataElementImpl child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
                        rect = child.getMetaAreaRect();
                        if (rect != null) {
                            if (contentRect.left > rect.left && rect.width() > 0) {
                                contentRect.left = rect.left;
                            }
                            if (contentRect.top > rect.top && rect.height() > 0) {
                                contentRect.top = rect.top;
                            }
                            if (contentRect.right < rect.right && rect.width() > 0) {
                                contentRect.right = rect.right;
                            }
                            if (contentRect.bottom < rect.bottom && rect.height() > 0) {
                                contentRect.bottom = rect.bottom;
                            }
                        }
                    }
                } else {
                    rect = element.getMetaAreaRect();
                    if (rect != null) {
                        if (contentRect.left > rect.left && rect.width() > 0) {
                            contentRect.left = rect.left;
                        }
                        if (contentRect.top > rect.top && rect.height() > 0) {
                            contentRect.top = rect.top;
                        }
                        if (contentRect.right < rect.right && rect.width() > 0) {
                            contentRect.right = rect.right;
                        }
                        if (contentRect.bottom < rect.bottom && rect.height() > 0) {
                            contentRect.bottom = rect.bottom;
                        }
                    }
                }
            }
            element = element.traverseNextElement(this.mRootElement);
        }
        if (contentRect.left > contentRect.right) {
            return new Rect();
        }
        if (this.mScaleRect.width() == 1.0f && this.mScaleRect.height() == 1.0f) {
            return contentRect;
        }
        float hScale = this.mScaleRect.width();
        float vScale = this.mScaleRect.height();
        if (hScale == 0.0f || vScale == 0.0f) {
            return contentRect;
        }
        Rect unScaledWinFrame = new Rect();
        unScaledWinFrame.left = this.mWinFrameRect.left;
        unScaledWinFrame.top = this.mWinFrameRect.top;
        unScaledWinFrame.right = (int) ((((float) this.mWinFrameRect.left) + (((float) this.mWinFrameRect.width()) / hScale)) + 0.5f);
        unScaledWinFrame.bottom = (int) ((((float) this.mWinFrameRect.top) + (((float) this.mWinFrameRect.height()) / vScale)) + 0.5f);
        if (this.mPenWindowBorder > 0) {
            if (contentRect.left < this.mPenWindowBorder) {
                contentRect.left += this.mPenWindowBorder;
            }
            if (contentRect.right > unScaledWinFrame.width() - this.mPenWindowBorder) {
                contentRect.right -= this.mPenWindowBorder;
            }
            if (contentRect.top < this.mPenWindowBorder) {
                contentRect.top += this.mPenWindowBorder;
            }
            if (contentRect.bottom > unScaledWinFrame.height() - this.mPenWindowBorder) {
                contentRect.bottom -= this.mPenWindowBorder;
            }
        }
        int contentWidth = contentRect.width();
        int contentHeight = contentRect.height();
        contentRect.left = unScaledWinFrame.left + Math.round((((float) contentRect.left) * hScale) + 0.5f);
        contentRect.top = unScaledWinFrame.top + Math.round((((float) contentRect.top) * vScale) + 0.5f);
        contentRect.right = contentRect.left + Math.round((((float) contentWidth) * hScale) - 0.5f);
        contentRect.bottom = contentRect.top + Math.round((((float) contentHeight) * vScale) - 0.5f);
        return contentRect;
    }

    public SmartClipMetaTagArrayImpl getAllMetaTagList() {
        if (this.mTags != null) {
            return this.mTags;
        }
        SmartClipMetaTagArrayImpl metaList = new SmartClipMetaTagArrayImpl();
        for (SmartClipDataElementImpl element = this.mRootElement; element != null; element = element.traverseNextElement(null)) {
            SmartClipMetaTagArrayImpl tags = (SmartClipMetaTagArrayImpl) element.getTagTable();
            if (tags != null) {
                int tagCount = tags.size();
                for (int i = 0; i < tagCount; i++) {
                    SlookSmartClipMetaTag curTag = (SlookSmartClipMetaTag) tags.get(i);
                    if (!curTag.getType().equals("plain_text")) {
                        metaList.add(curTag);
                    }
                }
            }
        }
        String plainText = getMergedPlainTextTag();
        if (plainText == null) {
            return metaList;
        }
        metaList.add(new SmartClipMetaTagImpl("plain_text", plainText));
        return metaList;
    }

    public SlookSmartClipMetaTagArray getAllMetaTag() {
        return getAllMetaTagList();
    }

    public SlookSmartClipMetaTagArray getMetaTag(String tagType) {
        SmartClipMetaTagArrayImpl metaList = new SmartClipMetaTagArrayImpl();
        int tagCount;
        int i;
        if (this.mTags != null) {
            tagCount = this.mTags.size();
            for (i = 0; i < tagCount; i++) {
                if (((SlookSmartClipMetaTag) this.mTags.get(i)).getType().equals(tagType)) {
                    metaList.add(this.mTags.get(i));
                }
            }
        } else if (tagType.equals("plain_text")) {
            String plainText = getMergedPlainTextTag();
            if (plainText != null) {
                metaList.add(new SmartClipMetaTagImpl("plain_text", plainText));
            }
        } else {
            for (SmartClipDataElementImpl element = this.mRootElement; element != null; element = element.traverseNextElement(null)) {
                SmartClipMetaTagArrayImpl tags = (SmartClipMetaTagArrayImpl) element.getTagTable();
                if (tags != null) {
                    tagCount = tags.size();
                    for (i = 0; i < tagCount; i++) {
                        SlookSmartClipMetaTag curTag = (SlookSmartClipMetaTag) tags.get(i);
                        if (curTag.getValue() != null && curTag.getType().equals(tagType)) {
                            metaList.add(curTag);
                        }
                    }
                }
            }
        }
        return metaList;
    }

    public SlookSmartClipMetaTagArray extractMetaTagFromString(Context context, String srcString) {
        return null;
    }

    public boolean dump(boolean dumpMetaTags) {
        Log.d(TAG, "----- Start of SmartClip repository informations -----");
        Log.d(TAG, "** Content type : " + getContentType());
        Log.d(TAG, "** Meta area rect : " + getContentRect().toString());
        Log.d(TAG, "** Captured image file path : " + this.mCapturedImageFilePath);
        if (dumpMetaTags) {
            Log.d(TAG, "** mTags");
            if (this.mTags != null) {
                this.mTags.dump();
            } else {
                Log.d(TAG, "mTags is null");
            }
            Log.d(TAG, "** Element tree **");
            if (this.mRootElement != null) {
                this.mRootElement.dump();
            }
        }
        Log.d(TAG, "----- End of SmartClip repository informations -----");
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        if (this.mContentType == null) {
            determineContentType();
        }
        out.writeString(this.mContentType);
        out.writeString(this.mRepositoryId);
        this.mContentRect = getContentRect();
        out.writeParcelable(this.mContentRect, flags);
        out.writeString(this.mCapturedImageFilePath);
        out.writeInt(this.mCapturedImageFileStyle);
        out.writeString(this.mAppPackageName);
        out.writeInt(this.mTargetWindowLayer);
        this.mTags = getAllMetaTagList();
        out.writeParcelable(this.mTags, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mContentType = in.readString();
        this.mRepositoryId = in.readString();
        this.mContentRect = (Rect) in.readParcelable(Rect.class.getClassLoader());
        this.mCapturedImageFilePath = in.readString();
        this.mCapturedImageFileStyle = in.readInt();
        this.mAppPackageName = in.readString();
        this.mTargetWindowLayer = in.readInt();
        SmartClipMetaTagArrayImpl listArray = new SmartClipMetaTagArrayImpl();
        this.mTags = (SmartClipMetaTagArrayImpl) in.readParcelable(SmartClipMetaTagImpl.class.getClassLoader());
    }

    public static SmartClipDataRepositoryImpl createRepositoryFromString(String jsonStr) {
        SmartClipDataRepositoryImpl repository = new SmartClipDataRepositoryImpl(null);
        JSONObject json_repository = new JSONObject(jsonStr);
        if (json_repository.has(FIELD_CONTENT_TYPE)) {
            repository.mContentType = json_repository.getString(FIELD_CONTENT_TYPE);
        }
        if (json_repository.has(FIELD_REPOSITORY_ID)) {
            repository.mRepositoryId = json_repository.getString(FIELD_REPOSITORY_ID);
        }
        if (json_repository.has(FIELD_CONTENT_RECT)) {
            JSONArray json_rect = json_repository.getJSONArray(FIELD_CONTENT_RECT);
            repository.mContentRect = new Rect(json_rect.getInt(0), json_rect.getInt(1), json_rect.getInt(2), json_rect.getInt(3));
        }
        if (json_repository.has(FIELD_CAPTURED_IMAGE_PATH)) {
            String capturedImageFilePath = json_repository.getString(FIELD_CAPTURED_IMAGE_PATH);
            int capturedImageFileStyle = json_repository.getInt(FIELD_CAPTURED_IMAGE_STYLE);
            if (capturedImageFilePath != null) {
                repository.setCapturedImage(capturedImageFilePath, capturedImageFileStyle);
            }
        }
        if (!json_repository.has(FIELD_META_TAGS)) {
            return repository;
        }
        repository.mTags = new SmartClipMetaTagArrayImpl();
        JSONArray json_tagArray = json_repository.getJSONArray(FIELD_META_TAGS);
        int tagCount = json_tagArray.length();
        for (int i = 0; i < tagCount; i++) {
            SlookSmartClipMetaTag metaTag;
            JSONObject json_tag = json_tagArray.getJSONObject(i);
            String tagType = json_tag.getString(FIELD_META_TAG_TYPE);
            String tagValue = "";
            try {
                tagValue = json_tag.getString(FIELD_META_TAG_VALUE);
            } catch (JSONException e) {
                Log.e(TAG, "There is no meta value! type=" + tagType);
            }
            try {
                byte[] extraData = Base64.decode(json_tag.getString(FIELD_META_TAG_EXTRA_DATA), 0);
                Log.d(TAG, "Decoding : Length of extra data = " + extraData.length);
                metaTag = new SmartClipMetaTagImpl(tagType, tagValue, extraData);
            } catch (JSONException e2) {
                try {
                    metaTag = new SlookSmartClipMetaTag(tagType, tagValue);
                } catch (JSONException e3) {
                    Log.e(TAG, "JSONException throwed : " + e3.toString());
                    e3.printStackTrace();
                    return null;
                }
            }
            repository.mTags.addTag(metaTag);
        }
        return repository;
    }

    public String encodeRepositoryToString() {
        try {
            JSONObject json_repository = new JSONObject();
            if (getContentType() != null) {
                json_repository.put(FIELD_CONTENT_TYPE, getContentType());
            }
            if (getRepositoryId() != null) {
                json_repository.put(FIELD_REPOSITORY_ID, getRepositoryId());
            }
            Rect contentRect = getContentRect();
            if (contentRect != null) {
                JSONArray json_rect = new JSONArray();
                json_rect.put(0, contentRect.left);
                json_rect.put(1, contentRect.top);
                json_rect.put(2, contentRect.right);
                json_rect.put(3, contentRect.bottom);
                json_repository.put(FIELD_CONTENT_RECT, json_rect);
            }
            String capturedImageFilePath = getCapturedImageFilePath();
            int capturedImageFileStyle = getCapturedImageFileStyle();
            if (capturedImageFilePath != null) {
                json_repository.put(FIELD_CAPTURED_IMAGE_PATH, capturedImageFilePath);
                json_repository.put(FIELD_CAPTURED_IMAGE_STYLE, capturedImageFileStyle);
            }
            SmartClipMetaTagArrayImpl tagArray = getAllMetaTagList();
            if (tagArray != null) {
                JSONArray json_tagArray = new JSONArray();
                Iterator i$ = tagArray.iterator();
                while (i$.hasNext()) {
                    SlookSmartClipMetaTag curTag = (SlookSmartClipMetaTag) i$.next();
                    if (curTag != null) {
                        JSONObject json_tag = new JSONObject();
                        json_tag.put(FIELD_META_TAG_TYPE, curTag.getType());
                        json_tag.put(FIELD_META_TAG_VALUE, curTag.getValue());
                        if (curTag instanceof SmartClipMetaTagImpl) {
                            SmartClipMetaTagImpl curTagImpl = (SmartClipMetaTagImpl) curTag;
                            if (curTagImpl.getExtraData() != null) {
                                byte[] extraData = curTagImpl.getExtraData();
                                Log.d(TAG, "Encoding : Length of extra data = " + extraData.length);
                                json_tag.put(FIELD_META_TAG_EXTRA_DATA, Base64.encodeToString(extraData, 0));
                            }
                        }
                        json_tagArray.put(json_tag);
                    }
                }
                if (json_tagArray.length() > 0) {
                    json_repository.put(FIELD_META_TAGS, json_tagArray);
                }
            }
            return json_repository.toString(1);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException throwed : " + e.toString());
            e.printStackTrace();
            return "";
        }
    }
}
