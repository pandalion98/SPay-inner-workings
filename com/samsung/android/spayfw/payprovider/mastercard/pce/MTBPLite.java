package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.os.Bundle;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mcbp.core.mcbpcards.profile.RemotePaymentData;
import com.mastercard.mcbp.core.mpplite.states.CheckTable;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Date;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPContextFactory;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPTransactionContext;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramInput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPConstants;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable.TAProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.RemoteCryptogramResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.ReturnCode;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.TransactionOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUHandler;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUHandler.APDUCommand;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCAPDUBaseCommandHandler;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCommandResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCommandResult.TransitionState;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.MC_TA_ERRORS;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TASetContext.TASetContextResponse.SetContextOut;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.EnumSet;
import java.util.HashMap;
import org.bouncycastle.asn1.eac.EACTags;

public class MTBPLite {
    private static final int CRYPTOGRAM_MD_AC_LENGTH = 4;
    private static final int CRYPTOGRAM_MD_AC_OFFSET = 0;
    private static final int CRYPTOGRAM_UMD_AC_LENGTH = 4;
    private static final int CRYPTOGRAM_UMD_AC_OFFSET = 4;
    private static final int CVR_COMPARE_CIAC_LENGTH = 3;
    private static final int CVR_COMPARE_CIAC_OFFSET = 3;
    private static final String DEFAULT_ECI_INDICATOR = "5";
    private static final int IAD_CVR_OFFSET = 2;
    private static final int IAD_DAC_IDN_OFFSET = 8;
    private static final short IAD_DAC_IDN_VALUE = (short) 0;
    private static final int IAD_MD_AC_LENGTH = 5;
    private static final int IAD_MD_AC_OFFSET = 11;
    private static final String MC_CARD_TYPE = "mc";
    private static final byte PROCESS_CHECK_TABLE_MASK = (byte) 3;
    private static final String TAG = "mcpce_MTBPLite";
    private static final boolean UCAF_MODE_MCBP_V1 = false;
    private static HashMap<MTBPState, EnumSet<MTBPState>> mStateTransitionsMap;
    private MCAPDUHandler mAPDUcommandHandlerFactory;
    private MTBPState mCurrentState;
    McCardProfileDaoImpl<DC_CP> mDao;
    private MCBaseCardProfile<?> mPaymentProfile;
    private MTBPTransactionContext mTransactionContext;

    public enum MTBPState {
        STOPPED,
        READY,
        NFC_SELECTED,
        NFC_INITIATED,
        MST_INITIATED
    }

    static {
        mStateTransitionsMap = new HashMap();
        mStateTransitionsMap.put(MTBPState.STOPPED, EnumSet.of(MTBPState.READY));
        mStateTransitionsMap.put(MTBPState.READY, EnumSet.of(MTBPState.NFC_SELECTED, MTBPState.MST_INITIATED, MTBPState.STOPPED));
        mStateTransitionsMap.put(MTBPState.NFC_SELECTED, EnumSet.of(MTBPState.NFC_INITIATED, MTBPState.STOPPED, MTBPState.READY));
        mStateTransitionsMap.put(MTBPState.NFC_INITIATED, EnumSet.of(MTBPState.READY, MTBPState.STOPPED, MTBPState.NFC_SELECTED));
        mStateTransitionsMap.put(MTBPState.MST_INITIATED, EnumSet.of(MTBPState.READY, MTBPState.STOPPED));
    }

    public MTBPLite(McCardProfileDaoImpl<DC_CP> mcCardProfileDaoImpl) {
        this.mCurrentState = MTBPState.STOPPED;
        this.mDao = mcCardProfileDaoImpl;
    }

    private void setState(MTBPState mTBPState) {
        if (mTBPState != null && !mTBPState.equals(getState())) {
            if (((EnumSet) mStateTransitionsMap.get(this.mCurrentState)).contains(mTBPState)) {
                this.mCurrentState = mTBPState;
            } else {
                Log.m286e(TAG, "setState: wrong state requested, current state = " + this.mCurrentState.name() + ", requested state = " + mTBPState.name());
                throw new MCTransactionException(generateErrorResponse());
            }
        }
    }

    public boolean isReadyForTransaction() {
        return getState().equals(MTBPState.READY);
    }

    private MTBPState getState() {
        return this.mCurrentState;
    }

    public void initialize(MCBaseCardProfile<?> mCBaseCardProfile) {
        if (mCBaseCardProfile == null || mCBaseCardProfile.getTADataContainer() == null || mCBaseCardProfile.getDigitalizedCardContainer() == null) {
            throw new MCTransactionException("Current card profile is not valid for payment.");
        }
        this.mPaymentProfile = mCBaseCardProfile;
        setState(MTBPState.READY);
        Log.m287i(TAG, "Get current state: " + getState());
    }

