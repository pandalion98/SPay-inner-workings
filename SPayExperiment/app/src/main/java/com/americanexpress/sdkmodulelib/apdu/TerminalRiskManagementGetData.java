/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.apdu.CommandProcessor;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;

public class TerminalRiskManagementGetData
implements CommandProcessor {
    @Override
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        if (!session.isWorkflowStepValid(APDURequestCommand.GETDATA)) {
            return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        try {
            if (!session.isProcessTransactionComplete()) {
                TokenDataRecord tokenDataRecord = session.getTokenDataRecord();
                session.setTokenDataRecord(TrustedAppFactory.getTrustedApp().processTransaction(1, tokenDataRecord));
                session.setProcessTransactionComplete(true);
            }
            String string = session.getTokenDataRecord().getNfcAtc();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("9F3602").append(string).append(commandInfo.getStatusWord());
            APDUResponse aPDUResponse = new APDUResponse();
            aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
            aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            aPDUResponse.setCommandName(APDURequestCommand.GETDATA.name());
            return aPDUResponse;
        }
        catch (Exception exception) {
            return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
    }
}

