package android.media;

import android.media.AudioAttributes.Builder;
import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class AudioFocusInfo implements Parcelable {
    public static final Creator<AudioFocusInfo> CREATOR = new Creator<AudioFocusInfo>() {
        public AudioFocusInfo createFromParcel(Parcel in) {
            return new AudioFocusInfo((AudioAttributes) AudioAttributes.CREATOR.createFromParcel(in), in.readString(), in.readString(), in.readInt(), in.readInt(), in.readInt());
        }

        public AudioFocusInfo[] newArray(int size) {
            return new AudioFocusInfo[size];
        }
    };
    private AudioAttributes mAttributes;
    private String mClientId;
    private int mFlags;
    private int mGainRequest;
    private int mLossReceived;
    private String mPackageName;

    public AudioFocusInfo(AudioAttributes aa, String clientId, String packageName, int gainRequest, int lossReceived, int flags) {
        if (aa == null) {
            aa = new Builder().build();
        }
        this.mAttributes = aa;
        if (clientId == null) {
            clientId = ProxyInfo.LOCAL_EXCL_LIST;
        }
        this.mClientId = clientId;
        if (packageName == null) {
            packageName = ProxyInfo.LOCAL_EXCL_LIST;
        }
        this.mPackageName = packageName;
        this.mGainRequest = gainRequest;
        this.mLossReceived = lossReceived;
        this.mFlags = flags;
    }

    public AudioAttributes getAttributes() {
        return this.mAttributes;
    }

    public String getClientId() {
        return this.mClientId;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public int getGainRequest() {
        return this.mGainRequest;
    }

    public int getLossReceived() {
        return this.mLossReceived;
    }

    public void clearLossReceived() {
        this.mLossReceived = 0;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        this.mAttributes.writeToParcel(dest, flags);
        dest.writeString(this.mClientId);
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mGainRequest);
        dest.writeInt(this.mLossReceived);
        dest.writeInt(this.mFlags);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mAttributes, this.mClientId, this.mPackageName, Integer.valueOf(this.mGainRequest), Integer.valueOf(this.mFlags)});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AudioFocusInfo other = (AudioFocusInfo) obj;
        if (!this.mAttributes.equals(other.mAttributes)) {
            return false;
        }
        if (!this.mClientId.equals(other.mClientId)) {
            return false;
        }
        if (!this.mPackageName.equals(other.mPackageName)) {
            return false;
        }
        if (this.mGainRequest != other.mGainRequest) {
            return false;
        }
        if (this.mLossReceived != other.mLossReceived) {
            return false;
        }
        if (this.mFlags != other.mFlags) {
            return false;
        }
        return true;
    }
}
