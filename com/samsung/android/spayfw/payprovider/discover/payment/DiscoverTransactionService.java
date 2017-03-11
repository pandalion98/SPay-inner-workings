package com.samsung.android.spayfw.payprovider.discover.payment;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.discover.db.DcStorageManager;
import com.samsung.android.spayfw.payprovider.discover.db.dao.DcCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverApduProcessingResult;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM.DiscoverCDCVMType;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext.DiscoverClTransactionType;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTrackData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataResponse;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse.C0528a;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAController;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.utils.Utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.g */
public class DiscoverTransactionService {
    private static final String TAG;
    private Context mContext;
    private DiscoverTransactionContext sS;
    private DcTAController ti;
    private DcCardProfileDaoImpl tj;
    private DiscoverApduHandlerState tk;
    private DiscoverPaymentCard tl;
    private boolean tm;
    private boolean tn;

    static {
        TAG = "DCSDK_" + DiscoverTransactionService.class.getSimpleName();
    }

    public DiscoverTransactionService(Context context) {
        this.tk = DiscoverApduHandlerState.DiscoverAPDUStateInitial;
        this.tl = null;
        this.tm = false;
        this.mContext = context;
        this.ti = DcTAController.m1039E(context);
        this.tj = new DcCardProfileDaoImpl(context);
    }

    public SelectCardResult m995q(long j) {
        SelectCardResult selectCardResult = new SelectCardResult();
        try {
            this.tl = m991r(j);
            if (this.tl == null) {
                Log.m286e(TAG, "selectCard: cannot load payment profile, token " + j);
                return selectCardResult;
            } else if (this.tl.getSecureObject() == null) {
                Log.m286e(TAG, "selectCard: card is not ready for payment, token " + j);
                return selectCardResult;
            } else if (this.ti.isTALoaded()) {
                String tAId = this.ti.getTAInfo().getTAId();
                if (tAId == null) {
                    Log.m286e(TAG, "selectCard: Discover TA id is not defined by ta controller.");
                    return selectCardResult;
                }
                Object k = this.ti.m1053k(this.tl.getSecureObject());
                if (k == null) {
                    Log.m286e(TAG, "selectCard, Discover TA returns null  nonce. Unload TA.");
                    this.ti.unloadTA();
                    return selectCardResult;
                }
                Log.m285d(TAG, "selectCard, nonce len: " + k.length);
                selectCardResult.setNonce((byte[]) k.clone());
                selectCardResult.setTaid(tAId);
                if (!dh()) {
                    Log.m286e(TAG, "selectCard, cannot initiate transaction context.");
                }
                Log.m287i(TAG, "selectCard, completed.");
                return selectCardResult;
            } else {
                Log.m286e(TAG, "selectCard: Discover TA is not loaded.");
                return selectCardResult;
            }
        } catch (Throwable e) {
            Log.m284c(TAG, "selectCard, unexpected TA Exception: " + e.getMessage(), e);
        } catch (Exception e2) {
            Log.m286e(TAG, "selectCard, unexpected exception on card selection: " + e2.getMessage());
            e2.printStackTrace();
        }
    }

    public boolean m993a(SecuredObject securedObject, DiscoverCDCVMType discoverCDCVMType) {
        if (securedObject == null) {
            Log.m286e(TAG, "authenticateTransaction, secure object not found.");
            return false;
        } else if (securedObject.getSecureObjectData() == null) {
            Log.m286e(TAG, "authenticateTransaction, secure object is empty.");
            return false;
        } else if (discoverCDCVMType == null) {
            Log.m286e(TAG, "authenticateTransaction, authentication type is null.");
            return false;
        } else {
            Log.m287i(TAG, "authenticateTransaction, start transaction authentication...");
            long j = -1;
            try {
                j = this.ti.authenticateTransaction(securedObject.getSecureObjectData());
            } catch (Throwable e) {
                Log.m284c(TAG, "authenticateTransaction, unexpected TA Exception: " + e.getMessage(), e);
            }
            Log.m287i(TAG, "authenticateTransaction, result: " + j);
            if (this.sS != null) {
                Log.m287i(TAG, "authenticateTransaction, set cvm result in transaction context.");
                this.sS.m983a(new DiscoverCDCVM(discoverCDCVMType, j));
                if (j == 0) {
                    this.sS.ed().m951b((byte) 1);
                    this.sS.ed().m953c((byte) 0);
                    this.sS.ed().dH().setBit(3, 7);
                    this.sS.ed().dH().setBit(2, 2);
                    this.sS.ed().dI().setBit(1, 5);
                }
            } else {
                Log.m286e(TAG, "authenticateTransaction, cannot find selected card.");
            }
            if (this.tl == null) {
                Log.m286e(TAG, "authenticateTransaction, card profile not found.");
                return false;
            } else if (this.tl.getDiscoverContactlessPaymentData().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(1, 5)) {
                Log.m286e(TAG, "authenticateTransaction, confirmation code is not supported by profile.");
                return false;
            } else {
                return j == 0;
            }
        }
    }

