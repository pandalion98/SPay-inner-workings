package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParser;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserEmv;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserMag;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class InitialApplicationProcessing implements CommandProcessor {
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        boolean z = true;
        if (!session.isWorkflowStepValid(APDURequestCommand.GPO)) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        ProcessingOptionsParser processingOptionsParser;
        if (session.isEMVTransaction()) {
            processingOptionsParser = (ProcessingOptionsParserEmv) parsedTokenRecord.getDataGroups().get(ProcessingOptionsParserEmv.class);
        } else {
            ProcessingOptionsParserMag processingOptionsParserMag = (ProcessingOptionsParserMag) parsedTokenRecord.getDataGroups().get(ProcessingOptionsParserMag.class);
        }
        String applicationFileLocator = processingOptionsParser.getApplicationFileLocator();
        String applicationInterchangeProfile = processingOptionsParser.getApplicationInterchangeProfile();
        String buildTLVForValue = TLVHelper.buildTLVForValue(APDUConstants.GPO_RESPONSE_TEMPLATE, applicationInterchangeProfile + applicationFileLocator);
        if (TokenDataParser.getExpressPayVersion(session.getTerminalTypeData()) == 3) {
            boolean z2;
            if (session.getAuthenticationPerformed() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            boolean isCVMRequired = TokenDataParser.isCVMRequired(session.getTerminalTypeData());
            if (session.getAuthenticationPerformed() != 1) {
                z = false;
            }
            buildTLVForValue = TokenDataParser.calculateCVMResult(z2, isCVMRequired, z);
            String buildTLVForValue2 = TLVHelper.buildTLVForValue(Constants.APPLICATION_FILE_LOCATOR_AIP_TAG, applicationInterchangeProfile);
            String buildTLVForValue3 = TLVHelper.buildTLVForValue(Constants.APPLICATION_FILE_LOCATOR_AFL_TAG, applicationFileLocator);
            buildTLVForValue = TLVHelper.buildTLVForValue(APDUConstants.GPO_RESPONSE_TEMPLATE_WITH_CVM, buildTLVForValue2 + buildTLVForValue3 + TLVHelper.buildTLVForValue(APDUConstants.GPO_CVM_RESULT_TAG, buildTLVForValue));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildTLVForValue).append(commandInfo.getStatusWord());
        APDUResponse aPDUResponse = new APDUResponse();
        aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
        aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        aPDUResponse.setCommandName(APDURequestCommand.GPO.name());
        return aPDUResponse;
    }
}
