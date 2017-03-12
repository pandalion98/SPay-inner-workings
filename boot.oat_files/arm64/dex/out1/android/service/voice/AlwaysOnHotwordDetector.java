package android.service.voice;

import android.content.Intent;
import android.hardware.soundtrigger.IRecognitionStatusCallback.Stub;
import android.hardware.soundtrigger.KeyphraseEnrollmentInfo;
import android.hardware.soundtrigger.KeyphraseMetadata;
import android.hardware.soundtrigger.SoundTrigger.ConfidenceLevel;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionExtra;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.media.AudioFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.app.IVoiceInteractionManagerService;
import java.io.PrintWriter;
import java.util.Locale;

public class AlwaysOnHotwordDetector {
    static final boolean DBG = false;
    public static final int MANAGE_ACTION_ENROLL = 0;
    public static final int MANAGE_ACTION_RE_ENROLL = 1;
    public static final int MANAGE_ACTION_UN_ENROLL = 2;
    private static final int MSG_AVAILABILITY_CHANGED = 1;
    private static final int MSG_DETECTION_ERROR = 3;
    private static final int MSG_DETECTION_PAUSE = 4;
    private static final int MSG_DETECTION_RESUME = 5;
    private static final int MSG_HOTWORD_DETECTED = 2;
    public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 2;
    public static final int RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO = 1;
    public static final int RECOGNITION_FLAG_NONE = 0;
    public static final int RECOGNITION_MODE_USER_IDENTIFICATION = 2;
    public static final int RECOGNITION_MODE_VOICE_TRIGGER = 1;
    public static final int STATE_HARDWARE_UNAVAILABLE = -2;
    private static final int STATE_INVALID = -3;
    public static final int STATE_KEYPHRASE_ENROLLED = 2;
    public static final int STATE_KEYPHRASE_UNENROLLED = 1;
    public static final int STATE_KEYPHRASE_UNSUPPORTED = -1;
    private static final int STATE_NOT_READY = 0;
    private static final int STATUS_ERROR = Integer.MIN_VALUE;
    private static final int STATUS_OK = 0;
    static final String TAG = "AlwaysOnHotwordDetector";
    private int mAvailability = 0;
    private final Callback mExternalCallback;
    private final Handler mHandler;
    private final SoundTriggerListener mInternalCallback;
    private final KeyphraseEnrollmentInfo mKeyphraseEnrollmentInfo;
    private final KeyphraseMetadata mKeyphraseMetadata;
    private final Locale mLocale;
    private final Object mLock = new Object();
    private final IVoiceInteractionManagerService mModelManagementService;
    private final String mText;
    private final IVoiceInteractionService mVoiceInteractionService;

    public static abstract class Callback {
        public abstract void onAvailabilityChanged(int i);

        public abstract void onDetected(EventPayload eventPayload);

        public abstract void onError();

        public abstract void onRecognitionPaused();

        public abstract void onRecognitionResumed();
    }

    public static class EventPayload {
        private final AudioFormat mAudioFormat;
        private final boolean mCaptureAvailable;
        private final int mCaptureSession;
        private final byte[] mData;
        private final boolean mTriggerAvailable;

        private EventPayload(boolean triggerAvailable, boolean captureAvailable, AudioFormat audioFormat, int captureSession, byte[] data) {
            this.mTriggerAvailable = triggerAvailable;
            this.mCaptureAvailable = captureAvailable;
            this.mCaptureSession = captureSession;
            this.mAudioFormat = audioFormat;
            this.mData = data;
        }

        public AudioFormat getCaptureAudioFormat() {
            return this.mAudioFormat;
        }

        public byte[] getTriggerAudio() {
            if (this.mTriggerAvailable) {
                return this.mData;
            }
            return null;
        }

        public Integer getCaptureSession() {
            if (this.mCaptureAvailable) {
                return Integer.valueOf(this.mCaptureSession);
            }
            return null;
        }
    }

