package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.os.Bundle;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCBPCardProfileImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;

public class MTBPCard implements MCBaseDigitalizedCard {
    private ByteArrayFactory baf;
    private MTBPCardListener mCardListener;
    private MCBPCardProfileImpl mCardProfile;
    private McCardProfileDaoImpl mDao;
    private MTBPLite mMppLite;
    private MTBPTransactionListener mTransactionListener;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCard.1 */
    class C05621 implements MTBPTransactionListener {
        C05621() {
        }

        public void onTransactionSuccess() {
            if (MTBPCard.this.mCardListener != null) {
                MTBPCard.this.mCardListener.onTransactionCompleted();
            }
        }

        public void onTransactionCanceled(MCTransactionResult mCTransactionResult) {
            if (MTBPCard.this.mCardListener != null) {
                MTBPCard.this.mCardListener.onTransactionTerminated(mCTransactionResult);
            }
        }
    }

    public MTBPCard(McCardProfileDaoImpl<DC_CP> mcCardProfileDaoImpl) {
        this.baf = ByteArrayFactory.getInstance();
        this.mTransactionListener = new C05621();
        this.mDao = mcCardProfileDaoImpl;
    }

    public MCBPCardProfileImpl getCardProfile() {
        return this.mCardProfile;
    }

    public void setCardProfile(MCBaseCardProfile<?> mCBaseCardProfile) {
        if (mCBaseCardProfile instanceof MCBPCardProfileImpl) {
            this.mCardProfile = (MCBPCardProfileImpl) mCBaseCardProfile;
        }
    }

    public void loadCardProfile(long j) {
        Log.m287i(MCTransactionService.TAG, "MTBPCard: Base profile id: " + j);
        MCBaseCardProfile data = this.mDao.getData(j);
        this.mCardProfile = new MCBPCardProfileImpl();
        if (data == null) {
            throw new MTBPPaymentException("MC Payment card profile cannot be found.");
        }
        if (data.getUnusedDGIElements() == null) {
            Log.m287i(MCTransactionService.TAG, "unusedDgi element is null");
        }
        this.mCardProfile.initializeMCProfile(j, (DC_CP) data.getDigitalizedCardContainer(), data.getTADataContainer(), data.getTAProfilesTable(), data.getTaAtcContainer(), data.getUnusedDGIElements());
    }

    public Bundle processDeactivated() {
        if (this.mMppLite != null) {
            return this.mMppLite.stopNfc();
        }
        Log.m286e(MCTransactionService.TAG, "processDeactivated: no processed transaction");
        return null;
    }

    public void clearContext() {
        if (this.mMppLite != null) {
            Log.m287i(MCTransactionService.TAG, "cleanContext: clean transaction context.");
            this.mMppLite.cleanTransactionContext();
            return;
        }
        Log.m286e(MCTransactionService.TAG, "cleanContext: transaction context not exist.");
    }

    public boolean isReadyForNFCTransaction() {
        return this.mMppLite != null && this.mCardProfile.isNFCTransactionSupported() && this.mMppLite.isReadyForTransaction();
    }

    public boolean isReadyForMSTTransaction() {
        return this.mMppLite != null && this.mCardProfile.isMSTTransactionSupported() && this.mMppLite.isReadyForTransaction();
    }

    public boolean isReadyForRPTransaction() {
        return this.mCardProfile.isRPTransactionSupported();
    }

    public long prepareMSTData() {
        Log.m287i(MCTransactionService.TAG, "prepareMSTData: Process MST transaction for card...");
        if (this.mCardProfile.getMCPaymentProfile() != null && this.mCardProfile.getTADataContainer() != null) {
            return this.mMppLite.initializeMST();
        }
        Log.m286e(MCTransactionService.TAG, "prepareMSTData: payment profile not found, return code 2");
        return 2;
    }

    public byte[] proccessApdu(byte[] bArr) {
        if (this.mMppLite == null) {
            Log.m286e(MCTransactionService.TAG, "proccessApdu: card is not initialized...");
            return this.baf.getFromWord(27266).getBytes();
        }
        Log.m285d(MCTransactionService.TAG, "proccessApdu: call mMppLite.processApdu...");
        ByteArray processAPDU = this.mMppLite.processAPDU(ByteArrayFactory.getInstance().getByteArray(bArr, bArr.length));
        if (processAPDU != null) {
            return processAPDU.getBytes();
        }
        Log.m286e(MCTransactionService.TAG, "proccessApdu: call mMppLite.processApdu...");
        return this.baf.getFromWord(27266).getBytes();
    }

    public MTBPCardActivationResult initTransaction(MCCVMResult mCCVMResult, MTBPCardListener mTBPCardListener) {
        Log.m287i(MCTransactionService.TAG, "initTransaction: start activateContactless.");
        if (this.mCardProfile == null || this.mCardProfile.getMCPaymentProfile() == null || this.mCardProfile.getTADataContainer() == null) {
            Log.m286e(MCTransactionService.TAG, "initTransaction: profile is not found.");
            return MTBPCardActivationResult.CARD_ACTIVATION_INVALID_PROFILE;
        }
        if (this.mMppLite == null) {
            Log.m287i(MCTransactionService.TAG, "initTransaction: create mpplite.");
            this.mMppLite = new MTBPLite(this.mDao);
        }
        try {
            this.mMppLite.initialize(this.mCardProfile);
            MCTransactionCredentials mCTransactionCredentials = new MCTransactionCredentials();
            if (this.mCardProfile.getTADataContainer() == null) {
                Log.m286e(MCTransactionService.TAG, "initTransaction:  invalid secure profile.");
                return MTBPCardActivationResult.CARD_ACTIVATION_INVALID_PROFILE;
            }
            mCTransactionCredentials.setSecureObject(this.mCardProfile.getTADataContainer());
            mCTransactionCredentials.setTAProfilesTable(this.mCardProfile.getTAProfilesTable());
            mCTransactionCredentials.setCVMResult(mCCVMResult);
            this.mCardListener = mTBPCardListener;
            this.mMppLite.setTransactionContext(mCTransactionCredentials, this.mTransactionListener);
            return MTBPCardActivationResult.CARD_ACTIVATION_SUCCESS;
        } catch (MCTransactionException e) {
            Log.m286e(MCTransactionService.TAG, "initTransaction: internal error on mpplite initialization...");
            return MTBPCardActivationResult.CARD_ACTIVATION_INTERNAL_ERROR;
        }
    }

    public DSRPOutputData getPayInfoData(DSRPInputData dSRPInputData) {
        Log.m287i(MCTransactionService.TAG, "MTBP card: getPayInfoData");
        if (this.mMppLite == null || !this.mMppLite.isReadyForTransaction()) {
            Log.m286e(MCTransactionService.TAG, "MTBP card: not ready for remote payment.");
            return null;
        }
        Log.m285d(MCTransactionService.TAG, "MTBP card: start getPayInfoData");
        return this.mMppLite.generateDSRPData(dSRPInputData);
    }

    public boolean isMSTSupported() {
        return this.mCardProfile.isMSTTransactionSupported();
    }

    public boolean isRPSupported() {
        return this.mCardProfile.isRPTransactionSupported();
    }
}
