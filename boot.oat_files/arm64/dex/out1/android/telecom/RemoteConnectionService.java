package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.telecom.RemoteConference.Callback;
import android.telecom.RemoteConnection.VideoProvider;
import com.android.internal.telecom.IConnectionService;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

final class RemoteConnectionService {
    private static final RemoteConference NULL_CONFERENCE = new RemoteConference("NULL", null);
    private static final RemoteConnection NULL_CONNECTION = new RemoteConnection("NULL", null, null);
    private final Map<String, RemoteConference> mConferenceById = new HashMap();
    private final Map<String, RemoteConnection> mConnectionById = new HashMap();
    private final DeathRecipient mDeathRecipient = new DeathRecipient() {
        public void binderDied() {
            for (RemoteConnection c : RemoteConnectionService.this.mConnectionById.values()) {
                c.setDestroyed();
            }
            for (RemoteConference c2 : RemoteConnectionService.this.mConferenceById.values()) {
                c2.setDestroyed();
            }
            RemoteConnectionService.this.mConnectionById.clear();
            RemoteConnectionService.this.mConferenceById.clear();
            RemoteConnectionService.this.mPendingConnections.clear();
            RemoteConnectionService.this.mOutgoingConnectionServiceRpc.asBinder().unlinkToDeath(RemoteConnectionService.this.mDeathRecipient, 0);
        }
    };
    private final ConnectionService mOurConnectionServiceImpl;
    private final IConnectionService mOutgoingConnectionServiceRpc;
    private final Set<RemoteConnection> mPendingConnections = new HashSet();
    private final ConnectionServiceAdapterServant mServant = new ConnectionServiceAdapterServant(this.mServantDelegate);
    private final IConnectionServiceAdapter mServantDelegate = new IConnectionServiceAdapter() {
        public void handleCreateConnectionComplete(String id, ConnectionRequest request, ParcelableConnection parcel) {
            RemoteConnection connection = RemoteConnectionService.this.findConnectionForAction(id, "handleCreateConnectionSuccessful");
            if (connection != RemoteConnectionService.NULL_CONNECTION && RemoteConnectionService.this.mPendingConnections.contains(connection)) {
                RemoteConnectionService.this.mPendingConnections.remove(connection);
                connection.setConnectionCapabilities(parcel.getConnectionCapabilities());
                if (!(parcel.getHandle() == null && parcel.getState() == 6)) {
                    connection.setAddress(parcel.getHandle(), parcel.getHandlePresentation());
                }
                if (!(parcel.getCallerDisplayName() == null && parcel.getState() == 6)) {
                    connection.setCallerDisplayName(parcel.getCallerDisplayName(), parcel.getCallerDisplayNamePresentation());
                }
                if (parcel.getState() == 6) {
                    connection.setDisconnected(parcel.getDisconnectCause());
                } else {
                    connection.setState(parcel.getState());
                }
                List<RemoteConnection> conferenceable = new ArrayList();
                for (String confId : parcel.getConferenceableConnectionIds()) {
                    if (RemoteConnectionService.this.mConnectionById.containsKey(confId)) {
                        conferenceable.add(RemoteConnectionService.this.mConnectionById.get(confId));
                    }
                }
                connection.setConferenceableConnections(conferenceable);
                connection.setVideoState(parcel.getVideoState());
                if (connection.getState() == 6) {
                    connection.setDestroyed();
                }
            }
        }

        public void setActive(String callId) {
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "setActive").setState(4);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, "setActive").setState(4);
            }
        }

        public void setRinging(String callId) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setRinging").setState(2);
        }

        public void setDialing(String callId) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setDialing").setState(3);
        }

        public void setDisconnected(String callId, DisconnectCause disconnectCause) {
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "setDisconnected").setDisconnected(disconnectCause);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, "setDisconnected").setDisconnected(disconnectCause);
            }
        }

        public void setOnHold(String callId) {
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "setOnHold").setState(5);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, "setOnHold").setState(5);
            }
        }

        public void setRingbackRequested(String callId, boolean ringing) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setRingbackRequested").setRingbackRequested(ringing);
        }

        public void setConnectionCapabilities(String callId, int connectionCapabilities) {
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "setConnectionCapabilities").setConnectionCapabilities(connectionCapabilities);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, "setConnectionCapabilities").setConnectionCapabilities(connectionCapabilities);
            }
        }

        public void setIsConferenced(String callId, String conferenceCallId) {
            RemoteConnection connection = RemoteConnectionService.this.findConnectionForAction(callId, "setIsConferenced");
            if (connection == RemoteConnectionService.NULL_CONNECTION) {
                return;
            }
            if (conferenceCallId != null) {
                RemoteConference conference = RemoteConnectionService.this.findConferenceForAction(conferenceCallId, "setIsConferenced");
                if (conference != RemoteConnectionService.NULL_CONFERENCE) {
                    conference.addConnection(connection);
                }
            } else if (connection.getConference() != null) {
                connection.getConference().removeConnection(connection);
            }
        }

        public void setConferenceMergeFailed(String callId) {
        }

        public void addConferenceCall(final String callId, ParcelableConference parcel) {
            RemoteConference conference = new RemoteConference(callId, RemoteConnectionService.this.mOutgoingConnectionServiceRpc);
            for (String id : parcel.getConnectionIds()) {
                RemoteConnection c = (RemoteConnection) RemoteConnectionService.this.mConnectionById.get(id);
                if (c != null) {
                    conference.addConnection(c);
                }
            }
            if (conference.getConnections().size() != 0) {
                conference.setState(parcel.getState());
                conference.setConnectionCapabilities(parcel.getConnectionCapabilities());
                RemoteConnectionService.this.mConferenceById.put(callId, conference);
                conference.registerCallback(new Callback() {
                    public void onDestroyed(RemoteConference c) {
                        RemoteConnectionService.this.mConferenceById.remove(callId);
                        RemoteConnectionService.this.maybeDisconnectAdapter();
                    }
                });
                RemoteConnectionService.this.mOurConnectionServiceImpl.addRemoteConference(conference);
            }
        }

        public void removeCall(String callId) {
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "removeCall").setDestroyed();
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, "removeCall").setDestroyed();
            }
        }

        public void onPostDialWait(String callId, String remaining) {
            RemoteConnectionService.this.findConnectionForAction(callId, "onPostDialWait").setPostDialWait(remaining);
        }

        public void onPostDialChar(String callId, char nextChar) {
            RemoteConnectionService.this.findConnectionForAction(callId, "onPostDialChar").onPostDialChar(nextChar);
        }

        public void queryRemoteConnectionServices(RemoteServiceCallback callback) {
        }

        public void setVideoProvider(String callId, IVideoProvider videoProvider) {
            VideoProvider remoteVideoProvider = null;
            if (videoProvider != null) {
                remoteVideoProvider = new VideoProvider(videoProvider);
            }
            RemoteConnectionService.this.findConnectionForAction(callId, "setVideoProvider").setVideoProvider(remoteVideoProvider);
        }

        public void setVideoState(String callId, int videoState) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setVideoState").setVideoState(videoState);
        }

        public void setIsVoipAudioMode(String callId, boolean isVoip) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setIsVoipAudioMode").setIsVoipAudioMode(isVoip);
        }

        public void setStatusHints(String callId, StatusHints statusHints) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setStatusHints").setStatusHints(statusHints);
        }

        public void setAddress(String callId, Uri address, int presentation) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setAddress").setAddress(address, presentation);
        }

        public void setCallerDisplayName(String callId, String callerDisplayName, int presentation) {
            RemoteConnectionService.this.findConnectionForAction(callId, "setCallerDisplayName").setCallerDisplayName(callerDisplayName, presentation);
        }

        public IBinder asBinder() {
            throw new UnsupportedOperationException();
        }

        public final void setConferenceableConnections(String callId, List<String> conferenceableConnectionIds) {
            List<RemoteConnection> conferenceable = new ArrayList();
            for (String id : conferenceableConnectionIds) {
                if (RemoteConnectionService.this.mConnectionById.containsKey(id)) {
                    conferenceable.add(RemoteConnectionService.this.mConnectionById.get(id));
                }
            }
            if (RemoteConnectionService.this.hasConnection(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "setConferenceableConnections").setConferenceableConnections(conferenceable);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, "setConferenceableConnections").setConferenceableConnections(conferenceable);
            }
        }

        public void addExistingConnection(String callId, ParcelableConnection connection) {
            RemoteConnectionService.this.mOurConnectionServiceImpl.addRemoteExistingConnection(new RemoteConnection(callId, RemoteConnectionService.this.mOutgoingConnectionServiceRpc, connection));
        }

        public void setExtras(String callId, Bundle extras) {
            if (RemoteConnectionService.this.mConnectionById.containsKey(callId)) {
                RemoteConnectionService.this.findConnectionForAction(callId, "setExtras").setExtras(extras);
            } else {
                RemoteConnectionService.this.findConferenceForAction(callId, "setExtras").setExtras(extras);
            }
        }
    };

    RemoteConnectionService(IConnectionService outgoingConnectionServiceRpc, ConnectionService ourConnectionServiceImpl) throws RemoteException {
        this.mOutgoingConnectionServiceRpc = outgoingConnectionServiceRpc;
        this.mOutgoingConnectionServiceRpc.asBinder().linkToDeath(this.mDeathRecipient, 0);
        this.mOurConnectionServiceImpl = ourConnectionServiceImpl;
    }

    public String toString() {
        return "[RemoteCS - " + this.mOutgoingConnectionServiceRpc.asBinder().toString() + "]";
    }

    final RemoteConnection createRemoteConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request, boolean isIncoming) {
        final String id = UUID.randomUUID().toString();
        ConnectionRequest newRequest = new ConnectionRequest(request.getAccountHandle(), request.getAddress(), request.getExtras(), request.getVideoState());
        try {
            if (this.mConnectionById.isEmpty()) {
                this.mOutgoingConnectionServiceRpc.addConnectionServiceAdapter(this.mServant.getStub());
            }
            RemoteConnection connection = new RemoteConnection(id, this.mOutgoingConnectionServiceRpc, newRequest);
            this.mPendingConnections.add(connection);
            this.mConnectionById.put(id, connection);
            this.mOutgoingConnectionServiceRpc.createConnection(connectionManagerPhoneAccount, id, newRequest, isIncoming, false);
            connection.registerCallback(new RemoteConnection.Callback() {
                public void onDestroyed(RemoteConnection connection) {
                    RemoteConnectionService.this.mConnectionById.remove(id);
                    RemoteConnectionService.this.maybeDisconnectAdapter();
                }
            });
            return connection;
        } catch (RemoteException e) {
            return RemoteConnection.failure(new DisconnectCause(1, e.toString()));
        }
    }

    private boolean hasConnection(String callId) {
        return this.mConnectionById.containsKey(callId);
    }

    private RemoteConnection findConnectionForAction(String callId, String action) {
        if (this.mConnectionById.containsKey(callId)) {
            return (RemoteConnection) this.mConnectionById.get(callId);
        }
        Log.w((Object) this, "%s - Cannot find Connection %s", action, callId);
        return NULL_CONNECTION;
    }

    private RemoteConference findConferenceForAction(String callId, String action) {
        if (this.mConferenceById.containsKey(callId)) {
            return (RemoteConference) this.mConferenceById.get(callId);
        }
        Log.w((Object) this, "%s - Cannot find Conference %s", action, callId);
        return NULL_CONFERENCE;
    }

    private void maybeDisconnectAdapter() {
        if (this.mConnectionById.isEmpty() && this.mConferenceById.isEmpty()) {
            try {
                this.mOutgoingConnectionServiceRpc.removeConnectionServiceAdapter(this.mServant.getStub());
            } catch (RemoteException e) {
            }
        }
    }
}
