package android.media;

/* compiled from: TtmlRenderer */
class TtmlRegionTag {
    private String TAG = "TtmlRegion";
    public String mExtentInfo;
    public String mOriginInfo;
    public String mRegionName;

    public TtmlRegionTag(String regionName) {
        this.mRegionName = regionName;
        this.mOriginInfo = null;
        this.mExtentInfo = null;
    }
}
