package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import java.util.ArrayList;
import java.util.List;

public class MstConfigSequence {
    public List<MstConfigLump> lumps;

    public MstConfigSequence() {
        this.lumps = new ArrayList();
    }

    public boolean addLump(MstConfigLump mstConfigLump) {
        return this.lumps.add(mstConfigLump);
    }
}
