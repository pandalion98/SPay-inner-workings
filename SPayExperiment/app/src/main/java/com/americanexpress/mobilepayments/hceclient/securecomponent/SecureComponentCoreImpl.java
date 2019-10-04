/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.americanexpress.mobilepayments.hceclient.securecomponent;

import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentSaturn;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.d;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.f;

public class SecureComponentCoreImpl
implements SecureComponentSaturn {
    private f iAmexTAController = d.cB();

    @Override
    public int close(byte[] arrby) {
        return this.iAmexTAController.close(arrby);
    }

    @Override
    public int computeAC(byte[] arrby, byte[] arrby2) {
        return this.iAmexTAController.computeAC(arrby, arrby2);
    }

    @Override
    public int getMeta() {
        return 0;
    }

    @Override
    public int getSignatureData(byte[] arrby, byte[] arrby2) {
        return this.iAmexTAController.getSignatureData(arrby, arrby2);
    }

    @Override
    public int init(byte[] arrby) {
        return this.iAmexTAController.init(arrby);
    }

    @Override
    public int initializeSecureChannel(byte[] arrby, byte[] arrby2) {
        return this.iAmexTAController.initializeSecureChannel(arrby, arrby2);
    }

    @Override
    public int lcm(int n2) {
        return this.iAmexTAController.lcm(n2);
    }

    @Override
    public int open(byte[] arrby) {
        return this.iAmexTAController.open(arrby);
    }

    @Override
    public int perso(int n2, byte[] arrby, byte[] arrby2) {
        return this.iAmexTAController.perso(n2, arrby, arrby2);
    }

    @Override
    public int reqInApp(byte[] arrby, byte[] arrby2) {
        return this.iAmexTAController.reqInApp(arrby, arrby2);
    }

    @Override
    public int reqMST(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        return this.iAmexTAController.reqMST(arrby, arrby2, arrby3);
    }

    @Override
    public int sign(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        return this.iAmexTAController.sign(arrby, arrby2, arrby3);
    }

    @Override
    public int unwrap(int n2, byte[] arrby, int n3, byte[] arrby2, int n4) {
        return this.iAmexTAController.unwrap(n2, arrby, n3, arrby2, n4);
    }

    @Override
    public int update(byte[] arrby) {
        return this.iAmexTAController.update(arrby);
    }

    @Override
    public int updateSecureChannel(byte[] arrby, byte[] arrby2) {
        return this.iAmexTAController.updateSecureChannel(arrby, arrby2);
    }

    @Override
    public int verify(byte[] arrby, byte[] arrby2) {
        return this.iAmexTAController.verify(arrby, arrby2);
    }

    @Override
    public int wrap(byte[] arrby, int n2, byte[] arrby2, int n3) {
        return this.iAmexTAController.wrap(arrby, n2, arrby2, n3);
    }
}

