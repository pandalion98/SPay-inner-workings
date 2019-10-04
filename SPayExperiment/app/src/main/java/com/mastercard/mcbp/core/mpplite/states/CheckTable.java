/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.mastercard.mcbp.core.mpplite.states;

import android.util.Log;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.bytes.ByteArrayFactory;

public class CheckTable {
    /*
     * Enabled aggressive block sorting
     */
    public static void processAddCheckTable(ByteArray byteArray, ByteArray byteArray2, ByteArray byteArray3) {
        int n2 = 1;
        byte by = byteArray2.getByte(0);
        int n3 = byteArray2.getByte(n2);
        int n4 = byteArray2.getByte(2);
        if (by == 0 || -1 + (by + n3) > byteArray.getLength() || n4 * n3 > 15 || n3 == 0 || n4 < 2) {
            return;
        }
        ByteArray byteArray4 = ByteArrayFactory.getInstance().getByteArray(n3);
        int n5 = 0;
        do {
            if (n5 >= n3) break;
            byteArray4.setByte(n5, (byte)(byteArray2.getByte(n5 + 3) & byteArray.getByte(-1 + (by + n5))));
            ++n5;
        } while (true);
        int n6 = n2;
        do {
            if (n6 >= n4) {
                n2 = 0;
                break;
            }
            if (byteArray4.isEqual(byteArray2.copyOfRange(3 + n6 * n3, 3 + n3 * (n6 + 1)))) break;
            ++n6;
        } while (true);
        byte by2 = byteArray3.getByte(5);
        if (n2 != 0) {
            Log.d((String)"MCBP", (String)"ACT: MATCH FOUND");
            byteArray3.setByte(5, (byte)(by2 | 2));
            return;
        }
        Log.d((String)"MCBP", (String)"ACT: NO MATCH FOUND");
        byteArray3.setByte(5, (byte)(by2 | 1));
    }
}

