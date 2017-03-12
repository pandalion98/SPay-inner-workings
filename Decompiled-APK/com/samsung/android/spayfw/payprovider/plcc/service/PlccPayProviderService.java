package com.samsung.android.spayfw.payprovider.plcc.service;

import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import java.io.File;
import java.util.HashMap;

public interface PlccPayProviderService {
    byte[] addCard(byte[] bArr);

    boolean authenticateTransaction(byte[] bArr);

    HashMap<String, byte[]> getCertDev();

    byte[] getNonce(int i);

    String getTaid(String str);

    boolean mstConfig(File file);

    boolean mstTransmit(PlccCard plccCard, int i, byte[] bArr);

    void setCertServer(byte[] bArr, byte[] bArr2, byte[] bArr3);

    boolean stopMstTransmit();

    byte[] utilityEnc4ServerTransport(byte[] bArr);
}
