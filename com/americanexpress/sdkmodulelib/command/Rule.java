package com.americanexpress.sdkmodulelib.command;

import java.util.ArrayList;

public interface Rule {
    boolean isRuleMatch(ArrayList<String> arrayList, String str);
}
