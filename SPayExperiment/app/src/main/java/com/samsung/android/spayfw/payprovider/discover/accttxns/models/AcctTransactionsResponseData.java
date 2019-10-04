/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

import com.samsung.android.spayfw.payprovider.discover.accttxns.models.Element;
import java.util.ArrayList;
import java.util.List;

public class AcctTransactionsResponseData {
    private List<Element> elements = new ArrayList();
    private String href;

    public static AcctTransactionsResponseData getInstance() {
        AcctTransactionsResponseData acctTransactionsResponseData = new AcctTransactionsResponseData();
        acctTransactionsResponseData.elements = new ArrayList(1);
        acctTransactionsResponseData.elements.add(0, (Object)Element.getInstance());
        return acctTransactionsResponseData;
    }

    public List<Element> getElements() {
        return this.elements;
    }
}

