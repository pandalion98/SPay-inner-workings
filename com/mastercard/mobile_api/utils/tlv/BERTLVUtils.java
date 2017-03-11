package com.mastercard.mobile_api.utils.tlv;

import android.support.v4.view.ViewCompat;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class BERTLVUtils {
    public static int setBERTLVLength(int i, byte[] bArr, int i2) {
        if (i < X509KeyUsage.digitalSignature) {
            bArr[i2] = (byte) i;
            return i2 + 1;
        } else if (i < SkeinMac.SKEIN_256) {
            bArr[i2] = TLVParser.BYTE_81;
            bArr[i2 + 1] = (byte) i;
            return i2 + 2;
        } else if (i < PKIFailureInfo.notAuthorized) {
            bArr[i2] = EMVSetStatusApdu.RESET_LOWEST_PRIORITY;
            bArr[i2 + 1] = (byte) (i >> 8);
            bArr[i2 + 2] = (byte) (i & GF2Field.MASK);
            return i2 + 3;
        } else if (i < ViewCompat.MEASURED_STATE_TOO_SMALL) {
            bArr[i2] = (byte) -125;
            bArr[i2 + 1] = (byte) (i >> 16);
            bArr[i2 + 2] = (byte) ((i >> 8) & GF2Field.MASK);
            bArr[i2 + 3] = (byte) (i & GF2Field.MASK);
            return i2 + 4;
        } else {
            bArr[i2] = PinChangeUnblockApdu.CLA;
            bArr[i2 + 1] = (byte) (i >> 24);
            bArr[i2 + 2] = (byte) ((i >> 16) & GF2Field.MASK);
            bArr[i2 + 3] = (byte) ((i >> 8) & GF2Field.MASK);
            bArr[i2 + 4] = (byte) (i & GF2Field.MASK);
            return i2 + 5;
        }
    }

    public static int getNbBytes(int i) {
        if (i < 0) {
            return 5;
        }
        if (i < X509KeyUsage.digitalSignature) {
            return 1;
        }
        if (i < SkeinMac.SKEIN_256) {
            return 2;
        }
        if (i < PKIFailureInfo.notAuthorized) {
            return 3;
        }
        if (i < ViewCompat.MEASURED_STATE_TOO_SMALL) {
            return 4;
        }
        return 5;
    }

    public static int getTLVLengthByte(byte[] bArr, int i) {
        if (bArr[i] == -127) {
            return 2;
        }
        if (bArr[i] == -126) {
            return 3;
        }
        if (bArr[i] == -125) {
            return 4;
        }
        if (bArr[i] == -124) {
            return 5;
        }
        return 1;
    }

    public static int getTLVLength(byte[] bArr, int i) {
        if (bArr[i] > null && (bArr[i] & GF2Field.MASK) < X509KeyUsage.digitalSignature) {
            return bArr[i] & GF2Field.MASK;
        }
        if (bArr[i] == -127) {
            return bArr[i + 1] & GF2Field.MASK;
        }
        if (bArr[i] == -126) {
            return ((bArr[i + 1] & GF2Field.MASK) << 8) | (bArr[i + 2] & GF2Field.MASK);
        }
        if (bArr[i] == -125) {
            return (((bArr[i + 1] & GF2Field.MASK) << 16) | ((bArr[i + 2] & GF2Field.MASK) << 8)) | (bArr[i + 3] & GF2Field.MASK);
        }
        if (bArr[i] == -124) {
            return ((((bArr[i + 0] & GF2Field.MASK) << 24) | ((bArr[i + 1] & GF2Field.MASK) << 16)) | ((bArr[i + 2] & GF2Field.MASK) << 8)) | (bArr[i + 3] & GF2Field.MASK);
        }
        return bArr[i] & GF2Field.MASK;
    }
}
