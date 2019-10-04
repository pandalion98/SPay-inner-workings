/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.commerce.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PaymentResponseData {
    private Card card;
    private String href;
    private String id;

    public Card getCard() {
        return this.card;
    }

    public String getHref() {
        return this.href;
    }

    public String getId() {
        return this.id;
    }

    public static class Card {
        public static final String STATUS_AUTHORIZED = "AUTHORIZED";
        public static final String STATUS_CHARGED = "CHARGED";
        public static final String STATUS_PENDING = "PENDING";
        public static final String STATUS_REFUNDED = "REFUNDED";
        private String reference;
        private String status;

        public String getReference() {
            return this.reference;
        }

        public String getStatus() {
            return this.status;
        }

        public String toJson() {
            return new GsonBuilder().disableHtmlEscaping().create().toJson((Object)this);
        }
    }

}

