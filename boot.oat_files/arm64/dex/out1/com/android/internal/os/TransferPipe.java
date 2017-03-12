package com.android.internal.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import java.io.FileDescriptor;
import java.io.IOException;

public final class TransferPipe implements Runnable {
    static final boolean DEBUG = false;
    static final long DEFAULT_TIMEOUT = 5000;
    static final String TAG = "TransferPipe";
    String mBufferPrefix;
    boolean mComplete;
    long mEndTime;
    String mFailure;
    final ParcelFileDescriptor[] mFds = ParcelFileDescriptor.createPipe();
    FileDescriptor mOutFd;
    final Thread mThread = new Thread(this, TAG);

    interface Caller {
        void go(IInterface iInterface, FileDescriptor fileDescriptor, String str, String[] strArr) throws RemoteException;
    }

    ParcelFileDescriptor getReadFd() {
        return this.mFds[0];
    }

    public ParcelFileDescriptor getWriteFd() {
        return this.mFds[1];
    }

    public void setBufferPrefix(String prefix) {
        this.mBufferPrefix = prefix;
    }

    static void go(Caller caller, IInterface iface, FileDescriptor out, String prefix, String[] args) throws IOException, RemoteException {
        go(caller, iface, out, prefix, args, 5000);
    }

    static void go(Caller caller, IInterface iface, FileDescriptor out, String prefix, String[] args, long timeout) throws IOException, RemoteException {
        if (iface.asBinder() instanceof Binder) {
            try {
                caller.go(iface, out, prefix, args);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        TransferPipe tp = new TransferPipe();
        try {
            caller.go(iface, tp.getWriteFd().getFileDescriptor(), prefix, args);
            tp.go(out, timeout);
        } finally {
            tp.kill();
        }
    }

    static void goDump(IBinder binder, FileDescriptor out, String[] args) throws IOException, RemoteException {
        goDump(binder, out, args, 5000);
    }

    static void goDump(IBinder binder, FileDescriptor out, String[] args, long timeout) throws IOException, RemoteException {
        if (binder instanceof Binder) {
            try {
                binder.dump(out, args);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        TransferPipe tp = new TransferPipe();
        try {
            binder.dumpAsync(tp.getWriteFd().getFileDescriptor(), args);
            tp.go(out, timeout);
        } finally {
            tp.kill();
        }
    }

    public void go(FileDescriptor out) throws IOException {
        go(out, 5000);
    }

    public void go(FileDescriptor out, long timeout) throws IOException {
        try {
            synchronized (this) {
                this.mOutFd = out;
                this.mEndTime = SystemClock.uptimeMillis() + timeout;
                closeFd(1);
                this.mThread.start();
                while (this.mFailure == null && !this.mComplete) {
                    long waitTime = this.mEndTime - SystemClock.uptimeMillis();
                    if (waitTime <= 0) {
                        this.mThread.interrupt();
                        throw new IOException("Timeout");
                    }
                    try {
                        wait(waitTime);
                    } catch (InterruptedException e) {
                    }
                }
                if (this.mFailure != null) {
                    throw new IOException(this.mFailure);
                }
            }
            kill();
        } catch (Throwable th) {
            kill();
        }
    }

    void closeFd(int num) {
        if (this.mFds[num] != null) {
            try {
                this.mFds[num].close();
            } catch (IOException e) {
            }
            this.mFds[num] = null;
        }
    }

    public void kill() {
        synchronized (this) {
            closeFd(0);
            closeFd(1);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r12 = this;
        r11 = 10;
        r10 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r10];
        monitor-enter(r12);
        r7 = r12.getReadFd();	 Catch:{ all -> 0x0050 }
        if (r7 != 0) goto L_0x0016;
    L_0x000d:
        r10 = "TransferPipe";
        r11 = "Pipe has been closed...";
        android.util.Slog.w(r10, r11);	 Catch:{ all -> 0x0050 }
        monitor-exit(r12);	 Catch:{ all -> 0x0050 }
    L_0x0015:
        return;
    L_0x0016:
        r3 = new java.io.FileInputStream;	 Catch:{ all -> 0x0050 }
        r10 = r7.getFileDescriptor();	 Catch:{ all -> 0x0050 }
        r3.<init>(r10);	 Catch:{ all -> 0x0050 }
        r4 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0050 }
        r10 = r12.mOutFd;	 Catch:{ all -> 0x0050 }
        r4.<init>(r10);	 Catch:{ all -> 0x0050 }
        monitor-exit(r12);	 Catch:{ all -> 0x0050 }
        r1 = 0;
        r6 = 1;
        r10 = r12.mBufferPrefix;
        if (r10 == 0) goto L_0x0033;
    L_0x002d:
        r10 = r12.mBufferPrefix;
        r1 = r10.getBytes();
    L_0x0033:
        r8 = r3.read(r0);	 Catch:{ IOException -> 0x0040 }
        if (r8 <= 0) goto L_0x007f;
    L_0x0039:
        if (r1 != 0) goto L_0x0053;
    L_0x003b:
        r10 = 0;
        r4.write(r0, r10, r8);	 Catch:{ IOException -> 0x0040 }
        goto L_0x0033;
    L_0x0040:
        r2 = move-exception;
        monitor-enter(r12);
        r10 = r2.toString();	 Catch:{ all -> 0x004d }
        r12.mFailure = r10;	 Catch:{ all -> 0x004d }
        r12.notifyAll();	 Catch:{ all -> 0x004d }
        monitor-exit(r12);	 Catch:{ all -> 0x004d }
        goto L_0x0015;
    L_0x004d:
        r10 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x004d }
        throw r10;
    L_0x0050:
        r10 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0050 }
        throw r10;
    L_0x0053:
        r9 = 0;
        r5 = 0;
    L_0x0055:
        if (r5 >= r8) goto L_0x0077;
    L_0x0057:
        r10 = r0[r5];	 Catch:{ IOException -> 0x0040 }
        if (r10 == r11) goto L_0x0074;
    L_0x005b:
        if (r5 <= r9) goto L_0x0062;
    L_0x005d:
        r10 = r5 - r9;
        r4.write(r0, r9, r10);	 Catch:{ IOException -> 0x0040 }
    L_0x0062:
        r9 = r5;
        if (r6 == 0) goto L_0x0069;
    L_0x0065:
        r4.write(r1);	 Catch:{ IOException -> 0x0040 }
        r6 = 0;
    L_0x0069:
        r5 = r5 + 1;
        if (r5 >= r8) goto L_0x0071;
    L_0x006d:
        r10 = r0[r5];	 Catch:{ IOException -> 0x0040 }
        if (r10 != r11) goto L_0x0069;
    L_0x0071:
        if (r5 >= r8) goto L_0x0074;
    L_0x0073:
        r6 = 1;
    L_0x0074:
        r5 = r5 + 1;
        goto L_0x0055;
    L_0x0077:
        if (r8 <= r9) goto L_0x0033;
    L_0x0079:
        r10 = r8 - r9;
        r4.write(r0, r9, r10);	 Catch:{ IOException -> 0x0040 }
        goto L_0x0033;
    L_0x007f:
        r10 = r12.mThread;	 Catch:{ IOException -> 0x0040 }
        r10 = r10.isInterrupted();	 Catch:{ IOException -> 0x0040 }
        if (r10 == 0) goto L_0x0087;
    L_0x0087:
        monitor-enter(r12);
        r10 = 1;
        r12.mComplete = r10;	 Catch:{ all -> 0x0090 }
        r12.notifyAll();	 Catch:{ all -> 0x0090 }
        monitor-exit(r12);	 Catch:{ all -> 0x0090 }
        goto L_0x0015;
    L_0x0090:
        r10 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0090 }
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.TransferPipe.run():void");
    }
}
