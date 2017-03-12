package android.security.keystore;

import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;

public class AndroidKeyStoreECPrivateKey extends AndroidKeyStorePrivateKey implements ECKey {
    private final ECParameterSpec mParams;

    public AndroidKeyStoreECPrivateKey(String alias, ECParameterSpec params) {
        super(alias, KeyProperties.KEY_ALGORITHM_EC);
        this.mParams = params;
    }

    public ECParameterSpec getParams() {
        return this.mParams;
    }
}
