/*
 * Decompiled with CFR 0.0.
 */
package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class EMVGetResponse
extends Apdu {
    public static final byte CLA = -128;
    public static final byte INS = -64;

    public EMVGetResponse() {
        super((byte)-128, (byte)-64, (byte)0, (byte)0);
    }
}

