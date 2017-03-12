package android.ktuca;

import android.content.Context;
import android.ktuca.IKtUcaIF.Stub;
import android.os.RemoteException;
import android.util.Log;

public class KtUcaService extends Stub {
    private KtUcaServiceJni KtUcaServiceJni = null;
    private final String TAG = "KT_UCA_SERVICE";
    private byte[] mChannel = new byte[10];
    private int[] mChannelLen = new int[1];
    private Context mContext;
    private int referenceCount = 0;

    public KtUcaService(Context context) {
        this.mContext = context;
        Log.d("KT_UCA_SERVICE", "+[KtUcaService]");
        new Thread(new Runnable() {
            public void run() {
                KtUcaService.this.KtUcaServiceJni = new KtUcaServiceJni();
                KtUcaService.this.KtUcaServiceJni;
                KtUcaServiceJni.KUCA_CHInit((byte) -53, KtUcaService.this.mChannel, KtUcaService.this.mChannelLen);
            }
        }).start();
        Log.d("KT_UCA_SERVICE", "-[KtUcaService]");
    }

    public long KUCA_getHandle(byte[] callerId, byte[] preKey, byte[] appId, byte[] handle, int[] handleLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getHandle]");
        long ret = this.KtUcaServiceJni.uca_getHandle(callerId, preKey, appId, handle, handleLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getHandle] ret : " + ret);
        return ret;
    }

    public long KUCA_getMSISDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getMSISDN]");
        long ret = this.KtUcaServiceJni.uca_getMSISDN(handle, output, outputLen, encryptType, deviceIp);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getMSISDN] ret : " + ret);
        return ret;
    }

    public long KUCA_getIMSI(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getIMSI]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getIMSI(handle, output, outputLen, encryptType, deviceIp);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getIMSI] ret : " + ret);
        return ret;
    }

    public long KUCA_getICCID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getICCID]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getICCID(handle, output, outputLen, encryptType, deviceIp);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getICCID] ret : " + ret);
        return ret;
    }

    public long KUCA_getPUID(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getPUID]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getPUID(handle, output, outputLen, encryptType, deviceIp);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getPUID] ret : " + ret);
        return ret;
    }

    public long KUCA_getMDN(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getMDN]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getMDN(handle, output, outputLen, encryptType, deviceIp);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getMDN] ret : " + ret);
        return ret;
    }

    public long KUCA_getMODEL(byte[] handle, byte[] output, int[] outputLen, int encryptType, byte[] deviceIp) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getMODEL]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getMODEL(handle, output, outputLen, encryptType, deviceIp);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getMODEL] ret : " + ret);
        return ret;
    }

    public long KUCA_getSIMInfo(byte[] handle, byte[] output, int[] outputLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getSIMInfo]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getSIMInfo(handle, output, outputLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getSIMInfo] ret : " + ret);
        return ret;
    }

    public long KUCA_usimAUTH(byte[] handle, byte[] rand, byte[] autn, byte[] output, int[] outputLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_usimAUTH]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_usimAUTH(handle, rand, autn, output, outputLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_usimAUTH] ret : " + ret);
        return ret;
    }

    public long KUCA_getPinStatus(byte[] handle, int pinId, byte[] output, int[] outputLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getPinStatus]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getPinStatus(handle, pinId, output, outputLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getPinStatus] ret : " + ret);
        return ret;
    }

    public long KUCA_verifyPin(byte[] handle, int pinId, String pinCode, byte[] output, int[] outputLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_verifyPin]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_verifyPin(handle, pinId, pinCode, output, outputLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_verifyPin] ret : " + ret);
        return ret;
    }

    public long KUCA_Open(byte[] handle, byte[] channel, int[] channelLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_Open]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_Open(handle, channel, channelLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_Open] ret : " + ret);
        return ret;
    }

    public long KUCA_Transmit(byte[] handle, byte[] input, int inputLen, byte[] output, int[] outputLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_Transmit]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_Transmit(handle, input, inputLen, output, outputLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_Transmit] ret : " + ret);
        return ret;
    }

    public long KUCA_Close(byte[] handle, byte channel) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_Close]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_Close(handle, channel);
        Log.d("KT_UCA_SERVICE", "-[KUCA_Close] ret : " + ret);
        return ret;
    }

    public long KUCA_getSimStatus(byte[] handle, byte[] output) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_getSimStatus]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_getSimStatus(handle, output);
        Log.d("KT_UCA_SERVICE", "-[KUCA_getSimStatus] ret : " + ret);
        return ret;
    }

    public long KUCA_UCAVersion(byte[] handle, byte[] output, int[] outputLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_UCAVersion]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_UCAVersion(handle, output, outputLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_UCAVersion] ret : " + ret);
        return ret;
    }

    public long KUCA_CHInit(byte ucatag, byte[] channel, int[] channelLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_CHInit]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_CHInit(ucatag, channel, channelLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_CHInit] ret : " + ret);
        return ret;
    }

    public long KUCA_printCHInfo(byte ucatag) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_printCHInfo]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_printCHInfo(ucatag);
        Log.d("KT_UCA_SERVICE", "-[KUCA_printCHInfo] ret : " + ret);
        return ret;
    }

    public long KUCA_KUH_Establish(byte ucatag) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_KUH_Establish]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_KUH_Establish(ucatag);
        Log.d("KT_UCA_SERVICE", "-[KUCA_KUH_Establish] ret : " + ret);
        return ret;
    }

    public long KUCA_KUH_Release(byte ucatag) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_KUH_Release]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_KUH_Release(ucatag);
        Log.d("KT_UCA_SERVICE", "+[KUCA_KUH_Release]");
        return ret;
    }

    public long KUCA_KUH_Transmit(byte ucatag, byte[] pbSendBuffer, int cbSendLength, byte[] pbRecvBuffer, int[] pcbRecvLength) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_KUH_Transmit]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_KUH_Transmit(ucatag, pbSendBuffer, cbSendLength, pbRecvBuffer, pcbRecvLength);
        Log.d("KT_UCA_SERVICE", "-[KUCA_KUH_Transmit]");
        return ret;
    }

    public long KUCA_OpenT(byte[] appId, byte[] channel, int[] channelLen) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_OpenT]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_OpenT(appId, channel, channelLen);
        Log.d("KT_UCA_SERVICE", "-[KUCA_OpenT]");
        return ret;
    }

    public long KUCA_CloseT(byte[] appId, byte channel) throws RemoteException {
        Log.d("KT_UCA_SERVICE", "+[KUCA_CloseT]");
        KtUcaServiceJni ktUcaServiceJni = this.KtUcaServiceJni;
        long ret = KtUcaServiceJni.KUCA_CloseT(appId, channel);
        Log.d("KT_UCA_SERVICE", "-[KUCA_CloseT]");
        return ret;
    }

    public int getResource() throws RemoteException {
        this.referenceCount++;
        Log.d("KT_UCA_SERVICE", "+[KtUcaService Ver]" + "2.0.0_R06_110317");
        Log.e("KT_UCA_SERVICE", "Ref. Cnt. : " + this.referenceCount);
        return this.referenceCount;
    }

    public int releaseResource() throws RemoteException {
        this.referenceCount--;
        Log.e("KT_UCA_SERVICE", "Ref. Cnt. : " + this.referenceCount);
        return this.referenceCount;
    }
}
