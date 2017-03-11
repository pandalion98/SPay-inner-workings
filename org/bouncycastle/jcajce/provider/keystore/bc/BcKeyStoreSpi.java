package org.bouncycastle.jcajce.provider.keystore.bc;

import com.google.android.gms.location.places.Place;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.io.DigestInputStream;
import org.bouncycastle.crypto.io.DigestOutputStream;
import org.bouncycastle.crypto.io.MacInputStream;
import org.bouncycastle.crypto.io.MacOutputStream;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.interfaces.BCKeyStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;
import org.bouncycastle.util.io.TeeOutputStream;

public class BcKeyStoreSpi extends KeyStoreSpi implements BCKeyStore {
    static final int CERTIFICATE = 1;
    static final int KEY = 2;
    private static final String KEY_CIPHER = "PBEWithSHAAnd3-KeyTripleDES-CBC";
    static final int KEY_PRIVATE = 0;
    static final int KEY_PUBLIC = 1;
    private static final int KEY_SALT_SIZE = 20;
    static final int KEY_SECRET = 2;
    private static final int MIN_ITERATIONS = 1024;
    static final int NULL = 0;
    static final int SEALED = 4;
    static final int SECRET = 3;
    private static final String STORE_CIPHER = "PBEWithSHAAndTwofish-CBC";
    private static final int STORE_SALT_SIZE = 20;
    private static final int STORE_VERSION = 2;
    private final JcaJceHelper helper;
    protected SecureRandom random;
    protected Hashtable table;
    protected int version;

    public static class BouncyCastleStore extends BcKeyStoreSpi {
        public BouncyCastleStore() {
            super(BcKeyStoreSpi.KEY_PUBLIC);
        }

        public void engineLoad(InputStream inputStream, char[] cArr) {
            this.table.clear();
            if (inputStream != null) {
                InputStream dataInputStream = new DataInputStream(inputStream);
                int readInt = dataInputStream.readInt();
                if (readInt == BcKeyStoreSpi.STORE_VERSION || readInt == 0 || readInt == BcKeyStoreSpi.KEY_PUBLIC) {
                    byte[] bArr = new byte[dataInputStream.readInt()];
                    if (bArr.length != BcKeyStoreSpi.STORE_SALT_SIZE) {
                        throw new IOException("Key store corrupted.");
                    }
                    dataInputStream.readFully(bArr);
                    int readInt2 = dataInputStream.readInt();
                    if (readInt2 < 0 || readInt2 > PKIFailureInfo.certConfirmed) {
                        throw new IOException("Key store corrupted.");
                    }
                    InputStream cipherInputStream = new CipherInputStream(dataInputStream, makePBECipher(readInt == 0 ? "OldPBEWithSHAAndTwofish-CBC" : BcKeyStoreSpi.STORE_CIPHER, BcKeyStoreSpi.STORE_VERSION, cArr, bArr, readInt2));
                    Digest sHA1Digest = new SHA1Digest();
                    loadStore(new DigestInputStream(cipherInputStream, sHA1Digest));
                    byte[] bArr2 = new byte[sHA1Digest.getDigestSize()];
                    sHA1Digest.doFinal(bArr2, BcKeyStoreSpi.NULL);
                    byte[] bArr3 = new byte[sHA1Digest.getDigestSize()];
                    Streams.readFully(cipherInputStream, bArr3);
                    if (!Arrays.constantTimeAreEqual(bArr2, bArr3)) {
                        this.table.clear();
                        throw new IOException("KeyStore integrity check failed.");
                    }
                    return;
                }
                throw new IOException("Wrong version of key store.");
            }
        }

