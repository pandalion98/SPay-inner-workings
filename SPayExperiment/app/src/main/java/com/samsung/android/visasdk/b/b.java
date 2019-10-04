/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.b;

import com.samsung.android.visasdk.a.c;
import com.samsung.android.visasdk.b.a;
import com.samsung.android.visasdk.facade.exception.CryptoException;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.visa.tainterface.VisaTAController;
import com.visa.tainterface.VisaTAException;
import java.io.ByteArrayOutputStream;

public class b {
    public static final boolean Dk = true & c.LOG_DEBUG;
    private static b Dl = null;
    private static a Dm = null;

    b() {
        Dm = new a();
    }

    private String bG(String string) {
        com.samsung.android.visasdk.c.a.d("CryptoManager", "extractTokenFromBlob()");
        int n2 = string.length();
        String string2 = "";
        if (n2 <= 0) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "extractTokenFromBlob(), token is empty");
            return null;
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            if (string.charAt(i2) < '0' || string.charAt(i2) > '9') {
                continue;
            }
            while (i2 < n2 && string.charAt(i2) >= '0' && string.charAt(i2) <= '9') {
                String string3 = string2 + string.charAt(i2);
                ++i2;
                string2 = string3;
            }
            break block0;
        }
        com.samsung.android.visasdk.c.a.d("CryptoManager", "extracted token is " + string2);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] d(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        com.samsung.android.visasdk.c.a.d("CryptoManager", "generateQVSDCandMSDVV() ");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (arrby2 == null || arrby2.length == 0) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "generateQVSDCandMSDVV  QVSDC Input error");
            return null;
        }
        byteArrayOutputStream.write((int)((byte)arrby2.length));
        byteArrayOutputStream.write(arrby2, 0, arrby2.length);
        if (arrby3 != null && arrby3.length > 0) {
            com.samsung.android.visasdk.c.a.d("CryptoManager", "MSDVVInput ");
            byteArrayOutputStream.write((int)((byte)arrby3.length));
            byteArrayOutputStream.write(arrby3, 0, arrby3.length);
        } else {
            byteArrayOutputStream.write(0);
        }
        byte[] arrby4 = byteArrayOutputStream.toByteArray();
        com.samsung.android.visasdk.c.a.d("CryptoManager", "generateQVSDCandMSDVV() transactionData: " + com.samsung.android.visasdk.a.b.o(arrby4));
        byte[] arrby5 = Dm.a(arrby, VisaTAController.VISA_CRYPTO_ALG.MT, arrby4);
        if (arrby5 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "generateQVSDCandMSDVV  failed");
            return null;
        }
        return arrby5;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static b fV() {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (Dl != null) return Dl;
            Dl = new b();
            if (Dl != null) return Dl;
            throw new InitializationException("cannot initialize crypto instance");
        }
    }

    public static byte[] i(byte[] arrby, byte[] arrby2) {
        com.samsung.android.visasdk.c.a.d("CryptoManager", "generateApplicationCryptogram() transactionData: " + com.samsung.android.visasdk.a.b.o(arrby2));
        byte[] arrby3 = Dm.a(arrby, VisaTAController.VISA_CRYPTO_ALG.MP, arrby2);
        if (arrby3 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "application cryptogram(ARQC) generation failed");
            arrby3 = null;
        }
        return arrby3;
    }

    public static byte[] j(byte[] arrby, byte[] arrby2) {
        com.samsung.android.visasdk.c.a.d("CryptoManager", "generateMstArqcCryptogram() transactionData: " + com.samsung.android.visasdk.a.b.o(arrby2));
        byte[] arrby3 = Dm.a(arrby, VisaTAController.VISA_CRYPTO_ALG.MR, arrby2);
        if (arrby3 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "application cryptogram(ARQC) generation failed");
            arrby3 = null;
        }
        return arrby3;
    }

    public static byte[] k(byte[] arrby, byte[] arrby2) {
        com.samsung.android.visasdk.c.a.d("CryptoManager", "generateSDAD()");
        byte[] arrby3 = Dm.a(arrby, VisaTAController.VISA_CRYPTO_ALG.MS, arrby2);
        if (arrby3 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "SDAD generation failed");
            arrby3 = null;
        }
        return arrby3;
    }

    public String a(byte by, String string) {
        com.samsung.android.visasdk.c.a.d("CryptoManager", "storeNFCVTSData()");
        throw new CryptoException("storeNFCVTSData is only used for test purpose");
    }

    public boolean a(byte[] arrby, com.visa.tainterface.a a2) {
        return Dm.a(arrby, a2);
    }

    public byte[] b(byte[] arrby, boolean bl) {
        return Dm.b(arrby, bl);
    }

    public String bH(String string) {
        com.samsung.android.visasdk.c.a.d("CryptoManager", "storeVTSData(), encVTSData=" + string);
        if (string == null) {
            throw new VisaTAException("input is null", 5);
        }
        byte[] arrby = string.getBytes();
        byte[] arrby2 = Dm.q(arrby);
        if (arrby2 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "storeNFCVTSData failed");
            return null;
        }
        if (Dk) {
            com.samsung.android.visasdk.a.b.e("storeVTSData(), encrypted data=", arrby2);
        }
        return com.samsung.android.visasdk.a.b.o(arrby2);
    }

    public byte[] h(byte[] arrby, byte[] arrby2) {
        byte[] arrby3;
        com.samsung.android.visasdk.c.a.d("CryptoManager", "generateMSDVerificationValue()");
        if (Dk) {
            com.samsung.android.visasdk.a.b.e("generateMSDVerificationValue(), encryptedLUK=", arrby);
        }
        if ((arrby3 = Dm.a(arrby, VisaTAController.VISA_CRYPTO_ALG.MQ, arrby2)) == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "MSD verification value generation failed");
            arrby3 = null;
        }
        return arrby3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String r(byte[] arrby) {
        byte[] arrby2 = Dm.retrieveFromStorage(arrby);
        if (arrby2 == null) {
            com.samsung.android.visasdk.c.a.d("CryptoManager", "decrypted token is null");
            return null;
        }
        String string = new String(arrby2);
        if (Dk) {
            com.samsung.android.visasdk.c.a.d("CryptoManager", "retrieveTokenFromStorage(), decToken is " + string + ", length is " + string.length());
        }
        if (string == null) return string;
        if (string.length() <= 0) return string;
        return this.bG(string);
    }

    public byte[] retrieveFromStorage(byte[] arrby) {
        if (arrby == null || arrby.length <= 0) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", " data to decrypt is null");
            return null;
        }
        byte[] arrby2 = Dm.retrieveFromStorage(arrby);
        if (arrby2 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", "decrypted data is null");
            return null;
        }
        return arrby2;
    }

    public byte[] storeData(byte[] arrby) {
        if (arrby == null || arrby.length <= 0) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", " data to encrypt is null");
            return null;
        }
        byte[] arrby2 = Dm.storeData(arrby);
        if (arrby2 == null) {
            com.samsung.android.visasdk.c.a.e("CryptoManager", " encrypted data is null");
            return null;
        }
        return arrby2;
    }
}

