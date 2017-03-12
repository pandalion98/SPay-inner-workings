package android.speech.tts;

import android.util.Log;

class PlaybackSynthesisCallback extends AbstractSynthesisCallback {
    private static final boolean DBG = false;
    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;
    private static final String TAG = "PlaybackSynthesisRequest";
    private final AudioOutputParams mAudioParams;
    private final AudioPlaybackHandler mAudioTrackHandler;
    private final Object mCallerIdentity;
    private final UtteranceProgressDispatcher mDispatcher;
    private volatile boolean mDone = false;
    private SynthesisPlaybackQueueItem mItem = null;
    private final AbstractEventLogger mLogger;
    private final Object mStateLock = new Object();
    protected int mStatusCode;

    PlaybackSynthesisCallback(AudioOutputParams audioParams, AudioPlaybackHandler audioTrackHandler, UtteranceProgressDispatcher dispatcher, Object callerIdentity, AbstractEventLogger logger, boolean clientIsUsingV2) {
        super(clientIsUsingV2);
        this.mAudioParams = audioParams;
        this.mAudioTrackHandler = audioTrackHandler;
        this.mDispatcher = dispatcher;
        this.mCallerIdentity = callerIdentity;
        this.mLogger = logger;
        this.mStatusCode = 0;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void stop() {
        /*
        r4 = this;
        r3 = -2;
        r2 = r4.mStateLock;
        monitor-enter(r2);
        r1 = r4.mDone;	 Catch:{ all -> 0x0018 }
        if (r1 == 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
    L_0x0009:
        return;
    L_0x000a:
        r1 = r4.mStatusCode;	 Catch:{ all -> 0x0018 }
        if (r1 != r3) goto L_0x001b;
    L_0x000e:
        r1 = "PlaybackSynthesisRequest";
        r3 = "stop() called twice";
        android.util.Log.w(r1, r3);	 Catch:{ all -> 0x0018 }
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
        goto L_0x0009;
    L_0x0018:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
        throw r1;
    L_0x001b:
        r0 = r4.mItem;	 Catch:{ all -> 0x0018 }
        r1 = -2;
        r4.mStatusCode = r1;	 Catch:{ all -> 0x0018 }
        monitor-exit(r2);	 Catch:{ all -> 0x0018 }
        if (r0 == 0) goto L_0x0027;
    L_0x0023:
        r0.stop(r3);
        goto L_0x0009;
    L_0x0027:
        r1 = r4.mLogger;
        r1.onCompleted(r3);
        r1 = r4.mDispatcher;
        r1.dispatchOnStop();
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.PlaybackSynthesisCallback.stop():void");
    }

    public int getMaxBufferSize() {
        return 8192;
    }

    public boolean hasStarted() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mItem != null;
        }
        return z;
    }

    public boolean hasFinished() {
        boolean z;
        synchronized (this.mStateLock) {
            z = this.mDone;
        }
        return z;
    }

    public int start(int sampleRateInHz, int audioFormat, int channelCount) {
        int channelConfig = BlockingAudioTrack.getChannelConfig(channelCount);
        synchronized (this.mStateLock) {
            if (channelConfig == 0) {
                Log.e(TAG, "Unsupported number of channels :" + channelCount);
                this.mStatusCode = -5;
                return -1;
            } else if (this.mStatusCode == -2) {
                int errorCodeOnStop = errorCodeOnStop();
                return errorCodeOnStop;
            } else if (this.mStatusCode != 0) {
                return -1;
            } else if (this.mItem != null) {
                Log.e(TAG, "Start called twice");
                return -1;
            } else {
                SynthesisPlaybackQueueItem item = new SynthesisPlaybackQueueItem(this.mAudioParams, sampleRateInHz, audioFormat, channelCount, this.mDispatcher, this.mCallerIdentity, this.mLogger);
                this.mAudioTrackHandler.enqueue(item);
                this.mItem = item;
                return 0;
            }
        }
    }

    public int audioAvailable(byte[] buffer, int offset, int length) {
        if (length > getMaxBufferSize() || length <= 0) {
            throw new IllegalArgumentException("buffer is too large or of zero length (" + length + " bytes)");
        }
        synchronized (this.mStateLock) {
            if (this.mItem == null) {
                this.mStatusCode = -5;
                return -1;
            } else if (this.mStatusCode != 0) {
                return -1;
            } else if (this.mStatusCode == -2) {
                int errorCodeOnStop = errorCodeOnStop();
                return errorCodeOnStop;
            } else {
                SynthesisPlaybackQueueItem item = this.mItem;
                byte[] bufferCopy = new byte[length];
                System.arraycopy(buffer, offset, bufferCopy, 0, length);
                try {
                    item.put(bufferCopy);
                    this.mLogger.onEngineDataReceived();
                    return 0;
                } catch (InterruptedException e) {
                    synchronized (this.mStateLock) {
                        this.mStatusCode = -5;
                        return -1;
                    }
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int done() {
        /*
        r6 = this;
        r2 = -1;
        r1 = 0;
        r0 = 0;
        r3 = r6.mStateLock;
        monitor-enter(r3);
        r4 = r6.mDone;	 Catch:{ all -> 0x001e }
        if (r4 == 0) goto L_0x0013;
    L_0x000a:
        r4 = "PlaybackSynthesisRequest";
        r5 = "Duplicate call to done()";
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x001e }
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
    L_0x0012:
        return r2;
    L_0x0013:
        r4 = r6.mStatusCode;	 Catch:{ all -> 0x001e }
        r5 = -2;
        if (r4 != r5) goto L_0x0021;
    L_0x0018:
        r2 = r6.errorCodeOnStop();	 Catch:{ all -> 0x001e }
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        goto L_0x0012;
    L_0x001e:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        throw r2;
    L_0x0021:
        r4 = 1;
        r6.mDone = r4;	 Catch:{ all -> 0x001e }
        r4 = r6.mItem;	 Catch:{ all -> 0x001e }
        if (r4 != 0) goto L_0x0047;
    L_0x0028:
        r4 = "PlaybackSynthesisRequest";
        r5 = "done() was called before start() call";
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x001e }
        r4 = r6.mStatusCode;	 Catch:{ all -> 0x001e }
        if (r4 != 0) goto L_0x003f;
    L_0x0033:
        r4 = r6.mDispatcher;	 Catch:{ all -> 0x001e }
        r4.dispatchOnSuccess();	 Catch:{ all -> 0x001e }
    L_0x0038:
        r4 = r6.mLogger;	 Catch:{ all -> 0x001e }
        r4.onEngineComplete();	 Catch:{ all -> 0x001e }
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        goto L_0x0012;
    L_0x003f:
        r4 = r6.mDispatcher;	 Catch:{ all -> 0x001e }
        r5 = r6.mStatusCode;	 Catch:{ all -> 0x001e }
        r4.dispatchOnError(r5);	 Catch:{ all -> 0x001e }
        goto L_0x0038;
    L_0x0047:
        r0 = r6.mItem;	 Catch:{ all -> 0x001e }
        r1 = r6.mStatusCode;	 Catch:{ all -> 0x001e }
        monitor-exit(r3);	 Catch:{ all -> 0x001e }
        if (r1 != 0) goto L_0x0058;
    L_0x004e:
        r0.done();
    L_0x0051:
        r2 = r6.mLogger;
        r2.onEngineComplete();
        r2 = 0;
        goto L_0x0012;
    L_0x0058:
        r0.stop(r1);
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.PlaybackSynthesisCallback.done():int");
    }

    public void error() {
        error(-3);
    }

    public void error(int errorCode) {
        synchronized (this.mStateLock) {
            if (this.mDone) {
                return;
            }
            this.mStatusCode = errorCode;
        }
    }
}