        public void engineStore(OutputStream outputStream, char[] cArr) {
            OutputStream dataOutputStream = new DataOutputStream(outputStream);
            byte[] bArr = new byte[BcKeyStoreSpi.STORE_SALT_SIZE];
            int nextInt = (this.random.nextInt() & Place.TYPE_SUBLOCALITY_LEVEL_1) + BcKeyStoreSpi.MIN_ITERATIONS;
            this.random.nextBytes(bArr);
            dataOutputStream.writeInt(this.version);
            dataOutputStream.writeInt(bArr.length);
            dataOutputStream.write(bArr);
            dataOutputStream.writeInt(nextInt);
            OutputStream cipherOutputStream = new CipherOutputStream(dataOutputStream, makePBECipher(BcKeyStoreSpi.STORE_CIPHER, BcKeyStoreSpi.KEY_PUBLIC, cArr, bArr, nextInt));
            OutputStream digestOutputStream = new DigestOutputStream(new SHA1Digest());
            saveStore(new TeeOutputStream(cipherOutputStream, digestOutputStream));
            cipherOutputStream.write(digestOutputStream.getDigest());
            cipherOutputStream.close();
        }
    }

    public static class Std extends BcKeyStoreSpi {
        public Std() {
            super(BcKeyStoreSpi.STORE_VERSION);
        }
    }

    private class StoreEntry {
        String alias;
        Certificate[] certChain;
        Date date;
        Object obj;
        int type;

        StoreEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) {
            this.date = new Date();
            this.type = BcKeyStoreSpi.SEALED;
            this.alias = str;
            this.certChain = certificateArr;
            byte[] bArr = new byte[BcKeyStoreSpi.STORE_SALT_SIZE];
            BcKeyStoreSpi.this.random.setSeed(System.currentTimeMillis());
            BcKeyStoreSpi.this.random.nextBytes(bArr);
            int nextInt = (BcKeyStoreSpi.this.random.nextInt() & Place.TYPE_SUBLOCALITY_LEVEL_1) + BcKeyStoreSpi.MIN_ITERATIONS;
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(bArr.length);
            dataOutputStream.write(bArr);
            dataOutputStream.writeInt(nextInt);
            DataOutputStream dataOutputStream2 = new DataOutputStream(new CipherOutputStream(dataOutputStream, BcKeyStoreSpi.this.makePBECipher(BcKeyStoreSpi.KEY_CIPHER, BcKeyStoreSpi.KEY_PUBLIC, cArr, bArr, nextInt)));
            BcKeyStoreSpi.this.encodeKey(key, dataOutputStream2);
            dataOutputStream2.close();
            this.obj = byteArrayOutputStream.toByteArray();
        }

        StoreEntry(String str, Certificate certificate) {
            this.date = new Date();
            this.type = BcKeyStoreSpi.KEY_PUBLIC;
            this.alias = str;
            this.obj = certificate;
            this.certChain = null;
        }

        StoreEntry(String str, Date date, int i, Object obj) {
            this.date = new Date();
            this.alias = str;
            this.date = date;
            this.type = i;
            this.obj = obj;
        }

        StoreEntry(String str, Date date, int i, Object obj, Certificate[] certificateArr) {
            this.date = new Date();
            this.alias = str;
            this.date = date;
            this.type = i;
            this.obj = obj;
            this.certChain = certificateArr;
        }

        StoreEntry(String str, byte[] bArr, Certificate[] certificateArr) {
            this.date = new Date();
            this.type = BcKeyStoreSpi.SECRET;
            this.alias = str;
            this.obj = bArr;
            this.certChain = certificateArr;
        }

        String getAlias() {
            return this.alias;
        }

        Certificate[] getCertificateChain() {
            return this.certChain;
        }

        Date getDate() {
            return this.date;
        }

        Object getObject() {
            return this.obj;
        }

