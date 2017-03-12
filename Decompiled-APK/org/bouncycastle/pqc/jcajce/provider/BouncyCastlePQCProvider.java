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
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jcajce.provider.util.AlgorithmProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;

public class BouncyCastlePQCProvider extends Provider implements ConfigurableProvider {
    private static final String[] ALGORITHMS;
    private static final String ALGORITHM_PACKAGE = "org.bouncycastle.pqc.jcajce.provider.";
    public static final ProviderConfiguration CONFIGURATION;
    public static String PROVIDER_NAME;
    private static String info;
    private static final Map keyInfoConverters;

    /* renamed from: org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider.1 */
    class C07721 implements PrivilegedAction {
        C07721() {
        }

        public Object run() {
            BouncyCastlePQCProvider.this.setup();
            return null;
        }
    }

    static {
        info = "BouncyCastle Post-Quantum Security Provider v1.52";
        PROVIDER_NAME = "BCPQC";
        CONFIGURATION = null;
        keyInfoConverters = new HashMap();
        ALGORITHMS = new String[]{"Rainbow", "McEliece"};
    }

    public BouncyCastlePQCProvider() {
        super(PROVIDER_NAME, 1.52d, info);
        AccessController.doPrivileged(new C07721());
    }

    public static PrivateKey getPrivateKey(PrivateKeyInfo privateKeyInfo) {
        AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = (AsymmetricKeyInfoConverter) keyInfoConverters.get(privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
        return asymmetricKeyInfoConverter == null ? null : asymmetricKeyInfoConverter.generatePrivate(privateKeyInfo);
    }

    public static PublicKey getPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = (AsymmetricKeyInfoConverter) keyInfoConverters.get(subjectPublicKeyInfo.getAlgorithm().getAlgorithm());
        return asymmetricKeyInfoConverter == null ? null : asymmetricKeyInfoConverter.generatePublic(subjectPublicKeyInfo);
    }

    private void loadAlgorithms(String str, String[] strArr) {
        for (int i = 0; i != strArr.length; i++) {
            Class cls = null;
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                cls = classLoader != null ? classLoader.loadClass(str + strArr[i] + "$Mappings") : Class.forName(str + strArr[i] + "$Mappings");
            } catch (ClassNotFoundException e) {
            }
            if (cls != null) {
                try {
                    ((AlgorithmProvider) cls.newInstance()).configure(this);
                } catch (Exception e2) {
                    throw new InternalError("cannot create instance of " + str + strArr[i] + "$Mappings : " + e2);
                }
            }
        }
    }

    private void setup() {
        loadAlgorithms(ALGORITHM_PACKAGE, ALGORITHMS);
    }

    public void addAlgorithm(String str, String str2) {
        if (containsKey(str)) {
            throw new IllegalStateException("duplicate provider key (" + str + ") found");
        }
        put(str, str2);
    }

    public void addKeyInfoConverter(ASN1ObjectIdentifier aSN1ObjectIdentifier, AsymmetricKeyInfoConverter asymmetricKeyInfoConverter) {
        keyInfoConverters.put(aSN1ObjectIdentifier, asymmetricKeyInfoConverter);
    }

    public boolean hasAlgorithm(String str, String str2) {
        return containsKey(new StringBuilder().append(str).append(".").append(str2).toString()) || containsKey("Alg.Alias." + str + "." + str2);
    }

    public void setParameter(String str, Object obj) {
        synchronized (CONFIGURATION) {
        }
    }
}
