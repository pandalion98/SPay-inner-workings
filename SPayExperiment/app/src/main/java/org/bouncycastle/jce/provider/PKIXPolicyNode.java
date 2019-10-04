/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.security.cert.PolicyNode
 *  java.util.ArrayList
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 */
package org.bouncycastle.jce.provider;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PKIXPolicyNode
implements PolicyNode {
    protected List children;
    protected boolean critical;
    protected int depth;
    protected Set expectedPolicies;
    protected PolicyNode parent;
    protected Set policyQualifiers;
    protected String validPolicy;

    public PKIXPolicyNode(List list, int n, Set set, PolicyNode policyNode, Set set2, String string, boolean bl) {
        this.children = list;
        this.depth = n;
        this.expectedPolicies = set;
        this.parent = policyNode;
        this.policyQualifiers = set2;
        this.validPolicy = string;
        this.critical = bl;
    }

    public void addChild(PKIXPolicyNode pKIXPolicyNode) {
        this.children.add((Object)pKIXPolicyNode);
        pKIXPolicyNode.setParent(this);
    }

    public Object clone() {
        return this.copy();
    }

    public PKIXPolicyNode copy() {
        HashSet hashSet = new HashSet();
        Iterator iterator = this.expectedPolicies.iterator();
        while (iterator.hasNext()) {
            hashSet.add((Object)new String((String)iterator.next()));
        }
        HashSet hashSet2 = new HashSet();
        Iterator iterator2 = this.policyQualifiers.iterator();
        while (iterator2.hasNext()) {
            hashSet2.add((Object)new String((String)iterator2.next()));
        }
        PKIXPolicyNode pKIXPolicyNode = new PKIXPolicyNode((List)new ArrayList(), this.depth, (Set)hashSet, null, (Set)hashSet2, new String(this.validPolicy), this.critical);
        Iterator iterator3 = this.children.iterator();
        while (iterator3.hasNext()) {
            PKIXPolicyNode pKIXPolicyNode2 = ((PKIXPolicyNode)iterator3.next()).copy();
            pKIXPolicyNode2.setParent(pKIXPolicyNode);
            pKIXPolicyNode.addChild(pKIXPolicyNode2);
        }
        return pKIXPolicyNode;
    }

    public Iterator getChildren() {
        return this.children.iterator();
    }

    public int getDepth() {
        return this.depth;
    }

    public Set getExpectedPolicies() {
        return this.expectedPolicies;
    }

    public PolicyNode getParent() {
        return this.parent;
    }

    public Set getPolicyQualifiers() {
        return this.policyQualifiers;
    }

    public String getValidPolicy() {
        return this.validPolicy;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public boolean isCritical() {
        return this.critical;
    }

    public void removeChild(PKIXPolicyNode pKIXPolicyNode) {
        this.children.remove((Object)pKIXPolicyNode);
    }

    public void setCritical(boolean bl) {
        this.critical = bl;
    }

    public void setExpectedPolicies(Set set) {
        this.expectedPolicies = set;
    }

    public void setParent(PKIXPolicyNode pKIXPolicyNode) {
        this.parent = pKIXPolicyNode;
    }

    public String toString() {
        return this.toString("");
    }

    public String toString(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(string);
        stringBuffer.append(this.validPolicy);
        stringBuffer.append(" {\n");
        for (int i = 0; i < this.children.size(); ++i) {
            stringBuffer.append(((PKIXPolicyNode)this.children.get(i)).toString(string + "    "));
        }
        stringBuffer.append(string);
        stringBuffer.append("}\n");
        return stringBuffer.toString();
    }
}

