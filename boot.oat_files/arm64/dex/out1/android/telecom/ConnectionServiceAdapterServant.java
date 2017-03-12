package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IConnectionServiceAdapter.Stub;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback;
import java.util.List;

final class ConnectionServiceAdapterServant {
    private static final int MSG_ADD_CONFERENCE_CALL = 10;
    private static final int MSG_ADD_EXISTING_CONNECTION = 21;
    private static final int MSG_HANDLE_CREATE_CONNECTION_COMPLETE = 1;
    private static final int MSG_ON_POST_DIAL_CHAR = 22;
    private static final int MSG_ON_POST_DIAL_WAIT = 12;
    private static final int MSG_QUERY_REMOTE_CALL_SERVICES = 13;
    private static final int MSG_REMOVE_CALL = 11;
    private static final int MSG_SET_ACTIVE = 2;
    private static final int MSG_SET_ADDRESS = 18;
    private static final int MSG_SET_CALLER_DISPLAY_NAME = 19;
    private static final int MSG_SET_CONFERENCEABLE_CONNECTIONS = 20;
    private static final int MSG_SET_CONFERENCE_MERGE_FAILED = 23;
    private static final int MSG_SET_CONNECTION_CAPABILITIES = 8;
    private static final int MSG_SET_DIALING = 4;
    private static final int MSG_SET_DISCONNECTED = 5;
    private static final int MSG_SET_EXTRAS = 24;
    private static final int MSG_SET_IS_CONFERENCED = 9;
    private static final int MSG_SET_IS_VOIP_AUDIO_MODE = 16;
    private static final int MSG_SET_ON_HOLD = 6;
    private static final int MSG_SET_RINGBACK_REQUESTED = 7;
    private static final int MSG_SET_RINGING = 3;
    private static final int MSG_SET_STATUS_HINTS = 17;
    private static final int MSG_SET_VIDEO_CALL_PROVIDER = 15;
    private static final int MSG_SET_VIDEO_STATE = 14;
    private final IConnectionServiceAdapter mDelegate;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                internalHandleMessage(msg);
            } catch (RemoteException e) {
            }
        }

        private void internalHandleMessage(Message msg) throws RemoteException {
            boolean z = true;
            SomeArgs args;
            IConnectionServiceAdapter access$000;
            String str;
            switch (msg.what) {
                case 1:
                    args = msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.handleCreateConnectionComplete((String) args.arg1, (ConnectionRequest) args.arg2, (ParcelableConnection) args.arg3);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 2:
                    ConnectionServiceAdapterServant.this.mDelegate.setActive((String) msg.obj);
                    return;
                case 3:
                    ConnectionServiceAdapterServant.this.mDelegate.setRinging((String) msg.obj);
                    return;
                case 4:
                    ConnectionServiceAdapterServant.this.mDelegate.setDialing((String) msg.obj);
                    return;
                case 5:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setDisconnected((String) args.arg1, (DisconnectCause) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 6:
                    ConnectionServiceAdapterServant.this.mDelegate.setOnHold((String) msg.obj);
                    return;
                case 7:
                    access$000 = ConnectionServiceAdapterServant.this.mDelegate;
                    str = (String) msg.obj;
                    if (msg.arg1 != 1) {
                        z = false;
                    }
                    access$000.setRingbackRequested(str, z);
                    return;
                case 8:
                    ConnectionServiceAdapterServant.this.mDelegate.setConnectionCapabilities((String) msg.obj, msg.arg1);
                    return;
                case 9:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setIsConferenced((String) args.arg1, (String) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 10:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.addConferenceCall((String) args.arg1, (ParcelableConference) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 11:
                    ConnectionServiceAdapterServant.this.mDelegate.removeCall((String) msg.obj);
                    return;
                case 12:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.onPostDialWait((String) args.arg1, (String) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 13:
                    ConnectionServiceAdapterServant.this.mDelegate.queryRemoteConnectionServices((RemoteServiceCallback) msg.obj);
                    return;
                case 14:
                    ConnectionServiceAdapterServant.this.mDelegate.setVideoState((String) msg.obj, msg.arg1);
                    return;
                case 15:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setVideoProvider((String) args.arg1, (IVideoProvider) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 16:
                    access$000 = ConnectionServiceAdapterServant.this.mDelegate;
                    str = (String) msg.obj;
                    if (msg.arg1 != 1) {
                        z = false;
                    }
                    access$000.setIsVoipAudioMode(str, z);
                    return;
                case 17:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setStatusHints((String) args.arg1, (StatusHints) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 18:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setAddress((String) args.arg1, (Uri) args.arg2, args.argi1);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 19:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setCallerDisplayName((String) args.arg1, (String) args.arg2, args.argi1);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 20:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setConferenceableConnections((String) args.arg1, (List) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 21:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.addExistingConnection((String) args.arg1, (ParcelableConnection) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 22:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.onPostDialChar((String) args.arg1, (char) args.argi1);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 23:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setConferenceMergeFailed((String) args.arg1);
                        return;
                    } finally {
                        args.recycle();
                    }
                case 24:
                    args = (SomeArgs) msg.obj;
                    try {
                        ConnectionServiceAdapterServant.this.mDelegate.setExtras((String) args.arg1, (Bundle) args.arg2);
                        return;
                    } finally {
                        args.recycle();
                    }
                default:
                    return;
            }
        }
    };
    private final IConnectionServiceAdapter mStub = new Stub() {
        public void handleCreateConnectionComplete(String id, ConnectionRequest request, ParcelableConnection connection) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = id;
            args.arg2 = request;
            args.arg3 = connection;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(1, args).sendToTarget();
        }

        public void setActive(String connectionId) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(2, connectionId).sendToTarget();
        }

        public void setRinging(String connectionId) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(3, connectionId).sendToTarget();
        }

        public void setDialing(String connectionId) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(4, connectionId).sendToTarget();
        }

        public void setDisconnected(String connectionId, DisconnectCause disconnectCause) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = disconnectCause;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(5, args).sendToTarget();
        }

        public void setOnHold(String connectionId) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(6, connectionId).sendToTarget();
        }

        public void setRingbackRequested(String connectionId, boolean ringback) {
            int i;
            Handler access$100 = ConnectionServiceAdapterServant.this.mHandler;
            if (ringback) {
                i = 1;
            } else {
                i = 0;
            }
            access$100.obtainMessage(7, i, 0, connectionId).sendToTarget();
        }

        public void setConnectionCapabilities(String connectionId, int connectionCapabilities) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(8, connectionCapabilities, 0, connectionId).sendToTarget();
        }

        public void setConferenceMergeFailed(String callId) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(23, args).sendToTarget();
        }

        public void setIsConferenced(String callId, String conferenceCallId) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = conferenceCallId;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(9, args).sendToTarget();
        }

        public void addConferenceCall(String callId, ParcelableConference parcelableConference) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = parcelableConference;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(10, args).sendToTarget();
        }

        public void removeCall(String connectionId) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(11, connectionId).sendToTarget();
        }

        public void onPostDialWait(String connectionId, String remainingDigits) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = remainingDigits;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(12, args).sendToTarget();
        }

        public void onPostDialChar(String connectionId, char nextChar) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.argi1 = nextChar;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(22, args).sendToTarget();
        }

        public void queryRemoteConnectionServices(RemoteServiceCallback callback) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(13, callback).sendToTarget();
        }

        public void setVideoState(String connectionId, int videoState) {
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(14, videoState, 0, connectionId).sendToTarget();
        }

        public void setVideoProvider(String connectionId, IVideoProvider videoProvider) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = videoProvider;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(15, args).sendToTarget();
        }

        public final void setIsVoipAudioMode(String connectionId, boolean isVoip) {
            int i;
            Handler access$100 = ConnectionServiceAdapterServant.this.mHandler;
            if (isVoip) {
                i = 1;
            } else {
                i = 0;
            }
            access$100.obtainMessage(16, i, 0, connectionId).sendToTarget();
        }

        public final void setStatusHints(String connectionId, StatusHints statusHints) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = statusHints;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(17, args).sendToTarget();
        }

        public final void setAddress(String connectionId, Uri address, int presentation) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = address;
            args.argi1 = presentation;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(18, args).sendToTarget();
        }

        public final void setCallerDisplayName(String connectionId, String callerDisplayName, int presentation) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = callerDisplayName;
            args.argi1 = presentation;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(19, args).sendToTarget();
        }

        public final void setConferenceableConnections(String connectionId, List<String> conferenceableConnectionIds) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = conferenceableConnectionIds;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(20, args).sendToTarget();
        }

        public final void addExistingConnection(String connectionId, ParcelableConnection connection) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = connection;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(21, args).sendToTarget();
        }

        public final void setExtras(String connectionId, Bundle extras) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = connectionId;
            args.arg2 = extras;
            ConnectionServiceAdapterServant.this.mHandler.obtainMessage(24, args).sendToTarget();
        }
    };

    public ConnectionServiceAdapterServant(IConnectionServiceAdapter delegate) {
        this.mDelegate = delegate;
    }

    public IConnectionServiceAdapter getStub() {
        return this.mStub;
    }
}
