package com.americanexpress.mobilepayments.hceclient.payments.nfc.process;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
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
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.HexUtils;
import com.americanexpress.mobilepayments.hceclient.utils.tlv.framework.TagValue;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.samsung.android.spayfw.appinterface.ISO7816;

public class ApplicationSelectionProcess extends CommandProcess {
    private static final byte[] _baPPSE_AID;

    static {
        _baPPSE_AID = new byte[]{(byte) 50, GetTemplateApdu.TAG_APPLICATION_LABEL_50, (byte) 65, (byte) 89, (byte) 46, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 89, ApplicationInfoManager.TERMINAL_MODE_CL_EMV, (byte) 46, (byte) 68, (byte) 68, ApplicationInfoManager.MOB_CVM_TYP_DEV_FINGERPRINT, (byte) 48, (byte) 49};
    }

    public TokenAPDUResponse process(CommandAPDU commandAPDU) {
        TokenAPDUResponse tokenAPDUResponse = new TokenAPDUResponse();
        try {
            if ((commandAPDU.getbP2() & 2) == 2) {
                throw new HCEClientException((short) ISO7816.SW_FILE_NOT_FOUND);
            } else if (commandAPDU.getbP1() != 4 || commandAPDU.getbP2() != null) {
                throw new HCEClientException((short) ISO7816.SW_INCORRECT_P1P2);
            } else if (commandAPDU.getbLc() < 5 || commandAPDU.getbLc() > 16) {
                throw new HCEClientException((short) ISO7816.SW_WRONG_LENGTH);
            } else {
                PPSEResponse pPSEResponse = (PPSEResponse) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_CUR_PPSE_RES_OBJ);
                if (pPSEResponse == null) {
                    throw new HCEClientException((short) ISO7816.SW_CONDITIONS_NOT_SATISFIED);
                }
                byte[] pPSERes;
                if (HexUtils.secureCompare(commandAPDU.getBaCData(), (short) 0, _baPPSE_AID, (short) 0, (short) commandAPDU.getbLc()) == Constants.MAGIC_TRUE) {
                    pPSERes = pPSEResponse.getPPSERes();
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_PPSE_SELECTED));
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_APP, Short.valueOf(ApplicationInfoManager.APP_NONE));
                } else {
                    byte isAIDPresent = pPSEResponse.isAIDPresent(commandAPDU.getBaCData());
                    if (isAIDPresent == null) {
                        throw new HCEClientException((short) ISO7816.SW_FILE_NOT_FOUND);
                    }
                    pPSEResponse.getCurrentAID();
                    if (((byte) (isAIDPresent & -128)) == -128) {
                        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_APP, Short.valueOf(ApplicationInfoManager.APP_PRIMARY));
                        pPSERes = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(HCEClientConstants.PRIMARY_AID_FCI));
                    } else {
                        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_APP, Short.valueOf(ApplicationInfoManager.APP_ALIAS));
                        pPSERes = HexUtils.hexStringToByteArray(TagsMapUtil.getTagValue(HCEClientConstants.ALIAS_AID_FCI));
                    }
                    setPDOLInAppInfo();
                    StateMode tokenStatus = PaymentUtils.getTokenStatus();
                    if (StateMode.BLOCKED == tokenStatus || StateMode.TERMINATE == tokenStatus) {
                        tokenAPDUResponse.setsSW(ISO7816.SW_SELECTED_FILE_INVALIDATED);
                    }
                    ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_SELECTED));
                }
                tokenAPDUResponse.setBaApduResponse(pPSERes);
                return tokenAPDUResponse;
            }
        } catch (HCEClientException e) {
            ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.TR_APP_STATE, Short.valueOf(ApplicationInfoManager.STATE_APP_IDLE));
            if (e.getIsoSW() == (short) 0) {
                tokenAPDUResponse.setsSW(ISO7816.SW_UNKNOWN);
            } else {
                tokenAPDUResponse.setsSW(e.getIsoSW());
            }
        }
    }

    private void setPDOLInAppInfo() {
        TagValue tagValue;
        Object parseDOL;
        if (((Short) ApplicationInfoManager.getApplcationInfoValue(ApplicationInfoManager.TR_ACTIVE_APP)).shortValue() == ApplicationInfoManager.APP_ALIAS) {
            tagValue = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.PAYMENT_PARAMETERS_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PDOL_TAG), true);
        } else {
            tagValue = null;
        }
        if (tagValue == null) {
            tagValue = TagsMapUtil.getTagValue(this.dataContext.getTagMap(), CPDLConfig.getDGI_TAG(CPDLConfig.SELECT_AID_DGI), CPDLConfig.getDGI_TAG(CPDLConfig.PDOL_TAG), true);
        }
        if (tagValue != null) {
            parseDOL = DOLParser.parseDOL(tagValue.getValue());
        } else {
            parseDOL = null;
        }
        ApplicationInfoManager.setApplcationInfoValue(ApplicationInfoManager.PDOL_LIST, parseDOL);
    }
}
