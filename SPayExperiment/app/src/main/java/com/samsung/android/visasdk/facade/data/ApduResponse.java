/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.c.a;
import com.samsung.android.visasdk.paywave.Constant;
import com.samsung.android.visasdk.paywave.data.ApduError;

public class ApduResponse {
    private static final String TAG = "ApduResponse";
    byte[] apduData;
    ApduError apduError;

    public ApduResponse(int n2) {
        this.apduData = this.constructErrorResponseApdu(n2);
        this.apduError = new ApduError(n2);
    }

    public ApduResponse(byte[] arrby) {
        this.apduData = arrby;
        this.apduError = new ApduError(0);
    }

    public ApduResponse(byte[] arrby, ApduError apduError) {
        this.apduData = arrby;
        this.apduError = apduError;
    }

    byte[] constructErrorResponseApdu(int n2) {
        a.d(TAG, "constructErrorResponseApdu(), errorIndex=" + n2);
        switch (n2) {
            default: {
                return Constant.DJ;
            }
            case 0: {
                return Constant.Du;
            }
            case -1: {
                return Constant.Dv;
            }
            case -2: {
                return Constant.Dw;
            }
            case -3: {
                return Constant.Dx;
            }
            case -4: {
                return Constant.Dy;
            }
            case -5: {
                return Constant.Dz;
            }
            case -6: {
                return Constant.DA;
            }
            case -7: {
                return Constant.DB;
            }
            case -8: {
                return Constant.DD;
            }
            case -9: {
                return Constant.DE;
            }
            case -10: {
                return Constant.DF;
            }
            case -11: {
                return Constant.DG;
            }
            case -12: {
                return Constant.DH;
            }
            case -13: 
        }
        return Constant.DI;
    }

    public byte[] getApduData() {
        return this.apduData;
    }

    public ApduError getApduError() {
        return this.apduError;
    }

    public void setApduData(byte[] arrby) {
        this.apduData = arrby;
    }

    public void setApduError(ApduError apduError) {
        this.apduError = apduError;
    }
}

