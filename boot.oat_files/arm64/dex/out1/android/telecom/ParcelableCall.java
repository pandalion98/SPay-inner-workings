package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.telecom.InCallService.VideoCall;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.IVideoProvider.Stub;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ParcelableCall implements Parcelable {
    public static final Creator<ParcelableCall> CREATOR = new Creator<ParcelableCall>() {
        public ParcelableCall createFromParcel(Parcel source) {
            ClassLoader classLoader = ParcelableCall.class.getClassLoader();
            String id = source.readString();
            int state = source.readInt();
            DisconnectCause disconnectCause = (DisconnectCause) source.readParcelable(classLoader);
            List<String> cannedSmsResponses = new ArrayList();
            source.readList(cannedSmsResponses, classLoader);
            int capabilities = source.readInt();
            int properties = source.readInt();
            long connectTimeMillis = source.readLong();
            Uri handle = (Uri) source.readParcelable(classLoader);
            int handlePresentation = source.readInt();
            String callerDisplayName = source.readString();
            int callerDisplayNamePresentation = source.readInt();
            GatewayInfo gatewayInfo = (GatewayInfo) source.readParcelable(classLoader);
            PhoneAccountHandle accountHandle = (PhoneAccountHandle) source.readParcelable(classLoader);
            boolean isVideoCallProviderChanged = source.readByte() == (byte) 1;
            IVideoProvider videoCallProvider = Stub.asInterface(source.readStrongBinder());
            String parentCallId = source.readString();
            List<String> childCallIds = new ArrayList();
            source.readList(childCallIds, classLoader);
            StatusHints statusHints = (StatusHints) source.readParcelable(classLoader);
            int videoState = source.readInt();
            List<String> conferenceableCallIds = new ArrayList();
            source.readList(conferenceableCallIds, classLoader);
            return new ParcelableCall(id, state, disconnectCause, cannedSmsResponses, capabilities, properties, connectTimeMillis, handle, handlePresentation, callerDisplayName, callerDisplayNamePresentation, gatewayInfo, accountHandle, isVideoCallProviderChanged, videoCallProvider, parentCallId, childCallIds, statusHints, videoState, conferenceableCallIds, source.readBundle(classLoader), source.readBundle(classLoader));
        }

        public ParcelableCall[] newArray(int size) {
            return new ParcelableCall[size];
        }
    };
    private final PhoneAccountHandle mAccountHandle;
    private final String mCallerDisplayName;
    private final int mCallerDisplayNamePresentation;
    private final List<String> mCannedSmsResponses;
    private final int mCapabilities;
    private final List<String> mChildCallIds;
    private final List<String> mConferenceableCallIds;
    private final long mConnectTimeMillis;
    private final DisconnectCause mDisconnectCause;
    private final Bundle mExtras;
    private final GatewayInfo mGatewayInfo;
    private final Uri mHandle;
    private final int mHandlePresentation;
    private final String mId;
    private final Bundle mIntentExtras;
    private final boolean mIsVideoCallProviderChanged;
    private final String mParentCallId;
    private final int mProperties;
    private final int mState;
    private final StatusHints mStatusHints;
    private VideoCall mVideoCall;
    private final IVideoProvider mVideoCallProvider;
    private final int mVideoState;

    public ParcelableCall(String id, int state, DisconnectCause disconnectCause, List<String> cannedSmsResponses, int capabilities, int properties, long connectTimeMillis, Uri handle, int handlePresentation, String callerDisplayName, int callerDisplayNamePresentation, GatewayInfo gatewayInfo, PhoneAccountHandle accountHandle, boolean isVideoCallProviderChanged, IVideoProvider videoCallProvider, String parentCallId, List<String> childCallIds, StatusHints statusHints, int videoState, List<String> conferenceableCallIds, Bundle intentExtras, Bundle extras) {
        this.mId = id;
        this.mState = state;
        this.mDisconnectCause = disconnectCause;
        this.mCannedSmsResponses = cannedSmsResponses;
        this.mCapabilities = capabilities;
        this.mProperties = properties;
        this.mConnectTimeMillis = connectTimeMillis;
        this.mHandle = handle;
        this.mHandlePresentation = handlePresentation;
        this.mCallerDisplayName = callerDisplayName;
        this.mCallerDisplayNamePresentation = callerDisplayNamePresentation;
        this.mGatewayInfo = gatewayInfo;
        this.mAccountHandle = accountHandle;
        this.mIsVideoCallProviderChanged = isVideoCallProviderChanged;
        this.mVideoCallProvider = videoCallProvider;
        this.mParentCallId = parentCallId;
        this.mChildCallIds = childCallIds;
        this.mStatusHints = statusHints;
        this.mVideoState = videoState;
        this.mConferenceableCallIds = Collections.unmodifiableList(conferenceableCallIds);
        this.mIntentExtras = intentExtras;
        this.mExtras = extras;
    }

    public String getId() {
        return this.mId;
    }

    public int getState() {
        return this.mState;
    }

    public DisconnectCause getDisconnectCause() {
        return this.mDisconnectCause;
    }

    public List<String> getCannedSmsResponses() {
        return this.mCannedSmsResponses;
    }

    public int getCapabilities() {
        return this.mCapabilities;
    }

    public int getProperties() {
        return this.mProperties;
    }

    public long getConnectTimeMillis() {
        return this.mConnectTimeMillis;
    }

    public Uri getHandle() {
        return this.mHandle;
    }

    public int getHandlePresentation() {
        return this.mHandlePresentation;
    }

    public String getCallerDisplayName() {
        return this.mCallerDisplayName;
    }

    public int getCallerDisplayNamePresentation() {
        return this.mCallerDisplayNamePresentation;
    }

    public GatewayInfo getGatewayInfo() {
        return this.mGatewayInfo;
    }

    public PhoneAccountHandle getAccountHandle() {
        return this.mAccountHandle;
    }

    public VideoCall getVideoCall(Call call) {
        if (this.mVideoCall == null && this.mVideoCallProvider != null) {
            try {
                this.mVideoCall = new VideoCallImpl(this.mVideoCallProvider, call);
            } catch (RemoteException e) {
            }
        }
        return this.mVideoCall;
    }

    public String getParentCallId() {
        return this.mParentCallId;
    }

    public List<String> getChildCallIds() {
        return this.mChildCallIds;
    }

    public List<String> getConferenceableCallIds() {
        return this.mConferenceableCallIds;
    }

    public StatusHints getStatusHints() {
        return this.mStatusHints;
    }

    public int getVideoState() {
        return this.mVideoState;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public Bundle getIntentExtras() {
        return this.mIntentExtras;
    }

    public boolean isVideoCallProviderChanged() {
        return this.mIsVideoCallProviderChanged;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel destination, int flags) {
        destination.writeString(this.mId);
        destination.writeInt(this.mState);
        destination.writeParcelable(this.mDisconnectCause, 0);
        destination.writeList(this.mCannedSmsResponses);
        destination.writeInt(this.mCapabilities);
        destination.writeInt(this.mProperties);
        destination.writeLong(this.mConnectTimeMillis);
        destination.writeParcelable(this.mHandle, 0);
        destination.writeInt(this.mHandlePresentation);
        destination.writeString(this.mCallerDisplayName);
        destination.writeInt(this.mCallerDisplayNamePresentation);
        destination.writeParcelable(this.mGatewayInfo, 0);
        destination.writeParcelable(this.mAccountHandle, 0);
        destination.writeByte((byte) (this.mIsVideoCallProviderChanged ? 1 : 0));
        destination.writeStrongBinder(this.mVideoCallProvider != null ? this.mVideoCallProvider.asBinder() : null);
        destination.writeString(this.mParentCallId);
        destination.writeList(this.mChildCallIds);
        destination.writeParcelable(this.mStatusHints, 0);
        destination.writeInt(this.mVideoState);
        destination.writeList(this.mConferenceableCallIds);
        destination.writeBundle(this.mIntentExtras);
        destination.writeBundle(this.mExtras);
    }

    public String toString() {
        return String.format("[%s, parent:%s, children:%s]", new Object[]{this.mId, this.mParentCallId, this.mChildCallIds});
    }
}
