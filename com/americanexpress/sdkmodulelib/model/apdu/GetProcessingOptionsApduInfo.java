package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import java.util.List;

public class GetProcessingOptionsApduInfo extends APDU {
    private static final String GPO_DATA_LENGTH = "03";
    private static final String TVL_TEMP_TAG = "F1";

    public GetProcessingOptionsApduInfo() {
        setCommand(APDURequestCommand.GPO);
    }

    public void setData(String str) {
        super.setData(str);
        if (str != null) {
            setTerminalType(str);
        }
    }

    public String validate() {
        try {
            setValid(false);
            if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter1())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
            }
            if (!HCEClientConstants.HEX_ZERO_BYTE.equals(getParameter2())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_INCORRECT_P1P2;
            }
            if (getLengthCommandData() != null && HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthCommandData())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            if (!GPO_DATA_LENGTH.equals(getLengthCommandData())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            if (getData() == null) {
                return "6A80";
            }
            if (!getData().substring(0, 2).equals("83")) {
                return "6A80";
            }
            if (getData().substring(2, 4).equals(HCEClientConstants.HEX_ZERO_BYTE)) {
                return "6A80";
            }
            if (TokenDataParser.isClientVersionUpdateRequired(getParsedTokenRecord())) {
                return "6986";
            }
            if (getLengthExpectedData() != null && !HCEClientConstants.HEX_ZERO_BYTE.equals(getLengthExpectedData())) {
                return APDUConstants.APDU_COMMAND_STATUS_WORD_WRONG_LENGTH;
            }
            setValid(true);
            return APDUConstants.APDU_COMMAND_STATUS_WORD_SUCCESS;
        } catch (Exception e) {
            setValid(false);
            return APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE;
        }
    }

    private void setTerminalType(String str) {
        String str2 = TVL_TEMP_TAG;
        String str3 = str2 + GPO_DATA_LENGTH;
        TagInfo tagInfo = null;
        try {
            List<TagInfo> parseTLV = new TLVHelper().parseTLV(str3 + str);
            if (parseTLV != null && parseTLV.size() == 2) {
                for (TagInfo tagInfo2 : parseTLV) {
                    if (!tagInfo2.getTagName().equals(str2)) {
                        break;
                    }
                }
            }
            TagInfo tagInfo22 = tagInfo;
            if (tagInfo22 == null) {
                SessionManager.getSession().setEMVTerminal(false);
                return;
            }
            SessionManager.getSession().setTerminalTypeData(tagInfo22.getTagValue());
            SessionManager.getSession().setEMVTerminal(TokenDataParser.isEMV(tagInfo22.getTagValue()));
            SessionManager.getSession().setIsTerminalOffline(TokenDataParser.isTerminalTypeOffline(tagInfo22.getTagValue()));
        } catch (Exception e) {
            if (tagInfo == null) {
                SessionManager.getSession().setEMVTerminal(false);
                return;
            }
            SessionManager.getSession().setTerminalTypeData(tagInfo.getTagValue());
            SessionManager.getSession().setEMVTerminal(TokenDataParser.isEMV(tagInfo.getTagValue()));
            SessionManager.getSession().setIsTerminalOffline(TokenDataParser.isTerminalTypeOffline(tagInfo.getTagValue()));
        } catch (Throwable th) {
            if (tagInfo == null) {
                SessionManager.getSession().setEMVTerminal(false);
            } else {
                SessionManager.getSession().setTerminalTypeData(tagInfo.getTagValue());
                SessionManager.getSession().setEMVTerminal(TokenDataParser.isEMV(tagInfo.getTagValue()));
                SessionManager.getSession().setIsTerminalOffline(TokenDataParser.isTerminalTypeOffline(tagInfo.getTagValue()));
            }
        }
    }

    public APDUResponse buildResponse() {
        return null;
    }
}
