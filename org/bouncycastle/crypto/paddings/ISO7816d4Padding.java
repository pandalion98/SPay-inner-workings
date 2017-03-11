package org.bouncycastle.crypto.paddings;

import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

public class ISO7816d4Padding implements BlockCipherPadding {
    public int addPadding(byte[] bArr, int i) {
        int length = bArr.length - i;
        bArr[i] = VerifyPINApdu.P2_PLAINTEXT;
        for (int i2 = i + 1; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) 0;
        }
        return length;
    }

    public String getPaddingName() {
        return "ISO7816-4";
    }

    public void init(SecureRandom secureRandom) {
    }

    public int padCount(byte[] bArr) {
        int length = bArr.length - 1;
        while (length > 0 && bArr[length] == null) {
            length--;
        }
        if (bArr[length] == -128) {
            return bArr.length - length;
        }
        throw new InvalidCipherTextException("pad block corrupted");
    }
}
