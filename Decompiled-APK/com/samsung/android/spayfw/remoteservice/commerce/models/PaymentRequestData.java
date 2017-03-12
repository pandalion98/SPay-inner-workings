package com.samsung.android.spayfw.remoteservice.commerce.models;

public class PaymentRequestData {
    private Card card;
    private Payment payment;

    public static class Card {
        private String data;
        private String reference;

        public Card(String str, String str2) {
            this.reference = str;
            this.data = str2;
        }

        public String getReference() {
            return this.reference;
        }

        public void setReference(String str) {
            this.reference = str;
        }

        public String getData() {
            return this.data;
        }

        public void setData(String str) {
            this.data = str;
        }
    }

    public static class Payment {
        private String provider;

        public Payment(String str) {
            this.provider = str;
        }

        public String getProvider() {
            return this.provider;
        }

        public void setProvider(String str) {
            this.provider = str;
        }
    }

    public PaymentRequestData(Card card, Payment payment) {
        this.card = card;
        this.payment = payment;
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
