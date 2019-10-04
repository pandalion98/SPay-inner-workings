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
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParser;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserEmv;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserMag;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import java.util.HashMap;

public class InitialApplicationProcessing
implements CommandProcessor {
    /*
     * Enabled aggressive block sorting
     */
    @Override
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        int n2 = 1;
        if (!session.isWorkflowStepValid(APDURequestCommand.GPO)) {
            return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        ProcessingOptionsParser processingOptionsParser = session.isEMVTransaction() ? (ProcessingOptionsParserEmv)parsedTokenRecord.getDataGroups().get(ProcessingOptionsParserEmv.class) : (ProcessingOptionsParserMag)parsedTokenRecord.getDataGroups().get(ProcessingOptionsParserMag.class);
        String string = processingOptionsParser.getApplicationFileLocator();
        String string2 = processingOptionsParser.getApplicationInterchangeProfile();
        String string3 = TLVHelper.buildTLVForValue("80", string2 + string);
        if (TokenDataParser.getExpressPayVersion(session.getTerminalTypeData()) == 3) {
            int n3 = session.getAuthenticationPerformed() == n2 ? n2 : 0;
            boolean bl = TokenDataParser.isCVMRequired(session.getTerminalTypeData());
            if (session.getAuthenticationPerformed() != n2) {
                n2 = 0;
            }
            String string4 = TokenDataParser.calculateCVMResult((boolean)n3, bl, (boolean)n2);
            String string5 = TLVHelper.buildTLVForValue("82", string2);
            String string6 = TLVHelper.buildTLVForValue("94", string);
            String string7 = TLVHelper.buildTLVForValue("9F71", string4);
            string3 = TLVHelper.buildTLVForValue("77", string5 + string6 + string7);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string3).append(commandInfo.getStatusWord());
        APDUResponse aPDUResponse = new APDUResponse();
        aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
        aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        aPDUResponse.setCommandName(APDURequestCommand.GPO.name());
        return aPDUResponse;
    }
}

