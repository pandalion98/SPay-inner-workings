package android.security.keystore;

import javax.crypto.SecretKey;

public class AndroidKeyStoreSecretKey extends AndroidKeyStoreKey implements SecretKey {
    public AndroidKeyStoreSecretKey(String alias, String algorithm) {
        super(alias, algorithm);
    }
}
