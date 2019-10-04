/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.core;

import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
import com.samsung.android.spayfw.b.c;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class g {
    /*
     * Enabled aggressive block sorting
     */
    public static byte[] a(MstPayConfigEntry mstPayConfigEntry) {
        Iterator iterator = mstPayConfigEntry.getMstPayConfigEntry().iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            MstPayConfigEntryItem mstPayConfigEntryItem = (MstPayConfigEntryItem)iterator.next();
            int n3 = n2 + 4;
            int n4 = g.l(mstPayConfigEntryItem.getTrackIndex()) ? n3 + 3 : n3;
            n2 = n4;
        }
        c.d("MstPayConfigHelper", "mstPayConfigEntry2ByteArray: size = " + n2);
        byte[] arrby = new byte[n2];
        Iterator iterator2 = mstPayConfigEntry.getMstPayConfigEntry().iterator();
        int n5 = 0;
        do {
            int n6;
            if (!iterator2.hasNext()) {
                c.v("MstPayConfigHelper", "mstPayConfigEntry2ByteArray: ret = " + Arrays.toString((byte[])arrby));
                return arrby;
            }
            MstPayConfigEntryItem mstPayConfigEntryItem = (MstPayConfigEntryItem)iterator2.next();
            int n7 = n5 + 1;
            arrby[n5] = (byte)mstPayConfigEntryItem.getTrackIndex();
            int n8 = n7 + 1;
            arrby[n7] = (byte)mstPayConfigEntryItem.getLeadingZeros();
            int n9 = n8 + 1;
            arrby[n8] = (byte)mstPayConfigEntryItem.getTrailingZeros();
            int n10 = n9 + 1;
            byte by = mstPayConfigEntryItem.getDirection() == 1 ? (byte)1 : 0;
            arrby[n9] = by;
            Bundle bundle = mstPayConfigEntryItem.getExtraParams();
            if (g.l(mstPayConfigEntryItem.getTrackIndex()) && bundle != null) {
                int n11 = bundle.getInt("panLength");
                int n12 = bundle.getInt("nameLength");
                int n13 = bundle.getInt("dataLength");
                int n14 = n10 + 1;
                arrby[n10] = (byte)n11;
                int n15 = n14 + 1;
                arrby[n14] = (byte)n12;
                n6 = n15 + 1;
                arrby[n15] = (byte)n13;
            } else {
                n6 = n10;
            }
            n5 = n6;
        } while (true);
    }

    private static boolean l(int n2) {
        switch (n2) {
            default: {
                return false;
            }
            case 255: 
        }
        return true;
    }
}

