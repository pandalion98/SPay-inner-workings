/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.payprovider.discover.payment;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.discover.db.DcStorageManager;
import com.samsung.android.spayfw.payprovider.discover.payment.DiscoverApduHandlerState;
import com.samsung.android.spayfw.payprovider.discover.payment.d;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverInAppCryptoData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverTrackData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.a;
import com.samsung.android.spayfw.payprovider.discover.payment.data.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverApplicationData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentCard;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverRecord;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTACommands;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.payprovider.discover.util.b;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javolution.io.Struct;

public class g {
    private static final String TAG = "DCSDK_" + g.class.getSimpleName();
    private Context mContext;
    private e sS;
    private com.samsung.android.spayfw.payprovider.discover.tzsvc.b ti;
    private com.samsung.android.spayfw.payprovider.discover.db.dao.d tj;
    private DiscoverApduHandlerState tk = DiscoverApduHandlerState.sZ;
    private DiscoverPaymentCard tl = null;
    private boolean tm = false;
    private boolean tn;

    public g(Context context) {
        this.mContext = context;
        this.ti = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.E(context);
        this.tj = new com.samsung.android.spayfw.payprovider.discover.db.dao.d(context);
    }

    private void di() {
        if (this.sS != null) {
            c.i(TAG, "cancelTransaction, update secure object...");
            DcStorageManager.a(this.tl.getTokenId(), this.sS.getSecureObject());
            if (this.sS.ed().dK() == DiscoverCLTransactionContext.DiscoverClTransactionType.uj) {
                c.i(TAG, "cancelTransaction, EMV transaction, update transaction profile.");
                DcStorageManager.a(this.tl.getTokenId(), this.sS.ed().getPaymentProfile());
                c.i(TAG, "cancelTransaction, EMV transaction, save pth.");
                DcStorageManager.b(this.tl.getTokenId(), this.sS.ed().getPth().getBytes());
            }
            c.i(TAG, "cancelTransaction, clear transaction context...");
            this.sS.clear();
            this.tk = DiscoverApduHandlerState.sY;
            return;
        }
        c.e(TAG, "cancelTransaction, transaction context is null.");
    }

    private DiscoverPaymentCard r(long l2) {
        DiscoverPaymentCard discoverPaymentCard = DcStorageManager.i(l2);
        c.i(TAG, "loadPaymentCard, loaded " + discoverPaymentCard);
        return discoverPaymentCard;
    }

