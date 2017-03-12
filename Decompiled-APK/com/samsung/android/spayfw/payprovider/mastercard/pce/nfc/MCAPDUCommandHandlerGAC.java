package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.mastercard.mcbp.core.mcbpcards.profile.AlternateContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mpplite.states.CheckTable;
import com.mastercard.mcbp.crypto.MCBPCryptoService;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.Utils;
import com.mastercard.mobile_api.utils.apdu.emv.GenACRespApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GenerateACApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable.TAProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class MCAPDUCommandHandlerGAC extends MCCAPDUBaseCommandHandler {
    public static final String TAG = "mcpce_MCAPDUCommandHandlerGAC";
    private ByteArray mCVR;
    private ContactlessPaymentData mClData;
    private final MCBPCryptoService mCryptoService;
    private GenerateACApdu mGenACApdu;
    private byte mP1;
    private ByteArray mUnmaskedCVR;

    public MCAPDUCommandHandlerGAC() {
        this.mCryptoService = MCBPCryptoService.getInstance();
    }

    public MCCommandResult processCommand(ByteArray byteArray) {
        MCCommandResult verifyPaymentProfile = verifyPaymentProfile(byteArray);
        if (MCTransactionResult.COMMAND_COMPLETED.equals(verifyPaymentProfile.getResponseCode())) {
            verifyPaymentProfile = initializeTransactionContext();
            if (MCTransactionResult.COMMAND_COMPLETED.equals(verifyPaymentProfile.getResponseCode())) {
                MCCryptoOutput mCCryptoOutput = new MCCryptoOutput();
                if (getTransactionContext().isAlternateAID()) {
                    mCCryptoOutput.setIssuerApplicationData(this.mPaymentProfile.getContactlessPaymentData().getAlternateContactlessPaymentData().getIssuerApplicationData().clone());
                    Log.m285d(TAG, "processCommand, iad: " + mCCryptoOutput.getIssuerApplicationData().getHexString());
                } else {
                    mCCryptoOutput.setIssuerApplicationData(this.mPaymentProfile.getContactlessPaymentData().getIssuerApplicationData().clone());
                }
                getTransactionContext().setCryptoOutput(mCCryptoOutput);
                verifyPaymentProfile = composeCardholderVerificationResult();
                if (MCTransactionResult.COMMAND_COMPLETED.equals(verifyPaymentProfile.getResponseCode())) {
                    byte[] computeCryptoInputData1 = computeCryptoInputData1();
                    if (computeCryptoInputData1 == null) {
                        Log.m286e(TAG, "GAC process command: input data 1 is null.");
                        return ERROR(27013);
                    }
                    byte[] computeCryptoInputData2 = computeCryptoInputData2();
                    if (computeCryptoInputData2 == null) {
                        Log.m286e(TAG, "GAC process command: input data 2 is null.");
                        return ERROR(27013);
                    } else if (getTransactionContext().getTransactionCredentials().getATC() == null) {
                        Log.m286e(TAG, "GAC process command: ATC value is empty...");
                        return ERROR(27013);
                    } else if (getTransactionContext().getTransactionCredentials().getProfileType() == 0) {
                        Log.m286e(TAG, "GAC process command: cannot find correct crypto profile...");
                        return ERROR(27013);
                    } else {
                        Object obj = null;
                        try {
                            obj = McTAController.getInstance().generateMAC(getTransactionContext().getTransactionCredentials().getProfileType(), computeCryptoInputData1, computeCryptoInputData2, getTransactionContext().getTransactionInformation().getUN().getBytes());
                        } catch (Exception e) {
                            Log.m286e(TAG, "GAC process command: unexpected TAException");
                            e.printStackTrace();
                        }
                        if (obj != null) {
                            getTransactionContext().getCryptoOutput().setCryptogram(this.baf.getByteArray((byte[]) obj.clone(), obj.length));
                            return generateResponseAPDU();
                        }
                        Log.m286e(TAG, "GAC process command: ");
                        return ERROR(27013);
                    }
                }
                Log.m286e(TAG, "GAC process command: compose CVR failed.");
                return verifyPaymentProfile;
            }
            Log.m286e(TAG, "GAC process command: init transaction context failed.");
            return verifyPaymentProfile;
        }
        Log.m286e(TAG, "GAC process command: verify payment profile failed.");
        return verifyPaymentProfile;
    }

    public MCCommandResult verifyPaymentProfile(ByteArray byteArray) {
        Log.m287i(TAG, "Start checkPaymentData...");
        this.mGenACApdu = new GenerateACApdu(byteArray);
        this.mP1 = byteArray.getByte(2);
        int i = byteArray.getByte(4) & GF2Field.MASK;
        if (getTransactionContext().isAlternateAID()) {
            AlternateContactlessPaymentData alternateContactlessPaymentData = getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData();
            this.mClData = getPaymentProfile().getContactlessPaymentData();
            this.mClData.setAID(alternateContactlessPaymentData.getAID());
            this.mClData.setPaymentFCI(alternateContactlessPaymentData.getPaymentFCI());
            this.mClData.setGPO_Response(alternateContactlessPaymentData.getGPO_Response());
            this.mClData.setCVR_MaskAnd(alternateContactlessPaymentData.getCVR_MaskAnd());
            this.mClData.setCIAC_Decline(alternateContactlessPaymentData.getCIAC_Decline());
        } else {
            this.mClData = getPaymentProfile().getContactlessPaymentData();
        }
        if (!checkMchipParameters()) {
            Log.m286e(TAG, "GAC process command: check MCHIP parameters failed.");
            return ERROR(27013);
        } else if (i >= 43 && i == this.mClData.getCDOL1_RelatedDataLength()) {
            return completeCommand();
        } else {
            Log.m286e(TAG, "GAC: lc wrong length, lc: " + i + ", CDOL1 related data length: " + this.mClData.getCDOL1_RelatedDataLength());
            return ERROR(26368);
        }
    }

    private MCCommandResult initializeTransactionContext() {
        Log.m287i(TAG, "initTransactionContext...");
        if (MCCAPDUBaseCommandHandler.isTerminalOffline(this.mGenACApdu.getTerminalType())) {
            Log.m286e(TAG, "GAC check transaction context: offline terminal.");
            return ERROR(27013);
        }
        MCTransactionInformation transactionInformation = getTransactionContext().getTransactionInformation();
        transactionInformation.setAmount(this.mGenACApdu.getAuthorizedAmount());
        transactionInformation.setCurrencyCode(this.mGenACApdu.getTransactionCurrencyCode());
        transactionInformation.setTransactionDate(this.mGenACApdu.getTransactionDate());
        transactionInformation.setTransactionType(this.mGenACApdu.getTransactionType());
        transactionInformation.setUN(this.mGenACApdu.getUnpredictableNumber());
        transactionInformation.setMccCategory(this.mGenACApdu.getMerchantCategoryCode());
        transactionInformation.setMerchantNameAndLoc(this.mGenACApdu.getMerchantNameLocation());
        return completeCommand();
    }

    private MCCommandResult composeCardholderVerificationResult() {
        int i = 1;
        Log.m287i(TAG, "check CVR");
        try {
            checkCVRFromTerminal();
            Log.m287i(TAG, "check CVM");
            byte b = (byte) (this.mP1 & -64);
            if (b == null) {
                getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
                getTransactionContext().setPOSCII(this.baf.getByteArray(3));
                Log.m286e(TAG, "GAC compose CVR: AAC requested by terminal, p1 = " + b);
                aac(this.mGenACApdu);
                return completeCommand();
            } else if (Utils.isZero(this.mCVR.copyOfRange(3, 6).bitWiseAnd(this.mClData.getCIAC_Decline()))) {
                if (getTransactionContext().getTransactionCredentials().getCVMResult() == null || getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() != 0) {
                    Log.m287i(TAG, "tap&Go: CVM check flow");
                    ByteArray cvmResults = this.mGenACApdu.getCvmResults();
                    byte b2 = (byte) (cvmResults.getByte(0) & 63);
                    if (!((b2 == (byte) 1 || b2 == 4) && cvmResults.getByte(2) == (byte) 2)) {
                        i = 0;
                    }
                    if (i != 0) {
                        this.mCVR.setByte(3, (byte) (this.mCVR.getByte(3) | 1));
                        errorCdcvmRequired();
                        Log.m286e(TAG, "tap&go: return AAC 5.5");
                        aac(this.mGenACApdu);
                        return completeCommand();
                    } else if (getTransactionContext().getTransactionCredentials().getCVMResult() != null && getTransactionContext().getTransactionCredentials().getCVMResult().isCVMRequired()) {
                        errorCdcvmRequired();
                        Log.m286e(TAG, "tap&go: return AAC 5.7");
                        aac(this.mGenACApdu);
                        return completeCommand();
                    }
                }
                getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
                if (getTransactionContext().isOnlineAllowed()) {
                    arqc(this.mGenACApdu);
                    Log.m285d(TAG, "CVM OK, ARQC");
                    return completeCommand();
                }
                getTransactionContext().setPOSCII(this.baf.getByteArray(3));
                Log.m286e(TAG, "GAC compose CVR: online transaction not alowed, AAC");
                aac(this.mGenACApdu);
                return completeCommand();
            } else {
                getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
                getTransactionContext().setPOSCII(this.baf.getByteArray(3));
                Log.m286e(TAG, "GAC compose CVR: found match in CIAC_decline");
                aac(this.mGenACApdu);
                return completeCommand();
            }
        } catch (MCTransactionException e) {
            Log.m286e(TAG, "Unexpected MCTransactionException: " + e.getMessage());
            e.printStackTrace();
            aac(this.mGenACApdu);
            return completeCommand();
        }
    }

    private void errorCdcvmRequired() {
        this.mCVR.setByte(5, (byte) (this.mCVR.getByte(5) | 8));
        getTransactionContext().setTransactionResult(MCTransactionResult.CONTEXT_CONFLICT_CVM);
        getTransactionContext().setTransactionError(MCTransactionResult.CONTEXT_CONFLICT_CVM);
        ByteArray byteArray = this.baf.getByteArray(3);
        byteArray.setByte(1, (byte) 1);
        getTransactionContext().setPOSCII(byteArray);
    }

    private byte[] computeCryptoInputData1() {
        return this.baf.getFromByteArray(this.mGenACApdu.getCDOL().copyOfRange(0, 29)).getBytes();
    }

    private byte[] computeCryptoInputData2() {
        ContactlessPaymentData contactlessPaymentData;
        int i = GF2Field.MASK;
        Log.m287i(TAG, "Compute input data 2");
        if (getTransactionContext().isAlternateAID()) {
            AlternateContactlessPaymentData alternateContactlessPaymentData = getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData();
            contactlessPaymentData = getPaymentProfile().getContactlessPaymentData();
            contactlessPaymentData.setAID(alternateContactlessPaymentData.getAID());
            contactlessPaymentData.setPaymentFCI(alternateContactlessPaymentData.getPaymentFCI());
            contactlessPaymentData.setGPO_Response(alternateContactlessPaymentData.getGPO_Response());
            contactlessPaymentData.setCVR_MaskAnd(alternateContactlessPaymentData.getCVR_MaskAnd());
            contactlessPaymentData.setCIAC_Decline(alternateContactlessPaymentData.getCIAC_Decline());
        } else {
            contactlessPaymentData = getPaymentProfile().getContactlessPaymentData();
        }
        this.mUnmaskedCVR = this.mCVR.clone();
        this.mCVR = this.mCVR.bitWiseAnd(contactlessPaymentData.getCVR_MaskAnd());
        ByteArray copyOfRange = this.mCVR.copyOfRange(1, this.mCVR.getLength());
        Log.m285d(TAG, "Compute input data 2 Done");
        if ((this.mUnmaskedCVR.getByte(3) & 64) != 64) {
            Log.m285d(TAG, "RRP MAX RRP time " + Utils.readShort(contactlessPaymentData.getMaxRRTime()));
            Log.m285d(TAG, "RRP MIN RRP time " + Utils.readShort(contactlessPaymentData.getMinRRTime()));
            ByteArray issuerApplicationData = getTransactionContext().getCryptoOutput().getIssuerApplicationData();
            int readShort = (Utils.readShort(contactlessPaymentData.getMaxRRTime()) / 10) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
            if (readShort > GF2Field.MASK) {
                readShort = GF2Field.MASK;
            }
            issuerApplicationData.setByte(17, (byte) readShort);
            readShort = (Utils.readShort(contactlessPaymentData.getMinRRTime()) / 10) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
            if (readShort <= GF2Field.MASK) {
                i = readShort;
            }
            issuerApplicationData.setByte(25, (byte) i);
            getTransactionContext().getCryptoOutput().setIssuerApplicationData(issuerApplicationData);
            contactlessPaymentData.setIssuerApplicationData(issuerApplicationData);
            Log.m285d(TAG, "RRP performed: IAD after RRP counters " + contactlessPaymentData.getIssuerApplicationData().getHexString());
        }
        if ((getTransactionContext().getCryptoOutput().getIssuerApplicationData().getByte(1) & 1) == 1) {
            Log.m287i(TAG, "CVN requires to add counters.");
            copyOfRange.append(getTransactionContext().getCryptoOutput().getIssuerApplicationData().copyOfRange(10, 26));
        }
        return copyOfRange.clone().getBytes();
    }

    public MCCommandResult checkP1P2Parameters(byte b, byte b2) {
        Log.m285d(TAG, "GAC check p1/p2: start checking P1/P2 parameters");
        if ((b & 47) == 0 && b2 == null && (b & CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) != CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) {
            Log.m285d(TAG, "GAC check p1/p2: Checking P1/P2 parameters OK");
            return completeCommand();
        }
        Log.m286e(TAG, "GAC check p1/p2: p1 = " + b + ", p2 = " + b2);
        return ERROR(27270);
    }

    public MCCommandResult generateResponseAPDU() {
        Exception e;
        boolean z;
        ByteArray byteArray;
        GenACRespApdu genACRespApdu;
        ByteArray clone = getTransactionContext().getCryptoOutput().getCryptogram().clone();
        ByteArray computeIssuerApplicationData = computeIssuerApplicationData();
        boolean z2 = false;
        try {
            if ((this.mUnmaskedCVR.getByte(1) & 64) == 64) {
                try {
                    if (getTransactionContext().getTransactionCredentials().getIDN() != null) {
                        ByteArray copyOfRange = getTransactionContext().getTransactionCredentials().getIDN().copyOfRange(8, 16);
                        clone = cda(this.mGenACApdu, clone, computeIssuerApplicationData, getTransactionContext().getCryptoOutput().getCid(), copyOfRange);
                        z2 = true;
                    } else {
                        Log.m286e(TAG, "GAC generate RAPDU: IDN is null.");
                        return ERROR(27013);
                    }
                } catch (Exception e2) {
                    e = e2;
                    z = true;
                    Log.m286e(TAG, "GAC generate RAPDU: Unexpected exception: " + e.getMessage());
                    e.printStackTrace();
                    byteArray = clone;
                    genACRespApdu = new GenACRespApdu(z, byteArray, getTransactionContext().getTransactionCredentials().getATC(), getTransactionContext().getCryptoOutput().getCid(), computeIssuerApplicationData, getTransactionContext().getPOSCII());
                    getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
                    return completeTransaction(genACRespApdu.getByteArray().clone());
                }
            }
            z = z2;
            byteArray = clone;
        } catch (Exception e3) {
            z = false;
            e = e3;
            Log.m286e(TAG, "GAC generate RAPDU: Unexpected exception: " + e.getMessage());
            e.printStackTrace();
            byteArray = clone;
            genACRespApdu = new GenACRespApdu(z, byteArray, getTransactionContext().getTransactionCredentials().getATC(), getTransactionContext().getCryptoOutput().getCid(), computeIssuerApplicationData, getTransactionContext().getPOSCII());
            getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
            return completeTransaction(genACRespApdu.getByteArray().clone());
        }
        genACRespApdu = new GenACRespApdu(z, byteArray, getTransactionContext().getTransactionCredentials().getATC(), getTransactionContext().getCryptoOutput().getCid(), computeIssuerApplicationData, getTransactionContext().getPOSCII());
        getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
        return completeTransaction(genACRespApdu.getByteArray().clone());
    }

    private boolean checkMchipParameters() {
        if (this.mClData.getCIAC_Decline() == null) {
            Log.m286e(TAG, "GAC check MChip parameters: CIAC_decline is null.");
        }
        if (this.mClData.getCVR_MaskAnd() == null) {
            Log.m286e(TAG, "GAC check MChip parameters: CVRMaskAnd is null.");
        }
        if (this.mClData.getIssuerApplicationData() == null) {
            Log.m286e(TAG, "GAC check MChip parameters: issuer application data is null.");
        }
        if (this.mClData.getICC_privateKey_a() == null) {
            Log.m286e(TAG, "GAC check MChip parameters: ICC_privateKey_a is null.");
        }
        return (this.mClData.getCIAC_Decline() == null || this.mClData.getCVR_MaskAnd() == null || this.mClData.getIssuerApplicationData() == null || this.mClData.getICC_privateKey_a() == null) ? false : true;
    }

    private void checkCVRFromTerminal() {
        int i = 0;
        this.mCVR = getPaymentProfile().getContactlessPaymentData().getIssuerApplicationData().clone().copyOfRange(2, 8);
        byte b = this.mCVR.getByte(3);
        if (getPaymentProfile().getCardRiskManagementData().getCRM_CountryCode().isEqual(this.mGenACApdu.getTerminalCountryCode())) {
            this.mCVR.setByte(3, (byte) (b | 2));
        } else {
            this.mCVR.setByte(3, (byte) (b | 4));
        }
        if (!((this.mClData.getCVR_MaskAnd().getByte(5) & 3) == 0 && (this.mClData.getCIAC_Decline().getByte(2) & 3) == 0)) {
            CheckTable.processAddCheckTable(this.mGenACApdu.getCDOL(), getPaymentProfile().getCardRiskManagementData().getAdditionalCheckTable(), this.mCVR);
        }
        b = this.mCVR.getByte(0);
        if (getTransactionContext().getTransactionCredentials().getCVMResult() == null || getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() != 0) {
            this.mCVR.setByte(3, (byte) (b | 32));
        } else {
            this.mCVR.setByte(0, (byte) (b | 5));
        }
        checkContext();
        if (getTransactionContext().getRRPCounter() == 0) {
            this.mCVR.setByte(3, (byte) (this.mCVR.getByte(3) | 64));
            return;
        }
        ByteArray un = getTransactionContext().getTransactionInformation().getUN();
        if (un == null) {
            throw new MCTransactionException("GAC check CVR from terminal: UN number is empty");
        }
        ByteArray terminalRREntropy = getTransactionContext().getTerminalRREntropy();
        if (terminalRREntropy == null || un.getLength() != terminalRREntropy.getLength()) {
            throw new MCTransactionException("GAC check CVR from terminal: Terminal RRE: wrong value");
        }
        while (i < un.getLength()) {
            if (un.getByte(i) != terminalRREntropy.getByte(i)) {
                throw new MCTransactionException("GAC check CVR from terminal: UN number is not equal terminal entropy.");
            }
            i++;
        }
        ByteArray terminalVerificationResults = this.mGenACApdu.getTerminalVerificationResults();
        if (terminalVerificationResults == null || terminalVerificationResults.getLength() <= 4) {
            throw new MCTransactionException("GAC check CVR from terminal: Terminal RRE: wrong tvr length");
        } else if ((terminalVerificationResults.getByte(4) & 2) != 2) {
            throw new MCTransactionException("GAC check CVR from terminal: TVR RRP not performed");
        }
    }

    private void aac(GenerateACApdu generateACApdu) {
        this.mCVR.setByte(0, (byte) (this.mCVR.getByte(0) | -128));
        getTransactionContext().getCryptoOutput().setCid((byte) 0);
        getTransactionContext().getTransactionInformation().setCid((byte) 0);
        if (this.mP1 == 16 && (getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0 || !getTransactionContext().getTransactionCredentials().getCVMResult().isCVMRequired())) {
            this.mCVR.setByte(1, (byte) (this.mCVR.getByte(1) | 64));
        }
        if (getTransactionContext().isAlternateAID()) {
            Log.m286e(TAG, "AAC: Alternate profile");
            getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM));
        } else if (getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0) {
            Log.m286e(TAG, "AAC: CL profile");
            getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_TA_GAC_DECLINE_CVM));
        } else {
            Log.m286e(TAG, "tap&Go: AAC profile");
            getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_TA_GAC_DECLINE_NO_CVM));
        }
        if (getTransactionContext().getTransactionError() == null || getTransactionContext().getTransactionError() == MCTransactionResult.CONTEXT_CONFLICT_PASS) {
            getTransactionContext().setTransactionError(MCTransactionResult.TRANSACTION_COMPLETED_ERROR_AAC);
        }
    }

    private void arqc(GenerateACApdu generateACApdu) {
        byte p1 = generateACApdu.getP1();
        this.mCVR.setByte(0, (byte) (this.mCVR.getByte(0) | -96));
        getTransactionContext().getCryptoOutput().setCid(VerifyPINApdu.P2_PLAINTEXT);
        getTransactionContext().getTransactionInformation().setCid(VerifyPINApdu.P2_PLAINTEXT);
        if ((p1 & 16) == 16) {
            this.mCVR.setByte(1, (byte) (this.mCVR.getByte(1) | 64));
        }
        if (getTransactionContext().isAlternateAID()) {
            Log.m287i(TAG, "ARQC: Alternate profile");
            getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM));
        } else if (getTransactionContext().getTransactionCredentials().getCVMResult() != null && getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0) {
            Log.m287i(TAG, "ARQC: CL profile");
            getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_TA_GAC_ONLINE_CVM));
        } else if (getTransactionContext().getTransactionCredentials().getCVMResult() != null && getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() != 0) {
            Log.m287i(TAG, "tap&Go: ARQC profile");
            getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_TA_GAC_ONLINE_NO_CVM));
        }
    }

    private ByteArray computeIssuerApplicationData() {
        int i = 0;
        ByteArray clone = getTransactionContext().getCryptoOutput().getIssuerApplicationData().clone();
        Log.m285d(TAG, "computeIssuerApplicationData, iad: " + clone.getHexString());
        ByteArray cdol = this.mGenACApdu.getCDOL();
        for (int i2 = 0; i2 < 6; i2++) {
            clone.setByte(i2 + 2, this.mCVR.getByte(i2));
        }
        if (Utils.isZero(cdol.copyOfRange(32, 40))) {
            while (i < 2) {
                clone.setByte(i + 8, this.mGenACApdu.getDataAuthenticationCode().getByte(i));
                i++;
            }
        } else {
            while (i < 2) {
                clone.setByte(i + 8, cdol.copyOfRange(32, 34).getByte(i));
                i++;
            }
        }
        return clone;
    }

    private ByteArray cda(GenerateACApdu generateACApdu, ByteArray byteArray, ByteArray byteArray2, byte b, ByteArray byteArray3) {
        int i;
        int i2;
        int i3;
        ByteArray cdol = generateACApdu.getCDOL();
        ByteArray fromByteArray = ByteArrayFactory.getInstance().getFromByteArray(getTransactionContext().getPDOL());
        fromByteArray.append(cdol);
        fromByteArray.append(this.baf.getFromWord(-24793));
        fromByteArray.appendByte((byte) 1);
        fromByteArray.appendByte(b);
        fromByteArray.append(this.baf.getFromWord(-24778));
        fromByteArray.appendByte((byte) 2);
        fromByteArray.append(getTransactionContext().getTransactionCredentials().getATC());
        fromByteArray.append(TLV.create(this.baf.getFromWord(-24816), byteArray2));
        if (getTransactionContext().getPOSCII() != null) {
            fromByteArray.append(TLV.create(this.baf.getFromWord(-8373), getTransactionContext().getPOSCII().clone()));
        }
        ByteArray SHA1 = this.mCryptoService.SHA1(fromByteArray);
        ContactlessPaymentData contactlessPaymentData = getPaymentProfile().getContactlessPaymentData();
        int initRSAPrivateKey = this.mCryptoService.initRSAPrivateKey(contactlessPaymentData.getICC_privateKey_p(), contactlessPaymentData.getICC_privateKey_q(), contactlessPaymentData.getICC_privateKey_dp(), contactlessPaymentData.getICC_privateKey_dq(), contactlessPaymentData.getICC_privateKey_a());
        if (getTransactionContext().getRRPCounter() > 0) {
            i = 77;
            i2 = 52;
            i3 = (byte) 1;
        } else {
            i = 63;
            i2 = 38;
            i3 = 0;
        }
        ByteArray byteArray4 = ByteArrayFactory.getInstance().getByteArray(initRSAPrivateKey - i);
        byteArray4.fill((byte) -69);
        ByteArray byteArray5 = this.baf.getByteArray(1);
        byteArray5.setByte(0, (byte) 5);
        byteArray5.appendByte((byte) 1);
        byteArray5.appendByte((byte) i2);
        byteArray5.appendByte((byte) byteArray3.getLength());
        byteArray5.append(byteArray3);
        byteArray5.appendByte(b);
        byteArray5.append(byteArray);
        byteArray5.append(SHA1);
        if (i3 != 0) {
            byteArray5.append(getTransactionContext().getTerminalRREntropy());
            byteArray5.append(getTransactionContext().getDeviceRREntropy());
            byteArray5.append(contactlessPaymentData.getMinRRTime());
            byteArray5.append(contactlessPaymentData.getMaxRRTime());
            byteArray5.append(contactlessPaymentData.getTransmissionRRTime());
        }
        byteArray5.append(byteArray4);
        byteArray5.append(generateACApdu.getUnpredictableNumber());
        cdol = this.mCryptoService.SHA1(byteArray5);
        fromByteArray = this.baf.getByteArray(1);
        fromByteArray.setByte(0, (byte) 106);
        fromByteArray.append(byteArray5.copyOfRange(0, (initRSAPrivateKey - cdol.getLength()) - 2));
        fromByteArray.append(cdol);
        fromByteArray.appendByte(PSSSigner.TRAILER_IMPLICIT);
        return this.mCryptoService.RSA(fromByteArray);
    }

    public boolean checkCLA(byte b) {
        return b == -128;
    }
}
