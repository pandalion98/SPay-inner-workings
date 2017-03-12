package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Account;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkApp.C0409a;
import com.samsung.android.spayfw.core.ResponseDataBuilder;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.QueryTokenRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.core.a.v */
public class TokenChangeChecker extends Processor implements Runnable {
    private static final Map<String, TokenChangeChecker> mo;
    private static final C0409a mp;
    protected IPushMessageCallback lS;
    protected String lU;
    protected String mTokenId;
    protected long mq;
    protected int mr;

    /* renamed from: com.samsung.android.spayfw.core.a.v.1 */
    class TokenChangeChecker extends C0413a<Response<TokenResponseData>, QueryTokenRequest> {
        final /* synthetic */ Card jL;
        final /* synthetic */ String lW;
        final /* synthetic */ TokenChangeChecker ms;

        TokenChangeChecker(TokenChangeChecker tokenChangeChecker, Card card, String str) {
            this.ms = tokenChangeChecker;
            this.jL = card;
            this.lW = str;
        }

        public void m519a(int i, Response<TokenResponseData> response) {
            Object obj;
            TokenStatus tokenStatus;
            int i2;
            String str;
            Object obj2;
            int i3 = 0;
            ProviderResponseData providerResponseData = null;
            String tokenStatus2 = this.jL.ac().getTokenStatus();
            Log.m287i("TokenChangeChecker", "onRequestComplete: Token change: code " + i);
            Card r = this.ms.iJ.m559r(this.ms.mTokenId);
            switch (i) {
                case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                    TokenChangeChecker.remove(this.ms.mTokenId);
                    obj = null;
                    tokenStatus = null;
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                    str = tokenStatus2;
                    obj2 = null;
                    break;
                case ECCurve.COORD_AFFINE /*0*/:
                    if (Utils.ak(this.ms.mContext)) {
                        TokenChangeChecker.m523a(this.ms.mContext, this.ms.mTokenId, 120000);
                    } else {
                        TokenChangeChecker.m523a(this.ms.mContext, this.ms.mTokenId, -1);
                    }
                    obj = null;
                    tokenStatus = null;
                    i2 = -1;
                    str = tokenStatus2;
                    obj2 = null;
                    break;
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    TokenResponseData tokenResponseData = (TokenResponseData) response.getResult();
                    int i4;
                    if (tokenResponseData != null) {
                        TokenRecord bq = this.ms.jJ.bq(this.ms.mTokenId);
                        if (r != null && bq != null) {
                            TokenStatus tokenStatus3 = new TokenStatus(ResponseDataBuilder.m635a(tokenResponseData), tokenResponseData.getStatus().getReason());
                            if (tokenResponseData.getData() != null && r.ad().isReplenishDataAvailable(tokenResponseData.getData())) {
                                providerResponseData = r.ad().replenishTokenTA(tokenResponseData.getData(), tokenResponseData.getStatus());
                                if (providerResponseData == null || providerResponseData.getErrorCode() != 0 || tokenStatus3 == null) {
                                    i3 = -36;
                                    obj = null;
                                } else {
                                    bq.setTokenStatus(tokenStatus3.getCode());
                                    bq.m1251H(tokenStatus3.getReason());
                                    r.ac().setTokenStatus(tokenStatus3.getCode());
                                    r.ac().m662H(tokenStatus3.getReason());
                                    this.ms.jJ.m1230d(bq);
                                    tokenStatus2 = bq.getTokenStatus();
                                    obj = 1;
                                }
                                TokenChangeChecker.remove(this.ms.mTokenId);
                                tokenStatus = tokenStatus3;
                                i2 = i3;
                                String str2 = tokenStatus2;
                                obj2 = obj;
                                i4 = 1;
                                str = str2;
                                break;
                            }
                            TokenChangeChecker.m523a(this.ms.mContext, this.ms.mTokenId, 120000);
                            return;
                        }
                        Log.m286e("TokenChangeChecker", "unable to get card object ");
                        if (bq != null) {
                            Log.m287i("TokenChangeChecker", "delete record from db ");
                            this.ms.jJ.m1229d(TokenColumn.ENROLLMENT_ID, bq.getEnrollmentId());
                        }
                        if (r != null) {
                            Log.m286e("TokenChangeChecker", "delete card object");
                            this.ms.iJ.m560s(r.getEnrollmentId());
                        }
                        TokenStatus tokenStatus4 = new TokenStatus(TokenStatus.DISPOSED, null);
                        TokenChangeChecker.remove(this.ms.mTokenId);
                        tokenStatus = tokenStatus4;
                        i2 = -6;
                        i4 = 1;
                        str = tokenStatus2;
                        obj2 = null;
                        break;
                    }
                    Log.m286e("TokenChangeChecker", "TokenResponseData is null");
                    i4 = 1;
                    tokenStatus = null;
                    i2 = 0;
                    str = tokenStatus2;
                    obj2 = null;
                    break;
                    break;
                case 404:
                case 410:
                    Log.m290w("TokenChangeChecker", "unable to find the token on server. something wrong. deleting the token");
                    ProviderResponseData providerResponseData2 = null;
                    TokenStatus tokenStatus5 = new TokenStatus(TokenStatus.DISPOSED, null);
                    if (r != null) {
                        providerResponseData2 = r.ad().updateTokenStatusTA(null, tokenStatus5);
                        this.ms.m521Q(this.ms.mTokenId);
                    }
                    TokenChangeChecker.remove(this.ms.mTokenId);
                    providerResponseData = providerResponseData2;
                    tokenStatus = tokenStatus5;
                    i2 = -6;
                    obj = null;
                    str = tokenStatus2;
                    obj2 = null;
                    break;
                case 503:
                    TokenChangeChecker.m523a(this.ms.mContext, this.ms.mTokenId, 120000);
                    obj = null;
                    tokenStatus = null;
                    i2 = -1;
                    str = tokenStatus2;
                    obj2 = null;
                    break;
                default:
                    if (i >= 500) {
                        TokenChangeChecker.m523a(this.ms.mContext, this.ms.mTokenId, 120000);
                    } else {
                        TokenChangeChecker.remove(this.ms.mTokenId);
                    }
                    obj = null;
                    tokenStatus = null;
                    i2 = -1;
                    str = tokenStatus2;
                    obj2 = null;
                    break;
            }
            if (i2 != 0) {
                Log.m286e("TokenChangeChecker", "Replenish Token Failed - Error Code = " + i2);
                if (r != null) {
                    r.ad().updateRequestStatus(new ProviderRequestStatus(23, -1, r.ac().aQ()));
                }
            } else if (r != null) {
                r.ad().updateRequestStatus(new ProviderRequestStatus(23, 0, r.ac().aQ()));
            }
            if (obj != null) {
                if (obj2 != null) {
                    this.ms.m331a(null, this.ms.mTokenId, str, PushMessage.TYPE_TOKEN_CHANGE, Card.m574y(this.lW), providerResponseData, true);
                } else {
                    Log.m286e("TokenChangeChecker", "processTokenChange:Send error report to TR server");
                    this.ms.m335b(null, this.ms.mTokenId, str, PushMessage.TYPE_TOKEN_CHANGE, Card.m574y(this.lW), providerResponseData, true);
                }
            }
            if (this.ms.lS != null) {
                if (i2 == -6) {
                    try {
                        this.ms.lS.onTokenStatusUpdate(this.ms.lU, this.ms.mTokenId, tokenStatus);
                    } catch (Throwable e) {
                        Log.m284c("TokenChangeChecker", e.getMessage(), e);
                    }
                } else if (i2 != 0) {
                    this.ms.lS.onFail(this.ms.lU, i2);
                } else {
                    this.ms.lS.onTokenReplenished(this.ms.lU, this.ms.mTokenId);
                }
                this.ms.lS = null;
                return;
            }
            Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
            if (i != -6 && i != -2) {
                intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_SYNC_ALL_CARDS);
            } else if (i == -6) {
                intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_SYNC_ALL_CARDS);
            } else if (i == -2) {
                intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_UPDATE_JWT_TOKEN);
            }
            PaymentFrameworkApp.m315a(intent);
        }
    }

    static {
        mo = new HashMap();
        mp = new C0409a();
    }

    public static synchronized void m523a(Context context, String str, long j) {
        synchronized (TokenChangeChecker.class) {
            Handler handler = TokenReplenisher.getHandler();
            Object obj = (TokenChangeChecker) mo.get(str);
            if (obj == null) {
                Log.m287i("TokenChangeChecker", "New Instance of Token Change Checker");
                obj = new TokenChangeChecker(context, str);
                obj.mq = j;
            } else {
                Log.m287i("TokenChangeChecker", "Update Instance of Token Change Checker");
                handler.removeCallbacks(obj);
                obj.mq = j;
            }
            if (obj.mq != -1) {
                handler.postDelayed(obj, obj.mq);
            }
            if (((long) obj.mr) >= 10) {
                Log.m290w("TokenChangeChecker", "Token Change Checker Retry Limit Reached. Try Token Replenisher.");
                TokenChangeChecker.remove(str);
                Account a = Account.m551a(context, null);
                if (a != null) {
                    Card r = a.m559r(str);
                    if (r != null) {
                        mp.m313a(r.ac().aQ());
                    } else {
                        Log.m286e("TokenChangeChecker", "Card is NULL");
                    }
                } else {
                    Log.m286e("TokenChangeChecker", "Account is NULL");
                }
            } else {
                mo.put(str, obj);
            }
        }
    }

    public static synchronized void m524a(Context context, String str, IPushMessageCallback iPushMessageCallback, String str2) {
        synchronized (TokenChangeChecker.class) {
            Handler handler = TokenReplenisher.getHandler();
            Runnable runnable = (TokenChangeChecker) mo.get(str);
            if (runnable == null) {
                Log.m287i("TokenChangeChecker", "New Instance of Token Change Checker");
                runnable = new TokenChangeChecker(context, str);
            } else {
                Log.m287i("TokenChangeChecker", "Update Instance of Token Change Checker");
                handler.removeCallbacks(runnable);
            }
            runnable.mq = 0;
            runnable.lS = iPushMessageCallback;
            runnable.lU = str2;
            handler.postDelayed(runnable, runnable.mq);
            mo.put(str, runnable);
        }
    }

    public static synchronized TokenChangeChecker m522S(String str) {
        TokenChangeChecker tokenChangeChecker;
        synchronized (TokenChangeChecker.class) {
            tokenChangeChecker = (TokenChangeChecker) mo.get(str);
        }
        return tokenChangeChecker;
    }

    public static synchronized void remove(String str) {
        synchronized (TokenChangeChecker.class) {
            Log.m287i("TokenChangeChecker", "Remove Instance of Token Change Checker");
            TokenReplenisher.getHandler().removeCallbacks((TokenChangeChecker) mo.remove(str));
        }
    }

    public static synchronized void restart() {
        synchronized (TokenChangeChecker.class) {
            Log.m287i("TokenChangeChecker", "restart");
            Handler handler = TokenReplenisher.getHandler();
            for (Entry entry : mo.entrySet()) {
                if (((TokenChangeChecker) entry.getValue()).mq == -1) {
                    TokenChangeChecker tokenChangeChecker = (TokenChangeChecker) entry.getValue();
                    tokenChangeChecker.mq = 0;
                    handler.postDelayed(tokenChangeChecker, tokenChangeChecker.mq);
                }
            }
        }
    }

    private TokenChangeChecker(Context context, String str) {
        super(context);
        this.mTokenId = str;
    }

    private void m521Q(String str) {
        Log.m287i("TokenChangeChecker", "deleting token id = " + str);
        if (this.jJ.m1229d(TokenColumn.TR_TOKEN_ID, str) < 1) {
            Log.m286e("TokenChangeChecker", "Not able to delete Token from DB");
        }
        this.iJ.m561t(str);
    }

    public void process() {
        TokenReplenisher.getHandler().post(this);
    }

    public void run() {
        Log.m285d("TokenChangeChecker", "Entered token change checker : tokenId " + this.mTokenId);
        Log.m287i("TokenChangeChecker", "Entered token change checker : retryCount " + this.mr);
        this.mr++;
        Card r = this.iJ.m559r(this.mTokenId);
        if (r == null) {
            Log.m286e("TokenChangeChecker", " unable to get card based on tokenId. ignore replenish request");
            return;
        }
        String cardBrand = r.getCardBrand();
        QueryTokenRequest x = this.lQ.m1141x(Card.m574y(r.getCardBrand()), this.mTokenId);
        x.m1205h(false);
        x.m839b(new TokenChangeChecker(this, r, cardBrand));
    }
}
