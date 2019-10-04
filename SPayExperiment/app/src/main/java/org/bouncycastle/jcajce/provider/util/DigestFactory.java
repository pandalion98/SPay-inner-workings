/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Map
 *  java.util.Set
 */
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
import org.bouncycastle.util.Strings;

public class DigestFactory {
    private static Set md5 = new HashSet();
    private static Map oids;
    private static Set sha1;
    private static Set sha224;
    private static Set sha256;
    private static Set sha384;
    private static Set sha512;

    static {
        sha1 = new HashSet();
        sha224 = new HashSet();
        sha256 = new HashSet();
        sha384 = new HashSet();
        sha512 = new HashSet();
        oids = new HashMap();
        md5.add((Object)"MD5");
        md5.add((Object)PKCSObjectIdentifiers.md5.getId());
        sha1.add((Object)"SHA1");
        sha1.add((Object)"SHA-1");
        sha1.add((Object)OIWObjectIdentifiers.idSHA1.getId());
        sha224.add((Object)"SHA224");
        sha224.add((Object)"SHA-224");
        sha224.add((Object)NISTObjectIdentifiers.id_sha224.getId());
        sha256.add((Object)"SHA256");
        sha256.add((Object)"SHA-256");
        sha256.add((Object)NISTObjectIdentifiers.id_sha256.getId());
        sha384.add((Object)"SHA384");
        sha384.add((Object)"SHA-384");
        sha384.add((Object)NISTObjectIdentifiers.id_sha384.getId());
        sha512.add((Object)"SHA512");
        sha512.add((Object)"SHA-512");
        sha512.add((Object)NISTObjectIdentifiers.id_sha512.getId());
        oids.put((Object)"MD5", (Object)PKCSObjectIdentifiers.md5);
        oids.put((Object)PKCSObjectIdentifiers.md5.getId(), (Object)PKCSObjectIdentifiers.md5);
        oids.put((Object)"SHA1", (Object)OIWObjectIdentifiers.idSHA1);
        oids.put((Object)"SHA-1", (Object)OIWObjectIdentifiers.idSHA1);
        oids.put((Object)OIWObjectIdentifiers.idSHA1.getId(), (Object)OIWObjectIdentifiers.idSHA1);
        oids.put((Object)"SHA224", (Object)NISTObjectIdentifiers.id_sha224);
        oids.put((Object)"SHA-224", (Object)NISTObjectIdentifiers.id_sha224);
        oids.put((Object)NISTObjectIdentifiers.id_sha224.getId(), (Object)NISTObjectIdentifiers.id_sha224);
        oids.put((Object)"SHA256", (Object)NISTObjectIdentifiers.id_sha256);
        oids.put((Object)"SHA-256", (Object)NISTObjectIdentifiers.id_sha256);
        oids.put((Object)NISTObjectIdentifiers.id_sha256.getId(), (Object)NISTObjectIdentifiers.id_sha256);
        oids.put((Object)"SHA384", (Object)NISTObjectIdentifiers.id_sha384);
        oids.put((Object)"SHA-384", (Object)NISTObjectIdentifiers.id_sha384);
        oids.put((Object)NISTObjectIdentifiers.id_sha384.getId(), (Object)NISTObjectIdentifiers.id_sha384);
        oids.put((Object)"SHA512", (Object)NISTObjectIdentifiers.id_sha512);
        oids.put((Object)"SHA-512", (Object)NISTObjectIdentifiers.id_sha512);
        oids.put((Object)NISTObjectIdentifiers.id_sha512.getId(), (Object)NISTObjectIdentifiers.id_sha512);
    }

    public static Digest getDigest(String string) {
        String string2 = Strings.toUpperCase(string);
        if (sha1.contains((Object)string2)) {
            return new SHA1Digest();
        }
        if (md5.contains((Object)string2)) {
            return new MD5Digest();
        }
        if (sha224.contains((Object)string2)) {
            return new SHA224Digest();
        }
        if (sha256.contains((Object)string2)) {
            return new SHA256Digest();
        }
        if (sha384.contains((Object)string2)) {
            return new SHA384Digest();
        }
        if (sha512.contains((Object)string2)) {
            return new SHA512Digest();
        }
        return null;
    }

    public static ASN1ObjectIdentifier getOID(String string) {
        return (ASN1ObjectIdentifier)oids.get((Object)string);
    }

    public static boolean isSameDigest(String string, String string2) {
        return sha1.contains((Object)string) && sha1.contains((Object)string2) || sha224.contains((Object)string) && sha224.contains((Object)string2) || sha256.contains((Object)string) && sha256.contains((Object)string2) || sha384.contains((Object)string) && sha384.contains((Object)string2) || sha512.contains((Object)string) && sha512.contains((Object)string2) || md5.contains((Object)string) && md5.contains((Object)string2);
    }
}

