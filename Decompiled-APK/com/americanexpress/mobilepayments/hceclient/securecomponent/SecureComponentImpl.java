package com.americanexpress.mobilepayments.hceclient.securecomponent;

import com.americanexpress.mobilepayments.hceclient.session.SessionConstants;
import com.americanexpress.mobilepayments.hceclient.session.SessionManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import java.util.ArrayList;
import java.util.List;

public class SecureComponentImpl implements SecureComponent {
    private List<Object> inputList;
    private SecureComponent secureComponent;

    public List<Object> getInputList() {
        return this.inputList;
    }

    public boolean isRetryExecuted() {
        Object value = SessionManager.getSession().getValue(SessionConstants.RETRY_REQUIRED, true);
        if (value != null) {
            return ((Boolean) value).booleanValue();
        }
        return false;
    }

    public SecureComponentImpl() {
        this.inputList = new ArrayList();
        this.secureComponent = null;
        this.secureComponent = new SecureComponentCoreImpl();
    }

    public int open(byte[] bArr) {
        return this.secureComponent.open(bArr);
    }

    public int close(byte[] bArr) {
        buildArrayList(bArr);
        int close = this.secureComponent.close(bArr);
        if (close >= HCEClientConstants.SC_DEST_BUFFER_CONDITION_CONSTANT) {
            return close;
        }
        close = this.secureComponent.close(new byte[determineBufferSize(close)]);
        buildArrayList(r1);
        SessionManager.getSession().setValue(SessionConstants.RETRY_REQUIRED, Boolean.valueOf("true"));
        return close;
    }

    public int init(byte[] bArr) {
        return this.secureComponent.init(bArr);
    }

    public int perso(int i, byte[] bArr, byte[] bArr2) {
        return this.secureComponent.perso(i, bArr, bArr2);
    }

    public int update(byte[] bArr) {
        return this.secureComponent.update(bArr);
    }

    public int initializeSecureChannel(byte[] bArr, byte[] bArr2) {
        buildArrayList(bArr, bArr2);
        int initializeSecureChannel = this.secureComponent.initializeSecureChannel(bArr, bArr2);
        if (initializeSecureChannel >= HCEClientConstants.SC_DEST_BUFFER_CONDITION_CONSTANT) {
            return initializeSecureChannel;
        }
        initializeSecureChannel = this.secureComponent.initializeSecureChannel((byte[]) this.inputList.get(0), new byte[determineBufferSize(initializeSecureChannel)]);
        buildArrayList(r1);
        SessionManager.getSession().setValue(SessionConstants.RETRY_REQUIRED, Boolean.valueOf("true"));
        return initializeSecureChannel;
    }

    public int updateSecureChannel(byte[] bArr, byte[] bArr2) {
        buildArrayList(bArr, bArr2);
        int updateSecureChannel = this.secureComponent.updateSecureChannel(bArr, bArr2);
        if (updateSecureChannel >= HCEClientConstants.SC_DEST_BUFFER_CONDITION_CONSTANT) {
            return updateSecureChannel;
        }
        updateSecureChannel = this.secureComponent.updateSecureChannel((byte[]) this.inputList.get(0), new byte[determineBufferSize(updateSecureChannel)]);
        buildArrayList(r1);
        SessionManager.getSession().setValue(SessionConstants.RETRY_REQUIRED, Boolean.valueOf("true"));
        return updateSecureChannel;
    }

    public int lcm(int i) {
        return this.secureComponent.lcm(i);
    }

    public int getMeta() {
        return this.secureComponent.getMeta();
    }

    public int computeAC(byte[] bArr, byte[] bArr2) {
        return this.secureComponent.computeAC(bArr, bArr2);
    }

    public int sign(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return this.secureComponent.sign(bArr, bArr2, bArr3);
    }

    public int reqInApp(byte[] bArr, byte[] bArr2) {
        return this.secureComponent.reqInApp(bArr, bArr2);
    }

    public int verify(byte[] bArr, byte[] bArr2) {
        return this.secureComponent.verify(bArr, bArr2);
    }

    public int wrap(byte[] bArr, int i, byte[] bArr2, int i2) {
        buildArrayList(bArr, Integer.valueOf(i), bArr2, Integer.valueOf(i2));
        int wrap = this.secureComponent.wrap(bArr, i, bArr2, i2);
        if (wrap >= HCEClientConstants.SC_DEST_BUFFER_CONDITION_CONSTANT) {
            return wrap;
        }
        byte[] bArr3 = new byte[determineBufferSize(wrap)];
        wrap = this.secureComponent.wrap((byte[]) this.inputList.get(0), ((Integer) this.inputList.get(1)).intValue(), bArr3, bArr3.length);
        buildArrayList(bArr3);
        SessionManager.getSession().setValue(SessionConstants.RETRY_REQUIRED, Boolean.valueOf("true"));
        return wrap;
    }

    public int unwrap(int i, byte[] bArr, int i2, byte[] bArr2, int i3) {
        buildArrayList(Integer.valueOf(i), bArr, Integer.valueOf(i2), bArr2, Integer.valueOf(i3));
        int unwrap = this.secureComponent.unwrap(i, bArr, i2, bArr2, i3);
        if (unwrap >= HCEClientConstants.SC_DEST_BUFFER_CONDITION_CONSTANT) {
            return unwrap;
        }
        byte[] bArr3 = new byte[determineBufferSize(unwrap)];
        unwrap = this.secureComponent.unwrap(((Integer) this.inputList.get(0)).intValue(), (byte[]) this.inputList.get(1), ((Integer) this.inputList.get(2)).intValue(), bArr3, bArr3.length);
        buildArrayList(bArr3);
        SessionManager.getSession().setValue(SessionConstants.RETRY_REQUIRED, Boolean.valueOf("true"));
        return unwrap;
    }

    public int getSignatureData(byte[] bArr, byte[] bArr2) {
        buildArrayList(bArr, bArr2);
        int signatureData = this.secureComponent.getSignatureData(bArr, bArr2);
        if (signatureData >= HCEClientConstants.SC_DEST_BUFFER_CONDITION_CONSTANT) {
            return signatureData;
        }
        signatureData = this.secureComponent.getSignatureData((byte[]) this.inputList.get(0), new byte[determineBufferSize(signatureData)]);
        buildArrayList(r1);
        SessionManager.getSession().setValue(SessionConstants.RETRY_REQUIRED, Boolean.valueOf("true"));
        return signatureData;
    }

    private void buildArrayList(Object... objArr) {
        this.inputList.clear();
        for (Object obj : objArr) {
            if (obj != null) {
                if (obj instanceof byte[]) {
                    Object obj2 = new byte[((byte[]) obj).length];
                    System.arraycopy((byte[]) obj, 0, obj2, 0, ((byte[]) obj).length);
                    this.inputList.add(obj2);
                } else {
                    this.inputList.add(obj);
                }
            }
        }
    }

    private int determineBufferSize(int i) {
        return Math.abs(Math.abs(i) + HCEClientConstants.SC_DEST_BUFFER_COMPUTATION_CONSTANT);
    }

    public byte[] getDestBuffer() {
        return (byte[]) getInputList().get(0);
    }
}
