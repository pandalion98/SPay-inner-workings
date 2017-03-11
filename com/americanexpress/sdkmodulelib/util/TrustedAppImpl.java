package com.americanexpress.sdkmodulelib.util;

import android.content.Context;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.GenerateInAppPaymentPayloadResponse;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.ProcessTransactionResponse;
import java.util.HashMap;

public class TrustedAppImpl implements TrustedApp {
    public String decryptTokenData(String str) {
        return AmexTAController.ct().decryptTokenData(str);
    }

    public String getNFCCryptogram(int i, int i2, HashMap<String, String> hashMap) {
        return AmexTAController.ct().m784a(i, i2, hashMap);
    }

    public TokenDataRecord processTransaction(int i, TokenDataRecord tokenDataRecord) {
        ProcessTransactionResponse a = AmexTAController.ct().m783a(i, tokenDataRecord.getApduBlob(), tokenDataRecord.getNfcLUPCBlob(), tokenDataRecord.getOtherLUPCBlob(), tokenDataRecord.getMetaDataBlob(), tokenDataRecord.getLupcMetadataBlob());
        tokenDataRecord.setNfcLUPCBlob(a.eNFCLUPCBlob);
        tokenDataRecord.setOtherLUPCBlob(a.eOtherLUPCBlob);
        tokenDataRecord.setLupcMetadataBlob(a.lupcMetaDataBlob);
        tokenDataRecord.setNfcAtc(a.atcLUPC);
        tokenDataRecord.setOtherTID(a.otherTID);
        return tokenDataRecord;
    }

    public TokenDataRecord generateInAppPaymentPayload(Object obj, String str, TokenDataRecord tokenDataRecord) {
        GenerateInAppPaymentPayloadResponse a = AmexTAController.ct().m781a(str, obj);
        tokenDataRecord.setPaymentPayload(a.payload);
        tokenDataRecord.setInAppTID(a.tid);
        return tokenDataRecord;
    }

    public String suspendToken(String str) {
        return AmexTAController.ct().suspendToken(str);
    }

    public String resumeToken(String str) {
        return AmexTAController.ct().resumeToken(str);
    }

    public String activateToken(String str) {
        return AmexTAController.ct().activateToken(str);
    }

    public void clearLUPC() {
        AmexTAController.ct().cw();
    }

    public Context getApplicationContext() {
        return AmexTAController.ct().getContext();
    }
}
