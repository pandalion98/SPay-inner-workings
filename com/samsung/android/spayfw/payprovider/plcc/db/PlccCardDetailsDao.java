package com.samsung.android.spayfw.payprovider.plcc.db;

import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import java.util.List;

public interface PlccCardDetailsDao {
    boolean addCard(PlccCard plccCard);

    String getMstConfig(String str);

    List<PlccCard> listCard();

    boolean removeCard(PlccCard plccCard);

    PlccCard selectCard(String str);

    boolean updateCard(PlccCard plccCard);

    boolean updateSequenceConfig(String str, String str2);
}
