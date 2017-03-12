package org.bouncycastle.pqc.crypto.gmss;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.MessageSigner;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSRandom;
import org.bouncycastle.pqc.crypto.gmss.util.GMSSUtil;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSVerify;
import org.bouncycastle.pqc.crypto.gmss.util.WinternitzOTSignature;
import org.bouncycastle.util.Arrays;

public class GMSSSigner implements MessageSigner {
    private byte[][][] currentAuthPaths;
    private GMSSDigestProvider digestProvider;
    private GMSSParameters gmssPS;
    private GMSSRandom gmssRandom;
    private GMSSUtil gmssUtil;
    private int[] index;
    GMSSKeyParameters key;
    private int mdLength;
    private Digest messDigestOTS;
    private Digest messDigestTrees;
    private int numLayer;
    private WinternitzOTSignature ots;
    private byte[] pubKeyBytes;
    private SecureRandom random;
    private byte[][] subtreeRootSig;

    public GMSSSigner(GMSSDigestProvider gMSSDigestProvider) {
        this.gmssUtil = new GMSSUtil();
        this.digestProvider = gMSSDigestProvider;
        this.messDigestTrees = gMSSDigestProvider.get();
        this.messDigestOTS = this.messDigestTrees;
        this.mdLength = this.messDigestTrees.getDigestSize();
        this.gmssRandom = new GMSSRandom(this.messDigestTrees);
    }

