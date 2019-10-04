/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Message
 *  java.lang.Object
 */
package com.samsung.android.spayfw.core;

import android.os.Message;

public class j {
    public Object jP;
    public Object jQ;
    public Object jR;
    public Object jS;
    public Object obj;

    private j(Object object, Object object2) {
        this.obj = object;
        this.jS = object2;
    }

    private j(Object object, Object object2, Object object3) {
        this.obj = object;
        this.jP = object2;
        this.jS = object3;
    }

    private j(Object object, Object object2, Object object3, Object object4) {
        this.obj = object;
        this.jP = object2;
        this.jQ = object3;
        this.jS = object4;
    }

    private j(Object object, Object object2, Object object3, Object object4, Object object5) {
        this.obj = object;
        this.jP = object2;
        this.jQ = object3;
        this.jR = object4;
        this.jS = object5;
    }

    public static Message a(int n2, Object object, Object object2) {
        Message message = new Message();
        message.what = n2;
        message.obj = new j(object, object2);
        return message;
    }

    public static Message a(int n2, Object object, Object object2, Object object3) {
        Message message = new Message();
        message.what = n2;
        message.obj = new j(object, object2, object3);
        return message;
    }

    public static Message a(int n2, Object object, Object object2, Object object3, Object object4) {
        Message message = new Message();
        message.what = n2;
        message.obj = new j(object, object2, object3, object4);
        return message;
    }

    public static Message a(int n2, Object object, Object object2, Object object3, Object object4, Object object5) {
        Message message = new Message();
        message.what = n2;
        message.obj = new j(object, object2, object3, object4, object5);
        return message;
    }
}

