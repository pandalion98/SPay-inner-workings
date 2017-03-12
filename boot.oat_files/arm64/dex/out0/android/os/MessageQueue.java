package android.os;

import android.util.Printer;
import android.util.SparseArray;
import java.io.FileDescriptor;
import java.util.ArrayList;

public final class MessageQueue {
    private static final boolean DEBUG = false;
    private static final String TAG = "MessageQueue";
    private boolean mBlocked;
    private SparseArray<FileDescriptorRecord> mFileDescriptorRecords;
    private final ArrayList<IdleHandler> mIdleHandlers = new ArrayList();
    Message mMessages;
    private int mNextBarrierToken;
    private IdleHandler[] mPendingIdleHandlers;
    private long mPtr;
    private final boolean mQuitAllowed;
    private boolean mQuitting;

    public interface IdleHandler {
        boolean queueIdle();
    }

    private static final class FileDescriptorRecord {
        public final FileDescriptor mDescriptor;
        public int mEvents;
        public OnFileDescriptorEventListener mListener;
        public int mSeq;

        public FileDescriptorRecord(FileDescriptor descriptor, int events, OnFileDescriptorEventListener listener) {
            this.mDescriptor = descriptor;
            this.mEvents = events;
            this.mListener = listener;
        }
    }

    public interface OnFileDescriptorEventListener {
        public static final int EVENT_ERROR = 4;
        public static final int EVENT_INPUT = 1;
        public static final int EVENT_OUTPUT = 2;

