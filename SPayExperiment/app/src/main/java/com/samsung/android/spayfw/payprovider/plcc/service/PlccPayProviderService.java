/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.payprovider.plcc.service;

import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import java.io.File;
import java.util.HashMap;

public interface PlccPayProviderService {
    public byte[] addCard(byte[] var1);

    public boolean authenticateTransaction(byte[] var1);

    public HashMap<String, byte[]> getCertDev();

    public byte[] getNonce(int var1);

    public String getTaid(String var1);

    public boolean mstConfig(File var1);

    public boolean mstTransmit(PlccCard var1, int var2, byte[] var3);

    public void setCertServer(byte[] var1, byte[] var2, byte[] var3);

    public boolean stopMstTransmit();

    public byte[] utilityEnc4ServerTransport(byte[] var1);
}

