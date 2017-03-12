package android.content.pm;

import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Xml;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class AASATokenParser {
    private String mArchiveSourcePath = ProxyInfo.LOCAL_EXCL_LIST;
    private String mCACertName = "/data/system/.aasa/AASApolicy/ASKS_INTER_1.crt";
    private String mCAKeyIndex = ProxyInfo.LOCAL_EXCL_LIST;
    private String mCertName = ProxyInfo.LOCAL_EXCL_LIST;
    private String mCertPath = ProxyInfo.LOCAL_EXCL_LIST;
    private boolean mIsCalledBySKA = false;
    private boolean mIsSigned512 = true;
    private String mRootCertName = "/system/etc/ASKS_ROOT_1.crt";
    private String mRootKeyIndex = ProxyInfo.LOCAL_EXCL_LIST;
    private byte[] mTokenContents = null;
    private String mTokenName = ProxyInfo.LOCAL_EXCL_LIST;
    private ArrayList<String> mTokenValue = null;
    private boolean misAppSystem = false;
    private boolean misDeviceMode = false;

    public void setCertPath(boolean isDeviceMode) {
        this.misDeviceMode = isDeviceMode;
        if (this.misDeviceMode) {
            this.mCertPath = "/system/etc/aasa_real_crt2.crt";
        } else {
            this.mCertPath = "/data/aasa_test_crt2.crt";
        }
    }

    public void setSystemApp(boolean isAppSystem) {
        this.misAppSystem = isAppSystem;
    }

    public ArrayList<String> getValue() {
        return this.mTokenValue;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String SHA256_FOR_PACKAGE(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        text = text + "AASAASKS";
        md.update(text.getBytes("ISO-8859-1"), 0, text.length());
        return convertToHex(md.digest());
    }

    public static String SHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes("ISO-8859-1"), 0, text.length());
        return convertToHex(md.digest());
    }

    public static String SHA(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("ISO-8859-1"), 0, text.length());
        return convertToHex(md.digest());
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 15;
            int two_halfs = 0;
            while (true) {
                char c = (halfbyte < 0 || halfbyte > 9) ? (char) ((halfbyte - 10) + 97) : (char) (halfbyte + 48);
                buf.append(c);
                halfbyte = b & 15;
                int two_halfs2 = two_halfs + 1;
                if (two_halfs >= 1) {
                    break;
                }
                two_halfs = two_halfs2;
            }
        }
        return buf.toString();
    }

    public static String byteListToString(List<Byte> l) throws UnsupportedEncodingException {
        if (l == null) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream(l.size());
        for (Byte current : l) {
            bout.write(current.byteValue());
        }
        return bout.toString("ISO-8859-1");
    }

    private static char[] toChars(byte[] mSignature) {
        byte[] sig = mSignature;
        int N = sig.length;
        char[] text = new char[(N * 2)];
        for (int j = 0; j < N; j++) {
            int i;
            byte v = sig[j];
            int d = (v >> 4) & 15;
            text[j * 2] = (char) (d >= 10 ? (d + 97) - 10 : d + 48);
            d = v & 15;
            int i2 = (j * 2) + 1;
            if (d >= 10) {
                i = (d + 97) - 10;
            } else {
                i = d + 48;
            }
            text[i2] = (char) i;
        }
        return text;
    }

    private static void StringToByteArray(String string, List<Byte> readBuffer) {
        for (byte valueOf : string.getBytes()) {
            readBuffer.add(Byte.valueOf(valueOf));
        }
    }

    private Certificate[] loadCertificates(JarFile jarFile, JarEntry je, MessageDigest md) {
        Certificate[] certificateArr = null;
        InputStream is = null;
        if (je != null) {
            try {
                is = jarFile.getInputStream(je);
                byte[] localBuf = new byte[4096];
                if (is != null) {
                    while (true) {
                        int size = is.read(localBuf, 0, 4096);
                        if (size == -1) {
                            break;
                        }
                        md.update(localBuf, 0, size);
                    }
                    is.close();
                }
                if (je != null) {
                    certificateArr = je.getCertificates();
                }
            } catch (IOException e) {
                System.err.println("TinyAASA + No IO " + e.toString());
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (RuntimeException e3) {
                System.err.println("TinyAASA + No RUN " + e3.toString());
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                    }
                }
            }
        }
        return certificateArr;
    }

    private boolean checkExistTAG(XmlPullParser parser, String target) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != 1) {
            String name;
            switch (eventType) {
                case 2:
                    name = parser.getName();
                    if (name != null && name.equals(target)) {
                        return true;
                    }
                case 3:
                    name = parser.getName();
                    if (name != null && name.equals(target)) {
                        break;
                    }
                default:
                    break;
            }
            eventType = parser.next();
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.security.cert.Certificate[] loadCertificates(java.util.jar.JarFile r8, java.util.jar.JarEntry r9, java.util.List<java.lang.Byte> r10) {
        /*
        r2 = 0;
        if (r9 == 0) goto L_0x0051;
    L_0x0003:
        r2 = r8.getInputStream(r9);	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
        r4 = 0;
        r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r3 = new byte[r5];	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
        if (r2 == 0) goto L_0x002a;
    L_0x000e:
        r5 = 0;
        r6 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r4 = r2.read(r3, r5, r6);	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
        r5 = -1;
        if (r4 == r5) goto L_0x0027;
    L_0x0018:
        r1 = 0;
    L_0x0019:
        if (r1 >= r4) goto L_0x000e;
    L_0x001b:
        r5 = r3[r1];	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
        r5 = java.lang.Byte.valueOf(r5);	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
        r10.add(r5);	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
        r1 = r1 + 1;
        goto L_0x0019;
    L_0x0027:
        r2.close();	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
    L_0x002a:
        r5 = r9.getCertificates();	 Catch:{ IOException -> 0x002f, RuntimeException -> 0x0053 }
    L_0x002e:
        return r5;
    L_0x002f:
        r0 = move-exception;
        r5 = "AASATokenParser";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "No IO ";
        r6 = r6.append(r7);
        r7 = r0.toString();
        r6 = r6.append(r7);
        r6 = r6.toString();
        android.util.Log.i(r5, r6);
        if (r2 == 0) goto L_0x0051;
    L_0x004e:
        r2.close();	 Catch:{ IOException -> 0x0078 }
    L_0x0051:
        r5 = 0;
        goto L_0x002e;
    L_0x0053:
        r0 = move-exception;
        r5 = "AASATokenParser";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "No RUN ";
        r6 = r6.append(r7);
        r7 = r0.toString();
        r6 = r6.append(r7);
        r6 = r6.toString();
        android.util.Log.i(r5, r6);
        if (r2 == 0) goto L_0x0051;
    L_0x0072:
        r2.close();	 Catch:{ IOException -> 0x0076 }
        goto L_0x0051;
    L_0x0076:
        r5 = move-exception;
        goto L_0x0051;
    L_0x0078:
        r5 = move-exception;
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.AASATokenParser.loadCertificates(java.util.jar.JarFile, java.util.jar.JarEntry, java.util.List):java.security.cert.Certificate[]");
    }

    private Certificate[] loadCertificates(JarFile jarFile, JarEntry je, byte[] readBuffer) {
        InputStream is = null;
        if (je != null) {
            try {
                is = jarFile.getInputStream(je);
                if (is != null) {
                    do {
                    } while (is.read(readBuffer, 0, readBuffer.length) != -1);
                    is.close();
                }
                return je.getCertificates();
            } catch (IOException e) {
                Log.i("AASATokenParser", "No IO " + e.toString());
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (IOException e3) {
                Log.i("AASATokenParser", "No RUN " + e3.toString());
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e4) {
                    }
                }
            }
        }
        return null;
    }

    private boolean parseXML(XmlPullParser parser, String target, ArrayList<String> arrayValue) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        int depth = 0;
        while (eventType != 1) {
            String name;
            switch (eventType) {
                case 2:
                    name = parser.getName();
                    if (name != null && name.equals(target)) {
                        depth = parser.getAttributeCount();
                        for (int a = 0; a < depth; a++) {
                            arrayValue.add(parser.getAttributeValue(a));
                        }
                        break;
                    }
                case 3:
                    name = parser.getName();
                    if (name != null && name.equals(target)) {
                        break;
                    }
                default:
                    break;
            }
            eventType = parser.next();
        }
        if (depth == 0) {
            return false;
        }
        return true;
    }

    byte[] checkIntegrity(byte[] contents) {
        boolean isSKA = false;
        int keySize = 512;
        if (!this.mCertName.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            isSKA = true;
        }
        if (!(isSKA || this.mIsSigned512)) {
            Log.i("AASATokenParser", "AASA Token change key 256");
            keySize = 256;
        }
        if (contents.length < keySize) {
            return null;
        }
        try {
            byte[] signData = new byte[keySize];
            System.arraycopy(contents, 0, signData, 0, keySize);
            byte[] tempBuf = new byte[7];
            int numberIndex = 0;
            boolean isError = false;
            int ii = keySize;
            while (contents[ii] != (byte) 44) {
                if (numberIndex >= 7) {
                    isError = true;
                    break;
                }
                tempBuf[numberIndex] = contents[ii];
                ii++;
                numberIndex++;
            }
            if (!isError) {
                byte[] number = new byte[numberIndex];
                System.arraycopy(tempBuf, 0, number, 0, numberIndex);
                int sizeOfcontents = Integer.parseInt(new String(number));
                this.mTokenContents = new byte[sizeOfcontents];
                System.arraycopy(contents, (keySize + numberIndex) + ",".length(), this.mTokenContents, 0, sizeOfcontents);
                Signature signature = Signature.getInstance("SHA256WithRSAEncryption");
                CertificateFactory certfactory = CertificateFactory.getInstance("x.509");
                InputStream certificate = null;
                X509Certificate x509Certificate = null;
                if (isSKA) {
                    InputStream fis = null;
                    JarFile jarFile = new JarFile(this.mArchiveSourcePath);
                    if (jarFile != null) {
                        JarEntry certEntry = jarFile.getJarEntry(this.mCertName);
                        if (certEntry != null) {
                            fis = jarFile.getInputStream(certEntry);
                            x509Certificate = (X509Certificate) certfactory.generateCertificate(fis);
                        } else {
                            Log.i("AASATokenParser", "Token Cert does not exist!");
                            if (jarFile != null) {
                                jarFile.close();
                            }
                            return null;
                        }
                    }
                    if (jarFile != null) {
                        jarFile.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    try {
                        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                        parser.setInput(new ByteArrayInputStream(this.mTokenContents), null);
                        ArrayList<String> verifyList = new ArrayList();
                        try {
                            if (parseXML(parser, "INDEX", verifyList)) {
                                XmlPullParserFactory.newInstance().newPullParser().setInput(new ByteArrayInputStream(this.mTokenContents), null);
                            }
                            String index = (String) verifyList.get(0);
                            Log.i("AASATokenParser", "index : " + index);
                            if ("0.0".equals(index)) {
                                Log.d("AASATokenParser", "ENG Cert Index");
                            } else {
                                String[] keyIndex = index.split("\\.");
                                String SignerVersion = this.mTokenName.replaceAll("[^0-9]", ProxyInfo.LOCAL_EXCL_LIST);
                                Log.d("AASATokenParser", "mTokenName : " + this.mTokenName + " SignerVersion : " + SignerVersion);
                                if (ProxyInfo.LOCAL_EXCL_LIST.equals(SignerVersion)) {
                                    SignerVersion = WifiEnterpriseConfig.ENGINE_ENABLE;
                                }
                                if (!SignerVersion.equals(keyIndex[1])) {
                                    Log.d("AASATokenParser", "Signer Cert File is not matched with index!");
                                    return null;
                                } else if (SKA_CheckList("SIGNER", keyIndex[1])) {
                                    Log.d("AASATokenParser", "SIGNER is in CRL");
                                    return null;
                                } else if (SKA_CheckList("INTER", keyIndex[0])) {
                                    Log.d("AASATokenParser", "INTER is in CRL");
                                    return null;
                                }
                            }
                        } catch (IOException e) {
                            Log.i("AASATokenParser", " " + e.toString());
                            return null;
                        }
                    } catch (XmlPullParserException e2) {
                        Log.i("AASATokenParser", " " + e2.toString());
                        return null;
                    }
                }
                if (!this.mIsSigned512) {
                    if (this.misDeviceMode) {
                        this.mCertPath = "/system/etc/aasa_real_crt.crt";
                    } else {
                        this.mCertPath = "/data/aasa_test_crt.crt";
                    }
                    this.mIsSigned512 = true;
                } else if (this.misDeviceMode) {
                    this.mCertPath = "/system/etc/aasa_real_crt2.crt";
                } else {
                    this.mCertPath = "/data/aasa_test_crt2.crt";
                }
                certificate = new FileInputStream(this.mCertPath);
                x509Certificate = (X509Certificate) certfactory.generateCertificate(certificate);
                signature.initVerify(x509Certificate);
                signature.update(this.mTokenContents, 0, sizeOfcontents);
                if (signature.verify(signData)) {
                    Log.i("AASATokenParser", "Token is verificated in checkIntegrity!");
                    if (isSKA) {
                        certificate = new FileInputStream(this.mCACertName);
                        X509Certificate CAcert = (X509Certificate) certfactory.generateCertificate(certificate);
                        try {
                            x509Certificate.verify(CAcert.getPublicKey());
                            Log.i("AASATokenParser", "signerCert is verificated!");
                            certificate = new FileInputStream(this.mRootCertName);
                            X509Certificate rootCert = (X509Certificate) certfactory.generateCertificate(certificate);
                            try {
                                CAcert.verify(rootCert.getPublicKey());
                                Log.i("AASATokenParser", "CAcert is verificated!");
                                try {
                                    rootCert.verify(rootCert.getPublicKey());
                                    if (certificate != null) {
                                        certificate.close();
                                    }
                                    Log.i("AASATokenParser", "rootCert is verificated!");
                                    return this.mTokenContents;
                                } catch (Exception e3) {
                                    Log.i("AASATokenParser", "ERROR: rootCert is not verified " + e3);
                                    if (certificate != null) {
                                        certificate.close();
                                    }
                                    return null;
                                }
                            } catch (Exception e32) {
                                Log.i("AASATokenParser", "ERROR: CACert is not verified by RootCert " + e32);
                                if (certificate != null) {
                                    certificate.close();
                                }
                                return null;
                            }
                        } catch (Exception e322) {
                            Log.i("AASATokenParser", "ERROR: SignerCert is not verified by CACert " + e322);
                            if (certificate != null) {
                                certificate.close();
                            }
                            return null;
                        }
                    }
                    if (certificate != null) {
                        certificate.close();
                    }
                    return this.mTokenContents;
                }
                Log.e("AASATokenParser", "Token is NOT verificated in checkIntegrity!");
                if (certificate != null) {
                    certificate.close();
                }
                return null;
            } else if (isSKA || !this.mIsSigned512) {
                if (!this.mIsSigned512) {
                    this.mIsSigned512 = true;
                }
                return null;
            } else {
                Log.i("AASATokenParser", "AASA Token might be signed 256");
                this.mIsSigned512 = false;
                return checkIntegrity(contents);
            }
        } catch (Exception e3222) {
            Log.e("AASATokenParser", "ERROR: checkIntegrity " + e3222);
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String AASA_getPackageInfo(java.lang.String r29) {
        /*
        r28 = this;
        r17 = 0;
        r4 = 0;
        r16 = 0;
        r13 = 1048833; // 0x100101 float:1.469728E-39 double:5.181924E-318;
        r20 = 1048834; // 0x100102 float:1.46973E-39 double:5.18193E-318;
        r14 = 1048835; // 0x100103 float:1.469731E-39 double:5.181933E-318;
        r21 = 36;
        r22 = 0;
        r25 = 0;
        r15 = 0;
        r19 = 0;
        r5 = new java.util.jar.JarFile;	 Catch:{ Exception -> 0x0147 }
        r0 = r29;
        r5.<init>(r0);	 Catch:{ Exception -> 0x0147 }
        r26 = "AndroidManifest.xml";
        r0 = r26;
        r26 = r5.getJarEntry(r0);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r0 = r26;
        r16 = r5.getInputStream(r0);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = r16.available();	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r0 = r26;
        r0 = new byte[r0];	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r24 = r0;
        r0 = r16;
        r1 = r24;
        r0.read(r1);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = 16;
        r0 = r28;
        r1 = r24;
        r2 = r26;
        r26 = r0.LEW(r1, r2);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = r26 * 4;
        r22 = r21 + r26;
        r26 = 12;
        r0 = r28;
        r1 = r24;
        r2 = r26;
        r25 = r0.LEW(r1, r2);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r15 = r25;
    L_0x005b:
        r0 = r24;
        r0 = r0.length;	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = r0;
        r26 = r26 + -4;
        r0 = r26;
        if (r15 >= r0) goto L_0x0079;
    L_0x0066:
        r0 = r28;
        r1 = r24;
        r26 = r0.LEW(r1, r15);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r27 = 1048834; // 0x100102 float:1.46973E-39 double:5.18193E-318;
        r0 = r26;
        r1 = r27;
        if (r0 != r1) goto L_0x0103;
    L_0x0077:
        r25 = r15;
    L_0x0079:
        r19 = r25;
    L_0x007b:
        r0 = r24;
        r0 = r0.length;	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = r0;
        r0 = r19;
        r1 = r26;
        if (r0 >= r1) goto L_0x0129;
    L_0x0086:
        r0 = r28;
        r1 = r24;
        r2 = r19;
        r23 = r0.LEW(r1, r2);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = 1048834; // 0x100102 float:1.46973E-39 double:5.18193E-318;
        r0 = r23;
        r1 = r26;
        if (r0 != r1) goto L_0x0113;
    L_0x0099:
        r26 = r19 + 28;
        r0 = r28;
        r1 = r24;
        r2 = r26;
        r18 = r0.LEW(r1, r2);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r19 = r19 + 36;
        r15 = 0;
    L_0x00a8:
        r0 = r18;
        if (r15 >= r0) goto L_0x007b;
    L_0x00ac:
        r26 = r19 + 4;
        r0 = r28;
        r1 = r24;
        r2 = r26;
        r7 = r0.LEW(r1, r2);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = r19 + 8;
        r0 = r28;
        r1 = r24;
        r2 = r26;
        r10 = r0.LEW(r1, r2);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = r19 + 16;
        r0 = r28;
        r1 = r24;
        r2 = r26;
        r8 = r0.LEW(r1, r2);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r6 = 0;
        r0 = r28;
        r1 = r24;
        r2 = r21;
        r3 = r22;
        r6 = r0.compXmlString(r1, r2, r3, r7);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r26 = "versionCode";
        r0 = r26;
        r26 = r0.equalsIgnoreCase(r6);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        if (r26 == 0) goto L_0x00fe;
    L_0x00e8:
        r26 = -1;
        r0 = r26;
        if (r10 == r0) goto L_0x0107;
    L_0x00ee:
        r0 = r28;
        r1 = r24;
        r2 = r21;
        r3 = r22;
        r9 = r0.compXmlString(r1, r2, r3, r10);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
    L_0x00fa:
        r17 = r9.trim();	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
    L_0x00fe:
        r19 = r19 + 20;
        r15 = r15 + 1;
        goto L_0x00a8;
    L_0x0103:
        r15 = r15 + 4;
        goto L_0x005b;
    L_0x0107:
        r26 = new java.lang.Integer;	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r0 = r26;
        r0.<init>(r8);	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        r9 = r26.toString();	 Catch:{ Exception -> 0x0175, all -> 0x0172 }
        goto L_0x00fa;
    L_0x0113:
        r26 = 1048835; // 0x100103 float:1.469731E-39 double:5.181933E-318;
        r0 = r23;
        r1 = r26;
        if (r0 != r1) goto L_0x0120;
    L_0x011c:
        r19 = r19 + 24;
        goto L_0x007b;
    L_0x0120:
        r26 = 1048833; // 0x100101 float:1.469728E-39 double:5.181924E-318;
        r0 = r23;
        r1 = r26;
        if (r0 != r1) goto L_0x0129;
    L_0x0129:
        if (r16 == 0) goto L_0x012e;
    L_0x012b:
        r16.close();	 Catch:{ IOException -> 0x0142 }
    L_0x012e:
        if (r5 == 0) goto L_0x0133;
    L_0x0130:
        r5.close();	 Catch:{ IOException -> 0x0142 }
    L_0x0133:
        if (r17 != 0) goto L_0x013e;
    L_0x0135:
        r17 = "MAYBEOTA";
        r26 = java.lang.System.out;
        r27 = "WARNING: Package Name can not be found, Maybe It is OTA";
        r26.println(r27);
    L_0x013e:
        r4 = r5;
        r26 = r17;
    L_0x0141:
        return r26;
    L_0x0142:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0133;
    L_0x0147:
        r11 = move-exception;
    L_0x0148:
        r26 = "AASATokenParser";
        r27 = "Exception on getpackageinfo";
        android.util.Log.e(r26, r27);	 Catch:{ all -> 0x0161 }
        r26 = 0;
        if (r16 == 0) goto L_0x0156;
    L_0x0153:
        r16.close();	 Catch:{ IOException -> 0x015c }
    L_0x0156:
        if (r4 == 0) goto L_0x0141;
    L_0x0158:
        r4.close();	 Catch:{ IOException -> 0x015c }
        goto L_0x0141;
    L_0x015c:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0141;
    L_0x0161:
        r26 = move-exception;
    L_0x0162:
        if (r16 == 0) goto L_0x0167;
    L_0x0164:
        r16.close();	 Catch:{ IOException -> 0x016d }
    L_0x0167:
        if (r4 == 0) goto L_0x016c;
    L_0x0169:
        r4.close();	 Catch:{ IOException -> 0x016d }
    L_0x016c:
        throw r26;
    L_0x016d:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x016c;
    L_0x0172:
        r26 = move-exception;
        r4 = r5;
        goto L_0x0162;
    L_0x0175:
        r11 = move-exception;
        r4 = r5;
        goto L_0x0148;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.AASATokenParser.AASA_getPackageInfo(java.lang.String):java.lang.String");
    }

    private int LEW(byte[] arr, int off) {
        return ((((arr[off + 3] << 24) & -16777216) | ((arr[off + 2] << 16) & 16711680)) | ((arr[off + 1] << 8) & 65280)) | (arr[off] & 255);
    }

    private String compXmlString(byte[] xml, int sitOff, int stOff, int strInd) {
        if (strInd < 0) {
            return null;
        }
        int strOff = stOff + LEW(xml, (strInd * 4) + sitOff);
        int strLen = ((xml[strOff + 1] << 8) & 65280) | (xml[strOff] & 255);
        byte[] chars = new byte[strLen];
        for (int ii = 0; ii < strLen; ii++) {
            chars[ii] = xml[(strOff + 2) + (ii * 2)];
        }
        return new String(chars);
    }

    public boolean AASA_IsSKAToken(String ArchiveSourcePath) throws IOException {
        IOException m;
        Throwable th;
        boolean ret = false;
        JarFile mTokenFile = null;
        try {
            JarFile mTokenFile2 = new JarFile(ArchiveSourcePath);
            if (mTokenFile2 != null) {
                try {
                    Enumeration<JarEntry> entries = mTokenFile2.entries();
                    while (entries.hasMoreElements()) {
                        String name = ((JarEntry) entries.nextElement()).getName();
                        if (!name.startsWith("SEC-INF") || !name.contains("buildinfo")) {
                            if (name.startsWith("META-INF") && name.contains("SEC-INF") && name.contains("buildinfo")) {
                                this.mArchiveSourcePath = ArchiveSourcePath;
                                this.mTokenName = name;
                                this.mCertName = "META-INF" + File.separator + "SEC-INF" + File.separator + "buildConfirm.crt";
                                ret = true;
                                break;
                            }
                        } else {
                            this.mArchiveSourcePath = ArchiveSourcePath;
                            this.mTokenName = name;
                            this.mCertName = "SEC-INF" + File.separator + "buildConfirm.crt";
                            ret = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    m = e;
                    mTokenFile = mTokenFile2;
                } catch (Throwable th2) {
                    th = th2;
                    mTokenFile = mTokenFile2;
                }
            }
            if (mTokenFile2 != null) {
                mTokenFile2.close();
                mTokenFile = mTokenFile2;
            }
        } catch (IOException e2) {
            m = e2;
            try {
                Log.i("AASATokenParser", " ERROR: AASA_SKAIsToken " + m);
                if (mTokenFile != null) {
                    mTokenFile.close();
                }
                if (ret) {
                    this.mIsCalledBySKA = true;
                }
                return ret;
            } catch (Throwable th3) {
                th = th3;
                if (mTokenFile != null) {
                    mTokenFile.close();
                }
                throw th;
            }
        }
        if (ret) {
            this.mIsCalledBySKA = true;
        }
        return ret;
    }

    public String AASA_getSKAInfo(String ArchiveSourcePath) throws IOException {
        String Hash = ProxyInfo.LOCAL_EXCL_LIST;
        this.mArchiveSourcePath = ArchiveSourcePath;
        Hash = advancedCheckHash();
        if (Hash == null || Hash.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
            this.mArchiveSourcePath = ProxyInfo.LOCAL_EXCL_LIST;
            return null;
        }
        this.mArchiveSourcePath = ProxyInfo.LOCAL_EXCL_LIST;
        return Hash;
    }

    public boolean AASA_IsToken(String ArchiveSourcePath) throws IOException {
        IOException m;
        Throwable th;
        boolean ret = false;
        JarFile mTokenFile = null;
        try {
            JarFile mTokenFile2 = new JarFile(ArchiveSourcePath);
            if (mTokenFile2 != null) {
                try {
                    Enumeration<JarEntry> entries = mTokenFile2.entries();
                    while (entries.hasMoreElements()) {
                        String name = ((JarEntry) entries.nextElement()).getName();
                        if (name.startsWith("token") && name.contains("Token.xml")) {
                            this.mCertName = ProxyInfo.LOCAL_EXCL_LIST;
                            this.mArchiveSourcePath = ArchiveSourcePath;
                            this.mTokenName = name;
                            ret = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    m = e;
                    mTokenFile = mTokenFile2;
                    try {
                        Log.i("AASATokenParser", " ERROR: AASA_IsToken " + m);
                        if (mTokenFile != null) {
                            mTokenFile.close();
                        }
                        return ret;
                    } catch (Throwable th2) {
                        th = th2;
                        if (mTokenFile != null) {
                            mTokenFile.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    mTokenFile = mTokenFile2;
                    if (mTokenFile != null) {
                        mTokenFile.close();
                    }
                    throw th;
                }
            }
            if (mTokenFile2 != null) {
                mTokenFile2.close();
                mTokenFile = mTokenFile2;
            }
        } catch (IOException e2) {
            m = e2;
            Log.i("AASATokenParser", " ERROR: AASA_IsToken " + m);
            if (mTokenFile != null) {
                mTokenFile.close();
            }
            return ret;
        }
        return ret;
    }

    private String advancedCheckHash() {
        IOException m;
        Throwable th;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        JarFile jarFile = null;
        try {
            JarFile jarFile2 = new JarFile(this.mArchiveSourcePath);
            if (jarFile2 != null) {
                try {
                    ArrayList<LinkedHashMap<String, String>> array_name_sha1 = new ArrayList();
                    LinkedHashMap<String, String> local_name_list = new LinkedHashMap();
                    Enumeration<JarEntry> entries = jarFile2.entries();
                    int i = 0;
                    while (entries.hasMoreElements()) {
                        i++;
                        JarEntry jarEntry = (JarEntry) entries.nextElement();
                        String name = jarEntry.getName();
                        if (!(name.startsWith("META-INF/") || name.startsWith("SEC-INF/") || name.startsWith("token/"))) {
                            loadCertificates(jarFile2, jarEntry, md);
                            local_name_list.put(name, convertToHex(md.digest()));
                            if (i >= 50000) {
                                i = 0;
                                array_name_sha1.add((LinkedHashMap) local_name_list.clone());
                                local_name_list.clear();
                            }
                        }
                    }
                    if (i != 0) {
                        array_name_sha1.add((LinkedHashMap) local_name_list.clone());
                        local_name_list.clear();
                    }
                    if (jarFile2 != null) {
                        jarFile2.close();
                    }
                    MessageDigest mdsha256 = null;
                    try {
                        mdsha256 = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    for (int ii = 0; ii < array_name_sha1.size(); ii++) {
                        TreeMap<String, String> treeMap = new TreeMap((Map) array_name_sha1.get(ii));
                        for (String key : treeMap.keySet()) {
                            String value = (String) treeMap.get(key);
                            try {
                                mdsha256.update(value.getBytes("ISO-8859-1"), 0, value.length());
                            } catch (UnsupportedEncodingException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                    String HASH = convertToHex(mdsha256.digest());
                    Log.i("AASATokenParser", " advanced hash::" + HASH);
                    if (jarFile2 != null) {
                        try {
                            jarFile2.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    jarFile = jarFile2;
                    return HASH;
                } catch (IOException e4) {
                    m = e4;
                    jarFile = jarFile2;
                } catch (Throwable th2) {
                    th = th2;
                    jarFile = jarFile2;
                }
            } else {
                if (jarFile2 != null) {
                    try {
                        jarFile2.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                        jarFile = jarFile2;
                    }
                }
                jarFile = jarFile2;
                return null;
            }
        } catch (IOException e5) {
            m = e5;
            try {
                Log.i("AASATokenParser", " ERROR: AASA_VerifyToken check hash " + m);
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (IOException e3222) {
                        e3222.printStackTrace();
                    }
                }
                throw th;
            }
        }
    }

    private boolean checkHash(String comp) {
        JarFile jarFile;
        IOException m;
        try {
            JarFile mTokenFile = new JarFile(this.mArchiveSourcePath);
            if (mTokenFile != null) {
                try {
                    List<Byte> value;
                    int iii;
                    LinkedHashMap<String, List<Byte>> name_list = new LinkedHashMap();
                    Enumeration<JarEntry> entries = mTokenFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = (JarEntry) entries.nextElement();
                        String name = jarEntry.getName();
                        List buf = new ArrayList();
                        if (!(name.startsWith("META-INF/") || (name.startsWith("token") && name.contains("Token.xml")))) {
                            loadCertificates(mTokenFile, jarEntry, buf);
                            name_list.put(name, buf);
                        }
                    }
                    if (mTokenFile != null) {
                        mTokenFile.close();
                    }
                    TreeMap<String, List<Byte>> treeMap = new TreeMap(name_list);
                    List<Byte> totalBuf = new ArrayList();
                    for (String key : treeMap.keySet()) {
                        value = (List) treeMap.get(key);
                        for (iii = 0; iii < value.size(); iii++) {
                            totalBuf.add(value.get(iii));
                        }
                    }
                    String HASH = ProxyInfo.LOCAL_EXCL_LIST;
                    try {
                        HASH = SHA256(byteListToString(totalBuf));
                        Log.i("AASATokenParser", " ascending hash::" + HASH + "  comp from token:" + comp);
                        if (comp.equals(HASH)) {
                            jarFile = mTokenFile;
                            return true;
                        }
                        totalBuf.clear();
                        for (String key2 : name_list.keySet()) {
                            value = (List) name_list.get(key2);
                            for (iii = 0; iii < value.size(); iii++) {
                                totalBuf.add(value.get(iii));
                            }
                        }
                        try {
                            HASH = SHA256(byteListToString(totalBuf));
                            Log.i("AASATokenParser", " insertion hash::" + HASH + "  comp from token:" + comp);
                            if (comp.equals(HASH)) {
                                jarFile = mTokenFile;
                                return true;
                            }
                        } catch (NoSuchAlgorithmException e) {
                            Log.i("AASATokenParser", " SHA1" + e.toString());
                            e.printStackTrace();
                            jarFile = mTokenFile;
                            return false;
                        }
                    } catch (NoSuchAlgorithmException e2) {
                        Log.i("AASATokenParser", " SHA1" + e2.toString());
                        e2.printStackTrace();
                        jarFile = mTokenFile;
                        return false;
                    }
                } catch (IOException e3) {
                    m = e3;
                    jarFile = mTokenFile;
                    Log.i("AASATokenParser", " ERROR: AASA_VerifyToken check hash " + m);
                    return false;
                }
            }
            jarFile = mTokenFile;
            return false;
        } catch (IOException e4) {
            m = e4;
            Log.i("AASATokenParser", " ERROR: AASA_VerifyToken check hash " + m);
            return false;
        }
    }

    public byte[] AASA_VerifyFile(byte[] contents) {
        byte[] mj = checkIntegrity(contents);
        return mj != null ? mj : null;
    }

    private boolean AASA_isTargetPackage(String packageName, String deviceID) {
        ArrayList<String> verifyList = new ArrayList();
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
            try {
                if (!parseXML(parser, "PACKAGE", verifyList)) {
                    return false;
                }
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                if (!parseXML(parser, "SERIALNUMBER", verifyList)) {
                    return false;
                }
                XmlPullParserFactory.newInstance().newPullParser().setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                if (verifyList == null || !verifyList.contains(packageName) || !verifyList.contains(deviceID)) {
                    return false;
                }
                Log.i("AASATokenParser", " pass all with devtoken :" + packageName);
                return true;
            } catch (IOException e) {
                Log.i("AASATokenParser", "AASA_verifyDevice er" + e.toString());
                return false;
            }
        } catch (XmlPullParserException e2) {
            Log.i("AASATokenParser", "AASA_verifyDevice err" + e2.toString());
            return false;
        }
    }

    private boolean AASA_verifyDevice(String deviceID) {
        ArrayList<String> verifyList = new ArrayList();
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
            try {
                if (!parseXML(parser, "DEVIEID", verifyList)) {
                    return false;
                }
                XmlPullParserFactory.newInstance().newPullParser().setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                if (verifyList == null || !verifyList.contains(deviceID)) {
                    return false;
                }
                Log.i("AASATokenParser", "AASA_verifyDevice OK with " + deviceID);
                return true;
            } catch (IOException e) {
                Log.i("AASATokenParser", "AASA_verifyDevice er" + e.toString());
                return false;
            }
        } catch (XmlPullParserException e2) {
            Log.i("AASATokenParser", "AASA_verifyDevice err" + e2.toString());
            return false;
        }
    }

    public boolean aasaDevToken(String path, String deviceID, String targetPackage) {
        int count;
        byte[] buffer;
        byte[] contents;
        boolean result = false;
        File file = new File(path);
        if (file != null) {
            if (!file.exists()) {
                return false;
            }
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(file);
                try {
                    count = fis2.available();
                    fis = fis2;
                } catch (FileNotFoundException e) {
                    fis = fis2;
                    count = 0;
                    if (count != 0) {
                        buffer = new byte[count];
                        try {
                            fis.read(buffer);
                            contents = AASA_VerifyFile(buffer);
                            if (contents != null) {
                                this.mTokenContents = (byte[]) contents.clone();
                                Log.i("AASATokenParser", " targetPackage:" + targetPackage);
                                if (AASA_isTargetPackage(targetPackage, deviceID)) {
                                    Log.i("AASATokenParser", " targetPackage:" + targetPackage + " is not DevTargert");
                                } else {
                                    Log.i("AASATokenParser", " targetPackage: ok: " + targetPackage);
                                    result = true;
                                }
                            }
                        } catch (IOException e2) {
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    return result;
                } catch (IOException e4) {
                    fis = fis2;
                    count = 0;
                    if (count != 0) {
                        buffer = new byte[count];
                        fis.read(buffer);
                        contents = AASA_VerifyFile(buffer);
                        if (contents != null) {
                            this.mTokenContents = (byte[]) contents.clone();
                            Log.i("AASATokenParser", " targetPackage:" + targetPackage);
                            if (AASA_isTargetPackage(targetPackage, deviceID)) {
                                Log.i("AASATokenParser", " targetPackage: ok: " + targetPackage);
                                result = true;
                            } else {
                                Log.i("AASATokenParser", " targetPackage:" + targetPackage + " is not DevTargert");
                            }
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    return result;
                }
            } catch (FileNotFoundException e5) {
                count = 0;
                if (count != 0) {
                    buffer = new byte[count];
                    fis.read(buffer);
                    contents = AASA_VerifyFile(buffer);
                    if (contents != null) {
                        this.mTokenContents = (byte[]) contents.clone();
                        Log.i("AASATokenParser", " targetPackage:" + targetPackage);
                        if (AASA_isTargetPackage(targetPackage, deviceID)) {
                            Log.i("AASATokenParser", " targetPackage:" + targetPackage + " is not DevTargert");
                        } else {
                            Log.i("AASATokenParser", " targetPackage: ok: " + targetPackage);
                            result = true;
                        }
                    }
                }
                if (fis != null) {
                    fis.close();
                }
                return result;
            } catch (IOException e6) {
                count = 0;
                if (count != 0) {
                    buffer = new byte[count];
                    fis.read(buffer);
                    contents = AASA_VerifyFile(buffer);
                    if (contents != null) {
                        this.mTokenContents = (byte[]) contents.clone();
                        Log.i("AASATokenParser", " targetPackage:" + targetPackage);
                        if (AASA_isTargetPackage(targetPackage, deviceID)) {
                            Log.i("AASATokenParser", " targetPackage: ok: " + targetPackage);
                            result = true;
                        } else {
                            Log.i("AASATokenParser", " targetPackage:" + targetPackage + " is not DevTargert");
                        }
                    }
                }
                if (fis != null) {
                    fis.close();
                }
                return result;
            }
            if (count != 0) {
                buffer = new byte[count];
                fis.read(buffer);
                contents = AASA_VerifyFile(buffer);
                if (contents != null) {
                    this.mTokenContents = (byte[]) contents.clone();
                    Log.i("AASATokenParser", " targetPackage:" + targetPackage);
                    if (AASA_isTargetPackage(targetPackage, deviceID)) {
                        Log.i("AASATokenParser", " targetPackage: ok: " + targetPackage);
                        result = true;
                    } else {
                        Log.i("AASATokenParser", " targetPackage:" + targetPackage + " is not DevTargert");
                    }
                }
            }
            if (fis != null) {
                fis.close();
            }
        }
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean AASA_VerifyToken(java.lang.String r37, java.lang.String r38) throws java.io.IOException {
        /*
        r36 = this;
        r33 = "AASATokenParser";
        r34 = " AASA_VerifyToken START";
        android.util.Log.i(r33, r34);
        r0 = r36;
        r0 = r0.mIsCalledBySKA;
        r17 = r0;
        r33 = 0;
        r0 = r33;
        r1 = r36;
        r1.mIsCalledBySKA = r0;
        r19 = 0;
        r18 = 0;
        r4 = 0;
        r20 = new java.util.jar.JarFile;	 Catch:{ IOException -> 0x007f }
        r0 = r36;
        r0 = r0.mArchiveSourcePath;	 Catch:{ IOException -> 0x007f }
        r33 = r0;
        r0 = r20;
        r1 = r33;
        r0.<init>(r1);	 Catch:{ IOException -> 0x007f }
        if (r20 == 0) goto L_0x0064;
    L_0x002b:
        r0 = r36;
        r0 = r0.mTokenName;	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
        r33 = r0;
        r0 = r20;
        r1 = r33;
        r18 = r0.getJarEntry(r1);	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
        if (r18 == 0) goto L_0x0064;
    L_0x003b:
        r34 = r18.getSize();	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
        r0 = r34;
        r0 = (int) r0;	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
        r33 = r0;
        r0 = r33;
        r0 = new byte[r0];	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
        r27 = r0;
        r0 = r36;
        r1 = r20;
        r2 = r18;
        r3 = r27;
        r4 = r0.loadCertificates(r1, r2, r3);	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
        r0 = r36;
        r1 = r27;
        r33 = r0.checkIntegrity(r1);	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
        r0 = r33;
        r1 = r36;
        r1.mTokenContents = r0;	 Catch:{ IOException -> 0x0998, all -> 0x0993 }
    L_0x0064:
        if (r20 == 0) goto L_0x099d;
    L_0x0066:
        r20.close();
        r19 = r20;
    L_0x006b:
        r0 = r36;
        r0 = r0.mTokenContents;
        r33 = r0;
        if (r33 == 0) goto L_0x0075;
    L_0x0073:
        if (r4 != 0) goto L_0x00a9;
    L_0x0075:
        r33 = "AASATokenParser";
        r34 = " ERROR: plz check certification in the device";
        android.util.Log.i(r33, r34);
        r33 = 0;
    L_0x007e:
        return r33;
    L_0x007f:
        r16 = move-exception;
    L_0x0080:
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00a2 }
        r34.<init>();	 Catch:{ all -> 0x00a2 }
        r35 = " ERROR: AASA_VerifyToken ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00a2 }
        r0 = r34;
        r1 = r16;
        r34 = r0.append(r1);	 Catch:{ all -> 0x00a2 }
        r34 = r34.toString();	 Catch:{ all -> 0x00a2 }
        android.util.Log.i(r33, r34);	 Catch:{ all -> 0x00a2 }
        if (r19 == 0) goto L_0x006b;
    L_0x009e:
        r19.close();
        goto L_0x006b;
    L_0x00a2:
        r33 = move-exception;
    L_0x00a3:
        if (r19 == 0) goto L_0x00a8;
    L_0x00a5:
        r19.close();
    L_0x00a8:
        throw r33;
    L_0x00a9:
        r19 = 0;
        r18 = 0;
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ XmlPullParserException -> 0x0268 }
        r25 = r26.newPullParser();	 Catch:{ XmlPullParserException -> 0x0268 }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ XmlPullParserException -> 0x0268 }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ XmlPullParserException -> 0x0268 }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ XmlPullParserException -> 0x0268 }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ XmlPullParserException -> 0x0268 }
        r31 = new java.util.ArrayList;	 Catch:{ XmlPullParserException -> 0x0268 }
        r31.<init>();	 Catch:{ XmlPullParserException -> 0x0268 }
        r33 = "MODE";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r3 = r31;
        r33 = r0.parseXML(r1, r2, r3);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0114;
    L_0x00e0:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        r0 = r31;
        r1 = r33;
        r21 = r0.get(r1);	 Catch:{ IOException -> 0x01eb }
        r21 = (java.lang.String) r21;	 Catch:{ IOException -> 0x01eb }
        r33 = isInteger(r21);	 Catch:{ IOException -> 0x01eb }
        if (r33 != 0) goto L_0x0118;
    L_0x0110:
        r33 = 0;
        goto L_0x007e;
    L_0x0114:
        r33 = 0;
        goto L_0x007e;
    L_0x0118:
        r32 = java.lang.Integer.parseInt(r21);	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.misDeviceMode;	 Catch:{ IOException -> 0x01eb }
        r33 = r0;
        r34 = 1;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x013d;
    L_0x012a:
        r33 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r0 = r32;
        r1 = r33;
        if (r0 != r1) goto L_0x013d;
    L_0x0132:
        r33 = "AASATokenParser";
        r34 = " Ship mode device does not support MODE 255";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        goto L_0x007e;
    L_0x013d:
        r11 = 0;
        r6 = new java.util.ArrayList;	 Catch:{ IOException -> 0x01eb }
        r6.<init>();	 Catch:{ IOException -> 0x01eb }
        switch(r32) {
            case 0: goto L_0x014a;
            case 1: goto L_0x0291;
            case 2: goto L_0x03dd;
            case 3: goto L_0x03e6;
            case 4: goto L_0x03ef;
            case 5: goto L_0x03f8;
            case 6: goto L_0x0408;
            case 7: goto L_0x0418;
            case 8: goto L_0x0446;
            case 10: goto L_0x0456;
            case 255: goto L_0x01a2;
            default: goto L_0x0146;
        };	 Catch:{ IOException -> 0x01eb }
    L_0x0146:
        r33 = 0;
        goto L_0x007e;
    L_0x014a:
        if (r17 == 0) goto L_0x020f;
    L_0x014c:
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r34.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = " Token 0:";
        r34 = r34.append(r35);	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r37;
        r34 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r34 = r34.toString();	 Catch:{ IOException -> 0x01eb }
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "ADVANCEDDIGEST";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r33 = r0.checkExistTAG(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x01e3;
    L_0x017d:
        r33 = "ADVANCEDDIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x0184:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
    L_0x01a2:
        r31.clear();	 Catch:{ IOException -> 0x01eb }
        r10 = 0;
    L_0x01a6:
        r33 = r6.size();	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        if (r10 >= r0) goto L_0x048e;
    L_0x01ae:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r3 = r31;
        r33 = r0.parseXML(r1, r2, r3);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0483;
    L_0x01c2:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
        r10 = r10 + 1;
        goto L_0x01a6;
    L_0x01e3:
        r33 = "DIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x0184;
    L_0x01eb:
        r8 = move-exception;
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;	 Catch:{ XmlPullParserException -> 0x0268 }
        r34.<init>();	 Catch:{ XmlPullParserException -> 0x0268 }
        r35 = " ";
        r34 = r34.append(r35);	 Catch:{ XmlPullParserException -> 0x0268 }
        r35 = r8.toString();	 Catch:{ XmlPullParserException -> 0x0268 }
        r34 = r34.append(r35);	 Catch:{ XmlPullParserException -> 0x0268 }
        r34 = r34.toString();	 Catch:{ XmlPullParserException -> 0x0268 }
        android.util.Log.i(r33, r34);	 Catch:{ XmlPullParserException -> 0x0268 }
        r8.printStackTrace();	 Catch:{ XmlPullParserException -> 0x0268 }
        r33 = 0;
        goto L_0x007e;
    L_0x020f:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CERT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.misAppSystem;	 Catch:{ IOException -> 0x01eb }
        r33 = r0;
        if (r33 != 0) goto L_0x0258;
    L_0x0225:
        r33 = "ADVANCEDDIGEST";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r33 = r0.checkExistTAG(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0289;
    L_0x0233:
        r33 = "ADVANCEDDIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x023a:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
    L_0x0258:
        r33 = "MODELS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CARRIERS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x0268:
        r8 = move-exception;
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = " ";
        r34 = r34.append(r35);
        r35 = r8.toString();
        r34 = r34.append(r35);
        r34 = r34.toString();
        android.util.Log.i(r33, r34);
        r33 = 0;
        goto L_0x007e;
    L_0x0289:
        r33 = "DIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x023a;
    L_0x0291:
        if (r17 == 0) goto L_0x0384;
    L_0x0293:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "DIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CREATE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "VERSION";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "INDEX";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "MODELS";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r33 = r0.checkExistTAG(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x02cb;
    L_0x02c4:
        r33 = "MODELS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x02cb:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
        r33 = "CARRIERS";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r33 = r0.checkExistTAG(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x02fe;
    L_0x02f7:
        r33 = "CARRIERS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x02fe:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
        r33 = "EXPIRED";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r33 = r0.checkExistTAG(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0331;
    L_0x032a:
        r33 = "EXPIRED";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x0331:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
        r33 = "SERIALNUMBER";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r33 = r0.checkExistTAG(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0364;
    L_0x035d:
        r33 = "SERIALNUMBER";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x0364:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x0384:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CERT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.misAppSystem;	 Catch:{ IOException -> 0x01eb }
        r33 = r0;
        if (r33 != 0) goto L_0x01a2;
    L_0x039a:
        r33 = "ADVANCEDDIGEST";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r33 = r0.checkExistTAG(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x03bd;
    L_0x03a8:
        r33 = "ADVANCEDDIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "MODELS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CARRIERS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x03bd:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x03dd:
        r33 = "CERT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x03e6:
        r33 = "UUID";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x03ef:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x03f8:
        r33 = "UUID";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "ACCOUNT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x0408:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CERT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x0418:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CERT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.misAppSystem;	 Catch:{ IOException -> 0x01eb }
        r33 = r0;
        if (r33 != 0) goto L_0x0435;
    L_0x042e:
        r33 = "DIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x0435:
        r33 = "MODELS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CARRIERS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r11 = 1;
        goto L_0x01a2;
    L_0x0446:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CERT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x0456:
        r33 = "PACKAGE";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CERT";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.misAppSystem;	 Catch:{ IOException -> 0x01eb }
        r33 = r0;
        if (r33 != 0) goto L_0x0473;
    L_0x046c:
        r33 = "ADVANCEDDIGEST";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
    L_0x0473:
        r33 = "MODELS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        r33 = "CARRIERS";
        r0 = r33;
        r6.add(r0);	 Catch:{ IOException -> 0x01eb }
        goto L_0x01a2;
    L_0x0483:
        r33 = "AASATokenParser";
        r34 = " Token does not have value";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        goto L_0x007e;
    L_0x048e:
        r28 = 0;
        r12 = 0;
        r13 = 0;
        r7 = 0;
        r9 = 0;
        r15 = 0;
        r23 = 0;
        r22 = 0;
        r10 = 0;
    L_0x049a:
        r33 = r6.size();	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        if (r10 >= r0) goto L_0x08e3;
    L_0x04a2:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "PACKAGE";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x04e9;
    L_0x04b0:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        r1 = r37;
        r33 = r0.equals(r1);	 Catch:{ IOException -> 0x01eb }
        r34 = 1;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x04e6;
    L_0x04c8:
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r34.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "OK:";
        r34 = r34.append(r35);	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r37;
        r34 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r34 = r34.toString();	 Catch:{ IOException -> 0x01eb }
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
    L_0x04e6:
        r10 = r10 + 1;
        goto L_0x049a;
    L_0x04e9:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "CERT";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0532;
    L_0x04f7:
        r14 = 0;
    L_0x04f8:
        r0 = r4.length;	 Catch:{ IOException -> 0x01eb }
        r33 = r0;
        r0 = r33;
        if (r14 >= r0) goto L_0x04e6;
    L_0x04ff:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = new java.lang.String;	 Catch:{ IOException -> 0x01eb }
        r35 = r4[r14];	 Catch:{ IOException -> 0x01eb }
        r35 = r35.getPublicKey();	 Catch:{ IOException -> 0x01eb }
        r35 = r35.getEncoded();	 Catch:{ IOException -> 0x01eb }
        r35 = toChars(r35);	 Catch:{ IOException -> 0x01eb }
        r34.<init>(r35);	 Catch:{ IOException -> 0x01eb }
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 1;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x052f;
    L_0x0526:
        r28 = r28 + 1;
        r33 = "AASATokenParser";
        r34 = "OK:CERT";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
    L_0x052f:
        r14 = r14 + 1;
        goto L_0x04f8;
    L_0x0532:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "UUID";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x055e;
    L_0x0540:
        r33 = "AASATokenParser";
        r34 = "OK:UUID";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        if (r38 == 0) goto L_0x04e6;
    L_0x0549:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        r1 = r38;
        r33 = r0.equals(r1);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x04e6;
    L_0x055b:
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x055e:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "ACCOUNT";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0577;
    L_0x056c:
        r33 = "AASATokenParser";
        r34 = "OK:ACCOUNT";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x0577:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "DIGEST";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0601;
    L_0x0585:
        if (r17 == 0) goto L_0x05e4;
    L_0x0587:
        r5 = r36.advancedCheckHash();	 Catch:{ IOException -> 0x01eb }
        if (r5 == 0) goto L_0x05a6;
    L_0x058d:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        r33 = r5.equals(r0);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x05a6;
    L_0x059b:
        r33 = "AASATokenParser";
        r34 = "OK:AdvancedHASH";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x05a6:
        r34 = "AASATokenParser";
        r33 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r33.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "NOT OK:AdvancedHASH : ";
        r0 = r33;
        r1 = r35;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        r33 = r0.append(r5);	 Catch:{ IOException -> 0x01eb }
        r35 = " comp : ";
        r0 = r33;
        r1 = r35;
        r35 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r35;
        r1 = r33;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r33 = r33.toString();	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r33;
        android.util.Log.i(r0, r1);	 Catch:{ IOException -> 0x01eb }
        goto L_0x04e6;
    L_0x05e4:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r1 = r33;
        r33 = r0.checkHash(r1);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x04e6;
    L_0x05f6:
        r33 = "AASATokenParser";
        r34 = "OK:HASH";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x0601:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "ADVANCEDDIGEST";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x066c;
    L_0x060f:
        r5 = r36.advancedCheckHash();	 Catch:{ IOException -> 0x01eb }
        if (r5 == 0) goto L_0x062e;
    L_0x0615:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        r33 = r5.equals(r0);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x062e;
    L_0x0623:
        r33 = "AASATokenParser";
        r34 = "OK:AdvancedHASH";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x062e:
        r34 = "AASATokenParser";
        r33 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r33.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "NOT OK:AdvancedHASH : ";
        r0 = r33;
        r1 = r35;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        r33 = r0.append(r5);	 Catch:{ IOException -> 0x01eb }
        r35 = " comp : ";
        r0 = r33;
        r1 = r35;
        r35 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r35;
        r1 = r33;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r33 = r33.toString();	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r33;
        android.util.Log.i(r0, r1);	 Catch:{ IOException -> 0x01eb }
        goto L_0x04e6;
    L_0x066c:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "MODELS";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x06a1;
    L_0x067a:
        if (r12 != 0) goto L_0x0687;
    L_0x067c:
        r12 = 1;
        r0 = r31;
        r23 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r23 = (java.lang.String) r23;	 Catch:{ IOException -> 0x01eb }
        goto L_0x04e6;
    L_0x0687:
        r0 = r31;
        r23 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r23 = (java.lang.String) r23;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r1 = r23;
        r2 = r22;
        r33 = r0.checkTokentarget(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x04e6;
    L_0x069b:
        r28 = r28 + 1;
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x06a1:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "CARRIERS";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x06da;
    L_0x06af:
        r33 = 1;
        r0 = r33;
        if (r12 != r0) goto L_0x06cf;
    L_0x06b5:
        r0 = r31;
        r22 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r22 = (java.lang.String) r22;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r1 = r23;
        r2 = r22;
        r33 = r0.checkTokentarget(r1, r2);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x04e6;
    L_0x06c9:
        r28 = r28 + 1;
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x06cf:
        r12 = 1;
        r0 = r31;
        r22 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r22 = (java.lang.String) r22;	 Catch:{ IOException -> 0x01eb }
        goto L_0x04e6;
    L_0x06da:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "CREATE";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0788;
    L_0x06e8:
        r34 = "AASATokenParser";
        r33 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r33.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "CREATE : ";
        r0 = r33;
        r1 = r35;
        r35 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r35;
        r1 = r33;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r33 = r33.toString();	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r33;
        android.util.Log.d(r0, r1);	 Catch:{ IOException -> 0x01eb }
        if (r13 == 0) goto L_0x077b;
    L_0x0716:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ NumberFormatException -> 0x076f }
        r0 = r33;
        r0 = (java.lang.String) r0;	 Catch:{ NumberFormatException -> 0x076f }
        r7 = r0;
        r33 = java.lang.Integer.parseInt(r7);	 Catch:{ NumberFormatException -> 0x076f }
        r34 = java.lang.Integer.parseInt(r9);	 Catch:{ NumberFormatException -> 0x076f }
        r0 = r33;
        r1 = r34;
        if (r0 <= r1) goto L_0x073a;
    L_0x072f:
        r33 = "AASATokenParser";
        r34 = "createDate is bigger than expiredDate.";
        android.util.Log.d(r33, r34);	 Catch:{ NumberFormatException -> 0x076f }
        r33 = 0;
        goto L_0x007e;
    L_0x073a:
        r33 = new java.text.SimpleDateFormat;	 Catch:{ NumberFormatException -> 0x076f }
        r34 = "yyyyMMdd";
        r33.<init>(r34);	 Catch:{ NumberFormatException -> 0x076f }
        r34 = new java.util.Date;	 Catch:{ NumberFormatException -> 0x076f }
        r34.<init>();	 Catch:{ NumberFormatException -> 0x076f }
        r33 = r33.format(r34);	 Catch:{ NumberFormatException -> 0x076f }
        r29 = java.lang.Integer.valueOf(r33);	 Catch:{ NumberFormatException -> 0x076f }
        r33 = r29.intValue();	 Catch:{ NumberFormatException -> 0x076f }
        r34 = java.lang.Integer.parseInt(r9);	 Catch:{ NumberFormatException -> 0x076f }
        r0 = r33;
        r1 = r34;
        if (r0 <= r1) goto L_0x0769;
    L_0x075d:
        r33 = "AASATokenParser";
        r34 = "today Date is bigger than expiredDate.";
        android.util.Log.d(r33, r34);	 Catch:{ NumberFormatException -> 0x076f }
        r33 = 0;
        goto L_0x007e;
    L_0x0769:
        r28 = r28 + 1;
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x076f:
        r24 = move-exception;
        r33 = "AASATokenParser";
        r34 = "CREATE : NumberFormatException";
        android.util.Log.d(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        goto L_0x007e;
    L_0x077b:
        r13 = 1;
        r0 = r31;
        r7 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r7 = (java.lang.String) r7;	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x0788:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "EXPIRED";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0832;
    L_0x0796:
        r34 = "AASATokenParser";
        r33 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r33.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "EXPIRED : ";
        r0 = r33;
        r1 = r35;
        r35 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r35;
        r1 = r33;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r33 = r33.toString();	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r33;
        android.util.Log.d(r0, r1);	 Catch:{ IOException -> 0x01eb }
        if (r13 == 0) goto L_0x0827;
    L_0x07c4:
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ NumberFormatException -> 0x081b }
        r0 = r33;
        r0 = (java.lang.String) r0;	 Catch:{ NumberFormatException -> 0x081b }
        r9 = r0;
        r33 = java.lang.Integer.parseInt(r7);	 Catch:{ NumberFormatException -> 0x081b }
        r34 = java.lang.Integer.parseInt(r9);	 Catch:{ NumberFormatException -> 0x081b }
        r0 = r33;
        r1 = r34;
        if (r0 <= r1) goto L_0x07e8;
    L_0x07dd:
        r33 = "AASATokenParser";
        r34 = "createDate is bigger than expiredDate.";
        android.util.Log.d(r33, r34);	 Catch:{ NumberFormatException -> 0x081b }
        r33 = 0;
        goto L_0x007e;
    L_0x07e8:
        r33 = new java.text.SimpleDateFormat;	 Catch:{ NumberFormatException -> 0x081b }
        r34 = "yyyyMMdd";
        r33.<init>(r34);	 Catch:{ NumberFormatException -> 0x081b }
        r34 = new java.util.Date;	 Catch:{ NumberFormatException -> 0x081b }
        r34.<init>();	 Catch:{ NumberFormatException -> 0x081b }
        r33 = r33.format(r34);	 Catch:{ NumberFormatException -> 0x081b }
        r29 = java.lang.Integer.valueOf(r33);	 Catch:{ NumberFormatException -> 0x081b }
        r33 = r29.intValue();	 Catch:{ NumberFormatException -> 0x081b }
        r34 = java.lang.Integer.parseInt(r9);	 Catch:{ NumberFormatException -> 0x081b }
        r0 = r33;
        r1 = r34;
        if (r0 <= r1) goto L_0x0817;
    L_0x080b:
        r33 = "AASATokenParser";
        r34 = "today Date is bigger than expiredDate.";
        android.util.Log.d(r33, r34);	 Catch:{ NumberFormatException -> 0x081b }
        r33 = 0;
        goto L_0x007e;
    L_0x0817:
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x081b:
        r24 = move-exception;
        r33 = "AASATokenParser";
        r34 = "EXPIRED : NumberFormatException";
        android.util.Log.d(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        goto L_0x007e;
    L_0x0827:
        r13 = 1;
        r0 = r31;
        r9 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r9 = (java.lang.String) r9;	 Catch:{ IOException -> 0x01eb }
        goto L_0x04e6;
    L_0x0832:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "VERSION";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0870;
    L_0x0840:
        r34 = "AASATokenParser";
        r33 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r33.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "VERSION : ";
        r0 = r33;
        r1 = r35;
        r35 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r35;
        r1 = r33;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r33 = r33.toString();	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r33;
        android.util.Log.d(r0, r1);	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x0870:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "INDEX";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x08a4;
    L_0x087e:
        r0 = r31;
        r15 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r15 = (java.lang.String) r15;	 Catch:{ IOException -> 0x01eb }
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r34.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "INDEX : ";
        r34 = r34.append(r35);	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r34 = r0.append(r15);	 Catch:{ IOException -> 0x01eb }
        r34 = r34.toString();	 Catch:{ IOException -> 0x01eb }
        android.util.Log.d(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x08a4:
        r33 = r6.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r34 = "SERIALNUMBER";
        r33 = r33.equals(r34);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x04e6;
    L_0x08b2:
        r34 = "AASATokenParser";
        r33 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r33.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = "SERIALNUMBER : ";
        r0 = r33;
        r1 = r35;
        r35 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r0 = r31;
        r33 = r0.get(r10);	 Catch:{ IOException -> 0x01eb }
        r33 = (java.lang.String) r33;	 Catch:{ IOException -> 0x01eb }
        r0 = r35;
        r1 = r33;
        r33 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r33 = r33.toString();	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r33;
        android.util.Log.d(r0, r1);	 Catch:{ IOException -> 0x01eb }
        r11 = 1;
        r28 = r28 + 1;
        goto L_0x04e6;
    L_0x08e3:
        r33 = r6.size();	 Catch:{ IOException -> 0x01eb }
        r0 = r33;
        r1 = r28;
        if (r0 != r1) goto L_0x0988;
    L_0x08ed:
        if (r11 != 0) goto L_0x08fa;
    L_0x08ef:
        r33 = "AASATokenParser";
        r34 = " Pass ALL";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 1;
        goto L_0x007e;
    L_0x08fa:
        r30 = new java.util.ArrayList;	 Catch:{ IOException -> 0x01eb }
        r30.<init>();	 Catch:{ IOException -> 0x01eb }
        r33 = "SERIALNUMBER";
        r0 = r36;
        r1 = r25;
        r2 = r33;
        r3 = r30;
        r33 = r0.parseXML(r1, r2, r3);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x092d;
    L_0x090f:
        r26 = org.xmlpull.v1.XmlPullParserFactory.newInstance();	 Catch:{ IOException -> 0x01eb }
        r25 = r26.newPullParser();	 Catch:{ IOException -> 0x01eb }
        r33 = new java.io.ByteArrayInputStream;	 Catch:{ IOException -> 0x01eb }
        r0 = r36;
        r0 = r0.mTokenContents;	 Catch:{ IOException -> 0x01eb }
        r34 = r0;
        r33.<init>(r34);	 Catch:{ IOException -> 0x01eb }
        r34 = 0;
        r0 = r25;
        r1 = r33;
        r2 = r34;
        r0.setInput(r1, r2);	 Catch:{ IOException -> 0x01eb }
    L_0x092d:
        r33 = r30.size();	 Catch:{ IOException -> 0x01eb }
        if (r33 != 0) goto L_0x093e;
    L_0x0933:
        r33 = "AASATokenParser";
        r34 = " Fail:uuidList";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        goto L_0x007e;
    L_0x093e:
        r0 = r30;
        r1 = r38;
        r33 = r0.contains(r1);	 Catch:{ IOException -> 0x01eb }
        if (r33 == 0) goto L_0x0968;
    L_0x0948:
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r34.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = " PASS ALL with S/N:";
        r34 = r34.append(r35);	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r38;
        r34 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r34 = r34.toString();	 Catch:{ IOException -> 0x01eb }
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 1;
        goto L_0x007e;
    L_0x0968:
        r33 = "AASATokenParser";
        r34 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x01eb }
        r34.<init>();	 Catch:{ IOException -> 0x01eb }
        r35 = " FAIL only S/N:";
        r34 = r34.append(r35);	 Catch:{ IOException -> 0x01eb }
        r0 = r34;
        r1 = r38;
        r34 = r0.append(r1);	 Catch:{ IOException -> 0x01eb }
        r34 = r34.toString();	 Catch:{ IOException -> 0x01eb }
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        goto L_0x007e;
    L_0x0988:
        r33 = "AASATokenParser";
        r34 = " Fail: auth";
        android.util.Log.i(r33, r34);	 Catch:{ IOException -> 0x01eb }
        r33 = 0;
        goto L_0x007e;
    L_0x0993:
        r33 = move-exception;
        r19 = r20;
        goto L_0x00a3;
    L_0x0998:
        r16 = move-exception;
        r19 = r20;
        goto L_0x0080;
    L_0x099d:
        r19 = r20;
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.AASATokenParser.AASA_VerifyToken(java.lang.String, java.lang.String):boolean");
    }

    public boolean AASA_getTokenContent() {
        this.mTokenValue = new ArrayList();
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
            try {
                if (!parseXML(parser, "PACKAGE", this.mTokenValue)) {
                    return false;
                }
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                if (!parseXML(parser, "DATE", this.mTokenValue)) {
                    return false;
                }
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                if (parseXML(parser, "VERSION", this.mTokenValue)) {
                    parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                } else {
                    this.mTokenValue.add(WifiEnterpriseConfig.ENGINE_ENABLE);
                    parser = XmlPullParserFactory.newInstance().newPullParser();
                    parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                }
                ArrayList<String> locSharedAllowPM = new ArrayList();
                parseXML(parser, "SharedAllowPM", locSharedAllowPM);
                parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                ArrayList<String> locSharedDeniedPM = new ArrayList();
                parseXML(parser, "PERMISSION", locSharedDeniedPM);
                XmlPullParserFactory.newInstance().newPullParser().setInput(new ByteArrayInputStream(this.mTokenContents), "UTF-8");
                if (locSharedAllowPM.size() == 0 && locSharedDeniedPM.size() == 0) {
                    return false;
                }
                int ii;
                this.mTokenValue.add(Integer.toString(locSharedAllowPM.size()) + "," + Integer.toString(locSharedDeniedPM.size()));
                if (locSharedAllowPM.size() > 0) {
                    for (ii = 0; ii < locSharedAllowPM.size(); ii++) {
                        this.mTokenValue.add(locSharedAllowPM.get(ii));
                    }
                }
                if (locSharedDeniedPM.size() > 0) {
                    for (ii = 0; ii < locSharedDeniedPM.size(); ii++) {
                        this.mTokenValue.add(locSharedDeniedPM.get(ii));
                    }
                }
                return true;
            } catch (IOException e) {
                Log.i("AASATokenParser", "er" + e.toString());
                return false;
            }
        } catch (XmlPullParserException e2) {
            Log.i("AASATokenParser", "er" + e2.toString());
            return false;
        }
    }

    public boolean SKA_CheckList(String pkgName, String hash) {
        HashMap<String, ArrayList<String>> checkList = new HashMap();
        ArrayList<String> hashList = new ArrayList();
        SKA_GetDataFromXml(checkList);
        if (checkList == null || !checkList.containsKey(pkgName)) {
            return false;
        }
        if (hash == null) {
            return true;
        }
        hashList = (ArrayList) checkList.get(pkgName);
        if (hashList == null || !hashList.contains(hash)) {
            return false;
        }
        return true;
    }

    private void SKA_GetDataFromXml(HashMap<String, ArrayList<String>> store) {
        String path = ProxyInfo.LOCAL_EXCL_LIST;
        String[] features = new String[]{"CERT", "NUM"};
        File file = new File("/data/system/.aasa/AASApolicy/ASKSC.xml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
            file.getParentFile().setReadable(true, false);
        }
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(fileReader);
                    String keyName = ProxyInfo.LOCAL_EXCL_LIST;
                    ArrayList<String> values = null;
                    String date = ProxyInfo.LOCAL_EXCL_LIST;
                    for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                        String name = parser.getName();
                        switch (eventType) {
                            case 2:
                                if (features[0].equals(name)) {
                                    if (parser.getAttributeValue(0) != null) {
                                        keyName = parser.getAttributeValue(0);
                                    }
                                    values = new ArrayList();
                                }
                                if (features[1].equals(name) && parser.getAttributeValue(0) != null) {
                                    values.add(parser.getAttributeValue(0));
                                }
                                if ("DATE".equals(name) && parser.getAttributeValue(0) != null) {
                                    date = parser.getAttributeValue(0);
                                    break;
                                }
                            case 3:
                                if (features[0].equals(name) && store != null) {
                                    store.put(keyName, values);
                                    break;
                                }
                        }
                    }
                    fileReader.close();
                } catch (XmlPullParserException e) {
                    try {
                        fileReader.close();
                    } catch (IOException e2) {
                    }
                    e.printStackTrace();
                } catch (IOException e3) {
                    try {
                        fileReader.close();
                    } catch (IOException e4) {
                    }
                    e3.printStackTrace();
                }
            } catch (FileNotFoundException e5) {
                e5.printStackTrace();
            }
        }
    }

    private boolean checkTokentarget(String modelName, String carrier) {
        if (modelName == null || carrier == null) {
            Log.i("AASATokenParser", "ERROR: checkTokenTarget input is null");
            return false;
        }
        String[] rule_model = modelName.split(",");
        String[] rule_carrier = carrier.split(",");
        String target_model = SystemProperties.get("ro.product.model");
        String target_carrier = SystemProperties.get("ro.csc.sales_code");
        boolean result = false;
        int i;
        if ("ALL".equals(rule_model[0])) {
            if (rule_model.length != 1) {
                result = true;
                for (i = 1; i < rule_model.length; i++) {
                    if (rule_model[i].equals(target_model)) {
                        result = false;
                        break;
                    }
                }
                if (!result) {
                    return result;
                }
                if ("ALL".equals(rule_carrier[0])) {
                    if (rule_carrier.length == 1) {
                        return true;
                    }
                    for (i = 1; i < rule_carrier.length; i++) {
                        if (rule_carrier[i].equals(target_carrier)) {
                            return false;
                        }
                    }
                    return result;
                } else if ("ALL".equals(rule_carrier[0])) {
                    return result;
                } else {
                    for (String equals : rule_carrier) {
                        if (equals.equals(target_carrier)) {
                            return true;
                        }
                    }
                    return false;
                }
            } else if ("ALL".equals(rule_carrier[0])) {
                if (rule_carrier.length == 1) {
                    return true;
                }
                for (i = 1; i < rule_carrier.length; i++) {
                    if (rule_carrier[i].equals(target_carrier)) {
                        return false;
                    }
                }
                return true;
            } else if ("ALL".equals(rule_carrier[0])) {
                return false;
            } else {
                for (String equals2 : rule_carrier) {
                    if (equals2.equals(target_carrier)) {
                        return true;
                    }
                }
                return false;
            }
        } else if ("ALL".equals(rule_model[0])) {
            return false;
        } else {
            for (String equals3 : rule_model) {
                if (equals3.equals(target_model)) {
                    result = true;
                    break;
                }
            }
            if (!result) {
                return result;
            }
            if ("ALL".equals(rule_carrier[0])) {
                if (rule_carrier.length == 1) {
                    for (String equals22 : rule_model) {
                        if (equals22.equals(target_model)) {
                            return true;
                        }
                    }
                    return false;
                }
                for (i = 1; i < rule_carrier.length; i++) {
                    if (rule_carrier[i].equals(target_carrier)) {
                        return false;
                    }
                }
                return result;
            } else if ("ALL".equals(rule_carrier[0])) {
                return result;
            } else {
                for (String equals222 : rule_carrier) {
                    if (equals222.equals(target_carrier)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }
}