        int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i);
    }

    private static native void nativeDestroy(long j);

    private static native long nativeInit();

    private static native boolean nativeIsPolling(long j);

    private native void nativePollOnce(long j, int i);

    private static native void nativeSetFileDescriptorEvents(long j, int i, int i2);

    private static native void nativeWake(long j);

    MessageQueue(boolean quitAllowed) {
        this.mQuitAllowed = quitAllowed;
        this.mPtr = nativeInit();
    }

    protected void finalize() throws Throwable {
        try {
            dispose();
        } finally {
            super.finalize();
        }
    }

    private void dispose() {
        if (this.mPtr != 0) {
            nativeDestroy(this.mPtr);
            this.mPtr = 0;
        }
    }

    public boolean isIdle() {
        boolean z;
        synchronized (this) {
            z = this.mMessages == null || SystemClock.uptimeMillis() < this.mMessages.when;
        }
        return z;
    }

    public void addIdleHandler(IdleHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Can't add a null IdleHandler");
        }
        synchronized (this) {
            this.mIdleHandlers.add(handler);
        }
    }

    public void removeIdleHandler(IdleHandler handler) {
        synchronized (this) {
            this.mIdleHandlers.remove(handler);
        }
    }

    public boolean isPolling() {
        boolean isPollingLocked;
        synchronized (this) {
            isPollingLocked = isPollingLocked();
        }
        return isPollingLocked;
    }

    private boolean isPollingLocked() {
        return !this.mQuitting && nativeIsPolling(this.mPtr);
    }

    public void addOnFileDescriptorEventListener(FileDescriptor fd, int events, OnFileDescriptorEventListener listener) {
        if (fd == null) {
            throw new IllegalArgumentException("fd must not be null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else {
            synchronized (this) {
                updateOnFileDescriptorEventListenerLocked(fd, events, listener);
            }
        }
    }

    public void removeOnFileDescriptorEventListener(FileDescriptor fd) {
        if (fd == null) {
            throw new IllegalArgumentException("fd must not be null");
        }
        synchronized (this) {
            updateOnFileDescriptorEventListenerLocked(fd, 0, null);
        }
    }

    private void updateOnFileDescriptorEventListenerLocked(FileDescriptor fd, int events, OnFileDescriptorEventListener listener) {
        int fdNum = fd.getInt$();
        int index = -1;
        FileDescriptorRecord record = null;
        if (this.mFileDescriptorRecords != null) {
            index = this.mFileDescriptorRecords.indexOfKey(fdNum);
            if (index >= 0) {
                record = (FileDescriptorRecord) this.mFileDescriptorRecords.valueAt(index);
                if (record != null && record.mEvents == events) {
                    return;
                }
            }
        }
        if (events != 0) {
            events |= 4;
            if (record == null) {
                if (this.mFileDescriptorRecords == null) {
                    this.mFileDescriptorRecords = new SparseArray();
                }
                this.mFileDescriptorRecords.put(fdNum, new FileDescriptorRecord(fd, events, listener));
            } else {
                record.mListener = listener;
                record.mEvents = events;
                record.mSeq++;
            }
            nativeSetFileDescriptorEvents(this.mPtr, fdNum, events);
        } else if (record != null) {
            record.mEvents = 0;
            this.mFileDescriptorRecords.removeAt(index);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int dispatchEvents(int r8, int r9) {
        /*
        r7 = this;
        monitor-enter(r7);
        r6 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x0015 }
        r4 = r6.get(r8);	 Catch:{ all -> 0x0015 }
        r4 = (android.os.MessageQueue.FileDescriptorRecord) r4;	 Catch:{ all -> 0x0015 }
        if (r4 != 0) goto L_0x000e;
    L_0x000b:
        r3 = 0;
        monitor-exit(r7);	 Catch:{ all -> 0x0015 }
    L_0x000d:
        return r3;
    L_0x000e:
        r3 = r4.mEvents;	 Catch:{ all -> 0x0015 }
        r9 = r9 & r3;
        if (r9 != 0) goto L_0x0018;
    L_0x0013:
        monitor-exit(r7);	 Catch:{ all -> 0x0015 }
        goto L_0x000d;
    L_0x0015:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0015 }
        throw r6;
    L_0x0018:
        r1 = r4.mListener;	 Catch:{ all -> 0x0015 }
        r5 = r4.mSeq;	 Catch:{ all -> 0x0015 }
        monitor-exit(r7);	 Catch:{ all -> 0x0015 }
        r6 = r4.mDescriptor;
        r2 = r1.onFileDescriptorEvents(r6, r9);
        if (r2 == 0) goto L_0x0027;
    L_0x0025:
        r2 = r2 | 4;
    L_0x0027:
        if (r2 == r3) goto L_0x0048;
    L_0x0029:
        monitor-enter(r7);
        r6 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x004a }
        r0 = r6.indexOfKey(r8);	 Catch:{ all -> 0x004a }
        if (r0 < 0) goto L_0x0047;
    L_0x0032:
        r6 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x004a }
        r6 = r6.valueAt(r0);	 Catch:{ all -> 0x004a }
        if (r6 != r4) goto L_0x0047;
    L_0x003a:
        r6 = r4.mSeq;	 Catch:{ all -> 0x004a }
        if (r6 != r5) goto L_0x0047;
    L_0x003e:
        r4.mEvents = r2;	 Catch:{ all -> 0x004a }
        if (r2 != 0) goto L_0x0047;
    L_0x0042:
        r6 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x004a }
        r6.removeAt(r0);	 Catch:{ all -> 0x004a }
    L_0x0047:
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
    L_0x0048:
        r3 = r2;
        goto L_0x000d;
    L_0x004a:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.dispatchEvents(int, int):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    android.os.Message next() {
        /*
        r18 = this;
        r0 = r18;
        r12 = r0.mPtr;
        r14 = 0;
        r14 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
        if (r14 != 0) goto L_0x000c;
    L_0x000a:
        r5 = 0;
    L_0x000b:
        return r5;
    L_0x000c:
        r7 = -1;
        r6 = 0;
    L_0x000e:
        if (r6 == 0) goto L_0x0013;
    L_0x0010:
        android.os.Binder.flushPendingCommands();
    L_0x0013:
        r0 = r18;
        r0.nativePollOnce(r12, r6);
        monitor-enter(r18);
        r8 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0052 }
        r10 = 0;
        r0 = r18;
        r5 = r0.mMessages;	 Catch:{ all -> 0x0052 }
        if (r5 == 0) goto L_0x0033;
    L_0x0024:
        r14 = r5.target;	 Catch:{ all -> 0x0052 }
        if (r14 != 0) goto L_0x0033;
    L_0x0028:
        r10 = r5;
        r5 = r5.next;	 Catch:{ all -> 0x0052 }
        if (r5 == 0) goto L_0x0033;
    L_0x002d:
        r14 = r5.isAsynchronous();	 Catch:{ all -> 0x0052 }
        if (r14 == 0) goto L_0x0028;
    L_0x0033:
        if (r5 == 0) goto L_0x006f;
    L_0x0035:
        r14 = r5.when;	 Catch:{ all -> 0x0052 }
        r14 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1));
        if (r14 >= 0) goto L_0x0055;
    L_0x003b:
        r14 = r5.when;	 Catch:{ all -> 0x0052 }
        r14 = r14 - r8;
        r16 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r14 = java.lang.Math.min(r14, r16);	 Catch:{ all -> 0x0052 }
        r6 = (int) r14;	 Catch:{ all -> 0x0052 }
    L_0x0046:
        r0 = r18;
        r14 = r0.mQuitting;	 Catch:{ all -> 0x0052 }
        if (r14 == 0) goto L_0x0071;
    L_0x004c:
        r18.dispose();	 Catch:{ all -> 0x0052 }
        r5 = 0;
        monitor-exit(r18);	 Catch:{ all -> 0x0052 }
        goto L_0x000b;
    L_0x0052:
        r14 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x0052 }
        throw r14;
    L_0x0055:
        r14 = 0;
        r0 = r18;
        r0.mBlocked = r14;	 Catch:{ all -> 0x0052 }
        if (r10 == 0) goto L_0x0068;
    L_0x005c:
        r14 = r5.next;	 Catch:{ all -> 0x0052 }
        r10.next = r14;	 Catch:{ all -> 0x0052 }
    L_0x0060:
        r14 = 0;
        r5.next = r14;	 Catch:{ all -> 0x0052 }
        r5.markInUse();	 Catch:{ all -> 0x0052 }
        monitor-exit(r18);	 Catch:{ all -> 0x0052 }
        goto L_0x000b;
    L_0x0068:
        r14 = r5.next;	 Catch:{ all -> 0x0052 }
        r0 = r18;
        r0.mMessages = r14;	 Catch:{ all -> 0x0052 }
        goto L_0x0060;
    L_0x006f:
        r6 = -1;
        goto L_0x0046;
    L_0x0071:
        if (r7 >= 0) goto L_0x008b;
    L_0x0073:
        r0 = r18;
        r14 = r0.mMessages;	 Catch:{ all -> 0x0052 }
        if (r14 == 0) goto L_0x0083;
    L_0x0079:
        r0 = r18;
        r14 = r0.mMessages;	 Catch:{ all -> 0x0052 }
        r14 = r14.when;	 Catch:{ all -> 0x0052 }
        r14 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1));
        if (r14 >= 0) goto L_0x008b;
    L_0x0083:
        r0 = r18;
        r14 = r0.mIdleHandlers;	 Catch:{ all -> 0x0052 }
        r7 = r14.size();	 Catch:{ all -> 0x0052 }
    L_0x008b:
        if (r7 > 0) goto L_0x0095;
    L_0x008d:
        r14 = 1;
        r0 = r18;
        r0.mBlocked = r14;	 Catch:{ all -> 0x0052 }
        monitor-exit(r18);	 Catch:{ all -> 0x0052 }
        goto L_0x000e;
    L_0x0095:
        r0 = r18;
        r14 = r0.mPendingIdleHandlers;	 Catch:{ all -> 0x0052 }
        if (r14 != 0) goto L_0x00a6;
    L_0x009b:
        r14 = 4;
        r14 = java.lang.Math.max(r7, r14);	 Catch:{ all -> 0x0052 }
        r14 = new android.os.MessageQueue.IdleHandler[r14];	 Catch:{ all -> 0x0052 }
        r0 = r18;
        r0.mPendingIdleHandlers = r14;	 Catch:{ all -> 0x0052 }
    L_0x00a6:
        r0 = r18;
        r14 = r0.mIdleHandlers;	 Catch:{ all -> 0x0052 }
        r0 = r18;
        r15 = r0.mPendingIdleHandlers;	 Catch:{ all -> 0x0052 }
        r14 = r14.toArray(r15);	 Catch:{ all -> 0x0052 }
        r14 = (android.os.MessageQueue.IdleHandler[]) r14;	 Catch:{ all -> 0x0052 }
        r0 = r18;
        r0.mPendingIdleHandlers = r14;	 Catch:{ all -> 0x0052 }
        monitor-exit(r18);	 Catch:{ all -> 0x0052 }
        r2 = 0;
    L_0x00ba:
        if (r2 >= r7) goto L_0x00e8;
    L_0x00bc:
        r0 = r18;
        r14 = r0.mPendingIdleHandlers;
        r3 = r14[r2];
        r0 = r18;
        r14 = r0.mPendingIdleHandlers;
        r15 = 0;
        r14[r2] = r15;
        r4 = 0;
        r4 = r3.queueIdle();	 Catch:{ Throwable -> 0x00dc }
    L_0x00ce:
        if (r4 != 0) goto L_0x00d9;
    L_0x00d0:
        monitor-enter(r18);
        r0 = r18;
        r14 = r0.mIdleHandlers;	 Catch:{ all -> 0x00e5 }
        r14.remove(r3);	 Catch:{ all -> 0x00e5 }
        monitor-exit(r18);	 Catch:{ all -> 0x00e5 }
    L_0x00d9:
        r2 = r2 + 1;
        goto L_0x00ba;
    L_0x00dc:
        r11 = move-exception;
        r14 = "MessageQueue";
        r15 = "IdleHandler threw exception";
        android.util.Log.wtf(r14, r15, r11);
        goto L_0x00ce;
    L_0x00e5:
        r14 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x00e5 }
        throw r14;
    L_0x00e8:
        r7 = 0;
        r6 = 0;
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.next():android.os.Message");
    }

    void quit(boolean safe) {
        if (this.mQuitAllowed) {
            synchronized (this) {
                if (this.mQuitting) {
                    return;
                }
                this.mQuitting = true;
                if (safe) {
                    removeAllFutureMessagesLocked();
                } else {
                    removeAllMessagesLocked();
                }
                nativeWake(this.mPtr);
                return;
            }
        }
        throw new IllegalStateException("Main thread not allowed to quit.");
    }

    public int postSyncBarrier() {
        return postSyncBarrier(SystemClock.uptimeMillis());
    }

    private int postSyncBarrier(long when) {
        int token;
        synchronized (this) {
            token = this.mNextBarrierToken;
            this.mNextBarrierToken = token + 1;
            Message msg = Message.obtain();
            msg.markInUse();
            msg.when = when;
            msg.arg1 = token;
            Message prev = null;
            Message p = this.mMessages;
            if (when != 0) {
                while (p != null && p.when <= when) {
                    prev = p;
                    p = p.next;
                }
            }
            if (prev != null) {
                msg.next = p;
                prev.next = msg;
            } else {
                msg.next = p;
                this.mMessages = msg;
            }
        }
        return token;
    }

    public void removeSyncBarrier(int token) {
        synchronized (this) {
            Message prev = null;
            Message p = this.mMessages;
            while (p != null && (p.target != null || p.arg1 != token)) {
                prev = p;
                p = p.next;
            }
            if (p == null) {
                throw new IllegalStateException("The specified message queue synchronization  barrier token has not been posted or has already been removed.");
            }
            boolean needWake;
            if (prev != null) {
                prev.next = p.next;
                needWake = false;
            } else {
                this.mMessages = p.next;
                needWake = this.mMessages == null || this.mMessages.target != null;
            }
            p.recycleUnchecked();
            if (needWake && !this.mQuitting) {
                nativeWake(this.mPtr);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean enqueueMessage(android.os.Message r9, long r10) {
        /*
        r8 = this;
        r4 = 1;
        r1 = 0;
        r5 = r9.target;
        if (r5 != 0) goto L_0x000e;
    L_0x0006:
        r4 = new java.lang.IllegalArgumentException;
        r5 = "Message must have a target.";
        r4.<init>(r5);
        throw r4;
    L_0x000e:
        r5 = r9.isInUse();
        if (r5 == 0) goto L_0x002d;
    L_0x0014:
        r4 = new java.lang.IllegalStateException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5 = r5.append(r9);
        r6 = " This message is already in use.";
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
    L_0x002d:
        monitor-enter(r8);
        r5 = r8.mQuitting;	 Catch:{ all -> 0x009e }
        if (r5 == 0) goto L_0x005a;
    L_0x0032:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x009e }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009e }
        r4.<init>();	 Catch:{ all -> 0x009e }
        r5 = r9.target;	 Catch:{ all -> 0x009e }
        r4 = r4.append(r5);	 Catch:{ all -> 0x009e }
        r5 = " sending message to a Handler on a dead thread";
        r4 = r4.append(r5);	 Catch:{ all -> 0x009e }
        r4 = r4.toString();	 Catch:{ all -> 0x009e }
        r0.<init>(r4);	 Catch:{ all -> 0x009e }
        r4 = "MessageQueue";
        r5 = r0.getMessage();	 Catch:{ all -> 0x009e }
        android.util.Log.w(r4, r5, r0);	 Catch:{ all -> 0x009e }
        r9.recycle();	 Catch:{ all -> 0x009e }
        monitor-exit(r8);	 Catch:{ all -> 0x009e }
    L_0x0059:
        return r1;
    L_0x005a:
        r9.markInUse();	 Catch:{ all -> 0x009e }
        r9.when = r10;	 Catch:{ all -> 0x009e }
        r2 = r8.mMessages;	 Catch:{ all -> 0x009e }
        if (r2 == 0) goto L_0x006f;
    L_0x0063:
        r6 = 0;
        r5 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1));
        if (r5 == 0) goto L_0x006f;
    L_0x0069:
        r6 = r2.when;	 Catch:{ all -> 0x009e }
        r5 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1));
        if (r5 >= 0) goto L_0x007f;
    L_0x006f:
        r9.next = r2;	 Catch:{ all -> 0x009e }
        r8.mMessages = r9;	 Catch:{ all -> 0x009e }
        r1 = r8.mBlocked;	 Catch:{ all -> 0x009e }
    L_0x0075:
        if (r1 == 0) goto L_0x007c;
    L_0x0077:
        r6 = r8.mPtr;	 Catch:{ all -> 0x009e }
        nativeWake(r6);	 Catch:{ all -> 0x009e }
    L_0x007c:
        monitor-exit(r8);	 Catch:{ all -> 0x009e }
        r1 = r4;
        goto L_0x0059;
    L_0x007f:
        r5 = r8.mBlocked;	 Catch:{ all -> 0x009e }
        if (r5 == 0) goto L_0x008e;
    L_0x0083:
        r5 = r2.target;	 Catch:{ all -> 0x009e }
        if (r5 != 0) goto L_0x008e;
    L_0x0087:
        r5 = r9.isAsynchronous();	 Catch:{ all -> 0x009e }
        if (r5 == 0) goto L_0x008e;
    L_0x008d:
        r1 = r4;
    L_0x008e:
        r3 = r2;
        r2 = r2.next;	 Catch:{ all -> 0x009e }
        if (r2 == 0) goto L_0x0099;
    L_0x0093:
        r6 = r2.when;	 Catch:{ all -> 0x009e }
        r5 = (r10 > r6 ? 1 : (r10 == r6 ? 0 : -1));
        if (r5 >= 0) goto L_0x00a1;
    L_0x0099:
        r9.next = r2;	 Catch:{ all -> 0x009e }
        r3.next = r9;	 Catch:{ all -> 0x009e }
        goto L_0x0075;
    L_0x009e:
        r4 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x009e }
        throw r4;
    L_0x00a1:
        if (r1 == 0) goto L_0x008e;
    L_0x00a3:
        r5 = r2.isAsynchronous();	 Catch:{ all -> 0x009e }
        if (r5 == 0) goto L_0x008e;
    L_0x00a9:
        r1 = 0;
        goto L_0x008e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.enqueueMessage(android.os.Message, long):boolean");
    }

    boolean hasMessages(Handler h, int what, Object object) {
        boolean z = false;
        if (h != null) {
            synchronized (this) {
                Message p = this.mMessages;
                while (p != null) {
                    if (p.target == h && p.what == what && (object == null || p.obj == object)) {
                        z = true;
                        break;
                    }
                    p = p.next;
                }
            }
        }
        return z;
    }

    boolean hasMessages(Handler h, Runnable r, Object object) {
        boolean z = false;
        if (h != null) {
            synchronized (this) {
                Message p = this.mMessages;
                while (p != null) {
                    if (p.target == h && p.callback == r && (object == null || p.obj == object)) {
                        z = true;
                        break;
                    }
                    p = p.next;
                }
            }
        }
        return z;
    }

    void removeMessages(Handler h, int what, Object object) {
        if (h != null) {
            synchronized (this) {
                Message n;
                Message p = this.mMessages;
                while (p != null && p.target == h && p.what == what && (object == null || p.obj == object)) {
                    n = p.next;
                    this.mMessages = n;
                    p.recycleUnchecked();
                    p = n;
                }
                while (p != null) {
                    n = p.next;
                    if (n != null && n.target == h && n.what == what && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycleUnchecked();
                        p.next = nn;
                    } else {
                        p = n;
                    }
                }
            }
        }
    }

    void removeMessages(Handler h, Runnable r, Object object) {
        if (h != null && r != null) {
            synchronized (this) {
                Message n;
                Message p = this.mMessages;
                while (p != null && p.target == h && p.callback == r && (object == null || p.obj == object)) {
                    n = p.next;
                    this.mMessages = n;
                    p.recycleUnchecked();
                    p = n;
                }
                while (p != null) {
                    n = p.next;
                    if (n != null && n.target == h && n.callback == r && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycleUnchecked();
                        p.next = nn;
                    } else {
                        p = n;
                    }
                }
            }
        }
    }

    void removeCallbacksAndMessages(Handler h, Object object) {
        if (h != null) {
            synchronized (this) {
                Message n;
                Message p = this.mMessages;
                while (p != null && p.target == h && (object == null || p.obj == object)) {
                    n = p.next;
                    this.mMessages = n;
                    p.recycleUnchecked();
                    p = n;
                }
                while (p != null) {
                    n = p.next;
                    if (n != null && n.target == h && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycleUnchecked();
                        p.next = nn;
                    } else {
                        p = n;
                    }
                }
            }
        }
    }

    private void removeAllMessagesLocked() {
        Message p = this.mMessages;
        while (p != null) {
            Message n = p.next;
            p.recycleUnchecked();
            p = n;
        }
        this.mMessages = null;
    }

    private void removeAllFutureMessagesLocked() {
        long now = SystemClock.uptimeMillis();
        Message p = this.mMessages;
        if (p == null) {
            return;
        }
        if (p.when > now) {
            removeAllMessagesLocked();
            return;
        }
        while (true) {
            Message n = p.next;
            if (n == null) {
                return;
            }
            if (n.when > now) {
                break;
            }
            p = n;
        }
        p.next = null;
        do {
            p = n;
            n = p.next;
            p.recycleUnchecked();
        } while (n != null);
    }

    void dump(Printer pw, String prefix) {
        synchronized (this) {
            long now = SystemClock.uptimeMillis();
            int n = 0;
            for (Message msg = this.mMessages; msg != null; msg = msg.next) {
                pw.println(prefix + "Message " + n + ": " + msg.toString(now));
                n++;
            }
            pw.println(prefix + "(Total messages: " + n + ", polling=" + isPollingLocked() + ", quitting=" + this.mQuitting + ")");
        }
    }
}
