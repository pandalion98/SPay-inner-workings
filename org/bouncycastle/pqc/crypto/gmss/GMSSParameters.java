package org.bouncycastle.pqc.crypto.gmss;

import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import org.bouncycastle.util.Arrays;

public class GMSSParameters {
    private int[] f399K;
    private int[] heightOfTrees;
    private int numOfLayers;
    private int[] winternitzParameter;

    public GMSSParameters(int i) {
        int[] iArr;
        if (i <= 10) {
            iArr = new int[]{10};
            init(iArr.length, iArr, new int[]{3}, new int[]{2});
        } else if (i <= 20) {
            iArr = new int[]{10, 10};
            init(iArr.length, iArr, new int[]{5, 4}, new int[]{2, 2});
        } else {
            iArr = new int[]{10, 10, 10, 10};
            init(iArr.length, iArr, new int[]{9, 9, 9, 3}, new int[]{2, 2, 2, 2});
        }
    }

    public GMSSParameters(int i, int[] iArr, int[] iArr2, int[] iArr3) {
        init(i, iArr, iArr2, iArr3);
    }

    private void init(int i, int[] iArr, int[] iArr2, int[] iArr3) {
        Object obj = 1;
        String str = BuildConfig.FLAVOR;
        this.numOfLayers = i;
        if (!(this.numOfLayers == iArr2.length && this.numOfLayers == iArr.length && this.numOfLayers == iArr3.length)) {
            str = "Unexpected parameterset format";
            obj = null;
        }
        Object obj2 = obj;
        String str2 = str;
        int i2 = 0;
        while (i2 < this.numOfLayers) {
            if (iArr3[i2] < 2 || (iArr[i2] - iArr3[i2]) % 2 != 0) {
                str2 = "Wrong parameter K (K >= 2 and H-K even required)!";
                obj2 = null;
            }
            if (iArr[i2] < 4 || iArr2[i2] < 2) {
                str2 = "Wrong parameter H or w (H > 3 and w > 1 required)!";
                obj2 = null;
            }
            i2++;
        }
        if (obj2 != null) {
            this.heightOfTrees = Arrays.clone(iArr);
            this.winternitzParameter = Arrays.clone(iArr2);
            this.f399K = Arrays.clone(iArr3);
            return;
        }
        throw new IllegalArgumentException(str2);
    }

    public int[] getHeightOfTrees() {
        return Arrays.clone(this.heightOfTrees);
    }

    public int[] getK() {
        return Arrays.clone(this.f399K);
    }

    public int getNumOfLayers() {
        return this.numOfLayers;
    }

    public int[] getWinternitzParameter() {
        return Arrays.clone(this.winternitzParameter);
    }
}
