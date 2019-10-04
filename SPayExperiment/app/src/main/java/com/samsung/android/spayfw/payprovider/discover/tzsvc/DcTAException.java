/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import com.samsung.android.spaytzsvc.api.TAException;
import java.util.HashMap;
import java.util.Map;

public class DcTAException
extends TAException {
    public DcTAException(Code code) {
        super(code.ey(), code.getCode());
    }

    public DcTAException(String string, int n2) {
        super(string, n2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static DcTAException b(long l2, String string) {
        Code code = Code.P(new Long(l2).intValue());
        if (string.length() > 0) {
            do {
                return new DcTAException(string, code.getCode());
                break;
            } while (true);
        }
        string = code.ey();
        return new DcTAException(string, code.getCode());
    }

    public String toString() {
        return "Err Msg: " + this.getMessage() + ", Err Code: " + this.getErrorCode();
    }

    public static final class Code
    extends Enum<Code> {
        public static final /* enum */ Code xG = new Code("Unknown Error", -1);
        public static final /* enum */ Code xH = new Code("No Error", 0);
        public static final /* enum */ Code xI = new Code("TA Not Loaded", 1);
        public static final /* enum */ Code xJ = new Code("Invalid Input", 2);
        public static final /* enum */ Code xK = new Code("Device Cert Load Failed", 3);
        public static final /* enum */ Code xL = new Code("TA Failed", 257);
        public static final /* enum */ Code xM = new Code("Invalid Input Error from TA", 258);
        public static final /* enum */ Code xN = new Code("Invalid Input Size Error from TA", 259);
        public static final /* enum */ Code xO = new Code("Invalid Output Size Error from TA", 260);
        public static final /* enum */ Code xP = new Code("Buffer Overflow Error from TA", 261);
        public static final /* enum */ Code xQ = new Code("Buffer Null Error from TA", 262);
        public static final /* enum */ Code xR = new Code("Insufficient Buffer Error from TA", 263);
        public static final /* enum */ Code xS = new Code("Missing Data Error from TA", 264);
        public static final /* enum */ Code xT = new Code("Integer Overflow Error from TA", 265);
        public static final /* enum */ Code xU = new Code("Certificate Verification Failed", 289);
        public static final /* enum */ Code xV = new Code("Certificate Parcing Failed", 290);
        public static final /* enum */ Code xW = new Code("Key Parcing Failed", 291);
        public static final /* enum */ Code xX = new Code("Unwrap Data Failed", 292);
        public static final /* enum */ Code xY = new Code("Wrap Data Failed", 293);
        public static final /* enum */ Code xZ = new Code("Crypto Error Unexpected Data", 294);
        public static final /* enum */ Code yA;
        public static final /* enum */ Code yB;
        public static final /* enum */ Code yC;
        public static final /* enum */ Code yD;
        public static final /* enum */ Code yE;
        public static final /* enum */ Code yF;
        public static final /* enum */ Code yG;
        public static final /* enum */ Code yH;
        public static final /* enum */ Code yI;
        public static final /* enum */ Code yJ;
        public static final /* enum */ Code yK;
        public static final /* enum */ Code yL;
        public static final /* enum */ Code yM;
        public static final /* enum */ Code yN;
        public static final /* enum */ Code yO;
        public static final /* enum */ Code yP;
        public static final /* enum */ Code yQ;
        public static final /* enum */ Code yR;
        public static final /* enum */ Code yS;
        public static final /* enum */ Code yT;
        private static final Map<Integer, Code> yU;
        private static final /* synthetic */ Code[] yV;
        public static final /* enum */ Code ya;
        public static final /* enum */ Code yb;
        public static final /* enum */ Code yc;
        public static final /* enum */ Code yd;
        public static final /* enum */ Code ye;
        public static final /* enum */ Code yf;
        public static final /* enum */ Code yg;
        public static final /* enum */ Code yh;
        public static final /* enum */ Code yi;
        public static final /* enum */ Code yj;
        public static final /* enum */ Code yk;
        public static final /* enum */ Code yl;
        public static final /* enum */ Code ym;
        public static final /* enum */ Code yn;
        public static final /* enum */ Code yo;
        public static final /* enum */ Code yp;
        public static final /* enum */ Code yq;
        public static final /* enum */ Code yr;
        public static final /* enum */ Code ys;
        public static final /* enum */ Code yt;
        public static final /* enum */ Code yu;
        public static final /* enum */ Code yv;
        public static final /* enum */ Code yw;
        public static final /* enum */ Code yx;
        public static final /* enum */ Code yy;
        public static final /* enum */ Code yz;
        private final int mErrCode;
        private final String mErrMsg;

        static {
            ya = new Code("Crypto Modulus Error", 295);
            yb = new Code("Crypto Exponent Error", 296);
            yc = new Code("Encryption Failed", 297);
            yd = new Code("HMAC Failed", 304);
            ye = new Code("Base64 Decode Failed", 305);
            yf = new Code("Base64 Encode Failed", 306);
            yg = new Code("Get Random Failed", 307);
            yh = new Code("Decryption Failed", 308);
            yi = new Code("Sign Operation Failed", 309);
            yj = new Code("Digest Failed", 310);
            yk = new Code("Encrypt Card Context Failed", 337);
            yl = new Code("Process Card Profile Failed", 353);
            ym = new Code("Process Card Profile B64 Decode Failed", 354);
            yn = new Code("Process Card Profile JSON Parcing Failed", 355);
            yo = new Code("Process Card Profile JSON Creation Failed", 356);
            yp = new Code("Encrypt DevKey Context Failed", 385);
            yq = new Code("Encrypt Replenish Context Failed", 513);
            yr = new Code("Process Replenish Data Failed", 545);
            ys = new Code("Get Nonce Failed", 577);
            yt = new Code("Get Nonce Invalid Input Error", 578);
            yu = new Code("Get Nonce Unwrapping Secure Obj Failed", 579);
            yv = new Code("Get Nonce Unwrapping OTPK Failed", 580);
            yw = new Code("Init Transaction Error", 593);
            yx = new Code("Invalid Input Length", 594);
            yy = new Code("Failed to unwrap Secure Object", 595);
            yz = new Code("Failed to unwrap OTPK Bundle", 596);
            yA = new Code("No Valid OTPK Available", 597);
            yB = new Code("Transaction Authentication Failed", 609);
            yC = new Code("Transaction Authentication Invalid Parameter", 610);
            yD = new Code("Compute DCVV Failed", 641);
            yE = new Code("Compute DCVV Wrong UN Length", 642);
            yF = new Code("Compute DCVV Wrong Key Size", 643);
            yG = new Code("Compute DCVV Invalid ATC Reference", 644);
            yH = new Code("Compute DCVV Unwrapping OTPK Failed", 645);
            yI = new Code("Compute AC Failed", 657);
            yJ = new Code("Compute AC Wrong Key Size", 658);
            yK = new Code("Compute AC Invalid ATC Reference", 659);
            yL = new Code("Crypto Operation Failed", 660);
            yM = new Code("Compute AC Failed", 769);
            yN = new Code("MST Internal Error", 1024);
            yO = new Code("MST Invalid Input", 1025);
            yP = new Code("String Decode Failed", 1026);
            yQ = new Code("Invalid Track Data", 1027);
            yR = new Code("Crypto Request Failed", 1105);
            yS = new Code("Unsupported data passed for operation", 1106);
            yT = new Code("Upper Bound", 65535);
            Code[] arrcode = new Code[]{xG, xH, xI, xJ, xK, xL, xM, xN, xO, xP, xQ, xR, xS, xT, xU, xV, xW, xX, xY, xZ, ya, yb, yc, yd, ye, yf, yg, yh, yi, yj, yk, yl, ym, yn, yo, yp, yq, yr, ys, yt, yu, yv, yw, yx, yy, yz, yA, yB, yC, yD, yE, yF, yG, yH, yI, yJ, yK, yL, yM, yN, yO, yP, yQ, yR, yS, yT};
            yV = arrcode;
            yU = new HashMap();
            for (Code code : Code.values()) {
                yU.put((Object)code.getCode(), (Object)code);
            }
        }

        private Code(String string2, int n3) {
            this.mErrMsg = string2;
            this.mErrCode = n3;
        }

        public static Code P(int n2) {
            Code code = (Code)((Object)yU.get((Object)n2));
            if (code == null) {
                code = xG;
            }
            return code;
        }

        public static Code valueOf(String string) {
            return (Code)Enum.valueOf(Code.class, (String)string);
        }

        public static Code[] values() {
            return (Code[])yV.clone();
        }

        public String ey() {
            return this.mErrMsg;
        }

        public int getCode() {
            return this.mErrCode;
        }
    }

}

