/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.data.profile.DiscoverIDDTag;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.List;

public class DiscoverIssuerOptions {
    private ByteBuffer mIADOL;
    private List<DiscoverIDDTag> mIDDTags;
    private ByteBuffer mIssuerApplicationData;
    private ByteBuffer mIssuerLifeCycleData;

    public ByteBuffer getIADOL() {
        return this.mIADOL;
    }

    public List<DiscoverIDDTag> getIDDTags() {
        return this.mIDDTags;
    }

    public ByteBuffer getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public ByteBuffer getIssuerLifeCycleData() {
        return this.mIssuerLifeCycleData;
    }

    public void setIADOL(ByteBuffer byteBuffer) {
        this.mIADOL = byteBuffer;
    }

    public void setIDDTags(List<DiscoverIDDTag> list) {
        this.mIDDTags = list;
    }

    public void setIssuerApplicationData(ByteBuffer byteBuffer) {
        this.mIssuerApplicationData = byteBuffer;
    }

    public void setIssuerLifeCycleData(ByteBuffer byteBuffer) {
        this.mIssuerLifeCycleData = byteBuffer;
    }
}

