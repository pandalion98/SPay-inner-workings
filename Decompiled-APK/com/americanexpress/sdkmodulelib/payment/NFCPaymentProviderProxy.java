package com.americanexpress.sdkmodulelib.payment;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.EndTransactionResponse;
import com.americanexpress.sdkmodulelib.model.ProcessInAppPaymentResponse;
import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public interface NFCPaymentProviderProxy {
    EndTransactionResponse endTransaction();

    APDUResponse generateAPDU(byte[] bArr);

    ProcessInAppPaymentResponse processInAppPayment(Object obj, String str);

    TokenDataStatus processOther();

    TokenDataStatus startTransaction(String str, int i, int i2, String str2);
}
