package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.LoyaltyCardDetail;
import com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.MstConfigurationManager;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkMessage;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.ExtractCardDetailResult;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.ServerCertificates;
import com.samsung.android.spayfw.remoteservice.tokenrequester.UpdateLoyaltyCardRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Instruction;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.l */
public class LoyaltyCardProcessor extends Processor {
    private static LoyaltyCardProcessor lu;

    /* renamed from: com.samsung.android.spayfw.core.a.l.a */
    private class LoyaltyCardProcessor extends C0413a<Response<Data>, UpdateLoyaltyCardRequest> {
        UpdateLoyaltyCardInfo lv;
        IUpdateLoyaltyCardCallback lw;
        final /* synthetic */ LoyaltyCardProcessor lx;

        public LoyaltyCardProcessor(LoyaltyCardProcessor loyaltyCardProcessor, UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback) {
            this.lx = loyaltyCardProcessor;
            this.lv = updateLoyaltyCardInfo;
            this.lw = iUpdateLoyaltyCardCallback;
        }

        public void m429a(int i, Response<Data> response) {
            int i2;
            String str;
            Log.m287i("LoyaltyCardProcessor", "UpdateLoyaltyCardProcessorCallback: onRequestComplete:  " + i);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    str = null;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                case 503:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE;
                    str = null;
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    if (response != null && response.getResult() != null && ((Data) response.getResult()).getData() != null) {
                        Card r = this.lx.iJ.m559r(this.lv.getTokenId());
                        if (r != null && r.ac() != null) {
                            str = r.ad().updateLoyaltyCardTA(((Data) response.getResult()).getData());
                            if (str != null) {
                                i2 = 0;
                                break;
                            } else {
                                i2 = -36;
                                break;
                            }
                        }
                        Log.m286e("LoyaltyCardProcessor", "onRequestComplete: Invalid tokenId");
                        i2 = -6;
                        str = null;
                        break;
                    }
                    Log.m286e("LoyaltyCardProcessor", "onRequestComplete: invalid response from server");
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_OUTPUT_INVALID;
                    str = null;
                    break;
                    break;
                case 204:
                case 404:
                case 410:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_CARD_NOT_SUPPORTED;
                    str = null;
                    break;
                case 500:
                    i2 = PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL;
                    str = null;
                    break;
                default:
                    i2 = -1;
                    str = null;
                    break;
            }
            if (i2 == 0) {
                try {
                    this.lw.onSuccess(str);
                    return;
                } catch (Throwable e) {
                    Log.m284c("LoyaltyCardProcessor", e.getMessage(), e);
                    return;
                }
            }
            this.lw.onFail(i2);
        }

