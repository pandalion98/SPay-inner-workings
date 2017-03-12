package com.samsung.android.spayfw.payprovider.discover.accttxns.models;

import java.util.ArrayList;
import java.util.List;

public class AcctTransactionsResponseData {
    private List<Element> elements;
    private String href;

    public AcctTransactionsResponseData() {
        this.elements = new ArrayList();
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public static AcctTransactionsResponseData getInstance() {
        AcctTransactionsResponseData acctTransactionsResponseData = new AcctTransactionsResponseData();
        acctTransactionsResponseData.elements = new ArrayList(1);
        acctTransactionsResponseData.elements.add(0, Element.getInstance());
        return acctTransactionsResponseData;
    }
}
