/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.visa.transaction;

import com.samsung.android.spayfw.payprovider.visa.transaction.Element;
import java.util.ArrayList;
import java.util.List;

public class VisaPayTransactionData {
    private Integer count;
    private List<Element> elements = new ArrayList();
    private String href;

    public Integer getCount() {
        return this.count;
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public String getHref() {
        return this.href;
    }

    public void setCount(Integer n2) {
        this.count = n2;
    }

    public void setElements(List<Element> list) {
        this.elements = list;
    }

    public void setHref(String string) {
        this.href = string;
    }

    public String toString() {
        return "VisaPayTransactionData [count=" + (Object)this.count + ", elements=" + this.elements + ", href=" + this.href + "]";
    }
}

