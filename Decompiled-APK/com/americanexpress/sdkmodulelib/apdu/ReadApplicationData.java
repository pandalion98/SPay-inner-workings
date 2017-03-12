package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.CardRiskManagmentParser;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.Track2Info;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class ReadApplicationData implements CommandProcessor {
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        if (!session.isWorkflowStepValid(APDURequestCommand.READ)) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        DataGroup dataGroup;
        if (session.isEMVTransaction()) {
            dataGroup = (DataGroup) parsedTokenRecord.getDataGroups().get(Constants.READ_RESPONSE_MAPPER_EMV.get(commandInfo.getApdu().getParameter1()));
        } else {
            dataGroup = (DataGroup) parsedTokenRecord.getDataGroups().get(Constants.READ_RESPONSE_MAPPER_MAG.get(commandInfo.getApdu().getParameter1()));
            if (session.isBioAuthentication() != null) {
                if (dataGroup instanceof Track2Info) {
                    String parsedData = dataGroup.getParsedData();
                    CharSequence track2EquivalentData = ((Track2Info) dataGroup).getTrack2EquivalentData();
                    dataGroup.setParsedData(parsedData.replace(track2EquivalentData, updateServiceCode(track2EquivalentData, session.isBioAuthentication().booleanValue() ? Constants.SERVICE_CODE_MAG_BIO : Constants.SERVICE_CODE_MAG_NON_BIO)));
                } else if (dataGroup instanceof CardRiskManagmentParser) {
                    dataGroup.setParsedData(dataGroup.getParsedData().replace("5F3002" + ((CardRiskManagmentParser) dataGroup).getServiceCode(), "5F30020" + (session.isBioAuthentication().booleanValue() ? Constants.SERVICE_CODE_MAG_BIO : Constants.SERVICE_CODE_MAG_NON_BIO)));
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dataGroup.getParsedData()).append(commandInfo.getStatusWord());
        APDUResponse aPDUResponse = new APDUResponse();
        aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
        aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        aPDUResponse.setCommandName(APDURequestCommand.READ.name());
        return aPDUResponse;
    }

    private String updateServiceCode(String str, String str2) {
        str.substring(0, str.indexOf("D"));
        String substring = str.substring(str.indexOf("D") + 1, str.length());
        String substring2 = substring.substring(0, 4);
        return str.replace(substring2 + substring.substring(4, 7), substring2 + str2);
    }
}
