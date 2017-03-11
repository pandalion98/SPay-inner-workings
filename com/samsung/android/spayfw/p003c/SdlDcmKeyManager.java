package com.samsung.android.spayfw.p003c;

import com.sec.dcm.DcmKeyManager;
import javax.net.ssl.X509KeyManager;

/* renamed from: com.samsung.android.spayfw.c.b */
public class SdlDcmKeyManager {
    public static X509KeyManager fh() {
        return new DcmKeyManager();
    }
}
