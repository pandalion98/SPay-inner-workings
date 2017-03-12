package com.americanexpress.mobilepayments.hceclient.securecomponent;

import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAControllerFactory;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.IAmexTAController;

public class SecureComponentCoreImpl implements SecureComponentSaturn {
    private IAmexTAController iAmexTAController;

    public SecureComponentCoreImpl() {
        this.iAmexTAController = AmexTAControllerFactory.cB();
    }

    public int open(byte[] bArr) {
        return this.iAmexTAController.open(bArr);
    }

    public int close(byte[] bArr) {
        return this.iAmexTAController.close(bArr);
    }

    public int init(byte[] bArr) {
        return this.iAmexTAController.init(bArr);
    }

    public int perso(int i, byte[] bArr, byte[] bArr2) {
        return this.iAmexTAController.perso(i, bArr, bArr2);
    }

    public int update(byte[] bArr) {
        return this.iAmexTAController.update(bArr);
    }

    public int initializeSecureChannel(byte[] bArr, byte[] bArr2) {
        return this.iAmexTAController.initializeSecureChannel(bArr, bArr2);
    }

    public int updateSecureChannel(byte[] bArr, byte[] bArr2) {
        return this.iAmexTAController.updateSecureChannel(bArr, bArr2);
    }

    public int lcm(int i) {
        return this.iAmexTAController.lcm(i);
    }

    public int getMeta() {
        return 0;
    }

    public int computeAC(byte[] bArr, byte[] bArr2) {
        return this.iAmexTAController.computeAC(bArr, bArr2);
    }

    public int sign(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return this.iAmexTAController.sign(bArr, bArr2, bArr3);
    }

    public int reqMST(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return this.iAmexTAController.reqMST(bArr, bArr2, bArr3);
    }

    public int reqInApp(byte[] bArr, byte[] bArr2) {
        return this.iAmexTAController.reqInApp(bArr, bArr2);
    }

    public int verify(byte[] bArr, byte[] bArr2) {
        return this.iAmexTAController.verify(bArr, bArr2);
    }

    public int wrap(byte[] bArr, int i, byte[] bArr2, int i2) {
        return this.iAmexTAController.wrap(bArr, i, bArr2, i2);
    }

    public int unwrap(int i, byte[] bArr, int i2, byte[] bArr2, int i3) {
        return this.iAmexTAController.unwrap(i, bArr, i2, bArr2, i3);
    }

    public int getSignatureData(byte[] bArr, byte[] bArr2) {
        return this.iAmexTAController.getSignatureData(bArr, bArr2);
    }
}
