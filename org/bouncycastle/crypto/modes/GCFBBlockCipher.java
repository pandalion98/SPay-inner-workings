package org.bouncycastle.crypto.modes;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.payment.cld.CLD;
import com.mastercard.mobile_api.payment.cld.CardSide;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetResponse;
import com.mastercard.mobile_api.utils.apdu.emv.GenerateACApdu;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.ParametersWithSBox;

public class GCFBBlockCipher extends StreamBlockCipher {
    private static final byte[] f195C;
    private final CFBBlockCipher cfbEngine;
    private long counter;
    private boolean forEncryption;
    private KeyParameter key;

    static {
        f195C = new byte[]{(byte) 105, (byte) 0, (byte) 114, (byte) 34, (byte) 100, (byte) -55, (byte) 4, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE, (byte) -115, (byte) 58, (byte) -37, (byte) -106, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) -23, GenerateACApdu.INS, (byte) -60, CardSide.NO_PIN_TEXT_TAG, (byte) -2, (byte) -84, (byte) -108, (byte) 0, (byte) -19, (byte) 7, CLD.FORM_FACTOR_TAG, EMVGetResponse.INS, (byte) -122, (byte) -36, (byte) -62, (byte) -17, (byte) 76, (byte) -87, (byte) 43};
    }

    public GCFBBlockCipher(BlockCipher blockCipher) {
        super(blockCipher);
        this.counter = 0;
        this.cfbEngine = new CFBBlockCipher(blockCipher, blockCipher.getBlockSize() * 8);
    }

    protected byte calculateByte(byte b) {
        if (this.counter > 0 && this.counter % 1024 == 0) {
            BlockCipher underlyingCipher = this.cfbEngine.getUnderlyingCipher();
            underlyingCipher.init(false, this.key);
            byte[] bArr = new byte[32];
            underlyingCipher.processBlock(f195C, 0, bArr, 0);
            underlyingCipher.processBlock(f195C, 8, bArr, 8);
            underlyingCipher.processBlock(f195C, 16, bArr, 16);
            underlyingCipher.processBlock(f195C, 24, bArr, 24);
            this.key = new KeyParameter(bArr);
            underlyingCipher.init(true, this.key);
            bArr = this.cfbEngine.getCurrentIV();
            underlyingCipher.processBlock(bArr, 0, bArr, 0);
            this.cfbEngine.init(this.forEncryption, new ParametersWithIV(this.key, bArr));
        }
        this.counter++;
        return this.cfbEngine.calculateByte(b);
    }

    public String getAlgorithmName() {
        String algorithmName = this.cfbEngine.getAlgorithmName();
        return algorithmName.substring(0, algorithmName.indexOf(47) - 1) + "/G" + algorithmName.substring(algorithmName.indexOf(47) + 1);
    }

    public int getBlockSize() {
        return this.cfbEngine.getBlockSize();
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.counter = 0;
        this.cfbEngine.init(z, cipherParameters);
        this.forEncryption = z;
        CipherParameters parameters = cipherParameters instanceof ParametersWithIV ? ((ParametersWithIV) cipherParameters).getParameters() : cipherParameters;
        if (parameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom) parameters).getParameters();
        }
        if (parameters instanceof ParametersWithSBox) {
            parameters = ((ParametersWithSBox) parameters).getParameters();
        }
        this.key = (KeyParameter) parameters;
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        processBytes(bArr, i, this.cfbEngine.getBlockSize(), bArr2, i2);
        return this.cfbEngine.getBlockSize();
    }

    public void reset() {
        this.counter = 0;
        this.cfbEngine.reset();
    }
}
