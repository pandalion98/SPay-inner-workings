package com.sec.smartcard.openssl;

import android.sec.enterprise.ClientCertificateManager;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.util.Log;
import com.android.org.conscrypt.OpenSSLEngine;
import java.security.InvalidKeyException;
import java.security.PrivateKey;

public class OpenSSLHelper {
    private static final String FUNCTION_LIST_NAME = "TZ_CCM_C_GetFunctionList";
    private static final String LIBRARY_NAME = "libtlc_tz_ccm.so";
    static final String TAG = "OpenSSLHelper";
    private PrivateKey pkey = null;

    public native int deregisterEngineKeychain();

    public native int registerEngineKeychain(String str, String str2, long j);

    static {
        System.loadLibrary("secopenssl_engine");
    }

    protected long getSlotID(String alias) {
        Log.d(TAG, "getSlotID function");
        ClientCertificateManager ccm = EnterpriseDeviceManager.getInstance().getClientCertificateManager();
        if (ccm != null) {
            return ccm.getSlotIdForCaller(alias);
        }
        return -1;
    }

    public boolean registerEngine(String alias) {
        Log.d(TAG, "registerEngine function");
        long slotid = getSlotID(alias);
        if (0 > slotid) {
            Log.d(TAG, "registerEngine - getSlotID returned invalid slotid = " + slotid);
            return false;
        } else if (new OpenSSLHelper().registerEngineKeychain(LIBRARY_NAME, FUNCTION_LIST_NAME, slotid) != 0) {
            return false;
        } else {
            Log.e(TAG, "Register engine success");
            return true;
        }
    }

    public boolean deregister_engine() {
        Log.d(TAG, "deregister_engine function");
        if (new OpenSSLHelper().deregisterEngineKeychain() != 0) {
            return false;
        }
        Log.e(TAG, "DeRegister engine success");
        return true;
    }

    public PrivateKey getPrivateKey(String alias) {
        Log.d(TAG, "getPrivateKey function");
        if (this.pkey == null) {
            OpenSSLEngine engine = OpenSSLEngine.getCustomInstance("secpkcs11");
            if (engine != null) {
                try {
                    this.pkey = engine.getPrivateKeyById(alias);
                } catch (InvalidKeyException e) {
                    Log.d(TAG, "InvalidKeyException");
                }
            }
        }
        return this.pkey;
    }
}
