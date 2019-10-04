/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.List
 */
package com.americanexpress.mobilepayments.hceclient.securecomponent;

import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponent;
import com.americanexpress.mobilepayments.hceclient.securecomponent.SecureComponentCoreImpl;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class SecureComponentImpl
implements SecureComponent {
    private List<Object> inputList = new ArrayList();
    private SecureComponent secureComponent = new SecureComponentCoreImpl();

    /*
     * Enabled aggressive block sorting
     */
    private /* varargs */ void buildArrayList(Object ... arrobject) {
        this.inputList.clear();
        int n2 = arrobject.length;
        int n3 = 0;
        while (n3 < n2) {
            Object object = arrobject[n3];
            if (object != null) {
                if (object instanceof byte[]) {
                    byte[] arrby = new byte[((byte[])object).length];
                    System.arraycopy((Object)((byte[])object), (int)0, (Object)arrby, (int)0, (int)((byte[])object).length);
                    this.inputList.add((Object)arrby);
                } else {
                    this.inputList.add(object);
                }
            }
            ++n3;
        }
        return;
    }

    private int determineBufferSize(int n2) {
        return Math.abs((int)(-32768 + Math.abs((int)n2)));
    }

    @Override
    public int close(byte[] arrby) {
        this.buildArrayList(new Object[]{arrby});
        int n2 = this.secureComponent.close(arrby);
        if (n2 < -12288) {
            byte[] arrby2 = new byte[this.determineBufferSize(n2)];
            n2 = this.secureComponent.close(arrby2);
            this.buildArrayList(new Object[]{arrby2});
            SessionManager.getSession().setValue("RETRY_REQUIRED", (Object)Boolean.valueOf((String)"true"));
        }
        return n2;
    }

    @Override
    public int computeAC(byte[] arrby, byte[] arrby2) {
        return this.secureComponent.computeAC(arrby, arrby2);
    }

    public byte[] getDestBuffer() {
        return (byte[])this.getInputList().get(0);
    }

    public List<Object> getInputList() {
        return this.inputList;
    }

    @Override
    public int getMeta() {
        return this.secureComponent.getMeta();
    }

    @Override
    public int getSignatureData(byte[] arrby, byte[] arrby2) {
        this.buildArrayList(arrby, arrby2);
        int n2 = this.secureComponent.getSignatureData(arrby, arrby2);
        if (n2 < -12288) {
            byte[] arrby3 = new byte[this.determineBufferSize(n2)];
            n2 = this.secureComponent.getSignatureData((byte[])this.inputList.get(0), arrby3);
            this.buildArrayList(new Object[]{arrby3});
            SessionManager.getSession().setValue("RETRY_REQUIRED", (Object)Boolean.valueOf((String)"true"));
        }
        return n2;
    }

    @Override
    public int init(byte[] arrby) {
        return this.secureComponent.init(arrby);
    }

    @Override
    public int initializeSecureChannel(byte[] arrby, byte[] arrby2) {
        this.buildArrayList(arrby, arrby2);
        int n2 = this.secureComponent.initializeSecureChannel(arrby, arrby2);
        if (n2 < -12288) {
            byte[] arrby3 = new byte[this.determineBufferSize(n2)];
            n2 = this.secureComponent.initializeSecureChannel((byte[])this.inputList.get(0), arrby3);
            this.buildArrayList(new Object[]{arrby3});
            SessionManager.getSession().setValue("RETRY_REQUIRED", (Object)Boolean.valueOf((String)"true"));
        }
        return n2;
    }

    public boolean isRetryExecuted() {
        Object object = SessionManager.getSession().getValue("RETRY_REQUIRED", true);
        if (object != null) {
            return (Boolean)object;
        }
        return false;
    }

    @Override
    public int lcm(int n2) {
        return this.secureComponent.lcm(n2);
    }

    @Override
    public int open(byte[] arrby) {
        return this.secureComponent.open(arrby);
    }

    @Override
    public int perso(int n2, byte[] arrby, byte[] arrby2) {
        return this.secureComponent.perso(n2, arrby, arrby2);
    }

    @Override
    public int reqInApp(byte[] arrby, byte[] arrby2) {
        return this.secureComponent.reqInApp(arrby, arrby2);
    }

    @Override
    public int sign(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        return this.secureComponent.sign(arrby, arrby2, arrby3);
    }

    @Override
    public int unwrap(int n2, byte[] arrby, int n3, byte[] arrby2, int n4) {
        Object[] arrobject = new Object[]{n2, arrby, n3, arrby2, n4};
        this.buildArrayList(arrobject);
        int n5 = this.secureComponent.unwrap(n2, arrby, n3, arrby2, n4);
        if (n5 < -12288) {
            byte[] arrby3 = new byte[this.determineBufferSize(n5)];
            n5 = this.secureComponent.unwrap((Integer)this.inputList.get(0), (byte[])this.inputList.get(1), (Integer)this.inputList.get(2), arrby3, arrby3.length);
            this.buildArrayList(new Object[]{arrby3});
            SessionManager.getSession().setValue("RETRY_REQUIRED", (Object)Boolean.valueOf((String)"true"));
        }
        return n5;
    }

    @Override
    public int update(byte[] arrby) {
        return this.secureComponent.update(arrby);
    }

    @Override
    public int updateSecureChannel(byte[] arrby, byte[] arrby2) {
        this.buildArrayList(arrby, arrby2);
        int n2 = this.secureComponent.updateSecureChannel(arrby, arrby2);
        if (n2 < -12288) {
            byte[] arrby3 = new byte[this.determineBufferSize(n2)];
            n2 = this.secureComponent.updateSecureChannel((byte[])this.inputList.get(0), arrby3);
            this.buildArrayList(new Object[]{arrby3});
            SessionManager.getSession().setValue("RETRY_REQUIRED", (Object)Boolean.valueOf((String)"true"));
        }
        return n2;
    }

    @Override
    public int verify(byte[] arrby, byte[] arrby2) {
        return this.secureComponent.verify(arrby, arrby2);
    }

    @Override
    public int wrap(byte[] arrby, int n2, byte[] arrby2, int n3) {
        Object[] arrobject = new Object[]{arrby, n2, arrby2, n3};
        this.buildArrayList(arrobject);
        int n4 = this.secureComponent.wrap(arrby, n2, arrby2, n3);
        if (n4 < -12288) {
            byte[] arrby3 = new byte[this.determineBufferSize(n4)];
            n4 = this.secureComponent.wrap((byte[])this.inputList.get(0), (Integer)this.inputList.get(1), arrby3, arrby3.length);
            this.buildArrayList(new Object[]{arrby3});
            SessionManager.getSession().setValue("RETRY_REQUIRED", (Object)Boolean.valueOf((String)"true"));
        }
        return n4;
    }
}

