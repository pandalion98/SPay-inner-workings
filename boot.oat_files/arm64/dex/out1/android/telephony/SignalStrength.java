package android.telephony;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.view.WindowManagerPolicy;
import com.android.internal.R;
import com.android.internal.os.PowerProfile;
import com.android.internal.telephony.TelephonyProperties;
import com.samsung.android.brnne.MemComp;
import com.samsung.android.smartface.SmartFaceManager;
import com.sec.android.app.CscFeature;

public class SignalStrength implements Parcelable {
    public static final Creator<SignalStrength> CREATOR = new Creator() {
        public SignalStrength createFromParcel(Parcel in) {
            return new SignalStrength(in);
        }

        public SignalStrength[] newArray(int size) {
            return new SignalStrength[size];
        }
    };
    private static final boolean DBG = false;
    public static final int INVALID = Integer.MAX_VALUE;
    private static final String LOG_TAG = "SignalStrength";
    public static int NUM_SIGNAL_STRENGTH_BINS = InitializeSignalBins();
    private static final int[] RSRP_THRESH_CHINA = new int[]{-140, -123, -118, -114, -105, -44};
    private static final int[] RSRP_THRESH_LENIENT = new int[]{-140, -128, -118, -108, -98, -44};
    private static final int[] RSRP_THRESH_STRICT = new int[]{-140, -115, -105, -95, -85, -44};
    private static final int RSRP_THRESH_TYPE_STRICT = 0;
    public static final int SIGNAL_STRENGTH_GOOD = 3;
    public static final int SIGNAL_STRENGTH_GREAT = 4;
    public static final int SIGNAL_STRENGTH_MODERATE = 2;
    public static final String[] SIGNAL_STRENGTH_NAMES = InitializeSignalNames();
    public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    public static final int SIGNAL_STRENGTH_POOR = 1;
    private static String mConfigNetworkTypeCapability = CscFeature.getInstance().getString("CscFeature_RIL_ConfigNetworkTypeCapability");
    private boolean isGsm;
    private int mCdmaDbm;
    private int mCdmaEcio;
    private int mEvdoDbm;
    private int mEvdoEcio;
    private int mEvdoSnr;
    private int mGsmBitErrorRate;
    private int mGsmSignalStrength;
    private int mLteCqi;
    private int mLteRsrp;
    private int mLteRsrq;
    private int mLteRssnr;
    private int mLteSignalStrength;
    private int mSignalBar;
    private int mTdScdmaRscp;

    public static SignalStrength newFromBundle(Bundle m) {
        SignalStrength ret = new SignalStrength();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    private static int InitializeSignalBins() {
        if (SmartFaceManager.PAGE_BOTTOM.equals(SmartFaceManager.PAGE_MIDDLE) || SmartFaceManager.PAGE_BOTTOM.equals(CscFeature.getInstance().getString("CscFeature_RIL_SignalstrengthPolicy"))) {
            return 6;
        }
        return 5;
    }

    private static String[] InitializeSignalNames() {
        if (SmartFaceManager.PAGE_BOTTOM.equals(SmartFaceManager.PAGE_MIDDLE) || SmartFaceManager.PAGE_BOTTOM.equals(CscFeature.getInstance().getString("CscFeature_RIL_SignalstrengthPolicy"))) {
            return new String[]{PowerProfile.POWER_NONE, "poor", "moderate", "good", "great", "excellent"};
        }
        return new String[]{PowerProfile.POWER_NONE, "poor", "moderate", "good", "great"};
    }

    public boolean isCdmaRatOnAllRatSupport() {
        boolean ret = false;
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") && !CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) {
            return false;
        }
        if (!"5".equals(SmartFaceManager.PAGE_MIDDLE) && !"5".equals(CscFeature.getInstance().getString("CscFeature_RIL_SignalstrengthPolicy"))) {
            return false;
        }
        if (!isGsm()) {
            ret = true;
        }
        return ret;
    }

    public SignalStrength() {
        this.mGsmSignalStrength = 99;
        this.mGsmBitErrorRate = -1;
        this.mCdmaDbm = -1;
        this.mCdmaEcio = -1;
        this.mEvdoDbm = -1;
        this.mEvdoEcio = -1;
        this.mEvdoSnr = -1;
        this.mLteSignalStrength = 99;
        this.mLteRsrp = Integer.MAX_VALUE;
        this.mLteRsrq = Integer.MAX_VALUE;
        this.mLteRssnr = Integer.MAX_VALUE;
        this.mLteCqi = Integer.MAX_VALUE;
        this.mTdScdmaRscp = Integer.MAX_VALUE;
        this.isGsm = true;
        this.mSignalBar = 0;
    }

