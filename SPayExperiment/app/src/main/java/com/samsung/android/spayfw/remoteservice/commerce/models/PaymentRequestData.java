/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.commerce.models;

public class PaymentRequestData {
    private Card card;
    private Payment payment;

    public PaymentRequestData(Card card, Payment payment) {
        this.card = card;
        this.payment = payment;
    }

    public Card getCard() {
        return this.card;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public static class Card {
        private String data;
        private String reference;

        public Card(String string, String string2) {
            this.reference = string;
            this.data = string2;
        }

        public String getData() {
            return this.data;
        }

        public String getReference() {
            return this.reference;
        }

        public void setData(String string) {
            this.data = string;
        }

        public void setReference(String string) {
            this.reference = string;
        }
    }

    public static class Payment {
        private String provider;

        public Payment(String string) {
            this.provider = string;
        }

        public String getProvider() {
            return this.provider;
        }

        public void setProvider(String string) {
            this.provider = string;
        }
    }

}