    public void clearCard() {
        if (this.tl == null) {
            Log.m286e(TAG, "clearCard: card is null");
            return;
        }
        if (this.tn) {
            Log.m287i(TAG, "clearCard: save otpk data.");
            DcStorageManager.m865c(this.tl.getTokenId(), this.sS.eb());
            DcStorageManager.m855a(this.tl.getTokenId(), this.sS.ee());
            Log.m287i(TAG, "clearCard: call cancelTransaction.");
            di();
        }
        Log.m287i(TAG, "clearCard: clean card and context.");
        this.tl = null;
        this.sS = null;
        this.tn = false;
    }

    public short m992K(int i) {
        if (this.tl == null) {
            Log.m286e(TAG, "stopNfcPay: card is null");
            return (short) 1;
        }
        Log.m287i(TAG, "stopNfcPay: reason : " + i);
        return (short) 2;
    }

    public boolean prepareMstPay() {
        Log.m285d(TAG, "prepareMstPay: Prepare  MST tracks... ");
        try {
            this.ti.ew();
            Log.m285d(TAG, "prepareMstPay: process MST transaction completed.");
            return true;
        } catch (Throwable e) {
            Log.m284c(TAG, "prepareMstPay, unexpected TA Exception: " + e.getMessage(), e);
            return false;
        }
    }

    public void interruptMstPay() {
    }

    public void stopMstPay(boolean z) {
        Log.m287i(TAG, "stopMstPay: clear  MST tracks... ");
        try {
            this.ti.ex();
        } catch (Throwable e) {
            Log.m284c(TAG, "stopMstPay, unexpected TA Exception: " + e.getMessage(), e);
        }
        Log.m285d(TAG, "stopMstPay: clear mst tracks data.");
    }

