/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.EnumSet
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.os.Bundle;
import com.mastercard.mcbp.core.mcbpcards.profile.CardRiskManagementData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mcbp.core.mcbpcards.profile.RemotePaymentData;
import com.mastercard.mcbp.core.mpplite.states.CheckTable;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Date;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPTransactionListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPContextFactory;
import com.samsung.android.spayfw.payprovider.mastercard.pce.context.MTBPTransactionContext;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramInput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.CryptogramType;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.RemoteCryptogramResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.ReturnCode;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.TransactionOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCAPDUHandler;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCAPDUBaseCommandHandler;
import com.samsung.android.spayfw.payprovider.mastercard.pce.nfc.MCCommandResult;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.McUtils;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.Blob;
import java.util.EnumSet;
import java.util.HashMap;

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
    private static final short IAD_DAC_IDN_VALUE = 0;
    private static final int IAD_MD_AC_LENGTH = 5;
    private static final int IAD_MD_AC_OFFSET = 11;
    private static final String MC_CARD_TYPE = "mc";
    private static final byte PROCESS_CHECK_TABLE_MASK = 3;
    private static final String TAG = "mcpce_MTBPLite";
    private static final boolean UCAF_MODE_MCBP_V1;
    private static HashMap<MTBPState, EnumSet<MTBPState>> mStateTransitionsMap;
    private MCAPDUHandler mAPDUcommandHandlerFactory;
    private MTBPState mCurrentState = MTBPState.STOPPED;
    McCardProfileDaoImpl<DC_CP> mDao;
    private MCBaseCardProfile<?> mPaymentProfile;
    private MTBPTransactionContext mTransactionContext;

    static {
        mStateTransitionsMap = new HashMap();
        mStateTransitionsMap.put((Object)MTBPState.STOPPED, (Object)EnumSet.of((Enum)MTBPState.READY));
        mStateTransitionsMap.put((Object)MTBPState.READY, (Object)EnumSet.of((Enum)MTBPState.NFC_SELECTED, (Enum)MTBPState.MST_INITIATED, (Enum)MTBPState.STOPPED));
        mStateTransitionsMap.put((Object)MTBPState.NFC_SELECTED, (Object)EnumSet.of((Enum)MTBPState.NFC_INITIATED, (Enum)MTBPState.STOPPED, (Enum)MTBPState.READY));
        mStateTransitionsMap.put((Object)MTBPState.NFC_INITIATED, (Object)EnumSet.of((Enum)MTBPState.READY, (Enum)MTBPState.STOPPED, (Enum)MTBPState.NFC_SELECTED));
        mStateTransitionsMap.put((Object)MTBPState.MST_INITIATED, (Object)EnumSet.of((Enum)MTBPState.READY, (Enum)MTBPState.STOPPED));
    }

    public MTBPLite(McCardProfileDaoImpl<DC_CP> mcCardProfileDaoImpl) {
        this.mDao = mcCardProfileDaoImpl;
    }

    /*
     * Enabled aggressive block sorting
     */
    private ByteArray buildCDOL(CryptogramInput cryptogramInput) {
        ByteArrayFactory byteArrayFactory = ByteArrayFactory.getInstance();
        boolean bl = cryptogramInput.getCryptoGramType() == CryptogramType.UCAF;
        ByteArray byteArray = bl || cryptogramInput.getAmountAuthorized() == null ? byteArrayFactory.getByteArray(6) : cryptogramInput.getAmountAuthorized().clone();
        if (bl || cryptogramInput.getAmountOther() == null) {
            byteArray.append(byteArrayFactory.getByteArray(6));
        } else {
            byteArray.append(cryptogramInput.getAmountOther());
        }
        if (bl || cryptogramInput.getTerminalCountryCode() == null) {
            byteArray.append(byteArrayFactory.getByteArray(2));
        } else {
            byteArray.append(cryptogramInput.getTerminalCountryCode());
        }
        byteArray.append(byteArrayFactory.getByteArray(5));
        if (bl || cryptogramInput.getTrxCurrencyCode() == null) {
            byteArray.append(byteArrayFactory.getByteArray(2));
        } else {
            byteArray.append(cryptogramInput.getTrxCurrencyCode());
        }
        if (bl || cryptogramInput.getTrxDate() == null) {
            byteArray.append(byteArrayFactory.getByteArray(3));
        } else {
            byteArray.append(cryptogramInput.getTrxDate());
        }
        if (bl || cryptogramInput.getTrxType() == null) {
            byteArray.append(byteArrayFactory.getByteArray(1));
        } else {
            byteArray.append(cryptogramInput.getTrxType());
        }
        if (cryptogramInput.getUnpredictableNumber() == null) {
            byteArray.append(byteArrayFactory.getByteArray(4));
        } else {
            byteArray.append(cryptogramInput.getUnpredictableNumber());
        }
        c.d(TAG, "UN Generated CDOL : " + byteArray.getHexString());
        return byteArray;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ByteArray buildDE55(CryptogramInput cryptogramInput, TransactionOutput transactionOutput) {
        ByteArrayFactory byteArrayFactory = ByteArrayFactory.getInstance();
        ByteArray byteArray = TLV.create(byteArrayFactory.getFromWord(-24794), transactionOutput.getCryptoGram().getCryptogram());
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24816), transactionOutput.getCryptoGram().getIssuerApplicationData()));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24778), transactionOutput.getCryptoGram().getATC()));
        byteArray.append(TLV.create((byte)-107, cryptogramInput.getTVR()));
        ByteArray byteArray2 = byteArrayFactory.getByteArray(1);
        byteArray2.setByte(0, transactionOutput.getCryptoGram().getCid());
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24793), byteArray2));
        ByteArray byteArray3 = byteArrayFactory.getByteArray(3);
        byteArray3.setByte(2, (byte)2);
        if (cryptogramInput.isCVM_Entered()) {
            byteArray3.setByte(0, (byte)1);
        } else {
            byteArray3.setByte(0, (byte)63);
        }
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24780), byteArray3));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24777), cryptogramInput.getUnpredictableNumber()));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24830), cryptogramInput.getAmountAuthorized()));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24829), cryptogramInput.getAmountOther()));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(24362), cryptogramInput.getTrxCurrencyCode()));
        byteArray.append(TLV.create((byte)-102, cryptogramInput.getTrxDate()));
        byteArray.append(TLV.create((byte)-100, cryptogramInput.getTrxType()));
        byteArray.append(TLV.create((byte)90, transactionOutput.getPAN()));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(24372), transactionOutput.getPANSequenceNumber()));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(24356), transactionOutput.getExpiryDate()));
        byteArray.append(TLV.create(byteArrayFactory.getFromWord(-24806), cryptogramInput.getTerminalCountryCode()));
        byteArray.append(TLV.create((byte)-126, transactionOutput.getAIP()));
        return byteArray;
    }

    private static ByteArray buildUCAF(CryptogramInput cryptogramInput, TransactionOutput transactionOutput) {
        ByteArrayFactory byteArrayFactory = ByteArrayFactory.getInstance();
        ByteArray byteArray = byteArrayFactory.getByteArray(1);
        byteArray.setByte(0, (byte)(15 & transactionOutput.getPANSequenceNumber().getByte(0)));
        byteArray.append(transactionOutput.getCryptoGram().getCryptogram());
        byteArray.append(transactionOutput.getCryptoGram().getATC());
        byteArray.append(cryptogramInput.getUnpredictableNumber());
        byteArray.append(transactionOutput.getAIP());
        byteArray.appendByte(transactionOutput.getCryptoGram().getIssuerApplicationData().getByte(0));
        byteArray.appendByte(transactionOutput.getCryptoGram().getIssuerApplicationData().getByte(1));
        c.d(TAG, "UCAF input: " + byteArray.getHexString());
        String string = byteArrayFactory.hexStringToBase64(byteArray.getHexString());
        c.d(TAG, "UCAF encoded: " + string);
        return byteArrayFactory.getByteArray(string.getBytes(), string.length());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private RemoteCryptogramResult createRemoteCryptogram(CryptogramInput cryptogramInput) {
        RemotePaymentData remotePaymentData;
        McTAController mcTAController;
        DC_CP_MPP dC_CP_MPP;
        if (cryptogramInput == null) {
            return new RemoteCryptogramResult(ReturnCode.ERROR_INVALID_INPUT, null);
        }
        if (this.mPaymentProfile.getDigitalizedCardContainer() instanceof DC_CP) {
            dC_CP_MPP = ((DC_CP)this.mPaymentProfile.getDigitalizedCardContainer()).getDC_CP_MPP();
            if (dC_CP_MPP == null) {
                c.e(TAG, "DSRP: internall error, can't get provisioned card profile, null.");
                return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
            }
            remotePaymentData = dC_CP_MPP.getRemotePaymentData();
        } else {
            dC_CP_MPP = null;
            remotePaymentData = null;
        }
        if (dC_CP_MPP == null) {
            c.e(TAG, "DSRP: internall error, provisioned card profile is null.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        }
        if (remotePaymentData == null) {
            c.e(TAG, "DSRP: internall error, Remote Payment data is null.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        }
        ByteArrayFactory byteArrayFactory = ByteArrayFactory.getInstance();
        TransactionOutput transactionOutput = this.initializeCryptoOutput(remotePaymentData);
        ByteArray byteArray = remotePaymentData.getCVR_MaskAnd().clone();
        c.d(TAG, "CVR mask: " + byteArray.getHexString());
        byte by = byteArray.getByte(3);
        if (cryptogramInput.getTerminalCountryCode() == null || !cryptogramInput.getTerminalCountryCode().isEqual(dC_CP_MPP.getCardRiskManagementData().getCRM_CountryCode())) {
            byteArray.setByte(3, (byte)(by | 4));
        } else {
            byteArray.setByte(3, (byte)(by | 2));
        }
        if ((byte)(3 & remotePaymentData.getCVR_MaskAnd().getByte(2)) != 0 || (byte)(3 & remotePaymentData.getCIAC_Decline().getByte(2)) != 0) {
            CheckTable.processAddCheckTable(this.buildCDOL(cryptogramInput), dC_CP_MPP.getCardRiskManagementData().getAdditionalCheckTable(), byteArray);
        }
        if (this.mTransactionContext.getTransactionCredentials().getCVMResult().getResultCode() == 0L) {
            byteArray.setByte(0, (byte)(5 | byteArray.getByte(0)));
        }
        c.d(TAG, "CVR: " + byteArray.getHexString());
        byteArray.setByte(3, (byte)(64 | byteArray.getByte(3)));
        if (!Utils.isZero(remotePaymentData.getCIAC_Decline().bitWiseAnd(byteArray.copyOfRange(3, 6)))) {
            byteArray.setByte(0, (byte)(-128 | byteArray.getByte(0)));
            transactionOutput.getCryptoGram().setCid((byte)0);
        } else {
            byteArray.setByte(0, (byte)(-96 | byteArray.getByte(0)));
            transactionOutput.getCryptoGram().setCid((byte)-128);
        }
        c.d(TAG, "Mask: " + remotePaymentData.getCVR_MaskAnd().getHexString());
        ByteArray byteArray2 = byteArray.bitWiseAnd(remotePaymentData.getCVR_MaskAnd());
        ByteArray byteArray3 = this.buildCDOL(cryptogramInput);
        try {
            McTAController mcTAController2;
            mcTAController = mcTAController2 = McTAController.getInstance();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            c.e(TAG, "GPO processCommand: cannot initiate MC TA. Unexpected TA exception.");
            mcTAController = null;
        }
        if (mcTAController == null) {
            c.e(TAG, "DSRP: internall error, MC TA isn't loaded.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        }
        McTACommands.TASetContext.TASetContextResponse.SetContextOut setContextOut = mcTAController.setContext(this.mTransactionContext.getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_DSRP_TA_GPO));
        if (setContextOut == null) return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        if (setContextOut._atc == null) return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        if (setContextOut._wrapped_atc_obj == null) return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        if (setContextOut._iccdn == null) {
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        }
        byte[] arrby = setContextOut._atc.getData();
        if (arrby == null || arrby.length != 2) {
            c.e(TAG, "DSRP: wrong ATC length.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        }
        byte[] arrby2 = setContextOut._wrapped_atc_obj.getData();
        if (arrby2 == null) {
            c.e(TAG, "DSRP: Wrong TA profile returned from MC TA.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        }
        transactionOutput.getCryptoGram().setATC(byteArrayFactory.getByteArray(arrby, arrby.length));
        this.mTransactionContext.getTransactionCredentials().setmWrappedAtcObject(arrby2);
        c.i(TAG, "ATC=" + transactionOutput.getCryptoGram().getATC().getHexString());
        byte by2 = this.mTransactionContext.getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_DSRP_TA_GAC_ONLINE_CVM);
        ByteArray byteArray4 = byteArray3.copyOfRange(0, 29);
        ByteArray byteArray5 = byteArray2.copyOfRange(1, byteArray2.getLength());
        c.d(TAG, "Input Data1: " + byteArray4.getHexString());
        c.d(TAG, "Input Data2: " + byteArray5.getHexString());
        byte[] arrby3 = mcTAController.generateMAC(by2, byteArray4.getBytes(), byteArray5.getBytes(), cryptogramInput.getUnpredictableNumber().getBytes());
        if (arrby3 == null || arrby3.length != 8) {
            c.e(TAG, "DSRP: wrong cryptogram.");
            return new RemoteCryptogramResult(ReturnCode.ERROR_INCOMPATIBLE_PROFILE, null);
        }
        c.i(TAG, "Cryptogram: generated cryptogram...");
        transactionOutput.getCryptoGram().setCryptoOutput(arrby3);
        transactionOutput.getCryptoGram().setCid((byte)-128);
        ByteArray byteArray6 = remotePaymentData.getIssuerApplicationData().clone();
        byteArray6.copyBytes(byteArray2, 0, 2, byteArray2.getLength());
        byteArray6.setShort(8, (short)0);
        transactionOutput.getCryptoGram().setIssuerApplicationData(byteArray6);
        transactionOutput.setExpiryDate(remotePaymentData.getApplicationExpiryDate());
        transactionOutput.setPAN(remotePaymentData.getPAN());
        transactionOutput.setPANSequenceNumber(remotePaymentData.getPAN_SequenceNumber());
        c.d(TAG, "PAR setting here : " + remotePaymentData.getPaymentAccountReference());
        transactionOutput.setPar(remotePaymentData.getPaymentAccountReference());
        try {
            this.cancelPayment(MCTransactionResult.TRANSACTION_COMPLETED, true);
        }
        catch (MCTransactionException mCTransactionException) {
            c.c(TAG, "Unexpected exception during inapp update: " + mCTransactionException.toString(), (Throwable)((Object)mCTransactionException));
            return new RemoteCryptogramResult(ReturnCode.OK, transactionOutput);
        }
        return new RemoteCryptogramResult(ReturnCode.OK, transactionOutput);
    }

    public static ByteArray getDateAsByteArray(Date date) {
        String string = "";
        if (date.getYear() < 2010) {
            string = string + "0";
        }
        String string2 = string + date.getYear() % 2000;
        if (date.getMonth() < 10) {
            string2 = string2 + "0";
        }
        String string3 = string2 + "" + date.getMonth();
        if (date.getDay() < 10) {
            string3 = string3 + "0";
        }
        String string4 = string3 + date.getDay();
        return ByteArrayFactory.getInstance().getByteArray(Utils.readHexString(string4), 3);
    }

    private ByteArray getDsrpUn(McTAController mcTAController) {
        byte[] arrby = mcTAController.generateUN();
        if (arrby == null || arrby.length != 4) {
            c.e(TAG, "DSRP: internall error, MC TA returns incorrect un.");
            return null;
        }
        return ByteArrayFactory.getInstance().getByteArray(arrby, arrby.length);
    }

    private MTBPState getState() {
        return this.mCurrentState;
    }

    private TransactionOutput initializeCryptoOutput(RemotePaymentData remotePaymentData) {
        TransactionOutput transactionOutput = new TransactionOutput();
        transactionOutput.setPAN(remotePaymentData.getPAN().clone());
        c.d(TAG, "PAN: " + remotePaymentData.getPAN().getHexString());
        transactionOutput.setPANSequenceNumber(remotePaymentData.getPAN_SequenceNumber().clone());
        c.d(TAG, "PAN SN: " + remotePaymentData.getPAN_SequenceNumber().getHexString());
        transactionOutput.setCVMEntered(true);
        transactionOutput.getCryptoGram().setIssuerApplicationData(remotePaymentData.getIssuerApplicationData().clone());
        c.d(TAG, "IAD: " + remotePaymentData.getIssuerApplicationData().getHexString());
        transactionOutput.setAIP(remotePaymentData.getAIP());
        c.d(TAG, "getCVR_MaskAnd: " + remotePaymentData.getCVR_MaskAnd().getHexString());
        c.d(TAG, "getIssuerApplicationData: " + remotePaymentData.getIssuerApplicationData().getHexString());
        c.d(TAG, "getApplicationExpiryDate: " + remotePaymentData.getApplicationExpiryDate().getHexString());
        c.d(TAG, "getCIAC_Decline: " + remotePaymentData.getCIAC_Decline().getHexString());
        return transactionOutput;
    }

    private boolean isDsrpInputValid(DSRPInputData dSRPInputData) {
        long l2 = dSRPInputData.getTransactionAmount();
        if (l2 < 0L || l2 > 999999999999L) {
            c.e(TAG, "DSRP: wrong transaction amount input : " + l2);
            return false;
        }
        long l3 = dSRPInputData.getOtherAmount();
        if (l3 < 0L || l3 > 999999999999L) {
            c.e(TAG, "DSRP: wrong other amount input : " + l3);
            return false;
        }
        long l4 = dSRPInputData.getCurrencyCode();
        if (l4 < 0L || l4 > 999L) {
            c.e(TAG, "DSRP: wrong currency code input : " + l4);
            return false;
        }
        byte by = dSRPInputData.getTransactionType();
        if (by < 0 || by > 99) {
            c.e(TAG, "DSRP: wrong transaction type : " + by);
            return false;
        }
        int n2 = dSRPInputData.getCountryCode();
        if (n2 < 0 || n2 > 999) {
            c.e(TAG, "DSRP: wrong transaction country code : " + n2);
            return false;
        }
        Date date = dSRPInputData.getTransactionDate();
        if (date == null) {
            c.e(TAG, "DSRP: transacion date is null");
            return false;
        }
        if (date.getDay() < 1 || date.getDay() > 31 || date.getMonth() < 1 || date.getMonth() > 12 || date.getYear() < 2000) {
            c.e(TAG, "DSRP: wrong transacion date : " + date.toString());
            return false;
        }
        if (dSRPInputData.getCryptogramType() != CryptogramType.DE55 && dSRPInputData.getCryptogramType() != CryptogramType.UCAF) {
            c.e(TAG, "DSRP: Unknown cryptogram type: " + (Object)((Object)dSRPInputData.getCryptogramType()));
            return false;
        }
        return true;
    }

    private void setState(MTBPState mTBPState) {
        block3 : {
            block2 : {
                if (mTBPState == null || mTBPState.equals((Object)this.getState())) break block2;
                if (!((EnumSet)mStateTransitionsMap.get((Object)this.mCurrentState)).contains((Object)mTBPState)) break block3;
                this.mCurrentState = mTBPState;
            }
            return;
        }
        c.e(TAG, "setState: wrong state requested, current state = " + this.mCurrentState.name() + ", requested state = " + mTBPState.name());
        throw new MCTransactionException(this.generateErrorResponse());
    }

    /*
     * Enabled aggressive block sorting
     */
    public void cancelPayment(MCTransactionResult mCTransactionResult, boolean bl) {
        if (!this.mCurrentState.equals((Object)MTBPState.STOPPED)) {
            if (MCTransactionResult.TRANSACTION_COMPLETED.equals((Object)mCTransactionResult)) {
                this.mTransactionContext.getTransactionListener().onTransactionSuccess();
            } else {
                this.mTransactionContext.getTransactionListener().onTransactionCanceled(MCTransactionResult.ERROR_NFC_COMMAND_CANCELED);
            }
            this.setState(MTBPState.READY);
        }
        if (!bl) {
            c.i(TAG, "cancelPayment: skip update data");
            return;
        } else {
            if (this.mTransactionContext.getTransactionCredentials().getmWrappedAtcObject() == null) {
                c.e(TAG, "cancelPayment: update atc null!!!!");
                return;
            }
            if (this.mDao == null || this.mPaymentProfile.getUniqueTokenReferenceId() < 0L) return;
            {
                c.i(TAG, "cancelPayment: update atc object :");
                if (this.mDao.updateWrappedAtcData(this.mTransactionContext.getTransactionCredentials().getmWrappedAtcObject(), this.mPaymentProfile.getUniqueTokenReferenceId())) return;
                {
                    c.e(TAG, "cancelPayment: update atc object failed !!!!");
                    return;
                }
            }
        }
    }

    public void cleanTransactionContext() {
        if (MTBPState.READY.equals((Object)this.getState()) || MTBPState.READY.equals((Object)MTBPState.STOPPED)) {
            this.mTransactionContext.clearCredentials();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DSRPOutputData generateDSRPData(DSRPInputData dSRPInputData) {
        McTAController mcTAController;
        if (dSRPInputData == null) return null;
        if (!this.isDsrpInputValid(dSRPInputData)) {
            return null;
        }
        ByteArrayFactory byteArrayFactory = ByteArrayFactory.getInstance();
        CryptogramInput cryptogramInput = new CryptogramInput();
        long l2 = dSRPInputData.getTransactionAmount();
        long l3 = dSRPInputData.getOtherAmount();
        long l4 = dSRPInputData.getCurrencyCode();
        int n2 = dSRPInputData.getCountryCode();
        Date date = dSRPInputData.getTransactionDate();
        try {
            McTAController mcTAController2;
            mcTAController = mcTAController2 = McTAController.getInstance();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            mcTAController = null;
        }
        if (mcTAController == null) {
            c.e(TAG, "DSRP: internall error, MC TA isn't loaded.");
            return null;
        }
        cryptogramInput.setOnlineAllowed(true);
        cryptogramInput.setTVR(byteArrayFactory.getByteArray(5));
        cryptogramInput.setCryptoGramType(dSRPInputData.getCryptogramType());
        cryptogramInput.setAmountAuthorized(byteArrayFactory.getByteArray(Utils.longToBCD(l2, 6), 6));
        cryptogramInput.setAmountOther(byteArrayFactory.getByteArray(Utils.longToBCD(l3, 6), 6));
        cryptogramInput.setTerminalCountryCode(byteArrayFactory.getByteArray(Utils.longToBCD(n2, 2), 2));
        cryptogramInput.setTrxCurrencyCode(byteArrayFactory.getByteArray(Utils.longToBCD(l4, 2), 2));
        cryptogramInput.setTrxDate(MTBPLite.getDateAsByteArray(date));
        ByteArray byteArray = byteArrayFactory.getByteArray(4);
        Utils.writeInt(byteArray, 0, dSRPInputData.getUnpredictableNumber());
        cryptogramInput.setUnpredictableNumber(byteArray);
        ByteArray byteArray2 = byteArrayFactory.getByteArray(1);
        byteArray2.setByte(0, dSRPInputData.getTransactionType());
        cryptogramInput.setTrxType(byteArray2);
        long l5 = dSRPInputData.getUnpredictableNumber();
        c.d(TAG, "Unpredictable number: " + l5);
        if (l5 == 0L) {
            cryptogramInput.setUnpredictableNumber(this.getDsrpUn(mcTAController));
        }
        c.d(TAG, "UN before createRemote Cryptogram : " + cryptogramInput.getUnpredictableNumber().getHexString());
        RemoteCryptogramResult remoteCryptogramResult = this.createRemoteCryptogram(cryptogramInput);
        if (remoteCryptogramResult.getCode() != ReturnCode.OK) {
            c.e(TAG, "DSRP: createRemoteCryptogram failure cause : " + (Object)((Object)remoteCryptogramResult.getCode()));
            return null;
        }
        TransactionOutput transactionOutput = remoteCryptogramResult.getOutput();
        if (transactionOutput == null) {
            c.e(TAG, "DSRP: TransactionOupt is null ");
            return null;
        }
        if (transactionOutput.getCryptoGram().getCid() != -128) {
            c.e(TAG, "DSRP: A wrong cid in DsrpOutput, cid = " + transactionOutput.getCryptoGram().getCid());
            return null;
        }
        if (dSRPInputData.getCryptogramType() == CryptogramType.UCAF && transactionOutput.getPANSequenceNumber().getByte(0) > 9) {
            c.e(TAG, "DSRP:  A wrong PANSN for UCAF ");
            return null;
        }
        DSRPOutputData dSRPOutputData = new DSRPOutputData();
        String string = transactionOutput.getPAN().getHexString();
        dSRPOutputData.setCryptoGramType(dSRPInputData.getCryptogramType());
        dSRPOutputData.setTransactionAmount(dSRPInputData.getTransactionAmount());
        dSRPOutputData.setCurrencyCode((int)dSRPInputData.getCurrencyCode());
        dSRPOutputData.setUcafVersion(0);
        dSRPOutputData.setUnpredictableNumber(cryptogramInput.getUnpredictableNumber().getBytes());
        dSRPOutputData.setPan(string.replaceAll("F", ""));
        dSRPOutputData.setExpiryDate(transactionOutput.getExpiryDate().getBytes());
        dSRPOutputData.setPanSequenceNumber(transactionOutput.getPANSequenceNumber().getByte(0));
        dSRPOutputData.setAtc(Utils.readInt(transactionOutput.getCryptoGram().getATC().getBytes(), 0));
        dSRPOutputData.setCryptoGram(remoteCryptogramResult.getOutput().getCryptoGram().getCryptogram().getBytes());
        if (dSRPInputData.getCryptogramType() == CryptogramType.UCAF) {
            dSRPOutputData.setTransactionCryptogramData(MTBPLite.buildUCAF(cryptogramInput, transactionOutput).getBytes());
        } else {
            dSRPOutputData.setTransactionCryptogramData(MTBPLite.buildDE55(cryptogramInput, transactionOutput).getBytes());
        }
        if (transactionOutput.getPar() == null) return dSRPOutputData;
        if (transactionOutput.getPar().getBytes() == null) return dSRPOutputData;
        c.d(TAG, "par in dsrpout data :" + transactionOutput.getPar().getHexString());
        dSRPOutputData.setPar(transactionOutput.getPar().getBytes());
        return dSRPOutputData;
    }

    public ByteArray generateErrorResponse() {
        return null;
    }

    public void initialize(MCBaseCardProfile<?> mCBaseCardProfile) {
        if (mCBaseCardProfile == null || mCBaseCardProfile.getTADataContainer() == null || mCBaseCardProfile.getDigitalizedCardContainer() == null) {
            throw new MCTransactionException("Current card profile is not valid for payment.");
        }
        this.mPaymentProfile = mCBaseCardProfile;
        this.setState(MTBPState.READY);
        c.i(TAG, "Get current state: " + (Object)((Object)this.getState()));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long initializeMST() {
        if (!MTBPState.READY.equals((Object)this.getState())) {
            c.e(TAG, "Wrong state on MST transaction init, required state: " + (Object)((Object)MTBPState.READY) + ", current state: " + (Object)((Object)this.getState()));
            return 1L;
        }
        if (this.mPaymentProfile == null || this.mPaymentProfile.getTADataContainer() == null) {
            c.e(TAG, "MC Card profile doesn't support MST transaction.");
            return 2L;
        }
        if (this.mTransactionContext == null || this.mTransactionContext.getTransactionCredentials() == null) {
            c.e(TAG, "MC transaction context is not initialized for MST transaction.");
            return 1L;
        }
        try {
            long l2;
            McTAController mcTAController = McTAController.getInstance();
            McTACommands.TASetContext.TASetContextResponse.SetContextOut setContextOut = mcTAController.setContext(this.mTransactionContext.getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_MST_TA_GPO));
            if (setContextOut == null || setContextOut._atc == null || setContextOut._wrapped_atc_obj == null) {
                c.e(TAG, "Wrong MC TA response on  for MST transaction.");
                return 2L;
            }
            byte[] arrby = setContextOut._atc.getData();
            if (arrby == null) {
                c.e(TAG, "Wrong ATC value returned from MC TA.");
                return 2L;
            }
            c.i(TAG, "ATC=" + McUtils.byteArrayToHex(arrby));
            byte[] arrby2 = setContextOut._wrapped_atc_obj.getData();
            if (arrby2 == null) {
                c.e(TAG, "Wrong TA profile returned from MC TA.");
                return 2L;
            }
            this.mTransactionContext.getTransactionCredentials().setATC(arrby);
            this.mTransactionContext.getTransactionCredentials().setmWrappedAtcObject(arrby2);
            this.mPaymentProfile.setTaAtcContainer(arrby2);
            if (this.mDao != null && this.mPaymentProfile.getUniqueTokenReferenceId() > 0L) {
                c.i(TAG, "initializeMST(): update profile");
                if (!this.mDao.updateWrappedAtcData(this.mTransactionContext.getTransactionCredentials().getmWrappedAtcObject(), this.mPaymentProfile.getUniqueTokenReferenceId())) {
                    c.e(TAG, "initializeMST: failed update ta object in the db.");
                    return 1L;
                }
            }
            if ((l2 = mcTAController.prepareMSTtracks(h.am(McProvider.getContext()) / 60000L % 100000L)) == (long)McTACommands.MC_TA_ERRORS.NO_ERROR.ordinal()) return 0L;
            {
                c.e(TAG, "prepare MST tracks failed in MC TA, return code " + l2);
                return 1L;
            }
        }
        catch (Exception exception) {
            c.e(TAG, "Failed to call MC TA for MST init, transit state to " + (Object)((Object)MTBPState.STOPPED));
            try {
                this.setState(MTBPState.STOPPED);
            }
            catch (MCTransactionException mCTransactionException) {
                c.e(TAG, "Failed to transit to the STOPPED state.");
                this.mCurrentState = MTBPState.STOPPED;
                mCTransactionException.printStackTrace();
            }
            exception.printStackTrace();
            return 1L;
        }
    }

    public boolean isReadyForTransaction() {
        return this.getState().equals((Object)MTBPState.READY);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public ByteArray processAPDU(ByteArray byteArray) {
        ByteArrayFactory byteArrayFactory = ByteArrayFactory.getInstance();
        byte by = byteArray.getByte(1);
        byte by2 = byteArray.getByte(0);
        c.i(TAG, "processAPDU: ins: " + McUtils.byteToHex(by) + " cla = " + McUtils.byteToHex(by2) + " : " + MCAPDUHandler.getApduName(by));
        MCAPDUHandler.APDUCommand aPDUCommand = this.mAPDUcommandHandlerFactory.getCommandHandlerByInstruction(by);
        if (aPDUCommand == null) {
            c.e(TAG, "APDU command not found, apdu = " + by);
            return byteArrayFactory.getFromWord(27904);
        }
        if (aPDUCommand.validateState(this.getState())) {
            try {
                MCCAPDUBaseCommandHandler mCCAPDUBaseCommandHandler = aPDUCommand.getAPDUHandler();
                if (!mCCAPDUBaseCommandHandler.checkCLA(by2)) {
                    c.e(TAG, "CLA is not supported for ins = " + by + ", cla = " + by2);
                    return byteArrayFactory.getFromWord(28160);
                }
                mCCAPDUBaseCommandHandler.setTransactionContext((DC_CP)this.mPaymentProfile.getDigitalizedCardContainer(), this.mTransactionContext);
                MCCommandResult mCCommandResult = mCCAPDUBaseCommandHandler.processAPDU(byteArray);
                if (MCCommandResult.TransitionState.TRANSITION_NEXT_STATE.equals((Object)mCCommandResult.getTransitionState())) {
                    this.setState(aPDUCommand.getNextState());
                    return mCCommandResult.getResponseAPDU();
                }
                if (!MCCommandResult.TransitionState.TRANSITION_CANCEL_STATE.equals((Object)mCCommandResult.getTransitionState())) return mCCommandResult.getResponseAPDU();
                this.cancelPayment(mCCommandResult.getResponseCode(), true);
                return mCCommandResult.getResponseAPDU();
            }
            catch (MCTransactionException mCTransactionException) {
                c.e(TAG, "processAPDU: Unexpected MCTransactionException.");
                try {
                    this.cancelPayment(MCCommandResult.iso7816ToResponseCode(27013), false);
                }
                catch (MCTransactionException mCTransactionException2) {
                    c.e(TAG, "processAPDU: error on cancel transaction.");
                    this.mCurrentState = MTBPState.STOPPED;
                    mCTransactionException2.printStackTrace();
                }
                mCTransactionException.printStackTrace();
                return byteArrayFactory.getFromWord(27013);
            }
        }
        c.e(TAG, "Invalid state for the apdu: " + by + ", state " + (Object)((Object)this.getState()));
        if (by == -88) {
            if (MTBPState.NFC_INITIATED.equals((Object)this.getState())) return byteArrayFactory.getFromWord(27013);
        }
        try {
            this.cancelPayment(MCTransactionResult.ERROR_NFC_COMMAND_INTERNAL_ERROR, false);
        }
        catch (MCTransactionException mCTransactionException) {
            c.e(TAG, "processAPDU: error on cancel transaction.");
            this.mCurrentState = MTBPState.READY;
            mCTransactionException.printStackTrace();
            return byteArrayFactory.getFromWord(27013);
        }
        return byteArrayFactory.getFromWord(27013);
    }

    public void setTransactionContext(MCTransactionCredentials mCTransactionCredentials, MTBPTransactionListener mTBPTransactionListener) {
        c.i(TAG, "Init factory state: " + (Object)((Object)this.getState()));
        if (this.getState() == MTBPState.READY) {
            c.i("mcpce_MCTransactionService", "State MCPCEInitializedState, startContactLessPayment");
            if (this.mPaymentProfile.getDigitalizedCardContainer() instanceof DC_CP) {
                if (((DC_CP)this.mPaymentProfile.getDigitalizedCardContainer()).getDC_CP_MPP().getContactlessPaymentData() == null) {
                    c.e(TAG, "NFC payment profile is not found.");
                    throw new MCTransactionException(this.generateErrorResponse());
                }
            } else {
                c.e(TAG, "Unknown payment profile.");
                throw new MCTransactionException(this.generateErrorResponse());
            }
            if (mCTransactionCredentials == null || mCTransactionCredentials.getSecureObject() == null) {
                c.e(TAG, "Credentials is null: " + mCTransactionCredentials);
                throw new MCTransactionException(this.generateErrorResponse());
            }
            this.mTransactionContext = MTBPContextFactory.getContext(mCTransactionCredentials);
            if (this.mTransactionContext.getTransactionInformation() == null) {
                c.e(TAG, "Transaction information is null");
                throw new MCTransactionException(this.generateErrorResponse());
            }
            this.mTransactionContext.setTransactionListener(mTBPTransactionListener);
            this.mAPDUcommandHandlerFactory = new MCAPDUHandler();
            return;
        }
        c.e(TAG, "Wrong state for state transaction context, state: " + (Object)((Object)this.getState()) + "; expected state: " + (Object)((Object)MTBPState.READY));
        throw new MCTransactionException(this.generateErrorResponse());
    }

    public Bundle stopNfc() {
        if (this.mTransactionContext == null) {
            return null;
        }
        return this.mTransactionContext.stopNfc();
    }

    public static final class MTBPState
    extends Enum<MTBPState> {
        private static final /* synthetic */ MTBPState[] $VALUES;
        public static final /* enum */ MTBPState MST_INITIATED;
        public static final /* enum */ MTBPState NFC_INITIATED;
        public static final /* enum */ MTBPState NFC_SELECTED;
        public static final /* enum */ MTBPState READY;
        public static final /* enum */ MTBPState STOPPED;

        static {
            STOPPED = new MTBPState();
            READY = new MTBPState();
            NFC_SELECTED = new MTBPState();
            NFC_INITIATED = new MTBPState();
            MST_INITIATED = new MTBPState();
            MTBPState[] arrmTBPState = new MTBPState[]{STOPPED, READY, NFC_SELECTED, NFC_INITIATED, MST_INITIATED};
            $VALUES = arrmTBPState;
        }

        public static MTBPState valueOf(String string) {
            return (MTBPState)Enum.valueOf(MTBPState.class, (String)string);
        }

        public static MTBPState[] values() {
            return (MTBPState[])$VALUES.clone();
        }
    }

}

