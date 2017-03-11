package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;

public class TerminalRiskManagementGetData implements CommandProcessor {
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        if (!session.isWorkflowStepValid(APDURequestCommand.GETDATA)) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        try {
            if (!session.isProcessTransactionComplete()) {
                session.setTokenDataRecord(TrustedAppFactory.getTrustedApp().processTransaction(1, session.getTokenDataRecord()));
                session.setProcessTransactionComplete(true);
            }
            String nfcAtc = session.getTokenDataRecord().getNfcAtc();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(APDUConstants.GET_DATA_PREPEND).append(nfcAtc).append(commandInfo.getStatusWord());
            APDUResponse aPDUResponse = new APDUResponse();
            aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
            aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            aPDUResponse.setCommandName(APDURequestCommand.GETDATA.name());
            return aPDUResponse;
        } catch (Exception e) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
    }
}
