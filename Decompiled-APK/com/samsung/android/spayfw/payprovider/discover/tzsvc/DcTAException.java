package com.samsung.android.spayfw.payprovider.discover.tzsvc;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.google.android.gms.location.places.Place;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTACommands;
import com.samsung.android.spaytui.SpayTuiTAInfo;
import com.samsung.android.spaytzsvc.api.TAException;
import com.squareup.okhttp.internal.http.StatusLine;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.crypto.macs.SkeinMac;

public class DcTAException extends TAException {

    public enum Code {
        ERR_UNKNOWN("Unknown Error", -1),
        ERR_NONE("No Error", 0),
        ERR_TA_NOT_LOADED("TA Not Loaded", 1),
        ERR_INVALID_INPUT("Invalid Input", 2),
        ERR_LOAD_CERT_FAILED("Device Cert Load Failed", 3),
        ERR_TA_FAILED("TA Failed", SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI),
        ERR_TA_INVALID_INPUT("Invalid Input Error from TA", McTACommands.MOP_MC_TA_CMD_GET_SPSD_INFO),
        ERR_TA_INVALID_INPUT_SIZE("Invalid Input Size Error from TA", McTACommands.MOP_MC_TA_CMD_PROVISION_TOKEN),
        ERR_TA_INVALID_OUTPUT_SIZE("Invalid Output Size Error from TA", McTACommands.MOP_MC_TA_CMD_PROCESS_MST),
        ERR_TA_BUFFER_OVERFLOW("Buffer Overflow Error from TA", McTACommands.MOP_MC_TA_CMD_PREPARE_MST_TRACKS),
        ERR_TA_BUFFER_NULL("Buffer Null Error from TA", McTACommands.MOP_MC_TA_CMD_GET_NONCE),
        ERR_TA_BUFFER_INSUFFICIENT("Insufficient Buffer Error from TA", McTACommands.MOP_MC_TA_CMD_TRANSACTION_AUTH),
        ERR_TA_MISSING_DATA("Missing Data Error from TA", McTACommands.MOP_MC_TA_CMD_CLEAR_MST_DATA),
        ERR_TA_INTEGER_OVERFLOW("Integer Overflow Error from TA", McTACommands.MOP_MC_TA_CMD_GENERATE_UN),
        ERR_TA_VERIFY_CERT("Certificate Verification Failed", 289),
        ERR_TA_PARSE_CERT("Certificate Parcing Failed", 290),
        ERR_TA_PARSE_KEY("Key Parcing Failed", 291),
        ERR_TA_UNWRAP_DATA("Unwrap Data Failed", 292),
        ERR_TA_WRAP_DATA("Wrap Data Failed", 293),
        ERR_TA_UNEXPECTED_DATA("Crypto Error Unexpected Data", 294),
        ERR_TA_MODULUS_ERROR("Crypto Modulus Error", 295),
        ERR_TA_EXPONENT_ERROR("Crypto Exponent Error", 296),
        ERR_TA_ENCRYPT_ERROR("Encryption Failed", 297),
        ERR_TA_HMAC_ERROR("HMAC Failed", 304),
        ERR_TA_BASE64_DECODE("Base64 Decode Failed", 305),
        ERR_TA_BASE64_ENCODE("Base64 Encode Failed", 306),
        ERR_TA_GET_RANDOM("Get Random Failed", StatusLine.HTTP_TEMP_REDIRECT),
        ERR_TA_DECRYPT_ERROR("Decryption Failed", StatusLine.HTTP_PERM_REDIRECT),
        ERR_TA_SIGN("Sign Operation Failed", 309),
        ERR_TA_DIGEST("Digest Failed", 310),
        ERR_TA_ENCRYPT_CC("Encrypt Card Context Failed", 337),
        ERR_TA_PROCESS_CP("Process Card Profile Failed", 353),
        ERR_TA_PROCESS_CP_B64DECODE("Process Card Profile B64 Decode Failed", 354),
        ERR_TA_PROCESS_CP_JSONPARSE("Process Card Profile JSON Parcing Failed", 355),
        ERR_TA_PROCESS_CP_JSONCREATE("Process Card Profile JSON Creation Failed", 356),
        ERR_TA_ENCRYPT_DKC("Encrypt DevKey Context Failed", 385),
        ERR_TA_ENCRYPT_RC("Encrypt Replenish Context Failed", McTACommands.MOP_MC_TA_CMD_CRYPTO_SET_CONTEXT),
        ERR_TA_PROCESS_RD("Process Replenish Data Failed", 545),
        ERR_TA_GET_NONCE("Get Nonce Failed", 577),
        ERR_TA_GET_NONCE_INVALID_INPUT("Get Nonce Invalid Input Error", 578),
        ERR_TA_GET_NONCE_SECUREOBJ_UNWRAP("Get Nonce Unwrapping Secure Obj Failed", 579),
        ERR_TA_GET_NONCE_OTPK_UNWRAP("Get Nonce Unwrapping OTPK Failed", 580),
        ERR_TA_INIT_TRANSACTION("Init Transaction Error", 593),
        ERR_TA_INIT_TRANSACTION_INVALID_INPUT_LEN("Invalid Input Length", 594),
        ERR_TA_INIT_TRANSACTION_SECURE_OBJECT_UNWRAP("Failed to unwrap Secure Object", 595),
        ERR_TA_INIT_TRANSACTION_OTPK_BUNDLE_UNWRAP("Failed to unwrap OTPK Bundle", 596),
        ERR_TA_INIT_TRANSACTION_NO_VALID_OTPK_ERROR("No Valid OTPK Available", 597),
        ERR_TA_TXN_AUTH("Transaction Authentication Failed", 609),
        ERR_TA_TXN_AUTH_INVALID_PARAM("Transaction Authentication Invalid Parameter", 610),
        ERR_TA_COMPUTE_DVCC("Compute DCVV Failed", 641),
        ERR_TA_COMPUTE_DVCC_WRONG_UN_LEN("Compute DCVV Wrong UN Length", 642),
        ERR_TA_COMPUTE_DVCC_WRONG_KEY_SIZE("Compute DCVV Wrong Key Size", 643),
        ERR_TA_COMPUTE_DVCC_INVALID_ATC_REF("Compute DCVV Invalid ATC Reference", 644),
        ERR_TA_COMPUTE_DVCC_OTPK_UNWRAP("Compute DCVV Unwrapping OTPK Failed", 645),
        ERR_TA_COMPUTE_AC("Compute AC Failed", 657),
        ERR_TA_COMPUTE_AC_WRONG_KEY_SIZE("Compute AC Wrong Key Size", 658),
        ERR_TA_COMPUTE_AC_INVALID_ATC_REF("Compute AC Invalid ATC Reference", 659),
        ERR_TA_COMPUTE_AC_ENCRYPTION("Crypto Operation Failed", 660),
        ERR_TA_COMPUTE_AC_OP("Compute AC Failed", McTACommands.MOP_MC_TA_CMD_CASD_GET_UID),
        ERR_TA_MST_INTERNAL("MST Internal Error", SkeinMac.SKEIN_1024),
        ERR_TA_MST_INVALID_INPUT("MST Invalid Input", Place.TYPE_SUBLOCALITY_LEVEL_3),
        ERR_TA_MST_STRING_DECODE("String Decode Failed", Place.TYPE_SUBLOCALITY_LEVEL_4),
        ERR_TA_MST_INVALID_TRACK_DATA("Invalid Track Data", Place.TYPE_SUBLOCALITY_LEVEL_5),
        ERR_TA_CRYPTO_REQ("Crypto Request Failed", 1105),
        ERR_TA_CRYPTO_REQ_INVALID_DATA("Unsupported data passed for operation", 1106),
        ERR_MAX("Upper Bound", HCEClientConstants.HIGHEST_ATC_DEC_VALUE);
        
        private static final Map<Integer, Code> yU;
        private final int mErrCode;
        private final String mErrMsg;

        static {
            yU = new HashMap();
            Code[] values = values();
            int length = values.length;
            int i;
            while (i < length) {
                Code code = values[i];
                yU.put(Integer.valueOf(code.getCode()), code);
                i++;
            }
        }

        public String ey() {
            return this.mErrMsg;
        }

        public int getCode() {
            return this.mErrCode;
        }

        public static Code m1037P(int i) {
            Code code = (Code) yU.get(Integer.valueOf(i));
            if (code == null) {
                return ERR_UNKNOWN;
            }
            return code;
        }

        private Code(String str, int i) {
            this.mErrMsg = str;
            this.mErrCode = i;
        }
    }

    public DcTAException(String str, int i) {
        super(str, i);
    }

    public DcTAException(Code code) {
        super(code.ey(), code.getCode());
    }

    public static DcTAException m1038b(long j, String str) {
        Code P = Code.m1037P(new Long(j).intValue());
        if (str.length() <= 0) {
            str = P.ey();
        }
        return new DcTAException(str, P.getCode());
    }

    public String toString() {
        return "Err Msg: " + getMessage() + ", Err Code: " + getErrorCode();
    }
}
