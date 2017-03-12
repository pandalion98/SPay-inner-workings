package com.americanexpress.mobilepayments.hceclient.delegate;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import org.bouncycastle.crypto.macs.SkeinMac;

public abstract class OperationDelegate {
    public abstract void doOperation();

    protected void checkSCStatus(int i) {
        if (i < 0) {
            throw new HCEClientException(HCEClientConstants.SECURE_COMPONENT_STATUS_MSG + i);
        }
    }

    protected int computeDestBufferSize(int i) {
        return (i * 2) + SkeinMac.SKEIN_1024;
    }
}
