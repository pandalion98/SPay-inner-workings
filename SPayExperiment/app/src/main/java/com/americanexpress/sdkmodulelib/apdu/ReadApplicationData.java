/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.CharSequence
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
import com.americanexpress.sdkmodulelib.model.token.CardRiskManagmentParser;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.Track2Info;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.Constants;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import java.util.HashMap;

public class ReadApplicationData
implements CommandProcessor {
    private String updateServiceCode(String string, String string2) {
        string.substring(0, string.indexOf("D"));
        String string3 = string.substring(1 + string.indexOf("D"), string.length());
        String string4 = string3.substring(0, 4);
        String string5 = string3.substring(4, 7);
        return string.replace((CharSequence)(string4 + string5), (CharSequence)(string4 + string2));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        DataGroup dataGroup;
        if (!session.isWorkflowStepValid(APDURequestCommand.READ)) {
            return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        if (session.isEMVTransaction()) {
            dataGroup = (DataGroup)parsedTokenRecord.getDataGroups().get(Constants.READ_RESPONSE_MAPPER_EMV.get((Object)commandInfo.getApdu().getParameter1()));
        } else {
            dataGroup = (DataGroup)parsedTokenRecord.getDataGroups().get(Constants.READ_RESPONSE_MAPPER_MAG.get((Object)commandInfo.getApdu().getParameter1()));
            if (session.isBioAuthentication() != null) {
                if (dataGroup instanceof Track2Info) {
                    String string = dataGroup.getParsedData();
                    String string2 = ((Track2Info)dataGroup).getTrack2EquivalentData();
                    String string3 = session.isBioAuthentication() != false ? "728" : "727";
                    dataGroup.setParsedData(string.replace((CharSequence)string2, (CharSequence)this.updateServiceCode(string2, string3)));
                } else if (dataGroup instanceof CardRiskManagmentParser) {
                    String string = dataGroup.getParsedData();
                    String string4 = "5F3002" + ((CardRiskManagmentParser)dataGroup).getServiceCode();
                    StringBuilder stringBuilder = new StringBuilder().append("5F30020");
                    String string5 = session.isBioAuthentication() != false ? "728" : "727";
                    dataGroup.setParsedData(string.replace((CharSequence)string4, (CharSequence)stringBuilder.append(string5).toString()));
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
}

