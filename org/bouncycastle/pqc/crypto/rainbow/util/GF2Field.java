package org.bouncycastle.pqc.crypto.rainbow.util;

import com.mastercard.mcbp.core.mpplite.states.StatesConstants;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import org.bouncycastle.crypto.tls.AlertDescription;

public class GF2Field {
    public static final int MASK = 255;
    static final short[] exps;
    static final short[] logs;

    static {
        exps = new short[]{(short) 1, (short) 2, (short) 4, (short) 8, (short) 16, (short) 32, (short) 64, (short) 128, (short) 77, (short) 154, (short) 121, (short) 242, (short) 169, (short) 31, (short) 62, (short) 124, (short) 248, (short) 189, (short) 55, AlertDescription.unsupported_extension, (short) 220, (short) 245, (short) 167, (short) 3, (short) 6, (short) 12, (short) 24, (short) 48, (short) 96, (short) 192, (short) 205, (short) 215, (short) 227, (short) 139, (short) 91, (short) 182, (short) 33, (short) 66, MCFCITemplate.TAG_DF_NAME, (short) 69, (short) 138, (short) 89, ISO7816.READ_RECORD, (short) 41, (short) 82, ISO7816.SELECT, (short) 5, (short) 10, (short) 20, (short) 40, (short) 80, (short) 160, (short) 13, (short) 26, (short) 52, (short) 104, (short) 208, (short) 237, (short) 151, (short) 99, (short) 198, (short) 193, (short) 207, (short) 211, (short) 235, (short) 155, (short) 123, (short) 246, (short) 161, (short) 15, (short) 30, (short) 60, (short) 120, (short) 240, (short) 173, (short) 23, (short) 46, (short) 92, (short) 184, (short) 61, (short) 122, (short) 244, MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE, (short) 7, (short) 14, (short) 28, (short) 56, AlertDescription.unrecognized_name, (short) 224, (short) 141, (short) 87, (short) 174, (short) 17, (short) 34, (short) 68, (short) 136, (short) 93, (short) 186, (short) 57, AlertDescription.bad_certificate_hash_value, (short) 228, (short) 133, (short) 71, (short) 142, (short) 81, (short) 162, (short) 9, (short) 18, (short) 36, (short) 72, (short) 144, (short) 109, (short) 218, (short) 249, (short) 191, (short) 51, (short) 102, (short) 204, (short) 213, (short) 231, (short) 131, (short) 75, (short) 150, (short) 97, (short) 194, (short) 201, (short) 223, (short) 243, (short) 171, (short) 27, (short) 54, (short) 108, (short) 216, (short) 253, (short) 183, (short) 35, (short) 70, (short) 140, (short) 85, (short) 170, (short) 25, (short) 50, (short) 100, (short) 200, (short) 221, (short) 247, (short) 163, (short) 11, (short) 22, (short) 44, (short) 88, (short) 176, (short) 45, (short) 90, (short) 180, (short) 37, (short) 74, (short) 148, (short) 101, (short) 202, (short) 217, StatesConstants.LENGTH_MASK, (short) 179, (short) 43, (short) 86, (short) 172, (short) 21, (short) 42, (short) 84, (short) 168, (short) 29, (short) 58, (short) 116, (short) 232, (short) 157, (short) 119, (short) 238, (short) 145, AlertDescription.certificate_unobtainable, (short) 222, (short) 241, (short) 175, (short) 19, (short) 38, (short) 76, (short) 152, (short) 125, (short) 250, (short) 185, (short) 63, (short) 126, (short) 252, (short) 181, (short) 39, (short) 78, (short) 156, (short) 117, (short) 234, (short) 153, (short) 127, (short) 254, (short) 177, (short) 47, (short) 94, (short) 188, (short) 53, (short) 106, (short) 212, (short) 229, MCFCITemplate.TAG_APP_PRIORITY_INDICATOR, (short) 67, (short) 134, (short) 65, (short) 130, (short) 73, (short) 146, (short) 105, (short) 210, (short) 233, (short) 159, AlertDescription.unknown_psk_identity, (short) 230, (short) 129, (short) 79, (short) 158, AlertDescription.bad_certificate_status_response, (short) 226, (short) 137, (short) 95, (short) 190, (short) 49, (short) 98, (short) 196, (short) 197, (short) 199, (short) 195, (short) 203, (short) 219, (short) 251, (short) 187, (short) 59, (short) 118, (short) 236, (short) 149, (short) 103, (short) 206, (short) 209, (short) 239, (short) 147, (short) 107, (short) 214, (short) 225, (short) 143, (short) 83, (short) 166, (short) 1};
        logs = new short[]{(short) 0, (short) 0, (short) 1, (short) 23, (short) 2, (short) 46, (short) 24, (short) 83, (short) 3, (short) 106, (short) 47, (short) 147, (short) 25, (short) 52, (short) 84, (short) 69, (short) 4, (short) 92, (short) 107, (short) 182, (short) 48, (short) 166, (short) 148, (short) 75, (short) 26, (short) 140, (short) 53, (short) 129, (short) 85, (short) 170, (short) 70, (short) 13, (short) 5, (short) 36, (short) 93, MCFCITemplate.TAG_APP_PRIORITY_INDICATOR, (short) 108, (short) 155, (short) 183, (short) 193, (short) 49, (short) 43, (short) 167, (short) 163, (short) 149, (short) 152, (short) 76, (short) 202, (short) 27, (short) 230, (short) 141, AlertDescription.unknown_psk_identity, (short) 54, (short) 205, (short) 130, (short) 18, (short) 86, (short) 98, (short) 171, (short) 240, (short) 71, (short) 79, (short) 14, (short) 189, (short) 6, (short) 212, (short) 37, (short) 210, (short) 94, (short) 39, (short) 136, (short) 102, (short) 109, (short) 214, (short) 156, (short) 121, (short) 184, (short) 8, (short) 194, (short) 223, (short) 50, (short) 104, (short) 44, (short) 253, (short) 168, (short) 138, ISO7816.SELECT, (short) 90, (short) 150, (short) 41, (short) 153, (short) 34, (short) 77, (short) 96, (short) 203, (short) 228, (short) 28, (short) 123, (short) 231, (short) 59, (short) 142, (short) 158, (short) 116, (short) 244, (short) 55, (short) 216, (short) 206, (short) 249, (short) 131, AlertDescription.certificate_unobtainable, (short) 19, ISO7816.READ_RECORD, (short) 87, (short) 225, (short) 99, (short) 220, (short) 172, (short) 196, (short) 241, (short) 175, (short) 72, (short) 10, (short) 80, (short) 66, (short) 15, (short) 186, (short) 190, (short) 199, (short) 7, (short) 222, (short) 213, (short) 120, (short) 38, (short) 101, (short) 211, (short) 209, (short) 95, (short) 227, (short) 40, (short) 33, (short) 137, (short) 89, (short) 103, (short) 252, AlertDescription.unsupported_extension, (short) 177, (short) 215, (short) 248, (short) 157, (short) 243, (short) 122, (short) 58, (short) 185, (short) 198, (short) 9, (short) 65, (short) 195, (short) 174, (short) 224, (short) 219, (short) 51, (short) 68, (short) 105, (short) 146, (short) 45, (short) 82, (short) 254, (short) 22, (short) 169, (short) 12, (short) 139, (short) 128, MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE, (short) 74, (short) 91, (short) 181, (short) 151, (short) 201, (short) 42, (short) 162, (short) 154, (short) 192, (short) 35, (short) 134, (short) 78, (short) 188, (short) 97, (short) 239, (short) 204, (short) 17, (short) 229, AlertDescription.bad_certificate_hash_value, (short) 29, (short) 61, (short) 124, (short) 235, (short) 232, (short) 233, (short) 60, (short) 234, (short) 143, (short) 125, (short) 159, (short) 236, (short) 117, (short) 30, (short) 245, (short) 62, (short) 56, (short) 246, (short) 217, (short) 63, (short) 207, (short) 118, (short) 250, (short) 31, MCFCITemplate.TAG_DF_NAME, (short) 160, AlertDescription.unrecognized_name, (short) 237, (short) 20, (short) 144, (short) 179, (short) 126, (short) 88, (short) 251, (short) 226, (short) 32, (short) 100, (short) 208, (short) 221, (short) 119, (short) 173, (short) 218, (short) 197, (short) 64, (short) 242, (short) 57, (short) 176, (short) 247, (short) 73, (short) 180, (short) 11, (short) 127, (short) 81, (short) 21, (short) 67, (short) 145, (short) 16, AlertDescription.bad_certificate_status_response, (short) 187, (short) 238, (short) 191, (short) 133, (short) 200, (short) 161};
    }

    public static short addElem(short s, short s2) {
        return (short) (s ^ s2);
    }

    public static short getExp(short s) {
        return exps[s];
    }

    public static short getLog(short s) {
        return logs[s];
    }

    public static short invElem(short s) {
        return s == (short) 0 ? (short) 0 : exps[255 - logs[s]];
    }

    public static short multElem(short s, short s2) {
        return (s == (short) 0 || s2 == (short) 0) ? (short) 0 : exps[(logs[s] + logs[s2]) % MASK];
    }
}
