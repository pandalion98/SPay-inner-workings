package android.media;

import android.hardware.SensorManager;
import android.net.ProxyInfo;

/* compiled from: WebVttRenderer */
class TextTrackRegion {
    static final int SCROLL_VALUE_NONE = 300;
    static final int SCROLL_VALUE_SCROLL_UP = 301;
    float mAnchorPointX = 0.0f;
    float mAnchorPointY = SensorManager.LIGHT_CLOUDY;
    String mId = ProxyInfo.LOCAL_EXCL_LIST;
    int mLines = 3;
    int mScrollValue = 300;
    float mViewportAnchorPointX = 0.0f;
    float mViewportAnchorPointY = SensorManager.LIGHT_CLOUDY;
    float mWidth = SensorManager.LIGHT_CLOUDY;

    TextTrackRegion() {
    }

    public String toString() {
        StringBuilder append = new StringBuilder(" {id:\"").append(this.mId).append("\", width:").append(this.mWidth).append(", lines:").append(this.mLines).append(", anchorPoint:(").append(this.mAnchorPointX).append(", ").append(this.mAnchorPointY).append("), viewportAnchorPoints:").append(this.mViewportAnchorPointX).append(", ").append(this.mViewportAnchorPointY).append("), scrollValue:");
        String str = this.mScrollValue == 300 ? "none" : this.mScrollValue == 301 ? "scroll_up" : "INVALID";
        return append.append(str).append("}").toString();
    }
}
