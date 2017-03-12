package android.media;

import java.util.ArrayList;
import java.util.List;

/* compiled from: TtmlRenderer */
class TtmlNode {
    public final String mAttributes;
    public final List<TtmlNode> mChildren = new ArrayList();
    public final long mEndTimeMs;
    public String mExtentInfo;
    public final String mName;
    public String mOriginInfo;
    public final TtmlNode mParent;
    public String mRegionName;
    public final long mRunId;
    public final long mStartTimeMs;
    public final String mText;

    public TtmlNode(String name, String attributes, String text, long startTimeMs, long endTimeMs, TtmlNode parent, long runId) {
        this.mName = name;
        this.mAttributes = attributes;
        this.mText = text;
        this.mStartTimeMs = startTimeMs;
        this.mEndTimeMs = endTimeMs;
        this.mParent = parent;
        this.mRunId = runId;
        this.mRegionName = null;
    }

    public void setTtmlNodeOrigin(String originInfo) {
        this.mOriginInfo = originInfo;
    }

    public void setTtmlNodeExtent(String extentInfo) {
        this.mExtentInfo = extentInfo;
    }

    public boolean isActive(long startTimeMs, long endTimeMs) {
        return this.mEndTimeMs > startTimeMs && this.mStartTimeMs < endTimeMs;
    }
}
