/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

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
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;

public class MCAPDUCommandHandlerGAC
extends MCCAPDUBaseCommandHandler {
    public static final String TAG = "mcpce_MCAPDUCommandHandlerGAC";
    private ByteArray mCVR;
    private ContactlessPaymentData mClData;
    private final MCBPCryptoService mCryptoService = MCBPCryptoService.getInstance();
    private GenerateACApdu mGenACApdu;
    private byte mP1;
    private ByteArray mUnmaskedCVR;

    /*
     * Enabled aggressive block sorting
     */
    private void aac(GenerateACApdu generateACApdu) {
        byte by = this.mCVR.getByte(0);
        this.mCVR.setByte(0, (byte)(by | -128));
        this.getTransactionContext().getCryptoOutput().setCid((byte)0);
        this.getTransactionContext().getTransactionInformation().setCid((byte)0);
        if (!(this.mP1 != 16 || this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() != 0L && this.getTransactionContext().getTransactionCredentials().getCVMResult().isCVMRequired())) {
            byte by2 = this.mCVR.getByte(1);
            this.mCVR.setByte(1, (byte)(by2 | 64));
        }
        if (this.getTransactionContext().isAlternateAID()) {
            Log.e(TAG, "AAC: Alternate profile");
            this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM));
        } else if (this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0L) {
            Log.e(TAG, "AAC: CL profile");
            this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_GAC_DECLINE_CVM));
        } else {
            Log.e(TAG, "tap&Go: AAC profile");
            this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_GAC_DECLINE_NO_CVM));
        }
        if (this.getTransactionContext().getTransactionError() == null || this.getTransactionContext().getTransactionError() == MCTransactionResult.CONTEXT_CONFLICT_PASS) {
            this.getTransactionContext().setTransactionError(MCTransactionResult.TRANSACTION_COMPLETED_ERROR_AAC);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void arqc(GenerateACApdu generateACApdu) {
        byte by = generateACApdu.getP1();
        byte by2 = this.mCVR.getByte(0);
        this.mCVR.setByte(0, (byte)(by2 | -96));
        this.getTransactionContext().getCryptoOutput().setCid((byte)-128);
        this.getTransactionContext().getTransactionInformation().setCid((byte)-128);
        if ((by & 16) == 16) {
            byte by3 = this.mCVR.getByte(1);
            this.mCVR.setByte(1, (byte)(by3 | 64));
        }
        if (this.getTransactionContext().isAlternateAID()) {
            Log.i(TAG, "ARQC: Alternate profile");
            this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM));
            return;
        } else {
            if (this.getTransactionContext().getTransactionCredentials().getCVMResult() != null && this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0L) {
                Log.i(TAG, "ARQC: CL profile");
                this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_GAC_ONLINE_CVM));
                return;
            }
            if (this.getTransactionContext().getTransactionCredentials().getCVMResult() == null || this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0L) return;
            {
                Log.i(TAG, "tap&Go: ARQC profile");
                this.getTransactionContext().getTransactionCredentials().setProfileType(this.getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_TA_GAC_ONLINE_NO_CVM));
                return;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private ByteArray cda(GenerateACApdu generateACApdu, ByteArray byteArray, ByteArray byteArray2, byte by, ByteArray byteArray3) {
        byte by2;
        int n2;
        boolean bl;
        ByteArray byteArray4 = generateACApdu.getCDOL();
        ByteArray byteArray5 = ByteArrayFactory.getInstance().getFromByteArray(this.getTransactionContext().getPDOL());
        byteArray5.append(byteArray4);
        byteArray5.append(this.baf.getFromWord(-24793));
        byteArray5.appendByte((byte)1);
        byteArray5.appendByte(by);
        byteArray5.append(this.baf.getFromWord(-24778));
        byteArray5.appendByte((byte)2);
        byteArray5.append(this.getTransactionContext().getTransactionCredentials().getATC());
        byteArray5.append(TLV.create(this.baf.getFromWord(-24816), byteArray2));
        if (this.getTransactionContext().getPOSCII() != null) {
            byteArray5.append(TLV.create(this.baf.getFromWord(-8373), this.getTransactionContext().getPOSCII().clone()));
        }
        ByteArray byteArray6 = this.mCryptoService.SHA1(byteArray5);
        ContactlessPaymentData contactlessPaymentData = this.getPaymentProfile().getContactlessPaymentData();
        int n3 = this.mCryptoService.initRSAPrivateKey(contactlessPaymentData.getICC_privateKey_p(), contactlessPaymentData.getICC_privateKey_q(), contactlessPaymentData.getICC_privateKey_dp(), contactlessPaymentData.getICC_privateKey_dq(), contactlessPaymentData.getICC_privateKey_a());
        if (this.getTransactionContext().getRRPCounter() > 0) {
            n2 = 77;
            by2 = 52;
            bl = true;
        } else {
            n2 = 63;
            by2 = 38;
            bl = false;
        }
        ByteArray byteArray7 = ByteArrayFactory.getInstance().getByteArray(n3 - n2);
        byteArray7.fill((byte)-69);
        ByteArray byteArray8 = this.baf.getByteArray(1);
        byteArray8.setByte(0, (byte)5);
        byteArray8.appendByte((byte)1);
        byteArray8.appendByte(by2);
        byteArray8.appendByte((byte)byteArray3.getLength());
        byteArray8.append(byteArray3);
        byteArray8.appendByte(by);
        byteArray8.append(byteArray);
        byteArray8.append(byteArray6);
        if (bl) {
            byteArray8.append(this.getTransactionContext().getTerminalRREntropy());
            byteArray8.append(this.getTransactionContext().getDeviceRREntropy());
            byteArray8.append(contactlessPaymentData.getMinRRTime());
            byteArray8.append(contactlessPaymentData.getMaxRRTime());
            byteArray8.append(contactlessPaymentData.getTransmissionRRTime());
        }
        byteArray8.append(byteArray7);
        byteArray8.append(generateACApdu.getUnpredictableNumber());
        ByteArray byteArray9 = this.mCryptoService.SHA1(byteArray8);
        ByteArray byteArray10 = this.baf.getByteArray(1);
        byteArray10.setByte(0, (byte)106);
        byteArray10.append(byteArray8.copyOfRange(0, -2 + (n3 - byteArray9.getLength())));
        byteArray10.append(byteArray9);
        byteArray10.appendByte((byte)-68);
        return this.mCryptoService.RSA(byteArray10);
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void checkCVRFromTerminal() {
        this.mCVR = this.getPaymentProfile().getContactlessPaymentData().getIssuerApplicationData().clone().copyOfRange(2, 8);
        byte by = this.mCVR.getByte(3);
        if (this.getPaymentProfile().getCardRiskManagementData().getCRM_CountryCode().isEqual(this.mGenACApdu.getTerminalCountryCode())) {
            this.mCVR.setByte(3, (byte)(by | 2));
        } else {
            this.mCVR.setByte(3, (byte)(by | 4));
        }
        if ((3 & this.mClData.getCVR_MaskAnd().getByte(5)) != 0 || (3 & this.mClData.getCIAC_Decline().getByte(2)) != 0) {
            CheckTable.processAddCheckTable(this.mGenACApdu.getCDOL(), this.getPaymentProfile().getCardRiskManagementData().getAdditionalCheckTable(), this.mCVR);
        }
        byte by2 = this.mCVR.getByte(0);
        if (this.getTransactionContext().getTransactionCredentials().getCVMResult() != null && this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0L) {
            this.mCVR.setByte(0, (byte)(by2 | 5));
        } else {
            this.mCVR.setByte(3, (byte)(by2 | 32));
        }
        this.checkContext();
        if (this.getTransactionContext().getRRPCounter() == 0) {
            this.mCVR.setByte(3, (byte)(64 | this.mCVR.getByte(3)));
            return;
        }
        ByteArray byteArray = this.getTransactionContext().getTransactionInformation().getUN();
        if (byteArray == null) {
            throw new MCTransactionException("GAC check CVR from terminal: UN number is empty");
        }
        ByteArray byteArray2 = this.getTransactionContext().getTerminalRREntropy();
        if (byteArray2 == null) throw new MCTransactionException("GAC check CVR from terminal: Terminal RRE: wrong value");
        int n2 = byteArray.getLength();
        int n3 = byteArray2.getLength();
        int n4 = 0;
        if (n2 != n3) throw new MCTransactionException("GAC check CVR from terminal: Terminal RRE: wrong value");
        do {
            if (n4 >= byteArray.getLength()) {
                ByteArray byteArray3 = this.mGenACApdu.getTerminalVerificationResults();
                if (byteArray3 == null) throw new MCTransactionException("GAC check CVR from terminal: Terminal RRE: wrong tvr length");
                if (byteArray3.getLength() <= 4) throw new MCTransactionException("GAC check CVR from terminal: Terminal RRE: wrong tvr length");
                if ((2 & byteArray3.getByte(4)) == 2) return;
                throw new MCTransactionException("GAC check CVR from terminal: TVR RRP not performed");
            }
            if (byteArray.getByte(n4) != byteArray2.getByte(n4)) {
                throw new MCTransactionException("GAC check CVR from terminal: UN number is not equal terminal entropy.");
            }
            ++n4;
        } while (true);
    }

    private boolean checkMchipParameters() {
        if (this.mClData.getCIAC_Decline() == null) {
            Log.e(TAG, "GAC check MChip parameters: CIAC_decline is null.");
        }
        if (this.mClData.getCVR_MaskAnd() == null) {
            Log.e(TAG, "GAC check MChip parameters: CVRMaskAnd is null.");
        }
        if (this.mClData.getIssuerApplicationData() == null) {
            Log.e(TAG, "GAC check MChip parameters: issuer application data is null.");
        }
        if (this.mClData.getICC_privateKey_a() == null) {
            Log.e(TAG, "GAC check MChip parameters: ICC_privateKey_a is null.");
        }
        return this.mClData.getCIAC_Decline() != null && this.mClData.getCVR_MaskAnd() != null && this.mClData.getIssuerApplicationData() != null && this.mClData.getICC_privateKey_a() != null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private MCCommandResult composeCardholderVerificationResult() {
        byte by = 1;
        Log.i(TAG, "check CVR");
        try {
            this.checkCVRFromTerminal();
        }
        catch (MCTransactionException mCTransactionException) {
            Log.e(TAG, "Unexpected MCTransactionException: " + mCTransactionException.getMessage());
            mCTransactionException.printStackTrace();
            this.aac(this.mGenACApdu);
            return this.completeCommand();
        }
        Log.i(TAG, "check CVM");
        byte by2 = (byte)(-64 & this.mP1);
        if (by2 == 0) {
            this.getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
            this.getTransactionContext().setPOSCII(this.baf.getByteArray(3));
            Log.e(TAG, "GAC compose CVR: AAC requested by terminal, p1 = " + by2);
            this.aac(this.mGenACApdu);
            return this.completeCommand();
        }
        if (!Utils.isZero(this.mCVR.copyOfRange(3, 6).bitWiseAnd(this.mClData.getCIAC_Decline()))) {
            this.getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
            this.getTransactionContext().setPOSCII(this.baf.getByteArray(3));
            Log.e(TAG, "GAC compose CVR: found match in CIAC_decline");
            this.aac(this.mGenACApdu);
            return this.completeCommand();
        }
        if (this.getTransactionContext().getTransactionCredentials().getCVMResult() == null || this.getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() != 0L) {
            Log.i(TAG, "tap&Go: CVM check flow");
            ByteArray byteArray = this.mGenACApdu.getCvmResults();
            byte by3 = (byte)(63 & byteArray.getByte(0));
            if (by3 != by && by3 != 4 || byteArray.getByte(2) != 2) {
                by = 0;
            }
            if (by != 0) {
                byte by4 = this.mCVR.getByte(3);
                this.mCVR.setByte(3, (byte)(by4 | 1));
                this.errorCdcvmRequired();
                Log.e(TAG, "tap&go: return AAC 5.5");
                this.aac(this.mGenACApdu);
                return this.completeCommand();
            }
            if (this.getTransactionContext().getTransactionCredentials().getCVMResult() != null && this.getTransactionContext().getTransactionCredentials().getCVMResult().isCVMRequired()) {
                this.errorCdcvmRequired();
                Log.e(TAG, "tap&go: return AAC 5.7");
                this.aac(this.mGenACApdu);
                return this.completeCommand();
            }
        }
        this.getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
        if (!this.getTransactionContext().isOnlineAllowed()) {
            this.getTransactionContext().setPOSCII(this.baf.getByteArray(3));
            Log.e(TAG, "GAC compose CVR: online transaction not alowed, AAC");
            this.aac(this.mGenACApdu);
            return this.completeCommand();
        }
        this.arqc(this.mGenACApdu);
        Log.d(TAG, "CVM OK, ARQC");
        return this.completeCommand();
    }

    private byte[] computeCryptoInputData1() {
        ByteArray byteArray = this.mGenACApdu.getCDOL();
        return this.baf.getFromByteArray(byteArray.copyOfRange(0, 29)).getBytes();
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] computeCryptoInputData2() {
        ContactlessPaymentData contactlessPaymentData;
        int n2 = 255;
        Log.i(TAG, "Compute input data 2");
        if (this.getTransactionContext().isAlternateAID()) {
            AlternateContactlessPaymentData alternateContactlessPaymentData = this.getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData();
            contactlessPaymentData = this.getPaymentProfile().getContactlessPaymentData();
            contactlessPaymentData.setAID(alternateContactlessPaymentData.getAID());
            contactlessPaymentData.setPaymentFCI(alternateContactlessPaymentData.getPaymentFCI());
            contactlessPaymentData.setGPO_Response(alternateContactlessPaymentData.getGPO_Response());
            contactlessPaymentData.setCVR_MaskAnd(alternateContactlessPaymentData.getCVR_MaskAnd());
            contactlessPaymentData.setCIAC_Decline(alternateContactlessPaymentData.getCIAC_Decline());
        } else {
            contactlessPaymentData = this.getPaymentProfile().getContactlessPaymentData();
        }
        this.mUnmaskedCVR = this.mCVR.clone();
        this.mCVR = this.mCVR.bitWiseAnd(contactlessPaymentData.getCVR_MaskAnd());
        ByteArray byteArray = this.mCVR.copyOfRange(1, this.mCVR.getLength());
        Log.d(TAG, "Compute input data 2 Done");
        if ((64 & this.mUnmaskedCVR.getByte(3)) != 64) {
            Log.d(TAG, "RRP MAX RRP time " + Utils.readShort(contactlessPaymentData.getMaxRRTime()));
            Log.d(TAG, "RRP MIN RRP time " + Utils.readShort(contactlessPaymentData.getMinRRTime()));
            ByteArray byteArray2 = this.getTransactionContext().getCryptoOutput().getIssuerApplicationData();
            int n3 = 65535 & Utils.readShort(contactlessPaymentData.getMaxRRTime()) / 10;
            if (n3 > n2) {
                n3 = n2;
            }
            byteArray2.setByte(17, (byte)n3);
            int n4 = 65535 & Utils.readShort(contactlessPaymentData.getMinRRTime()) / 10;
            if (n4 <= n2) {
                n2 = n4;
            }
            byteArray2.setByte(25, (byte)n2);
            this.getTransactionContext().getCryptoOutput().setIssuerApplicationData(byteArray2);
            contactlessPaymentData.setIssuerApplicationData(byteArray2);
            Log.d(TAG, "RRP performed: IAD after RRP counters " + contactlessPaymentData.getIssuerApplicationData().getHexString());
        }
        if ((1 & this.getTransactionContext().getCryptoOutput().getIssuerApplicationData().getByte(1)) == 1) {
            Log.i(TAG, "CVN requires to add counters.");
            byteArray.append(this.getTransactionContext().getCryptoOutput().getIssuerApplicationData().copyOfRange(10, 26));
        }
        return byteArray.clone().getBytes();
    }

    private ByteArray computeIssuerApplicationData() {
        ByteArray byteArray;
        int n2;
        byteArray = this.getTransactionContext().getCryptoOutput().getIssuerApplicationData().clone();
        Log.d(TAG, "computeIssuerApplicationData, iad: " + byteArray.getHexString());
        ByteArray byteArray2 = this.mGenACApdu.getCDOL();
        for (int i2 = 0; i2 < 6; ++i2) {
            byteArray.setByte(i2 + 2, this.mCVR.getByte(i2));
        }
        boolean bl = Utils.isZero(byteArray2.copyOfRange(32, 40));
        if (bl) {
            for (n2 = 0; n2 < 2; ++n2) {
                byteArray.setByte(n2 + 8, this.mGenACApdu.getDataAuthenticationCode().getByte(n2));
            }
        } else {
            while (n2 < 2) {
                byteArray.setByte(n2 + 8, byteArray2.copyOfRange(32, 34).getByte(n2));
                ++n2;
            }
        }
        return byteArray;
    }

    private void errorCdcvmRequired() {
        byte by = this.mCVR.getByte(5);
        this.mCVR.setByte(5, (byte)(by | 8));
        this.getTransactionContext().setTransactionResult(MCTransactionResult.CONTEXT_CONFLICT_CVM);
        this.getTransactionContext().setTransactionError(MCTransactionResult.CONTEXT_CONFLICT_CVM);
        ByteArray byteArray = this.baf.getByteArray(3);
        byteArray.setByte(1, (byte)1);
        this.getTransactionContext().setPOSCII(byteArray);
    }

    private MCCommandResult initializeTransactionContext() {
        Log.i(TAG, "initTransactionContext...");
        if (MCAPDUCommandHandlerGAC.isTerminalOffline(this.mGenACApdu.getTerminalType())) {
            Log.e(TAG, "GAC check transaction context: offline terminal.");
            return this.ERROR(27013);
        }
        MCTransactionInformation mCTransactionInformation = this.getTransactionContext().getTransactionInformation();
        mCTransactionInformation.setAmount(this.mGenACApdu.getAuthorizedAmount());
        mCTransactionInformation.setCurrencyCode(this.mGenACApdu.getTransactionCurrencyCode());
        mCTransactionInformation.setTransactionDate(this.mGenACApdu.getTransactionDate());
        mCTransactionInformation.setTransactionType(this.mGenACApdu.getTransactionType());
        mCTransactionInformation.setUN(this.mGenACApdu.getUnpredictableNumber());
        mCTransactionInformation.setMccCategory(this.mGenACApdu.getMerchantCategoryCode());
        mCTransactionInformation.setMerchantNameAndLoc(this.mGenACApdu.getMerchantNameLocation());
        return this.completeCommand();
    }

    @Override
    public boolean checkCLA(byte by) {
        return by == -128;
    }

    @Override
    public MCCommandResult checkP1P2Parameters(byte by, byte by2) {
        Log.d(TAG, "GAC check p1/p2: start checking P1/P2 parameters");
        if ((by & 47) != 0 || by2 != 0 || (by & 192) == 192) {
            Log.e(TAG, "GAC check p1/p2: p1 = " + by + ", p2 = " + by2);
            return this.ERROR(27270);
        }
        Log.d(TAG, "GAC check p1/p2: Checking P1/P2 parameters OK");
        return this.completeCommand();
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public MCCommandResult generateResponseAPDU() {
        boolean bl;
        ByteArray byteArray2;
        ByteArray byteArray;
        block7 : {
            ByteArray byteArray3;
            block6 : {
                byteArray3 = this.getTransactionContext().getCryptoOutput().getCryptogram().clone();
                byteArray = this.computeIssuerApplicationData();
                byte by = this.mUnmaskedCVR.getByte(1);
                int n2 = by & 64;
                boolean bl2 = false;
                if (n2 == 64) {
                    ByteArray byteArray4;
                    if (this.getTransactionContext().getTransactionCredentials().getIDN() == null) break block6;
                    ByteArray byteArray5 = this.getTransactionContext().getTransactionCredentials().getIDN().copyOfRange(8, 16);
                    byteArray3 = byteArray4 = this.cda(this.mGenACApdu, byteArray3, byteArray, this.getTransactionContext().getCryptoOutput().getCid(), byteArray5);
                    bl2 = true;
                }
                bl = bl2;
                byteArray2 = byteArray3;
                break block7;
            }
            Log.e(TAG, "GAC generate RAPDU: IDN is null.");
            return this.ERROR(27013);
            catch (Exception exception) {
                Exception exception2;
                block8 : {
                    bl = false;
                    exception2 = exception;
                    break block8;
                    catch (Exception exception3) {
                        bl = true;
                    }
                }
                Log.e(TAG, "GAC generate RAPDU: Unexpected exception: " + exception2.getMessage());
                exception2.printStackTrace();
                byteArray2 = byteArray3;
            }
        }
        ByteArray byteArray6 = this.getTransactionContext().getPOSCII();
        GenACRespApdu genACRespApdu = new GenACRespApdu(bl, byteArray2, this.getTransactionContext().getTransactionCredentials().getATC(), this.getTransactionContext().getCryptoOutput().getCid(), byteArray, byteArray6);
        this.getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
        return this.completeTransaction(genACRespApdu.getByteArray().clone());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public MCCommandResult processCommand(ByteArray byteArray) {
        byte[] arrby;
        MCCommandResult mCCommandResult = this.verifyPaymentProfile(byteArray);
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)mCCommandResult.getResponseCode())) {
            Log.e(TAG, "GAC process command: verify payment profile failed.");
            return mCCommandResult;
        }
        MCCommandResult mCCommandResult2 = this.initializeTransactionContext();
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)mCCommandResult2.getResponseCode())) {
            Log.e(TAG, "GAC process command: init transaction context failed.");
            return mCCommandResult2;
        }
        MCCryptoOutput mCCryptoOutput = new MCCryptoOutput();
        if (this.getTransactionContext().isAlternateAID()) {
            mCCryptoOutput.setIssuerApplicationData(this.mPaymentProfile.getContactlessPaymentData().getAlternateContactlessPaymentData().getIssuerApplicationData().clone());
            Log.d(TAG, "processCommand, iad: " + mCCryptoOutput.getIssuerApplicationData().getHexString());
        } else {
            mCCryptoOutput.setIssuerApplicationData(this.mPaymentProfile.getContactlessPaymentData().getIssuerApplicationData().clone());
        }
        this.getTransactionContext().setCryptoOutput(mCCryptoOutput);
        MCCommandResult mCCommandResult3 = this.composeCardholderVerificationResult();
        if (!MCTransactionResult.COMMAND_COMPLETED.equals((Object)mCCommandResult3.getResponseCode())) {
            Log.e(TAG, "GAC process command: compose CVR failed.");
            return mCCommandResult3;
        }
        byte[] arrby2 = this.computeCryptoInputData1();
        if (arrby2 == null) {
            Log.e(TAG, "GAC process command: input data 1 is null.");
            return this.ERROR(27013);
        }
        byte[] arrby3 = this.computeCryptoInputData2();
        if (arrby3 == null) {
            Log.e(TAG, "GAC process command: input data 2 is null.");
            return this.ERROR(27013);
        }
        if (this.getTransactionContext().getTransactionCredentials().getATC() == null) {
            Log.e(TAG, "GAC process command: ATC value is empty...");
            return this.ERROR(27013);
        }
        if (this.getTransactionContext().getTransactionCredentials().getProfileType() == 0) {
            Log.e(TAG, "GAC process command: cannot find correct crypto profile...");
            return this.ERROR(27013);
        }
        try {
            byte[] arrby4;
            arrby = arrby4 = McTAController.getInstance().generateMAC(this.getTransactionContext().getTransactionCredentials().getProfileType(), arrby2, arrby3, this.getTransactionContext().getTransactionInformation().getUN().getBytes());
        }
        catch (Exception exception) {
            Log.e(TAG, "GAC process command: unexpected TAException");
            exception.printStackTrace();
            arrby = null;
        }
        if (arrby != null) {
            this.getTransactionContext().getCryptoOutput().setCryptogram(this.baf.getByteArray((byte[])arrby.clone(), arrby.length));
            return this.generateResponseAPDU();
        }
        Log.e(TAG, "GAC process command: ");
        return this.ERROR(27013);
    }

    /*
     * Enabled aggressive block sorting
     */
    public MCCommandResult verifyPaymentProfile(ByteArray byteArray) {
        Log.i(TAG, "Start checkPaymentData...");
        this.mGenACApdu = new GenerateACApdu(byteArray);
        this.mP1 = byteArray.getByte(2);
        int n2 = 255 & byteArray.getByte(4);
        if (this.getTransactionContext().isAlternateAID()) {
            AlternateContactlessPaymentData alternateContactlessPaymentData = this.getPaymentProfile().getContactlessPaymentData().getAlternateContactlessPaymentData();
            this.mClData = this.getPaymentProfile().getContactlessPaymentData();
            this.mClData.setAID(alternateContactlessPaymentData.getAID());
            this.mClData.setPaymentFCI(alternateContactlessPaymentData.getPaymentFCI());
            this.mClData.setGPO_Response(alternateContactlessPaymentData.getGPO_Response());
            this.mClData.setCVR_MaskAnd(alternateContactlessPaymentData.getCVR_MaskAnd());
            this.mClData.setCIAC_Decline(alternateContactlessPaymentData.getCIAC_Decline());
        } else {
            this.mClData = this.getPaymentProfile().getContactlessPaymentData();
        }
        if (!this.checkMchipParameters()) {
            Log.e(TAG, "GAC process command: check MCHIP parameters failed.");
            return this.ERROR(27013);
        }
        if (n2 >= 43 && n2 == this.mClData.getCDOL1_RelatedDataLength()) {
            return this.completeCommand();
        }
        Log.e(TAG, "GAC: lc wrong length, lc: " + n2 + ", CDOL1 related data length: " + this.mClData.getCDOL1_RelatedDataLength());
        return this.ERROR(26368);
    }
}

