package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class SelectApduInfo extends APDU {
    public SelectApduInfo() {
        setCommand(APDURequestCommand.SELECT_AID);
    }

    public String validate() {
        try {
            setValid(false);
            if (!HCEClientConstants.API_INDEX_TOKEN_UPDATE.equals(getParameter1())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
            }
            if (Constants.SERVICE_CODE_LENGTH.equals(getParameter2())) {
                return "6A82";
            }
            if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter2())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
            }
            if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthExpectedData())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            if (getDataLength() == 0) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            if (getData() != null && !getData().startsWith(APDUConstants.APDU_AMEX_AID_PIX)) {
                return "6A82";
            }
            if (getDataLength() != 0 && (getData() == null || getData().length() != getDataLength())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            if (TokenDataParser.isClientVersionUpdateRequired(getParsedTokenRecord())) {
                return "6283";
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
