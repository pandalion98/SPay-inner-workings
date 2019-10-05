/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;

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
                Log.e(TAG, "GPO initTransactionContext: LC_3 wrong length");
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
                Log.e(TAG, "GPO initTransactionContext: wrong Lc length: " + n2);
                return this.completeCommand(26368);
            }
            if (byteArray.getByte(5) != -125 || byteArray.getByte(6) != 11) {
                Log.e(TAG, "GPO initTransactionContext: LC_D wrong length");
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
            Log.e(TAG, "GPO initTransactionContext: online terminal requested.");
            return this.completeCommand(27013);
        }
        return this.completeCommand();
    }

    @Override
    public boolean checkCLA(byte by) {
        Log.d(TAG, "GPO checkCLA " + McUtils.byteToHex(by));
        return by == -128;
    }

    @Override
    public MCCommandResult checkP1P2Parameters(byte by, byte by2) {
        Log.d(TAG, "GPO checking params...");
        if (by != 0 || by2 != 0) {
            Log.e(TAG, "GPO check params failed: p1 = " + by + ", p2 = " + by2);
            return this.completeCommand(27270);
        }
        Log.d(TAG, "checkParams OK");
        return this.completeCommand();
    }

    @Override
    public MCCommandResult generateResponseAPDU() {
        Log.i(TAG, "GPO start to generate RAPDU");
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
            Log.e(TAG, "GPO processCommand: init transaction failed.");
            return mCCommandResult;
        }
        try {
            McTAController mcTAController2;
            mcTAController = mcTAController2 = McTAController.getInstance();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Log.e(TAG, "GPO processCommand: cannot initiate MC TA. Unexpected TA exception.");
            mcTAController = null;
        }
        if (mcTAController == null) {
            Log.e(TAG, "GPO processCommand: internall error, MC TA isn't loaded.");
            return this.ERROR(28416);
        }
        McTACommands.TASetContext.TASetContextResponse.SetContextOut setContextOut = mcTAController.setContext(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_GPO));
        if (setContextOut == null || setContextOut._atc == null || setContextOut._wrapped_atc_obj == null || setContextOut._iccdn == null) {
            return this.completeCommand(27013);
        }
        byte[] arrby = setContextOut._atc.getData();
        if (arrby == null || arrby.length != 2) {
            Log.e(TAG, "GPO setContext: wrong ATC length.");
            return this.ERROR(27013);
        }
        this.getTransactionContext().getTransactionCredentials().setATC(arrby);
        Log.i(TAG, "Contactless transaction ATC: " + this.baf.getByteArray(arrby, arrby.length).getHexString());
        byte[] arrby2 = setContextOut._iccdn.getData();
        if (arrby2 == null || arrby2.length != 16) {
            Log.e(TAG, "GPO setContext: wrong ICCDN length.");
            return this.ERROR(27013);
        }
        Log.d(TAG, "Contactless transaction IDN: " + this.baf.getByteArray(arrby2, arrby2.length).getHexString());
        this.getTransactionContext().getTransactionCredentials().setIDN(arrby2);
        byte[] arrby3 = setContextOut._wrapped_atc_obj.getData();
        if (arrby3 == null) {
            Log.e(TAG, "GPO setContext: wrong profile returned from TA.");
            return this.ERROR(27013);
        }
        this.getTransactionContext().getTransactionCredentials().setmWrappedAtcObject(arrby3);
        return this.generateResponseAPDU();
    }
}

