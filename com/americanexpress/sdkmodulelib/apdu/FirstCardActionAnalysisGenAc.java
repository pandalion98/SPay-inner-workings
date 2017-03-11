package com.americanexpress.sdkmodulelib.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.IssuerApplicationParserEmv;
import com.americanexpress.sdkmodulelib.model.token.IssuerApplicationParserMag;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.ProcessingOptionsParserEmv;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.americanexpress.sdkmodulelib.util.AxpeLogUtils;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedApp;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import java.util.HashMap;

public class FirstCardActionAnalysisGenAc implements CommandProcessor {
    public APDUResponse getApduResponse(CommandInfo commandInfo, ParsedTokenRecord parsedTokenRecord, Session session) {
        int i = 1;
        if (!session.isWorkflowStepValid(APDURequestCommand.GENAC)) {
            return new APDUResponse(Util.fromHexString(APDUConstants.APDU_COMMAND_STATUS_WORD_TERMINATE), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
        String applicationInterchangeProfile;
        String issuerApplicationData;
        String str;
        String str2;
        HashMap buildEMVRequestParams;
        String str3;
        boolean z;
        if (session.isEMVTransaction()) {
            applicationInterchangeProfile = ((ProcessingOptionsParserEmv) parsedTokenRecord.getDataGroups().get(ProcessingOptionsParserEmv.class)).getApplicationInterchangeProfile();
            issuerApplicationData = ((IssuerApplicationParserEmv) parsedTokenRecord.getDataGroups().get(IssuerApplicationParserEmv.class)).getIssuerApplicationData();
            str = applicationInterchangeProfile;
            applicationInterchangeProfile = ((IssuerApplicationParserEmv) parsedTokenRecord.getDataGroups().get(IssuerApplicationParserEmv.class)).getCardVerificationResult();
            str2 = issuerApplicationData;
        } else {
            issuerApplicationData = ((IssuerApplicationParserMag) parsedTokenRecord.getDataGroups().get(IssuerApplicationParserMag.class)).getIssuerApplicationData();
            str = null;
            applicationInterchangeProfile = ((IssuerApplicationParserMag) parsedTokenRecord.getDataGroups().get(IssuerApplicationParserMag.class)).getCardVerificationResult();
            str2 = issuerApplicationData;
        }
        AxpeLogUtils.log("isEMV=" + session.isEMVTransaction() + " , aip[" + str + "], issuerApplicationData[" + str2 + "] , currentCVR[" + applicationInterchangeProfile + "]");
        if (!session.isProcessTransactionComplete()) {
            AxpeLogUtils.log("calling ProcessTransaction....");
            TokenDataRecord processTransaction = TrustedAppFactory.getTrustedApp().processTransaction(1, session.getTokenDataRecord());
            AxpeLogUtils.log("completed calling ProcessTransaction....");
            session.setTokenDataRecord(processTransaction);
            session.setProcessTransactionComplete(true);
        }
        String nfcAtc = session.getTokenDataRecord().getNfcAtc();
        AxpeLogUtils.log("atc=" + nfcAtc);
        if (session.isEMVTransaction()) {
            buildEMVRequestParams = buildEMVRequestParams(str, new String(commandInfo.getApdu().getData()));
        } else {
            buildEMVRequestParams = buildMAGRequestParams(new String(commandInfo.getApdu().getData()));
        }
        AxpeLogUtils.log("requestParam=" + buildEMVRequestParams);
        String str4 = APDUConstants.GEN_AC_PREPEND_TAG;
        if (TokenDataParser.isTerminalGenACRequestConnectivityOffline(commandInfo.getApduCommand()) || session.isTerminalOffline()) {
            str3 = APDUConstants.GEN_AC_PREPEND_OFFLINE_TAG;
            String constructCVRBasedOnTerminalConnectivity = TokenDataParser.constructCVRBasedOnTerminalConnectivity(applicationInterchangeProfile, false);
            str2 = TokenDataParser.updateCVR(str2, applicationInterchangeProfile, constructCVRBasedOnTerminalConnectivity);
            applicationInterchangeProfile = str3;
            str3 = constructCVRBasedOnTerminalConnectivity;
            issuerApplicationData = str2;
            z = false;
            str4 = issuerApplicationData;
        } else {
            str3 = TokenDataParser.constructCVRBasedOnTerminalConnectivity(applicationInterchangeProfile, true);
            str2 = TokenDataParser.updateCVR(str2, applicationInterchangeProfile, str3);
            applicationInterchangeProfile = str4;
            str4 = str2;
            z = true;
        }
        AxpeLogUtils.log("onlineStatus[" + z + "] , issuerApplicationData[" + str4 + "] currentCVR[" + str3 + "] IsAuthenticationPerformed[" + session.getAuthenticationPerformed() + "]");
        AxpeLogUtils.log("session.isEMVTransaction()=" + session.isEMVTransaction());
        AxpeLogUtils.log("session.getAuthenticationPerformed()=" + session.getAuthenticationPerformed());
        if (session.getAuthenticationPerformed() == 1) {
            AxpeLogUtils.log("update cvr");
            constructCVRBasedOnTerminalConnectivity = TokenDataParser.constructCVRBasedOnAuthenticationType(str3, session.isBioAuthentication().booleanValue());
            AxpeLogUtils.log("updatedCVR=" + constructCVRBasedOnTerminalConnectivity);
            str4 = TokenDataParser.updateCVR(str4, str3, constructCVRBasedOnTerminalConnectivity);
            AxpeLogUtils.log("issuerApplicationData=" + str4);
        }
        AxpeLogUtils.log("onlineStatus[" + z + "] , issuerApplicationData[" + str4 + "] currentCVR[" + str3 + "]");
        AxpeLogUtils.log("Calling TZ crypto generation..");
        TrustedApp trustedApp = TrustedAppFactory.getTrustedApp();
        int i2 = session.isEMVTransaction() ? 2 : 1;
        if (!z) {
            i = 2;
        }
        String nFCCryptogram = trustedApp.getNFCCryptogram(i2, i, buildEMVRequestParams);
        AxpeLogUtils.log("Completed calling TZ crypto generation..");
        session.getTokenDataRecord().setNfcCryptogram(nFCCryptogram);
        session.getTokenDataRecord().setNfcUnpredictableNumber((String) buildEMVRequestParams.get("unpredictableNumber"));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(applicationInterchangeProfile).append(nfcAtc).append(nFCCryptogram).append(str4).append(commandInfo.getStatusWord());
        APDUResponse aPDUResponse = new APDUResponse();
        aPDUResponse.setApduBytes(Util.fromHexString(stringBuilder.toString()));
        aPDUResponse.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        aPDUResponse.setCommandName(APDURequestCommand.GENAC.name());
        return aPDUResponse;
    }

    private HashMap<String, String> buildEMVRequestParams(String str, String str2) {
        HashMap<String, String> hashMap = new HashMap();
        Object fromHexString = Util.fromHexString(str2);
        if (fromHexString.length == 29) {
            hashMap.put("aip", str);
            Object obj = new byte[6];
            System.arraycopy(fromHexString, 0, obj, 0, 6);
            hashMap.put("authorizedAmount", Util.byteArrayToHexString(obj));
            obj = new byte[6];
            System.arraycopy(fromHexString, 6, obj, 0, 6);
            hashMap.put("otherAmount", Util.byteArrayToHexString(obj));
            obj = new byte[2];
            System.arraycopy(fromHexString, 12, obj, 0, 2);
            hashMap.put("terminalCountryCode", Util.byteArrayToHexString(obj));
            obj = new byte[5];
            System.arraycopy(fromHexString, 14, obj, 0, 5);
            hashMap.put("terminalVerfResult", Util.byteArrayToHexString(obj));
            obj = new byte[2];
            System.arraycopy(fromHexString, 19, obj, 0, 2);
            hashMap.put("transactionCurrencyCode", Util.byteArrayToHexString(obj));
            obj = new byte[3];
            System.arraycopy(fromHexString, 21, obj, 0, 3);
            hashMap.put("transactionDate", Util.byteArrayToHexString(obj));
            obj = new byte[1];
            System.arraycopy(fromHexString, 24, obj, 0, 1);
            hashMap.put(PaymentFramework.EXTRA_TRANSACTION_TYPE, Util.byteArrayToHexString(obj));
            obj = new byte[4];
            System.arraycopy(fromHexString, 25, obj, 0, 4);
            hashMap.put("unpredictableNumber", Util.byteArrayToHexString(obj));
        }
        return hashMap;
    }

    private HashMap<String, String> buildMAGRequestParams(String str) {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("unpredictableNumber", str);
        return hashMap;
    }
}
