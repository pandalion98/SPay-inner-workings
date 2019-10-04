/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.HashMap
 */
package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.apdu.CommandProcessor;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.IssuerApplicationParserEmv;
import com.americanexpress.sdkmodulelib.model.token.IssuerApplicationParserMag;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserEmv;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.AxpeLogUtils;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedApp;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;
import java.util.HashMap;

public class FirstCardActionAnalysisGenAc
implements CommandProcessor {
    private HashMap<String, String> buildEMVRequestParams(String string, String string2) {
        HashMap hashMap = new HashMap();
        byte[] arrby = Util.fromHexString(string2);
        if (arrby.length == 29) {
            hashMap.put((Object)"aip", (Object)string);
            byte[] arrby2 = new byte[6];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)6);
            hashMap.put((Object)"authorizedAmount", (Object)Util.byteArrayToHexString(arrby2));
            byte[] arrby3 = new byte[6];
            System.arraycopy((Object)arrby, (int)6, (Object)arrby3, (int)0, (int)6);
            hashMap.put((Object)"otherAmount", (Object)Util.byteArrayToHexString(arrby3));
            byte[] arrby4 = new byte[2];
            System.arraycopy((Object)arrby, (int)12, (Object)arrby4, (int)0, (int)2);
            hashMap.put((Object)"terminalCountryCode", (Object)Util.byteArrayToHexString(arrby4));
            byte[] arrby5 = new byte[5];
            System.arraycopy((Object)arrby, (int)14, (Object)arrby5, (int)0, (int)5);
            hashMap.put((Object)"terminalVerfResult", (Object)Util.byteArrayToHexString(arrby5));
            byte[] arrby6 = new byte[2];
            System.arraycopy((Object)arrby, (int)19, (Object)arrby6, (int)0, (int)2);
            hashMap.put((Object)"transactionCurrencyCode", (Object)Util.byteArrayToHexString(arrby6));
            byte[] arrby7 = new byte[3];
            System.arraycopy((Object)arrby, (int)21, (Object)arrby7, (int)0, (int)3);
            hashMap.put((Object)"transactionDate", (Object)Util.byteArrayToHexString(arrby7));
            byte[] arrby8 = new byte[1];
            System.arraycopy((Object)arrby, (int)24, (Object)arrby8, (int)0, (int)1);
            hashMap.put((Object)"transactionType", (Object)Util.byteArrayToHexString(arrby8));
            byte[] arrby9 = new byte[4];
            System.arraycopy((Object)arrby, (int)25, (Object)arrby9, (int)0, (int)4);
            hashMap.put((Object)"unpredictableNumber", (Object)Util.byteArrayToHexString(arrby9));
        }
        return hashMap;
    }

    private HashMap<String, String> buildMAGRequestParams(String string) {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"unpredictableNumber", (Object)string);
        return hashMap;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        String string;
        String string2;
        String string3;
        String string4;
        int n2;
        String string5;
        String string6;
        int n3 = 1;
        if (!session.isWorkflowStepValid(APDURequestCommand.GENAC)) {
            return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        if (session.isEMVTransaction()) {
            String string7 = ((ProcessingOptionsParserEmv)parsedTokenRecord.getDataGroups().get(ProcessingOptionsParserEmv.class)).getApplicationInterchangeProfile();
            String string8 = ((IssuerApplicationParserEmv)parsedTokenRecord.getDataGroups().get(IssuerApplicationParserEmv.class)).getIssuerApplicationData();
            String string9 = ((IssuerApplicationParserEmv)parsedTokenRecord.getDataGroups().get(IssuerApplicationParserEmv.class)).getCardVerificationResult();
            string2 = string7;
            string6 = string9;
            string3 = string8;
        } else {
            String string10 = ((IssuerApplicationParserMag)parsedTokenRecord.getDataGroups().get(IssuerApplicationParserMag.class)).getIssuerApplicationData();
            string6 = ((IssuerApplicationParserMag)parsedTokenRecord.getDataGroups().get(IssuerApplicationParserMag.class)).getCardVerificationResult();
            string3 = string10;
            string2 = null;
        }
        AxpeLogUtils.log("isEMV=" + session.isEMVTransaction() + " , aip[" + string2 + "], issuerApplicationData[" + string3 + "] , currentCVR[" + string6 + "]");
        if (!session.isProcessTransactionComplete()) {
            AxpeLogUtils.log("calling ProcessTransaction....");
            TokenDataRecord tokenDataRecord = session.getTokenDataRecord();
            TokenDataRecord tokenDataRecord2 = TrustedAppFactory.getTrustedApp().processTransaction(n3, tokenDataRecord);
            AxpeLogUtils.log("completed calling ProcessTransaction....");
            session.setTokenDataRecord(tokenDataRecord2);
            session.setProcessTransactionComplete((boolean)n3);
        }
        String string11 = session.getTokenDataRecord().getNfcAtc();
        AxpeLogUtils.log("atc=" + string11);
        HashMap<String, String> hashMap = session.isEMVTransaction() ? this.buildEMVRequestParams(string2, new String(commandInfo.getApdu().getData())) : this.buildMAGRequestParams(new String(commandInfo.getApdu().getData()));
        AxpeLogUtils.log("requestParam=" + hashMap);
        if (TokenDataParser.isTerminalGenACRequestConnectivityOffline(commandInfo.getApduCommand()) || session.isTerminalOffline()) {
            String string12 = TokenDataParser.constructCVRBasedOnTerminalConnectivity(string6, false);
            String string13 = TokenDataParser.updateCVR(string3, string6, string12);
            string5 = "801200";
            string = string12;
            n2 = 0;
            string4 = string13;
        } else {
            string = TokenDataParser.constructCVRBasedOnTerminalConnectivity(string6, (boolean)n3);
            String string14 = TokenDataParser.updateCVR(string3, string6, string);
            string5 = "801280";
            string4 = string14;
            n2 = n3;
        }
        AxpeLogUtils.log("onlineStatus[" + (boolean)n2 + "] , issuerApplicationData[" + string4 + "] currentCVR[" + string + "] IsAuthenticationPerformed[" + session.getAuthenticationPerformed() + "]");
        AxpeLogUtils.log("session.isEMVTransaction()=" + session.isEMVTransaction());
        AxpeLogUtils.log("session.getAuthenticationPerformed()=" + session.getAuthenticationPerformed());
        if (session.getAuthenticationPerformed() == n3) {
            AxpeLogUtils.log("update cvr");
            String string15 = TokenDataParser.constructCVRBasedOnAuthenticationType(string, session.isBioAuthentication());
            AxpeLogUtils.log("updatedCVR=" + string15);
            string4 = TokenDataParser.updateCVR(string4, string, string15);
            AxpeLogUtils.log("issuerApplicationData=" + string4);
        }
        AxpeLogUtils.log("onlineStatus[" + (boolean)n2 + "] , issuerApplicationData[" + string4 + "] currentCVR[" + string + "]");
        AxpeLogUtils.log("Calling TZ crypto generation..");
        TrustedApp trustedApp = TrustedAppFactory.getTrustedApp();
        int n4 = session.isEMVTransaction() ? 2 : n3;
        if (n2 == 0) {
            n3 = 2;
        }
        String string16 = trustedApp.getNFCCryptogram(n4, n3, hashMap);
        AxpeLogUtils.log("Completed calling TZ crypto generation..");
        session.getTokenDataRecord().setNfcCryptogram(string16);
        session.getTokenDataRecord().setNfcUnpredictableNumber((String)hashMap.get((Object)"unpredictableNumber"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string5).append(string11).append(string16).append(string4).append(commandInfo.getStatusWord());
        APDUResponse aPDUResponse = new APDUResponse();
        aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
        aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        aPDUResponse.setCommandName(APDURequestCommand.GENAC.name());
        return aPDUResponse;
    }
}

