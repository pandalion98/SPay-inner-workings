/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package org.bouncycastle.jcajce.provider.util;

import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.ntt.NTTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.util.Integers;

public class SecretKeyUtil {
    private static Map keySizes = new HashMap();

    static {
        keySizes.put((Object)PKCSObjectIdentifiers.des_EDE3_CBC.getId(), (Object)Integers.valueOf(192));
        keySizes.put((Object)NISTObjectIdentifiers.id_aes128_CBC, (Object)Integers.valueOf(128));
        keySizes.put((Object)NISTObjectIdentifiers.id_aes192_CBC, (Object)Integers.valueOf(192));
        keySizes.put((Object)NISTObjectIdentifiers.id_aes256_CBC, (Object)Integers.valueOf(256));
        keySizes.put((Object)NTTObjectIdentifiers.id_camellia128_cbc, (Object)Integers.valueOf(128));
        keySizes.put((Object)NTTObjectIdentifiers.id_camellia192_cbc, (Object)Integers.valueOf(192));
        keySizes.put((Object)NTTObjectIdentifiers.id_camellia256_cbc, (Object)Integers.valueOf(256));
    }

    public static int getKeySize(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        Integer n = (Integer)keySizes.get((Object)aSN1ObjectIdentifier);
        if (n != null) {
            return n;
        }
        return -1;
    }
}

