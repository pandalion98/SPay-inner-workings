package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import com.android.internal.util.Preconditions;

public final class OutputConfiguration implements Parcelable {
    public static final Creator<OutputConfiguration> CREATOR = new Creator<OutputConfiguration>() {
        public OutputConfiguration createFromParcel(Parcel source) {
            try {
                return new OutputConfiguration(source);
            } catch (Exception e) {
                Log.e(OutputConfiguration.TAG, "Exception creating OutputConfiguration from parcel", e);
                return null;
            }
        }

        public OutputConfiguration[] newArray(int size) {
            return new OutputConfiguration[size];
        }
    };
    public static final int ROTATION_0 = 0;
    public static final int ROTATION_180 = 2;
    public static final int ROTATION_270 = 3;
    public static final int ROTATION_90 = 1;
    private static final String TAG = "OutputConfiguration";
    private final int mConfiguredDataspace;
    private final int mConfiguredFormat;
    private final Size mConfiguredSize;
    private final int mRotation;
    private final Surface mSurface;

    public OutputConfiguration(Surface surface) {
        this(surface, 0);
    }

    public OutputConfiguration(Surface surface, int rotation) {
        Preconditions.checkNotNull(surface, "Surface must not be null");
        Preconditions.checkArgumentInRange(rotation, 0, 3, "Rotation constant");
        this.mSurface = surface;
        this.mRotation = rotation;
        this.mConfiguredSize = SurfaceUtils.getSurfaceSize(surface);
        this.mConfiguredFormat = SurfaceUtils.getSurfaceFormat(surface);
        this.mConfiguredDataspace = SurfaceUtils.getSurfaceDataspace(surface);
    }

    private OutputConfiguration(Parcel source) {
        int rotation = source.readInt();
        Surface surface = (Surface) Surface.CREATOR.createFromParcel(source);
        Preconditions.checkNotNull(surface, "Surface must not be null");
        Preconditions.checkArgumentInRange(rotation, 0, 3, "Rotation constant");
        this.mSurface = surface;
        this.mRotation = rotation;
        this.mConfiguredSize = SurfaceUtils.getSurfaceSize(this.mSurface);
        this.mConfiguredFormat = SurfaceUtils.getSurfaceFormat(this.mSurface);
        this.mConfiguredDataspace = SurfaceUtils.getSurfaceDataspace(this.mSurface);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public int getRotation() {
        return this.mRotation;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (dest == null) {
            throw new IllegalArgumentException("dest must not be null");
        }
        dest.writeInt(this.mRotation);
        this.mSurface.writeToParcel(dest, flags);
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OutputConfiguration)) {
            return false;
        }
        OutputConfiguration other = (OutputConfiguration) obj;
        if (!(this.mSurface == other.mSurface && this.mRotation == other.mRotation && this.mConfiguredSize.equals(other.mConfiguredSize) && this.mConfiguredFormat == other.mConfiguredFormat && this.mConfiguredDataspace == other.mConfiguredDataspace)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mSurface.hashCode(), this.mRotation);
    }
}
