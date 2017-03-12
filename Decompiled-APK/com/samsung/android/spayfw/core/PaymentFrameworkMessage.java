package com.samsung.android.spayfw.core;

import android.os.Message;

/* renamed from: com.samsung.android.spayfw.core.j */
public class PaymentFrameworkMessage {
    public Object jP;
    public Object jQ;
    public Object jR;
    public Object jS;
    public Object obj;

    private PaymentFrameworkMessage(Object obj, Object obj2) {
        this.obj = obj;
        this.jS = obj2;
    }

    private PaymentFrameworkMessage(Object obj, Object obj2, Object obj3) {
        this.obj = obj;
        this.jP = obj2;
        this.jS = obj3;
    }

    private PaymentFrameworkMessage(Object obj, Object obj2, Object obj3, Object obj4) {
        this.obj = obj;
        this.jP = obj2;
        this.jQ = obj3;
        this.jS = obj4;
    }

    private PaymentFrameworkMessage(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        this.obj = obj;
        this.jP = obj2;
        this.jQ = obj3;
        this.jR = obj4;
        this.jS = obj5;
    }

    public static Message m620a(int i, Object obj, Object obj2) {
        Message message = new Message();
        message.what = i;
        message.obj = new PaymentFrameworkMessage(obj, obj2);
        return message;
    }

    public static Message m621a(int i, Object obj, Object obj2, Object obj3) {
        Message message = new Message();
        message.what = i;
        message.obj = new PaymentFrameworkMessage(obj, obj2, obj3);
        return message;
    }

    public static Message m622a(int i, Object obj, Object obj2, Object obj3, Object obj4) {
        Message message = new Message();
        message.what = i;
        message.obj = new PaymentFrameworkMessage(obj, obj2, obj3, obj4);
        return message;
    }

    public static Message m623a(int i, Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        Message message = new Message();
        message.what = i;
        message.obj = new PaymentFrameworkMessage(obj, obj2, obj3, obj4, obj5);
        return message;
    }
}
