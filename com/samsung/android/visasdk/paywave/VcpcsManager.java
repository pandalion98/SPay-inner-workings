package com.samsung.android.visasdk.paywave;

import android.content.Context;
import android.os.Bundle;
import com.americanexpress.mobilepayments.hceclient.context.ApplicationInfoManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mastercard.mcbp.core.mpplite.states.StatesConstants;
import com.mastercard.mobile_api.payment.cld.CardSide;
import com.mastercard.mobile_api.utils.apdu.emv.EMVGetResponse;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.GetTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PutTemplateApdu;
import com.mastercard.mobile_api.utils.apdu.emv.SetResetParamApdu;
import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.data.ApduResponse;
import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenStatus;
import com.samsung.android.visasdk.facade.data.TransactionError;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.p023a.Utils;
import com.samsung.android.visasdk.p023a.Version;
import com.samsung.android.visasdk.p024b.CryptoManager;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.paywave.Constant.TransactionType;
import com.samsung.android.visasdk.paywave.data.TVL;
import com.samsung.android.visasdk.paywave.model.AidInfo;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.EncICCPrivateKey;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.ICC;
import com.samsung.android.visasdk.paywave.model.ICCCRTPrivateKey;
import com.samsung.android.visasdk.paywave.model.Mst;
import com.samsung.android.visasdk.paywave.model.NFCReplenishData;
import com.samsung.android.visasdk.paywave.model.PaymentData;
import com.samsung.android.visasdk.paywave.model.PaymentRequest;
import com.samsung.android.visasdk.paywave.model.ProvisionedData;
import com.samsung.android.visasdk.paywave.model.QVSDCData;
import com.samsung.android.visasdk.paywave.model.StaticParams;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import com.samsung.android.visasdk.storage.DbEnhancedTokenInfoDao;
import com.samsung.android.visasdk.storage.DbTvlDao;
import com.visa.tainterface.TrackData;
import com.visa.tainterface.VisaTAException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.visasdk.paywave.a */
class VcpcsManager {
    public static final boolean Ei;
    public static final boolean Ej;
    public static final boolean Ek;
    public static final boolean El;
    public static final boolean Em;
    public static final boolean En;
    public static final boolean Eo;
    public static final boolean Ep;
    public static final boolean Eq;
    private static VcpcsManager Er;
    private CryptoManager Dl;
    private String EA;
    private List<AidInfo> EB;
    private int EC;
    private int ED;
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
    private DbEnhancedTokenInfoDao Es;
    private DbTvlDao Et;
    private TokenInfo Eu;
    private byte[] Ev;
    private byte[] Ew;
    private String Ex;
    private byte[] Ey;
    private String Ez;
    private long FA;
    private int FB;
    private int FC;
    private List<TVL> FD;
    private ApduResponse FE;
    private ApduResponse FF;
    private ApduResponse FG;
    private ApduResponse FH;
    private ApduResponse FI;
    private ApduResponse FJ;
    private TokenKey FK;
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
    private CvmMode FW;
    private TransactionType FX;
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
    private TransactionStatus Ge;
    private String Gf;
    private boolean Gg;
    private boolean Gh;
    private byte Gi;
    private Bundle Gj;

