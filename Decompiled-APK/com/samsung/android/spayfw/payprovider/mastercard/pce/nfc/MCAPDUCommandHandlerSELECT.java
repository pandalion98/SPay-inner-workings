package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mcbpcards.profile.AlternateContactlessPaymentData;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class MCAPDUCommandHandlerSELECT extends MCCAPDUBaseCommandHandler {
    private static final String TAG = "mcpce_MCAPDUCommandHandlerSELECT";
    private ByteArray mAID;

    public MCAPDUCommandHandlerSELECT() {
        this.mAID = null;
    }

    public MCCommandResult checkP1P2Parameters(byte b, byte b2) {
        if (b == (byte) 4 && b2 == null) {
            return completeCommand();
        }
        Log.m286e(TAG, "C-APDU SELECT Wrong P1P2 params, p1 = " + b + ", p2 = " + b2 + "; expected  p1 = " + 4 + ", expected p2 = " + 0);
        return ERROR(27270);
    }

    public MCCommandResult processCommand(ByteArray byteArray) {
        try {
            this.mAID = parseSelectAID(byteArray);
            return completeCommand();
        } catch (Exception e) {
            Log.m286e(TAG, "C-APDU SELECT Unexpected excception during aid parsing, msg = " + e.getMessage());
            e.printStackTrace();
            return ERROR(26368);
        }
    }

    public MCCommandResult generateResponseAPDU() {
        if (this.mAID == null) {
            Log.m286e(TAG, "C-APDU SELECT Cannot parse primary aid, null");
            return ERROR(26368);
        } else if (this.mAID.isEqual(MCAPDUConstants.PPSE_AID)) {
            return completeCommand(getPaymentProfile().getContactlessPaymentData().getPPSE_FCI().clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
        } else {
            if (this.mAID.isEqual(getPaymentProfile().getContactlessPaymentData().getAID())) {
                getTransactionContext().setAlternateAID(false);
                return startTransition(getPaymentProfile().getContactlessPaymentData().getPaymentFCI().clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
            }
            AlternateContactlessPaymentData alternateContactlessPaymentData = getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData();
            if (alternateContactlessPaymentData == null || alternateContactlessPaymentData.getAID() == null || !this.mAID.isEqual(alternateContactlessPaymentData.getAID())) {
                Log.m286e(TAG, "C-APDU SELECT Cannot find requested aid: " + this.mAID.getHexString());
                if (alternateContactlessPaymentData == null || alternateContactlessPaymentData.getAID() == null) {
                    Log.m286e(TAG, "Alt Aid is null");
                } else {
                    Log.m287i(TAG, "C-APDU SELECT Alt aid: " + alternateContactlessPaymentData.getAID().getHexString());
                }
                return ERROR(27266);
            }
            getTransactionContext().setAlternateAID(true);
            return startTransition(alternateContactlessPaymentData.getPaymentFCI().clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
        }
    }

    protected ByteArray parseSelectAID(ByteArray byteArray) {
        byte b = (byte) (byteArray.getByte(4) & GF2Field.MASK);
        ByteArray byteArray2 = ByteArrayFactory.getInstance().getByteArray(b);
        if (byteArray2 != null) {
            byteArray2.copyBytes(byteArray, 5, 0, b);
        }
        return byteArray2;
    }

    public boolean checkCLA(byte b) {
        Log.m287i(TAG, "SELECT checkCLA: " + McUtils.byteToHex(b));
        return b == -128 || b == null;
    }
}
