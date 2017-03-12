package android.media;

public class SecMMFWConfiguration {
    public static final int SEC_PRODUCT_FEATURE_MMFW_TICK_PLAY = 3;
    public static final int SEC_PRODUCT_FEATURE_MMFW_VIDEO_CAPTURE = 1;
    public static final int SEC_PRODUCT_FEATURE_MMFW_VIDEO_PREVIEW_SEEK_HOVERING = 2;
    public static final int SEC_PRODUCT_FEATURE_MMFW_VIDEO_ZOOM = 4;

    public static boolean isEnabledFeature(int feature) {
        if (feature == 1 || feature == 2 || feature == 3 || feature == 4) {
            return true;
        }
        return false;
    }
}
