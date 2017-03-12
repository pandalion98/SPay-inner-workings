package android.security.keystore;

import android.hardware.fingerprint.FingerprintManager;
import android.security.GateKeeper;
import android.security.KeyStore;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyProperties.Digest;
import com.android.internal.util.ArrayUtils;
import java.security.ProviderException;

public abstract class KeymasterUtils {
    private KeymasterUtils() {
    }

    public static int getDigestOutputSizeBits(int keymasterDigest) {
        switch (keymasterDigest) {
            case 0:
                return -1;
            case 1:
                return 128;
            case 2:
                return 160;
            case 3:
                return 224;
            case 4:
                return 256;
            case 5:
                return 384;
            case 6:
                return 512;
            default:
                throw new IllegalArgumentException("Unknown digest: " + keymasterDigest);
        }
    }

    public static boolean isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(int keymasterBlockMode) {
        switch (keymasterBlockMode) {
            case 1:
                return false;
            case 2:
            case 3:
            case 32:
                return true;
            default:
                throw new IllegalArgumentException("Unsupported block mode: " + keymasterBlockMode);
        }
    }

    public static boolean isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(int keymasterPadding) {
        switch (keymasterPadding) {
            case 1:
                return false;
            case 2:
            case 4:
                return true;
            default:
                throw new IllegalArgumentException("Unsupported asymmetric encryption padding scheme: " + keymasterPadding);
        }
    }

    public static void addUserAuthArgs(KeymasterArguments args, boolean userAuthenticationRequired, int userAuthenticationValidityDurationSeconds) {
        if (!userAuthenticationRequired) {
            args.addBoolean(KeymasterDefs.KM_TAG_NO_AUTH_REQUIRED);
        } else if (userAuthenticationValidityDurationSeconds == -1) {
            long fingerprintOnlySid;
            FingerprintManager fingerprintManager = (FingerprintManager) KeyStore.getApplicationContext().getSystemService(FingerprintManager.class);
            if (fingerprintManager != null) {
                fingerprintOnlySid = fingerprintManager.getAuthenticatorId();
            } else {
                fingerprintOnlySid = 0;
            }
            if (fingerprintOnlySid == 0) {
                throw new IllegalStateException("At least one fingerprint must be enrolled to create keys requiring user authentication for every use");
            }
            args.addUnsignedLong(KeymasterDefs.KM_TAG_USER_SECURE_ID, KeymasterArguments.toUint64(fingerprintOnlySid));
            args.addEnum(KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 2);
        } else {
            long rootSid = GateKeeper.getSecureUserId();
            if (rootSid == 0) {
                throw new IllegalStateException("Secure lock screen must be enabled to create keys requiring user authentication");
            }
            args.addUnsignedLong(KeymasterDefs.KM_TAG_USER_SECURE_ID, KeymasterArguments.toUint64(rootSid));
            args.addEnum(KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 3);
            args.addUnsignedInt(KeymasterDefs.KM_TAG_AUTH_TIMEOUT, (long) userAuthenticationValidityDurationSeconds);
        }
    }

    public static void addMinMacLengthAuthorizationIfNecessary(KeymasterArguments args, int keymasterAlgorithm, int[] keymasterBlockModes, int[] keymasterDigests) {
        switch (keymasterAlgorithm) {
            case 32:
                if (ArrayUtils.contains(keymasterBlockModes, 32)) {
                    args.addUnsignedInt(KeymasterDefs.KM_TAG_MIN_MAC_LENGTH, 96);
                    return;
                }
                return;
            case 128:
                if (keymasterDigests.length != 1) {
                    throw new ProviderException("Unsupported number of authorized digests for HMAC key: " + keymasterDigests.length + ". Exactly one digest must be authorized");
                }
                int keymasterDigest = keymasterDigests[0];
                int digestOutputSizeBits = getDigestOutputSizeBits(keymasterDigest);
                if (digestOutputSizeBits == -1) {
                    throw new ProviderException("HMAC key authorized for unsupported digest: " + Digest.fromKeymaster(keymasterDigest));
                }
                args.addUnsignedInt(KeymasterDefs.KM_TAG_MIN_MAC_LENGTH, (long) digestOutputSizeBits);
                return;
            default:
                return;
        }
    }
}
