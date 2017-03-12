package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.InCallService.VideoCall;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Call {
    public static final String AVAILABLE_PHONE_ACCOUNTS = "selectPhoneAccountAccounts";
    public static final int STATE_ACTIVE = 4;
    public static final int STATE_CONNECTING = 9;
    public static final int STATE_DIALING = 1;
    public static final int STATE_DISCONNECTED = 7;
    public static final int STATE_DISCONNECTING = 10;
    public static final int STATE_HOLDING = 3;
    public static final int STATE_NEW = 0;
    @Deprecated
    public static final int STATE_PRE_DIAL_WAIT = 8;
    public static final int STATE_RINGING = 2;
    public static final int STATE_SELECT_PHONE_ACCOUNT = 8;
    private final List<CallbackRecord<Callback>> mCallbackRecords = new CopyOnWriteArrayList();
    private List<String> mCannedTextResponses = null;
    private final List<Call> mChildren = new ArrayList();
    private boolean mChildrenCached;
    private final List<String> mChildrenIds = new ArrayList();
    private final List<Call> mConferenceableCalls = new ArrayList();
    private Details mDetails;
    private final InCallAdapter mInCallAdapter;
    private String mParentId = null;
    private final Phone mPhone;
    private String mRemainingPostDialSequence;
    private int mState;
    private final String mTelecomCallId;
    private final List<Call> mUnmodifiableChildren = Collections.unmodifiableList(this.mChildren);
    private final List<Call> mUnmodifiableConferenceableCalls = Collections.unmodifiableList(this.mConferenceableCalls);
    private VideoCall mVideoCall;

    public static abstract class Callback {
        public void onStateChanged(Call call, int state) {
        }

        public void onParentChanged(Call call, Call parent) {
        }

        public void onChildrenChanged(Call call, List<Call> list) {
        }

        public void onDetailsChanged(Call call, Details details) {
        }

        public void onCannedTextResponsesLoaded(Call call, List<String> list) {
        }

        public void onPostDialWait(Call call, String remainingPostDialSequence) {
        }

        public void onVideoCallChanged(Call call, VideoCall videoCall) {
        }

        public void onCallDestroyed(Call call) {
        }

        public void onConferenceableCallsChanged(Call call, List<Call> list) {
        }
    }

    public static class Details {
        public static final int CAPABILITY_CALL_TRANSFER = 67108864;
        public static final int CAPABILITY_CAN_PAUSE_VIDEO = 1048576;
        public static final int CAPABILITY_CAN_UPGRADE_TO_VIDEO = 524288;
        public static final int CAPABILITY_CONFERENCE_HAS_NO_CHILDREN = 2097152;
        public static final int CAPABILITY_CONTROL_WAITINGCALL = 33554432;
        public static final int CAPABILITY_DISCONNECT_FROM_CONFERENCE = 8192;
        public static final int CAPABILITY_GROUP_CALL = 8388608;
        public static final int CAPABILITY_HOLD = 1;
        public static final int CAPABILITY_MANAGE_CONFERENCE = 128;
        public static final int CAPABILITY_MERGE_CONFERENCE = 4;
        public static final int CAPABILITY_MUTE = 64;
        public static final int CAPABILITY_RESPOND_VIA_TEXT = 32;
        public static final int CAPABILITY_ROAMING_AREA = 4194304;
        public static final int CAPABILITY_SEPARATE_FROM_CONFERENCE = 4096;
        public static final int CAPABILITY_SESSION_MODIFY = 16777216;
        public static final int CAPABILITY_SPEED_UP_MT_AUDIO = 262144;
        public static final int CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL = 768;
        public static final int CAPABILITY_SUPPORTS_VT_LOCAL_RX = 256;
        public static final int CAPABILITY_SUPPORTS_VT_LOCAL_TX = 512;
        public static final int CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL = 3072;
        public static final int CAPABILITY_SUPPORTS_VT_REMOTE_RX = 1024;
        public static final int CAPABILITY_SUPPORTS_VT_REMOTE_TX = 2048;
        public static final int CAPABILITY_SUPPORT_HOLD = 2;
        public static final int CAPABILITY_SWAP_CONFERENCE = 8;
        public static final int CAPABILITY_UNUSED_1 = 16;
        public static final int CAPABILITY_VOICE_PRIVACY = 134217728;
        public static final int PROPERTY_CONFERENCE = 1;
        public static final int PROPERTY_EMERGENCY_CALLBACK_MODE = 4;
        public static final int PROPERTY_GENERIC_CONFERENCE = 2;
        public static final int PROPERTY_HIGH_DEF_AUDIO = 16;
        public static final int PROPERTY_WIFI = 8;
        private final PhoneAccountHandle mAccountHandle;
        private final int mCallCapabilities;
        private final int mCallProperties;
        private final String mCallerDisplayName;
        private final int mCallerDisplayNamePresentation;
        private final long mConnectTimeMillis;
        private final DisconnectCause mDisconnectCause;
        private final Bundle mExtras;
        private final GatewayInfo mGatewayInfo;
        private final Uri mHandle;
        private final int mHandlePresentation;
        private final Bundle mIntentExtras;
        private final StatusHints mStatusHints;
        private final int mVideoState;

        public static boolean can(int capabilities, int capability) {
            return (capabilities & capability) != 0;
        }

        public boolean can(int capability) {
            return can(this.mCallCapabilities, capability);
        }

        public static String capabilitiesToString(int capabilities) {
            StringBuilder builder = new StringBuilder();
            builder.append("[Capabilities:");
            if (can(capabilities, 1)) {
                builder.append(" CAPABILITY_HOLD");
            }
            if (can(capabilities, 2)) {
                builder.append(" CAPABILITY_SUPPORT_HOLD");
            }
            if (can(capabilities, 4)) {
                builder.append(" CAPABILITY_MERGE_CONFERENCE");
            }
            if (can(capabilities, 8)) {
                builder.append(" CAPABILITY_SWAP_CONFERENCE");
            }
            if (can(capabilities, 32)) {
                builder.append(" CAPABILITY_RESPOND_VIA_TEXT");
            }
            if (can(capabilities, 64)) {
                builder.append(" CAPABILITY_MUTE");
            }
            if (can(capabilities, 128)) {
                builder.append(" CAPABILITY_MANAGE_CONFERENCE");
            }
            if (can(capabilities, 256)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_RX");
            }
            if (can(capabilities, 512)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_TX");
            }
            if (can(capabilities, 768)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL");
            }
            if (can(capabilities, 1024)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_RX");
            }
            if (can(capabilities, 2048)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_TX");
            }
            if (can(capabilities, 3072)) {
                builder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL");
            }
            if (can(capabilities, 262144)) {
                builder.append(" CAPABILITY_SPEED_UP_MT_AUDIO");
            }
            if (can(capabilities, 524288)) {
                builder.append(" CAPABILITY_CAN_UPGRADE_TO_VIDEO");
            }
            if (can(capabilities, 1048576)) {
                builder.append(" CAPABILITY_CAN_PAUSE_VIDEO");
            }
            if (can(capabilities, 2097152)) {
                builder.append(" CAPABILITY_CONFERENCE_HAS_NO_CHILDREN");
            }
            if (can(capabilities, 4194304)) {
                builder.append(" CAPABILITY_ROAMING_AREA");
            }
            if (can(capabilities, 8388608)) {
                builder.append(" CAPABILITY_GROUP_CALL");
            }
            if (can(capabilities, 16777216)) {
                builder.append(" CAPABILITY_SESSION_MODIFY");
            }
            if (can(capabilities, 33554432)) {
                builder.append(" CAPABILITY_CONTROL_WAITINGCALL");
            }
            if (can(capabilities, 67108864)) {
                builder.append(" CAPABILITY_CALL_TRANSFER");
            }
            if (can(capabilities, 134217728)) {
                builder.append(" CAPABILITY_VOICE_PRIVACY");
            }
            builder.append("]");
            return builder.toString();
        }

        public static boolean hasProperty(int properties, int property) {
            return (properties & property) != 0;
        }

        public boolean hasProperty(int property) {
            return hasProperty(this.mCallProperties, property);
        }

        public static String propertiesToString(int properties) {
            StringBuilder builder = new StringBuilder();
            builder.append("[Properties:");
            if (hasProperty(properties, 1)) {
                builder.append(" PROPERTY_CONFERENCE");
            }
            if (hasProperty(properties, 2)) {
                builder.append(" PROPERTY_GENERIC_CONFERENCE");
            }
            if (hasProperty(properties, 8)) {
                builder.append(" PROPERTY_WIFI");
            }
            if (hasProperty(properties, 16)) {
                builder.append(" PROPERTY_HIGH_DEF_AUDIO");
            }
            if (hasProperty(properties, 4)) {
                builder.append(" PROPERTY_EMERGENCY_CALLBACK_MODE");
            }
            builder.append("]");
            return builder.toString();
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

        public PhoneAccountHandle getAccountHandle() {
            return this.mAccountHandle;
        }

        public int getCallCapabilities() {
            return this.mCallCapabilities;
        }

        public int getCallProperties() {
            return this.mCallProperties;
        }

        public DisconnectCause getDisconnectCause() {
            return this.mDisconnectCause;
        }

        public final long getConnectTimeMillis() {
            return this.mConnectTimeMillis;
        }

        public GatewayInfo getGatewayInfo() {
            return this.mGatewayInfo;
        }

        public int getVideoState() {
            return this.mVideoState;
        }

        public StatusHints getStatusHints() {
            return this.mStatusHints;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public Bundle getIntentExtras() {
            return this.mIntentExtras;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Details)) {
                return false;
            }
            Details d = (Details) o;
            if (Objects.equals(this.mHandle, d.mHandle) && Objects.equals(Integer.valueOf(this.mHandlePresentation), Integer.valueOf(d.mHandlePresentation)) && Objects.equals(this.mCallerDisplayName, d.mCallerDisplayName) && Objects.equals(Integer.valueOf(this.mCallerDisplayNamePresentation), Integer.valueOf(d.mCallerDisplayNamePresentation)) && Objects.equals(this.mAccountHandle, d.mAccountHandle) && Objects.equals(Integer.valueOf(this.mCallCapabilities), Integer.valueOf(d.mCallCapabilities)) && Objects.equals(Integer.valueOf(this.mCallProperties), Integer.valueOf(d.mCallProperties)) && Objects.equals(this.mDisconnectCause, d.mDisconnectCause) && Objects.equals(Long.valueOf(this.mConnectTimeMillis), Long.valueOf(d.mConnectTimeMillis)) && Objects.equals(this.mGatewayInfo, d.mGatewayInfo) && Objects.equals(Integer.valueOf(this.mVideoState), Integer.valueOf(d.mVideoState)) && Objects.equals(this.mStatusHints, d.mStatusHints) && Objects.equals(this.mExtras, d.mExtras) && Objects.equals(this.mIntentExtras, d.mIntentExtras)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return ((((((((((((Objects.hashCode(this.mHandle) + Objects.hashCode(Integer.valueOf(this.mHandlePresentation))) + Objects.hashCode(this.mCallerDisplayName)) + Objects.hashCode(Integer.valueOf(this.mCallerDisplayNamePresentation))) + Objects.hashCode(this.mAccountHandle)) + Objects.hashCode(Integer.valueOf(this.mCallCapabilities))) + Objects.hashCode(Integer.valueOf(this.mCallProperties))) + Objects.hashCode(this.mDisconnectCause)) + Objects.hashCode(Long.valueOf(this.mConnectTimeMillis))) + Objects.hashCode(this.mGatewayInfo)) + Objects.hashCode(Integer.valueOf(this.mVideoState))) + Objects.hashCode(this.mStatusHints)) + Objects.hashCode(this.mExtras)) + Objects.hashCode(this.mIntentExtras);
        }

        public Details(Uri handle, int handlePresentation, String callerDisplayName, int callerDisplayNamePresentation, PhoneAccountHandle accountHandle, int capabilities, int properties, DisconnectCause disconnectCause, long connectTimeMillis, GatewayInfo gatewayInfo, int videoState, StatusHints statusHints, Bundle extras, Bundle intentExtras) {
            this.mHandle = handle;
            this.mHandlePresentation = handlePresentation;
            this.mCallerDisplayName = callerDisplayName;
            this.mCallerDisplayNamePresentation = callerDisplayNamePresentation;
            this.mAccountHandle = accountHandle;
            this.mCallCapabilities = capabilities;
            this.mCallProperties = properties;
            this.mDisconnectCause = disconnectCause;
            this.mConnectTimeMillis = connectTimeMillis;
            this.mGatewayInfo = gatewayInfo;
            this.mVideoState = videoState;
            this.mStatusHints = statusHints;
            this.mExtras = extras;
            this.mIntentExtras = intentExtras;
        }
    }

    @Deprecated
    public static abstract class Listener extends Callback {
    }

    public String getRemainingPostDialSequence() {
        return this.mRemainingPostDialSequence;
    }

    public void answer(int videoState) {
        this.mInCallAdapter.answerCall(this.mTelecomCallId, videoState);
    }

    public void reject(boolean rejectWithMessage, String textMessage) {
        this.mInCallAdapter.rejectCall(this.mTelecomCallId, rejectWithMessage, textMessage);
    }

    public void disconnect() {
        this.mInCallAdapter.disconnectCall(this.mTelecomCallId);
    }

    public void hold() {
        this.mInCallAdapter.holdCall(this.mTelecomCallId);
    }

    public void unhold() {
        this.mInCallAdapter.unholdCall(this.mTelecomCallId);
    }

    public void playDtmfTone(char digit) {
        this.mInCallAdapter.playDtmfTone(this.mTelecomCallId, digit);
    }

    public void stopDtmfTone() {
        this.mInCallAdapter.stopDtmfTone(this.mTelecomCallId);
    }

    public void postDialContinue(boolean proceed) {
        this.mInCallAdapter.postDialContinue(this.mTelecomCallId, proceed);
    }

    public void phoneAccountSelected(PhoneAccountHandle accountHandle, boolean setDefault) {
        this.mInCallAdapter.phoneAccountSelected(this.mTelecomCallId, accountHandle, setDefault);
    }

    public void conference(Call callToConferenceWith) {
        if (callToConferenceWith != null) {
            this.mInCallAdapter.conference(this.mTelecomCallId, callToConferenceWith.mTelecomCallId);
        }
    }

    public void splitFromConference() {
        this.mInCallAdapter.splitFromConference(this.mTelecomCallId);
    }

    public void mergeConference() {
        this.mInCallAdapter.mergeConference(this.mTelecomCallId);
    }

    public void swapConference() {
        this.mInCallAdapter.swapConference(this.mTelecomCallId);
    }

    public Call getParent() {
        if (this.mParentId != null) {
            return this.mPhone.internalGetCallByTelecomId(this.mParentId);
        }
        return null;
    }

    public List<Call> getChildren() {
        if (!this.mChildrenCached) {
            this.mChildrenCached = true;
            this.mChildren.clear();
            for (String id : this.mChildrenIds) {
                Call call = this.mPhone.internalGetCallByTelecomId(id);
                if (call == null) {
                    this.mChildrenCached = false;
                } else {
                    this.mChildren.add(call);
                }
            }
        }
        return this.mUnmodifiableChildren;
    }

    public List<Call> getConferenceableCalls() {
        return this.mUnmodifiableConferenceableCalls;
    }

    public int getState() {
        return this.mState;
    }

    public List<String> getCannedTextResponses() {
        return this.mCannedTextResponses;
    }

    public VideoCall getVideoCall() {
        return this.mVideoCall;
    }

    public Details getDetails() {
        return this.mDetails;
    }

    public void registerCallback(Callback callback) {
        registerCallback(callback, new Handler());
    }

    public void registerCallback(Callback callback, Handler handler) {
        unregisterCallback(callback);
        if (callback != null && handler != null && this.mState != 7) {
            this.mCallbackRecords.add(new CallbackRecord(callback, handler));
        }
    }

    public void unregisterCallback(Callback callback) {
        if (callback != null && this.mState != 7) {
            for (CallbackRecord<Callback> record : this.mCallbackRecords) {
                if (record.getCallback() == callback) {
                    this.mCallbackRecords.remove(record);
                    return;
                }
            }
        }
    }

    @Deprecated
    public void addListener(Listener listener) {
        registerCallback(listener);
    }

    @Deprecated
    public void removeListener(Listener listener) {
        unregisterCallback(listener);
    }

    Call(Phone phone, String telecomCallId, InCallAdapter inCallAdapter) {
        this.mPhone = phone;
        this.mTelecomCallId = telecomCallId;
        this.mInCallAdapter = inCallAdapter;
        this.mState = 0;
    }

    Call(Phone phone, String telecomCallId, InCallAdapter inCallAdapter, int state) {
        this.mPhone = phone;
        this.mTelecomCallId = telecomCallId;
        this.mInCallAdapter = inCallAdapter;
        this.mState = state;
    }

    final String internalGetCallId() {
        return this.mTelecomCallId;
    }

    final void internalUpdate(ParcelableCall parcelableCall, Map<String, Call> callIdMap) {
        Details details = new Details(parcelableCall.getHandle(), parcelableCall.getHandlePresentation(), parcelableCall.getCallerDisplayName(), parcelableCall.getCallerDisplayNamePresentation(), parcelableCall.getAccountHandle(), parcelableCall.getCapabilities(), parcelableCall.getProperties(), parcelableCall.getDisconnectCause(), parcelableCall.getConnectTimeMillis(), parcelableCall.getGatewayInfo(), parcelableCall.getVideoState(), parcelableCall.getStatusHints(), parcelableCall.getExtras(), parcelableCall.getIntentExtras());
        boolean detailsChanged = !Objects.equals(this.mDetails, details);
        if (detailsChanged) {
            this.mDetails = details;
        }
        if (!(this.mCannedTextResponses != null || parcelableCall.getCannedSmsResponses() == null || parcelableCall.getCannedSmsResponses().isEmpty())) {
            this.mCannedTextResponses = Collections.unmodifiableList(parcelableCall.getCannedSmsResponses());
        }
        boolean videoCallChanged = parcelableCall.isVideoCallProviderChanged() && !Objects.equals(this.mVideoCall, parcelableCall.getVideoCall(this));
        if (videoCallChanged) {
            this.mVideoCall = parcelableCall.getVideoCall(this);
        }
        int state = parcelableCall.getState();
        boolean stateChanged = this.mState != state;
        if ((this.mState == 4 && state == 2) || (this.mState == 4 && state == 1)) {
            stateChanged = false;
        }
        if (stateChanged) {
            this.mState = state;
        }
        String parentId = parcelableCall.getParentCallId();
        boolean parentChanged = !Objects.equals(this.mParentId, parentId);
        if (parentChanged) {
            this.mParentId = parentId;
        }
        boolean childrenChanged = !Objects.equals(parcelableCall.getChildCallIds(), this.mChildrenIds);
        if (childrenChanged) {
            this.mChildrenIds.clear();
            this.mChildrenIds.addAll(parcelableCall.getChildCallIds());
            this.mChildrenCached = false;
        }
        List<String> conferenceableCallIds = parcelableCall.getConferenceableCallIds();
        List<Call> arrayList = new ArrayList(conferenceableCallIds.size());
        for (String otherId : conferenceableCallIds) {
            if (callIdMap.containsKey(otherId)) {
                arrayList.add(callIdMap.get(otherId));
            }
        }
        if (!Objects.equals(this.mConferenceableCalls, arrayList)) {
            this.mConferenceableCalls.clear();
            this.mConferenceableCalls.addAll(arrayList);
            fireConferenceableCallsChanged();
        }
        if (stateChanged) {
            fireStateChanged(this.mState);
        }
        if (detailsChanged) {
            fireDetailsChanged(this.mDetails);
        }
        if (false) {
            fireCannedTextResponsesLoaded(this.mCannedTextResponses);
        }
        if (videoCallChanged) {
            fireVideoCallChanged(this.mVideoCall);
        }
        if (parentChanged) {
            fireParentChanged(getParent());
        }
        if (childrenChanged) {
            fireChildrenChanged(getChildren());
        }
        if (this.mState == 7) {
            fireCallDestroyed();
        }
    }

    final void internalSetPostDialWait(String remaining) {
        this.mRemainingPostDialSequence = remaining;
        firePostDialWait(this.mRemainingPostDialSequence);
    }

    final void internalSetDisconnected() {
        if (this.mState != 7) {
            this.mState = 7;
            fireStateChanged(this.mState);
            fireCallDestroyed();
        }
    }

    private void fireStateChanged(final int newState) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onStateChanged(call, newState);
                }
            });
        }
    }

    private void fireParentChanged(final Call newParent) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onParentChanged(call, newParent);
                }
            });
        }
    }

    private void fireChildrenChanged(final List<Call> children) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onChildrenChanged(call, children);
                }
            });
        }
    }

    private void fireDetailsChanged(final Details details) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onDetailsChanged(call, details);
                }
            });
        }
    }

    private void fireCannedTextResponsesLoaded(final List<String> cannedTextResponses) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onCannedTextResponsesLoaded(call, cannedTextResponses);
                }
            });
        }
    }

    private void fireVideoCallChanged(final VideoCall videoCall) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onVideoCallChanged(call, videoCall);
                }
            });
        }
    }

    private void firePostDialWait(final String remainingPostDialSequence) {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onPostDialWait(call, remainingPostDialSequence);
                }
            });
        }
    }

    private void fireCallDestroyed() {
        final Call call = this;
        if (this.mCallbackRecords.isEmpty()) {
            this.mPhone.internalRemoveCall(call);
        }
        for (final CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    boolean isFinalRemoval = false;
                    RuntimeException toThrow = null;
                    try {
                        callback.onCallDestroyed(call);
                    } catch (RuntimeException e) {
                        toThrow = e;
                    }
                    synchronized (Call.this) {
                        Call.this.mCallbackRecords.remove(record);
                        if (Call.this.mCallbackRecords.isEmpty()) {
                            isFinalRemoval = true;
                        }
                    }
                    if (isFinalRemoval) {
                        Call.this.mPhone.internalRemoveCall(call);
                    }
                    if (toThrow != null) {
                        throw toThrow;
                    }
                }
            });
        }
    }

    private void fireConferenceableCallsChanged() {
        for (CallbackRecord<Callback> record : this.mCallbackRecords) {
            final Call call = this;
            final Callback callback = (Callback) record.getCallback();
            record.getHandler().post(new Runnable() {
                public void run() {
                    callback.onConferenceableCallsChanged(call, Call.this.mUnmodifiableConferenceableCalls);
                }
            });
        }
    }
}
