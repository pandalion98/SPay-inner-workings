package com.americanexpress.mobilepayments.hceclient.payments.nfc.process;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.samsung.android.spayfw.appinterface.ISO7816;

public class TerminalRiskManagmentProcess extends CommandProcess {
    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        TokenAPDUResponse tokenAPDUResponse = new TokenAPDUResponse();
        try {
            if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException((short) ISO7816.SW_COMMAND_NOT_ALLOWED);
            } else if (StateMode.TERMINATE == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() == ApplicationInfoManager.STATE_APP_SELECTED || ((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() == ApplicationInfoManager.STATE_PPSE_SELECTED) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE)).byteValue() != 66) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (commandAPDU.getbP1() != -97 || commandAPDU.getbP2() != 54) {
                throw new HCEClientException((short) ISO7816.SW_INCORRECT_P1P2);
            } else if (commandAPDU.getbCommandCase() != CommandAPDU.CASE_2) {
                throw new HCEClientException((short) ISO7816.SW_WRONG_LENGTH);
            } else {
                String atc;
                NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_NFC_LUPC_OBJ);
                String str = "0000";
                if (nFCLUPCTagValue != null) {
                    atc = nFCLUPCTagValue.getAtc();
                } else {
                    atc = MetaDataManager.getMetaDataValue(MetaDataManager.RUNNING_ATC);
                }
                tokenAPDUResponse.setBaApduResponse(HexUtils.hexStringToByteArray(APDUConstants.GET_DATA_PREPEND + atc));
                return tokenAPDUResponse;
            }
        } catch (HCEClientException e) {
            if (e.getIsoSW() == (short) 0) {
                tokenAPDUResponse.setsSW(ISO7816.SW_UNKNOWN);
            } else {
                tokenAPDUResponse.setsSW(e.getIsoSW());
            }
        }
    }
}
