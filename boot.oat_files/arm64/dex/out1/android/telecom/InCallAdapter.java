package android.telecom;

import android.os.RemoteException;
import com.android.internal.telecom.IInCallAdapter;

public final class InCallAdapter {
    private final IInCallAdapter mAdapter;

    public InCallAdapter(IInCallAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void answerCall(String callId, int videoState) {
        try {
            this.mAdapter.answerCall(callId, videoState);
        } catch (RemoteException e) {
        }
    }

    public void rejectCall(String callId, boolean rejectWithMessage, String textMessage) {
        try {
            this.mAdapter.rejectCall(callId, rejectWithMessage, textMessage);
        } catch (RemoteException e) {
        }
    }

    public void disconnectCall(String callId) {
        try {
            this.mAdapter.disconnectCall(callId);
        } catch (RemoteException e) {
        }
    }

    public void holdCall(String callId) {
        try {
            this.mAdapter.holdCall(callId);
        } catch (RemoteException e) {
        }
    }

    public void unholdCall(String callId) {
        try {
            this.mAdapter.unholdCall(callId);
        } catch (RemoteException e) {
        }
    }

    public void mute(boolean shouldMute) {
        try {
            this.mAdapter.mute(shouldMute);
        } catch (RemoteException e) {
        }
    }

    public void requestRcsObserver(int rcsObserver, int registerUnregister) {
        try {
            this.mAdapter.requestRcsObserver(rcsObserver, registerUnregister);
        } catch (RemoteException e) {
        }
    }

    public void setAudioRoute(int route) {
        try {
            this.mAdapter.setAudioRoute(route);
        } catch (RemoteException e) {
        }
    }

    public void playDtmfTone(String callId, char digit) {
        try {
            this.mAdapter.playDtmfTone(callId, digit);
        } catch (RemoteException e) {
        }
    }

    public void stopDtmfTone(String callId) {
        try {
            this.mAdapter.stopDtmfTone(callId);
        } catch (RemoteException e) {
        }
    }

    public void postDialContinue(String callId, boolean proceed) {
        try {
            this.mAdapter.postDialContinue(callId, proceed);
        } catch (RemoteException e) {
        }
    }

    public void phoneAccountSelected(String callId, PhoneAccountHandle accountHandle, boolean setDefault) {
        try {
            this.mAdapter.phoneAccountSelected(callId, accountHandle, setDefault);
        } catch (RemoteException e) {
        }
    }

    public void conference(String callId, String otherCallId) {
        try {
            this.mAdapter.conference(callId, otherCallId);
        } catch (RemoteException e) {
        }
    }

    public void splitFromConference(String callId) {
        try {
            this.mAdapter.splitFromConference(callId);
        } catch (RemoteException e) {
        }
    }

    public void mergeConference(String callId) {
        try {
            this.mAdapter.mergeConference(callId);
        } catch (RemoteException e) {
        }
    }

    public void swapConference(String callId) {
        try {
            this.mAdapter.swapConference(callId);
        } catch (RemoteException e) {
        }
    }

    public void turnProximitySensorOn() {
        try {
            this.mAdapter.turnOnProximitySensor();
        } catch (RemoteException e) {
        }
    }

    public void turnProximitySensorOff(boolean screenOnImmediately) {
        try {
            this.mAdapter.turnOffProximitySensor(screenOnImmediately);
        } catch (RemoteException e) {
        }
    }
}