    class MyHandler extends Handler {
        MyHandler() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r5) {
            /*
            r4 = this;
            r0 = android.service.voice.AlwaysOnHotwordDetector.this;
            r1 = r0.mLock;
            monitor-enter(r1);
            r0 = android.service.voice.AlwaysOnHotwordDetector.this;	 Catch:{ all -> 0x003c }
            r0 = r0.mAvailability;	 Catch:{ all -> 0x003c }
            r2 = -3;
            if (r0 != r2) goto L_0x0032;
        L_0x0010:
            r0 = "AlwaysOnHotwordDetector";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x003c }
            r2.<init>();	 Catch:{ all -> 0x003c }
            r3 = "Received message: ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x003c }
            r3 = r5.what;	 Catch:{ all -> 0x003c }
            r2 = r2.append(r3);	 Catch:{ all -> 0x003c }
            r3 = " for an invalid detector";
            r2 = r2.append(r3);	 Catch:{ all -> 0x003c }
            r2 = r2.toString();	 Catch:{ all -> 0x003c }
            android.util.Slog.w(r0, r2);	 Catch:{ all -> 0x003c }
            monitor-exit(r1);	 Catch:{ all -> 0x003c }
        L_0x0031:
            return;
        L_0x0032:
            monitor-exit(r1);	 Catch:{ all -> 0x003c }
            r0 = r5.what;
            switch(r0) {
                case 1: goto L_0x003f;
                case 2: goto L_0x004b;
                case 3: goto L_0x0059;
                case 4: goto L_0x0063;
                case 5: goto L_0x006d;
                default: goto L_0x0038;
            };
        L_0x0038:
            super.handleMessage(r5);
            goto L_0x0031;
        L_0x003c:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x003c }
            throw r0;
        L_0x003f:
            r0 = android.service.voice.AlwaysOnHotwordDetector.this;
            r0 = r0.mExternalCallback;
            r1 = r5.arg1;
            r0.onAvailabilityChanged(r1);
            goto L_0x0031;
        L_0x004b:
            r0 = android.service.voice.AlwaysOnHotwordDetector.this;
            r1 = r0.mExternalCallback;
            r0 = r5.obj;
            r0 = (android.service.voice.AlwaysOnHotwordDetector.EventPayload) r0;
            r1.onDetected(r0);
            goto L_0x0031;
        L_0x0059:
            r0 = android.service.voice.AlwaysOnHotwordDetector.this;
            r0 = r0.mExternalCallback;
            r0.onError();
            goto L_0x0031;
        L_0x0063:
            r0 = android.service.voice.AlwaysOnHotwordDetector.this;
            r0 = r0.mExternalCallback;
            r0.onRecognitionPaused();
            goto L_0x0031;
        L_0x006d:
            r0 = android.service.voice.AlwaysOnHotwordDetector.this;
            r0 = r0.mExternalCallback;
            r0.onRecognitionResumed();
            goto L_0x0031;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.service.voice.AlwaysOnHotwordDetector.MyHandler.handleMessage(android.os.Message):void");
        }
    }

    class RefreshAvailabiltyTask extends AsyncTask<Void, Void, Void> {
        RefreshAvailabiltyTask() {
        }

        public Void doInBackground(Void... params) {
            int availability = internalGetInitialAvailability();
            if (availability == 0 || availability == 1 || availability == 2) {
                if (internalGetIsEnrolled(AlwaysOnHotwordDetector.this.mKeyphraseMetadata.id, AlwaysOnHotwordDetector.this.mLocale)) {
                    availability = 2;
                } else {
                    availability = 1;
                }
            }
            synchronized (AlwaysOnHotwordDetector.this.mLock) {
                AlwaysOnHotwordDetector.this.mAvailability = availability;
                AlwaysOnHotwordDetector.this.notifyStateChangedLocked();
            }
            return null;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int internalGetInitialAvailability() {
            /*
            r5 = this;
            r2 = -3;
            r3 = android.service.voice.AlwaysOnHotwordDetector.this;
            r3 = r3.mLock;
            monitor-enter(r3);
            r4 = android.service.voice.AlwaysOnHotwordDetector.this;	 Catch:{ all -> 0x0028 }
            r4 = r4.mAvailability;	 Catch:{ all -> 0x0028 }
            if (r4 != r2) goto L_0x0012;
        L_0x0010:
            monitor-exit(r3);	 Catch:{ all -> 0x0028 }
        L_0x0011:
            return r2;
        L_0x0012:
            monitor-exit(r3);	 Catch:{ all -> 0x0028 }
            r0 = 0;
            r2 = android.service.voice.AlwaysOnHotwordDetector.this;	 Catch:{ RemoteException -> 0x002b }
            r2 = r2.mModelManagementService;	 Catch:{ RemoteException -> 0x002b }
            r3 = android.service.voice.AlwaysOnHotwordDetector.this;	 Catch:{ RemoteException -> 0x002b }
            r3 = r3.mVoiceInteractionService;	 Catch:{ RemoteException -> 0x002b }
            r0 = r2.getDspModuleProperties(r3);	 Catch:{ RemoteException -> 0x002b }
        L_0x0024:
            if (r0 != 0) goto L_0x0034;
        L_0x0026:
            r2 = -2;
            goto L_0x0011;
        L_0x0028:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0028 }
            throw r2;
        L_0x002b:
            r1 = move-exception;
            r2 = "AlwaysOnHotwordDetector";
            r3 = "RemoteException in getDspProperties!";
            android.util.Slog.w(r2, r3, r1);
            goto L_0x0024;
        L_0x0034:
            r2 = android.service.voice.AlwaysOnHotwordDetector.this;
            r2 = r2.mKeyphraseMetadata;
            if (r2 != 0) goto L_0x003e;
        L_0x003c:
            r2 = -1;
            goto L_0x0011;
        L_0x003e:
            r2 = 0;
            goto L_0x0011;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.service.voice.AlwaysOnHotwordDetector.RefreshAvailabiltyTask.internalGetInitialAvailability():int");
        }

        private boolean internalGetIsEnrolled(int keyphraseId, Locale locale) {
            try {
                return AlwaysOnHotwordDetector.this.mModelManagementService.isEnrolledForKeyphrase(AlwaysOnHotwordDetector.this.mVoiceInteractionService, keyphraseId, locale.toLanguageTag());
            } catch (RemoteException e) {
                Slog.w(AlwaysOnHotwordDetector.TAG, "RemoteException in listRegisteredKeyphraseSoundModels!", e);
                return false;
            }
        }
    }

    static final class SoundTriggerListener extends Stub {
        private final Handler mHandler;

        public SoundTriggerListener(Handler handler) {
            this.mHandler = handler;
        }

        public void onDetected(KeyphraseRecognitionEvent event) {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onDetected");
            Message.obtain(this.mHandler, 2, new EventPayload(event.triggerInData, event.captureAvailable, event.captureFormat, event.captureSession, event.data)).sendToTarget();
        }

        public void onError(int status) {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onError: " + status);
            this.mHandler.sendEmptyMessage(3);
        }

        public void onRecognitionPaused() {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onRecognitionPaused");
            this.mHandler.sendEmptyMessage(4);
        }

        public void onRecognitionResumed() {
            Slog.i(AlwaysOnHotwordDetector.TAG, "onRecognitionResumed");
            this.mHandler.sendEmptyMessage(5);
        }
    }

    public AlwaysOnHotwordDetector(String text, Locale locale, Callback callback, KeyphraseEnrollmentInfo keyphraseEnrollmentInfo, IVoiceInteractionService voiceInteractionService, IVoiceInteractionManagerService modelManagementService) {
        this.mText = text;
        this.mLocale = locale;
        this.mKeyphraseEnrollmentInfo = keyphraseEnrollmentInfo;
        this.mKeyphraseMetadata = this.mKeyphraseEnrollmentInfo.getKeyphraseMetadata(text, locale);
        this.mExternalCallback = callback;
        this.mHandler = new MyHandler();
        this.mInternalCallback = new SoundTriggerListener(this.mHandler);
        this.mVoiceInteractionService = voiceInteractionService;
        this.mModelManagementService = modelManagementService;
        new RefreshAvailabiltyTask().execute(new Void[0]);
    }

    public int getSupportedRecognitionModes() {
        int supportedRecognitionModesLocked;
        synchronized (this.mLock) {
            supportedRecognitionModesLocked = getSupportedRecognitionModesLocked();
        }
        return supportedRecognitionModesLocked;
    }

    private int getSupportedRecognitionModesLocked() {
        if (this.mAvailability == -3) {
            throw new IllegalStateException("getSupportedRecognitionModes called on an invalid detector");
        } else if (this.mAvailability == 2 || this.mAvailability == 1) {
            return this.mKeyphraseMetadata.recognitionModeFlags;
        } else {
            throw new UnsupportedOperationException("Getting supported recognition modes for the keyphrase is not supported");
        }
    }

    public boolean startRecognition(int recognitionFlags) {
        boolean z;
        synchronized (this.mLock) {
            if (this.mAvailability == -3) {
                throw new IllegalStateException("startRecognition called on an invalid detector");
            } else if (this.mAvailability != 2) {
                throw new UnsupportedOperationException("Recognition for the given keyphrase is not supported");
            } else {
                z = startRecognitionLocked(recognitionFlags) == 0;
            }
        }
        return z;
    }

    public boolean stopRecognition() {
        boolean z;
        synchronized (this.mLock) {
            if (this.mAvailability == -3) {
                throw new IllegalStateException("stopRecognition called on an invalid detector");
            } else if (this.mAvailability != 2) {
                throw new UnsupportedOperationException("Recognition for the given keyphrase is not supported");
            } else {
                z = stopRecognitionLocked() == 0;
            }
        }
        return z;
    }

    public Intent createEnrollIntent() {
        Intent manageIntentLocked;
        synchronized (this.mLock) {
            manageIntentLocked = getManageIntentLocked(0);
        }
        return manageIntentLocked;
    }

    public Intent createUnEnrollIntent() {
        Intent manageIntentLocked;
        synchronized (this.mLock) {
            manageIntentLocked = getManageIntentLocked(2);
        }
        return manageIntentLocked;
    }

    public Intent createReEnrollIntent() {
        Intent manageIntentLocked;
        synchronized (this.mLock) {
            manageIntentLocked = getManageIntentLocked(1);
        }
        return manageIntentLocked;
    }

    private Intent getManageIntentLocked(int action) {
        if (this.mAvailability == -3) {
            throw new IllegalStateException("getManageIntent called on an invalid detector");
        } else if (this.mAvailability == 2 || this.mAvailability == 1) {
            return this.mKeyphraseEnrollmentInfo.getManageKeyphraseIntent(action, this.mText, this.mLocale);
        } else {
            throw new UnsupportedOperationException("Managing the given keyphrase is not supported");
        }
    }

    void invalidate() {
        synchronized (this.mLock) {
            this.mAvailability = -3;
            notifyStateChangedLocked();
        }
    }

    void onSoundModelsChanged() {
        synchronized (this.mLock) {
            if (this.mAvailability == -3 || this.mAvailability == -2 || this.mAvailability == -1) {
                Slog.w(TAG, "Received onSoundModelsChanged for an unsupported keyphrase/config");
                return;
            }
            stopRecognitionLocked();
            new RefreshAvailabiltyTask().execute(new Void[0]);
        }
    }

    private int startRecognitionLocked(int recognitionFlags) {
        boolean captureTriggerAudio;
        boolean allowMultipleTriggers = true;
        KeyphraseRecognitionExtra[] recognitionExtra = new KeyphraseRecognitionExtra[]{new KeyphraseRecognitionExtra(this.mKeyphraseMetadata.id, this.mKeyphraseMetadata.recognitionModeFlags, 0, new ConfidenceLevel[0])};
        if ((recognitionFlags & 1) != 0) {
            captureTriggerAudio = true;
        } else {
            captureTriggerAudio = false;
        }
        if ((recognitionFlags & 2) == 0) {
            allowMultipleTriggers = false;
        }
        int code = Integer.MIN_VALUE;
        try {
            code = this.mModelManagementService.startRecognition(this.mVoiceInteractionService, this.mKeyphraseMetadata.id, this.mLocale.toLanguageTag(), this.mInternalCallback, new RecognitionConfig(captureTriggerAudio, allowMultipleTriggers, recognitionExtra, null));
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException in startRecognition!", e);
        }
        if (code != 0) {
            Slog.w(TAG, "startRecognition() failed with error code " + code);
        }
        return code;
    }

    private int stopRecognitionLocked() {
        int code = Integer.MIN_VALUE;
        try {
            code = this.mModelManagementService.stopRecognition(this.mVoiceInteractionService, this.mKeyphraseMetadata.id, this.mInternalCallback);
        } catch (RemoteException e) {
            Slog.w(TAG, "RemoteException in stopRecognition!", e);
        }
        if (code != 0) {
            Slog.w(TAG, "stopRecognition() failed with error code " + code);
        }
        return code;
    }

    private void notifyStateChangedLocked() {
        Message message = Message.obtain(this.mHandler, 1);
        message.arg1 = this.mAvailability;
        message.sendToTarget();
    }

    public void dump(String prefix, PrintWriter pw) {
        synchronized (this.mLock) {
            pw.print(prefix);
            pw.print("Text=");
            pw.println(this.mText);
            pw.print(prefix);
            pw.print("Locale=");
            pw.println(this.mLocale);
            pw.print(prefix);
            pw.print("Availability=");
            pw.println(this.mAvailability);
            pw.print(prefix);
            pw.print("KeyphraseMetadata=");
            pw.println(this.mKeyphraseMetadata);
            pw.print(prefix);
            pw.print("EnrollmentInfo=");
            pw.println(this.mKeyphraseEnrollmentInfo);
        }
    }
}
