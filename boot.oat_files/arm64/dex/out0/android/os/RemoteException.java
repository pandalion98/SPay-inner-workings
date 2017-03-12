package android.os;

import android.util.AndroidException;

public class RemoteException extends AndroidException {
    public RemoteException(String message) {
        super(message);
    }

    public RuntimeException rethrowAsRuntimeException() {
        throw new RuntimeException(this);
    }
}
