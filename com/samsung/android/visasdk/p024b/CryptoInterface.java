package com.samsung.android.visasdk.p024b;

import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.p023a.Utils;
import com.samsung.android.visasdk.p025c.Log;
import com.visa.tainterface.TrackData;
import com.visa.tainterface.VisaTAController;
import com.visa.tainterface.VisaTAController.VISA_CRYPTO_ALG;

/* renamed from: com.samsung.android.visasdk.b.a */
class CryptoInterface {
    public static final boolean Dk;
    private VisaTAController zH;

    static {
        Dk = CryptoManager.Dk;
    }

    CryptoInterface() {
        this.zH = null;
        this.zH = VisaTAController.iq();
        if (this.zH == null) {
            throw new InitializationException("cannot initialize visa TA controller");
        }
    }

    byte[] m1288a(byte[] bArr, VISA_CRYPTO_ALG visa_crypto_alg, byte[] bArr2) {
        Log.m1300d("CryptoInterface", "generate()");
        byte[] a = this.zH.m1712a(bArr, visa_crypto_alg, bArr2);
        if (a == null) {
            Log.m1301e("CryptoInterface", "generate(), responseData is null");
        } else if (Dk) {
            Log.m1300d("CryptoInterface", "generate(), responseData=" + Utils.m1285o(a));
        }
        return a;
    }

    byte[] retrieveFromStorage(byte[] bArr) {
        if (Dk) {
            Utils.m1283e("retrieveTokenFromStorage(), encData(HEX)=", bArr);
        }
        return this.zH.retrieveFromStorage(bArr);
    }

    byte[] m1289b(byte[] bArr, boolean z) {
        if (Dk) {
            Utils.m1283e("prepareDataForVTS(), encData=", bArr);
        }
        return this.zH.m1713b(bArr, z);
    }

    byte[] m1290q(byte[] bArr) {
        Log.m1300d("CryptoInterface", "storeVTSData(), vtsData=" + Utils.m1285o(bArr));
        return this.zH.m1717q(bArr);
    }

    byte[] storeData(byte[] bArr) {
        Log.m1300d("CryptoInterface", "storeData()");
        return this.zH.storeData(bArr);
    }

    boolean m1287a(byte[] bArr, TrackData trackData) {
        return this.zH.m1711a(bArr, trackData);
    }
}
