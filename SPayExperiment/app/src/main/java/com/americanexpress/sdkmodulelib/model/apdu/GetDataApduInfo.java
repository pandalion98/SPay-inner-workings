/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;

public class GetDataApduInfo
extends APDU {
    public GetDataApduInfo() {
        this.setCommand(APDURequestCommand.GETDATA);
    }

    @Override
    public APDUResponse buildResponse() {
        return null;
    }

    @Override
    public String validate() {
        block11 : {
            block10 : {
                block9 : {
                    block8 : {
                        block7 : {
                            try {
                                this.setValid(false);
                                if ("9F".equals((Object)this.getParameter1())) break block7;
                                return "6A86";
                            }
                            catch (Exception exception) {
                                this.setValid(false);
                                return "6985";
                            }
                        }
                        if ("36".equals((Object)this.getParameter2()) || "13".equals((Object)this.getParameter2())) break block8;
                        return "6A86";
                    }
                    if (this.getLengthCommandData() == null || "00".equals((Object)this.getLengthCommandData())) break block9;
                    return "6700";
                }
                if (this.getLengthExpectedData() == null || "00".equals((Object)this.getLengthExpectedData())) break block10;
                return "6700";
            }
            if (!TokenDataParser.isClientVersionUpdateRequired(this.getParsedTokenRecord())) break block11;
            return "6986";
        }
        this.setValid(true);
        return "9000";
    }
}

