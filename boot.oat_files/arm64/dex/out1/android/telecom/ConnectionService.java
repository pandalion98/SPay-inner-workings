package android.telecom;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telecom.Conference.Listener;
import android.telecom.Connection.VideoProvider;
import com.android.ims.ImsCallProfile;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IConnectionService.Stub;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConnectionService extends Service {
    private static final int MSG_ABORT = 3;
    private static final int MSG_ADD_CONNECTION_SERVICE_ADAPTER = 1;
    private static final int MSG_ANSWER = 4;
    private static final int MSG_ANSWER_VIDEO = 17;
    private static final int MSG_CONFERENCE = 12;
    private static final int MSG_CREATE_CONNECTION = 2;
    private static final int MSG_DISCONNECT = 6;
    private static final int MSG_HOLD = 7;
    private static final int MSG_MERGE_CONFERENCE = 18;
    private static final int MSG_ON_CALL_AUDIO_STATE_CHANGED = 9;
    private static final int MSG_ON_POST_DIAL_CONTINUE = 14;
    private static final int MSG_PLAY_DTMF_TONE = 10;
    private static final int MSG_REJECT = 5;
    private static final int MSG_REMOVE_CONNECTION_SERVICE_ADAPTER = 16;
    private static final int MSG_SPLIT_FROM_CONFERENCE = 13;
    private static final int MSG_STOP_DTMF_TONE = 11;
    private static final int MSG_SWAP_CONFERENCE = 19;
    private static final int MSG_UNHOLD = 8;
    private static final boolean PII_DEBUG = Log.isLoggable(3);
    public static final String SERVICE_INTERFACE = "android.telecom.ConnectionService";
    private static Connection sNullConnection;
    private final ConnectionServiceAdapter mAdapter = new ConnectionServiceAdapter();
    private boolean mAreAccountsInitialized = false;
    private final IBinder mBinder = new Stub() {
        public void addConnectionServiceAdapter(IConnectionServiceAdapter adapter) {
            ConnectionService.this.mHandler.obtainMessage(1, adapter).sendToTarget();
        }

        public void removeConnectionServiceAdapter(IConnectionServiceAdapter adapter) {
            ConnectionService.this.mHandler.obtainMessage(16, adapter).sendToTarget();
        }

        public void createConnection(PhoneAccountHandle connectionManagerPhoneAccount, String id, ConnectionRequest request, boolean isIncoming, boolean isUnknown) {
            int i;
            int i2 = 1;
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionManagerPhoneAccount;
            args.arg2 = id;
            args.arg3 = request;
            if (isIncoming) {
                i = 1;
            } else {
                i = 0;
            }
            args.argi1 = i;
            if (!isUnknown) {
                i2 = 0;
            }
            args.argi2 = i2;
            ConnectionService.this.mHandler.obtainMessage(2, args).sendToTarget();
        }

        public void abort(String callId) {
            ConnectionService.this.mHandler.obtainMessage(3, callId).sendToTarget();
        }

        public void answerVideo(String callId, int videoState) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            args.argi1 = videoState;
            ConnectionService.this.mHandler.obtainMessage(17, args).sendToTarget();
        }

        public void answer(String callId) {
            ConnectionService.this.mHandler.obtainMessage(4, callId).sendToTarget();
        }

        public void reject(String callId) {
            ConnectionService.this.mHandler.obtainMessage(5, callId).sendToTarget();
        }

        public void disconnect(String callId) {
            ConnectionService.this.mHandler.obtainMessage(6, callId).sendToTarget();
        }

        public void hold(String callId) {
            ConnectionService.this.mHandler.obtainMessage(7, callId).sendToTarget();
        }

        public void unhold(String callId) {
            ConnectionService.this.mHandler.obtainMessage(8, callId).sendToTarget();
        }

        public void onCallAudioStateChanged(String callId, CallAudioState callAudioState) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = callAudioState;
            ConnectionService.this.mHandler.obtainMessage(9, args).sendToTarget();
        }

        public void playDtmfTone(String callId, char digit) {
            ConnectionService.this.mHandler.obtainMessage(10, digit, 0, callId).sendToTarget();
        }

        public void stopDtmfTone(String callId) {
            ConnectionService.this.mHandler.obtainMessage(11, callId).sendToTarget();
        }

        public void conference(String callId1, String callId2) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId1;
            args.arg2 = callId2;
            ConnectionService.this.mHandler.obtainMessage(12, args).sendToTarget();
        }

        public void splitFromConference(String callId) {
            ConnectionService.this.mHandler.obtainMessage(13, callId).sendToTarget();
        }

        public void mergeConference(String callId) {
            ConnectionService.this.mHandler.obtainMessage(18, callId).sendToTarget();
        }

        public void swapConference(String callId) {
            ConnectionService.this.mHandler.obtainMessage(19, callId).sendToTarget();
        }

        public void onPostDialContinue(String callId, boolean proceed) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            args.argi1 = proceed ? 1 : 0;
            ConnectionService.this.mHandler.obtainMessage(14, args).sendToTarget();
        }
    };
    private final Map<String, Conference> mConferenceById = new ConcurrentHashMap();
    private final Listener mConferenceListener = new Listener() {
        public void onStateChanged(Conference conference, int oldState, int newState) {
            String id = (String) ConnectionService.this.mIdByConference.get(conference);
            switch (newState) {
                case 4:
                    ConnectionService.this.mAdapter.setActive(id);
                    return;
                case 5:
                    ConnectionService.this.mAdapter.setOnHold(id);
                    return;
                default:
                    return;
            }
        }

        public void onDisconnected(Conference conference, DisconnectCause disconnectCause) {
            ConnectionService.this.mAdapter.setDisconnected((String) ConnectionService.this.mIdByConference.get(conference), disconnectCause);
        }

        public void onConnectionAdded(Conference conference, Connection connection) {
        }

        public void onConnectionRemoved(Conference conference, Connection connection) {
        }

        public void onConferenceableConnectionsChanged(Conference conference, List<Connection> conferenceableConnections) {
            ConnectionService.this.mAdapter.setConferenceableConnections((String) ConnectionService.this.mIdByConference.get(conference), ConnectionService.this.createConnectionIdList(conferenceableConnections));
        }

        public void onDestroyed(Conference conference) {
            ConnectionService.this.removeConference(conference);
        }

        public void onConnectionCapabilitiesChanged(Conference conference, int connectionCapabilities) {
            String id = (String) ConnectionService.this.mIdByConference.get(conference);
            Log.d((Object) this, "call capabilities: conference: %s", Connection.capabilitiesToString(connectionCapabilities));
            ConnectionService.this.mAdapter.setConnectionCapabilities(id, connectionCapabilities);
        }

        public void onVideoStateChanged(Conference c, int videoState) {
            String id = (String) ConnectionService.this.mIdByConference.get(c);
            Log.d((Object) this, "onVideoStateChanged set video state %d", Integer.valueOf(videoState));
            ConnectionService.this.mAdapter.setVideoState(id, videoState);
        }

        public void onVideoProviderChanged(Conference c, VideoProvider videoProvider) {
            String id = (String) ConnectionService.this.mIdByConference.get(c);
            Log.d((Object) this, "onVideoProviderChanged: Connection: %s, VideoProvider: %s", c, videoProvider);
            ConnectionService.this.mAdapter.setVideoProvider(id, videoProvider);
        }

        public void onStatusHintsChanged(Conference conference, StatusHints statusHints) {
            ConnectionService.this.mAdapter.setStatusHints((String) ConnectionService.this.mIdByConference.get(conference), statusHints);
        }

        public void onExtrasChanged(Conference conference, Bundle extras) {
            ConnectionService.this.mAdapter.setExtras((String) ConnectionService.this.mIdByConference.get(conference), extras);
        }
    };
    private final Map<String, Connection> mConnectionById = new ConcurrentHashMap();
    private final Connection.Listener mConnectionListener = new Connection.Listener() {
        public void onStateChanged(Connection c, int state) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "Adapter set state %s %s", id, Connection.stateToString(state));
            switch (state) {
                case 2:
                    ConnectionService.this.mAdapter.setRinging(id);
                    return;
                case 3:
                    ConnectionService.this.mAdapter.setDialing(id);
                    return;
                case 4:
                    ConnectionService.this.mAdapter.setActive(id);
                    return;
                case 5:
                    ConnectionService.this.mAdapter.setOnHold(id);
                    return;
                default:
                    return;
            }
        }

        public void onDisconnected(Connection c, DisconnectCause disconnectCause) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "Adapter set disconnected %s", disconnectCause);
            ConnectionService.this.mAdapter.setDisconnected(id, disconnectCause);
        }

        public void onVideoStateChanged(Connection c, int videoState) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "Adapter set video state %d", Integer.valueOf(videoState));
            ConnectionService.this.mAdapter.setVideoState(id, videoState);
        }

        public void onAddressChanged(Connection c, Uri address, int presentation) {
            ConnectionService.this.mAdapter.setAddress((String) ConnectionService.this.mIdByConnection.get(c), address, presentation);
        }

        public void onCallerDisplayNameChanged(Connection c, String callerDisplayName, int presentation) {
            ConnectionService.this.mAdapter.setCallerDisplayName((String) ConnectionService.this.mIdByConnection.get(c), callerDisplayName, presentation);
        }

        public void onDestroyed(Connection c) {
            ConnectionService.this.removeConnection(c);
        }

        public void onPostDialWait(Connection c, String remaining) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "Adapter onPostDialWait %s, %s", c, remaining);
            ConnectionService.this.mAdapter.onPostDialWait(id, remaining);
        }

        public void onPostDialChar(Connection c, char nextChar) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "Adapter onPostDialChar %s, %s", c, Character.valueOf(nextChar));
            ConnectionService.this.mAdapter.onPostDialChar(id, nextChar);
        }

        public void onRingbackRequested(Connection c, boolean ringback) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "Adapter onRingback %b", Boolean.valueOf(ringback));
            ConnectionService.this.mAdapter.setRingbackRequested(id, ringback);
        }

        public void onConnectionCapabilitiesChanged(Connection c, int capabilities) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "capabilities: parcelableconnection: %s", Connection.capabilitiesToString(capabilities));
            ConnectionService.this.mAdapter.setConnectionCapabilities(id, capabilities);
        }

        public void onVideoProviderChanged(Connection c, VideoProvider videoProvider) {
            String id = (String) ConnectionService.this.mIdByConnection.get(c);
            Log.d((Object) this, "onVideoProviderChanged: Connection: %s, VideoProvider: %s", c, videoProvider);
            ConnectionService.this.mAdapter.setVideoProvider(id, videoProvider);
        }

        public void onAudioModeIsVoipChanged(Connection c, boolean isVoip) {
            ConnectionService.this.mAdapter.setIsVoipAudioMode((String) ConnectionService.this.mIdByConnection.get(c), isVoip);
        }

        public void onStatusHintsChanged(Connection c, StatusHints statusHints) {
            ConnectionService.this.mAdapter.setStatusHints((String) ConnectionService.this.mIdByConnection.get(c), statusHints);
        }

        public void onConferenceablesChanged(Connection connection, List<Conferenceable> conferenceables) {
            ConnectionService.this.mAdapter.setConferenceableConnections((String) ConnectionService.this.mIdByConnection.get(connection), ConnectionService.this.createIdList(conferenceables));
        }

        public void onConferenceChanged(Connection connection, Conference conference) {
            String id = (String) ConnectionService.this.mIdByConnection.get(connection);
            if (id != null) {
                String conferenceId = null;
                if (conference != null) {
                    conferenceId = (String) ConnectionService.this.mIdByConference.get(conference);
                }
                ConnectionService.this.mAdapter.setIsConferenced(id, conferenceId);
            }
        }

        public void onConferenceMergeFailed(Connection connection) {
            String id = (String) ConnectionService.this.mIdByConnection.get(connection);
            if (id != null) {
                ConnectionService.this.mAdapter.onConferenceMergeFailed(id);
            }
        }

        public void onExtrasChanged(Connection connection, Bundle extras) {
            String id = (String) ConnectionService.this.mIdByConnection.get(connection);
            if (id != null) {
                ConnectionService.this.mAdapter.setExtras(id, extras);
            }
        }
    };
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            SomeArgs args;
            switch (msg.what) {
                case 1:
                    ConnectionService.this.mAdapter.addAdapter((IConnectionServiceAdapter) msg.obj);
                    ConnectionService.this.onAdapterAttached();
                    return;
                case 2:
                    args = msg.obj;
                    try {
                        final PhoneAccountHandle connectionManagerPhoneAccount = args.arg1;
                        final String id = args.arg2;
                        final ConnectionRequest request = args.arg3;
                        final boolean isIncoming = args.argi1 == 1;
                        final boolean isUnknown = args.argi2 == 1;
                        if (ConnectionService.this.mAreAccountsInitialized) {
                            ConnectionService.this.createConnection(connectionManagerPhoneAccount, id, request, isIncoming, isUnknown);
                        } else {
                            Log.d((Object) this, "Enqueueing pre-init request %s", id);
                            ConnectionService.this.mPreInitializationConnectionRequests.add(new Runnable() {
                                public void run() {
                                    ConnectionService.this.createConnection(connectionManagerPhoneAccount, id, request, isIncoming, isUnknown);
                                }
                            });
                        }
                        args.recycle();
                        return;
                    } catch (Throwable th) {
                        args.recycle();
                    }
                case 3:
                    ConnectionService.this.abort((String) msg.obj);
                    return;
                case 4:
                    ConnectionService.this.answer((String) msg.obj);
                    return;
                case 5:
                    ConnectionService.this.reject((String) msg.obj);
                    return;
                case 6:
                    ConnectionService.this.disconnect((String) msg.obj);
                    return;
                case 7:
                    ConnectionService.this.hold((String) msg.obj);
                    return;
                case 8:
                    ConnectionService.this.unhold((String) msg.obj);
                    return;
                case 9:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionService.this.onCallAudioStateChanged((String) args.arg1, new CallAudioState(args.arg2));
                        return;
                    } finally {
                        args.recycle();
                    }
                case 10:
                    ConnectionService.this.playDtmfTone((String) msg.obj, (char) msg.arg1);
                    return;
                case 11:
                    ConnectionService.this.stopDtmfTone((String) msg.obj);
                    return;
                case 12:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionService.this.conference(args.arg1, args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 13:
                    ConnectionService.this.splitFromConference((String) msg.obj);
                    return;
                case 14:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionService.this.onPostDialContinue((String) args.arg1, args.argi1 == 1);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 16:
                    ConnectionService.this.mAdapter.removeAdapter((IConnectionServiceAdapter) msg.obj);
                    return;
                case 17:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionService.this.answerVideo(args.arg1, args.argi1);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 18:
                    ConnectionService.this.mergeConference((String) msg.obj);
                    return;
                case 19:
                    ConnectionService.this.swapConference((String) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private final Map<Conference, String> mIdByConference = new ConcurrentHashMap();
    private final Map<Connection, String> mIdByConnection = new ConcurrentHashMap();
    private final List<Runnable> mPreInitializationConnectionRequests = new ArrayList();
    private final RemoteConnectionManager mRemoteConnectionManager = new RemoteConnectionManager(this);
    private Conference sNullConference;

    public final IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        endAllConnections();
        return super.onUnbind(intent);
    }

    private void createConnection(PhoneAccountHandle callManagerAccount, String callId, ConnectionRequest request, boolean isIncoming, boolean isUnknown) {
        IVideoProvider iVideoProvider;
        Log.d((Object) this, "createConnection, callManagerAccount: %s, callId: %s, request: %s, isIncoming: %b, isUnknown: %b", callManagerAccount, callId, request, Boolean.valueOf(isIncoming), Boolean.valueOf(isUnknown));
        Connection connection = isUnknown ? onCreateUnknownConnection(callManagerAccount, request) : isIncoming ? onCreateIncomingConnection(callManagerAccount, request) : onCreateOutgoingConnection(callManagerAccount, request);
        Log.d((Object) this, "createConnection, connection: %s", connection);
        if (connection == null) {
            connection = Connection.createFailedConnection(new DisconnectCause(1));
        }
        if (connection.getState() != 6) {
            addConnection(callId, connection);
        }
        Uri address = connection.getAddress();
        String number = address == null ? "null" : address.getSchemeSpecificPart();
        Log.v((Object) this, "createConnection, number: %s, state: %s, capabilities: %s", Connection.toLogSafePhoneNumber(number), Connection.stateToString(connection.getState()), Connection.capabilitiesToString(connection.getConnectionCapabilities()));
        Log.d((Object) this, "createConnection, calling handleCreateConnectionSuccessful %s", callId);
        ConnectionServiceAdapter connectionServiceAdapter = this.mAdapter;
        PhoneAccountHandle accountHandle = request.getAccountHandle();
        int state = connection.getState();
        int connectionCapabilities = connection.getConnectionCapabilities();
        Uri address2 = connection.getAddress();
        int addressPresentation = connection.getAddressPresentation();
        String callerDisplayName = connection.getCallerDisplayName();
        int callerDisplayNamePresentation = connection.getCallerDisplayNamePresentation();
        if (connection.getVideoProvider() == null) {
            iVideoProvider = null;
        } else {
            iVideoProvider = connection.getVideoProvider().getInterface();
        }
        connectionServiceAdapter.handleCreateConnectionComplete(callId, request, new ParcelableConnection(accountHandle, state, connectionCapabilities, address2, addressPresentation, callerDisplayName, callerDisplayNamePresentation, iVideoProvider, connection.getVideoState(), connection.isRingbackRequested(), connection.getAudioModeIsVoip(), connection.getConnectTimeMillis(), connection.getStatusHints(), connection.getDisconnectCause(), createIdList(connection.getConferenceables()), connection.getExtras()));
        if (isUnknown) {
            triggerConferenceRecalculate();
        }
    }

    private void abort(String callId) {
        Log.d((Object) this, "abort %s", callId);
        findConnectionForAction(callId, "abort").onAbort();
    }

    private void answerVideo(String callId, int videoState) {
        Log.d((Object) this, "answerVideo %s", callId);
        findConnectionForAction(callId, "answer").onAnswer(videoState);
    }

    private void answer(String callId) {
        Log.d((Object) this, "answer %s", callId);
        findConnectionForAction(callId, "answer").onAnswer();
    }

    private void reject(String callId) {
        Log.d((Object) this, "reject %s", callId);
        findConnectionForAction(callId, "reject").onReject();
    }

    private void disconnect(String callId) {
        Log.d((Object) this, "disconnect %s", callId);
        if (this.mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "disconnect").onDisconnect();
        } else {
            findConferenceForAction(callId, "disconnect").onDisconnect();
        }
    }

    private void hold(String callId) {
        Log.d((Object) this, "hold %s", callId);
        if (this.mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "hold").onHold();
        } else {
            findConferenceForAction(callId, "hold").onHold();
        }
    }

    private void unhold(String callId) {
        Log.d((Object) this, "unhold %s", callId);
        if (this.mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "unhold").onUnhold();
        } else {
            findConferenceForAction(callId, "unhold").onUnhold();
        }
    }

    private void onCallAudioStateChanged(String callId, CallAudioState callAudioState) {
        Log.d((Object) this, "onAudioStateChanged %s %s", callId, callAudioState);
        if (this.mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "onCallAudioStateChanged").setCallAudioState(callAudioState);
        } else {
            findConferenceForAction(callId, "onCallAudioStateChanged").setCallAudioState(callAudioState);
        }
    }

    private void playDtmfTone(String callId, char digit) {
        Log.d((Object) this, "playDtmfTone %s %c", callId, Character.valueOf(digit));
        if (this.mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "playDtmfTone").onPlayDtmfTone(digit);
        } else {
            findConferenceForAction(callId, "playDtmfTone").onPlayDtmfTone(digit);
        }
    }

    private void stopDtmfTone(String callId) {
        Log.d((Object) this, "stopDtmfTone %s", callId);
        if (this.mConnectionById.containsKey(callId)) {
            findConnectionForAction(callId, "stopDtmfTone").onStopDtmfTone();
        } else {
            findConferenceForAction(callId, "stopDtmfTone").onStopDtmfTone();
        }
    }

    private void conference(String callId1, String callId2) {
        Log.d((Object) this, "conference %s, %s", callId1, callId2);
        Connection connection2 = findConnectionForAction(callId2, ImsCallProfile.EXTRA_CONFERENCE);
        Conference conference2 = getNullConference();
        if (connection2 == getNullConnection()) {
            conference2 = findConferenceForAction(callId2, ImsCallProfile.EXTRA_CONFERENCE);
            if (conference2 == getNullConference()) {
                Log.w((Object) this, "Connection2 or Conference2 missing in conference request %s.", callId2);
                return;
            }
        }
        Connection connection1 = findConnectionForAction(callId1, ImsCallProfile.EXTRA_CONFERENCE);
        if (connection1 == getNullConnection()) {
            Conference conference1 = findConferenceForAction(callId1, "addConnection");
            if (conference1 == getNullConference()) {
                Log.w((Object) this, "Connection1 or Conference1 missing in conference request %s.", callId1);
            } else if (connection2 != getNullConnection()) {
                conference1.onMerge(connection2);
            } else {
                Log.wtf((Object) this, "There can only be one conference and an attempt was made to merge two conferences.", new Object[0]);
            }
        } else if (conference2 != getNullConference()) {
            conference2.onMerge(connection1);
        } else {
            onConference(connection1, connection2);
        }
    }

    private void splitFromConference(String callId) {
        Log.d((Object) this, "splitFromConference(%s)", callId);
        Connection connection = findConnectionForAction(callId, "splitFromConference");
        if (connection == getNullConnection()) {
            Log.w((Object) this, "Connection missing in conference request %s.", callId);
            return;
        }
        Conference conference = connection.getConference();
        if (conference != null) {
            conference.onSeparate(connection);
        }
    }

    private void mergeConference(String callId) {
        Log.d((Object) this, "mergeConference(%s)", callId);
        Conference conference = findConferenceForAction(callId, "mergeConference");
        if (conference != null) {
            conference.onMerge();
        }
    }

    private void swapConference(String callId) {
        Log.d((Object) this, "swapConference(%s)", callId);
        Conference conference = findConferenceForAction(callId, "swapConference");
        if (conference != null) {
            conference.onSwap();
        }
    }

    private void onPostDialContinue(String callId, boolean proceed) {
        Log.d((Object) this, "onPostDialContinue(%s)", callId);
        findConnectionForAction(callId, "stopDtmfTone").onPostDialContinue(proceed);
    }

    private void onAdapterAttached() {
        if (!this.mAreAccountsInitialized) {
            this.mAdapter.queryRemoteConnectionServices(new RemoteServiceCallback.Stub() {
                public void onResult(final List<ComponentName> componentNames, final List<IBinder> services) {
                    ConnectionService.this.mHandler.post(new Runnable() {
                        public void run() {
                            int i = 0;
                            while (i < componentNames.size() && i < services.size()) {
                                ConnectionService.this.mRemoteConnectionManager.addConnectionService((ComponentName) componentNames.get(i), Stub.asInterface((IBinder) services.get(i)));
                                i++;
                            }
                            ConnectionService.this.onAccountsInitialized();
                            Log.d((Object) this, "remote connection services found: " + services, new Object[0]);
                        }
                    });
                }

                public void onError() {
                    ConnectionService.this.mHandler.post(new Runnable() {
                        public void run() {
                            ConnectionService.this.mAreAccountsInitialized = true;
                        }
                    });
                }
            });
        }
    }

    public final RemoteConnection createRemoteIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        return this.mRemoteConnectionManager.createRemoteConnection(connectionManagerPhoneAccount, request, true);
    }

    public final RemoteConnection createRemoteOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        return this.mRemoteConnectionManager.createRemoteConnection(connectionManagerPhoneAccount, request, false);
    }

    public final void conferenceRemoteConnections(RemoteConnection remoteConnection1, RemoteConnection remoteConnection2) {
        this.mRemoteConnectionManager.conferenceRemoteConnections(remoteConnection1, remoteConnection2);
    }

    public final void addConference(Conference conference) {
        Log.d((Object) this, "addConference: conference=%s", conference);
        String id = addConferenceInternal(conference);
        if (id != null) {
            IVideoProvider iVideoProvider;
            List<String> connectionIds = new ArrayList(2);
            for (Connection connection : conference.getConnections()) {
                if (this.mIdByConnection.containsKey(connection)) {
                    connectionIds.add(this.mIdByConnection.get(connection));
                }
            }
            PhoneAccountHandle phoneAccountHandle = conference.getPhoneAccountHandle();
            int state = conference.getState();
            int connectionCapabilities = conference.getConnectionCapabilities();
            if (conference.getVideoProvider() == null) {
                iVideoProvider = null;
            } else {
                iVideoProvider = conference.getVideoProvider().getInterface();
            }
            this.mAdapter.addConferenceCall(id, new ParcelableConference(phoneAccountHandle, state, connectionCapabilities, connectionIds, iVideoProvider, conference.getVideoState(), conference.getConnectTimeMillis(), conference.getStatusHints(), conference.getExtras()));
            this.mAdapter.setVideoProvider(id, conference.getVideoProvider());
            this.mAdapter.setVideoState(id, conference.getVideoState());
            for (Connection connection2 : conference.getConnections()) {
                String connectionId = (String) this.mIdByConnection.get(connection2);
                if (connectionId != null) {
                    this.mAdapter.setIsConferenced(connectionId, id);
                }
            }
        }
    }

    public final void addExistingConnection(PhoneAccountHandle phoneAccountHandle, Connection connection) {
        String id = addExistingConnectionInternal(connection);
        if (id != null) {
            IVideoProvider iVideoProvider;
            List<String> arrayList = new ArrayList(0);
            int state = connection.getState();
            int connectionCapabilities = connection.getConnectionCapabilities();
            Uri address = connection.getAddress();
            int addressPresentation = connection.getAddressPresentation();
            String callerDisplayName = connection.getCallerDisplayName();
            int callerDisplayNamePresentation = connection.getCallerDisplayNamePresentation();
            if (connection.getVideoProvider() == null) {
                iVideoProvider = null;
            } else {
                iVideoProvider = connection.getVideoProvider().getInterface();
            }
            this.mAdapter.addExistingConnection(id, new ParcelableConnection(phoneAccountHandle, state, connectionCapabilities, address, addressPresentation, callerDisplayName, callerDisplayNamePresentation, iVideoProvider, connection.getVideoState(), connection.isRingbackRequested(), connection.getAudioModeIsVoip(), connection.getConnectTimeMillis(), connection.getStatusHints(), connection.getDisconnectCause(), arrayList, connection.getExtras()));
        }
    }

    public final Collection<Connection> getAllConnections() {
        return this.mConnectionById.values();
    }

    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        return null;
    }

    public void triggerConferenceRecalculate() {
    }

    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        return null;
    }

    public Connection onCreateUnknownConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        return null;
    }

    public void onConference(Connection connection1, Connection connection2) {
    }

    public void onRemoteConferenceAdded(RemoteConference conference) {
    }

    public void onRemoteExistingConnectionAdded(RemoteConnection connection) {
    }

    public boolean containsConference(Conference conference) {
        return this.mIdByConference.containsKey(conference);
    }

    void addRemoteConference(RemoteConference remoteConference) {
        onRemoteConferenceAdded(remoteConference);
    }

    void addRemoteExistingConnection(RemoteConnection remoteConnection) {
        onRemoteExistingConnectionAdded(remoteConnection);
    }

    private void onAccountsInitialized() {
        this.mAreAccountsInitialized = true;
        for (Runnable r : this.mPreInitializationConnectionRequests) {
            r.run();
        }
        this.mPreInitializationConnectionRequests.clear();
    }

    private String addExistingConnectionInternal(Connection connection) {
        String id = UUID.randomUUID().toString();
        addConnection(id, connection);
        return id;
    }

    private void addConnection(String callId, Connection connection) {
        this.mConnectionById.put(callId, connection);
        this.mIdByConnection.put(connection, callId);
        connection.addConnectionListener(this.mConnectionListener);
        connection.setConnectionService(this);
    }

    protected void removeConnection(Connection connection) {
        String id = (String) this.mIdByConnection.get(connection);
        connection.unsetConnectionService(this);
        connection.removeConnectionListener(this.mConnectionListener);
        this.mConnectionById.remove(this.mIdByConnection.get(connection));
        this.mIdByConnection.remove(connection);
        this.mAdapter.removeCall(id);
    }

    private String addConferenceInternal(Conference conference) {
        if (this.mIdByConference.containsKey(conference)) {
            Log.w((Object) this, "Re-adding an existing conference: %s.", conference);
        } else if (conference != null) {
            String id = UUID.randomUUID().toString();
            this.mConferenceById.put(id, conference);
            this.mIdByConference.put(conference, id);
            conference.addListener(this.mConferenceListener);
            return id;
        }
        return null;
    }

    private void removeConference(Conference conference) {
        if (this.mIdByConference.containsKey(conference)) {
            conference.removeListener(this.mConferenceListener);
            String id = (String) this.mIdByConference.get(conference);
            this.mConferenceById.remove(id);
            this.mIdByConference.remove(conference);
            this.mAdapter.removeCall(id);
        }
    }

    private Connection findConnectionForAction(String callId, String action) {
        if (this.mConnectionById.containsKey(callId)) {
            return (Connection) this.mConnectionById.get(callId);
        }
        Log.w((Object) this, "%s - Cannot find Connection %s", action, callId);
        return getNullConnection();
    }

    static synchronized Connection getNullConnection() {
        Connection connection;
        synchronized (ConnectionService.class) {
            if (sNullConnection == null) {
                sNullConnection = new Connection() {
                };
            }
            connection = sNullConnection;
        }
        return connection;
    }

    private Conference findConferenceForAction(String conferenceId, String action) {
        if (this.mConferenceById.containsKey(conferenceId)) {
            return (Conference) this.mConferenceById.get(conferenceId);
        }
        Log.w((Object) this, "%s - Cannot find conference %s", action, conferenceId);
        return getNullConference();
    }

    private List<String> createConnectionIdList(List<Connection> connections) {
        List<String> ids = new ArrayList();
        for (Connection c : connections) {
            if (this.mIdByConnection.containsKey(c)) {
                ids.add(this.mIdByConnection.get(c));
            }
        }
        Collections.sort(ids);
        return ids;
    }

    private List<String> createIdList(List<Conferenceable> conferenceables) {
        List<String> ids = new ArrayList();
        for (Conferenceable c : conferenceables) {
            if (c instanceof Connection) {
                Connection connection = (Connection) c;
                if (this.mIdByConnection.containsKey(connection)) {
                    ids.add(this.mIdByConnection.get(connection));
                }
            } else if (c instanceof Conference) {
                Conference conference = (Conference) c;
                if (this.mIdByConference.containsKey(conference)) {
                    ids.add(this.mIdByConference.get(conference));
                }
            }
        }
        Collections.sort(ids);
        return ids;
    }

    private Conference getNullConference() {
        if (this.sNullConference == null) {
            this.sNullConference = new Conference(null) {
            };
        }
        return this.sNullConference;
    }

    private void endAllConnections() {
        for (Connection connection : this.mIdByConnection.keySet()) {
            if (connection.getConference() == null) {
                connection.onDisconnect();
            }
        }
        for (Conference conference : this.mIdByConference.keySet()) {
            conference.onDisconnect();
        }
    }
}