    private void initSign() {
        this.messDigestTrees.reset();
        GMSSPrivateKeyParameters gMSSPrivateKeyParameters = (GMSSPrivateKeyParameters) this.key;
        if (gMSSPrivateKeyParameters.isUsed()) {
            throw new IllegalStateException("Private key already used");
        } else if (gMSSPrivateKeyParameters.getIndex(0) >= gMSSPrivateKeyParameters.getNumLeafs(0)) {
            throw new IllegalStateException("No more signatures can be generated");
        } else {
            int i;
            this.gmssPS = gMSSPrivateKeyParameters.getParameters();
            this.numLayer = this.gmssPS.getNumOfLayers();
            byte[] bArr = new byte[this.mdLength];
            Object obj = new byte[this.mdLength];
            System.arraycopy(gMSSPrivateKeyParameters.getCurrentSeeds()[this.numLayer - 1], 0, obj, 0, this.mdLength);
            this.ots = new WinternitzOTSignature(this.gmssRandom.nextSeed(obj), this.digestProvider.get(), this.gmssPS.getWinternitzParameter()[this.numLayer - 1]);
            byte[][][] currentAuthPaths = gMSSPrivateKeyParameters.getCurrentAuthPaths();
            this.currentAuthPaths = new byte[this.numLayer][][];
            for (int i2 = 0; i2 < this.numLayer; i2++) {
                this.currentAuthPaths[i2] = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{currentAuthPaths[i2].length, this.mdLength});
                for (i = 0; i < currentAuthPaths[i2].length; i++) {
                    System.arraycopy(currentAuthPaths[i2][i], 0, this.currentAuthPaths[i2][i], 0, this.mdLength);
                }
            }
            this.index = new int[this.numLayer];
            System.arraycopy(gMSSPrivateKeyParameters.getIndex(), 0, this.index, 0, this.numLayer);
            this.subtreeRootSig = new byte[(this.numLayer - 1)][];
            for (i = 0; i < this.numLayer - 1; i++) {
                obj = gMSSPrivateKeyParameters.getSubtreeRootSig(i);
                this.subtreeRootSig[i] = new byte[obj.length];
                System.arraycopy(obj, 0, this.subtreeRootSig[i], 0, obj.length);
            }
            gMSSPrivateKeyParameters.markUsed();
        }
    }

    private void initVerify() {
        this.messDigestTrees.reset();
        GMSSPublicKeyParameters gMSSPublicKeyParameters = (GMSSPublicKeyParameters) this.key;
        this.pubKeyBytes = gMSSPublicKeyParameters.getPublicKey();
        this.gmssPS = gMSSPublicKeyParameters.getParameters();
        this.numLayer = this.gmssPS.getNumOfLayers();
    }

    public byte[] generateSignature(byte[] bArr) {
        byte[] bArr2 = new byte[this.mdLength];
        Object signature = this.ots.getSignature(bArr);
        Object concatenateArray = this.gmssUtil.concatenateArray(this.currentAuthPaths[this.numLayer - 1]);
        Object intToBytesLittleEndian = this.gmssUtil.intToBytesLittleEndian(this.index[this.numLayer - 1]);
        Object obj = new byte[((intToBytesLittleEndian.length + signature.length) + concatenateArray.length)];
        System.arraycopy(intToBytesLittleEndian, 0, obj, 0, intToBytesLittleEndian.length);
        System.arraycopy(signature, 0, obj, intToBytesLittleEndian.length, signature.length);
        System.arraycopy(concatenateArray, 0, obj, signature.length + intToBytesLittleEndian.length, concatenateArray.length);
        concatenateArray = new byte[0];
        for (int i = (this.numLayer - 1) - 1; i >= 0; i--) {
            intToBytesLittleEndian = this.gmssUtil.concatenateArray(this.currentAuthPaths[i]);
            Object intToBytesLittleEndian2 = this.gmssUtil.intToBytesLittleEndian(this.index[i]);
            Object obj2 = new byte[concatenateArray.length];
            System.arraycopy(concatenateArray, 0, obj2, 0, concatenateArray.length);
            concatenateArray = new byte[(((obj2.length + intToBytesLittleEndian2.length) + this.subtreeRootSig[i].length) + intToBytesLittleEndian.length)];
            System.arraycopy(obj2, 0, concatenateArray, 0, obj2.length);
            System.arraycopy(intToBytesLittleEndian2, 0, concatenateArray, obj2.length, intToBytesLittleEndian2.length);
            System.arraycopy(this.subtreeRootSig[i], 0, concatenateArray, obj2.length + intToBytesLittleEndian2.length, this.subtreeRootSig[i].length);
            System.arraycopy(intToBytesLittleEndian, 0, concatenateArray, (intToBytesLittleEndian2.length + obj2.length) + this.subtreeRootSig[i].length, intToBytesLittleEndian.length);
        }
        signature = new byte[(obj.length + concatenateArray.length)];
        System.arraycopy(obj, 0, signature, 0, obj.length);
        System.arraycopy(concatenateArray, 0, signature, obj.length, concatenateArray.length);
        return signature;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (!z) {
            this.key = (GMSSPublicKeyParameters) cipherParameters;
            initVerify();
        } else if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.random = parametersWithRandom.getRandom();
            this.key = (GMSSPrivateKeyParameters) parametersWithRandom.getParameters();
            initSign();
        } else {
            this.random = new SecureRandom();
            this.key = (GMSSPrivateKeyParameters) cipherParameters;
            initSign();
        }
    }

    public boolean verifySignature(byte[] bArr, byte[] bArr2) {
        this.messDigestOTS.reset();
        int i = this.numLayer - 1;
        int i2 = 0;
        while (i >= 0) {
            WinternitzOTSVerify winternitzOTSVerify = new WinternitzOTSVerify(this.digestProvider.get(), this.gmssPS.getWinternitzParameter()[i]);
            int signatureLength = winternitzOTSVerify.getSignatureLength();
            int bytesToIntLittleEndian = this.gmssUtil.bytesToIntLittleEndian(bArr2, i2);
            i2 += 4;
            Object obj = new byte[signatureLength];
            System.arraycopy(bArr2, i2, obj, 0, signatureLength);
            signatureLength += i2;
            obj = winternitzOTSVerify.Verify(bArr, obj);
            if (obj == null) {
                System.err.println("OTS Public Key is null in GMSSSignature.verify");
                return false;
            }
            Object obj2;
            byte[][] bArr3 = (byte[][]) Array.newInstance(Byte.TYPE, new int[]{this.gmssPS.getHeightOfTrees()[i], this.mdLength});
            i2 = signatureLength;
            for (Object obj22 : bArr3) {
                System.arraycopy(bArr2, i2, obj22, 0, this.mdLength);
                i2 += this.mdLength;
            }
            byte[] bArr4 = new byte[this.mdLength];
            signatureLength = (1 << bArr3.length) + bytesToIntLittleEndian;
            Object obj3 = obj;
            int i3 = signatureLength;
            for (signatureLength = 0; signatureLength < bArr3.length; signatureLength++) {
                obj22 = new byte[(this.mdLength << 1)];
                if (i3 % 2 == 0) {
                    System.arraycopy(obj3, 0, obj22, 0, this.mdLength);
                    System.arraycopy(bArr3[signatureLength], 0, obj22, this.mdLength, this.mdLength);
                    i3 /= 2;
                } else {
                    System.arraycopy(bArr3[signatureLength], 0, obj22, 0, this.mdLength);
                    System.arraycopy(obj3, 0, obj22, this.mdLength, obj3.length);
                    i3 = (i3 - 1) / 2;
                }
                this.messDigestTrees.update(obj22, 0, obj22.length);
                obj3 = new byte[this.messDigestTrees.getDigestSize()];
                this.messDigestTrees.doFinal(obj3, 0);
            }
            i--;
            Object obj4 = obj3;
        }
        return Arrays.areEqual(this.pubKeyBytes, bArr);
    }
}
