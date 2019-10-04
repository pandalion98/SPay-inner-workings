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

public class SelectApduInfo
extends APDU {
    public SelectApduInfo() {
        this.setCommand(APDURequestCommand.SELECT_AID);
    }

    @Override
    public APDUResponse buildResponse() {
        return null;
    }

    @Override
    public String validate() {
        block15 : {
            block16 : {
                block14 : {
                    block13 : {
                        block12 : {
                            block11 : {
                                block10 : {
                                    block9 : {
                                        try {
                                            this.setValid(false);
                                            if ("04".equals((Object)this.getParameter1())) break block9;
                                            return "6A86";
                                        }
                                        catch (Exception exception) {
                                            this.setValid(false);
                                            return "6985";
                                        }
                                    }
                                    if (!"02".equals((Object)this.getParameter2())) break block10;
                                    return "6A82";
                                }
                                if ("00".equals((Object)this.getParameter2())) break block11;
                                return "6A86";
                            }
                            if ("00".equals((Object)this.getLengthExpectedData())) break block12;
                            return "6700";
                        }
                        if (this.getDataLength() != 0) break block13;
                        return "6700";
                    }
                    if (this.getData() == null || this.getData().startsWith("A00000002501")) break block14;
                    return "6A82";
                }
                if (this.getDataLength() != 0 && (this.getData() == null || this.getData().length() != this.getDataLength())) break block15;
                if (!TokenDataParser.isClientVersionUpdateRequired(this.getParsedTokenRecord())) break block16;
                return "6283";
            }
            this.setValid(true);
            return "9000";
        }
        return "6700";
    }
}

