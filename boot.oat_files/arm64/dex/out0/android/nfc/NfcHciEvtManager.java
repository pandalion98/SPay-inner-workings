package android.nfc;

import android.content.Intent;
import android.nfc.INfcHciEventCallback.Stub;
import android.nfc.NfcAdapter.HciEvtCallback;
import android.os.RemoteException;

public final class NfcHciEvtManager extends Stub {
    static final String TAG = "NFC";
    final NfcAdapter mAdapter;
    private HciEvtCallback mCallback = null;

    public NfcHciEvtManager(NfcAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setOnHciEvtCallback(HciEvtCallback callback) {
        try {
            NfcAdapter.sService.setHciEventCallback(this);
            this.mCallback = callback;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onHciEvtTrasactionDetected(Intent intent) {
        synchronized (this) {
            if (this.mCallback != null) {
                this.mCallback.hciEvtTransaction(intent);
            }
        }
    }
}
