/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.MSTLUPCDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.NFCLUPCDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.PPSEDataCollector;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.collector.TLVDataCollector;

public class TLVDataCollectorFactory {
    public static TLVDataCollector getDataCollector(String string, String string2) {
        if (CPDLConfig.getDGI_TAG("NFC_LUPC_DGI").equalsIgnoreCase(string) && CPDLConfig.getDGI_TAG("NFC_LUPC_TAG").equalsIgnoreCase(string2)) {
            return new NFCLUPCDataCollector();
        }
        if (CPDLConfig.getDGI_TAG("MST_LUPC_DGI").equalsIgnoreCase(string) && CPDLConfig.getDGI_TAG("MST_LUPC_TAG").equalsIgnoreCase(string2)) {
            return new MSTLUPCDataCollector();
        }
        if (CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI").equalsIgnoreCase(string) && (CPDLConfig.getDGI_TAG("PPSE_TAG_V1").equalsIgnoreCase(string2) || CPDLConfig.getDGI_TAG("PPSE_TAG_V2").equalsIgnoreCase(string2))) {
            return new PPSEDataCollector();
        }
        return null;
    }
}

