/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NoSuchFieldError
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.Buffer
 *  java.nio.ByteBuffer
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Locale
 *  java.util.Map
 */
package com.samsung.android.visasdk.paywave;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.visasdk.a.c;
import com.samsung.android.visasdk.facade.data.ApduResponse;
import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenStatus;
import com.samsung.android.visasdk.facade.data.TransactionError;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.paywave.Constant;
import com.samsung.android.visasdk.paywave.data.ApduError;
import com.samsung.android.visasdk.paywave.data.TVL;
import com.samsung.android.visasdk.paywave.model.AidInfo;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.EncICCPrivateKey;
import com.samsung.android.visasdk.paywave.model.ExpirationDate;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.ICC;
import com.samsung.android.visasdk.paywave.model.ICCCRTPrivateKey;
import com.samsung.android.visasdk.paywave.model.MsdData;
import com.samsung.android.visasdk.paywave.model.Mst;
import com.samsung.android.visasdk.paywave.model.NFCReplenishData;
import com.samsung.android.visasdk.paywave.model.PaymentData;
import com.samsung.android.visasdk.paywave.model.PaymentRequest;
import com.samsung.android.visasdk.paywave.model.ProvisionedData;
import com.samsung.android.visasdk.paywave.model.QVSDCData;
import com.samsung.android.visasdk.paywave.model.QVSDCWithODA;
import com.samsung.android.visasdk.paywave.model.QVSDCWithoutODA;
import com.samsung.android.visasdk.paywave.model.StaticParams;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.samsung.android.visasdk.paywave.model.Track2DataDec;
import com.samsung.android.visasdk.paywave.model.Track2DataNotDec;
import com.samsung.android.visasdk.storage.e;
import com.visa.tainterface.VisaTAException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class a {
    public static final boolean Ei = true & c.LOG_DEBUG;
    public static final boolean Ej = true & c.LOG_DEBUG;
    public static final boolean Ek = true & c.LOG_DEBUG;
    public static final boolean El = true & c.LOG_DEBUG;
    public static final boolean Em = true & c.LOG_DEBUG;
    public static final boolean En = true & c.LOG_DEBUG;
    public static final boolean Eo = true & c.LOG_DEBUG;
    public static final boolean Ep = true & c.LOG_DEBUG;
    public static final boolean Eq = true & c.LOG_DEBUG;
    private static a Er;
    private com.samsung.android.visasdk.b.b Dl;
    private String EA;
    private List<AidInfo> EB = new ArrayList();
    private int EC = 0;
    private int ED = -1;
    private byte[] EE;
    private byte[] EF;
    private byte[] EG;
    private byte[] EH;
    private byte[] EI;
    private String EJ;
    private byte[] EK;
    private byte[] EL;
    private byte[] EM;
    private byte[] EN;
    private byte[] EO;
    private byte[] EP;
    private byte[] EQ;
    private byte[] ER;
    private byte[] ES;
    private byte[] ET;
    private byte[] EU;
    private byte[] EV;
    private byte[] EW;
    private byte[] EX;
    private byte[] EY;
    private byte[] EZ;
    private com.samsung.android.visasdk.storage.b Es;
    private e Et;
    private TokenInfo Eu;
    private byte[] Ev;
    private byte[] Ew;
    private String Ex;
    private byte[] Ey;
    private String Ez;
    private long FA;
    private int FB;
    private int FC;
    private List<TVL> FD = new ArrayList();
    private ApduResponse FE;
    private ApduResponse FF;
    private ApduResponse FG;
    private ApduResponse FH;
    private ApduResponse FI;
    private ApduResponse FJ;
    private TokenKey FK = null;
    private boolean FL;
    private boolean FM;
    private boolean FN;
    private boolean FO;
    private String FP;
    private boolean FQ;
    private boolean FS;
    private boolean FT;
    private boolean FU;
    private boolean FV;
    private CvmMode FW = Constant.DK;
    private Constant.TransactionType FX;
    private String FY;
    private String FZ;
    private byte[] Fa;
    private byte[] Fb;
    private byte[] Fc;
    private byte[] Fd;
    private byte[] Fe;
    private byte[] Ff;
    private byte[] Fg;
    private byte[] Fh;
    private byte[] Fi;
    private byte[] Fj;
    private byte[] Fk;
    private byte[] Fl;
    private byte[] Fm;
    private byte[] Fn;
    private byte[] Fo;
    private byte[] Fp;
    private String Fq;
    private String Fr;
    private String Fs;
    private String Ft;
    private byte[] Fu;
    private byte[] Fv;
    private int Fw;
    private byte[] Fx;
    private byte[] Fy;
    private byte[] Fz;
    private String Ga;
    private String Gb;
    private boolean Gc;
    private boolean Gd;
    private TransactionStatus Ge = new TransactionStatus(TransactionError.NO_ERROR, false);
    private String Gf;
    private boolean Gg = false;
    private boolean Gh = false;
    private byte Gi = 0;
    private Bundle Gj;

    private a(Context context, com.samsung.android.visasdk.b.b b2, Bundle bundle) {
        if (b2 == null) {
            throw new InitializationException("cryptoManager is null");
        }
        this.Dl = b2;
        this.Gj = bundle;
        this.Es = new com.samsung.android.visasdk.storage.b(context);
        if (this.Es == null) {
            throw new InitializationException("cannot access db");
        }
        this.Et = new e(context);
        if (this.Et == null) {
            throw new InitializationException("cannot access db");
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private a G(byte[] arrby) {
        if (arrby == null || arrby.length == 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "generated generateQVSDCandMSDVV is null");
            return null;
        }
        short s2 = arrby[0];
        short s3 = (short)(true ? 1 : 0);
        if (arrby.length < s2 + s3 || s2 <= 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "ARQC data length is not satisfied");
            return null;
        }
        byte[] arrby2 = new byte[s2];
        com.samsung.android.visasdk.a.b.a(arrby, s3, arrby2, 0, s2);
        short s4 = (short)(s2 + s3);
        a a2 = new a();
        a2.J(arrby2);
        if (arrby.length > s4) {
            short s5 = arrby[s4];
            short s6 = (short)(s4 + 1);
            if (arrby.length < s5 + s6 || s5 <= 0) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "MSDVV data length is not satisfied");
                return null;
            }
            byte[] arrby3 = new byte[s5];
            com.samsung.android.visasdk.a.b.a(arrby, s6, arrby3, 0, s5);
            a2.I(arrby3);
            do {
                return a2;
                break;
            } while (true);
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "MSDVV output not avail");
        return a2;
    }

    private b H(byte[] arrby) {
        if (arrby == null || arrby.length == 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "SDAD result not satisfied ");
            return null;
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "result lenght " + arrby.length);
        short s2 = arrby[0];
        short s3 = (short)(true ? 1 : 0);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "UN len " + s2);
        if (s2 <= 0 || arrby.length < s3 + s2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "UN length error result length " + s2);
            return null;
        }
        byte[] arrby2 = new byte[s2];
        com.samsung.android.visasdk.a.b.a(arrby, s3, arrby2, 0, s2);
        b b2 = new b();
        b2.K(arrby2);
        short s4 = (short)(s2 + s3);
        if (arrby.length < s4 + 2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "length error result length not able to retrieve signature len ");
            return null;
        }
        short s5 = (short)(s4 + 2);
        short s6 = com.samsung.android.visasdk.a.b.getShort(arrby, s4);
        if (arrby.length < s5 + s6) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "Signature length error result length " + s6);
            return null;
        }
        byte[] arrby3 = new byte[s6];
        com.samsung.android.visasdk.a.b.a(arrby, s5, arrby3, 0, s6);
        (short)(s5 + s6);
        b2.L(arrby3);
        return b2;
    }

    private ApduResponse U(int n2) {
        byte[] arrby;
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructSelectAID() for aid " + n2);
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)256);
        byteBuffer.put((byte)111);
        int n3 = byteBuffer.position();
        byteBuffer.put((byte)0);
        if (this.EB == null) {
            this.EB = new ArrayList();
        }
        AidInfo aidInfo = (AidInfo)this.EB.get(n2);
        byteBuffer.put((byte)-124);
        byte[] arrby2 = com.samsung.android.visasdk.a.b.hexStringToBytes(aidInfo.getAid());
        if (arrby2 == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "constructed AID is null");
            return null;
        }
        byteBuffer.put((byte)arrby2.length);
        byteBuffer.put(com.samsung.android.visasdk.a.b.hexStringToBytes(aidInfo.getAid()));
        byteBuffer.put((byte)-91);
        int n4 = byteBuffer.position();
        byteBuffer.put((byte)0);
        if (aidInfo.getApplicationLabel() != null && aidInfo.getApplicationLabel().length() > 0 && (arrby = com.samsung.android.visasdk.a.b.hexStringToBytes(aidInfo.getApplicationLabel())) != null) {
            byteBuffer.put((byte)80);
            byteBuffer.put((byte)arrby.length);
            byteBuffer.put(arrby);
        }
        if (this.EI == null || this.EI.length <= 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "PDOL is incorrect");
            return new ApduResponse(-4);
        }
        byteBuffer.putShort((short)-24776);
        byteBuffer.put((byte)this.EI.length);
        byteBuffer.put(this.EI);
        if (this.EK != null && this.EK.length > 0) {
            byteBuffer.putShort((short)24365);
            byteBuffer.put((byte)this.EK.length);
            byteBuffer.put(this.EK);
        }
        byteBuffer.put((byte)-65);
        byteBuffer.put((byte)12);
        int n5 = byteBuffer.position();
        byteBuffer.put((byte)0);
        if (this.Ew == null || this.Ew.length < 5 || this.Ew.length > 16) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "application program id is incorrect");
            return new ApduResponse(-4);
        }
        byteBuffer.putShort((short)-24742);
        byteBuffer.put((byte)this.Ew.length);
        byteBuffer.put(this.Ew);
        if (!this.FT) {
            byteBuffer.putShort((short)-16541);
            byteBuffer.put((byte)4);
            byteBuffer.putShort((short)-8416);
            byteBuffer.put((byte)1);
            byteBuffer.put((byte)-128);
        }
        byteBuffer.put(n5, (byte)(-1 + (byteBuffer.position() - n5)));
        byteBuffer.put(n4, (byte)(-1 + (byteBuffer.position() - n4)));
        byteBuffer.put(n3, (byte)(-1 + (byteBuffer.position() - n3)));
        byte[] arrby3 = a.a(byteBuffer, byteBuffer.position());
        com.samsung.android.visasdk.a.b.e("constructSelectAID=", arrby3);
        return new ApduResponse(arrby3);
    }

    private byte[] V(int n2) {
        byte[] arrby = new byte[8];
        com.samsung.android.visasdk.a.b.a(Constant.Dq, 0, arrby, 0, Constant.Dq.length);
        int n3 = n2 % 10000;
        arrby[1] = (byte)(n3 % 10);
        int n4 = n3 / 10;
        arrby[1] = (byte)(arrby[1] + (byte)(16 * (n4 % 10)));
        int n5 = n4 / 10;
        arrby[0] = (byte)(n5 % 10);
        int n6 = n5 / 10;
        arrby[0] = (byte)(arrby[0] + (byte)(16 * (n6 % 10)));
        com.samsung.android.visasdk.a.b.e("MSDVV input=", arrby);
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int a(byte[] arrby, byte[] arrby2, byte[] arrby3, byte[] arrby4) {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "build_qVSDC_CVM()");
        if (arrby.length != 4) return -5;
        if (arrby2.length != 4) return -5;
        if (arrby3.length != 6) return -5;
        if (arrby4.length != 2) {
            return -5;
        }
        arrby4[0] = com.samsung.android.visasdk.a.b.a(arrby4[0], 7, 0);
        arrby4[0] = com.samsung.android.visasdk.a.b.a(arrby4[0], 8, 0);
        String string = ((AidInfo)this.EB.get(this.ED)).getCVMrequired();
        boolean bl = "Y".equals((Object)string) || "y".equals((Object)string);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "build_qVSDC_CVM(), Cap=" + com.samsung.android.visasdk.a.b.o(arrby2) + ", TTQ=" + com.samsung.android.visasdk.a.b.o(arrby) + ", CVMRequired=" + string);
        if (com.samsung.android.visasdk.a.b.isBitSet(arrby[1], 7) || bl) {
            if (bl) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "cvm flag is set");
            }
            if (com.samsung.android.visasdk.a.b.isBitSet(arrby[0], 3) && (com.samsung.android.visasdk.a.b.isBitSet(arrby2[2], 8) || com.samsung.android.visasdk.a.b.isBitSet(arrby2[2], 7))) {
                if (Ek) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "build_qVSDC_CVM online pin");
                }
                arrby4[0] = com.samsung.android.visasdk.a.b.a(arrby4[0], 8, 1);
                arrby3[0] = 110;
            } else if (com.samsung.android.visasdk.a.b.isBitSet(arrby[2], 7) && com.samsung.android.visasdk.a.b.isBitSet(arrby2[2], 4)) {
                if (Ek) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "build_qVSDC_CVM CDCVM");
                }
                if (!this.fW()) {
                    if (!Ek) return -6;
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "cdcvm not verified");
                    return -6;
                }
                arrby4[1] = com.samsung.android.visasdk.a.b.a(arrby4[1], 8, 1);
                if (this.FW == null) {
                    return -6;
                }
                arrby3[0] = this.FW.getCvmByte();
            } else if (com.samsung.android.visasdk.a.b.isBitSet(arrby[0], 2) && com.samsung.android.visasdk.a.b.isBitSet(arrby2[2], 5)) {
                if (Ek) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "build_qVSDC_CVM signature");
                }
                arrby4[0] = com.samsung.android.visasdk.a.b.a(arrby4[0], 7, 1);
                arrby3[0] = 109;
            } else {
                if (bl) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "error for brazil electron card");
                    return -5;
                }
                if (Ek) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "build_qVSDC_CVM not required");
                }
                arrby4[1] = com.samsung.android.visasdk.a.b.a(arrby4[1], 8, 1);
                arrby3[0] = 0;
            }
        } else {
            if (Ek) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "build_qVSDC_CVM not required, too");
            }
            arrby4[1] = com.samsung.android.visasdk.a.b.a(arrby4[1], 8, 1);
            arrby3[0] = 0;
        }
        arrby3[1] = com.samsung.android.visasdk.a.b.a(arrby3[1], 8, 0);
        arrby3[1] = com.samsung.android.visasdk.a.b.a(arrby3[1], 7, 0);
        arrby3[1] = com.samsung.android.visasdk.a.b.a(arrby3[1], 6, 1);
        arrby3[1] = com.samsung.android.visasdk.a.b.a(arrby3[1], 5, 0);
        if (this.Gh) {
            arrby3[2] = com.samsung.android.visasdk.a.b.a(arrby3[2], 6, 1);
            if (Ek) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "update CVRexceededVelocity to " + arrby3[2]);
            }
        }
        arrby3[4] = this.Gi;
        if (Ek) {
            com.samsung.android.visasdk.a.b.e("build_qVSDC_CVM CVR=", arrby3);
        }
        boolean bl2 = Ek;
        int n2 = 0;
        if (!bl2) return n2;
        com.samsung.android.visasdk.a.b.e("build_qVSDC_CVM CTQ=", arrby4);
        return 0;
    }

    private long a(TokenKey tokenKey, PaymentData paymentData) {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "selectCard()");
        if (tokenKey == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "token key is null");
        }
        if (paymentData == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "payment data is null");
        }
        if (tokenKey == null || paymentData == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "selected token is invalid");
            return -1L;
        }
        this.gk();
        this.FK = tokenKey;
        this.Eu = paymentData.getTokenInfo();
        this.a(this.Eu);
        try {
            if (!this.a(this.Eu, paymentData.getAtc())) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot process selected card data");
                this.Gc = false;
                return -1L;
            }
        }
        catch (VisaTAException visaTAException) {
            visaTAException.printStackTrace();
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "card selected successfully");
        return tokenKey.getTokenId();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static a a(Context context, com.samsung.android.visasdk.b.b b2, Bundle bundle) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (Er != null) return Er;
            Er = new a(context, b2, bundle);
            if (Er != null) return Er;
            throw new InitializationException("cannot initialize manager");
        }
    }

    private void a(ICC iCC) {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructEncICCPrivateKey");
        EncICCPrivateKey encICCPrivateKey = new EncICCPrivateKey();
        ICCCRTPrivateKey iCCCRTPrivateKey = new ICCCRTPrivateKey();
        iCCCRTPrivateKey.setPrimeP(iCC.getIccCRTprimep());
        iCCCRTPrivateKey.setPrimeQ(iCC.getIccCRTprimeq());
        iCCCRTPrivateKey.setExponent(iCC.getIccPrivKExpo());
        iCCCRTPrivateKey.setCoefDmodP(iCC.getIccCRTCoeffDModP());
        iCCCRTPrivateKey.setCoefDmodQ(iCC.getIccCRTCoeffDModQ());
        iCCCRTPrivateKey.setCoefQinvModP(iCC.getIccCRTCoeffQModP());
        iCCCRTPrivateKey.setModulus(iCC.getIccKeymod());
        encICCPrivateKey.setIccCRTPrivateKey(iCCCRTPrivateKey);
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(new int[]{128}).create();
        iCC.setEncIccCRTPrivKey(this.Dl.a((byte)26, gson.toJson((Object)encICCPrivateKey)));
    }

    private void a(TokenInfo tokenInfo) {
        QVSDCData qVSDCData;
        if (!c.LOG_DEBUG) {
            return;
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "=========== print token info starts =============");
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "--TokenInfo-------------------------------");
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "encTokenInfo=" + tokenInfo.getEncTokenInfo());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "expiryDate.month=" + tokenInfo.getExpirationDate().getMonth());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "expiryDate.year=" + tokenInfo.getExpirationDate().getYear());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "appPrgrmID=" + tokenInfo.getAppPrgrmID());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "lang=" + tokenInfo.getLang());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "last4=" + tokenInfo.getLast4());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "tokenReferenceID=" + tokenInfo.getTokenReferenceID());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "tokenRequestorID=" + tokenInfo.getTokenRequestorID());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "tokenStatus=" + tokenInfo.getTokenStatus());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "---------DynParams------------------------");
        DynParams dynParams = tokenInfo.getHceData().getDynParams();
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "api=" + dynParams.getApi());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "dki=" + dynParams.getDki());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "encKeyinfo=" + dynParams.getEncKeyInfo());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "keyExpTS=" + (Object)dynParams.getKeyExpTS());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "maxPmts=" + (Object)dynParams.getMaxPmts());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "sc=" + dynParams.getSc());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "---------StaticParams(common)-------------");
        StaticParams staticParams = tokenInfo.getHceData().getStaticParams();
        int n2 = staticParams.getAidInfo().size();
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "token Aids number: " + n2 + "{");
        for (int i2 = 0; i2 < n2; ++i2) {
            AidInfo aidInfo = (AidInfo)staticParams.getAidInfo().get(i2);
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "  aid=" + aidInfo.getAid());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "  applicationLabel=" + aidInfo.getApplicationLabel());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "  cap=" + aidInfo.getCap());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "  priority=" + aidInfo.getPriority());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "  CVMrequired=" + aidInfo.getCVMrequired());
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "}");
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "cardHolderNameVCPCS=" + staticParams.getCardHolderNameVCPCS());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "countryCode5F55=" + staticParams.getCountryCode5F55());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "issuerIdentificationNumber=" + staticParams.getIssuerIdentificationNumber());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "kernelIdentifier=" + staticParams.getKernelIdentifier());
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "pdol=" + staticParams.getPdol());
        if (tokenInfo.getMst() != null) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "---------------StaticParams(MST)----------");
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "mstSvcCode=" + tokenInfo.getMst().getSvcCode());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "mstCVV=" + tokenInfo.getMst().getCvv());
        }
        if (staticParams.getMsdData() != null) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "---------------StaticParams(MSD)----------");
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "afl(MSD)=" + staticParams.getMsdData().getAfl());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "aip(MSD)=" + staticParams.getMsdData().getAip());
        }
        if ((qVSDCData = staticParams.getQVSDCData()) != null) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "---------------StaticParams(QVSDC)--------");
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "auc=" + qVSDCData.getAuc());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "ced=" + qVSDCData.getCed());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "cid=" + qVSDCData.getCid());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "countryCode=" + qVSDCData.getCountryCode());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "ctq=" + qVSDCData.getCtq());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "cvn=" + qVSDCData.getCvn());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "digitalWalletId=" + qVSDCData.getDigitalWalletID());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "ffi=" + qVSDCData.getFfi());
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "psn=" + qVSDCData.getPsn());
            if (qVSDCData.getQVSDCWithoutODA() != null) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "------------------StaticParams(QVSDCnoODA)");
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "afl(noODA)=" + qVSDCData.getQVSDCWithoutODA().getAfl());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "aip(noODA)=" + qVSDCData.getQVSDCWithoutODA().getAip());
            }
            if (qVSDCData.getqVSDCWithODA() != null) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "------------------StaticParams(QVSDCwithODA)");
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "afl=" + qVSDCData.getqVSDCWithODA().getAfl());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "aip=" + qVSDCData.getqVSDCWithODA().getAip());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "appExpDate=" + qVSDCData.getqVSDCWithODA().getAppExpDate());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "capki=" + qVSDCData.getqVSDCWithODA().getCapki());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "cardAuthData=" + qVSDCData.getqVSDCWithODA().getCardAuthData());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "iPubKCert=" + qVSDCData.getqVSDCWithODA().getIPubkCert());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "iPubKExpo=" + qVSDCData.getqVSDCWithODA().getIPubkExpo());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "iPubKeyRem=" + qVSDCData.getqVSDCWithODA().getIPubkRem());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "icc=" + qVSDCData.getqVSDCWithODA().getIcc());
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "sdad=" + qVSDCData.getqVSDCWithODA().getSdad());
            }
        }
        if (staticParams.getTrack2DataDec() != null) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "---------------Track2(Decimalize)-------");
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "" + staticParams.getTrack2DataDec());
        }
        if (staticParams.getTrack2DataNotDec() != null) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "---------------Track2(NotDecimalize)---------");
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "" + staticParams.getTrack2DataNotDec());
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "=========== print token info ends =============");
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean a(NFCReplenishData nFCReplenishData) {
        if (nFCReplenishData == null) {
            return false;
        }
        if (nFCReplenishData.getEncKeyInfo() != null && nFCReplenishData.getEncKeyInfo().length() > 0) {
            this.Fz = com.samsung.android.visasdk.a.b.hexStringToBytes(this.Dl.a((byte)22, nFCReplenishData.getEncKeyInfo()));
        }
        if (nFCReplenishData.getEncTokenInfo() != null && nFCReplenishData.getEncTokenInfo().length() > 0) {
            this.Ey = com.samsung.android.visasdk.a.b.hexStringToBytes(this.Dl.a((byte)23, nFCReplenishData.getEncTokenInfo()));
        }
        if (nFCReplenishData.getATC() != null && nFCReplenishData.getATC().length() > 0) {
            try {
                this.Fw = Integer.parseInt((String)nFCReplenishData.getATC());
            }
            catch (NumberFormatException numberFormatException) {}
        }
        this.Gc = "Y".equals((Object)nFCReplenishData.getReadyPayState());
        if ("Y".equals((Object)nFCReplenishData.getCDCVMverified())) {
            this.j(true);
        } else {
            this.j(false);
        }
        if (nFCReplenishData.getApi() != null && nFCReplenishData.getApi().length() > 0) {
            this.Fx = com.samsung.android.visasdk.a.b.hexStringToBytes(nFCReplenishData.getApi());
        }
        this.Gh = "Y".equals((Object)nFCReplenishData.getCVRexceededVelocity());
        if (nFCReplenishData.getConsumerDeviceState() != null && nFCReplenishData.getConsumerDeviceState().length() == 2) {
            this.Gi = (byte)(255 & Integer.parseInt((String)nFCReplenishData.getConsumerDeviceState(), (int)16));
        }
        try {
            this.gh();
            return true;
        }
        catch (VisaTAException visaTAException) {
            visaTAException.printStackTrace();
            return false;
        }
        catch (VisaTAException visaTAException) {
            return false;
        }
        catch (VisaTAException visaTAException) {
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean a(TokenInfo tokenInfo, int n2) {
        QVSDCData qVSDCData;
        HceData hceData;
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "retriveProvisionedData()");
        if (tokenInfo == null || n2 < 0 || n2 > 65535) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provisioned data is invalid");
            return false;
        }
        this.Ew = com.samsung.android.visasdk.a.b.hexStringToBytes(tokenInfo.getAppPrgrmID());
        this.Ex = tokenInfo.getTokenStatus();
        this.FL = false;
        this.FM = false;
        this.FN = false;
        this.FO = false;
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "retrieve AIDs");
        }
        this.EC = this.Eu.getHceData().getStaticParams().getAidInfo().size();
        if (this.EC > 0 && this.EC <= 2) {
            for (int i2 = 0; i2 < this.EC; ++i2) {
                AidInfo aidInfo = (AidInfo)tokenInfo.getHceData().getStaticParams().getAidInfo().get(i2);
                this.EB.add((Object)aidInfo);
                if ("A0000000031010".equals((Object)aidInfo.getAid())) {
                    this.FM = true;
                    if (!Ei) continue;
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "SUPPORT VISA_CREDIT");
                    continue;
                }
                if ("A0000000980840".equals((Object)aidInfo.getAid())) {
                    this.FL = true;
                    if (!Ei) continue;
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "SUPPORT US_COMMON_DEBIT");
                    continue;
                }
                if ("A0000000032020".equals((Object)aidInfo.getAid())) {
                    this.FN = true;
                    if (!Ei) continue;
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "SUPPORT V PAY");
                    continue;
                }
                if (!"A0000000032010".equals((Object)aidInfo.getAid())) continue;
                this.FO = true;
                if (!Ei) continue;
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "SUPPORT VISA ELECTRON");
            }
        }
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "retrieve HceData");
        }
        if ((hceData = tokenInfo.getHceData()) == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "hceData is null");
            return false;
        }
        StaticParams staticParams = hceData.getStaticParams();
        if (hceData.getStaticParams() == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "staticParams are null");
            return false;
        }
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "retrieve tokenInfo");
        }
        this.Ey = com.samsung.android.visasdk.a.b.hexStringToBytes(tokenInfo.getEncTokenInfo());
        this.Ez = tokenInfo.getExpirationDate().getYear();
        this.EA = tokenInfo.getExpirationDate().getMonth();
        this.EE = com.samsung.android.visasdk.a.b.hexStringToBytes(tokenInfo.getHceData().getStaticParams().getCardHolderNameVCPCS());
        if (this.EE.length > 26) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provision data error: CardholderName length is invalid");
            return false;
        }
        String string = "";
        if (tokenInfo.getTokenRequestorID().length() == 11) {
            string = string + '0';
        }
        this.Ev = com.samsung.android.visasdk.a.b.hexStringToBytes(string + tokenInfo.getTokenRequestorID());
        this.EK = com.samsung.android.visasdk.a.b.hexStringToBytes(tokenInfo.getLang());
        this.EF = com.samsung.android.visasdk.a.b.hexStringToBytes(staticParams.getCountryCode5F55());
        this.EG = com.samsung.android.visasdk.a.b.hexStringToBytes(staticParams.getIssuerIdentificationNumber());
        this.EH = com.samsung.android.visasdk.a.b.hexStringToBytes(staticParams.getKernelIdentifier());
        this.EJ = staticParams.getPdol();
        this.EI = this.gn() ? com.samsung.android.visasdk.a.b.hexStringToBytes(this.EJ + "9F4E20") : com.samsung.android.visasdk.a.b.hexStringToBytes(this.EJ);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "sPdol " + com.samsung.android.visasdk.a.b.o(this.EI));
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "retrieve qVSDC data");
        }
        if ((qVSDCData = staticParams.getQVSDCData()) != null) {
            this.EL = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getAuc());
            this.EM = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getCed());
            this.EN = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getCid());
            this.EO = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getCountryCode());
            this.EP = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getCtq());
            this.EQ = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getCvn());
            this.ER = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getDigitalWalletID());
            this.ES = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getFfi());
            this.ET = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getPsn());
            if (qVSDCData.getQVSDCWithoutODA() != null) {
                this.EU = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getQVSDCWithoutODA().getAfl());
                this.EV = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getQVSDCWithoutODA().getAip());
            }
            if (qVSDCData.getqVSDCWithODA() != null && qVSDCData.getqVSDCWithODA().getIcc() != null) {
                this.EW = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getqVSDCWithODA().getAfl());
                this.EX = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getqVSDCWithODA().getAip());
                this.EY = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getqVSDCWithODA().getAppExpDate());
                this.EZ = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getqVSDCWithODA().getCapki());
                this.Fa = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getqVSDCWithODA().getIPubkCert());
                this.Fb = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getqVSDCWithODA().getIPubkExpo());
                this.Fc = com.samsung.android.visasdk.a.b.hexStringToBytes(qVSDCData.getqVSDCWithODA().getIPubkRem());
                ICC iCC = qVSDCData.getqVSDCWithODA().getIcc();
                this.Fk = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccCRTCoeffDModP());
                this.Fl = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccCRTCoeffDModQ());
                this.Fm = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccCRTCoeffQModP());
                this.Fn = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccCRTprimep());
                this.Fo = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccCRTprimeq());
                this.Fp = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccKeymod());
                this.Ff = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccPrivKExpo());
                this.Fg = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccPubKCert());
                this.Fh = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccPubKExpo());
                this.Fi = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getIccPubKRem());
                this.Fj = com.samsung.android.visasdk.a.b.hexStringToBytes(iCC.getEncIccCRTPrivKey());
            }
        }
        if (staticParams.getMsdData() != null) {
            this.Fu = com.samsung.android.visasdk.a.b.hexStringToBytes(staticParams.getMsdData().getAfl());
            this.Fv = com.samsung.android.visasdk.a.b.hexStringToBytes(staticParams.getMsdData().getAip());
        }
        this.Fq = staticParams.getTrack2DataDec() != null && staticParams.getTrack2DataDec().getSvcCode() != null && staticParams.getTrack2DataDec().getSvcCode().length() > 0 ? staticParams.getTrack2DataDec().getSvcCode() : null;
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "retrieve Track2 data");
        }
        if (staticParams.getTrack2DataNotDec() != null) {
            if (staticParams.getTrack2DataNotDec().getSvcCode() != null && staticParams.getTrack2DataNotDec().getSvcCode().length() > 0) {
                this.Fq = staticParams.getTrack2DataNotDec().getSvcCode();
                this.Fr = staticParams.getTrack2DataNotDec().getSvcCode();
            }
            this.Fs = staticParams.getTrack2DataNotDec().getPinVerField();
            this.Ft = staticParams.getTrack2DataNotDec().getTrack2DiscData();
        }
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "retrieve dynamic params");
        }
        DynParams dynParams = hceData.getDynParams();
        this.Fw = n2;
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "ATC=" + this.Fw);
        }
        this.Fx = com.samsung.android.visasdk.a.b.hexStringToBytes(dynParams.getApi());
        this.Fy = com.samsung.android.visasdk.a.b.hexStringToBytes(dynParams.getDki());
        this.Fz = com.samsung.android.visasdk.a.b.hexStringToBytes(dynParams.getEncKeyInfo());
        this.FA = dynParams.getKeyExpTS();
        this.FB = dynParams.getMaxPmts();
        this.FC = dynParams.getSc();
        return this.gh();
    }

    private static byte[] a(ByteBuffer byteBuffer, int n2) {
        if (byteBuffer == null || n2 <= 0) {
            return Constant.Dy;
        }
        byteBuffer.putShort((short)-28672);
        byte[] arrby = new byte[n2 + 2];
        byteBuffer.position(0);
        byteBuffer.get(arrby, 0, arrby.length);
        return arrby;
    }

    private static void c(int n2, byte[] arrby) {
        if (arrby == null || arrby.length < 2) {
            return;
        }
        arrby[1] = (byte)(n2 % 16);
        int n3 = n2 / 16;
        arrby[1] = (byte)(arrby[1] + 16 * (byte)(n3 % 16));
        int n4 = n3 / 16;
        arrby[0] = (byte)(n4 % 16);
        int n5 = n4 / 16;
        arrby[0] = (byte)(arrby[0] + 16 * (byte)(n5 % 16));
    }

    /*
     * Enabled aggressive block sorting
     */
    private static byte[] c(byte[] arrby, int n2) {
        int n3;
        if (arrby.length != n2) {
            return null;
        }
        byte[] arrby2 = new byte[3];
        String string = com.samsung.android.visasdk.a.b.o(arrby);
        int n4 = string.length();
        int n5 = 0;
        int n6 = 0;
        do {
            int n7;
            n3 = 0;
            if (n5 >= n4) break;
            n3 = 0;
            if (n6 >= 3) break;
            if ('0' <= string.charAt(n5) && string.charAt(n5) <= '9') {
                n7 = (short)(n6 + 1);
                arrby2[n6] = (byte)string.charAt(n5);
            } else {
                n7 = n6;
            }
            n5 = (short)(n5 + 1);
            n6 = n7;
        } while (true);
        do {
            int n8;
            block7 : {
                block8 : {
                    block5 : {
                        block6 : {
                            if (n3 >= n4 || n6 >= 3) break block5;
                            if ('A' > string.charAt(n3) || string.charAt(n3) > 'Z') break block6;
                            n8 = (short)(n6 + 1);
                            arrby2[n6] = (byte)(48 + (-10 + (-65 + string.charAt(n3))));
                            break block7;
                        }
                        if ('a' > string.charAt(n3) || string.charAt(n3) > 'z') break block8;
                        n8 = (short)(n6 + 1);
                        arrby2[n6] = (byte)(48 + (-10 + (-97 + string.charAt(n3))));
                        break block7;
                    }
                    return arrby2;
                }
                n8 = n6;
            }
            n3 = (short)(n3 + 1);
            n6 = n8;
        } while (true);
    }

    private byte[] fX() {
        String string;
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructTrack2noMSDVerificationValue()");
        this.FP = this.Dl.r(this.Ey);
        if (this.FP == null || this.FP.length() <= 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "constructTrack2withMSDVerificationValue(), dec token is null");
            return null;
        }
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "Token has been retrieved from storage");
        }
        String string2 = this.FP;
        String string3 = string2 + 'D';
        String string4 = string3 + this.Ez.substring(2, 4);
        String string5 = string4 + this.EA;
        String string6 = string5 + this.Fr;
        if (this.Fs.length() == 5) {
            string = string6 + this.Fs;
            if (this.Ft.length() > 38) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "provision data error: track2 discretionary data length is invalid");
                return null;
            }
        } else {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provision data error: pin verify filed length is ignored");
            return null;
        }
        String string7 = string + this.Ft;
        if (string7.length() % 2 != 0) {
            string7 = string7 + 'F';
            if (El) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "PADDING F... at " + string7.length());
            }
        }
        if (El) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructed track2 data: " + string7);
        }
        return com.samsung.android.visasdk.a.b.hexStringToBytes(string7);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private ApduResponse fY() {
        block13 : {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructSelectPPSE()");
            var1_1 = ByteBuffer.allocate((int)256);
            var1_1.put((byte)111);
            var3_2 = var1_1.position();
            var1_1.put((byte)0);
            var1_1.put((byte)-124);
            var1_1.put((byte)Constant.Ds.length);
            var1_1.put(Constant.Ds);
            var1_1.put((byte)-91);
            var9_3 = var1_1.position();
            var1_1.put((byte)0);
            var1_1.putShort((short)-16628);
            var12_4 = var1_1.position();
            var1_1.put((byte)0);
            if (!this.Gc) ** GOTO lbl40
            if (this.EC != 1) break block13;
            var47_5 = (AidInfo)this.EB.get(0);
            if (a.En) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "appLabel=" + var47_5.getApplicationLabel());
            }
            var1_1.put((byte)97);
            var49_6 = var1_1.position();
            var1_1.put((byte)0);
            var1_1.put((byte)79);
            var52_7 = com.samsung.android.visasdk.a.b.hexStringToBytes(var47_5.getAid());
            var1_1.put((byte)var52_7.length);
            var1_1.put(var52_7);
            if (var47_5.getApplicationLabel().length() > 0) {
                var1_1.put((byte)80);
                var57_8 = com.samsung.android.visasdk.a.b.hexStringToBytes(var47_5.getApplicationLabel());
                var1_1.put((byte)var57_8.length);
                var1_1.put(var57_8);
            }
            var1_1.put(var49_6, (byte)(-1 + (var1_1.position() - var49_6)));
            ** GOTO lbl40
        }
        if (this.EC == 2) {
            var18_10 = 0;
        } else {
            if (this.EC <= 0 || this.EC >= 3) {
                // empty if block
            }
lbl40: // 6 sources:
            do {
                var1_1.put(var12_4, (byte)(-1 + (var1_1.position() - var12_4)));
                var1_1.put(var9_3, (byte)(-1 + (var1_1.position() - var9_3)));
                var1_1.put(var3_2, (byte)(-1 + (var1_1.position() - var3_2)));
                var17_9 = a.a(var1_1, var1_1.position());
                com.samsung.android.visasdk.a.b.e("constructSelectPPSE=", var17_9);
                return new ApduResponse(var17_9);
                break;
            } while (true);
        }
        do {
            if (var18_10 >= this.EC) ** continue;
            var19_11 = (AidInfo)this.EB.get(var18_10);
            if (a.En) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "appLabel=" + var19_11.getApplicationLabel());
            }
            var1_1.put((byte)97);
            var21_12 = var1_1.position();
            var1_1.put((byte)0);
            var1_1.put((byte)79);
            var24_13 = com.samsung.android.visasdk.a.b.hexStringToBytes(var19_11.getAid());
            var1_1.put((byte)var24_13.length);
            var1_1.put(var24_13);
            if (var19_11.getApplicationLabel().length() > 0) {
                var1_1.put((byte)80);
                var44_14 = com.samsung.android.visasdk.a.b.hexStringToBytes(var19_11.getApplicationLabel());
                var1_1.put((byte)var44_14.length);
                var1_1.put(var44_14);
            }
            var1_1.put((byte)-121);
            var1_1.put((byte)1);
            var1_1.put(Byte.parseByte((String)var19_11.getPriority()));
            var1_1.putShort((short)-24790);
            var1_1.put((byte)this.EH.length);
            var1_1.put(this.EH);
            if (this.EG != null && this.EG.length > 0) {
                var1_1.put((byte)66);
                var1_1.put((byte)this.EG.length);
                var1_1.put(this.EG);
            }
            if (this.EF != null && this.EF.length > 0) {
                var1_1.putShort((short)24405);
                var1_1.put((byte)this.EF.length);
                var1_1.put(this.EF);
            } else if (this.FL) {
                var1_1.putShort((short)24405);
                var1_1.put((byte)2);
                var1_1.putShort((short)21843);
            }
            var1_1.put(var21_12, (byte)(-1 + (var1_1.position() - var21_12)));
            var18_10 = (short)(var18_10 + 1);
        } while (true);
    }

    private ApduResponse fZ() {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructGPO MSD");
        byte[] arrby = new byte[]{-128, 6, 0, -64, 8, 1, 1, 0, -112, 0};
        com.samsung.android.visasdk.a.b.e("GPO_MSD=", arrby);
        return new ApduResponse(arrby);
    }

    private ApduResponse ga() {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "preconstructGPO_qVSDCnoODA()");
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)256);
        byteBuffer.put((byte)119);
        byteBuffer.put((byte)0);
        byteBuffer.putShort((short)-24794);
        byteBuffer.put((byte)8);
        byteBuffer.position(8 + byteBuffer.position());
        if (this.EU == null || this.EU.length != 4) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA afl is not valid");
            return new ApduResponse(-4);
        }
        byteBuffer.put((byte)-108);
        byteBuffer.put((byte)4);
        byteBuffer.put(this.EU);
        if (this.EV == null || this.EV.length != 2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA aip is not valid");
            return new ApduResponse(-4);
        }
        byteBuffer.put((byte)-126);
        byteBuffer.put((byte)2);
        byteBuffer.put(this.EV);
        byteBuffer.putShort((short)-24778);
        byteBuffer.put((byte)2);
        byteBuffer.position(2 + byteBuffer.position());
        byteBuffer.putShort((short)-24724);
        byteBuffer.put((byte)2);
        byteBuffer.position(2 + byteBuffer.position());
        if (this.ES == null || this.ES.length != 4) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA ffi is not valid");
            return new ApduResponse(-4);
        }
        byteBuffer.putShort((short)-24722);
        byteBuffer.put((byte)4);
        byteBuffer.put(this.ES);
        byteBuffer.putShort((short)-24816);
        byteBuffer.put((byte)32);
        byteBuffer.position(32 + byteBuffer.position());
        byteBuffer.put((byte)87);
        byte[] arrby = new byte[byteBuffer.position()];
        byteBuffer.position(0);
        byteBuffer.get(arrby, 0, arrby.length);
        com.samsung.android.visasdk.a.b.e("preconstructGPO_qVSDCnoODA=", arrby);
        return new ApduResponse(arrby);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private ApduResponse gb() {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructReadRecord() -MSD");
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)256);
        byteBuffer.put((byte)112);
        int n2 = byteBuffer.position();
        byteBuffer.put((byte)0);
        byteBuffer.put((byte)87);
        byte[] arrby = null;
        try {
            byte[] arrby2 = this.V(this.Fw);
            byte[] arrby3 = this.Dl.h(this.Fz, arrby2);
            arrby = this.v(arrby3);
            com.samsung.android.visasdk.a.b.p(arrby3);
        }
        catch (VisaTAException visaTAException) {
            visaTAException.printStackTrace();
        }
        if (arrby == null || arrby.length <= 0) {
            return new ApduResponse(-9);
        }
        byteBuffer.put((byte)arrby.length);
        byteBuffer.put(arrby);
        if (this.EE != null && this.EE.length <= 26 && this.EE.length > 0) {
            byteBuffer.put((byte)95);
            byteBuffer.put((byte)32);
            byteBuffer.put((byte)this.EE.length);
            byteBuffer.put(this.EE);
        }
        byteBuffer.put(n2, (byte)(-1 + (byteBuffer.position() - n2)));
        byte[] arrby4 = a.a(byteBuffer, byteBuffer.position());
        if (Ep && Eq) {
            com.samsung.android.visasdk.a.b.e("constructReadRecord0101=", arrby4);
        }
        return new ApduResponse(arrby4);
    }

    private ApduResponse gd() {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructReadRecord() -qVSDC no ODA");
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)256);
        byteBuffer.put((byte)112);
        int n2 = byteBuffer.position();
        byteBuffer.put((byte)0);
        if (this.EE != null && this.EE.length <= 26 && this.EE.length > 0) {
            byteBuffer.put((byte)95);
            byteBuffer.put((byte)32);
            byteBuffer.put((byte)this.EE.length);
            byteBuffer.put(this.EE);
        }
        if (this.EO == null || this.EO.length != 2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provision data error: Issuer Country Code length is invalid");
            return new ApduResponse(-9);
        }
        byteBuffer.putShort((short)24360);
        byteBuffer.put((byte)this.EO.length);
        byteBuffer.put(this.EO);
        if (this.EM != null && this.EM.length > 0 && this.EM.length <= 32) {
            byteBuffer.putShort((short)-24708);
            byteBuffer.put((byte)this.EM.length);
            byteBuffer.put(this.EM);
        }
        if (this.EL.length != 2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provision data error: auc length is invalid");
            return new ApduResponse(-9);
        }
        byteBuffer.putShort((short)-24825);
        byteBuffer.put((byte)this.EL.length);
        byteBuffer.put(this.EL);
        if (this.Ev == null || this.Ev.length != 6) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provision data error: Token Requester ID (TRID) length is invalid");
            return new ApduResponse(-9);
        }
        byteBuffer.putShort((short)-24807);
        byteBuffer.put((byte)6);
        byteBuffer.put(this.Ev);
        byteBuffer.put(n2, (byte)(-1 + (byteBuffer.position() - n2)));
        byte[] arrby = a.a(byteBuffer, byteBuffer.position());
        if (Ep && Eq) {
            com.samsung.android.visasdk.a.b.e("constructReadRecord0103=", arrby);
        }
        return new ApduResponse(arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    private ApduResponse ge() {
        int n2;
        ByteBuffer byteBuffer;
        block6 : {
            ApduResponse apduResponse;
            block5 : {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructReadRecord() -qVSDC with ODA 1");
                apduResponse = new ApduResponse(-9);
                byteBuffer = ByteBuffer.allocate((int)256);
                byteBuffer.put((byte)112);
                byteBuffer.put((byte)-127);
                n2 = byteBuffer.position();
                byteBuffer.put((byte)0);
                if (this.Fa == null || this.Fa.length > 192) break block5;
                byteBuffer.put((byte)-112);
                byteBuffer.put((byte)-127);
                byteBuffer.put((byte)this.Fa.length);
                byteBuffer.put(this.Fa);
                if (this.Fb == null || this.Fb.length != 3 && this.Fb.length != 1) break block5;
                byteBuffer.putShort((short)-24782);
                byteBuffer.put((byte)this.Fb.length);
                byteBuffer.put(this.Fb);
                if (this.Fc != null && this.Fc.length <= 36) {
                    byteBuffer.put((byte)-110);
                    byteBuffer.put((byte)this.Fc.length);
                    byteBuffer.put(this.Fc);
                }
                if (this.EZ != null && this.EZ.length == 1) break block6;
            }
            return apduResponse;
        }
        byteBuffer.put((byte)-113);
        byteBuffer.put((byte)1);
        byteBuffer.put(this.EZ[0]);
        byteBuffer.put(n2, (byte)(-1 + (byteBuffer.position() - n2)));
        byte[] arrby = a.a(byteBuffer, byteBuffer.position());
        if (Ep && Eq) {
            com.samsung.android.visasdk.a.b.e("constructReadRecord0202=", arrby);
        }
        return new ApduResponse(arrby);
    }

    /*
     * Enabled aggressive block sorting
     */
    private ApduResponse gf() {
        int n2;
        ByteBuffer byteBuffer;
        block6 : {
            ApduResponse apduResponse;
            block5 : {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructReadRecord() -qVSDC with ODA ");
                apduResponse = new ApduResponse(-9);
                byteBuffer = ByteBuffer.allocate((int)256);
                byteBuffer.put((byte)112);
                byteBuffer.put((byte)-127);
                n2 = byteBuffer.position();
                byteBuffer.put((byte)0);
                if (this.Fg == null || this.Fg.length > 192) break block5;
                byteBuffer.putShort((short)-24762);
                byteBuffer.put((byte)-127);
                byteBuffer.put((byte)this.Fg.length);
                byteBuffer.put(this.Fg);
                if (this.Fh != null && (this.Fh.length == 3 || this.Fh.length == 1)) break block6;
            }
            return apduResponse;
        }
        byteBuffer.putShort((short)-24761);
        byteBuffer.put((byte)this.Fh.length);
        byteBuffer.put(this.Fh);
        if (this.Fi != null && this.Fi.length <= 42) {
            byteBuffer.putShort((short)-24760);
            byteBuffer.put((byte)this.Fi.length);
            byteBuffer.put(this.Fi);
        }
        byteBuffer.put(n2, (byte)(-1 + (byteBuffer.position() - n2)));
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "buffer length=" + byteBuffer.position() + ", value=" + (byte)(-1 + (byteBuffer.position() - n2)));
        byte[] arrby = a.a(byteBuffer, byteBuffer.position());
        if (Ep && Eq) {
            com.samsung.android.visasdk.a.b.e("constructReadRecord0203=", arrby);
        }
        return new ApduResponse(arrby);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private ApduResponse gg() {
        int n2;
        b b2;
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructReadRecord() -qVSDC with ODA 3");
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "testing");
        ApduResponse apduResponse = new ApduResponse(-9);
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)256);
        byteBuffer.put((byte)112);
        byteBuffer.put((byte)-127);
        int n3 = byteBuffer.position();
        byteBuffer.put((byte)0);
        if (this.Fd == null) return apduResponse;
        if (this.Fd.length != 7) return apduResponse;
        byte[] arrby = this.gm();
        if (arrby == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "SDAD input generation null");
            return new ApduResponse(-5);
        }
        try {
            byte[] arrby2 = com.samsung.android.visasdk.b.b.k(this.Fj, arrby);
            b2 = this.H(arrby2);
            com.samsung.android.visasdk.a.b.p(arrby2);
            if (b2 == null || b2.gq() == null || b2.getSignature() == null) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "SDAD result not satisfied ");
                return new ApduResponse(-5);
            }
            n2 = b2.gq().length;
            if (n2 != 4) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "UN length error result length " + n2);
                return new ApduResponse(-5);
            }
        }
        catch (Exception exception) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot generate SDAD");
            exception.printStackTrace();
            return new ApduResponse(-5);
        }
        com.samsung.android.visasdk.a.b.a(b2.gq(), 0, this.Fd, 1, n2);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "new sQVSDCData_withODA_cardAuthData " + com.samsung.android.visasdk.a.b.o(this.Fd));
        int n4 = b2.getSignature().length;
        if (n4 <= 0 || n4 > 192) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "length error result length not able to retrieve signature len " + n4);
            return new ApduResponse(-5);
        }
        this.Fe = new byte[n4];
        com.samsung.android.visasdk.a.b.a(b2.getSignature(), 0, this.Fe, 0, n4);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "sdad output " + com.samsung.android.visasdk.a.b.o(this.Fe));
        b2.clear();
        if (this.Fe == null) return apduResponse;
        if (this.Fe.length <= 0) return apduResponse;
        if (this.Fe.length > 192) return apduResponse;
        byteBuffer.putShort((short)-24727);
        byteBuffer.put((byte)this.Fd.length);
        byteBuffer.put(this.Fd);
        byteBuffer.putShort((short)-24757);
        byteBuffer.put((byte)-127);
        byteBuffer.put((byte)this.Fe.length);
        byteBuffer.put(this.Fe);
        if (this.EL != null && this.EL.length == 2) {
            byteBuffer.putShort((short)-24825);
            byteBuffer.put((byte)this.EL.length);
            byteBuffer.put(this.EL);
        }
        if (this.EO == null || this.EO.length != 2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provision data error: Issuer Country Code length is invalid");
            return new ApduResponse(-9);
        }
        byteBuffer.put((byte)95);
        byteBuffer.put((byte)40);
        byteBuffer.put((byte)this.EO.length);
        byteBuffer.put(this.EO);
        if (this.EY == null) return apduResponse;
        if (this.EY.length > 3) return apduResponse;
        byteBuffer.putShort((short)24356);
        byteBuffer.put((byte)this.EY.length);
        byteBuffer.put(this.EY);
        if (this.FP == null) return apduResponse;
        if (this.FP.length() > 20) {
            return apduResponse;
        }
        byte[] arrby3 = com.samsung.android.visasdk.a.b.hexStringToBytes(this.FP);
        byteBuffer.put((byte)90);
        byteBuffer.put((byte)arrby3.length);
        byteBuffer.put(arrby3);
        if (this.Ev != null && this.Ev.length == 6) {
            byteBuffer.putShort((short)-24807);
            byteBuffer.put((byte)this.Ev.length);
            byteBuffer.put(this.Ev);
        }
        byteBuffer.put(n3, (byte)(-1 + (byteBuffer.position() - n3)));
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "buffer length=" + byteBuffer.position() + ", value=" + (byte)(-1 + (byteBuffer.position() - n3)));
        byte[] arrby4 = a.a(byteBuffer, byteBuffer.position());
        if (!Ep) return new ApduResponse(arrby4);
        if (!Eq) return new ApduResponse(arrby4);
        com.samsung.android.visasdk.a.b.e("constructReadRecord0204=", arrby4);
        return new ApduResponse(arrby4);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean gh() {
        if (this.Fu == null || this.Fu.length <= 0 || this.Fv == null || this.Fv.length <= 0) {
            this.FU = true;
            if (Ei) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "msd data is not provisioned");
            }
        } else {
            this.FU = false;
            if (Ei) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "msd data is provisioned");
            }
        }
        if (this.EL == null || this.EL.length <= 0 || this.EP == null || this.EP.length <= 0) {
            this.FQ = true;
            if (Ei) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "qVSDC data is not provisioned");
            }
        } else {
            this.FQ = false;
            if (Ei) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "qVSDC data is provisioned");
            }
        }
        if (this.EU == null || this.EU.length <= 0 || this.EV == null || this.EV.length <= 0) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "noODA data is not provisioned");
            this.FS = true;
        } else {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "noODA data is provisioned");
            this.FS = false;
        }
        if (this.EW == null || this.EW.length <= 0 || this.EX == null || this.EX.length <= 0) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "ODA data is not provisioned");
            this.FT = true;
        } else {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "ODA data is provisioned");
            this.FT = false;
        }
        if (this.Fr != null && this.Fr.length() > 0) {
            this.FV = false;
            if (Ei) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "notDecSVC is provisioned");
            }
        } else {
            this.FV = true;
            if (Ei) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "notDecSVC is not provisioned");
            }
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "preprocess data end");
        return this.gj();
    }

    private boolean gi() {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "clearProvisionedData()");
        this.Ew = null;
        this.Ex = TokenStatus.NOT_FOUND.getStatus();
        this.EB.clear();
        this.EC = 0;
        this.FL = false;
        this.FM = false;
        this.EC = 0;
        com.samsung.android.visasdk.a.b.p(this.Ey);
        this.Ey = null;
        this.Ez = null;
        this.EA = null;
        this.EE = null;
        com.samsung.android.visasdk.a.b.p(this.Ev);
        this.Ev = null;
        this.EK = null;
        this.EF = null;
        this.EG = null;
        this.EH = null;
        this.EI = null;
        this.EJ = null;
        this.EL = null;
        this.EM = null;
        this.EN = null;
        this.EO = null;
        this.EP = null;
        this.EQ = null;
        this.ER = null;
        this.ES = null;
        this.ET = null;
        this.EU = null;
        this.EV = null;
        this.EW = null;
        this.EX = null;
        this.EY = null;
        this.EZ = null;
        this.Fd = null;
        this.Fa = null;
        this.Fb = null;
        this.Fc = null;
        this.Fu = null;
        this.Fv = null;
        this.Fq = null;
        this.Fr = null;
        this.Fs = null;
        this.Ft = null;
        this.Fw = 0;
        this.Fx = null;
        this.Fy = null;
        com.samsung.android.visasdk.a.b.p(this.Fz);
        this.Fz = null;
        this.FA = 0L;
        this.FB = 0;
        this.FC = 0;
        this.FU = true;
        this.FQ = true;
        this.FS = true;
        this.FT = true;
        this.FV = true;
        this.FP = null;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean gj() {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "preprocessApdus()");
        this.Gc = TokenStatus.ACTIVE.getStatus().equals((Object)this.Ex);
        this.FE = this.fY();
        if (this.FE == null || this.FE.getApduError() == null || this.FE.getApduError().getErrorCode() < 0) {
            this.Gc = false;
        }
        if (this.EC > 0) {
            this.FF = this.U(0);
            if (this.FF == null || this.FF.getApduError() == null || this.FF.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
            if (this.EC > 1) {
                this.FG = this.U(1);
                if (this.FG == null || this.FG.getApduError() == null || this.FG.getApduError().getErrorCode() < 0) {
                    this.Gc = false;
                }
            }
        }
        if (!this.FU) {
            this.FH = this.fZ();
            if (this.FH == null || this.FH.getApduError() == null || this.FH.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
        }
        if (!this.FQ) {
            this.FI = this.ga();
            if (this.FI == null || this.FI.getApduError() == null || this.FI.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
            this.FJ = this.gd();
            if (this.FJ == null || this.FJ.getApduError() == null || this.FJ.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
        }
        if (this.Gc) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "apdu pre-processed successfully");
            do {
                return this.Gc;
                break;
            } while (true);
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "apdu pre-processed error");
        return this.Gc;
    }

    private void gk() {
        this.gl();
        this.FK = null;
        this.gi();
        this.FD.clear();
        this.Gc = false;
        this.Gd = false;
        this.FW = Constant.DK;
        this.Gh = false;
        this.j(true);
        this.Gi = 0;
        this.Eu = null;
    }

    private byte[] gm() {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructSDADTransactionData");
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)256);
        if (this.Fh == null || this.Fh.length == 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "sQVSDCData_withODA_iccPubKExpo null");
            return null;
        }
        byteBuffer.put((byte)this.Fh.length);
        byteBuffer.put(this.Fh);
        byteBuffer.put((byte)-107);
        byteBuffer.put((byte)1);
        byte[] arrby = new byte[2];
        a.c(this.Fw, arrby);
        if (arrby.length != 2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "ATC length is not 2");
            return null;
        }
        byteBuffer.put((byte)3);
        byteBuffer.put((byte)2);
        byteBuffer.put(arrby);
        if (this.FY == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "sUnpredictableNumber null");
            return null;
        }
        byte[] arrby2 = com.samsung.android.visasdk.a.b.hexStringToBytes(this.FY);
        if (arrby2 == null || arrby2.length != 4) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "sUnpredictableNumber size not 4");
            return null;
        }
        byteBuffer.put(arrby2);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "UN len " + arrby2.length);
        if (this.FZ == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "sUnpredictableNumber null");
            return null;
        }
        byte[] arrby3 = com.samsung.android.visasdk.a.b.hexStringToBytes(this.FZ);
        if (arrby3 == null || arrby3.length != 6) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "authorizedAmount size not 6:");
            return null;
        }
        byteBuffer.put(arrby3);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "authorizedAmount len " + arrby3.length);
        if (this.Ga == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "sCurrencyCode null");
            return null;
        }
        byte[] arrby4 = com.samsung.android.visasdk.a.b.hexStringToBytes(this.Ga);
        if (arrby4 == null || arrby4.length != 2) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "currencyCode size not 2:");
            return null;
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "currencyCode len " + arrby4.length);
        byteBuffer.put(arrby4);
        byteBuffer.put(this.Fd);
        int n2 = byteBuffer.position();
        byte[] arrby5 = new byte[n2];
        byteBuffer.position(0);
        byteBuffer.get(arrby5, 0, n2);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "SDAD input data length " + arrby5.length);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "SDAD input data :" + com.samsung.android.visasdk.a.b.o(arrby5));
        return arrby5;
    }

    private boolean gn() {
        if (this.Gj == null) {
            return false;
        }
        return this.Gj.getBoolean("pdolValues");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void k(boolean bl) {
        StaticParams staticParams;
        com.samsung.android.visasdk.c.a.d("VcpcsManager", this.Gf);
        Gson gson = new Gson();
        if (bl) {
            if (Em) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "update selected card through NFC");
            }
            this.a((NFCReplenishData)gson.fromJson(this.Gf, NFCReplenishData.class));
            return;
        }
        if (Em) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "select card from NFC VTS blob");
        }
        ProvisionedData provisionedData = (ProvisionedData)gson.fromJson(this.Gf, ProvisionedData.class);
        TokenKey tokenKey = new TokenKey(0L);
        TokenInfo tokenInfo = provisionedData.getTokenInfo();
        if (tokenInfo == null || tokenInfo.getHceData() == null || tokenInfo.getHceData().getDynParams() == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "token info is null");
            return;
        }
        DynParams dynParams = tokenInfo.getHceData().getDynParams();
        if (dynParams.getEncKeyInfo() == null || dynParams.getEncKeyInfo().length() <= 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot store VTS data, no card is selected");
            return;
        }
        if (dynParams.getEncKeyInfo().length() > 65535) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "encKeyInfo size is too large");
            return;
        }
        try {
            dynParams.setEncKeyInfo(this.Dl.a((byte)22, dynParams.getEncKeyInfo()));
        }
        catch (VisaTAException visaTAException) {
            visaTAException.printStackTrace();
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot parse NFC VTS blob");
            return;
        }
        if (tokenInfo.getEncTokenInfo().length() > 65535) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "encTokenInfo size is too large");
            return;
        }
        try {
            tokenInfo.setEncTokenInfo(this.Dl.a((byte)23, tokenInfo.getEncTokenInfo()));
        }
        catch (VisaTAException visaTAException) {
            visaTAException.printStackTrace();
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot get token to parse NFC VTS blob");
        }
        if ((staticParams = tokenInfo.getHceData().getStaticParams()).getQVSDCData().getqVSDCWithODA() != null && staticParams.getQVSDCData().getqVSDCWithODA().getIcc() != null) {
            try {
                this.a(staticParams.getQVSDCData().getqVSDCWithODA().getIcc());
            }
            catch (VisaTAException visaTAException) {
                visaTAException.printStackTrace();
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot get token to parse NFC VTS blob");
            }
        }
        PaymentData paymentData = new PaymentData();
        paymentData.setTokenInfo(tokenInfo);
        paymentData.setAtc(0);
        paymentData.setLukUseCount(0);
        this.a(tokenKey, paymentData);
    }

    private static boolean l(byte[] arrby, byte[] arrby2) {
        return com.samsung.android.visasdk.a.b.g(arrby, Constant.DM) && com.samsung.android.visasdk.a.b.g(arrby2, Constant.DN);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private short m(byte[] arrby, byte[] arrby2) {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "buildIADFormat()");
        if (arrby2.length != 32 || arrby.length != 6) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "IAD format buffer or CVR length is incorrect");
            return (short)-4;
        }
        int n2 = this.EQ.length;
        int n3 = this.Fy.length;
        int n4 = this.ER.length;
        int n5 = this.Fx.length;
        if (n2 != 1 || n3 != 1 || arrby.length != 6 || n4 != 4 || n5 != 4) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "buildIADFormat(): provison data error");
            return -4;
        }
        if (14 + (1 + (n5 + (n4 + (n3 + (n2 + 1) + arrby.length)))) != arrby2.length) {
            return -1;
        }
        short s2 = (short)(true ? 1 : 0);
        arrby2[0] = 31;
        short s3 = (short)(s2 + 1);
        arrby2[s2] = this.EQ[0];
        short s4 = (short)(s3 + 1);
        arrby2[s3] = this.Fy[0];
        com.samsung.android.visasdk.a.b.a(arrby, 0, arrby2, s4, arrby.length);
        short s5 = (short)(s4 + arrby.length);
        com.samsung.android.visasdk.a.b.a(this.ER, 0, arrby2, s5, n4);
        short s6 = (short)(s5 + n4);
        com.samsung.android.visasdk.a.b.a(this.Fx, 0, arrby2, s6, n5);
        short s7 = (short)(s6 + n5);
        short s8 = (short)(s7 + 1);
        arrby2[s7] = 0;
        com.samsung.android.visasdk.a.b.a(arrby2, s8, (byte)0, 14);
        int n6 = (short)(s8 + 14);
        if (n6 == arrby2.length) return (short)n6;
        return -4;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private ApduResponse n(byte[] var1_1, byte[] var2_2) {
        block18 : {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructGPO_qVSDCnoODA()");
            if (var1_1.length != 33) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "pdol value length is invalid");
                return new ApduResponse(-4);
            }
            var3_3 = ByteBuffer.allocate((int)256);
            if (this.FI == null) {
                return new ApduResponse(-9);
            }
            var4_4 = this.FI.getApduData().length;
            var3_3.put(this.FI.getApduData());
            var6_5 = new byte[]{1, -128};
            var7_6 = new byte[]{0, 0, 0, 0, 0, 0};
            var8_7 = com.samsung.android.visasdk.a.b.hexStringToBytes(((AidInfo)this.EB.get(this.ED)).getCap());
            com.samsung.android.visasdk.a.b.a(this.EP, 0, var6_5, 0, 2);
            if (this.ED < 0) {
                return new ApduResponse(-5);
            }
            var9_8 = this.a(var2_2, var8_7, var7_6, var6_5);
            if (var9_8 != 0) {
                return new ApduResponse(var9_8);
            }
            var10_9 = new byte[32];
            var11_10 = this.m(var7_6, var10_9);
            if (var10_9.length != 32) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "IAD_FORMAT length is not 32");
                return new ApduResponse(-5);
            }
            if (var11_10 <= 0) {
                return new ApduResponse(var11_10);
            }
            var12_11 = new byte[2];
            a.c(this.Fw, var12_11);
            if (var12_11.length != 2) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "ATC length is not 2");
                return new ApduResponse(-5);
            }
            var13_12 = new byte[65];
            com.samsung.android.visasdk.a.b.a(var1_1, 4, var13_12, 0, -4 + var1_1.length);
            var14_13 = (short)(0 + (-4 + var1_1.length));
            com.samsung.android.visasdk.a.b.a(this.EV, 0, var13_12, var14_13, 2);
            var15_14 = (short)(var14_13 + 2);
            com.samsung.android.visasdk.a.b.a(var12_11, 0, var13_12, var15_14, 2);
            com.samsung.android.visasdk.a.b.a(var10_9, 0, var13_12, (short)(var15_14 + 2), 32);
            try {
                var18_15 = com.samsung.android.visasdk.b.b.i(this.Fz, var13_12);
                ** if (var18_15 != null) goto lbl-1000
            }
            catch (VisaTAException var16_16) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot generate cryptogram");
                return new ApduResponse(-5);
            }
