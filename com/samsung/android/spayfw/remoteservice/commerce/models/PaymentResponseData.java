package com.samsung.android.spayfw.remoteservice.commerce.models;

import com.google.gson.GsonBuilder;

public class PaymentResponseData {
    private Card card;
    private String href;
    private String id;

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
            return new GsonBuilder().disableHtmlEscaping().create().toJson((Object) this);
        }
    }

    public String getId() {
        return this.id;
    }

    public String getHref() {
        return this.href;
    }

    public Card getCard() {
        return this.card;
    }
}
