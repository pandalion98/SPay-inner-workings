package android.security.keystore;

import android.security.Credentials;
import android.security.KeyStore;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyProperties.BlockMode;
import android.security.keystore.KeyProperties.Digest;
import android.security.keystore.KeyProperties.EncryptionPadding;
import android.security.keystore.KeyProperties.Origin;
import android.security.keystore.KeyProperties.Purpose;
import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.SecretKeySpec;

public class AndroidKeyStoreSecretKeyFactorySpi extends SecretKeyFactorySpi {
    private final KeyStore mKeyStore = KeyStore.getInstance();

    protected KeySpec engineGetKeySpec(SecretKey key, Class keySpecClass) throws InvalidKeySpecException {
        if (keySpecClass == null) {
            throw new InvalidKeySpecException("keySpecClass == null");
        } else if (!(key instanceof AndroidKeyStoreSecretKey)) {
            throw new InvalidKeySpecException("Only Android KeyStore secret keys supported: " + (key != null ? key.getClass().getName() : "null"));
        } else if (SecretKeySpec.class.isAssignableFrom(keySpecClass)) {
            throw new InvalidKeySpecException("Key material export of Android KeyStore keys is not supported");
        } else if (KeyInfo.class.equals(keySpecClass)) {
            String keyAliasInKeystore = ((AndroidKeyStoreKey) key).getAlias();
            if (keyAliasInKeystore.startsWith(Credentials.USER_SECRET_KEY)) {
                return getKeyInfo(this.mKeyStore, keyAliasInKeystore.substring(Credentials.USER_SECRET_KEY.length()), keyAliasInKeystore);
            }
            throw new InvalidKeySpecException("Invalid key alias: " + keyAliasInKeystore);
        } else {
            throw new InvalidKeySpecException("Unsupported key spec: " + keySpecClass.getName());
        }
    }

    static KeyInfo getKeyInfo(KeyStore keyStore, String entryAlias, String keyAliasInKeystore) {
        int keymasterPadding;
        KeyCharacteristics keyCharacteristics = new KeyCharacteristics();
        int errorCode = keyStore.getKeyCharacteristics(keyAliasInKeystore, null, null, keyCharacteristics);
        if (errorCode != 1) {
            throw new ProviderException("Failed to obtain information about key. Keystore error: " + errorCode);
        }
        try {
            boolean insideSecureHardware;
            int origin;
            if (keyCharacteristics.hwEnforced.containsTag(KeymasterDefs.KM_TAG_ORIGIN)) {
                insideSecureHardware = true;
                origin = Origin.fromKeymaster(keyCharacteristics.hwEnforced.getEnum(KeymasterDefs.KM_TAG_ORIGIN, -1));
            } else if (keyCharacteristics.swEnforced.containsTag(KeymasterDefs.KM_TAG_ORIGIN)) {
                insideSecureHardware = false;
                origin = Origin.fromKeymaster(keyCharacteristics.swEnforced.getEnum(KeymasterDefs.KM_TAG_ORIGIN, -1));
            } else {
                throw new ProviderException("Key origin not available");
            }
            long keySizeUnsigned = keyCharacteristics.getUnsignedInt(KeymasterDefs.KM_TAG_KEY_SIZE, -1);
            if (keySizeUnsigned == -1) {
                throw new ProviderException("Key size not available");
            } else if (keySizeUnsigned > 2147483647L) {
                throw new ProviderException("Key too large: " + keySizeUnsigned + " bits");
            } else {
                int keySize = (int) keySizeUnsigned;
                int purposes = Purpose.allFromKeymaster(keyCharacteristics.getEnums(KeymasterDefs.KM_TAG_PURPOSE));
                List<String> encryptionPaddingsList = new ArrayList();
                List<String> signaturePaddingsList = new ArrayList();
                for (Integer intValue : keyCharacteristics.getEnums(KeymasterDefs.KM_TAG_PADDING)) {
                    keymasterPadding = intValue.intValue();
                    try {
                        encryptionPaddingsList.add(EncryptionPadding.fromKeymaster(keymasterPadding));
                    } catch (IllegalArgumentException e) {
                        signaturePaddingsList.add(SignaturePadding.fromKeymaster(keymasterPadding));
                    }
                }
                String[] encryptionPaddings = (String[]) encryptionPaddingsList.toArray(new String[encryptionPaddingsList.size()]);
                String[] signaturePaddings = (String[]) signaturePaddingsList.toArray(new String[signaturePaddingsList.size()]);
                String[] digests = Digest.allFromKeymaster(keyCharacteristics.getEnums(KeymasterDefs.KM_TAG_DIGEST));
                String[] blockModes = BlockMode.allFromKeymaster(keyCharacteristics.getEnums(KeymasterDefs.KM_TAG_BLOCK_MODE));
                int keymasterSwEnforcedUserAuthenticators = keyCharacteristics.swEnforced.getEnum(KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 0);
                int keymasterHwEnforcedUserAuthenticators = keyCharacteristics.hwEnforced.getEnum(KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 0);
                Date keyValidityStart = keyCharacteristics.getDate(KeymasterDefs.KM_TAG_ACTIVE_DATETIME);
                Date keyValidityForOriginationEnd = keyCharacteristics.getDate(KeymasterDefs.KM_TAG_ORIGINATION_EXPIRE_DATETIME);
                Date keyValidityForConsumptionEnd = keyCharacteristics.getDate(KeymasterDefs.KM_TAG_USAGE_EXPIRE_DATETIME);
                boolean userAuthenticationRequired = !keyCharacteristics.getBoolean(KeymasterDefs.KM_TAG_NO_AUTH_REQUIRED);
                long userAuthenticationValidityDurationSeconds = keyCharacteristics.getUnsignedInt(KeymasterDefs.KM_TAG_AUTH_TIMEOUT, -1);
                if (userAuthenticationValidityDurationSeconds > 2147483647L) {
                    throw new ProviderException("User authentication timeout validity too long: " + userAuthenticationValidityDurationSeconds + " seconds");
                }
                boolean userAuthenticationRequirementEnforcedBySecureHardware = userAuthenticationRequired && keymasterHwEnforcedUserAuthenticators != 0 && keymasterSwEnforcedUserAuthenticators == 0;
                return new KeyInfo(entryAlias, insideSecureHardware, origin, keySize, keyValidityStart, keyValidityForOriginationEnd, keyValidityForConsumptionEnd, purposes, encryptionPaddings, signaturePaddings, digests, blockModes, userAuthenticationRequired, (int) userAuthenticationValidityDurationSeconds, userAuthenticationRequirementEnforcedBySecureHardware);
            }
        } catch (IllegalArgumentException e2) {
            throw new ProviderException("Unsupported encryption padding: " + keymasterPadding);
        } catch (Throwable e3) {
            throw new ProviderException("Unsupported key characteristic", e3);
        }
    }

    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        throw new InvalidKeySpecException("To generate secret key in Android Keystore, use KeyGenerator initialized with " + KeyGenParameterSpec.class.getName());
    }

    protected SecretKey engineTranslateKey(SecretKey key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("key == null");
        } else if (key instanceof AndroidKeyStoreSecretKey) {
            return key;
        } else {
            throw new InvalidKeyException("To import a secret key into Android Keystore, use KeyStore.setEntry");
        }
    }
}
