/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.mastercard.mcbp.core.mcbpcards.profile;

import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_BL;
import com.mastercard.mcbp.core.mcbpcards.profile.DC_CP_MPP;
import com.mastercard.mobile_api.bytes.ByteArray;

public class DC_CP {
    private boolean CL_Supported;
    private DC_CP_BL DC_CP_BL;
    private ByteArray DC_CP_LDE;
    private ByteArray DC_CP_MK;
    private DC_CP_MPP DC_CP_MPP;
    private ByteArray DC_ID;
    private boolean RP_Supported;

    public boolean getCL_Supported() {
        return this.CL_Supported;
    }

    public DC_CP_BL getDC_CP_BL() {
        return this.DC_CP_BL;
    }

    public ByteArray getDC_CP_LDE() {
        return this.DC_CP_LDE;
    }

    public ByteArray getDC_CP_MK() {
        return this.DC_CP_MK;
    }

    public DC_CP_MPP getDC_CP_MPP() {
        return this.DC_CP_MPP;
    }

    public ByteArray getDC_ID() {
        return this.DC_ID;
    }

    public boolean getRP_Supported() {
        return this.RP_Supported;
    }

    public void setCL_Supported(boolean bl) {
        this.CL_Supported = bl;
    }

    public void setDC_CP_BL(DC_CP_BL dC_CP_BL) {
        this.DC_CP_BL = dC_CP_BL;
    }

    public void setDC_CP_LDE(ByteArray byteArray) {
        this.DC_CP_LDE = byteArray;
    }

    public void setDC_CP_MK(ByteArray byteArray) {
        this.DC_CP_MK = byteArray;
    }

    public void setDC_CP_MPP(DC_CP_MPP dC_CP_MPP) {
        this.DC_CP_MPP = dC_CP_MPP;
    }

    public void setDC_ID(ByteArray byteArray) {
        this.DC_ID = byteArray;
    }

    public void setRP_Supported(boolean bl) {
        this.RP_Supported = bl;
    }
}

