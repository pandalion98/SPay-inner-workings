/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Base64
 *  com.android.org.conscrypt.OpenSSLKey
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.NoSuchAlgorithmException
 *  java.security.PrivateKey
 *  java.security.cert.CertificateFactory
 *  java.security.cert.X509Certificate
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.cncc;

import android.content.Context;
import android.util.Base64;
import com.android.org.conscrypt.OpenSSLKey;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.cncc.CNCCTAController;
import com.samsung.android.spayfw.cncc.SSLSetupInfo;
import com.samsung.android.spayfw.cncc.Utils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SpaySSLAdapter {
    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";
    private static final String TAG = "SpaySSLAdapter";
    private static SpaySSLAdapter gInstance = null;
    CNCCTAController.DevicePublicCerts cnccPublicCerts = null;
    private CNCCTAController mCNCCTAController = null;
    private X509Certificate[] mCertChain = null;
    private PrivateKey mPrivateKey = null;
    private SSLSetupInfo mRSAPublicKeyInfo = null;

    static {
        System.loadLibrary((String)"sslconnector");
    }

    private SpaySSLAdapter(Context context) {
        this.mCNCCTAController = CNCCTAController.createOnlyInstance(context);
        this.cnccPublicCerts = this.mCNCCTAController.getDeviceCertificates();
    }

    private SSLSetupInfo createSSLSetupInfo() {
        try {
            CertificateFactory.getInstance((String)"X.509");
            SSLSetupInfo sSLSetupInfo = new SSLSetupInfo(Base64.decode((String)this.cnccPublicCerts.deviceSigningCertificate.replaceAll(BEGIN_CERT, "").replaceAll(END_CERT, ""), (int)0));
            return sSLSetupInfo;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static SpaySSLAdapter getInstance(Context context) {
        Class<SpaySSLAdapter> class_ = SpaySSLAdapter.class;
        synchronized (SpaySSLAdapter.class) {
            try {
                if (gInstance != null) return gInstance;
                gInstance = new SpaySSLAdapter(context);
                return gInstance;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }

    private SSLSetupInfo getRSAPublicKeyInfo() {
        if (this.mRSAPublicKeyInfo == null) {
            this.mRSAPublicKeyInfo = this.createSSLSetupInfo();
        }
        return this.mRSAPublicKeyInfo;
    }

    public static boolean isSupported(Context context) {
        return CNCCTAController.isSupported(context);
    }

    private native void resetSSLConnector();

    private native long setupSSLConnector(SSLSetupInfo var1);

    private byte[] signJavaCallback(byte[] arrby) {
        byte[] arrby2;
        block4 : {
            c.i("SpaySSLAdapter", "signJavaCallback()");
            c.i("SpaySSLAdapter", "Data to be Signed " + Utils.encodeHex(arrby));
            try {
                arrby2 = this.mCNCCTAController.processData(null, arrby, CNCCTAController.DataType.DATATYPE_RAW_DATA, CNCCTAController.ProcessingOption.OPTION_RAW_SIGN, null, null);
                if (arrby2 == null) break block4;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
            c.d("SpaySSLAdapter", "signJavaCallback() result len " + arrby2.length);
            c.i("SpaySSLAdapter", "Signature" + Utils.encodeHex(arrby2));
            return arrby2;
        }
        c.e("SpaySSLAdapter", "signJavaCallback() result nulll ");
        return arrby2;
    }

    protected void finalize() {
        SpaySSLAdapter spaySSLAdapter = this;
        synchronized (spaySSLAdapter) {
            this.resetSSLConnector();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public PrivateKey getPrivateKey() {
        SpaySSLAdapter spaySSLAdapter = this;
        synchronized (spaySSLAdapter) {
            if (this.mPrivateKey != null) {
                return this.mPrivateKey;
            }
            long l2 = this.setupSSLConnector(this.getRSAPublicKeyInfo());
            if (l2 == 0L) {
                c.e("SpaySSLAdapter", "Error: setupSSLConnector failed - returned EVP_KEY is 0");
                return null;
            }
            OpenSSLKey openSSLKey = new OpenSSLKey(l2);
            try {
                this.mPrivateKey = openSSLKey.getPrivateKey();
                return null;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
                return null;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public X509Certificate[] getX509CertificateChain() {
        SpaySSLAdapter spaySSLAdapter = this;
        synchronized (spaySSLAdapter) {
            block9 : {
                if (this.mCertChain == null) break block9;
                return this.mCertChain;
            }
            CertificateFactory certificateFactory = CertificateFactory.getInstance((String)"X.509");
            String string = this.cnccPublicCerts.deviceSigningCertificate.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "");
            String string2 = this.cnccPublicCerts.deviceCertificate.replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "");
            byte[] arrby = Base64.decode((String)string, (int)0);
            byte[] arrby2 = Base64.decode((String)string2, (int)0);
            byte[] arrby3 = new byte[arrby.length + arrby2.length];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby3, (int)0, (int)arrby.length);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)arrby.length, (int)arrby2.length);
            Collection collection = certificateFactory.generateCertificates((InputStream)new ByteArrayInputStream(arrby3));
            this.mCertChain = new X509Certificate[collection.size()];
            Iterator iterator = collection.iterator();
            int n2 = 0;
            do {
                if (!iterator.hasNext()) break;
                this.mCertChain[n2] = (X509Certificate)iterator.next();
                c.d("SpaySSLAdapter", "certificate count = " + (n2 + 1));
                ++n2;
            } while (true);
            try {
                c.d("SpaySSLAdapter", "X509 Chain length: " + collection.size());
                return this.mCertChain;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }
}

