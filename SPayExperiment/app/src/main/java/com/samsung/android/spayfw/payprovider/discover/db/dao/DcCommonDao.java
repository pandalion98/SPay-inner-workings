/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.db.dao;

public interface DcCommonDao<T> {

    public static final class DetailDataId
    extends Enum<DetailDataId> {
        public static final /* enum */ DetailDataId sI = new DetailDataId(0);
        public static final /* enum */ DetailDataId sJ = new DetailDataId(1);
        public static final /* enum */ DetailDataId sK = new DetailDataId(2);
        public static final /* enum */ DetailDataId sL = new DetailDataId(3);
        public static final /* enum */ DetailDataId sM = new DetailDataId(4);
        public static final /* enum */ DetailDataId sN = new DetailDataId(5);
        public static final /* enum */ DetailDataId sO = new DetailDataId(6);
        public static final /* enum */ DetailDataId sP = new DetailDataId(99);
        private static final /* synthetic */ DetailDataId[] sQ;
        private int mValue;

        static {
            DetailDataId[] arrdetailDataId = new DetailDataId[]{sI, sJ, sK, sL, sM, sN, sO, sP};
            sQ = arrdetailDataId;
        }

        private DetailDataId(int n3) {
            this.mValue = n3;
        }

        public static DetailDataId valueOf(String string) {
            return (DetailDataId)Enum.valueOf(DetailDataId.class, (String)string);
        }

        public static DetailDataId[] values() {
            return (DetailDataId[])sQ.clone();
        }

        public int cJ() {
            return this.mValue;
        }
    }

}

