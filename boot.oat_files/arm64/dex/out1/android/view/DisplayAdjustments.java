package android.view;

import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import java.util.Objects;

public class DisplayAdjustments {
    public static final DisplayAdjustments DEFAULT_DISPLAY_ADJUSTMENTS = new DisplayAdjustments();
    public static final boolean DEVELOPMENT_RESOURCES_DEPEND_ON_ACTIVITY_TOKEN = false;
    private volatile CompatibilityInfo mCompatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
    private Configuration mConfiguration = Configuration.EMPTY;

    public DisplayAdjustments(Configuration configuration) {
        this.mConfiguration = configuration;
    }

    public DisplayAdjustments(DisplayAdjustments daj) {
        setCompatibilityInfo(daj.mCompatInfo);
        this.mConfiguration = daj.mConfiguration;
    }

    public void setCompatibilityInfo(CompatibilityInfo compatInfo) {
        if (this == DEFAULT_DISPLAY_ADJUSTMENTS) {
            throw new IllegalArgumentException("setCompatbilityInfo: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
        } else if (compatInfo == null || (!compatInfo.isScalingRequired() && compatInfo.supportsScreen())) {
            this.mCompatInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        } else {
            this.mCompatInfo = compatInfo;
        }
    }

    public CompatibilityInfo getCompatibilityInfo() {
        return this.mCompatInfo;
    }

    public void setConfiguration(Configuration configuration) {
        if (this == DEFAULT_DISPLAY_ADJUSTMENTS) {
            throw new IllegalArgumentException("setConfiguration: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
        }
        this.mConfiguration = configuration;
    }

    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    public int hashCode() {
        return this.mCompatInfo.hashCode() + 527;
    }

    public boolean equals(Object o) {
        if (!(o instanceof DisplayAdjustments)) {
            return false;
        }
        DisplayAdjustments daj = (DisplayAdjustments) o;
        if (Objects.equals(daj.mCompatInfo, this.mCompatInfo) && Objects.equals(daj.mConfiguration, this.mConfiguration)) {
            return true;
        }
        return false;
    }
}
