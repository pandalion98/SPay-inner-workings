package android.security.keystore;

import android.security.Credentials;
import android.security.KeyPairGeneratorSpec;
import android.security.KeyStore;
import android.security.KeyStore.State;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyGenParameterSpec.Builder;
import android.security.keystore.KeyProperties.BlockMode;
import android.security.keystore.KeyProperties.Digest;
import android.security.keystore.KeyProperties.EncryptionPadding;
import android.security.keystore.KeyProperties.KeyAlgorithm;
import android.security.keystore.KeyProperties.Purpose;
import com.android.internal.util.ArrayUtils;
import com.android.org.bouncycastle.asn1.ASN1EncodableVector;
import com.android.org.bouncycastle.asn1.ASN1InputStream;
import com.android.org.bouncycastle.asn1.ASN1Integer;
import com.android.org.bouncycastle.asn1.DERBitString;
import com.android.org.bouncycastle.asn1.DERInteger;
import com.android.org.bouncycastle.asn1.DERNull;
import com.android.org.bouncycastle.asn1.DERSequence;
import com.android.org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import com.android.org.bouncycastle.asn1.x509.Certificate;
import com.android.org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import com.android.org.bouncycastle.asn1.x509.TBSCertificate;
import com.android.org.bouncycastle.asn1.x509.Time;
import com.android.org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import com.android.org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import com.android.org.bouncycastle.jce.X509Principal;
import com.android.org.bouncycastle.jce.provider.X509CertificateObject;
import com.android.org.bouncycastle.x509.X509V3CertificateGenerator;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import libcore.util.EmptyArray;

public abstract class AndroidKeyStoreKeyPairGeneratorSpi extends KeyPairGeneratorSpi {
    private static final int EC_DEFAULT_KEY_SIZE = 256;
    private static final int RSA_DEFAULT_KEY_SIZE = 2048;
    private static final int RSA_MAX_KEY_SIZE = 8192;
    private static final int RSA_MIN_KEY_SIZE = 512;
    private static final List<String> SUPPORTED_EC_NIST_CURVE_NAMES = new ArrayList();
    private static final Map<String, Integer> SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE = new HashMap();
    private static final List<Integer> SUPPORTED_EC_NIST_CURVE_SIZES = new ArrayList();
    private boolean mEncryptionAtRestRequired;
    private String mEntryAlias;
    private String mJcaKeyAlgorithm;
    private int mKeySizeBits;
    private KeyStore mKeyStore;
    private int mKeymasterAlgorithm = -1;
    private int[] mKeymasterBlockModes;
    private int[] mKeymasterDigests;
    private int[] mKeymasterEncryptionPaddings;
    private int[] mKeymasterPurposes;
    private int[] mKeymasterSignaturePaddings;
    private final int mOriginalKeymasterAlgorithm;
    private BigInteger mRSAPublicExponent;
    private SecureRandom mRng;
    private KeyGenParameterSpec mSpec;

    public static class EC extends AndroidKeyStoreKeyPairGeneratorSpi {
        public EC() {
            super(3);
        }
    }

    public static class RSA extends AndroidKeyStoreKeyPairGeneratorSpi {
        public RSA() {
            super(1);
        }
    }

