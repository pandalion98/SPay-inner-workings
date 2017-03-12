package com.americanexpress.mobilepayments.hceclient.service;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public interface AmexPaySaturn extends AmexPay {
    TokenOperationStatus tokenMST();
}
