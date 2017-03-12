package android.media;

import android.media.AudioManager.OnAudioPortUpdateListener;
import android.os.Handler;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class AudioPortEventHandler {
    private static final int AUDIOPORT_EVENT_NEW_LISTENER = 4;
    private static final int AUDIOPORT_EVENT_PATCH_LIST_UPDATED = 2;
    private static final int AUDIOPORT_EVENT_PORT_LIST_UPDATED = 1;
    private static final int AUDIOPORT_EVENT_SERVICE_DIED = 3;
    private static final String TAG = "AudioPortEventHandler";
    private Handler mHandler;
    private long mJniCallback;
    private final ArrayList<OnAudioPortUpdateListener> mListeners = new ArrayList();

    private native void native_finalize();

    private native void native_setup(Object obj);

    AudioPortEventHandler() {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void init() {
        /*
        r2 = this;
        monitor-enter(r2);
        r1 = r2.mHandler;	 Catch:{ all -> 0x001e }
        if (r1 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r2);	 Catch:{ all -> 0x001e }
    L_0x0006:
        return;
    L_0x0007:
        r0 = android.os.Looper.getMainLooper();	 Catch:{ all -> 0x001e }
        if (r0 == 0) goto L_0x0021;
    L_0x000d:
        r1 = new android.media.AudioPortEventHandler$1;	 Catch:{ all -> 0x001e }
        r1.<init>(r0);	 Catch:{ all -> 0x001e }
        r2.mHandler = r1;	 Catch:{ all -> 0x001e }
        r1 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x001e }
        r1.<init>(r2);	 Catch:{ all -> 0x001e }
        r2.native_setup(r1);	 Catch:{ all -> 0x001e }
    L_0x001c:
        monitor-exit(r2);	 Catch:{ all -> 0x001e }
        goto L_0x0006;
    L_0x001e:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001e }
        throw r1;
    L_0x0021:
        r1 = 0;
        r2.mHandler = r1;	 Catch:{ all -> 0x001e }
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioPortEventHandler.init():void");
    }

    protected void finalize() {
        native_finalize();
    }

    void registerListener(OnAudioPortUpdateListener l) {
        synchronized (this) {
            this.mListeners.add(l);
        }
        if (this.mHandler != null) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(4, 0, 0, l));
        }
    }

    void unregisterListener(OnAudioPortUpdateListener l) {
        synchronized (this) {
            this.mListeners.remove(l);
        }
    }

    Handler handler() {
        return this.mHandler;
    }

    private static void postEventFromNative(Object module_ref, int what, int arg1, int arg2, Object obj) {
        AudioPortEventHandler eventHandler = (AudioPortEventHandler) ((WeakReference) module_ref).get();
        if (eventHandler != null && eventHandler != null) {
            Handler handler = eventHandler.handler();
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
            }
        }
    }
}
