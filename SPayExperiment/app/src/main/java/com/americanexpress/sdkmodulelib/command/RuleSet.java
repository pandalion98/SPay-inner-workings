/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.americanexpress.sdkmodulelib.command;

import com.americanexpress.sdkmodulelib.command.Rule;
import com.americanexpress.sdkmodulelib.command.RuleImpl;
import java.util.ArrayList;

public class RuleSet {
    private String[][] ruleSets;
    private Rule[] rules;

    public RuleSet(String[][] arrstring) {
        this.initializeRules(arrstring);
    }

    private void initializeRules(String[][] arrstring) {
        this.ruleSets = arrstring;
        this.rules = new Rule[arrstring.length];
        for (int i2 = 0; i2 < arrstring.length; ++i2) {
            this.rules[i2] = new RuleImpl(arrstring[i2]);
        }
    }

    public boolean isFullRuleMatch(ArrayList<String> arrayList) {
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = this.rules.length;
                    bl = false;
                    if (n2 >= n3) break block3;
                    boolean bl2 = this.rules[n2].isRuleMatch(arrayList, null);
                    int n4 = ((RuleImpl)this.rules[n2]).getCommandsCount();
                    if (!bl2 || n4 != arrayList.size()) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }

    public boolean isRuleMatch(ArrayList<String> arrayList, String string) {
        int n2 = 0;
        do {
            block4 : {
                boolean bl;
                block3 : {
                    int n3 = this.rules.length;
                    bl = false;
                    if (n2 >= n3) break block3;
                    if (!this.rules[n2].isRuleMatch(arrayList, string)) break block4;
                    bl = true;
                }
                return bl;
            }
            ++n2;
        } while (true);
    }
}

