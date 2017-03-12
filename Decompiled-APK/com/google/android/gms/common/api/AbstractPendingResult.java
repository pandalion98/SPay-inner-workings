package com.google.android.gms.common.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.api.PendingResult.BatchCallback;
import com.google.android.gms.common.internal.ICancelToken;
import com.google.android.gms.common.internal.zzx;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractPendingResult<R extends Result> implements PendingResult<R> {
    protected final CallbackHandler<R> mHandler;
    private final Object zzLK;
    private final ArrayList<BatchCallback> zzLL;
    private ResultCallback<R> zzLM;
    private volatile R zzLN;
    private volatile boolean zzLO;
    private boolean zzLP;
    private boolean zzLQ;
    private ICancelToken zzLR;
    private final CountDownLatch zzmx;

    public static class CallbackHandler<R extends Result> extends Handler {
        public static final int CALLBACK_ON_COMPLETE = 1;
        public static final int CALLBACK_ON_TIMEOUT = 2;

        public CallbackHandler() {
            this(Looper.getMainLooper());
        }

        public CallbackHandler(Looper looper) {
            super(looper);
        }

        protected void deliverResultCallback(ResultCallback<R> resultCallback, R r) {
            try {
                resultCallback.onResult(r);
            } catch (RuntimeException e) {
                AbstractPendingResult.zzb(r);
                throw e;
            }
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case CALLBACK_ON_COMPLETE /*1*/:
                    Pair pair = (Pair) message.obj;
                    deliverResultCallback((ResultCallback) pair.first, (Result) pair.second);
                case CALLBACK_ON_TIMEOUT /*2*/:
                    ((AbstractPendingResult) message.obj).zzhS();
                default:
                    Log.wtf("AbstractPendingResult", "Don't know how to handle this message.");
            }
        }

        public void removeTimeoutMessages() {
            removeMessages(CALLBACK_ON_TIMEOUT);
        }

        public void sendResultCallback(ResultCallback<R> resultCallback, R r) {
            sendMessage(obtainMessage(CALLBACK_ON_COMPLETE, new Pair(resultCallback, r)));
        }

        public void sendTimeoutResultCallback(AbstractPendingResult<R> abstractPendingResult, long j) {
            sendMessageDelayed(obtainMessage(CALLBACK_ON_TIMEOUT, abstractPendingResult), j);
        }
    }

    protected AbstractPendingResult(Looper looper) {
        this.zzLK = new Object();
        this.zzmx = new CountDownLatch(1);
        this.zzLL = new ArrayList();
        this.mHandler = new CallbackHandler(looper);
    }

    protected AbstractPendingResult(CallbackHandler<R> callbackHandler) {
        this.zzLK = new Object();
        this.zzmx = new CountDownLatch(1);
        this.zzLL = new ArrayList();
        this.mHandler = callbackHandler;
    }

    private void zza(R r) {
        this.zzLN = r;
        this.zzLR = null;
        this.zzmx.countDown();
        Status status = this.zzLN.getStatus();
        if (this.zzLM != null) {
            this.mHandler.removeTimeoutMessages();
            if (!this.zzLP) {
                this.mHandler.sendResultCallback(this.zzLM, zzhQ());
            }
        }
        Iterator it = this.zzLL.iterator();
        while (it.hasNext()) {
            ((BatchCallback) it.next()).zzl(status);
        }
        this.zzLL.clear();
    }

    static void zzb(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (Throwable e) {
                Log.w("AbstractPendingResult", "Unable to release " + result, e);
            }
        }
    }

    private R zzhQ() {
        R r;
        boolean z = true;
        synchronized (this.zzLK) {
            if (this.zzLO) {
                z = false;
            }
            zzx.zza(z, (Object) "Result has already been consumed.");
            zzx.zza(isReady(), (Object) "Result is not ready.");
            r = this.zzLN;
            this.zzLN = null;
            this.zzLM = null;
            this.zzLO = true;
        }
        onResultConsumed();
        return r;
    }

    private void zzhR() {
        synchronized (this.zzLK) {
            if (!isReady()) {
                setResult(createFailedResult(Status.zzNp));
                this.zzLQ = true;
            }
        }
    }

    public final void addBatchCallback(BatchCallback batchCallback) {
        zzx.zza(!this.zzLO, (Object) "Result has already been consumed.");
        synchronized (this.zzLK) {
            if (isReady()) {
                batchCallback.zzl(this.zzLN.getStatus());
            } else {
                this.zzLL.add(batchCallback);
            }
        }
    }

    public final R await() {
        boolean z = true;
        zzx.zza(Looper.myLooper() != Looper.getMainLooper(), (Object) "await must not be called on the UI thread");
        if (this.zzLO) {
            z = false;
        }
        zzx.zza(z, (Object) "Result has already been consumed");
        try {
            this.zzmx.await();
        } catch (InterruptedException e) {
            zzhR();
        }
        zzx.zza(isReady(), (Object) "Result is not ready.");
        return zzhQ();
    }

    public final R await(long j, TimeUnit timeUnit) {
        boolean z = true;
        boolean z2 = j <= 0 || Looper.myLooper() != Looper.getMainLooper();
        zzx.zza(z2, (Object) "await must not be called on the UI thread when time is greater than zero.");
        if (this.zzLO) {
            z = false;
        }
        zzx.zza(z, (Object) "Result has already been consumed.");
        try {
            if (!this.zzmx.await(j, timeUnit)) {
                zzhS();
            }
        } catch (InterruptedException e) {
            zzhR();
        }
        zzx.zza(isReady(), (Object) "Result is not ready.");
        return zzhQ();
    }

    public void cancel() {
        synchronized (this.zzLK) {
            if (this.zzLP || this.zzLO) {
                return;
            }
            if (this.zzLR != null) {
                try {
                    this.zzLR.cancel();
                } catch (RemoteException e) {
                }
            }
            zzb(this.zzLN);
            this.zzLM = null;
            this.zzLP = true;
            zza(createFailedResult(Status.zzNs));
        }
    }

    protected abstract R createFailedResult(Status status);

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzLK) {
            z = this.zzLP;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zzmx.getCount() == 0;
    }

    protected void onResultConsumed() {
    }

    protected final void setCancelToken(ICancelToken iCancelToken) {
        synchronized (this.zzLK) {
            this.zzLR = iCancelToken;
        }
    }

    public final void setResult(R r) {
        boolean z = true;
        synchronized (this.zzLK) {
            if (this.zzLQ || this.zzLP) {
                zzb(r);
                return;
            }
            zzx.zza(!isReady(), (Object) "Results have already been set");
            if (this.zzLO) {
                z = false;
            }
            zzx.zza(z, (Object) "Result has already been consumed");
            zza(r);
        }
    }

    public final void setResultCallback(ResultCallback<R> resultCallback) {
        zzx.zza(!this.zzLO, (Object) "Result has already been consumed.");
        synchronized (this.zzLK) {
            if (isCanceled()) {
                return;
            }
            if (isReady()) {
                this.mHandler.sendResultCallback(resultCallback, zzhQ());
            } else {
                this.zzLM = resultCallback;
            }
        }
    }

    public final void setResultCallback(ResultCallback<R> resultCallback, long j, TimeUnit timeUnit) {
        boolean z = true;
        zzx.zza(!this.zzLO, (Object) "Result has already been consumed.");
        if (this.mHandler == null) {
            z = false;
        }
        zzx.zza(z, (Object) "CallbackHandler has not been set before calling setResultCallback.");
        synchronized (this.zzLK) {
            if (isCanceled()) {
                return;
            }
            if (isReady()) {
                this.mHandler.sendResultCallback(resultCallback, zzhQ());
            } else {
                this.zzLM = resultCallback;
                this.mHandler.sendTimeoutResultCallback(this, timeUnit.toMillis(j));
            }
        }
    }

    void zzhS() {
        synchronized (this.zzLK) {
            if (!isReady()) {
                setResult(createFailedResult(Status.zzNr));
                this.zzLQ = true;
            }
        }
    }
}
