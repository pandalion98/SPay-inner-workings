package com.samsung.android.spayfw.payprovider.visa.transaction;

import java.util.ArrayList;
import java.util.List;

public class VisaPayTransactionData {
    private Integer count;
    private List<Element> elements;
    private String href;

    public VisaPayTransactionData() {
        this.elements = new ArrayList();
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer num) {
        this.count = num;
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public void setElements(List<Element> list) {
        this.elements = list;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String str) {
        this.href = str;
    }

    public String toString() {
        return "VisaPayTransactionData [count=" + this.count + ", elements=" + this.elements + ", href=" + this.href + "]";
    }
}
