package android.os;

import android.os.IRemoteCallback.Stub;
import android.os.Parcelable.Creator;

public abstract class RemoteCallback implements Parcelable {
    public static final Creator<RemoteCallback> CREATOR = new Creator<RemoteCallback>() {
        public RemoteCallback createFromParcel(Parcel in) {
            IBinder target = in.readStrongBinder();
            return target != null ? new RemoteCallbackProxy(Stub.asInterface(target)) : null;
        }

        public RemoteCallback[] newArray(int size) {
            return new RemoteCallback[size];
        }
    };
    final Handler mHandler;
    final IRemoteCallback mTarget;

    class DeliverResult implements Runnable {
        final Bundle mResult;

        DeliverResult(Bundle result) {
            this.mResult = result;
        }

        public void run() {
            RemoteCallback.this.onResult(this.mResult);
        }
    }

    class LocalCallback extends Stub {
        LocalCallback() {
        }

        public void sendResult(Bundle bundle) {
            RemoteCallback.this.mHandler.post(new DeliverResult(bundle));
        }
    }

    static class RemoteCallbackProxy extends RemoteCallback {
        RemoteCallbackProxy(IRemoteCallback target) {
            super(target);
        }

        protected void onResult(Bundle bundle) {
        }
    }

    protected abstract void onResult(Bundle bundle);

    public RemoteCallback(Handler handler) {
        this.mHandler = handler;
        this.mTarget = new LocalCallback();
    }

    RemoteCallback(IRemoteCallback target) {
        this.mHandler = null;
        this.mTarget = target;
    }

    public void sendResult(Bundle bundle) throws RemoteException {
        this.mTarget.sendResult(bundle);
    }

    public boolean equals(Object otherObj) {
        boolean z = false;
        if (otherObj != null) {
            try {
                z = this.mTarget.asBinder().equals(((RemoteCallback) otherObj).mTarget.asBinder());
            } catch (ClassCastException e) {
            }
        }
        return z;
    }

    public int hashCode() {
        return this.mTarget.asBinder().hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeStrongBinder(this.mTarget.asBinder());
    }
}
