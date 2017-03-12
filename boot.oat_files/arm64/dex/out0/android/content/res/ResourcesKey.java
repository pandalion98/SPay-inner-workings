package android.content.res;

import android.os.IBinder;
import android.os.Process;
import android.os.UserHandle;
import java.util.Objects;

public final class ResourcesKey {
    public final int mDisplayId;
    private final int mHash;
    public final Configuration mOverrideConfiguration;
    private final String mResDir;
    private final float mScale;
    private final int mUserId;

    public ResourcesKey(String resDir, int displayId, Configuration overrideConfiguration, float scale) {
        this(resDir, displayId, overrideConfiguration, scale, UserHandle.getUserId(Process.myUid()));
    }

    public ResourcesKey(String resDir, int displayId, Configuration overrideConfiguration, float scale, int userId) {
        this(resDir, displayId, overrideConfiguration, scale, userId, null);
    }

    public ResourcesKey(String resDir, int displayId, Configuration overrideConfiguration, float scale, int userId, IBinder token) {
        this.mUserId = userId;
        this.mResDir = resDir;
        this.mDisplayId = displayId;
        if (overrideConfiguration == null) {
            overrideConfiguration = Configuration.EMPTY;
        }
        this.mOverrideConfiguration = overrideConfiguration;
        this.mScale = scale;
        this.mHash = (((((((((this.mResDir == null ? 0 : this.mResDir.hashCode()) + 527) * 31) + this.mDisplayId) * 31) + this.mOverrideConfiguration.hashCode()) * 31) + Float.floatToIntBits(this.mScale)) * 31) + this.mUserId;
    }

    public boolean hasOverrideConfiguration() {
        return !Configuration.EMPTY.equals(this.mOverrideConfiguration);
    }

    public int hashCode() {
        return this.mHash;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ResourcesKey)) {
            return false;
        }
        ResourcesKey peer = (ResourcesKey) obj;
        if (Objects.equals(this.mResDir, peer.mResDir) && this.mDisplayId == peer.mDisplayId && this.mOverrideConfiguration.equals(peer.mOverrideConfiguration) && this.mScale == peer.mScale && this.mUserId == peer.mUserId) {
            return true;
        }
        return false;
    }

    public String toString() {
        return Integer.toHexString(this.mHash);
    }
}
