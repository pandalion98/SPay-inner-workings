/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.String
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc.process;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.Map;

public class ReadApplicationDataProcess
extends CommandProcess {
    /*
     * Enabled aggressive block sorting
     */
    private short checkAFL(byte by, byte by2, TokenAPDUResponse tokenAPDUResponse) {
        tokenAPDUResponse.setsSW((short)27266);
        byte[] arrby = (byte[])ApplicationInfoManager.getApplcationInfoValue("TR_ACTIVE_AFL");
        byte[] arrby2 = arrby == null ? (PaymentUtils.checkXPMConfig((byte)2, (byte)1) == Constants.MAGIC_TRUE ? HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG("MS_AIP_DGI"), CPDLConfig.getDGI_TAG("EMV_AFL_TAG"), true).getValue()) : HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG("EMV_AIP_DGI"), CPDLConfig.getDGI_TAG("EMV_AFL_TAG"), true).getValue())) : arrby;
        if (arrby2 != null) {
            int n2 = 0;
            while (n2 < arrby2.length) {
                short s2 = (short)(n2 + 1);
                byte by3 = (byte)(arrby2[n2] >> 3);
                short s3 = (short)(s2 + 1);
                byte by4 = arrby2[s2];
                short s4 = (short)(s3 + 1);
                byte by5 = arrby2[s3];
                if (by3 == by) {
                    tokenAPDUResponse.setsSW((short)27267);
                    if (by2 >= by4 && by2 <= by5) {
                        tokenAPDUResponse.setsSW((short)-28672);
                        return Constants.MAGIC_TRUE;
                    }
                    tokenAPDUResponse.setsSW((short)27267);
                }
                n2 = (short)(s4 + 1);
            }
        }
        return Constants.MAGIC_FALSE;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        boolean bl = false;
        TokenAPDUResponse tokenAPDUResponse = new TokenAPDUResponse();
        try {
            String string;
            byte by = (byte)(commandAPDU.getbP2() >> 3);
            byte by2 = commandAPDU.getbP1();
            if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException(27014);
            }
            if (StateMode.TERMINATE == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException(27013);
            }
            if ((Short)ApplicationInfoManager.getApplcationInfoValue("TR_APP_STATE") != 13141 && (Short)ApplicationInfoManager.getApplcationInfoValue("TR_APP_STATE") != 31357 && (Short)ApplicationInfoManager.getApplcationInfoValue("TR_APP_STATE") != 26772) {
                throw new HCEClientException(27013);
            }
            if ((7 & commandAPDU.getbP2()) != 4 || commandAPDU.getbP1() == 0 || (short)((short)(248 & commandAPDU.getbP2()) >> 3) > 30) {
                throw new HCEClientException(27270);
            }
            if (commandAPDU.getbCommandCase() != CommandAPDU.CASE_2) {
                throw new HCEClientException(26368);
            }
            if (by == 1 && by2 == 1) {
                if ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TERMINAL_MODE") == 83) {
                    string = CPDLConfig.getDGI_TAG("SFI1_REC1_DGI_EMV");
                } else {
                    string = CPDLConfig.getDGI_TAG("SFI1_REC1_DGI_MS");
                    bl = true;
                }
            } else {
                if (Constants.MAGIC_FALSE == this.checkAFL(by, by2, tokenAPDUResponse)) {
                    throw new HCEClientException(tokenAPDUResponse.getsSW());
                }
                string = Utility.byte2Hex(by) + Utility.byte2Hex(by2);
                bl = false;
            }
            String string2 = (String)this.dataContext.getDgiMap().get((Object)string);
            if (bl && PaymentUtils.checkXPMConfig((byte)4, (byte)1) == Constants.MAGIC_TRUE && -102 == (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_STATUS")) {
                int n2 = Integer.parseInt((String)TagsMapUtil.getTagValue("SERVICE_CODE_MS_OFFSET"), (int)10);
                byte by3 = (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_TYPE");
                String string3 = "721";
                switch (by3) {
                    case 70: {
                        string3 = "728";
                        break;
                    }
                    case 35: 
                    case 81: {
                        string3 = "727";
                        break;
                    }
                }
                string2 = string2.substring(0, n2) + string3 + string2.substring(n2 + 3);
            }
            tokenAPDUResponse.setBaApduResponse(HexUtils.hexStringToByteArray(string2));
            return tokenAPDUResponse;
        }
        catch (HCEClientException hCEClientException) {
            if (hCEClientException.getIsoSW() == 0) {
                tokenAPDUResponse.setsSW((short)28416);
            } else {
                tokenAPDUResponse.setsSW(hCEClientException.getIsoSW());
            }
            ApplicationInfoManager.setApplcationInfoValue("TR_APP_STATE", (short)14161);
            return tokenAPDUResponse;
        }
    }
}

