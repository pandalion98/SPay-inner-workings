package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.mastercard.mobile_api.payment.cld.CLD;
import com.mastercard.mobile_api.payment.cld.CardSide;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetResponse;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GenerateACApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetDataApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutData80Apdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIDDTag;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.BERTLV;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.payment.data.c */
public class DiscoverDataTags {
    public static final ByteBuffer uA;
    public static final ByteBuffer uB;
    public static final ByteBuffer uC;
    public static final ByteBuffer uD;
    public static final ByteBuffer uE;
    public static final ByteBuffer uF;
    public static final ByteBuffer uG;
    public static final ByteBuffer uH;
    public static final ByteBuffer uI;
    public static final ByteBuffer uJ;
    public static final ByteBuffer uK;
    public static final ByteBuffer uL;
    public static final ByteBuffer uM;
    public static final ByteBuffer uN;
    public static final ByteBuffer uO;
    public static final ByteBuffer uP;
    public static final ByteBuffer uQ;
    public static final ByteBuffer uR;
    public static final ByteBuffer uS;
    public static final ByteBuffer uT;
    public static final ByteBuffer uU;
    public static final ByteBuffer uV;
    public static final ByteBuffer uW;
    public static final ByteBuffer uX;
    public static final ByteBuffer uY;
    public static final ByteBuffer uZ;
    public static final ByteBuffer ux;
    public static final ByteBuffer uy;
    public static final ByteBuffer uz;
    public static final ByteBuffer vA;
    public static final ByteBuffer vB;
    public static final ByteBuffer vC;
    public static final ByteBuffer vD;
    public static final ByteBuffer vE;
    public static final ByteBuffer vF;
    public static final ByteBuffer vG;
    public static final ByteBuffer vH;
    public static final ByteBuffer vI;
    public static final ByteBuffer vJ;
    public static final ByteBuffer vK;
    public static final ByteBuffer vL;
    public static final ByteBuffer vM;
    public static final ByteBuffer vN;
    public static final ByteBuffer vO;
    public static final ByteBuffer vP;
    public static final ByteBuffer vQ;
    public static final ByteBuffer vR;
    public static final ByteBuffer vS;
    public static final ByteBuffer vT;
    public static final ByteBuffer vU;
    public static final ByteBuffer vV;
    public static final ByteBuffer vW;
    public static final ByteBuffer vX;
    private static HashMap<Integer, ByteBuffer> vY;
    private static List<Integer> vZ;
    public static final ByteBuffer va;
    public static final ByteBuffer vb;
    public static final ByteBuffer vc;
    public static final ByteBuffer vd;
    public static final ByteBuffer ve;
    public static final ByteBuffer vf;
    public static final ByteBuffer vg;
    public static final ByteBuffer vh;
    public static final ByteBuffer vj;
    public static final ByteBuffer vk;
    public static final ByteBuffer vl;
    public static final ByteBuffer vm;
    public static final ByteBuffer vn;
    public static final ByteBuffer vo;
    public static final ByteBuffer vp;
    public static final ByteBuffer vq;
    public static final ByteBuffer vr;
    public static final ByteBuffer vs;
    public static final ByteBuffer vt;
    public static final ByteBuffer vu;
    public static final ByteBuffer vv;
    public static final ByteBuffer vw;
    public static final ByteBuffer vx;
    public static final ByteBuffer vy;
    public static final ByteBuffer vz;

