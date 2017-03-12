package android.media;

import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public final class MediaSync {
    private static final int CB_RETURN_AUDIO_BUFFER = 1;
    private static final int EVENT_CALLBACK = 1;
    private static final int EVENT_SET_CALLBACK = 2;
    public static final int MEDIASYNC_ERROR_AUDIOTRACK_FAIL = 1;
    public static final int MEDIASYNC_ERROR_SURFACE_FAIL = 2;
    private static final String TAG = "MediaSync";
    private List<AudioBuffer> mAudioBuffers = new LinkedList();
    private Handler mAudioHandler = null;
    private final Object mAudioLock = new Object();
    private Looper mAudioLooper = null;
    private Thread mAudioThread = null;
    private AudioTrack mAudioTrack = null;
    private Callback mCallback = null;
    private Handler mCallbackHandler = null;
    private final Object mCallbackLock = new Object();
    private long mNativeContext;
    private OnErrorListener mOnErrorListener = null;
    private Handler mOnErrorListenerHandler = null;
    private final Object mOnErrorListenerLock = new Object();
    private float mPlaybackRate = 0.0f;

    private static class AudioBuffer {
        public int mBufferIndex;
        public ByteBuffer mByteBuffer;
        long mPresentationTimeUs;

        public AudioBuffer(ByteBuffer byteBuffer, int bufferId, long presentationTimeUs) {
            this.mByteBuffer = byteBuffer;
            this.mBufferIndex = bufferId;
            this.mPresentationTimeUs = presentationTimeUs;
        }
    }

    public static abstract class Callback {
        public abstract void onAudioBufferConsumed(MediaSync mediaSync, ByteBuffer byteBuffer, int i);
    }

    public interface OnErrorListener {
        void onError(MediaSync mediaSync, int i, int i2);
    }

    private final native void native_finalize();

    private final native void native_flush();

    private final native long native_getPlayTimeForPendingAudioFrames();

    private final native boolean native_getTimestamp(MediaTimestamp mediaTimestamp);

    private static final native void native_init();

    private final native void native_release();

    private final native void native_setAudioTrack(AudioTrack audioTrack);

    private native float native_setPlaybackParams(PlaybackParams playbackParams);

    private final native void native_setSurface(Surface surface);

    private native float native_setSyncParams(SyncParams syncParams);

    private final native void native_setup();

    private final native void native_updateQueuedAudioData(int i, long j);

    public final native Surface createInputSurface();

    public native PlaybackParams getPlaybackParams();

    public native SyncParams getSyncParams();

    public MediaSync() {
        native_setup();
    }

    protected void finalize() {
        native_finalize();
    }

    public final void release() {
        returnAudioBuffers();
        if (!(this.mAudioThread == null || this.mAudioLooper == null)) {
            this.mAudioLooper.quit();
        }
        setCallback(null, null);
        native_release();
    }

    public void setCallback(Callback cb, Handler handler) {
        synchronized (this.mCallbackLock) {
            if (handler != null) {
                this.mCallbackHandler = handler;
            } else {
                Looper looper = Looper.myLooper();
                if (looper == null) {
                    looper = Looper.getMainLooper();
                }
                if (looper == null) {
                    this.mCallbackHandler = null;
                } else {
                    this.mCallbackHandler = new Handler(looper);
                }
            }
            this.mCallback = cb;
        }
    }

    public void setOnErrorListener(OnErrorListener listener, Handler handler) {
        synchronized (this.mOnErrorListenerLock) {
            if (handler != null) {
                this.mOnErrorListenerHandler = handler;
            } else {
                Looper looper = Looper.myLooper();
                if (looper == null) {
                    looper = Looper.getMainLooper();
                }
                if (looper == null) {
                    this.mOnErrorListenerHandler = null;
                } else {
                    this.mOnErrorListenerHandler = new Handler(looper);
                }
            }
            this.mOnErrorListener = listener;
        }
    }

    public void setSurface(Surface surface) {
        native_setSurface(surface);
    }

    public void setAudioTrack(AudioTrack audioTrack) {
        native_setAudioTrack(audioTrack);
        this.mAudioTrack = audioTrack;
        if (audioTrack != null && this.mAudioThread == null) {
            createAudioThread();
        }
    }

    public void setPlaybackParams(PlaybackParams params) {
        synchronized (this.mAudioLock) {
            this.mPlaybackRate = native_setPlaybackParams(params);
        }
        if (((double) this.mPlaybackRate) != 0.0d && this.mAudioThread != null) {
            postRenderAudio(0);
        }
    }

    public void setSyncParams(SyncParams params) {
        synchronized (this.mAudioLock) {
            this.mPlaybackRate = native_setSyncParams(params);
        }
        if (((double) this.mPlaybackRate) != 0.0d && this.mAudioThread != null) {
            postRenderAudio(0);
        }
    }

    public void flush() {
        synchronized (this.mAudioLock) {
            this.mAudioBuffers.clear();
            this.mCallbackHandler.removeCallbacksAndMessages(null);
        }
        if (this.mAudioTrack != null) {
            this.mAudioTrack.pause();
            this.mAudioTrack.flush();
            this.mAudioTrack.stop();
        }
        native_flush();
    }

    public MediaTimestamp getTimestamp() {
        try {
            MediaTimestamp timestamp = new MediaTimestamp();
            return native_getTimestamp(timestamp) ? timestamp : null;
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public void queueAudio(ByteBuffer audioData, int bufferId, long presentationTimeUs) {
        if (this.mAudioTrack == null || this.mAudioThread == null) {
            throw new IllegalStateException("AudioTrack is NOT set or audio thread is not created");
        }
        synchronized (this.mAudioLock) {
            this.mAudioBuffers.add(new AudioBuffer(audioData, bufferId, presentationTimeUs));
        }
        if (((double) this.mPlaybackRate) != 0.0d) {
            postRenderAudio(0);
        }
    }

    private void postRenderAudio(long delayMillis) {
        this.mAudioHandler.postDelayed(new Runnable() {
            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                r14 = this;
                r12 = -1;
                r6 = android.media.MediaSync.this;
                r7 = r6.mAudioLock;
                monitor-enter(r7);
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6 = r6.mPlaybackRate;	 Catch:{ all -> 0x0026 }
                r8 = (double) r6;	 Catch:{ all -> 0x0026 }
                r10 = 0;
                r6 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
                if (r6 != 0) goto L_0x0018;
            L_0x0016:
                monitor-exit(r7);	 Catch:{ all -> 0x0026 }
            L_0x0017:
                return;
            L_0x0018:
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6 = r6.mAudioBuffers;	 Catch:{ all -> 0x0026 }
                r6 = r6.isEmpty();	 Catch:{ all -> 0x0026 }
                if (r6 == 0) goto L_0x0029;
            L_0x0024:
                monitor-exit(r7);	 Catch:{ all -> 0x0026 }
                goto L_0x0017;
            L_0x0026:
                r6 = move-exception;
                monitor-exit(r7);	 Catch:{ all -> 0x0026 }
                throw r6;
            L_0x0029:
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6 = r6.mAudioBuffers;	 Catch:{ all -> 0x0026 }
                r8 = 0;
                r0 = r6.get(r8);	 Catch:{ all -> 0x0026 }
                r0 = (android.media.MediaSync.AudioBuffer) r0;	 Catch:{ all -> 0x0026 }
                r6 = r0.mByteBuffer;	 Catch:{ all -> 0x0026 }
                r4 = r6.remaining();	 Catch:{ all -> 0x0026 }
                if (r4 <= 0) goto L_0x0054;
            L_0x003e:
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6 = r6.mAudioTrack;	 Catch:{ all -> 0x0026 }
                r6 = r6.getPlayState();	 Catch:{ all -> 0x0026 }
                r8 = 3;
                if (r6 == r8) goto L_0x0054;
            L_0x004b:
                r6 = android.media.MediaSync.this;	 Catch:{ IllegalStateException -> 0x009b }
                r6 = r6.mAudioTrack;	 Catch:{ IllegalStateException -> 0x009b }
                r6.play();	 Catch:{ IllegalStateException -> 0x009b }
            L_0x0054:
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6 = r6.mAudioTrack;	 Catch:{ all -> 0x0026 }
                r8 = r0.mByteBuffer;	 Catch:{ all -> 0x0026 }
                r9 = 1;
                r5 = r6.write(r8, r4, r9);	 Catch:{ all -> 0x0026 }
                if (r5 <= 0) goto L_0x00a4;
            L_0x0063:
                r8 = r0.mPresentationTimeUs;	 Catch:{ all -> 0x0026 }
                r6 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
                if (r6 == 0) goto L_0x0074;
            L_0x0069:
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r8 = r0.mPresentationTimeUs;	 Catch:{ all -> 0x0026 }
                r6.native_updateQueuedAudioData(r4, r8);	 Catch:{ all -> 0x0026 }
                r8 = -1;
                r0.mPresentationTimeUs = r8;	 Catch:{ all -> 0x0026 }
            L_0x0074:
                if (r5 != r4) goto L_0x00a4;
            L_0x0076:
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6.postReturnByteBuffer(r0);	 Catch:{ all -> 0x0026 }
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6 = r6.mAudioBuffers;	 Catch:{ all -> 0x0026 }
                r8 = 0;
                r6.remove(r8);	 Catch:{ all -> 0x0026 }
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r6 = r6.mAudioBuffers;	 Catch:{ all -> 0x0026 }
                r6 = r6.isEmpty();	 Catch:{ all -> 0x0026 }
                if (r6 != 0) goto L_0x0098;
            L_0x0091:
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r8 = 0;
                r6.postRenderAudio(r8);	 Catch:{ all -> 0x0026 }
            L_0x0098:
                monitor-exit(r7);	 Catch:{ all -> 0x0026 }
                goto L_0x0017;
            L_0x009b:
                r1 = move-exception;
                r6 = "MediaSync";
                r8 = "could not start audio track";
                android.util.Log.w(r6, r8);	 Catch:{ all -> 0x0026 }
                goto L_0x0054;
            L_0x00a4:
                r6 = java.util.concurrent.TimeUnit.MICROSECONDS;	 Catch:{ all -> 0x0026 }
                r8 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r8 = r8.native_getPlayTimeForPendingAudioFrames();	 Catch:{ all -> 0x0026 }
                r2 = r6.toMillis(r8);	 Catch:{ all -> 0x0026 }
                r6 = android.media.MediaSync.this;	 Catch:{ all -> 0x0026 }
                r8 = 2;
                r8 = r2 / r8;
                r6.postRenderAudio(r8);	 Catch:{ all -> 0x0026 }
                monitor-exit(r7);	 Catch:{ all -> 0x0026 }
                goto L_0x0017;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.media.MediaSync.1.run():void");
            }
        }, delayMillis);
    }

    private final void postReturnByteBuffer(final AudioBuffer audioBuffer) {
        synchronized (this.mCallbackLock) {
            if (this.mCallbackHandler != null) {
                final MediaSync sync = this;
                this.mCallbackHandler.post(new Runnable() {
                    /* JADX WARNING: inconsistent code. */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                        r4 = this;
                        r1 = android.media.MediaSync.this;
                        r2 = r1.mCallbackLock;
                        monitor-enter(r2);
                        r1 = android.media.MediaSync.this;	 Catch:{ all -> 0x003c }
                        r0 = r1.mCallback;	 Catch:{ all -> 0x003c }
                        r1 = android.media.MediaSync.this;	 Catch:{ all -> 0x003c }
                        r1 = r1.mCallbackHandler;	 Catch:{ all -> 0x003c }
                        if (r1 == 0) goto L_0x0029;
                    L_0x0015:
                        r1 = android.media.MediaSync.this;	 Catch:{ all -> 0x003c }
                        r1 = r1.mCallbackHandler;	 Catch:{ all -> 0x003c }
                        r1 = r1.getLooper();	 Catch:{ all -> 0x003c }
                        r1 = r1.getThread();	 Catch:{ all -> 0x003c }
                        r3 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x003c }
                        if (r1 == r3) goto L_0x002b;
                    L_0x0029:
                        monitor-exit(r2);	 Catch:{ all -> 0x003c }
                    L_0x002a:
                        return;
                    L_0x002b:
                        monitor-exit(r2);	 Catch:{ all -> 0x003c }
                        if (r0 == 0) goto L_0x002a;
                    L_0x002e:
                        r1 = r0;
                        r2 = r5;
                        r2 = r2.mByteBuffer;
                        r3 = r5;
                        r3 = r3.mBufferIndex;
                        r0.onAudioBufferConsumed(r1, r2, r3);
                        goto L_0x002a;
                    L_0x003c:
                        r1 = move-exception;
                        monitor-exit(r2);	 Catch:{ all -> 0x003c }
                        throw r1;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaSync.2.run():void");
                    }
                });
            }
        }
    }

    private final void returnAudioBuffers() {
        synchronized (this.mAudioLock) {
            for (AudioBuffer audioBuffer : this.mAudioBuffers) {
                postReturnByteBuffer(audioBuffer);
            }
            this.mAudioBuffers.clear();
        }
    }

    private void createAudioThread() {
        this.mAudioThread = new Thread() {
            public void run() {
                Looper.prepare();
                synchronized (MediaSync.this.mAudioLock) {
                    MediaSync.this.mAudioLooper = Looper.myLooper();
                    MediaSync.this.mAudioHandler = new Handler();
                    MediaSync.this.mAudioLock.notify();
                }
                Looper.loop();
            }
        };
        this.mAudioThread.start();
        synchronized (this.mAudioLock) {
            try {
                this.mAudioLock.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }
}
