/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave;

import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.VerifyingEntity;
import com.samsung.android.visasdk.facade.data.VerifyingType;

public class Constant {
    public static final byte[] DA;
    public static final byte[] DB;
    public static final byte[] DD;
    public static final byte[] DE;
    public static final byte[] DF;
    public static final byte[] DG;
    public static final byte[] DH;
    public static final byte[] DI;
    public static final byte[] DJ;
    public static final CvmMode DK;
    public static final byte[] DM;
    public static final byte[] DN;
    public static final byte[] DO;
    public static final byte[] Dn;
    public static final byte[] Do;
    public static String Dp;
    public static final byte[] Dq;
    public static final byte[] Dr;
    public static final byte[] Ds;
    public static final byte[] Dt;
    public static final byte[] Du;
    public static final byte[] Dv;
    public static final byte[] Dw;
    public static final byte[] Dx;
    public static final byte[] Dy;
    public static final byte[] Dz;
    public static final byte[] iO;

    static {
        Dn = new byte[]{49, 56, 48, 57, 49, 52, 32, 86, 67, 80, 67, 83, 32, 49, 46, 51, 46, 48};
        Do = new byte[]{1, 0};
        Dp = "4761739001010010";
        Dq = new byte[]{0, 0, 0, 0, 0, 0, 0, 1};
        Dr = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        iO = new byte[]{0, -92, 4, 0, 14, 50, 80, 65, 89, 46, 83, 89, 83, 46, 68, 68, 70, 48, 49, 0};
        Ds = new byte[]{50, 80, 65, 89, 46, 83, 89, 83, 46, 68, 68, 70, 48, 49};
        Dt = new byte[]{-97, 102, 4, -97, 2, 6, -97, 3, 6, -97, 26, 2, -107, 5, 95, 42, 2, -102, 3, -100, 1, -97, 55, 4};
        Du = new byte[]{-112, 0};
        Dv = new byte[]{103, 0};
        Dw = new byte[]{105, -126};
        Dx = new byte[]{98, -125};
        Dy = new byte[]{105, -124};
        Dz = new byte[]{105, -123};
        DA = new byte[]{105, -122};
        DB = new byte[]{106, -128};
        DD = new byte[]{106, -127};
        DE = new byte[]{106, -126};
        DF = new byte[]{106, -125};
        DG = new byte[]{106, -122};
        DH = new byte[]{109, 0};
        DI = new byte[]{110, 0};
        DJ = new byte[]{111, 0};
        DK = new CvmMode(VerifyingEntity.MOBILE_APP, VerifyingType.PASSCODE);
        DM = new byte[]{0, 0, 0, 0, 0, 0};
        DN = new byte[]{8, 38};
        DO = new byte[]{8, 64};
    }

    public static final class State
    extends Enum<State> {
        public static final /* enum */ State DP = new State();
        public static final /* enum */ State DQ = new State();
        public static final /* enum */ State DR = new State();
        public static final /* enum */ State DS = new State();
        public static final /* enum */ State DU = new State();
        public static final /* enum */ State DV = new State();
        public static final /* enum */ State DW = new State();
        private static final /* synthetic */ State[] DX;

        static {
            State[] arrstate = new State[]{DP, DQ, DR, DS, DU, DV, DW};
            DX = arrstate;
        }

        public static State valueOf(String string) {
            return (State)Enum.valueOf(State.class, (String)string);
        }

        public static State[] values() {
            return (State[])DX.clone();
        }
    }

    public static final class TransactionType
    extends Enum<TransactionType> {
        public static final /* enum */ TransactionType DY = new TransactionType();
        public static final /* enum */ TransactionType DZ = new TransactionType();
        public static final /* enum */ TransactionType Ea = new TransactionType();
        public static final /* enum */ TransactionType Eb = new TransactionType();
        public static final /* enum */ TransactionType Ec = new TransactionType();
        public static final /* enum */ TransactionType Ed = new TransactionType();
        public static final /* enum */ TransactionType Ee = new TransactionType();
        public static final /* enum */ TransactionType Ef = new TransactionType();
        public static final /* enum */ TransactionType Eg = new TransactionType();
        private static final /* synthetic */ TransactionType[] Eh;

        static {
            TransactionType[] arrtransactionType = new TransactionType[]{DY, DZ, Ea, Eb, Ec, Ed, Ee, Ef, Eg};
            Eh = arrtransactionType;
        }

        public static TransactionType valueOf(String string) {
            return (TransactionType)Enum.valueOf(TransactionType.class, (String)string);
        }

        public static TransactionType[] values() {
            return (TransactionType[])Eh.clone();
        }
    }

}

