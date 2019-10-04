/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.plcc.db;

import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import java.util.List;

public interface PlccCardDetailsDao {
    public boolean addCard(PlccCard var1);

    public String getMstConfig(String var1);

    public List<PlccCard> listCard();

    public boolean removeCard(PlccCard var1);

    public PlccCard selectCard(String var1);

    public boolean updateCard(PlccCard var1);

    public boolean updateSequenceConfig(String var1, String var2);
}

