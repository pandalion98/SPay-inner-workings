package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class GenerateAppCryptoApduInfo extends APDU {
    public GenerateAppCryptoApduInfo() {
        setCommand(APDURequestCommand.GENAC);
    }

    public String validate() {
        try {
            if (SessionManager.getSession().isEMVTransaction()) {
                return validateEMV();
            }
            return validateMAG();
        } catch (Exception e) {
            setValid(false);
            return APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE;
        }
    }

    private String validateEMV() {
        setValid(false);
        if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter1()) && !"40".equals(getParameter1()) && !APDUConstants.GPO_RESPONSE_TEMPLATE.equals(getParameter1())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
        }
        if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter2())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
        }
        if (getLengthCommandData() != null && HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthCommandData())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
        }
        if (!"1d".equalsIgnoreCase(getLengthCommandData())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
        }
        if (getLengthExpectedData() != null && !HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthExpectedData())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
        }
        if (TokenDataParser.isClientVersionUpdateRequired(getParsedTokenRecord())) {
            return "6986";
        }
        setValid(true);
        return APDUConstants.APDU_COMMAND_STATUS_WORD_SUCCESS;
    }

    private String validateMAG() {
        setValid(false);
        if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter1()) && !"40".equals(getParameter1()) && !APDUConstants.GPO_RESPONSE_TEMPLATE.equals(getParameter1())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
        }
        if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter2())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
        }
        if (getLengthCommandData() != null && HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthCommandData())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
        }
        if (!HCEClientConstants.API_INDEX_TOKEN_UPDATE.equals(getLengthCommandData())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
        }
        if (getLengthExpectedData() != null && !HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthExpectedData())) {
            return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
        }
        if (TokenDataParser.isClientVersionUpdateRequired(getParsedTokenRecord())) {
            return "6986";
        }
        setValid(true);
        return APDUConstants.APDU_COMMAND_STATUS_WORD_SUCCESS;
    }

    public APDUResponse buildResponse() {
        return null;
    }

    public String getRandomNumber() {
        return getData();
    }
}