    public void cancelPayment(MCTransactionResult mCTransactionResult, boolean z) {
        if (!this.mCurrentState.equals(MTBPState.STOPPED)) {
            if (MCTransactionResult.TRANSACTION_COMPLETED.equals(mCTransactionResult)) {
                this.mTransactionContext.getTransactionListener().onTransactionSuccess();
            } else {
                this.mTransactionContext.getTransactionListener().onTransactionCanceled(MCTransactionResult.ERROR_NFC_COMMAND_CANCELED);
            }
            setState(MTBPState.READY);
        }
        if (!z) {
            Log.m287i(TAG, "cancelPayment: skip update data");
        } else if (this.mTransactionContext.getTransactionCredentials().getmWrappedAtcObject() == null) {
            Log.m286e(TAG, "cancelPayment: update atc null!!!!");
        } else if (this.mDao != null && this.mPaymentProfile.getUniqueTokenReferenceId() >= 0) {
            Log.m287i(TAG, "cancelPayment: update atc object :");
            if (!this.mDao.updateWrappedAtcData(this.mTransactionContext.getTransactionCredentials().getmWrappedAtcObject(), this.mPaymentProfile.getUniqueTokenReferenceId())) {
                Log.m286e(TAG, "cancelPayment: update atc object failed !!!!");
            }
        }
    }

    public ByteArray processAPDU(ByteArray byteArray) {
        ByteArrayFactory instance = ByteArrayFactory.getInstance();
        byte b = byteArray.getByte(1);
        byte b2 = byteArray.getByte(CRYPTOGRAM_MD_AC_OFFSET);
        Log.m287i(TAG, "processAPDU: ins: " + McUtils.byteToHex(b) + " cla = " + McUtils.byteToHex(b2) + " : " + MCAPDUHandler.getApduName(b));
        APDUCommand commandHandlerByInstruction = this.mAPDUcommandHandlerFactory.getCommandHandlerByInstruction(b);
        if (commandHandlerByInstruction == null) {
            Log.m286e(TAG, "APDU command not found, apdu = " + b);
            return instance.getFromWord(27904);
        } else if (commandHandlerByInstruction.validateState(getState())) {
            try {
                MCCAPDUBaseCommandHandler aPDUHandler = commandHandlerByInstruction.getAPDUHandler();
                if (aPDUHandler.checkCLA(b2)) {
                    aPDUHandler.setTransactionContext((DC_CP) this.mPaymentProfile.getDigitalizedCardContainer(), this.mTransactionContext);
                    MCCommandResult processAPDU = aPDUHandler.processAPDU(byteArray);
                    if (TransitionState.TRANSITION_NEXT_STATE.equals(processAPDU.getTransitionState())) {
                        setState(commandHandlerByInstruction.getNextState());
                    } else if (TransitionState.TRANSITION_CANCEL_STATE.equals(processAPDU.getTransitionState())) {
                        cancelPayment(processAPDU.getResponseCode(), true);
                    }
                    return processAPDU.getResponseAPDU();
                }
                Log.m286e(TAG, "CLA is not supported for ins = " + b + ", cla = " + b2);
                return instance.getFromWord(28160);
            } catch (MCTransactionException e) {
                Log.m286e(TAG, "processAPDU: Unexpected MCTransactionException.");
                try {
                    cancelPayment(MCCommandResult.iso7816ToResponseCode(27013), false);
                } catch (MCTransactionException e2) {
                    Log.m286e(TAG, "processAPDU: error on cancel transaction.");
                    this.mCurrentState = MTBPState.STOPPED;
                    e2.printStackTrace();
                }
                e.printStackTrace();
                return instance.getFromWord(27013);
            }
        } else {
            Log.m286e(TAG, "Invalid state for the apdu: " + b + ", state " + getState());
            if (!(b == -88 && MTBPState.NFC_INITIATED.equals(getState()))) {
                try {
                    cancelPayment(MCTransactionResult.ERROR_NFC_COMMAND_INTERNAL_ERROR, false);
                } catch (MCTransactionException e3) {
                    Log.m286e(TAG, "processAPDU: error on cancel transaction.");
                    this.mCurrentState = MTBPState.READY;
                    e3.printStackTrace();
                }
            }
            return instance.getFromWord(27013);
        }
    }