    /* renamed from: com.samsung.android.visasdk.paywave.a.1 */
    static /* synthetic */ class VcpcsManager {
        static final /* synthetic */ int[] Gk;

        static {
            Gk = new int[TransactionType.values().length];
            try {
                Gk[TransactionType.MSD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                Gk[TransactionType.QVSDC_NO_ODA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                Gk[TransactionType.QVSDC_WITH_ODA_1.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                Gk[TransactionType.QVSDC_WITH_ODA_2.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                Gk[TransactionType.QVSDC_WITH_ODA_3.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                Gk[TransactionType.UNKNOWN_TYPE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    /* renamed from: com.samsung.android.visasdk.paywave.a.a */
    private class VcpcsManager {
        private byte[] Gl;
        private byte[] Gm;
        final /* synthetic */ VcpcsManager Gn;

        private VcpcsManager(VcpcsManager vcpcsManager) {
            this.Gn = vcpcsManager;
        }

        public byte[] go() {
            return this.Gl;
        }

        public void m1307I(byte[] bArr) {
            this.Gl = bArr;
        }

        public byte[] gp() {
            return this.Gm;
        }

        public void m1308J(byte[] bArr) {
            this.Gm = bArr;
        }

        public void clear() {
            Utils.m1286p(this.Gl);
            Utils.m1286p(this.Gm);
        }
    }

    /* renamed from: com.samsung.android.visasdk.paywave.a.b */
    private class VcpcsManager {
        final /* synthetic */ VcpcsManager Gn;
        private byte[] Go;
        private byte[] signature;

        private VcpcsManager(VcpcsManager vcpcsManager) {
            this.Gn = vcpcsManager;
        }

        public byte[] gq() {
            return this.Go;
        }

        public void m1309K(byte[] bArr) {
            this.Go = bArr;
        }

        public byte[] getSignature() {
            return this.signature;
        }

        public void m1310L(byte[] bArr) {
            this.signature = bArr;
        }

        public void clear() {
            Utils.m1286p(this.Go);
            Utils.m1286p(this.signature);
        }
    }

    static {
        Ei = Version.LOG_DEBUG & 1;
        Ej = Version.LOG_DEBUG & 1;
        Ek = Version.LOG_DEBUG & 1;
        El = Version.LOG_DEBUG & 1;
        Em = Version.LOG_DEBUG & 1;
        En = Version.LOG_DEBUG & 1;
        Eo = Version.LOG_DEBUG & 1;
        Ep = Version.LOG_DEBUG & 1;
        Eq = Version.LOG_DEBUG & 1;
    }

    static synchronized VcpcsManager m1317a(Context context, CryptoManager cryptoManager, Bundle bundle) {
        VcpcsManager vcpcsManager;
        synchronized (VcpcsManager.class) {
            if (Er == null) {
                Er = new VcpcsManager(context, cryptoManager, bundle);
                if (Er == null) {
                    throw new InitializationException("cannot initialize manager");
                }
            }
            vcpcsManager = Er;
        }
        return vcpcsManager;
    }

    private VcpcsManager(Context context, CryptoManager cryptoManager, Bundle bundle) {
        this.EB = new ArrayList();
        this.EC = 0;
        this.ED = -1;
        this.FD = new ArrayList();
        this.FK = null;
        this.FW = Constant.DK;
        this.Ge = new TransactionStatus(TransactionError.NO_ERROR, false);
        this.Gg = false;
        this.Gh = false;
        this.Gi = (byte) 0;
        if (cryptoManager == null) {
            throw new InitializationException("cryptoManager is null");
        }
        this.Dl = cryptoManager;
        this.Gj = bundle;
        this.Es = new DbEnhancedTokenInfoDao(context);
        if (this.Es == null) {
            throw new InitializationException("cannot access db");
        }
        this.Et = new DbTvlDao(context);
        if (this.Et == null) {
            throw new InitializationException("cannot access db");
        }
    }

    private static boolean m1326l(byte[] bArr, byte[] bArr2) {
        return Utils.m1284g(bArr, Constant.DM) && Utils.m1284g(bArr2, Constant.DN);
    }

    private void m1319a(TokenInfo tokenInfo) {
        if (Version.LOG_DEBUG) {
            Log.m1300d("VcpcsManager", "=========== print token info starts =============");
            Log.m1300d("VcpcsManager", "--TokenInfo-------------------------------");
            Log.m1300d("VcpcsManager", "encTokenInfo=" + tokenInfo.getEncTokenInfo());
            Log.m1300d("VcpcsManager", "expiryDate.month=" + tokenInfo.getExpirationDate().getMonth());
            Log.m1300d("VcpcsManager", "expiryDate.year=" + tokenInfo.getExpirationDate().getYear());
            Log.m1300d("VcpcsManager", "appPrgrmID=" + tokenInfo.getAppPrgrmID());
            Log.m1300d("VcpcsManager", "lang=" + tokenInfo.getLang());
            Log.m1300d("VcpcsManager", "last4=" + tokenInfo.getLast4());
            Log.m1300d("VcpcsManager", "tokenReferenceID=" + tokenInfo.getTokenReferenceID());
            Log.m1300d("VcpcsManager", "tokenRequestorID=" + tokenInfo.getTokenRequestorID());
            Log.m1300d("VcpcsManager", "tokenStatus=" + tokenInfo.getTokenStatus());
            Log.m1300d("VcpcsManager", "---------DynParams------------------------");
            DynParams dynParams = tokenInfo.getHceData().getDynParams();
            Log.m1300d("VcpcsManager", "api=" + dynParams.getApi());
            Log.m1300d("VcpcsManager", "dki=" + dynParams.getDki());
            Log.m1300d("VcpcsManager", "encKeyinfo=" + dynParams.getEncKeyInfo());
            Log.m1300d("VcpcsManager", "keyExpTS=" + dynParams.getKeyExpTS());
            Log.m1300d("VcpcsManager", "maxPmts=" + dynParams.getMaxPmts());
            Log.m1300d("VcpcsManager", "sc=" + dynParams.getSc());
            Log.m1300d("VcpcsManager", "---------StaticParams(common)-------------");
            StaticParams staticParams = tokenInfo.getHceData().getStaticParams();
            int size = staticParams.getAidInfo().size();
            Log.m1300d("VcpcsManager", "token Aids number: " + size + "{");
            for (int i = 0; i < size; i++) {
                AidInfo aidInfo = (AidInfo) staticParams.getAidInfo().get(i);
                Log.m1300d("VcpcsManager", "  aid=" + aidInfo.getAid());
                Log.m1300d("VcpcsManager", "  applicationLabel=" + aidInfo.getApplicationLabel());
                Log.m1300d("VcpcsManager", "  cap=" + aidInfo.getCap());
                Log.m1300d("VcpcsManager", "  priority=" + aidInfo.getPriority());
                Log.m1300d("VcpcsManager", "  CVMrequired=" + aidInfo.getCVMrequired());
            }
            Log.m1300d("VcpcsManager", "}");
            Log.m1300d("VcpcsManager", "cardHolderNameVCPCS=" + staticParams.getCardHolderNameVCPCS());
            Log.m1300d("VcpcsManager", "countryCode5F55=" + staticParams.getCountryCode5F55());
            Log.m1300d("VcpcsManager", "issuerIdentificationNumber=" + staticParams.getIssuerIdentificationNumber());
            Log.m1300d("VcpcsManager", "kernelIdentifier=" + staticParams.getKernelIdentifier());
            Log.m1300d("VcpcsManager", "pdol=" + staticParams.getPdol());
            if (tokenInfo.getMst() != null) {
                Log.m1300d("VcpcsManager", "---------------StaticParams(MST)----------");
                Log.m1300d("VcpcsManager", "mstSvcCode=" + tokenInfo.getMst().getSvcCode());
                Log.m1300d("VcpcsManager", "mstCVV=" + tokenInfo.getMst().getCvv());
            }
            if (staticParams.getMsdData() != null) {
                Log.m1300d("VcpcsManager", "---------------StaticParams(MSD)----------");
                Log.m1300d("VcpcsManager", "afl(MSD)=" + staticParams.getMsdData().getAfl());
                Log.m1300d("VcpcsManager", "aip(MSD)=" + staticParams.getMsdData().getAip());
            }
            QVSDCData qVSDCData = staticParams.getQVSDCData();
            if (qVSDCData != null) {
                Log.m1300d("VcpcsManager", "---------------StaticParams(QVSDC)--------");
                Log.m1300d("VcpcsManager", "auc=" + qVSDCData.getAuc());
                Log.m1300d("VcpcsManager", "ced=" + qVSDCData.getCed());
                Log.m1300d("VcpcsManager", "cid=" + qVSDCData.getCid());
                Log.m1300d("VcpcsManager", "countryCode=" + qVSDCData.getCountryCode());
                Log.m1300d("VcpcsManager", "ctq=" + qVSDCData.getCtq());
                Log.m1300d("VcpcsManager", "cvn=" + qVSDCData.getCvn());
                Log.m1300d("VcpcsManager", "digitalWalletId=" + qVSDCData.getDigitalWalletID());
                Log.m1300d("VcpcsManager", "ffi=" + qVSDCData.getFfi());
                Log.m1300d("VcpcsManager", "psn=" + qVSDCData.getPsn());
                if (qVSDCData.getQVSDCWithoutODA() != null) {
                    Log.m1300d("VcpcsManager", "------------------StaticParams(QVSDCnoODA)");
                    Log.m1300d("VcpcsManager", "afl(noODA)=" + qVSDCData.getQVSDCWithoutODA().getAfl());
                    Log.m1300d("VcpcsManager", "aip(noODA)=" + qVSDCData.getQVSDCWithoutODA().getAip());
                }
                if (qVSDCData.getqVSDCWithODA() != null) {
                    Log.m1300d("VcpcsManager", "------------------StaticParams(QVSDCwithODA)");
                    Log.m1300d("VcpcsManager", "afl=" + qVSDCData.getqVSDCWithODA().getAfl());
                    Log.m1300d("VcpcsManager", "aip=" + qVSDCData.getqVSDCWithODA().getAip());
                    Log.m1300d("VcpcsManager", "appExpDate=" + qVSDCData.getqVSDCWithODA().getAppExpDate());
                    Log.m1300d("VcpcsManager", "capki=" + qVSDCData.getqVSDCWithODA().getCapki());
                    Log.m1300d("VcpcsManager", "cardAuthData=" + qVSDCData.getqVSDCWithODA().getCardAuthData());
                    Log.m1300d("VcpcsManager", "iPubKCert=" + qVSDCData.getqVSDCWithODA().getIPubkCert());
                    Log.m1300d("VcpcsManager", "iPubKExpo=" + qVSDCData.getqVSDCWithODA().getIPubkExpo());
                    Log.m1300d("VcpcsManager", "iPubKeyRem=" + qVSDCData.getqVSDCWithODA().getIPubkRem());
                    Log.m1300d("VcpcsManager", "icc=" + qVSDCData.getqVSDCWithODA().getIcc());
                    Log.m1300d("VcpcsManager", "sdad=" + qVSDCData.getqVSDCWithODA().getSdad());
                }
            }
            if (staticParams.getTrack2DataDec() != null) {
                Log.m1300d("VcpcsManager", "---------------Track2(Decimalize)-------");
                Log.m1300d("VcpcsManager", BuildConfig.FLAVOR + staticParams.getTrack2DataDec());
            }
            if (staticParams.getTrack2DataNotDec() != null) {
                Log.m1300d("VcpcsManager", "---------------Track2(NotDecimalize)---------");
                Log.m1300d("VcpcsManager", BuildConfig.FLAVOR + staticParams.getTrack2DataNotDec());
            }
            Log.m1300d("VcpcsManager", "=========== print token info ends =============");
        }
    }

    boolean fW() {
        return this.Gg;
    }

    void m1344j(boolean z) {
        this.Gg = z;
    }

    private static byte[] m1322a(ByteBuffer byteBuffer, int i) {
        if (byteBuffer == null || i <= 0) {
            return Constant.Dy;
        }
        byteBuffer.putShort(ISO7816.SW_NO_ERROR);
        byte[] bArr = new byte[(i + 2)];
        byteBuffer.position(0);
        byteBuffer.get(bArr, 0, bArr.length);
        return bArr;
    }

    private static void m1323c(int i, byte[] bArr) {
        if (bArr != null && bArr.length >= 2) {
            bArr[1] = (byte) (i % 16);
            int i2 = i / 16;
            bArr[1] = (byte) (bArr[1] + (((byte) (i2 % 16)) * 16));
            i2 /= 16;
            bArr[0] = (byte) (i2 % 16);
            bArr[0] = (byte) ((((byte) ((i2 / 16) % 16)) * 16) + bArr[0]);
        }
    }

    private static byte[] m1324c(byte[] bArr, int i) {
        int i2 = 0;
        if (bArr.length != i) {
            return null;
        }
        byte[] bArr2 = new byte[3];
        String o = Utils.m1285o(bArr);
        int length = o.length();
        int i3 = 0;
        short s = (short) 0;
        while (i3 < length && s < (short) 3) {
            short s2;
            if (LLVARUtil.EMPTY_STRING > o.charAt(i3) || o.charAt(i3) > '9') {
                s2 = s;
            } else {
                s2 = (short) (s + 1);
                bArr2[s] = (byte) o.charAt(i3);
            }
            short s3 = (short) (i3 + 1);
            s = s2;
        }
        while (i2 < length && s < (short) 3) {
            if ('A' <= o.charAt(i2) && o.charAt(i2) <= Matrix.MATRIX_TYPE_ZERO) {
                s2 = (short) (s + 1);
                bArr2[s] = (byte) (((o.charAt(i2) - 65) - 10) + 48);
            } else if ('a' > o.charAt(i2) || o.charAt(i2) > 'z') {
                s2 = s;
            } else {
                s2 = (short) (s + 1);
                bArr2[s] = (byte) (((o.charAt(i2) - 97) - 10) + 48);
            }
            short s4 = (short) (i2 + 1);
            s = s2;
        }
        return bArr2;
    }

    private int m1315a(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        Log.m1300d("VcpcsManager", "build_qVSDC_CVM()");
        if (bArr.length != 4 || bArr2.length != 4 || bArr3.length != 6 || bArr4.length != 2) {
            return -5;
        }
        bArr4[0] = Utils.m1279a(bArr4[0], 7, 0);
        bArr4[0] = Utils.m1279a(bArr4[0], 8, 0);
        String cVMrequired = ((AidInfo) this.EB.get(this.ED)).getCVMrequired();
        int i = (ActivationData.YES.equals(cVMrequired) || "y".equals(cVMrequired)) ? 1 : 0;
        Log.m1300d("VcpcsManager", "build_qVSDC_CVM(), Cap=" + Utils.m1285o(bArr2) + ", TTQ=" + Utils.m1285o(bArr) + ", CVMRequired=" + cVMrequired);
        if (Utils.isBitSet(bArr[1], 7) || i != 0) {
            if (i != 0) {
                Log.m1300d("VcpcsManager", "cvm flag is set");
            }
            if (Utils.isBitSet(bArr[0], 3) && (Utils.isBitSet(bArr2[2], 8) || Utils.isBitSet(bArr2[2], 7))) {
                if (Ek) {
                    Log.m1300d("VcpcsManager", "build_qVSDC_CVM online pin");
                }
                bArr4[0] = Utils.m1279a(bArr4[0], 8, 1);
                bArr3[0] = (byte) 110;
            } else if (Utils.isBitSet(bArr[2], 7) && Utils.isBitSet(bArr2[2], 4)) {
                if (Ek) {
                    Log.m1300d("VcpcsManager", "build_qVSDC_CVM CDCVM");
                }
                if (fW()) {
                    bArr4[1] = Utils.m1279a(bArr4[1], 8, 1);
                    if (this.FW == null) {
                        return -6;
                    }
                    bArr3[0] = this.FW.getCvmByte();
                } else {
                    if (Ek) {
                        Log.m1300d("VcpcsManager", "cdcvm not verified");
                    }
                    return -6;
                }
            } else if (Utils.isBitSet(bArr[0], 2) && Utils.isBitSet(bArr2[2], 5)) {
                if (Ek) {
                    Log.m1300d("VcpcsManager", "build_qVSDC_CVM signature");
                }
                bArr4[0] = Utils.m1279a(bArr4[0], 7, 1);
                bArr3[0] = (byte) 109;
            } else if (i != 0) {
                Log.m1300d("VcpcsManager", "error for brazil electron card");
                return -5;
            } else {
                if (Ek) {
                    Log.m1300d("VcpcsManager", "build_qVSDC_CVM not required");
                }
                bArr4[1] = Utils.m1279a(bArr4[1], 8, 1);
                bArr3[0] = (byte) 0;
            }
        } else {
            if (Ek) {
                Log.m1300d("VcpcsManager", "build_qVSDC_CVM not required, too");
            }
            bArr4[1] = Utils.m1279a(bArr4[1], 8, 1);
            bArr3[0] = (byte) 0;
        }
        bArr3[1] = Utils.m1279a(bArr3[1], 8, 0);
        bArr3[1] = Utils.m1279a(bArr3[1], 7, 0);
        bArr3[1] = Utils.m1279a(bArr3[1], 6, 1);
        bArr3[1] = Utils.m1279a(bArr3[1], 5, 0);
        if (this.Gh) {
            bArr3[2] = Utils.m1279a(bArr3[2], 6, 1);
            if (Ek) {
                Log.m1300d("VcpcsManager", "update CVRexceededVelocity to " + bArr3[2]);
            }
        }
        bArr3[4] = this.Gi;
        if (Ek) {
            Utils.m1283e("build_qVSDC_CVM CVR=", bArr3);
        }
        if (!Ek) {
            return 0;
        }
        Utils.m1283e("build_qVSDC_CVM CTQ=", bArr4);
        return 0;
    }

    private short m1327m(byte[] bArr, byte[] bArr2) {
        Log.m1300d("VcpcsManager", "buildIADFormat()");
        if (bArr2.length == 32 && bArr.length == 6) {
            int length = this.EQ.length;
            int length2 = this.Fy.length;
            int length3 = this.ER.length;
            int length4 = this.Fx.length;
            if (length != 1 || length2 != 1 || bArr.length != 6 || length3 != 4 || length4 != 4) {
                Log.m1301e("VcpcsManager", "buildIADFormat(): provison data error");
                return (short) -4;
            } else if (((((((length + 1) + length2) + bArr.length) + length3) + length4) + 1) + 14 != bArr2.length) {
                return (short) -1;
            } else {
                short s = (short) 1;
                bArr2[0] = (byte) 31;
                short s2 = (short) (s + 1);
                bArr2[s] = this.EQ[0];
                s = (short) (s2 + 1);
                bArr2[s2] = this.Fy[0];
                Utils.m1281a(bArr, 0, bArr2, s, bArr.length);
                s = (short) (s + bArr.length);
                Utils.m1281a(this.ER, 0, bArr2, s, length3);
                s = (short) (s + length3);
                Utils.m1281a(this.Fx, 0, bArr2, s, length4);
                s = (short) (s + length4);
                s2 = (short) (s + 1);
                bArr2[s] = (byte) 0;
                Utils.m1280a(bArr2, s2, (byte) 0, 14);
                s = (short) (s2 + 14);
                if (s != bArr2.length) {
                    return (short) -4;
                }
                return s;
            }
        }
        Log.m1301e("VcpcsManager", "IAD format buffer or CVR length is incorrect");
        return (short) -4;
    }

    private boolean m1330s(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        short length = bArr.length;
        if (length <= (short) 0 || length > SkeinMac.SKEIN_256 || bArr[0] != -128 || bArr[1] != -17) {
            return false;
        }
        short s = (short) (5 + (bArr[4] & GF2Field.MASK));
        if ((s + 1 == length && bArr[s] == null) || s == length) {
            return true;
        }
        return false;
    }

    private short m1331t(byte[] bArr) {
        this.Gf = BuildConfig.FLAVOR;
        int i = bArr[4] & GF2Field.MASK;
        int i2 = bArr[6] & GF2Field.MASK;
        byte[] bArr2 = new byte[i2];
        Utils.m1281a(bArr, 7, bArr2, 0, i2);
        String str = new String(bArr2);
        if (!Constant.Dp.equals(str)) {
            Log.m1301e("VcpcsManager", "test account number is invalid: " + str);
        }
        int i3 = 7 + i2;
        i = (i - i2) - 2;
        byte[] bArr3 = new byte[i];
        Utils.m1281a(bArr, i3, bArr3, 0, i);
        String str2 = new String(bArr3);
        this.Gf += str2;
        if (Ei) {
            Log.m1300d("VcpcsManager", BuildConfig.FLAVOR + str2);
        }
        return (short) 0;
    }

    ApduResponse m1345u(byte[] bArr) {
        int i = 0;
        if (!m1330s(bArr)) {
            return new ApduResponse(-12);
        }
        byte b = bArr[2];
        if (Em) {
            Log.m1300d("VcpcsManager", "updateTestParams() p1=" + b);
        }
        if (b == -128) {
            if (Em) {
                Log.m1300d("VcpcsManager", "multiple time perso --start");
            }
            i = m1331t(bArr);
        } else if (b == null) {
            if (Em) {
                Log.m1300d("VcpcsManager", "multiple time perso --continue");
            }
            r1 = bArr[4] & GF2Field.MASK;
            r2 = new byte[r1];
            Utils.m1281a(bArr, 5, r2, 0, r1);
            r1 = new String(r2);
            this.Gf += r1;
            if (Em) {
                Log.m1300d("VcpcsManager", r1);
            }
        } else if (b == 64) {
            if (Em) {
                Log.m1300d("VcpcsManager", "multiple time perso --end");
            }
            r1 = bArr[4] & GF2Field.MASK;
            r2 = new byte[r1];
            Utils.m1281a(bArr, 5, r2, 0, r1);
            r1 = new String(r2);
            this.Gf += r1;
            if (Em) {
                Log.m1300d("VcpcsManager", r1);
            }
            m1325k(false);
        } else if (b == -64) {
            if (Em) {
                Log.m1300d("VcpcsManager", "one time perso --update (one time)");
            }
            i = m1331t(bArr);
            m1325k(true);
        } else {
            if (Em) {
                Log.m1300d("VcpcsManager", "multiple time perso --not supported cmd");
            }
            i = -12;
        }
        return new ApduResponse(i);
    }

    private boolean m1320a(NFCReplenishData nFCReplenishData) {
        if (nFCReplenishData == null) {
            return false;
        }
        if (nFCReplenishData.getEncKeyInfo() != null && nFCReplenishData.getEncKeyInfo().length() > 0) {
            try {
                this.Fz = Utils.hexStringToBytes(this.Dl.m1295a((byte) CardSide.ALWAYS_TEXT_TAG, nFCReplenishData.getEncKeyInfo()));
            } catch (VisaTAException e) {
                return false;
            }
        }
        if (nFCReplenishData.getEncTokenInfo() != null && nFCReplenishData.getEncTokenInfo().length() > 0) {
            try {
                this.Ey = Utils.hexStringToBytes(this.Dl.m1295a((byte) CardSide.PIN_TEXT_TAG, nFCReplenishData.getEncTokenInfo()));
            } catch (VisaTAException e2) {
                return false;
            }
        }
        if (nFCReplenishData.getATC() != null && nFCReplenishData.getATC().length() > 0) {
            try {
                this.Fw = Integer.parseInt(nFCReplenishData.getATC());
            } catch (NumberFormatException e3) {
            }
        }
        if (ActivationData.YES.equals(nFCReplenishData.getReadyPayState())) {
            this.Gc = true;
        } else {
            this.Gc = false;
        }
        if (ActivationData.YES.equals(nFCReplenishData.getCDCVMverified())) {
            m1344j(true);
        } else {
            m1344j(false);
        }
        if (nFCReplenishData.getApi() != null && nFCReplenishData.getApi().length() > 0) {
            this.Fx = Utils.hexStringToBytes(nFCReplenishData.getApi());
        }
        if (ActivationData.YES.equals(nFCReplenishData.getCVRexceededVelocity())) {
            this.Gh = true;
        } else {
            this.Gh = false;
        }
        if (nFCReplenishData.getConsumerDeviceState() != null && nFCReplenishData.getConsumerDeviceState().length() == 2) {
            this.Gi = (byte) (Integer.parseInt(nFCReplenishData.getConsumerDeviceState(), 16) & GF2Field.MASK);
        }
        try {
            gh();
            return true;
        } catch (VisaTAException e4) {
            e4.printStackTrace();
            return false;
        }
    }

    private void m1325k(boolean z) {
        Log.m1300d("VcpcsManager", this.Gf);
        Gson gson = new Gson();
        if (z) {
            if (Em) {
                Log.m1300d("VcpcsManager", "update selected card through NFC");
            }
            m1320a((NFCReplenishData) gson.fromJson(this.Gf, NFCReplenishData.class));
            return;
        }
        if (Em) {
            Log.m1300d("VcpcsManager", "select card from NFC VTS blob");
        }
        ProvisionedData provisionedData = (ProvisionedData) gson.fromJson(this.Gf, ProvisionedData.class);
        TokenKey tokenKey = new TokenKey(0);
        TokenInfo tokenInfo = provisionedData.getTokenInfo();
        if (tokenInfo == null || tokenInfo.getHceData() == null || tokenInfo.getHceData().getDynParams() == null) {
            Log.m1301e("VcpcsManager", "token info is null");
            return;
        }
        DynParams dynParams = tokenInfo.getHceData().getDynParams();
        if (dynParams.getEncKeyInfo() == null || dynParams.getEncKeyInfo().length() <= 0) {
            Log.m1301e("VcpcsManager", "cannot store VTS data, no card is selected");
        } else if (dynParams.getEncKeyInfo().length() > HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
            Log.m1301e("VcpcsManager", "encKeyInfo size is too large");
        } else {
            try {
                dynParams.setEncKeyInfo(this.Dl.m1295a((byte) CardSide.ALWAYS_TEXT_TAG, dynParams.getEncKeyInfo()));
                if (tokenInfo.getEncTokenInfo().length() > HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
                    Log.m1301e("VcpcsManager", "encTokenInfo size is too large");
                    return;
                }
                try {
                    tokenInfo.setEncTokenInfo(this.Dl.m1295a((byte) CardSide.PIN_TEXT_TAG, tokenInfo.getEncTokenInfo()));
                } catch (VisaTAException e) {
                    e.printStackTrace();
                    Log.m1301e("VcpcsManager", "cannot get token to parse NFC VTS blob");
                }
                StaticParams staticParams = tokenInfo.getHceData().getStaticParams();
                if (!(staticParams.getQVSDCData().getqVSDCWithODA() == null || staticParams.getQVSDCData().getqVSDCWithODA().getIcc() == null)) {
                    try {
                        m1318a(staticParams.getQVSDCData().getqVSDCWithODA().getIcc());
                    } catch (VisaTAException e2) {
                        e2.printStackTrace();
                        Log.m1301e("VcpcsManager", "cannot get token to parse NFC VTS blob");
                    }
                }
                PaymentData paymentData = new PaymentData();
                paymentData.setTokenInfo(tokenInfo);
                paymentData.setAtc(0);
                paymentData.setLukUseCount(0);
                m1316a(tokenKey, paymentData);
            } catch (VisaTAException e22) {
                e22.printStackTrace();
                Log.m1301e("VcpcsManager", "cannot parse NFC VTS blob");
            }
        }
    }

    private byte[] m1332v(byte[] bArr) {
        Log.m1300d("VcpcsManager", "constructTrack2withMSDVerificationValue()");
        this.FP = this.Dl.m1299r(this.Ey);
        if (this.FP == null || this.FP.length() <= 0) {
            Log.m1301e("VcpcsManager", "constructTrack2withMSDVerificationValue(), dec token is null");
            return null;
        }
        if (Ei) {
            Log.m1300d("VcpcsManager", "Token has been retrieved from storage");
        }
        String str = (((this.FP + 'D') + this.Ez.substring(2, 4)) + this.EA) + this.Fq;
        String o = Utils.m1285o(this.Fx);
        if (o == null || o.length() != 8) {
            Log.m1301e("VcpcsManager", "API is null");
            return null;
        }
        str = str + o.substring(2, 8);
        r2 = new byte[4];
        int i = this.Fw % 10000;
        r2[3] = (byte) ((i % 10) + 48);
        i /= 10;
        r2[2] = (byte) ((i % 10) + 48);
        i /= 10;
        r2[1] = (byte) ((i % 10) + 48);
        r2[0] = (byte) (((i / 10) % 10) + 48);
        str = str + new String(r2);
        if (bArr == null || bArr.length != 8) {
            Log.m1301e("VcpcsManager", "generated msdVV is null");
            return null;
        }
        r2 = VcpcsManager.m1324c(bArr, 8);
        if (r2 == null) {
            return null;
        }
        String str2 = str + new String(r2);
        if (str2.length() % 2 != 0) {
            str2 = str2 + 'F';
            if (El) {
                Log.m1301e("VcpcsManager", "PADDING F... at " + str2.length());
            }
        }
        if (El) {
            Log.m1300d("VcpcsManager", "constructed track2 data:" + str2);
        }
        return Utils.hexStringToBytes(str2);
    }

    private byte[] fX() {
        Log.m1300d("VcpcsManager", "constructTrack2noMSDVerificationValue()");
        this.FP = this.Dl.m1299r(this.Ey);
        if (this.FP == null || this.FP.length() <= 0) {
            Log.m1301e("VcpcsManager", "constructTrack2withMSDVerificationValue(), dec token is null");
            return null;
        }
        if (Ei) {
            Log.m1300d("VcpcsManager", "Token has been retrieved from storage");
        }
        String str = (((this.FP + 'D') + this.Ez.substring(2, 4)) + this.EA) + this.Fr;
        if (this.Fs.length() == 5) {
            str = str + this.Fs;
            if (this.Ft.length() > 38) {
                Log.m1301e("VcpcsManager", "provision data error: track2 discretionary data length is invalid");
                return null;
            }
            String str2 = str + this.Ft;
            if (str2.length() % 2 != 0) {
                str2 = str2 + 'F';
                if (El) {
                    Log.m1300d("VcpcsManager", "PADDING F... at " + str2.length());
                }
            }
            if (El) {
                Log.m1300d("VcpcsManager", "constructed track2 data: " + str2);
            }
            return Utils.hexStringToBytes(str2);
        }
        Log.m1301e("VcpcsManager", "provision data error: pin verify filed length is ignored");
        return null;
    }

    private boolean m1333w(byte[] bArr) {
        if (bArr != null && bArr.length == 5 && bArr[0] == -128 && bArr[1] == -54 && bArr[2] == -97 && bArr[3] == 8 && bArr[4] == null) {
            return true;
        }
        return false;
    }

    private boolean m1334x(byte[] bArr) {
        int length = bArr.length;
        if (((bArr[4] & GF2Field.MASK) + 5) + 1 != length || length > SkeinMac.SKEIN_256 || bArr[0] != null || bArr[1] != -92 || bArr[2] != (byte) 4 || (bArr[4] & GF2Field.MASK) < 5 || (bArr[4] & GF2Field.MASK) > 16) {
            return false;
        }
        short s = (short) (bArr[4] & GF2Field.MASK);
        String o = Utils.m1285o(Utils.m1282d(bArr, 5, s));
        if (En) {
            Log.m1300d("VcpcsManager", "selectedAid=" + o);
        }
        if (o == null || this.EC < 1 || this.EC > 2) {
            return false;
        }
        int i;
        int i2;
        if (bArr[3] == null) {
            for (i = 0; i < this.EC; i++) {
                if (o.equals(((AidInfo) this.EB.get(i)).getAid())) {
                    i2 = i;
                    break;
                }
            }
        }
        i2 = -1;
        if (i2 < 0 && s < (short) 7) {
            if (bArr[3] == null) {
                this.ED = -1;
            }
            String aid;
            if (this.ED < 0) {
                i = 0;
                while (i < this.EC) {
                    aid = ((AidInfo) this.EB.get(i)).getAid();
                    if (aid != null && aid.startsWith(o)) {
                        break;
                    }
                    i++;
                }
                i = i2;
                i2 = i;
            } else if (this.EC == 1 || this.ED == 1) {
                return false;
            } else {
                aid = ((AidInfo) this.EB.get(1)).getAid();
                if (aid != null && aid.startsWith(o)) {
                    i2 = 1;
                }
            }
        }
        this.ED = i2;
        if (this.ED < 0) {
            return false;
        }
        if (En) {
            if (this.ED < 0 || this.ED > 1) {
                Log.m1301e("VcpcsManager", "no AID is selected");
            } else {
                Log.m1300d("VcpcsManager", "sActiveAid=" + ((AidInfo) this.EB.get(this.ED)).getAid());
            }
        }
        int i3 = 5 + s;
        if (bArr[i3] == null && i3 + 1 == length) {
            return true;
        }
        return false;
    }

    private boolean m1335y(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        short length = bArr.length;
        if (bArr[0] != -128 || bArr[1] != -88 || bArr[2] != null || bArr[3] != null) {
            return false;
        }
        if (gn()) {
            if (!(bArr[6] == 65 && bArr[4] == 67)) {
                return false;
            }
        } else if (bArr[6] != 33) {
            return false;
        } else {
            if (bArr[4] != 35) {
                return false;
            }
        }
        if (bArr[5] != -125) {
            return false;
        }
        short s = (short) (bArr[4] + 5);
        if (bArr[s] == null && ((short) (s + 1)) == length) {
            return true;
        }
        return false;
    }

    private boolean m1336z(byte[] bArr) {
        if (bArr != null && bArr[0] == null && bArr[1] == -78) {
            return true;
        }
        return false;
    }

    private ApduResponse fY() {
        byte[] hexStringToBytes;
        Log.m1300d("VcpcsManager", "constructSelectPPSE()");
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put(MCFCITemplate.TAG_FCI_TEMPLATE);
        int position = allocate.position();
        allocate.put((byte) 0);
        allocate.put(PinChangeUnblockApdu.CLA);
        allocate.put((byte) Constant.Ds.length);
        allocate.put(Constant.Ds);
        allocate.put(PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG);
        int position2 = allocate.position();
        allocate.put((byte) 0);
        allocate.putShort(GetTemplateApdu.TAG_FCI_ISSUER_DISCRETIONARY_DATA_BF0C);
        int position3 = allocate.position();
        allocate.put((byte) 0);
        if (this.Gc) {
            AidInfo aidInfo;
            int position4;
            if (this.EC == 1) {
                aidInfo = (AidInfo) this.EB.get(0);
                if (En) {
                    Log.m1300d("VcpcsManager", "appLabel=" + aidInfo.getApplicationLabel());
                }
                allocate.put(PutTemplateApdu.DIRECTORY_TEMPLATE_TAG);
                position4 = allocate.position();
                allocate.put((byte) 0);
                allocate.put(GetTemplateApdu.TAG_DF_NAME_4F);
                byte[] hexStringToBytes2 = Utils.hexStringToBytes(aidInfo.getAid());
                allocate.put((byte) hexStringToBytes2.length);
                allocate.put(hexStringToBytes2);
                if (aidInfo.getApplicationLabel().length() > 0) {
                    allocate.put(GetTemplateApdu.TAG_APPLICATION_LABEL_50);
                    hexStringToBytes = Utils.hexStringToBytes(aidInfo.getApplicationLabel());
                    allocate.put((byte) hexStringToBytes.length);
                    allocate.put(hexStringToBytes);
                }
                allocate.put(position4, (byte) ((allocate.position() - position4) - 1));
            } else if (this.EC == 2) {
                for (position4 = 0; position4 < this.EC; short s = (short) (position4 + 1)) {
                    aidInfo = (AidInfo) this.EB.get(position4);
                    if (En) {
                        Log.m1300d("VcpcsManager", "appLabel=" + aidInfo.getApplicationLabel());
                    }
                    allocate.put(PutTemplateApdu.DIRECTORY_TEMPLATE_TAG);
                    int position5 = allocate.position();
                    allocate.put((byte) 0);
                    allocate.put(GetTemplateApdu.TAG_DF_NAME_4F);
                    byte[] hexStringToBytes3 = Utils.hexStringToBytes(aidInfo.getAid());
                    allocate.put((byte) hexStringToBytes3.length);
                    allocate.put(hexStringToBytes3);
                    if (aidInfo.getApplicationLabel().length() > 0) {
                        allocate.put(GetTemplateApdu.TAG_APPLICATION_LABEL_50);
                        hexStringToBytes3 = Utils.hexStringToBytes(aidInfo.getApplicationLabel());
                        allocate.put((byte) hexStringToBytes3.length);
                        allocate.put(hexStringToBytes3);
                    }
                    allocate.put(GetTemplateApdu.TAG_PRIORITY_INDICATOR_87);
                    allocate.put((byte) 1);
                    allocate.put(Byte.parseByte(aidInfo.getPriority()));
                    allocate.putShort(PutTemplateApdu.KERNEL_IDENTIFIER_TAG);
                    allocate.put((byte) this.EH.length);
                    allocate.put(this.EH);
                    if (this.EG != null && this.EG.length > 0) {
                        allocate.put(MCFCITemplate.TAG_FCI_ISSUER_IIN);
                        allocate.put((byte) this.EG.length);
                        allocate.put(this.EG);
                    }
                    if (this.EF != null && this.EF.length > 0) {
                        allocate.putShort((short) 24405);
                        allocate.put((byte) this.EF.length);
                        allocate.put(this.EF);
                    } else if (this.FL) {
                        allocate.putShort((short) 24405);
                        allocate.put((byte) 2);
                        allocate.putShort((short) 21843);
                    }
                    allocate.put(position5, (byte) ((allocate.position() - position5) - 1));
                }
            } else if (this.EC > 0 && this.EC >= 3) {
            }
        }
        allocate.put(position3, (byte) ((allocate.position() - position3) - 1));
        allocate.put(position2, (byte) ((allocate.position() - position2) - 1));
        allocate.put(position, (byte) ((allocate.position() - position) - 1));
        hexStringToBytes = VcpcsManager.m1322a(allocate, allocate.position());
        Utils.m1283e("constructSelectPPSE=", hexStringToBytes);
        return new ApduResponse(hexStringToBytes);
    }

    private ApduResponse m1313U(int i) {
        Log.m1300d("VcpcsManager", "constructSelectAID() for aid " + i);
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put(MCFCITemplate.TAG_FCI_TEMPLATE);
        int position = allocate.position();
        allocate.put((byte) 0);
        if (this.EB == null) {
            this.EB = new ArrayList();
        }
        AidInfo aidInfo = (AidInfo) this.EB.get(i);
        allocate.put(PinChangeUnblockApdu.CLA);
        byte[] hexStringToBytes = Utils.hexStringToBytes(aidInfo.getAid());
        if (hexStringToBytes == null) {
            Log.m1301e("VcpcsManager", "constructed AID is null");
            return null;
        }
        allocate.put((byte) hexStringToBytes.length);
        allocate.put(Utils.hexStringToBytes(aidInfo.getAid()));
        allocate.put(PutTemplateApdu.FCI_PROPRIETARY_TEMPLATE_TAG);
        int position2 = allocate.position();
        allocate.put((byte) 0);
        if (aidInfo.getApplicationLabel() != null && aidInfo.getApplicationLabel().length() > 0) {
            byte[] hexStringToBytes2 = Utils.hexStringToBytes(aidInfo.getApplicationLabel());
            if (hexStringToBytes2 != null) {
                allocate.put(GetTemplateApdu.TAG_APPLICATION_LABEL_50);
                allocate.put((byte) hexStringToBytes2.length);
                allocate.put(hexStringToBytes2);
            }
        }
        if (this.EI == null || this.EI.length <= 0) {
            Log.m1301e("VcpcsManager", "PDOL is incorrect");
            return new ApduResponse(-4);
        }
        allocate.putShort((short) -24776);
        allocate.put((byte) this.EI.length);
        allocate.put(this.EI);
        if (this.EK != null && this.EK.length > 0) {
            allocate.putShort((short) 24365);
            allocate.put((byte) this.EK.length);
            allocate.put(this.EK);
        }
        allocate.put(PutTemplateApdu.FCI_ISSUER_DATA_HIGHER_BYTE_TAG);
        allocate.put(PutTemplateApdu.FCI_ISSUER_DATA_LOWER_BYTE_TAG);
        int position3 = allocate.position();
        allocate.put((byte) 0);
        if (this.Ew == null || this.Ew.length < 5 || this.Ew.length > 16) {
            Log.m1301e("VcpcsManager", "application program id is incorrect");
            return new ApduResponse(-4);
        }
        allocate.putShort((short) -24742);
        allocate.put((byte) this.Ew.length);
        allocate.put(this.Ew);
        if (!this.FT) {
            allocate.putShort((short) -16541);
            allocate.put((byte) 4);
            allocate.putShort((short) -8416);
            allocate.put((byte) 1);
            allocate.put(VerifyPINApdu.P2_PLAINTEXT);
        }
        allocate.put(position3, (byte) ((allocate.position() - position3) - 1));
        allocate.put(position2, (byte) ((allocate.position() - position2) - 1));
        allocate.put(position, (byte) ((allocate.position() - position) - 1));
        byte[] a = VcpcsManager.m1322a(allocate, allocate.position());
        Utils.m1283e("constructSelectAID=", a);
        return new ApduResponse(a);
    }

    private ApduResponse fZ() {
        Log.m1300d("VcpcsManager", "constructGPO MSD");
        byte[] bArr = new byte[]{VerifyPINApdu.P2_PLAINTEXT, (byte) 6, (byte) 0, EMVGetResponse.INS, (byte) 8, (byte) 1, (byte) 1, (byte) 0, SetResetParamApdu.CLA, (byte) 0};
        Utils.m1283e("GPO_MSD=", bArr);
        return new ApduResponse(bArr);
    }

    private ApduResponse ga() {
        Log.m1300d("VcpcsManager", "preconstructGPO_qVSDCnoODA()");
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put(ApplicationInfoManager.TERM_XP2);
        allocate.put((byte) 0);
        allocate.putShort((short) -24794);
        allocate.put((byte) 8);
        allocate.position(allocate.position() + 8);
        if (this.EU == null || this.EU.length != 4) {
            Log.m1301e("VcpcsManager", "qVSDC noODA afl is not valid");
            return new ApduResponse(-4);
        }
        allocate.put((byte) -108);
        allocate.put((byte) 4);
        allocate.put(this.EU);
        if (this.EV == null || this.EV.length != 2) {
            Log.m1301e("VcpcsManager", "qVSDC noODA aip is not valid");
            return new ApduResponse(-4);
        }
        allocate.put(EMVSetStatusApdu.RESET_LOWEST_PRIORITY);
        allocate.put((byte) 2);
        allocate.put(this.EV);
        allocate.putShort(StatesConstants.ATC_TAG);
        allocate.put((byte) 2);
        allocate.position(allocate.position() + 2);
        allocate.putShort((short) -24724);
        allocate.put((byte) 2);
        allocate.position(allocate.position() + 2);
        if (this.ES == null || this.ES.length != 4) {
            Log.m1301e("VcpcsManager", "qVSDC noODA ffi is not valid");
            return new ApduResponse(-4);
        }
        allocate.putShort((short) -24722);
        allocate.put((byte) 4);
        allocate.put(this.ES);
        allocate.putShort(StatesConstants.IAD_TAG);
        allocate.put(VerifyPINApdu.INS);
        allocate.position(allocate.position() + 32);
        allocate.put((byte) 87);
        byte[] bArr = new byte[allocate.position()];
        allocate.position(0);
        allocate.get(bArr, 0, bArr.length);
        Utils.m1283e("preconstructGPO_qVSDCnoODA=", bArr);
        return new ApduResponse(bArr);
    }

    private ApduResponse m1328n(byte[] bArr, byte[] bArr2) {
        Log.m1300d("VcpcsManager", "constructGPO_qVSDCnoODA()");
        if (bArr.length != 33) {
            Log.m1301e("VcpcsManager", "pdol value length is invalid");
            return new ApduResponse(-4);
        }
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        if (this.FI == null) {
            return new ApduResponse(-9);
        }
        int length = this.FI.getApduData().length;
        allocate.put(this.FI.getApduData());
        byte[] bArr3 = new byte[]{(byte) 1, VerifyPINApdu.P2_PLAINTEXT};
        byte[] bArr4 = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        byte[] hexStringToBytes = Utils.hexStringToBytes(((AidInfo) this.EB.get(this.ED)).getCap());
        Utils.m1281a(this.EP, 0, bArr3, 0, 2);
        if (this.ED < 0) {
            return new ApduResponse(-5);
        }
        int a = m1315a(bArr2, hexStringToBytes, bArr4, bArr3);
        if (a != 0) {
            return new ApduResponse(a);
        }
        hexStringToBytes = new byte[32];
        int m = m1327m(bArr4, hexStringToBytes);
        if (hexStringToBytes.length != 32) {
            Log.m1301e("VcpcsManager", "IAD_FORMAT length is not 32");
            return new ApduResponse(-5);
        } else if (m <= (short) 0) {
            return new ApduResponse(m);
        } else {
            bArr4 = new byte[2];
            VcpcsManager.m1323c(this.Fw, bArr4);
            if (bArr4.length != 2) {
                Log.m1301e("VcpcsManager", "ATC length is not 2");
                return new ApduResponse(-5);
            }
            byte[] bArr5 = new byte[65];
            Utils.m1281a(bArr, 4, bArr5, 0, bArr.length - 4);
            short length2 = (short) ((bArr.length - 4) + 0);
            Utils.m1281a(this.EV, 0, bArr5, length2, 2);
            length2 = (short) (length2 + 2);
            Utils.m1281a(bArr4, 0, bArr5, length2, 2);
            Utils.m1281a(hexStringToBytes, 0, bArr5, (short) (length2 + 2), 32);
            try {
                CryptoManager cryptoManager = this.Dl;
                bArr5 = CryptoManager.m1292i(this.Fz, bArr5);
                if (bArr5 == null) {
                    Log.m1301e("VcpcsManager", "generated application cryptogram is null");
                    return new ApduResponse(-5);
                } else if (bArr5.length != 8) {
                    Log.m1301e("VcpcsManager", "ARQC data length is not 8");
                    return new ApduResponse(-5);
                } else {
                    Utils.m1283e("ARQC data=", bArr5);
                    allocate.position(5);
                    allocate.put(bArr5);
                    Utils.m1286p(bArr5);
                    allocate.position(26);
                    allocate.put(bArr4);
                    allocate.position(31);
                    allocate.put(bArr3);
                    allocate.position(43);
                    allocate.put(hexStringToBytes);
                    try {
                        if (this.FV) {
                            bArr3 = this.Dl.m1298h(this.Fz, m1314V(this.Fw));
                            hexStringToBytes = m1332v(bArr3);
                            Utils.m1286p(bArr3);
                        } else {
                            hexStringToBytes = fX();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        hexStringToBytes = null;
                    }
                    if (hexStringToBytes == null || hexStringToBytes.length <= 0) {
                        Log.m1301e("VcpcsManager", "track data error: token is not retrieved?");
                        return new ApduResponse(-5);
                    }
                    allocate.position(length);
                    allocate.put((byte) hexStringToBytes.length);
                    allocate.put(hexStringToBytes);
                    if (this.ET.length != 1) {
                        Log.m1301e("VcpcsManager", "provison data error: PSN is invalid");
                        return new ApduResponse(-4);
                    }
                    allocate.put(MCFCITemplate.TAG_FILE_CONTROL_INFORMATION);
                    allocate.put((byte) 52);
                    allocate.put((byte) 1);
                    allocate.put(this.ET[0]);
                    if (this.EN == null || this.EN.length != 1) {
                        Log.m1301e("VcpcsManager", "qVSDC noODA cid is not valid");
                        return new ApduResponse(-4);
                    }
                    allocate.putShort(StatesConstants.CID_TAG);
                    allocate.put((byte) 1);
                    allocate.put(this.EN);
                    allocate.put(1, (byte) ((allocate.position() - 1) - 1));
                    byte[] a2 = VcpcsManager.m1322a(allocate, allocate.position());
                    if (Eo && Eq) {
                        Utils.m1283e("constructGPO_qVSDCnoODA", a2);
                    }
                    return new ApduResponse(a2);
                }
            } catch (VisaTAException e2) {
                Log.m1301e("VcpcsManager", "cannot generate cryptogram");
                return new ApduResponse(-5);
            }
        }
    }

    private ApduResponse m1329o(byte[] bArr, byte[] bArr2) {
        Log.m1300d("VcpcsManager", "constructGPO_qVSDCwithODA()");
        if (bArr == null || bArr.length != 33) {
            Log.m1301e("VcpcsManager", "pdol value length is invalid");
            return new ApduResponse(-4);
        } else if (this.EX == null || this.EX.length != 2) {
            Log.m1301e("VcpcsManager", "qVSDC noODA aip is not valid");
            return new ApduResponse(-4);
        } else {
            ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
            byte[] bArr3 = new byte[]{(byte) 1, VerifyPINApdu.P2_PLAINTEXT};
            byte[] bArr4 = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
            if (this.ED < 0) {
                return new ApduResponse(-5);
            }
            byte[] hexStringToBytes = Utils.hexStringToBytes(((AidInfo) this.EB.get(this.ED)).getCap());
            Utils.m1281a(this.EP, 0, bArr3, 0, 2);
            int a = m1315a(bArr2, hexStringToBytes, bArr4, bArr3);
            if (a != 0) {
                return new ApduResponse(a);
            }
            Log.m1300d("VcpcsManager", "CTQ : " + Utils.m1285o(bArr3));
            byte[] bArr5 = new byte[32];
            int m = m1327m(bArr4, bArr5);
            if (bArr5.length != 32) {
                Log.m1301e("VcpcsManager", "IAD_FORMAT length is not 32");
                return new ApduResponse(-5);
            } else if (m <= (short) 0) {
                return new ApduResponse(m);
            } else {
                byte[] bArr6 = new byte[2];
                VcpcsManager.m1323c(this.Fw, bArr6);
                if (bArr6.length != 2) {
                    Log.m1301e("VcpcsManager", "ATC length is not 2");
                    return new ApduResponse(-5);
                }
                byte[] bArr7 = new byte[65];
                Utils.m1281a(bArr, 4, bArr7, 0, bArr.length - 4);
                short length = (short) (0 + (bArr.length - 4));
                Utils.m1281a(this.EX, 0, bArr7, length, 2);
                length = (short) (length + 2);
                Utils.m1281a(bArr6, 0, bArr7, length, 2);
                Utils.m1281a(bArr5, 0, bArr7, (short) (length + 2), 32);
                byte[] bArr8 = new byte[8];
                bArr4 = null;
                try {
                    if (this.FV) {
                        bArr4 = m1314V(this.Fw);
                    }
                    Log.m1300d("VcpcsManager", "generate cryptogram " + Utils.m1285o(this.Fz));
                    CryptoManager cryptoManager = this.Dl;
                    bArr4 = CryptoManager.m1291d(this.Fz, bArr7, bArr4);
                    VcpcsManager G = m1311G(bArr4);
                    Utils.m1286p(bArr4);
                    if (G == null || G.gp() == null || G.gp().length != 8) {
                        Log.m1301e("VcpcsManager", "generated generateQVSDCandMSDVV is not right");
                        return new ApduResponse(-5);
                    }
                    Utils.m1281a(G.gp(), 0, bArr8, 0, 8);
                    if (G.go() == null || G.go().length <= 0) {
                        bArr4 = null;
                    } else {
                        hexStringToBytes = new byte[G.go().length];
                        Utils.m1281a(G.go(), 0, hexStringToBytes, 0, G.go().length);
                        bArr4 = hexStringToBytes;
                    }
                    G.clear();
                    if (bArr8 == null) {
                        Log.m1301e("VcpcsManager", "generated application cryptogram is null");
                        return new ApduResponse(-5);
                    } else if (bArr8.length != 8) {
                        Log.m1301e("VcpcsManager", "ARQC data length is not 8");
                        return new ApduResponse(-5);
                    } else {
                        Utils.m1283e("ARQC data=", bArr8);
                        try {
                            if (this.FV) {
                                hexStringToBytes = m1332v(bArr4);
                                Utils.m1286p(bArr4);
                            } else {
                                hexStringToBytes = fX();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hexStringToBytes = null;
                        }
                        if (hexStringToBytes == null || hexStringToBytes.length <= 0) {
                            Log.m1301e("VcpcsManager", "track data error: token is not retrieved?");
                            return new ApduResponse(-5);
                        }
                        allocate.put(ApplicationInfoManager.TERM_XP2);
                        m = allocate.position();
                        allocate.put((byte) 0);
                        allocate.putShort((short) -24794);
                        allocate.put((byte) 8);
                        allocate.put(bArr8);
                        if (this.EW == null || this.EW.length != 4) {
                            Log.m1301e("VcpcsManager", "qVSDC noODA afl is not valid");
                            return new ApduResponse(-4);
                        }
                        allocate.put((byte) -108);
                        allocate.put((byte) 4);
                        allocate.put(this.EW);
                        allocate.put(EMVSetStatusApdu.RESET_LOWEST_PRIORITY);
                        allocate.put((byte) 2);
                        allocate.put(this.EX);
                        allocate.putShort(StatesConstants.ATC_TAG);
                        allocate.put((byte) 2);
                        allocate.put(bArr6);
                        allocate.putShort((short) -24724);
                        allocate.put((byte) 2);
                        allocate.put(bArr3);
                        if (this.EN == null || this.EN.length != 1) {
                            Log.m1301e("VcpcsManager", "qVSDC noODA cid is not valid");
                            return new ApduResponse(-4);
                        }
                        allocate.putShort(StatesConstants.CID_TAG);
                        allocate.put((byte) 1);
                        allocate.put(this.EN);
                        if (this.ES == null || this.ES.length != 4) {
                            Log.m1301e("VcpcsManager", "qVSDC noODA ffi is not valid");
                            return new ApduResponse(-4);
                        }
                        allocate.putShort((short) -24722);
                        allocate.put((byte) 4);
                        allocate.put(this.ES);
                        allocate.putShort(StatesConstants.IAD_TAG);
                        allocate.put(VerifyPINApdu.INS);
                        allocate.put(bArr5);
                        allocate.put((byte) 87);
                        allocate.put((byte) hexStringToBytes.length);
                        allocate.put(hexStringToBytes);
                        if (this.ET.length != 1) {
                            Log.m1301e("VcpcsManager", "provison data error: PSN is invalid");
                            return new ApduResponse(-4);
                        }
                        allocate.put(MCFCITemplate.TAG_FILE_CONTROL_INFORMATION);
                        allocate.put((byte) 52);
                        allocate.put((byte) 1);
                        allocate.put(this.ET[0]);
                        if (this.EE != null && this.EE.length <= 26 && this.EE.length > 0) {
                            allocate.put(MCFCITemplate.TAG_FILE_CONTROL_INFORMATION);
                            allocate.put(VerifyPINApdu.INS);
                            allocate.put((byte) this.EE.length);
                            allocate.put(this.EE);
                        }
                        if (this.EM != null && this.EM.length > 0 && this.EM.length <= 32) {
                            allocate.putShort((short) -24708);
                            allocate.put((byte) this.EM.length);
                            allocate.put(this.EM);
                        }
                        allocate.put(m, (byte) ((allocate.position() - m) - 1));
                        bArr4 = VcpcsManager.m1322a(allocate, allocate.position());
                        if (Eo && Eq) {
                            Utils.m1283e("constructGPO_qVSDCwithODA", bArr4);
                        }
                        this.Fd = new byte[7];
                        this.Fd[0] = (byte) 1;
                        this.Fd[1] = (byte) 1;
                        this.Fd[2] = (byte) 1;
                        this.Fd[3] = (byte) 1;
                        this.Fd[4] = (byte) 1;
                        Utils.m1281a(bArr3, 0, this.Fd, 5, 2);
                        Log.m1300d("VcpcsManager", "sQVSDCData_withODA_cardAuthData " + Utils.m1285o(this.Fd));
                        return new ApduResponse(bArr4);
                    }
                } catch (VisaTAException e2) {
                    Log.m1301e("VcpcsManager", "cannot generate cryptogram ");
                    return new ApduResponse(-5);
                }
            }
        }
    }

    private ApduResponse gb() {
        Log.m1300d("VcpcsManager", "constructReadRecord() -MSD");
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put((byte) 112);
        int position = allocate.position();
        allocate.put((byte) 0);
        allocate.put((byte) 87);
        byte[] bArr = null;
        try {
            byte[] h = this.Dl.m1298h(this.Fz, m1314V(this.Fw));
            bArr = m1332v(h);
            Utils.m1286p(h);
        } catch (VisaTAException e) {
            e.printStackTrace();
        }
        if (bArr == null || bArr.length <= 0) {
            return new ApduResponse(-9);
        }
        allocate.put((byte) bArr.length);
        allocate.put(bArr);
        if (this.EE != null && this.EE.length <= 26 && this.EE.length > 0) {
            allocate.put(MCFCITemplate.TAG_FILE_CONTROL_INFORMATION);
            allocate.put(VerifyPINApdu.INS);
            allocate.put((byte) this.EE.length);
            allocate.put(this.EE);
        }
        allocate.put(position, (byte) ((allocate.position() - position) - 1));
        bArr = VcpcsManager.m1322a(allocate, allocate.position());
        if (Ep && Eq) {
            Utils.m1283e("constructReadRecord0101=", bArr);
        }
        return new ApduResponse(bArr);
    }

    private ApduResponse gd() {
        Log.m1300d("VcpcsManager", "constructReadRecord() -qVSDC no ODA");
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put((byte) 112);
        int position = allocate.position();
        allocate.put((byte) 0);
        if (this.EE != null && this.EE.length <= 26 && this.EE.length > 0) {
            allocate.put(MCFCITemplate.TAG_FILE_CONTROL_INFORMATION);
            allocate.put(VerifyPINApdu.INS);
            allocate.put((byte) this.EE.length);
            allocate.put(this.EE);
        }
        if (this.EO == null || this.EO.length != 2) {
            Log.m1301e("VcpcsManager", "provision data error: Issuer Country Code length is invalid");
            return new ApduResponse(-9);
        }
        allocate.putShort((short) 24360);
        allocate.put((byte) this.EO.length);
        allocate.put(this.EO);
        if (this.EM != null && this.EM.length > 0 && this.EM.length <= 32) {
            allocate.putShort((short) -24708);
            allocate.put((byte) this.EM.length);
            allocate.put(this.EM);
        }
        if (this.EL.length != 2) {
            Log.m1301e("VcpcsManager", "provision data error: auc length is invalid");
            return new ApduResponse(-9);
        }
        allocate.putShort((short) -24825);
        allocate.put((byte) this.EL.length);
        allocate.put(this.EL);
        if (this.Ev == null || this.Ev.length != 6) {
            Log.m1301e("VcpcsManager", "provision data error: Token Requester ID (TRID) length is invalid");
            return new ApduResponse(-9);
        }
        allocate.putShort((short) -24807);
        allocate.put((byte) 6);
        allocate.put(this.Ev);
        allocate.put(position, (byte) ((allocate.position() - position) - 1));
        byte[] a = VcpcsManager.m1322a(allocate, allocate.position());
        if (Ep && Eq) {
            Utils.m1283e("constructReadRecord0103=", a);
        }
        return new ApduResponse(a);
    }

    private ApduResponse ge() {
        Log.m1300d("VcpcsManager", "constructReadRecord() -qVSDC with ODA 1");
        ApduResponse apduResponse = new ApduResponse(-9);
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put((byte) 112);
        allocate.put(TLVParser.BYTE_81);
        int position = allocate.position();
        allocate.put((byte) 0);
        if (this.Fa == null || this.Fa.length > CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) {
            return apduResponse;
        }
        allocate.put(SetResetParamApdu.CLA);
        allocate.put(TLVParser.BYTE_81);
        allocate.put((byte) this.Fa.length);
        allocate.put(this.Fa);
        if (this.Fb == null) {
            return apduResponse;
        }
        if (this.Fb.length != 3 && this.Fb.length != 1) {
            return apduResponse;
        }
        allocate.putShort((short) -24782);
        allocate.put((byte) this.Fb.length);
        allocate.put(this.Fb);
        if (this.Fc != null && this.Fc.length <= 36) {
            allocate.put((byte) -110);
            allocate.put((byte) this.Fc.length);
            allocate.put(this.Fc);
        }
        if (this.EZ == null || this.EZ.length != 1) {
            return apduResponse;
        }
        allocate.put((byte) -113);
        allocate.put((byte) 1);
        allocate.put(this.EZ[0]);
        allocate.put(position, (byte) ((allocate.position() - position) - 1));
        byte[] a = VcpcsManager.m1322a(allocate, allocate.position());
        if (Ep && Eq) {
            Utils.m1283e("constructReadRecord0202=", a);
        }
        return new ApduResponse(a);
    }

    private ApduResponse gf() {
        Log.m1300d("VcpcsManager", "constructReadRecord() -qVSDC with ODA ");
        ApduResponse apduResponse = new ApduResponse(-9);
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put((byte) 112);
        allocate.put(TLVParser.BYTE_81);
        int position = allocate.position();
        allocate.put((byte) 0);
        if (this.Fg == null || this.Fg.length > CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) {
            return apduResponse;
        }
        allocate.putShort((short) -24762);
        allocate.put(TLVParser.BYTE_81);
        allocate.put((byte) this.Fg.length);
        allocate.put(this.Fg);
        if (this.Fh == null) {
            return apduResponse;
        }
        if (this.Fh.length != 3 && this.Fh.length != 1) {
            return apduResponse;
        }
        allocate.putShort((short) -24761);
        allocate.put((byte) this.Fh.length);
        allocate.put(this.Fh);
        if (this.Fi != null && this.Fi.length <= 42) {
            allocate.putShort((short) -24760);
            allocate.put((byte) this.Fi.length);
            allocate.put(this.Fi);
        }
        allocate.put(position, (byte) ((allocate.position() - position) - 1));
        Log.m1300d("VcpcsManager", "buffer length=" + allocate.position() + ", value=" + ((byte) ((allocate.position() - position) - 1)));
        byte[] a = VcpcsManager.m1322a(allocate, allocate.position());
        if (Ep && Eq) {
            Utils.m1283e("constructReadRecord0203=", a);
        }
        return new ApduResponse(a);
    }

    private ApduResponse gg() {
        Log.m1300d("VcpcsManager", "constructReadRecord() -qVSDC with ODA 3");
        Log.m1300d("VcpcsManager", "testing");
        ApduResponse apduResponse = new ApduResponse(-9);
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        allocate.put((byte) 112);
        allocate.put(TLVParser.BYTE_81);
        int position = allocate.position();
        allocate.put((byte) 0);
        if (this.Fd == null || this.Fd.length != 7) {
            return apduResponse;
        }
        byte[] gm = gm();
        if (gm == null) {
            Log.m1301e("VcpcsManager", "SDAD input generation null");
            return new ApduResponse(-5);
        }
        try {
            CryptoManager cryptoManager = this.Dl;
            gm = CryptoManager.m1294k(this.Fj, gm);
            VcpcsManager H = m1312H(gm);
            Utils.m1286p(gm);
            if (H == null || H.gq() == null || H.getSignature() == null) {
                Log.m1301e("VcpcsManager", "SDAD result not satisfied ");
                return new ApduResponse(-5);
            }
            int length = H.gq().length;
            if (length != 4) {
                Log.m1301e("VcpcsManager", "UN length error result length " + length);
                return new ApduResponse(-5);
            }
            Utils.m1281a(H.gq(), 0, this.Fd, 1, length);
            Log.m1300d("VcpcsManager", "new sQVSDCData_withODA_cardAuthData " + Utils.m1285o(this.Fd));
            length = H.getSignature().length;
            if (length <= 0 || length > CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) {
                Log.m1301e("VcpcsManager", "length error result length not able to retrieve signature len " + length);
                return new ApduResponse(-5);
            }
            this.Fe = new byte[length];
            Utils.m1281a(H.getSignature(), 0, this.Fe, 0, length);
            Log.m1300d("VcpcsManager", "sdad output " + Utils.m1285o(this.Fe));
            H.clear();
            if (this.Fe == null || this.Fe.length <= 0 || this.Fe.length > CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) {
                return apduResponse;
            }
            allocate.putShort((short) -24727);
            allocate.put((byte) this.Fd.length);
            allocate.put(this.Fd);
            allocate.putShort((short) -24757);
            allocate.put(TLVParser.BYTE_81);
            allocate.put((byte) this.Fe.length);
            allocate.put(this.Fe);
            if (this.EL != null && this.EL.length == 2) {
                allocate.putShort((short) -24825);
                allocate.put((byte) this.EL.length);
                allocate.put(this.EL);
            }
            if (this.EO == null || this.EO.length != 2) {
                Log.m1301e("VcpcsManager", "provision data error: Issuer Country Code length is invalid");
                return new ApduResponse(-9);
            }
            allocate.put(MCFCITemplate.TAG_FILE_CONTROL_INFORMATION);
            allocate.put((byte) 40);
            allocate.put((byte) this.EO.length);
            allocate.put(this.EO);
            if (this.EY == null || this.EY.length > 3) {
                return apduResponse;
            }
            allocate.putShort((short) 24356);
            allocate.put((byte) this.EY.length);
            allocate.put(this.EY);
            if (this.FP == null || this.FP.length() > 20) {
                return apduResponse;
            }
            byte[] hexStringToBytes = Utils.hexStringToBytes(this.FP);
            allocate.put((byte) 90);
            allocate.put((byte) hexStringToBytes.length);
            allocate.put(hexStringToBytes);
            if (this.Ev != null && this.Ev.length == 6) {
                allocate.putShort((short) -24807);
                allocate.put((byte) this.Ev.length);
                allocate.put(this.Ev);
            }
            allocate.put(position, (byte) ((allocate.position() - position) - 1));
            Log.m1300d("VcpcsManager", "buffer length=" + allocate.position() + ", value=" + ((byte) ((allocate.position() - position) - 1)));
            byte[] a = VcpcsManager.m1322a(allocate, allocate.position());
            if (Ep && Eq) {
                Utils.m1283e("constructReadRecord0204=", a);
            }
            return new ApduResponse(a);
        } catch (Exception e) {
            Log.m1301e("VcpcsManager", "cannot generate SDAD");
            e.printStackTrace();
            return new ApduResponse(-5);
        }
    }

    ApduResponse m1337B(byte[] bArr) {
        if (!m1333w(bArr)) {
            return new ApduResponse(-12);
        }
        if (Utils.getShort(bArr, 2) == PutTemplateApdu.PPSE_VERSION_TAG) {
            return new ApduResponse(Constant.Do);
        }
        return new ApduResponse(-12);
    }

    ApduResponse m1338C(byte[] bArr) {
        Log.m1300d("VcpcsManager", "selectPPSE()");
        if (this.FE == null || !this.Gc) {
            this.FE = fY();
        }
        ApduResponse apduResponse = this.FE;
        if (En && Eq) {
            Utils.m1283e("selectPPSE=", apduResponse.getApduData());
        }
        return apduResponse;
    }

    ApduResponse m1339D(byte[] bArr) {
        Log.m1300d("VcpcsManager", "selectAID()");
        if (this.FK == null || this.FK.getTokenId() < 0) {
            Log.m1301e("VcpcsManager", "no card is selected yet");
            return new ApduResponse(-9);
        } else if (m1334x(bArr)) {
            ApduResponse apduResponse = new ApduResponse(null);
            if (this.ED == 0) {
                apduResponse = this.FF;
            } else if (this.ED != 1) {
                return new ApduResponse(-3);
            } else {
                apduResponse = this.FG;
            }
            if (!En || !Eq) {
                return apduResponse;
            }
            Utils.m1283e("selectAID=", apduResponse.getApduData());
            return apduResponse;
        } else if (bArr[3] != null) {
            return new ApduResponse(-9);
        } else {
            return new ApduResponse(-3);
        }
    }

    ApduResponse m1340E(byte[] bArr) {
        Log.m1300d("VcpcsManager", "getProcessingOptions()");
        if (!m1335y(bArr)) {
            return new ApduResponse(-8);
        }
        if (this.FC >= HCEClientConstants.HIGHEST_ATC_DEC_VALUE || this.Fw >= HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
            return new ApduResponse(-5);
        }
        if (!this.Gc) {
            return new ApduResponse(-6);
        }
        this.Fw++;
        Log.m1300d("VcpcsManager", "update ATC counter to " + this.Fw);
        int i = 33;
        if (gn()) {
            i = 65;
        }
        if (i + 7 > bArr.length) {
            return new ApduResponse(-1);
        }
        byte[] bArr2;
        byte[] bArr3 = new byte[i];
        Utils.m1281a(bArr, 7, bArr3, 0, i);
        ApduResponse apduResponse = new ApduResponse(-5);
        this.FX = TransactionType.UNKNOWN_TYPE;
        if (Eo) {
            Log.m1300d("VcpcsManager", "pdol= " + Utils.m1285o(bArr3) + ", pdol_length= " + i + ", TTQ_offset=" + 0 + ", aa_offset=" + 4 + ", oa_offset=" + 10 + ", countrycode_offset=" + 16 + ", currencyCode_offset=" + 23 + ", unprediactableNumber_offset=" + 29);
        }
        byte[] bArr4 = new byte[4];
        Utils.m1281a(bArr3, 0, bArr4, 0, bArr4.length);
        byte[] bArr5 = new byte[4];
        Utils.m1281a(bArr3, 29, bArr5, 0, bArr5.length);
        this.FY = Utils.m1285o(bArr5);
        byte[] bArr6 = new byte[6];
        Utils.m1281a(bArr3, 4, bArr6, 0, bArr6.length);
        this.FZ = Utils.m1285o(bArr6);
        bArr5 = new byte[6];
        Utils.m1281a(bArr3, 10, bArr5, 0, bArr5.length);
        byte[] bArr7 = new byte[2];
        Utils.m1281a(bArr3, 16, bArr7, 0, bArr7.length);
        byte[] bArr8 = new byte[2];
        Utils.m1281a(bArr3, 23, bArr8, 0, bArr8.length);
        this.Ga = Utils.m1285o(bArr8);
        if (gn()) {
            bArr2 = new byte[32];
            Utils.m1281a(bArr3, 33, bArr2, 0, bArr2.length);
            this.Gb = Utils.m1285o(bArr2);
            String bF = Utils.bF(this.Gb);
            this.Ge.addPdolValue(TransactionStatus.EXTRA_MERCHANT_NAME_TAG, bF);
            Log.m1300d("VcpcsManager", "9F4E= " + this.Gb);
            Log.m1300d("VcpcsManager", "merchant Name= " + bF);
        }
        bArr2 = new byte[33];
        Utils.m1281a(bArr3, 0, bArr2, 0, bArr2.length);
        if (Eo) {
            Log.m1300d("VcpcsManager", "authorized=" + Utils.m1285o(bArr6) + ", other=" + Utils.m1285o(bArr5) + ", country=" + Utils.m1285o(bArr7) + ", currency=" + Utils.m1285o(bArr8));
        }
        if (this.ED < 0) {
            return new ApduResponse(-5);
        }
        ApduResponse o;
        bArr5 = Utils.hexStringToBytes(((AidInfo) this.EB.get(this.ED)).getCap());
        if (Utils.isBitSet(bArr4[0], 6)) {
            if (this.FQ) {
                Log.m1301e("VcpcsManager", "qVSDC data is not provisioned for qVSDC transaction");
                return new ApduResponse(-5);
            } else if (Utils.isBitSet(bArr4[0], 1) && !this.FT && !Utils.isBitSet(bArr5[1], 6)) {
                this.FX = TransactionType.QVSDC_WITH_ODA_1;
                boolean l = VcpcsManager.m1326l(bArr6, bArr8);
                this.Ge.setTapNGoAllowed(l);
                if (fW()) {
                    Log.m1300d("VcpcsManager", "tap with authentication");
                } else if (!l || fW()) {
                    this.Ge.setError(TransactionError.NO_AUTH_AMOUNT_REQ_NOT_SATISFIED);
                    return new ApduResponse(-5);
                } else {
                    Log.m1300d("VcpcsManager", "tapAndGo filter satisfied");
                    this.Gd = true;
                }
                if (Eo) {
                    Log.m1300d("VcpcsManager", "this is qVSDC with ODA transaction");
                }
                o = m1329o(bArr2, bArr4);
            } else if (this.FS) {
                return new ApduResponse(-5);
            } else {
                if (fW()) {
                    this.FX = TransactionType.QVSDC_NO_ODA;
                    if (Eo) {
                        Log.m1300d("VcpcsManager", "this is qVSDC no ODA transaction");
                    }
                    o = m1328n(bArr2, bArr4);
                } else {
                    this.Ge.setError(TransactionError.NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED);
                    return new ApduResponse(-5);
                }
            }
        } else if (Utils.isBitSet(bArr4[0], 8) && !Utils.isBitSet(bArr4[0], 6)) {
            if (Eo) {
                Log.m1300d("VcpcsManager", "this is MSD transaciton");
            }
            if (this.FU) {
                Log.m1301e("VcpcsManager", "msd data is not provisioned for MSD transaction");
                return new ApduResponse(-5);
            } else if (fW()) {
                this.FX = TransactionType.MSD;
                o = this.FH;
            } else {
                Log.m1301e("VcpcsManager", "cdcvm is not verified");
                this.Ge.setError(TransactionError.NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED);
                return new ApduResponse(-5);
            }
        } else if (Utils.isBitSet(bArr4[0], 8) || Utils.isBitSet(bArr4[0], 6)) {
            o = apduResponse;
        } else {
            if (Eo) {
                Log.m1300d("VcpcsManager", "this is unknown transaction");
            }
            return new ApduResponse(-5);
        }
        if (!Eo || !Eq) {
            return o;
        }
        Utils.m1283e("GPO=", o.getApduData());
        return o;
    }

    ApduResponse m1341F(byte[] bArr) {
        Object obj = 1;
        Log.m1300d("VcpcsManager", "readRecord()");
        if (!m1336z(bArr)) {
            return new ApduResponse(-3);
        }
        ApduResponse gb;
        byte b = (byte) ((bArr[3] >> 3) & GF2Field.MASK);
        byte b2 = bArr[2];
        if (Ep) {
            Log.m1300d("VcpcsManager", "readrecord(): record=" + b + ", control=" + b2);
        }
        TVL tvl = new TVL();
        tvl.setTokenKey(this.FK);
        tvl.setTimeStamp(Utils.fU());
        tvl.setUnpredictableNumber(this.FY);
        tvl.setAtc(this.Fw);
        tvl.setApi(Utils.m1285o(this.Fx));
        switch (VcpcsManager.Gk[this.FX.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                if (b == (byte) 1 && b2 == (byte) 1) {
                    if (Ep) {
                        Log.m1300d("VcpcsManager", "msd transaction, readrecord0101");
                    }
                    gb = gb();
                    break;
                }
                Log.m1301e("VcpcsManager", "msd transaction, cannot read record0101");
                return new ApduResponse(-9);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                if (b == (byte) 1 && b2 == (byte) 3) {
                    if (Ep) {
                        Log.m1300d("VcpcsManager", "qVSDC no ODA transaction, readrecord0103");
                    }
                    gb = this.FJ;
                    break;
                }
                Log.m1301e("VcpcsManager", "qVSDC no ODA transaction, cannot read record0103");
                return new ApduResponse(-9);
            case F2m.PPB /*3*/:
                if (b == (byte) 2 && b2 == (byte) 2) {
                    Log.m1300d("VcpcsManager", "qVSDC with ODA transaction, readrecord0202");
                    gb = ge();
                    this.FX = TransactionType.QVSDC_WITH_ODA_2;
                    obj = null;
                    break;
                }
                Log.m1301e("VcpcsManager", "qVSDC with ODA transaction, cannot read record0202");
                return new ApduResponse(-9);
                break;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                if (b == (byte) 2 && b2 == (byte) 3) {
                    Log.m1300d("VcpcsManager", "qVSDC with ODA transaction, readrecord0203");
                    gb = gf();
                    this.FX = TransactionType.QVSDC_WITH_ODA_3;
                    obj = null;
                    break;
                }
                Log.m1301e("VcpcsManager", "qVSDC with ODA transaction, cannot read record0203");
                return new ApduResponse(-9);
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                if (b == (byte) 2 && b2 == 4) {
                    Log.m1300d("VcpcsManager", "qVSDC with ODA transaction, readrecord0204");
                    gb = gg();
                    break;
                }
                Log.m1301e("VcpcsManager", "qVSDC with ODA transaction, cannot read record0204");
                return new ApduResponse(-9);
                break;
            default:
                gb = new ApduResponse(-9);
                obj = null;
                break;
        }
        if (!(obj == null || gb.getApduError() == null || gb.getApduError().getErrorCode() != 0)) {
            if (this.FX == TransactionType.MSD) {
                tvl.setTransactionType("M");
            } else {
                tvl.setTransactionType("Q");
            }
            this.FD.add(tvl);
            this.FX = TransactionType.COMPLETED;
        }
        if (!Ep || !Eq) {
            return gb;
        }
        Utils.m1283e("readRecord=", gb.getApduData());
        return gb;
    }

    private boolean m1321a(TokenInfo tokenInfo, int i) {
        Log.m1300d("VcpcsManager", "retriveProvisionedData()");
        if (tokenInfo == null || i < 0 || i > HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
            Log.m1301e("VcpcsManager", "provisioned data is invalid");
            return false;
        }
        this.Ew = Utils.hexStringToBytes(tokenInfo.getAppPrgrmID());
        this.Ex = tokenInfo.getTokenStatus();
        this.FL = false;
        this.FM = false;
        this.FN = false;
        this.FO = false;
        if (Ei) {
            Log.m1300d("VcpcsManager", "retrieve AIDs");
        }
        this.EC = this.Eu.getHceData().getStaticParams().getAidInfo().size();
        if (this.EC > 0 && this.EC <= 2) {
            for (int i2 = 0; i2 < this.EC; i2++) {
                AidInfo aidInfo = (AidInfo) tokenInfo.getHceData().getStaticParams().getAidInfo().get(i2);
                this.EB.add(aidInfo);
                if ("A0000000031010".equals(aidInfo.getAid())) {
                    this.FM = true;
                    if (Ei) {
                        Log.m1300d("VcpcsManager", "SUPPORT VISA_CREDIT");
                    }
                } else if ("A0000000980840".equals(aidInfo.getAid())) {
                    this.FL = true;
                    if (Ei) {
                        Log.m1300d("VcpcsManager", "SUPPORT US_COMMON_DEBIT");
                    }
                } else if ("A0000000032020".equals(aidInfo.getAid())) {
                    this.FN = true;
                    if (Ei) {
                        Log.m1300d("VcpcsManager", "SUPPORT V PAY");
                    }
                } else if ("A0000000032010".equals(aidInfo.getAid())) {
                    this.FO = true;
                    if (Ei) {
                        Log.m1300d("VcpcsManager", "SUPPORT VISA ELECTRON");
                    }
                }
            }
        }
        if (Ei) {
            Log.m1300d("VcpcsManager", "retrieve HceData");
        }
        HceData hceData = tokenInfo.getHceData();
        if (hceData == null) {
            Log.m1301e("VcpcsManager", "hceData is null");
            return false;
        }
        StaticParams staticParams = hceData.getStaticParams();
        if (hceData.getStaticParams() == null) {
            Log.m1301e("VcpcsManager", "staticParams are null");
            return false;
        }
        if (Ei) {
            Log.m1300d("VcpcsManager", "retrieve tokenInfo");
        }
        this.Ey = Utils.hexStringToBytes(tokenInfo.getEncTokenInfo());
        this.Ez = tokenInfo.getExpirationDate().getYear();
        this.EA = tokenInfo.getExpirationDate().getMonth();
        this.EE = Utils.hexStringToBytes(tokenInfo.getHceData().getStaticParams().getCardHolderNameVCPCS());
        if (this.EE.length > 26) {
            Log.m1301e("VcpcsManager", "provision data error: CardholderName length is invalid");
            return false;
        }
        String str = BuildConfig.FLAVOR;
        if (tokenInfo.getTokenRequestorID().length() == 11) {
            str = str + LLVARUtil.EMPTY_STRING;
        }
        this.Ev = Utils.hexStringToBytes(str + tokenInfo.getTokenRequestorID());
        this.EK = Utils.hexStringToBytes(tokenInfo.getLang());
        this.EF = Utils.hexStringToBytes(staticParams.getCountryCode5F55());
        this.EG = Utils.hexStringToBytes(staticParams.getIssuerIdentificationNumber());
        this.EH = Utils.hexStringToBytes(staticParams.getKernelIdentifier());
        this.EJ = staticParams.getPdol();
        if (gn()) {
            this.EI = Utils.hexStringToBytes(this.EJ + "9F4E20");
        } else {
            this.EI = Utils.hexStringToBytes(this.EJ);
        }
        Log.m1300d("VcpcsManager", "sPdol " + Utils.m1285o(this.EI));
        if (Ei) {
            Log.m1300d("VcpcsManager", "retrieve qVSDC data");
        }
        QVSDCData qVSDCData = staticParams.getQVSDCData();
        if (qVSDCData != null) {
            this.EL = Utils.hexStringToBytes(qVSDCData.getAuc());
            this.EM = Utils.hexStringToBytes(qVSDCData.getCed());
            this.EN = Utils.hexStringToBytes(qVSDCData.getCid());
            this.EO = Utils.hexStringToBytes(qVSDCData.getCountryCode());
            this.EP = Utils.hexStringToBytes(qVSDCData.getCtq());
            this.EQ = Utils.hexStringToBytes(qVSDCData.getCvn());
            this.ER = Utils.hexStringToBytes(qVSDCData.getDigitalWalletID());
            this.ES = Utils.hexStringToBytes(qVSDCData.getFfi());
            this.ET = Utils.hexStringToBytes(qVSDCData.getPsn());
            if (qVSDCData.getQVSDCWithoutODA() != null) {
                this.EU = Utils.hexStringToBytes(qVSDCData.getQVSDCWithoutODA().getAfl());
                this.EV = Utils.hexStringToBytes(qVSDCData.getQVSDCWithoutODA().getAip());
            }
            if (!(qVSDCData.getqVSDCWithODA() == null || qVSDCData.getqVSDCWithODA().getIcc() == null)) {
                this.EW = Utils.hexStringToBytes(qVSDCData.getqVSDCWithODA().getAfl());
                this.EX = Utils.hexStringToBytes(qVSDCData.getqVSDCWithODA().getAip());
                this.EY = Utils.hexStringToBytes(qVSDCData.getqVSDCWithODA().getAppExpDate());
                this.EZ = Utils.hexStringToBytes(qVSDCData.getqVSDCWithODA().getCapki());
                this.Fa = Utils.hexStringToBytes(qVSDCData.getqVSDCWithODA().getIPubkCert());
                this.Fb = Utils.hexStringToBytes(qVSDCData.getqVSDCWithODA().getIPubkExpo());
                this.Fc = Utils.hexStringToBytes(qVSDCData.getqVSDCWithODA().getIPubkRem());
                ICC icc = qVSDCData.getqVSDCWithODA().getIcc();
                this.Fk = Utils.hexStringToBytes(icc.getIccCRTCoeffDModP());
                this.Fl = Utils.hexStringToBytes(icc.getIccCRTCoeffDModQ());
                this.Fm = Utils.hexStringToBytes(icc.getIccCRTCoeffQModP());
                this.Fn = Utils.hexStringToBytes(icc.getIccCRTprimep());
                this.Fo = Utils.hexStringToBytes(icc.getIccCRTprimeq());
                this.Fp = Utils.hexStringToBytes(icc.getIccKeymod());
                this.Ff = Utils.hexStringToBytes(icc.getIccPrivKExpo());
                this.Fg = Utils.hexStringToBytes(icc.getIccPubKCert());
                this.Fh = Utils.hexStringToBytes(icc.getIccPubKExpo());
                this.Fi = Utils.hexStringToBytes(icc.getIccPubKRem());
                this.Fj = Utils.hexStringToBytes(icc.getEncIccCRTPrivKey());
            }
        }
        if (staticParams.getMsdData() != null) {
            this.Fu = Utils.hexStringToBytes(staticParams.getMsdData().getAfl());
            this.Fv = Utils.hexStringToBytes(staticParams.getMsdData().getAip());
        }
        if (staticParams.getTrack2DataDec() == null || staticParams.getTrack2DataDec().getSvcCode() == null || staticParams.getTrack2DataDec().getSvcCode().length() <= 0) {
            this.Fq = null;
        } else {
            this.Fq = staticParams.getTrack2DataDec().getSvcCode();
        }
        if (Ei) {
            Log.m1300d("VcpcsManager", "retrieve Track2 data");
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
            Log.m1300d("VcpcsManager", "retrieve dynamic params");
        }
        DynParams dynParams = hceData.getDynParams();
        this.Fw = i;
        if (Ei) {
            Log.m1300d("VcpcsManager", "ATC=" + this.Fw);
        }
        this.Fx = Utils.hexStringToBytes(dynParams.getApi());
        this.Fy = Utils.hexStringToBytes(dynParams.getDki());
        this.Fz = Utils.hexStringToBytes(dynParams.getEncKeyInfo());
        this.FA = dynParams.getKeyExpTS().longValue();
        this.FB = dynParams.getMaxPmts().intValue();
        this.FC = dynParams.getSc();
        return gh();
    }

    private boolean gh() {
        if (this.Fu == null || this.Fu.length <= 0 || this.Fv == null || this.Fv.length <= 0) {
            this.FU = true;
            if (Ei) {
                Log.m1300d("VcpcsManager", "msd data is not provisioned");
            }
        } else {
            this.FU = false;
            if (Ei) {
                Log.m1300d("VcpcsManager", "msd data is provisioned");
            }
        }
        if (this.EL == null || this.EL.length <= 0 || this.EP == null || this.EP.length <= 0) {
            this.FQ = true;
            if (Ei) {
                Log.m1300d("VcpcsManager", "qVSDC data is not provisioned");
            }
        } else {
            this.FQ = false;
            if (Ei) {
                Log.m1300d("VcpcsManager", "qVSDC data is provisioned");
            }
        }
        if (this.EU == null || this.EU.length <= 0 || this.EV == null || this.EV.length <= 0) {
            Log.m1300d("VcpcsManager", "noODA data is not provisioned");
            this.FS = true;
        } else {
            Log.m1300d("VcpcsManager", "noODA data is provisioned");
            this.FS = false;
        }
        if (this.EW == null || this.EW.length <= 0 || this.EX == null || this.EX.length <= 0) {
            Log.m1300d("VcpcsManager", "ODA data is not provisioned");
            this.FT = true;
        } else {
            Log.m1300d("VcpcsManager", "ODA data is provisioned");
            this.FT = false;
        }
        if (this.Fr == null || this.Fr.length() <= 0) {
            this.FV = true;
            if (Ei) {
                Log.m1300d("VcpcsManager", "notDecSVC is not provisioned");
            }
        } else {
            this.FV = false;
            if (Ei) {
                Log.m1300d("VcpcsManager", "notDecSVC is provisioned");
            }
        }
        Log.m1300d("VcpcsManager", "preprocess data end");
        return gj();
    }

    private boolean gi() {
        Log.m1300d("VcpcsManager", "clearProvisionedData()");
        this.Ew = null;
        this.Ex = TokenStatus.NOT_FOUND.getStatus();
        this.EB.clear();
        this.EC = 0;
        this.FL = false;
        this.FM = false;
        this.EC = 0;
        Utils.m1286p(this.Ey);
        this.Ey = null;
        this.Ez = null;
        this.EA = null;
        this.EE = null;
        Utils.m1286p(this.Ev);
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
        Utils.m1286p(this.Fz);
        this.Fz = null;
        this.FA = 0;
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

    private boolean gj() {
        Log.m1300d("VcpcsManager", "preprocessApdus()");
        this.Gc = TokenStatus.ACTIVE.getStatus().equals(this.Ex);
        this.FE = fY();
        if (this.FE == null || this.FE.getApduError() == null || this.FE.getApduError().getErrorCode() < 0) {
            this.Gc = false;
        }
        if (this.EC > 0) {
            this.FF = m1313U(0);
            if (this.FF == null || this.FF.getApduError() == null || this.FF.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
            if (this.EC > 1) {
                this.FG = m1313U(1);
                if (this.FG == null || this.FG.getApduError() == null || this.FG.getApduError().getErrorCode() < 0) {
                    this.Gc = false;
                }
            }
        }
        if (!this.FU) {
            this.FH = fZ();
            if (this.FH == null || this.FH.getApduError() == null || this.FH.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
        }
        if (!this.FQ) {
            this.FI = ga();
            if (this.FI == null || this.FI.getApduError() == null || this.FI.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
            this.FJ = gd();
            if (this.FJ == null || this.FJ.getApduError() == null || this.FJ.getApduError().getErrorCode() < 0) {
                this.Gc = false;
            }
        }
        if (this.Gc) {
            Log.m1300d("VcpcsManager", "apdu pre-processed successfully");
        } else {
            Log.m1300d("VcpcsManager", "apdu pre-processed error");
        }
        return this.Gc;
    }

    private void gk() {
        gl();
        this.FK = null;
        gi();
        this.FD.clear();
        this.Gc = false;
        this.Gd = false;
        this.FW = Constant.DK;
        this.Gh = false;
        m1344j(true);
        this.Gi = (byte) 0;
        this.Eu = null;
    }

    void gl() {
        this.ED = -1;
        this.FY = "00000000";
        this.FX = TransactionType.UNKNOWN_TYPE;
        this.Ge.initialize();
    }

    void setCvmVerificationMode(CvmMode cvmMode) {
        this.FW = cvmMode;
    }

    long m1343b(TokenKey tokenKey) {
        return m1316a(tokenKey, this.Es.m1367b(tokenKey, true));
    }

    private long m1316a(TokenKey tokenKey, PaymentData paymentData) {
        Log.m1300d("VcpcsManager", "selectCard()");
        if (tokenKey == null) {
            Log.m1301e("VcpcsManager", "token key is null");
        }
        if (paymentData == null) {
            Log.m1301e("VcpcsManager", "payment data is null");
        }
        if (tokenKey == null || paymentData == null) {
            Log.m1301e("VcpcsManager", "selected token is invalid");
            return -1;
        }
        gk();
        this.FK = tokenKey;
        this.Eu = paymentData.getTokenInfo();
        m1319a(this.Eu);
        try {
            if (!m1321a(this.Eu, paymentData.getAtc())) {
                Log.m1301e("VcpcsManager", "cannot process selected card data");
                this.Gc = false;
                return -1;
            }
        } catch (VisaTAException e) {
            e.printStackTrace();
        }
        Log.m1300d("VcpcsManager", "card selected successfully");
        return tokenKey.getTokenId();
    }

    protected synchronized TransactionStatus m1342a(TokenKey tokenKey, boolean z) {
        TransactionStatus transactionStatus;
        Log.m1300d("VcpcsManager", "processTransactionComplete()");
        if (this.FD == null || this.FK == null || this.FK.getTokenId() < 0) {
            transactionStatus = new TransactionStatus(TransactionError.OTHER_ERROR, false);
        } else {
            Object obj = this.FX == TransactionType.COMPLETED ? 1 : null;
            int size = this.FD.size();
            if (this.FB - size < 0) {
                this.FB = 0;
            } else {
                this.FB -= size;
            }
            if (this.Es != null) {
                this.Es.m1365a(tokenKey, this.Fw, this.FB);
            } else {
                Log.m1300d("VcpcsManager", "visa db helper is null");
            }
            if (this.FD != null && this.FD.size() > 0) {
                for (size = 0; size < this.FD.size(); short s = (short) (size + 1)) {
                    if (this.Et == null) {
                        Log.m1301e("VcpcsManager", "cannot access db");
                    } else if (!z) {
                        r0 = (TVL) this.FD.get(size);
                        if (r0 != null && "S".equals(r0.getTransactionType())) {
                            Log.m1300d("VcpcsManager", "store " + r0.toString());
                            this.Et.m1374a(r0);
                        }
                    } else if (obj != null) {
                        r0 = (TVL) this.FD.get(size);
                        if (r0 != null && ("M".equals(r0.getTransactionType()) || "Q".equals(r0.getTransactionType()))) {
                            Log.m1300d("VcpcsManager", "store " + r0.toString());
                            this.Et.m1374a(r0);
                        }
                    }
                }
                this.FD.clear();
            }
            transactionStatus = new TransactionStatus(this.Ge.getError(), this.Ge.isTapNGoAllowed());
            if (z) {
                transactionStatus.setPdolValues(this.Ge.getPdolValues());
                gl();
                Log.m1300d("VcpcsManager", "nfc transaction details are cleared");
                if (obj != null) {
                    gk();
                    Log.m1300d("VcpcsManager", "card data is cleared for nfc transaction");
                }
            } else {
                gk();
                Log.m1300d("VcpcsManager", "card data is cleared for mst transaction");
            }
            if (!z) {
                transactionStatus = new TransactionStatus(TransactionError.NO_ERROR, false);
            } else if (transactionStatus.getError() == TransactionError.NO_ERROR && obj == null) {
                transactionStatus.setError(TransactionError.OTHER_ERROR);
            }
        }
        return transactionStatus;
    }

    public boolean prepareMstData() {
        boolean z = false;
        if (this.Eu == null) {
            Log.m1301e("VcpcsManager", "no card is selected for MST");
        } else {
            try {
                this.FP = this.Dl.m1299r(this.Ey);
                if (this.FP == null || this.FP.length() <= 0) {
                    Log.m1301e("VcpcsManager", "retrieved token for MST is null");
                } else {
                    Log.m1300d("VcpcsManager", "Token has been retrieved from storage for MST");
                    try {
                        TrackData trackData = new TrackData();
                        trackData.m1718N(this.Ey);
                        this.Fw++;
                        trackData.cB(String.format(Locale.US, "%04d", new Object[]{Integer.valueOf(this.Fw)}));
                        Mst mst = this.Eu.getMst();
                        trackData.setCVV("000");
                        trackData.cA(Utils.m1285o(this.Fx).substring(6, 8));
                        trackData.setServiceCode(mst.getSvcCode());
                        trackData.cC("00000000");
                        trackData.setTimestamp(Utils.m1285o(this.Fx).substring(2, 6));
                        trackData.cz(this.Ez.substring(2, 4) + this.EA);
                        Log.m1300d("VcpcsManager", "api=" + Utils.m1285o(this.Fx));
                        if (El) {
                            Log.m1301e("VcpcsManager", "token=" + Utils.m1285o(trackData.ii()));
                            Log.m1301e("VcpcsManager", "atc=" + Utils.m1285o(trackData.in()));
                            Log.m1301e("VcpcsManager", "cvv=" + Utils.m1285o(trackData.io()));
                            Log.m1301e("VcpcsManager", "cc=" + Utils.m1285o(trackData.im()));
                            Log.m1301e("VcpcsManager", "svccode=" + Utils.m1285o(trackData.ik()));
                            Log.m1301e("VcpcsManager", "timestamp=" + Utils.m1285o(trackData.il()));
                            Log.m1301e("VcpcsManager", "expiredate=" + Utils.m1285o(trackData.ij()));
                            Log.m1301e("VcpcsManager", "reservedBytes=" + Utils.m1285o(trackData.ip()));
                        }
                        z = this.Dl.m1296a(this.Fz, trackData);
                        if (z) {
                            TVL tvl = new TVL();
                            tvl.setTokenKey(this.FK);
                            tvl.setUnpredictableNumber(BuildConfig.FLAVOR);
                            tvl.setTimeStamp(Utils.fU());
                            tvl.setTransactionType("S");
                            tvl.setAtc(this.Fw);
                            tvl.setApi(Utils.m1285o(this.Fx));
                            if (this.FD == null) {
                                this.FD = new ArrayList();
                            }
                            this.FD.add(tvl);
                        }
                    } catch (VisaTAException e) {
                        e.printStackTrace();
                        Log.m1301e("VcpcsManager", "cannot prepare MST data");
                    }
                }
            } catch (VisaTAException e2) {
                Log.m1301e("VcpcsManager", "cannot retrieve token for MST");
                e2.printStackTrace();
            }
        }
        return z;
    }

    public boolean shouldTapAndGo(TokenKey tokenKey) {
        return this.Gd;
    }

    private void m1318a(ICC icc) {
        Log.m1300d("VcpcsManager", "constructEncICCPrivateKey");
        Object encICCPrivateKey = new EncICCPrivateKey();
        ICCCRTPrivateKey iCCCRTPrivateKey = new ICCCRTPrivateKey();
        iCCCRTPrivateKey.setPrimeP(icc.getIccCRTprimep());
        iCCCRTPrivateKey.setPrimeQ(icc.getIccCRTprimeq());
        iCCCRTPrivateKey.setExponent(icc.getIccPrivKExpo());
        iCCCRTPrivateKey.setCoefDmodP(icc.getIccCRTCoeffDModP());
        iCCCRTPrivateKey.setCoefDmodQ(icc.getIccCRTCoeffDModQ());
        iCCCRTPrivateKey.setCoefQinvModP(icc.getIccCRTCoeffQModP());
        iCCCRTPrivateKey.setModulus(icc.getIccKeymod());
        encICCPrivateKey.setIccCRTPrivateKey(iCCCRTPrivateKey);
        icc.setEncIccCRTPrivKey(this.Dl.m1295a((byte) 26, new GsonBuilder().excludeFieldsWithModifiers(X509KeyUsage.digitalSignature).create().toJson(encICCPrivateKey)));
    }

    private byte[] gm() {
        Log.m1300d("VcpcsManager", "constructSDADTransactionData");
        ByteBuffer allocate = ByteBuffer.allocate(SkeinMac.SKEIN_256);
        if (this.Fh == null || this.Fh.length == 0) {
            Log.m1301e("VcpcsManager", "sQVSDCData_withODA_iccPubKExpo null");
            return null;
        }
        allocate.put((byte) this.Fh.length);
        allocate.put(this.Fh);
        allocate.put((byte) -107);
        allocate.put((byte) 1);
        byte[] bArr = new byte[2];
        VcpcsManager.m1323c(this.Fw, bArr);
        if (bArr.length != 2) {
            Log.m1301e("VcpcsManager", "ATC length is not 2");
            return null;
        }
        allocate.put((byte) 3);
        allocate.put((byte) 2);
        allocate.put(bArr);
        if (this.FY == null) {
            Log.m1301e("VcpcsManager", "sUnpredictableNumber null");
            return null;
        }
        bArr = Utils.hexStringToBytes(this.FY);
        if (bArr == null || bArr.length != 4) {
            Log.m1301e("VcpcsManager", "sUnpredictableNumber size not 4");
            return null;
        }
        allocate.put(bArr);
        Log.m1300d("VcpcsManager", "UN len " + bArr.length);
        if (this.FZ == null) {
            Log.m1301e("VcpcsManager", "sUnpredictableNumber null");
            return null;
        }
        bArr = Utils.hexStringToBytes(this.FZ);
        if (bArr == null || bArr.length != 6) {
            Log.m1301e("VcpcsManager", "authorizedAmount size not 6:");
            return null;
        }
        allocate.put(bArr);
        Log.m1300d("VcpcsManager", "authorizedAmount len " + bArr.length);
        if (this.Ga == null) {
            Log.m1301e("VcpcsManager", "sCurrencyCode null");
            return null;
        }
        bArr = Utils.hexStringToBytes(this.Ga);
        if (bArr == null || bArr.length != 2) {
            Log.m1301e("VcpcsManager", "currencyCode size not 2:");
            return null;
        }
        Log.m1300d("VcpcsManager", "currencyCode len " + bArr.length);
        allocate.put(bArr);
        allocate.put(this.Fd);
        int position = allocate.position();
        byte[] bArr2 = new byte[position];
        allocate.position(0);
        allocate.get(bArr2, 0, position);
        Log.m1300d("VcpcsManager", "SDAD input data length " + bArr2.length);
        Log.m1300d("VcpcsManager", "SDAD input data :" + Utils.m1285o(bArr2));
        return bArr2;
    }

    private byte[] m1314V(int i) {
        byte[] bArr = new byte[8];
        Utils.m1281a(Constant.Dq, 0, bArr, 0, Constant.Dq.length);
        int i2 = i % 10000;
        bArr[1] = (byte) (i2 % 10);
        i2 /= 10;
        bArr[1] = (byte) (bArr[1] + ((byte) ((i2 % 10) * 16)));
        i2 /= 10;
        bArr[0] = (byte) (i2 % 10);
        bArr[0] = (byte) (((byte) (((i2 / 10) % 10) * 16)) + bArr[0]);
        Utils.m1283e("MSDVV input=", bArr);
        return bArr;
    }

    public void processInAppTransactionComplete(TokenKey tokenKey, String str, boolean z) {
        Log.m1300d("VcpcsManager", "processInAppTransactionComplete " + z);
        if (z) {
            if (this.Et != null) {
                TVL tvl = new TVL();
                tvl.setTokenKey(this.FK);
                tvl.setTimeStamp(Utils.fU());
                tvl.setUnpredictableNumber(BuildConfig.FLAVOR);
                tvl.setAtc(this.Fw);
                tvl.setApi(Utils.m1285o(this.Fx));
                tvl.setTransactionType("I");
                this.Et.m1374a(tvl);
            } else {
                Log.m1301e("VcpcsManager", "cannot access db");
            }
            this.FB--;
            if (this.Es != null) {
                this.Es.m1365a(tokenKey, this.Fw, this.FB);
            } else {
                Log.m1301e("VcpcsManager", "visa db helper is null");
            }
        }
    }

    public PaymentDataRequest constructPaymentDataRequest(String str, TokenKey tokenKey, String str2, String str3) {
        PaymentDataRequest paymentDataRequest = new PaymentDataRequest();
        paymentDataRequest.setClientPaymentDataID(str2);
        paymentDataRequest.setvProvisionedTokenID(TransactionInfo.VISA_TRANSACTIONTYPE_REFUND);
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setTransactionType(str3);
        paymentDataRequest.setPaymentRequest(paymentRequest);
        this.Fw++;
        if (this.Es != null) {
            this.Es.m1365a(tokenKey, this.Fw, this.FB);
        } else {
            Log.m1301e("VcpcsManager", "visa db helper is null");
        }
        Log.m1300d("VcpcsManager", "constructPaymentDataRequest update ATC counter to " + this.Fw);
        paymentDataRequest.setAtc(Integer.toString(this.Fw));
        return paymentDataRequest;
    }

    private VcpcsManager m1311G(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            Log.m1301e("VcpcsManager", "generated generateQVSDCandMSDVV is null");
            return null;
        }
        short s = (short) bArr[0];
        short s2 = (short) 1;
        if (bArr.length < s + s2 || s <= (short) 0) {
            Log.m1301e("VcpcsManager", "ARQC data length is not satisfied");
            return null;
        }
        byte[] bArr2 = new byte[s];
        Utils.m1281a(bArr, s2, bArr2, 0, s);
        s2 = (short) (s + s2);
        VcpcsManager vcpcsManager = new VcpcsManager();
        vcpcsManager.m1308J(bArr2);
        if (bArr.length > s2) {
            short s3 = (short) bArr[s2];
            s2 = (short) (s2 + 1);
            if (bArr.length < s3 + s2 || s3 <= (short) 0) {
                Log.m1301e("VcpcsManager", "MSDVV data length is not satisfied");
                return null;
            }
            byte[] bArr3 = new byte[s3];
            Utils.m1281a(bArr, s2, bArr3, 0, s3);
            vcpcsManager.m1307I(bArr3);
        } else {
            Log.m1300d("VcpcsManager", "MSDVV output not avail");
        }
        return vcpcsManager;
    }

    private VcpcsManager m1312H(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            Log.m1301e("VcpcsManager", "SDAD result not satisfied ");
            return null;
        }
        Log.m1300d("VcpcsManager", "result lenght " + bArr.length);
        short s = (short) bArr[0];
        short s2 = (short) 1;
        Log.m1300d("VcpcsManager", "UN len " + s);
        if (s <= (short) 0 || bArr.length < s2 + s) {
            Log.m1301e("VcpcsManager", "UN length error result length " + s);
            return null;
        }
        byte[] bArr2 = new byte[s];
        Utils.m1281a(bArr, s2, bArr2, 0, s);
        VcpcsManager vcpcsManager = new VcpcsManager();
        vcpcsManager.m1309K(bArr2);
        s = (short) (s + s2);
        if (bArr.length < s + 2) {
            Log.m1301e("VcpcsManager", "length error result length not able to retrieve signature len ");
            return null;
        }
        s2 = Utils.getShort(bArr, s);
        s = (short) (s + 2);
        if (bArr.length < s + s2) {
            Log.m1301e("VcpcsManager", "Signature length error result length " + s2);
            return null;
        }
        byte[] bArr3 = new byte[s2];
        Utils.m1281a(bArr, s, bArr3, 0, s2);
        s = (short) (s + s2);
        vcpcsManager.m1310L(bArr3);
        return vcpcsManager;
    }

    private boolean gn() {
        if (this.Gj == null) {
            return false;
        }
        return this.Gj.getBoolean(TransactionStatus.EXTRA_PDOL_VALUES);
    }
}
