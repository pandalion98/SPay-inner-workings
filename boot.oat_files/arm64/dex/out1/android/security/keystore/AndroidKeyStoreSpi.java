package android.security.keystore;

import android.security.Credentials;
import android.security.KeyStore;
import android.security.KeyStoreParameter;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyProperties.BlockMode;
import android.security.keystore.KeyProperties.Digest;
import android.security.keystore.KeyProperties.EncryptionPadding;
import android.security.keystore.KeyProperties.KeyAlgorithm;
import android.security.keystore.KeyProperties.Purpose;
import android.security.keystore.KeyProtection.Builder;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import libcore.util.EmptyArray;

public class AndroidKeyStoreSpi extends KeyStoreSpi {
    public static final String NAME = "AndroidKeyStore";
    private KeyStore mKeyStore;

    static class KeyStoreX509Certificate extends DelegatingX509Certificate {
        private final String mPrivateKeyAlias;

        KeyStoreX509Certificate(String privateKeyAlias, X509Certificate delegate) {
            super(delegate);
            this.mPrivateKeyAlias = privateKeyAlias;
        }

        public PublicKey getPublicKey() {
            PublicKey original = super.getPublicKey();
            return AndroidKeyStoreProvider.getAndroidKeyStorePublicKey(this.mPrivateKeyAlias, original.getAlgorithm(), original.getEncoded());
        }
    }

    public Key engineGetKey(String alias, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        if (isPrivateKeyEntry(alias)) {
            return AndroidKeyStoreProvider.loadAndroidKeyStorePrivateKeyFromKeystore(this.mKeyStore, Credentials.USER_PRIVATE_KEY + alias);
        } else if (!isSecretKeyEntry(alias)) {
            return null;
        } else {
            return AndroidKeyStoreProvider.loadAndroidKeyStoreSecretKeyFromKeystore(this.mKeyStore, Credentials.USER_SECRET_KEY + alias);
        }
    }

    public Certificate[] engineGetCertificateChain(String alias) {
        if (alias == null) {
            throw new NullPointerException("alias == null");
        }
        X509Certificate leaf = (X509Certificate) engineGetCertificate(alias);
        if (leaf == null) {
            return null;
        }
        Certificate[] caList;
        byte[] caBytes = this.mKeyStore.get(Credentials.CA_CERTIFICATE + alias);
        if (caBytes != null) {
            Collection<X509Certificate> caChain = toCertificates(caBytes);
            caList = new Certificate[(caChain.size() + 1)];
            int i = 1;
            for (Certificate certificate : caChain) {
                int i2 = i + 1;
                caList[i] = certificate;
                i = i2;
            }
        } else {
            caList = new Certificate[1];
        }
        caList[0] = leaf;
        return caList;
    }

    public Certificate engineGetCertificate(String alias) {
        if (alias == null) {
            throw new NullPointerException("alias == null");
        }
        byte[] encodedCert = this.mKeyStore.get(Credentials.USER_CERTIFICATE + alias);
        if (encodedCert != null) {
            return getCertificateForPrivateKeyEntry(alias, encodedCert);
        }
        encodedCert = this.mKeyStore.get(Credentials.CA_CERTIFICATE + alias);
        if (encodedCert != null) {
            return getCertificateForTrustedCertificateEntry(encodedCert);
        }
        return null;
    }

    private Certificate getCertificateForTrustedCertificateEntry(byte[] encodedCert) {
        return toCertificate(encodedCert);
    }

    private Certificate getCertificateForPrivateKeyEntry(String alias, byte[] encodedCert) {
        X509Certificate cert = toCertificate(encodedCert);
        if (cert == null) {
            return null;
        }
        String privateKeyAlias = Credentials.USER_PRIVATE_KEY + alias;
        if (this.mKeyStore.contains(privateKeyAlias)) {
            return wrapIntoKeyStoreCertificate(privateKeyAlias, cert);
        }
        return cert;
    }

    private static KeyStoreX509Certificate wrapIntoKeyStoreCertificate(String privateKeyAlias, X509Certificate certificate) {
        return certificate != null ? new KeyStoreX509Certificate(privateKeyAlias, certificate) : null;
    }

