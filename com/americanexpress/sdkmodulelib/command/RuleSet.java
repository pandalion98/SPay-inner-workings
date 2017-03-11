package com.americanexpress.sdkmodulelib.command;

import java.util.ArrayList;

public class RuleSet {
    private String[][] ruleSets;
    private Rule[] rules;

    public RuleSet(String[][] strArr) {
        initializeRules(strArr);
    }

    public boolean isRuleMatch(ArrayList<String> arrayList, String str) {
        for (Rule isRuleMatch : this.rules) {
            if (isRuleMatch.isRuleMatch(arrayList, str)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFullRuleMatch(ArrayList<String> arrayList) {
        for (int i = 0; i < this.rules.length; i++) {
            boolean isRuleMatch = this.rules[i].isRuleMatch(arrayList, null);
            int commandsCount = ((RuleImpl) this.rules[i]).getCommandsCount();
            if (isRuleMatch && commandsCount == arrayList.size()) {
                return true;
            }
        }
        return false;
    }

    private void initializeRules(String[][] strArr) {
        this.ruleSets = strArr;
        this.rules = new Rule[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            this.rules[i] = new RuleImpl(strArr[i]);
        }
    }
}
