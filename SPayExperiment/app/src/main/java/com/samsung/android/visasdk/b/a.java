/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.b;

import com.samsung.android.visasdk.b.b;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.visa.tainterface.VisaTAController;

class a {
    public static final boolean Dk = b.Dk;
    private VisaTAController zH = VisaTAController.iq();

    a() {
        if (this.zH == null) {
            throw new InitializationException("cannot initialize visa TA controller");
        }
    }

    boolean a(byte[] arrby, com.visa.tainterface.a a2) {
        return this.zH.a(arrby, a2);
    }

    /*
     * Enabled aggressive block sorting
     */
    byte[] a(byte[] arrby, VisaTAController.VISA_CRYPTO_ALG vISA_CRYPTO_ALG, byte[] arrby2) {
        com.samsung.android.visasdk.c.a.d("CryptoInterface", "generate()");
        byte[] arrby3 = this.zH.a(arrby, vISA_CRYPTO_ALG, arrby2);
        if (arrby3 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoInterface", "generate(), responseData is null");
            return arrby3;
        } else {
            if (!Dk) return arrby3;
            {
                com.samsung.android.visasdk.c.a.d("CryptoInterface", "generate(), responseData=" + com.samsung.android.visasdk.a.b.o(arrby3));
                return arrby3;
            }
        }
    }

    byte[] b(byte[] arrby, boolean bl) {
        if (Dk) {
            com.samsung.android.visasdk.a.b.e("prepareDataForVTS(), encData=", arrby);
        }
        return this.zH.b(arrby, bl);
    }

    byte[] q(byte[] arrby) {
        com.samsung.android.visasdk.c.a.d("CryptoInterface", "storeVTSData(), vtsData=" + com.samsung.android.visasdk.a.b.o(arrby));
        return this.zH.q(arrby);
    }

    byte[] retrieveFromStorage(byte[] arrby) {
        if (Dk) {
            com.samsung.android.visasdk.a.b.e("retrieveTokenFromStorage(), encData(HEX)=", arrby);
        }
        return this.zH.retrieveFromStorage(arrby);
    }

    byte[] storeData(byte[] arrby) {
        com.samsung.android.visasdk.c.a.d("CryptoInterface", "storeData()");
        return this.zH.storeData(arrby);
    }
}

