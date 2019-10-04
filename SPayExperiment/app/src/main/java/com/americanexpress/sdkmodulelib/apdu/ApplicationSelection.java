/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.HashMap
 */
package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.apdu.CommandProcessor;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.ApplicationSelectionParser;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import java.util.HashMap;

public class ApplicationSelection
implements CommandProcessor {
    @Override
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        if (APDURequestCommand.SELECT_PPSE.name().equalsIgnoreCase(commandInfo.getApdu().getCommand().name()) && !session.isWorkflowStepValid(APDURequestCommand.SELECT_PPSE)) {
            return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        if (APDURequestCommand.SELECT_AID.name().equalsIgnoreCase(commandInfo.getApdu().getCommand().name()) && !session.isWorkflowStepValid(APDURequestCommand.SELECT_AID)) {
            return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        if (APDURequestCommand.SELECT_PPSE.name().equalsIgnoreCase(commandInfo.getApdu().getCommand().name())) {
            APDUResponse aPDUResponse = new APDUResponse();
            aPDUResponse.setApduBytes(Util.fromHexString("6F2A840E325041592E5359532E4444463031A518BF0C1561134F08A0000000250110015004414D45588701019000".toString()));
            aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            aPDUResponse.setCommandName(APDURequestCommand.SELECT_PPSE.name());
            return aPDUResponse;
        }
        String string = ((TokenMetaInfoParser)parsedTokenRecord.getDataGroups().get(TokenMetaInfoParser.class)).getApplicationIndentifier();
        String string2 = ((ApplicationSelectionParser)parsedTokenRecord.getDataGroups().get(ApplicationSelectionParser.class)).getParsedData();
        String string3 = TLVHelper.buildTLVForValue("84", string);
        String string4 = TLVHelper.buildTLVForValue("6F", string3 + string2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string4).append(commandInfo.getStatusWord());
        APDUResponse aPDUResponse = new APDUResponse();
        aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
        aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        aPDUResponse.setCommandName(APDURequestCommand.SELECT_AID.name());
        return aPDUResponse;
    }
}

