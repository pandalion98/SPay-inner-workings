/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Byte
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.security.DigestException
 *  java.security.MessageDigest
 *  java.util.List
 *  java.util.Map
 */
package com.americanexpress.mobilepayments.hceclient.payments.nfc.process;

import android.util.Log;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.DataContext;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLParser;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.TIDGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagKey;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import java.math.BigInteger;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

public class FirstCardActionAnalysisProcess
extends CommandProcess {
    public static final short OFF_CDA_AC = 13;
    public static final short OFF_CDA_CID = 12;
    public static final short OFF_CDA_IDN = 4;
    public static final short OFF_CDA_PADDING_BB = 41;
    public static final short OFF_CDA_TRANS_HASH_CODE = 21;
    private static final byte[] baPref_CDA_header = new byte[]{5, 1, 38, 8};
    private byte[] _baPref_GENAC_fmt2 = new byte[]{-97, 39, 1, 0, -97, 54, 2, 0, 0};

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private byte[] buildIAD(byte var1_1, byte var2_2, byte var3_3, short var4_4) {
        block17 : {
            block16 : {
                if (var1_1 != 83) break block16;
                var24_5 = 8;
                var25_6 = HexUtils.hexStringToByteArray((String)this.dataContext.getDgiMap().get((Object)CPDLConfig.getDGI_TAG("ISSUER_APPLICATION_DATA_EMV_DGI")));
                if (var25_6.length < 11) {
                    throw new HCEClientException(28416);
                }
                var26_7 = var25_6[10];
                if (var26_7 != 1) ** GOTO lbl-1000
                var24_5 = 27;
                if (-11 + var25_6.length > 0) {
                    var5_8 = 27 + (-11 + var25_6.length);
                    var6_9 = var26_7;
                    var8_10 = true;
                    var7_11 = var25_6;
                } else lbl-1000: // 2 sources:
                {
                    var5_8 = var24_5;
                    var6_9 = var26_7;
                    var7_11 = var25_6;
                    var8_10 = false;
                }
                break block17;
            }
            var5_8 = 7;
            var6_9 = 2;
            var7_11 = null;
            var8_10 = false;
        }
        if (var4_4 == Constants.MAGIC_TRUE) {
            var5_8 += 3;
        }
        var9_12 = new byte[var5_8];
        if (var4_4 == Constants.MAGIC_TRUE) {
            var22_13 = HexUtils.setShort(var9_12, (short)0, (short)-24816);
            var23_14 = (short)(var22_13 + 1);
            var9_12[var22_13] = (byte)(-3 + var9_12.length);
            var10_15 = (short)(var23_14 + 1);
            var9_12[var23_14] = (byte)(-4 + var9_12.length);
        } else {
            var10_15 = (short)(true ? 1 : 0);
            var9_12[0] = (byte)(-1 + var9_12.length);
        }
        var11_16 = (short)(var10_15 + 1);
        var9_12[var10_15] = var2_2;
        var12_17 = (short)(var11_16 + 1);
        var9_12[var11_16] = var3_3;
        System.arraycopy((Object)((byte[])ApplicationInfoManager.getApplcationInfoValue("TR_CVR")), (int)0, (Object)var9_12, (int)var12_17, (int)4);
        var13_18 = (short)(var12_17 + 4);
        if (var1_1 != 83) return var9_12;
        var14_19 = (short)(var13_18 + 1);
        var9_12[var13_18] = var6_9;
        if (var6_9 != 1) return var9_12;
        var15_20 = (NFCLUPCTagValue)ApplicationInfoManager.getApplcationInfoValue("TR_NFC_LUPC_OBJ");
        var16_21 = var15_20 != null ? var15_20.getStartDate() : MetaDataManager.getSKDDForATC(MetaDataManager.getMetaDataValue("RUNNING_ATC"));
        if (var16_21 != null) {
            var20_22 = HexUtils.hexStringToByteArray(var16_21);
            var21_23 = (short)(var14_19 + 1);
            var9_12[var14_19] = (byte)var20_22.length;
            System.arraycopy((Object)var20_22, (int)0, (Object)var9_12, (int)var21_23, (int)var20_22.length);
            var17_24 = (short)(var21_23 + 16);
        } else {
            var17_24 = (short)(var14_19 + 1);
            var9_12[var14_19] = 0;
        }
        var18_25 = (short)(var17_24 + 1);
        var9_12[var17_24] = 1;
        if ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_STATUS") == -102) {
            switch ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_TYPE")) {
                default: {
                    var19_26 = var18_25;
                    break;
                }
                case 35: {
                    var19_26 = (short)(var18_25 + 1);
                    var9_12[var18_25] = 1;
                    break;
                }
                case -104: {
                    var19_26 = (short)(var18_25 + 1);
                    var9_12[var18_25] = 2;
                    break;
                }
                case 81: {
                    var19_26 = (short)(var18_25 + 1);
                    var9_12[var18_25] = 3;
                    break;
                }
                case 70: {
                    var19_26 = (short)(var18_25 + 1);
                    var9_12[var18_25] = 5;
                    break;
                }
            }
        } else {
            var19_26 = (short)(var18_25 + 1);
            var9_12[var18_25] = 0;
        }
        if (var8_10 == false) return var9_12;
        System.arraycopy((Object)var7_11, (int)11, (Object)var9_12, (int)var19_26, (int)(1 + var7_11[11]));
        return var9_12;
    }

    private short cardRiskManagement(byte[] arrby, byte by) {
        short s2 = 258;
        if ((Short)ApplicationInfoManager.getApplcationInfoValue("TR_TERMINAL_ONLINE_CAPABILITY") == Constants.MAGIC_FALSE) {
            s2 = 4402;
        }
        if (117 == (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_MOB_CVM_STATUS") && ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TXN_AMNT_STATUS") == -7 || (Short)ApplicationInfoManager.getApplcationInfoValue("TR_ABORT_IN_GAC") == Constants.MAGIC_TRUE)) {
            s2 = (short)(s2 | 16576);
        }
        return s2;
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
        block46 : {
            block50 : {
                block49 : {
                    block48 : {
                        block47 : {
                            block52 : {
                                block51 : {
                                    var2_2 = new TokenAPDUResponse();
                                    var8_3 = (byte)(-64 & var1_1.getbP1());
                                    var9_4 = (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TERMINAL_MODE");
                                    var10_5 = (byte[])ApplicationInfoManager.getApplcationInfoValue("TR_ACTIVE_AIP");
                                    if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                                        throw new HCEClientException(27014);
                                    }
                                    if (StateMode.TERMINATE == PaymentUtils.getTokenStatus()) throw new HCEClientException(27013);
                                    if ((Short)ApplicationInfoManager.getApplcationInfoValue("TR_APP_STATE") != 31357) {
                                        throw new HCEClientException(27013);
                                    }
                                    var11_8 = (byte)(-64 & var1_1.getbP1());
                                    if (var1_1.getbP2() != 0) throw new HCEClientException(27270);
                                    if ((byte)(47 & var1_1.getbP1()) != 0) throw new HCEClientException(27270);
                                    if (var11_8 == -64) {
                                        throw new HCEClientException(27270);
                                    }
                                    var12_9 = var9_4 == 83 ? (List)ApplicationInfoManager.getApplcationInfoValue("EMV_CDOL_LIST") : (List)ApplicationInfoManager.getApplcationInfoValue("MS_CDOL_LIST");
                                    if (Constants.MAGIC_FALSE == DOLParser.computeDOL(HexUtils.byteArrayToHexString(var1_1.getBaCData()), (List<DOLTag>)var12_9)) {
                                        throw new HCEClientException(26368);
                                    }
                                    if (var1_1.getbLc() != ((Short)ApplicationInfoManager.getApplcationInfoValue("DOL_TOTAL_LEN")).shortValue()) {
                                        throw new HCEClientException(26368);
                                    }
                                    if ((byte)(16 & var1_1.getbP1()) == 16) {
                                        var13_10 = Constants.MAGIC_TRUE;
                                    } else {
                                        if (Operation.OPERATION.getMode().equals((Object)OperationMode.TAP_PAYMENT) && PaymentUtils.checkXPMConfig((byte)4, (byte)8) == Constants.MAGIC_TRUE && HexUtils.checkBIT(var10_5, (short)0, (byte)1) == Constants.MAGIC_TRUE && var8_3 != 0) {
                                            throw new HCEClientException(27014);
                                        }
                                        var13_10 = Constants.MAGIC_FALSE;
                                    }
                                    if (var13_10 == Constants.MAGIC_TRUE && HexUtils.checkBIT(var10_5, (short)0, (byte)1) == Constants.MAGIC_FALSE) {
                                        throw new HCEClientException(27013);
                                    }
                                    if ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TXN_AMNT_STATUS") == -68) {
                                        if ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TERMINAL_TYPE") == 119 && var9_4 == 83 && HexUtils.checkBIT(HexUtils.hexStringToByteArray((String)ApplicationInfoManager.getApplcationInfoValue("95")), (short)2, (byte)-128) == Constants.MAGIC_TRUE) {
                                            ApplicationInfoManager.setApplcationInfoValue("TR_TXN_AMNT_STATUS", (byte)-7);
                                        } else {
                                            ApplicationInfoManager.setApplcationInfoValue("TR_TXN_AMNT_STATUS", (byte)-45);
                                        }
                                        if (var9_4 == 66) {
                                            ApplicationInfoManager.setApplcationInfoValue("TR_TXN_AMNT_STATUS", (byte)-7);
                                        }
                                    }
                                    var14_11 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_APP_CURRENCY_CD"), false);
                                    var15_12 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG("RISK_PARAMS_DGI"), CPDLConfig.getDGI_TAG("TAG_MOB_CVM_REQ_LIMIT"), false);
                                    if (var9_4 != 83) break block51;
                                    if (var14_11 != null && HexUtils.getShort(HexUtils.hexStringToByteArray(var14_11.getValue()), 0) != HexUtils.getShort(HexUtils.hexStringToByteArray((String)ApplicationInfoManager.getApplcationInfoValue("5F2A")), 0)) {
                                        ApplicationInfoManager.setApplcationInfoValue("TR_TXN_AMNT_STATUS", (byte)-7);
                                    }
                                    if (var15_12 == null) ** GOTO lbl-1000
                                    if (new BigInteger(var15_12.getValue(), 16).compareTo(new BigInteger((String)ApplicationInfoManager.getApplcationInfoValue("9F02"), 16)) == -1) {
                                        ApplicationInfoManager.setApplcationInfoValue("TR_TXN_AMNT_STATUS", (byte)-7);
                                    }
                                    var16_13 = var8_3;
                                    break block52;
                                }
                                if (var8_3 == 64) {
                                    var16_13 = -128;
                                } else lbl-1000: // 2 sources:
                                {
                                    var16_13 = var8_3;
                                }
                            }
                            if ((short)((var17_14 = this.cardRiskManagement(var1_1.getBaCData(), var9_4)) & -16192) == 16576) {
                                PaymentUtils.setOperationStatusInSession(OperationStatus.FURTHER_ACTION_REQUIRED);
                                throw new HCEClientException(27012);
                            }
                            var18_15 = (short)(var17_14 & 12336) == 4144 || var16_13 == 0 ? 0 : -128;
                            if (var18_15 == 0) {
                                PaymentUtils.resetCVR((byte)1, (byte)48);
                                var20_16 = Constants.MAGIC_FALSE;
                            } else {
                                PaymentUtils.setCVR((byte)1, (byte)32);
                                var20_16 = var13_10;
                            }
                            if (var20_16 == Constants.MAGIC_TRUE) {
                                PaymentUtils.setCVR((byte)3, (byte)2);
                            }
                            if (var9_4 == 66) {
                                PaymentUtils.resetCVR((byte)2, (byte)-2);
                                PaymentUtils.resetCVR((byte)3, (byte)-1);
                            }
                            if (var9_4 == 66) {
                                var23_17 = new byte[7 + var1_1.getbLc()];
                                var24_18 = (String)ApplicationInfoManager.getApplcationInfoValue("9F37");
                                var25_19 = var23_17;
                            } else {
                                var77_29 = new byte[9 + var1_1.getbLc()];
                                var24_18 = (String)ApplicationInfoManager.getApplcationInfoValue("9F02") + (String)ApplicationInfoManager.getApplcationInfoValue("9F03") + (String)ApplicationInfoManager.getApplcationInfoValue("9F1A") + (String)ApplicationInfoManager.getApplcationInfoValue("95") + (String)ApplicationInfoManager.getApplcationInfoValue("5F2A") + (String)ApplicationInfoManager.getApplcationInfoValue("9A") + (String)ApplicationInfoManager.getApplcationInfoValue("9C") + (String)ApplicationInfoManager.getApplcationInfoValue("9F37");
                                var25_19 = var77_29;
                            }
                            var26_20 = (short)(true ? 1 : 0);
                            var25_19[0] = var1_1.getbLc();
                            System.arraycopy((Object)HexUtils.hexStringToByteArray(var24_18), (int)0, (Object)var25_19, (int)var26_20, (int)var1_1.getbLc());
                            var27_21 = (short)(var26_20 + var1_1.getbLc());
                            var28_22 = (NFCLUPCTagValue)ApplicationInfoManager.getApplcationInfoValue("TR_NFC_LUPC_OBJ");
                            var29_23 = var28_22 != null ? Utility.byteArrayToInt(HexUtils.hexStringToByteArray(var28_22.getAtc())) : Utility.byteArrayToInt(HexUtils.hexStringToByteArray(MetaDataManager.getMetaDataValue("RUNNING_ATC")));
                            var30_24 = (byte[])ApplicationInfoManager.getApplcationInfoValue("TR_CVR");
                            if (var9_4 == 83) {
                                System.arraycopy((Object)var10_5, (int)0, (Object)var25_19, (int)var27_21, (int)var10_5.length);
                                var31_25 = (short)(var27_21 + var10_5.length);
                            } else {
                                var31_25 = var27_21;
                            }
                            System.arraycopy((Object)var30_24, (int)0, (Object)var25_19, (int)HexUtils.setShort(var25_19, var31_25, (short)var29_23), (int)var30_24.length);
                            var32_26 = new SecureComponentImpl();
                            var33_27 = new byte[8];
                            var34_28 = var32_26.computeAC(var25_19, var33_27);
                            if (var34_28 < 0) {
                                throw new HCEClientException(27013);
                            }
                            if (var34_28 == 65535) {
                                PaymentUtils.setTokenStatus(StateMode.TERMINATE);
                                Log.e((String)"core-hceclient", (String)"::FirstCardActionAnalysisProcess::computeAC::HIGHEST ATC REACHED - APPLICATION TERMINATED!!");
                                throw new HCEClientException(27013);
                            }
                            if (var28_22 != null) {
                                var36_30 = var28_22.getDki();
                            } else {
                                var76_32 = new Object[]{var34_28};
                                var36_30 = MetaDataManager.getDKIForATC(String.format((String)"%04x", (Object[])var76_32));
                            }
                            var37_31 = TagsMapUtil.getTagValue("MS_CVN_VALUE");
                            if (var9_4 == 83) {
                                var37_31 = TagsMapUtil.getTagValue("EMV_CVN_VALUE");
                            }
                            if (var36_30 != null && var37_31 != null) {
                                var39_33 = this.buildIAD(var9_4, HexUtils.hexStringToByteArray(var36_30)[0], HexUtils.hexStringToByteArray(var37_31)[0], var20_16);
                                PaymentUtils.removeUsedLUPCAndAdvanceATC();
                                if (var20_16 == Constants.MAGIC_FALSE) {
                                    var71_34 = new byte[13 + var39_33.length];
                                    var72_35 = (short)(true ? 1 : 0);
                                    var71_34[0] = -128;
                                    var73_36 = (short)(var72_35 + 1);
                                    var71_34[var72_35] = (byte)(-2 + var71_34.length);
                                    var74_37 = (short)(var73_36 + 1);
                                    var71_34[var73_36] = var18_15;
                                    var75_38 = HexUtils.setShort(var71_34, var74_37, (short)var34_28);
                                    System.arraycopy((Object)var33_27, (int)0, (Object)var71_34, (int)var75_38, (int)8);
                                    System.arraycopy((Object)var39_33, (int)0, (Object)var71_34, (int)((short)(var75_38 + 8)), (int)var39_33.length);
                                    var2_2.setBaApduResponse(var71_34);
                                    break block46;
                                }
                                var40_41 = (MessageDigest)ApplicationInfoManager.getApplcationInfoValue("TR_CDA_TXN_HASH_OBJ");
                                var40_41.update(var1_1.getBaCData());
                                var41_42 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG("TOKEN_METADATA_DGI"), CPDLConfig.getDGI_TAG("ICC_KEY_LENGTH_TAG"), true);
                                var42_43 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), TagsMapUtil.getTagKey("LOCK_CODE"));
                                if (var41_42 == null || var42_43 == null) {
                                    Log.e((String)"core-hceclient", (String)"FirstCardActionAnalysisProcess::process::ICC Key Len / Lock Code Not Found");
                                    throw new HCEClientException(28416);
                                }
                                var44_44 = Integer.parseInt((String)var41_42.getValue(), (int)16) / 8;
                                var45_45 = HexUtils.hexStringToByteArray(var42_43.getValue());
                                var46_46 = new byte[9 + var39_33.length];
                                var47_47 = new byte[var44_44 - 2];
                                System.arraycopy((Object)this._baPref_GENAC_fmt2, (int)0, (Object)var46_46, (int)0, (int)this._baPref_GENAC_fmt2.length);
                                var46_46[3] = var18_15;
                                var48_48 = HexUtils.setShort(var46_46, (short)7, (short)var34_28);
                                System.arraycopy((Object)var39_33, (int)0, (Object)var46_46, (int)var48_48, (int)var39_33.length);
                                var40_41.update(var46_46, 0, (int)((short)(var48_48 + var39_33.length)));
                                var40_41.digest(var47_47, 21, 20);
                                System.arraycopy((Object)FirstCardActionAnalysisProcess.baPref_CDA_header, (int)0, (Object)var47_47, (int)0, (int)FirstCardActionAnalysisProcess.baPref_CDA_header.length);
                                HexUtils.arrayFil(var47_47, (short)4, (short)8, (byte)0);
                                HexUtils.setShort(var47_47, (short)10, (short)var34_28);
                                var47_47[12] = var18_15;
                                System.arraycopy((Object)var33_27, (int)0, (Object)var47_47, (int)13, (int)8);
                                var52_49 = HexUtils.arrayFil(var47_47, (short)41, (short)(-25 + (var44_44 - 38)), (byte)-69);
                                System.arraycopy((Object)HexUtils.hexStringToByteArray((String)ApplicationInfoManager.getApplcationInfoValue("9F37")), (int)0, (Object)var47_47, (int)((short)(-25 + (-38 + (var44_44 + 41)))), (int)4);
                                (short)(var52_49 + 4);
                                var54_50 = new SecureComponentImpl();
                                var55_51 = new byte[var44_44];
                                var56_52 = var54_50.sign(var47_47, var55_51, var45_45);
                                if (var56_52 < 0) {
                                    throw new HCEClientException(27013);
                                }
                                var57_53 = var44_44 + 14 + var39_33.length;
                                if (var44_44 <= 127) break block47;
                                var57_53 += 2;
                                break block48;
                            }
                            Log.e((String)"core-hceclient", (String)"::FirstCardActionAnalysisProcess::process::DKI or CVN not available");
                            throw new HCEClientException(28416);
                        }
                        if (var57_53 - 2 > 127) {
                            ++var57_53;
                        }
                    }
                    try {
                        var58_54 = new byte[var57_53];
                        var59_55 = (short)(true ? 1 : 0);
                        var58_54[0] = 119;
                        if (-2 + var58_54.length <= 127) break block49;
                        var60_56 = (short)(var59_55 + 1);
                        var58_54[var59_55] = -127;
                        var61_57 = (short)(var60_56 + 1);
                        var58_54[var60_56] = (byte)(-3 + var58_54.length);
                        break block50;
                    }
                    catch (HCEClientException var5_6) {
                        ApplicationInfoManager.setApplcationInfoValue("TR_APP_STATE", (short)14161);
                        var2_2.setsSW(var5_6.getIsoSW());
                        return var2_2;
                    }
                    catch (DigestException var3_7) {
                        ApplicationInfoManager.setApplcationInfoValue("TR_APP_STATE", (short)14161);
                        var2_2.setsSW((short)28416);
                        return var2_2;
                    }
                }
                var61_57 = (short)(var59_55 + 1);
                var58_54[var59_55] = (byte)(-2 + var58_54.length);
            }
            System.arraycopy((Object)var46_46, (int)0, (Object)var58_54, (int)var61_57, (int)var46_46.length);
            var62_58 = HexUtils.setShort(var58_54, (short)(var61_57 + var46_46.length), (short)-24757);
            if (var44_44 > 127) {
                var63_59 = (short)(var62_58 + 1);
                var58_54[var62_58] = -127;
            } else {
                var63_59 = var62_58;
            }
            var64_60 = (short)(var63_59 + 1);
            var58_54[var63_59] = (byte)var56_52;
            System.arraycopy((Object)var55_51, (int)0, (Object)var58_54, (int)var64_60, (int)var56_52);
            var2_2.setBaApduResponse(var58_54);
        }
        if ((Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TXN_AMNT_STATUS") != -68) {
            var69_39 = OperationStatus.TXN_AMOUNT_HIGH;
            var70_40 = (Byte)ApplicationInfoManager.getApplcationInfoValue("TR_TXN_AMNT_STATUS") == -45 ? OperationStatus.TXN_AMOUNT_LOW : var69_39;
            PaymentUtils.setOperationStatusInSession(var70_40);
        }
        ApplicationInfoManager.setApplcationInfoValue("APPLICATION_CYPTOGRAM", HexUtils.byteArrayToHexString(var33_27));
        ApplicationInfoManager.setApplcationInfoValue("UNPREDICTABLE_NUMBER", (String)ApplicationInfoManager.getApplcationInfoValue("9F37"));
        ApplicationInfoManager.setApplcationInfoValue("TID", TIDGenerator.getTransactionID(var9_4));
        ApplicationInfoManager.setApplcationInfoValue("TR_APP_STATE", (short)26772);
        return var2_2;
    }
}

