/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.ByteArrayOutputStream
 *  java.io.DataInputStream
 *  java.io.DataOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.OutputStream
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.security.Key
 *  java.security.KeyFactory
 *  java.security.KeyStoreException
 *  java.security.KeyStoreSpi
 *  java.security.NoSuchProviderException
 *  java.security.PrivateKey
 *  java.security.Provider
 *  java.security.PublicKey
 *  java.security.SecureRandom
 *  java.security.Security
 *  java.security.UnrecoverableKeyException
 *  java.security.cert.Certificate
 *  java.security.cert.CertificateEncodingException
 *  java.security.cert.CertificateException
 *  java.security.cert.CertificateFactory
 *  java.security.spec.AlgorithmParameterSpec
 *  java.security.spec.KeySpec
 *  java.security.spec.PKCS8EncodedKeySpec
 *  java.security.spec.X509EncodedKeySpec
 *  java.util.Date
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  javax.crypto.Cipher
 *  javax.crypto.CipherInputStream
 *  javax.crypto.CipherOutputStream
 *  javax.crypto.SecretKey
 *  javax.crypto.SecretKeyFactory
 *  javax.crypto.spec.PBEKeySpec
 *  javax.crypto.spec.PBEParameterSpec
 *  javax.crypto.spec.SecretKeySpec
 */
package org.bouncycastle.jcajce.provider.keystore.bc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
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
import java.security.cert.CertificateFactory;
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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
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