    public short K(int n2) {
        if (this.tl == null) {
            c.e(TAG, "stopNfcPay: card is null");
            return 1;
        }
        c.i(TAG, "stopNfcPay: reason : " + n2);
        return 2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean a(SecuredObject securedObject, DiscoverCDCVM.DiscoverCDCVMType discoverCDCVMType) {
        if (securedObject == null) {
            c.e(TAG, "authenticateTransaction, secure object not found.");
            return false;
        }
        if (securedObject.getSecureObjectData() == null) {
            c.e(TAG, "authenticateTransaction, secure object is empty.");
            return false;
        }
        if (discoverCDCVMType == null) {
            c.e(TAG, "authenticateTransaction, authentication type is null.");
            return false;
        }
        c.i(TAG, "authenticateTransaction, start transaction authentication...");
        long l2 = -1L;
        try {
            long l3;
            l2 = l3 = this.ti.authenticateTransaction(securedObject.getSecureObjectData());
        }
        catch (DcTAException dcTAException) {
            c.c(TAG, "authenticateTransaction, unexpected TA Exception: " + dcTAException.getMessage(), (Throwable)((Object)dcTAException));
        }
        c.i(TAG, "authenticateTransaction, result: " + l2);
        if (this.sS != null) {
            c.i(TAG, "authenticateTransaction, set cvm result in transaction context.");
            this.sS.a(new DiscoverCDCVM(discoverCDCVMType, l2));
            if (l2 == 0L) {
                this.sS.ed().b((byte)1);
                this.sS.ed().c((byte)0);
                this.sS.ed().dH().setBit(3, 7);
                this.sS.ed().dH().setBit(2, 2);
                this.sS.ed().dI().setBit(1, 5);
            }
        } else {
            c.e(TAG, "authenticateTransaction, cannot find selected card.");
        }
        if (this.tl == null) {
            c.e(TAG, "authenticateTransaction, card profile not found.");
            return false;
        }
        if (this.tl.getDiscoverContactlessPaymentData().getDiscoverApplicationData().getCLApplicationConfigurationOptions().checkBit(1, 5)) {
            c.e(TAG, "authenticateTransaction, confirmation code is not supported by profile.");
            return false;
        }
        if (l2 != 0L) return false;
        return true;
    }

    public void clearCard() {
        if (this.tl == null) {
            c.e(TAG, "clearCard: card is null");
            return;
        }
        if (this.tn) {
            c.i(TAG, "clearCard: save otpk data.");
            DcStorageManager.c(this.tl.getTokenId(), this.sS.eb());
            DcStorageManager.a(this.tl.getTokenId(), this.sS.ee());
            c.i(TAG, "clearCard: call cancelTransaction.");
            this.di();
        }
        c.i(TAG, "clearCard: clean card and context.");
        this.tl = null;
        this.sS = null;
        this.tn = false;
    }

    public boolean dh() {
        c.d(TAG, "initTransaction.");
        this.sS = new e();
        this.tn = false;
        if (this.tl != null) {
            if (this.tl.getOTPK() == null) {
                c.e(TAG, "initTransaction, no available otpk.");
                return false;
            }
            if (this.tl.getSecureObject() == null) {
                c.e(TAG, "initTransaction, no secure object found.");
                return false;
            }
            this.sS.i(this.tl.getOTPK());
            this.sS.setSecureObject(this.tl.getSecureObject());
            HashMap<Integer, DiscoverPaymentProfile> hashMap = this.tl.getDiscoverContactlessPaymentData().getPaymentProfiles();
            if (hashMap == null) {
                c.e(TAG, "initTransaction, no profile map found.");
                return false;
            }
            if (hashMap.size() == 0) {
                c.e(TAG, "initTransaction, cannot find default profile, profiles map is empty.");
            }
            this.sS.ed().a((DiscoverPaymentProfile)hashMap.get((Object)0));
            this.sS.ed().setPth(this.tl.getDiscoverContactlessPaymentData().getPth());
            ByteBuffer byteBuffer = ((DiscoverPaymentProfile)hashMap.get((Object)0)).getCpr();
            if (byteBuffer != null) {
                byteBuffer.clearBit(1, 8);
                byteBuffer.clearBit(1, 7);
                byteBuffer.clearBit(1, 6);
                this.sS.ed().s(byteBuffer);
                c.d(TAG, "initTransaction, cpr: " + this.sS.ed().dI().toHexString());
            }
            this.sS.ed().b((byte)0);
            this.sS.ed().c((byte)1);
            DiscoverRecord discoverRecord = DiscoverTrackData.b(this.tl.getDiscoverContactlessPaymentData().getTrack1DataZipMsMode(), this.tl.getDiscoverContactlessPaymentData().getTrack2DataZipMsMode());
            c.i(TAG, "initTransaction, set ZIP record.");
            this.sS.ed().b(discoverRecord);
            this.tk = DiscoverApduHandlerState.sY;
            return true;
        }
        c.e(TAG, "initTransaction, card is not selected");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        if (inAppDetailedTransactionInfo == null) {
            c.e(TAG, "generateInAppPaymentPayload, transaction info is null.");
            return null;
        }
        if (this.tl == null) {
            c.e(TAG, "generateInAppPaymentPayload, selected card is null.");
            return null;
        }
        byte[] arrby = this.tl.getOTPK();
        if (arrby == null) {
            c.e(TAG, "generateInAppPaymentPayload, otpk bundle is null.");
            return null;
        }
        byte[] arrby2 = this.tl.getSecureObject();
        if (arrby2 == null) {
            c.e(TAG, "generateInAppPaymentPayload, secure object is null.");
            return null;
        }
        byte[] arrby3 = inAppDetailedTransactionInfo.getNonce();
        if (arrby3 == null) {
            c.e(TAG, "generateInAppPaymentPayload, nonce is null.");
            return null;
        }
        long l2 = h.am(this.mContext);
        String string = new SimpleDateFormat("yyMMdd").format(new Date(l2));
        c.d(TAG, "generateInAppPaymentPayload, inapp timestamp: " + string);
        byte[] arrby4 = string != null ? string.getBytes() : null;
        if (arrby4 == null) {
            c.e(TAG, "generateInAppPaymentPayload, timestamp is null.");
            return null;
        }
        d d2 = new d();
        DiscoverInAppCryptoData discoverInAppCryptoData = d2.a(inAppDetailedTransactionInfo, this.tl.getDiscoverContactlessPaymentData());
        c.d(TAG, "generateInAppPaymentPayload, inapp data generation...");
        try {
            c.i(TAG, "generateInAppPaymentPayload, TA call getInAppData.");
            DcTACommands.DiscoverTAGetInAppData.DiscoverTAGetInAppDataResponse discoverTAGetInAppDataResponse = this.ti.c(arrby, arrby2, arrby4);
            c.d(TAG, "generateInAppPaymentPayload, check TA response.");
            if (discoverTAGetInAppDataResponse == null) {
                c.e(TAG, "generateInAppPaymentPayload, TA response is null.");
                return null;
            }
            c.d(TAG, "generateInAppPaymentPayload, get payload");
            if (discoverTAGetInAppDataResponse.en() != null) {
                DcStorageManager.c(this.tl.getTokenId(), discoverTAGetInAppDataResponse.en());
                DcStorageManager.a(this.tl.getTokenId(), discoverTAGetInAppDataResponse.getRemainingOtpkCount());
            }
            discoverInAppCryptoData.setCryptogram(discoverTAGetInAppDataResponse.getPayload());
            c.d(TAG, "generateInAppPaymentPayload, cavvInappCryptogram = " + b.byteArrayToHex(discoverTAGetInAppDataResponse.getPayload()));
            c.d(TAG, "generateInAppPaymentPayload, cavvInappCryptogram atc = " + discoverTAGetInAppDataResponse.getRemainingOtpkCount());
            return d2.a(arrby3, discoverInAppCryptoData);
        }
        catch (DcTAException dcTAException) {
            c.e(TAG, "generateInAppPaymentPayload, unexpected DC TA Exception: " + dcTAException.getMessage());
            dcTAException.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] h(byte[] arrby) {
        ByteBuffer byteBuffer;
        c.i(TAG, "handleApdu: start");
        if (arrby == null || arrby.length == 0) {
            c.e(TAG, "Empty apdu received, apdu byte array is null or empty...");
            return null;
        }
        try {
            byteBuffer = new ByteBuffer(arrby);
            byte by = byteBuffer.getByte(1);
            c.i(TAG, "handleApdu, APDU INS: " + (by & 255));
            if (this.tl == null) {
                c.e(TAG, "handleApdu, APDU INS, card is no selected.");
                return null;
            }
            if (this.sS == null) {
                c.e(TAG, "handleApdu, APDU INS, context is not initialized.");
                return null;
            }
        }
        catch (Exception exception) {
            c.e(TAG, "handleApdu: unexpected exception, " + exception.getMessage());
            exception.printStackTrace();
            return new ByteBuffer(27013).getBytes();
        }
        c.d(TAG, "handleApdu, current state: : " + (Object)((Object)this.tk));
        a a2 = this.tk.e(byteBuffer, this.sS, this.tl);
        if (a2 == null) {
            c.e(TAG, "handleApdu, apdu processing result is null.");
            return new ByteBuffer(27013).getBytes();
        }
        if (a2.dA() != null) {
            this.tk = a2.dA();
        }
        if (a2.dB() != null && a2.dB().getBytes() != null) {
            c.i(TAG, "handleApdu: end: result: " + a2.dB().toHexString());
            return a2.dB().getBytes();
        }
        c.e(TAG, "handleApdu, apdu processing result data is null.");
        return new ByteBuffer(27013).getBytes();
    }

    public void interruptMstPay() {
    }

    public Bundle isTransactionComplete() {
        c.i(TAG, "isTransactionComplete, begin...");
        Bundle bundle = new Bundle();
        if (this.sS == null) {
            c.i(TAG, "isTransactionComplete, transaction context is null, return false.");
            bundle.putBoolean("TRANSACTION_COMPLETE_STATE", false);
            return bundle;
        }
        if (this.sS.ec() == null) {
            c.i(TAG, "isTransactionComplete, transaction result is null, return false.");
            bundle.putBoolean("TRANSACTION_COMPLETE_STATE", false);
            return bundle;
        }
        c.i(TAG, "isTransactionComplete, return transaction complete state: " + this.sS.ec().dC());
        bundle.putBoolean("TRANSACTION_COMPLETE_STATE", this.sS.ec().dC());
        return bundle;
    }

    public boolean prepareMstPay() {
        c.d(TAG, "prepareMstPay: Prepare  MST tracks... ");
        try {
            this.ti.ew();
        }
        catch (DcTAException dcTAException) {
            c.c(TAG, "prepareMstPay, unexpected TA Exception: " + dcTAException.getMessage(), (Throwable)((Object)dcTAException));
            return false;
        }
        c.d(TAG, "prepareMstPay: process MST transaction completed.");
        return true;
    }

    public boolean prepareNfcPay() {
        if (this.sS == null) {
            c.e(TAG, "prepareNfcPay, transaction context is not initialized.");
            return false;
        }
        if (this.sS.getSecureObject() == null) {
            c.e(TAG, "prepareNfcPay, secure object not found.");
            return false;
        }
        if (this.sS.eb() == null) {
            c.e(TAG, "prepareNfcPay, otpk not found.");
            return false;
        }
        try {
            DcTACommands.DiscoverTAInitiateTransaction.DiscoverTAInitiateTransactionResponse.a a2 = this.ti.e(this.sS.getSecureObject(), this.sS.eb());
            this.tn = true;
            this.sS.v(a2.wY.get());
            c.i(TAG, "Commit Data Set, Remaining otpks: " + this.sS.ee());
            this.sS.i(a2.xb.getData());
            this.sS.u(new ByteBuffer(a2.xc.getData()));
            return true;
        }
        catch (DcTAException dcTAException) {
            c.c(TAG, "prepareNfcPay, unexpected TA Exception: " + dcTAException.getMessage(), (Throwable)((Object)dcTAException));
            return false;
        }
        catch (Exception exception) {
            c.e(TAG, "prepareNfcPay, unexpected Exception: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public SelectCardResult q(long l2) {
        SelectCardResult selectCardResult = new SelectCardResult();
        try {
            this.tl = this.r(l2);
            if (this.tl == null) {
                c.e(TAG, "selectCard: cannot load payment profile, token " + l2);
                return selectCardResult;
            }
            if (this.tl.getSecureObject() == null) {
                c.e(TAG, "selectCard: card is not ready for payment, token " + l2);
                return selectCardResult;
            }
            if (!this.ti.isTALoaded()) {
                c.e(TAG, "selectCard: Discover TA is not loaded.");
                return selectCardResult;
            }
            String string = this.ti.getTAInfo().getTAId();
            if (string == null) {
                c.e(TAG, "selectCard: Discover TA id is not defined by ta controller.");
                return selectCardResult;
            }
            byte[] arrby = this.ti.k(this.tl.getSecureObject());
            if (arrby == null) {
                c.e(TAG, "selectCard, Discover TA returns null  nonce. Unload TA.");
                this.ti.unloadTA();
                return selectCardResult;
            }
            c.d(TAG, "selectCard, nonce len: " + arrby.length);
            selectCardResult.setNonce((byte[])arrby.clone());
            selectCardResult.setTaid(string);
            if (!this.dh()) {
                c.e(TAG, "selectCard, cannot initiate transaction context.");
            }
        }
        catch (DcTAException dcTAException) {
            c.c(TAG, "selectCard, unexpected TA Exception: " + dcTAException.getMessage(), (Throwable)((Object)dcTAException));
        }
        catch (Exception exception) {
            c.e(TAG, "selectCard, unexpected exception on card selection: " + exception.getMessage());
            exception.printStackTrace();
        }
        c.i(TAG, "selectCard, completed.");
        return selectCardResult;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean s(long l2) {
        c.d(TAG, "getPayReadyState: mCommitData - " + this.tn);
        if (this.tn) {
            if (this.sS.ee() >= 1L) return true;
            return false;
        }
        if (DcStorageManager.j(l2) < 1L) return false;
        return true;
    }

    public boolean startMstPay(int n2, byte[] arrby) {
        c.i(TAG, "startMstPay: Process MST transaction in the Transaction Service... ");
        try {
            this.ti.b(n2, arrby);
        }
        catch (DcTAException dcTAException) {
            c.c(TAG, "transmitMSTTracks, unexpected TA Exception: " + dcTAException.getMessage(), (Throwable)((Object)dcTAException));
            return false;
        }
        c.i(TAG, "Process MST transaction completed.");
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void stopMstPay(boolean bl) {
        c.i(TAG, "stopMstPay: clear  MST tracks... ");
        try {
            this.ti.ex();
        }
        catch (DcTAException dcTAException) {
            c.c(TAG, "stopMstPay, unexpected TA Exception: " + dcTAException.getMessage(), (Throwable)((Object)dcTAException));
        }
        c.d(TAG, "stopMstPay: clear mst tracks data.");
    }
}

