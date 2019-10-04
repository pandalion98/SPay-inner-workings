/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.HashMap
 */
package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.apdu.APDUCommandProcessorFactory;
import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.EndTransactionResponse;
import com.americanexpress.sdkmodulelib.model.ProcessInAppPaymentResponse;
import com.americanexpress.sdkmodulelib.model.Session;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;
import com.americanexpress.sdkmodulelib.model.apdu.CommandInfo;
import com.americanexpress.sdkmodulelib.model.token.DataGroup;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.model.token.TokenMetaInfoParser;
import com.americanexpress.sdkmodulelib.model.token.Track2InfoEmv;
import com.americanexpress.sdkmodulelib.model.token.Track2InfoMag;
import com.americanexpress.sdkmodulelib.payment.NFCPaymentProviderProxy;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.tlv.Util;
import com.americanexpress.sdkmodulelib.util.AxpeLogUtils;
import com.americanexpress.sdkmodulelib.util.ErrorConstants;
import com.americanexpress.sdkmodulelib.util.TAErrorUtils;
import com.americanexpress.sdkmodulelib.util.TIDUtil;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import com.americanexpress.sdkmodulelib.util.TrustedAppFactory;
import java.util.HashMap;

public class NFCPaymentProviderProxyImpl
implements NFCPaymentProviderProxy {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void calculateTID(EndTransactionResponse endTransactionResponse) {
        String string;
        Session session = SessionManager.getSession();
        if (session.isInAppTransaction()) {
            String string2 = session.getTokenDataRecord().getInAppTID();
            endTransactionResponse.setInAppTransactionId(string2);
            if (string2 != null && !"".equals((Object)string2)) return;
            {
                throw new Exception("Failed to calculate TID");
            }
        }
        String string3 = session.isEMVTransaction() ? ((Track2InfoEmv)session.getParsedTokenRecord().getDataGroups().get(Track2InfoEmv.class)).getAccountRefNumber() : (string = ((Track2InfoMag)session.getParsedTokenRecord().getDataGroups().get(Track2InfoMag.class)).getAccountRefNumber());
        String string4 = ((TokenMetaInfoParser)session.getParsedTokenRecord().getDataGroups().get(TokenMetaInfoParser.class)).getDeviceId();
        String string5 = session.getTokenDataRecord().getNfcUnpredictableNumber();
        String string6 = session.getTokenDataRecord().getNfcCryptogram();
        String string7 = session.getTokenDataRecord().getOtherTID();
        endTransactionResponse.setOtherTransactionId(string7);
        String string8 = null;
        if (string5 != null) {
            string8 = null;
            if (string6 != null) {
                string8 = TIDUtil.generateHash(session.isEMVTransaction(), string3, string4, string5, string6);
                endTransactionResponse.setNfcTransactionId(string8);
            }
        }
        if (string8 != null && !"".equals(string8) || string7 != null && !"".equals((Object)string7)) return;
        {
            throw new Exception("Failed to calculate TID");
        }
    }

    private TokenDataStatus validateTokenData(ParsedTokenRecord parsedTokenRecord) {
        if (parsedTokenRecord.isTokenMalformed()) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TOKEN_DATA_MALFORMED_FAILURE);
        }
        if (parsedTokenRecord.isTokenPartiallyMalformed()) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_PARTIAL_TOKEN_DATA_MALFORMED_FAILURE);
        }
        if (TokenDataParser.isClientVersionUpdateRequired(parsedTokenRecord)) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_CLIENT_VERSION_FAILURE);
        }
        if (TokenDataParser.isUpdateTokenData(parsedTokenRecord)) {
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TOKEN_VERSION_FAILURE);
        }
        return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_SUCCESS);
    }

    /*
     * Exception decompiling
     */
    @Override
    public EndTransactionResponse endTransaction() {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public APDUResponse generateAPDU(byte[] var1_1) {
        block10 : {
            SessionManager.getSession().setIsProcessOther(false);
            var2_2 = null;
            if (var1_1 == null) return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            if (var1_1.length == 0) {
                return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            }
            var4_4 = Util.byteArrayToHexString(var1_1);
            AxpeLogUtils.log("* Apdu Request=" + var4_4);
            var5_5 = var4_4.getBytes();
            if (var5_5 == null) return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            if (var5_5.length > 1024) {
                return new APDUResponse(Util.fromHexString("6985"), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            }
            var6_6 = new CommandInfo(var5_5);
            if (var6_6.isValid()) break block10;
            var3_3 = new APDUResponse(Util.fromHexString(var6_6.getStatusWord().toUpperCase()), TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            if (var3_3 != null) return var3_3;
            var12_7 = new APDUResponse();
            var12_7.setApduBytes(Util.fromHexString("6985"));
            var12_7.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            return var3_3;
        }
        try {
            var13_8 = SessionManager.getSession().getParsedTokenRecord();
            var10_10 = var14_9 = APDUCommandProcessorFactory.getCommandProcessor(var6_6).getApduResponse(var6_6, var13_8, SessionManager.getSession());
            ** if (var10_10 != null) goto lbl29
        }
        catch (Exception var9_11) {
            block11 : {
                try {
                    AxpeLogUtils.log(var9_11.getMessage());
                    AxpeLogUtils.log(var9_11.toString());
                    var10_10 = false == false ? new APDUResponse() : null;
                }
                catch (Throwable var7_12) {}
                ** GOTO lbl-1000
                try {
                    var10_10.setApduBytes(Util.fromHexString("6985"));
                    var10_10.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
                    if (var10_10 != null) break block11;
                }
                catch (Throwable var11_15) {
                    var2_2 = var10_10;
                    var7_13 = var11_15;
                }
                var10_10 = new APDUResponse();
                var10_10.setApduBytes(Util.fromHexString("6985"));
                var10_10.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
            }
            AxpeLogUtils.log("* Apdu Response=" + Util.byteArrayToHexString(var10_10.getApduBytes()));
            return var10_10;
lbl-1000: // 2 sources:
            {
                if (var2_2 != null) throw var7_13;
                var8_14 = new APDUResponse();
                var8_14.setApduBytes(Util.fromHexString("6985"));
                var8_14.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
                throw var7_13;
            }
        }
lbl-1000: // 1 sources:
        {
            var10_10 = new APDUResponse();
            var10_10.setApduBytes(Util.fromHexString("6985"));
            var10_10.setStatus(TokenDataParser.buildTokenDataStatus(ErrorConstants.GENERATE_APDU_RESPONSE));
        }
lbl29: // 2 sources:
        ** GOTO lbl44
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public ProcessInAppPaymentResponse processInAppPayment(Object object, String string) {
        TokenDataStatus tokenDataStatus;
        ProcessInAppPaymentResponse processInAppPaymentResponse = new ProcessInAppPaymentResponse();
        try {
            TokenDataStatus tokenDataStatus2;
            Session session = SessionManager.getSession();
            SessionManager.getSession().setIsInAppTransaction(true);
            if (!session.isProcessTransactionComplete()) {
                session.setTokenDataRecord(TrustedAppFactory.getTrustedApp().processTransaction(3, session.getTokenDataRecord()));
                session.setProcessTransactionComplete(true);
            }
            TokenDataRecord tokenDataRecord = TrustedAppFactory.getTrustedApp().generateInAppPaymentPayload(object, string, session.getTokenDataRecord());
            session.setTokenDataRecord(tokenDataRecord);
            processInAppPaymentResponse.setPaymentPayload(tokenDataRecord.getPaymentPayload());
            session.setIsInAppTransactionSuccess(true);
            tokenDataStatus = tokenDataStatus2 = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_RESPONSE);
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                tokenDataStatus = TAErrorUtils.isTrustedAppCommunicationError(exception) ? TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception)) : TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            tokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_IN_APP_FAILURE);
        }
        processInAppPaymentResponse.setTokenDataStatus(tokenDataStatus);
        return processInAppPaymentResponse;
    }

    @Override
    public TokenDataStatus processOther() {
        try {
            Session session = SessionManager.getSession();
            if (!session.isProcessTransactionComplete()) {
                session.setTokenDataRecord(TrustedAppFactory.getTrustedApp().processTransaction(2, session.getTokenDataRecord()));
                session.setProcessTransactionComplete(true);
            }
            TokenDataStatus tokenDataStatus = TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_RESPONSE);
            SessionManager.getSession().setIsProcessOther(true);
            SessionManager.getSession().setIsMSTInvokedAtleastOnce(true);
            return tokenDataStatus;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    return TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception));
                }
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.PROCESS_OTHER_FAILURE);
        }
    }

    @Override
    public TokenDataStatus startTransaction(String string, int n2, int n3, String string2) {
        AxpeLogUtils.log("authenticationPerformed=" + n2);
        AxpeLogUtils.log("authenticationType=" + n3);
        try {
            SessionManager.cleanSession();
            TokenDataRecord tokenDataRecord = TokenDataParser.loadFromStorage(string);
            String string3 = TrustedAppFactory.getTrustedApp().decryptTokenData(tokenDataRecord.getApduBlob());
            String string4 = TrustedAppFactory.getTrustedApp().decryptTokenData(tokenDataRecord.getMetaDataBlob());
            ParsedTokenRecord parsedTokenRecord = TokenDataParser.parseToken((string3 + string4).toUpperCase());
            TokenDataStatus tokenDataStatus = this.validateTokenData(parsedTokenRecord);
            if (ErrorConstants.START_TRANS_SUCCESS[0].equals((Object)tokenDataStatus.getReasonCode())) {
                SessionManager.createSession().setParsedTokenRecord(parsedTokenRecord);
                SessionManager.getSession().setAuthenticationPerformed(n2);
                SessionManager.getSession().setAuthenticationType(n3);
            }
            return tokenDataStatus;
        }
        catch (Exception exception) {
            if (TAErrorUtils.isTAError(exception)) {
                if (TAErrorUtils.isTrustedAppCommunicationError(exception)) {
                    return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TA_COMM_FAILURE, TAErrorUtils.getErrorCode(exception));
                }
                return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_TA_FAILURE, TAErrorUtils.getErrorCode(exception));
            }
            return TokenDataParser.buildTokenDataStatus(ErrorConstants.START_TRANS_GENERIC_FAILURE);
        }
    }
}

