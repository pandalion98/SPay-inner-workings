package android.media;

import android.media.SubtitleTrack.Cue;

/* compiled from: TtmlRenderer */
class TtmlCue extends Cue {
    private String TAG = "TtmlCue";
    public String mExtentInfo;
    public int mLineChangeCount;
    public String mOriginInfo;
    public boolean mRegion;
    public String mText;
    public String mTtmlFragment;

    public TtmlCue(long startTimeMs, long endTimeMs, String text, String ttmlFragment, long mCurrentRunId) {
        this.mStartTimeMs = startTimeMs;
        this.mEndTimeMs = endTimeMs;
        this.mRunID = mCurrentRunId;
        this.mText = text;
        this.mTtmlFragment = ttmlFragment;
        this.mOriginInfo = null;
        this.mExtentInfo = null;
        this.mRegion = false;
        this.mLineChangeCount = 0;
    }

    public void setTtmlCueOrigin(String originInfo) {
        this.mOriginInfo = originInfo;
    }

    public void setTtmlCueExtent(String extentInfo) {
        this.mExtentInfo = extentInfo;
    }
}