lbl-1000: // 1 sources:
            {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "generated application cryptogram is null");
                return new ApduResponse(-5);
            }
lbl-1000: // 1 sources:
            {
            }
            if (var18_15.length != 8) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "ARQC data length is not 8");
                return new ApduResponse(-5);
            }
            com.samsung.android.visasdk.a.b.e("ARQC data=", var18_15);
            var3_3.position(5);
            var3_3.put(var18_15);
            com.samsung.android.visasdk.a.b.p(var18_15);
            var3_3.position(26);
            var3_3.put(var12_11);
            var3_3.position(31);
            var3_3.put(var6_5);
            var3_3.position(43);
            var3_3.put(var10_9);
            try {
                if (!this.FV) {
                    var28_18 = var43_17 = this.fX();
                    break block18;
                }
                var41_19 = this.V(this.Fw);
                var42_20 = this.Dl.h(this.Fz, var41_19);
                var28_18 = this.v(var42_20);
                com.samsung.android.visasdk.a.b.p(var42_20);
            }
            catch (Exception var27_21) {
                var27_21.printStackTrace();
                var28_18 = null;
            }
        }
        if (var28_18 == null || var28_18.length <= 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "track data error: token is not retrieved?");
            return new ApduResponse(-5);
        }
        var3_3.position(var4_4);
        var3_3.put((byte)var28_18.length);
        var3_3.put(var28_18);
        if (this.ET.length != 1) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provison data error: PSN is invalid");
            return new ApduResponse(-4);
        }
        var3_3.put((byte)95);
        var3_3.put((byte)52);
        var3_3.put((byte)1);
        var3_3.put(this.ET[0]);
        if (this.EN != null && this.EN.length == 1) {
            var3_3.putShort((short)-24793);
            var3_3.put((byte)1);
            var3_3.put(this.EN);
            var3_3.put(1, (byte)(-1 + (-1 + var3_3.position())));
            var40_22 = a.a(var3_3, var3_3.position());
            if (a.Eo == false) return new ApduResponse(var40_22);
            if (a.Eq == false) return new ApduResponse(var40_22);
            com.samsung.android.visasdk.a.b.e("constructGPO_qVSDCnoODA", var40_22);
            return new ApduResponse(var40_22);
        }
        com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA cid is not valid");
        return new ApduResponse(-4);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private ApduResponse o(byte[] arrby, byte[] arrby2) {
        byte[] arrby3;
        byte[] arrby4;
        byte[] arrby5;
        byte[] arrby6;
        ByteBuffer byteBuffer;
        byte[] arrby7;
        block28 : {
            byte[] arrby8;
            a a2;
            block27 : {
                block26 : {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructGPO_qVSDCwithODA()");
                    if (arrby == null || arrby.length != 33) {
                        com.samsung.android.visasdk.c.a.e("VcpcsManager", "pdol value length is invalid");
                        return new ApduResponse(-4);
                    }
                    if (this.EX == null || this.EX.length != 2) {
                        com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA aip is not valid");
                        return new ApduResponse(-4);
                    }
                    byteBuffer = ByteBuffer.allocate((int)256);
                    arrby5 = new byte[]{1, -128};
                    byte[] arrby9 = new byte[]{0, 0, 0, 0, 0, 0};
                    if (this.ED < 0) {
                        return new ApduResponse(-5);
                    }
                    byte[] arrby10 = com.samsung.android.visasdk.a.b.hexStringToBytes(((AidInfo)this.EB.get(this.ED)).getCap());
                    com.samsung.android.visasdk.a.b.a(this.EP, 0, arrby5, 0, 2);
                    int n2 = this.a(arrby2, arrby10, arrby9, arrby5);
                    if (n2 != 0) {
                        return new ApduResponse(n2);
                    }
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "CTQ : " + com.samsung.android.visasdk.a.b.o(arrby5));
                    arrby3 = new byte[32];
                    short s2 = this.m(arrby9, arrby3);
                    if (arrby3.length != 32) {
                        com.samsung.android.visasdk.c.a.e("VcpcsManager", "IAD_FORMAT length is not 32");
                        return new ApduResponse(-5);
                    }
                    if (s2 <= 0) {
                        return new ApduResponse(s2);
                    }
                    arrby6 = new byte[2];
                    a.c(this.Fw, arrby6);
                    if (arrby6.length != 2) {
                        com.samsung.android.visasdk.c.a.e("VcpcsManager", "ATC length is not 2");
                        return new ApduResponse(-5);
                    }
                    byte[] arrby11 = new byte[65];
                    com.samsung.android.visasdk.a.b.a(arrby, 4, arrby11, 0, -4 + arrby.length);
                    short s3 = (short)(0 + (-4 + arrby.length));
                    com.samsung.android.visasdk.a.b.a(this.EX, 0, arrby11, s3, 2);
                    short s4 = (short)(s3 + 2);
                    com.samsung.android.visasdk.a.b.a(arrby6, 0, arrby11, s4, 2);
                    com.samsung.android.visasdk.a.b.a(arrby3, 0, arrby11, (short)(s4 + 2), 32);
                    arrby7 = new byte[8];
                    try {
                        boolean bl = this.FV;
                        byte[] arrby12 = null;
                        if (bl) {
                            arrby12 = this.V(this.Fw);
                        }
                        com.samsung.android.visasdk.c.a.d("VcpcsManager", "generate cryptogram " + com.samsung.android.visasdk.a.b.o(this.Fz));
                        byte[] arrby13 = com.samsung.android.visasdk.b.b.d(this.Fz, arrby11, arrby12);
                        a2 = this.G(arrby13);
                        com.samsung.android.visasdk.a.b.p(arrby13);
                        if (a2 == null || a2.gp() == null || a2.gp().length != 8) {
                            com.samsung.android.visasdk.c.a.e("VcpcsManager", "generated generateQVSDCandMSDVV is not right");
                            return new ApduResponse(-5);
                        }
                        com.samsung.android.visasdk.a.b.a(a2.gp(), 0, arrby7, 0, 8);
                        if (a2.go() == null || a2.go().length <= 0) break block26;
                        byte[] arrby14 = new byte[a2.go().length];
                        com.samsung.android.visasdk.a.b.a(a2.go(), 0, arrby14, 0, a2.go().length);
                        arrby8 = arrby14;
                        break block27;
                    }
                    catch (VisaTAException visaTAException) {
                        com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot generate cryptogram ");
                        return new ApduResponse(-5);
                    }
                }
                arrby8 = null;
            }
            a2.clear();
            if (arrby7 == null) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "generated application cryptogram is null");
                return new ApduResponse(-5);
            }
            if (arrby7.length != 8) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "ARQC data length is not 8");
                return new ApduResponse(-5);
            }
            com.samsung.android.visasdk.a.b.e("ARQC data=", arrby7);
            try {
                if (!this.FV) {
                    byte[] arrby15;
                    arrby4 = arrby15 = this.fX();
                    break block28;
                }
                arrby4 = this.v(arrby8);
                com.samsung.android.visasdk.a.b.p(arrby8);
            }
            catch (Exception exception) {
                exception.printStackTrace();
                arrby4 = null;
            }
        }
        if (arrby4 == null || arrby4.length <= 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "track data error: token is not retrieved?");
            return new ApduResponse(-5);
        }
        byteBuffer.put((byte)119);
        int n3 = byteBuffer.position();
        byteBuffer.put((byte)0);
        byteBuffer.putShort((short)-24794);
        byteBuffer.put((byte)8);
        byteBuffer.put(arrby7);
        if (this.EW == null || this.EW.length != 4) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA afl is not valid");
            return new ApduResponse(-4);
        }
        byteBuffer.put((byte)-108);
        byteBuffer.put((byte)4);
        byteBuffer.put(this.EW);
        byteBuffer.put((byte)-126);
        byteBuffer.put((byte)2);
        byteBuffer.put(this.EX);
        byteBuffer.putShort((short)-24778);
        byteBuffer.put((byte)2);
        byteBuffer.put(arrby6);
        byteBuffer.putShort((short)-24724);
        byteBuffer.put((byte)2);
        byteBuffer.put(arrby5);
        if (this.EN == null || this.EN.length != 1) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA cid is not valid");
            return new ApduResponse(-4);
        }
        byteBuffer.putShort((short)-24793);
        byteBuffer.put((byte)1);
        byteBuffer.put(this.EN);
        if (this.ES == null || this.ES.length != 4) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC noODA ffi is not valid");
            return new ApduResponse(-4);
        }
        byteBuffer.putShort((short)-24722);
        byteBuffer.put((byte)4);
        byteBuffer.put(this.ES);
        byteBuffer.putShort((short)-24816);
        byteBuffer.put((byte)32);
        byteBuffer.put(arrby3);
        byteBuffer.put((byte)87);
        byteBuffer.put((byte)arrby4.length);
        byteBuffer.put(arrby4);
        if (this.ET.length != 1) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "provison data error: PSN is invalid");
            return new ApduResponse(-4);
        }
        byteBuffer.put((byte)95);
        byteBuffer.put((byte)52);
        byteBuffer.put((byte)1);
        byteBuffer.put(this.ET[0]);
        if (this.EE != null && this.EE.length <= 26 && this.EE.length > 0) {
            byteBuffer.put((byte)95);
            byteBuffer.put((byte)32);
            byteBuffer.put((byte)this.EE.length);
            byteBuffer.put(this.EE);
        }
        if (this.EM != null && this.EM.length > 0 && this.EM.length <= 32) {
            byteBuffer.putShort((short)-24708);
            byteBuffer.put((byte)this.EM.length);
            byteBuffer.put(this.EM);
        }
        byteBuffer.put(n3, (byte)(-1 + (byteBuffer.position() - n3)));
        byte[] arrby16 = a.a(byteBuffer, byteBuffer.position());
        if (Eo && Eq) {
            com.samsung.android.visasdk.a.b.e("constructGPO_qVSDCwithODA", arrby16);
        }
        this.Fd = new byte[7];
        this.Fd[0] = 1;
        this.Fd[1] = 1;
        this.Fd[2] = 1;
        this.Fd[3] = 1;
        this.Fd[4] = 1;
        com.samsung.android.visasdk.a.b.a(arrby5, 0, this.Fd, 5, 2);
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "sQVSDCData_withODA_cardAuthData " + com.samsung.android.visasdk.a.b.o(this.Fd));
        return new ApduResponse(arrby16);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean s(byte[] arrby) {
        int n2;
        short s2;
        return arrby != null && (n2 = arrby.length) > 0 && n2 <= 256 && arrby[0] == -128 && arrby[1] == -17 && ((s2 = (short)(5 + (255 & arrby[4]))) + 1 == n2 && arrby[s2] == 0 || s2 == n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private short t(byte[] arrby) {
        this.Gf = "";
        int n2 = 255 & arrby[4];
        int n3 = 255 & arrby[6];
        byte[] arrby2 = new byte[n3];
        com.samsung.android.visasdk.a.b.a(arrby, 7, arrby2, 0, n3);
        String string = new String(arrby2);
        if (!Constant.Dp.equals((Object)string)) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "test account number is invalid: " + string);
        }
        int n4 = 7 + n3;
        int n5 = -2 + (n2 - n3);
        byte[] arrby3 = new byte[n5];
        com.samsung.android.visasdk.a.b.a(arrby, n4, arrby3, 0, n5);
        String string2 = new String(arrby3);
        this.Gf = this.Gf + string2;
        if (Ei) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "" + string2);
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] v(byte[] arrby) {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructTrack2withMSDVerificationValue()");
        this.FP = this.Dl.r(this.Ey);
        if (this.FP == null || this.FP.length() <= 0) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "constructTrack2withMSDVerificationValue(), dec token is null");
            return null;
        } else {
            if (Ei) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "Token has been retrieved from storage");
            }
            String string = this.FP;
            String string2 = string + 'D';
            String string3 = string2 + this.Ez.substring(2, 4);
            String string4 = string3 + this.EA;
            String string5 = string4 + this.Fq;
            String string6 = com.samsung.android.visasdk.a.b.o(this.Fx);
            if (string6 == null || string6.length() != 8) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "API is null");
                return null;
            }
            String string7 = string6.substring(2, 8);
            String string8 = string5 + string7;
            byte[] arrby2 = new byte[4];
            int n2 = this.Fw % 10000;
            arrby2[3] = (byte)(48 + n2 % 10);
            int n3 = n2 / 10;
            arrby2[2] = (byte)(48 + n3 % 10);
            int n4 = n3 / 10;
            arrby2[1] = (byte)(48 + n4 % 10);
            arrby2[0] = (byte)(48 + n4 / 10 % 10);
            String string9 = string8 + new String(arrby2);
            if (arrby == null || arrby.length != 8) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "generated msdVV is null");
                return null;
            }
            byte[] arrby3 = a.c(arrby, 8);
            if (arrby3 == null) return null;
            {
                String string10 = string9 + new String(arrby3);
                if (string10.length() % 2 != 0) {
                    string10 = string10 + 'F';
                    if (El) {
                        com.samsung.android.visasdk.c.a.e("VcpcsManager", "PADDING F... at " + string10.length());
                    }
                }
                if (!El) return com.samsung.android.visasdk.a.b.hexStringToBytes(string10);
                {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructed track2 data:" + string10);
                }
                return com.samsung.android.visasdk.a.b.hexStringToBytes(string10);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean w(byte[] arrby) {
        return arrby != null && arrby.length == 5 && arrby[0] == -128 && arrby[1] == -54 && arrby[2] == -97 && arrby[3] == 8 && arrby[4] == 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean x(byte[] arrby) {
        block14 : {
            int n2;
            short s2;
            int n3;
            block15 : {
                int n4;
                block13 : {
                    String string;
                    block17 : {
                        block16 : {
                            n2 = arrby.length;
                            if (1 + (5 + (255 & arrby[4])) != n2 || n2 > 256 || arrby[0] != 0 || arrby[1] != -92 || arrby[2] != 4 || (255 & arrby[4]) < 5 || (255 & arrby[4]) > 16) break block14;
                            s2 = (short)(255 & arrby[4]);
                            string = com.samsung.android.visasdk.a.b.o(com.samsung.android.visasdk.a.b.d(arrby, 5, s2));
                            if (En) {
                                com.samsung.android.visasdk.c.a.d("VcpcsManager", "selectedAid=" + string);
                            }
                            if (string == null || this.EC < 1 || this.EC > 2) break block14;
                            if (arrby[3] == 0) {
                                for (int i2 = 0; i2 < this.EC; ++i2) {
                                    if (!string.equals((Object)((AidInfo)this.EB.get(i2)).getAid())) continue;
                                    n3 = i2;
                                    break;
                                }
                            } else {
                                n3 = -1;
                            }
                            if (n3 >= 0 || s2 >= 7) break block15;
                            if (arrby[3] == 0) {
                                this.ED = -1;
                            }
                            if (this.ED >= 0) break block16;
                            break block17;
                        }
                        if (this.EC == 1 || this.ED == 1) break block14;
                        String string2 = ((AidInfo)this.EB.get(1)).getAid();
                        if (string2 != null && string2.startsWith(string)) {
                            n3 = 1;
                        }
                        break block15;
                    }
                    for (n4 = 0; n4 < this.EC; ++n4) {
                        String string3 = ((AidInfo)this.EB.get(n4)).getAid();
                        if (string3 == null || !string3.startsWith(string)) {
                            continue;
                        }
                        break block13;
                    }
                    n4 = n3;
                }
                n3 = n4;
            }
            this.ED = n3;
            if (this.ED >= 0) {
                int n5;
                if (En) {
                    if (this.ED >= 0 && this.ED <= 1) {
                        com.samsung.android.visasdk.c.a.d("VcpcsManager", "sActiveAid=" + ((AidInfo)this.EB.get(this.ED)).getAid());
                    } else {
                        com.samsung.android.visasdk.c.a.e("VcpcsManager", "no AID is selected");
                    }
                }
                if (arrby[n5 = 5 + s2] == 0 && n5 + 1 == n2) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean y(byte[] arrby) {
        short s2;
        if (arrby == null) return false;
        int n2 = arrby.length;
        if (arrby[0] != -128 || arrby[1] != -88 || arrby[2] != 0 || arrby[3] != 0) return false;
        if (this.gn()) {
            if (arrby[6] != 65 || arrby[4] != 67) return false;
        } else {
            if (arrby[6] != 33) return false;
            {
                if (arrby[4] != 35) {
                    return false;
                }
            }
        }
        if (arrby[5] != -125 || arrby[s2 = (short)(5 + arrby[4])] != 0 || (short)(s2 + 1) != n2) return false;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean z(byte[] arrby) {
        return arrby != null && arrby[0] == 0 && arrby[1] == -78;
    }

    ApduResponse B(byte[] arrby) {
        if (!this.w(arrby)) {
            return new ApduResponse(-12);
        }
        if (com.samsung.android.visasdk.a.b.getShort(arrby, 2) == -24824) {
            return new ApduResponse(Constant.Do);
        }
        return new ApduResponse(-12);
    }

    ApduResponse C(byte[] arrby) {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "selectPPSE()");
        if (this.FE == null || !this.Gc) {
            this.FE = this.fY();
        }
        ApduResponse apduResponse = this.FE;
        if (En && Eq) {
            com.samsung.android.visasdk.a.b.e("selectPPSE=", apduResponse.getApduData());
        }
        return apduResponse;
    }

    /*
     * Enabled aggressive block sorting
     */
    ApduResponse D(byte[] arrby) {
        ApduResponse apduResponse;
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "selectAID()");
        if (this.FK == null || this.FK.getTokenId() < 0L) {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "no card is selected yet");
            return new ApduResponse(-9);
        }
        if (!this.x(arrby)) {
            if (arrby[3] == 0) return new ApduResponse(-3);
            return new ApduResponse(-9);
        }
        new ApduResponse(null);
        if (this.ED == 0) {
            apduResponse = this.FF;
        } else {
            if (this.ED != 1) {
                return new ApduResponse(-3);
            }
            apduResponse = this.FG;
        }
        if (!En) return apduResponse;
        if (!Eq) return apduResponse;
        com.samsung.android.visasdk.a.b.e("selectAID=", apduResponse.getApduData());
        return apduResponse;
    }

    /*
     * Enabled aggressive block sorting
     */
    ApduResponse E(byte[] arrby) {
        ApduResponse apduResponse;
        block25 : {
            ApduResponse apduResponse2;
            byte[] arrby2;
            block20 : {
                byte[] arrby3;
                block21 : {
                    block23 : {
                        block24 : {
                            block22 : {
                                com.samsung.android.visasdk.c.a.d("VcpcsManager", "getProcessingOptions()");
                                if (!this.y(arrby)) {
                                    return new ApduResponse(-8);
                                }
                                if (this.FC >= 65535) return new ApduResponse(-5);
                                if (this.Fw >= 65535) {
                                    return new ApduResponse(-5);
                                }
                                if (!this.Gc) {
                                    return new ApduResponse(-6);
                                }
                                this.Fw = 1 + this.Fw;
                                com.samsung.android.visasdk.c.a.d("VcpcsManager", "update ATC counter to " + this.Fw);
                                int n2 = 33;
                                if (this.gn()) {
                                    n2 = 65;
                                }
                                if (n2 + 7 > arrby.length) {
                                    return new ApduResponse(-1);
                                }
                                byte[] arrby4 = new byte[n2];
                                com.samsung.android.visasdk.a.b.a(arrby, 7, arrby4, 0, n2);
                                apduResponse2 = new ApduResponse(-5);
                                this.FX = Constant.TransactionType.DY;
                                if (Eo) {
                                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "pdol= " + com.samsung.android.visasdk.a.b.o(arrby4) + ", pdol_length= " + n2 + ", TTQ_offset=" + 0 + ", aa_offset=" + 4 + ", oa_offset=" + 10 + ", countrycode_offset=" + 16 + ", currencyCode_offset=" + 23 + ", unprediactableNumber_offset=" + 29);
                                }
                                arrby2 = new byte[4];
                                com.samsung.android.visasdk.a.b.a(arrby4, 0, arrby2, 0, arrby2.length);
                                byte[] arrby5 = new byte[4];
                                com.samsung.android.visasdk.a.b.a(arrby4, 29, arrby5, 0, arrby5.length);
                                this.FY = com.samsung.android.visasdk.a.b.o(arrby5);
                                byte[] arrby6 = new byte[6];
                                com.samsung.android.visasdk.a.b.a(arrby4, 4, arrby6, 0, arrby6.length);
                                this.FZ = com.samsung.android.visasdk.a.b.o(arrby6);
                                byte[] arrby7 = new byte[6];
                                com.samsung.android.visasdk.a.b.a(arrby4, 10, arrby7, 0, arrby7.length);
                                byte[] arrby8 = new byte[2];
                                com.samsung.android.visasdk.a.b.a(arrby4, 16, arrby8, 0, arrby8.length);
                                byte[] arrby9 = new byte[2];
                                com.samsung.android.visasdk.a.b.a(arrby4, 23, arrby9, 0, arrby9.length);
                                this.Ga = com.samsung.android.visasdk.a.b.o(arrby9);
                                if (this.gn()) {
                                    byte[] arrby10 = new byte[32];
                                    com.samsung.android.visasdk.a.b.a(arrby4, 33, arrby10, 0, arrby10.length);
                                    this.Gb = com.samsung.android.visasdk.a.b.o(arrby10);
                                    String string = com.samsung.android.visasdk.a.b.bF(this.Gb);
                                    this.Ge.addPdolValue("9F4E", string);
                                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "9F4E= " + this.Gb);
                                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "merchant Name= " + string);
                                }
                                arrby3 = new byte[33];
                                com.samsung.android.visasdk.a.b.a(arrby4, 0, arrby3, 0, arrby3.length);
                                if (Eo) {
                                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "authorized=" + com.samsung.android.visasdk.a.b.o(arrby6) + ", other=" + com.samsung.android.visasdk.a.b.o(arrby7) + ", country=" + com.samsung.android.visasdk.a.b.o(arrby8) + ", currency=" + com.samsung.android.visasdk.a.b.o(arrby9));
                                }
                                if (this.ED < 0) {
                                    return new ApduResponse(-5);
                                }
                                byte[] arrby11 = com.samsung.android.visasdk.a.b.hexStringToBytes(((AidInfo)this.EB.get(this.ED)).getCap());
                                if (!com.samsung.android.visasdk.a.b.isBitSet(arrby2[0], 6)) break block20;
                                if (this.FQ) {
                                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC data is not provisioned for qVSDC transaction");
                                    return new ApduResponse(-5);
                                }
                                if (!com.samsung.android.visasdk.a.b.isBitSet(arrby2[0], 1) || this.FT || com.samsung.android.visasdk.a.b.isBitSet(arrby11[1], 6)) break block21;
                                this.FX = Constant.TransactionType.Eb;
                                boolean bl = a.l(arrby6, arrby9);
                                this.Ge.setTapNGoAllowed(bl);
                                if (this.fW()) break block22;
                                if (!bl || this.fW()) break block23;
                                com.samsung.android.visasdk.c.a.d("VcpcsManager", "tapAndGo filter satisfied");
                                this.Gd = true;
                                break block24;
                            }
                            com.samsung.android.visasdk.c.a.d("VcpcsManager", "tap with authentication");
                        }
                        if (Eo) {
                            com.samsung.android.visasdk.c.a.d("VcpcsManager", "this is qVSDC with ODA transaction");
                        }
                        apduResponse = this.o(arrby3, arrby2);
                        break block25;
                    }
                    this.Ge.setError(TransactionError.NO_AUTH_AMOUNT_REQ_NOT_SATISFIED);
                    return new ApduResponse(-5);
                }
                if (this.FS) {
                    return new ApduResponse(-5);
                }
                if (!this.fW()) {
                    this.Ge.setError(TransactionError.NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED);
                    return new ApduResponse(-5);
                }
                this.FX = Constant.TransactionType.Ea;
                if (Eo) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "this is qVSDC no ODA transaction");
                }
                apduResponse = this.n(arrby3, arrby2);
                break block25;
            }
            if (com.samsung.android.visasdk.a.b.isBitSet(arrby2[0], 8) && !com.samsung.android.visasdk.a.b.isBitSet(arrby2[0], 6)) {
                if (Eo) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "this is MSD transaciton");
                }
                if (this.FU) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "msd data is not provisioned for MSD transaction");
                    return new ApduResponse(-5);
                }
                if (!this.fW()) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "cdcvm is not verified");
                    this.Ge.setError(TransactionError.NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED);
                    return new ApduResponse(-5);
                }
                this.FX = Constant.TransactionType.DZ;
                apduResponse = this.FH;
            } else {
                if (!com.samsung.android.visasdk.a.b.isBitSet(arrby2[0], 8) && !com.samsung.android.visasdk.a.b.isBitSet(arrby2[0], 6)) {
                    if (!Eo) return new ApduResponse(-5);
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "this is unknown transaction");
                    return new ApduResponse(-5);
                }
                apduResponse = apduResponse2;
            }
        }
        if (!Eo) return apduResponse;
        if (!Eq) return apduResponse;
        com.samsung.android.visasdk.a.b.e("GPO=", apduResponse.getApduData());
        return apduResponse;
    }

    /*
     * Enabled aggressive block sorting
     */
    ApduResponse F(byte[] arrby) {
        ApduResponse apduResponse;
        byte by = 1;
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "readRecord()");
        if (!this.z(arrby)) {
            return new ApduResponse(-3);
        }
        byte by2 = (byte)(255 & arrby[3] >> 3);
        byte by3 = arrby[2];
        if (Ep) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "readrecord(): record=" + by2 + ", control=" + by3);
        }
        TVL tVL = new TVL();
        tVL.setTokenKey(this.FK);
        tVL.setTimeStamp(com.samsung.android.visasdk.a.b.fU());
        tVL.setUnpredictableNumber(this.FY);
        tVL.setAtc(this.Fw);
        tVL.setApi(com.samsung.android.visasdk.a.b.o(this.Fx));
        switch (this.FX) {
            default: {
                apduResponse = new ApduResponse(-9);
                by = 0;
                break;
            }
            case DZ: {
                if (by2 != by || by3 != by) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "msd transaction, cannot read record0101");
                    return new ApduResponse(-9);
                }
                if (Ep) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "msd transaction, readrecord0101");
                }
                apduResponse = this.gb();
                break;
            }
            case Ea: {
                if (by2 != by || by3 != 3) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC no ODA transaction, cannot read record0103");
                    return new ApduResponse(-9);
                }
                if (Ep) {
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "qVSDC no ODA transaction, readrecord0103");
                }
                apduResponse = this.FJ;
                break;
            }
            case Eb: {
                if (by2 != 2 || by3 != 2) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC with ODA transaction, cannot read record0202");
                    return new ApduResponse(-9);
                }
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "qVSDC with ODA transaction, readrecord0202");
                apduResponse = this.ge();
                this.FX = Constant.TransactionType.Ec;
                by = 0;
                break;
            }
            case Ec: {
                if (by2 != 2 || by3 != 3) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC with ODA transaction, cannot read record0203");
                    return new ApduResponse(-9);
                }
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "qVSDC with ODA transaction, readrecord0203");
                apduResponse = this.gf();
                this.FX = Constant.TransactionType.Ed;
                by = 0;
                break;
            }
            case Ed: {
                if (by2 != 2 || by3 != 4) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "qVSDC with ODA transaction, cannot read record0204");
                    return new ApduResponse(-9);
                }
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "qVSDC with ODA transaction, readrecord0204");
                apduResponse = this.gg();
            }
        }
        if (by != 0 && apduResponse.getApduError() != null && apduResponse.getApduError().getErrorCode() == 0) {
            if (this.FX == Constant.TransactionType.DZ) {
                tVL.setTransactionType("M");
            } else {
                tVL.setTransactionType("Q");
            }
            this.FD.add((Object)tVL);
            this.FX = Constant.TransactionType.Eg;
        }
        if (!Ep) return apduResponse;
        if (!Eq) return apduResponse;
        com.samsung.android.visasdk.a.b.e("readRecord=", apduResponse.getApduData());
        return apduResponse;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    protected TransactionStatus a(TokenKey var1_1, boolean var2_2) {
        block20 : {
            var10_3 = this;
            // MONITORENTER : var10_3
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "processTransactionComplete()");
            if (this.FD == null || this.FK == null || this.FK.getTokenId() < 0L) {
                var4_4 = new TransactionStatus(TransactionError.OTHER_ERROR, false);
                return var4_4;
            }
            var5_5 = this.FX == Constant.TransactionType.Eg;
            var6_6 = this.FD.size();
            this.FB = this.FB - var6_6 < 0 ? 0 : (this.FB -= var6_6);
            if (this.Es != null) {
                this.Es.a(var1_1, this.Fw, this.FB);
            } else {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "visa db helper is null");
            }
            if (this.FD == null || this.FD.size() <= 0) ** GOTO lbl37
            var7_7 = 0;
            break block20;
            do {
                // MONITOREXIT : var10_3
                return var4_4;
                break;
            } while (true);
        }
        do {
            if (var7_7 < this.FD.size()) {
                if (this.Et != null) {
                    if (!var2_2) {
                        var8_8 = (TVL)this.FD.get(var7_7);
                        if (var8_8 != null && "S".equals((Object)var8_8.getTransactionType())) {
                            com.samsung.android.visasdk.c.a.d("VcpcsManager", "store " + var8_8.toString());
                            this.Et.a(var8_8);
                        }
                    } else if (var5_5 && (var9_9 = (TVL)this.FD.get(var7_7)) != null && ("M".equals((Object)var9_9.getTransactionType()) || "Q".equals((Object)var9_9.getTransactionType()))) {
                        com.samsung.android.visasdk.c.a.d("VcpcsManager", "store " + var9_9.toString());
                        this.Et.a(var9_9);
                    }
                } else {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot access db");
                }
            } else {
                this.FD.clear();
lbl37: // 2 sources:
                var4_4 = new TransactionStatus(this.Ge.getError(), this.Ge.isTapNGoAllowed());
                if (var2_2) {
                    var4_4.setPdolValues(this.Ge.getPdolValues());
                    this.gl();
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "nfc transaction details are cleared");
                    if (var5_5) {
                        this.gk();
                        com.samsung.android.visasdk.c.a.d("VcpcsManager", "card data is cleared for nfc transaction");
                    }
                } else {
                    this.gk();
                    com.samsung.android.visasdk.c.a.d("VcpcsManager", "card data is cleared for mst transaction");
                }
                if (!var2_2) {
                    return new TransactionStatus(TransactionError.NO_ERROR, false);
                }
                if (var4_4.getError() != TransactionError.NO_ERROR || var5_5) ** continue;
                var4_4.setError(TransactionError.OTHER_ERROR);
                return var4_4;
            }
            var7_7 = (short)(var7_7 + 1);
        } while (true);
    }

    long b(TokenKey tokenKey) {
        return this.a(tokenKey, this.Es.b(tokenKey, true));
    }

    /*
     * Enabled aggressive block sorting
     */
    public PaymentDataRequest constructPaymentDataRequest(String string, TokenKey tokenKey, String string2, String string3) {
        PaymentDataRequest paymentDataRequest = new PaymentDataRequest();
        paymentDataRequest.setClientPaymentDataID(string2);
        paymentDataRequest.setvProvisionedTokenID("1");
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setTransactionType(string3);
        paymentDataRequest.setPaymentRequest(paymentRequest);
        this.Fw = 1 + this.Fw;
        if (this.Es != null) {
            this.Es.a(tokenKey, this.Fw, this.FB);
        } else {
            com.samsung.android.visasdk.c.a.e("VcpcsManager", "visa db helper is null");
        }
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "constructPaymentDataRequest update ATC counter to " + this.Fw);
        paymentDataRequest.setAtc(Integer.toString((int)this.Fw));
        return paymentDataRequest;
    }

    boolean fW() {
        return this.Gg;
    }

    void gl() {
        this.ED = -1;
        this.FY = "00000000";
        this.FX = Constant.TransactionType.DY;
        this.Ge.initialize();
    }

    void j(boolean bl) {
        this.Gg = bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean prepareMstData() {
        boolean bl;
        block8 : {
            bl = false;
            if (this.Eu == null) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "no card is selected for MST");
                return bl;
            }
            try {
                this.FP = this.Dl.r(this.Ey);
            }
            catch (VisaTAException visaTAException) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot retrieve token for MST");
                visaTAException.printStackTrace();
                return false;
            }
            if (this.FP == null || this.FP.length() <= 0) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "retrieved token for MST is null");
                return false;
            }
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "Token has been retrieved from storage for MST");
            try {
                boolean bl2;
                com.visa.tainterface.a a2 = new com.visa.tainterface.a();
                a2.N(this.Ey);
                this.Fw = 1 + this.Fw;
                Locale locale = Locale.US;
                Object[] arrobject = new Object[]{this.Fw};
                a2.cB(String.format((Locale)locale, (String)"%04d", (Object[])arrobject));
                Mst mst = this.Eu.getMst();
                a2.setCVV("000");
                a2.cA(com.samsung.android.visasdk.a.b.o(this.Fx).substring(6, 8));
                a2.setServiceCode(mst.getSvcCode());
                a2.cC("00000000");
                a2.setTimestamp(com.samsung.android.visasdk.a.b.o(this.Fx).substring(2, 6));
                a2.cz(this.Ez.substring(2, 4) + this.EA);
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "api=" + com.samsung.android.visasdk.a.b.o(this.Fx));
                if (El) {
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "token=" + com.samsung.android.visasdk.a.b.o(a2.ii()));
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "atc=" + com.samsung.android.visasdk.a.b.o(a2.in()));
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "cvv=" + com.samsung.android.visasdk.a.b.o(a2.io()));
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "cc=" + com.samsung.android.visasdk.a.b.o(a2.im()));
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "svccode=" + com.samsung.android.visasdk.a.b.o(a2.ik()));
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "timestamp=" + com.samsung.android.visasdk.a.b.o(a2.il()));
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "expiredate=" + com.samsung.android.visasdk.a.b.o(a2.ij()));
                    com.samsung.android.visasdk.c.a.e("VcpcsManager", "reservedBytes=" + com.samsung.android.visasdk.a.b.o(a2.ip()));
                }
                if (bl = (bl2 = this.Dl.a(this.Fz, a2))) break block8;
            }
            catch (VisaTAException visaTAException) {
                visaTAException.printStackTrace();
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot prepare MST data");
                return false;
            }
            return bl;
        }
        TVL tVL = new TVL();
        tVL.setTokenKey(this.FK);
        tVL.setUnpredictableNumber("");
        tVL.setTimeStamp(com.samsung.android.visasdk.a.b.fU());
        tVL.setTransactionType("S");
        tVL.setAtc(this.Fw);
        tVL.setApi(com.samsung.android.visasdk.a.b.o(this.Fx));
        if (this.FD == null) {
            this.FD = new ArrayList();
        }
        this.FD.add((Object)tVL);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void processInAppTransactionComplete(TokenKey tokenKey, String string, boolean bl) {
        com.samsung.android.visasdk.c.a.d("VcpcsManager", "processInAppTransactionComplete " + bl);
        if (bl) {
            if (this.Et != null) {
                TVL tVL = new TVL();
                tVL.setTokenKey(this.FK);
                tVL.setTimeStamp(com.samsung.android.visasdk.a.b.fU());
                tVL.setUnpredictableNumber("");
                tVL.setAtc(this.Fw);
                tVL.setApi(com.samsung.android.visasdk.a.b.o(this.Fx));
                tVL.setTransactionType("I");
                this.Et.a(tVL);
            } else {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "cannot access db");
            }
            this.FB = -1 + this.FB;
            if (this.Es == null) {
                com.samsung.android.visasdk.c.a.e("VcpcsManager", "visa db helper is null");
                return;
            }
            this.Es.a(tokenKey, this.Fw, this.FB);
        }
    }

    void setCvmVerificationMode(CvmMode cvmMode) {
        this.FW = cvmMode;
    }

    public boolean shouldTapAndGo(TokenKey tokenKey) {
        return this.Gd;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    ApduResponse u(byte[] arrby) {
        int n2;
        if (!this.s(arrby)) {
            return new ApduResponse(-12);
        }
        byte by = arrby[2];
        if (Em) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "updateTestParams() p1=" + by);
        }
        if (by == -128) {
            if (Em) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "multiple time perso --start");
            }
            n2 = this.t(arrby);
            do {
                return new ApduResponse(n2);
                break;
            } while (true);
        }
        if (by == 0) {
            if (Em) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "multiple time perso --continue");
            }
            int n3 = 255 & arrby[4];
            byte[] arrby2 = new byte[n3];
            com.samsung.android.visasdk.a.b.a(arrby, 5, arrby2, 0, n3);
            String string = new String(arrby2);
            this.Gf = this.Gf + string;
            boolean bl = Em;
            n2 = 0;
            if (!bl) return new ApduResponse(n2);
            {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", string);
                n2 = 0;
            }
            return new ApduResponse(n2);
        }
        if (by == 64) {
            if (Em) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "multiple time perso --end");
            }
            int n4 = 255 & arrby[4];
            byte[] arrby3 = new byte[n4];
            com.samsung.android.visasdk.a.b.a(arrby, 5, arrby3, 0, n4);
            String string = new String(arrby3);
            this.Gf = this.Gf + string;
            if (Em) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", string);
            }
            this.k(false);
            n2 = 0;
            return new ApduResponse(n2);
        }
        if (by == -64) {
            if (Em) {
                com.samsung.android.visasdk.c.a.d("VcpcsManager", "one time perso --update (one time)");
            }
            n2 = this.t(arrby);
            this.k(true);
            return new ApduResponse(n2);
        }
        if (Em) {
            com.samsung.android.visasdk.c.a.d("VcpcsManager", "multiple time perso --not supported cmd");
        }
        n2 = -12;
        return new ApduResponse(n2);
    }

    private class a {
        private byte[] Gl;
        private byte[] Gm;

        private a() {
        }

        public void I(byte[] arrby) {
            this.Gl = arrby;
        }

        public void J(byte[] arrby) {
            this.Gm = arrby;
        }

        public void clear() {
            com.samsung.android.visasdk.a.b.p(this.Gl);
            com.samsung.android.visasdk.a.b.p(this.Gm);
        }

        public byte[] go() {
            return this.Gl;
        }

        public byte[] gp() {
            return this.Gm;
        }
    }

    private class b {
        private byte[] Go;
        private byte[] signature;

        private b() {
        }

        public void K(byte[] arrby) {
            this.Go = arrby;
        }

        public void L(byte[] arrby) {
            this.signature = arrby;
        }

        public void clear() {
            com.samsung.android.visasdk.a.b.p(this.Go);
            com.samsung.android.visasdk.a.b.p(this.signature);
        }

        public byte[] getSignature() {
            return this.signature;
        }

        public byte[] gq() {
            return this.Go;
        }
    }

}

