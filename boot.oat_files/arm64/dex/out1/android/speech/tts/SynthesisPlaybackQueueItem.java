package android.speech.tts;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

final class SynthesisPlaybackQueueItem extends PlaybackQueueItem {
    private static final boolean DBG = false;
    private static final long MAX_UNCONSUMED_AUDIO_MS = 500;
    private static final String TAG = "TTS.SynthQueueItem";
    public static boolean userSelectedOtherItemInPauseState = false;
    private final BlockingAudioTrack mAudioTrack;
    private final LinkedList<ListEntry> mDataBufferList = new LinkedList();
    private volatile boolean mDone = false;
    private final Lock mListLock = new ReentrantLock();
    private final AbstractEventLogger mLogger;
    private final Condition mNotFull = this.mListLock.newCondition();
    private final Condition mReadReady = this.mListLock.newCondition();
    private volatile int mStatusCode = 0;
    private volatile boolean mStopped = false;
    private int mUnconsumedBytes = 0;

    static final class ListEntry {
        final byte[] mBytes;

        ListEntry(byte[] bytes) {
            this.mBytes = bytes;
        }
    }

    SynthesisPlaybackQueueItem(AudioOutputParams audioParams, int sampleRate, int audioFormat, int channelCount, UtteranceProgressDispatcher dispatcher, Object callerIdentity, AbstractEventLogger logger) {
        super(dispatcher, callerIdentity);
        this.mAudioTrack = new BlockingAudioTrack(audioParams, sampleRate, audioFormat, channelCount);
        this.mLogger = logger;
    }

    public void run() {
        UtteranceProgressDispatcher dispatcher = getDispatcher();
        dispatcher.dispatchOnStart();
        if (this.mAudioTrack.init()) {
            while (true) {
                try {
                    byte[] buffer = take();
                    if (buffer == null) {
                        break;
                    }
                    this.mAudioTrack.write(buffer);
                    this.mLogger.onAudioDataWritten();
                } catch (InterruptedException e) {
                }
            }
            this.mAudioTrack.waitAndRelease();
            if (this.mStatusCode == 0) {
                dispatcher.dispatchOnSuccess();
            } else if (this.mStatusCode == -2) {
                dispatcher.dispatchOnStop();
            } else {
                dispatcher.dispatchOnError(this.mStatusCode);
            }
            this.mLogger.onCompleted(this.mStatusCode);
            return;
        }
        dispatcher.dispatchOnError(-5);
    }

    void stop(int statusCode) {
        try {
            this.mListLock.lock();
            this.mStopped = true;
            this.mStatusCode = statusCode;
            this.mReadReady.signal();
            this.mNotFull.signal();
            this.mAudioTrack.stop();
        } finally {
            this.mListLock.unlock();
        }
    }

    void done() {
        try {
            this.mListLock.lock();
            this.mDone = true;
            this.mReadReady.signal();
            this.mNotFull.signal();
        } finally {
            this.mListLock.unlock();
        }
    }

    void put(byte[] buffer) throws InterruptedException {
        try {
            this.mListLock.lock();
            while (this.mAudioTrack.getAudioLengthMs(this.mUnconsumedBytes) > MAX_UNCONSUMED_AUDIO_MS && !this.mStopped) {
                this.mNotFull.await();
            }
            if (!this.mStopped) {
                this.mDataBufferList.add(new ListEntry(buffer));
                this.mUnconsumedBytes += buffer.length;
                this.mReadReady.signal();
                this.mListLock.unlock();
            }
        } finally {
            this.mListLock.unlock();
        }
    }

    private byte[] take() throws InterruptedException {
        byte[] bArr = null;
        try {
            TextToSpeechService.pauseLock.lock();
            if (TextToSpeechService.PAUSE_STATE) {
                TextToSpeechService.pauseCondition.await();
            }
            TextToSpeechService.PAUSE_STATE = false;
            if (userSelectedOtherItemInPauseState) {
                userSelectedOtherItemInPauseState = false;
                stop(0);
            }
            this.mListLock.lock();
            while (this.mDataBufferList.size() == 0 && !this.mStopped && !this.mDone) {
                this.mReadReady.await();
            }
            if (!this.mStopped) {
                ListEntry entry = (ListEntry) this.mDataBufferList.poll();
                if (entry == null) {
                    this.mListLock.unlock();
                    TextToSpeechService.pauseLock.unlock();
                } else {
                    this.mUnconsumedBytes -= entry.mBytes.length;
                    this.mNotFull.signal();
                    bArr = entry.mBytes;
                    this.mListLock.unlock();
                    TextToSpeechService.pauseLock.unlock();
                }
            }
            return bArr;
        } finally {
            this.mListLock.unlock();
            TextToSpeechService.pauseLock.unlock();
        }
    }
}
