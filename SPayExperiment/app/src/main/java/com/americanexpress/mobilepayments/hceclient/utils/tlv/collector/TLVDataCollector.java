/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.utils.tlv.collector;

import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagDetail;

public interface TLVDataCollector {
    public TagDetail collectData(byte[] var1, String var2, String var3);
}

