/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.LinkedHashSet
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.command.RuleSet;
import com.americanexpress.sdkmodulelib.model.TokenDataRecord;
import com.americanexpress.sdkmodulelib.model.apdu.APDURequestCommand;
import com.americanexpress.sdkmodulelib.model.token.ParsedTokenRecord;
import com.americanexpress.sdkmodulelib.payment.SessionManager;
import com.americanexpress.sdkmodulelib.util.Constants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class Session {
    int authenticationPerformed;
    int authenticationType;
    private ArrayList<String> commandList = new ArrayList();
    private RuleSet emvRuleSet = new RuleSet(Constants.NFC_SUPPORTED_EMV_COMMANDS);
    private boolean isEMVTerminal = false;
    private boolean isInAppTransaction = false;
    private boolean isInAppTransactionSuccess = false;
    private boolean isMSTInvokedAtleastOnce = false;
    private boolean isProcessOther = false;
    private boolean isProcessTransactionComplete = false;
    private boolean isTerminalOffline = false;
    private RuleSet magRuleSet = new RuleSet(Constants.NFC_SUPPORTED_MAG_COMMANDS);
    private ParsedTokenRecord parsedTokenRecord;
    String terminalTypeData;
    private String tokenDataBlob;
    private TokenDataRecord tokenDataRecord;

    /*
     * Enabled aggressive block sorting
     */
    public boolean checkWorkflowCompleted() {
        if (this.isProcessOther() || this.isInAppTransaction()) {
            return true;
        }
        RuleSet ruleSet = SessionManager.getSession().isEMVTransaction() ? this.emvRuleSet : this.magRuleSet;
        ArrayList arrayList = (ArrayList)SessionManager.getSession().getCommandList().clone();
        LinkedHashSet linkedHashSet = new LinkedHashSet((Collection)arrayList);
        arrayList.clear();
        arrayList.addAll((Collection)linkedHashSet);
        return ruleSet.isFullRuleMatch((ArrayList<String>)arrayList);
    }

    public int getAuthenticationPerformed() {
        return this.authenticationPerformed;
    }

    public int getAuthenticationType() {
        return this.authenticationType;
    }

    public ArrayList<String> getCommandList() {
        return this.commandList;
    }

    public ParsedTokenRecord getParsedTokenRecord() {
        return this.parsedTokenRecord;
    }

    public String getTerminalTypeData() {
        return this.terminalTypeData;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public TokenDataRecord getTokenDataRecord() {
        return this.tokenDataRecord;
    }

    /*
     * Enabled aggressive block sorting
     */
    public Boolean isBioAuthentication() {
        block5 : {
            block4 : {
                if (this.getAuthenticationPerformed() == 2) break block4;
                int n2 = this.getAuthenticationType();
                if (n2 == 1 || n2 == 2 || n2 == 3 || n2 == 4) {
                    return false;
                }
                if (n2 == 5) break block5;
            }
            return null;
        }
        return true;
    }

    public boolean isEMVTerminal() {
        return this.isEMVTerminal;
    }

    public boolean isEMVTransaction() {
        return this.parsedTokenRecord.isTokenDataContainsEMV() && this.isEMVTerminal;
    }

    public boolean isInAppTransaction() {
        return this.isInAppTransaction;
    }

    public boolean isInAppTransactionSuccess() {
        return this.isInAppTransactionSuccess;
    }

    public boolean isMSTInvokedAtleastOnce() {
        return this.isMSTInvokedAtleastOnce;
    }

    public boolean isProcessOther() {
        return this.isProcessOther;
    }

    public boolean isProcessTransactionComplete() {
        return this.isProcessTransactionComplete;
    }

    public boolean isTerminalOffline() {
        return this.isTerminalOffline;
    }

    public boolean isWorkflowStepValid(APDURequestCommand aPDURequestCommand) {
        if (SessionManager.getSession().getParsedTokenRecord().isTokenDataContainsEMV() && this.isEMVTerminal) {
            if (this.emvRuleSet.isRuleMatch(SessionManager.getSession().getCommandList(), aPDURequestCommand.name())) {
                SessionManager.getSession().getCommandList().add((Object)aPDURequestCommand.name());
                return true;
            }
            return false;
        }
        if (this.magRuleSet.isRuleMatch(SessionManager.getSession().getCommandList(), aPDURequestCommand.name())) {
            SessionManager.getSession().getCommandList().add((Object)aPDURequestCommand.name());
            return true;
        }
        return false;
    }

    public void setAuthenticationPerformed(int n2) {
        this.authenticationPerformed = n2;
    }

    public void setAuthenticationType(int n2) {
        this.authenticationType = n2;
    }

    public void setEMVTerminal(boolean bl) {
        this.isEMVTerminal = bl;
    }

    public void setIsInAppTransaction(boolean bl) {
        this.isInAppTransaction = bl;
    }

    public void setIsInAppTransactionSuccess(boolean bl) {
        this.isInAppTransactionSuccess = bl;
    }

    public void setIsMSTInvokedAtleastOnce(boolean bl) {
        this.isMSTInvokedAtleastOnce = bl;
    }

    public void setIsProcessOther(boolean bl) {
        this.isProcessOther = bl;
    }

    public void setIsTerminalOffline(boolean bl) {
        this.isTerminalOffline = bl;
    }

    public void setParsedTokenRecord(ParsedTokenRecord parsedTokenRecord) {
        this.parsedTokenRecord = parsedTokenRecord;
    }

    public void setProcessTransactionComplete(boolean bl) {
        this.isProcessTransactionComplete = bl;
    }

    public void setTerminalTypeData(String string) {
        this.terminalTypeData = string;
    }

    public void setTokenDataBlob(String string) {
        this.tokenDataBlob = string;
    }

    public void setTokenDataRecord(TokenDataRecord tokenDataRecord) {
        this.tokenDataRecord = tokenDataRecord;
    }
}

