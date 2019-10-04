/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mcbpcards.profile.AlternateContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPTransactionContext;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUConstants;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCAPDUBaseCommandHandler;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCommandResult;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;

public class MCAPDUCommandHandlerSELECT
extends MCCAPDUBaseCommandHandler {
    private static final String TAG = "mcpce_MCAPDUCommandHandlerSELECT";
    private ByteArray mAID = null;

    @Override
    public boolean checkCLA(byte by) {
        c.i(TAG, "SELECT checkCLA: " + McUtils.byteToHex(by));
        return by == -128 || by == 0;
    }

    @Override
    public MCCommandResult checkP1P2Parameters(byte by, byte by2) {
        if (by != 4 || by2 != 0) {
            c.e(TAG, "C-APDU SELECT Wrong P1P2 params, p1 = " + by + ", p2 = " + by2 + "; expected  p1 = " + 4 + ", expected p2 = " + 0);
            return this.ERROR(27270);
        }
        return this.completeCommand();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public MCCommandResult generateResponseAPDU() {
        if (this.mAID == null) {
            c.e(TAG, "C-APDU SELECT Cannot parse primary aid, null");
            return this.ERROR(26368);
        }
        if (this.mAID.isEqual(MCAPDUConstants.PPSE_AID)) {
            return this.completeCommand(this.getPaymentProfile().getContactlessPaymentData().getPPSE_FCI().clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
        }
        if (this.mAID.isEqual(this.getPaymentProfile().getContactlessPaymentData().getAID())) {
            this.getTransactionContext().setAlternateAID(false);
            return this.startTransition(this.getPaymentProfile().getContactlessPaymentData().getPaymentFCI().clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
        }
        AlternateContactlessPaymentData alternateContactlessPaymentData = this.getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData();
        if (alternateContactlessPaymentData == null || alternateContactlessPaymentData.getAID() == null || !this.mAID.isEqual(alternateContactlessPaymentData.getAID())) {
            c.e(TAG, "C-APDU SELECT Cannot find requested aid: " + this.mAID.getHexString());
            if (alternateContactlessPaymentData != null && alternateContactlessPaymentData.getAID() != null) {
                c.i(TAG, "C-APDU SELECT Alt aid: " + alternateContactlessPaymentData.getAID().getHexString());
                do {
                    return this.ERROR(27266);
                    break;
                } while (true);
            }
            c.e(TAG, "Alt Aid is null");
            return this.ERROR(27266);
        }
        this.getTransactionContext().setAlternateAID(true);
        return this.startTransition(alternateContactlessPaymentData.getPaymentFCI().clone().append(ByteArrayFactory.getInstance().getFromWord(-28672)));
    }

    protected ByteArray parseSelectAID(ByteArray byteArray) {
        byte by = (byte)(255 & byteArray.getByte(4));
        ByteArray byteArray2 = ByteArrayFactory.getInstance().getByteArray(by);
        if (byteArray2 != null) {
            byteArray2.copyBytes(byteArray, 5, 0, by);
        }
        return byteArray2;
    }

    @Override
    public MCCommandResult processCommand(ByteArray byteArray) {
        try {
            this.mAID = this.parseSelectAID(byteArray);
            MCCommandResult mCCommandResult = this.completeCommand();
            return mCCommandResult;
        }
        catch (Exception exception) {
            c.e(TAG, "C-APDU SELECT Unexpected excception during aid parsing, msg = " + exception.getMessage());
            exception.printStackTrace();
            return this.ERROR(26368);
        }
    }
}

