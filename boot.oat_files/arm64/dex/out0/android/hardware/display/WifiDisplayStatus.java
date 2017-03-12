package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class WifiDisplayStatus implements Parcelable {
    public static final int CONN_STATE_CHANGEPLAYER_GALLERY = 7;
    public static final int CONN_STATE_CHANGEPLAYER_MUSIC = 8;
    public static final int CONN_STATE_CHANGEPLAYER_VIDEO = 6;
    public static final int CONN_STATE_FRIDGE = 5;
    public static final int CONN_STATE_HOMESYNC_MIRROR_MOUSE = 1;
    public static final int CONN_STATE_HOMESYNC_SCREEN_MIRRORING = 2;
    public static final int CONN_STATE_MULTI_ANGLE_RECORDER = 4;
    public static final int CONN_STATE_NFC = 9;
    public static final int CONN_STATE_NORMAL = -1;
    public static final int CONN_STATE_REMOTE_VIEWFINDER = 0;
    public static final int CONN_STATE_SIDESYNC = 3;
    public static final Creator<WifiDisplayStatus> CREATOR = new Creator<WifiDisplayStatus>() {
        public WifiDisplayStatus createFromParcel(Parcel in) {
            int featureState = in.readInt();
            int scanState = in.readInt();
            int activeDisplayState = in.readInt();
            WifiDisplay activeDisplay = null;
            if (in.readInt() != 0) {
                activeDisplay = (WifiDisplay) WifiDisplay.CREATOR.createFromParcel(in);
            }
            WifiDisplay[] displays = (WifiDisplay[]) WifiDisplay.CREATOR.newArray(in.readInt());
            for (int i = 0; i < displays.length; i++) {
                displays[i] = (WifiDisplay) WifiDisplay.CREATOR.createFromParcel(in);
            }
            return new WifiDisplayStatus(featureState, scanState, activeDisplayState, activeDisplay, displays, (WifiDisplaySessionInfo) WifiDisplaySessionInfo.CREATOR.createFromParcel(in), in.readInt(), in.readInt());
        }

        public WifiDisplayStatus[] newArray(int size) {
            return new WifiDisplayStatus[size];
        }
    };
    public static final int DISPLAY_STATE_CONNECTED = 2;
    public static final int DISPLAY_STATE_CONNECTING = 1;
    public static final int DISPLAY_STATE_DISCONNECTING = 3;
    public static final int DISPLAY_STATE_FAILED = 4;
    public static final int DISPLAY_STATE_NOT_CONNECTED = 0;
    public static final int DISPLAY_STATE_PAUSED = 5;
    public static final int FEATURE_STATE_DISABLED = 1;
    public static final int FEATURE_STATE_OFF = 2;
    public static final int FEATURE_STATE_ON = 3;
    public static final int FEATURE_STATE_UNAVAILABLE = 0;
    public static final int SCAN_STATE_NOT_SCANNING = 0;
    public static final int SCAN_STATE_SCANNING = 1;
    public static final int SCREEN_SHARING_STATE_PAUSED = 7;
    public static final int SCREEN_SHARING_STATE_RESUMED = 6;
    public static final int WFD_BRIDGE_STATE_CONNECTED = 1;
    public static final int WFD_BRIDGE_STATE_NOT_CONNECTED = 0;
    private final WifiDisplay mActiveDisplay;
    private final int mActiveDisplayState;
    private final int mConnectedState;
    private final WifiDisplay[] mDisplays;
    private final int mFeatureState;
    private final int mNetworkQoS;
    private final int mScanState;
    private final WifiDisplaySessionInfo mSessionInfo;

    public WifiDisplayStatus() {
        this(3, 0, 0, null, WifiDisplay.EMPTY_ARRAY, null);
    }

    public WifiDisplayStatus(int featureState, int scanState, int activeDisplayState, WifiDisplay activeDisplay, WifiDisplay[] displays, WifiDisplaySessionInfo sessionInfo) {
        if (displays == null) {
            throw new IllegalArgumentException("displays must not be null");
        }
        this.mFeatureState = featureState;
        this.mScanState = scanState;
        this.mActiveDisplayState = activeDisplayState;
        this.mActiveDisplay = activeDisplay;
        this.mDisplays = displays;
        this.mConnectedState = -1;
        this.mNetworkQoS = 0;
        if (sessionInfo == null) {
            sessionInfo = new WifiDisplaySessionInfo();
        }
        this.mSessionInfo = sessionInfo;
    }

    public WifiDisplayStatus(int featureState, int scanState, int activeDisplayState, WifiDisplay activeDisplay, WifiDisplay[] displays, WifiDisplaySessionInfo sessionInfo, int connectedState, int networkQos) {
        if (displays == null) {
            throw new IllegalArgumentException("displays must not be null");
        }
        this.mFeatureState = featureState;
        this.mScanState = scanState;
        this.mActiveDisplayState = activeDisplayState;
        this.mActiveDisplay = activeDisplay;
        Arrays.sort(displays);
        this.mDisplays = displays;
        this.mConnectedState = connectedState;
        this.mNetworkQoS = networkQos;
        if (sessionInfo == null) {
            sessionInfo = new WifiDisplaySessionInfo();
        }
        this.mSessionInfo = sessionInfo;
    }

    public int getConnectedState() {
        return this.mConnectedState;
    }

    public int getNetworkQosLevel() {
        return this.mNetworkQoS;
    }

    public int getFeatureState() {
        return this.mFeatureState;
    }

    public int getScanState() {
        return this.mScanState;
    }

    public int getActiveDisplayState() {
        return this.mActiveDisplayState;
    }

    public WifiDisplay getActiveDisplay() {
        return this.mActiveDisplay;
    }

    public WifiDisplay[] getDisplays() {
        return this.mDisplays;
    }

    public WifiDisplaySessionInfo getSessionInfo() {
        return this.mSessionInfo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mFeatureState);
        dest.writeInt(this.mScanState);
        dest.writeInt(this.mActiveDisplayState);
        if (this.mActiveDisplay != null) {
            dest.writeInt(1);
            this.mActiveDisplay.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.mDisplays.length);
        for (WifiDisplay display : this.mDisplays) {
            display.writeToParcel(dest, flags);
        }
        this.mSessionInfo.writeToParcel(dest, flags);
        dest.writeInt(this.mConnectedState);
        dest.writeInt(this.mNetworkQoS);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "WifiDisplayStatus{featureState=" + this.mFeatureState + ", scanState=" + this.mScanState + ", activeDisplayState=" + this.mActiveDisplayState + ", activeDisplay=" + this.mActiveDisplay + ", displays=" + Arrays.toString(this.mDisplays) + ", sessionInfo=" + this.mSessionInfo + ", connectedState=" + this.mConnectedState + ", networkQoS=" + this.mNetworkQoS + "}";
    }
}
