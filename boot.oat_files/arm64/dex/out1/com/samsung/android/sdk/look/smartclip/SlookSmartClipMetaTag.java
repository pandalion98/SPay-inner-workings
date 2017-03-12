package com.samsung.android.sdk.look.smartclip;

public class SlookSmartClipMetaTag {
    public static final String TAG_TYPE_APP_DEEP_LINK = "app_deep_link";
    public static final String TAG_TYPE_PLAIN_TEXT = "plain_text";
    public static final String TAG_TYPE_TITLE = "title";
    public static final String TAG_TYPE_URL = "url";
    private String mType = null;
    private String mValue = null;

    public SlookSmartClipMetaTag(String tagType, String value) {
        this.mType = tagType;
        this.mValue = value;
    }

    public String getType() {
        return this.mType;
    }

    public String getValue() {
        return this.mValue;
    }

    public void setType(String tagType) {
        this.mType = tagType;
    }

    public void setValue(String value) {
        this.mValue = value;
    }
}
