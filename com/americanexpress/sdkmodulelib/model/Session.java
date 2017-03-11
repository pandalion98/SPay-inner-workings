package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.command.RuleSet;
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
    private ArrayList<String> commandList;
    private RuleSet emvRuleSet;
    private boolean isEMVTerminal;
    private boolean isInAppTransaction;
    private boolean isInAppTransactionSuccess;
    private boolean isMSTInvokedAtleastOnce;
    private boolean isProcessOther;
    private boolean isProcessTransactionComplete;
    private boolean isTerminalOffline;
    private RuleSet magRuleSet;
    private ParsedTokenRecord parsedTokenRecord;
    String terminalTypeData;
    private String tokenDataBlob;
    private TokenDataRecord tokenDataRecord;

    public Session() {
        this.isEMVTerminal = false;
        this.isProcessTransactionComplete = false;
        this.isProcessOther = false;
        this.isMSTInvokedAtleastOnce = false;
        this.isTerminalOffline = false;
        this.isInAppTransaction = false;
        this.isInAppTransactionSuccess = false;
        this.commandList = new ArrayList();
        this.magRuleSet = new RuleSet(Constants.NFC_SUPPORTED_MAG_COMMANDS);
        this.emvRuleSet = new RuleSet(Constants.NFC_SUPPORTED_EMV_COMMANDS);
    }

    public ParsedTokenRecord getParsedTokenRecord() {
        return this.parsedTokenRecord;
    }

    public void setParsedTokenRecord(ParsedTokenRecord parsedTokenRecord) {
        this.parsedTokenRecord = parsedTokenRecord;
    }

    public boolean isWorkflowStepValid(APDURequestCommand aPDURequestCommand) {
        if (SessionManager.getSession().getParsedTokenRecord().isTokenDataContainsEMV() && this.isEMVTerminal) {
            if (!this.emvRuleSet.isRuleMatch(SessionManager.getSession().getCommandList(), aPDURequestCommand.name())) {
                return false;
            }
            SessionManager.getSession().getCommandList().add(aPDURequestCommand.name());
            return true;
        } else if (!this.magRuleSet.isRuleMatch(SessionManager.getSession().getCommandList(), aPDURequestCommand.name())) {
            return false;
        } else {
            SessionManager.getSession().getCommandList().add(aPDURequestCommand.name());
            return true;
        }
    }

    public boolean checkWorkflowCompleted() {
        if (isProcessOther() || isInAppTransaction()) {
            return true;
        }
        RuleSet ruleSet;
        if (SessionManager.getSession().isEMVTransaction()) {
            ruleSet = this.emvRuleSet;
        } else {
            ruleSet = this.magRuleSet;
        }
        ArrayList arrayList = (ArrayList) SessionManager.getSession().getCommandList().clone();
        Collection linkedHashSet = new LinkedHashSet(arrayList);
        arrayList.clear();
        arrayList.addAll(linkedHashSet);
        if (ruleSet.isFullRuleMatch(arrayList)) {
            return true;
        }
        return false;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public void setTokenDataBlob(String str) {
        this.tokenDataBlob = str;
    }

    public boolean isEMVTerminal() {
        return this.isEMVTerminal;
    }

    public void setEMVTerminal(boolean z) {
        this.isEMVTerminal = z;
    }

    public TokenDataRecord getTokenDataRecord() {
        return this.tokenDataRecord;
    }

    public void setTokenDataRecord(TokenDataRecord tokenDataRecord) {
        this.tokenDataRecord = tokenDataRecord;
    }

    public boolean isProcessTransactionComplete() {
        return this.isProcessTransactionComplete;
    }

    public void setProcessTransactionComplete(boolean z) {
        this.isProcessTransactionComplete = z;
    }

    public boolean isMSTInvokedAtleastOnce() {
        return this.isMSTInvokedAtleastOnce;
    }

    public void setIsMSTInvokedAtleastOnce(boolean z) {
        this.isMSTInvokedAtleastOnce = z;
    }

    public boolean isTerminalOffline() {
        return this.isTerminalOffline;
    }

    public void setIsTerminalOffline(boolean z) {
        this.isTerminalOffline = z;
    }

    public boolean isInAppTransaction() {
        return this.isInAppTransaction;
    }

    public void setIsInAppTransaction(boolean z) {
        this.isInAppTransaction = z;
    }

    public boolean isInAppTransactionSuccess() {
        return this.isInAppTransactionSuccess;
    }

    public void setIsInAppTransactionSuccess(boolean z) {
        this.isInAppTransactionSuccess = z;
    }

    public int getAuthenticationPerformed() {
        return this.authenticationPerformed;
    }

    public void setAuthenticationPerformed(int i) {
        this.authenticationPerformed = i;
    }

    public int getAuthenticationType() {
        return this.authenticationType;
    }

    public void setAuthenticationType(int i) {
        this.authenticationType = i;
    }

    public boolean isEMVTransaction() {
        if (this.parsedTokenRecord.isTokenDataContainsEMV() && this.isEMVTerminal) {
            return true;
        }
        return false;
    }

    public Boolean isBioAuthentication() {
        if (getAuthenticationPerformed() == 2) {
            return null;
        }
        int authenticationType = getAuthenticationType();
        if (authenticationType == 1 || authenticationType == 2 || authenticationType == 3 || authenticationType == 4) {
            return Boolean.valueOf(false);
        }
        if (authenticationType == 5) {
            return Boolean.valueOf(true);
        }
        return null;
    }

    public String getTerminalTypeData() {
        return this.terminalTypeData;
    }

    public void setTerminalTypeData(String str) {
        this.terminalTypeData = str;
    }

    public boolean isProcessOther() {
        return this.isProcessOther;
    }

    public void setIsProcessOther(boolean z) {
        this.isProcessOther = z;
    }

    public ArrayList<String> getCommandList() {
        return this.commandList;
    }
}
