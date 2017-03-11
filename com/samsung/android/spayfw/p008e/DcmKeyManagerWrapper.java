package com.samsung.android.spayfw.p008e;

import android.util.Log;
import com.samsung.android.spayfw.p003c.SdlDcmKeyManager;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;
import javax.net.ssl.X509KeyManager;

/* renamed from: com.samsung.android.spayfw.e.b */
public class DcmKeyManagerWrapper {
    public static X509KeyManager fh() {
        if (Platformutils.fT()) {
            Log.e("DcmKeyManagerWrapper", "it's se device, should not comming here");
            return null;
        }
        Log.d("DcmKeyManagerWrapper", "it's non se device, use DcmKeyManager");
        return SdlDcmKeyManager.fh();
    }
}
