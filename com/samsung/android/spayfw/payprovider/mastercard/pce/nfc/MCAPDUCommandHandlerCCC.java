package com.samsung.android.spayfw.payprovider.mastercard.pce.nfc;

import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.TLV;
import com.mastercard.mobile_api.utils.apdu.emv.CCCRespApdu;
import com.mastercard.mobile_api.utils.apdu.emv.ComputeCCApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCryptoOutput;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable.TAProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionInformation;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.TAComputeCC.TAComputeCCResponse.TAComputeCCOut;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class MCAPDUCommandHandlerCCC extends MCCAPDUBaseCommandHandler {
    public static final String TAG = "mcpce_MCAPDUCommandHandlerCCC";
    private ComputeCCApdu mCCApdu;

    public MCCommandResult processCommand(ByteArray byteArray) {
        MCCommandResult verifyPaymentProfile = verifyPaymentProfile(byteArray);
        if (MCTransactionResult.COMMAND_COMPLETED.equals(verifyPaymentProfile.getResponseCode())) {
            verifyPaymentProfile = initializeTransactionContext();
            if (MCTransactionResult.COMMAND_COMPLETED.equals(verifyPaymentProfile.getResponseCode())) {
                MCCryptoOutput mCCryptoOutput = new MCCryptoOutput();
                mCCryptoOutput.setIssuerApplicationData(this.mPaymentProfile.getContactlessPaymentData().getIssuerApplicationData().clone());
                getTransactionContext().setCryptoOutput(mCCryptoOutput);
                if (getTASecureContainer() == null) {
                    Log.m286e(TAG, "ComputeCC: secure object not found");
                    return ERROR(27013);
                }
                verifyPaymentProfile = composeCardholderVerificationResult();
                if (MCTransactionResult.COMMAND_COMPLETED.equals(verifyPaymentProfile.getResponseCode())) {
                    byte[] computeCryptoInputData1 = computeCryptoInputData1();
                    if (computeCryptoInputData1 == null) {
                        Log.m286e(TAG, "ComputeCC: input data 1 is empty for MagStripe transaction.");
                        return ERROR(27013);
                    } else if (getTransactionContext().getTransactionCredentials().getATC() == null) {
                        Log.m286e(TAG, "ComputeCC: ATC value is empty...");
                        return ERROR(27013);
                    } else if (getTransactionContext().getTransactionCredentials().getProfileType() == 0) {
                        Log.m286e(TAG, "ComputeCC: Cannot find correct crypto profile...");
                        return ERROR(27013);
                    } else {
                        try {
                            McTAController instance = McTAController.getInstance();
                            int profileType = getTransactionContext().getTransactionCredentials().getProfileType();
                            TAComputeCCOut computeCC = instance.computeCC(profileType, profileType + 1, computeCryptoInputData1, getTransactionContext().getTransactionInformation().getUN().getBytes());
                            if (computeCC == null) {
                                Log.m286e(TAG, "ComputeCC processCommand: ComputeCC result is null returned from TA.");
                                return ERROR(27013);
                            } else if (computeCC._taMACTrack1 == null) {
                                Log.m286e(TAG, "ComputeCC processCommand: failed to compute track1 data.");
                                return ERROR(27013);
                            } else if (computeCC._taMACTrack2 == null) {
                                Log.m286e(TAG, "ComputeCC processCommand: failed to compute track1 data.");
                                return ERROR(27013);
                            } else {
                                Object data = computeCC._taMACTrack1.getData();
                                if (data == null || data.length != 8) {
                                    Log.m286e(TAG, "ComputeCC: wrong track1 cryptogram length...");
                                    return ERROR(27010);
                                }
                                Object data2 = computeCC._taMACTrack2.getData();
                                if (data2 == null || data2.length != 8) {
                                    Log.m286e(TAG, "ComputeCC: wrong track2 cryptogram length...");
                                    return ERROR(27010);
                                }
                                getTransactionContext().getCryptoOutput().setCryptogram(this.baf.getByteArray((byte[]) data.clone(), data.length));
                                getTransactionContext().getCryptoOutput().setCryptogramTrack2(this.baf.getByteArray((byte[]) data2.clone(), data2.length));
                                return generateResponseAPDU();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.m286e(TAG, "Unexpected TAException: " + e.toString());
                            return ERROR(27013);
                        }
                    }
                }
                Log.m286e(TAG, "ComputeCC: compose CVR failed, response code " + verifyPaymentProfile.getResponseCode());
                return verifyPaymentProfile;
            }
            Log.m286e(TAG, "ComputeCC: initialize transaction context failed, error code " + verifyPaymentProfile.getResponseCode());
            return verifyPaymentProfile;
        }
        Log.m286e(TAG, "ComputeCC: verify payment profile failed, error code " + verifyPaymentProfile.getResponseCode());
        return verifyPaymentProfile;
    }

    private MCCommandResult verifyPaymentProfile(ByteArray byteArray) {
        if (byteArray == null || byteArray.getLength() <= 4) {
            Log.m286e(TAG, "ComputeCC verifyPaymentProfile: wronng apdu length.");
            return ERROR(26368);
        }
        int i = byteArray.getByte(4) & GF2Field.MASK;
        if (i != 20 && i != 16) {
            Log.m286e(TAG, "ComputeCC verifyPaymentProfile: wronng lc length.");
            return ERROR(26368);
        } else if (this.mPaymentProfile.getContactlessPaymentData().getCIAC_DeclineOnPPMS() == null) {
            Log.m286e(TAG, "ComputeCC verifyPaymentProfile: CIAC_DeclineOnPPMS is empty.");
            return ERROR(27013);
        } else {
            this.mCCApdu = new ComputeCCApdu(byteArray);
            return completeCommand();
        }
    }

    private MCCommandResult initializeTransactionContext() {
        if (MCCAPDUBaseCommandHandler.isTerminalOffline(this.mCCApdu.getTerminalType())) {
            Log.m286e(TAG, "ComputeCC initializeTransactionContext: offline terminal.");
            return ERROR(27013);
        }
        MCTransactionInformation transactionInformation = getTransactionContext().getTransactionInformation();
        transactionInformation.setAmount(this.mCCApdu.getAuthorizedAmount().clone());
        transactionInformation.setCurrencyCode(this.mCCApdu.getTransactionCurrencyCode());
        transactionInformation.setTransactionDate(this.mCCApdu.getTransactionDate());
        transactionInformation.setTransactionType(this.mCCApdu.getTransactionType());
        transactionInformation.setUN(this.mCCApdu.getUnpredictableNumber());
        Log.m287i(TAG, "Init transaction context and check terminal type OK");
        return completeCommand();
    }

    private MCCommandResult composeCardholderVerificationResult() {
        Log.m287i(TAG, "Start checking CVM/CC Context/ Card risk management data");
        ByteArray byteArray = this.baf.getByteArray(3);
        if (getTransactionContext().getTransactionCredentials().getCVMResult() != null && getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0) {
            byteArray.setByte(1, Tnaf.POW_2_WIDTH);
        }
        getTransactionContext().setPOSCII(byteArray);
        if (getPaymentProfile().getCardRiskManagementData().getCRM_CountryCode().isEqual(this.mCCApdu.getTerminalCountryCode())) {
            byteArray = this.baf.getFromWord(SkeinMac.SKEIN_512);
            if (getPaymentProfile().getContactlessPaymentData().getCIAC_DeclineOnPPMS().bitWiseAnd(byteArray).isEqual(byteArray)) {
                Log.m286e(TAG, "ComputeCC CIAC_DeclineOnPPMS failed: bb0200");
                return completeTransaction(decline(this.mCCApdu));
            }
        }
        byteArray = this.baf.getFromWord(SkeinMac.SKEIN_1024);
        if (getPaymentProfile().getContactlessPaymentData().getCIAC_DeclineOnPPMS().bitWiseAnd(byteArray).isEqual(byteArray)) {
            Log.m286e(TAG, "ComputeCC CIAC_DeclineOnPPMS failed: bb0400");
            return completeTransaction(decline(this.mCCApdu));
        }
        if (getTransactionContext().getTransactionCredentials().getCVMResult() == null || getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() != 0) {
            Log.m286e(TAG, "ComputeCC 3.3 rejection as CVM required");
            getTransactionContext().setTransactionResult(MCTransactionResult.CONTEXT_CONFLICT_CVM);
            getTransactionContext().setTransactionError(MCTransactionResult.CONTEXT_CONFLICT_CVM);
            getTransactionContext().getPOSCII().setByte(1, (byte) (getTransactionContext().getPOSCII().getByte(1) | 1));
            return completeTransaction(decline(this.mCCApdu));
        } else if (getTransactionContext().isOnlineAllowed()) {
            getTransactionContext().getTransactionInformation().setCid(VerifyPINApdu.P2_PLAINTEXT);
            byte b = this.mCCApdu.getMobileSupportIndicator().getByte(0);
            if (getTransactionContext().getTransactionCredentials().getCVMResult() != null && getTransactionContext().getTransactionCredentials().getCVMResult().getResultCode() == 0 && (b & 1) == 1) {
                getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_TA_TRACK1_CVM));
            } else {
                getTransactionContext().getTransactionCredentials().setProfileType(getTransactionContext().getTransactionCredentials().getTAProfilesTable().getTAProfileReference(TAProfile.PROFILE_CL_TA_TRACK1_NO_CVM));
            }
            Log.m287i(TAG, "Compute CC: start checking CVM/CC Context/ Card risk management data OK");
            return completeCommand();
        } else {
            Log.m286e(TAG, "ComputeCC online transaction is not allowed.");
            return completeTransaction(decline(this.mCCApdu));
        }
    }

    private byte[] computeCryptoInputData1() {
        return this.mCCApdu.getUnpredictableNumber().getBytes();
    }

    public MCCommandResult checkP1P2Parameters(byte b, byte b2) {
        if (b == -114 && b2 == -128) {
            return completeCommand();
        }
        Log.m286e(TAG, "Compute CC: incorrect p1/p2 params, p1 = " + b + ", p2 = " + b2 + "; expected p1 = " + CipherSuite.TLS_DHE_PSK_WITH_RC4_128_SHA + ", expected p2 = " + X509KeyUsage.digitalSignature);
        return ERROR(27270);
    }

    public MCCommandResult generateResponseAPDU() {
        if (getTransactionContext().getCryptoOutput().getCryptogram() == null || getTransactionContext().getCryptoOutput().getCryptogram().getLength() != 8) {
            if (getTransactionContext().getCryptoOutput().getCryptogram() != null) {
                Log.m286e(TAG, "Compute CC: cryptogram track1 len is invalid: " + getTransactionContext().getCryptoOutput().getCryptogram().getLength());
            } else {
                Log.m286e(TAG, "Compute CC: empty cryptogram track1 ");
            }
            return ERROR(27010);
        }
        Log.m285d(TAG, "Compute CC: Calling generateRAPDU..." + getTransactionContext().getCryptoOutput().getCryptogram().getHexString());
        if (getTransactionContext().getCryptoOutput().getCryptogramTrack2() == null || getTransactionContext().getCryptoOutput().getCryptogramTrack2().getLength() != 8) {
            if (getTransactionContext().getCryptoOutput().getCryptogramTrack2() != null) {
                Log.m286e(TAG, "Compute CC: cryptogram track2 len is invalid: " + getTransactionContext().getCryptoOutput().getCryptogramTrack2().getLength());
            } else {
                Log.m286e(TAG, "Compute CC: empty cryptogram track2 ");
            }
            return ERROR(27010);
        }
        ByteArray fromWord = this.baf.getFromWord(-24735);
        fromWord.appendByte((byte) 2);
        fromWord.append(getTransactionContext().getCryptoOutput().getCryptogramTrack2().copyOfRange(6, 8));
        fromWord.append(this.baf.getFromWord(-24736));
        fromWord.appendByte((byte) 2);
        fromWord.append(getTransactionContext().getCryptoOutput().getCryptogram().copyOfRange(6, 8));
        fromWord.append(this.baf.getFromWord(-24778));
        fromWord.appendByte((byte) 2);
        fromWord.append(getTransactionContext().getTransactionCredentials().getATC());
        Log.m287i(TAG, "Compute CC: Calling generateRAPDU...POCII");
        if ((this.mCCApdu.getMobileSupportIndicator().getByte(0) & 1) == 1) {
            getTransactionContext().getPOSCII().setByte(1, (byte) (getTransactionContext().getPOSCII().getByte(1) | 16));
            fromWord.append(TLV.create(this.baf.getFromWord(-8373), getTransactionContext().getPOSCII().clone()));
        }
        Log.m287i(TAG, "Compute CC: Calling generateRAPDU...OK");
        getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
        return completeTransaction(new CCCRespApdu(fromWord).getByteArray());
    }

    private ByteArray decline(ComputeCCApdu computeCCApdu) {
        ByteArray fromWord;
        Log.m286e(TAG, "Compute CC: start decline transaction");
        getTransactionContext().getTransactionInformation().setCid((byte) 0);
        if ((computeCCApdu.getMobileSupportIndicator().getByte(0) & 1) != 1) {
            Log.m286e(TAG, "Compute CC: failed online support indicator mask.");
            fromWord = this.baf.getFromWord(27010);
        } else {
            fromWord = this.baf.getFromWord(-24778);
            fromWord.appendByte((byte) 2);
            fromWord.append(getTransactionContext().getTransactionCredentials().getATC());
            fromWord.append(this.baf.getFromWord(-8373));
            fromWord.appendByte((byte) 3);
            fromWord.append(getTransactionContext().getPOSCII());
            fromWord = new CCCRespApdu(fromWord).getByteArray();
        }
        getTransactionContext().setTransactionResult(MCTransactionResult.TRANSACTION_COMPLETED);
        if (getTransactionContext().getTransactionError() == null || getTransactionContext().getTransactionError() == MCTransactionResult.CONTEXT_CONFLICT_PASS) {
            getTransactionContext().setTransactionError(MCTransactionResult.TRANSACTION_COMPLETED_ERROR_CCC_DECLINE);
        }
        return fromWord;
    }

    public boolean checkCLA(byte b) {
        return b == -128;
    }
}
