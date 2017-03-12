package com.samsung.android.visasdk.p024b;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.exception.CryptoException;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.p023a.Utils;
import com.samsung.android.visasdk.p023a.Version;
import com.samsung.android.visasdk.p025c.Log;
import com.visa.tainterface.TrackData;
import com.visa.tainterface.VisaTAController.VISA_CRYPTO_ALG;
import com.visa.tainterface.VisaTAException;
import java.io.ByteArrayOutputStream;

/* renamed from: com.samsung.android.visasdk.b.b */
public class CryptoManager {
    public static final boolean Dk;
    private static CryptoManager Dl;
    private static CryptoInterface Dm;

    static {
        Dk = Version.LOG_DEBUG & 1;
        Dl = null;
        Dm = null;
    }

    public static synchronized CryptoManager fV() {
        CryptoManager cryptoManager;
        synchronized (CryptoManager.class) {
            if (Dl == null) {
                Dl = new CryptoManager();
                if (Dl == null) {
                    throw new InitializationException("cannot initialize crypto instance");
                }
            }
            cryptoManager = Dl;
        }
        return cryptoManager;
    }

    CryptoManager() {
        Dm = new CryptoInterface();
    }

    private String bG(String str) {
        Log.m1300d("CryptoManager", "extractTokenFromBlob()");
        int length = str.length();
        String str2 = BuildConfig.FLAVOR;
        if (length <= 0) {
            Log.m1301e("CryptoManager", "extractTokenFromBlob(), token is empty");
            return null;
        }
        int i = 0;
        while (i < length) {
            if (str.charAt(i) < LLVARUtil.EMPTY_STRING || str.charAt(i) > '9') {
                i++;
            } else {
                while (i < length && str.charAt(i) >= LLVARUtil.EMPTY_STRING && str.charAt(i) <= '9') {
                    String str3 = str2 + str.charAt(i);
                    i++;
                    str2 = str3;
                }
                Log.m1300d("CryptoManager", "extracted token is " + str2);
                return str2;
            }
        }
        Log.m1300d("CryptoManager", "extracted token is " + str2);
        return str2;
    }

    public String m1295a(byte b, String str) {
        Log.m1300d("CryptoManager", "storeNFCVTSData()");
        throw new CryptoException("storeNFCVTSData is only used for test purpose");
    }

    public String bH(String str) {
        Log.m1300d("CryptoManager", "storeVTSData(), encVTSData=" + str);
        if (str == null) {
            throw new VisaTAException("input is null", 5);
        }
        byte[] q = Dm.m1290q(str.getBytes());
        if (q == null) {
            Log.m1301e("CryptoManager", "storeNFCVTSData failed");
            return null;
        }
        if (Dk) {
            Utils.m1283e("storeVTSData(), encrypted data=", q);
        }
        return Utils.m1285o(q);
    }

    public byte[] m1297b(byte[] bArr, boolean z) {
        return Dm.m1289b(bArr, z);
    }

    public String m1299r(byte[] bArr) {
        byte[] retrieveFromStorage = Dm.retrieveFromStorage(bArr);
        if (retrieveFromStorage == null) {
            Log.m1300d("CryptoManager", "decrypted token is null");
            return null;
        }
        String str = new String(retrieveFromStorage);
        if (Dk) {
            Log.m1300d("CryptoManager", "retrieveTokenFromStorage(), decToken is " + str + ", length is " + str.length());
        }
        if (str == null || str.length() <= 0) {
            return str;
        }
        return bG(str);
    }

    public byte[] storeData(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            Log.m1301e("CryptoManager", " data to encrypt is null");
            return null;
        }
        byte[] storeData = Dm.storeData(bArr);
        if (storeData != null) {
            return storeData;
        }
        Log.m1301e("CryptoManager", " encrypted data is null");
        return null;
    }

    public byte[] retrieveFromStorage(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            Log.m1301e("CryptoManager", " data to decrypt is null");
            return null;
        }
        byte[] retrieveFromStorage = Dm.retrieveFromStorage(bArr);
        if (retrieveFromStorage != null) {
            return retrieveFromStorage;
        }
        Log.m1301e("CryptoManager", "decrypted data is null");
        return null;
    }

    public byte[] m1298h(byte[] bArr, byte[] bArr2) {
        Log.m1300d("CryptoManager", "generateMSDVerificationValue()");
        if (Dk) {
            Utils.m1283e("generateMSDVerificationValue(), encryptedLUK=", bArr);
        }
        byte[] a = Dm.m1288a(bArr, VISA_CRYPTO_ALG.VISA_ALG_NFC_MSD_CRYPTOGRAM, bArr2);
        if (a != null) {
            return a;
        }
        Log.m1301e("CryptoManager", "MSD verification value generation failed");
        return null;
    }

    public static byte[] m1292i(byte[] bArr, byte[] bArr2) {
        Log.m1300d("CryptoManager", "generateApplicationCryptogram() transactionData: " + Utils.m1285o(bArr2));
        byte[] a = Dm.m1288a(bArr, VISA_CRYPTO_ALG.VISA_ALG_NFC_QVSDC_CRYPTOGRAM, bArr2);
        if (a != null) {
            return a;
        }
        Log.m1301e("CryptoManager", "application cryptogram(ARQC) generation failed");
        return null;
    }

    public static byte[] m1293j(byte[] bArr, byte[] bArr2) {
        Log.m1300d("CryptoManager", "generateMstArqcCryptogram() transactionData: " + Utils.m1285o(bArr2));
        byte[] a = Dm.m1288a(bArr, VISA_CRYPTO_ALG.VISA_ALG_MST_ARQC_CRYPTOGRAM, bArr2);
        if (a != null) {
            return a;
        }
        Log.m1301e("CryptoManager", "application cryptogram(ARQC) generation failed");
        return null;
    }

    public boolean m1296a(byte[] bArr, TrackData trackData) {
        return Dm.m1287a(bArr, trackData);
    }

    public static byte[] m1294k(byte[] bArr, byte[] bArr2) {
        Log.m1300d("CryptoManager", "generateSDAD()");
        byte[] a = Dm.m1288a(bArr, VISA_CRYPTO_ALG.VISA_ALG_NFC_SDAD, bArr2);
        if (a != null) {
            return a;
        }
        Log.m1301e("CryptoManager", "SDAD generation failed");
        return null;
    }

    public static byte[] m1291d(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        Log.m1300d("CryptoManager", "generateQVSDCandMSDVV() ");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bArr2 == null || bArr2.length == 0) {
            Log.m1301e("CryptoManager", "generateQVSDCandMSDVV  QVSDC Input error");
            return null;
        }
        byteArrayOutputStream.write((byte) bArr2.length);
        byteArrayOutputStream.write(bArr2, 0, bArr2.length);
        if (bArr3 == null || bArr3.length <= 0) {
            byteArrayOutputStream.write(0);
        } else {
            Log.m1300d("CryptoManager", "MSDVVInput ");
            byteArrayOutputStream.write((byte) bArr3.length);
            byteArrayOutputStream.write(bArr3, 0, bArr3.length);
        }
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        Log.m1300d("CryptoManager", "generateQVSDCandMSDVV() transactionData: " + Utils.m1285o(toByteArray));
        toByteArray = Dm.m1288a(bArr, VISA_CRYPTO_ALG.VISA_ALG_NFC_QVSDC_MSD_CRYPTOGRAM, toByteArray);
        if (toByteArray != null) {
            return toByteArray;
        }
        Log.m1301e("CryptoManager", "generateQVSDCandMSDVV  failed");
        return null;
    }
}
