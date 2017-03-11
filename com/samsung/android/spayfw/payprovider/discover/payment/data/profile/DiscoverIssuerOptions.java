package com.samsung.android.spayfw.payprovider.discover.payment.data.profile;

import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;
import java.util.List;

public class DiscoverIssuerOptions {
    private ByteBuffer mIADOL;
    private List<DiscoverIDDTag> mIDDTags;
    private ByteBuffer mIssuerApplicationData;
    private ByteBuffer mIssuerLifeCycleData;

    public ByteBuffer getIssuerApplicationData() {
        return this.mIssuerApplicationData;
    }

    public void setIssuerApplicationData(ByteBuffer byteBuffer) {
        this.mIssuerApplicationData = byteBuffer;
    }

    public ByteBuffer getIssuerLifeCycleData() {
        return this.mIssuerLifeCycleData;
    }

    public void setIssuerLifeCycleData(ByteBuffer byteBuffer) {
        this.mIssuerLifeCycleData = byteBuffer;
    }

    public List<DiscoverIDDTag> getIDDTags() {
        return this.mIDDTags;
    }

    public void setIDDTags(List<DiscoverIDDTag> list) {
        this.mIDDTags = list;
    }

    public void setIADOL(ByteBuffer byteBuffer) {
        this.mIADOL = byteBuffer;
    }

    public ByteBuffer getIADOL() {
        return this.mIADOL;
    }
}
