/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.List
 */
package com.americanexpress.sdkmodulelib.model.apdu;

import com.americanexpress.sdkmodulelib.model.APDUResponse;
import com.americanexpress.sdkmodulelib.model.TagInfo;
import com.americanexpress.sdkmodulelib.model.apdu.APDU;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.util.TLVHelper;
import com.americanexpress.sdkmodulelib.util.TokenDataParser;
import java.util.List;

public class GetProcessingOptionsApduInfo
extends APDU {
    private static final String GPO_DATA_LENGTH = "03";
    private static final String TVL_TEMP_TAG = "F1";

    public GetProcessingOptionsApduInfo() {
        this.setCommand(APDURequestCommand.GPO);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void setTerminalType(String string) {
        TagInfo tagInfo2;
        block8 : {
            block7 : {
                String string2 = TVL_TEMP_TAG + GPO_DATA_LENGTH;
                TLVHelper tLVHelper = new TLVHelper();
                try {
                    List<TagInfo> list = tLVHelper.parseTLV(string2 + string);
                    if (list == null || list.size() != 2) break block7;
                    for (TagInfo tagInfo2 : list) {
                        boolean bl = tagInfo2.getTagName().equals((Object)TVL_TEMP_TAG);
                        if (bl) continue;
                        break block8;
                    }
                }
                catch (Exception exception) {
                    if (!false) {
                        SessionManager.getSession().setEMVTerminal(false);
                        return;
                    }
                    SessionManager.getSession().setTerminalTypeData(((TagInfo)null).getTagValue());
                    SessionManager.getSession().setEMVTerminal(TokenDataParser.isEMV(((TagInfo)null).getTagValue()));
                    SessionManager.getSession().setIsTerminalOffline(TokenDataParser.isTerminalTypeOffline(((TagInfo)null).getTagValue()));
                    return;
                }
                catch (Throwable throwable) {
                    if (!false) {
                        SessionManager.getSession().setEMVTerminal(false);
                        throw throwable;
                    }
                    SessionManager.getSession().setTerminalTypeData(((TagInfo)null).getTagValue());
                    SessionManager.getSession().setEMVTerminal(TokenDataParser.isEMV(((TagInfo)null).getTagValue()));
                    SessionManager.getSession().setIsTerminalOffline(TokenDataParser.isTerminalTypeOffline(((TagInfo)null).getTagValue()));
                    throw throwable;
                }
            }
            tagInfo2 = null;
        }
        if (tagInfo2 == null) {
            SessionManager.getSession().setEMVTerminal(false);
            return;
        }
        SessionManager.getSession().setTerminalTypeData(tagInfo2.getTagValue());
        SessionManager.getSession().setEMVTerminal(TokenDataParser.isEMV(tagInfo2.getTagValue()));
        SessionManager.getSession().setIsTerminalOffline(TokenDataParser.isTerminalTypeOffline(tagInfo2.getTagValue()));
    }

    @Override
    public APDUResponse buildResponse() {
        return null;
    }

    @Override
    public void setData(String string) {
        super.setData(string);
        if (string != null) {
            this.setTerminalType(string);
        }
    }

    @Override
    public String validate() {
        block19 : {
            block18 : {
                block17 : {
                    block16 : {
                        block15 : {
                            block14 : {
                                block13 : {
                                    block12 : {
                                        block11 : {
                                            try {
                                                this.setValid(false);
                                                if ("00".equals((Object)this.getParameter1())) break block11;
                                                return "6A86";
                                            }
                                            catch (Exception exception) {
                                                this.setValid(false);
                                                return "6985";
                                            }
                                        }
                                        if ("00".equals((Object)this.getParameter2())) break block12;
                                        return "6A86";
                                    }
                                    if (this.getLengthCommandData() == null || !"00".equals((Object)this.getLengthCommandData())) break block13;
                                    return "6700";
                                }
                                if (GPO_DATA_LENGTH.equals((Object)this.getLengthCommandData())) break block14;
                                return "6700";
                            }
                            if (this.getData() != null) break block15;
                            return "6A80";
                        }
                        if (this.getData().substring(0, 2).equals((Object)"83")) break block16;
                        return "6A80";
                    }
                    if (!this.getData().substring(2, 4).equals((Object)"00")) break block17;
                    return "6A80";
                }
                if (!TokenDataParser.isClientVersionUpdateRequired(this.getParsedTokenRecord())) break block18;
                return "6986";
            }
            if (this.getLengthExpectedData() == null || "00".equals((Object)this.getLengthExpectedData())) break block19;
            return "6700";
        }
        this.setValid(true);
        return "9000";
    }
}

