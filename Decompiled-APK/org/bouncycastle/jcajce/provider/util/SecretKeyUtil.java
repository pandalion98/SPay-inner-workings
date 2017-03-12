package org.bouncycastle.jcajce.provider.util;

import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.ntt.NTTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.util.Integers;

public class SecretKeyUtil {
    private static Map keySizes;

    static {
        keySizes = new HashMap();
        keySizes.put(PKCSObjectIdentifiers.des_EDE3_CBC.getId(), Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256));
        keySizes.put(NISTObjectIdentifiers.id_aes128_CBC, Integers.valueOf(X509KeyUsage.digitalSignature));
        keySizes.put(NISTObjectIdentifiers.id_aes192_CBC, Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256));
        keySizes.put(NISTObjectIdentifiers.id_aes256_CBC, Integers.valueOf(SkeinMac.SKEIN_256));
        keySizes.put(NTTObjectIdentifiers.id_camellia128_cbc, Integers.valueOf(X509KeyUsage.digitalSignature));
        keySizes.put(NTTObjectIdentifiers.id_camellia192_cbc, Integers.valueOf(CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256));
        keySizes.put(NTTObjectIdentifiers.id_camellia256_cbc, Integers.valueOf(SkeinMac.SKEIN_256));
    }

    public static int getKeySize(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Integer num = (Integer) keySizes.get(aSN1ObjectIdentifier);
        return num != null ? num.intValue() : -1;
    }
}
