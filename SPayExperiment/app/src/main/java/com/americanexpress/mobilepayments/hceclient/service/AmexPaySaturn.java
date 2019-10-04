/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.americanexpress.mobilepayments.hceclient.service;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;
import com.americanexpress.mobilepayments.hceclient.service.AmexPay;

public interface AmexPaySaturn
extends AmexPay {
    public TokenOperationStatus tokenMST();
}