    public void setTransactionContext(MCTransactionCredentials mCTransactionCredentials, MTBPTransactionListener mTBPTransactionListener) {
        Log.m287i(TAG, "Init factory state: " + getState());
        if (getState() == MTBPState.READY) {
            Log.m287i(MCTransactionService.TAG, "State MCPCEInitializedState, startContactLessPayment");
            if (!(this.mPaymentProfile.getDigitalizedCardContainer() instanceof DC_CP)) {
                Log.m286e(TAG, "Unknown payment profile.");
                throw new MCTransactionException(generateErrorResponse());
            } else if (((DC_CP) this.mPaymentProfile.getDigitalizedCardContainer()).getDC_CP_MPP().getContactlessPaymentData() == null) {
                Log.m286e(TAG, "NFC payment profile is not found.");
                throw new MCTransactionException(generateErrorResponse());
            } else if (mCTransactionCredentials == null || mCTransactionCredentials.getSecureObject() == null) {
                Log.m286e(TAG, "Credentials is null: " + mCTransactionCredentials);
                throw new MCTransactionException(generateErrorResponse());
            } else {
                this.mTransactionContext = MTBPContextFactory.getContext(mCTransactionCredentials);
                if (this.mTransactionContext.getTransactionInformation() == null) {
                    Log.m286e(TAG, "Transaction information is null");
                    throw new MCTransactionException(generateErrorResponse());
                }
                this.mTransactionContext.setTransactionListener(mTBPTransactionListener);
                this.mAPDUcommandHandlerFactory = new MCAPDUHandler();
                return;
            }
        }
        Log.m286e(TAG, "Wrong state for state transaction context, state: " + getState() + "; expected state: " + MTBPState.READY);
        throw new MCTransactionException(generateErrorResponse());
    }

    public void cleanTransactionContext() {
        if (MTBPState.READY.equals(getState()) || MTBPState.READY.equals(MTBPState.STOPPED)) {
            this.mTransactionContext.clearCredentials();
        }
    }

    public Bundle stopNfc() {
        if (this.mTransactionContext == null) {
            return null;
        }
        return this.mTransactionContext.stopNfc();
    }

