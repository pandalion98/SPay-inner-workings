/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.text.TextUtils
 *  com.google.gson.reflect.TypeToken
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.lang.reflect.Type
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.mastercard.mcbp.core.mcbpcards.profile.CardRiskManagementData;
import com.mastercard.mcbp.core.mcbpcards.profile.ContactlessPaymentData;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mcbp.core.mcbpcards.profile.Records;
import com.mastercard.mcbp.core.mcbpcards.profile.RemotePaymentData;
import com.mastercard.mcbp.crypto.AndroidMCBPCryptoService;
import com.mastercard.mcbp.crypto.MCBPCryptoService;
import com.mastercard.mobile_api.bytes.AndroidByteArrayFactory;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.mastercard.McProvider;
import com.samsung.android.spayfw.payprovider.mastercard.card.McCardClearData;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseDigitalizedCard;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCard;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCardActivationResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCardListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPPaymentException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.TlvParserUtil;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import java.lang.reflect.Type;
import java.util.List;

public class MCTransactionService {
    public static final int ERROR = -1;
    public static final String TAG = "mcpce_MCTransactionService";
    protected MCBaseDigitalizedCard mCurrentCard;
    private McCardProfileDaoImpl<DC_CP> mDao;
    private final McTAController mMCController;

    public MCTransactionService(Context context) {
        this.mDao = new McCardProfileDaoImpl(context, (Type)DC_CP.class);
        this.mMCController = McTAController.getInstance();
        this.initializePaymentService();
    }

    private MCBaseDigitalizedCard getDigitalizedCard(long l2) {
        MTBPCard mTBPCard = new MTBPCard(this.mDao);
        mTBPCard.loadCardProfile(l2);
        return mTBPCard;
    }

    private void initializePaymentService() {
        ByteArrayFactory.setInstance(new AndroidByteArrayFactory());
        MCBPCryptoService.setInstance(new AndroidMCBPCryptoService());
    }

    public boolean authenticateTransaction(SecuredObject securedObject) {
        if (securedObject != null && securedObject.getSecureObjectData() != null) {
            c.i(TAG, "Obtained so, start transaction authentication.");
            long l2 = this.mMCController.authenticateTransaction(securedObject.getSecureObjectData());
            c.i(TAG, "authenticateTransaction: authentication result: " + l2);
            long l3 = l2 LCMP 0L;
            boolean bl = false;
            if (l3 == false) {
                bl = true;
            }
            return bl;
        }
        c.e(TAG, "authenticateTransaction: authentication failed, secure object not found. Unload TA.");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void clearCard() {
        c.i(TAG, "clearCard: clean so");
        this.mMCController.clearSecureData();
        if (this.mCurrentCard != null) {
            c.i(TAG, "clearCard: clear TC");
            this.mCurrentCard.clearContext();
        } else {
            c.e(TAG, "clearCard: no selected card.");
        }
        this.mCurrentCard = null;
    }

    public int getIssuerCountryCode(f f2) {
        c.i(TAG, "getIssuerCountryCode");
        if (this.mDao == null) {
            this.mDao = new McCardProfileDaoImpl(McProvider.getContext(), (Type)DC_CP.class);
        }
        try {
            int n2 = 65535 & Utils.readShort(((DC_CP)((MCBaseCardProfile)this.mDao.getData(f2.cm())).getDigitalizedCardContainer()).getDC_CP_MPP().getCardRiskManagementData().getCRM_CountryCode());
            c.d(TAG, "getIssuerCountryCode: CrmCountry : " + Integer.toHexString((int)n2));
            return n2;
        }
        catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            c.c(TAG, "Exception CRM!!!!", nullPointerException);
            return -1;
        }
    }

    public DSRPOutputData getPayInfoData(DSRPInputData dSRPInputData) {
        if (dSRPInputData == null) {
            return null;
        }
        if (this.mCurrentCard != null) {
            if (!this.mCurrentCard.isReadyForRPTransaction()) {
                c.e(TAG, "getPayInfoData: MC card profile doesn't support in-app transaction.");
                return null;
            }
            c.e(TAG, "getPayInfoData: start remote payment transaction.");
            return this.mCurrentCard.getPayInfoData(dSRPInputData);
        }
        c.e(TAG, "getPayInfoData: card is null, not selected.");
        return null;
    }

    public void initTransaction(boolean bl) {
        c.i(TAG, "initTransaction is authenticated " + bl);
        if (this.mCurrentCard == null) {
            c.e(TAG, "initTransaction: card not selected!!! ");
            return;
        }
        if (!bl) {
            this.mCurrentCard.initTransaction(new MCCVMResult(-1L, false), null);
            return;
        }
        this.mCurrentCard.initTransaction(new MCCVMResult(0L, true), null);
    }

