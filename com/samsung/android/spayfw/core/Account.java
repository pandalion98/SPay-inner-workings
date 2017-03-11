package com.samsung.android.spayfw.core;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.core.a */
public class Account {
    private static Account iJ;
    private String iH;
    private List<Card> iI;
    private Context mContext;

    static {
        iJ = null;
    }

    public static synchronized Account m551a(Context context, String str) {
        Account account;
        synchronized (Account.class) {
            Log.m285d("Account", "Account getInstance accId = " + str);
            if (str == null) {
                Log.m285d("Account", "Account getInstance accId is null");
                account = iJ;
            } else if (iJ == null) {
                Log.m285d("Account", "Account getInstance mAccount is null");
                iJ = new Account(context, str);
                account = iJ;
            } else if (iJ.getAccountId().equals(str)) {
                Log.m285d("Account", "Account getInstance mAccId equals accId");
                account = iJ;
            } else {
                Log.m285d("Account", "Account getInstance accId = " + str + "mAccId = " + iJ.getAccountId());
                account = null;
            }
        }
        return account;
    }

    private Account(Context context, String str) {
        this.iH = null;
        this.iI = null;
        this.iH = str;
        this.mContext = context;
    }

    public String getAccountId() {
        return this.iH;
    }

    public boolean m557p(String str) {
        if (this.iH == null || !this.iH.equals(str)) {
            return false;
        }
        return true;
    }

    public synchronized Card m555a(EnrollCardInfo enrollCardInfo) {
        Card card;
        String str;
        String str2;
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            String cardBrand;
            BinAttribute binAttribute = BinAttribute.getBinAttribute(((EnrollCardPanInfo) enrollCardInfo).getPAN());
            Log.m285d("Account", "BinAttribute : EnrollCardPanInfo : " + ((EnrollCardPanInfo) enrollCardInfo).getPAN());
            if (binAttribute != null) {
                cardBrand = binAttribute.getCardBrand();
            } else {
                cardBrand = null;
            }
            str = null;
            str2 = cardBrand;
        } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
            Log.m285d("Account", "getCardBrand");
            str2 = ((EnrollCardReferenceInfo) enrollCardInfo).getCardBrand();
            str = ((EnrollCardReferenceInfo) enrollCardInfo).getCardType();
        } else if (enrollCardInfo instanceof EnrollCardLoyaltyInfo) {
            Log.m285d("Account", "PaymentFramework.CARD_BRAND_LOYALTY");
            str2 = PaymentFramework.CARD_BRAND_LOYALTY;
            str = null;
        } else {
            str = null;
            str2 = null;
        }
        if (str2 != null) {
            int x = m552x(enrollCardInfo.getCardPresentationMode());
            if (PlccConstants.BRAND.equals(str2)) {
                x &= -2;
            }
            card = new Card(this.mContext, str2, str, enrollCardInfo.getCardEntryMode(), x);
            if (this.iI == null) {
                this.iI = new ArrayList();
            }
            this.iI.add(card);
        } else {
            Log.m286e("Account", "No valid cardType is found");
            card = null;
        }
        return card;
    }

    public synchronized boolean m556a(Card card) {
        boolean z;
        z = false;
        if (card != null) {
            if (this.iI == null) {
                this.iI = new ArrayList();
            }
            this.iI.add(card);
            z = true;
        }
        return z;
    }

    public synchronized Card m558q(String str) {
        Card card;
        if (!(this.iI == null || this.iI.isEmpty() || str == null)) {
            for (Card card2 : this.iI) {
                if (card2.getEnrollmentId() != null && str.equals(card2.getEnrollmentId())) {
                    break;
                }
            }
        }
        card2 = null;
        return card2;
    }

    public synchronized Card m559r(String str) {
        Card card;
        if (str == null) {
            card = null;
        } else {
            if (!(this.iI == null || this.iI.isEmpty())) {
                for (Card card2 : this.iI) {
                    if (str.equals(card2.ac().getTokenId())) {
                        break;
                    }
                }
            }
            card2 = null;
        }
        return card2;
    }

    public synchronized boolean m560s(String str) {
        boolean z;
        if (!(this.iI == null || this.iI.isEmpty() || str == null)) {
            for (int i = 0; i < this.iI.size(); i++) {
                Card card = (Card) this.iI.get(i);
                if (card.getEnrollmentId() != null && card.getEnrollmentId().equals(str)) {
                    card.delete();
                    Log.m285d("Account", "deleteCardByEnrollmentId: done for " + str);
                    this.iI.remove(i);
                    z = true;
                    break;
                }
            }
        }
        z = false;
        return z;
    }

    public synchronized boolean m561t(String str) {
        boolean z;
        if (!(this.iI == null || this.iI.isEmpty() || str == null)) {
            for (int i = 0; i < this.iI.size(); i++) {
                Card card = (Card) this.iI.get(i);
                if (card.ac().getTokenId() != null && card.ac().getTokenId().equals(str)) {
                    card.delete();
                    Log.m285d("Account", "deleteCardByTokenId: done for " + str);
                    this.iI.remove(i);
                    z = true;
                    break;
                }
            }
        }
        z = false;
        return z;
    }

    public synchronized List<Card> m553W() {
        return this.iI;
    }

    public synchronized String m562u(String str) {
        String str2;
        str2 = null;
        if (str == null) {
            str2 = "NONE";
        } else {
            if (!(this.iI == null || this.iI.isEmpty())) {
                int i = 0;
                String str3 = null;
                while (i < this.iI.size()) {
                    Card card = (Card) this.iI.get(i);
                    if (!card.getCardBrand().equals(str) || card.ac() == null || card.ac().getTokenId() == null || card.ac().getTokenStatus() == null) {
                        str2 = str3;
                    } else if (TokenStatus.PENDING_ENROLLED.equals(card.ac().getTokenStatus())) {
                        str2 = str3;
                    } else {
                        String tokenId = card.ac().getTokenId();
                        str2 = tokenId + ":" + ResponseDataBuilder.m628F(card.ac().getTokenStatus());
                        if (str3 != null) {
                            str2 = str3 + "," + str2;
                        }
                    }
                    i++;
                    str3 = str2;
                }
                str2 = str3;
            }
            if (str2 == null) {
                str2 = "NONE";
            }
        }
        return str2;
    }

    public synchronized Card m554X() {
        Card card;
        String config = ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_DEFAULT_CARD);
        if (config == null) {
            card = null;
        } else {
            card = m559r(config);
        }
        return card;
    }

    private int m552x(String str) {
        if (str == null) {
            return 0;
        }
        if (str.contains(EnrollCardInfo.CARD_PRESENTATION_MODE_ALL)) {
            return 15;
        }
        if (str.contains(EnrollCardInfo.CARD_PRESENTATION_MODE_MST)) {
            return 2;
        }
        if (str.contains(EnrollCardInfo.CARD_PRESENTATION_MODE_NFC)) {
            return 1;
        }
        if (str.contains(EnrollCardInfo.CARD_PRESENTATION_MODE_ECM)) {
            return 4;
        }
        return 0;
    }
}
