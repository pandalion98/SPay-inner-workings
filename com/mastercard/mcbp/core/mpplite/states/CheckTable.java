package com.mastercard.mcbp.core.mpplite.states;

import android.util.Log;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;

public class CheckTable {
    public static void processAddCheckTable(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3) {
        int i = 1;
        byte b = byteArray2.getByte(0);
        byte b2 = byteArray2.getByte(1);
        byte b3 = byteArray2.getByte(2);
        if (b != null && (b + b2) - 1 <= byteArray.getLength() && b3 * b2 <= 15 && b2 != null && b3 >= (byte) 2) {
            byte b4;
            ByteArray byteArray4 = ByteArrayFactory.getInstance().getByteArray(b2);
            for (b4 = (byte) 0; b4 < b2; b4++) {
                byteArray4.setByte(b4, (byte) (byteArray2.getByte(b4 + 3) & byteArray.getByte((b + b4) - 1)));
            }
            for (b4 = (byte) 1; b4 < b3; b4++) {
                if (byteArray4.isEqual(byteArray2.copyOfRange((b4 * b2) + 3, ((b4 + 1) * b2) + 3))) {
                    break;
                }
            }
            i = 0;
            byte b5 = byteArray3.getByte(5);
            if (i != 0) {
                Log.d("MCBP", "ACT: MATCH FOUND");
                byteArray3.setByte(5, (byte) (b5 | 2));
                return;
            }
            Log.d("MCBP", "ACT: NO MATCH FOUND");
            byteArray3.setByte(5, (byte) (b5 | 1));
        }
    }
}
