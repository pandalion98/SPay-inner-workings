/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.ClassNotFoundException
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.InternalError
 *  java.lang.Object
 *  java.lang.String
 *  java.security.AccessController
 *  java.security.PrivateKey
 *  java.security.PrivilegedAction
 *  java.security.Provider
 *  java.security.PublicKey
 *  java.util.HashMap
 *  java.util.Map
 */
package org.bouncycastle.pqc.jcajce.provider;

import java.security.AccessController;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

public class BouncyCastlePQCProvider
extends Provider
implements ConfigurableProvider {
    private static final String[] ALGORITHMS;
    private static final String ALGORITHM_PACKAGE = "org.bouncycastle.pqc.jcajce.provider.";
    public static final ProviderConfiguration CONFIGURATION;
    public static String PROVIDER_NAME;
    private static String info;
    private static final Map keyInfoConverters;

    static {
        info = "BouncyCastle Post-Quantum Security Provider v1.52";
        PROVIDER_NAME = "BCPQC";
        CONFIGURATION = null;
        keyInfoConverters = new HashMap();
        ALGORITHMS = new String[]{"Rainbow", "McEliece"};
    }

    public BouncyCastlePQCProvider() {
        super(PROVIDER_NAME, 1.52, info);
        AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction(){

            public Object run() {
                BouncyCastlePQCProvider.this.setup();
                return null;
            }
        });
    }

    public static PrivateKey getPrivateKey(PrivateKeyInfo privateKeyInfo) {
        AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = (AsymmetricKeyInfoConverter)keyInfoConverters.get((Object)privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePrivate(privateKeyInfo);
    }

    public static PublicKey getPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = (AsymmetricKeyInfoConverter)keyInfoConverters.get((Object)subjectPublicKeyInfo.getAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePublic(subjectPublicKeyInfo);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void loadAlgorithms(String string, String[] arrstring) {
        int n = 0;
        while (n != arrstring.length) {
            Class class_;
            try {
                Class class_2;
                Class class_3;
                ClassLoader classLoader = this.getClass().getClassLoader();
                class_ = classLoader != null ? (class_3 = classLoader.loadClass(string + arrstring[n] + "$Mappings")) : (class_2 = Class.forName((String)(string + arrstring[n] + "$Mappings")));
            }
            catch (ClassNotFoundException classNotFoundException) {
                class_ = null;
            }
            if (class_ != null) {
                ((AlgorithmProvider)class_.newInstance()).configure(this);
            }
            ++n;
        }
        return;
        catch (Exception exception) {
            throw new InternalError("cannot create instance of " + string + arrstring[n] + "$Mappings : " + (Object)((Object)exception));
        }
    }

    private void setup() {
        this.loadAlgorithms(ALGORITHM_PACKAGE, ALGORITHMS);
    }

    @Override
    public void addAlgorithm(String string, String string2) {
        if (this.containsKey((Object)string)) {
            throw new IllegalStateException("duplicate provider key (" + string + ") found");
        }
        this.put((Object)string, (Object)string2);
    }

    @Override
    public void addKeyInfoConverter(ASN1ObjectIdentifier aSN1ObjectIdentifier, AsymmetricKeyInfoConverter asymmetricKeyInfoConverter) {
        keyInfoConverters.put((Object)aSN1ObjectIdentifier, (Object)asymmetricKeyInfoConverter);
    }

    @Override
    public boolean hasAlgorithm(String string, String string2) {
        return this.containsKey((Object)(string + "." + string2)) || this.containsKey((Object)("Alg.Alias." + string + "." + string2));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void setParameter(String string, Object object) {
        ProviderConfiguration providerConfiguration;
        ProviderConfiguration providerConfiguration2 = providerConfiguration = CONFIGURATION;
        // MONITORENTER : providerConfiguration2
        // MONITOREXIT : providerConfiguration2
    }

}

