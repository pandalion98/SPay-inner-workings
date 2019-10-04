/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetChallengeApdu
extends Apdu {
    public static final byte CLA = 0;
    public static final byte INS = -124;

    public GetChallengeApdu() {
        super((byte)0, (byte)-124, (byte)0, (byte)0);
    }
}

