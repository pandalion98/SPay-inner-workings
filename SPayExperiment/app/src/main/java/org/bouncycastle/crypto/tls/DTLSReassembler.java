/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.System
 *  java.util.Vector
 */
package org.bouncycastle.crypto.tls;

import java.util.Vector;

class DTLSReassembler {
    private final byte[] body;
    private Vector missing = new Vector();
    private final short msg_type;

    DTLSReassembler(short s2, int n2) {
        this.msg_type = s2;
        this.body = new byte[n2];
        this.missing.addElement((Object)new Range(0, n2));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    void contributeFragment(short var1_1, int var2_2, byte[] var3_3, int var4_4, int var5_5, int var6_6) {
        var7_7 = var5_5 + var6_6;
        if (this.msg_type != var1_1) return;
        if (this.body.length != var2_2) return;
        if (var7_7 > var2_2) {
            return;
        }
        var8_8 = 0;
        if (var6_6 != 0) ** GOTO lbl33
        if (var5_5 != 0) return;
        if (this.missing.isEmpty() != false) return;
        if (((Range)this.missing.firstElement()).getEnd() != 0) return;
        this.missing.removeElementAt(0);
        return;
lbl-1000: // 1 sources:
        {
            if (var9_9.getEnd() > var5_5) {
                var10_10 = Math.max((int)var9_9.getStart(), (int)var5_5);
                var11_11 = Math.min((int)var9_9.getEnd(), (int)var7_7);
                var12_12 = var11_11 - var10_10;
                System.arraycopy((Object)var3_3, (int)(var4_4 + var10_10 - var5_5), (Object)this.body, (int)var10_10, (int)var12_12);
                if (var10_10 == var9_9.getStart()) {
                    if (var11_11 == var9_9.getEnd()) {
                        var15_15 = this.missing;
                        var16_16 = var8_8 - 1;
                        var15_15.removeElementAt(var8_8);
                        var8_8 = var16_16;
                    } else {
                        var9_9.setStart(var11_11);
                    }
                } else {
                    if (var11_11 != var9_9.getEnd()) {
                        var13_13 = this.missing;
                        var14_14 = new Range(var11_11, var9_9.getEnd());
                        var13_13.insertElementAt((Object)var14_14, ++var8_8);
                    }
                    var9_9.setEnd(var10_10);
                }
            }
            ++var8_8;
lbl33: // 2 sources:
            if (var8_8 >= this.missing.size()) return;
            ** while ((var9_9 = (Range)this.missing.elementAt((int)var8_8)).getStart() < var7_7)
        }
lbl35: // 1 sources:
    }

    byte[] getBodyIfComplete() {
        if (this.missing.isEmpty()) {
            return this.body;
        }
        return null;
    }

    short getMsgType() {
        return this.msg_type;
    }

    void reset() {
        this.missing.removeAllElements();
        this.missing.addElement((Object)new Range(0, this.body.length));
    }

    private static class Range {
        private int end;
        private int start;

        Range(int n2, int n3) {
            this.start = n2;
            this.end = n3;
        }

        public int getEnd() {
            return this.end;
        }

        public int getStart() {
            return this.start;
        }

        public void setEnd(int n2) {
            this.end = n2;
        }

        public void setStart(int n2) {
            this.start = n2;
        }
    }

}

