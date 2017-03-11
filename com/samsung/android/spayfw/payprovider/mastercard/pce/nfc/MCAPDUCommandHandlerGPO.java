package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable.TAProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUConstants.MCCountryCode;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TASetContext.TASetContextResponse.SetContextOut;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class MCAPDUCommandHandlerGPO extends MCCAPDUBaseCommandHandler {
    public static final String TAG = "mcpce_MCAPDUCommandHandlerGPO";

    public MCCommandResult checkP1P2Parameters(byte b, byte b2) {
        Log.m285d(TAG, "GPO checking params...");
        if (b == null && b2 == null) {
            Log.m285d(TAG, "checkParams OK");
            return completeCommand();
        }
        Log.m286e(TAG, "GPO check params failed: p1 = " + b + ", p2 = " + b2);
        return completeCommand(27270);
    }

    public MCCommandResult processCommand(ByteArray byteArray) {
        MCCommandResult initTransactionContext = initTransactionContext(byteArray);
        if (MCTransactionResult.COMMAND_COMPLETED.equals(initTransactionContext.getResponseCode())) {
            McTAController mcTAController = null;
            try {
                mcTAController = McTAController.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
                Log.m286e(TAG, "GPO processCommand: cannot initiate MC TA. Unexpected TA exception.");
            }
            if (mcTAController == null) {
                Log.m286e(TAG, "GPO processCommand: internall error, MC TA isn't loaded.");
                return ERROR(28416);
            }
            SetContextOut context = mcTAController.setContext(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_TA_GPO));
            if (context == null || context._atc == null || context._wrapped_atc_obj == null || context._iccdn == null) {
                return completeCommand(27013);
            }
            byte[] data = context._atc.getData();
            if (data == null || data.length != 2) {
                Log.m286e(TAG, "GPO setContext: wrong ATC length.");
                return ERROR(27013);
            }
            getTransactionContext().getTransactionCredentials().setATC(data);
            Log.m287i(TAG, "Contactless transaction ATC: " + this.baf.getByteArray(data, data.length).getHexString());
            data = context._iccdn.getData();
            if (data == null || data.length != 16) {
                Log.m286e(TAG, "GPO setContext: wrong ICCDN length.");
                return ERROR(27013);
            }
            Log.m285d(TAG, "Contactless transaction IDN: " + this.baf.getByteArray(data, data.length).getHexString());
            getTransactionContext().getTransactionCredentials().setIDN(data);
            byte[] data2 = context._wrapped_atc_obj.getData();
            if (data2 == null) {
                Log.m286e(TAG, "GPO setContext: wrong profile returned from TA.");
                return ERROR(27013);
            }
            getTransactionContext().getTransactionCredentials().setmWrappedAtcObject(data2);
            return generateResponseAPDU();
        }
        Log.m286e(TAG, "GPO processCommand: init transaction failed.");
        return initTransactionContext;
    }

    public MCCommandResult generateResponseAPDU() {
        Log.m287i(TAG, "GPO start to generate RAPDU");
        ByteArray clone = getPaymentProfile().getContactlessPaymentData().getGPO_Response().clone();
        if (getTransactionContext().isAlternateAID()) {
            clone = getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response().clone();
        }
        clone.setByte(4, getTransactionContext().getAIP().getByte(0));
        clone.setByte(5, getTransactionContext().getAIP().getByte(1));
        return startTransition(clone.append(ByteArrayFactory.getInstance().getFromWord(-28672)));
    }

    private MCCommandResult initTransactionContext(ByteArray byteArray) {
        byte b;
        int i = byteArray.getByte(4) & GF2Field.MASK;
        ByteArray byteArray2;
        if (i == 3) {
            if (byteArray.getByte(5) == (byte) -125 && byteArray.getByte(6) == 1) {
                b = byteArray.getByte(7);
                if (getTransactionContext().isAlternateAID()) {
                    getTransactionContext().setAIP(getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
                } else {
                    getTransactionContext().setAIP(getPaymentProfile().getContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
                }
                byteArray2 = ByteArrayFactory.getInstance().getByteArray(1);
                byteArray2.setByte(0, b);
                getTransactionContext().setPDOL(byteArray2);
            } else {
                Log.m286e(TAG, "GPO initTransactionContext: LC_3 wrong length");
                return completeCommand(27013);
            }
        } else if (i != 13) {
            Log.m286e(TAG, "GPO initTransactionContext: wrong Lc length: " + i);
            return completeCommand(26368);
        } else if (byteArray.getByte(5) == (byte) -125 && byteArray.getByte(6) == 11) {
            b = byteArray.getByte(17);
            byteArray2 = byteArray.copyOfRange(7, 15);
            ByteArray copyOfRange = byteArray.copyOfRange(15, 17);
            if (getTransactionContext().isAlternateAID()) {
                getTransactionContext().setAIP(getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
            } else {
                getTransactionContext().setAIP(getPaymentProfile().getContactlessPaymentData().getGPO_Response().copyOfRange(4, 6));
            }
            getTransactionContext().setPDOL(byteArray.copyOfRange(7, 18));
            if ((copyOfRange.isEqual(ByteArrayFactory.getInstance().getFromWord(MCCountryCode.US)) || Utils.isZero(copyOfRange)) && Utils.isZero(byteArray2)) {
                getTransactionContext().setAIP(getTransactionContext().getAIP().bitWiseAnd(ByteArrayFactory.getInstance().getFromWord(-129)));
            }
        } else {
            Log.m286e(TAG, "GPO initTransactionContext: LC_D wrong length");
            return completeCommand(27013);
        }
        if (!MCCAPDUBaseCommandHandler.isTerminalOffline(b)) {
            return completeCommand();
        }
        Log.m286e(TAG, "GPO initTransactionContext: online terminal requested.");
        return completeCommand(27013);
    }

    public boolean checkCLA(byte b) {
        Log.m285d(TAG, "GPO checkCLA " + McUtils.byteToHex(b));
        return b == -128;
    }
}
