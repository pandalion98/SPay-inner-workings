package android.location;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

abstract class LocalListenerHelper<TListener> {
    private final Context mContext;
    private final HashSet<TListener> mListeners = new HashSet();
    private final String mTag;

    protected interface ListenerOperation<TListener> {
        void execute(TListener tListener) throws RemoteException;
    }

    protected abstract boolean registerWithServer() throws RemoteException;

    protected abstract void unregisterFromServer() throws RemoteException;

    protected LocalListenerHelper(Context context, String name) {
        Preconditions.checkNotNull(name);
        this.mContext = context;
        this.mTag = name;
    }

    public boolean add(TListener listener) {
        boolean z = false;
        Preconditions.checkNotNull(listener);
        synchronized (this.mListeners) {
            if (this.mListeners.isEmpty()) {
                try {
                    if (!registerWithServer()) {
                        Log.e(this.mTag, "Unable to register listener transport.");
                    }
                } catch (RemoteException e) {
                    Log.e(this.mTag, "Error handling first listener.", e);
                }
            }
            if (this.mListeners.contains(listener)) {
                z = true;
            } else {
                z = this.mListeners.add(listener);
            }
        }
        return z;
    }

    public void remove(TListener listener) {
        Preconditions.checkNotNull(listener);
        synchronized (this.mListeners) {
            boolean isLastRemoved = this.mListeners.remove(listener) && this.mListeners.isEmpty();
            if (isLastRemoved) {
                try {
                    unregisterFromServer();
                } catch (RemoteException e) {
                    Log.v(this.mTag, "Error handling last listener removal", e);
                }
            }
        }
    }

    protected Context getContext() {
        return this.mContext;
    }

    protected void foreach(ListenerOperation<TListener> operation) {
        synchronized (this.mListeners) {
            Collection<TListener> listeners = new ArrayList(this.mListeners);
        }
        for (TListener listener : listeners) {
            try {
                operation.execute(listener);
            } catch (RemoteException e) {
                Log.e(this.mTag, "Error in monitored listener.", e);
            }
        }
    }
}
