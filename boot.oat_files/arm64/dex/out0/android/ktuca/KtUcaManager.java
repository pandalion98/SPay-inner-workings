package android.ktuca;

import android.os.RemoteException;

public class KtUcaManager {
    private IKtUcaIF mService;

    public KtUcaManager(IKtUcaIF service) {
        this.mService = service;
    }

    public long KUCA_getHandle(byte[] callerId, byte[] preKey, byte[] appId, byte[] handle, int[] handleLen) throws RemoteException {
        return this.mService.KUCA_getHandle(callerId, preKey, appId, handle, handleLen);
    }

    public long KUCA_getMSISDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        return this.mService.KUCA_getMSISDN(handle, output, outputLen, encryptType, deviceIp);
    }

    public long KUCA_getIMSI(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        return this.mService.KUCA_getIMSI(handle, output, outputLen, encryptType, deviceIp);
    }

    public long KUCA_getICCID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        return this.mService.KUCA_getICCID(handle, output, outputLen, encryptType, deviceIp);
    }

    public long KUCA_getPUID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        return this.mService.KUCA_getPUID(handle, output, outputLen, encryptType, deviceIp);
    }

    public long KUCA_getMDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        return this.mService.KUCA_getMDN(handle, output, outputLen, encryptType, deviceIp);
    }

    public long KUCA_getMODEL(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        return this.mService.KUCA_getMODEL(handle, output, outputLen, encryptType, deviceIp);
    }

    public long KUCA_getSIMInfo(byte[] handle, byte[] output, int[] outputLen) throws RemoteException {
        return this.mService.KUCA_getSIMInfo(handle, output, outputLen);
    }

    public long KUCA_usimAUTH(byte[] handle, byte[] rand, byte[] autn, byte[] output, int[] outputLen) throws RemoteException {
        return this.mService.KUCA_usimAUTH(handle, rand, autn, output, outputLen);
    }

    public long KUCA_getPinStatus(byte[] handle, int pinId, byte[] output, int[] outputLen) throws RemoteException {
        return this.mService.KUCA_getPinStatus(handle, pinId, output, outputLen);
    }

    public long KUCA_verifyPin(byte[] handle, int pinId, String pinCode, byte[] output, int[] outputLen) throws RemoteException {
        return this.mService.KUCA_verifyPin(handle, pinId, pinCode, output, outputLen);
    }

    public long KUCA_Open(byte[] handle, byte[] channel, int[] channelLen) throws RemoteException {
        return this.mService.KUCA_Open(handle, channel, channelLen);
    }

    public long KUCA_Transmit(byte[] handle, byte[] input, int inputLen, byte[] output, int[] outputLen) throws RemoteException {
        return this.mService.KUCA_Transmit(handle, input, inputLen, output, outputLen);
    }

    public long KUCA_Close(byte[] handle, byte channel) throws RemoteException {
        return this.mService.KUCA_Close(handle, channel);
    }

    public long KUCA_getSimStatus(byte[] handle, byte[] output) throws RemoteException {
        return this.mService.KUCA_getSimStatus(handle, output);
    }

    public long KUCA_UCAVersion(byte[] handle, byte[] output, int[] outputLen) throws RemoteException {
        return this.mService.KUCA_UCAVersion(handle, output, outputLen);
    }

    public long KUCA_CHInit(byte ucatag, byte[] channel, int[] channelLen) throws RemoteException {
        return this.mService.KUCA_CHInit(ucatag, channel, channelLen);
    }

    public long KUCA_printCHInfo(byte ucatag) throws RemoteException {
        return this.mService.KUCA_printCHInfo(ucatag);
    }

    public long KUCA_KUH_Establish(byte ucatag) throws RemoteException {
        return this.mService.KUCA_KUH_Establish(ucatag);
    }

    public long KUCA_KUH_Release(byte ucatag) throws RemoteException {
        return this.mService.KUCA_KUH_Release(ucatag);
    }

    public long KUCA_KUH_Transmit(byte ucatag, byte[] pbSendBuffer, int cbSendLength, byte[] pbRecvBuffer, int[] pcbRecvLength) throws RemoteException {
        return this.mService.KUCA_KUH_Transmit(ucatag, pbSendBuffer, cbSendLength, pbRecvBuffer, pcbRecvLength);
    }

    public long KUCA_OpenT(byte[] appId, byte[] channel, int[] channelLen) throws RemoteException {
        return this.mService.KUCA_OpenT(appId, channel, channelLen);
    }

    public long KUCA_CloseT(byte[] appId, byte channel) throws RemoteException {
        return this.mService.KUCA_CloseT(appId, channel);
    }

    public int getResource() throws RemoteException {
        return this.mService.getResource();
    }

    public int releaseResource() throws RemoteException {
        return this.mService.releaseResource();
    }
}
