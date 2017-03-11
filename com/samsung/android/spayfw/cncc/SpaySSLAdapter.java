package com.samsung.android.spayfw.cncc;

import android.content.Context;
import android.util.Base64;
import com.android.org.conscrypt.OpenSSLKey;
import com.samsung.android.spayfw.cncc.CNCCTAController.DataType;
import com.samsung.android.spayfw.cncc.CNCCTAController.DevicePublicCerts;
import com.samsung.android.spayfw.cncc.CNCCTAController.ProcessingOption;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;

public class SpaySSLAdapter {
    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";
    private static final String TAG = "SpaySSLAdapter";
    private static SpaySSLAdapter gInstance;
    DevicePublicCerts cnccPublicCerts;
    private CNCCTAController mCNCCTAController;
    private X509Certificate[] mCertChain;
    private PrivateKey mPrivateKey;
    private SSLSetupInfo mRSAPublicKeyInfo;

    private native void resetSSLConnector();

    private native long setupSSLConnector(SSLSetupInfo sSLSetupInfo);

    static {
        gInstance = null;
        System.loadLibrary("sslconnector");
    }

    public static synchronized SpaySSLAdapter getInstance(Context context) {
        SpaySSLAdapter spaySSLAdapter;
        synchronized (SpaySSLAdapter.class) {
            try {
                if (gInstance == null) {
                    gInstance = new SpaySSLAdapter(context);
                }
                spaySSLAdapter = gInstance;
            } catch (Exception e) {
                e.printStackTrace();
                spaySSLAdapter = null;
            }
        }
        return spaySSLAdapter;
    }

    private SpaySSLAdapter(Context context) {
        this.mCNCCTAController = null;
        this.mRSAPublicKeyInfo = null;
        this.mPrivateKey = null;
        this.mCertChain = null;
        this.cnccPublicCerts = null;
        this.mCNCCTAController = CNCCTAController.createOnlyInstance(context);
        this.cnccPublicCerts = this.mCNCCTAController.getDeviceCertificates();
    }

    protected synchronized void finalize() {
        resetSSLConnector();
    }

    public synchronized PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        synchronized (this) {
            if (this.mPrivateKey != null) {
                privateKey = this.mPrivateKey;
            } else {
                long j = setupSSLConnector(getRSAPublicKeyInfo());
                if (j == 0) {
                    Log.m286e(TAG, "Error: setupSSLConnector failed - returned EVP_KEY is 0");
                } else {
                    try {
                        this.mPrivateKey = new OpenSSLKey(j).getPrivateKey();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return privateKey;
    }

    private byte[] signJavaCallback(byte[] bArr) {
        Log.m287i(TAG, "signJavaCallback()");
        Log.m287i(TAG, "Data to be Signed " + Utils.encodeHex(bArr));
        try {
            byte[] processData = this.mCNCCTAController.processData(null, bArr, DataType.DATATYPE_RAW_DATA, ProcessingOption.OPTION_RAW_SIGN, null, null);
            if (processData != null) {
                Log.m285d(TAG, "signJavaCallback() result len " + processData.length);
                Log.m287i(TAG, "Signature" + Utils.encodeHex(processData));
                return processData;
            }
            Log.m286e(TAG, "signJavaCallback() result nulll ");
            return processData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized X509Certificate[] getX509CertificateChain() {
        X509Certificate[] x509CertificateArr;
        if (this.mCertChain != null) {
            x509CertificateArr = this.mCertChain;
        } else {
            try {
                CertificateFactory instance = CertificateFactory.getInstance("X.509");
                String replaceAll = this.cnccPublicCerts.deviceSigningCertificate.replaceAll(BEGIN_CERT, BuildConfig.FLAVOR).replaceAll(END_CERT, BuildConfig.FLAVOR);
                String replaceAll2 = this.cnccPublicCerts.deviceCertificate.replaceAll(BEGIN_CERT, BuildConfig.FLAVOR).replaceAll(END_CERT, BuildConfig.FLAVOR);
                Object decode = Base64.decode(replaceAll, 0);
                Object decode2 = Base64.decode(replaceAll2, 0);
                Object obj = new byte[(decode.length + decode2.length)];
                System.arraycopy(decode, 0, obj, 0, decode.length);
                System.arraycopy(decode2, 0, obj, decode.length, decode2.length);
                Collection<X509Certificate> generateCertificates = instance.generateCertificates(new ByteArrayInputStream(obj));
                this.mCertChain = new X509Certificate[generateCertificates.size()];
                int i = 0;
                for (X509Certificate x509Certificate : generateCertificates) {
                    this.mCertChain[i] = x509Certificate;
                    Log.m285d(TAG, "certificate count = " + (i + 1));
                    i++;
                }
                Log.m285d(TAG, "X509 Chain length: " + generateCertificates.size());
                x509CertificateArr = this.mCertChain;
            } catch (Exception e) {
                e.printStackTrace();
                x509CertificateArr = null;
            }
        }
        return x509CertificateArr;
    }

    public static boolean isSupported(Context context) {
        return CNCCTAController.isSupported(context);
    }

    private SSLSetupInfo getRSAPublicKeyInfo() {
        if (this.mRSAPublicKeyInfo == null) {
            this.mRSAPublicKeyInfo = createSSLSetupInfo();
        }
        return this.mRSAPublicKeyInfo;
    }

    private SSLSetupInfo createSSLSetupInfo() {
        try {
            CertificateFactory.getInstance("X.509");
            return new SSLSetupInfo(Base64.decode(this.cnccPublicCerts.deviceSigningCertificate.replaceAll(BEGIN_CERT, BuildConfig.FLAVOR).replaceAll(END_CERT, BuildConfig.FLAVOR), 0));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
