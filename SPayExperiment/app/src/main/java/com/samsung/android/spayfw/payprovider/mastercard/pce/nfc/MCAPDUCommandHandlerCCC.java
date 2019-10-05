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
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.emv.CCCRespApdu;
import com.mastercard.mobile_api.utils.apdu.emv.ComputeCCApdu;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;

public class MCAPDUCommandHandlerCCC
extends MCCAPDUBaseCommandHandler {
    public static final String TAG = "mcpce_MCAPDUCommandHandlerCCC";
    private ComputeCCApdu mCCApdu;

    /*
     * Enabled aggressive block sorting
     */
    private MCCommandResult composeCardholderVerificationResult() {
        Log.i(TAG, "Start checking CVM/CC Context/ Card risk management data");
        ByteArray byteArray = this.baf.getByteArray(3);
        if (this.getTransactionContext().getTransactionCredentials().getCVMResult() != null && this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0L) {
            byteArray.setByte(1, (byte)16);
        }
        this.getTransactionContext().setPOSCII(byteArray);
        if (this.getPaymentProfile().getCardRiskManagementData().getCRM_CountryCode().isEqual(this.mCCApdu.getTerminalCountryCode())) {
            ByteArray byteArray2 = this.baf.getFromWord(512);
            if (this.getPaymentProfile().getContactlessPaymentData().getCIAC_DeclineOnPPMS().bitWiseAnd(byteArray2).isEqual(byteArray2)) {
                Log.e(TAG, "ComputeCC CIAC_DeclineOnPPMS failed: bb0200");
                return this.completeTransaction(this.decline(this.mCCApdu));
            }
        } else {
            ByteArray byteArray3 = this.baf.getFromWord(1024);
            if (this.getPaymentProfile().getContactlessPaymentData().getCIAC_DeclineOnPPMS().bitWiseAnd(byteArray3).isEqual(byteArray3)) {
                Log.e(TAG, "ComputeCC CIAC_DeclineOnPPMS failed: bb0400");
                return this.completeTransaction(this.decline(this.mCCApdu));
            }
        }
        if (this.getTransactionContext().getTransactionCredentials().getCVMResult() == null || this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() != 0L) {
            Log.e(TAG, "ComputeCC 3.3 rejection as CVM required");
            this.getTransactionContext().setTransactionResult(MCTransactionResult.CONTEXT_CONFLICT_CVM);
            this.getTransactionContext().setTransactionError(MCTransactionResult.CONTEXT_CONFLICT_CVM);
            byte by = (byte)(1 | this.getTransactionContext().getPOSCII().getByte(1));
            this.getTransactionContext().getPOSCII().setByte(1, by);
            return this.completeTransaction(this.decline(this.mCCApdu));
        }
        if (!this.getTransactionContext().isOnlineAllowed()) {
            Log.e(TAG, "ComputeCC online transaction is not allowed.");
            return this.completeTransaction(this.decline(this.mCCApdu));
        }
        this.getTransactionContext().getTransactionInformation().setCid((byte)-128);
        byte by = this.mCCApdu.getMobileSupportIndicator().getByte(0);
        if (this.getTransactionContext().getTransactionCredentials().getCVMResult() != null && this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0L && (by & 1) == 1) {
            this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_TRACK1_CVM));
        } else {
            this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_TRACK1_NO_CVM));
        }
        Log.i(TAG, "Compute CC: start checking CVM/CC Context/ Card risk management data OK");
        return this.completeCommand();
    }

    private byte[] computeCryptoInputData1() {
        return this.mCCApdu.getUnpredictableNumber().getBytes();
    }

    /*
     * Enabled aggressive block sorting
     */
    private ByteArray decline(ComputeCCApdu computeCCApdu) {
        ByteArray byteArray;
        Log.e(TAG, "Compute CC: start decline transaction");
        this.getTransactionContext().getTransactionInformation().setCid((byte)0);
        if ((1 & computeCCApdu.getMobileSupportIndicator().getByte(0)) != 1) {
            Log.e(TAG, "Compute CC: failed online support indicator mask.");
            byteArray = this.baf.getFromWord(27010);
        } else {
            ByteArray byteArray2 = this.baf.getFromWord(-24778);
            byteArray2.appendByte((byte)2);
            byteArray2.append(this.getTransactionContext().getTransactionCredentials().getATC());
            byteArray2.append(this.baf.getFromWord(-8373));
            byteArray2.appendByte((byte)3);
            byteArray2.append(this.getTransactionContext().getPOSCII());
            byteArray = new CCCRespApdu(byteArray2).getByteArray();
        }
        this.getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
        if (this.getTransactionContext().getTransactionError() == null || this.getTransactionContext().getTransactionError() == MCTransactionResult.CONTEXT_CONFLICT_PASS) {
            this.getTransactionContext().setTransactionError(MCTransactionResult.TRANSACTION_COMPLETED_ERROR_CCC_DECLINE);
        }
        return byteArray;
    }

    private MCCommandResult initializeTransactionContext() {
        if (MCAPDUCommandHandlerCCC.isTerminalOffline(this.mCCApdu.getTerminalType())) {
            Log.e(TAG, "ComputeCC initializeTransactionContext: offline terminal.");
            return this.ERROR(27013);
        }
        MCTransactionInformation mCTransactionInformation = this.getTransactionContext().getTransactionInformation();
        mCTransactionInformation.setAmount(this.mCCApdu.getAuthorizedAmount().clone());
        mCTransactionInformation.setCurrencyCode(this.mCCApdu.getTransactionCurrencyCode());
        mCTransactionInformation.setTransactionDate(this.mCCApdu.getTransactionDate());
        mCTransactionInformation.setTransactionType(this.mCCApdu.getTransactionType());
        mCTransactionInformation.setUN(this.mCCApdu.getUnpredictableNumber());
        Log.i(TAG, "Init transaction context and check terminal type OK");
        return this.completeCommand();
    }

    private MCCommandResult verifyPaymentProfile(ByteArray byteArray) {
        if (byteArray != null && byteArray.getLength() > 4) {
            int n2 = 255 & byteArray.getByte(4);
            if (n2 != 20 && n2 != 16) {
                Log.e(TAG, "ComputeCC verifyPaymentProfile: wronng lc length.");
                return this.ERROR(26368);
            }
            if (this.mPaymentProfile.getContactlessPaymentData().getCIAC_DeclineOnPPMS() == null) {
                Log.e(TAG, "ComputeCC verifyPaymentProfile: CIAC_DeclineOnPPMS is empty.");
                return this.ERROR(27013);
            }
            this.mCCApdu = new ComputeCCApdu(byteArray);
            return this.completeCommand();
        }
        Log.e(TAG, "ComputeCC verifyPaymentProfile: wronng apdu length.");
        return this.ERROR(26368);
    }

    @Override
    public boolean checkCLA(byte by) {
        return by == -128;
    }

    @Override
    public MCCommandResult checkP1P2Parameters(byte by, byte by2) {
        if (by != -114 || by2 != -128) {
            Log.e(TAG, "Compute CC: incorrect p1/p2 params, p1 = " + by + ", p2 = " + by2 + "; expected p1 = " + 142 + ", expected p2 = " + 128);
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
        if (this.getTransactionContext().getCryptoOutput().getCryptogram() == null || this.getTransactionContext().getCryptoOutput().getCryptogram().getLength() != 8) {
            if (this.getTransactionContext().getCryptoOutput().getCryptogram() != null) {
                Log.e(TAG, "Compute CC: cryptogram track1 len is invalid: " + this.getTransactionContext().getCryptoOutput().getCryptogram().getLength());
                do {
                    return this.ERROR(27010);
                    break;
                } while (true);
            }
            Log.e(TAG, "Compute CC: empty cryptogram track1 ");
            return this.ERROR(27010);
        }
        Log.d(TAG, "Compute CC: Calling generateRAPDU..." + this.getTransactionContext().getCryptoOutput().getCryptogram().getHexString());
        if (this.getTransactionContext().getCryptoOutput().getCryptogramTrack2() == null || this.getTransactionContext().getCryptoOutput().getCryptogramTrack2().getLength() != 8) {
            if (this.getTransactionContext().getCryptoOutput().getCryptogramTrack2() != null) {
                Log.e(TAG, "Compute CC: cryptogram track2 len is invalid: " + this.getTransactionContext().getCryptoOutput().getCryptogramTrack2().getLength());
                do {
                    return this.ERROR(27010);
                    break;
                } while (true);
            }
            Log.e(TAG, "Compute CC: empty cryptogram track2 ");
            return this.ERROR(27010);
        }
        ByteArray byteArray = this.baf.getFromWord(-24735);
        byteArray.appendByte((byte)2);
        byteArray.append(this.getTransactionContext().getCryptoOutput().getCryptogramTrack2().copyOfRange(6, 8));
        byteArray.append(this.baf.getFromWord(-24736));
        byteArray.appendByte((byte)2);
        byteArray.append(this.getTransactionContext().getCryptoOutput().getCryptogram().copyOfRange(6, 8));
        byteArray.append(this.baf.getFromWord(-24778));
        byteArray.appendByte((byte)2);
        byteArray.append(this.getTransactionContext().getTransactionCredentials().getATC());
        Log.i(TAG, "Compute CC: Calling generateRAPDU...POCII");
        if ((1 & this.mCCApdu.getMobileSupportIndicator().getByte(0)) == 1) {
            byte by = (byte)(16 | this.getTransactionContext().getPOSCII().getByte(1));
            this.getTransactionContext().getPOSCII().setByte(1, by);
            byteArray.append(TLV.create(this.baf.getFromWord(-8373), this.getTransactionContext().getPOSCII().clone()));
        }
        Log.i(TAG, "Compute CC: Calling generateRAPDU...OK");
        this.getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
        return this.completeTransaction(new CCCRespApdu(byteArray).getByteArray());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public MCCommandResult processCommand(ByteArray byteArray) {
        MCCommandResult mCCommandResult = this.verifyPaymentProfile(byteArray);
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)mCCommandResult.getResponseCode())) {
            Log.e(TAG, "ComputeCC: verify payment profile failed, error code " + (Object)((Object)mCCommandResult.getResponseCode()));
            return mCCommandResult;
        }
        MCCommandResult mCCommandResult2 = this.initializeTransactionContext();
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)mCCommandResult2.getResponseCode())) {
            Log.e(TAG, "ComputeCC: initialize transaction context failed, error code " + (Object)((Object)mCCommandResult2.getResponseCode()));
            return mCCommandResult2;
        }
        MCCryptoOutput mCCryptoOutput = new MCCryptoOutput();
        mCCryptoOutput.setIssuerApplicationData(this.mPaymentProfile.getContactlessPaymentData().getIssuerApplicationData().clone());
        this.getTransactionContext().setCryptoOutput(mCCryptoOutput);
        if (this.getTASecureContainer() == null) {
            Log.e(TAG, "ComputeCC: secure object not found");
            return this.ERROR(27013);
        }
        MCCommandResult mCCommandResult3 = this.composeCardholderVerificationResult();
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)mCCommandResult3.getResponseCode())) {
            Log.e(TAG, "ComputeCC: compose CVR failed, response code " + (Object)((Object)mCCommandResult3.getResponseCode()));
            return mCCommandResult3;
        }
        byte[] arrby = this.computeCryptoInputData1();
        if (arrby == null) {
            Log.e(TAG, "ComputeCC: input data 1 is empty for MagStripe transaction.");
            return this.ERROR(27013);
        }
        if (this.getTransactionContext().getTransactionCredentials().getATC() == null) {
            Log.e(TAG, "ComputeCC: ATC value is empty...");
            return this.ERROR(27013);
        }
        if (this.getTransactionContext().getTransactionCredentials().getProfileType() == 0) {
            Log.e(TAG, "ComputeCC: Cannot find correct crypto profile...");
            return this.ERROR(27013);
        }
        try {
            McTAController mcTAController = McTAController.getInstance();
            int n2 = this.getTransactionContext().getTransactionCredentials().getProfileType();
            McTACommands.TAComputeCC.TAComputeCCResponse.TAComputeCCOut tAComputeCCOut = mcTAController.computeCC(n2, n2 + 1, arrby, this.getTransactionContext().getTransactionInformation().getUN().getBytes());
            if (tAComputeCCOut == null) {
                Log.e(TAG, "ComputeCC processCommand: ComputeCC result is null returned from TA.");
                return this.ERROR(27013);
            }
            if (tAComputeCCOut._taMACTrack1 == null) {
                Log.e(TAG, "ComputeCC processCommand: failed to compute track1 data.");
                return this.ERROR(27013);
            }
            if (tAComputeCCOut._taMACTrack2 == null) {
                Log.e(TAG, "ComputeCC processCommand: failed to compute track1 data.");
                return this.ERROR(27013);
            }
            byte[] arrby2 = tAComputeCCOut._taMACTrack1.getData();
            if (arrby2 == null || arrby2.length != 8) {
                Log.e(TAG, "ComputeCC: wrong track1 cryptogram length...");
                return this.ERROR(27010);
            }
            byte[] arrby3 = tAComputeCCOut._taMACTrack2.getData();
            if (arrby3 == null || arrby3.length != 8) {
                Log.e(TAG, "ComputeCC: wrong track2 cryptogram length...");
                return this.ERROR(27010);
            }
            this.getTransactionContext().getCryptoOutput().setCryptogram(this.baf.getByteArray((byte[])arrby2.clone(), arrby2.length));
            this.getTransactionContext().getCryptoOutput().setCryptogramTrack2(this.baf.getByteArray((byte[])arrby3.clone(), arrby3.length));
        }
        catch (Exception exception) {
            exception.printStackTrace();
            Log.e(TAG, "Unexpected TAException: " + exception.toString());
            return this.ERROR(27013);
        }
        return this.generateResponseAPDU();
    }
}

