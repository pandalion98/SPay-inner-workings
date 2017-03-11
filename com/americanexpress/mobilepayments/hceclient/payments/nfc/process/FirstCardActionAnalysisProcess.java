package com.americanexpress.mobilepayments.hceclient.payments.nfc.process;

import android.util.Log;
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
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentImpl;
import com.americanexpress.mobilepayments.hceclient.session.Operation;
import com.americanexpress.mobilepayments.hceclient.session.OperationMode;
import com.americanexpress.mobilepayments.hceclient.session.StateMode;
import com.americanexpress.mobilepayments.hceclient.utils.common.CPDLConfig;
import com.americanexpress.mobilepayments.hceclient.utils.common.DOLParser;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.OperationStatus;
import com.americanexpress.mobilepayments.hceclient.utils.common.TIDGenerator;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.Utility;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.NFCLUPCTagValue;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.mastercard.mcbp.core.mpplite.states.StatesConstants;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.math.BigInteger;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.List;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.asn1.eac.EACTags;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.crypto.tls.ExtensionType;

public class FirstCardActionAnalysisProcess extends CommandProcess {
    public static final short OFF_CDA_AC = (short) 13;
    public static final short OFF_CDA_CID = (short) 12;
    public static final short OFF_CDA_IDN = (short) 4;
    public static final short OFF_CDA_PADDING_BB = (short) 41;
    public static final short OFF_CDA_TRANS_HASH_CODE = (short) 21;
    private static final byte[] baPref_CDA_header;
    private byte[] _baPref_GENAC_fmt2;

    public FirstCardActionAnalysisProcess() {
        this._baPref_GENAC_fmt2 = new byte[]{(byte) -97, (byte) 39, (byte) 1, (byte) 0, (byte) -97, (byte) 54, (byte) 2, (byte) 0, (byte) 0};
    }

