package com.americanexpress.sdkmodulelib.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class RuleImpl implements Rule {
    private ArrayList<CommandExpression> commandExpressions;

    class CommandExpression {
        boolean isRepeatable;
        String name;

        public CommandExpression(String str) {
            if (str.endsWith("*")) {
                this.isRepeatable = true;
                this.name = str.substring(0, str.lastIndexOf("*"));
                return;
            }
            this.name = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public boolean isRepeatable() {
            return this.isRepeatable;
        }
    }

    public RuleImpl(String[] strArr) {
        this.commandExpressions = new ArrayList();
        initialize(strArr);
    }

    public boolean isRuleMatch(ArrayList<String> arrayList, String str) {
        ArrayList arrayList2 = (ArrayList) arrayList.clone();
        if (str != null) {
            arrayList2.add(str);
        }
        return isRuleSatisfied(arrayList2);
    }

    private void initialize(String[] strArr) {
        for (String commandExpression : Arrays.asList(strArr)) {
            this.commandExpressions.add(new CommandExpression(commandExpression));
        }
    }

    private boolean isRuleSatisfied(ArrayList<String> arrayList) {
        Iterator it = this.commandExpressions.iterator();
        ListIterator listIterator = arrayList.listIterator();
        while (listIterator.hasNext()) {
            String str = (String) listIterator.next();
            CommandExpression commandExpression = getCommandExpression(it);
            if (commandExpression == null) {
                return false;
            }
            if (!commandExpression.getName().equals(str)) {
                return false;
            }
            if (commandExpression.isRepeatable()) {
                while (isNextCommandRepeated(commandExpression.getName(), getCommand(arrayList, listIterator.nextIndex()))) {
                    listIterator.next();
                }
            }
        }
        return true;
    }

    private boolean isNextCommandRepeated(String str, String str2) {
        if (str.equalsIgnoreCase(str2)) {
            return true;
        }
        return false;
    }

    private CommandExpression getCommandExpression(Iterator<CommandExpression> it) {
        try {
            return (CommandExpression) it.next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private String getCommand(ArrayList<String> arrayList, int i) {
        try {
            return (String) arrayList.get(i);
        } catch (Exception e) {
            return null;
        }
    }

    public int getCommandsCount() {
        return this.commandExpressions.size();
    }
}
