/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.spec.PBEKeySpec
 *  javax.crypto.spec.PBEParameterSpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.TigerDigest;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;

public interface PBE {
    public static final int GOST3411 = 6;
    public static final int MD2 = 5;
    public static final int MD5 = 0;
    public static final int OPENSSL = 3;
    public static final int PKCS12 = 2;
    public static final int PKCS5S1 = 0;
    public static final int PKCS5S1_UTF8 = 4;
    public static final int PKCS5S2 = 1;
    public static final int PKCS5S2_UTF8 = 5;
    public static final int RIPEMD160 = 2;
    public static final int SHA1 = 1;
    public static final int SHA256 = 4;
    public static final int TIGER = 3;

    public static class Util {
        private static byte[] convertPassword(int n, PBEKeySpec pBEKeySpec) {
            if (n == 2) {
                return PBEParametersGenerator.PKCS12PasswordToBytes(pBEKeySpec.getPassword());
            }
            if (n == 5 || n == 4) {
                return PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(pBEKeySpec.getPassword());
            }
            return PBEParametersGenerator.PKCS5PasswordToBytes(pBEKeySpec.getPassword());
        }

        private static PBEParametersGenerator makePBEGenerator(int n, int n2) {
            if (n == 0 || n == 4) {
                switch (n2) {
                    default: {
                        throw new IllegalStateException("PKCS5 scheme 1 only supports MD2, MD5 and SHA1.");
                    }
                    case 5: {
                        return new PKCS5S1ParametersGenerator(new MD2Digest());
                    }
                    case 0: {
                        return new PKCS5S1ParametersGenerator(new MD5Digest());
                    }
                    case 1: 
                }
                return new PKCS5S1ParametersGenerator(new SHA1Digest());
            }
            if (n == 1 || n == 5) {
                switch (n2) {
                    default: {
                        throw new IllegalStateException("unknown digest scheme for PBE PKCS5S2 encryption.");
                    }
                    case 5: {
                        return new PKCS5S2ParametersGenerator(new MD2Digest());
                    }
                    case 0: {
                        return new PKCS5S2ParametersGenerator(new MD5Digest());
                    }
                    case 1: {
                        return new PKCS5S2ParametersGenerator(new SHA1Digest());
                    }
                    case 2: {
                        return new PKCS5S2ParametersGenerator(new RIPEMD160Digest());
                    }
                    case 3: {
                        return new PKCS5S2ParametersGenerator(new TigerDigest());
                    }
                    case 4: {
                        return new PKCS5S2ParametersGenerator(new SHA256Digest());
                    }
                    case 6: 
                }
                return new PKCS5S2ParametersGenerator(new GOST3411Digest());
            }
            if (n == 2) {
                switch (n2) {
                    default: {
                        throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                    }
                    case 5: {
                        return new PKCS12ParametersGenerator(new MD2Digest());
                    }
                    case 0: {
                        return new PKCS12ParametersGenerator(new MD5Digest());
                    }
                    case 1: {
                        return new PKCS12ParametersGenerator(new SHA1Digest());
                    }
                    case 2: {
                        return new PKCS12ParametersGenerator(new RIPEMD160Digest());
                    }
                    case 3: {
                        return new PKCS12ParametersGenerator(new TigerDigest());
                    }
                    case 4: {
                        return new PKCS12ParametersGenerator(new SHA256Digest());
                    }
                    case 6: 
                }
                return new PKCS12ParametersGenerator(new GOST3411Digest());
            }
            return new OpenSSLPBEParametersGenerator();
        }

        public static CipherParameters makePBEMacParameters(PBEKeySpec pBEKeySpec, int n, int n2, int n3) {
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(n, n2);
            byte[] arrby = Util.convertPassword(n, pBEKeySpec);
            pBEParametersGenerator.init(arrby, pBEKeySpec.getSalt(), pBEKeySpec.getIterationCount());
            CipherParameters cipherParameters = pBEParametersGenerator.generateDerivedMacParameters(n3);
            for (int i = 0; i != arrby.length; ++i) {
                arrby[i] = 0;
            }
            return cipherParameters;
        }

        public static CipherParameters makePBEMacParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(bCPBEKey.getType(), bCPBEKey.getDigest());
            byte[] arrby = bCPBEKey.getEncoded();
            if (bCPBEKey.shouldTryWrongPKCS12()) {
                arrby = new byte[2];
            }
            pBEParametersGenerator.init(arrby, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParameters = pBEParametersGenerator.generateDerivedMacParameters(bCPBEKey.getKeySize());
            for (int i = 0; i != arrby.length; ++i) {
                arrby[i] = 0;
            }
            return cipherParameters;
        }

        /*
         * Enabled aggressive block sorting
         */
        public static CipherParameters makePBEParameters(PBEKeySpec pBEKeySpec, int n, int n2, int n3, int n4) {
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(n, n2);
            byte[] arrby = Util.convertPassword(n, pBEKeySpec);
            pBEParametersGenerator.init(arrby, pBEKeySpec.getSalt(), pBEKeySpec.getIterationCount());
            CipherParameters cipherParameters = n4 != 0 ? pBEParametersGenerator.generateDerivedParameters(n3, n4) : pBEParametersGenerator.generateDerivedParameters(n3);
            int n5 = 0;
            while (n5 != arrby.length) {
                arrby[n5] = 0;
                ++n5;
            }
            return cipherParameters;
        }

        /*
         * Enabled aggressive block sorting
         */
        public static CipherParameters makePBEParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec, String string) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            PBEParametersGenerator pBEParametersGenerator = Util.makePBEGenerator(bCPBEKey.getType(), bCPBEKey.getDigest());
            byte[] arrby = bCPBEKey.getEncoded();
            byte[] arrby2 = bCPBEKey.shouldTryWrongPKCS12() ? new byte[2] : arrby;
            pBEParametersGenerator.init(arrby2, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters cipherParameters = bCPBEKey.getIvSize() != 0 ? pBEParametersGenerator.generateDerivedParameters(bCPBEKey.getKeySize(), bCPBEKey.getIvSize()) : pBEParametersGenerator.generateDerivedParameters(bCPBEKey.getKeySize());
            if (string.startsWith("DES")) {
                if (cipherParameters instanceof ParametersWithIV) {
                    DESParameters.setOddParity(((KeyParameter)((ParametersWithIV)cipherParameters).getParameters()).getKey());
                } else {
                    DESParameters.setOddParity(((KeyParameter)cipherParameters).getKey());
                }
            }
            int n = 0;
            while (n != arrby2.length) {
                arrby2[n] = 0;
                ++n;
            }
            return cipherParameters;
        }
    }

}

