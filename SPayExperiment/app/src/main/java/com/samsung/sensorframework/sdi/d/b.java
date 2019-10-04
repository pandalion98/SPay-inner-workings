/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.List
 */
package com.samsung.sensorframework.sdi.d;

import java.util.List;

public class b {
    private double LB;
    private List<String> LC;

    public b(List<String> list, double d2) {
        this.LC = list;
        this.LB = d2;
    }

    public double hY() {
        return this.LB;
    }

    public List<String> hZ() {
        return this.LC;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NearbyPoIs size: ");
        String string = this.LC != null ? this.LC.size() + "" : "null";
        stringBuilder.append(string);
        stringBuilder.append(", distanceToClosestPoI: ");
        stringBuilder.append(this.LB);
        return stringBuilder.toString();
    }
}

