package android.nfc;

import android.content.Context;

public final class NfcManager {
    private NfcAdapter mAdapter;
    private boolean mIsBinded = false;

    public NfcManager(Context context) {
        context = context.getApplicationContext();
        if (context == null) {
            throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
        }
        NfcAdapter adapter;
        try {
            adapter = NfcAdapter.getNfcAdapter(context);
            this.mIsBinded = true;
        } catch (UnsupportedOperationException e) {
            adapter = null;
            this.mIsBinded = false;
        }
        this.mAdapter = adapter;
    }

    public void bindNfcService(Context context) {
        if (!this.mIsBinded) {
            context = context.getApplicationContext();
            if (context == null) {
                throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
            }
            NfcAdapter adapter;
            try {
                adapter = NfcAdapter.getNfcAdapter(context);
                this.mIsBinded = true;
            } catch (UnsupportedOperationException e) {
                adapter = null;
                this.mIsBinded = false;
            }
            this.mAdapter = adapter;
        }
    }

    public NfcAdapter getDefaultAdapter() {
        return this.mAdapter;
    }
}