    static {
        baPref_CDA_header = new byte[]{(byte) 5, (byte) 1, (byte) 38, (byte) 8};
    }

    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        TokenAPDUResponse tokenAPDUResponse = new TokenAPDUResponse();
        try {
            short s = Constants.MAGIC_FALSE;
            byte b = (byte) (commandAPDU.getbP1() & -64);
            byte byteValue = ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_MODE)).byteValue();
            byte[] bArr = (byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_AIP);
            if (StateMode.BLOCKED == PaymentUtils.getTokenStatus()) {
                throw new HCEClientException((short) ISO7816.SW_COMMAND_NOT_ALLOWED);
            } else if (StateMode.TERMINATE == PaymentUtils.getTokenStatus() || ((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE)).shortValue() != ApplicationInfoManager.STATE_APP_INITIATED) {
                throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
            } else {
                byte b2 = (byte) (commandAPDU.getbP1() & -64);
                if (commandAPDU.getbP2() == null && ((byte) (commandAPDU.getbP1() & 47)) == null && b2 != -64) {
                    List list;
                    if (byteValue == 83) {
                        list = (List) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.EMV_CDOL_LIST);
                    } else {
                        list = (List) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.MS_CDOL_LIST);
                    }
                    if (Constants.MAGIC_FALSE == DOLParser.computeDOL(HexUtils.byteArrayToHexString(commandAPDU.getBaCData()), list)) {
                        throw new HCEClientException((short) ISO7816.SW_WRONG_LENGTH);
                    } else if (commandAPDU.getbLc() != ((Short) ApplicationInfoManager.getApplcationInfoValue(DOLParser.DOL_TOTAL_LEN)).shortValue()) {
                        throw new HCEClientException((short) ISO7816.SW_WRONG_LENGTH);
                    } else {
                        short s2;
                        if (((byte) (commandAPDU.getbP1() & 16)) == 16) {
                            s2 = Constants.MAGIC_TRUE;
                        } else if (Operation.OPERATION.getMode().equals(OperationMode.TAP_PAYMENT) && PaymentUtils.checkXPMConfig((byte) 4, (byte) 8) == Constants.MAGIC_TRUE && HexUtils.checkBIT(bArr, (short) 0, (byte) 1) == Constants.MAGIC_TRUE && b != null) {
                            throw new HCEClientException((short) ISO7816.SW_COMMAND_NOT_ALLOWED);
                        } else {
                            s2 = Constants.MAGIC_FALSE;
                        }
                        if (s2 == Constants.MAGIC_TRUE && HexUtils.checkBIT(bArr, (short) 0, (byte) 1) == Constants.MAGIC_FALSE) {
                            throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                        }
                        Object obj;
                        if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS)).byteValue() == -68) {
                            if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_TYPE)).byteValue() == 119 && byteValue == 83 && HexUtils.checkBIT(HexUtils.hexStringToByteArray((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_TERM_VERIFICATION_RSLT)), (short) 2, VerifyPINApdu.P2_PLAINTEXT) == Constants.MAGIC_TRUE) {
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf((byte) -7));
                            } else {
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf(ApplicationInfoManager.AMOUNT_STATUS_LOW));
                            }
                            if (byteValue == 66) {
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf((byte) -7));
                            }
                        }
                        TagValue tagValue = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_APP_CURRENCY_CD), false);
                        TagValue tagValue2 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.RISK_PARAMS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.TAG_MOB_CVM_REQ_LIMIT), false);
                        if (byteValue == 83) {
                            if (!(tagValue == null || HexUtils.getShort(HexUtils.hexStringToByteArray(tagValue.getValue()), 0) == HexUtils.getShort(HexUtils.hexStringToByteArray((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_TXN_CURR_CD)), 0))) {
                                ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf((byte) -7));
                            }
                            if (tagValue2 != null) {
                                if (new BigInteger(tagValue2.getValue(), 16).compareTo(new BigInteger((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_AMOUNT_AUTH), 16)) == -1) {
                                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS, Byte.valueOf((byte) -7));
                                }
                                obj = b;
                            }
                            obj = b;
                        } else {
                            if (b == 64) {
                                obj = -128;
                            }
                            obj = b;
                        }
                        short cardRiskManagement = cardRiskManagement(commandAPDU.getBaCData(), byteValue);
                        if (((short) (cardRiskManagement & -16192)) == Constants.CRM_SW6984) {
                            PaymentUtils.setOperationStatusInSession(OperationStatus.FURTHER_ACTION_REQUIRED);
                            throw new HCEClientException((short) ISO7816.SW_DATA_INVALID);
                        }
                        Object obj2;
                        short s3;
                        Object obj3;
                        Object obj4;
                        int byteArrayToInt;
                        if (((short) (cardRiskManagement & 12336)) == Constants.CRM_REJECT_TRANS || r1 == null) {
                            obj2 = null;
                        } else {
                            int i = -128;
                        }
                        if (obj2 == null) {
                            PaymentUtils.resetCVR((byte) 1, (byte) 48);
                            s3 = Constants.MAGIC_FALSE;
                        } else {
                            PaymentUtils.setCVR((byte) 1, VerifyPINApdu.INS);
                            s3 = s2;
                        }
                        if (s3 == Constants.MAGIC_TRUE) {
                            PaymentUtils.setCVR((byte) 3, (byte) 2);
                        }
                        if (byteValue == 66) {
                            PaymentUtils.resetCVR((byte) 2, (byte) -2);
                            PaymentUtils.resetCVR((byte) 3, (byte) -1);
                        }
                        String str = BuildConfig.FLAVOR;
                        if (byteValue == 66) {
                            str = (String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_UN);
                            obj3 = new byte[(commandAPDU.getbLc() + 7)];
                        } else {
                            obj4 = new byte[(commandAPDU.getbLc() + 9)];
                            str = ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_AMOUNT_AUTH)) + ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_AMOUNT_OTHR)) + ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_TERM_CNTRY_CD)) + ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_TERM_VERIFICATION_RSLT)) + ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_TXN_CURR_CD)) + ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_TXN_DATE)) + ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_TXN_TYPE)) + ((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_UN));
                            obj3 = obj4;
                        }
                        s2 = (short) 1;
                        obj3[0] = commandAPDU.getbLc();
                        System.arraycopy(HexUtils.hexStringToByteArray(str), 0, obj3, s2, commandAPDU.getbLc());
                        cardRiskManagement = (short) (commandAPDU.getbLc() + s2);
                        NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_NFC_LUPC_OBJ);
                        if (nFCLUPCTagValue != null) {
                            byteArrayToInt = Utility.byteArrayToInt(HexUtils.hexStringToByteArray(nFCLUPCTagValue.getAtc()));
                        } else {
                            byteArrayToInt = Utility.byteArrayToInt(HexUtils.hexStringToByteArray(MetaDataManager.getMetaDataValue(MetaDataManager.RUNNING_ATC)));
                        }
                        byte[] bArr2 = (byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_CVR);
                        if (byteValue == 83) {
                            System.arraycopy(bArr, 0, obj3, cardRiskManagement, bArr.length);
                            s = (short) (bArr.length + cardRiskManagement);
                        } else {
                            s = cardRiskManagement;
                        }
                        System.arraycopy(bArr2, 0, obj3, HexUtils.setShort(obj3, s, (short) byteArrayToInt), bArr2.length);
                        obj4 = new byte[8];
                        int computeAC = new SecureComponentImpl().computeAC(obj3, obj4);
                        if (computeAC < 0) {
                            throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                        } else if (computeAC == HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
                            PaymentUtils.setTokenStatus(StateMode.TERMINATE);
                            Log.e(HCEClientConstants.TAG, "::FirstCardActionAnalysisProcess::computeAC::HIGHEST ATC REACHED - APPLICATION TERMINATED!!");
                            throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                        } else {
                            if (nFCLUPCTagValue != null) {
                                str = nFCLUPCTagValue.getDki();
                            } else {
                                str = MetaDataManager.getDKIForATC(String.format("%04x", new Object[]{Integer.valueOf(computeAC)}));
                            }
                            String tagValue3 = TagsMapUtil.getTagValue(HCEClientConstants.MS_CVN_VALUE);
                            if (byteValue == 83) {
                                tagValue3 = TagsMapUtil.getTagValue(HCEClientConstants.EMV_CVN_VALUE);
                            }
                            if (str == null || tagValue3 == null) {
                                Log.e(HCEClientConstants.TAG, "::FirstCardActionAnalysisProcess::process::DKI or CVN not available");
                                throw new HCEClientException((short) ISO7816.SW_UNKNOWN);
                            }
                            obj = buildIAD(byteValue, HexUtils.hexStringToByteArray(str)[0], HexUtils.hexStringToByteArray(tagValue3)[0], s3);
                            PaymentUtils.removeUsedLUPCAndAdvanceATC();
                            if (s3 == Constants.MAGIC_FALSE) {
                                Object obj5 = new byte[(obj.length + 13)];
                                short s4 = (short) 1;
                                obj5[0] = VerifyPINApdu.P2_PLAINTEXT;
                                short s5 = (short) (s4 + 1);
                                obj5[s4] = (byte) (obj5.length - 2);
                                s4 = (short) (s5 + 1);
                                obj5[s5] = obj2;
                                cardRiskManagement = HexUtils.setShort(obj5, s4, (short) computeAC);
                                System.arraycopy(obj4, 0, obj5, cardRiskManagement, 8);
                                System.arraycopy(obj, 0, obj5, (short) (cardRiskManagement + 8), obj.length);
                                tokenAPDUResponse.setBaApduResponse(obj5);
                            } else {
                                MessageDigest messageDigest = (MessageDigest) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_CDA_TXN_HASH_OBJ);
                                messageDigest.update(commandAPDU.getBaCData());
                                tagValue2 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.TOKEN_METADATA_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.ICC_KEY_LENGTH_TAG), true);
                                TagValue tagValue4 = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), TagsMapUtil.getTagKey(HCEClientConstants.LOCK_CODE));
                                if (tagValue2 == null || tagValue4 == null) {
                                    Log.e(HCEClientConstants.TAG, "FirstCardActionAnalysisProcess::process::ICC Key Len / Lock Code Not Found");
                                    throw new HCEClientException((short) ISO7816.SW_UNKNOWN);
                                }
                                byteArrayToInt = Integer.parseInt(tagValue2.getValue(), 16) / 8;
                                byte[] hexStringToByteArray = HexUtils.hexStringToByteArray(tagValue4.getValue());
                                Object obj6 = new byte[(obj.length + 9)];
                                Object obj7 = new byte[(byteArrayToInt - 2)];
                                System.arraycopy(this._baPref_GENAC_fmt2, 0, obj6, 0, this._baPref_GENAC_fmt2.length);
                                obj6[3] = obj2;
                                short s6 = HexUtils.setShort(obj6, (short) 7, (short) computeAC);
                                System.arraycopy(obj, 0, obj6, s6, obj.length);
                                messageDigest.update(obj6, 0, (short) (s6 + obj.length));
                                messageDigest.digest(obj7, 21, 20);
                                System.arraycopy(baPref_CDA_header, 0, obj7, 0, baPref_CDA_header.length);
                                HexUtils.arrayFil(obj7, OFF_CDA_IDN, (short) 8, (byte) 0);
                                HexUtils.setShort(obj7, (short) 10, (short) computeAC);
                                obj7[12] = obj2;
                                System.arraycopy(obj4, 0, obj7, 13, 8);
                                cardRiskManagement = HexUtils.arrayFil(obj7, OFF_CDA_PADDING_BB, (short) ((byteArrayToInt - 38) - 25), (byte) -69);
                                System.arraycopy(HexUtils.hexStringToByteArray((String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_UN)), 0, obj7, (short) (((byteArrayToInt + 41) - 38) - 25), 4);
                                s = (short) (cardRiskManagement + 4);
                                Object obj8 = new byte[byteArrayToInt];
                                int sign = new SecureComponentImpl().sign(obj7, obj8, hexStringToByteArray);
                                if (sign < 0) {
                                    throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                                }
                                int length = (byteArrayToInt + 14) + obj.length;
                                if (byteArrayToInt > CertificateBody.profileType) {
                                    length += 2;
                                } else if (length - 2 > CertificateBody.profileType) {
                                    length++;
                                }
                                obj2 = new byte[length];
                                short s7 = (short) 1;
                                obj2[0] = ApplicationInfoManager.TERM_XP2;
                                if (obj2.length - 2 > CertificateBody.profileType) {
                                    short s8 = (short) (s7 + 1);
                                    obj2[s7] = TLVParser.BYTE_81;
                                    length = (short) (s8 + 1);
                                    obj2[s8] = (byte) (obj2.length - 3);
                                } else {
                                    length = (short) (s7 + 1);
                                    obj2[s7] = (byte) (obj2.length - 2);
                                }
                                System.arraycopy(obj6, 0, obj2, length, obj6.length);
                                s7 = HexUtils.setShort(obj2, (short) (length + obj6.length), (short) -24757);
                                if (byteArrayToInt > CertificateBody.profileType) {
                                    length = (short) (s7 + 1);
                                    obj2[s7] = TLVParser.BYTE_81;
                                } else {
                                    s = s7;
                                }
                                s7 = (short) (length + 1);
                                obj2[length] = (byte) sign;
                                System.arraycopy(obj8, 0, obj2, s7, sign);
                                tokenAPDUResponse.setBaApduResponse(obj2);
                            }
                            if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS)).byteValue() != -68) {
                                OperationStatus operationStatus;
                                OperationStatus operationStatus2 = OperationStatus.TXN_AMOUNT_HIGH;
                                if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS)).byteValue() == -45) {
                                    operationStatus = OperationStatus.TXN_AMOUNT_LOW;
                                } else {
                                    operationStatus = operationStatus2;
                                }
                                PaymentUtils.setOperationStatusInSession(operationStatus);
                            }
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.APPLICATION_CYPTOGRAM, HexUtils.byteArrayToHexString(obj4));
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.UNPREDICTABLE_NUMBER, (String) ApplicationInfoManager.getApplcationInfoValue(EMVConstants.TAG_UN));
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TID, TIDGenerator.getTransactionID(byteValue));
                            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_COMPLETED));
                            return tokenAPDUResponse;
                        }
                    }
                }
                throw new HCEClientException((short) ISO7816.SW_INCORRECT_P1P2);
            }
        } catch (HCEClientException e) {
            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_IDLE));
            tokenAPDUResponse.setsSW(e.getIsoSW());
        } catch (DigestException e2) {
            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_IDLE));
            tokenAPDUResponse.setsSW(ISO7816.SW_UNKNOWN);
        }
    }

    private short cardRiskManagement(byte[] bArr, byte b) {
        short s = Constants.CRM_NO_CONSTRAINT;
        if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TERMINAL_ONLINE_CAPABILITY)).shortValue() == Constants.MAGIC_FALSE) {
            s = (short) 4402;
        }
        if (117 != ((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue()) {
            return s;
        }
        if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_TXN_AMNT_STATUS)).byteValue() == -7 || ((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_ABORT_IN_GAC)).shortValue() == Constants.MAGIC_TRUE) {
            return (short) (s | 16576);
        }
        return s;
    }

    private byte[] buildIAD(byte b, byte b2, byte b3, short s) {
        int length;
        byte b4;
        Object obj;
        byte[] bArr;
        short s2;
        short s3;
        if (b == 83) {
            int i = 8;
            byte[] hexStringToByteArray = HexUtils.hexStringToByteArray((String) this.dataContext.getDgiMap().get(CPDLConfig.getDGI_TAG(CPDLConfig.ISSUER_APPLICATION_DATA_EMV_DGI)));
            if (hexStringToByteArray.length < 11) {
                throw new HCEClientException((short) ISO7816.SW_UNKNOWN);
            }
            byte[] bArr2;
            byte b5 = hexStringToByteArray[10];
            if (b5 == 1) {
                i = 27;
                if (hexStringToByteArray.length - 11 > 0) {
                    bArr2 = hexStringToByteArray;
                    length = (hexStringToByteArray.length - 11) + 27;
                    b4 = b5;
                    obj = 1;
                    bArr = bArr2;
                }
            }
            bArr2 = hexStringToByteArray;
            length = i;
            b4 = b5;
            obj = null;
            bArr = bArr2;
        } else {
            length = 7;
            b4 = (byte) 2;
            obj = null;
            bArr = null;
        }
        if (s == Constants.MAGIC_TRUE) {
            length += 3;
        }
        Object obj2 = new byte[length];
        if (s == Constants.MAGIC_TRUE) {
            s2 = HexUtils.setShort(obj2, (short) 0, StatesConstants.IAD_TAG);
            s3 = (short) (s2 + 1);
            obj2[s2] = (byte) (obj2.length - 3);
            length = (short) (s3 + 1);
            obj2[s3] = (byte) (obj2.length - 4);
        } else {
            length = (short) 1;
            obj2[0] = (byte) (obj2.length - 1);
        }
        s3 = (short) (length + 1);
        obj2[length] = b2;
        short s4 = (short) (s3 + 1);
        obj2[s3] = b3;
        System.arraycopy((byte[]) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_CVR), 0, obj2, s4, 4);
        s2 = (short) (s4 + 4);
        if (b == 83) {
            s3 = (short) (s2 + 1);
            obj2[s2] = b4;
            if (b4 == 1) {
                String startDate;
                short s5;
                NFCLUPCTagValue nFCLUPCTagValue = (NFCLUPCTagValue) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_NFC_LUPC_OBJ);
                if (nFCLUPCTagValue != null) {
                    startDate = nFCLUPCTagValue.getStartDate();
                } else {
                    startDate = MetaDataManager.getSKDDForATC(MetaDataManager.getMetaDataValue(MetaDataManager.RUNNING_ATC));
                }
                if (startDate != null) {
                    Object hexStringToByteArray2 = HexUtils.hexStringToByteArray(startDate);
                    s5 = (short) (s3 + 1);
                    obj2[s3] = (byte) hexStringToByteArray2.length;
                    System.arraycopy(hexStringToByteArray2, 0, obj2, s5, hexStringToByteArray2.length);
                    length = (short) (s5 + 16);
                } else {
                    length = (short) (s3 + 1);
                    obj2[s3] = (byte) 0;
                }
                s5 = (short) (length + 1);
                obj2[length] = (byte) 1;
                if (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_STATUS)).byteValue() == -102) {
                    switch (((Byte) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_MOB_CVM_TYPE)).byteValue()) {
                        case PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_CATEGORY /*-104*/:
                            length = (short) (s5 + 1);
                            obj2[s5] = (byte) 2;
                            break;
                        case ExtensionType.session_ticket /*35*/:
                            length = (short) (s5 + 1);
                            obj2[s5] = (byte) 1;
                            break;
                        case CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA /*70*/:
                            length = (short) (s5 + 1);
                            obj2[s5] = (byte) 5;
                            break;
                        case EACTags.FILE_REFERENCE /*81*/:
                            length = (short) (s5 + 1);
                            obj2[s5] = (byte) 3;
                            break;
                        default:
                            length = s5;
                            break;
                    }
                }
                length = (short) (s5 + 1);
                obj2[s5] = (byte) 0;
                if (obj != null) {
                    System.arraycopy(bArr, 11, obj2, length, bArr[11] + 1);
                }
            }
        }
        return obj2;
    }
}
