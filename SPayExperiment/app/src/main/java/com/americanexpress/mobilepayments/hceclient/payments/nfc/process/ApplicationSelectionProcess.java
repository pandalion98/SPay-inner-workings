/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Short
 *  java.lang.String
 *  java.util.List
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
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PPSEResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLParser;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.util.List;
import java.util.Map;

public class ApplicationSelectionProcess
extends CommandProcess {
    private static final byte[] _baPPSE_AID = new byte[]{50, 80, 65, 89, 46, 83, 89, 83, 46, 68, 68, 70, 48, 49};

    /*
     * Enabled aggressive block sorting
     */
    private void setPDOLInAppInfo() {
        TagValue tagValue = (Short)ApplicationInfoManager.getApplcationInfoValue("TR_ACTIVE_APP") == -31176 ? TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG("PAYMENT_PARAMETERS_DGI"), CPDLConfig.getDGI_TAG("PDOL_TAG"), true) : null;
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG("SELECT_AID_DGI"), CPDLConfig.getDGI_TAG("PDOL_TAG"), true);
        }
        List<DOLTag> list = tagValue != null ? DOLParser.parseDOL(tagValue.getValue()) : null;
        ApplicationInfoManager.setApplcationInfoValue("PDOL_LIST", list);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public TokenAPDUResponse process(CommandAPDU var1_1) {
        block13 : {
            block15 : {
                var2_2 = new TokenAPDUResponse();
                try {
                    block14 : {
                        if ((2 & var1_1.getbP2()) == 2) {
                            throw new HCEClientException(27266);
                        }
                        if (var1_1.getbP1() != 4) throw new HCEClientException(27270);
                        if (var1_1.getbP2() != 0) {
                            throw new HCEClientException(27270);
                        }
                        if (var1_1.getbLc() < 5) throw new HCEClientException(26368);
                        if (var1_1.getbLc() > 16) {
                            throw new HCEClientException(26368);
                        }
                        var5_4 = (PPSEResponse)ApplicationInfoManager.getApplcationInfoValue("TR_CUR_PPSE_RES_OBJ");
                        if (var5_4 == null) {
                            throw new HCEClientException(27013);
                        }
                        if (HexUtils.secureCompare(var1_1.getBaCData(), (short)0, ApplicationSelectionProcess._baPPSE_AID, (short)0, var1_1.getbLc()) != Constants.MAGIC_TRUE) break block14;
                        var9_5 = var5_4.getPPSERes();
                        ApplicationInfoManager.setApplcationInfoValue("TR_APP_STATE", (short)-23174);
                        ApplicationInfoManager.setApplcationInfoValue("TR_ACTIVE_APP", (short)-26794);
                        ** GOTO lbl42
                    }
                    var6_6 = var5_4.isAIDPresent(var1_1.getBaCData());
                    if (var6_6 == 0) {
                        throw new HCEClientException(27266);
                    }
                    var5_4.getCurrentAID();
                }
                catch (HCEClientException var3_3) {
                    ApplicationInfoManager.setApplcationInfoValue("TR_APP_STATE", (short)14161);
                    if (var3_3.getIsoSW() == 0) {
                        var2_2.setsSW((short)28416);
                        return var2_2;
                    }
                    break block13;
                }
                if ((byte)(var6_6 & -128) == -128) {
                    ApplicationInfoManager.setApplcationInfoValue("TR_ACTIVE_APP", (short)9509);
                    var9_5 = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue("PRIMARY_AID_FCI"));
                }
                break block15;
lbl36: // 2 sources:
                do {
                    this.setPDOLInAppInfo();
                    var10_7 = PaymentUtils.getTokenStatus();
                    if (StateMode.BLOCKED == var10_7 || StateMode.TERMINATE == var10_7) {
                        var2_2.setsSW((short)25219);
                    }
                    ApplicationInfoManager.setApplcationInfoValue("TR_APP_STATE", (short)13141);
lbl42: // 2 sources:
                    var2_2.setBaApduResponse(var9_5);
                    return var2_2;
                    break;
                } while (true);
            }
            ApplicationInfoManager.setApplcationInfoValue("TR_ACTIVE_APP", (short)-31176);
            var13_8 = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue("ALIAS_AID_FCI"));
            var9_5 = var13_8;
            ** while (true)
        }
        var2_2.setsSW(var3_3.getIsoSW());
        return var2_2;
    }
}

