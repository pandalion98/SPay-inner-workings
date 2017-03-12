package com.mastercard.mobile_api.utils;

import android.support.v4.view.ViewCompat;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;
import com.mastercard.mobile_api.utils.apdu.emv.EMVSetStatusApdu;
import com.mastercard.mobile_api.utils.apdu.emv.PinChangeUnblockApdu;
import com.mastercard.mobile_api.utils.tlv.TLVParser;
import org.bouncycastle.asn1.eac.CertificateBody;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class TLV {
    public static ByteArray lengthBytes(ByteArray byteArray) {
        int length = byteArray.getLength();
        ByteArray byteArray2;
        if (length <= CertificateBody.profileType) {
            byteArray2 = ByteArrayFactory.getInstance().getByteArray(1);
            byteArray2.setByte(0, (byte) length);
            return byteArray2;
        } else if (length <= GF2Field.MASK) {
            byteArray2 = ByteArrayFactory.getInstance().getByteArray(2);
            byteArray2.setByte(0, TLVParser.BYTE_81);
            byteArray2.setByte(1, (byte) length);
            return byteArray2;
        } else {
            if (length <= HCEClientConstants.HIGHEST_ATC_DEC_VALUE) {
                byteArray2 = ByteArrayFactory.getInstance().getByteArray(3);
                byteArray2.setByte(0, EMVSetStatusApdu.RESET_LOWEST_PRIORITY);
                byteArray2.setByte(1, (byte) length);
            } else if (length <= ViewCompat.MEASURED_SIZE_MASK) {
                byteArray2 = ByteArrayFactory.getInstance().getByteArray(4);
                byteArray2.setByte(0, (byte) -125);
                byteArray2.setByte(1, (byte) length);
                return byteArray2;
            }
            byteArray2 = ByteArrayFactory.getInstance().getByteArray(4);
            byteArray2.setByte(0, PinChangeUnblockApdu.CLA);
            byteArray2.setByte(1, (byte) length);
            return byteArray2;
        }
    }

    public static ByteArray create(ByteArray byteArray, ByteArray byteArray2) {
        ByteArray fromByteArray = ByteArrayFactory.getInstance().getFromByteArray(byteArray);
        fromByteArray.append(lengthBytes(byteArray2));
        fromByteArray.append(byteArray2);
        return fromByteArray;
    }

    public static ByteArray create(byte b, ByteArray byteArray) {
        ByteArray lengthBytes = lengthBytes(byteArray);
        int length = lengthBytes.getLength();
        ByteArray byteArray2 = ByteArrayFactory.getInstance().getByteArray((length + 1) + byteArray.getLength());
        byteArray2.setByte(0, b);
        byteArray2.copyBytes(lengthBytes, 0, 1, length);
        byteArray2.copyBytes(byteArray, 0, length + 1, byteArray.getLength());
        return byteArray2;
    }
}