    public boolean startMstPay(int i, byte[] bArr) {
        Log.m287i(TAG, "startMstPay: Process MST transaction in the Transaction Service... ");
        try {
            this.ti.m1046b(i, bArr);
            Log.m287i(TAG, "Process MST transaction completed.");
            return true;
        } catch (Throwable e) {
            Log.m284c(TAG, "transmitMSTTracks, unexpected TA Exception: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean dh() {
        Log.m285d(TAG, "initTransaction.");
        this.sS = new DiscoverTransactionContext();
        this.tn = false;
        if (this.tl == null) {
            Log.m286e(TAG, "initTransaction, card is not selected");
            return false;
        } else if (this.tl.getOTPK() == null) {
            Log.m286e(TAG, "initTransaction, no available otpk.");
            return false;
        } else if (this.tl.getSecureObject() == null) {
            Log.m286e(TAG, "initTransaction, no secure object found.");
            return false;
        } else {
            this.sS.m985i(this.tl.getOTPK());
            this.sS.setSecureObject(this.tl.getSecureObject());
            HashMap paymentProfiles = this.tl.getDiscoverContactlessPaymentData().getPaymentProfiles();
            if (paymentProfiles == null) {
                Log.m286e(TAG, "initTransaction, no profile map found.");
                return false;
            }
            if (paymentProfiles.size() == 0) {
                Log.m286e(TAG, "initTransaction, cannot find default profile, profiles map is empty.");
            }
            this.sS.ed().m950a((DiscoverPaymentProfile) paymentProfiles.get(Integer.valueOf(0)));
            this.sS.ed().setPth(this.tl.getDiscoverContactlessPaymentData().getPth());
            ByteBuffer cpr = ((DiscoverPaymentProfile) paymentProfiles.get(Integer.valueOf(0))).getCpr();
            if (cpr != null) {
                cpr.clearBit(1, 8);
                cpr.clearBit(1, 7);
                cpr.clearBit(1, 6);
                this.sS.ed().m954s(cpr);
                Log.m285d(TAG, "initTransaction, cpr: " + this.sS.ed().dI().toHexString());
            }
            this.sS.ed().m951b((byte) 0);
            this.sS.ed().m953c((byte) 1);
            DiscoverRecord b = DiscoverTrackData.m957b(this.tl.getDiscoverContactlessPaymentData().getTrack1DataZipMsMode(), this.tl.getDiscoverContactlessPaymentData().getTrack2DataZipMsMode());
            Log.m287i(TAG, "initTransaction, set ZIP record.");
            this.sS.ed().m952b(b);
            this.tk = DiscoverApduHandlerState.DiscoverAPDUStateReady;
            return true;
        }
    }

    public boolean prepareNfcPay() {
        if (this.sS == null) {
            Log.m286e(TAG, "prepareNfcPay, transaction context is not initialized.");
            return false;
        } else if (this.sS.getSecureObject() == null) {
            Log.m286e(TAG, "prepareNfcPay, secure object not found.");
            return false;
        } else if (this.sS.eb() == null) {
            Log.m286e(TAG, "prepareNfcPay, otpk not found.");
            return false;
        } else {
            try {
                C0528a e = this.ti.m1052e(this.sS.getSecureObject(), this.sS.eb());
                this.tn = true;
                this.sS.m987v(e.wY.get());
                Log.m287i(TAG, "Commit Data Set, Remaining otpks: " + this.sS.ee());
                this.sS.m985i(e.xb.getData());
                this.sS.m986u(new ByteBuffer(e.xc.getData()));
                return true;
            } catch (Throwable e2) {
                Log.m284c(TAG, "prepareNfcPay, unexpected TA Exception: " + e2.getMessage(), e2);
                return false;
            } catch (Exception e3) {
                Log.m286e(TAG, "prepareNfcPay, unexpected Exception: " + e3.getMessage());
                e3.printStackTrace();
                return false;
            }
        }
    }

    public byte[] m994h(byte[] bArr) {
        Log.m287i(TAG, "handleApdu: start");
        if (bArr == null || bArr.length == 0) {
            Log.m286e(TAG, "Empty apdu received, apdu byte array is null or empty...");
            return null;
        }
        try {
            ByteBuffer byteBuffer = new ByteBuffer(bArr);
            Log.m287i(TAG, "handleApdu, APDU INS: " + (byteBuffer.getByte(1) & GF2Field.MASK));
            if (this.tl == null) {
                Log.m286e(TAG, "handleApdu, APDU INS, card is no selected.");
                return null;
            } else if (this.sS == null) {
                Log.m286e(TAG, "handleApdu, APDU INS, context is not initialized.");
                return null;
            } else {
                Log.m285d(TAG, "handleApdu, current state: : " + this.tk);
                DiscoverApduProcessingResult e = this.tk.m895e(byteBuffer, this.sS, this.tl);
                if (e == null) {
                    Log.m286e(TAG, "handleApdu, apdu processing result is null.");
                    return new ByteBuffer(27013).getBytes();
                }
                if (e.dA() != null) {
                    this.tk = e.dA();
                }
                if (e.dB() == null || e.dB().getBytes() == null) {
                    Log.m286e(TAG, "handleApdu, apdu processing result data is null.");
                    return new ByteBuffer(27013).getBytes();
                }
                Log.m287i(TAG, "handleApdu: end: result: " + e.dB().toHexString());
                return e.dB().getBytes();
            }
        } catch (Exception e2) {
            Log.m286e(TAG, "handleApdu: unexpected exception, " + e2.getMessage());
            e2.printStackTrace();
            return new ByteBuffer(27013).getBytes();
        }
    }

    public byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        if (inAppDetailedTransactionInfo == null) {
            Log.m286e(TAG, "generateInAppPaymentPayload, transaction info is null.");
            return null;
        } else if (this.tl == null) {
            Log.m286e(TAG, "generateInAppPaymentPayload, selected card is null.");
            return null;
        } else {
            byte[] otpk = this.tl.getOTPK();
            if (otpk == null) {
                Log.m286e(TAG, "generateInAppPaymentPayload, otpk bundle is null.");
                return null;
            }
            byte[] secureObject = this.tl.getSecureObject();
            if (secureObject == null) {
                Log.m286e(TAG, "generateInAppPaymentPayload, secure object is null.");
                return null;
            }
            byte[] nonce = inAppDetailedTransactionInfo.getNonce();
            if (nonce == null) {
                Log.m286e(TAG, "generateInAppPaymentPayload, nonce is null.");
                return null;
            }
            byte[] bytes;
            String format = new SimpleDateFormat("yyMMdd").format(new Date(Utils.am(this.mContext)));
            Log.m285d(TAG, "generateInAppPaymentPayload, inapp timestamp: " + format);
            if (format != null) {
                bytes = format.getBytes();
            } else {
                bytes = null;
            }
            if (bytes == null) {
                Log.m286e(TAG, "generateInAppPaymentPayload, timestamp is null.");
                return null;
            }
            DiscoverInAppHandler discoverInAppHandler = new DiscoverInAppHandler();
            DiscoverInAppCryptoData a = discoverInAppHandler.m942a(inAppDetailedTransactionInfo, this.tl.getDiscoverContactlessPaymentData());
            Log.m285d(TAG, "generateInAppPaymentPayload, inapp data generation...");
            try {
                Log.m287i(TAG, "generateInAppPaymentPayload, TA call getInAppData.");
                DiscoverTAGetInAppDataResponse c = this.ti.m1049c(otpk, secureObject, bytes);
                Log.m285d(TAG, "generateInAppPaymentPayload, check TA response.");
                if (c != null) {
                    Log.m285d(TAG, "generateInAppPaymentPayload, get payload");
                    if (c.en() != null) {
                        DcStorageManager.m865c(this.tl.getTokenId(), c.en());
                        DcStorageManager.m855a(this.tl.getTokenId(), c.getRemainingOtpkCount());
                    }
                    a.setCryptogram(c.getPayload());
                    Log.m285d(TAG, "generateInAppPaymentPayload, cavvInappCryptogram = " + com.samsung.android.spayfw.payprovider.discover.util.Utils.byteArrayToHex(c.getPayload()));
                    Log.m285d(TAG, "generateInAppPaymentPayload, cavvInappCryptogram atc = " + c.getRemainingOtpkCount());
                    return discoverInAppHandler.m943a(nonce, a);
                }
                Log.m286e(TAG, "generateInAppPaymentPayload, TA response is null.");
                return null;
            } catch (DcTAException e) {
                Log.m286e(TAG, "generateInAppPaymentPayload, unexpected DC TA Exception: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    private void di() {
        if (this.sS != null) {
            Log.m287i(TAG, "cancelTransaction, update secure object...");
            DcStorageManager.m861a(this.tl.getTokenId(), this.sS.getSecureObject());
            if (this.sS.ed().dK() == DiscoverClTransactionType.DISCOVER_CL_EMV) {
                Log.m287i(TAG, "cancelTransaction, EMV transaction, update transaction profile.");
                DcStorageManager.m859a(this.tl.getTokenId(), this.sS.ed().getPaymentProfile());
                Log.m287i(TAG, "cancelTransaction, EMV transaction, save pth.");
                DcStorageManager.m864b(this.tl.getTokenId(), this.sS.ed().getPth().getBytes());
            }
            Log.m287i(TAG, "cancelTransaction, clear transaction context...");
            this.sS.clear();
            this.tk = DiscoverApduHandlerState.DiscoverAPDUStateReady;
            return;
        }
        Log.m286e(TAG, "cancelTransaction, transaction context is null.");
    }

    private DiscoverPaymentCard m991r(long j) {
        DiscoverPaymentCard i = DcStorageManager.m870i(j);
        Log.m287i(TAG, "loadPaymentCard, loaded " + i);
        return i;
    }

    public boolean m996s(long j) {
        Log.m285d(TAG, "getPayReadyState: mCommitData - " + this.tn);
        if (this.tn) {
            if (this.sS.ee() >= 1) {
                return true;
            }
            return false;
        } else if (DcStorageManager.m871j(j) < 1) {
            return false;
        } else {
            return true;
        }
    }

    public Bundle isTransactionComplete() {
        Log.m287i(TAG, "isTransactionComplete, begin...");
        Bundle bundle = new Bundle();
        if (this.sS == null) {
            Log.m287i(TAG, "isTransactionComplete, transaction context is null, return false.");
            bundle.putBoolean("TRANSACTION_COMPLETE_STATE", false);
        } else if (this.sS.ec() == null) {
            Log.m287i(TAG, "isTransactionComplete, transaction result is null, return false.");
            bundle.putBoolean("TRANSACTION_COMPLETE_STATE", false);
        } else {
            Log.m287i(TAG, "isTransactionComplete, return transaction complete state: " + this.sS.ec().dC());
            bundle.putBoolean("TRANSACTION_COMPLETE_STATE", this.sS.ec().dC());
        }
        return bundle;
    }
}
