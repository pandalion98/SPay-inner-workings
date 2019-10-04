/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayInputStream
 *  java.io.InputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.security.NoSuchAlgorithmException
 *  java.security.Provider
 *  java.util.Collection
 */
package org.bouncycastle.x509;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Collection;
import org.bouncycastle.x509.NoSuchParserException;
import org.bouncycastle.x509.X509StreamParserSpi;
import org.bouncycastle.x509.X509Util;
import org.bouncycastle.x509.util.StreamParser;

public class X509StreamParser
implements StreamParser {
    private Provider _provider;
    private X509StreamParserSpi _spi;

    private X509StreamParser(Provider provider, X509StreamParserSpi x509StreamParserSpi) {
        this._provider = provider;
        this._spi = x509StreamParserSpi;
    }

    private static X509StreamParser createParser(X509Util.Implementation implementation) {
        X509StreamParserSpi x509StreamParserSpi = (X509StreamParserSpi)implementation.getEngine();
        return new X509StreamParser(implementation.getProvider(), x509StreamParserSpi);
    }

    public static X509StreamParser getInstance(String string) {
        try {
            X509StreamParser x509StreamParser = X509StreamParser.createParser(X509Util.getImplementation("X509StreamParser", string));
            return x509StreamParser;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new NoSuchParserException(noSuchAlgorithmException.getMessage());
        }
    }

    public static X509StreamParser getInstance(String string, String string2) {
        return X509StreamParser.getInstance(string, X509Util.getProvider(string2));
    }

    public static X509StreamParser getInstance(String string, Provider provider) {
        try {
            X509StreamParser x509StreamParser = X509StreamParser.createParser(X509Util.getImplementation("X509StreamParser", string, provider));
            return x509StreamParser;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new NoSuchParserException(noSuchAlgorithmException.getMessage());
        }
    }

    public Provider getProvider() {
        return this._provider;
    }

    public void init(InputStream inputStream) {
        this._spi.engineInit(inputStream);
    }

    public void init(byte[] arrby) {
        this._spi.engineInit((InputStream)new ByteArrayInputStream(arrby));
    }

    @Override
    public Object read() {
        return this._spi.engineRead();
    }

    @Override
    public Collection readAll() {
        return this._spi.engineReadAll();
    }
}

