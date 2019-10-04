/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.NoSuchAlgorithmException
 *  java.security.Provider
 *  java.util.Collection
 */
package org.bouncycastle.x509;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Collection;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;
import org.bouncycastle.x509.NoSuchStoreException;
import org.bouncycastle.x509.X509StoreParameters;
import org.bouncycastle.x509.X509StoreSpi;
import org.bouncycastle.x509.X509Util;

public class X509Store
implements Store {
    private Provider _provider;
    private X509StoreSpi _spi;

    private X509Store(Provider provider, X509StoreSpi x509StoreSpi) {
        this._provider = provider;
        this._spi = x509StoreSpi;
    }

    private static X509Store createStore(X509Util.Implementation implementation, X509StoreParameters x509StoreParameters) {
        X509StoreSpi x509StoreSpi = (X509StoreSpi)implementation.getEngine();
        x509StoreSpi.engineInit(x509StoreParameters);
        return new X509Store(implementation.getProvider(), x509StoreSpi);
    }

    public static X509Store getInstance(String string, X509StoreParameters x509StoreParameters) {
        try {
            X509Store x509Store = X509Store.createStore(X509Util.getImplementation("X509Store", string), x509StoreParameters);
            return x509Store;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new NoSuchStoreException(noSuchAlgorithmException.getMessage());
        }
    }

    public static X509Store getInstance(String string, X509StoreParameters x509StoreParameters, String string2) {
        return X509Store.getInstance(string, x509StoreParameters, X509Util.getProvider(string2));
    }

    public static X509Store getInstance(String string, X509StoreParameters x509StoreParameters, Provider provider) {
        try {
            X509Store x509Store = X509Store.createStore(X509Util.getImplementation("X509Store", string, provider), x509StoreParameters);
            return x509Store;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new NoSuchStoreException(noSuchAlgorithmException.getMessage());
        }
    }

    public Collection getMatches(Selector selector) {
        return this._spi.engineGetMatches(selector);
    }

    public Provider getProvider() {
        return this._provider;
    }
}

