package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mobile_api.bytes.ByteArray;

public class DC_CP {
    private boolean CL_Supported;
    private DC_CP_BL DC_CP_BL;
    private ByteArray DC_CP_LDE;
    private ByteArray DC_CP_MK;
    private DC_CP_MPP DC_CP_MPP;
    private ByteArray DC_ID;
    private boolean RP_Supported;

    public ByteArray getDC_CP_MK() {
        return this.DC_CP_MK;
    }

    public void setDC_CP_MK(ByteArray byteArray) {
        this.DC_CP_MK = byteArray;
    }

    public boolean getRP_Supported() {
        return this.RP_Supported;
    }

    public void setRP_Supported(boolean z) {
        this.RP_Supported = z;
    }

    public ByteArray getDC_CP_LDE() {
        return this.DC_CP_LDE;
    }

    public void setDC_CP_LDE(ByteArray byteArray) {
        this.DC_CP_LDE = byteArray;
    }

    public DC_CP_MPP getDC_CP_MPP() {
        return this.DC_CP_MPP;
    }

    public void setDC_CP_MPP(DC_CP_MPP dc_cp_mpp) {
        this.DC_CP_MPP = dc_cp_mpp;
    }

    public boolean getCL_Supported() {
        return this.CL_Supported;
    }

    public void setCL_Supported(boolean z) {
        this.CL_Supported = z;
    }

    public DC_CP_BL getDC_CP_BL() {
        return this.DC_CP_BL;
    }

    public void setDC_CP_BL(DC_CP_BL dc_cp_bl) {
        this.DC_CP_BL = dc_cp_bl;
    }

    public ByteArray getDC_ID() {
        return this.DC_ID;
    }

    public void setDC_ID(ByteArray byteArray) {
        this.DC_ID = byteArray;
    }
}
