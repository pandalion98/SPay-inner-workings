/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Short
 *  java.lang.String
 */
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

public class TerminalRiskManagmentProcess
extends CommandProcess {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        HCEClientException hCEClientException2;
        TokenAPDUResponse tokenAPDUResponse;
        block10 : {
            String string;
            tokenAPDUResponse = new TokenAPDUResponse();
            try {
                if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                    throw new HCEClientException(27014);
                }
                if (StateMode.TERMINATE == PaymentUtils.getTokenStatus()) {
                    throw new HCEClientException(27013);
                }
            }
            catch (HCEClientException hCEClientException2) {
                if (hCEClientException2.getIsoSW() == 0) {
                    tokenAPDUResponse.setsSW((short)28416);
                    return tokenAPDUResponse;
                }
                break block10;
            }
            if ((Short)ApplicationInfoManager.getApplcationInfoValue("TR_APP_STATE") == 13141 || (Short)ApplicationInfoManager.getApplcationInfoValue("TR_APP_STATE") == -23174) {
                throw new HCEClientException(27013);
            }
            if ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TERMINAL_MODE") != 66) {
                throw new HCEClientException(27013);
            }
            if (commandAPDU.getbP1() != -97 || commandAPDU.getbP2() != 54) {
                throw new HCEClientException(27270);
            }
            if (commandAPDU.getbCommandCase() != CommandAPDU.CASE_2) {
                throw new HCEClientException(26368);
            }
            NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue)ApplicationInfoManager.getApplcationInfoValue("TR_NFC_LUPC_OBJ");
            String string2 = nFCLUPCTagValue != null ? nFCLUPCTagValue.getAtc() : (string = MetaDataManager.getMetaDataValue("RUNNING_ATC"));
            tokenAPDUResponse.setBaApduResponse(HexUtils.hexStringToByteArray("9F3602" + string2));
            return tokenAPDUResponse;
        }
        tokenAPDUResponse.setsSW(hCEClientException2.getIsoSW());
        return tokenAPDUResponse;
    }
}

