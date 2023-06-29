package com.youwu.shouyinsaas.ui.handover.bean;

import java.io.Serializable;


public class HandoverBean implements Serializable {


    /**
     * total_amount : {"balance":42.5,"we_chat":0,"ali_pay":0,"cash":0,"total_amount":42.5}
     * total_orders : {"we_chat_order":1,"store_order":0,"total_order":1}
     * pay_amount : {"balance":42.5,"we_chat":0,"ali_pay":0,"cash":0,"total_pay_amount":42.5}
     * reduced_amount : {"total_reduced_amount":0,"total_discount_amount":"0.00"}
     * recharge_amount : {"total_recharge_amount":0}
     */

    private TotalAmountBean total_amount;
    private TotalOrdersBean total_orders;
    private PayAmountBean pay_amount;
    private ReducedAmountBean reduced_amount;
    private RechargeAmountBean recharge_amount;

    public TotalAmountBean getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(TotalAmountBean total_amount) {
        this.total_amount = total_amount;
    }

    public TotalOrdersBean getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(TotalOrdersBean total_orders) {
        this.total_orders = total_orders;
    }

    public PayAmountBean getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(PayAmountBean pay_amount) {
        this.pay_amount = pay_amount;
    }

    public ReducedAmountBean getReduced_amount() {
        return reduced_amount;
    }

    public void setReduced_amount(ReducedAmountBean reduced_amount) {
        this.reduced_amount = reduced_amount;
    }

    public RechargeAmountBean getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(RechargeAmountBean recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public static class TotalAmountBean implements Serializable {
        /**
         * balance : 42.5
         * we_chat : 0
         * ali_pay : 0
         * cash : 0
         * total_amount : 42.5
         */

        private double balance;
        private double we_chat;
        private double ali_pay;
        private double cash;
        private double total_amount;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getWe_chat() {
            return we_chat;
        }

        public void setWe_chat(double we_chat) {
            this.we_chat = we_chat;
        }

        public double getAli_pay() {
            return ali_pay;
        }

        public void setAli_pay(double ali_pay) {
            this.ali_pay = ali_pay;
        }

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }
    }

    
    public static class TotalOrdersBean implements Serializable {
        /**
         * we_chat_order : 1
         * store_order : 0
         * total_order : 1
         */

        private int we_chat_order;
        private int store_order;
        private int total_order;

        public int getWe_chat_order() {
            return we_chat_order;
        }

        public void setWe_chat_order(int we_chat_order) {
            this.we_chat_order = we_chat_order;
        }

        public int getStore_order() {
            return store_order;
        }

        public void setStore_order(int store_order) {
            this.store_order = store_order;
        }

        public int getTotal_order() {
            return total_order;
        }

        public void setTotal_order(int total_order) {
            this.total_order = total_order;
        }
    }

    
    public static class PayAmountBean implements Serializable {
        /**
         * balance : 42.5
         * we_chat : 0
         * ali_pay : 0
         * cash : 0
         * total_pay_amount : 42.5
         */

        private double balance;
        private double we_chat;
        private double ali_pay;
        private double cash;
        private double total_pay_amount;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getWe_chat() {
            return we_chat;
        }

        public void setWe_chat(double we_chat) {
            this.we_chat = we_chat;
        }

        public double getAli_pay() {
            return ali_pay;
        }

        public void setAli_pay(double ali_pay) {
            this.ali_pay = ali_pay;
        }

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public double getTotal_pay_amount() {
            return total_pay_amount;
        }

        public void setTotal_pay_amount(double total_pay_amount) {
            this.total_pay_amount = total_pay_amount;
        }
    }

    
    public static class ReducedAmountBean implements Serializable {
        /**
         * total_reduced_amount : 0
         * total_discount_amount : 0.00
         */

        private double total_reduced_amount;
        private String total_discount_amount;

        public double getTotal_reduced_amount() {
            return total_reduced_amount;
        }

        public void setTotal_reduced_amount(double total_reduced_amount) {
            this.total_reduced_amount = total_reduced_amount;
        }

        public String getTotal_discount_amount() {
            return total_discount_amount;
        }

        public void setTotal_discount_amount(String total_discount_amount) {
            this.total_discount_amount = total_discount_amount;
        }
    }

    
    public static class RechargeAmountBean implements Serializable {
        /**
         * total_recharge_amount : 0
         */

        private double total_recharge_amount;

        public double getTotal_recharge_amount() {
            return total_recharge_amount;
        }

        public void setTotal_recharge_amount(double total_recharge_amount) {
            this.total_recharge_amount = total_recharge_amount;
        }
    }
}