    public SignalStrength(boolean gsmFlag) {
        this.mGsmSignalStrength = 99;
        this.mGsmBitErrorRate = -1;
        this.mCdmaDbm = -1;
        this.mCdmaEcio = -1;
        this.mEvdoDbm = -1;
        this.mEvdoEcio = -1;
        this.mEvdoSnr = -1;
        this.mLteSignalStrength = 99;
        this.mLteRsrp = Integer.MAX_VALUE;
        this.mLteRsrq = Integer.MAX_VALUE;
        this.mLteRssnr = Integer.MAX_VALUE;
        this.mLteCqi = Integer.MAX_VALUE;
        this.mTdScdmaRscp = Integer.MAX_VALUE;
        this.isGsm = gsmFlag;
        this.mSignalBar = 0;
    }

    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, int tdScdmaRscp, boolean gsmFlag) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, gsmFlag);
        this.mTdScdmaRscp = tdScdmaRscp;
    }

    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, boolean gsmFlag) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, gsmFlag);
    }

    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, boolean gsmFlag, int signalBar) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, gsmFlag);
        this.mSignalBar = signalBar;
    }

    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, int tdScdmaRscp, boolean gsmFlag, int signalBar) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, lteSignalStrength, lteRsrp, lteRsrq, lteRssnr, lteCqi, gsmFlag);
        this.mTdScdmaRscp = tdScdmaRscp;
        this.mSignalBar = signalBar;
    }

    public SignalStrength(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, boolean gsmFlag) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, 99, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, gsmFlag);
    }

    public SignalStrength(SignalStrength s) {
        copyFrom(s);
    }

    public void initialize(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, boolean gsm) {
        initialize(gsmSignalStrength, gsmBitErrorRate, cdmaDbm, cdmaEcio, evdoDbm, evdoEcio, evdoSnr, 99, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, gsm);
    }

    public void initialize(int gsmSignalStrength, int gsmBitErrorRate, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr, int lteSignalStrength, int lteRsrp, int lteRsrq, int lteRssnr, int lteCqi, boolean gsm) {
        this.mGsmSignalStrength = gsmSignalStrength;
        this.mGsmBitErrorRate = gsmBitErrorRate;
        this.mCdmaDbm = cdmaDbm;
        this.mCdmaEcio = cdmaEcio;
        this.mEvdoDbm = evdoDbm;
        this.mEvdoEcio = evdoEcio;
        this.mEvdoSnr = evdoSnr;
        this.mLteSignalStrength = lteSignalStrength;
        this.mLteRsrp = lteRsrp;
        this.mLteRsrq = lteRsrq;
        this.mLteRssnr = lteRssnr;
        this.mLteCqi = lteCqi;
        this.mTdScdmaRscp = Integer.MAX_VALUE;
        this.isGsm = gsm;
        this.mSignalBar = 0;
    }

    protected void copyFrom(SignalStrength s) {
        this.mGsmSignalStrength = s.mGsmSignalStrength;
        this.mGsmBitErrorRate = s.mGsmBitErrorRate;
        this.mCdmaDbm = s.mCdmaDbm;
        this.mCdmaEcio = s.mCdmaEcio;
        this.mEvdoDbm = s.mEvdoDbm;
        this.mEvdoEcio = s.mEvdoEcio;
        this.mEvdoSnr = s.mEvdoSnr;
        this.mLteSignalStrength = s.mLteSignalStrength;
        this.mLteRsrp = s.mLteRsrp;
        this.mLteRsrq = s.mLteRsrq;
        this.mLteRssnr = s.mLteRssnr;
        this.mLteCqi = s.mLteCqi;
        this.mTdScdmaRscp = s.mTdScdmaRscp;
        this.isGsm = s.isGsm;
        this.mSignalBar = s.mSignalBar;
    }

    public SignalStrength(Parcel in) {
        this.mGsmSignalStrength = in.readInt();
        this.mGsmBitErrorRate = in.readInt();
        this.mCdmaDbm = in.readInt();
        this.mCdmaEcio = in.readInt();
        this.mEvdoDbm = in.readInt();
        this.mEvdoEcio = in.readInt();
        this.mEvdoSnr = in.readInt();
        this.mLteSignalStrength = in.readInt();
        this.mLteRsrp = in.readInt();
        this.mLteRsrq = in.readInt();
        this.mLteRssnr = in.readInt();
        this.mLteCqi = in.readInt();
        this.mTdScdmaRscp = in.readInt();
        this.isGsm = in.readInt() != 0;
        this.mSignalBar = in.readInt();
    }

    public static SignalStrength makeSignalStrengthFromRilParcel(Parcel in) {
        SignalStrength ss = new SignalStrength();
        ss.mGsmSignalStrength = in.readInt();
        ss.mGsmBitErrorRate = in.readInt();
        ss.mCdmaDbm = in.readInt();
        ss.mCdmaEcio = in.readInt();
        ss.mEvdoDbm = in.readInt();
        ss.mEvdoEcio = in.readInt();
        ss.mEvdoSnr = in.readInt();
        ss.mLteSignalStrength = in.readInt();
        ss.mLteRsrp = in.readInt();
        ss.mLteRsrq = in.readInt();
        ss.mLteRssnr = in.readInt();
        ss.mLteCqi = in.readInt();
        ss.mTdScdmaRscp = in.readInt();
        return ss;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mGsmSignalStrength);
        out.writeInt(this.mGsmBitErrorRate);
        out.writeInt(this.mCdmaDbm);
        out.writeInt(this.mCdmaEcio);
        out.writeInt(this.mEvdoDbm);
        out.writeInt(this.mEvdoEcio);
        out.writeInt(this.mEvdoSnr);
        out.writeInt(this.mLteSignalStrength);
        out.writeInt(this.mLteRsrp);
        out.writeInt(this.mLteRsrq);
        out.writeInt(this.mLteRssnr);
        out.writeInt(this.mLteCqi);
        out.writeInt(this.mTdScdmaRscp);
        out.writeInt(this.isGsm ? 1 : 0);
        out.writeInt(this.mSignalBar);
    }

    public int describeContents() {
        return 0;
    }

    public void validateInput() {
        int i;
        int i2 = -120;
        int i3 = 99;
        int i4 = -1;
        int i5 = Integer.MAX_VALUE;
        log("Signal before validate=" + this);
        if (!isCdmaRatOnAllRatSupport()) {
            if (this.mGsmBitErrorRate == -1) {
                this.mSignalBar = 0;
                this.mGsmBitErrorRate = 99;
            } else {
                this.mSignalBar = (this.mGsmBitErrorRate & WindowManagerPolicy.WINDOW_MODE_OPTION_COMMON_MASK) >> 8;
                this.mGsmBitErrorRate &= 255;
            }
        }
        if (this.mGsmSignalStrength >= 0) {
            i = this.mGsmSignalStrength;
        } else {
            i = 99;
        }
        this.mGsmSignalStrength = i;
        if (this.mCdmaDbm > 0) {
            i = -this.mCdmaDbm;
        } else {
            i = -120;
        }
        this.mCdmaDbm = i;
        this.mCdmaEcio = this.mCdmaEcio > 0 ? -this.mCdmaEcio : -160;
        if (this.mEvdoDbm > 0) {
            i2 = -this.mEvdoDbm;
        }
        this.mEvdoDbm = i2;
        if (this.mEvdoEcio >= 0) {
            i = -this.mEvdoEcio;
        } else {
            i = -1;
        }
        this.mEvdoEcio = i;
        if (this.mEvdoSnr > 0 && this.mEvdoSnr <= 8) {
            i4 = this.mEvdoSnr;
        }
        this.mEvdoSnr = i4;
        if (this.mLteSignalStrength >= 0) {
            i3 = this.mLteSignalStrength;
        }
        this.mLteSignalStrength = i3;
        if (this.mLteRsrp < 44 || this.mLteRsrp > 140) {
            i = Integer.MAX_VALUE;
        } else {
            i = -this.mLteRsrp;
        }
        this.mLteRsrp = i;
        if (this.mLteRsrq < 3 || this.mLteRsrq > 20) {
            i = Integer.MAX_VALUE;
        } else {
            i = -this.mLteRsrq;
        }
        this.mLteRsrq = i;
        if (this.mLteRssnr < -200 || this.mLteRssnr > 300) {
            i = Integer.MAX_VALUE;
        } else {
            i = this.mLteRssnr;
        }
        this.mLteRssnr = i;
        if (this.mTdScdmaRscp >= 25 && this.mTdScdmaRscp <= 120) {
            i5 = -this.mTdScdmaRscp;
        }
        this.mTdScdmaRscp = i5;
        log("Signal after validate=" + this);
    }

    public void setGsm(boolean gsmFlag) {
        this.isGsm = gsmFlag;
    }

    public int getGsmSignalStrength() {
        return this.mGsmSignalStrength;
    }

    public int getGsmBitErrorRate() {
        return this.mGsmBitErrorRate;
    }

    public int getCdmaDbm() {
        return this.mCdmaDbm;
    }

    public int getCdmaEcio() {
        return this.mCdmaEcio;
    }

    public int getEvdoDbm() {
        return this.mEvdoDbm;
    }

    public int getEvdoEcio() {
        return this.mEvdoEcio;
    }

    public int getEvdoSnr() {
        return this.mEvdoSnr;
    }

    public int getLteSignalStrength() {
        return this.mLteSignalStrength;
    }

    public int getLteRsrp() {
        return this.mLteRsrp;
    }

    public int getLteRsrq() {
        return this.mLteRsrq;
    }

    public int getLteRssnr() {
        return this.mLteRssnr;
    }

    public int getLteCqi() {
        return this.mLteCqi;
    }

    public void setSignalBars() {
    }

    public void setSignalBarsLTE() {
    }

    public void setSignalBarsVolte(boolean isVolteSupported) {
    }

    public void setSignalBarsLTE(int rilRadioTechnology, int ssRilRadioTechnology, boolean isVolteSupported) {
    }

    public int getAlternateLteLevel() {
        if (this.mLteRsrp > -44) {
            return -1;
        }
        if (this.mLteRsrp >= -97) {
            return 4;
        }
        if (this.mLteRsrp >= -105) {
            return 3;
        }
        if (this.mLteRsrp >= -113) {
            return 2;
        }
        if (this.mLteRsrp >= -120) {
            return 1;
        }
        if (this.mLteRsrp >= -140) {
            return 0;
        }
        return 0;
    }

    public int getLevel() {
        int level;
        if (this.isGsm) {
            level = getLteLevel();
            if (level == 0) {
                level = getTdScdmaLevel();
                if (level == 0) {
                    level = getGsmLevel();
                }
            }
            if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_CdmalteTelephonyCommon")) {
                boolean displayCdmaLevel = false;
                if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_NwCdmaCsfb") && level == 0) {
                    displayCdmaLevel = true;
                }
                if (SystemProperties.getInt(TelephonyProperties.CURRENT_ACTIVE_PHONE, 2) == 2 && level == 0) {
                    displayCdmaLevel = true;
                }
                if (displayCdmaLevel) {
                    level = getCdmaLevel();
                }
            }
        } else {
            int cdmaLevel = getCdmaLevel();
            int evdoLevel = getEvdoLevel();
            if (evdoLevel == 0) {
                level = cdmaLevel;
            } else if (cdmaLevel == 0) {
                level = evdoLevel;
            } else {
                if (isCdmaRatOnAllRatSupport()) {
                    level = cdmaLevel;
                } else {
                    level = cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
                }
                if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("VZW-")) {
                    level = evdoLevel;
                }
                if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("USC-USC-")) {
                    level = cdmaLevel;
                }
            }
            if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_NwCdmaCsfb") && level == 0) {
                level = getLteLevel();
            }
        }
        if (level >= NUM_SIGNAL_STRENGTH_BINS) {
            return NUM_SIGNAL_STRENGTH_BINS - 1;
        }
        return level;
    }

    public int getSignalBar() {
        int dbgBar = SystemProperties.getInt("ril.dbg.SignalBar", -1);
        if (dbgBar > 0) {
            return dbgBar;
        }
        return this.mSignalBar >= 0 ? this.mSignalBar : 0;
    }

    public int getGsmSignalBar() {
        return getSignalBar();
    }

    public int getAsuLevel() {
        if (!this.isGsm) {
            int cdmaAsuLevel = getCdmaAsuLevel();
            int evdoAsuLevel = getEvdoAsuLevel();
            if (evdoAsuLevel == 0) {
                return cdmaAsuLevel;
            }
            if (cdmaAsuLevel == 0) {
                return evdoAsuLevel;
            }
            int asuLevel = cdmaAsuLevel < evdoAsuLevel ? cdmaAsuLevel : evdoAsuLevel;
            if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("VZW-")) {
                asuLevel = evdoAsuLevel;
            }
            if (mConfigNetworkTypeCapability == null || !mConfigNetworkTypeCapability.startsWith("USC-USC-")) {
                return asuLevel;
            }
            return cdmaAsuLevel;
        } else if (getLteLevel() != 0 || getLteDbm() != Integer.MAX_VALUE) {
            return getLteAsuLevel();
        } else {
            if (getTdScdmaLevel() == 0) {
                return getGsmAsuLevel();
            }
            return getTdScdmaAsuLevel();
        }
    }

    public int getDbm() {
        if (isGsm()) {
            int dBm = getLteDbm();
            if (dBm == Integer.MAX_VALUE) {
                if (getTdScdmaLevel() == 0) {
                    dBm = getGsmDbm();
                } else {
                    dBm = getTdScdmaDbm();
                }
            }
            return dBm;
        }
        int cdmaDbm = getCdmaDbm();
        int evdoDbm = getEvdoDbm();
        if (mConfigNetworkTypeCapability == null || !mConfigNetworkTypeCapability.startsWith("VZW-")) {
            if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("USC-USC-")) {
                if (cdmaDbm != -120) {
                    evdoDbm = cdmaDbm;
                }
                return evdoDbm;
            } else if (evdoDbm == -120) {
                return cdmaDbm;
            } else {
                if (cdmaDbm == -120) {
                    return evdoDbm;
                }
                if (cdmaDbm >= evdoDbm) {
                    return evdoDbm;
                }
                return cdmaDbm;
            }
        } else if (evdoDbm != -120) {
            return evdoDbm;
        } else {
            return cdmaDbm;
        }
    }

    public int getGsmDbm() {
        int asu;
        int gsmSignalStrength = getGsmSignalStrength();
        if (gsmSignalStrength == 99) {
            asu = -1;
        } else {
            asu = gsmSignalStrength;
        }
        if (asu != -1) {
            return (asu * 2) - 113;
        }
        return -1;
    }

    public int getGsmLevel() {
        int level;
        int asu = getGsmSignalStrength();
        if (asu <= 2 || asu == 99) {
            level = 0;
        } else if (asu >= 12) {
            level = 4;
        } else if (asu >= 8) {
            level = 3;
        } else if (asu >= 5) {
            level = 2;
        } else {
            level = 1;
        }
        if (isCdmaRatOnAllRatSupport()) {
            return level;
        }
        return getSignalBar() & 15;
    }

    public int getSglteGsmLevel() {
        return 0;
    }

    public int getSglteRssiDisplayNum() {
        return 0;
    }

    public int getGsmAsuLevel() {
        return getGsmSignalStrength();
    }

    public int getCdmaLevel() {
        int levelDbm;
        int levelEcio;
        int level;
        int cdmaDbm = getCdmaDbm();
        int cdmaEcio = getCdmaEcio();
        if ("CTC".equals(SystemProperties.get("ro.csc.sales_code")) || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) {
            if (cdmaDbm >= -85) {
                levelDbm = 4;
            } else if (cdmaDbm >= -95) {
                levelDbm = 3;
            } else if (cdmaDbm >= MemComp.ERR_OUT_OF_MEMORY) {
                levelDbm = 2;
            } else if (cdmaDbm >= -108) {
                levelDbm = 1;
            } else {
                levelDbm = 0;
            }
        } else if (cdmaDbm >= -75) {
            levelDbm = 4;
        } else if (cdmaDbm >= -85) {
            levelDbm = 3;
        } else if (cdmaDbm >= -95) {
            levelDbm = 2;
        } else if (cdmaDbm >= -100) {
            levelDbm = 1;
        } else {
            levelDbm = 0;
        }
        if (cdmaEcio >= -90) {
            levelEcio = 4;
        } else if (cdmaEcio >= -110) {
            levelEcio = 3;
        } else if (cdmaEcio >= -130) {
            levelEcio = 2;
        } else if (cdmaEcio >= -150) {
            levelEcio = 1;
        } else {
            levelEcio = 0;
        }
        if (levelDbm < levelEcio) {
            level = levelDbm;
        } else {
            level = levelEcio;
        }
        if ("CTC".equals(SystemProperties.get("ro.csc.sales_code")) || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) {
            if (levelDbm > levelEcio) {
                level = levelDbm;
            } else {
                level = levelEcio;
            }
        }
        if ((CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) && ("5".equals(SmartFaceManager.PAGE_MIDDLE) || "5".equals(CscFeature.getInstance().getString("CscFeature_RIL_SignalstrengthPolicy")))) {
            return level;
        }
        return (getSignalBar() & 240) >> 4;
    }

    public int getCdmaAsuLevel() {
        int cdmaAsuLevel;
        int ecioAsuLevel;
        int cdmaDbm = getCdmaDbm();
        int cdmaEcio = getCdmaEcio();
        if (cdmaDbm >= -75) {
            cdmaAsuLevel = 16;
        } else if (cdmaDbm >= -82) {
            cdmaAsuLevel = 8;
        } else if (cdmaDbm >= -90) {
            cdmaAsuLevel = 4;
        } else if (cdmaDbm >= -95) {
            cdmaAsuLevel = 2;
        } else if (cdmaDbm >= -100) {
            cdmaAsuLevel = 1;
        } else {
            cdmaAsuLevel = 99;
        }
        if (cdmaEcio >= -90) {
            ecioAsuLevel = 16;
        } else if (cdmaEcio >= -100) {
            ecioAsuLevel = 8;
        } else if (cdmaEcio >= -115) {
            ecioAsuLevel = 4;
        } else if (cdmaEcio >= -130) {
            ecioAsuLevel = 2;
        } else if (cdmaEcio >= -150) {
            ecioAsuLevel = 1;
        } else {
            ecioAsuLevel = 99;
        }
        if (cdmaAsuLevel < ecioAsuLevel) {
            return cdmaAsuLevel;
        }
        return ecioAsuLevel;
    }

    public int getEvdoLevel() {
        int levelEvdoDbm;
        int levelEvdoSnr;
        int level;
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        if ("CTC".equals(SystemProperties.get("ro.csc.sales_code")) || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) {
            if (evdoDbm >= -85) {
                levelEvdoDbm = 4;
            } else if (evdoDbm >= -95) {
                levelEvdoDbm = 3;
            } else if (evdoDbm >= MemComp.ERR_OUT_OF_MEMORY) {
                levelEvdoDbm = 2;
            } else if (evdoDbm >= -108) {
                levelEvdoDbm = 1;
            } else {
                levelEvdoDbm = 0;
            }
        } else if (evdoDbm >= -65) {
            levelEvdoDbm = 4;
        } else if (evdoDbm >= -75) {
            levelEvdoDbm = 3;
        } else if (evdoDbm >= -90) {
            levelEvdoDbm = 2;
        } else if (evdoDbm >= -105) {
            levelEvdoDbm = 1;
        } else {
            levelEvdoDbm = 0;
        }
        if (evdoSnr >= 7) {
            levelEvdoSnr = 4;
        } else if (evdoSnr >= 5) {
            levelEvdoSnr = 3;
        } else if (evdoSnr >= 3) {
            levelEvdoSnr = 2;
        } else if (evdoSnr >= 1) {
            levelEvdoSnr = 1;
        } else {
            levelEvdoSnr = 0;
        }
        if (levelEvdoDbm < levelEvdoSnr) {
            level = levelEvdoDbm;
        } else {
            level = levelEvdoSnr;
        }
        if ("CTC".equals(SystemProperties.get("ro.csc.sales_code")) || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) {
            if (levelEvdoDbm > levelEvdoSnr) {
                level = levelEvdoDbm;
            } else {
                level = levelEvdoSnr;
            }
        }
        if ((CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) && ("5".equals(SmartFaceManager.PAGE_MIDDLE) || "5".equals(CscFeature.getInstance().getString("CscFeature_RIL_SignalstrengthPolicy")))) {
            return level;
        }
        return (getSignalBar() & 3840) >> 8;
    }

    public int getEvdoAsuLevel() {
        int levelEvdoDbm;
        int levelEvdoSnr;
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        if (evdoDbm >= -65) {
            levelEvdoDbm = 16;
        } else if (evdoDbm >= -75) {
            levelEvdoDbm = 8;
        } else if (evdoDbm >= -85) {
            levelEvdoDbm = 4;
        } else if (evdoDbm >= -95) {
            levelEvdoDbm = 2;
        } else if (evdoDbm >= -105) {
            levelEvdoDbm = 1;
        } else {
            levelEvdoDbm = 99;
        }
        if (evdoSnr >= 7) {
            levelEvdoSnr = 16;
        } else if (evdoSnr >= 6) {
            levelEvdoSnr = 8;
        } else if (evdoSnr >= 5) {
            levelEvdoSnr = 4;
        } else if (evdoSnr >= 3) {
            levelEvdoSnr = 2;
        } else if (evdoSnr >= 1) {
            levelEvdoSnr = 1;
        } else {
            levelEvdoSnr = 99;
        }
        if (levelEvdoDbm < levelEvdoSnr) {
            return levelEvdoDbm;
        }
        return levelEvdoSnr;
    }

    public int getLteDbm() {
        return this.mLteRsrp;
    }

    public int getLteLevel() {
        int[] threshRsrp;
        int rsrpIconLevel = -1;
        if (Resources.getSystem().getInteger(R.integer.config_LTE_RSRP_threshold_type) == 0) {
            threshRsrp = RSRP_THRESH_STRICT;
        } else {
            threshRsrp = RSRP_THRESH_LENIENT;
        }
        if ("CTC".equals(SystemProperties.get("ro.csc.sales_code")) || isCdmaRatOnAllRatSupport()) {
            threshRsrp = RSRP_THRESH_CHINA;
        }
        if (this.mLteRsrp > threshRsrp[5]) {
            rsrpIconLevel = -1;
        } else if (this.mLteRsrp >= threshRsrp[4]) {
            rsrpIconLevel = 4;
        } else if (this.mLteRsrp >= threshRsrp[3]) {
            rsrpIconLevel = 3;
        } else if (this.mLteRsrp >= threshRsrp[2]) {
            rsrpIconLevel = 2;
        } else if (this.mLteRsrp >= threshRsrp[1]) {
            rsrpIconLevel = 1;
        } else if (this.mLteRsrp >= threshRsrp[0]) {
            rsrpIconLevel = 0;
        }
        if ("CTC".equals(SystemProperties.get("ro.csc.sales_code")) || isCdmaRatOnAllRatSupport()) {
            if (this.mLteRssnr <= 300) {
                if (this.mLteRssnr < 90) {
                    if (this.mLteRssnr < 10) {
                        if (this.mLteRssnr < -30) {
                            if (this.mLteRssnr < -50) {
                                if (this.mLteRssnr >= -200) {
                                }
                            }
                        }
                    }
                }
            }
        } else if (this.mLteRssnr <= 300) {
            if (this.mLteRssnr < 130) {
                if (this.mLteRssnr < 45) {
                    if (this.mLteRssnr < 10) {
                        if (this.mLteRssnr < -30) {
                            if (this.mLteRssnr >= -200) {
                            }
                        }
                    }
                }
            }
        }
        if (mConfigNetworkTypeCapability != null && mConfigNetworkTypeCapability.startsWith("USC-USC-")) {
            return (getSignalBar() & 240) >> 4;
        }
        if (isCdmaRatOnAllRatSupport()) {
            return rsrpIconLevel;
        }
        return (getSignalBar() & 61440) >> 12;
    }

    public int getLteAsuLevel() {
        int lteDbm = getLteDbm();
        if (lteDbm == Integer.MAX_VALUE) {
            return 255;
        }
        return lteDbm + 140;
    }

    public boolean isGsm() {
        return this.isGsm;
    }

    public int getTdScdmaDbm() {
        return this.mTdScdmaRscp;
    }

    public int getTdScdmaLevel() {
        int level;
        int tdScdmaDbm = getTdScdmaDbm();
        if (tdScdmaDbm > -25 || tdScdmaDbm == Integer.MAX_VALUE) {
            level = 0;
        } else if (tdScdmaDbm >= -88) {
            level = 4;
        } else if (tdScdmaDbm >= -92) {
            level = 3;
        } else if (tdScdmaDbm >= -97) {
            level = 2;
        } else if (tdScdmaDbm >= MemComp.ERR_OUT_OF_MEMORY) {
            level = 1;
        } else {
            level = 0;
        }
        if (level == 0 || isCdmaRatOnAllRatSupport()) {
            return level;
        }
        return getSignalBar() & 255;
    }

    public int getTdScdmaAsuLevel() {
        int tdScdmaDbm = getTdScdmaDbm();
        if (tdScdmaDbm == Integer.MAX_VALUE) {
            return 0;
        }
        return tdScdmaDbm + 120;
    }

    public int hashCode() {
        return (this.isGsm ? 1 : 0) + ((this.mTdScdmaRscp * 31) + (((((((((((((this.mGsmSignalStrength * 31) + (this.mGsmBitErrorRate * 31)) + (this.mCdmaDbm * 31)) + (this.mCdmaEcio * 31)) + (this.mEvdoDbm * 31)) + (this.mEvdoEcio * 31)) + (this.mEvdoSnr * 31)) + (this.mLteSignalStrength * 31)) + (this.mLteRsrp * 31)) + (this.mLteRsrq * 31)) + (this.mLteRssnr * 31)) + (this.mLteCqi * 31)) + (this.mSignalBar * 31)));
    }

    public boolean equals(Object o) {
        try {
            SignalStrength s = (SignalStrength) o;
            if (o != null && this.mGsmSignalStrength == s.mGsmSignalStrength && this.mGsmBitErrorRate == s.mGsmBitErrorRate && this.mCdmaDbm == s.mCdmaDbm && this.mCdmaEcio == s.mCdmaEcio && this.mEvdoDbm == s.mEvdoDbm && this.mEvdoEcio == s.mEvdoEcio && this.mEvdoSnr == s.mEvdoSnr && this.mLteSignalStrength == s.mLteSignalStrength && this.mLteRsrp == s.mLteRsrp && this.mLteRsrq == s.mLteRsrq && this.mLteRssnr == s.mLteRssnr && this.mLteCqi == s.mLteCqi && this.mTdScdmaRscp == s.mTdScdmaRscp && this.mSignalBar == s.mSignalBar && this.isGsm == s.isGsm) {
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        return "SignalStrength: " + this.mGsmSignalStrength + " " + this.mGsmBitErrorRate + " " + this.mCdmaDbm + " " + this.mCdmaEcio + " " + this.mEvdoDbm + " " + this.mEvdoEcio + " " + this.mEvdoSnr + " " + this.mLteSignalStrength + " " + this.mLteRsrp + " " + this.mLteRsrq + " " + this.mLteRssnr + " " + this.mLteCqi + " " + this.mTdScdmaRscp + " 0x" + Integer.toHexString(this.mSignalBar) + " " + (this.isGsm ? "gsm|lte" : "cdma");
    }

    private void setFromNotifierBundle(Bundle m) {
        this.mGsmSignalStrength = m.getInt("GsmSignalStrength");
        this.mGsmBitErrorRate = m.getInt("GsmBitErrorRate");
        this.mCdmaDbm = m.getInt("CdmaDbm");
        this.mCdmaEcio = m.getInt("CdmaEcio");
        this.mEvdoDbm = m.getInt("EvdoDbm");
        this.mEvdoEcio = m.getInt("EvdoEcio");
        this.mEvdoSnr = m.getInt("EvdoSnr");
        this.mLteSignalStrength = m.getInt("LteSignalStrength");
        this.mLteRsrp = m.getInt("LteRsrp");
        this.mLteRsrq = m.getInt("LteRsrq");
        this.mLteRssnr = m.getInt("LteRssnr");
        this.mLteCqi = m.getInt("LteCqi");
        this.mTdScdmaRscp = m.getInt("TdScdma");
        this.isGsm = m.getBoolean("isGsm");
        this.mSignalBar = m.getInt("SignalBar");
    }

    public void fillInNotifierBundle(Bundle m) {
        m.putInt("GsmSignalStrength", this.mGsmSignalStrength);
        m.putInt("GsmBitErrorRate", this.mGsmBitErrorRate);
        m.putInt("CdmaDbm", this.mCdmaDbm);
        m.putInt("CdmaEcio", this.mCdmaEcio);
        m.putInt("EvdoDbm", this.mEvdoDbm);
        m.putInt("EvdoEcio", this.mEvdoEcio);
        m.putInt("EvdoSnr", this.mEvdoSnr);
        m.putInt("LteSignalStrength", this.mLteSignalStrength);
        m.putInt("LteRsrp", this.mLteRsrp);
        m.putInt("LteRsrq", this.mLteRsrq);
        m.putInt("LteRssnr", this.mLteRssnr);
        m.putInt("LteCqi", this.mLteCqi);
        m.putInt("TdScdma", this.mTdScdmaRscp);
        m.putBoolean("isGsm", Boolean.valueOf(this.isGsm).booleanValue());
        m.putInt("SignalBar", this.mSignalBar);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
