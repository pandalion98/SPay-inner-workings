package android.hardware.scontext;

import android.os.Bundle;
import android.util.Log;

public class SContextWakeUpVoiceAttribute extends SContextAttribute {
    static int MODE_REFERENCE_DATA = 1;
    static int MODE_REGISTER = 0;
    private static final String TAG = "SContextWakeUpVoiceAttribute";
    private byte[] mGramData;
    private int mMode;
    private byte[] mNetData;
    private int mVoiceMode;

    SContextWakeUpVoiceAttribute() {
        this.mMode = -1;
        this.mVoiceMode = 1;
        this.mNetData = null;
        this.mGramData = null;
        this.mMode = MODE_REGISTER;
        setAttribute();
    }

    SContextWakeUpVoiceAttribute(int mode) {
        this.mMode = -1;
        this.mVoiceMode = 1;
        this.mNetData = null;
        this.mGramData = null;
        this.mMode = MODE_REGISTER;
        this.mVoiceMode = mode;
        setAttribute();
    }

    public SContextWakeUpVoiceAttribute(byte[] netData, byte[] gramData) {
        this.mMode = -1;
        this.mVoiceMode = 1;
        this.mNetData = null;
        this.mGramData = null;
        this.mMode = MODE_REFERENCE_DATA;
        this.mNetData = netData;
        this.mGramData = gramData;
        setAttribute();
    }

    boolean checkAttribute() {
        if (this.mMode == MODE_REGISTER) {
            if (this.mVoiceMode == 1 || this.mVoiceMode == 2) {
                return true;
            }
            return false;
        } else if (this.mNetData == null) {
            Log.e(TAG, "The net data is null.");
            return false;
        } else if (this.mGramData != null) {
            return true;
        } else {
            Log.e(TAG, "The gram data is null.");
            return false;
        }
    }

    private void setAttribute() {
        Bundle attribute = new Bundle();
        attribute.putInt("mode", this.mMode);
        if (this.mMode == MODE_REGISTER) {
            attribute.putInt("voice_mode", this.mVoiceMode);
        } else {
            attribute.putByteArray("net_data", this.mNetData);
            attribute.putByteArray("gram_data", this.mGramData);
        }
        super.setAttribute(16, attribute);
    }
}
