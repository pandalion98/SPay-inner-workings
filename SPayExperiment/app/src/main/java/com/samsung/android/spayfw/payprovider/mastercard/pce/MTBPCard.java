/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.pce;

import android.os.Bundle;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.dao.McCardProfileDaoImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseCardProfile;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCBaseDigitalizedCard;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCardActivationResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPCardListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPLite;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPPaymentException;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MTBPTransactionListener;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCBPCardProfileImpl;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCCVMResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCProfilesTable;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionCredentials;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCTransactionResult;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCUnusedDGIElements;

public class MTBPCard
implements MCBaseDigitalizedCard {
    private ByteArrayFactory baf = ByteArrayFactory.getInstance();
    private MTBPCardListener mCardListener;
    private MCBPCardProfileImpl mCardProfile;
    private McCardProfileDaoImpl mDao;
    private MTBPLite mMppLite;
    private MTBPTransactionListener mTransactionListener = new MTBPTransactionListener(){

        @Override
        public void onTransactionCanceled(MCTransactionResult mCTransactionResult) {
            if (MTBPCard.this.mCardListener != null) {
                MTBPCard.this.mCardListener.onTransactionTerminated(mCTransactionResult);
            }
        }

        @Override
        public void onTransactionSuccess() {
            if (MTBPCard.this.mCardListener != null) {
                MTBPCard.this.mCardListener.onTransactionCompleted();
            }
        }
    };

    public MTBPCard(McCardProfileDaoImpl<DC_CP> mcCardProfileDaoImpl) {
        this.mDao = mcCardProfileDaoImpl;
    }

    @Override
    public void clearContext() {
        if (this.mMppLite != null) {
            c.i("mcpce_MCTransactionService", "cleanContext: clean transaction context.");
            this.mMppLite.cleanTransactionContext();
            return;
        }
        c.e("mcpce_MCTransactionService", "cleanContext: transaction context not exist.");
    }

    public MCBPCardProfileImpl getCardProfile() {
        return this.mCardProfile;
    }

    @Override
    public DSRPOutputData getPayInfoData(DSRPInputData dSRPInputData) {
        c.i("mcpce_MCTransactionService", "MTBP card: getPayInfoData");
        if (this.mMppLite != null && this.mMppLite.isReadyForTransaction()) {
            c.d("mcpce_MCTransactionService", "MTBP card: start getPayInfoData");
            return this.mMppLite.generateDSRPData(dSRPInputData);
        }
        c.e("mcpce_MCTransactionService", "MTBP card: not ready for remote payment.");
        return null;
    }

    @Override
    public MTBPCardActivationResult initTransaction(MCCVMResult mCCVMResult, MTBPCardListener mTBPCardListener) {
        c.i("mcpce_MCTransactionService", "initTransaction: start activateContactless.");
        if (this.mCardProfile == null || this.mCardProfile.getMCPaymentProfile() == null || this.mCardProfile.getTADataContainer() == null) {
            c.e("mcpce_MCTransactionService", "initTransaction: profile is not found.");
            return MTBPCardActivationResult.CARD_ACTIVATION_INVALID_PROFILE;
        }
        if (this.mMppLite == null) {
            c.i("mcpce_MCTransactionService", "initTransaction: create mpplite.");
            this.mMppLite = new MTBPLite(this.mDao);
        }
        try {
            this.mMppLite.initialize(this.mCardProfile);
            MCTransactionCredentials mCTransactionCredentials = new MCTransactionCredentials();
            if (this.mCardProfile.getTADataContainer() == null) {
                c.e("mcpce_MCTransactionService", "initTransaction:  invalid secure profile.");
                return MTBPCardActivationResult.CARD_ACTIVATION_INVALID_PROFILE;
            }
            mCTransactionCredentials.setSecureObject(this.mCardProfile.getTADataContainer());
            mCTransactionCredentials.setTAProfilesTable(this.mCardProfile.getTAProfilesTable());
            mCTransactionCredentials.setCVMResult(mCCVMResult);
            this.mCardListener = mTBPCardListener;
            this.mMppLite.setTransactionContext(mCTransactionCredentials, this.mTransactionListener);
            return MTBPCardActivationResult.CARD_ACTIVATION_SUCCESS;
        }
        catch (MCTransactionException mCTransactionException) {
            c.e("mcpce_MCTransactionService", "initTransaction: internal error on mpplite initialization...");
            return MTBPCardActivationResult.CARD_ACTIVATION_INTERNAL_ERROR;
        }
    }

    @Override
    public boolean isMSTSupported() {
        return this.mCardProfile.isMSTTransactionSupported();
    }

    @Override
    public boolean isRPSupported() {
        return this.mCardProfile.isRPTransactionSupported();
    }

    @Override
    public boolean isReadyForMSTTransaction() {
        return this.mMppLite != null && this.mCardProfile.isMSTTransactionSupported() && this.mMppLite.isReadyForTransaction();
    }

    @Override
    public boolean isReadyForNFCTransaction() {
        return this.mMppLite != null && this.mCardProfile.isNFCTransactionSupported() && this.mMppLite.isReadyForTransaction();
    }

    @Override
    public boolean isReadyForRPTransaction() {
        return this.mCardProfile.isRPTransactionSupported();
    }

    @Override
    public void loadCardProfile(long l2) {
        c.i("mcpce_MCTransactionService", "MTBPCard: Base profile id: " + l2);
        Object object = this.mDao.getData(l2);
        this.mCardProfile = new MCBPCardProfileImpl();
        if (object == null) {
            throw new MTBPPaymentException("MC Payment card profile cannot be found.");
        }
        if (((MCBaseCardProfile)object).getUnusedDGIElements() == null) {
            c.i("mcpce_MCTransactionService", "unusedDgi element is null");
        }
        this.mCardProfile.initializeMCProfile(l2, (DC_CP)((MCBaseCardProfile)object).getDigitalizedCardContainer(), ((MCBaseCardProfile)object).getTADataContainer(), ((MCBaseCardProfile)object).getTAProfilesTable(), ((MCBaseCardProfile)object).getTaAtcContainer(), ((MCBaseCardProfile)object).getUnusedDGIElements());
    }

    @Override
    public long prepareMSTData() {
        c.i("mcpce_MCTransactionService", "prepareMSTData: Process MST transaction for card...");
        if (this.mCardProfile.getMCPaymentProfile() == null || this.mCardProfile.getTADataContainer() == null) {
            c.e("mcpce_MCTransactionService", "prepareMSTData: payment profile not found, return code 2");
            return 2L;
        }
        return this.mMppLite.initializeMST();
    }

    @Override
    public byte[] proccessApdu(byte[] arrby) {
        if (this.mMppLite == null) {
            c.e("mcpce_MCTransactionService", "proccessApdu: card is not initialized...");
            return this.baf.getFromWord(27266).getBytes();
        }
        c.d("mcpce_MCTransactionService", "proccessApdu: call mMppLite.processApdu...");
        ByteArray byteArray = ByteArrayFactory.getInstance().getByteArray(arrby, arrby.length);
        ByteArray byteArray2 = this.mMppLite.processAPDU(byteArray);
        if (byteArray2 == null) {
            c.e("mcpce_MCTransactionService", "proccessApdu: call mMppLite.processApdu...");
            return this.baf.getFromWord(27266).getBytes();
        }
        return byteArray2.getBytes();
    }

    @Override
    public Bundle processDeactivated() {
        if (this.mMppLite == null) {
            c.e("mcpce_MCTransactionService", "processDeactivated: no processed transaction");
            return null;
        }
        return this.mMppLite.stopNfc();
    }

    @Override
    public void setCardProfile(MCBaseCardProfile<?> mCBaseCardProfile) {
        if (mCBaseCardProfile instanceof MCBPCardProfileImpl) {
            this.mCardProfile = (MCBPCardProfileImpl)mCBaseCardProfile;
        }
    }

}