public class BcKeyStoreSpi
extends KeyStoreSpi
implements BCKeyStore {
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
    private final JcaJceHelper helper = new BCJcaJceHelper();
    protected SecureRandom random = new SecureRandom();
    protected Hashtable table = new Hashtable();
    protected int version;

    public BcKeyStoreSpi(int n) {
        this.version = n;
    }

    static /* synthetic */ Key access$100(BcKeyStoreSpi bcKeyStoreSpi, DataInputStream dataInputStream) {
        return bcKeyStoreSpi.decodeKey(dataInputStream);
    }

    private Certificate decodeCertificate(DataInputStream dataInputStream) {
        String string = dataInputStream.readUTF();
        byte[] arrby = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(arrby);
        try {
            Certificate certificate = this.helper.createCertificateFactory(string).generateCertificate((InputStream)new ByteArrayInputStream(arrby));
            return certificate;
        }
        catch (NoSuchProviderException noSuchProviderException) {
            throw new IOException(noSuchProviderException.toString());
        }
        catch (CertificateException certificateException) {
            throw new IOException(certificateException.toString());
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private Key decodeKey(DataInputStream dataInputStream) {
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec;
        int n = dataInputStream.read();
        String string = dataInputStream.readUTF();
        String string2 = dataInputStream.readUTF();
        byte[] arrby = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(arrby);
        if (string.equals((Object)"PKCS#8") || string.equals((Object)"PKCS8")) {
            pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(arrby);
        } else {
            if (!string.equals((Object)"X.509") && !string.equals((Object)"X509")) {
                if (!string.equals((Object)"RAW")) throw new IOException("Key format " + string + " not recognised!");
                return new SecretKeySpec(arrby, string2);
            }
            pKCS8EncodedKeySpec = new X509EncodedKeySpec(arrby);
        }
        switch (n) {
            default: {
                throw new IOException("Key type " + n + " not recognised!");
            }
            case 0: {
                try {
                    return this.helper.createKeyFactory(string2).generatePrivate((KeySpec)pKCS8EncodedKeySpec);
                }
                catch (Exception exception) {
                    throw new IOException("Exception creating key: " + exception.toString());
                }
            }
            case 1: {
                return this.helper.createKeyFactory(string2).generatePublic((KeySpec)pKCS8EncodedKeySpec);
            }
            case 2: 
        }
        return this.helper.createSecretKeyFactory(string2).generateSecret((KeySpec)pKCS8EncodedKeySpec);
    }

    private void encodeCertificate(Certificate certificate, DataOutputStream dataOutputStream) {
        try {
            byte[] arrby = certificate.getEncoded();
            dataOutputStream.writeUTF(certificate.getType());
            dataOutputStream.writeInt(arrby.length);
            dataOutputStream.write(arrby);
            return;
        }
        catch (CertificateEncodingException certificateEncodingException) {
            throw new IOException(certificateEncodingException.toString());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void encodeKey(Key key, DataOutputStream dataOutputStream) {
        byte[] arrby = key.getEncoded();
        if (key instanceof PrivateKey) {
            dataOutputStream.write(0);
        } else if (key instanceof PublicKey) {
            dataOutputStream.write(1);
        } else {
            dataOutputStream.write(2);
        }
        dataOutputStream.writeUTF(key.getFormat());
        dataOutputStream.writeUTF(key.getAlgorithm());
        dataOutputStream.writeInt(arrby.length);
        dataOutputStream.write(arrby);
    }

    static Provider getBouncyCastleProvider() {
        if (Security.getProvider((String)"BC") != null) {
            return Security.getProvider((String)"BC");
        }
        return new BouncyCastleProvider();
    }

    public Enumeration engineAliases() {
        return this.table.keys();
    }

    public boolean engineContainsAlias(String string) {
        return this.table.get((Object)string) != null;
    }

    public void engineDeleteEntry(String string) {
        if (this.table.get((Object)string) == null) {
            return;
        }
        this.table.remove((Object)string);
    }

    public Certificate engineGetCertificate(String string) {
        StoreEntry storeEntry = (StoreEntry)this.table.get((Object)string);
        if (storeEntry != null) {
            if (storeEntry.getType() == 1) {
                return (Certificate)storeEntry.getObject();
            }
            Certificate[] arrcertificate = storeEntry.getCertificateChain();
            if (arrcertificate != null) {
                return arrcertificate[0];
            }
        }
        return null;
    }

    public String engineGetCertificateAlias(Certificate certificate) {
        Enumeration enumeration = this.table.elements();
        while (enumeration.hasMoreElements()) {
            Certificate[] arrcertificate;
            StoreEntry storeEntry = (StoreEntry)enumeration.nextElement();
            if (!(storeEntry.getObject() instanceof Certificate ? ((Certificate)storeEntry.getObject()).equals((Object)certificate) : (arrcertificate = storeEntry.getCertificateChain()) != null && arrcertificate[0].equals((Object)certificate))) continue;
            return storeEntry.getAlias();
        }
        return null;
    }

    public Certificate[] engineGetCertificateChain(String string) {
        StoreEntry storeEntry = (StoreEntry)this.table.get((Object)string);
        if (storeEntry != null) {
            return storeEntry.getCertificateChain();
        }
        return null;
    }

    public Date engineGetCreationDate(String string) {
        StoreEntry storeEntry = (StoreEntry)this.table.get((Object)string);
        if (storeEntry != null) {
            return storeEntry.getDate();
        }
        return null;
    }

    public Key engineGetKey(String string, char[] arrc) {
        StoreEntry storeEntry = (StoreEntry)this.table.get((Object)string);
        if (storeEntry == null || storeEntry.getType() == 1) {
            return null;
        }
        return (Key)storeEntry.getObject(arrc);
    }

    public boolean engineIsCertificateEntry(String string) {
        StoreEntry storeEntry = (StoreEntry)this.table.get((Object)string);
        return storeEntry != null && storeEntry.getType() == 1;
    }

    public boolean engineIsKeyEntry(String string) {
        StoreEntry storeEntry = (StoreEntry)this.table.get((Object)string);
        return storeEntry != null && storeEntry.getType() != 1;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void engineLoad(InputStream inputStream, char[] arrc) {
        HMac hMac;
        DataInputStream dataInputStream;
        block6 : {
            block7 : {
                block5 : {
                    this.table.clear();
                    if (inputStream == null) break block5;
                    dataInputStream = new DataInputStream(inputStream);
                    int n = dataInputStream.readInt();
                    if (n != 2 && n != 0 && n != 1) {
                        throw new IOException("Wrong version of key store.");
                    }
                    int n2 = dataInputStream.readInt();
                    if (n2 <= 0) {
                        throw new IOException("Invalid salt detected");
                    }
                    byte[] arrby = new byte[n2];
                    dataInputStream.readFully(arrby);
                    int n3 = dataInputStream.readInt();
                    hMac = new HMac(new SHA1Digest());
                    if (arrc == null || arrc.length == 0) break block6;
                    byte[] arrby2 = PBEParametersGenerator.PKCS12PasswordToBytes(arrc);
                    PKCS12ParametersGenerator pKCS12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
                    pKCS12ParametersGenerator.init(arrby2, arrby, n3);
                    CipherParameters cipherParameters = n != 2 ? ((PBEParametersGenerator)pKCS12ParametersGenerator).generateDerivedMacParameters(hMac.getMacSize()) : ((PBEParametersGenerator)pKCS12ParametersGenerator).generateDerivedMacParameters(8 * hMac.getMacSize());
                    Arrays.fill(arrby2, (byte)0);
                    hMac.init(cipherParameters);
                    this.loadStore((InputStream)new MacInputStream((InputStream)dataInputStream, hMac));
                    byte[] arrby3 = new byte[hMac.getMacSize()];
                    hMac.doFinal(arrby3, 0);
                    byte[] arrby4 = new byte[hMac.getMacSize()];
                    dataInputStream.readFully(arrby4);
                    if (!Arrays.constantTimeAreEqual(arrby3, arrby4)) break block7;
                }
                return;
            }
            this.table.clear();
            throw new IOException("KeyStore integrity check failed.");
        }
        this.loadStore((InputStream)dataInputStream);
        dataInputStream.readFully(new byte[hMac.getMacSize()]);
    }

    public void engineSetCertificateEntry(String string, Certificate certificate) {
        StoreEntry storeEntry = (StoreEntry)this.table.get((Object)string);
        if (storeEntry != null && storeEntry.getType() != 1) {
            throw new KeyStoreException("key store already has a key entry with alias " + string);
        }
        this.table.put((Object)string, (Object)new StoreEntry(string, certificate));
    }

    public void engineSetKeyEntry(String string, Key key, char[] arrc, Certificate[] arrcertificate) {
        if (key instanceof PrivateKey && arrcertificate == null) {
            throw new KeyStoreException("no certificate chain for private key");
        }
        try {
            this.table.put((Object)string, (Object)new StoreEntry(string, key, arrc, arrcertificate));
            return;
        }
        catch (Exception exception) {
            throw new KeyStoreException(exception.toString());
        }
    }

    public void engineSetKeyEntry(String string, byte[] arrby, Certificate[] arrcertificate) {
        this.table.put((Object)string, (Object)new StoreEntry(string, arrby, arrcertificate));
    }

    public int engineSize() {
        return this.table.size();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void engineStore(OutputStream outputStream, char[] arrc) {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        byte[] arrby = new byte[20];
        int n = 1024 + (1023 & this.random.nextInt());
        this.random.nextBytes(arrby);
        dataOutputStream.writeInt(this.version);
        dataOutputStream.writeInt(arrby.length);
        dataOutputStream.write(arrby);
        dataOutputStream.writeInt(n);
        HMac hMac = new HMac(new SHA1Digest());
        MacOutputStream macOutputStream = new MacOutputStream(hMac);
        PKCS12ParametersGenerator pKCS12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
        byte[] arrby2 = PBEParametersGenerator.PKCS12PasswordToBytes(arrc);
        pKCS12ParametersGenerator.init(arrby2, arrby, n);
        if (this.version < 2) {
            hMac.init(((PBEParametersGenerator)pKCS12ParametersGenerator).generateDerivedMacParameters(hMac.getMacSize()));
        } else {
            hMac.init(((PBEParametersGenerator)pKCS12ParametersGenerator).generateDerivedMacParameters(8 * hMac.getMacSize()));
        }
        int n2 = 0;
        do {
            if (n2 == arrby2.length) {
                this.saveStore(new TeeOutputStream((OutputStream)dataOutputStream, macOutputStream));
                byte[] arrby3 = new byte[hMac.getMacSize()];
                hMac.doFinal(arrby3, 0);
                dataOutputStream.write(arrby3);
                dataOutputStream.close();
                return;
            }
            arrby2[n2] = 0;
            ++n2;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void loadStore(InputStream inputStream) {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int n = dataInputStream.read();
        while (n > 0) {
            String string = dataInputStream.readUTF();
            Date date = new Date(dataInputStream.readLong());
            int n2 = dataInputStream.readInt();
            Certificate[] arrcertificate = null;
            if (n2 != 0) {
                arrcertificate = new Certificate[n2];
                for (int i = 0; i != n2; ++i) {
                    arrcertificate[i] = this.decodeCertificate(dataInputStream);
                }
            }
            switch (n) {
                default: {
                    throw new RuntimeException("Unknown object type in store.");
                }
                case 1: {
                    Certificate certificate = this.decodeCertificate(dataInputStream);
                    this.table.put((Object)string, (Object)new StoreEntry(string, date, 1, (Object)certificate));
                    break;
                }
                case 2: {
                    Key key = this.decodeKey(dataInputStream);
                    this.table.put((Object)string, (Object)new StoreEntry(string, date, 2, (Object)key, arrcertificate));
                    break;
                }
                case 3: 
                case 4: {
                    byte[] arrby = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(arrby);
                    this.table.put((Object)string, (Object)new StoreEntry(string, date, n, arrby, arrcertificate));
                }
            }
            n = dataInputStream.read();
        }
        return;
    }

    protected Cipher makePBECipher(String string, int n, char[] arrc, byte[] arrby, int n2) {
        try {
            PBEKeySpec pBEKeySpec = new PBEKeySpec(arrc);
            SecretKeyFactory secretKeyFactory = this.helper.createSecretKeyFactory(string);
            PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(arrby, n2);
            Cipher cipher = this.helper.createCipher(string);
            cipher.init(n, (Key)secretKeyFactory.generateSecret((KeySpec)pBEKeySpec), (AlgorithmParameterSpec)pBEParameterSpec);
            return cipher;
        }
        catch (Exception exception) {
            throw new IOException("Error initialising store of key store: " + (Object)((Object)exception));
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void saveStore(OutputStream outputStream) {
        Enumeration enumeration = this.table.elements();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        block5 : do {
            if (!enumeration.hasMoreElements()) {
                dataOutputStream.write(0);
                return;
            }
            StoreEntry storeEntry = (StoreEntry)enumeration.nextElement();
            dataOutputStream.write(storeEntry.getType());
            dataOutputStream.writeUTF(storeEntry.getAlias());
            dataOutputStream.writeLong(storeEntry.getDate().getTime());
            Certificate[] arrcertificate = storeEntry.getCertificateChain();
            if (arrcertificate == null) {
                dataOutputStream.writeInt(0);
            } else {
                dataOutputStream.writeInt(arrcertificate.length);
                for (int i = 0; i != arrcertificate.length; ++i) {
                    this.encodeCertificate(arrcertificate[i], dataOutputStream);
                }
            }
            switch (storeEntry.getType()) {
                default: {
                    throw new RuntimeException("Unknown object type in store.");
                }
                case 1: {
                    this.encodeCertificate((Certificate)storeEntry.getObject(), dataOutputStream);
                    continue block5;
                }
                case 2: {
                    this.encodeKey((Key)storeEntry.getObject(), dataOutputStream);
                    continue block5;
                }
                case 3: 
                case 4: 
            }
            byte[] arrby = (byte[])storeEntry.getObject();
            dataOutputStream.writeInt(arrby.length);
            dataOutputStream.write(arrby);
        } while (true);
    }

    @Override
    public void setRandom(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    public static class BouncyCastleStore
    extends BcKeyStoreSpi {
        public BouncyCastleStore() {
            super(1);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void engineLoad(InputStream inputStream, char[] arrc) {
            block7 : {
                block6 : {
                    this.table.clear();
                    if (inputStream == null) break block6;
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    int n = dataInputStream.readInt();
                    if (n != 2 && n != 0 && n != 1) {
                        throw new IOException("Wrong version of key store.");
                    }
                    byte[] arrby = new byte[dataInputStream.readInt()];
                    if (arrby.length != 20) {
                        throw new IOException("Key store corrupted.");
                    }
                    dataInputStream.readFully(arrby);
                    int n2 = dataInputStream.readInt();
                    if (n2 < 0 || n2 > 4096) {
                        throw new IOException("Key store corrupted.");
                    }
                    String string = n == 0 ? "OldPBEWithSHAAndTwofish-CBC" : BcKeyStoreSpi.STORE_CIPHER;
                    CipherInputStream cipherInputStream = new CipherInputStream((InputStream)dataInputStream, this.makePBECipher(string, 2, arrc, arrby, n2));
                    SHA1Digest sHA1Digest = new SHA1Digest();
                    this.loadStore((InputStream)new DigestInputStream((InputStream)cipherInputStream, sHA1Digest));
                    byte[] arrby2 = new byte[sHA1Digest.getDigestSize()];
                    sHA1Digest.doFinal(arrby2, 0);
                    byte[] arrby3 = new byte[sHA1Digest.getDigestSize()];
                    Streams.readFully((InputStream)cipherInputStream, arrby3);
                    if (!Arrays.constantTimeAreEqual(arrby2, arrby3)) break block7;
                }
                return;
            }
            this.table.clear();
            throw new IOException("KeyStore integrity check failed.");
        }

        @Override
        public void engineStore(OutputStream outputStream, char[] arrc) {
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            byte[] arrby = new byte[20];
            int n = 1024 + (1023 & this.random.nextInt());
            this.random.nextBytes(arrby);
            dataOutputStream.writeInt(this.version);
            dataOutputStream.writeInt(arrby.length);
            dataOutputStream.write(arrby);
            dataOutputStream.writeInt(n);
            CipherOutputStream cipherOutputStream = new CipherOutputStream((OutputStream)dataOutputStream, this.makePBECipher(BcKeyStoreSpi.STORE_CIPHER, 1, arrc, arrby, n));
            DigestOutputStream digestOutputStream = new DigestOutputStream(new SHA1Digest());
            this.saveStore(new TeeOutputStream((OutputStream)cipherOutputStream, digestOutputStream));
            cipherOutputStream.write(digestOutputStream.getDigest());
            cipherOutputStream.close();
        }
    }

    public static class Std
    extends BcKeyStoreSpi {
        public Std() {
            super(2);
        }
    }

    private class StoreEntry {
        String alias;
        Certificate[] certChain;
        Date date = new Date();
        Object obj;
        int type;

        StoreEntry(String string, Key key, char[] arrc, Certificate[] arrcertificate) {
            this.type = 4;
            this.alias = string;
            this.certChain = arrcertificate;
            byte[] arrby = new byte[20];
            BcKeyStoreSpi.this.random.setSeed(System.currentTimeMillis());
            BcKeyStoreSpi.this.random.nextBytes(arrby);
            int n = 1024 + (1023 & BcKeyStoreSpi.this.random.nextInt());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream((OutputStream)byteArrayOutputStream);
            dataOutputStream.writeInt(arrby.length);
            dataOutputStream.write(arrby);
            dataOutputStream.writeInt(n);
            DataOutputStream dataOutputStream2 = new DataOutputStream((OutputStream)new CipherOutputStream((OutputStream)dataOutputStream, BcKeyStoreSpi.this.makePBECipher(BcKeyStoreSpi.KEY_CIPHER, 1, arrc, arrby, n)));
            BcKeyStoreSpi.this.encodeKey(key, dataOutputStream2);
            dataOutputStream2.close();
            this.obj = byteArrayOutputStream.toByteArray();
        }

        StoreEntry(String string, Certificate certificate) {
            this.type = 1;
            this.alias = string;
            this.obj = certificate;
            this.certChain = null;
        }

        StoreEntry(String string, Date date, int n, Object object) {
            this.alias = string;
            this.date = date;
            this.type = n;
            this.obj = object;
        }

        StoreEntry(String string, Date date, int n, Object object, Certificate[] arrcertificate) {
            this.alias = string;
            this.date = date;
            this.type = n;
            this.obj = object;
            this.certChain = arrcertificate;
        }

        StoreEntry(String string, byte[] arrby, Certificate[] arrcertificate) {
            this.type = 3;
            this.alias = string;
            this.obj = arrby;
            this.certChain = arrcertificate;
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

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        Object getObject(char[] var1_1) {
            if ((var1_1 == null || var1_1.length == 0) && this.obj instanceof Key) {
                return this.obj;
            }
            if (this.type != 4) throw new RuntimeException("forget something!");
            var2_2 = new DataInputStream((InputStream)new ByteArrayInputStream((byte[])this.obj));
            var4_3 = new byte[var2_2.readInt()];
            var2_2.readFully(var4_3);
            var5_4 = var2_2.readInt();
            var6_5 = new CipherInputStream((InputStream)var2_2, BcKeyStoreSpi.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 2, var1_1, var4_3, var5_4));
lbl-1000: // 3 sources:
            {
                catch (Exception var3_20) {
                    throw new UnrecoverableKeyException("no match");
                }
            }
            try {
                return BcKeyStoreSpi.access$100(BcKeyStoreSpi.this, new DataInputStream((InputStream)var6_5));
            }
            catch (Exception var7_7) {
                var8_8 = new DataInputStream((InputStream)new ByteArrayInputStream((byte[])this.obj));
                var9_9 = new byte[var8_8.readInt()];
                var8_8.readFully(var9_9);
                var10_10 = var8_8.readInt();
                var11_11 = new CipherInputStream((InputStream)var8_8, BcKeyStoreSpi.this.makePBECipher("BrokenPBEWithSHAAnd3-KeyTripleDES-CBC", 2, var1_1, var9_9, var10_10));
                var15_13 = var19_12 = BcKeyStoreSpi.access$100(BcKeyStoreSpi.this, new DataInputStream((InputStream)var11_11));
                {
                    catch (Exception var12_17) {
                        var13_18 = new DataInputStream((InputStream)new ByteArrayInputStream((byte[])this.obj));
                        var9_9 = new byte[var13_18.readInt()];
                        var13_18.readFully(var9_9);
                        var10_10 = var13_18.readInt();
                        var14_19 = new CipherInputStream((InputStream)var13_18, BcKeyStoreSpi.this.makePBECipher("OldPBEWithSHAAnd3-KeyTripleDES-CBC", 2, var1_1, var9_9, var10_10));
                        var15_13 = BcKeyStoreSpi.access$100(BcKeyStoreSpi.this, new DataInputStream((InputStream)var14_19));
                    }
                    if (var15_13 == null) throw new UnrecoverableKeyException("no match");
                    ** try [egrp 4[TRYBLOCK] [4 : 227->438)] { 
lbl33: // 1 sources:
                    var16_14 = new ByteArrayOutputStream();
                    var17_15 = new DataOutputStream((OutputStream)var16_14);
                    var17_15.writeInt(var9_9.length);
                    var17_15.write(var9_9);
                    var17_15.writeInt(var10_10);
                    var18_16 = new DataOutputStream((OutputStream)new CipherOutputStream((OutputStream)var17_15, BcKeyStoreSpi.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 1, var1_1, var9_9, var10_10)));
                    BcKeyStoreSpi.access$000(BcKeyStoreSpi.this, var15_13, var18_16);
                    var18_16.close();
                    this.obj = var16_14.toByteArray();
                    return var15_13;
                }
            }
        }

        int getType() {
            return this.type;
        }
    }

    public static class Version1
    extends BcKeyStoreSpi {
        public Version1() {
            super(1);
        }
    }

}

