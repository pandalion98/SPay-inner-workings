package com.samsung.android.spayfw.core;

import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
import com.samsung.android.spayfw.p002b.Log;
import java.util.Arrays;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.core.g */
public class MstPayConfigHelper {
    private static boolean m608l(int i) {
        switch (i) {
            case GF2Field.MASK /*255*/:
                return true;
            default:
                return false;
        }
    }

    public static byte[] m607a(MstPayConfigEntry mstPayConfigEntry) {
        int i;
        int i2 = 0;
        for (MstPayConfigEntryItem trackIndex : mstPayConfigEntry.getMstPayConfigEntry()) {
            i2 += 4;
            if (MstPayConfigHelper.m608l(trackIndex.getTrackIndex())) {
                i = i2 + 3;
            } else {
                i = i2;
            }
            i2 = i;
        }
        Log.m285d("MstPayConfigHelper", "mstPayConfigEntry2ByteArray: size = " + i2);
        byte[] bArr = new byte[i2];
        i2 = 0;
        for (MstPayConfigEntryItem trackIndex2 : mstPayConfigEntry.getMstPayConfigEntry()) {
            int i3 = i2 + 1;
            bArr[i2] = (byte) trackIndex2.getTrackIndex();
            i2 = i3 + 1;
            bArr[i3] = (byte) trackIndex2.getLeadingZeros();
            int i4 = i2 + 1;
            bArr[i2] = (byte) trackIndex2.getTrailingZeros();
            i3 = i4 + 1;
            bArr[i4] = trackIndex2.getDirection() == 1 ? (byte) 1 : (byte) 0;
            Bundle extraParams = trackIndex2.getExtraParams();
            if (!MstPayConfigHelper.m608l(trackIndex2.getTrackIndex()) || extraParams == null) {
                i = i3;
            } else {
                i = extraParams.getInt(MstPayConfigEntryItem.EXTRA_PARAMS_KEY_PAN_LENGTH);
                i4 = extraParams.getInt(MstPayConfigEntryItem.EXTRA_PARAMS_KEY_NAME_LENGTH);
                i2 = extraParams.getInt(MstPayConfigEntryItem.EXTRA_PARAMS_KEY_DATA_LENGTH);
                int i5 = i3 + 1;
                bArr[i3] = (byte) i;
                i3 = i5 + 1;
                bArr[i5] = (byte) i4;
                i = i3 + 1;
                bArr[i3] = (byte) i2;
            }
            i2 = i;
        }
        Log.m289v("MstPayConfigHelper", "mstPayConfigEntry2ByteArray: ret = " + Arrays.toString(bArr));
        return bArr;
    }
}
