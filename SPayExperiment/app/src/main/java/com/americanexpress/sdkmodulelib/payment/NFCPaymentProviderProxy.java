/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.EndTransactionResponse;
import com.americanexpress.sdkmodulelib.model.ProcessInAppPaymentResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public interface NFCPaymentProviderProxy {
    public EndTransactionResponse endTransaction();

    public APDUResponse generateAPDU(byte[] var1);

    public ProcessInAppPaymentResponse processInAppPayment(Object var1, String var2);

    public TokenDataStatus processOther();

    public TokenDataStatus startTransaction(String var1, int var2, int var3, String var4);
}

