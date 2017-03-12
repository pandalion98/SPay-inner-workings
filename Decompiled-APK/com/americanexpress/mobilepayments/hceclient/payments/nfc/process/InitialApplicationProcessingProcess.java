package com.americanexpress.mobilepayments.hceclient.payments.nfc.process;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.context.TagsMapUtil;
import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import com.americanexpress.mobilepayments.hceclient.model.TokenAPDUResponse;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandAPDU;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.CommandProcess;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.Constants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.EMVConstants;
import com.americanexpress.mobilepayments.hceclient.payments.nfc.PaymentUtils;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLParser;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLTag;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HashUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.americanexpress.sdkmodulelib.util.APDUConstants;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.security.MessageDigest;
import java.util.List;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;

public class InitialApplicationProcessingProcess extends CommandProcess {
    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        TokenAPDUResponse tokenAPDUResponse = new TokenAPDUResponse();
        try {
            if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException((short) ISO7816.SW_COMMAND_NOT_ALLOWED);
            } else if (StateMode.TERMINATE == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() != ApplicationInfoManager.STATE_APP_SELECTED) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else if (commandAPDU.getbP1() != null || commandAPDU.getbP2() != null) {
                throw new HCEClientException((short) ISO7816.SW_INCORRECT_P1P2);
            } else if (commandAPDU.getBaCData()[0] == -125 && commandAPDU.getbLc() == ((byte) (commandAPDU.getBaCData()[1] + 2))) {
                byte b;
                int i;
                List list = (List) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.PDOL_LIST);
                if (list == null || list.size() <= 0) {
                    b = (byte) 2;
                } else {
                    i = 0;
                    b = (byte) 2;
                    while (i < list.size()) {
                        byte tagLength;
                        DOLTag dOLTag = (DOLTag) list.get(i);
                        if (dOLTag.getTagName().compareToIgnoreCase(DOLParser.DOL_TOTAL_LEN) == 0) {
                            tagLength = (short) (dOLTag.getTagLength() + b);
                        } else {
                            tagLength = b;
                        }
                        i++;
                        b = tagLength;
                    }
                }
                if (b > (byte) 2 && commandAPDU.getbLc() <= (byte) 2) {
                    throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                } else if (commandAPDU.getbLc() != b) {
                    throw new HCEClientException((short) ISO7816.SW_WRONG_LENGTH);
                } else if (Constants.MAGIC_FALSE == DOLParser.computeDOL(HexUtils.byteArrayToHexString(commandAPDU.getBaCData(), 2, commandAPDU.getbLc() - 2), list)) {
                    throw new HCEClientException((short) ISO7816.SW_WRONG_LENGTH);
                } else if (Utility.byteArrayToInt(HexUtils.hexStringToByteArray(MetaDataManager.getMetaDataValue(MetaDataManager.RUNNING_ATC))) == HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
                    throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                } else {
                    byte[] hexStringToByteArray;
                    Object obj;
                    int i2;
                    int i3;
                    byte b2;
                    String str = (String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TERMINAL_TYPE);
                    String str2 = (String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TERMINAL_CAPABILITY);
                    if (str != null && str.compareToIgnoreCase(BuildConfig.FLAVOR) != 0) {
                        hexStringToByteArray = HexUtils.hexStringToByteArray(str);
                        obj = null;
                    } else if (str2 == null || str2.compareToIgnoreCase(BuildConfig.FLAVOR) == 0) {
                        hexStringToByteArray = HexUtils.hexStringToByteArray("B0");
                        obj = null;
                    } else {
                        hexStringToByteArray = HexUtils.hexStringToByteArray(str2);
                        i = 1;
                    }
                    int i4;
                    if (obj != null) {
                        b = hexStringToByteArray[0];
                        i2 = b;
                        i3 = hexStringToByteArray[2];
                        i4 = hexStringToByteArray[1];
                    } else {
                        b2 = hexStringToByteArray[0];
                        i4 = 0;
                        b = b2;
                        byte b3 = b2;
                    }
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_ONLINE_CAPABILITY, Short.valueOf(Constants.MAGIC_FALSE));
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_CVR, new byte[]{(byte) 3, VerifyPINApdu.P2_PLAINTEXT, (byte) 0, (byte) 0});
                    if (Operation.OPERATION.getMode().equals(OperationMode.PAYMENT) && 117 == ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue()) {
                        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS, Byte.valueOf(ApplicationInfoManager.MOB_CVM_PERFORMED));
                        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE, Byte.valueOf(ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSCODE));
                    }
                    if (obj != null) {
                        if (((byte) (i3 & -128)) == null) {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_ONLINE_CAPABILITY, Short.valueOf(Constants.MAGIC_TRUE));
                        }
                        if (((byte) (i3 & 64)) == 64 && ((byte) (r1 & -128)) == -128) {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ, Short.valueOf(Constants.MAGIC_TRUE));
                        } else {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ, Short.valueOf(Constants.MAGIC_FALSE));
                        }
                        if (((byte) (i2 & 8)) == 8) {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE, Byte.valueOf(ApplicationInfoManager.TERM_XP3));
                        } else {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE, Byte.valueOf(ApplicationInfoManager.TERM_XP2));
                        }
                        if (((byte) (i2 & 16)) == 16) {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(ApplicationInfoManager.TERMINAL_MODE_CL_EMV));
                            if (PaymentUtils.checkXPMConfig((byte) 2, (byte) 1) == Constants.MAGIC_TRUE || ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_CAPABILITY)).byteValue() == -63) {
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(MCFCITemplate.TAG_FCI_ISSUER_IIN));
                            }
                        } else {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(MCFCITemplate.TAG_FCI_ISSUER_IIN));
                        }
                    } else {
                        b2 = (byte) (i3 & 7);
                        if (b2 == (byte) 1 || b2 == (byte) 2 || b2 == 4 || b2 == 5) {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_ONLINE_CAPABILITY, Short.valueOf(Constants.MAGIC_TRUE));
                        }
                        if (((byte) (i2 & 8)) == 8) {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ, Short.valueOf(Constants.MAGIC_TRUE));
                        } else {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ, Short.valueOf(Constants.MAGIC_FALSE));
                        }
                        switch ((byte) (i2 & -64)) {
                            case Byte.MIN_VALUE:
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(ApplicationInfoManager.TERMINAL_MODE_CL_EMV));
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE, Byte.valueOf(ApplicationInfoManager.TERM_XP2));
                                if (PaymentUtils.checkXPMConfig((byte) 2, (byte) 1) == Constants.MAGIC_TRUE || ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_CAPABILITY)).byteValue() == -63) {
                                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(MCFCITemplate.TAG_FCI_ISSUER_IIN));
                                }
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ, Short.valueOf(Constants.MAGIC_FALSE));
                                break;
                            case (byte) -64:
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(ApplicationInfoManager.TERMINAL_MODE_CL_EMV));
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE, Byte.valueOf(ApplicationInfoManager.TERM_XP3));
                                if (PaymentUtils.checkXPMConfig((byte) 2, (byte) 1) == Constants.MAGIC_TRUE || ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_CAPABILITY)).byteValue() == -63) {
                                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(MCFCITemplate.TAG_FCI_ISSUER_IIN));
                                    break;
                                }
                            case ECCurve.COORD_AFFINE /*0*/:
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(MCFCITemplate.TAG_FCI_ISSUER_IIN));
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE, Byte.valueOf(ApplicationInfoManager.TERM_XP1));
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ, Short.valueOf(Constants.MAGIC_FALSE));
                                break;
                            case X509KeyUsage.nonRepudiation /*64*/:
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE, Byte.valueOf(MCFCITemplate.TAG_FCI_ISSUER_IIN));
                                if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ)).shortValue() != Constants.MAGIC_TRUE) {
                                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE, Byte.valueOf(ApplicationInfoManager.TERM_XP2));
                                    break;
                                }
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE, Byte.valueOf(ApplicationInfoManager.TERM_XP3));
                                break;
                            default:
                                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                        }
                        PaymentUtils.setCVRandCVMBasedOnCDCVMSTatus();
                    }
                    str2 = CPDLConfig.getDGI_TAG(CPDLConfig.MS_AIP_DGI);
                    if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE)).byteValue() == 83) {
                        str2 = CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_DGI);
                        if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_APP)).shortValue() == ApplicationInfoManager.APP_ALIAS) {
                            str2 = CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI);
                        }
                    }
                    if (Operation.OPERATION.getMode().equals(OperationMode.TAP_PAYMENT) && PaymentUtils.checkXPMConfig((byte) 4, (byte) 8) == Constants.MAGIC_TRUE && ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE)).byteValue() != 83) {
                        throw new HCEClientException((short) ISO7816.SW_COMMAND_NOT_ALLOWED);
                    }
                    TagValue tagValue = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), str2, CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AIP_TAG), true);
                    TagValue tagValue2 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), str2, CPDLConfig.getDGI_TAG(CPDLConfig.EMV_AFL_TAG), true);
                    if (tagValue == null || tagValue2 == null) {
                        throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                    }
                    byte[] hexStringToByteArray2;
                    str = tagValue.getValue();
                    str2 = tagValue2.getValue();
                    Object hexStringToByteArray3 = HexUtils.hexStringToByteArray(str);
                    Object hexStringToByteArray4 = HexUtils.hexStringToByteArray(str2);
                    if (PaymentUtils.checkXPMConfig((byte) 4, (byte) 4) == Constants.MAGIC_FALSE) {
                        hexStringToByteArray3[0] = (byte) (hexStringToByteArray3[0] & -2);
                    }
                    PaymentUtils.setOperationStatusInSession(OperationStatus.NO_FURTHER_ACTION_REQUIRED);
                    switch (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE)).byteValue()) {
                        case CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA /*51*/:
                            if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_REQ)).shortValue() != Constants.MAGIC_TRUE) {
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf(ApplicationInfoManager.AMOUNT_STATUS_LOW));
                                break;
                            }
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf((byte) -7));
                            break;
                        case EACTags.TRACK3_APPLICATION /*88*/:
                            hexStringToByteArray3[0] = (byte) (hexStringToByteArray3[0] & EACTags.DYNAMIC_AUTHENTIFICATION_TEMPLATE);
                            hexStringToByteArray3[1] = (byte) 0;
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf((byte) -7));
                            break;
                        case (byte) 119:
                            if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE)).byteValue() == 66) {
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf((byte) -7));
                                break;
                            }
                            break;
                    }
                    str2 = HexUtils.byteArrayToHexString(hexStringToByteArray3);
                    String byteArrayToHexString = HexUtils.byteArrayToHexString(hexStringToByteArray4);
                    if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE)).byteValue() == 51 && PaymentUtils.checkXPMConfig((byte) 4, (byte) 1) == Constants.MAGIC_TRUE) {
                        hexStringToByteArray2 = HexUtils.hexStringToByteArray(buildEp3Resposne(str2, byteArrayToHexString, HexUtils.byteArrayToHexString((byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.MOBILE_CVM_RESULTS))));
                    } else {
                        hexStringToByteArray2 = HexUtils.hexStringToByteArray(APDUConstants.GPO_RESPONSE_TEMPLATE + Utility.constructLV(str2 + byteArrayToHexString));
                    }
                    if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS)).byteValue() == -7 && 117 == ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue()) {
                        PaymentUtils.setOperationStatusInSession(OperationStatus.FURTHER_ACTION_REQUIRED);
                        if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE)).byteValue() == 51) {
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_ABORT_IN_GAC, Short.valueOf(Constants.MAGIC_TRUE));
                        }
                    }
                    tokenAPDUResponse.setBaApduResponse(hexStringToByteArray2);
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_INITIATED));
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_AFL, hexStringToByteArray4);
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_AIP, hexStringToByteArray3);
                    MessageDigest sHA1MessageDigest = HashUtils.getSHA1MessageDigest();
                    sHA1MessageDigest.update(commandAPDU.getBaCData(), 2, commandAPDU.getBaCData()[1]);
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_CDA_TXN_HASH_OBJ, sHA1MessageDigest);
                    return tokenAPDUResponse;
                }
            } else {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            }
        } catch (HCEClientException e) {
            if (e.getIsoSW() == (short) 0) {
                tokenAPDUResponse.setsSW(ISO7816.SW_UNKNOWN);
            } else {
                tokenAPDUResponse.setsSW(e.getIsoSW());
            }
            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_IDLE));
        }
    }

    private static String buildEp3Resposne(String str, String str2, String str3) {
        StringBuilder stringBuilder = new StringBuilder();
        String str4 = com.americanexpress.sdkmodulelib.util.Constants.APPLICATION_FILE_LOCATOR_AIP_TAG + Utility.constructLV(str);
        String str5 = com.americanexpress.sdkmodulelib.util.Constants.APPLICATION_FILE_LOCATOR_AFL_TAG + Utility.constructLV(str2);
        stringBuilder.append(APDUConstants.GPO_RESPONSE_TEMPLATE_WITH_CVM).append(Utility.constructLV(str4 + str5 + (APDUConstants.GPO_CVM_RESULT_TAG + Utility.constructLV(str3))));
        return stringBuilder.toString();
    }
}
