package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
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
        private static byte[] convertPassword(int i, PBEKeySpec pBEKeySpec) {
            return i == PBE.RIPEMD160 ? PBEParametersGenerator.PKCS12PasswordToBytes(pBEKeySpec.getPassword()) : (i == PBE.PKCS5S2_UTF8 || i == PBE.SHA256) ? PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(pBEKeySpec.getPassword()) : PBEParametersGenerator.PKCS5PasswordToBytes(pBEKeySpec.getPassword());
        }

        private static PBEParametersGenerator makePBEGenerator(int i, int i2) {
            if (i == 0 || i == PBE.SHA256) {
                switch (i2) {
                    case PBE.PKCS5S1 /*0*/:
                        return new PKCS5S1ParametersGenerator(new MD5Digest());
                    case PBE.SHA1 /*1*/:
                        return new PKCS5S1ParametersGenerator(new SHA1Digest());
                    case PBE.PKCS5S2_UTF8 /*5*/:
                        return new PKCS5S1ParametersGenerator(new MD2Digest());
                    default:
                        throw new IllegalStateException("PKCS5 scheme 1 only supports MD2, MD5 and SHA1.");
                }
            } else if (i == PBE.SHA1 || i == PBE.PKCS5S2_UTF8) {
                switch (i2) {
                    case PBE.PKCS5S1 /*0*/:
                        return new PKCS5S2ParametersGenerator(new MD5Digest());
                    case PBE.SHA1 /*1*/:
                        return new PKCS5S2ParametersGenerator(new SHA1Digest());
                    case PBE.RIPEMD160 /*2*/:
                        return new PKCS5S2ParametersGenerator(new RIPEMD160Digest());
                    case PBE.TIGER /*3*/:
                        return new PKCS5S2ParametersGenerator(new TigerDigest());
                    case PBE.SHA256 /*4*/:
                        return new PKCS5S2ParametersGenerator(new SHA256Digest());
                    case PBE.PKCS5S2_UTF8 /*5*/:
                        return new PKCS5S2ParametersGenerator(new MD2Digest());
                    case PBE.GOST3411 /*6*/:
                        return new PKCS5S2ParametersGenerator(new GOST3411Digest());
                    default:
                        throw new IllegalStateException("unknown digest scheme for PBE PKCS5S2 encryption.");
                }
            } else if (i != PBE.RIPEMD160) {
                return new OpenSSLPBEParametersGenerator();
            } else {
                switch (i2) {
                    case PBE.PKCS5S1 /*0*/:
                        return new PKCS12ParametersGenerator(new MD5Digest());
                    case PBE.SHA1 /*1*/:
                        return new PKCS12ParametersGenerator(new SHA1Digest());
                    case PBE.RIPEMD160 /*2*/:
                        return new PKCS12ParametersGenerator(new RIPEMD160Digest());
                    case PBE.TIGER /*3*/:
                        return new PKCS12ParametersGenerator(new TigerDigest());
                    case PBE.SHA256 /*4*/:
                        return new PKCS12ParametersGenerator(new SHA256Digest());
                    case PBE.PKCS5S2_UTF8 /*5*/:
                        return new PKCS12ParametersGenerator(new MD2Digest());
                    case PBE.GOST3411 /*6*/:
                        return new PKCS12ParametersGenerator(new GOST3411Digest());
                    default:
                        throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                }
            }
        }

        public static CipherParameters makePBEMacParameters(PBEKeySpec pBEKeySpec, int i, int i2, int i3) {
            PBEParametersGenerator makePBEGenerator = makePBEGenerator(i, i2);
            byte[] convertPassword = convertPassword(i, pBEKeySpec);
            makePBEGenerator.init(convertPassword, pBEKeySpec.getSalt(), pBEKeySpec.getIterationCount());
            CipherParameters generateDerivedMacParameters = makePBEGenerator.generateDerivedMacParameters(i3);
            for (int i4 = PBE.PKCS5S1; i4 != convertPassword.length; i4 += PBE.SHA1) {
                convertPassword[i4] = (byte) 0;
            }
            return generateDerivedMacParameters;
        }

        public static CipherParameters makePBEMacParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec) algorithmParameterSpec;
            PBEParametersGenerator makePBEGenerator = makePBEGenerator(bCPBEKey.getType(), bCPBEKey.getDigest());
            byte[] encoded = bCPBEKey.getEncoded();
            if (bCPBEKey.shouldTryWrongPKCS12()) {
                encoded = new byte[PBE.RIPEMD160];
            }
            makePBEGenerator.init(encoded, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters generateDerivedMacParameters = makePBEGenerator.generateDerivedMacParameters(bCPBEKey.getKeySize());
            for (int i = PBE.PKCS5S1; i != encoded.length; i += PBE.SHA1) {
                encoded[i] = (byte) 0;
            }
            return generateDerivedMacParameters;
        }

        public static CipherParameters makePBEParameters(PBEKeySpec pBEKeySpec, int i, int i2, int i3, int i4) {
            PBEParametersGenerator makePBEGenerator = makePBEGenerator(i, i2);
            byte[] convertPassword = convertPassword(i, pBEKeySpec);
            makePBEGenerator.init(convertPassword, pBEKeySpec.getSalt(), pBEKeySpec.getIterationCount());
            CipherParameters generateDerivedParameters = i4 != 0 ? makePBEGenerator.generateDerivedParameters(i3, i4) : makePBEGenerator.generateDerivedParameters(i3);
            for (int i5 = PBE.PKCS5S1; i5 != convertPassword.length; i5 += PBE.SHA1) {
                convertPassword[i5] = (byte) 0;
            }
            return generateDerivedParameters;
        }

        public static CipherParameters makePBEParameters(BCPBEKey bCPBEKey, AlgorithmParameterSpec algorithmParameterSpec, String str) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            PBEParameterSpec pBEParameterSpec = (PBEParameterSpec) algorithmParameterSpec;
            PBEParametersGenerator makePBEGenerator = makePBEGenerator(bCPBEKey.getType(), bCPBEKey.getDigest());
            byte[] encoded = bCPBEKey.shouldTryWrongPKCS12() ? new byte[PBE.RIPEMD160] : bCPBEKey.getEncoded();
            makePBEGenerator.init(encoded, pBEParameterSpec.getSalt(), pBEParameterSpec.getIterationCount());
            CipherParameters generateDerivedParameters = bCPBEKey.getIvSize() != 0 ? makePBEGenerator.generateDerivedParameters(bCPBEKey.getKeySize(), bCPBEKey.getIvSize()) : makePBEGenerator.generateDerivedParameters(bCPBEKey.getKeySize());
            if (str.startsWith("DES")) {
                if (generateDerivedParameters instanceof ParametersWithIV) {
                    DESParameters.setOddParity(((KeyParameter) ((ParametersWithIV) generateDerivedParameters).getParameters()).getKey());
                } else {
                    DESParameters.setOddParity(((KeyParameter) generateDerivedParameters).getKey());
                }
            }
            for (int i = PBE.PKCS5S1; i != encoded.length; i += PBE.SHA1) {
                encoded[i] = (byte) 0;
            }
            return generateDerivedParameters;
        }
    }
}