        public void m430a(int i, ServerCertificates serverCertificates, UpdateLoyaltyCardRequest updateLoyaltyCardRequest) {
            Log.m285d("LoyaltyCardProcessor", "onCertsReceived: called for UpdateLoyaltyCardProcessorCallback");
            Card r = this.lx.iJ.m559r(this.lv.getTokenId());
            if (r == null) {
                Log.m286e("LoyaltyCardProcessor", "UpdateLoyaltyCardProcessorCallback : cannot get Card object :" + this.lv.getTokenId());
                try {
                    this.lw.onFail(-6);
                } catch (Throwable e) {
                    Log.m284c("LoyaltyCardProcessor", e.getMessage(), e);
                }
            } else if (this.lx.m334a(r.getEnrollmentId(), r, serverCertificates)) {
                updateLoyaltyCardRequest.m843k(LoyaltyCardProcessor.m433a(r, this.lv));
                updateLoyaltyCardRequest.bf(this.lx.m329P(r.getCardBrand()));
                updateLoyaltyCardRequest.m836a((C0413a) this);
                Log.m287i("LoyaltyCardProcessor", "update loyalty card request successfully sent after server cert update");
            } else {
                Log.m286e("LoyaltyCardProcessor", "UpdateLoyaltyCardProcessorCallback: Server certificate update failed");
                try {
                    this.lw.onFail(-1);
                } catch (Throwable e2) {
                    Log.m284c("LoyaltyCardProcessor", e2.getMessage(), e2);
                }
            }
        }
    }

    static {
        lu = null;
    }

    public static synchronized LoyaltyCardProcessor m436p(Context context) {
        LoyaltyCardProcessor loyaltyCardProcessor;
        synchronized (LoyaltyCardProcessor.class) {
            if (lu == null) {
                lu = new LoyaltyCardProcessor(context);
            }
            loyaltyCardProcessor = lu;
        }
        return loyaltyCardProcessor;
    }

    private static List<Instruction> m433a(Card card, UpdateLoyaltyCardInfo updateLoyaltyCardInfo) {
        List<Instruction> arrayList = new ArrayList();
        for (com.samsung.android.spayfw.appinterface.Instruction instruction : updateLoyaltyCardInfo.getInstructions()) {
            String value = instruction.getValue();
            if (instruction.isEncrypt()) {
                value = card.ad().prepareLoyaltyDataForServerTA(value);
                if (value == null) {
                    Log.m286e("LoyaltyCardProcessor", "updateLoyaltyCard: Error getting encrypted value");
                }
            }
            Instruction instruction2 = new Instruction();
            instruction2.setOp(instruction.getOp());
            instruction2.setPath(instruction.getPath());
            instruction2.setValue(value);
            arrayList.add(instruction2);
        }
        return arrayList;
    }

    private LoyaltyCardProcessor(Context context) {
        super(context);
    }

    public void m439a(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback) {
        Log.m285d("LoyaltyCardProcessor", "updateLoyaltyCard() start");
        if (updateLoyaltyCardInfo != null) {
            try {
                if (!(updateLoyaltyCardInfo.getInstructions() == null || updateLoyaltyCardInfo.getInstructions().isEmpty() || updateLoyaltyCardInfo.getCardId() == null || updateLoyaltyCardInfo.getCardId().isEmpty() || updateLoyaltyCardInfo.getTokenId() == null || updateLoyaltyCardInfo.getTokenId().isEmpty() || iUpdateLoyaltyCardCallback == null)) {
                    if (this.iJ == null) {
                        Log.m286e("LoyaltyCardProcessor", "updateLoyaltyCard: Failed to initialize account");
                        iUpdateLoyaltyCardCallback.onFail(-1);
                        return;
                    }
                    Card r = this.iJ.m559r(updateLoyaltyCardInfo.getTokenId());
                    if (r == null || r.ac() == null) {
                        Log.m286e("LoyaltyCardProcessor", "updateLoyaltyCard: Invalid tokenId");
                        iUpdateLoyaltyCardCallback.onFail(-6);
                        return;
                    }
                    UpdateLoyaltyCardRequest b = this.lQ.m1140b(updateLoyaltyCardInfo.getCardId(), LoyaltyCardProcessor.m433a(r, updateLoyaltyCardInfo));
                    b.bf(m329P(r.getCardBrand()));
                    b.m836a(new LoyaltyCardProcessor(this, updateLoyaltyCardInfo, iUpdateLoyaltyCardCallback));
                    Log.m287i("LoyaltyCardProcessor", "updateLoyaltyCard: update loyalty card request made for cardId : " + updateLoyaltyCardInfo.getCardId());
                    Log.m285d("LoyaltyCardProcessor", "updateLoyaltyCard() end");
                    return;
                }
            } catch (Throwable e) {
                Log.m284c("LoyaltyCardProcessor", e.getMessage(), e);
            }
        }
        if (iUpdateLoyaltyCardCallback != null) {
            iUpdateLoyaltyCardCallback.onFail(-5);
        }
        Log.m286e("LoyaltyCardProcessor", "Input Validation failed: cardInfo = " + updateLoyaltyCardInfo + "; cb = " + iUpdateLoyaltyCardCallback);
        if (updateLoyaltyCardInfo != null) {
            Log.m286e("LoyaltyCardProcessor", "Input Validation failed: cardInfo.toString = " + updateLoyaltyCardInfo.toString());
        }
    }

    public void m437a(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, IExtractLoyaltyCardDetailResponseCallback iExtractLoyaltyCardDetailResponseCallback) {
        Log.m287i("LoyaltyCardProcessor", "extractLoyaltyCardDetails()");
        if (!(extractLoyaltyCardDetailRequest == null || iExtractLoyaltyCardDetailResponseCallback == null)) {
            try {
                if (!(extractLoyaltyCardDetailRequest.getTokenId() == null || extractLoyaltyCardDetailRequest.getTokenId().isEmpty() || extractLoyaltyCardDetailRequest.getCardRefID() == null || extractLoyaltyCardDetailRequest.getCardRefID().length <= 0 || extractLoyaltyCardDetailRequest.getTzEncData() == null || extractLoyaltyCardDetailRequest.getTzEncData().length <= 0)) {
                    Log.m285d("LoyaltyCardProcessor", "token id = " + extractLoyaltyCardDetailRequest.getTokenId());
                    if (this.iJ == null) {
                        Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: Failed to initialize account");
                        iExtractLoyaltyCardDetailResponseCallback.onFail(-1);
                        return;
                    }
                    Card r = this.iJ.m559r(extractLoyaltyCardDetailRequest.getTokenId());
                    if (r == null || r.ac() == null) {
                        Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: Invalid tokenId");
                        iExtractLoyaltyCardDetailResponseCallback.onFail(-6);
                        MstConfigurationManager.m604j(this.mContext).ap();
                        return;
                    }
                    ExtractCardDetailResult extractLoyaltyCardDetailTA = r.ad().extractLoyaltyCardDetailTA(extractLoyaltyCardDetailRequest);
                    if (extractLoyaltyCardDetailTA == null) {
                        Log.m286e("LoyaltyCardProcessor", " extractLoyaltyCardDetailTA returns null");
                        iExtractLoyaltyCardDetailResponseCallback.onFail(-36);
                    } else if (extractLoyaltyCardDetailTA.getErrorCode() == 0) {
                        LoyaltyCardDetail loyaltyCardDetail = new LoyaltyCardDetail();
                        Bundle bundle = new Bundle();
                        if (extractLoyaltyCardDetailTA.getCardnumber() != null) {
                            bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_CARD_NUMBER, extractLoyaltyCardDetailTA.getCardnumber());
                            Log.m285d("LoyaltyCardProcessor", "Card Number = " + extractLoyaltyCardDetailTA.getCardnumber());
                        }
                        if (extractLoyaltyCardDetailTA.getBarcodeContent() != null) {
                            bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_BARCODE_CONTENT, extractLoyaltyCardDetailTA.getBarcodeContent());
                            Log.m285d("LoyaltyCardProcessor", "Barcode Content = " + extractLoyaltyCardDetailTA.getBarcodeContent());
                        }
                        if (extractLoyaltyCardDetailTA.getImgSessionKey() != null) {
                            bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_IMG_SESSION_KEY, extractLoyaltyCardDetailTA.getImgSessionKey());
                            Log.m285d("LoyaltyCardProcessor", "Image Session Key = " + extractLoyaltyCardDetailTA.getImgSessionKey());
                        }
                        if (extractLoyaltyCardDetailTA.getExtraContent() != null) {
                            bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_AC_TOKEN_EXTRA, extractLoyaltyCardDetailTA.getExtraContent());
                            Log.m285d("LoyaltyCardProcessor", "Extra content = " + extractLoyaltyCardDetailTA.getExtraContent());
                        }
                        loyaltyCardDetail.setCardDetailbundle(bundle);
                        iExtractLoyaltyCardDetailResponseCallback.onSuccess(loyaltyCardDetail);
                    } else {
                        iExtractLoyaltyCardDetailResponseCallback.onFail(extractLoyaltyCardDetailTA.getErrorCode());
                    }
                    MstConfigurationManager.m604j(this.mContext).ap();
                    Log.m287i("LoyaltyCardProcessor", "extractLoyaltyCardDetails() end");
                    return;
                }
            } catch (Throwable e) {
                Log.m284c("LoyaltyCardProcessor", e.getMessage(), e);
                MstConfigurationManager.m604j(this.mContext).ap();
            }
        }
        if (extractLoyaltyCardDetailRequest == null) {
            Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(request) is null!");
        } else if (extractLoyaltyCardDetailRequest.getTokenId() == null || extractLoyaltyCardDetailRequest.getTokenId().isEmpty()) {
            Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(tokenId) is null!");
        } else if (extractLoyaltyCardDetailRequest.getCardRefID() == null || extractLoyaltyCardDetailRequest.getCardRefID().length <= 0) {
            Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(cardRefId) is null!");
        } else if (extractLoyaltyCardDetailRequest.getTzEncData() == null || extractLoyaltyCardDetailRequest.getTzEncData().length <= 0) {
            Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(cardData) is null!");
        } else {
            Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: inputs(callback) is null!");
        }
        if (iExtractLoyaltyCardDetailResponseCallback != null) {
            iExtractLoyaltyCardDetailResponseCallback.onFail(-5);
        }
        MstConfigurationManager.m604j(this.mContext).ap();
    }

    public void m438a(LoyaltyCardShowRequest loyaltyCardShowRequest, IPayCallback iPayCallback) {
        String str = null;
        Log.m285d("LoyaltyCardProcessor", "startLoyaltyCardPay() start");
        if (loyaltyCardShowRequest == null || iPayCallback == null || loyaltyCardShowRequest.getTokenId() == null) {
            if (iPayCallback != null) {
                if (loyaltyCardShowRequest != null) {
                    str = loyaltyCardShowRequest.getTokenId();
                }
                iPayCallback.onFail(str, -5);
            }
            Log.m286e("LoyaltyCardProcessor", "inputs are null!");
            return;
        }
        Card r = this.iJ.m559r(loyaltyCardShowRequest.getTokenId());
        if (r == null || r.ac() == null) {
            Log.m286e("LoyaltyCardProcessor", "extractLoyaltyCardDetails: Invalid tokenId");
            iPayCallback.onFail(loyaltyCardShowRequest.getTokenId(), -6);
            return;
        }
        PaymentDetailsRecord paymentDetailsRecord = new PaymentDetailsRecord();
        paymentDetailsRecord.setPaymentType(Card.m574y(r.getCardBrand()));
        paymentDetailsRecord.setBarcodeAttempted("ATTEMPTED");
        paymentDetailsRecord.setTrTokenId(loyaltyCardShowRequest.getTokenId());
        if (loyaltyCardShowRequest.getExtras() != null) {
            String string = loyaltyCardShowRequest.getExtras().getString(LoyaltyCardShowRequest.EXTRAS_KEY_CARD_NAME);
            if (string != null) {
                paymentDetailsRecord.setCardName(string);
            }
        }
        m435d(paymentDetailsRecord);
        iPayCallback.onFinish(loyaltyCardShowRequest.getTokenId(), 0, null);
        Log.m285d("LoyaltyCardProcessor", "startLoyaltyCardPay() end");
    }

    private void m435d(PaymentDetailsRecord paymentDetailsRecord) {
        Handler az = PaymentFrameworkApp.az();
        if (az != null) {
            Log.m285d("LoyaltyCardProcessor", "Post PAYFW_OPT_ANALYTICS_REPORT request");
            az.sendMessage(PaymentFrameworkMessage.m620a(21, paymentDetailsRecord, null));
            return;
        }
        Log.m286e("LoyaltyCardProcessor", "HANDLER IS NOT INITIAILIZED");
    }
}
