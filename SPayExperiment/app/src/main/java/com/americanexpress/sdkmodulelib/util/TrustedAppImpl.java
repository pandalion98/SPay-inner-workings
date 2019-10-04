/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.americanexpress.sdkmodulelib.util;

import android.content.Context;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.util.TrustedApp;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import java.util.HashMap;
import java.util.Map;

public class TrustedAppImpl
implements TrustedApp {
    @Override
    public String activateToken(String string) {
        return AmexTAController.ct().activateToken(string);
    }

    @Override
    public void clearLUPC() {
        AmexTAController.ct().cw();
    }

    @Override
    public String decryptTokenData(String string) {
        return AmexTAController.ct().decryptTokenData(string);
    }

    @Override
    public TokenDataRecord generateInAppPaymentPayload(Object object, String string, TokenDataRecord tokenDataRecord) {
        AmexTAController.GenerateInAppPaymentPayloadResponse generateInAppPaymentPayloadResponse = AmexTAController.ct().a(string, object);
        tokenDataRecord.setPaymentPayload(generateInAppPaymentPayloadResponse.payload);
        tokenDataRecord.setInAppTID(generateInAppPaymentPayloadResponse.tid);
        return tokenDataRecord;
    }

    @Override
    public Context getApplicationContext() {
        return AmexTAController.ct().getContext();
    }

    @Override
    public String getNFCCryptogram(int n2, int n3, HashMap<String, String> hashMap) {
        return AmexTAController.ct().a(n2, n3, (Map<String, String>)hashMap);
    }

    @Override
    public TokenDataRecord processTransaction(int n2, TokenDataRecord tokenDataRecord) {
        AmexTAController.ProcessTransactionResponse processTransactionResponse = AmexTAController.ct().a(n2, tokenDataRecord.getApduBlob(), tokenDataRecord.getNfcLUPCBlob(), tokenDataRecord.getOtherLUPCBlob(), tokenDataRecord.getMetaDataBlob(), tokenDataRecord.getLupcMetadataBlob());
        tokenDataRecord.setNfcLUPCBlob(processTransactionResponse.eNFCLUPCBlob);
        tokenDataRecord.setOtherLUPCBlob(processTransactionResponse.eOtherLUPCBlob);
        tokenDataRecord.setLupcMetadataBlob(processTransactionResponse.lupcMetaDataBlob);
        tokenDataRecord.setNfcAtc(processTransactionResponse.atcLUPC);
        tokenDataRecord.setOtherTID(processTransactionResponse.otherTID);
        return tokenDataRecord;
    }

    @Override
    public String resumeToken(String string) {
        return AmexTAController.ct().resumeToken(string);
    }

    @Override
    public String suspendToken(String string) {
        return AmexTAController.ct().suspendToken(string);
    }
}