    public void interruptMstPay() {
        c.i(TAG, "MCProvider: interruptMstPay");
        this.mMCController.abortMstTransmission();
    }

    public boolean isDSRPSupportedByProfile() {
        if (this.mCurrentCard == null) {
            c.e(TAG, "MCProvider: isDSRPSupportedByProfile: current card is not selected.");
            return false;
        }
        return this.mCurrentCard.isRPSupported();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isDSRPSupportedByProfile(f f2) {
        MCBaseDigitalizedCard mCBaseDigitalizedCard;
        try {
            mCBaseDigitalizedCard = this.getDigitalizedCard(f2.cm());
        }
        catch (MTBPPaymentException mTBPPaymentException) {
            String string = "isDSRPSupportedByProfile: Id " + f2 == null ? "null" : f2.cn();
            c.c(TAG, string, (Throwable)((Object)mTBPPaymentException));
            return false;
        }
        return mCBaseDigitalizedCard.isRPSupported();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isMSTSupportedByProfile(f f2) {
        c.i(TAG, "MCProvider: isMSTTransactionSupported");
        if (f2 == null) {
            c.e(TAG, "MCProvider: providerTokenKey is null");
            return false;
        }
        if (this.mDao == null) {
            this.mDao = new McCardProfileDaoImpl(McProvider.getContext(), (Type)DC_CP.class);
        }
        try {
            MCProfilesTable mCProfilesTable = this.mDao.getProfileTable(f2.cm());
            if (mCProfilesTable != null) return mCProfilesTable.isMSTProfileExist();
        }
        catch (Exception exception) {
            c.e(TAG, "MCProvider: dao access exception: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
        c.e(TAG, "MCProvider: isMSTTransactionSupported: profiles table is null.");
        return false;
    }

    public void loadTA() {
        c.i(TAG, "MCProvider: load MC TA");
        if (!this.mMCController.loadTA()) {
            c.e(TAG, "MC TA loading failed");
        }
    }

    public boolean prepareMstPay() {
        if (this.mCurrentCard != null && this.mCurrentCard.isReadyForMSTTransaction()) {
            if (this.mCurrentCard.prepareMSTData() != 0L) {
                c.e(TAG, "prepareMstPay: MST prepareMSTData failed.");
                return false;
            }
            c.i(TAG, "prepareMstPay: MST prepareMSTData success.");
            return true;
        }
        c.e(TAG, "prepareMstPay: the card is not found or not ready for the MST payment.");
        return false;
    }

    public byte[] processApdu(byte[] arrby, Bundle bundle) {
        if (this.mCurrentCard == null) {
            c.e(TAG, "processApdu: Card not found. Activation failed.");
            return ByteArrayFactory.getInstance().getFromWord(27266).getBytes();
        }
        return this.mCurrentCard.proccessApdu(arrby);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public SelectCardResult selectCard(f var1_1) {
        var2_2 = new SelectCardResult();
        if (var1_1 != null) ** GOTO lbl6
        try {
            c.e("mcpce_MCTransactionService", "selectCard: invalid tokenRefId.");
            return var2_2;
lbl6: // 1 sources:
            c.i("mcpce_MCTransactionService", "selectCard: Id :" + var1_1.cm());
            this.mCurrentCard = this.getDigitalizedCard(var1_1.cm());
            var5_3 = this.mCurrentCard.getCardProfile();
            if (var5_3 == null) {
                c.e("mcpce_MCTransactionService", "selectCard: invalid card profile.");
                return var2_2;
            }
            if (var5_3.getDigitalizedCardContainer() == null) {
                c.e("mcpce_MCTransactionService", "selectCard: invalid card profile Contactless");
                return var2_2;
            }
            if (var5_3.getTADataContainer() == null) {
                c.e("mcpce_MCTransactionService", "selectCard: invalid card profile TA");
                return var2_2;
            }
            var6_4 = McTAController.getTaid();
            if (var6_4 == null) {
                c.e("mcpce_MCTransactionService", "Cannot find TA id for the MC TA.");
                return var2_2;
            }
            if (this.mMCController.isTALoaded()) {
                var7_5 = this.mMCController.getNonce(var5_3.getTADataContainer(), var5_3.getTaAtcContainer());
                if (var7_5 != null) {
                    c.i("mcpce_MCTransactionService", "MC TA nonce len: " + var7_5.length);
                    var2_2.setNonce((byte[])var7_5.clone());
                    var2_2.setTaid(var6_4);
                    return var2_2;
                }
                c.e("mcpce_MCTransactionService", "MC TA Controller getNonce() failed. Unload TA.");
                this.mMCController.unloadTA();
                return var2_2;
            }
            c.e("mcpce_MCTransactionService", "selectCard: MC TA is not loaded.");
            return var2_2;
        }
        catch (MTBPPaymentException var3_6) {
            var4_7 = "selectCard: Id " + var1_1 == null ? "null" : var1_1.cn();
            c.c("mcpce_MCTransactionService", var4_7, (Throwable)var3_6);
            return var2_2;
        }
    }

    public boolean startMstPay(int n2, byte[] arrby) {
        c.i(TAG, "startMstPay: Process MST transaction in the Transaction Service... ");
        MCBaseCardProfile<?> mCBaseCardProfile = this.mCurrentCard.getCardProfile();
        if (mCBaseCardProfile == null || mCBaseCardProfile.getTADataContainer() == null) {
            c.e(TAG, "startMstPay: invalid card profile.");
            return false;
        }
        if (this.mMCController.processMST(n2, arrby) != (long)McTACommands.MC_TA_ERRORS.NO_ERROR.ordinal()) {
            c.e(TAG, "startMstPay: Invalid card profile.");
            return false;
        }
        c.i(TAG, "Process MST transaction completed.");
        return true;
    }

    public void stopMstPay() {
        c.i(TAG, "stopMstPay");
        long l2 = this.mMCController.clearMstData();
        if (l2 != (long)McTACommands.MC_TA_ERRORS.NO_ERROR.ordinal()) {
            c.e(TAG, "stopMstPay: clean mst data failed, return code: " + l2);
        }
    }

    public Bundle stopNfcPay(int n2) {
        c.i(TAG, "stopNfcPay: " + n2);
        if (this.mCurrentCard == null) {
            c.e(TAG, "stopNfcPay: card is null");
            return null;
        }
        return this.mCurrentCard.processDeactivated();
    }

    public void unloadTA() {
        c.i(TAG, "MCProvider: start unload MC TA");
        McTAController.getInstance().unloadTA();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void validateAlternateProfile(f f2) {
        if (f2 == null) {
            c.e(TAG, "validateAlternateProfile: token key is null.");
            return;
        }
        try {
            MCBaseCardProfile<?> mCBaseCardProfile = this.getDigitalizedCard(f2.cm()).getCardProfile();
            if (mCBaseCardProfile == null) {
                c.e(TAG, "validateAlternateProfile: cannot find card profile.");
                return;
            }
            MCProfilesTable mCProfilesTable = mCBaseCardProfile.getTAProfilesTable();
            if (mCProfilesTable == null) {
                c.e(TAG, "validateAlternateProfile: profiles table is null.");
                return;
            }
            DC_CP dC_CP = (DC_CP)mCBaseCardProfile.getDigitalizedCardContainer();
            if (dC_CP == null) {
                c.e(TAG, "validateAlternateProfile: dc_cp is null.");
                return;
            }
            if (mCProfilesTable.getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_ALT_TA_GAC_DECLINE_NO_CVM) == 0 || mCProfilesTable.getTAProfileReference(MCProfilesTable.TAProfile.PROFILE_CL_ALT_TA_GAC_ONLINE_NO_CVM) == 0) {
                c.e(TAG, "validateAlternateProfile: alternate profile is not supported in the profile table.");
                return;
            }
            DC_CP_MPP dC_CP_MPP = dC_CP.getDC_CP_MPP();
            if (dC_CP_MPP == null) {
                c.e(TAG, "validateAlternateProfile: dc_cp_mpp is null.");
                return;
            }
            ContactlessPaymentData contactlessPaymentData = dC_CP_MPP.getContactlessPaymentData();
            if (contactlessPaymentData == null) {
                c.e(TAG, "validateAlternateProfile: contactless payment data is null.");
                return;
            }
            c.i(TAG, "validateAlternateProfile: fillAlternatePaymentData...");
            if (!McCardClearData.fillAlternateProfile(contactlessPaymentData)) {
                c.e(TAG, "validateAlternateProfile: alternate profile is not supported...");
                return;
            }
            Type type = new TypeToken<DC_CP>(){}.getType();
            McCardProfileDaoImpl mcCardProfileDaoImpl = new McCardProfileDaoImpl(McTokenManager.getContext(), type);
            boolean bl = mcCardProfileDaoImpl.updateData(mCBaseCardProfile, f2.cm());
            c.i(TAG, "validateAlternateProfile: updateData...: " + bl);
            byte[] arrby = mCBaseCardProfile.getTADataContainer();
            if (arrby == null) {
                c.e(TAG, "validateAlternateProfile: secure object is null for tokenId = " + f2.cm());
                return;
            }
            try {
                byte[] arrby2 = this.mMCController.copyACKey(arrby);
                if (arrby2 != null) {
                    c.i(TAG, "validateAlternateProfile: update secure data...");
                    mcCardProfileDaoImpl.updateTaData(arrby2, f2.cm());
                    c.i(TAG, "validateAlternateProfile: profile has been updated.");
                    return;
                }
                c.e(TAG, "validateAlternateProfile: updated secure profile is null...");
                return;
            }
            catch (Exception exception) {
                c.e(TAG, "validateAlternateProfile: cannot get MCTAController: " + exception.getMessage());
                exception.printStackTrace();
                return;
            }
        }
        catch (MTBPPaymentException mTBPPaymentException) {
            String string = "validateAlternateProfile: Id " + f2 == null ? "null" : f2.cn();
            c.c(TAG, string, (Throwable)((Object)mTBPPaymentException));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void validateDSRPProfile(f f2) {
        DC_CP_MPP dC_CP_MPP;
        MCBaseCardProfile<?> mCBaseCardProfile;
        Records records2;
        RemotePaymentData remotePaymentData;
        DC_CP dC_CP;
        block14 : {
            try {
                mCBaseCardProfile = this.getDigitalizedCard(f2.cm()).getCardProfile();
                if (mCBaseCardProfile == null) {
                    c.e(TAG, "validateDSRPProfile: cannot find card profile.");
                    return;
                }
                MCProfilesTable mCProfilesTable = mCBaseCardProfile.getTAProfilesTable();
                if (mCProfilesTable == null) {
                    c.e(TAG, "validateDSRPProfile: profiles table is null.");
                    return;
                }
                if (!mCProfilesTable.isDSRPProfileExist()) {
                    c.e(TAG, "validateDSRPProfile: DSRP secure profiles are not supported.");
                    return;
                }
            }
            catch (MTBPPaymentException mTBPPaymentException) {
                c.c(TAG, "validateDSRPProfile: Id " + f2.cn(), (Throwable)((Object)mTBPPaymentException));
                return;
            }
            dC_CP = (DC_CP)mCBaseCardProfile.getDigitalizedCardContainer();
            if (dC_CP == null) {
                c.e(TAG, "validateDSRPProfile: dc_cp is null.");
                return;
            }
            dC_CP_MPP = dC_CP.getDC_CP_MPP();
            if (dC_CP_MPP == null) {
                c.e(TAG, "validateDSRPProfile: dc_cp_mpp is null.");
                return;
            }
            RemotePaymentData remotePaymentData2 = dC_CP_MPP.getRemotePaymentData();
            if (remotePaymentData2 == null) {
                c.i(TAG, "validateDSRPProfile: add remote payment data.");
                remotePaymentData = new RemotePaymentData();
            } else {
                remotePaymentData = remotePaymentData2;
            }
            for (Records records2 : dC_CP_MPP.getContactlessPaymentData().getRecords()) {
                if (records2.getRecordNumber() != 1 || records2.getSFI() != 2) {
                    continue;
                }
                break block14;
            }
            records2 = null;
        }
        dC_CP.setRP_Supported(true);
        c.i(TAG, "validateDSRPProfile: fillRemotePaymentData...");
        McCardClearData.fillRemotePaymentData(remotePaymentData, records2.getRecordValue());
        List<String> list = TlvParserUtil.extractTagData(records2.getRecordValue().getBytes(), TlvParserUtil.Mctags.PAR);
        if (list != null && !list.isEmpty()) {
            String string = (String)list.get(0);
            if (!TextUtils.isEmpty((CharSequence)string)) {
                remotePaymentData.setPaymentAccountReference(ByteArrayFactory.getInstance().fromHexString(string));
            }
            c.d(TAG, "validateDSRPProfile: PAR..." + remotePaymentData.getPaymentAccountReference());
        }
        dC_CP_MPP.setRemotePaymentData(remotePaymentData);
        c.i(TAG, "validateDSRPProfile: updateData...");
        Type type = new TypeToken<DC_CP>(){}.getType();
        boolean bl = new McCardProfileDaoImpl(McTokenManager.getContext(), type).updateData(mCBaseCardProfile, f2.cm());
        c.i(TAG, "validateDSRPProfile: profile has been updated :" + bl);
    }

}

