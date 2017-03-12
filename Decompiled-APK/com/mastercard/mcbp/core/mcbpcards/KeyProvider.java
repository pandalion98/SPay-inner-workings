package com.mastercard.mcbp.core.mcbpcards;

import com.mastercard.mobile_api.bytes.ByteArray;

public interface KeyProvider {
    ByteArray provideKey();
}
