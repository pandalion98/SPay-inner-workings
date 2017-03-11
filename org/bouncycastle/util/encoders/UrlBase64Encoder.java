package org.bouncycastle.util.encoders;

import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;

public class UrlBase64Encoder extends Base64Encoder {
    public UrlBase64Encoder() {
        this.encodingTable[this.encodingTable.length - 2] = SetResetParamApdu.INS;
        this.encodingTable[this.encodingTable.length - 1] = MCFCITemplate.TAG_FILE_CONTROL_INFORMATION;
        this.padding = (byte) 46;
        initialiseDecodingTable();
    }
}
