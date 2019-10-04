/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  com.google.gson.JsonObject
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.core;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.CardArts;
import com.samsung.android.spayfw.appinterface.CardColors;
import com.samsung.android.spayfw.appinterface.CardIssuer;
import com.samsung.android.spayfw.appinterface.CardIssuerApp;
import com.samsung.android.spayfw.appinterface.EnrollCardResult;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.SelectIdvResponse;
import com.samsung.android.spayfw.appinterface.TnC;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.d;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.remoteservice.models.Art;
import com.samsung.android.spayfw.remoteservice.models.Eula;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardColor;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardIssuerAppInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardIssuerInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ContactInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Expiry;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvSelectionResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Transaction;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.UserName;
import com.samsung.android.spayfw.storage.models.a;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.TAException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class m {
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String F(String string) {
        if (string == null) {
            return null;
        }
        if ("ENROLLED".equals((Object)string)) return "PENDING";
        if (!"PENDING_PROVISION".equals((Object)string)) return string;
        return "PENDING";
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static EnrollCardResult a(Context context, EnrollmentResponseData enrollmentResponseData) {
        if (enrollmentResponseData == null) {
            return null;
        }
        EnrollCardResult enrollCardResult = new EnrollCardResult();
        enrollCardResult.setEnrollmentId(enrollmentResponseData.getId());
        List<Eula> list = enrollmentResponseData.getEulas();
        if (list != null && !list.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (Eula eula : list) {
                TnC tnC = new TnC();
                tnC.setLocale(eula.getLocale());
                tnC.setType(eula.getType());
                tnC.setUsage(eula.getUsage());
                if (eula.getLocalFilePath() != null) {
                    File file = new File(eula.getLocalFilePath());
                    try {
                        tnC.setFd(ParcelFileDescriptor.open((File)file, (int)805306368));
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
                arrayList.add((Object)tnC);
            }
            enrollCardResult.setTnC((List<TnC>)arrayList);
        }
        com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", enrollCardResult.toString());
        return enrollCardResult;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ProvisionTokenResult a(Context context, com.samsung.android.spayfw.remoteservice.tokenrequester.f f2, c c2, e e2) {
        IdvOptionsData idvOptionsData;
        if (f2 == null || f2.fd() == null && f2.getResult() == null) {
            com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "Provision Response is null.");
            return null;
        }
        ProvisionTokenResult provisionTokenResult = new ProvisionTokenResult();
        if (f2.fd() != null && f2.fd().getMethods() != null && (idvOptionsData = f2.fd()) != null && idvOptionsData.getMethods() != null && idvOptionsData.getMethods().length > 0) {
            IdvOptionData[] arridvOptionData = idvOptionsData.getMethods();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < idvOptionsData.getMethods().length; ++i2) {
                IdvMethod idvMethod = new IdvMethod();
                idvMethod.setId(arridvOptionData[i2].getId());
                idvMethod.setType(arridvOptionData[i2].getType());
                idvMethod.setValue(arridvOptionData[i2].getValue());
                idvMethod.setScheme(arridvOptionData[i2].getScheme());
                if (arridvOptionData[i2].getData() != null) {
                    idvMethod.setData(arridvOptionData[i2].getData().toString());
                    com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "IDV data: " + arridvOptionData[i2].getData().toString());
                }
                if (("APP".equals((Object)idvMethod.getType()) || "CODE_ONLINEBANKING".equals((Object)idvMethod.getType())) && c2 != null) {
                    e e3 = c2.ad().processIdvOptionsDataTA(idvMethod);
                    if (e3 != null && e3.getErrorCode() == 0 && e3.cg() != null) {
                        idvMethod.setExtra(e3.cg());
                    } else {
                        com.samsung.android.spayfw.b.c.i("ResponseDataBuilder", " PayProvider error occurred while processing App2App idv data");
                    }
                }
                arrayList.add((Object)idvMethod);
            }
            provisionTokenResult.setIdv((List<IdvMethod>)arrayList);
        }
        provisionTokenResult.setToken(m.a(context, c2, (TokenResponseData)f2.getResult()));
        if (provisionTokenResult.getToken() != null && provisionTokenResult.getToken().getMetadata() != null && c2 != null) {
            provisionTokenResult.getToken().getMetadata().setSecurityCode(c2.getSecurityCode());
            m.a(context, provisionTokenResult.getToken().getMetadata(), c2);
        }
        if (e2 != null && e2.cg() != null) {
            provisionTokenResult.setBundle(e2.cg());
        }
        com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", provisionTokenResult.toString());
        return provisionTokenResult;
    }

    public static SelectIdvResponse a(IdvSelectionResponseData idvSelectionResponseData) {
        if (idvSelectionResponseData == null) {
            return null;
        }
        SelectIdvResponse selectIdvResponse = new SelectIdvResponse();
        selectIdvResponse.setMethodId(idvSelectionResponseData.getMethodId());
        selectIdvResponse.setCodeLength(idvSelectionResponseData.getCodeLength());
        selectIdvResponse.setMaxRetry((int)idvSelectionResponseData.getExpiryMax());
        selectIdvResponse.setExpirationTime(idvSelectionResponseData.getExpiryOn());
        selectIdvResponse.setMaxRequest(idvSelectionResponseData.getMaxAttempts());
        com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", selectIdvResponse.toString());
        return selectIdvResponse;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Token a(Context context, c c2, TokenResponseData tokenResponseData) {
        TokenStatus tokenStatus;
        int n2 = 0;
        if (tokenResponseData == null) {
            com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "TokenResponseData is null.");
            return null;
        }
        Token token = new Token();
        token.setTokenId(tokenResponseData.getId());
        if (tokenResponseData.getStatus() != null) {
            String string = tokenResponseData.getStatus().getReason();
            tokenStatus = new TokenStatus(m.a(tokenResponseData), string);
        } else {
            tokenStatus = new TokenStatus("PENDING_PROVISION", null);
        }
        token.setTokenStatus(tokenStatus);
        token.setTokenSuffix(tokenResponseData.getSuffix());
        if (tokenResponseData.getExpiry() != null) {
            token.setTokenExpiryMonth(tokenResponseData.getExpiry().getMonth());
            token.setTokenExpiryYear(tokenResponseData.getExpiry().getYear());
        }
        TokenMetaData tokenMetaData = new TokenMetaData();
        tokenMetaData.setCardPresentationMode(m.c(tokenResponseData));
        if (c2 != null && c2.ad() != null) {
            f f2 = c2.ad().getProviderTokenKey();
            if (f2 != null && f2.cn() != null) {
                Bundle bundle = c2.ad().getTokenMetaData();
                if (bundle != null) {
                    tokenMetaData.setExtraMetaData(bundle);
                }
            } else {
                com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "ProviderTokenKey is Null");
            }
        } else {
            com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "Card or PayNetProv is Null");
        }
        if (tokenResponseData.getUser() != null && tokenResponseData.getUser().getName() != null && tokenResponseData.getUser().getName().getFull() != null) {
            tokenMetaData.setCardHolderName(tokenResponseData.getUser().getName().getFull());
        }
        if (tokenResponseData.getCard() != null) {
            CardInfo cardInfo = tokenResponseData.getCard();
            tokenMetaData.setCardNetworkType(cardInfo.getBrand());
            tokenMetaData.setCardProductName(cardInfo.getName());
            tokenMetaData.setCardProductDesc(cardInfo.getDescription());
            tokenMetaData.setCardType(cardInfo.getType());
            tokenMetaData.setCardRefernceId(cardInfo.getReference());
            tokenMetaData.setCardSuffix(cardInfo.getSuffix());
            if (cardInfo.getExpiry() != null) {
                tokenMetaData.setCardExpiryMonth(cardInfo.getExpiry().getMonth());
                tokenMetaData.setCardExpiryYear(cardInfo.getExpiry().getYear());
            }
            if (cardInfo.getArts() != null) {
                ArrayList arrayList = new ArrayList();
                Art[] arrart = cardInfo.getArts();
                for (int i2 = 0; i2 < arrart.length; ++i2) {
                    CardArts cardArts = new CardArts();
                    cardArts.setHeight(arrart[i2].getHeight());
                    cardArts.setType(arrart[i2].getType());
                    cardArts.setUsage(arrart[i2].getUsage());
                    cardArts.setWidth(arrart[i2].getWidth());
                    if (arrart[i2].getLocalFilePath() != null) {
                        File file = new File(arrart[i2].getLocalFilePath());
                        try {
                            cardArts.setFd(ParcelFileDescriptor.open((File)file, (int)805306368));
                        }
                        catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                    }
                    arrayList.add((Object)cardArts);
                }
                tokenMetaData.setCardArts((List<CardArts>)arrayList);
            }
            if (cardInfo.getColors() != null) {
                CardColor[] arrcardColor = cardInfo.getColors();
                ArrayList arrayList = new ArrayList();
                while (n2 < arrcardColor.length) {
                    CardColors cardColors = new CardColors();
                    cardColors.setCode(arrcardColor[n2].getCode());
                    cardColors.setUsage(arrcardColor[n2].getUsage());
                    arrayList.add((Object)cardColors);
                    ++n2;
                }
                tokenMetaData.setCardColors((List<CardColors>)arrayList);
            }
            if (cardInfo.getIssuer() != null) {
                CardIssuerInfo cardIssuerInfo = cardInfo.getIssuer();
                CardIssuer cardIssuer = new CardIssuer();
                if (cardIssuerInfo.getContacts() != null) {
                    ContactInfo contactInfo = cardIssuerInfo.getContacts();
                    cardIssuer.setEmail(contactInfo.getEmail());
                    cardIssuer.setPhone(contactInfo.getPhone());
                    cardIssuer.setWeb(contactInfo.getWebsite());
                    cardIssuer.setFacebookAddress(contactInfo.getFacebook());
                    cardIssuer.setPinterestAddress(contactInfo.getPinterest());
                    cardIssuer.setTwitterAddress(contactInfo.getTwitter());
                }
                ArrayList arrayList = new ArrayList();
                if (cardIssuerInfo.getTnc() != null) {
                    Eula eula = cardIssuerInfo.getTnc();
                    TnC tnC = new TnC();
                    tnC.setType(eula.getType());
                    tnC.setUrl(eula.getUrl());
                    tnC.setUsage("SERVICE");
                    tnC.setLocale(eula.getLocale());
                    if (eula.getLocalFilePath() != null) {
                        File file = new File(eula.getLocalFilePath());
                        try {
                            tnC.setFd(ParcelFileDescriptor.open((File)file, (int)805306368));
                        }
                        catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                    }
                    arrayList.add((Object)tnC);
                }
                if (cardIssuerInfo.getPrivacy() != null) {
                    Eula eula = cardIssuerInfo.getPrivacy();
                    TnC tnC = new TnC();
                    tnC.setType(eula.getType());
                    tnC.setUrl(eula.getUrl());
                    tnC.setUsage("PRIVACY");
                    tnC.setLocale(eula.getLocale());
                    if (eula.getLocalFilePath() != null) {
                        File file = new File(eula.getLocalFilePath());
                        try {
                            tnC.setFd(ParcelFileDescriptor.open((File)file, (int)805306368));
                        }
                        catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                    }
                    arrayList.add((Object)tnC);
                }
                if (!arrayList.isEmpty()) {
                    cardIssuer.setTnC((List<TnC>)arrayList);
                }
                if (cardIssuerInfo.getApp() != null) {
                    CardIssuerAppInfo cardIssuerAppInfo = cardIssuerInfo.getApp();
                    CardIssuerApp cardIssuerApp = new CardIssuerApp();
                    cardIssuerApp.setDescription(cardIssuerAppInfo.getDescription());
                    cardIssuerApp.setDownloadUrl(cardIssuerAppInfo.getDownload());
                    cardIssuerApp.setName(cardIssuerAppInfo.getName());
                    cardIssuerApp.setPackageName(cardIssuerAppInfo.getId());
                    cardIssuerApp.setStore(cardIssuerAppInfo.getStore());
                    if (cardIssuerAppInfo.getIcon() != null) {
                        CardArts cardArts = new CardArts();
                        cardArts.setHeight(cardIssuerAppInfo.getIcon().getHeight());
                        cardArts.setWidth(cardIssuerAppInfo.getIcon().getWidth());
                        cardArts.setType(cardIssuerAppInfo.getIcon().getType());
                        cardArts.setUsage("BANK_APP_ICON");
                        if (cardIssuerAppInfo.getIcon().getLocalFilePath() != null) {
                            File file = new File(cardIssuerAppInfo.getIcon().getLocalFilePath());
                            try {
                                cardArts.setFd(ParcelFileDescriptor.open((File)file, (int)805306368));
                            }
                            catch (IOException iOException) {
                                iOException.printStackTrace();
                            }
                        }
                        cardIssuerApp.setCardArt(cardArts);
                    }
                    cardIssuer.setIssuerAppDetails(cardIssuerApp);
                }
                cardIssuer.setName(cardIssuerInfo.getName());
                tokenMetaData.setCardIssuer(cardIssuer);
            }
            token.setMetadata(tokenMetaData);
        }
        return token;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static c a(Context context, a a2) {
        c c2;
        if (a2 == null || a2.getCardBrand() == null) {
            com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "record or card brand is null");
            return null;
        }
        q q2 = new q();
        if (a2.getTokenStatus() != null) {
            q2.setTokenStatus(a2.getTokenStatus());
        }
        if (a2.fy() != null) {
            q2.H(a2.fy());
        }
        if (a2.getTrTokenId() != null) {
            q2.setTokenId(a2.getTrTokenId());
            if (a2.getTokenRefId() != null) {
                f f2 = new f(a2.getTokenRefId());
                f2.setTrTokenId(a2.getTrTokenId());
                q2.c(f2);
                com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "providerKeys is set: " + f2);
            }
        } else {
            com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "No Token Id for this Card");
        }
        try {
            c2 = new c(context, a2.getCardBrand(), a2.getCardType(), null, a2.ab(), q2);
        }
        catch (TAException tAException) {
            throw tAException;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        c2.setEnrollmentId(a2.getEnrollmentId());
        if (a2.fA() != 0L) {
            d d2 = new d();
            d2.b(a2.fA());
            c2.a(d2);
        }
        c2.j(a2.ab());
        return c2;
    }

    public static a a(a a2, TokenResponseData tokenResponseData, f f2) {
        if (a2 == null || tokenResponseData == null) {
            return a2;
        }
        if (f2 != null) {
            a2.setTokenRefId(f2.cn());
        }
        if (tokenResponseData.getStatus() != null) {
            a2.setTokenStatus(m.a(tokenResponseData));
            a2.H(tokenResponseData.getStatus().getReason());
        }
        if (tokenResponseData.getTransaction() != null) {
            a2.bv(tokenResponseData.getTransaction().getTransactionUrl());
        }
        if (tokenResponseData.getCard() != null && tokenResponseData.getCard().getType() != null) {
            a2.setCardType(tokenResponseData.getCard().getType());
        }
        if (tokenResponseData.getId() != null) {
            a2.setTrTokenId(tokenResponseData.getId());
        }
        a2.j(m.c(tokenResponseData));
        return a2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String a(TokenResponseData tokenResponseData) {
        block6 : {
            block5 : {
                if (tokenResponseData == null) break block5;
                if (tokenResponseData.getId() == null) {
                    return "ENROLLED";
                }
                if (tokenResponseData.getStatus() != null) break block6;
            }
            return null;
        }
        if ("PENDING".equals((Object)tokenResponseData.getStatus().getCode())) {
            return "PENDING_PROVISION";
        }
        return tokenResponseData.getStatus().getCode();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static List<IdvMethod> a(c c2, IdvOptionsData idvOptionsData) {
        if (idvOptionsData == null || idvOptionsData.getMethods() == null || idvOptionsData.getMethods().length <= 0) {
            return null;
        }
        IdvOptionData[] arridvOptionData = idvOptionsData.getMethods();
        ArrayList arrayList = new ArrayList();
        int n2 = 0;
        while (n2 < idvOptionsData.getMethods().length) {
            IdvMethod idvMethod = new IdvMethod();
            idvMethod.setId(arridvOptionData[n2].getId());
            idvMethod.setType(arridvOptionData[n2].getType());
            idvMethod.setValue(arridvOptionData[n2].getValue());
            idvMethod.setScheme(arridvOptionData[n2].getScheme());
            if (arridvOptionData[n2].getData() != null) {
                idvMethod.setData(arridvOptionData[n2].getData().toString());
                com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "IDV data: " + arridvOptionData[n2].getData().toString());
            }
            if (("APP".equals((Object)idvMethod.getType()) || "CODE_ONLINEBANKING".equals((Object)idvMethod.getType())) && c2 != null) {
                e e2 = c2.ad().processIdvOptionsDataTA(idvMethod);
                if (e2 != null && e2.getErrorCode() == 0 && e2.cg() != null) {
                    idvMethod.setExtra(e2.cg());
                } else {
                    com.samsung.android.spayfw.b.c.i("ResponseDataBuilder", " PayProvider error occurred while processing App2App idv data");
                }
            }
            arrayList.add((Object)idvMethod);
            ++n2;
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void a(Context context, TokenMetaData tokenMetaData, c c2) {
        int n2;
        block6 : {
            block5 : {
                block4 : {
                    n2 = 2;
                    if (c2 == null || tokenMetaData == null || tokenMetaData.getCardPresentationMode() != 0) break block4;
                    if (c2.ac() == null || c2.ac().aQ() == null) {
                        com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "Card Token OR Token Reference Id is NULL");
                        tokenMetaData.setCardPresentationMode(0);
                        return;
                    }
                    if (!h.ao(context) || !c2.ad().isPayAllowedForPresentationMode(n2)) break block5;
                    break block6;
                }
                com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "Card OR TokenMetaData is NULL");
                return;
            }
            n2 = 0;
        }
        if (c2.ad().isPayAllowedForPresentationMode(1)) {
            n2 |= 1;
        }
        if (c2.ad().isPayAllowedForPresentationMode(4)) {
            n2 |= 4;
        }
        if (c2.ad().isPayAllowedForPresentationMode(8)) {
            n2 |= 8;
        }
        tokenMetaData.setCardPresentationMode(n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void a(EnrollCardResult enrollCardResult) {
        if (enrollCardResult != null && enrollCardResult.getTnC() != null) {
            for (TnC tnC : enrollCardResult.getTnC()) {
                if (tnC == null || tnC.getFd() == null) continue;
                try {
                    tnC.getFd().close();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void a(TokenMetaData tokenMetaData) {
        if (tokenMetaData != null && tokenMetaData.getCardArts() != null) {
            for (CardArts cardArts : tokenMetaData.getCardArts()) {
                if (cardArts == null || cardArts.getFd() == null) continue;
                try {
                    cardArts.getFd().close();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            if (tokenMetaData.getCardIssuer() != null) {
                if (tokenMetaData.getCardIssuer().getIssuerAppDetails() != null && tokenMetaData.getCardIssuer().getIssuerAppDetails().getCardArt() != null && tokenMetaData.getCardIssuer().getIssuerAppDetails().getCardArt().getFd() != null) {
                    try {
                        tokenMetaData.getCardIssuer().getIssuerAppDetails().getCardArt().getFd().close();
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
                for (TnC tnC : tokenMetaData.getCardIssuer().getTnC()) {
                    if (tnC == null || tnC.getFd() == null) continue;
                    try {
                        tnC.getFd().close();
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                    }
                }
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static void a(com.samsung.android.spayfw.remoteservice.tokenrequester.f var0, ProvisionTokenResult var1_1) {
        if (var1_1 == null) ** GOTO lbl-1000
        m.b(var1_1.getBundle());
        if (var1_1.getToken() != null) {
            var2_2 = var1_1.getToken().getMetadata();
        } else lbl-1000: // 2 sources:
        {
            var2_2 = null;
        }
        if (var0 != null && var0.getResult() != null) {
            m.a((TokenResponseData)var0.getResult(), var2_2);
            return;
        }
        m.a(var2_2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void a(EnrollmentResponseData enrollmentResponseData, EnrollCardResult enrollCardResult) {
        m.a(enrollCardResult);
        if (enrollmentResponseData != null && enrollmentResponseData.getEulas() != null) {
            Iterator iterator = enrollmentResponseData.getEulas().iterator();
            while (iterator.hasNext()) {
                String string = ((Eula)iterator.next()).getLocalFilePath();
                if (string == null || string.isEmpty()) continue;
                new File(string).delete();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void a(TokenResponseData tokenResponseData, TokenMetaData tokenMetaData) {
        block9 : {
            block8 : {
                m.a(tokenMetaData);
                if (tokenMetaData != null && tokenMetaData.getExtraMetaData() != null) {
                    m.c(tokenMetaData.getExtraMetaData());
                }
                if (tokenResponseData == null || tokenResponseData.getCard() == null) break block8;
                if (tokenResponseData.getCard().getArts() != null) {
                    for (Art art : tokenResponseData.getCard().getArts()) {
                        if (art.getLocalFilePath() == null) continue;
                        new File(art.getLocalFilePath()).delete();
                    }
                }
                if (tokenResponseData.getCard().getIssuer() == null) break block8;
                if (tokenResponseData.getCard().getIssuer().getApp() != null && tokenResponseData.getCard().getIssuer().getApp().getIcon() != null && tokenResponseData.getCard().getIssuer().getApp().getIcon().getLocalFilePath() != null) {
                    new File(tokenResponseData.getCard().getIssuer().getApp().getIcon().getLocalFilePath()).delete();
                }
                if (tokenResponseData.getCard().getIssuer().getPrivacy() != null && tokenResponseData.getCard().getIssuer().getPrivacy().getLocalFilePath() != null) {
                    new File(tokenResponseData.getCard().getIssuer().getPrivacy().getLocalFilePath()).delete();
                }
                if (tokenResponseData.getCard().getIssuer().getTnc() != null && tokenResponseData.getCard().getIssuer().getTnc().getLocalFilePath() != null) break block9;
            }
            return;
        }
        new File(tokenResponseData.getCard().getIssuer().getTnc().getLocalFilePath()).delete();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Bundle b(TokenResponseData tokenResponseData) {
        if (tokenResponseData == null) {
            com.samsung.android.spayfw.b.c.i("ResponseDataBuilder", "Provision Response is null.");
            return null;
        } else {
            if (tokenResponseData.getCard() == null || tokenResponseData.getCard().getReference() == null) return null;
            {
                Bundle bundle = new Bundle();
                bundle.putString("cardRefId", tokenResponseData.getCard().getReference());
                return bundle;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void b(Bundle bundle) {
        if (bundle != null) {
            String string;
            com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "Clean providerProvision Data");
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor)bundle.getParcelable("loyaltyResponseDataFd");
            if (parcelFileDescriptor != null) {
                try {
                    com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "Close provider Pfd");
                    parcelFileDescriptor.close();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            if ((string = bundle.getString("loyaltyResponseDataFilePath")) != null) {
                com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "Delete  temp File");
                new File(string).delete();
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int c(TokenResponseData tokenResponseData) {
        int n2;
        String[] arrstring;
        int n3 = 0;
        if (tokenResponseData != null) {
            arrstring = tokenResponseData.getPresentationModes();
            n3 = 0;
            if (arrstring == null) return n3;
            int n4 = arrstring.length;
            n3 = 0;
            if (n4 <= 0) return n3;
            n2 = arrstring.length;
        } else {
            com.samsung.android.spayfw.b.c.e("ResponseDataBuilder", "TokenResponseData is NULL");
            return n3;
        }
        for (int i2 = 0; i2 < n2; ++i2) {
            String string = arrstring[i2];
            if (string.equals((Object)"MST")) {
                n3 |= 2;
                continue;
            }
            if (string.equals((Object)"NFC")) {
                n3 |= 1;
                continue;
            }
            if (string.equals((Object)"ECM")) {
                n3 |= 4;
                continue;
            }
            if (!string.equals((Object)"APP")) continue;
            n3 |= 8;
        }
        return n3;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void c(Bundle bundle) {
        if (bundle != null) {
            String string;
            com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "cleanAppTokenMetaExtra");
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor)bundle.getParcelable("extraMetaDataFd");
            if (parcelFileDescriptor != null) {
                try {
                    com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "Close provider Pfd");
                    parcelFileDescriptor.close();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
            if ((string = bundle.getString("extraMetaDataFilePath")) != null) {
                com.samsung.android.spayfw.b.c.d("ResponseDataBuilder", "Delete  temp File");
                new File(string).delete();
            }
        }
    }
}

