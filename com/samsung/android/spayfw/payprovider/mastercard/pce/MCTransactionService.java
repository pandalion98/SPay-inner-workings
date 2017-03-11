package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.google.gson.reflect.TypeToken;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mcbp.core.mcbpcards.profile.Records;
import com.mastercard.mcbp.core.mcbpcards.profile.RemotePaymentData;
import com.mastercard.mcbp.crypto.AndroidMCBPCryptoService;
import com.mastercard.mcbp.crypto.MCBPCryptoService;
import com.mastercard.mobile_api.bytes.AndroidByteArrayFactory;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.TlvParserUtil.Mctags;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable.TAProfile;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands.MC_TA_ERRORS;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import java.util.List;

public class MCTransactionService {
    public static final int ERROR = -1;
    public static final String TAG = "mcpce_MCTransactionService";
    protected MCBaseDigitalizedCard mCurrentCard;
    private McCardProfileDaoImpl<DC_CP> mDao;
    private final McTAController mMCController;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionService.1 */
    class C05601 extends TypeToken<DC_CP> {
        C05601() {
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionService.2 */
    class C05612 extends TypeToken<DC_CP> {
        C05612() {
        }
    }

    public MCTransactionService(Context context) {
        this.mDao = new McCardProfileDaoImpl(context, DC_CP.class);
        this.mMCController = McTAController.getInstance();
        initializePaymentService();
    }

    public SelectCardResult selectCard(ProviderTokenKey providerTokenKey) {
        SelectCardResult selectCardResult = new SelectCardResult();
        if (providerTokenKey == null) {
            try {
                Log.m286e(TAG, "selectCard: invalid tokenRefId.");
                return selectCardResult;
            } catch (Throwable e) {
                String str;
                Throwable th = e;
                String str2 = TAG;
                if (("selectCard: Id " + providerTokenKey) == null) {
                    str = "null";
                } else {
                    str = providerTokenKey.cn();
                }
                Log.m284c(str2, str, th);
            }
        } else {
            Log.m287i(TAG, "selectCard: Id :" + providerTokenKey.cm());
            this.mCurrentCard = getDigitalizedCard(providerTokenKey.cm());
            MCBaseCardProfile cardProfile = this.mCurrentCard.getCardProfile();
            if (cardProfile == null) {
                Log.m286e(TAG, "selectCard: invalid card profile.");
                return selectCardResult;
            } else if (cardProfile.getDigitalizedCardContainer() == null) {
                Log.m286e(TAG, "selectCard: invalid card profile Contactless");
                return selectCardResult;
            } else if (cardProfile.getTADataContainer() == null) {
                Log.m286e(TAG, "selectCard: invalid card profile TA");
                return selectCardResult;
            } else {
                String taid = McTAController.getTaid();
                if (taid == null) {
                    Log.m286e(TAG, "Cannot find TA id for the MC TA.");
                    return selectCardResult;
                }
                if (this.mMCController.isTALoaded()) {
                    Object nonce = this.mMCController.getNonce(cardProfile.getTADataContainer(), cardProfile.getTaAtcContainer());
                    if (nonce != null) {
                        Log.m287i(TAG, "MC TA nonce len: " + nonce.length);
                        selectCardResult.setNonce((byte[]) nonce.clone());
                        selectCardResult.setTaid(taid);
                    } else {
                        Log.m286e(TAG, "MC TA Controller getNonce() failed. Unload TA.");
                        this.mMCController.unloadTA();
                    }
                } else {
                    Log.m286e(TAG, "selectCard: MC TA is not loaded.");
                }
                return selectCardResult;
            }
        }
    }

    public byte[] processApdu(byte[] bArr, Bundle bundle) {
        if (this.mCurrentCard != null) {
            return this.mCurrentCard.proccessApdu(bArr);
        }
        Log.m286e(TAG, "processApdu: Card not found. Activation failed.");
        return ByteArrayFactory.getInstance().getFromWord(27266).getBytes();
    }

    public boolean authenticateTransaction(SecuredObject securedObject) {
        if (securedObject == null || securedObject.getSecureObjectData() == null) {
            Log.m286e(TAG, "authenticateTransaction: authentication failed, secure object not found. Unload TA.");
            return false;
        }
        Log.m287i(TAG, "Obtained so, start transaction authentication.");
        long authenticateTransaction = this.mMCController.authenticateTransaction(securedObject.getSecureObjectData());
        Log.m287i(TAG, "authenticateTransaction: authentication result: " + authenticateTransaction);
        if (authenticateTransaction == 0) {
            return true;
        }
        return false;
    }

    public boolean prepareMstPay() {
        if (this.mCurrentCard == null || !this.mCurrentCard.isReadyForMSTTransaction()) {
            Log.m286e(TAG, "prepareMstPay: the card is not found or not ready for the MST payment.");
            return false;
        } else if (this.mCurrentCard.prepareMSTData() != 0) {
            Log.m286e(TAG, "prepareMstPay: MST prepareMSTData failed.");
            return false;
        } else {
            Log.m287i(TAG, "prepareMstPay: MST prepareMSTData success.");
            return true;
        }
    }

    public boolean startMstPay(int i, byte[] bArr) {
        Log.m287i(TAG, "startMstPay: Process MST transaction in the Transaction Service... ");
        MCBaseCardProfile cardProfile = this.mCurrentCard.getCardProfile();
        if (cardProfile == null || cardProfile.getTADataContainer() == null) {
            Log.m286e(TAG, "startMstPay: invalid card profile.");
            return false;
        } else if (this.mMCController.processMST(i, bArr) != ((long) MC_TA_ERRORS.NO_ERROR.ordinal())) {
            Log.m286e(TAG, "startMstPay: Invalid card profile.");
            return false;
        } else {
            Log.m287i(TAG, "Process MST transaction completed.");
            return true;
        }
    }

    public void stopMstPay() {
        Log.m287i(TAG, "stopMstPay");
        long clearMstData = this.mMCController.clearMstData();
        if (clearMstData != ((long) MC_TA_ERRORS.NO_ERROR.ordinal())) {
            Log.m286e(TAG, "stopMstPay: clean mst data failed, return code: " + clearMstData);
        }
    }

    public void clearCard() {
        Log.m287i(TAG, "clearCard: clean so");
        this.mMCController.clearSecureData();
        if (this.mCurrentCard != null) {
            Log.m287i(TAG, "clearCard: clear TC");
            this.mCurrentCard.clearContext();
        } else {
            Log.m286e(TAG, "clearCard: no selected card.");
        }
        this.mCurrentCard = null;
    }

    public DSRPOutputData getPayInfoData(DSRPInputData dSRPInputData) {
        if (dSRPInputData == null) {
            return null;
        }
        if (this.mCurrentCard == null) {
            Log.m286e(TAG, "getPayInfoData: card is null, not selected.");
            return null;
        } else if (this.mCurrentCard.isReadyForRPTransaction()) {
            Log.m286e(TAG, "getPayInfoData: start remote payment transaction.");
            return this.mCurrentCard.getPayInfoData(dSRPInputData);
        } else {
            Log.m286e(TAG, "getPayInfoData: MC card profile doesn't support in-app transaction.");
            return null;
        }
    }

    private MCBaseDigitalizedCard getDigitalizedCard(long j) {
        MCBaseDigitalizedCard mTBPCard = new MTBPCard(this.mDao);
        mTBPCard.loadCardProfile(j);
        return mTBPCard;
    }

    private void initializePaymentService() {
        ByteArrayFactory.setInstance(new AndroidByteArrayFactory());
        MCBPCryptoService.setInstance(new AndroidMCBPCryptoService());
    }

    public Bundle stopNfcPay(int i) {
        Log.m287i(TAG, "stopNfcPay: " + i);
        if (this.mCurrentCard != null) {
            return this.mCurrentCard.processDeactivated();
        }
        Log.m286e(TAG, "stopNfcPay: card is null");
        return null;
    }

    public void loadTA() {
        Log.m287i(TAG, "MCProvider: load MC TA");
        if (!this.mMCController.loadTA()) {
            Log.m286e(TAG, "MC TA loading failed");
        }
    }

    public void unloadTA() {
        Log.m287i(TAG, "MCProvider: start unload MC TA");
        McTAController.getInstance().unloadTA();
    }

    public void interruptMstPay() {
        Log.m287i(TAG, "MCProvider: interruptMstPay");
        this.mMCController.abortMstTransmission();
    }

    public boolean isMSTSupportedByProfile(ProviderTokenKey providerTokenKey) {
        Log.m287i(TAG, "MCProvider: isMSTTransactionSupported");
        if (providerTokenKey == null) {
            Log.m286e(TAG, "MCProvider: providerTokenKey is null");
            return false;
        }
        if (this.mDao == null) {
            this.mDao = new McCardProfileDaoImpl(McProvider.getContext(), DC_CP.class);
        }
        try {
            MCProfilesTable profileTable = this.mDao.getProfileTable(providerTokenKey.cm());
            if (profileTable != null) {
                return profileTable.isMSTProfileExist();
            }
            Log.m286e(TAG, "MCProvider: isMSTTransactionSupported: profiles table is null.");
            return false;
        } catch (Exception e) {
            Log.m286e(TAG, "MCProvider: dao access exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getIssuerCountryCode(ProviderTokenKey providerTokenKey) {
        Log.m287i(TAG, "getIssuerCountryCode");
        if (this.mDao == null) {
            this.mDao = new McCardProfileDaoImpl(McProvider.getContext(), DC_CP.class);
        }
        try {
            int readShort = Utils.readShort(((DC_CP) this.mDao.getData(providerTokenKey.cm()).getDigitalizedCardContainer()).getDC_CP_MPP().getCardRiskManagementData().getCRM_CountryCode()) & HCEClientConstants.HIGHEST_ATC_DEC_VALUE;
            Log.m285d(TAG, "getIssuerCountryCode: CrmCountry : " + Integer.toHexString(readShort));
            return readShort;
        } catch (Throwable e) {
            e.printStackTrace();
            Log.m284c(TAG, "Exception CRM!!!!", e);
            return ERROR;
        }
    }

    public boolean isDSRPSupportedByProfile() {
        if (this.mCurrentCard != null) {
            return this.mCurrentCard.isRPSupported();
        }
        Log.m286e(TAG, "MCProvider: isDSRPSupportedByProfile: current card is not selected.");
        return false;
    }

    public boolean isDSRPSupportedByProfile(ProviderTokenKey providerTokenKey) {
        try {
            return getDigitalizedCard(providerTokenKey.cm()).isRPSupported();
        } catch (Throwable e) {
            String str;
            Throwable th = e;
            String str2 = TAG;
            if (("isDSRPSupportedByProfile: Id " + providerTokenKey) == null) {
                str = "null";
            } else {
                str = providerTokenKey.cn();
            }
            Log.m284c(str2, str, th);
            return false;
        }
    }

    public void validateDSRPProfile(ProviderTokenKey providerTokenKey) {
        try {
            MCBaseCardProfile cardProfile = getDigitalizedCard(providerTokenKey.cm()).getCardProfile();
            if (cardProfile == null) {
                Log.m286e(TAG, "validateDSRPProfile: cannot find card profile.");
                return;
            }
            MCProfilesTable tAProfilesTable = cardProfile.getTAProfilesTable();
            if (tAProfilesTable == null) {
                Log.m286e(TAG, "validateDSRPProfile: profiles table is null.");
            } else if (tAProfilesTable.isDSRPProfileExist()) {
                DC_CP dc_cp = (DC_CP) cardProfile.getDigitalizedCardContainer();
                if (dc_cp == null) {
                    Log.m286e(TAG, "validateDSRPProfile: dc_cp is null.");
                    return;
                }
                DC_CP_MPP dc_cp_mpp = dc_cp.getDC_CP_MPP();
                if (dc_cp_mpp == null) {
                    Log.m286e(TAG, "validateDSRPProfile: dc_cp_mpp is null.");
                    return;
                }
                RemotePaymentData remotePaymentData;
                RemotePaymentData remotePaymentData2 = dc_cp_mpp.getRemotePaymentData();
                if (remotePaymentData2 == null) {
                    Log.m287i(TAG, "validateDSRPProfile: add remote payment data.");
                    remotePaymentData = new RemotePaymentData();
                } else {
                    remotePaymentData = remotePaymentData2;
                }
                for (Records records : dc_cp_mpp.getContactlessPaymentData().getRecords()) {
                    if (records.getRecordNumber() == 1 && records.getSFI() == 2) {
                        break;
                    }
                }
                Records records2 = null;
                dc_cp.setRP_Supported(true);
                Log.m287i(TAG, "validateDSRPProfile: fillRemotePaymentData...");
                McCardClearData.fillRemotePaymentData(remotePaymentData, records2.getRecordValue());
                List extractTagData = TlvParserUtil.extractTagData(records2.getRecordValue().getBytes(), Mctags.PAR);
                if (!(extractTagData == null || extractTagData.isEmpty())) {
                    String str = (String) extractTagData.get(0);
                    if (!TextUtils.isEmpty(str)) {
                        remotePaymentData.setPaymentAccountReference(ByteArrayFactory.getInstance().fromHexString(str));
                    }
                    Log.m285d(TAG, "validateDSRPProfile: PAR..." + remotePaymentData.getPaymentAccountReference());
                }
                dc_cp_mpp.setRemotePaymentData(remotePaymentData);
                Log.m287i(TAG, "validateDSRPProfile: updateData...");
                Log.m287i(TAG, "validateDSRPProfile: profile has been updated :" + new McCardProfileDaoImpl(McTokenManager.getContext(), new C05601().getType()).updateData(cardProfile, providerTokenKey.cm()));
            } else {
                Log.m286e(TAG, "validateDSRPProfile: DSRP secure profiles are not supported.");
            }
        } catch (Throwable e) {
            Log.m284c(TAG, "validateDSRPProfile: Id " + providerTokenKey.cn(), e);
        }
    }

    public void validateAlternateProfile(ProviderTokenKey providerTokenKey) {
        if (providerTokenKey == null) {
            Log.m286e(TAG, "validateAlternateProfile: token key is null.");
            return;
        }
        try {
            MCBaseCardProfile cardProfile = getDigitalizedCard(providerTokenKey.cm()).getCardProfile();
            if (cardProfile == null) {
                Log.m286e(TAG, "validateAlternateProfile: cannot find card profile.");
                return;
            }
            MCProfilesTable tAProfilesTable = cardProfile.getTAProfilesTable();
            if (tAProfilesTable == null) {
                Log.m286e(TAG, "validateAlternateProfile: profiles table is null.");
                return;
            }
            DC_CP dc_cp = (DC_CP) cardProfile.getDigitalizedCardContainer();
            if (dc_cp == null) {
                Log.m286e(TAG, "validateAlternateProfile: dc_cp is null.");
            } else if (tAProfilesTable.getTAProfileReference(TAProfile.PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM) == null || tAProfilesTable.getTAProfileReference(TAProfile.PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM) == null) {
                Log.m286e(TAG, "validateAlternateProfile: alternate profile is not supported in the profile table.");
            } else {
                DC_CP_MPP dc_cp_mpp = dc_cp.getDC_CP_MPP();
                if (dc_cp_mpp == null) {
                    Log.m286e(TAG, "validateAlternateProfile: dc_cp_mpp is null.");
                    return;
                }
                ContactlessPaymentData contactlessPaymentData = dc_cp_mpp.getContactlessPaymentData();
                if (contactlessPaymentData == null) {
                    Log.m286e(TAG, "validateAlternateProfile: contactless payment data is null.");
                    return;
                }
                Log.m287i(TAG, "validateAlternateProfile: fillAlternatePaymentData...");
                if (McCardClearData.fillAlternateProfile(contactlessPaymentData)) {
                    McCardProfileDaoImpl mcCardProfileDaoImpl = new McCardProfileDaoImpl(McTokenManager.getContext(), new C05612().getType());
                    Log.m287i(TAG, "validateAlternateProfile: updateData...: " + mcCardProfileDaoImpl.updateData(cardProfile, providerTokenKey.cm()));
                    byte[] tADataContainer = cardProfile.getTADataContainer();
                    if (tADataContainer == null) {
                        Log.m286e(TAG, "validateAlternateProfile: secure object is null for tokenId = " + providerTokenKey.cm());
                        return;
                    }
                    try {
                        tADataContainer = this.mMCController.copyACKey(tADataContainer);
                        if (tADataContainer != null) {
                            Log.m287i(TAG, "validateAlternateProfile: update secure data...");
                            mcCardProfileDaoImpl.updateTaData(tADataContainer, providerTokenKey.cm());
                            Log.m287i(TAG, "validateAlternateProfile: profile has been updated.");
                            return;
                        }
                        Log.m286e(TAG, "validateAlternateProfile: updated secure profile is null...");
                        return;
                    } catch (Exception e) {
                        Log.m286e(TAG, "validateAlternateProfile: cannot get MCTAController: " + e.getMessage());
                        e.printStackTrace();
                        return;
                    }
                }
                Log.m286e(TAG, "validateAlternateProfile: alternate profile is not supported...");
            }
        } catch (Throwable e2) {
            String str;
            Throwable th = e2;
            String str2 = TAG;
            if (("validateAlternateProfile: Id " + providerTokenKey) == null) {
                str = "null";
            } else {
                str = providerTokenKey.cn();
            }
            Log.m284c(str2, str, th);
        }
    }

    public void initTransaction(boolean z) {
        Log.m287i(TAG, "initTransaction is authenticated " + z);
        if (this.mCurrentCard == null) {
            Log.m286e(TAG, "initTransaction: card not selected!!! ");
        } else if (z) {
            this.mCurrentCard.initTransaction(new MCCVMResult(0, true), null);
        } else {
            this.mCurrentCard.initTransaction(new MCCVMResult(-1, false), null);
        }
    }
}
