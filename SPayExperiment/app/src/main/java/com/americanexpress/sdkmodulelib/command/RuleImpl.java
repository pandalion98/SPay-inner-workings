/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Iterator
 *  java.util.ListIterator
 *  java.util.NoSuchElementException
 */
package com.americanexpress.sdkmodulelib.command;

import com.americanexpress.sdkmodulelib.command.Rule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class RuleImpl
implements Rule {
    private ArrayList<CommandExpression> commandExpressions = new ArrayList();

    public RuleImpl(String[] arrstring) {
        this.initialize(arrstring);
    }

    private String getCommand(ArrayList<String> arrayList, int n2) {
        try {
            String string = (String)arrayList.get(n2);
            return string;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private CommandExpression getCommandExpression(Iterator<CommandExpression> iterator) {
        try {
            CommandExpression commandExpression = (CommandExpression)iterator.next();
            return commandExpression;
        }
        catch (NoSuchElementException noSuchElementException) {
            return null;
        }
    }

    private void initialize(String[] arrstring) {
        for (String string : Arrays.asList((Object[])arrstring)) {
            this.commandExpressions.add((Object)new CommandExpression(string));
        }
    }

    private boolean isNextCommandRepeated(String string, String string2) {
        return string.equalsIgnoreCase(string2);
    }

    private boolean isRuleSatisfied(ArrayList<String> arrayList) {
        Iterator iterator = this.commandExpressions.iterator();
        ListIterator listIterator = arrayList.listIterator();
        while (listIterator.hasNext()) {
            String string = (String)listIterator.next();
            CommandExpression commandExpression = this.getCommandExpression((Iterator<CommandExpression>)iterator);
            if (commandExpression == null) {
                return false;
            }
            if (commandExpression.getName().equals((Object)string)) {
                if (!commandExpression.isRepeatable()) continue;
                while (this.isNextCommandRepeated(commandExpression.getName(), this.getCommand(arrayList, listIterator.nextIndex()))) {
                    listIterator.next();
                }
                continue;
            }
            return false;
        }
        return true;
    }

    public int getCommandsCount() {
        return this.commandExpressions.size();
    }

    @Override
    public boolean isRuleMatch(ArrayList<String> arrayList, String string) {
        ArrayList arrayList2 = (ArrayList)arrayList.clone();
        if (string != null) {
            arrayList2.add((Object)string);
        }
        return this.isRuleSatisfied((ArrayList<String>)arrayList2);
    }

    class CommandExpression {
        boolean isRepeatable;
        String name;

        public CommandExpression(String string) {
            if (string.endsWith("*")) {
                this.isRepeatable = true;
                this.name = string.substring(0, string.lastIndexOf("*"));
                return;
            }
            this.name = string;
        }

        public String getName() {
            return this.name;
        }

        public boolean isRepeatable() {
            return this.isRepeatable;
        }

        public void setName(String string) {
            this.name = string;
        }
    }

}