    public long initializeMST() {
        if (!MTBPState.READY.equals(getState())) {
            Log.m286e(TAG, "Wrong state on MST transaction init, required state: " + MTBPState.READY + ", current state: " + getState());
            return 1;
        } else if (this.mPaymentProfile == null || this.mPaymentProfile.getTADataContainer() == null) {
            Log.m286e(TAG, "MC Card profile doesn't support MST transaction.");
            return 2;
        } else if (this.mTransactionContext == null || this.mTransactionContext.getTransactionCredentials() == null) {
            Log.m286e(TAG, "MC transaction context is not initialized for MST transaction.");
            return 1;
        } else {
            try {
                McTAController instance = McTAController.getInstance();
                SetContextOut context = instance.setContext(this.mTransactionContext.getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_MST_TA_GPO));
                if (context == null || context._atc == null || context._wrapped_atc_obj == null) {
                    Log.m286e(TAG, "Wrong MC TA response on  for MST transaction.");
                    return 2;
                }
                byte[] data = context._atc.getData();
                if (data == null) {
                    Log.m286e(TAG, "Wrong ATC value returned from MC TA.");
                    return 2;
                }
                Log.m287i(TAG, "ATC=" + McUtils.byteArrayToHex(data));
                byte[] data2 = context._wrapped_atc_obj.getData();
                if (data2 == null) {
                    Log.m286e(TAG, "Wrong TA profile returned from MC TA.");
                    return 2;
                }
                this.mTransactionContext.getTransactionCredentials().setATC(data);
                this.mTransactionContext.getTransactionCredentials().setmWrappedAtcObject(data2);
                this.mPaymentProfile.setTaAtcContainer(data2);
                if (this.mDao != null && this.mPaymentProfile.getUniqueTokenReferenceId() > 0) {
                    Log.m287i(TAG, "initializeMST(): update profile");
                    if (!this.mDao.updateWrappedAtcData(this.mTransactionContext.getTransactionCredentials().getmWrappedAtcObject(), this.mPaymentProfile.getUniqueTokenReferenceId())) {
                        Log.m286e(TAG, "initializeMST: failed update ta object in the db.");
                        return 1;
                    }
                }
                long prepareMSTtracks = instance.prepareMSTtracks((Utils.am(McProvider.getContext()) / 60000) % 100000);
                if (prepareMSTtracks == ((long) MC_TA_ERRORS.NO_ERROR.ordinal())) {
                    return 0;
                }
                Log.m286e(TAG, "prepare MST tracks failed in MC TA, return code " + prepareMSTtracks);
                return 1;
            } catch (Exception e) {
                Log.m286e(TAG, "Failed to call MC TA for MST init, transit state to " + MTBPState.STOPPED);
                try {
                    setState(MTBPState.STOPPED);
                } catch (MCTransactionException e2) {
                    Log.m286e(TAG, "Failed to transit to the STOPPED state.");
                    this.mCurrentState = MTBPState.STOPPED;
                    e2.printStackTrace();
                }
                e.printStackTrace();
                return 1;
            }
        }
    }

    public DSRPOutputData generateDSRPData(DSRPInputData dSRPInputData) {
        if (dSRPInputData == null || !isDsrpInputValid(dSRPInputData)) {
            return null;
        }
        ByteArrayFactory instance = ByteArrayFactory.getInstance();
        McTAController mcTAController = null;
        CryptogramInput cryptogramInput = new CryptogramInput();
        long transactionAmount = dSRPInputData.getTransactionAmount();
        long otherAmount = dSRPInputData.getOtherAmount();
        long currencyCode = dSRPInputData.getCurrencyCode();
        int countryCode = dSRPInputData.getCountryCode();
        Date transactionDate = dSRPInputData.getTransactionDate();
        try {
            mcTAController = McTAController.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mcTAController == null) {
            Log.m286e(TAG, "DSRP: internall error, MC TA isn't loaded.");
            return null;
        }
        cryptogramInput.setOnlineAllowed(true);
        cryptogramInput.setTVR(instance.getByteArray(IAD_MD_AC_LENGTH));
        cryptogramInput.setCryptoGramType(dSRPInputData.getCryptogramType());
        cryptogramInput.setAmountAuthorized(instance.getByteArray(com.mastercard.mobile_api.utils.Utils.longToBCD(transactionAmount, 6), 6));
        cryptogramInput.setAmountOther(instance.getByteArray(com.mastercard.mobile_api.utils.Utils.longToBCD(otherAmount, 6), 6));
        cryptogramInput.setTerminalCountryCode(instance.getByteArray(com.mastercard.mobile_api.utils.Utils.longToBCD((long) countryCode, IAD_CVR_OFFSET), IAD_CVR_OFFSET));
        cryptogramInput.setTrxCurrencyCode(instance.getByteArray(com.mastercard.mobile_api.utils.Utils.longToBCD(currencyCode, IAD_CVR_OFFSET), IAD_CVR_OFFSET));
        cryptogramInput.setTrxDate(getDateAsByteArray(transactionDate));
        ByteArray byteArray = instance.getByteArray(CRYPTOGRAM_UMD_AC_OFFSET);
        com.mastercard.mobile_api.utils.Utils.writeInt(byteArray, CRYPTOGRAM_MD_AC_OFFSET, dSRPInputData.getUnpredictableNumber());
        cryptogramInput.setUnpredictableNumber(byteArray);
        ByteArray byteArray2 = instance.getByteArray(1);
        byteArray2.setByte(CRYPTOGRAM_MD_AC_OFFSET, dSRPInputData.getTransactionType());
        cryptogramInput.setTrxType(byteArray2);
        transactionAmount = dSRPInputData.getUnpredictableNumber();
        Log.m285d(TAG, "Unpredictable number: " + transactionAmount);
        if (transactionAmount == 0) {
            cryptogramInput.setUnpredictableNumber(getDsrpUn(mcTAController));
        }
        Log.m285d(TAG, "UN before createRemote Cryptogram : " + cryptogramInput.getUnpredictableNumber().getHexString());
        RemoteCryptogramResult createRemoteCryptogram = createRemoteCryptogram(cryptogramInput);
        if (createRemoteCryptogram.getCode() != ReturnCode.OK) {
            Log.m286e(TAG, "DSRP: createRemoteCryptogram failure cause : " + createRemoteCryptogram.getCode());
            return null;
        }
        TransactionOutput output = createRemoteCryptogram.getOutput();
        if (output == null) {
            Log.m286e(TAG, "DSRP: TransactionOupt is null ");
            return null;
        } else if (output.getCryptoGram().getCid() != -128) {
            Log.m286e(TAG, "DSRP: A wrong cid in DsrpOutput, cid = " + output.getCryptoGram().getCid());
            return null;
        } else if (dSRPInputData.getCryptogramType() != CryptogramType.UCAF || output.getPANSequenceNumber().getByte(CRYPTOGRAM_MD_AC_OFFSET) <= 9) {
            DSRPOutputData dSRPOutputData = new DSRPOutputData();
            String hexString = output.getPAN().getHexString();
            dSRPOutputData.setCryptoGramType(dSRPInputData.getCryptogramType());
            dSRPOutputData.setTransactionAmount(dSRPInputData.getTransactionAmount());
            dSRPOutputData.setCurrencyCode((int) dSRPInputData.getCurrencyCode());
            dSRPOutputData.setUcafVersion(CRYPTOGRAM_MD_AC_OFFSET);
            dSRPOutputData.setUnpredictableNumber(cryptogramInput.getUnpredictableNumber().getBytes());
            dSRPOutputData.setPan(hexString.replaceAll("F", BuildConfig.FLAVOR));
            dSRPOutputData.setExpiryDate(output.getExpiryDate().getBytes());
            dSRPOutputData.setPanSequenceNumber(output.getPANSequenceNumber().getByte(CRYPTOGRAM_MD_AC_OFFSET));
            dSRPOutputData.setAtc(com.mastercard.mobile_api.utils.Utils.readInt(output.getCryptoGram().getATC().getBytes(), CRYPTOGRAM_MD_AC_OFFSET));
            dSRPOutputData.setCryptoGram(createRemoteCryptogram.getOutput().getCryptoGram().getCryptogram().getBytes());
            if (dSRPInputData.getCryptogramType() == CryptogramType.UCAF) {
                dSRPOutputData.setTransactionCryptogramData(buildUCAF(cryptogramInput, output).getBytes());
            } else {
                dSRPOutputData.setTransactionCryptogramData(buildDE55(cryptogramInput, output).getBytes());
            }
            if (output.getPar() == null || output.getPar().getBytes() == null) {
                return dSRPOutputData;
            }
            Log.m285d(TAG, "par in dsrpout data :" + output.getPar().getHexString());
            dSRPOutputData.setPar(output.getPar().getBytes());
            return dSRPOutputData;
        } else {
            Log.m286e(TAG, "DSRP:  A wrong PANSN for UCAF ");
            return null;
        }
    }

    private boolean isDsrpInputValid(DSRPInputData dSRPInputData) {
        long transactionAmount = dSRPInputData.getTransactionAmount();
        if (transactionAmount < 0 || transactionAmount > DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
            Log.m286e(TAG, "DSRP: wrong transaction amount input : " + transactionAmount);
            return false;
        }
        transactionAmount = dSRPInputData.getOtherAmount();
        if (transactionAmount < 0 || transactionAmount > DSRPConstants.DSRP_INPUT_AMOUNT_MAX) {
            Log.m286e(TAG, "DSRP: wrong other amount input : " + transactionAmount);
            return false;
        }
        transactionAmount = dSRPInputData.getCurrencyCode();
        if (transactionAmount < 0 || transactionAmount > 999) {
            Log.m286e(TAG, "DSRP: wrong currency code input : " + transactionAmount);
            return false;
        }
        byte transactionType = dSRPInputData.getTransactionType();
        if (transactionType < null || transactionType > 99) {
            Log.m286e(TAG, "DSRP: wrong transaction type : " + transactionType);
            return false;
        }
        int countryCode = dSRPInputData.getCountryCode();
        if (countryCode < 0 || countryCode > DSRPConstants.DSRP_INPUT_CURRENCY_CODE_MAX) {
            Log.m286e(TAG, "DSRP: wrong transaction country code : " + countryCode);
            return false;
        }
        Date transactionDate = dSRPInputData.getTransactionDate();
        if (transactionDate == null) {
            Log.m286e(TAG, "DSRP: transacion date is null");
            return false;
        } else if (transactionDate.getDay() < 1 || transactionDate.getDay() > 31 || transactionDate.getMonth() < 1 || transactionDate.getMonth() > 12 || transactionDate.getYear() < 2000) {
            Log.m286e(TAG, "DSRP: wrong transacion date : " + transactionDate.toString());
            return false;
        } else if (dSRPInputData.getCryptogramType() == CryptogramType.DE55 || dSRPInputData.getCryptogramType() == CryptogramType.UCAF) {
            return true;
        } else {
            Log.m286e(TAG, "DSRP: Unknown cryptogram type: " + dSRPInputData.getCryptogramType());
            return false;
        }
    }

    private ByteArray getDsrpUn(McTAController mcTAController) {
        byte[] generateUN = mcTAController.generateUN();
        if (generateUN != null && generateUN.length == CRYPTOGRAM_UMD_AC_OFFSET) {
            return ByteArrayFactory.getInstance().getByteArray(generateUN, generateUN.length);
        }
        Log.m286e(TAG, "DSRP: internall error, MC TA returns incorrect un.");
        return null;
    }

    private static ByteArray buildDE55(CryptogramInput cryptogramInput, TransactionOutput transactionOutput) {
        ByteArrayFactory instance = ByteArrayFactory.getInstance();
        ByteArray create = TLV.create(instance.getFromWord(-24794), transactionOutput.getCryptoGram().getCryptogram());
        create.append(TLV.create(instance.getFromWord(-24816), transactionOutput.getCryptoGram().getIssuerApplicationData()));
        create.append(TLV.create(instance.getFromWord(-24778), transactionOutput.getCryptoGram().getATC()));
        create.append(TLV.create((byte) -107, cryptogramInput.getTVR()));
        ByteArray byteArray = instance.getByteArray(1);
        byteArray.setByte(CRYPTOGRAM_MD_AC_OFFSET, transactionOutput.getCryptoGram().getCid());
        create.append(TLV.create(instance.getFromWord(-24793), byteArray));
        byteArray = instance.getByteArray(CVR_COMPARE_CIAC_OFFSET);
        byteArray.setByte(IAD_CVR_OFFSET, (byte) 2);
        if (cryptogramInput.isCVM_Entered()) {
            byteArray.setByte(CRYPTOGRAM_MD_AC_OFFSET, (byte) 1);
        } else {
            byteArray.setByte(CRYPTOGRAM_MD_AC_OFFSET, (byte) 63);
        }
        create.append(TLV.create(instance.getFromWord(-24780), byteArray));
        create.append(TLV.create(instance.getFromWord(-24777), cryptogramInput.getUnpredictableNumber()));
        create.append(TLV.create(instance.getFromWord(-24830), cryptogramInput.getAmountAuthorized()));
        create.append(TLV.create(instance.getFromWord(-24829), cryptogramInput.getAmountOther()));
        create.append(TLV.create(instance.getFromWord(EACTags.CURRENCY_CODE), cryptogramInput.getTrxCurrencyCode()));
        create.append(TLV.create((byte) ApplicationInfoManager.MOB_CVM_PERFORMED, cryptogramInput.getTrxDate()));
        create.append(TLV.create((byte) -100, cryptogramInput.getTrxType()));
        create.append(TLV.create((byte) 90, transactionOutput.getPAN()));
        create.append(TLV.create(instance.getFromWord(EACTags.CARD_SEQUENCE_NUMBER), transactionOutput.getPANSequenceNumber()));
        create.append(TLV.create(instance.getFromWord(24356), transactionOutput.getExpiryDate()));
        create.append(TLV.create(instance.getFromWord(-24806), cryptogramInput.getTerminalCountryCode()));
        create.append(TLV.create((byte) EMVSetStatusApdu.RESET_LOWEST_PRIORITY, transactionOutput.getAIP()));
        return create;
    }

    private static ByteArray buildUCAF(CryptogramInput cryptogramInput, TransactionOutput transactionOutput) {
        ByteArrayFactory instance = ByteArrayFactory.getInstance();
        ByteArray byteArray = instance.getByteArray(1);
        byteArray.setByte(CRYPTOGRAM_MD_AC_OFFSET, (byte) (transactionOutput.getPANSequenceNumber().getByte(CRYPTOGRAM_MD_AC_OFFSET) & 15));
        byteArray.append(transactionOutput.getCryptoGram().getCryptogram());
        byteArray.append(transactionOutput.getCryptoGram().getATC());
        byteArray.append(cryptogramInput.getUnpredictableNumber());
        byteArray.append(transactionOutput.getAIP());
        byteArray.appendByte(transactionOutput.getCryptoGram().getIssuerApplicationData().getByte(CRYPTOGRAM_MD_AC_OFFSET));
        byteArray.appendByte(transactionOutput.getCryptoGram().getIssuerApplicationData().getByte(1));
        Log.m285d(TAG, "UCAF input: " + byteArray.getHexString());
        String hexStringToBase64 = instance.hexStringToBase64(byteArray.getHexString());
        Log.m285d(TAG, "UCAF encoded: " + hexStringToBase64);
        return instance.getByteArray(hexStringToBase64.getBytes(), hexStringToBase64.length());
    }

    private RemoteCryptogramResult createRemoteCryptogram(CryptogramInput cryptogramInput) {
        if (cryptogramInput == null) {
            return new RemoteCryptogramResult(ReturnCode.ERROR_INVALID_INPUT, null);
        }
        DC_CP_MPP dc_cp_mpp;
        RemotePaymentData remotePaymentData;
        if (this.mPaymentProfile.getDigitalizedCardContainer() instanceof DC_CP) {
            dc_cp_mpp = ((DC_CP) this.mPaymentProfile.getDigitalizedCardContainer()).getDC_CP_MPP();
            if (dc_cp_mpp == null) {
                Log.m286e(TAG, "DSRP: internall error, can't get provisioned card profile, null.");
                return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
            }
            remotePaymentData = dc_cp_mpp.getRemotePaymentData();
        } else {
            dc_cp_mpp = null;
            remotePaymentData = null;
        }
        if (dc_cp_mpp == null) {
            Log.m286e(TAG, "DSRP: internall error, provisioned card profile is null.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        } else if (remotePaymentData == null) {
            Log.m286e(TAG, "DSRP: internall error, Remote Payment data is null.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        } else {
            McTAController instance;
            ByteArrayFactory instance2 = ByteArrayFactory.getInstance();
            TransactionOutput initializeCryptoOutput = initializeCryptoOutput(remotePaymentData);
            ByteArray clone = remotePaymentData.getCVR_MaskAnd().clone();
            Log.m285d(TAG, "CVR mask: " + clone.getHexString());
            byte b = clone.getByte(CVR_COMPARE_CIAC_OFFSET);
            if (cryptogramInput.getTerminalCountryCode() == null || !cryptogramInput.getTerminalCountryCode().isEqual(dc_cp_mpp.getCardRiskManagementData().getCRM_CountryCode())) {
                clone.setByte(CVR_COMPARE_CIAC_OFFSET, (byte) (b | CRYPTOGRAM_UMD_AC_OFFSET));
            } else {
                clone.setByte(CVR_COMPARE_CIAC_OFFSET, (byte) (b | IAD_CVR_OFFSET));
            }
            if (!(((byte) (remotePaymentData.getCVR_MaskAnd().getByte(IAD_CVR_OFFSET) & CVR_COMPARE_CIAC_OFFSET)) == null && ((byte) (remotePaymentData.getCIAC_Decline().getByte(IAD_CVR_OFFSET) & CVR_COMPARE_CIAC_OFFSET)) == null)) {
                CheckTable.processAddCheckTable(buildCDOL(cryptogramInput), dc_cp_mpp.getCardRiskManagementData().getAdditionalCheckTable(), clone);
            }
            if (this.mTransactionContext.getTransactionCredentials().getCVMResult().getResultCode() == 0) {
                clone.setByte(CRYPTOGRAM_MD_AC_OFFSET, (byte) (clone.getByte(CRYPTOGRAM_MD_AC_OFFSET) | IAD_MD_AC_LENGTH));
            }
            Log.m285d(TAG, "CVR: " + clone.getHexString());
            clone.setByte(CVR_COMPARE_CIAC_OFFSET, (byte) (clone.getByte(CVR_COMPARE_CIAC_OFFSET) | 64));
            if (com.mastercard.mobile_api.utils.Utils.isZero(remotePaymentData.getCIAC_Decline().bitWiseAnd(clone.copyOfRange(CVR_COMPARE_CIAC_OFFSET, 6)))) {
                clone.setByte(CRYPTOGRAM_MD_AC_OFFSET, (byte) (clone.getByte(CRYPTOGRAM_MD_AC_OFFSET) | -96));
                initializeCryptoOutput.getCryptoGram().setCid(VerifyPINApdu.P2_PLAINTEXT);
            } else {
                clone.setByte(CRYPTOGRAM_MD_AC_OFFSET, (byte) (clone.getByte(CRYPTOGRAM_MD_AC_OFFSET) | -128));
                initializeCryptoOutput.getCryptoGram().setCid((byte) 0);
            }
            Log.m285d(TAG, "Mask: " + remotePaymentData.getCVR_MaskAnd().getHexString());
            clone = clone.bitWiseAnd(remotePaymentData.getCVR_MaskAnd());
            ByteArray buildCDOL = buildCDOL(cryptogramInput);
            try {
                instance = McTAController.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
                Log.m286e(TAG, "GPO processCommand: cannot initiate MC TA. Unexpected TA exception.");
                instance = null;
            }
            if (instance == null) {
                Log.m286e(TAG, "DSRP: internall error, MC TA isn't loaded.");
                return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
            }
            SetContextOut context = instance.setContext(this.mTransactionContext.getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_DSRP_TA_GPO));
            if (context == null || context._atc == null || context._wrapped_atc_obj == null || context._iccdn == null) {
                return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
            }
            byte[] data = context._atc.getData();
            if (data == null || data.length != IAD_CVR_OFFSET) {
                Log.m286e(TAG, "DSRP: wrong ATC length.");
                return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
            }
            byte[] data2 = context._wrapped_atc_obj.getData();
            if (data2 == null) {
                Log.m286e(TAG, "DSRP: Wrong TA profile returned from MC TA.");
                return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
            }
            initializeCryptoOutput.getCryptoGram().setATC(instance2.getByteArray(data, data.length));
            this.mTransactionContext.getTransactionCredentials().setmWrappedAtcObject(data2);
            Log.m287i(TAG, "ATC=" + initializeCryptoOutput.getCryptoGram().getATC().getHexString());
            byte tAProfileReference = this.mTransactionContext.getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_DSRP_TA_GAC_ONLINE_CVM);
            buildCDOL = buildCDOL.copyOfRange(CRYPTOGRAM_MD_AC_OFFSET, 29);
            ByteArray copyOfRange = clone.copyOfRange(1, clone.getLength());
            Log.m285d(TAG, "Input Data1: " + buildCDOL.getHexString());
            Log.m285d(TAG, "Input Data2: " + copyOfRange.getHexString());
            byte[] generateMAC = instance.generateMAC(tAProfileReference, buildCDOL.getBytes(), copyOfRange.getBytes(), cryptogramInput.getUnpredictableNumber().getBytes());
            if (generateMAC == null || generateMAC.length != IAD_DAC_IDN_OFFSET) {
                Log.m286e(TAG, "DSRP: wrong cryptogram.");
                return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
            }
            Log.m287i(TAG, "Cryptogram: generated cryptogram...");
            initializeCryptoOutput.getCryptoGram().setCryptoOutput(generateMAC);
            initializeCryptoOutput.getCryptoGram().setCid(VerifyPINApdu.P2_PLAINTEXT);
            ByteArray clone2 = remotePaymentData.getIssuerApplicationData().clone();
            clone2.copyBytes(clone, CRYPTOGRAM_MD_AC_OFFSET, IAD_CVR_OFFSET, clone.getLength());
            clone2.setShort(IAD_DAC_IDN_OFFSET, IAD_DAC_IDN_VALUE);
            initializeCryptoOutput.getCryptoGram().setIssuerApplicationData(clone2);
            initializeCryptoOutput.setExpiryDate(remotePaymentData.getApplicationExpiryDate());
            initializeCryptoOutput.setPAN(remotePaymentData.getPAN());
            initializeCryptoOutput.setPANSequenceNumber(remotePaymentData.getPAN_SequenceNumber());
            Log.m285d(TAG, "PAR setting here : " + remotePaymentData.getPaymentAccountReference());
            initializeCryptoOutput.setPar(remotePaymentData.getPaymentAccountReference());
            try {
                cancelPayment(MCTransactionResult.TRANSACTION_COMPLETED, true);
            } catch (Throwable e2) {
                Log.m284c(TAG, "Unexpected exception during inapp update: " + e2.toString(), e2);
            }
            return new RemoteCryptogramResult(ReturnCode.OK, initializeCryptoOutput);
        }
    }

    private ByteArray buildCDOL(CryptogramInput cryptogramInput) {
        int i;
        ByteArray byteArray;
        ByteArrayFactory instance = ByteArrayFactory.getInstance();
        if (cryptogramInput.getCryptoGramType() == CryptogramType.UCAF) {
            i = 1;
        } else {
            i = CRYPTOGRAM_MD_AC_OFFSET;
        }
        if (i != 0 || cryptogramInput.getAmountAuthorized() == null) {
            byteArray = instance.getByteArray(6);
        } else {
            byteArray = cryptogramInput.getAmountAuthorized().clone();
        }
        if (i != 0 || cryptogramInput.getAmountOther() == null) {
            byteArray.append(instance.getByteArray(6));
        } else {
            byteArray.append(cryptogramInput.getAmountOther());
        }
        if (i != 0 || cryptogramInput.getTerminalCountryCode() == null) {
            byteArray.append(instance.getByteArray(IAD_CVR_OFFSET));
        } else {
            byteArray.append(cryptogramInput.getTerminalCountryCode());
        }
        byteArray.append(instance.getByteArray(IAD_MD_AC_LENGTH));
        if (i != 0 || cryptogramInput.getTrxCurrencyCode() == null) {
            byteArray.append(instance.getByteArray(IAD_CVR_OFFSET));
        } else {
            byteArray.append(cryptogramInput.getTrxCurrencyCode());
        }
        if (i != 0 || cryptogramInput.getTrxDate() == null) {
            byteArray.append(instance.getByteArray(CVR_COMPARE_CIAC_OFFSET));
        } else {
            byteArray.append(cryptogramInput.getTrxDate());
        }
        if (i != 0 || cryptogramInput.getTrxType() == null) {
            byteArray.append(instance.getByteArray(1));
        } else {
            byteArray.append(cryptogramInput.getTrxType());
        }
        if (cryptogramInput.getUnpredictableNumber() == null) {
            byteArray.append(instance.getByteArray(CRYPTOGRAM_UMD_AC_OFFSET));
        } else {
            byteArray.append(cryptogramInput.getUnpredictableNumber());
        }
        Log.m285d(TAG, "UN Generated CDOL : " + byteArray.getHexString());
        return byteArray;
    }

    private TransactionOutput initializeCryptoOutput(RemotePaymentData remotePaymentData) {
        TransactionOutput transactionOutput = new TransactionOutput();
        transactionOutput.setPAN(remotePaymentData.getPAN().clone());
        Log.m285d(TAG, "PAN: " + remotePaymentData.getPAN().getHexString());
        transactionOutput.setPANSequenceNumber(remotePaymentData.getPAN_SequenceNumber().clone());
        Log.m285d(TAG, "PAN SN: " + remotePaymentData.getPAN_SequenceNumber().getHexString());
        transactionOutput.setCVMEntered(true);
        transactionOutput.getCryptoGram().setIssuerApplicationData(remotePaymentData.getIssuerApplicationData().clone());
        Log.m285d(TAG, "IAD: " + remotePaymentData.getIssuerApplicationData().getHexString());
        transactionOutput.setAIP(remotePaymentData.getAIP());
        Log.m285d(TAG, "getCVR_MaskAnd: " + remotePaymentData.getCVR_MaskAnd().getHexString());
        Log.m285d(TAG, "getIssuerApplicationData: " + remotePaymentData.getIssuerApplicationData().getHexString());
        Log.m285d(TAG, "getApplicationExpiryDate: " + remotePaymentData.getApplicationExpiryDate().getHexString());
        Log.m285d(TAG, "getCIAC_Decline: " + remotePaymentData.getCIAC_Decline().getHexString());
        return transactionOutput;
    }

    public static ByteArray getDateAsByteArray(Date date) {
        String str = BuildConfig.FLAVOR;
        if (date.getYear() < 2010) {
            str = str + TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
        }
        str = str + (date.getYear() % 2000);
        if (date.getMonth() < 10) {
            str = str + TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
        }
        str = str + BuildConfig.FLAVOR + date.getMonth();
        if (date.getDay() < 10) {
            str = str + TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
        }
        return ByteArrayFactory.getInstance().getByteArray(com.mastercard.mobile_api.utils.Utils.readHexString(str + date.getDay()), CVR_COMPARE_CIAC_OFFSET);
    }

    public ByteArray generateErrorResponse() {
        return null;
    }
}
