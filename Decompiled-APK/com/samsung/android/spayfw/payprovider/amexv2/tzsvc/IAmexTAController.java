package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

/* renamed from: com.samsung.android.spayfw.payprovider.amexv2.tzsvc.f */
public interface IAmexTAController {
    int close(byte[] bArr);

    int computeAC(byte[] bArr, byte[] bArr2);

    int getSignatureData(byte[] bArr, byte[] bArr2);

    int init(byte[] bArr);

    int initializeSecureChannel(byte[] bArr, byte[] bArr2);

    int lcm(int i);

    int open(byte[] bArr);

    int perso(int i, byte[] bArr, byte[] bArr2);

    int reqMST(byte[] bArr, byte[] bArr2, byte[] bArr3);

    int sign(byte[] bArr, byte[] bArr2, byte[] bArr3);

    int unwrap(int i, byte[] bArr, int i2, byte[] bArr2, int i3);

    int update(byte[] bArr);

    int updateSecureChannel(byte[] bArr, byte[] bArr2);

    int verify(byte[] bArr, byte[] bArr2);

    int wrap(byte[] bArr, int i, byte[] bArr2, int i2);
}
