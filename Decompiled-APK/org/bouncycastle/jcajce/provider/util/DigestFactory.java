package org.bouncycastle.jcajce.provider.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2ParameterSpec;
import org.bouncycastle.util.Strings;

public class DigestFactory {
    private static Set md5;
    private static Map oids;
    private static Set sha1;
    private static Set sha224;
    private static Set sha256;
    private static Set sha384;
    private static Set sha512;

    static {
        md5 = new HashSet();
        sha1 = new HashSet();
        sha224 = new HashSet();
        sha256 = new HashSet();
        sha384 = new HashSet();
        sha512 = new HashSet();
        oids = new HashMap();
        md5.add("MD5");
        md5.add(PKCSObjectIdentifiers.md5.getId());
        sha1.add("SHA1");
        sha1.add("SHA-1");
        sha1.add(OIWObjectIdentifiers.idSHA1.getId());
        sha224.add("SHA224");
        sha224.add("SHA-224");
        sha224.add(NISTObjectIdentifiers.id_sha224.getId());
        sha256.add(McElieceCCA2ParameterSpec.DEFAULT_MD);
        sha256.add("SHA-256");
        sha256.add(NISTObjectIdentifiers.id_sha256.getId());
        sha384.add("SHA384");
        sha384.add("SHA-384");
        sha384.add(NISTObjectIdentifiers.id_sha384.getId());
        sha512.add("SHA512");
        sha512.add("SHA-512");
        sha512.add(NISTObjectIdentifiers.id_sha512.getId());
        oids.put("MD5", PKCSObjectIdentifiers.md5);
        oids.put(PKCSObjectIdentifiers.md5.getId(), PKCSObjectIdentifiers.md5);
        oids.put("SHA1", OIWObjectIdentifiers.idSHA1);
        oids.put("SHA-1", OIWObjectIdentifiers.idSHA1);
        oids.put(OIWObjectIdentifiers.idSHA1.getId(), OIWObjectIdentifiers.idSHA1);
        oids.put("SHA224", NISTObjectIdentifiers.id_sha224);
        oids.put("SHA-224", NISTObjectIdentifiers.id_sha224);
        oids.put(NISTObjectIdentifiers.id_sha224.getId(), NISTObjectIdentifiers.id_sha224);
        oids.put(McElieceCCA2ParameterSpec.DEFAULT_MD, NISTObjectIdentifiers.id_sha256);
        oids.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        oids.put(NISTObjectIdentifiers.id_sha256.getId(), NISTObjectIdentifiers.id_sha256);
        oids.put("SHA384", NISTObjectIdentifiers.id_sha384);
        oids.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        oids.put(NISTObjectIdentifiers.id_sha384.getId(), NISTObjectIdentifiers.id_sha384);
        oids.put("SHA512", NISTObjectIdentifiers.id_sha512);
        oids.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        oids.put(NISTObjectIdentifiers.id_sha512.getId(), NISTObjectIdentifiers.id_sha512);
    }

    public static Digest getDigest(String str) {
        String toUpperCase = Strings.toUpperCase(str);
        return sha1.contains(toUpperCase) ? new SHA1Digest() : md5.contains(toUpperCase) ? new MD5Digest() : sha224.contains(toUpperCase) ? new SHA224Digest() : sha256.contains(toUpperCase) ? new SHA256Digest() : sha384.contains(toUpperCase) ? new SHA384Digest() : sha512.contains(toUpperCase) ? new SHA512Digest() : null;
    }

    public static ASN1ObjectIdentifier getOID(String str) {
        return (ASN1ObjectIdentifier) oids.get(str);
    }

    public static boolean isSameDigest(String str, String str2) {
        return (sha1.contains(str) && sha1.contains(str2)) || ((sha224.contains(str) && sha224.contains(str2)) || ((sha256.contains(str) && sha256.contains(str2)) || ((sha384.contains(str) && sha384.contains(str2)) || ((sha512.contains(str) && sha512.contains(str2)) || (md5.contains(str) && md5.contains(str2))))));
    }
}
