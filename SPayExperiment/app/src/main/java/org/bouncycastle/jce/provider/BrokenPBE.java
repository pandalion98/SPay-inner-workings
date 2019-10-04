/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.spec.PBEParameterSpec
 */
package org.bouncycastle.jce.provider;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jce.provider.OldPKCS12ParametersGenerator;

public interface BrokenPBE {
    public static final int MD5 = 0;
    public static final int OLD_PKCS12 = 3;
    public static final int PKCS12 = 2;
    public static final int PKCS5S1 = 0;
    public static final int PKCS5S2 = 1;
    public static final int RIPEMD160 = 2;
    public static final int SHA1 = 1;

    public static class Util {
        private static PBEParametersGenerator makePBEGenerator(int n, int n2) {
            if (n == 0) {
                switch (n2) {
                    default: {
                        throw new IllegalStateException("PKCS5 scheme 1 only supports only MD5 and SHA1.");
                    }
                    case 0: {
                        return new PKCS5S1ParametersGenerator(new MD5Digest());
                    }
                    case 1: 
                }
                return new PKCS5S1ParametersGenerator(new SHA1Digest());
            }
            if (n == 1) {
                return new PKCS5S2ParametersGenerator();
            }
            if (n == 3) {
                switch (n2) {
                    default: {
                        throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                    }
                    case 0: {
                        return new OldPKCS12ParametersGenerator(new MD5Digest());
                    }
                    case 1: {
                        return new OldPKCS12ParametersGenerator(new SHA1Digest());
                    }
                    case 2: 
                }
                return new OldPKCS12ParametersGenerator(new RIPEMD160Digest());
            }
            switch (n2) {
                default: {
                    throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                }
                case 0: {
                    return new PKCS12ParametersGenerator(new MD5Digest());
                }
                case 1: {
                    return new PKCS12ParametersGenerator(new SHA1Digest());
                }
                case 2: 
            }
            return new PKCS12ParametersGenerator(new RIPEMD160Digest());
        }

        static CipherParameters makePBEMacParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec, int n, int n2, int n3) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(n, n2);
            byte[] arrby = bCPBEKey.getEncoded();
            pBEParametersGenerator.init(arrby, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParameters = pBEParametersGenerator.generateDerivedMacParameters(n3);
            for (int i = 0; i != arrby.length; ++i) {
                arrby[i] = 0;
            }
            return cipherParameters;
        }

        /*
         * Enabled aggressive block sorting
         */
        static CipherParameters makePBEParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec, int n, int n2, String string, int n3, int n4) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(n, n2);
            byte[] arrby = bCPBEKey.getEncoded();
            pBEParametersGenerator.init(arrby, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParameters = n4 != 0 ? pBEParametersGenerator.generateDerivedParameters(n3, n4) : pBEParametersGenerator.generateDerivedParameters(n3);
            if (string.startsWith("DES")) {
                if (cipherParameters instanceof ParametersWithIV) {
                    Util.setOddParity(((KeyParameter)((ParametersWithIV)cipherParameters).getParameters()).getKey());
                } else {
                    Util.setOddParity(((KeyParameter)cipherParameters).getKey());
                }
            }
            int n5 = 0;
            while (n5 != arrby.length) {
                arrby[n5] = 0;
                ++n5;
            }
            return cipherParameters;
        }

        private static void setOddParity(byte[] arrby) {
            for (int i = 0; i < arrby.length; ++i) {
                byte by = arrby[i];
                arrby[i] = (byte)(by & 254 | 1 ^ (by >> 1 ^ by >> 2 ^ by >> 3 ^ by >> 4 ^ by >> 5 ^ by >> 6 ^ by >> 7));
            }
        }
    }

}

