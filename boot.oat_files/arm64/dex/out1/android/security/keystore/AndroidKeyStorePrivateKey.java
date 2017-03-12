package android.security.keystore;

import java.security.PrivateKey;

public class AndroidKeyStorePrivateKey extends AndroidKeyStoreKey implements PrivateKey {
    public AndroidKeyStorePrivateKey(String alias, String algorithm) {
        super(alias, algorithm);
    }
}
