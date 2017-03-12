package android.media.audiopolicy;

import android.content.Context;
import android.media.AudioFocusInfo;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.IAudioService;
import android.media.audiopolicy.IAudioPolicyCallback.Stub;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class AudioPolicy {
    private static final boolean DEBUG = false;
    public static final int FOCUS_POLICY_DUCKING_DEFAULT = 0;
    public static final int FOCUS_POLICY_DUCKING_IN_APP = 0;
    public static final int FOCUS_POLICY_DUCKING_IN_POLICY = 1;
    private static final int MSG_FOCUS_GRANT = 1;
    private static final int MSG_FOCUS_LOSS = 2;
    private static final int MSG_MIX_STATE_UPDATE = 3;
    private static final int MSG_POLICY_STATUS_CHANGE = 0;
    public static final int POLICY_STATUS_REGISTERED = 2;
    public static final int POLICY_STATUS_UNREGISTERED = 1;
    private static final String TAG = "AudioPolicy";
    private static IAudioService sService;
    private AudioPolicyConfig mConfig;
    private Context mContext;
    private final EventHandler mEventHandler;
    private AudioPolicyFocusListener mFocusListener;
    private final Object mLock;
    private final IAudioPolicyCallback mPolicyCb;
    private String mRegistrationId;
    private int mStatus;
    private AudioPolicyStatusListener mStatusListener;

    public static abstract class AudioPolicyFocusListener {
        public void onAudioFocusGrant(AudioFocusInfo afi, int requestResult) {
        }

        public void onAudioFocusLoss(AudioFocusInfo afi, boolean wasNotified) {
        }
    }

    public static abstract class AudioPolicyStatusListener {
        public void onStatusChange() {
        }

        public void onMixStateUpdate(AudioMix mix) {
        }
    }

    public static class Builder {
        private Context mContext;
        private AudioPolicyFocusListener mFocusListener;
        private Looper mLooper;
        private ArrayList<AudioMix> mMixes = new ArrayList();
        private AudioPolicyStatusListener mStatusListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder addMix(AudioMix mix) throws IllegalArgumentException {
            if (mix == null) {
                throw new IllegalArgumentException("Illegal null AudioMix argument");
            }
            this.mMixes.add(mix);
            return this;
        }

        public Builder setLooper(Looper looper) throws IllegalArgumentException {
            if (looper == null) {
                throw new IllegalArgumentException("Illegal null Looper argument");
            }
            this.mLooper = looper;
            return this;
        }

        public void setAudioPolicyFocusListener(AudioPolicyFocusListener l) {
            this.mFocusListener = l;
        }

        public void setAudioPolicyStatusListener(AudioPolicyStatusListener l) {
            this.mStatusListener = l;
        }

        public AudioPolicy build() {
            if (this.mStatusListener != null) {
                Iterator i$ = this.mMixes.iterator();
                while (i$.hasNext()) {
                    AudioMix mix = (AudioMix) i$.next();
                    mix.mCallbackFlags |= 1;
                }
            }
            return new AudioPolicy(new AudioPolicyConfig(this.mMixes), this.mContext, this.mLooper, this.mFocusListener, this.mStatusListener);
        }
    }

    private class EventHandler extends Handler {
        public EventHandler(AudioPolicy ap, Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    AudioPolicy.this.onPolicyStatusChange();
                    return;
                case 1:
                    if (AudioPolicy.this.mFocusListener != null) {
                        AudioPolicy.this.mFocusListener.onAudioFocusGrant((AudioFocusInfo) msg.obj, msg.arg1);
                        return;
                    }
                    return;
                case 2:
                    if (AudioPolicy.this.mFocusListener != null) {
                        AudioPolicy.this.mFocusListener.onAudioFocusLoss((AudioFocusInfo) msg.obj, msg.arg1 != 0);
                        return;
                    }
                    return;
                case 3:
                    if (AudioPolicy.this.mStatusListener != null) {
                        AudioPolicy.this.mStatusListener.onMixStateUpdate((AudioMix) msg.obj);
                        return;
                    }
                    return;
                default:
                    Log.e(AudioPolicy.TAG, "Unknown event " + msg.what);
                    return;
            }
        }
    }

    public AudioPolicyConfig getConfig() {
        return this.mConfig;
    }

    public boolean hasFocusListener() {
        return this.mFocusListener != null;
    }

    private AudioPolicy(AudioPolicyConfig config, Context context, Looper looper, AudioPolicyFocusListener fl, AudioPolicyStatusListener sl) {
        this.mLock = new Object();
        this.mPolicyCb = new Stub() {
            public void notifyAudioFocusGrant(AudioFocusInfo afi, int requestResult) {
                AudioPolicy.this.sendMsg(1, afi, requestResult);
            }

            public void notifyAudioFocusLoss(AudioFocusInfo afi, boolean wasNotified) {
                AudioPolicy.this.sendMsg(2, afi, wasNotified ? 1 : 0);
            }

            public void notifyMixStateUpdate(String regId, int state) {
                Iterator i$ = AudioPolicy.this.mConfig.getMixes().iterator();
                while (i$.hasNext()) {
                    AudioMix mix = (AudioMix) i$.next();
                    if (mix.getRegistration().equals(regId)) {
                        mix.mMixState = state;
                        AudioPolicy.this.sendMsg(3, mix, 0);
                    }
                }
            }
        };
        this.mConfig = config;
        this.mStatus = 1;
        this.mContext = context;
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            this.mEventHandler = null;
            Log.e(TAG, "No event handler due to looper without a thread");
        }
        this.mFocusListener = fl;
        this.mStatusListener = sl;
    }

    public void setRegistration(String regId) {
        synchronized (this.mLock) {
            this.mRegistrationId = regId;
            this.mConfig.setRegistration(regId);
            if (regId != null) {
                this.mStatus = 2;
            } else {
                this.mStatus = 1;
            }
        }
        sendMsg(0);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean policyReadyToUse() {
        /*
        r4 = this;
        r0 = 0;
        r1 = r4.mLock;
        monitor-enter(r1);
        r2 = r4.mStatus;	 Catch:{ all -> 0x001f }
        r3 = 2;
        if (r2 == r3) goto L_0x0012;
    L_0x0009:
        r2 = "AudioPolicy";
        r3 = "Cannot use unregistered AudioPolicy";
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x001f }
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
    L_0x0011:
        return r0;
    L_0x0012:
        r2 = r4.mContext;	 Catch:{ all -> 0x001f }
        if (r2 != 0) goto L_0x0022;
    L_0x0016:
        r2 = "AudioPolicy";
        r3 = "Cannot use AudioPolicy without context";
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x001f }
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        goto L_0x0011;
    L_0x001f:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        throw r0;
    L_0x0022:
        r2 = r4.mRegistrationId;	 Catch:{ all -> 0x001f }
        if (r2 != 0) goto L_0x002f;
    L_0x0026:
        r2 = "AudioPolicy";
        r3 = "Cannot use unregistered AudioPolicy";
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x001f }
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        goto L_0x0011;
    L_0x002f:
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        r1 = r4.mContext;
        r2 = "android.permission.MODIFY_AUDIO_ROUTING";
        r1 = r1.checkCallingOrSelfPermission(r2);
        if (r1 == 0) goto L_0x006b;
    L_0x003a:
        r1 = "AudioPolicy";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Cannot use AudioPolicy for pid ";
        r2 = r2.append(r3);
        r3 = android.os.Binder.getCallingPid();
        r2 = r2.append(r3);
        r3 = " / uid ";
        r2 = r2.append(r3);
        r3 = android.os.Binder.getCallingUid();
        r2 = r2.append(r3);
        r3 = ", needs MODIFY_AUDIO_ROUTING";
        r2 = r2.append(r3);
        r2 = r2.toString();
        android.util.Slog.w(r1, r2);
        goto L_0x0011;
    L_0x006b:
        r0 = 1;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.audiopolicy.AudioPolicy.policyReadyToUse():boolean");
    }

    private void checkMixReadyToUse(AudioMix mix, boolean forTrack) throws IllegalArgumentException {
        if (mix == null) {
            throw new IllegalArgumentException(forTrack ? "Invalid null AudioMix for AudioTrack creation" : "Invalid null AudioMix for AudioRecord creation");
        } else if (!this.mConfig.mMixes.contains(mix)) {
            throw new IllegalArgumentException("Invalid mix: not part of this policy");
        } else if ((mix.getRouteFlags() & 2) != 2) {
            throw new IllegalArgumentException("Invalid AudioMix: not defined for loop back");
        } else if (forTrack && mix.getMixType() != 1) {
            throw new IllegalArgumentException("Invalid AudioMix: not defined for being a recording source");
        } else if (!forTrack && mix.getMixType() != 0) {
            throw new IllegalArgumentException("Invalid AudioMix: not defined for capturing playback");
        }
    }

    public int getFocusDuckingBehavior() {
        return this.mConfig.mDuckingPolicy;
    }

    public int setFocusDuckingBehavior(int behavior) throws IllegalArgumentException, IllegalStateException {
        if (behavior == 0 || behavior == 1) {
            int status;
            synchronized (this.mLock) {
                if (this.mStatus != 2) {
                    throw new IllegalStateException("Cannot change ducking behavior for unregistered policy");
                }
                if (behavior == 1) {
                    if (this.mFocusListener == null) {
                        throw new IllegalStateException("Cannot handle ducking without an audio focus listener");
                    }
                }
                try {
                    status = getService().setFocusPropertiesForPolicy(behavior, cb());
                    if (status == 0) {
                        this.mConfig.mDuckingPolicy = behavior;
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Dead object in setFocusPropertiesForPolicy for behavior", e);
                    status = -1;
                }
            }
            return status;
        }
        throw new IllegalArgumentException("Invalid ducking behavior " + behavior);
    }

    public AudioRecord createAudioRecordSink(AudioMix mix) throws IllegalArgumentException {
        if (policyReadyToUse()) {
            checkMixReadyToUse(mix, false);
            return new AudioRecord(new android.media.AudioAttributes.Builder().setInternalCapturePreset(8).addTag(addressForTag(mix)).build(), new android.media.AudioFormat.Builder(mix.getFormat()).setChannelMask(AudioFormat.inChannelMaskFromOutChannelMask(mix.getFormat().getChannelMask())).build(), AudioRecord.getMinBufferSize(mix.getFormat().getSampleRate(), 12, mix.getFormat().getEncoding()), 0);
        }
        Log.e(TAG, "Cannot create AudioRecord sink for AudioMix");
        return null;
    }

    public AudioTrack createAudioTrackSource(AudioMix mix) throws IllegalArgumentException {
        if (policyReadyToUse()) {
            checkMixReadyToUse(mix, true);
            return new AudioTrack(new android.media.AudioAttributes.Builder().setUsage(15).addTag(addressForTag(mix)).build(), mix.getFormat(), AudioTrack.getMinBufferSize(mix.getFormat().getSampleRate(), mix.getFormat().getChannelMask(), mix.getFormat().getEncoding()), 1, 0);
        }
        Log.e(TAG, "Cannot create AudioTrack source for AudioMix");
        return null;
    }

    public int getStatus() {
        return this.mStatus;
    }

    private void onPolicyStatusChange() {
        synchronized (this.mLock) {
            if (this.mStatusListener == null) {
                return;
            }
            AudioPolicyStatusListener l = this.mStatusListener;
            l.onStatusChange();
        }
    }

    public IAudioPolicyCallback cb() {
        return this.mPolicyCb;
    }

    private static String addressForTag(AudioMix mix) {
        return "addr=" + mix.getRegistration();
    }

    private void sendMsg(int msg) {
        if (this.mEventHandler != null) {
            this.mEventHandler.sendEmptyMessage(msg);
        }
    }

    private void sendMsg(int msg, Object obj, int i) {
        if (this.mEventHandler != null) {
            this.mEventHandler.sendMessage(this.mEventHandler.obtainMessage(msg, i, 0, obj));
        }
    }

    private static IAudioService getService() {
        if (sService != null) {
            return sService;
        }
        sService = IAudioService.Stub.asInterface(ServiceManager.getService(Context.AUDIO_SERVICE));
        return sService;
    }

    public String toLogFriendlyString() {
        return new String("android.media.audiopolicy.AudioPolicy:\n") + "config=" + this.mConfig.toLogFriendlyString();
    }
}
