package android.telecom;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telecom.Phone.Listener;
import android.telecom.VideoProfile.CameraCapabilities;
import android.view.Surface;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IInCallAdapter;
import com.android.internal.telecom.IInCallService.Stub;
import java.util.Collections;
import java.util.List;

public abstract class InCallService extends Service {
    private static final int MSG_ADD_CALL = 2;
    private static final int MSG_BRING_TO_FOREGROUND = 6;
    private static final int MSG_ON_CALL_AUDIO_STATE_CHANGED = 5;
    private static final int MSG_ON_CAN_ADD_CALL_CHANGED = 7;
    private static final int MSG_ON_CHANGE_IN_CONTENT = 8;
    private static final int MSG_SET_IN_CALL_ADAPTER = 1;
    private static final int MSG_SET_POST_DIAL_WAIT = 4;
    private static final int MSG_UPDATE_CALL = 3;
    public static final String SERVICE_INTERFACE = "android.telecom.InCallService";
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            boolean z = true;
            if (InCallService.this.mPhone != null || msg.what == 1) {
                Phone access$000;
                switch (msg.what) {
                    case 1:
                        InCallService.this.mPhone = new Phone(new InCallAdapter((IInCallAdapter) msg.obj));
                        InCallService.this.mPhone.addListener(InCallService.this.mPhoneListener);
                        InCallService.this.onPhoneCreated(InCallService.this.mPhone);
                        return;
                    case 2:
                        InCallService.this.mPhone.internalAddCall((ParcelableCall) msg.obj);
                        return;
                    case 3:
                        InCallService.this.mPhone.internalUpdateCall((ParcelableCall) msg.obj);
                        return;
                    case 4:
                        SomeArgs args = msg.obj;
                        try {
                            InCallService.this.mPhone.internalSetPostDialWait(args.arg1, args.arg2);
                            return;
                        } finally {
                            args.recycle();
                        }
                    case 5:
                        InCallService.this.mPhone.internalCallAudioStateChanged((CallAudioState) msg.obj);
                        return;
                    case 6:
                        access$000 = InCallService.this.mPhone;
                        if (msg.arg1 != 1) {
                            z = false;
                        }
                        access$000.internalBringToForeground(z);
                        return;
                    case 7:
                        access$000 = InCallService.this.mPhone;
                        if (msg.arg1 != 1) {
                            z = false;
                        }
                        access$000.internalSetCanAddCall(z);
                        return;
                    case 8:
                        InCallService.this.mPhone.internalChangeInContent(((Integer) msg.obj).intValue());
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private Phone mPhone;
    private Listener mPhoneListener = new Listener() {
        public void onAudioStateChanged(Phone phone, AudioState audioState) {
            InCallService.this.onAudioStateChanged(audioState);
        }

        public void onCallAudioStateChanged(Phone phone, CallAudioState callAudioState) {
            InCallService.this.onCallAudioStateChanged(callAudioState);
        }

        public void onBringToForeground(Phone phone, boolean showDialpad) {
            InCallService.this.onBringToForeground(showDialpad);
        }

        public void onCallAdded(Phone phone, Call call) {
            InCallService.this.onCallAdded(call);
        }

        public void onCallRemoved(Phone phone, Call call) {
            InCallService.this.onCallRemoved(call);
        }

        public void onCanAddCallChanged(Phone phone, boolean canAddCall) {
            InCallService.this.onCanAddCallChanged(canAddCall);
        }
    };

    private final class InCallServiceBinder extends Stub {
        private InCallServiceBinder() {
        }

        public void setInCallAdapter(IInCallAdapter inCallAdapter) {
            InCallService.this.mHandler.obtainMessage(1, inCallAdapter).sendToTarget();
        }

        public void addCall(ParcelableCall call) {
            InCallService.this.mHandler.obtainMessage(2, call).sendToTarget();
        }

        public void updateCall(ParcelableCall call) {
            InCallService.this.mHandler.obtainMessage(3, call).sendToTarget();
        }

        public void setPostDial(String callId, String remaining) {
        }

        public void setPostDialWait(String callId, String remaining) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = callId;
            args.arg2 = remaining;
            InCallService.this.mHandler.obtainMessage(4, args).sendToTarget();
        }

        public void onCallAudioStateChanged(CallAudioState callAudioState) {
            InCallService.this.mHandler.obtainMessage(5, callAudioState).sendToTarget();
        }

        public void onChangeInContent(int contentObserver) {
            InCallService.this.mHandler.obtainMessage(8, Integer.valueOf(contentObserver)).sendToTarget();
        }

        public void bringToForeground(boolean showDialpad) {
            int i;
            Handler access$200 = InCallService.this.mHandler;
            if (showDialpad) {
                i = 1;
            } else {
                i = 0;
            }
            access$200.obtainMessage(6, i, 0).sendToTarget();
        }

        public void onCanAddCallChanged(boolean canAddCall) {
            int i;
            Handler access$200 = InCallService.this.mHandler;
            if (canAddCall) {
                i = 1;
            } else {
                i = 0;
            }
            access$200.obtainMessage(7, i, 0).sendToTarget();
        }
    }

    public static abstract class VideoCall {

        public static abstract class Callback {
            public abstract void onCallDataUsageChanged(long j);

            public abstract void onCallSessionEvent(int i);

            public abstract void onCameraCapabilitiesChanged(CameraCapabilities cameraCapabilities);

            public abstract void onPeerDimensionsChanged(int i, int i2);

            public abstract void onSessionModifyRequestReceived(VideoProfile videoProfile);

            public abstract void onSessionModifyResponseReceived(int i, VideoProfile videoProfile, VideoProfile videoProfile2);

            public abstract void onVideoQualityChanged(int i);
        }

        public abstract void destroy();

        public abstract void registerCallback(Callback callback);

        public abstract void registerCallback(Callback callback, Handler handler);

        public abstract void requestCallDataUsage();

        public abstract void requestCameraCapabilities();

        public abstract void sendSessionModifyRequest(VideoProfile videoProfile);

        public abstract void sendSessionModifyResponse(VideoProfile videoProfile);

        public abstract void setCamera(String str);

        public abstract void setDeviceOrientation(int i);

        public abstract void setDisplaySurface(Surface surface);

        public abstract void setPauseImage(Uri uri);

        public abstract void setPreviewSurface(Surface surface);

        public abstract void setZoom(float f);

        public abstract void unregisterCallback(Callback callback);
    }

    public IBinder onBind(Intent intent) {
        return new InCallServiceBinder();
    }

    public boolean onUnbind(Intent intent) {
        if (this.mPhone != null) {
            Phone oldPhone = this.mPhone;
            this.mPhone = null;
            oldPhone.destroy();
            oldPhone.removeListener(this.mPhoneListener);
            onPhoneDestroyed(oldPhone);
        }
        return false;
    }

    @Deprecated
    public Phone getPhone() {
        return this.mPhone;
    }

    public final List<Call> getCalls() {
        return this.mPhone == null ? Collections.emptyList() : this.mPhone.getCalls();
    }

    public final boolean canAddCall() {
        return this.mPhone == null ? false : this.mPhone.canAddCall();
    }

    @Deprecated
    public final AudioState getAudioState() {
        return this.mPhone == null ? null : this.mPhone.getAudioState();
    }

    public final CallAudioState getCallAudioState() {
        return this.mPhone == null ? null : this.mPhone.getCallAudioState();
    }

    public final void setMuted(boolean state) {
        if (this.mPhone != null) {
            this.mPhone.setMuted(state);
        }
    }

    public final void setAudioRoute(int route) {
        if (this.mPhone != null) {
            this.mPhone.setAudioRoute(route);
        }
    }

    @Deprecated
    public void onPhoneCreated(Phone phone) {
    }

    @Deprecated
    public void onPhoneDestroyed(Phone phone) {
    }

    @Deprecated
    public void onAudioStateChanged(AudioState audioState) {
    }

    public void onCallAudioStateChanged(CallAudioState audioState) {
    }

    public void onBringToForeground(boolean showDialpad) {
    }

    public void onCallAdded(Call call) {
    }

    public void onCallRemoved(Call call) {
    }

    public void onCanAddCallChanged(boolean canAddCall) {
    }
}
