package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.ApplicationSelectionParser;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class ApplicationSelection implements CommandProcessor {
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        if (APDURequestCommand.SELECT_PPSE.name().equalsIgnoreCase(commandInfo.getApdu().getCommand().name()) && !session.isWorkflowStepValid(APDURequestCommand.SELECT_PPSE)) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        if (APDURequestCommand.SELECT_AID.name().equalsIgnoreCase(commandInfo.getApdu().getCommand().name()) && !session.isWorkflowStepValid(APDURequestCommand.SELECT_AID)) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        if (APDURequestCommand.SELECT_PPSE.name().equalsIgnoreCase(commandInfo.getApdu().getCommand().name())) {
            APDUResponse aPDUResponse = new APDUResponse();
            aPDUResponse.setApduBytes(Util.fromHexString(APDUConstants.SELECT_PPSE_RESPONSE.toString()));
            aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            aPDUResponse.setCommandName(APDURequestCommand.SELECT_PPSE.name());
            return aPDUResponse;
        }
        String applicationIndentifier = ((TokenMetaInfoParser) parsedTokenRecord.getDataGroups().get(TokenMetaInfoParser.class)).getApplicationIndentifier();
        String buildTLVForValue = TLVHelper.buildTLVForValue(APDUConstants.SELECT_RESPONSE_FCI_TEMPLATE, TLVHelper.buildTLVForValue(APDUConstants.SELECT_RESPONSE_DF_NAME, applicationIndentifier) + ((ApplicationSelectionParser) parsedTokenRecord.getDataGroups().get(ApplicationSelectionParser.class)).getParsedData());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildTLVForValue).append(commandInfo.getStatusWord());
        aPDUResponse = new APDUResponse();
        aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
        aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        aPDUResponse.setCommandName(APDURequestCommand.SELECT_AID.name());
        return aPDUResponse;
    }
}