    private static X509Certificate toCertificate(byte[] bytes) {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bytes));
        } catch (CertificateException e) {
            Log.w("AndroidKeyStore", "Couldn't parse certificate in keystore", e);
            return null;
        }
    }

    private static Collection<X509Certificate> toCertificates(byte[] bytes) {
        try {
            return CertificateFactory.getInstance("X.509").generateCertificates(new ByteArrayInputStream(bytes));
        } catch (CertificateException e) {
            Log.w("AndroidKeyStore", "Couldn't parse certificates in keystore", e);
            return new ArrayList();
        }
    }

    private Date getModificationDate(String alias) {
        long epochMillis = this.mKeyStore.getmtime(alias);
        if (epochMillis == -1) {
            return null;
        }
        return new Date(epochMillis);
    }

    public Date engineGetCreationDate(String alias) {
        if (alias == null) {
            throw new NullPointerException("alias == null");
        }
        Date d = getModificationDate(Credentials.USER_PRIVATE_KEY + alias);
        if (d != null) {
            return d;
        }
        d = getModificationDate(Credentials.USER_SECRET_KEY + alias);
        if (d != null) {
            return d;
        }
        d = getModificationDate(Credentials.USER_CERTIFICATE + alias);
        if (d != null) {
            return d;
        }
        return getModificationDate(Credentials.CA_CERTIFICATE + alias);
    }

    public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain) throws KeyStoreException {
        if (password != null && password.length > 0) {
            throw new KeyStoreException("entries cannot be protected with passwords");
        } else if (key instanceof PrivateKey) {
            setPrivateKeyEntry(alias, (PrivateKey) key, chain, null);
        } else if (key instanceof SecretKey) {
            setSecretKeyEntry(alias, (SecretKey) key, null);
        } else {
            throw new KeyStoreException("Only PrivateKey and SecretKey are supported");
        }
    }

    private static KeyProtection getLegacyKeyProtectionParameter(PrivateKey key) throws KeyStoreException {
        Builder specBuilder;
        String keyAlgorithm = key.getAlgorithm();
        if (KeyProperties.KEY_ALGORITHM_EC.equalsIgnoreCase(keyAlgorithm)) {
            specBuilder = new Builder(12);
            specBuilder.setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512);
        } else if (KeyProperties.KEY_ALGORITHM_RSA.equalsIgnoreCase(keyAlgorithm)) {
            specBuilder = new Builder(15);
            specBuilder.setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512);
            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE, KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1, KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
            specBuilder.setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1, KeyProperties.SIGNATURE_PADDING_RSA_PSS);
            specBuilder.setRandomizedEncryptionRequired(false);
        } else {
            throw new KeyStoreException("Unsupported key algorithm: " + keyAlgorithm);
        }
        specBuilder.setUserAuthenticationRequired(false);
        return specBuilder.build();
    }

    private void setPrivateKeyEntry(String alias, PrivateKey key, Certificate[] chain, ProtectionParameter param) throws KeyStoreException {
        KeyProtection spec;
        RuntimeException e;
        int flags = 0;
        if (param == null) {
            spec = getLegacyKeyProtectionParameter(key);
        } else if (param instanceof KeyStoreParameter) {
            spec = getLegacyKeyProtectionParameter(key);
            if (((KeyStoreParameter) param).isEncryptionRequired()) {
                flags = 1;
            }
        } else if (param instanceof KeyProtection) {
            spec = (KeyProtection) param;
        } else {
            throw new KeyStoreException("Unsupported protection parameter class:" + param.getClass().getName() + ". Supported: " + KeyProtection.class.getName() + ", " + KeyStoreParameter.class.getName());
        }
        if (chain == null || chain.length == 0) {
            throw new KeyStoreException("Must supply at least one Certificate with PrivateKey");
        }
        X509Certificate[] x509chain = new X509Certificate[chain.length];
        int i = 0;
        while (i < chain.length) {
            if (!"X.509".equals(chain[i].getType())) {
                throw new KeyStoreException("Certificates must be in X.509 format: invalid cert #" + i);
            } else if (chain[i] instanceof X509Certificate) {
                x509chain[i] = (X509Certificate) chain[i];
                i++;
            } else {
                throw new KeyStoreException("Certificates must be in X.509 format: invalid cert #" + i);
            }
        }
        try {
            byte[] chainBytes;
            String pkeyAlias;
            boolean shouldReplacePrivateKey;
            KeymasterArguments importArgs;
            byte[] pkcs8EncodedPrivateKeyBytes;
            int errorCode;
            byte[] userCertBytes = x509chain[0].getEncoded();
            if (chain.length > 1) {
                byte[][] certsBytes = new byte[(x509chain.length - 1)][];
                int totalCertLength = 0;
                i = 0;
                while (i < certsBytes.length) {
                    try {
                        certsBytes[i] = x509chain[i + 1].getEncoded();
                        totalCertLength += certsBytes[i].length;
                        i++;
                    } catch (CertificateEncodingException e2) {
                        throw new KeyStoreException("Failed to encode certificate #" + i, e2);
                    }
                }
                chainBytes = new byte[totalCertLength];
                int outputOffset = 0;
                for (i = 0; i < certsBytes.length; i++) {
                    int certLength = certsBytes[i].length;
                    System.arraycopy(certsBytes[i], 0, chainBytes, outputOffset, certLength);
                    outputOffset += certLength;
                    certsBytes[i] = null;
                }
            } else {
                chainBytes = null;
            }
            if (key instanceof AndroidKeyStorePrivateKey) {
                pkeyAlias = ((AndroidKeyStoreKey) key).getAlias();
            } else {
                pkeyAlias = null;
            }
            if (pkeyAlias != null) {
                if (pkeyAlias.startsWith(Credentials.USER_PRIVATE_KEY)) {
                    String keySubalias = pkeyAlias.substring(Credentials.USER_PRIVATE_KEY.length());
                    if (alias.equals(keySubalias)) {
                        shouldReplacePrivateKey = false;
                        importArgs = null;
                        pkcs8EncodedPrivateKeyBytes = null;
                        if (shouldReplacePrivateKey) {
                            Credentials.deleteCertificateTypesForAlias(this.mKeyStore, alias);
                            Credentials.deleteSecretKeyTypeForAlias(this.mKeyStore, alias);
                        } else {
                            try {
                                Credentials.deleteAllTypesForAlias(this.mKeyStore, alias);
                                errorCode = this.mKeyStore.importKey(Credentials.USER_PRIVATE_KEY + alias, importArgs, 1, pkcs8EncodedPrivateKeyBytes, flags, new KeyCharacteristics());
                                if (errorCode != 1) {
                                    throw new KeyStoreException("Failed to store private key", KeyStore.getKeyStoreException(errorCode));
                                }
                            } catch (Throwable th) {
                                if (!false) {
                                    if (shouldReplacePrivateKey) {
                                        Credentials.deleteAllTypesForAlias(this.mKeyStore, alias);
                                    } else {
                                        Credentials.deleteCertificateTypesForAlias(this.mKeyStore, alias);
                                        Credentials.deleteSecretKeyTypeForAlias(this.mKeyStore, alias);
                                    }
                                }
                            }
                        }
                        errorCode = this.mKeyStore.insert(Credentials.USER_CERTIFICATE + alias, userCertBytes, -1, flags);
                        if (errorCode == 1) {
                            throw new KeyStoreException("Failed to store certificate #0", KeyStore.getKeyStoreException(errorCode));
                        }
                        errorCode = this.mKeyStore.insert(Credentials.CA_CERTIFICATE + alias, chainBytes, -1, flags);
                        if (errorCode != 1) {
                            throw new KeyStoreException("Failed to store certificate chain", KeyStore.getKeyStoreException(errorCode));
                        } else if (true) {
                            return;
                        } else {
                            if (shouldReplacePrivateKey) {
                                Credentials.deleteCertificateTypesForAlias(this.mKeyStore, alias);
                                Credentials.deleteSecretKeyTypeForAlias(this.mKeyStore, alias);
                                return;
                            }
                            Credentials.deleteAllTypesForAlias(this.mKeyStore, alias);
                            return;
                        }
                    }
                    throw new KeyStoreException("Can only replace keys with same alias: " + alias + " != " + keySubalias);
                }
            }
            shouldReplacePrivateKey = true;
            String keyFormat = key.getFormat();
            if (keyFormat == null || !"PKCS#8".equals(keyFormat)) {
                throw new KeyStoreException("Unsupported private key export format: " + keyFormat + ". Only private keys which export their key material in PKCS#8 format are" + " supported.");
            }
            pkcs8EncodedPrivateKeyBytes = key.getEncoded();
            if (pkcs8EncodedPrivateKeyBytes == null) {
                throw new KeyStoreException("Private key did not export any key material");
            }
            importArgs = new KeymasterArguments();
            try {
                importArgs.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(key.getAlgorithm()));
                int purposes = spec.getPurposes();
                importArgs.addEnums(KeymasterDefs.KM_TAG_PURPOSE, Purpose.allToKeymaster(purposes));
                if (spec.isDigestsSpecified()) {
                    importArgs.addEnums(KeymasterDefs.KM_TAG_DIGEST, Digest.allToKeymaster(spec.getDigests()));
                }
                importArgs.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, BlockMode.allToKeymaster(spec.getBlockModes()));
                int[] keymasterEncryptionPaddings = EncryptionPadding.allToKeymaster(spec.getEncryptionPaddings());
                if ((purposes & 1) != 0 && spec.isRandomizedEncryptionRequired()) {
                    int[] arr$ = keymasterEncryptionPaddings;
                    int len$ = arr$.length;
                    int i$ = 0;
                    while (i$ < len$) {
                        int keymasterPadding = arr$[i$];
                        if (KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(keymasterPadding)) {
                            i$++;
                        } else {
                            throw new KeyStoreException("Randomized encryption (IND-CPA) required but is violated by encryption padding mode: " + EncryptionPadding.fromKeymaster(keymasterPadding) + ". See KeyProtection documentation.");
                        }
                    }
                }
                importArgs.addEnums(KeymasterDefs.KM_TAG_PADDING, keymasterEncryptionPaddings);
                importArgs.addEnums(KeymasterDefs.KM_TAG_PADDING, SignaturePadding.allToKeymaster(spec.getSignaturePaddings()));
                KeymasterUtils.addUserAuthArgs(importArgs, spec.isUserAuthenticationRequired(), spec.getUserAuthenticationValidityDurationSeconds());
                importArgs.addDateIfNotNull(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, spec.getKeyValidityStart());
                importArgs.addDateIfNotNull(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, spec.getKeyValidityForOriginationEnd());
                importArgs.addDateIfNotNull(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, spec.getKeyValidityForConsumptionEnd());
                if (shouldReplacePrivateKey) {
                    Credentials.deleteCertificateTypesForAlias(this.mKeyStore, alias);
                    Credentials.deleteSecretKeyTypeForAlias(this.mKeyStore, alias);
                } else {
                    Credentials.deleteAllTypesForAlias(this.mKeyStore, alias);
                    errorCode = this.mKeyStore.importKey(Credentials.USER_PRIVATE_KEY + alias, importArgs, 1, pkcs8EncodedPrivateKeyBytes, flags, new KeyCharacteristics());
                    if (errorCode != 1) {
                        throw new KeyStoreException("Failed to store private key", KeyStore.getKeyStoreException(errorCode));
                    }
                }
                errorCode = this.mKeyStore.insert(Credentials.USER_CERTIFICATE + alias, userCertBytes, -1, flags);
                if (errorCode == 1) {
                    errorCode = this.mKeyStore.insert(Credentials.CA_CERTIFICATE + alias, chainBytes, -1, flags);
                    if (errorCode != 1) {
                        throw new KeyStoreException("Failed to store certificate chain", KeyStore.getKeyStoreException(errorCode));
                    } else if (true) {
                        if (shouldReplacePrivateKey) {
                            Credentials.deleteCertificateTypesForAlias(this.mKeyStore, alias);
                            Credentials.deleteSecretKeyTypeForAlias(this.mKeyStore, alias);
                            return;
                        }
                        Credentials.deleteAllTypesForAlias(this.mKeyStore, alias);
                        return;
                    } else {
                        return;
                    }
                }
                throw new KeyStoreException("Failed to store certificate #0", KeyStore.getKeyStoreException(errorCode));
            } catch (IllegalArgumentException e3) {
                e = e3;
                throw new KeyStoreException(e);
            } catch (IllegalStateException e4) {
                e = e4;
                throw new KeyStoreException(e);
            }
        } catch (CertificateEncodingException e22) {
            throw new KeyStoreException("Failed to encode certificate #0", e22);
        }
    }

    private void setSecretKeyEntry(String entryAlias, SecretKey key, ProtectionParameter param) throws KeyStoreException {
        RuntimeException e;
        if (param == null || (param instanceof KeyProtection)) {
            KeyProtection params = (KeyProtection) param;
            if (key instanceof AndroidKeyStoreSecretKey) {
                String keyAliasInKeystore = ((AndroidKeyStoreSecretKey) key).getAlias();
                if (keyAliasInKeystore == null) {
                    throw new KeyStoreException("KeyStore-backed secret key does not have an alias");
                } else if (keyAliasInKeystore.startsWith(Credentials.USER_SECRET_KEY)) {
                    String keyEntryAlias = keyAliasInKeystore.substring(Credentials.USER_SECRET_KEY.length());
                    if (!entryAlias.equals(keyEntryAlias)) {
                        throw new KeyStoreException("Can only replace KeyStore-backed keys with same alias: " + entryAlias + " != " + keyEntryAlias);
                    } else if (params != null) {
                        throw new KeyStoreException("Modifying KeyStore-backed key using protection parameters not supported");
                    } else {
                        return;
                    }
                } else {
                    throw new KeyStoreException("KeyStore-backed secret key has invalid alias: " + keyAliasInKeystore);
                }
            } else if (params == null) {
                throw new KeyStoreException("Protection parameters must be specified when importing a symmetric key");
            } else {
                String keyExportFormat = key.getFormat();
                if (keyExportFormat == null) {
                    throw new KeyStoreException("Only secret keys that export their key material are supported");
                } else if ("RAW".equals(keyExportFormat)) {
                    byte[] keyMaterial = key.getEncoded();
                    if (keyMaterial == null) {
                        throw new KeyStoreException("Key did not export its key material despite supporting RAW format export");
                    }
                    KeymasterArguments args = new KeymasterArguments();
                    try {
                        int[] keymasterDigests;
                        int keymasterAlgorithm = KeyAlgorithm.toKeymasterSecretKeyAlgorithm(key.getAlgorithm());
                        args.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, keymasterAlgorithm);
                        if (keymasterAlgorithm == 128) {
                            int keymasterImpliedDigest = KeyAlgorithm.toKeymasterDigest(key.getAlgorithm());
                            if (keymasterImpliedDigest == -1) {
                                throw new ProviderException("HMAC key algorithm digest unknown for key algorithm " + key.getAlgorithm());
                            }
                            keymasterDigests = new int[]{keymasterImpliedDigest};
                            if (params.isDigestsSpecified()) {
                                int[] keymasterDigestsFromParams = Digest.allToKeymaster(params.getDigests());
                                if (!(keymasterDigestsFromParams.length == 1 && keymasterDigestsFromParams[0] == keymasterImpliedDigest)) {
                                    throw new KeyStoreException("Unsupported digests specification: " + Arrays.asList(params.getDigests()) + ". Only " + Digest.fromKeymaster(keymasterImpliedDigest) + " supported for HMAC key algorithm " + key.getAlgorithm());
                                }
                            }
                        } else if (params.isDigestsSpecified()) {
                            keymasterDigests = Digest.allToKeymaster(params.getDigests());
                        } else {
                            keymasterDigests = EmptyArray.INT;
                        }
                        args.addEnums(KeymasterDefs.KM_TAG_DIGEST, keymasterDigests);
                        int purposes = params.getPurposes();
                        int[] keymasterBlockModes = BlockMode.allToKeymaster(params.getBlockModes());
                        if ((purposes & 1) != 0 && params.isRandomizedEncryptionRequired()) {
                            int[] arr$ = keymasterBlockModes;
                            int len$ = arr$.length;
                            int i$ = 0;
                            while (i$ < len$) {
                                int keymasterBlockMode = arr$[i$];
                                if (KeymasterUtils.isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(keymasterBlockMode)) {
                                    i$++;
                                } else {
                                    throw new KeyStoreException("Randomized encryption (IND-CPA) required but may be violated by block mode: " + BlockMode.fromKeymaster(keymasterBlockMode) + ". See KeyProtection documentation.");
                                }
                            }
                        }
                        args.addEnums(KeymasterDefs.KM_TAG_PURPOSE, Purpose.allToKeymaster(purposes));
                        args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, keymasterBlockModes);
                        if (params.getSignaturePaddings().length > 0) {
                            throw new KeyStoreException("Signature paddings not supported for symmetric keys");
                        }
                        args.addEnums(KeymasterDefs.KM_TAG_PADDING, EncryptionPadding.allToKeymaster(params.getEncryptionPaddings()));
                        KeymasterUtils.addUserAuthArgs(args, params.isUserAuthenticationRequired(), params.getUserAuthenticationValidityDurationSeconds());
                        KeymasterUtils.addMinMacLengthAuthorizationIfNecessary(args, keymasterAlgorithm, keymasterBlockModes, keymasterDigests);
                        args.addDateIfNotNull(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, params.getKeyValidityStart());
                        args.addDateIfNotNull(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, params.getKeyValidityForOriginationEnd());
                        args.addDateIfNotNull(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, params.getKeyValidityForConsumptionEnd());
                        if (!((purposes & 1) == 0 || params.isRandomizedEncryptionRequired())) {
                            args.addBoolean(KeymasterDefs.KM_TAG_CALLER_NONCE);
                        }
                        Credentials.deleteAllTypesForAlias(this.mKeyStore, entryAlias);
                        int errorCode = this.mKeyStore.importKey(Credentials.USER_SECRET_KEY + entryAlias, args, 3, keyMaterial, 0, new KeyCharacteristics());
                        if (errorCode != 1) {
                            throw new KeyStoreException("Failed to import secret key. Keystore error code: " + errorCode);
                        }
                        return;
                    } catch (IllegalArgumentException e2) {
                        e = e2;
                        throw new KeyStoreException(e);
                    } catch (IllegalStateException e3) {
                        e = e3;
                        throw new KeyStoreException(e);
                    }
                } else {
                    throw new KeyStoreException("Unsupported secret key material export format: " + keyExportFormat);
                }
            }
        }
        throw new KeyStoreException("Unsupported protection parameter class: " + param.getClass().getName() + ". Supported: " + KeyProtection.class.getName());
    }

    public void engineSetKeyEntry(String alias, byte[] userKey, Certificate[] chain) throws KeyStoreException {
        throw new KeyStoreException("Operation not supported because key encoding is unknown");
    }

    public void engineSetCertificateEntry(String alias, Certificate cert) throws KeyStoreException {
        if (isKeyEntry(alias)) {
            throw new KeyStoreException("Entry exists and is not a trusted certificate");
        } else if (cert == null) {
            throw new NullPointerException("cert == null");
        } else {
            try {
                if (!this.mKeyStore.put(Credentials.CA_CERTIFICATE + alias, cert.getEncoded(), -1, 0)) {
                    throw new KeyStoreException("Couldn't insert certificate; is KeyStore initialized?");
                }
            } catch (CertificateEncodingException e) {
                throw new KeyStoreException(e);
            }
        }
    }

    public void engineDeleteEntry(String alias) throws KeyStoreException {
        if (engineContainsAlias(alias) && !Credentials.deleteAllTypesForAlias(this.mKeyStore, alias)) {
            throw new KeyStoreException("Failed to delete entry: " + alias);
        }
    }

    private Set<String> getUniqueAliases() {
        String[] rawAliases = this.mKeyStore.list("");
        if (rawAliases == null) {
            return new HashSet();
        }
        Set<String> aliases = new HashSet(rawAliases.length);
        for (String alias : rawAliases) {
            int idx = alias.indexOf(95);
            if (idx == -1 || alias.length() <= idx) {
                Log.e("AndroidKeyStore", "invalid alias: " + alias);
            } else {
                aliases.add(new String(alias.substring(idx + 1)));
            }
        }
        return aliases;
    }

    public Enumeration<String> engineAliases() {
        return Collections.enumeration(getUniqueAliases());
    }

    public boolean engineContainsAlias(String alias) {
        if (alias != null) {
            return this.mKeyStore.contains(new StringBuilder().append(Credentials.USER_PRIVATE_KEY).append(alias).toString()) || this.mKeyStore.contains(Credentials.USER_SECRET_KEY + alias) || this.mKeyStore.contains(Credentials.USER_CERTIFICATE + alias) || this.mKeyStore.contains(Credentials.CA_CERTIFICATE + alias);
        } else {
            throw new NullPointerException("alias == null");
        }
    }

    public int engineSize() {
        return getUniqueAliases().size();
    }

    public boolean engineIsKeyEntry(String alias) {
        return isKeyEntry(alias);
    }

    private boolean isKeyEntry(String alias) {
        return isPrivateKeyEntry(alias) || isSecretKeyEntry(alias);
    }

    private boolean isPrivateKeyEntry(String alias) {
        if (alias != null) {
            return this.mKeyStore.contains(Credentials.USER_PRIVATE_KEY + alias);
        }
        throw new NullPointerException("alias == null");
    }

    private boolean isSecretKeyEntry(String alias) {
        if (alias != null) {
            return this.mKeyStore.contains(Credentials.USER_SECRET_KEY + alias);
        }
        throw new NullPointerException("alias == null");
    }

    private boolean isCertificateEntry(String alias) {
        if (alias != null) {
            return this.mKeyStore.contains(Credentials.CA_CERTIFICATE + alias);
        }
        throw new NullPointerException("alias == null");
    }

    public boolean engineIsCertificateEntry(String alias) {
        return !isKeyEntry(alias) && isCertificateEntry(alias);
    }

    public String engineGetCertificateAlias(Certificate cert) {
        if (cert == null) {
            return null;
        }
        if (!"X.509".equalsIgnoreCase(cert.getType())) {
            return null;
        }
        try {
            byte[] targetCertBytes = cert.getEncoded();
            if (targetCertBytes == null) {
                return null;
            }
            byte[] certBytes;
            Set<String> nonCaEntries = new HashSet();
            String[] certAliases = this.mKeyStore.list(Credentials.USER_CERTIFICATE);
            if (certAliases != null) {
                for (String alias : certAliases) {
                    certBytes = this.mKeyStore.get(Credentials.USER_CERTIFICATE + alias);
                    if (certBytes != null) {
                        nonCaEntries.add(alias);
                        if (Arrays.equals(certBytes, targetCertBytes)) {
                            return alias;
                        }
                    }
                }
            }
            String[] caAliases = this.mKeyStore.list(Credentials.CA_CERTIFICATE);
            if (certAliases != null) {
                for (String alias2 : caAliases) {
                    if (!nonCaEntries.contains(alias2)) {
                        certBytes = this.mKeyStore.get(Credentials.CA_CERTIFICATE + alias2);
                        if (certBytes != null && Arrays.equals(certBytes, targetCertBytes)) {
                            return alias2;
                        }
                    }
                }
            }
            return null;
        } catch (CertificateEncodingException e) {
            return null;
        }
    }

    public void engineStore(OutputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        throw new UnsupportedOperationException("Can not serialize AndroidKeyStore to OutputStream");
    }

    public void engineLoad(InputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        if (stream != null) {
            throw new IllegalArgumentException("InputStream not supported");
        } else if (password != null) {
            throw new IllegalArgumentException("password not supported");
        } else {
            this.mKeyStore = KeyStore.getInstance();
        }
    }

    public void engineSetEntry(String alias, Entry entry, ProtectionParameter param) throws KeyStoreException {
        if (entry == null) {
            throw new KeyStoreException("entry == null");
        }
        Credentials.deleteAllTypesForAlias(this.mKeyStore, alias);
        if (entry instanceof TrustedCertificateEntry) {
            engineSetCertificateEntry(alias, ((TrustedCertificateEntry) entry).getTrustedCertificate());
        } else if (entry instanceof PrivateKeyEntry) {
            PrivateKeyEntry prE = (PrivateKeyEntry) entry;
            setPrivateKeyEntry(alias, prE.getPrivateKey(), prE.getCertificateChain(), param);
        } else if (entry instanceof SecretKeyEntry) {
            setSecretKeyEntry(alias, ((SecretKeyEntry) entry).getSecretKey(), param);
        } else {
            throw new KeyStoreException("Entry must be a PrivateKeyEntry, SecretKeyEntry or TrustedCertificateEntry; was " + entry);
        }
    }
}
