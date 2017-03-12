package android.ktuca;

import android.os.Handler;
import android.util.Log;

public class KtUcaServiceJni {
    private static final boolean D = true;
    private static final String TAG = "KtUcaServiceJni";
    private static Handler m_Handler = null;

    public static native long KUCA_CHInit(byte b, byte[] bArr, int[] iArr);

    public static native long KUCA_Close(byte[] bArr, byte b);

    public static native long KUCA_CloseT(byte[] bArr, byte b);

    public static native long KUCA_KUH_Establish(byte b);

    public static native long KUCA_KUH_Release(byte b);

    public static native long KUCA_KUH_Transmit(byte b, byte[] bArr, int i, byte[] bArr2, int[] iArr);

    public static native long KUCA_Open(byte[] bArr, byte[] bArr2, int[] iArr);

    public static native long KUCA_OpenT(byte[] bArr, byte[] bArr2, int[] iArr);

    public static native long KUCA_Transmit(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int[] iArr);

    public static native long KUCA_UCAVersion(byte[] bArr, byte[] bArr2, int[] iArr);

    public static native long KUCA_getHandle(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int[] iArr);

    public static native long KUCA_getICCID(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3);

    public static native long KUCA_getIMSI(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3);

    public static native long KUCA_getMDN(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3);

    public static native long KUCA_getMODEL(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3);

    public static native long KUCA_getMSISDN(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3);

    public static native long KUCA_getPUID(byte[] bArr, byte[] bArr2, int[] iArr, int i, byte[] bArr3);

    public static native long KUCA_getPinStatus(byte[] bArr, int i, byte[] bArr2, int[] iArr);

    public static native long KUCA_getSIMInfo(byte[] bArr, byte[] bArr2, int[] iArr);

    public static native long KUCA_getSimStatus(byte[] bArr, byte[] bArr2);

    public static native long KUCA_printCHInfo(byte b);

    public static native long KUCA_usimAUTH(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int[] iArr);

    public static native long KUCA_verifyPin(byte[] bArr, int i, String str, byte[] bArr2, int[] iArr);

    static {
        Log.d(TAG, "System.loadLibrary");
        System.loadLibrary("ktuca2");
    }

    public KtUcaServiceJni() {
        Log.d(TAG, "[STAT] KtUcaServiceJni Constructor");
    }

    public long uca_getHandle(byte[] callerId, byte[] preKey, byte[] appId, byte[] authHandle, int[] authHandleLen) {
        return KUCA_getHandle(callerId, preKey, appId, authHandle, authHandleLen);
    }

    public long uca_getMSISDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) {
        return KUCA_getMSISDN(handle, output, outputLen, encryptType, deviceIp);
    }

    public long uca_getIMSI(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) {
        return KUCA_getIMSI(handle, output, outputLen, encryptType, deviceIp);
    }

    public long uca_getICCID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) {
        return KUCA_getICCID(handle, output, outputLen, encryptType, deviceIp);
    }

    public long uca_getPUID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) {
        return KUCA_getPUID(handle, output, outputLen, encryptType, deviceIp);
    }

    public long uca_getMDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) {
        return KUCA_getMDN(handle, output, outputLen, encryptType, deviceIp);
    }

    public long uca_getMODEL(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) {
        return KUCA_getMODEL(handle, output, outputLen, encryptType, deviceIp);
    }

    public long uca_getSIMInfo(byte[] handle, byte[] output, int[] outputLen) {
        return KUCA_getSIMInfo(handle, output, outputLen);
    }

    public long uca_usimAUTH(byte[] handle, byte[] rand, byte[] autn, byte[] output, int[] outputLen) {
        return KUCA_usimAUTH(handle, rand, autn, output, outputLen);
    }

    public long uca_getPinStatus(byte[] handle, int pinId, byte[] output, int[] outputLen) {
        return KUCA_getPinStatus(handle, pinId, output, outputLen);
    }

    public long uca_verifyPin(byte[] handle, int pinId, String pinCode, byte[] output, int[] outputLen) {
        return KUCA_verifyPin(handle, pinId, pinCode, output, outputLen);
    }

    public long uca_Open(byte[] handle, byte[] channel, int[] channelLen) {
        return KUCA_Open(handle, channel, channelLen);
    }

    public long uca_Transmit(byte[] handle, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        return KUCA_Transmit(handle, input, inputLen, output, outputLen);
    }

    public long uca_Close(byte[] handle, byte channel) {
        return KUCA_Close(handle, channel);
    }

    public long uca_getSimStatus(byte[] handle, byte[] output) {
        return KUCA_getSimStatus(handle, output);
    }

    public long uca_UCAVersion(byte[] handle, byte[] output, int[] outputLen) {
        return KUCA_UCAVersion(handle, output, outputLen);
    }

    public long uca_CHInit(byte ucatag, byte[] channel, int[] channelLen) {
        Log.d(TAG, "[STAT] uca_CHInit");
        return KUCA_CHInit(ucatag, channel, channelLen);
    }

    public long uca_printCHInfo(byte ucatag) {
        return KUCA_printCHInfo(ucatag);
    }

    public long uca_KUH_Establish(byte ucatag) {
        return KUCA_KUH_Establish(ucatag);
    }

    public long uca_KUH_Release(byte ucatag) {
        return KUCA_KUH_Release(ucatag);
    }

    public long uca_KUH_Transmit(byte ucatag, byte[] pbSendBuffer, int cbSendLength, byte[] pbRecvBuffer, int[] pcbRecvLength) {
        return KUCA_KUH_Transmit(ucatag, pbSendBuffer, cbSendLength, pbRecvBuffer, pcbRecvLength);
    }

    public long uca_OpenT(byte[] appId, byte[] channel, int[] channelLen) {
        return KUCA_OpenT(appId, channel, channelLen);
    }

    public long uca_CloseT(byte[] appId, byte channel) {
        return KUCA_CloseT(appId, channel);
    }
}
