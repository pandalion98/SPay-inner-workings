package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;

public class TLVDataCollectorFactory {
    public static TLVDataCollector getDataCollector(String str, String str2) {
        if (CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_DGI).equalsIgnoreCase(str) && CPDLConfig.getDGI_TAG(CPDLConfig.NFC_LUPC_TAG).equalsIgnoreCase(str2)) {
            return new NFCLUPCDataCollector();
        }
        if (CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_DGI).equalsIgnoreCase(str) && CPDLConfig.getDGI_TAG(CPDLConfig.MST_LUPC_TAG).equalsIgnoreCase(str2)) {
            return new MSTLUPCDataCollector();
        }
        if (CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI).equalsIgnoreCase(str) && (CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V1).equalsIgnoreCase(str2) || CPDLConfig.getDGI_TAG(CPDLConfig.PPSE_TAG_V2).equalsIgnoreCase(str2))) {
            return new PPSEDataCollector();
        }
        return null;
    }
}
