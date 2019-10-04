/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCLTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.payment.data.e;
import com.samsung.android.spayfw.payprovider.discover.payment.data.f;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverApplicationData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverContactlessPaymentData;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIDDTag;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIssuerOptions;
import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverPaymentProfile;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class c {
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
        ux = new ByteBuffer(new byte[]{-126});
        uy = new ByteBuffer(new byte[]{-108});
        uz = new ByteBuffer(new byte[]{-64});
        uA = new ByteBuffer(new byte[]{-62});
        uB = new ByteBuffer(new byte[]{-34});
        uC = new ByteBuffer(new byte[]{-97, 113});
        uD = new ByteBuffer(new byte[]{-97, 108});
        uE = new ByteBuffer(new byte[]{-61});
        uF = new ByteBuffer(new byte[]{-60});
        uG = new ByteBuffer(new byte[]{-33, 48});
        uH = new ByteBuffer(new byte[]{-33, 49});
        uI = new ByteBuffer(new byte[]{-33, 50});
        uJ = new ByteBuffer(new byte[]{-33, 51});
        uK = new ByteBuffer(new byte[]{-33, 52});
        uL = new ByteBuffer(new byte[]{-44});
        uM = new ByteBuffer(new byte[]{-39});
        uN = new ByteBuffer(new byte[]{-38});
        uO = new ByteBuffer(new byte[]{-37});
        uP = new ByteBuffer(new byte[]{-43});
        uQ = new ByteBuffer(new byte[]{-42});
        uR = new ByteBuffer(new byte[]{-41});
        uS = new ByteBuffer(new byte[]{-40});
        uT = new ByteBuffer(new byte[]{-49});
        uU = new ByteBuffer(new byte[]{-33, 64});
        uV = new ByteBuffer(new byte[]{-33, 67});
        uW = new ByteBuffer(new byte[]{-33, 68});
        uX = new ByteBuffer(new byte[]{-33, 65});
        uY = new ByteBuffer(new byte[]{-33, 66});
        uZ = new ByteBuffer(new byte[]{-50});
        va = new ByteBuffer(new byte[]{-51});
        vb = new ByteBuffer(new byte[]{-53});
        vc = new ByteBuffer(new byte[]{-52});
        vd = new ByteBuffer(new byte[]{-56});
        ve = new ByteBuffer(new byte[]{-55});
        vf = new ByteBuffer(new byte[]{-54});
        vg = new ByteBuffer(new byte[]{-48});
        vh = new ByteBuffer(new byte[]{-46});
        vj = new ByteBuffer(new byte[]{-45});
        vk = new ByteBuffer(new byte[]{-97, 7});
        vl = new ByteBuffer(new byte[]{-97, 19});
        vm = new ByteBuffer(new byte[]{-97, 23});
        vn = new ByteBuffer(new byte[]{-97, 54});
        vo = new ByteBuffer(new byte[]{-97, 79});
        vp = new ByteBuffer(new byte[]{-97, 120});
        vq = new ByteBuffer(new byte[]{-65, 80});
        vr = new ByteBuffer(new byte[]{-65, 81});
        vs = new ByteBuffer(new byte[]{-33, 16});
        vt = new ByteBuffer(new byte[]{-33, 17});
        vu = new ByteBuffer(new byte[]{-33, 18});
        vv = new ByteBuffer(new byte[]{-97, 38});
        vw = new ByteBuffer(new byte[]{-97, 75});
        vx = new ByteBuffer(new byte[]{-97, 16});
        vy = new ByteBuffer(new byte[]{-97, 39});
        vz = new ByteBuffer(new byte[]{87});
        vA = new ByteBuffer(new byte[]{95, 40});
        vB = new ByteBuffer(new byte[]{95, 52});
        vC = new ByteBuffer(new byte[]{-97, 2});
        vD = new ByteBuffer(new byte[]{95, 42});
        vE = new ByteBuffer(new byte[]{-102});
        vF = new ByteBuffer(new byte[]{-97, 26});
        vG = new ByteBuffer(new byte[]{-107});
        vH = new ByteBuffer(new byte[]{-100});
        vI = new ByteBuffer(new byte[]{-97, 55});
        vJ = new ByteBuffer(new byte[]{-97, 3});
        vK = new ByteBuffer(new byte[]{-97, 83});
        vL = new ByteBuffer(new byte[]{-97, 102});
        vM = new ByteBuffer(new byte[]{11, 10});
        vN = new ByteBuffer(new byte[]{-36});
        vO = new ByteBuffer(new byte[]{111});
        vP = new ByteBuffer(new byte[]{-124});
        vQ = new ByteBuffer(new byte[]{-91});
        vR = new ByteBuffer(new byte[]{-65, 12});
        vS = new ByteBuffer(new byte[]{79});
        vT = new ByteBuffer(new byte[]{97});
        vU = new ByteBuffer(new byte[]{-47});
        vV = new ByteBuffer(new byte[]{-97, 93});
        vW = new ByteBuffer(new byte[]{95, 37});
        vX = new ByteBuffer(new byte[]{-97, 8});
        vY = new HashMap();
        vZ = new ArrayList();
        vZ.add((Object)uL.getInt());
        vZ.add((Object)uM.getInt());
        vZ.add((Object)uN.getInt());
        vZ.add((Object)uO.getInt());
        vZ.add((Object)uP.getInt());
        vZ.add((Object)uQ.getInt());
        vZ.add((Object)uR.getInt());
        vZ.add((Object)uS.getInt());
        vZ.add((Object)uG.getInt());
        vZ.add((Object)uH.getInt());
        vZ.add((Object)uI.getInt());
        vZ.add((Object)uJ.getInt());
        vZ.add((Object)uK.getInt());
        vZ.add((Object)uU.getInt());
        vZ.add((Object)uV.getInt());
        vZ.add((Object)uW.getInt());
        vZ.add((Object)uX.getInt());
        vZ.add((Object)uY.getInt());
        vZ.add((Object)uZ.getInt());
        vZ.add((Object)va.getInt());
        vZ.add((Object)vb.getInt());
        vZ.add((Object)vc.getInt());
        vZ.add((Object)vd.getInt());
        vZ.add((Object)ve.getInt());
        vZ.add((Object)vf.getInt());
        vZ.add((Object)vm.getInt());
        vZ.add((Object)vM.getInt());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean A(ByteBuffer byteBuffer) {
        boolean bl = true;
        if (byteBuffer == null) return false;
        if (byteBuffer.getSize() != 2) {
            return false;
        }
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "processApdu, C-APDU get data, profile tag found, tag " + byteBuffer.toHexString());
        if ((255 & byteBuffer.getByte(0)) == 191) return bl;
        if ((255 & byteBuffer.getByte(0)) != 223) return false;
        if ((255 & byteBuffer.getByte((int)bl)) < 33) return false;
        if ((255 & byteBuffer.getByte((int)bl)) > 47) return false;
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, profile tag");
        return bl;
    }

    public static boolean B(ByteBuffer byteBuffer) {
        if (byteBuffer == null || byteBuffer.getSize() != 2) {
            return false;
        }
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, profile tag found, tag " + byteBuffer.toHexString());
        if ((255 & byteBuffer.getByte(0)) == 223 && (255 & byteBuffer.getByte(1)) >= 1 && (255 & byteBuffer.getByte(1)) <= 10) {
            com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, IDD tag");
            return true;
        }
        return false;
    }

    public static ByteBuffer a(ByteBuffer byteBuffer, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "getProfile, get profile...");
        if (byteBuffer == null || byteBuffer.getSize() != 2) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "getProfile, wrong profile tag requested.");
            return null;
        }
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "getProfile, profile tag requested: " + byteBuffer.toHexString());
        if (c.A(byteBuffer)) {
            int n2 = 255 & byteBuffer.getInt();
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, profile requested: " + (255 & byteBuffer.getInt()));
            if (n2 == 80 || n2 >= 33 && n2 <= 47) {
                int n3 = n2 & 15;
                HashMap<Integer, DiscoverPaymentProfile> hashMap = discoverContactlessPaymentData.getPaymentProfiles();
                if (hashMap == null || hashMap.isEmpty()) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, payment profile map is empty");
                    return null;
                }
                DiscoverPaymentProfile discoverPaymentProfile = (DiscoverPaymentProfile)hashMap.get((Object)n3);
                if (discoverPaymentProfile == null) {
                    com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, profile not found, id " + n3);
                    return null;
                }
                com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, compose profile");
                return c.b(discoverPaymentProfile);
            }
            if (n2 == 81) {
                com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, ZIP profile requested");
                com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, ZIP afl:" + discoverContactlessPaymentData.getZipAfl());
                com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, ZIP aip:" + discoverContactlessPaymentData.getZipAip());
                if (discoverContactlessPaymentData.getZipAfl() != null && discoverContactlessPaymentData.getZipAip() != null) {
                    ByteBuffer byteBuffer2 = a.c(ux, discoverContactlessPaymentData.getZipAip());
                    byteBuffer2.append(a.c(uy, discoverContactlessPaymentData.getZipAfl()));
                    return byteBuffer2;
                }
                return null;
            }
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "getProfile, unknown profile id " + n2);
            return null;
        }
        com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "getProfile, not profile tag");
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static ByteBuffer a(Integer n2, int n3, DiscoverPaymentProfile discoverPaymentProfile, e e2, List<DiscoverIDDTag> list) {
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IDD tag: " + (Object)n2 + ", length: " + n3);
        if (n2.intValue() == vd.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getLCOA(), n3));
        }
        if (n2.intValue() == ve.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getUCOA(), n3));
        }
        if (n2.intValue() == vf.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getSTA(), n3));
        }
        if (n2.intValue() == vb.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getLCOL(), n3));
        }
        if (n2.intValue() == vc.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getUCOL(), n3));
        }
        if (n2.intValue() == va.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getCrmCounter(), n3));
        }
        if (n2.intValue() == uZ.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getCrmAccumulator(), n3));
        }
        if (n2.intValue() == uL.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCvmAccumulator(), n3));
        }
        if (n2.intValue() == uM.getInt()) {
            com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "profile.getCVM().getCvmCounter(): " + discoverPaymentProfile.getCVM().getCvmCounter());
            return ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCvmCounter(), n3);
        }
        if (n2.intValue() == uU.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCl().getClAccumulator(), n3));
        }
        if (n2.intValue() == uV.getInt()) {
            return ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCl().getClCounter(), n3));
        }
        int n4 = n2;
        int n5 = vU.getInt();
        ByteBuffer byteBuffer = null;
        if (n4 == n5) return byteBuffer;
        int n6 = n2;
        int n7 = vV.getInt();
        byteBuffer = null;
        if (n6 == n7) return byteBuffer;
        if (n2.intValue() == vN.getInt()) {
            byteBuffer = null;
            if (e2 == null) return byteBuffer;
            DiscoverCDCVM discoverCDCVM = e2.ea();
            byteBuffer = null;
            if (discoverCDCVM == null) return byteBuffer;
            DiscoverCDCVM.DiscoverCDCVMType discoverCDCVMType = e2.ea().dE();
            byteBuffer = null;
            if (discoverCDCVMType == null) return byteBuffer;
            return ByteBuffer.fromHexString(c.a(e2.ea().dE().dG(), n3));
        }
        int n8 = 255 & n2 >> 8;
        byteBuffer = null;
        if (n8 != 223) return byteBuffer;
        if (list == null || list.isEmpty()) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IDD tags are empty.");
            return null;
        }
        int n9 = -1 + (255 & n2);
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, iddTagNum: " + n9);
        if (n9 >= list.size()) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "DiscoverDataTags, composeIDD, IDD tag requested " + n9 + "," + "IDD tags in the profile " + list.size());
            return null;
        }
        Object object = list.get(n9);
        byteBuffer = null;
        if (object == null) return byteBuffer;
        return ((DiscoverIDDTag)list.get(n9)).getData();
    }

    private static String a(ByteBuffer byteBuffer, int n2) {
        if (byteBuffer == null) {
            return new ByteBuffer(n2).toHexString();
        }
        if (byteBuffer.getSize() < n2) {
            ByteBuffer byteBuffer2 = new ByteBuffer(n2);
            for (int i2 = 0; i2 < byteBuffer.getSize(); ++i2) {
                byteBuffer2.setByte(i2 + (n2 - byteBuffer.getSize()), byteBuffer.getByte(i2));
            }
            return byteBuffer2.toHexString();
        }
        if (byteBuffer.getSize() > n2) {
            return byteBuffer.copyBytes(0, n2).toHexString();
        }
        return byteBuffer.toHexString();
    }

    public static void a(e e2, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        DiscoverPaymentProfile discoverPaymentProfile = e2.ed().getPaymentProfile();
        vY.put((Object)ux.getInt(), (Object)discoverPaymentProfile.getAip());
        vY.put((Object)uy.getInt(), (Object)discoverPaymentProfile.getAfl());
        vY.put((Object)uz.getInt(), (Object)discoverContactlessPaymentData.getDiscoverApplicationData().getCLApplicationConfigurationOptions());
        vY.put((Object)uB.getInt(), (Object)discoverContactlessPaymentData.getCaco());
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "CPR to MAP: " + e2.ed().dI());
        vY.put((Object)uC.getInt(), (Object)e2.ed().dI());
        vY.put((Object)uE.getInt(), (Object)discoverContactlessPaymentData.getSecondaryCurrency1());
        vY.put((Object)uF.getInt(), (Object)discoverContactlessPaymentData.getSecondaryCurrency2());
        vY.put((Object)uG.getInt(), (Object)discoverPaymentProfile.getCRM().getCRM_CAC_Switch_Interface());
        vY.put((Object)uH.getInt(), (Object)discoverPaymentProfile.getCRM().getCRM_CAC_Denial());
        vY.put((Object)uI.getInt(), (Object)discoverPaymentProfile.getCRM().getCRM_CAC_Online());
        vY.put((Object)uJ.getInt(), (Object)discoverPaymentProfile.getCRM().getCRM_CAC_Default());
        vY.put((Object)uK.getInt(), (Object)discoverPaymentProfile.getCRM().getCRM_CAC_Online());
        vY.put((Object)uL.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCvmAccumulator(), 6)));
        vY.put((Object)uM.getInt(), (Object)ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCvmCounter(), 1));
        vY.put((Object)uN.getInt(), (Object)ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCVM_Cons_Limit_1()));
        vY.put((Object)uO.getInt(), (Object)ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCVM_Cons_Limit_2()));
        vY.put((Object)uP.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Cum_Limit_1(), 6)));
        vY.put((Object)uQ.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Cum_Limit_2(), 6)));
        vY.put((Object)uR.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Sta_Limit_1(), 6)));
        vY.put((Object)uS.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Sta_Limit_2(), 6)));
        vY.put((Object)uT.getInt(), (Object)e2.ed().getPth());
        vY.put((Object)uU.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCl().getClAccumulator(), 6)));
        vY.put((Object)uV.getInt(), (Object)ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCl().getClCounter())));
        vY.put((Object)uW.getInt(), (Object)ByteBuffer.getFromLong(discoverPaymentProfile.getCl().getCL_Cons_Limit()));
        vY.put((Object)uX.getInt(), (Object)ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCl().getCL_Cum_Limit())));
        vY.put((Object)uY.getInt(), (Object)ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCl().getCL_STA_Limit())));
        vY.put((Object)uZ.getInt(), (Object)ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCRM().getCrmAccumulator())));
        vY.put((Object)va.getInt(), (Object)ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCRM().getCrmCounter())));
        vY.put((Object)vb.getInt(), (Object)ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCRM().getLCOL())));
        vY.put((Object)vc.getInt(), (Object)ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCRM().getUCOL())));
        vY.put((Object)vd.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getLCOA(), 6)));
        vY.put((Object)ve.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getUCOA(), 6)));
        vY.put((Object)vf.getInt(), (Object)ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getSTA(), 6)));
        vY.put((Object)vh.getInt(), (Object)discoverContactlessPaymentData.getCountryCode());
        vY.put((Object)vj.getInt(), (Object)discoverContactlessPaymentData.getCurrencyCodeCode());
        vY.put((Object)vk.getInt(), (Object)discoverPaymentProfile.getApplicationUsageControl());
        vY.put((Object)vn.getInt(), (Object)e2.dT());
        vY.put((Object)vq.getInt(), (Object)e2.ed().dQ());
        vY.put((Object)vr.getInt(), (Object)e2.ed().dR());
        vY.put((Object)vg.getInt(), (Object)discoverContactlessPaymentData.getIssuerApplicationData().getIADOL());
        vY.put((Object)vt.getInt(), (Object)discoverContactlessPaymentData.getPDOLProfileCheckTable());
        vY.put((Object)vp.getInt(), (Object)discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationState());
        vY.put((Object)uA.getInt(), (Object)discoverContactlessPaymentData.getIssuerApplicationData().getIssuerLifeCycleData());
        List<DiscoverIDDTag> list = discoverContactlessPaymentData.getIssuerApplicationData().getIDDTags();
        if (list != null) {
            for (DiscoverIDDTag discoverIDDTag : list) {
                if (discoverIDDTag == null || discoverIDDTag.getTag() == null || discoverIDDTag.getData() == null) continue;
                com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "put data tag: " + discoverIDDTag.getTag().toHexString() + ", get data " + discoverIDDTag.getData().toHexString());
                vY.put((Object)discoverIDDTag.getTag().getInt(), (Object)discoverIDDTag.getData());
            }
        }
        if (e2.ea() != null && e2.ea().dE() != null) {
            vY.put((Object)vN.getInt(), (Object)e2.ea().dE().dG());
        }
        if (e2.ed().dP() != null) {
            vY.put((Object)vM.getInt(), (Object)e2.ed().dP().dW());
        }
    }

    /*
     * Exception decompiling
     */
    public static void a(ByteBuffer var0, DiscoverPaymentProfile var1_1, e var2_2, List<DiscoverIDDTag> var3_3, ByteBuffer var4_4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public static DiscoverIDDTag b(ByteBuffer byteBuffer, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        if (discoverContactlessPaymentData == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "getIDDTag, paymentProfileData is null.");
            return null;
        }
        if (!c.B(byteBuffer)) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "getIDDTag, wrong idd tag.");
            return null;
        }
        List<DiscoverIDDTag> list = discoverContactlessPaymentData.getIssuerApplicationData().getIDDTags();
        if (list == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "getIDDTag, no valid IDD tag found.");
            return null;
        }
        for (DiscoverIDDTag discoverIDDTag : list) {
            if (discoverIDDTag == null || discoverIDDTag.getTag().getInt() != byteBuffer.getInt()) continue;
            return discoverIDDTag;
        }
        com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "getIDDTag, no valid IDD tag found, tag " + byteBuffer.toHexString());
        return null;
    }

    public static ByteBuffer b(DiscoverPaymentProfile discoverPaymentProfile) {
        ByteBuffer byteBuffer = a.c(ux, discoverPaymentProfile.getAip());
        byteBuffer.append(a.c(uy, discoverPaymentProfile.getAfl()));
        byteBuffer.append(a.c(vk, discoverPaymentProfile.getApplicationUsageControl()));
        byteBuffer.append(a.c(uC, discoverPaymentProfile.getCpr()));
        byteBuffer.append(a.c(uG, discoverPaymentProfile.getCRM().getCRM_CAC_Switch_Interface()));
        byteBuffer.append(a.c(uH, discoverPaymentProfile.getCRM().getCRM_CAC_Denial()));
        byteBuffer.append(a.c(uI, discoverPaymentProfile.getCRM().getCRM_CAC_Online()));
        byteBuffer.append(a.c(uJ, discoverPaymentProfile.getCRM().getCRM_CAC_Default()));
        byteBuffer.append(a.c(va, ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCRM().getCrmCounter()))));
        byteBuffer.append(a.c(uZ, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getCrmAccumulator(), 6))));
        byteBuffer.append(a.c(vb, ByteBuffer.getFromLong(discoverPaymentProfile.getCRM().getLCOL())));
        byteBuffer.append(a.c(vc, ByteBuffer.getFromLong(discoverPaymentProfile.getCRM().getUCOL())));
        byteBuffer.append(a.c(vd, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getLCOA(), 6))));
        byteBuffer.append(a.c(ve, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getUCOA(), 6))));
        byteBuffer.append(a.c(vf, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCRM().getSTA(), 6))));
        byteBuffer.append(a.c(uK, discoverPaymentProfile.getCRM().getCRM_CAC_Online()));
        byteBuffer.append(a.c(uL, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCvmAccumulator(), 6))));
        byteBuffer.append(a.c(uM, ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCVM().getCvmCounter()))));
        byteBuffer.append(a.c(uN, ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCVM_Cons_Limit_1())));
        byteBuffer.append(a.c(uO, ByteBuffer.getFromLong(discoverPaymentProfile.getCVM().getCVM_Cons_Limit_2())));
        byteBuffer.append(a.c(uP, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Cum_Limit_1(), 6))));
        byteBuffer.append(a.c(uQ, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Cum_Limit_2(), 6))));
        byteBuffer.append(a.c(uR, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Sta_Limit_1(), 6))));
        byteBuffer.append(a.c(uS, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCVM().getCVM_Sta_Limit_2(), 6))));
        byteBuffer.append(a.c(uU, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCl().getClAccumulator(), 6))));
        byteBuffer.append(a.c(uV, ByteBuffer.fromHexString(Long.toString((long)discoverPaymentProfile.getCl().getClCounter()))));
        byteBuffer.append(a.c(uW, ByteBuffer.getFromLong(discoverPaymentProfile.getCl().getCL_Cons_Limit())));
        byteBuffer.append(a.c(uX, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCl().getCL_Cum_Limit(), 6))));
        byteBuffer.append(a.c(uY, ByteBuffer.fromHexString(c.b(discoverPaymentProfile.getCl().getCL_STA_Limit(), 6))));
        byteBuffer.append(a.c(uD, discoverPaymentProfile.getCtq()));
        return byteBuffer;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static String b(long var0, int var2_1) {
        block6 : {
            block5 : {
                var3_2 = Long.toString((long)var0);
                com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "getTagValue, value " + var3_2 + ", length " + var2_1);
                var4_3 = var3_2.length() % 2 == 0 ? var3_2.length() / 2 : 1 + var3_2.length() / 2;
                com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "getTagValue, tagLength " + var4_3);
                var5_4 = new StringBuffer(var3_2);
                if (var4_3 >= var2_1) break block5;
                for (var10_5 = 0; var10_5 < var2_1 - var4_3; ++var10_5) {
                    var5_4.insert(0, "0").insert(0, "0");
                }
                if (var5_4.length() % 2 == 0) ** GOTO lbl24
                var5_4.insert(0, "0");
                var6_6 = var5_4;
                break block6;
            }
            if (var4_3 > var2_1) {
                for (var8_7 = 0; var8_7 < var4_3 - var2_1; ++var8_7) {
                    var9_8 = var5_4.delete(0, 1);
                    var5_4 = var9_8;
                }
                var6_6 = var5_4;
            } else {
                if (var5_4.length() % 2 != 0) {
                    var5_4.insert(0, "0");
                }
lbl24: // 4 sources:
                var6_6 = var5_4;
            }
        }
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "DiscoverDataTags, getTagValue: " + var6_6.toString());
        return var6_6.toString();
    }

    public static void b(e e2, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        ByteBuffer byteBuffer = a.c(ux, discoverContactlessPaymentData.getZipAip());
        byteBuffer.append(a.c(uy, discoverContactlessPaymentData.getZipAfl()));
        ByteBuffer byteBuffer2 = a.c(vr, byteBuffer);
        e2.ed().t(byteBuffer2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ByteBuffer c(ByteBuffer byteBuffer, DiscoverContactlessPaymentData discoverContactlessPaymentData) {
        if (byteBuffer == null) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "processApdu, C-APDU GET DATA, requested data tag is null");
            return null;
        } else {
            int n2 = byteBuffer.getInt();
            if (n2 == uz.getInt()) {
                return discoverContactlessPaymentData.getDiscoverApplicationData().getCLApplicationConfigurationOptions();
            }
            if (n2 == uB.getInt()) {
                return discoverContactlessPaymentData.getCaco();
            }
            if (n2 == uE.getInt()) {
                return discoverContactlessPaymentData.getSecondaryCurrency1();
            }
            if (n2 == uF.getInt()) {
                return discoverContactlessPaymentData.getSecondaryCurrency2();
            }
            if (n2 == vh.getInt()) {
                return discoverContactlessPaymentData.getCountryCode();
            }
            if (n2 == vj.getInt()) {
                return discoverContactlessPaymentData.getCurrencyCodeCode();
            }
            if (n2 == vg.getInt()) {
                return discoverContactlessPaymentData.getIssuerApplicationData().getIADOL();
            }
            if (n2 == vt.getInt()) {
                return discoverContactlessPaymentData.getPDOLProfileCheckTable();
            }
            if (n2 == vp.getInt()) {
                return discoverContactlessPaymentData.getDiscoverApplicationData().getApplicationState();
            }
            if (n2 != uA.getInt()) return null;
            return discoverContactlessPaymentData.getIssuerApplicationData().getIssuerLifeCycleData();
        }
    }

    public static boolean w(ByteBuffer byteBuffer) {
        com.samsung.android.spayfw.b.c.d("DCSDK_DiscoverDataTags", "Map size: " + vY.size());
        if (byteBuffer == null || byteBuffer.getSize() == 0) {
            com.samsung.android.spayfw.b.c.e("DCSDK_DiscoverDataTags", "checkDataTag, tag is null.");
            return false;
        }
        if ((255 & byteBuffer.getByte(0)) == 223) {
            com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "checkDataTag, tag is IDD tag.");
            return true;
        }
        return vY.containsKey((Object)byteBuffer.getInt());
    }

    public static ByteBuffer x(ByteBuffer byteBuffer) {
        com.samsung.android.spayfw.b.c.i("DCSDK_DiscoverDataTags", "Tag data: " + vY.get((Object)byteBuffer.getInt()));
        return (ByteBuffer)vY.get((Object)byteBuffer.getInt());
    }

    public static boolean y(ByteBuffer byteBuffer) {
        return vZ.contains((Object)byteBuffer.getInt());
    }

    public static boolean z(ByteBuffer byteBuffer) {
        return byteBuffer.getByte(0) == -33 && byteBuffer.getByte(1) > 0 && byteBuffer.getByte(1) <= 10;
    }
}

