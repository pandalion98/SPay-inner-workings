package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.util.APDUConstants;

public class SelectPPSEApduInfo extends APDU {
    public SelectPPSEApduInfo() {
        setCommand(APDURequestCommand.SELECT_PPSE);
    }

    public String validate() {
        setValid(true);
        return APDUConstants.APDU_COMMAND_STATUS_WORD_SUCCESS;
    }

    public APDUResponse buildResponse() {
        return null;
    }
}