    static {
        ux = new ByteBuffer(new byte[]{EMVSetStatusApdu.RESET_LOWEST_PRIORITY});
        uy = new ByteBuffer(new byte[]{(byte) -108});
        uz = new ByteBuffer(new byte[]{EMVGetResponse.INS});
        uA = new ByteBuffer(new byte[]{(byte) -62});
        uB = new ByteBuffer(new byte[]{(byte) -34});
        uC = new ByteBuffer(new byte[]{(byte) -97, (byte) 113});
        uD = new ByteBuffer(new byte[]{(byte) -97, (byte) 108});
        uE = new ByteBuffer(new byte[]{(byte) -61});
        uF = new ByteBuffer(new byte[]{(byte) -60});
        uG = new ByteBuffer(new byte[]{(byte) -33, (byte) 48});
        uH = new ByteBuffer(new byte[]{(byte) -33, (byte) 49});
        uI = new ByteBuffer(new byte[]{(byte) -33, (byte) 50});
        uJ = new ByteBuffer(new byte[]{(byte) -33, ApplicationInfoManager.TERM_XP3});
        uK = new ByteBuffer(new byte[]{(byte) -33, (byte) 52});
        uL = new ByteBuffer(new byte[]{GetTemplateApdu.INS});
        uM = new ByteBuffer(new byte[]{(byte) -39});
        uN = new ByteBuffer(new byte[]{PutData80Apdu.INS});
        uO = new ByteBuffer(new byte[]{(byte) -37});
        uP = new ByteBuffer(new byte[]{(byte) -43});
        uQ = new ByteBuffer(new byte[]{(byte) -42});
        uR = new ByteBuffer(new byte[]{(byte) -41});
        uS = new ByteBuffer(new byte[]{(byte) -40});
        uT = new ByteBuffer(new byte[]{(byte) -49});
        uU = new ByteBuffer(new byte[]{(byte) -33, EMVGetStatusApdu.P1});
        uV = new ByteBuffer(new byte[]{(byte) -33, (byte) 67});
        uW = new ByteBuffer(new byte[]{(byte) -33, (byte) 68});
        uX = new ByteBuffer(new byte[]{(byte) -33, (byte) 65});
        uY = new ByteBuffer(new byte[]{(byte) -33, MCFCITemplate.TAG_FCI_ISSUER_IIN});
        uZ = new ByteBuffer(new byte[]{(byte) -50});
        va = new ByteBuffer(new byte[]{(byte) -51});
        vb = new ByteBuffer(new byte[]{(byte) -53});
        vc = new ByteBuffer(new byte[]{(byte) -52});
        vd = new ByteBuffer(new byte[]{(byte) -56});
        ve = new ByteBuffer(new byte[]{(byte) -55});
        vf = new ByteBuffer(new byte[]{GetDataApdu.INS});
        vg = new ByteBuffer(new byte[]{(byte) -48});
        vh = new ByteBuffer(new byte[]{PutTemplateApdu.INS});
        vj = new ByteBuffer(new byte[]{ApplicationInfoManager.AMOUNT_STATUS_LOW});
        vk = new ByteBuffer(new byte[]{(byte) -97, (byte) 7});
        vl = new ByteBuffer(new byte[]{(byte) -97, CardSide.BACKGROUND_TAG});
        vm = new ByteBuffer(new byte[]{(byte) -97, CardSide.PIN_TEXT_TAG});
        vn = new ByteBuffer(new byte[]{(byte) -97, (byte) 54});
        vo = new ByteBuffer(new byte[]{(byte) -97, GetTemplateApdu.TAG_DF_NAME_4F});
        vp = new ByteBuffer(new byte[]{(byte) -97, ApplicationInfoManager.TERMINAL_MODE_NONE});
        vq = new ByteBuffer(new byte[]{PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, GetTemplateApdu.TAG_APPLICATION_LABEL_50});
        vr = new ByteBuffer(new byte[]{PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, ApplicationInfoManager.MOB_CVM_TYP_DEV_PASSWORD});
        vs = new ByteBuffer(new byte[]{(byte) -33, Tnaf.POW_2_WIDTH});
        vt = new ByteBuffer(new byte[]{(byte) -33, CLD.VERSION_TAG});
        vu = new ByteBuffer(new byte[]{(byte) -33, CLD.FORM_FACTOR_TAG});
        vv = new ByteBuffer(new byte[]{(byte) -97, (byte) 38});
        vw = new ByteBuffer(new byte[]{(byte) -97, (byte) 75});
        vx = new ByteBuffer(new byte[]{(byte) -97, Tnaf.POW_2_WIDTH});
        vy = new ByteBuffer(new byte[]{(byte) -97, (byte) 39});
        vz = new ByteBuffer(new byte[]{(byte) 87});
        vA = new ByteBuffer(new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 40});
        vB = new ByteBuffer(new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 52});
        vC = new ByteBuffer(new byte[]{(byte) -97, (byte) 2});
        vD = new ByteBuffer(new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, GenerateACApdu.INS});
        vE = new ByteBuffer(new byte[]{ApplicationInfoManager.MOB_CVM_PERFORMED});
        vF = new ByteBuffer(new byte[]{(byte) -97, (byte) 26});
        vG = new ByteBuffer(new byte[]{(byte) -107});
        vH = new ByteBuffer(new byte[]{(byte) -100});
        vI = new ByteBuffer(new byte[]{(byte) -97, (byte) 55});
        vJ = new ByteBuffer(new byte[]{(byte) -97, (byte) 3});
        vK = new ByteBuffer(new byte[]{(byte) -97, ApplicationInfoManager.TERMINAL_MODE_CL_EMV});
        vL = new ByteBuffer(new byte[]{(byte) -97, (byte) 102});
        vM = new ByteBuffer(new byte[]{(byte) 11, (byte) 10});
        vN = new ByteBuffer(new byte[]{(byte) -36});
        vO = new ByteBuffer(new byte[]{MCFCITemplate.TAG_FCI_TEMPLATE});
        vP = new ByteBuffer(new byte[]{PinChangeUnblockApdu.CLA});
        vQ = new ByteBuffer(new byte[]{PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG});
        vR = new ByteBuffer(new byte[]{PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG, PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG});
        vS = new ByteBuffer(new byte[]{GetTemplateApdu.TAG_DF_NAME_4F});
        vT = new ByteBuffer(new byte[]{PutTemplateApdu.DIRECTORY_TEMPLATE_TAG});
        vU = new ByteBuffer(new byte[]{(byte) -47});
        vV = new ByteBuffer(new byte[]{(byte) -97, (byte) 93});
        vW = new ByteBuffer(new byte[]{MCFCITemplate.TAG_FILE_CONTROL_INFORMATION, (byte) 37});
        vX = new ByteBuffer(new byte[]{(byte) -97, (byte) 8});
        vY = new HashMap();
        vZ = new ArrayList();
        vZ.add(Integer.valueOf(uL.getInt()));
        vZ.add(Integer.valueOf(uM.getInt()));
        vZ.add(Integer.valueOf(uN.getInt()));
        vZ.add(Integer.valueOf(uO.getInt()));
        vZ.add(Integer.valueOf(uP.getInt()));
        vZ.add(Integer.valueOf(uQ.getInt()));
        vZ.add(Integer.valueOf(uR.getInt()));
        vZ.add(Integer.valueOf(uS.getInt()));
        vZ.add(Integer.valueOf(uG.getInt()));
        vZ.add(Integer.valueOf(uH.getInt()));
        vZ.add(Integer.valueOf(uI.getInt()));
        vZ.add(Integer.valueOf(uJ.getInt()));
        vZ.add(Integer.valueOf(uK.getInt()));
        vZ.add(Integer.valueOf(uU.getInt()));
        vZ.add(Integer.valueOf(uV.getInt()));
        vZ.add(Integer.valueOf(uW.getInt()));
        vZ.add(Integer.valueOf(uX.getInt()));
        vZ.add(Integer.valueOf(uY.getInt()));
        vZ.add(Integer.valueOf(uZ.getInt()));
        vZ.add(Integer.valueOf(va.getInt()));
        vZ.add(Integer.valueOf(vb.getInt()));
        vZ.add(Integer.valueOf(vc.getInt()));
        vZ.add(Integer.valueOf(vd.getInt()));
        vZ.add(Integer.valueOf(ve.getInt()));
        vZ.add(Integer.valueOf(vf.getInt()));
        vZ.add(Integer.valueOf(vm.getInt()));
        vZ.add(Integer.valueOf(vM.getInt()));
    }

    public static void m972a(DiscoverTransactionContext discoverTransactionContext, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        DiscoverPaymentProfile paymentProfile = discoverTransactionContext.ed().getPaymentProfile();
        vY.put(Integer.valueOf(ux.getInt()), paymentProfile.getAip());
        vY.put(Integer.valueOf(uy.getInt()), paymentProfile.getAfl());
        vY.put(Integer.valueOf(uz.getInt()), discoverContactlessPaymentData.getDiscoverApplicationData().getCLApplicationConfigurationOptions());
        vY.put(Integer.valueOf(uB.getInt()), discoverContactlessPaymentData.getCaco());
        Log.m285d("DCSDK_DiscoverDataTags", "CPR to MAP: " + discoverTransactionContext.ed().dI());
        vY.put(Integer.valueOf(uC.getInt()), discoverTransactionContext.ed().dI());
        vY.put(Integer.valueOf(uE.getInt()), discoverContactlessPaymentData.getSecondaryCurrency1());
        vY.put(Integer.valueOf(uF.getInt()), discoverContactlessPaymentData.getSecondaryCurrency2());
        vY.put(Integer.valueOf(uG.getInt()), paymentProfile.getCRM().getCRM_CAC_Switch_Interface());
        vY.put(Integer.valueOf(uH.getInt()), paymentProfile.getCRM().getCRM_CAC_Denial());
        vY.put(Integer.valueOf(uI.getInt()), paymentProfile.getCRM().getCRM_CAC_Online());
        vY.put(Integer.valueOf(uJ.getInt()), paymentProfile.getCRM().getCRM_CAC_Default());
        vY.put(Integer.valueOf(uK.getInt()), paymentProfile.getCRM().getCRM_CAC_Online());
        vY.put(Integer.valueOf(uL.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCVM().getCvmAccumulator(), 6)));
        vY.put(Integer.valueOf(uM.getInt()), ByteBuffer.getFromLong(paymentProfile.getCVM().getCvmCounter(), 1));
        vY.put(Integer.valueOf(uN.getInt()), ByteBuffer.getFromLong(paymentProfile.getCVM().getCVM_Cons_Limit_1()));
        vY.put(Integer.valueOf(uO.getInt()), ByteBuffer.getFromLong(paymentProfile.getCVM().getCVM_Cons_Limit_2()));
        vY.put(Integer.valueOf(uP.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCVM().getCVM_Cum_Limit_1(), 6)));
        vY.put(Integer.valueOf(uQ.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCVM().getCVM_Cum_Limit_2(), 6)));
        vY.put(Integer.valueOf(uR.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCVM().getCVM_Sta_Limit_1(), 6)));
        vY.put(Integer.valueOf(uS.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCVM().getCVM_Sta_Limit_2(), 6)));
        vY.put(Integer.valueOf(uT.getInt()), discoverTransactionContext.ed().getPth());
        vY.put(Integer.valueOf(uU.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCl().getClAccumulator(), 6)));
        vY.put(Integer.valueOf(uV.getInt()), ByteBuffer.fromHexString(Long.toString(paymentProfile.getCl().getClCounter())));
        vY.put(Integer.valueOf(uW.getInt()), ByteBuffer.getFromLong(paymentProfile.getCl().getCL_Cons_Limit()));
        vY.put(Integer.valueOf(uX.getInt()), ByteBuffer.fromHexString(Long.toString(paymentProfile.getCl().getCL_Cum_Limit())));
        vY.put(Integer.valueOf(uY.getInt()), ByteBuffer.fromHexString(Long.toString(paymentProfile.getCl().getCL_STA_Limit())));
        vY.put(Integer.valueOf(uZ.getInt()), ByteBuffer.fromHexString(Long.toString(paymentProfile.getCRM().getCrmAccumulator())));
        vY.put(Integer.valueOf(va.getInt()), ByteBuffer.fromHexString(Long.toString(paymentProfile.getCRM().getCrmCounter())));
        vY.put(Integer.valueOf(vb.getInt()), ByteBuffer.fromHexString(Long.toString(paymentProfile.getCRM().getLCOL())));
        vY.put(Integer.valueOf(vc.getInt()), ByteBuffer.fromHexString(Long.toString(paymentProfile.getCRM().getUCOL())));
        vY.put(Integer.valueOf(vd.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCRM().getLCOA(), 6)));
        vY.put(Integer.valueOf(ve.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCRM().getUCOA(), 6)));
        vY.put(Integer.valueOf(vf.getInt()), ByteBuffer.fromHexString(DiscoverDataTags.m976b(paymentProfile.getCRM().getSTA(), 6)));
        vY.put(Integer.valueOf(vh.getInt()), discoverContactlessPaymentData.getCountryCode());
        vY.put(Integer.valueOf(vj.getInt()), discoverContactlessPaymentData.getCurrencyCodeCode());
        vY.put(Integer.valueOf(vk.getInt()), paymentProfile.getApplicationUsageControl());
        vY.put(Integer.valueOf(vn.getInt()), discoverTransactionContext.dT());
        vY.put(Integer.valueOf(vq.getInt()), discoverTransactionContext.ed().dQ());
        vY.put(Integer.valueOf(vr.getInt()), discoverTransactionContext.ed().dR());
        vY.put(Integer.valueOf(vg.getInt()), discoverContactlessPaymentData.getIssuerApplicationData().getIADOL());
        vY.put(Integer.valueOf(vt.getInt()), discoverContactlessPaymentData.getPDOLProfileCheckTable());
        vY.put(Integer.valueOf(vp.getInt()), discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationState());
        vY.put(Integer.valueOf(uA.getInt()), discoverContactlessPaymentData.getIssuerApplicationData().getIssuerLifeCycleData());
        List<DiscoverIDDTag> iDDTags = discoverContactlessPaymentData.getIssuerApplicationData().getIDDTags();
        if (iDDTags != null) {
            for (DiscoverIDDTag discoverIDDTag : iDDTags) {
                if (!(discoverIDDTag == null || discoverIDDTag.getTag() == null || discoverIDDTag.getData() == null)) {
                    Log.m285d("DCSDK_DiscoverDataTags", "put data tag: " + discoverIDDTag.getTag().toHexString() + ", get data " + discoverIDDTag.getData().toHexString());
                    vY.put(Integer.valueOf(discoverIDDTag.getTag().getInt()), discoverIDDTag.getData());
                }
            }
        }
        if (!(discoverTransactionContext.ea() == null || discoverTransactionContext.ea().dE() == null)) {
            vY.put(Integer.valueOf(vN.getInt()), discoverTransactionContext.ea().dE().dG());
        }
        if (discoverTransactionContext.ed().dP() != null) {
            vY.put(Integer.valueOf(vM.getInt()), discoverTransactionContext.ed().dP().dW());
        }
    }

    public static boolean m979w(ByteBuffer byteBuffer) {
        Log.m285d("DCSDK_DiscoverDataTags", "Map size: " + vY.size());
        if (byteBuffer == null || byteBuffer.getSize() == 0) {
            Log.m286e("DCSDK_DiscoverDataTags", "checkDataTag, tag is null.");
            return false;
        } else if ((byteBuffer.getByte(0) & GF2Field.MASK) != 223) {
            return vY.containsKey(Integer.valueOf(byteBuffer.getInt()));
        } else {
            Log.m287i("DCSDK_DiscoverDataTags", "checkDataTag, tag is IDD tag.");
            return true;
        }
    }

    public static ByteBuffer m969a(ByteBuffer byteBuffer, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        Log.m287i("DCSDK_DiscoverDataTags", "getProfile, get profile...");
        if (byteBuffer == null || byteBuffer.getSize() != 2) {
            Log.m286e("DCSDK_DiscoverDataTags", "getProfile, wrong profile tag requested.");
            return null;
        }
        Log.m287i("DCSDK_DiscoverDataTags", "getProfile, profile tag requested: " + byteBuffer.toHexString());
        if (DiscoverDataTags.m967A(byteBuffer)) {
            int i = byteBuffer.getInt() & GF2Field.MASK;
            Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, profile requested: " + (byteBuffer.getInt() & GF2Field.MASK));
            if (i == 80 || (i >= 33 && i <= 47)) {
                int i2 = i & 15;
                HashMap paymentProfiles = discoverContactlessPaymentData.getPaymentProfiles();
                if (paymentProfiles == null || paymentProfiles.isEmpty()) {
                    Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, payment profile map is empty");
                    return null;
                }
                DiscoverPaymentProfile discoverPaymentProfile = (DiscoverPaymentProfile) paymentProfiles.get(Integer.valueOf(i2));
                if (discoverPaymentProfile == null) {
                    Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, profile not found, id " + i2);
                    return null;
                }
                Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, compose profile");
                return DiscoverDataTags.m975b(discoverPaymentProfile);
            } else if (i == 81) {
                Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, ZIP profile requested");
                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, ZIP afl:" + discoverContactlessPaymentData.getZipAfl());
                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, ZIP aip:" + discoverContactlessPaymentData.getZipAip());
                if (discoverContactlessPaymentData.getZipAfl() == null || discoverContactlessPaymentData.getZipAip() == null) {
                    return null;
                }
                ByteBuffer c = BERTLV.m1004c(ux, discoverContactlessPaymentData.getZipAip());
                c.append(BERTLV.m1004c(uy, discoverContactlessPaymentData.getZipAfl()));
                return c;
            } else {
                Log.m286e("DCSDK_DiscoverDataTags", "getProfile, unknown profile id " + i);
                return null;
            }
        }
        Log.m286e("DCSDK_DiscoverDataTags", "getProfile, not profile tag");
        return null;
    }

    public static ByteBuffer m980x(ByteBuffer byteBuffer) {
        Log.m287i("DCSDK_DiscoverDataTags", "Tag data: " + vY.get(Integer.valueOf(byteBuffer.getInt())));
        return (ByteBuffer) vY.get(Integer.valueOf(byteBuffer.getInt()));
    }

    public static DiscoverIDDTag m974b(ByteBuffer byteBuffer, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        if (discoverContactlessPaymentData == null) {
            Log.m286e("DCSDK_DiscoverDataTags", "getIDDTag, paymentProfileData is null.");
            return null;
        } else if (DiscoverDataTags.m968B(byteBuffer)) {
            List<DiscoverIDDTag> iDDTags = discoverContactlessPaymentData.getIssuerApplicationData().getIDDTags();
            if (iDDTags == null) {
                Log.m286e("DCSDK_DiscoverDataTags", "getIDDTag, no valid IDD tag found.");
                return null;
            }
            for (DiscoverIDDTag discoverIDDTag : iDDTags) {
                if (discoverIDDTag != null && discoverIDDTag.getTag().getInt() == byteBuffer.getInt()) {
                    return discoverIDDTag;
                }
            }
            Log.m286e("DCSDK_DiscoverDataTags", "getIDDTag, no valid IDD tag found, tag " + byteBuffer.toHexString());
            return null;
        } else {
            Log.m286e("DCSDK_DiscoverDataTags", "getIDDTag, wrong idd tag.");
            return null;
        }
    }

    public static boolean m981y(ByteBuffer byteBuffer) {
        return vZ.contains(Integer.valueOf(byteBuffer.getInt()));
    }

    public static boolean m982z(ByteBuffer byteBuffer) {
        return byteBuffer.getByte(0) == -33 && byteBuffer.getByte(1) > null && byteBuffer.getByte(1) <= 10;
    }

    public static ByteBuffer m975b(DiscoverPaymentProfile discoverPaymentProfile) {
        ByteBuffer c = BERTLV.m1004c(ux, discoverPaymentProfile.getAip());
        c.append(BERTLV.m1004c(uy, discoverPaymentProfile.getAfl()));
        c.append(BERTLV.m1004c(vk, discoverPaymentProfile.getApplicationUsageControl()));
        c.append(BERTLV.m1004c(uC, discoverPaymentProfile.getCpr()));
        c.append(BERTLV.m1004c(uG, discoverPaymentProfile.getCRM().getCRM_CAC_Switch_Interface()));
        c.append(BERTLV.m1004c(uH, discoverPaymentProfile.getCRM().getCRM_CAC_Denial()));
        c.append(BERTLV.m1004c(uI, discoverPaymentProfile.getCRM().getCRM_CAC_Online()));
        c.append(BERTLV.m1004c(uJ, discoverPaymentProfile.getCRM().getCRM_CAC_Default()));
        c.append(BERTLV.m1004c(va, ByteBuffer.fromHexString(Long.toString(discoverPaymentProfile.getCRM().getCrmCounter()))));
        c.append(BERTLV.m1004c(uZ, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getCrmAccumulator(), 6))));
        c.append(BERTLV.m1004c(vb, ByteBuffer.getFromLong(discoverPaymentProfile.getCRM().getLCOL())));
        c.append(BERTLV.m1004c(vc, ByteBuffer.getFromLong(discoverPaymentProfile.getCRM().getUCOL())));
        c.append(BERTLV.m1004c(vd, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getLCOA(), 6))));
        c.append(BERTLV.m1004c(ve, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getUCOA(), 6))));
        c.append(BERTLV.m1004c(vf, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getSTA(), 6))));
        c.append(BERTLV.m1004c(uK, discoverPaymentProfile.getCRM().getCRM_CAC_Online()));
        c.append(BERTLV.m1004c(uL, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCVM().getCvmAccumulator(), 6))));
        c.append(BERTLV.m1004c(uM, ByteBuffer.fromHexString(Long.toString(discoverPaymentProfile.getCVM().getCvmCounter()))));
        c.append(BERTLV.m1004c(uN, ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCVM_Cons_Limit_1())));
        c.append(BERTLV.m1004c(uO, ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCVM_Cons_Limit_2())));
        c.append(BERTLV.m1004c(uP, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCVM().getCVM_Cum_Limit_1(), 6))));
        c.append(BERTLV.m1004c(uQ, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCVM().getCVM_Cum_Limit_2(), 6))));
        c.append(BERTLV.m1004c(uR, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCVM().getCVM_Sta_Limit_1(), 6))));
        c.append(BERTLV.m1004c(uS, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCVM().getCVM_Sta_Limit_2(), 6))));
        c.append(BERTLV.m1004c(uU, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCl().getClAccumulator(), 6))));
        c.append(BERTLV.m1004c(uV, ByteBuffer.fromHexString(Long.toString(discoverPaymentProfile.getCl().getClCounter()))));
        c.append(BERTLV.m1004c(uW, ByteBuffer.getFromLong(discoverPaymentProfile.getCl().getCL_Cons_Limit())));
        c.append(BERTLV.m1004c(uX, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCl().getCL_Cum_Limit(), 6))));
        c.append(BERTLV.m1004c(uY, ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCl().getCL_STA_Limit(), 6))));
        c.append(BERTLV.m1004c(uD, discoverPaymentProfile.getCtq()));
        return c;
    }

    public static void m977b(DiscoverTransactionContext discoverTransactionContext, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        ByteBuffer c = BERTLV.m1004c(ux, discoverContactlessPaymentData.getZipAip());
        c.append(BERTLV.m1004c(uy, discoverContactlessPaymentData.getZipAfl()));
        discoverTransactionContext.ed().m956t(BERTLV.m1004c(vr, c));
    }

    public static void m973a(ByteBuffer byteBuffer, DiscoverPaymentProfile discoverPaymentProfile, DiscoverTransactionContext discoverTransactionContext, List<DiscoverIDDTag> list, ByteBuffer byteBuffer2) {
        if (byteBuffer2 == null) {
            Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IAD is null.");
            return;
        }
        Object obj = byteBuffer2.getSize() <= 10 ? 1 : null;
        if (discoverPaymentProfile == null) {
            Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, payment profile is null.");
        } else if (byteBuffer == null) {
            Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IADOL object is null.");
        } else {
            Map hashMap = new HashMap();
            try {
                BERTLV.m1002a(byteBuffer, hashMap);
                if (hashMap.isEmpty()) {
                    Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, no tags found in IADOL.");
                    return;
                }
                int i = 10;
                for (Integer num : hashMap.keySet()) {
                    Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, tag: " + (num != null ? num : null));
                    if (num == null) {
                        Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, tag value is null, continue...");
                    } else {
                        int i2;
                        ByteBuffer a = DiscoverDataTags.m970a(num, ((Integer) hashMap.get(num)).intValue(), discoverPaymentProfile, discoverTransactionContext, (List) list);
                        ByteBuffer byteBuffer3 = new ByteBuffer(0);
                        if (obj != null) {
                            Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, append IDD tag, tag " + num);
                            byte[] bArr;
                            if (a == null) {
                                Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, cannot find tag value, tag " + num);
                                bArr = new byte[((Integer) hashMap.get(num)).intValue()];
                                Arrays.fill(bArr, (byte) 0);
                                byteBuffer3.append(new ByteBuffer(bArr));
                                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, iad " + byteBuffer2.toHexString());
                            } else if (a.getSize() == ((Integer) hashMap.get(num)).intValue()) {
                                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, append tag " + num + ", value " + a.toHexString());
                                byteBuffer3.append(a);
                            } else if (a.getSize() < ((Integer) hashMap.get(num)).intValue()) {
                                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, data length is less than required length.");
                                bArr = new byte[(((Integer) hashMap.get(num)).intValue() - a.getSize())];
                                Arrays.fill(bArr, (byte) 0);
                                byteBuffer3.append(new ByteBuffer(bArr));
                                byteBuffer3.append(a);
                            } else {
                                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, data length is greater than required length.");
                                bArr = new byte[((Integer) hashMap.get(num)).intValue()];
                                Arrays.fill(bArr, (byte) 0);
                                byteBuffer3.append(new ByteBuffer(bArr));
                                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, iad " + byteBuffer2.toHexString());
                            }
                            if (byteBuffer2.getSize() + byteBuffer3.getSize() <= 32) {
                                byteBuffer2.append(byteBuffer3);
                                i2 = i;
                            } else {
                                Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, iad max length exceeded, " + (32 - byteBuffer2.getSize()));
                                if (byteBuffer3.getSize() >= 32 - byteBuffer2.getSize()) {
                                    Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, last tag: " + byteBuffer3.copyBytes(0, 32 - byteBuffer2.getSize()).toHexString());
                                    byteBuffer2.append(byteBuffer3.copyBytes(0, 32 - byteBuffer2.getSize()));
                                    return;
                                }
                                return;
                            }
                        }
                        Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, data: " + (a != null ? a.toHexString() : null));
                        if (a == null) {
                            Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, tag " + num + " not found in the profile.");
                            i += ((Integer) hashMap.get(num)).intValue();
                        } else {
                            int i3;
                            int intValue = ((Integer) hashMap.get(num)).intValue();
                            if (a.getSize() <= intValue) {
                                intValue = a.getSize();
                                i3 = intValue;
                                intValue = i + (intValue - a.getSize());
                            } else {
                                i3 = intValue;
                                intValue = i;
                            }
                            if (intValue + i3 <= byteBuffer2.getSize()) {
                                i2 = intValue;
                                intValue = 0;
                                while (intValue < i3) {
                                    byteBuffer2.setByte(i2, a.getByte(intValue));
                                    intValue++;
                                    i2++;
                                }
                            } else if (intValue + i3 <= 32) {
                                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, append additional tag tag " + num);
                                byteBuffer2.append(a);
                                i2 = intValue + 1;
                            } else {
                                Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, cannot place full tag data into the IAD, max length tag " + num);
                                if (intValue <= 32 && byteBuffer2.getSize() < 32 && a.getSize() <= 32 - intValue) {
                                    Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, append first " + (32 - intValue) + " bytes");
                                    byteBuffer2.append(a.copyBytes(0, 32 - intValue));
                                    return;
                                }
                                return;
                            }
                        }
                        i = i2;
                    }
                }
            } catch (ParseException e) {
                Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, ParseException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static ByteBuffer m970a(Integer num, int i, DiscoverPaymentProfile discoverPaymentProfile, DiscoverTransactionContext discoverTransactionContext, List<DiscoverIDDTag> list) {
        Log.m287i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IDD tag: " + num + ", length: " + i);
        if (num.intValue() == vd.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getLCOA(), i));
        }
        if (num.intValue() == ve.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getUCOA(), i));
        }
        if (num.intValue() == vf.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getSTA(), i));
        }
        if (num.intValue() == vb.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getLCOL(), i));
        }
        if (num.intValue() == vc.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getUCOL(), i));
        }
        if (num.intValue() == va.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getCrmCounter(), i));
        }
        if (num.intValue() == uZ.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCRM().getCrmAccumulator(), i));
        }
        if (num.intValue() == uL.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCVM().getCvmAccumulator(), i));
        }
        if (num.intValue() == uM.getInt()) {
            Log.m285d("DCSDK_DiscoverDataTags", "profile.getCVM().getCvmCounter(): " + discoverPaymentProfile.getCVM().getCvmCounter());
            return ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCvmCounter(), i);
        } else if (num.intValue() == uU.getInt()) {
            return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCl().getClAccumulator(), i));
        } else {
            if (num.intValue() == uV.getInt()) {
                return ByteBuffer.fromHexString(DiscoverDataTags.m976b(discoverPaymentProfile.getCl().getClCounter(), i));
            }
            if (num.intValue() == vU.getInt() || num.intValue() == vV.getInt()) {
                return null;
            }
            if (num.intValue() == vN.getInt()) {
                if (discoverTransactionContext == null || discoverTransactionContext.ea() == null || discoverTransactionContext.ea().dE() == null) {
                    return null;
                }
                return ByteBuffer.fromHexString(DiscoverDataTags.m971a(discoverTransactionContext.ea().dE().dG(), i));
            } else if (((num.intValue() >> 8) & GF2Field.MASK) != 223) {
                return null;
            } else {
                if (list == null || list.isEmpty()) {
                    Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IDD tags are empty.");
                    return null;
                }
                int intValue = (num.intValue() & GF2Field.MASK) - 1;
                Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, iddTagNum: " + intValue);
                if (intValue >= list.size()) {
                    Log.m286e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IDD tag requested " + intValue + "," + "IDD tags in the profile " + list.size());
                    return null;
                } else if (list.get(intValue) != null) {
                    return ((DiscoverIDDTag) list.get(intValue)).getData();
                } else {
                    return null;
                }
            }
        }
    }

    private static String m971a(ByteBuffer byteBuffer, int i) {
        int i2 = 0;
        if (byteBuffer == null) {
            return new ByteBuffer(i).toHexString();
        }
        if (byteBuffer.getSize() < i) {
            ByteBuffer byteBuffer2 = new ByteBuffer(i);
            while (i2 < byteBuffer.getSize()) {
                byteBuffer2.setByte((i - byteBuffer.getSize()) + i2, byteBuffer.getByte(i2));
                i2++;
            }
            return byteBuffer2.toHexString();
        } else if (byteBuffer.getSize() > i) {
            return byteBuffer.copyBytes(0, i).toHexString();
        } else {
            return byteBuffer.toHexString();
        }
    }

    private static String m976b(long j, int i) {
        StringBuffer stringBuffer;
        String l = Long.toString(j);
        Log.m285d("DCSDK_DiscoverDataTags", "getTagValue, value " + l + ", length " + i);
        int length = l.length() % 2 == 0 ? l.length() / 2 : (l.length() / 2) + 1;
        Log.m285d("DCSDK_DiscoverDataTags", "getTagValue, tagLength " + length);
        StringBuffer stringBuffer2 = new StringBuffer(l);
        int i2;
        if (length < i) {
            for (i2 = 0; i2 < i - length; i2++) {
                stringBuffer2.insert(0, TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE).insert(0, TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE);
            }
            if (stringBuffer2.length() % 2 != 0) {
                stringBuffer2.insert(0, TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE);
                stringBuffer = stringBuffer2;
            }
            stringBuffer = stringBuffer2;
        } else if (length > i) {
            i2 = 0;
            while (i2 < length - i) {
                i2++;
                stringBuffer2 = stringBuffer2.delete(0, 1);
            }
            stringBuffer = stringBuffer2;
        } else {
            if (stringBuffer2.length() % 2 != 0) {
                stringBuffer2.insert(0, TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE);
            }
            stringBuffer = stringBuffer2;
        }
        Log.m285d("DCSDK_DiscoverDataTags", "DiscoverDataTags, getTagValue: " + stringBuffer.toString());
        return stringBuffer.toString();
    }

    public static boolean m967A(ByteBuffer byteBuffer) {
        if (byteBuffer == null || byteBuffer.getSize() != 2) {
            return false;
        }
        Log.m287i("DCSDK_DiscoverDataTags", "processApdu, C-APDU get data, profile tag found, tag " + byteBuffer.toHexString());
        if ((byteBuffer.getByte(0) & GF2Field.MASK) == CipherSuite.TLS_DH_anon_WITH_CAMELLIA_128_CBC_SHA256) {
            return true;
        }
        if ((byteBuffer.getByte(0) & GF2Field.MASK) != 223 || (byteBuffer.getByte(1) & GF2Field.MASK) < 33 || (byteBuffer.getByte(1) & GF2Field.MASK) > 47) {
            return false;
        }
        Log.m285d("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, profile tag");
        return true;
    }

    public static boolean m968B(ByteBuffer byteBuffer) {
        if (byteBuffer == null || byteBuffer.getSize() != 2) {
            return false;
        }
        Log.m287i("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, profile tag found, tag " + byteBuffer.toHexString());
        if ((byteBuffer.getByte(0) & GF2Field.MASK) != 223 || (byteBuffer.getByte(1) & GF2Field.MASK) < 1 || (byteBuffer.getByte(1) & GF2Field.MASK) > 10) {
            return false;
        }
        Log.m285d("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, IDD tag");
        return true;
    }

    public static ByteBuffer m978c(ByteBuffer byteBuffer, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        if (byteBuffer == null) {
            Log.m286e("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, requested data tag is null");
            return null;
        }
        int i = byteBuffer.getInt();
        if (i == uz.getInt()) {
            return discoverContactlessPaymentData.getDiscoverApplicationData().getCLApplicationConfigurationOptions();
        }
        if (i == uB.getInt()) {
            return discoverContactlessPaymentData.getCaco();
        }
        if (i == uE.getInt()) {
            return discoverContactlessPaymentData.getSecondaryCurrency1();
        }
        if (i == uF.getInt()) {
            return discoverContactlessPaymentData.getSecondaryCurrency2();
        }
        if (i == vh.getInt()) {
            return discoverContactlessPaymentData.getCountryCode();
        }
        if (i == vj.getInt()) {
            return discoverContactlessPaymentData.getCurrencyCodeCode();
        }
        if (i == vg.getInt()) {
            return discoverContactlessPaymentData.getIssuerApplicationData().getIADOL();
        }
        if (i == vt.getInt()) {
            return discoverContactlessPaymentData.getPDOLProfileCheckTable();
        }
        if (i == vp.getInt()) {
            return discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationState();
        }
        if (i == uA.getInt()) {
            return discoverContactlessPaymentData.getIssuerApplicationData().getIssuerLifeCycleData();
        }
        return null;
    }
}