        Object getObject(char[] cArr) {
            byte[] bArr;
            Key access$100;
            if ((cArr == null || cArr.length == 0) && (this.obj instanceof Key)) {
                return this.obj;
            }
            if (this.type == BcKeyStoreSpi.SEALED) {
                InputStream dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[]) this.obj));
                try {
                    bArr = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(bArr);
                    return BcKeyStoreSpi.this.decodeKey(new DataInputStream(new CipherInputStream(dataInputStream, BcKeyStoreSpi.this.makePBECipher(BcKeyStoreSpi.KEY_CIPHER, BcKeyStoreSpi.STORE_VERSION, cArr, bArr, dataInputStream.readInt()))));
                } catch (Exception e) {
                    dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[]) this.obj));
                    bArr = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(bArr);
                    int readInt = dataInputStream.readInt();
                    try {
                        access$100 = BcKeyStoreSpi.this.decodeKey(new DataInputStream(new CipherInputStream(dataInputStream, BcKeyStoreSpi.this.makePBECipher("BrokenPBEWithSHAAnd3-KeyTripleDES-CBC", BcKeyStoreSpi.STORE_VERSION, cArr, bArr, readInt))));
                    } catch (Exception e2) {
                        dataInputStream = new DataInputStream(new ByteArrayInputStream((byte[]) this.obj));
                        bArr = new byte[dataInputStream.readInt()];
                        dataInputStream.readFully(bArr);
                        readInt = dataInputStream.readInt();
                        access$100 = BcKeyStoreSpi.this.decodeKey(new DataInputStream(new CipherInputStream(dataInputStream, BcKeyStoreSpi.this.makePBECipher("OldPBEWithSHAAnd3-KeyTripleDES-CBC", BcKeyStoreSpi.STORE_VERSION, cArr, bArr, readInt))));
                    }
                    if (access$100 != null) {
                        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        OutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                        dataOutputStream.writeInt(bArr.length);
                        dataOutputStream.write(bArr);
                        dataOutputStream.writeInt(readInt);
                        DataOutputStream dataOutputStream2 = new DataOutputStream(new CipherOutputStream(dataOutputStream, BcKeyStoreSpi.this.makePBECipher(BcKeyStoreSpi.KEY_CIPHER, BcKeyStoreSpi.KEY_PUBLIC, cArr, bArr, readInt)));
                        BcKeyStoreSpi.this.encodeKey(access$100, dataOutputStream2);
                        dataOutputStream2.close();
                        this.obj = byteArrayOutputStream.toByteArray();
                        return access$100;
                    }
                    throw new UnrecoverableKeyException("no match");
                } catch (Exception e3) {
                    throw new UnrecoverableKeyException("no match");
                }
            }
            throw new RuntimeException("forget something!");
        }

        int getType() {
            return this.type;
        }
    }

    public static class Version1 extends BcKeyStoreSpi {
        public Version1() {
            super(BcKeyStoreSpi.KEY_PUBLIC);
        }
    }

    public BcKeyStoreSpi(int i) {
        this.table = new Hashtable();
        this.random = new SecureRandom();
        this.helper = new BCJcaJceHelper();
        this.version = i;
    }

    private Certificate decodeCertificate(DataInputStream dataInputStream) {
        String readUTF = dataInputStream.readUTF();
        byte[] bArr = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(bArr);
        try {
            return this.helper.createCertificateFactory(readUTF).generateCertificate(new ByteArrayInputStream(bArr));
        } catch (NoSuchProviderException e) {
            throw new IOException(e.toString());
        } catch (CertificateException e2) {
            throw new IOException(e2.toString());
        }
    }

    private Key decodeKey(DataInputStream dataInputStream) {
        KeySpec pKCS8EncodedKeySpec;
        int read = dataInputStream.read();
        String readUTF = dataInputStream.readUTF();
        String readUTF2 = dataInputStream.readUTF();
        byte[] bArr = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(bArr);
        if (readUTF.equals("PKCS#8") || readUTF.equals("PKCS8")) {
            pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(bArr);
        } else if (readUTF.equals("X.509") || readUTF.equals("X509")) {
            pKCS8EncodedKeySpec = new X509EncodedKeySpec(bArr);
        } else if (readUTF.equals("RAW")) {
            return new SecretKeySpec(bArr, readUTF2);
        } else {
            throw new IOException("Key format " + readUTF + " not recognised!");
        }
        switch (read) {
            case NULL /*0*/:
                return this.helper.createKeyFactory(readUTF2).generatePrivate(pKCS8EncodedKeySpec);
            case KEY_PUBLIC /*1*/:
                return this.helper.createKeyFactory(readUTF2).generatePublic(pKCS8EncodedKeySpec);
            case STORE_VERSION /*2*/:
                return this.helper.createSecretKeyFactory(readUTF2).generateSecret(pKCS8EncodedKeySpec);
            default:
                try {
                    throw new IOException("Key type " + read + " not recognised!");
                } catch (Exception e) {
                    throw new IOException("Exception creating key: " + e.toString());
                }
        }
        throw new IOException("Exception creating key: " + e.toString());
    }

    private void encodeCertificate(Certificate certificate, DataOutputStream dataOutputStream) {
        try {
            byte[] encoded = certificate.getEncoded();
            dataOutputStream.writeUTF(certificate.getType());
            dataOutputStream.writeInt(encoded.length);
            dataOutputStream.write(encoded);
        } catch (CertificateEncodingException e) {
            throw new IOException(e.toString());
        }
    }

    private void encodeKey(Key key, DataOutputStream dataOutputStream) {
        byte[] encoded = key.getEncoded();
        if (key instanceof PrivateKey) {
            dataOutputStream.write(NULL);
        } else if (key instanceof PublicKey) {
            dataOutputStream.write(KEY_PUBLIC);
        } else {
            dataOutputStream.write(STORE_VERSION);
        }
        dataOutputStream.writeUTF(key.getFormat());
        dataOutputStream.writeUTF(key.getAlgorithm());
        dataOutputStream.writeInt(encoded.length);
        dataOutputStream.write(encoded);
    }

    static Provider getBouncyCastleProvider() {
        return Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) != null ? Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) : new BouncyCastleProvider();
    }

    public Enumeration engineAliases() {
        return this.table.keys();
    }

    public boolean engineContainsAlias(String str) {
        return this.table.get(str) != null;
    }

    public void engineDeleteEntry(String str) {
        if (this.table.get(str) != null) {
            this.table.remove(str);
        }
    }

    public Certificate engineGetCertificate(String str) {
        StoreEntry storeEntry = (StoreEntry) this.table.get(str);
        if (storeEntry != null) {
            if (storeEntry.getType() == KEY_PUBLIC) {
                return (Certificate) storeEntry.getObject();
            }
            Certificate[] certificateChain = storeEntry.getCertificateChain();
            if (certificateChain != null) {
                return certificateChain[NULL];
            }
        }
        return null;
    }

    public String engineGetCertificateAlias(Certificate certificate) {
        Enumeration elements = this.table.elements();
        while (elements.hasMoreElements()) {
            StoreEntry storeEntry = (StoreEntry) elements.nextElement();
            if (!(storeEntry.getObject() instanceof Certificate)) {
                Certificate[] certificateChain = storeEntry.getCertificateChain();
                if (certificateChain != null && certificateChain[NULL].equals(certificate)) {
                    return storeEntry.getAlias();
                }
            } else if (((Certificate) storeEntry.getObject()).equals(certificate)) {
                return storeEntry.getAlias();
            }
        }
        return null;
    }

    public Certificate[] engineGetCertificateChain(String str) {
        StoreEntry storeEntry = (StoreEntry) this.table.get(str);
        return storeEntry != null ? storeEntry.getCertificateChain() : null;
    }

    public Date engineGetCreationDate(String str) {
        StoreEntry storeEntry = (StoreEntry) this.table.get(str);
        return storeEntry != null ? storeEntry.getDate() : null;
    }

    public Key engineGetKey(String str, char[] cArr) {
        StoreEntry storeEntry = (StoreEntry) this.table.get(str);
        return (storeEntry == null || storeEntry.getType() == KEY_PUBLIC) ? null : (Key) storeEntry.getObject(cArr);
    }

    public boolean engineIsCertificateEntry(String str) {
        StoreEntry storeEntry = (StoreEntry) this.table.get(str);
        return storeEntry != null && storeEntry.getType() == KEY_PUBLIC;
    }

    public boolean engineIsKeyEntry(String str) {
        StoreEntry storeEntry = (StoreEntry) this.table.get(str);
        return (storeEntry == null || storeEntry.getType() == KEY_PUBLIC) ? false : true;
    }

    public void engineLoad(InputStream inputStream, char[] cArr) {
        this.table.clear();
        if (inputStream != null) {
            InputStream dataInputStream = new DataInputStream(inputStream);
            int readInt = dataInputStream.readInt();
            if (readInt == STORE_VERSION || readInt == 0 || readInt == KEY_PUBLIC) {
                int readInt2 = dataInputStream.readInt();
                if (readInt2 <= 0) {
                    throw new IOException("Invalid salt detected");
                }
                byte[] bArr = new byte[readInt2];
                dataInputStream.readFully(bArr);
                int readInt3 = dataInputStream.readInt();
                HMac hMac = new HMac(new SHA1Digest());
                if (cArr == null || cArr.length == 0) {
                    loadStore(dataInputStream);
                    dataInputStream.readFully(new byte[hMac.getMacSize()]);
                    return;
                }
                byte[] PKCS12PasswordToBytes = PBEParametersGenerator.PKCS12PasswordToBytes(cArr);
                PBEParametersGenerator pKCS12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
                pKCS12ParametersGenerator.init(PKCS12PasswordToBytes, bArr, readInt3);
                CipherParameters generateDerivedMacParameters = readInt != STORE_VERSION ? pKCS12ParametersGenerator.generateDerivedMacParameters(hMac.getMacSize()) : pKCS12ParametersGenerator.generateDerivedMacParameters(hMac.getMacSize() * 8);
                Arrays.fill(PKCS12PasswordToBytes, (byte) 0);
                hMac.init(generateDerivedMacParameters);
                loadStore(new MacInputStream(dataInputStream, hMac));
                byte[] bArr2 = new byte[hMac.getMacSize()];
                hMac.doFinal(bArr2, NULL);
                bArr = new byte[hMac.getMacSize()];
                dataInputStream.readFully(bArr);
                if (!Arrays.constantTimeAreEqual(bArr2, bArr)) {
                    this.table.clear();
                    throw new IOException("KeyStore integrity check failed.");
                }
                return;
            }
            throw new IOException("Wrong version of key store.");
        }
    }

    public void engineSetCertificateEntry(String str, Certificate certificate) {
        StoreEntry storeEntry = (StoreEntry) this.table.get(str);
        if (storeEntry == null || storeEntry.getType() == KEY_PUBLIC) {
            this.table.put(str, new StoreEntry(str, certificate));
            return;
        }
        throw new KeyStoreException("key store already has a key entry with alias " + str);
    }

    public void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) {
        if ((key instanceof PrivateKey) && certificateArr == null) {
            throw new KeyStoreException("no certificate chain for private key");
        }
        try {
            this.table.put(str, new StoreEntry(str, key, cArr, certificateArr));
        } catch (Exception e) {
            throw new KeyStoreException(e.toString());
        }
    }

    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) {
        this.table.put(str, new StoreEntry(str, bArr, certificateArr));
    }

    public int engineSize() {
        return this.table.size();
    }

    public void engineStore(OutputStream outputStream, char[] cArr) {
        OutputStream dataOutputStream = new DataOutputStream(outputStream);
        byte[] bArr = new byte[STORE_SALT_SIZE];
        int nextInt = (this.random.nextInt() & Place.TYPE_SUBLOCALITY_LEVEL_1) + MIN_ITERATIONS;
        this.random.nextBytes(bArr);
        dataOutputStream.writeInt(this.version);
        dataOutputStream.writeInt(bArr.length);
        dataOutputStream.write(bArr);
        dataOutputStream.writeInt(nextInt);
        HMac hMac = new HMac(new SHA1Digest());
        OutputStream macOutputStream = new MacOutputStream(hMac);
        PBEParametersGenerator pKCS12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
        byte[] PKCS12PasswordToBytes = PBEParametersGenerator.PKCS12PasswordToBytes(cArr);
        pKCS12ParametersGenerator.init(PKCS12PasswordToBytes, bArr, nextInt);
        if (this.version < STORE_VERSION) {
            hMac.init(pKCS12ParametersGenerator.generateDerivedMacParameters(hMac.getMacSize()));
        } else {
            hMac.init(pKCS12ParametersGenerator.generateDerivedMacParameters(hMac.getMacSize() * 8));
        }
        for (int i = NULL; i != PKCS12PasswordToBytes.length; i += KEY_PUBLIC) {
            PKCS12PasswordToBytes[i] = (byte) 0;
        }
        saveStore(new TeeOutputStream(dataOutputStream, macOutputStream));
        bArr = new byte[hMac.getMacSize()];
        hMac.doFinal(bArr, NULL);
        dataOutputStream.write(bArr);
        dataOutputStream.close();
    }

    protected void loadStore(InputStream inputStream) {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        for (int read = dataInputStream.read(); read > 0; read = dataInputStream.read()) {
            String readUTF = dataInputStream.readUTF();
            Date date = new Date(dataInputStream.readLong());
            int readInt = dataInputStream.readInt();
            Certificate[] certificateArr = null;
            if (readInt != 0) {
                certificateArr = new Certificate[readInt];
                for (int i = NULL; i != readInt; i += KEY_PUBLIC) {
                    certificateArr[i] = decodeCertificate(dataInputStream);
                }
            }
            switch (read) {
                case KEY_PUBLIC /*1*/:
                    this.table.put(readUTF, new StoreEntry(readUTF, date, (int) KEY_PUBLIC, decodeCertificate(dataInputStream)));
                    break;
                case STORE_VERSION /*2*/:
                    this.table.put(readUTF, new StoreEntry(readUTF, date, STORE_VERSION, decodeKey(dataInputStream), certificateArr));
                    break;
                case SECRET /*3*/:
                case SEALED /*4*/:
                    Object obj = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(obj);
                    this.table.put(readUTF, new StoreEntry(readUTF, date, read, obj, certificateArr));
                    break;
                default:
                    throw new RuntimeException("Unknown object type in store.");
            }
        }
    }

    protected Cipher makePBECipher(String str, int i, char[] cArr, byte[] bArr, int i2) {
        try {
            KeySpec pBEKeySpec = new PBEKeySpec(cArr);
            SecretKeyFactory createSecretKeyFactory = this.helper.createSecretKeyFactory(str);
            AlgorithmParameterSpec pBEParameterSpec = new PBEParameterSpec(bArr, i2);
            Cipher createCipher = this.helper.createCipher(str);
            createCipher.init(i, createSecretKeyFactory.generateSecret(pBEKeySpec), pBEParameterSpec);
            return createCipher;
        } catch (Exception e) {
            throw new IOException("Error initialising store of key store: " + e);
        }
    }

    protected void saveStore(OutputStream outputStream) {
        Enumeration elements = this.table.elements();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        while (elements.hasMoreElements()) {
            StoreEntry storeEntry = (StoreEntry) elements.nextElement();
            dataOutputStream.write(storeEntry.getType());
            dataOutputStream.writeUTF(storeEntry.getAlias());
            dataOutputStream.writeLong(storeEntry.getDate().getTime());
            Certificate[] certificateChain = storeEntry.getCertificateChain();
            if (certificateChain == null) {
                dataOutputStream.writeInt(NULL);
            } else {
                dataOutputStream.writeInt(certificateChain.length);
                for (int i = NULL; i != certificateChain.length; i += KEY_PUBLIC) {
                    encodeCertificate(certificateChain[i], dataOutputStream);
                }
            }
            switch (storeEntry.getType()) {
                case KEY_PUBLIC /*1*/:
                    encodeCertificate((Certificate) storeEntry.getObject(), dataOutputStream);
                    break;
                case STORE_VERSION /*2*/:
                    encodeKey((Key) storeEntry.getObject(), dataOutputStream);
                    break;
                case SECRET /*3*/:
                case SEALED /*4*/:
                    byte[] bArr = (byte[]) storeEntry.getObject();
                    dataOutputStream.writeInt(bArr.length);
                    dataOutputStream.write(bArr);
                    break;
                default:
                    throw new RuntimeException("Unknown object type in store.");
            }
        }
        dataOutputStream.write(NULL);
    }

    public void setRandom(SecureRandom secureRandom) {
        this.random = secureRandom;
    }
}
