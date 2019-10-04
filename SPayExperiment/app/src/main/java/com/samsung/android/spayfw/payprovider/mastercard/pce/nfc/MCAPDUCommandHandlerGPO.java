/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mcbp.core.mcbpcards.profile.AlternateContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPTransactionContext;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCAPDUBaseCommandHandler;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCommandResult;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spaytzsvc.api.Blob;

public class MCAPDUCommandHandlerGPO
extends MCCAPDUBaseCommandHandler {
    public static final String TAG = "mcpce_MCAPDUCommandHandlerGPO";

    /*
     * Enabled aggressive block sorting
     */
    private MCCommandResult initTransactionContext(ByteArray byteArray) {
        byte by;
        int n2 = 255 & byteArray.getByte(4);
        if (n2 == 3) {
            if (byteArray.getByte(5) != -125 || byteArray.getByte(6) != 1) {
                c.e(TAG, "GPO initTransactionContext: LC_3 wrong length");
                return this.completeCommand(27013);
            }
            by = byteArray.getByte(7);
            if (this.getTransactionContext().isAlternateAID()) {
                this.getTransactionContext().setAIP(this.getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
            } else {
                this.getTransactionContext().setAIP(this.getPaymentProfile().getContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
            }
            ByteArray byteArray2 = ByteArrayFactory.getInstance().getByteArray(1);
            byteArray2.setByte(0, by);
            this.getTransactionContext().setPDOL(byteArray2);
        } else {
            if (n2 != 13) {
                c.e(TAG, "GPO initTransactionContext: wrong Lc length: " + n2);
                return this.completeCommand(26368);
            }
            if (byteArray.getByte(5) != -125 || byteArray.getByte(6) != 11) {
                c.e(TAG, "GPO initTransactionContext: LC_D wrong length");
                return this.completeCommand(27013);
            }
            by = byteArray.getByte(17);
            ByteArray byteArray3 = byteArray.copyOfRange(7, 15);
            ByteArray byteArray4 = byteArray.copyOfRange(15, 17);
            if (this.getTransactionContext().isAlternateAID()) {
                this.getTransactionContext().setAIP(this.getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
            } else {
                this.getTransactionContext().setAIP(this.getPaymentProfile().getContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
            }
            this.getTransactionContext().setPDOL(byteArray.copyOfRange(7, 18));
            if ((byteArray4.isEqual(ByteArrayFactory.getInstance().getFromWord(2112)) || Utils.isZero(byteArray4)) && Utils.isZero(byteArray3)) {
                ByteArray byteArray5 = this.getTransactionContext().getAIP();
                this.getTransactionContext().setAIP(byteArray5.bitWiseAnd(ByteArrayFactory.getInstance().getFromWord(-129)));
            }
        }
        if (MCAPDUCommandHandlerGPO.isTerminalOffline(by)) {
            c.e(TAG, "GPO initTransactionContext: online terminal requested.");
            return this.completeCommand(27013);
        }
        return this.completeCommand();
    }

    @Override
    public boolean checkCLA(byte by) {
        c.d(TAG, "GPO checkCLA " + McUtils.byteToHex(by));
        return by == -128;
    }

    @Override
    public MCCommandResult checkP1P2Parameters(byte by, byte by2) {
        c.d(TAG, "GPO checking params...");
        if (by != 0 || by2 != 0) {
            c.e(TAG, "GPO check params failed: p1 = " + by + ", p2 = " + by2);
            return this.completeCommand(27270);
        }
        c.d(TAG, "checkParams OK");
        return this.completeCommand();
    }

    @Override
    public MCCommandResult generateResponseAPDU() {
        c.i(TAG, "GPO start to generate RAPDU");
        ByteArray byteArray = this.getPaymentProfile().getContactlessPaymentData().getGPO_Response().clone();
        if (this.getTransactionContext().isAlternateAID()) {
            byteArray = this.getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response().clone();
        }
        byteArray.setByte(4, this.getTransactionContext().getAIP().getByte(0));
        byteArray.setByte(5, this.getTransactionContext().getAIP().getByte(1));
        return this.startTransition(byteArray.append(ByteArrayFactory.getInstance().getFromWord(-28672)));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public MCCommandResult processCommand(ByteArray byteArray) {
        McTAController mcTAController;
        MCCommandResult mCCommandResult = this.initTransactionContext(byteArray);
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)mCCommandResult.getResponseCode())) {
            c.e(TAG, "GPO processCommand: init transaction failed.");
            return mCCommandResult;
        }
        try {
            McTAController mcTAController2;
            mcTAController = mcTAController2 = McTAController.getInstance();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c.e(TAG, "GPO processCommand: cannot initiate MC TA. Unexpected TA exception.");
            mcTAController = null;
        }
        if (mcTAController == null) {
            c.e(TAG, "GPO processCommand: internall error, MC TA isn't loaded.");
            return this.ERROR(28416);
        }
        McTACommands.TASetContext.TASetContextResponse.SetContextOut setContextOut = mcTAController.setContext(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_GPO));
        if (setContextOut == null || setContextOut._atc == null || setContextOut._wrapped_atc_obj == null || setContextOut._iccdn == null) {
            return this.completeCommand(27013);
        }
        byte[] arrby = setContextOut._atc.getData();
        if (arrby == null || arrby.length != 2) {
            c.e(TAG, "GPO setContext: wrong ATC length.");
            return this.ERROR(27013);
        }
        this.getTransactionContext().getTransactionCredentials().setATC(arrby);
        c.i(TAG, "Contactless transaction ATC: " + this.baf.getByteArray(arrby, arrby.length).getHexString());
        byte[] arrby2 = setContextOut._iccdn.getData();
        if (arrby2 == null || arrby2.length != 16) {
            c.e(TAG, "GPO setContext: wrong ICCDN length.");
            return this.ERROR(27013);
        }
        c.d(TAG, "Contactless transaction IDN: " + this.baf.getByteArray(arrby2, arrby2.length).getHexString());
        this.getTransactionContext().getTransactionCredentials().setIDN(arrby2);
        byte[] arrby3 = setContextOut._wrapped_atc_obj.getData();
        if (arrby3 == null) {
            c.e(TAG, "GPO setContext: wrong profile returned from TA.");
            return this.ERROR(27013);
        }
        this.getTransactionContext().getTransactionCredentials().setmWrappedAtcObject(arrby3);
        return this.generateResponseAPDU();
    }
}

