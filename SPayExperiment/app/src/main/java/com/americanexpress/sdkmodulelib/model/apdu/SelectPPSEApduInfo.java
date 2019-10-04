/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;

public class SelectPPSEApduInfo
extends APDU {
    public SelectPPSEApduInfo() {
        this.setCommand(APDURequestCommand.SELECT_PPSE);
    }

    @Override
    public APDUResponse buildResponse() {
        return null;
    }

    @Override
    public String validate() {
        this.setValid(true);
        return "9000";
    }
}

