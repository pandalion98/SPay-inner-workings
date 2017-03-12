package com.mastercard.mobile_api.utils.apdu.emv;

import com.mastercard.mobile_api.utils.apdu.Apdu;

public class GetChallengeApdu extends Apdu {
    public static final byte CLA = (byte) 0;
    public static final byte INS = (byte) -124;

    public GetChallengeApdu() {
        super(CLA, INS, CLA, CLA);
    }
}