    static {
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-224", Integer.valueOf(224));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp224r1", Integer.valueOf(224));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-256", Integer.valueOf(256));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp256r1", Integer.valueOf(256));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("prime256v1", Integer.valueOf(256));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-384", Integer.valueOf(384));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp384r1", Integer.valueOf(384));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-521", Integer.valueOf(521));
        SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp521r1", Integer.valueOf(521));
        SUPPORTED_EC_NIST_CURVE_NAMES.addAll(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.keySet());
        Collections.sort(SUPPORTED_EC_NIST_CURVE_NAMES);
        SUPPORTED_EC_NIST_CURVE_SIZES.addAll(new HashSet(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.values()));
        Collections.sort(SUPPORTED_EC_NIST_CURVE_SIZES);
    }

    protected AndroidKeyStoreKeyPairGeneratorSpi(int keymasterAlgorithm) {
        this.mOriginalKeymasterAlgorithm = keymasterAlgorithm;
    }

    public void initialize(int keysize, SecureRandom random) {
        throw new IllegalArgumentException(KeyGenParameterSpec.class.getName() + " or " + KeyPairGeneratorSpec.class.getName() + " required to initialize this KeyPairGenerator");
    }

    public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        RuntimeException e;
        resetAll();
        if (params == null) {
            try {
                throw new InvalidAlgorithmParameterException("Must supply params of type " + KeyGenParameterSpec.class.getName() + " or " + KeyPairGeneratorSpec.class.getName());
            } catch (Throwable th) {
                if (!false) {
                    resetAll();
                }
            }
        } else {
            KeyGenParameterSpec spec;
            boolean encryptionAtRestRequired = false;
            int keymasterAlgorithm = this.mOriginalKeymasterAlgorithm;
            if (params instanceof KeyGenParameterSpec) {
                spec = (KeyGenParameterSpec) params;
            } else if (params instanceof KeyPairGeneratorSpec) {
                KeyPairGeneratorSpec legacySpec = (KeyPairGeneratorSpec) params;
                try {
                    Builder specBuilder;
                    String specKeyAlgorithm = legacySpec.getKeyType();
                    if (specKeyAlgorithm != null) {
                        keymasterAlgorithm = KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(specKeyAlgorithm);
                    }
                    switch (keymasterAlgorithm) {
                        case 1:
                            specBuilder = new Builder(legacySpec.getKeystoreAlias(), 15);
                            specBuilder.setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512);
                            specBuilder.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE, KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1, KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
                            specBuilder.setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1, KeyProperties.SIGNATURE_PADDING_RSA_PSS);
                            specBuilder.setRandomizedEncryptionRequired(false);
                            break;
                        case 3:
                            specBuilder = new Builder(legacySpec.getKeystoreAlias(), 12);
                            specBuilder.setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224, KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384, KeyProperties.DIGEST_SHA512);
                            break;
                        default:
                            throw new ProviderException("Unsupported algorithm: " + this.mKeymasterAlgorithm);
                    }
                    if (legacySpec.getKeySize() != -1) {
                        specBuilder.setKeySize(legacySpec.getKeySize());
                    }
                    if (legacySpec.getAlgorithmParameterSpec() != null) {
                        specBuilder.setAlgorithmParameterSpec(legacySpec.getAlgorithmParameterSpec());
                    }
                    specBuilder.setCertificateSubject(legacySpec.getSubjectDN());
                    specBuilder.setCertificateSerialNumber(legacySpec.getSerialNumber());
                    specBuilder.setCertificateNotBefore(legacySpec.getStartDate());
                    specBuilder.setCertificateNotAfter(legacySpec.getEndDate());
                    encryptionAtRestRequired = legacySpec.isEncryptionRequired();
                    specBuilder.setUserAuthenticationRequired(false);
                    spec = specBuilder.build();
                } catch (IllegalArgumentException e2) {
                    throw new InvalidAlgorithmParameterException("Invalid key type in parameters", e2);
                } catch (RuntimeException e3) {
                    e = e3;
                    throw new InvalidAlgorithmParameterException(e);
                } catch (RuntimeException e32) {
                    e = e32;
                    throw new InvalidAlgorithmParameterException(e);
                }
            } else {
                throw new InvalidAlgorithmParameterException("Unsupported params class: " + params.getClass().getName() + ". Supported: " + KeyGenParameterSpec.class.getName() + ", " + KeyPairGeneratorSpec.class.getName());
            }
            this.mEntryAlias = spec.getKeystoreAlias();
            this.mSpec = spec;
            this.mKeymasterAlgorithm = keymasterAlgorithm;
            this.mEncryptionAtRestRequired = encryptionAtRestRequired;
            this.mKeySizeBits = spec.getKeySize();
            initAlgorithmSpecificParameters();
            if (this.mKeySizeBits == -1) {
                this.mKeySizeBits = getDefaultKeySize(keymasterAlgorithm);
            }
            checkValidKeySize(keymasterAlgorithm, this.mKeySizeBits);
            if (spec.getKeystoreAlias() == null) {
                throw new InvalidAlgorithmParameterException("KeyStore entry alias not provided");
            }
            try {
                String jcaKeyAlgorithm = KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(keymasterAlgorithm);
                this.mKeymasterPurposes = Purpose.allToKeymaster(spec.getPurposes());
                this.mKeymasterBlockModes = BlockMode.allToKeymaster(spec.getBlockModes());
                this.mKeymasterEncryptionPaddings = EncryptionPadding.allToKeymaster(spec.getEncryptionPaddings());
                if ((spec.getPurposes() & 1) != 0 && spec.isRandomizedEncryptionRequired()) {
                    int[] arr$ = this.mKeymasterEncryptionPaddings;
                    int len$ = arr$.length;
                    int i$ = 0;
                    while (i$ < len$) {
                        int keymasterPadding = arr$[i$];
                        if (KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(keymasterPadding)) {
                            i$++;
                        } else {
                            throw new InvalidAlgorithmParameterException("Randomized encryption (IND-CPA) required but may be violated by padding scheme: " + EncryptionPadding.fromKeymaster(keymasterPadding) + ". See " + KeyGenParameterSpec.class.getName() + " documentation.");
                        }
                    }
                }
                this.mKeymasterSignaturePaddings = SignaturePadding.allToKeymaster(spec.getSignaturePaddings());
                if (spec.isDigestsSpecified()) {
                    this.mKeymasterDigests = Digest.allToKeymaster(spec.getDigests());
                } else {
                    this.mKeymasterDigests = EmptyArray.INT;
                }
                KeymasterUtils.addUserAuthArgs(new KeymasterArguments(), this.mSpec.isUserAuthenticationRequired(), this.mSpec.getUserAuthenticationValidityDurationSeconds());
                this.mJcaKeyAlgorithm = jcaKeyAlgorithm;
                this.mRng = random;
                this.mKeyStore = KeyStore.getInstance();
                if (!true) {
                    resetAll();
                }
            } catch (RuntimeException e322) {
                e = e322;
                throw new InvalidAlgorithmParameterException(e);
            } catch (RuntimeException e3222) {
                e = e3222;
                throw new InvalidAlgorithmParameterException(e);
            }
        }
    }

    private void resetAll() {
        this.mEntryAlias = null;
        this.mJcaKeyAlgorithm = null;
        this.mKeymasterAlgorithm = -1;
        this.mKeymasterPurposes = null;
        this.mKeymasterBlockModes = null;
        this.mKeymasterEncryptionPaddings = null;
        this.mKeymasterSignaturePaddings = null;
        this.mKeymasterDigests = null;
        this.mKeySizeBits = 0;
        this.mSpec = null;
        this.mRSAPublicExponent = null;
        this.mEncryptionAtRestRequired = false;
        this.mRng = null;
        this.mKeyStore = null;
    }

    private void initAlgorithmSpecificParameters() throws InvalidAlgorithmParameterException {
        AlgorithmParameterSpec algSpecificSpec = this.mSpec.getAlgorithmParameterSpec();
        switch (this.mKeymasterAlgorithm) {
            case 1:
                BigInteger publicExponent = null;
                if (algSpecificSpec instanceof RSAKeyGenParameterSpec) {
                    RSAKeyGenParameterSpec rsaSpec = (RSAKeyGenParameterSpec) algSpecificSpec;
                    if (this.mKeySizeBits == -1) {
                        this.mKeySizeBits = rsaSpec.getKeysize();
                    } else if (this.mKeySizeBits != rsaSpec.getKeysize()) {
                        throw new InvalidAlgorithmParameterException("RSA key size must match  between " + this.mSpec + " and " + algSpecificSpec + ": " + this.mKeySizeBits + " vs " + rsaSpec.getKeysize());
                    }
                    publicExponent = rsaSpec.getPublicExponent();
                } else if (algSpecificSpec != null) {
                    throw new InvalidAlgorithmParameterException("RSA may only use RSAKeyGenParameterSpec");
                }
                if (publicExponent == null) {
                    publicExponent = RSAKeyGenParameterSpec.F4;
                }
                if (publicExponent.compareTo(BigInteger.ZERO) < 1) {
                    throw new InvalidAlgorithmParameterException("RSA public exponent must be positive: " + publicExponent);
                } else if (publicExponent.compareTo(KeymasterArguments.UINT64_MAX_VALUE) > 0) {
                    throw new InvalidAlgorithmParameterException("Unsupported RSA public exponent: " + publicExponent + ". Maximum supported value: " + KeymasterArguments.UINT64_MAX_VALUE);
                } else {
                    this.mRSAPublicExponent = publicExponent;
                    return;
                }
            case 3:
                if (algSpecificSpec instanceof ECGenParameterSpec) {
                    String curveName = ((ECGenParameterSpec) algSpecificSpec).getName();
                    Integer ecSpecKeySizeBits = (Integer) SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.get(curveName.toLowerCase(Locale.US));
                    if (ecSpecKeySizeBits == null) {
                        throw new InvalidAlgorithmParameterException("Unsupported EC curve name: " + curveName + ". Supported: " + SUPPORTED_EC_NIST_CURVE_NAMES);
                    } else if (this.mKeySizeBits == -1) {
                        this.mKeySizeBits = ecSpecKeySizeBits.intValue();
                        return;
                    } else if (this.mKeySizeBits != ecSpecKeySizeBits.intValue()) {
                        throw new InvalidAlgorithmParameterException("EC key size must match  between " + this.mSpec + " and " + algSpecificSpec + ": " + this.mKeySizeBits + " vs " + ecSpecKeySizeBits);
                    } else {
                        return;
                    }
                } else if (algSpecificSpec != null) {
                    throw new InvalidAlgorithmParameterException("EC may only use ECGenParameterSpec");
                } else {
                    return;
                }
            default:
                throw new ProviderException("Unsupported algorithm: " + this.mKeymasterAlgorithm);
        }
    }

    public KeyPair generateKeyPair() {
        if (this.mKeyStore == null || this.mSpec == null) {
            throw new IllegalStateException("Not initialized");
        }
        int flags = this.mEncryptionAtRestRequired ? 1 : 0;
        if ((flags & 1) == 0 || this.mKeyStore.state() == State.UNLOCKED) {
            KeymasterArguments args = new KeymasterArguments();
            args.addUnsignedInt(KeymasterDefs.KM_TAG_KEY_SIZE, (long) this.mKeySizeBits);
            args.addEnum(KeymasterDefs.KM_TAG_ALGORITHM, this.mKeymasterAlgorithm);
            args.addEnums(KeymasterDefs.KM_TAG_PURPOSE, this.mKeymasterPurposes);
            args.addEnums(KeymasterDefs.KM_TAG_BLOCK_MODE, this.mKeymasterBlockModes);
            args.addEnums(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterEncryptionPaddings);
            args.addEnums(KeymasterDefs.KM_TAG_PADDING, this.mKeymasterSignaturePaddings);
            args.addEnums(KeymasterDefs.KM_TAG_DIGEST, this.mKeymasterDigests);
            KeymasterUtils.addUserAuthArgs(args, this.mSpec.isUserAuthenticationRequired(), this.mSpec.getUserAuthenticationValidityDurationSeconds());
            args.addDateIfNotNull(KeymasterDefs.KM_TAG_ACTIVE_DATETIME, this.mSpec.getKeyValidityStart());
            args.addDateIfNotNull(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME, this.mSpec.getKeyValidityForOriginationEnd());
            args.addDateIfNotNull(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME, this.mSpec.getKeyValidityForConsumptionEnd());
            addAlgorithmSpecificParameters(args);
            byte[] additionalEntropy = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(this.mRng, (this.mKeySizeBits + 7) / 8);
            String privateKeyAlias = Credentials.USER_PRIVATE_KEY + this.mEntryAlias;
            try {
                Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias);
                int errorCode = this.mKeyStore.generateKey(privateKeyAlias, args, additionalEntropy, flags, new KeyCharacteristics());
                if (errorCode != 1) {
                    throw new ProviderException("Failed to generate key pair", KeyStore.getKeyStoreException(errorCode));
                }
                KeyPair result = AndroidKeyStoreProvider.loadAndroidKeyStoreKeyPairFromKeystore(this.mKeyStore, privateKeyAlias);
                if (this.mJcaKeyAlgorithm.equalsIgnoreCase(result.getPrivate().getAlgorithm())) {
                    int insertErrorCode = this.mKeyStore.insert(Credentials.USER_CERTIFICATE + this.mEntryAlias, generateSelfSignedCertificate(result.getPrivate(), result.getPublic()).getEncoded(), -1, flags);
                    if (insertErrorCode != 1) {
                        throw new ProviderException("Failed to store self-signed certificate", KeyStore.getKeyStoreException(insertErrorCode));
                    }
                    if (!true) {
                        Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias);
                    }
                    return result;
                }
                throw new ProviderException("Generated key pair algorithm does not match requested algorithm: " + result.getPrivate().getAlgorithm() + " vs " + this.mJcaKeyAlgorithm);
            } catch (CertificateEncodingException e) {
                throw new ProviderException("Failed to obtain encoded form of self-signed certificate", e);
            } catch (Exception e2) {
                throw new ProviderException("Failed to generate self-signed certificate", e2);
            } catch (UnrecoverableKeyException e3) {
                throw new ProviderException("Failed to load generated key pair from keystore", e3);
            } catch (Throwable th) {
                if (!false) {
                    Credentials.deleteAllTypesForAlias(this.mKeyStore, this.mEntryAlias);
                }
            }
        } else {
            throw new IllegalStateException("Encryption at rest using secure lock screen credential requested for key pair, but the user has not yet entered the credential");
        }
    }

    private void addAlgorithmSpecificParameters(KeymasterArguments keymasterArgs) {
        switch (this.mKeymasterAlgorithm) {
            case 1:
                keymasterArgs.addUnsignedLong(KeymasterDefs.KM_TAG_RSA_PUBLIC_EXPONENT, this.mRSAPublicExponent);
                return;
            case 3:
                return;
            default:
                throw new ProviderException("Unsupported algorithm: " + this.mKeymasterAlgorithm);
        }
    }

    private X509Certificate generateSelfSignedCertificate(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        String signatureAlgorithm = getCertificateSignatureAlgorithm(this.mKeymasterAlgorithm, this.mKeySizeBits, this.mSpec);
        if (signatureAlgorithm == null) {
            return generateSelfSignedCertificateWithFakeSignature(publicKey);
        }
        try {
            return generateSelfSignedCertificateWithValidSignature(privateKey, publicKey, signatureAlgorithm);
        } catch (Exception e) {
            return generateSelfSignedCertificateWithFakeSignature(publicKey);
        }
    }

    private X509Certificate generateSelfSignedCertificateWithValidSignature(PrivateKey privateKey, PublicKey publicKey, String signatureAlgorithm) throws Exception {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setPublicKey(publicKey);
        certGen.setSerialNumber(this.mSpec.getCertificateSerialNumber());
        certGen.setSubjectDN(this.mSpec.getCertificateSubject());
        certGen.setIssuerDN(this.mSpec.getCertificateSubject());
        certGen.setNotBefore(this.mSpec.getCertificateNotBefore());
        certGen.setNotAfter(this.mSpec.getCertificateNotAfter());
        certGen.setSignatureAlgorithm(signatureAlgorithm);
        return certGen.generate(privateKey);
    }

    private X509Certificate generateSelfSignedCertificateWithFakeSignature(PublicKey publicKey) throws Exception {
        AlgorithmIdentifier sigAlgId;
        byte[] signature;
        Throwable th;
        V3TBSCertificateGenerator tbsGenerator = new V3TBSCertificateGenerator();
        switch (this.mKeymasterAlgorithm) {
            case 1:
                sigAlgId = new AlgorithmIdentifier(PKCSObjectIdentifiers.sha256WithRSAEncryption, DERNull.INSTANCE);
                signature = new byte[1];
                break;
            case 3:
                sigAlgId = new AlgorithmIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA256);
                ASN1EncodableVector v = new ASN1EncodableVector();
                v.add(new DERInteger(0));
                v.add(new DERInteger(0));
                signature = new DERSequence().getEncoded();
                break;
            default:
                throw new ProviderException("Unsupported key algorithm: " + this.mKeymasterAlgorithm);
        }
        ASN1InputStream publicKeyInfoIn = new ASN1InputStream(publicKey.getEncoded());
        Throwable th2 = null;
        try {
            tbsGenerator.setSubjectPublicKeyInfo(SubjectPublicKeyInfo.getInstance(publicKeyInfoIn.readObject()));
            if (publicKeyInfoIn != null) {
                if (th2 != null) {
                    try {
                        publicKeyInfoIn.close();
                    } catch (Throwable x2) {
                        th2.addSuppressed(x2);
                    }
                } else {
                    publicKeyInfoIn.close();
                }
            }
            tbsGenerator.setSerialNumber(new ASN1Integer(this.mSpec.getCertificateSerialNumber()));
            X509Principal subject = new X509Principal(this.mSpec.getCertificateSubject().getEncoded());
            tbsGenerator.setSubject(subject);
            tbsGenerator.setIssuer(subject);
            tbsGenerator.setStartDate(new Time(this.mSpec.getCertificateNotBefore()));
            tbsGenerator.setEndDate(new Time(this.mSpec.getCertificateNotAfter()));
            tbsGenerator.setSignature(sigAlgId);
            TBSCertificate tbsCertificate = tbsGenerator.generateTBSCertificate();
            ASN1EncodableVector result = new ASN1EncodableVector();
            result.add(tbsCertificate);
            result.add(sigAlgId);
            result.add(new DERBitString(signature));
            return new X509CertificateObject(Certificate.getInstance(new DERSequence(result)));
        } catch (Throwable th22) {
            Throwable th3 = th22;
            th22 = th;
            th = th3;
        }
        throw th;
        if (publicKeyInfoIn != null) {
            if (th22 != null) {
                try {
                    publicKeyInfoIn.close();
                } catch (Throwable x22) {
                    th22.addSuppressed(x22);
                }
            } else {
                publicKeyInfoIn.close();
            }
        }
        throw th;
    }

    private static int getDefaultKeySize(int keymasterAlgorithm) {
        switch (keymasterAlgorithm) {
            case 1:
                return 2048;
            case 3:
                return 256;
            default:
                throw new ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    private static void checkValidKeySize(int keymasterAlgorithm, int keySize) throws InvalidAlgorithmParameterException {
        switch (keymasterAlgorithm) {
            case 1:
                if (keySize < 512 || keySize > 8192) {
                    throw new InvalidAlgorithmParameterException("RSA key size must be >= 512 and <= 8192");
                }
                return;
            case 3:
                if (!SUPPORTED_EC_NIST_CURVE_SIZES.contains(Integer.valueOf(keySize))) {
                    throw new InvalidAlgorithmParameterException("Unsupported EC key size: " + keySize + " bits. Supported: " + SUPPORTED_EC_NIST_CURVE_SIZES);
                }
                return;
            default:
                throw new ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    private static String getCertificateSignatureAlgorithm(int keymasterAlgorithm, int keySizeBits, KeyGenParameterSpec spec) {
        if ((spec.getPurposes() & 4) == 0) {
            return null;
        }
        if (spec.isUserAuthenticationRequired()) {
            return null;
        }
        if (!spec.isDigestsSpecified()) {
            return null;
        }
        int bestKeymasterDigest;
        int bestDigestOutputSizeBits;
        int keymasterDigest;
        int outputSizeBits;
        switch (keymasterAlgorithm) {
            case 1:
                if (!ArrayUtils.contains(SignaturePadding.allToKeymaster(spec.getSignaturePaddings()), 5)) {
                    return null;
                }
                int maxDigestOutputSizeBits = keySizeBits - 240;
                bestKeymasterDigest = -1;
                bestDigestOutputSizeBits = -1;
                for (Integer intValue : getAvailableKeymasterSignatureDigests(spec.getDigests(), AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests())) {
                    keymasterDigest = intValue.intValue();
                    outputSizeBits = KeymasterUtils.getDigestOutputSizeBits(keymasterDigest);
                    if (outputSizeBits <= maxDigestOutputSizeBits) {
                        if (bestKeymasterDigest == -1) {
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        } else if (outputSizeBits > bestDigestOutputSizeBits) {
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        }
                    }
                }
                if (bestKeymasterDigest == -1) {
                    return null;
                }
                return Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest) + "WithRSA";
            case 3:
                bestKeymasterDigest = -1;
                bestDigestOutputSizeBits = -1;
                for (Integer intValue2 : getAvailableKeymasterSignatureDigests(spec.getDigests(), AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests())) {
                    keymasterDigest = intValue2.intValue();
                    outputSizeBits = KeymasterUtils.getDigestOutputSizeBits(keymasterDigest);
                    if (outputSizeBits == keySizeBits) {
                        bestKeymasterDigest = keymasterDigest;
                        bestDigestOutputSizeBits = outputSizeBits;
                        if (bestKeymasterDigest != -1) {
                            return null;
                        }
                        return Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest) + "WithECDSA";
                    } else if (bestKeymasterDigest == -1) {
                        bestKeymasterDigest = keymasterDigest;
                        bestDigestOutputSizeBits = outputSizeBits;
                    } else if (bestDigestOutputSizeBits < keySizeBits) {
                        if (outputSizeBits > bestDigestOutputSizeBits) {
                            bestKeymasterDigest = keymasterDigest;
                            bestDigestOutputSizeBits = outputSizeBits;
                        }
                    } else if (outputSizeBits < bestDigestOutputSizeBits && outputSizeBits >= keySizeBits) {
                        bestKeymasterDigest = keymasterDigest;
                        bestDigestOutputSizeBits = outputSizeBits;
                    }
                }
                if (bestKeymasterDigest != -1) {
                    return Digest.fromKeymasterToSignatureAlgorithmDigest(bestKeymasterDigest) + "WithECDSA";
                }
                return null;
            default:
                throw new ProviderException("Unsupported algorithm: " + keymasterAlgorithm);
        }
    }

    private static Set<Integer> getAvailableKeymasterSignatureDigests(String[] authorizedKeyDigests, String[] supportedSignatureDigests) {
        Set<Integer> authorizedKeymasterKeyDigests = new HashSet();
        for (int keymasterDigest : Digest.allToKeymaster(authorizedKeyDigests)) {
            authorizedKeymasterKeyDigests.add(Integer.valueOf(keymasterDigest));
        }
        Set<Integer> supportedKeymasterSignatureDigests = new HashSet();
        for (int keymasterDigest2 : Digest.allToKeymaster(supportedSignatureDigests)) {
            supportedKeymasterSignatureDigests.add(Integer.valueOf(keymasterDigest2));
        }
        Set<Integer> result = new HashSet(supportedKeymasterSignatureDigests);
        result.retainAll(authorizedKeymasterKeyDigests);
        return result;
    }
}
