/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import com.samsung.android.spayfw.payprovider.plcc.tzsvc.MstConfigLump;
import java.util.ArrayList;
import java.util.List;

public class MstConfigSequence {
    public List<MstConfigLump> lumps = new ArrayList();

    public boolean addLump(MstConfigLump mstConfigLump) {
        return this.lumps.add((Object)mstConfigLump);
    }
}

