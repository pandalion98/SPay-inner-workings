package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Arrays;

public class TlsBlockCipher implements TlsCipher {
    protected TlsContext context;
    protected BlockCipher decryptCipher;
    protected BlockCipher encryptCipher;
    protected boolean encryptThenMAC;
    protected byte[] randomData;
    protected TlsMac readMac;
    protected boolean useExplicitIV;
    protected TlsMac writeMac;

    public TlsBlockCipher(TlsContext tlsContext, BlockCipher blockCipher, BlockCipher blockCipher2, Digest digest, Digest digest2, int i) {
        byte[] bArr;
        this.context = tlsContext;
        this.randomData = new byte[SkeinMac.SKEIN_256];
        tlsContext.getNonceRandomGenerator().nextBytes(this.randomData);
        this.useExplicitIV = TlsUtils.isTLSv11(tlsContext);
        this.encryptThenMAC = tlsContext.getSecurityParameters().encryptThenMAC;
        int digestSize = ((i * 2) + digest.getDigestSize()) + digest2.getDigestSize();
        int blockSize = !this.useExplicitIV ? digestSize + (blockCipher.getBlockSize() + blockCipher2.getBlockSize()) : digestSize;
        byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, blockSize);
        TlsMac tlsMac = new TlsMac(tlsContext, digest, calculateKeyBlock, 0, digest.getDigestSize());
        int digestSize2 = 0 + digest.getDigestSize();
        TlsMac tlsMac2 = new TlsMac(tlsContext, digest2, calculateKeyBlock, digestSize2, digest2.getDigestSize());
        int digestSize3 = digest2.getDigestSize() + digestSize2;
        CipherParameters keyParameter = new KeyParameter(calculateKeyBlock, digestSize3, i);
        digestSize3 += i;
        CipherParameters keyParameter2 = new KeyParameter(calculateKeyBlock, digestSize3, i);
        int i2 = digestSize3 + i;
        if (this.useExplicitIV) {
            calculateKeyBlock = new byte[blockCipher.getBlockSize()];
            bArr = new byte[blockCipher2.getBlockSize()];
            digestSize3 = i2;
        } else {
            bArr = Arrays.copyOfRange(calculateKeyBlock, i2, blockCipher.getBlockSize() + i2);
            i2 += blockCipher.getBlockSize();
            byte[] copyOfRange = Arrays.copyOfRange(calculateKeyBlock, i2, blockCipher2.getBlockSize() + i2);
            digestSize3 = blockCipher2.getBlockSize() + i2;
            calculateKeyBlock = bArr;
            bArr = copyOfRange;
        }
        if (digestSize3 != blockSize) {
            throw new TlsFatalAlert((short) 80);
        }
        CipherParameters parametersWithIV;
        CipherParameters parametersWithIV2;
        if (tlsContext.isServer()) {
            this.writeMac = tlsMac2;
            this.readMac = tlsMac;
            this.encryptCipher = blockCipher2;
            this.decryptCipher = blockCipher;
            parametersWithIV = new ParametersWithIV(keyParameter2, bArr);
            parametersWithIV2 = new ParametersWithIV(keyParameter, calculateKeyBlock);
        } else {
            this.writeMac = tlsMac;
            this.readMac = tlsMac2;
            this.encryptCipher = blockCipher;
            this.decryptCipher = blockCipher2;
            parametersWithIV = new ParametersWithIV(keyParameter, calculateKeyBlock);
            parametersWithIV2 = new ParametersWithIV(keyParameter2, bArr);
        }
        this.encryptCipher.init(true, parametersWithIV);
        this.decryptCipher.init(false, parametersWithIV2);
    }

    protected int checkPaddingConstantTime(byte[] bArr, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7 = i + i2;
        byte b = bArr[i7 - 1];
        int i8 = (b & GF2Field.MASK) + 1;
        if ((!TlsUtils.isSSL(this.context) || i8 <= i3) && i4 + i8 <= i2) {
            i5 = i7 - i8;
            int i9 = 0;
            while (true) {
                i6 = i5 + 1;
                i5 = (byte) ((bArr[i5] ^ b) | i9);
                if (i6 >= i7) {
                    break;
                }
                i9 = i5;
                i5 = i6;
            }
            if (i5 != 0) {
                i6 = i8;
                i8 = 0;
            } else {
                i6 = i8;
            }
        } else {
            i5 = (byte) 0;
            i6 = 0;
            i8 = 0;
        }
        byte[] bArr2 = this.randomData;
        for (i6 = 
        /* Method generation error in method: org.bouncycastle.crypto.tls.TlsBlockCipher.checkPaddingConstantTime(byte[], int, int, int, int):int
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r1_4 'i6' int) = (r1_1 'i6' int), (r1_2 'i6' int), (r1_3 'i6' int) binds: {(r1_1 'i6' int)=B:14:0x0039, (r1_2 'i6' int)=B:17:0x0043, (r1_3 'i6' int)=B:5:0x0019} in method: org.bouncycastle.crypto.tls.TlsBlockCipher.checkPaddingConstantTime(byte[], int, int, int, int):int
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:225)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:177)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:324)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:263)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:226)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:116)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:81)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.CodegenException: Unknown instruction: PHI in method: org.bouncycastle.crypto.tls.TlsBlockCipher.checkPaddingConstantTime(byte[], int, int, int, int):int
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:512)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:219)
	... 15 more
 */

        protected int chooseExtraPadBlocks(SecureRandom secureRandom, int i) {
            return Math.min(lowestBitSet(secureRandom.nextInt()), i);
        }

        public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) {
            int blockSize = this.decryptCipher.getBlockSize();
            int size = this.readMac.getSize();
            int max = this.encryptThenMAC ? blockSize + size : Math.max(blockSize, size + 1);
            if (this.useExplicitIV) {
                max += blockSize;
            }
            if (i2 < max) {
                throw new TlsFatalAlert((short) 50);
            }
            max = this.encryptThenMAC ? i2 - size : i2;
            if (max % blockSize != 0) {
                throw new TlsFatalAlert((short) 21);
            }
            int i3;
            int i4;
            int i5;
            if (this.encryptThenMAC) {
                i3 = i + i2;
                if ((!Arrays.constantTimeAreEqual(this.readMac.calculateMac(j, s, bArr, i, i2 - size), Arrays.copyOfRange(bArr, i3 - size, i3)) ? 1 : null) != null) {
                    throw new TlsFatalAlert((short) 20);
                }
            }
            if (this.useExplicitIV) {
                this.decryptCipher.init(false, new ParametersWithIV(null, bArr, i, blockSize));
                i4 = i + blockSize;
                i5 = max - blockSize;
            } else {
                i5 = max;
                i4 = i;
            }
            for (max = 0; max < i5; max += blockSize) {
                this.decryptCipher.processBlock(bArr, i4 + max, bArr, i4 + max);
            }
            i3 = checkPaddingConstantTime(bArr, i4, i5, blockSize, this.encryptThenMAC ? 0 : size);
            max = i3 == 0 ? 1 : 0;
            blockSize = i5 - i3;
            if (!this.encryptThenMAC) {
                blockSize -= size;
                i3 = i4 + blockSize;
                max |= !Arrays.constantTimeAreEqual(this.readMac.calculateMacConstantTime(j, s, bArr, i4, blockSize, i5 - size, this.randomData), Arrays.copyOfRange(bArr, i3, i3 + size)) ? 1 : 0;
            }
            if (max != 0) {
                throw new TlsFatalAlert((short) 20);
            }
            return Arrays.copyOfRange(bArr, i4, i4 + blockSize);
        }

        public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) {
            int i3;
            int blockSize = this.encryptCipher.getBlockSize();
            int size = this.writeMac.getSize();
            ProtocolVersion serverVersion = this.context.getServerVersion();
            int i4 = (blockSize - 1) - ((!this.encryptThenMAC ? i2 + size : i2) % blockSize);
            if (!(serverVersion.isDTLS() || serverVersion.isSSL())) {
                i4 += chooseExtraPadBlocks(this.context.getSecureRandom(), (255 - i4) / blockSize) * blockSize;
            }
            size = ((size + i2) + i4) + 1;
            if (this.useExplicitIV) {
                size += blockSize;
            }
            Object obj = new byte[size];
            if (this.useExplicitIV) {
                Object obj2 = new byte[blockSize];
                this.context.getNonceRandomGenerator().nextBytes(obj2);
                this.encryptCipher.init(true, new ParametersWithIV(null, obj2));
                System.arraycopy(obj2, 0, obj, 0, blockSize);
                i3 = 0 + blockSize;
            } else {
                i3 = 0;
            }
            System.arraycopy(bArr, i, obj, i3, i2);
            int i5 = i3 + i2;
            if (this.encryptThenMAC) {
                size = i5;
            } else {
                Object calculateMac = this.writeMac.calculateMac(j, s, bArr, i, i2);
                System.arraycopy(calculateMac, 0, obj, i5, calculateMac.length);
                size = calculateMac.length + i5;
            }
            int i6 = size;
            size = 0;
            while (size <= i4) {
                int i7 = i6 + 1;
                obj[i6] = (byte) i4;
                size++;
                i6 = i7;
            }
            while (i3 < i6) {
                this.encryptCipher.processBlock(obj, i3, obj, i3);
                i3 += blockSize;
            }
            if (this.encryptThenMAC) {
                Object calculateMac2 = this.writeMac.calculateMac(j, s, obj, 0, i6);
                System.arraycopy(calculateMac2, 0, obj, i6, calculateMac2.length);
                i4 = calculateMac2.length + i6;
            }
            return obj;
        }

        public int getPlaintextLimit(int i) {
            int blockSize = this.encryptCipher.getBlockSize();
            int size = this.writeMac.getSize();
            if (this.useExplicitIV) {
                i -= blockSize;
            }
            if (this.encryptThenMAC) {
                size = i - size;
                blockSize = size - (size % blockSize);
            } else {
                blockSize = (i - (i % blockSize)) - size;
            }
            return blockSize - 1;
        }

        public TlsMac getReadMac() {
            return this.readMac;
        }

        public TlsMac getWriteMac() {
            return this.writeMac;
        }

        protected int lowestBitSet(int i) {
            if (i == 0) {
                return 32;
            }
            int i2 = 0;
            while ((i & 1) == 0) {
                i2++;
                i >>= 1;
            }
            return i2;
        }
    }
