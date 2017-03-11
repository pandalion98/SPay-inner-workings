package com.samsung.android.spayfw.core;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import com.samsung.android.spayfw.appinterface.CardArts;
import com.samsung.android.spayfw.appinterface.CardColors;
import com.samsung.android.spayfw.appinterface.CardIssuer;
import com.samsung.android.spayfw.appinterface.CardIssuerApp;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardResult;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.SelectIdvResponse;
import com.samsung.android.spayfw.appinterface.TnC;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData.Card;
import com.samsung.android.spayfw.remoteservice.models.Art;
import com.samsung.android.spayfw.remoteservice.models.Eula;
import com.samsung.android.spayfw.remoteservice.tokenrequester.ProvisionResponse;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardColor;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardIssuerAppInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardIssuerInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ContactInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvSelectionResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.TAException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.core.m */
public class ResponseDataBuilder {
    public static EnrollCardResult m629a(Context context, EnrollmentResponseData enrollmentResponseData) {
        if (enrollmentResponseData == null) {
            return null;
        }
        EnrollCardResult enrollCardResult = new EnrollCardResult();
        enrollCardResult.setEnrollmentId(enrollmentResponseData.getId());
        List<Eula> eulas = enrollmentResponseData.getEulas();
        if (!(eulas == null || eulas.isEmpty())) {
            List arrayList = new ArrayList();
            for (Eula eula : eulas) {
                TnC tnC = new TnC();
                tnC.setLocale(eula.getLocale());
                tnC.setType(eula.getType());
                tnC.setUsage(eula.getUsage());
                if (eula.getLocalFilePath() != null) {
                    try {
                        tnC.setFd(ParcelFileDescriptor.open(new File(eula.getLocalFilePath()), 805306368));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                arrayList.add(tnC);
            }
            enrollCardResult.setTnC(arrayList);
        }
        Log.m285d("ResponseDataBuilder", enrollCardResult.toString());
        return enrollCardResult;
    }

    public static void m641a(EnrollmentResponseData enrollmentResponseData, EnrollCardResult enrollCardResult) {
        ResponseDataBuilder.m638a(enrollCardResult);
        if (enrollmentResponseData != null && enrollmentResponseData.getEulas() != null) {
            for (Eula localFilePath : enrollmentResponseData.getEulas()) {
                String localFilePath2 = localFilePath.getLocalFilePath();
                if (!(localFilePath2 == null || localFilePath2.isEmpty())) {
                    new File(localFilePath2).delete();
                }
            }
        }
    }

    public static ProvisionTokenResult m630a(Context context, ProvisionResponse provisionResponse, Card card, ProviderResponseData providerResponseData) {
        if (provisionResponse == null || (provisionResponse.fd() == null && provisionResponse.getResult() == null)) {
            Log.m286e("ResponseDataBuilder", "Provision Response is null.");
            return null;
        }
        ProvisionTokenResult provisionTokenResult = new ProvisionTokenResult();
        if (!(provisionResponse.fd() == null || provisionResponse.fd().getMethods() == null)) {
            IdvOptionsData fd = provisionResponse.fd();
            if (!(fd == null || fd.getMethods() == null || fd.getMethods().length <= 0)) {
                IdvOptionData[] methods = fd.getMethods();
                List arrayList = new ArrayList();
                for (int i = 0; i < fd.getMethods().length; i++) {
                    IdvMethod idvMethod = new IdvMethod();
                    idvMethod.setId(methods[i].getId());
                    idvMethod.setType(methods[i].getType());
                    idvMethod.setValue(methods[i].getValue());
                    idvMethod.setScheme(methods[i].getScheme());
                    if (methods[i].getData() != null) {
                        idvMethod.setData(methods[i].getData().toString());
                        Log.m285d("ResponseDataBuilder", "IDV data: " + methods[i].getData().toString());
                    }
                    if ((IdvMethod.IDV_TYPE_APP.equals(idvMethod.getType()) || IdvMethod.IDV_TYPE_CODE_ONLINEBANKING.equals(idvMethod.getType())) && card != null) {
                        ProviderResponseData processIdvOptionsDataTA = card.ad().processIdvOptionsDataTA(idvMethod);
                        if (processIdvOptionsDataTA == null || processIdvOptionsDataTA.getErrorCode() != 0 || processIdvOptionsDataTA.cg() == null) {
                            Log.m287i("ResponseDataBuilder", " PayProvider error occurred while processing App2App idv data");
                        } else {
                            idvMethod.setExtra(processIdvOptionsDataTA.cg());
                        }
                    }
                    arrayList.add(idvMethod);
                }
                provisionTokenResult.setIdv(arrayList);
            }
        }
        provisionTokenResult.setToken(ResponseDataBuilder.m632a(context, card, (TokenResponseData) provisionResponse.getResult()));
        if (!(provisionTokenResult.getToken() == null || provisionTokenResult.getToken().getMetadata() == null || card == null)) {
            provisionTokenResult.getToken().getMetadata().setSecurityCode(card.getSecurityCode());
            ResponseDataBuilder.m637a(context, provisionTokenResult.getToken().getMetadata(), card);
        }
        if (!(providerResponseData == null || providerResponseData.cg() == null)) {
            provisionTokenResult.setBundle(providerResponseData.cg());
        }
        Log.m285d("ResponseDataBuilder", provisionTokenResult.toString());
        return provisionTokenResult;
    }

    public static Token m632a(Context context, Card card, TokenResponseData tokenResponseData) {
        int i = 0;
        if (tokenResponseData == null) {
            Log.m286e("ResponseDataBuilder", "TokenResponseData is null.");
            return null;
        }
        TokenStatus tokenStatus;
        Token token = new Token();
        token.setTokenId(tokenResponseData.getId());
        if (tokenResponseData.getStatus() != null) {
            tokenStatus = new TokenStatus(ResponseDataBuilder.m635a(tokenResponseData), tokenResponseData.getStatus().getReason());
        } else {
            tokenStatus = new TokenStatus(TokenStatus.PENDING_PROVISION, null);
        }
        token.setTokenStatus(tokenStatus);
        token.setTokenSuffix(tokenResponseData.getSuffix());
        if (tokenResponseData.getExpiry() != null) {
            token.setTokenExpiryMonth(tokenResponseData.getExpiry().getMonth());
            token.setTokenExpiryYear(tokenResponseData.getExpiry().getYear());
        }
        TokenMetaData tokenMetaData = new TokenMetaData();
        tokenMetaData.setCardPresentationMode(ResponseDataBuilder.m645c(tokenResponseData));
        if (card == null || card.ad() == null) {
            Log.m286e("ResponseDataBuilder", "Card or PayNetProv is Null");
        } else {
            ProviderTokenKey providerTokenKey = card.ad().getProviderTokenKey();
            if (providerTokenKey == null || providerTokenKey.cn() == null) {
                Log.m286e("ResponseDataBuilder", "ProviderTokenKey is Null");
            } else {
                Bundle tokenMetaData2 = card.ad().getTokenMetaData();
                if (tokenMetaData2 != null) {
                    tokenMetaData.setExtraMetaData(tokenMetaData2);
                }
            }
        }
        if (!(tokenResponseData.getUser() == null || tokenResponseData.getUser().getName() == null || tokenResponseData.getUser().getName().getFull() == null)) {
            tokenMetaData.setCardHolderName(tokenResponseData.getUser().getName().getFull());
        }
        if (tokenResponseData.getCard() != null) {
            CardInfo card2 = tokenResponseData.getCard();
            tokenMetaData.setCardNetworkType(card2.getBrand());
            tokenMetaData.setCardProductName(card2.getName());
            tokenMetaData.setCardProductDesc(card2.getDescription());
            tokenMetaData.setCardType(card2.getType());
            tokenMetaData.setCardRefernceId(card2.getReference());
            tokenMetaData.setCardSuffix(card2.getSuffix());
            if (card2.getExpiry() != null) {
                tokenMetaData.setCardExpiryMonth(card2.getExpiry().getMonth());
                tokenMetaData.setCardExpiryYear(card2.getExpiry().getYear());
            }
            if (card2.getArts() != null) {
                List arrayList = new ArrayList();
                Art[] arts = card2.getArts();
                for (int i2 = 0; i2 < arts.length; i2++) {
                    CardArts cardArts = new CardArts();
                    cardArts.setHeight(arts[i2].getHeight());
                    cardArts.setType(arts[i2].getType());
                    cardArts.setUsage(arts[i2].getUsage());
                    cardArts.setWidth(arts[i2].getWidth());
                    if (arts[i2].getLocalFilePath() != null) {
                        try {
                            cardArts.setFd(ParcelFileDescriptor.open(new File(arts[i2].getLocalFilePath()), 805306368));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    arrayList.add(cardArts);
                }
                tokenMetaData.setCardArts(arrayList);
            }
            if (card2.getColors() != null) {
                CardColor[] colors = card2.getColors();
                List arrayList2 = new ArrayList();
                while (i < colors.length) {
                    CardColors cardColors = new CardColors();
                    cardColors.setCode(colors[i].getCode());
                    cardColors.setUsage(colors[i].getUsage());
                    arrayList2.add(cardColors);
                    i++;
                }
                tokenMetaData.setCardColors(arrayList2);
            }
            if (card2.getIssuer() != null) {
                Eula tnc;
                TnC tnC;
                CardIssuerInfo issuer = card2.getIssuer();
                CardIssuer cardIssuer = new CardIssuer();
                if (issuer.getContacts() != null) {
                    ContactInfo contacts = issuer.getContacts();
                    cardIssuer.setEmail(contacts.getEmail());
                    cardIssuer.setPhone(contacts.getPhone());
                    cardIssuer.setWeb(contacts.getWebsite());
                    cardIssuer.setFacebookAddress(contacts.getFacebook());
                    cardIssuer.setPinterestAddress(contacts.getPinterest());
                    cardIssuer.setTwitterAddress(contacts.getTwitter());
                }
                List arrayList3 = new ArrayList();
                if (issuer.getTnc() != null) {
                    tnc = issuer.getTnc();
                    tnC = new TnC();
                    tnC.setType(tnc.getType());
                    tnC.setUrl(tnc.getUrl());
                    tnC.setUsage(TnC.USAGE_SERVICE);
                    tnC.setLocale(tnc.getLocale());
                    if (tnc.getLocalFilePath() != null) {
                        try {
                            tnC.setFd(ParcelFileDescriptor.open(new File(tnc.getLocalFilePath()), 805306368));
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    arrayList3.add(tnC);
                }
                if (issuer.getPrivacy() != null) {
                    tnc = issuer.getPrivacy();
                    tnC = new TnC();
                    tnC.setType(tnc.getType());
                    tnC.setUrl(tnc.getUrl());
                    tnC.setUsage(TnC.USAGE_PRIVACY);
                    tnC.setLocale(tnc.getLocale());
                    if (tnc.getLocalFilePath() != null) {
                        try {
                            tnC.setFd(ParcelFileDescriptor.open(new File(tnc.getLocalFilePath()), 805306368));
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    arrayList3.add(tnC);
                }
                if (!arrayList3.isEmpty()) {
                    cardIssuer.setTnC(arrayList3);
                }
                if (issuer.getApp() != null) {
                    CardIssuerAppInfo app = issuer.getApp();
                    CardIssuerApp cardIssuerApp = new CardIssuerApp();
                    cardIssuerApp.setDescription(app.getDescription());
                    cardIssuerApp.setDownloadUrl(app.getDownload());
                    cardIssuerApp.setName(app.getName());
                    cardIssuerApp.setPackageName(app.getId());
                    cardIssuerApp.setStore(app.getStore());
                    if (app.getIcon() != null) {
                        CardArts cardArts2 = new CardArts();
                        cardArts2.setHeight(app.getIcon().getHeight());
                        cardArts2.setWidth(app.getIcon().getWidth());
                        cardArts2.setType(app.getIcon().getType());
                        cardArts2.setUsage(CardArts.USAGE_BANK_APP_ICON);
                        if (app.getIcon().getLocalFilePath() != null) {
                            try {
                                cardArts2.setFd(ParcelFileDescriptor.open(new File(app.getIcon().getLocalFilePath()), 805306368));
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        cardIssuerApp.setCardArt(cardArts2);
                    }
                    cardIssuer.setIssuerAppDetails(cardIssuerApp);
                }
                cardIssuer.setName(issuer.getName());
                tokenMetaData.setCardIssuer(cardIssuer);
            }
            token.setMetadata(tokenMetaData);
        }
        return token;
    }

    public static String m635a(TokenResponseData tokenResponseData) {
        if (tokenResponseData == null) {
            return null;
        }
        if (tokenResponseData.getId() == null) {
            return TokenStatus.PENDING_ENROLLED;
        }
        if (tokenResponseData.getStatus() == null) {
            return null;
        }
        if (Card.STATUS_PENDING.equals(tokenResponseData.getStatus().getCode())) {
            return TokenStatus.PENDING_PROVISION;
        }
        return tokenResponseData.getStatus().getCode();
    }

    public static void m640a(ProvisionResponse provisionResponse, ProvisionTokenResult provisionTokenResult) {
        TokenMetaData metadata;
        if (provisionTokenResult != null) {
            ResponseDataBuilder.m644b(provisionTokenResult.getBundle());
            if (provisionTokenResult.getToken() != null) {
                metadata = provisionTokenResult.getToken().getMetadata();
                if (provisionResponse != null || provisionResponse.getResult() == null) {
                    ResponseDataBuilder.m639a(metadata);
                } else {
                    ResponseDataBuilder.m642a((TokenResponseData) provisionResponse.getResult(), metadata);
                    return;
                }
            }
        }
        metadata = null;
        if (provisionResponse != null) {
        }
        ResponseDataBuilder.m639a(metadata);
    }

    private static void m644b(Bundle bundle) {
        if (bundle != null) {
            Log.m285d("ResponseDataBuilder", "Clean providerProvision Data");
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) bundle.getParcelable(ProvisionTokenResult.BUNDLE_KEY_RESPONSE_DATA_FD);
            if (parcelFileDescriptor != null) {
                try {
                    Log.m285d("ResponseDataBuilder", "Close provider Pfd");
                    parcelFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String string = bundle.getString(ProvisionTokenResult.BUNDLE_KEY_RESPONSE_DATA_FILE_PATH);
            if (string != null) {
                Log.m285d("ResponseDataBuilder", "Delete  temp File");
                new File(string).delete();
            }
        }
    }

    public static void m646c(Bundle bundle) {
        if (bundle != null) {
            Log.m285d("ResponseDataBuilder", "cleanAppTokenMetaExtra");
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) bundle.getParcelable(TokenMetaData.BUNDLE_KEY_EXTRA_META_DATA_FD);
            if (parcelFileDescriptor != null) {
                try {
                    Log.m285d("ResponseDataBuilder", "Close provider Pfd");
                    parcelFileDescriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String string = bundle.getString(TokenMetaData.BUNDLE_KEY_EXTRA_META_DATA_FILE_PATH);
            if (string != null) {
                Log.m285d("ResponseDataBuilder", "Delete  temp File");
                new File(string).delete();
            }
        }
    }

    public static void m642a(TokenResponseData tokenResponseData, TokenMetaData tokenMetaData) {
        ResponseDataBuilder.m639a(tokenMetaData);
        if (!(tokenMetaData == null || tokenMetaData.getExtraMetaData() == null)) {
            ResponseDataBuilder.m646c(tokenMetaData.getExtraMetaData());
        }
        if (tokenResponseData != null && tokenResponseData.getCard() != null) {
            if (tokenResponseData.getCard().getArts() != null) {
                for (Art art : tokenResponseData.getCard().getArts()) {
                    if (art.getLocalFilePath() != null) {
                        new File(art.getLocalFilePath()).delete();
                    }
                }
            }
            if (tokenResponseData.getCard().getIssuer() != null) {
                if (!(tokenResponseData.getCard().getIssuer().getApp() == null || tokenResponseData.getCard().getIssuer().getApp().getIcon() == null || tokenResponseData.getCard().getIssuer().getApp().getIcon().getLocalFilePath() == null)) {
                    new File(tokenResponseData.getCard().getIssuer().getApp().getIcon().getLocalFilePath()).delete();
                }
                if (!(tokenResponseData.getCard().getIssuer().getPrivacy() == null || tokenResponseData.getCard().getIssuer().getPrivacy().getLocalFilePath() == null)) {
                    new File(tokenResponseData.getCard().getIssuer().getPrivacy().getLocalFilePath()).delete();
                }
                if (tokenResponseData.getCard().getIssuer().getTnc() != null && tokenResponseData.getCard().getIssuer().getTnc().getLocalFilePath() != null) {
                    new File(tokenResponseData.getCard().getIssuer().getTnc().getLocalFilePath()).delete();
                }
            }
        }
    }

    public static SelectIdvResponse m631a(IdvSelectionResponseData idvSelectionResponseData) {
        if (idvSelectionResponseData == null) {
            return null;
        }
        SelectIdvResponse selectIdvResponse = new SelectIdvResponse();
        selectIdvResponse.setMethodId(idvSelectionResponseData.getMethodId());
        selectIdvResponse.setCodeLength(idvSelectionResponseData.getCodeLength());
        selectIdvResponse.setMaxRetry((int) idvSelectionResponseData.getExpiryMax());
        selectIdvResponse.setExpirationTime(idvSelectionResponseData.getExpiryOn());
        selectIdvResponse.setMaxRequest(idvSelectionResponseData.getMaxAttempts());
        Log.m285d("ResponseDataBuilder", selectIdvResponse.toString());
        return selectIdvResponse;
    }

    public static List<IdvMethod> m636a(Card card, IdvOptionsData idvOptionsData) {
        if (idvOptionsData == null || idvOptionsData.getMethods() == null || idvOptionsData.getMethods().length <= 0) {
            return null;
        }
        IdvOptionData[] methods = idvOptionsData.getMethods();
        List<IdvMethod> arrayList = new ArrayList();
        for (int i = 0; i < idvOptionsData.getMethods().length; i++) {
            IdvMethod idvMethod = new IdvMethod();
            idvMethod.setId(methods[i].getId());
            idvMethod.setType(methods[i].getType());
            idvMethod.setValue(methods[i].getValue());
            idvMethod.setScheme(methods[i].getScheme());
            if (methods[i].getData() != null) {
                idvMethod.setData(methods[i].getData().toString());
                Log.m285d("ResponseDataBuilder", "IDV data: " + methods[i].getData().toString());
            }
            if ((IdvMethod.IDV_TYPE_APP.equals(idvMethod.getType()) || IdvMethod.IDV_TYPE_CODE_ONLINEBANKING.equals(idvMethod.getType())) && card != null) {
                ProviderResponseData processIdvOptionsDataTA = card.ad().processIdvOptionsDataTA(idvMethod);
                if (processIdvOptionsDataTA == null || processIdvOptionsDataTA.getErrorCode() != 0 || processIdvOptionsDataTA.cg() == null) {
                    Log.m287i("ResponseDataBuilder", " PayProvider error occurred while processing App2App idv data");
                } else {
                    idvMethod.setExtra(processIdvOptionsDataTA.cg());
                }
            }
            arrayList.add(idvMethod);
        }
        return arrayList;
    }

    public static Card m633a(Context context, TokenRecord tokenRecord) {
        if (tokenRecord == null || tokenRecord.getCardBrand() == null) {
            Log.m286e("ResponseDataBuilder", "record or card brand is null");
            return null;
        }
        Token token = new Token();
        if (tokenRecord.getTokenStatus() != null) {
            token.setTokenStatus(tokenRecord.getTokenStatus());
        }
        if (tokenRecord.fy() != null) {
            token.m662H(tokenRecord.fy());
        }
        if (tokenRecord.getTrTokenId() != null) {
            token.setTokenId(tokenRecord.getTrTokenId());
            if (tokenRecord.getTokenRefId() != null) {
                ProviderTokenKey providerTokenKey = new ProviderTokenKey(tokenRecord.getTokenRefId());
                providerTokenKey.setTrTokenId(tokenRecord.getTrTokenId());
                token.m663c(providerTokenKey);
                Log.m285d("ResponseDataBuilder", "providerKeys is set: " + providerTokenKey);
            }
        } else {
            Log.m286e("ResponseDataBuilder", "No Token Id for this Card");
        }
        try {
            Card card = new Card(context, tokenRecord.getCardBrand(), tokenRecord.getCardType(), null, tokenRecord.ab(), token);
            card.setEnrollmentId(tokenRecord.getEnrollmentId());
            if (tokenRecord.fA() != 0) {
                CardMetaData cardMetaData = new CardMetaData();
                cardMetaData.m579b(tokenRecord.fA());
                card.m575a(cardMetaData);
            }
            card.m577j(tokenRecord.ab());
            return card;
        } catch (TAException e) {
            throw e;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static TokenRecord m634a(TokenRecord tokenRecord, TokenResponseData tokenResponseData, ProviderTokenKey providerTokenKey) {
        if (!(tokenRecord == null || tokenResponseData == null)) {
            if (providerTokenKey != null) {
                tokenRecord.setTokenRefId(providerTokenKey.cn());
            }
            if (tokenResponseData.getStatus() != null) {
                tokenRecord.setTokenStatus(ResponseDataBuilder.m635a(tokenResponseData));
                tokenRecord.m1251H(tokenResponseData.getStatus().getReason());
            }
            if (tokenResponseData.getTransaction() != null) {
                tokenRecord.bv(tokenResponseData.getTransaction().getTransactionUrl());
            }
            if (!(tokenResponseData.getCard() == null || tokenResponseData.getCard().getType() == null)) {
                tokenRecord.setCardType(tokenResponseData.getCard().getType());
            }
            if (tokenResponseData.getId() != null) {
                tokenRecord.setTrTokenId(tokenResponseData.getId());
            }
            tokenRecord.m1255j(ResponseDataBuilder.m645c(tokenResponseData));
        }
        return tokenRecord;
    }

    public static String m628F(String str) {
        if (str == null) {
            return null;
        }
        if (TokenStatus.PENDING_ENROLLED.equals(str) || TokenStatus.PENDING_PROVISION.equals(str)) {
            return Card.STATUS_PENDING;
        }
        return str;
    }

    public static void m637a(Context context, TokenMetaData tokenMetaData, Card card) {
        int i = 2;
        if (card == null || tokenMetaData == null || tokenMetaData.getCardPresentationMode() != 0) {
            Log.m286e("ResponseDataBuilder", "Card OR TokenMetaData is NULL");
        } else if (card.ac() == null || card.ac().aQ() == null) {
            Log.m286e("ResponseDataBuilder", "Card Token OR Token Reference Id is NULL");
            tokenMetaData.setCardPresentationMode(0);
        } else {
            if (!(Utils.ao(context) && card.ad().isPayAllowedForPresentationMode(2))) {
                i = 0;
            }
            if (card.ad().isPayAllowedForPresentationMode(1)) {
                i |= 1;
            }
            if (card.ad().isPayAllowedForPresentationMode(4)) {
                i |= 4;
            }
            if (card.ad().isPayAllowedForPresentationMode(8)) {
                i |= 8;
            }
            tokenMetaData.setCardPresentationMode(i);
        }
    }

    public static Bundle m643b(TokenResponseData tokenResponseData) {
        if (tokenResponseData == null) {
            Log.m287i("ResponseDataBuilder", "Provision Response is null.");
            return null;
        } else if (tokenResponseData.getCard() == null || tokenResponseData.getCard().getReference() == null) {
            return null;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("cardRefId", tokenResponseData.getCard().getReference());
            return bundle;
        }
    }

    private static void m638a(EnrollCardResult enrollCardResult) {
        if (enrollCardResult != null && enrollCardResult.getTnC() != null) {
            for (TnC tnC : enrollCardResult.getTnC()) {
                if (!(tnC == null || tnC.getFd() == null)) {
                    try {
                        tnC.getFd().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void m639a(TokenMetaData tokenMetaData) {
        if (tokenMetaData != null && tokenMetaData.getCardArts() != null) {
            for (CardArts cardArts : tokenMetaData.getCardArts()) {
                if (!(cardArts == null || cardArts.getFd() == null)) {
                    try {
                        cardArts.getFd().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (tokenMetaData.getCardIssuer() != null) {
                if (!(tokenMetaData.getCardIssuer().getIssuerAppDetails() == null || tokenMetaData.getCardIssuer().getIssuerAppDetails().getCardArt() == null || tokenMetaData.getCardIssuer().getIssuerAppDetails().getCardArt().getFd() == null)) {
                    try {
                        tokenMetaData.getCardIssuer().getIssuerAppDetails().getCardArt().getFd().close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                for (TnC tnC : tokenMetaData.getCardIssuer().getTnC()) {
                    if (!(tnC == null || tnC.getFd() == null)) {
                        try {
                            tnC.getFd().close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static int m645c(TokenResponseData tokenResponseData) {
        int i = 0;
        if (tokenResponseData != null) {
            String[] presentationModes = tokenResponseData.getPresentationModes();
            if (presentationModes != null && presentationModes.length > 0) {
                for (String str : presentationModes) {
                    if (str.equals(EnrollCardInfo.CARD_PRESENTATION_MODE_MST)) {
                        i |= 2;
                    } else if (str.equals(EnrollCardInfo.CARD_PRESENTATION_MODE_NFC)) {
                        i |= 1;
                    } else if (str.equals(EnrollCardInfo.CARD_PRESENTATION_MODE_ECM)) {
                        i |= 4;
                    } else if (str.equals(IdvMethod.IDV_TYPE_APP)) {
                        i |= 8;
                    }
                }
            }
        } else {
            Log.m286e("ResponseDataBuilder", "TokenResponseData is NULL");
        }
        return i;
    }
}
