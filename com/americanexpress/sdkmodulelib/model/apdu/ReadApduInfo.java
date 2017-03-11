package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class ReadApduInfo extends APDU {
    public ReadApduInfo() {
        setCommand(APDURequestCommand.READ);
    }

    public String validate() {
        try {
            setValid(false);
            if (getLengthCommandData() != null) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            if (getLengthExpectedData() != null && !HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthExpectedData())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            if (HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter1())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
            }
            if (!"0C".equals(getParameter2())) {
                return "6A83";
            }
            if (TokenDataParser.isClientVersionUpdateRequired(getParsedTokenRecord())) {
                return "6986";
            }
            setValid(true);
            return APDUConstants.APDU_COMMAND_STATUS_WORD_SUCCESS;
        } catch (Exception e) {
            setValid(false);
            return APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE;
        }
    }

    public APDUResponse buildResponse() {
        return null;
    }
}
